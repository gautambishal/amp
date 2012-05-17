<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>


<bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" />
<digi:form action="/viewNewAdvancedReport.do" module="aim">
	<table width="400px" style="font-size: 11px;" cellpadding="2" cellspacing="2" align="center">
		<tr>
			<td align="right">
				<digi:trn key="aim:popsort:hierarchy:level">Hierarchy Level:</digi:trn>
			</td>
			<td>
				<html:select property="levelPicked" styleClass="dropdwn_sm">
					<logic:iterate name="reportMeta" property="hierarchies"  id="iter">
						<c:set var="key" value="aim:popsort:hierarchy:${iter.column.columnName}"/>
						<html:option value="${iter.levelId}">
							<digi:trn key="${key}">
							${iter.column.columnName}
						</digi:trn>
						</html:option>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="right"><digi:trn key="aim:popsort:hierarchy:sortby">Sort by:</digi:trn></td>
			<td>
				<html:select property="levelSorter" styleClass="dropdwn_sm">
					<html:option value="Title">
						<digi:trn key="aim:popsort:hierarchy:title">Hierarchy Title</digi:trn>
					</html:option>
					<logic:iterate name="report" property="trailCells"  id="iter1">
						<c:if test="${not empty iter1.column.absoluteColumnName}">
						<html:option value="${iter1.column.absoluteColumnName}">
							<c:set var="key1" value="aim:popsort:hierarchy:${iter1.column.absoluteColumnName}"/>
							<digi:trn key="${key1}">
							${iter1.column.absoluteColumnName}
						</digi:trn>
						</html:option>
						</c:if>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td align="right"><digi:trn key="aim:popsort:hierarchy:sortorder">Sort Order:</digi:trn></td>
			<td>
				<html:select property="levelSortOrder" styleClass="dropdwn_sm">
					<html:option value="ascending"><digi:trn key="aim:popsort:ascending">Ascending</digi:trn></html:option>
					<html:option value="descending"><digi:trn key="aim:popsort:descending">Descending</digi:trn></html:option>				
				</html:select>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<logic:notEqual name="widget" scope="request" value="true">
				<td>
					<html:submit property="applySorter" styleClass="buttonx">
						<digi:trn key="aim:popsort:apply">Apply Sorting</digi:trn>
					</html:submit>
				</td>
			</logic:notEqual>
			<logic:equal name="widget" scope="request" value="true">			
				<td>
					<br>
					<input type="button" name="applySorter" class="buttonx"
					value='<digi:trn key="aim:popsort:hierarchy:apply">Apply Sorting</digi:trn>' onclick="changeTabUrl('MyTabs','Tab-<bean:write name="reportMeta" property="ampReportId"/>','/aim/viewNewAdvancedReport.do~applySorter=true~viewFormat=foldable~ampReportId=<bean:write name="reportMeta" property="ampReportId"/>~widget=true~levelPicked='+levelPicked.options[levelPicked.selectedIndex].value+'~levelSorter='+levelSorter.options[levelSorter.selectedIndex].value+'~levelSortOrder='+levelSortOrder.options[levelSortOrder.selectedIndex].value);hideSorter();"/>
				</td>
			</logic:equal>
		</tr>
	</table>
</digi:form>
