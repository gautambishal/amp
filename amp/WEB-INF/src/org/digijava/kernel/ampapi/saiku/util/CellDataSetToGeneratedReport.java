/**
 * 
 */
package org.digijava.kernel.ampapi.saiku.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.error.AMPException;
import org.dgfoundation.amp.newreports.AmountCell;
import org.dgfoundation.amp.newreports.ReportArea;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportOutputColumn;
import org.dgfoundation.amp.newreports.ReportSpecification;
import org.dgfoundation.amp.newreports.TextCell;
import org.dgfoundation.amp.reports.mondrian.MondrianReportUtils;
import org.digijava.kernel.ampapi.saiku.SaikuReportArea;
import org.saiku.olap.dto.resultset.CellDataSet;
import org.saiku.service.olap.totals.TotalNode;
import org.saiku.service.olap.totals.aggregators.TotalAggregator;

/**
 * Translator of the Saiku CellDataSet to API GeneratedReport
 * @author Nadejda Mandrescu
 *
 */
public class CellDataSetToGeneratedReport {
	private ReportSpecification spec;
	private CellDataSet cellDataSet;
	List<ReportOutputColumn> leafHeaders;
	DecimalFormat numberFormat;
	TotalAggregator[][] measureTotals = null;
	List<TotalNode>[] rowTotals = null;
	int[] currentSubGroupIndex;
	
	public CellDataSetToGeneratedReport(ReportSpecification spec, CellDataSet cellDataSet, List<ReportOutputColumn> leafHeaders) {
		this.spec = spec;
		this.cellDataSet = cellDataSet;
		this.leafHeaders = leafHeaders;
		init();
	}
	
	private void init() {
		if (spec.getSettings().getCurrencyFormat() != null )
			this.numberFormat = spec.getSettings().getCurrencyFormat();
		else 
			this.numberFormat = (DecimalFormat)DecimalFormat.getCurrencyInstance();
		//init measure totals if they are available
		if (cellDataSet.getColTotalsLists() != null && cellDataSet.getColTotalsLists().length > 0 
				&& cellDataSet.getColTotalsLists()[0] != null && cellDataSet.getColTotalsLists()[0].size() > 0) {
			this.measureTotals = cellDataSet.getColTotalsLists()[0].get(0).getTotalGroups();
			addTotalMeasures();
		}
		this.rowTotals = cellDataSet.getRowTotalsLists();
	}
	
	public ReportAreaImpl transformTo(Class<? extends ReportAreaImpl> reportAreaType) throws AMPException {
		ReportAreaImpl root = MondrianReportUtils.getNewReportArea(reportAreaType);
		
		Deque<List<ReportArea>> stack = new ArrayDeque<List<ReportArea>>();
		//assumption that concatenation was done and totals are required starting for the 1st non-hierarchical column backwards
		int maxDepth = spec.isCalculateRowTotals() ? Math.max(1, spec.getHierarchies().size()) - (spec.getHierarchies().size() / spec.getColumns().size()) : 0; 
		int maxStackSize = 1 + maxDepth * 2; //* 2 for totals, where maxDepth != 0 
		refillStack(stack, maxStackSize); //prepare the stack
		currentSubGroupIndex = new int[maxDepth + 1];
		
		for (int rowId = 0; rowId < cellDataSet.getCellSetBody().length; rowId++) {
			Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>();
			ReportAreaImpl reportArea = MondrianReportUtils.getNewReportArea(reportAreaType);
			
			//stores the current textual column id with not null data
			int notNullColId = addRowData(rowId, contents); 
			int nextNotNullColId = nextNotNull(rowId, maxDepth);
			
			reportArea.setContents(contents);
			//remember the source row id that will be used during sorting
			if (reportArea instanceof SaikuReportArea)
				((SaikuReportArea)reportArea).setOrigId(rowId);
			
			boolean areaEnd = isEndOfArea(rowId, notNullColId, nextNotNullColId);
			
			if (areaEnd) {
				//check if this is the end of the entire report, i.e. nextNotNullColId == -1
				int depth = maxDepth - nextNotNullColId;
				updateGroupData (stack, reportArea, rowId, maxDepth, depth, nextNotNullColId == -1 ? 0 : maxStackSize);
			} else { 
				stack.peek().add(reportArea);
			}
		}
		root.setChildren(stack.pop());
		if (root.getChildren().size() == 1)
			root = (ReportAreaImpl) root.getChildren().get(0);
		else if(root instanceof SaikuReportArea)
			((SaikuReportArea)root).setOrigLeafId(getOrigLeafId((SaikuReportArea)root));
		
		return root;
	}
	
	//returns not null textual column id
	private int addRowData(int rowId, Map<ReportOutputColumn, ReportCell> contents) {
		int notNullColId = 0;
		int rowLength = cellDataSet.getCellSetBody()[rowId].length;
		
		//adds data to the content
		for(int colId = 0; colId < rowLength; colId++) {
			String value = cellDataSet.getCellSetBody()[rowId][colId].getFormattedValue();
			ReportCell cellData = null;
			//textual columns
			if (colId < cellDataSet.getLeftOffset()) {
				cellData = new TextCell(value == null ? "" : value);
				if (value == null)
					notNullColId ++;
			} else { //measure columns
				double dVal = parseValue(value);
				cellData = new AmountCell(dVal, this.numberFormat);
				cellDataSet.getCellSetBody()[rowId][colId].setFormattedValue(this.numberFormat.format(dVal));
			}	
			contents.put(leafHeaders.get(colId), cellData);
		}
		
		//adds measure totals to the content
		if (measureTotals != null) {
			int headerColId = rowLength;
			for (int colId = measureTotals.length - spec.getMeasures().size(); colId < measureTotals.length; colId ++) {
				//Unfortunately cannot use getValue() because during concatenation we override the value, but the only way to override is via formatted value
				double value = parseValue(measureTotals[colId][rowId].getFormattedValue()); 
				contents.put(leafHeaders.get(headerColId++), new AmountCell(value, this.numberFormat));
				//also re-format, via MDX formatting works a bit differently
				measureTotals[colId][rowId].setFormattedValue(this.numberFormat.format(value));
			}
		}
		
		return notNullColId;
	}
	
	private double parseValue(String value) {
		Number iVal = 0;
		try {
			iVal = numberFormat.parse(value);
		} catch (ParseException e) {
			//empty string
		}
		return iVal.doubleValue();
	}
	
	private int nextNotNull(int rowId, int colId) {
		//if this is the end of the report, then not null is out of range, -1
		if (rowId + 1 == cellDataSet.getCellSetBody().length)
			colId = -1;
		else 
			//otherwise detect next row not null column id 
			while (colId > 0 && cellDataSet.getCellSetBody()[rowId + 1][colId-1].getRawValue() != null)
				colId --;
		return colId;
	}
	
	/**
	 * Adds and updates the stack with report area and also stores the totals
	 * @param stack - the stack that stores report area
	 * @param current - the current report area to be added to the stack
	 * @param depth - how depth we should navigate through the stack
	 * @param maxStackSize - the maximum size of the stack
	 * @throws AMPException
	 */
	private void updateGroupData(Deque<List<ReportArea>> stack, ReportAreaImpl current, int rowId, int totColId, int depth, int maxStackSize) throws AMPException {
		while(depth > 0) {
			stack.peek().add(current);
			depth --;
			if (spec.isCalculateRowTotals()) {
				current = MondrianReportUtils.getNewReportArea(current.getClass());
				current.setChildren(stack.pop());
				if(current instanceof SaikuReportArea)
					((SaikuReportArea)current).setOrigLeafId(getOrigLeafId((SaikuReportArea)current));
				addCurentRowTotal(current, totColId);
				totColId --;
				if (depth == 0)
					stack.peek().add(current);
			} else if (depth > 0) {
				current = MondrianReportUtils.getNewReportArea(current.getClass());
				current.setChildren(stack.pop());
			}
		}
		refillStack(stack, maxStackSize);
	}
	
	private int getOrigLeafId(SaikuReportArea current) {
		if (current.getChildren() != null && current.getChildren().size() > 0)
			return getOrigLeafId((SaikuReportArea)current.getChildren().get(0));
		return current.getOrigId();
	}
	
	private void addCurentRowTotal(ReportAreaImpl current, int colId) {
		if (rowTotals == null || rowTotals.length == 0) return; //if needed, we can be built manually in this case, but... it's getting more and more like a custom report generation
		Map<ReportOutputColumn, ReportCell> contents = new LinkedHashMap<ReportOutputColumn, ReportCell>(leafHeaders.size());
		
		if (current instanceof SaikuReportArea) {
			((SaikuReportArea)current).setOrigId(currentSubGroupIndex[colId]);
			((SaikuReportArea)current).setTotalRow(true);
		}
		
		//taking row totals for the subgroup identified by colId and this is currentSubGroupIndex[colId] retrieval from this subgroup
		TotalAggregator[][] totals = rowTotals[colId].get(currentSubGroupIndex[colId]).getTotalGroups();
		currentSubGroupIndex[colId] ++;
		int headerPos = 0;
		//adding textual column data
		String totalsName = getTotalsName(current, colId - 1);
		int totColNameId = Math.max(0,  colId - 1);
		for (int a = 0; a < cellDataSet.getLeftOffset(); a++, headerPos++) {
			String value = a == totColNameId ? totalsName : ""; 
			contents.put(leafHeaders.get(headerPos), new TextCell(value));
		}
		//adding data totals of the current area
		for (int a = 0; a < totals.length; a ++) //normally totals.length == 1
			for (int b = 0; b < totals[a].length; b++) {
				contents.put(leafHeaders.get(headerPos++), new AmountCell(totals[a][b].getValue(), this.numberFormat));
				totals[a][b].setFormattedValue(this.numberFormat.format(totals[a][b].getValue()));
			}
			
		//calculate total measures of the current area
		double[] currentTotalMeasuresColumnTotals = new double[spec.getMeasures().size()];
		for (ReportArea childArea : current.getChildren()) {
			ReportCell[] childContent = childArea.getContents().values().toArray(new ReportCell[0]);
			for (int a = headerPos; a < leafHeaders.size(); a ++) {
				double value = (Double)((AmountCell)childContent[a]).value;
				currentTotalMeasuresColumnTotals[a - headerPos] += value; 
			}
		}
		//adding total measures
		for (int b = 0; b < spec.getMeasures().size(); b++, headerPos++) 
			contents.put(leafHeaders.get(headerPos), new AmountCell(currentTotalMeasuresColumnTotals[b], this.numberFormat));
		
		current.setContents(contents);
	}
	
	//TODO: replace with translatable value and TBD how to name?
	private String getTotalsName(ReportAreaImpl current, int colId) {
		if (colId == -1)
			return "Report Totals";
		if (current.getChildren() != null && current.getChildren().size() > 0)
			return getTotalsName((ReportAreaImpl)current.getChildren().get(0), colId);
		Iterator<ReportCell> iter = current.getContents().values().iterator();
		while(iter.hasNext() && colId > 0) {
			colId --;
			iter.next();
		}
		return iter.hasNext() ? iter.next().displayedValue + " Totals" : "Totals"; 
	}
	
	/**
	 * Verifies if this is an area end (the last row of a group) 
	 * @param rowId - current row index from result data
	 * @param notNullColId - currently detected not empty textual column index 
	 * @return true if this is an end of an area group
	 */
	private boolean isEndOfArea(int rowId, int notNullColId, int nextNotNullColId) {
		//check if this is last row in the entire result set
		if (nextNotNullColId == -1)
			return true;
		//check if next is coming a new hierarchy group of not last column
		if (nextNotNullColId < spec.getHierarchies().size() && nextNotNullColId < spec.getColumns().size() - 1)
			return true;
		//check if this is 1 row group: if this is not the last column  
		if (notNullColId + 1 < cellDataSet.getLeftOffset() && cellDataSet.getCellSetBody()[rowId + 1][notNullColId].getRawValue() != null)
			return true;
		//check if we are at not first level which is the last row of the current group
		if (notNullColId > 0 && cellDataSet.getCellSetBody()[rowId + 1][notNullColId - 1].getRawValue() != null)
			return true;
		return false;
	}
	
	private void addTotalMeasures() {
		ReportOutputColumn totalMeasuresColumn = new ReportOutputColumn("Total Measures", null);
		for (ReportMeasure measure : spec.getMeasures()) {
			leafHeaders.add(new ReportOutputColumn(measure.getMeasureName(), totalMeasuresColumn));
		}
	}
	
	private void refillStack(Deque<List<ReportArea>> stack, int maxSize) {
		for (int i = stack.size(); i < maxSize; i++) {
			stack.push(new ArrayList<ReportArea>()); 
		}
	}
}
