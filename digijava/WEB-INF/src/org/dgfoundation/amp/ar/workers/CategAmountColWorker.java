/**
 * CategAmountColWorker.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmountCellColumn;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.FundingTypeSortedString;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.CategAmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.EthiopianCalendar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 13, 2006
 * 
 */
public class CategAmountColWorker extends ColumnWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 */
	public CategAmountColWorker(String condition, String viewName,
			String columnName,ReportGenerator generator) {
		super(condition, viewName, columnName,generator);
		// TODO Auto-generated constructor stub
	}

	/**filter.getFromYear()!=null
	 * Decides if the CategAmountCell is showable or not, based on the measures selected
	 * in the report wizard.
	 * @param cac the given CategAmountCell
	 * @return true if showable
	 */
	public boolean isShowable(CategAmountCell cac) {
		boolean showable=true;
		Set measures=generator.getReportMetadata().getMeasures();
		showable=ARUtil.containsMeasure(cac.getMetaValueString(ArConstants.FUNDING_TYPE),measures);
		if(!showable) return false;
		
		//we now check if the year filtering is used - we do not want items from other years to be shown
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		if(filter.getFromYear()!=null || filter.getToYear()!=null) {
			Integer itemYear=(Integer) MetaInfo.getMetaInfo(cac.getMetaData(),ArConstants.YEAR).getValue();
			if(filter.getFromYear()!=null && filter.getFromYear().intValue()>itemYear.intValue()) showable=false;
			if(filter.getToYear()!=null && filter.getToYear().intValue()<itemYear.intValue()) showable=false;
		}
		return showable;
	}

	
	protected boolean isCummulativeShowable(CategAmountCell cac) {
		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		if(filter.getToYear()==null) return true;
		int cellYear=Integer.parseInt(cac.getMetaValueString(ArConstants.YEAR));
		if(cellYear>filter.getToYear().intValue()) return false;
		return true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet,
	 *      java.lang.String)
	 */
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		Long ownerId = new Long(rs.getLong(1));
		Long id = new Long(rs.getLong(3));
		CategAmountCell acc = new CategAmountCell(ownerId);

		acc.setId(id);

		AmpARFilter filter=(AmpARFilter) generator.getFilter();
		
		
		int tr_type = rs.getInt("transaction_type");
		int adj_type = rs.getInt("adjustment_type");
		double tr_amount = rs.getDouble("transaction_amount");
		java.sql.Date td = rs.getDate("transaction_date");
		double exchangeRate=rs.getDouble("exchange_rate");
		String currencyCode=rs.getString("currency_code");
		String perspectiveCode=rs.getString("perspective_code");
		
		//the most important meta name, the source name (donor name, region name, component name)
		String headMetaName=rsmd.getColumnName(4);


		try {
			String termsAssist = rs.getString("terms_assist_name");
			MetaInfo termsAssistMeta = new MetaInfo(ArConstants.TERMS_OF_ASSISTANCE,
					termsAssist);
			acc.getMetaData().add(termsAssistMeta);
		} catch (SQLException e) {

		}

		MetaInfo headMeta=null;
		
		if("region_name".equals(headMetaName)){
			String regionName = rs.getString("region_name");
			headMeta= new MetaInfo(ArConstants.REGION, regionName);			
		} else
		
		if("component_name".equals(headMetaName)){
			String componentName = rs.getString("component_name");
			headMeta= new MetaInfo(ArConstants.COMPONENT, componentName);			
		} else
	
		if("donor_name".equals(headMetaName)){
			String donorName = rs.getString("donor_name");
			headMeta= new MetaInfo(ArConstants.DONOR, donorName);			
		}

		acc.setAmount(tr_amount);
		acc.setFromExchangeRate(exchangeRate);
		acc.setCurrencyDate(td);
		acc.setCurrencyCode(currencyCode);
		//put toExchangeRate
		acc.setToExchangeRate(1);
		
		
		MetaInfo adjMs = new MetaInfo(ArConstants.ADJUSTMENT_TYPE,
				adj_type == 0 ? ArConstants.PLANNED : ArConstants.ACTUAL);
		String trStr = null;

		switch (tr_type) {
		case 0:
			trStr = ArConstants.COMMITMENT;
			break;
		case 1:
			trStr = ArConstants.DISBURSEMENT;
			break;
		case 2:
			trStr = ArConstants.EXPENDITURE;
			break;
		}

		MetaInfo trMs = new MetaInfo(ArConstants.TRANSACTION_TYPE, trStr);
		MetaInfo fundMs = new MetaInfo(ArConstants.FUNDING_TYPE, new FundingTypeSortedString((String) adjMs
				.getValue()
				+ " " + (String) trMs.getValue()));

		//Date handling..
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(td);

		String quarter=null;
		Integer year=null;
		
		if(filter.getCalendarType()==null || filter.getCalendarType().intValue()==Constants.GREGORIAN.intValue()) {
			quarter= "Q"+ new Integer(calendar.get(Calendar.MONTH) / 4 + 1);
			year=new Integer(calendar.get(Calendar.YEAR));
		} else
		if(filter.getCalendarType().intValue()==Constants.ETH_CAL.intValue() || filter.getCalendarType().intValue()==Constants.ETH_FY.intValue()) {
			EthiopianCalendar ec=new EthiopianCalendar();
			EthiopianCalendar tempDate=new EthiopianCalendar();
			ec=tempDate.getEthiopianDate(calendar);
			if(filter.getCalendarType().intValue()==Constants.ETH_FY.intValue())
			{
				year=new Integer(ec.ethFiscalYear);
				quarter=new String("Q"+ec.ethFiscalQrt);
			}
			if(filter.getCalendarType().intValue()==Constants.ETH_CAL.intValue())
			{
				year=new Integer(ec.ethYear);
				quarter=new String("Q"+ec.ethQtr);
			}
		}

		
		
		
		
		MetaInfo perspMs=new MetaInfo(ArConstants.PERSPECTIVE,perspectiveCode);
		
		//we eliminate the perspective items that do not match the filter one
		if(!filter.getPerspectiveCode().equals(perspMs.getValue())) return null;
		
		MetaInfo qMs = new MetaInfo(ArConstants.QUARTER,quarter);
		MetaInfo aMs = new MetaInfo(ArConstants.YEAR, year);

		
		//add the newly created metainfo objects to the virtual funding object
		acc.getMetaData().add(adjMs);
		acc.getMetaData().add(trMs);
		acc.getMetaData().add(fundMs);
		acc.getMetaData().add(aMs);
		acc.getMetaData().add(qMs);
		acc.getMetaData().add(headMeta);
		acc.getMetaData().add(perspMs);

		//set the showable flag, based on selected measures - THIS NEEDS TO BE MOVED OUT
		//TODO: move this to postProcess!!
		acc.setShow(isShowable(acc));
		acc.setCummulativeShow(isCummulativeShowable(acc));
		
		//UGLY get exchage rate if cross-rates are needed (if we need to convert from X to USD and then to Y)
		if(filter.getAmpCurrencyCode()!=null && !"USD".equals(filter.getAmpCurrencyCode())) 
			acc.setToExchangeRate(ARUtil.getExchange(filter.getAmpCurrencyCode(),td));
		
		return acc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
	 */
	protected Cell getCellFromCell(Cell src) {
		// TODO Auto-generated method stub
		return null;
	}

	public CellColumn newColumnInstance() {
		return new AmountCellColumn(columnName);
	}

	public Cell newCellInstance() {
		return new CategAmountCell();
	}

}
