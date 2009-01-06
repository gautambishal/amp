<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		900px;
	background-color: #f4f4f2;
}

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>

<script language="javascript">

function resetFields() {
	var keyword = document.getElementsByName("keyword")[0];
	var queryType = document.getElementsByName("queryType")[0];
	keyword.value ="";
	queryType.value = -1;
}

function popup(mylink, windowname)
{
if (! window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
return false;
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}

$(document).ready(function(){
	document.getElementsByName("searchform")[0].onsubmit = function(){
		var keyword = document.getElementsByName("keyword");
		if(keyword)
		{
			keyword = keyword[0];
			if(keyword.value.length < 3)
			{
				alert("<digi:trn keyWords="AMP Search">Please enter a search of more than 2 characters.</digi:trn>");
				return false;
			}
		
		}
		return true;
	}

	try
	{
		setStripsTable("dataTable1", "tableEven", "tableOdd");
		setHoveredTable("dataTable1", false);
	}
	catch(e) {}
	try
	{
		setStripsTable("dataTable2", "tableEven", "tableOdd");
		setHoveredTable("dataTable2", false);
	}
	catch(e) {}
	try
	{
		setStripsTable("dataTable3", "tableEven", "tableOdd");
		setHoveredTable("dataTable3", false);
	}
	catch(e) {}
	try
	{
		setStripsTable("dataTable4", "tableEven", "tableOdd");
		setHoveredTable("dataTable4", false);
	}
	catch(e) {}
});

</script>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33>
                    	<span class=crumb>
						<c:set var="translation">
							<digi:trn>Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}">
                        My Desktop
						</digi:link>
                        &nbsp;&gt;&nbsp;
						Tools
                        &nbsp;&gt;&nbsp;
                        Search
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Search</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td valign="top">
									<br />
                                	<div class="contentbox_border" style="padding: 20px 0px 20px 0px;">
                                    <digi:form action="/search.do">
                                    <table cellPadding=2 cellSpacing=2 width="100%">
                                        <tr>
                                            <td valign="top">
                                                <digi:trn>Keyword</digi:trn>: <html:text property="keyword"></html:text><br/>
                                            </td>
                                            <td valign="top">
                                                <digi:trn>Type</digi:trn>: 
                                                <html:select property="queryType">
                                                    <html:option value="-1"><digi:trn>ALL</digi:trn></html:option>
                                                    <html:option value="0"><digi:trn>Activities</digi:trn></html:option>
                                                    <html:option value="1"><digi:trn>Reports</digi:trn></html:option>
                                                    <html:option value="2"><digi:trn>Tabs</digi:trn></html:option>
                                                    <html:option value="3"><digi:trn>Resources</digi:trn></html:option>
                                                </html:select>
                                            </td>
<!--                                            <td valign="top">
                                                Results per page: 
                                                <html:select property="resultsPerPage" value="-1">
                                                    <html:option value="-1">ALL</html:option>
                                                    <html:option value="10">10</html:option>
                                                    <html:option value="20">20</html:option>
                                                    <html:option value="50">50</html:option>
                                                </html:select>
                                            </td>-->
                                            <td valign="top">
                                                <html:submit><digi:trn>Submit</digi:trn></html:submit>
                                            </td>
                                            <td valign="top">
	                                            <input type="button" onclick="resetFields()" value="<digi:trn>Reset</digi:trn>"/>
                                            </td>
                                        </tr>
                                    </table>
                                    </digi:form>

<logic:notPresent name="resultList" scope="request">
<c:if test="${param.reset != 'true'}">
    &nbsp;&nbsp;&nbsp;&nbsp;<strong><digi:trn>Your search return no results. Please try another keyword.</digi:trn></strong>
</c:if>
</logic:notPresent>
<logic:present name="resultList" scope="request">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%">	
										<tr>
											<td valign="top" align="center">
												<table border=0 cellPadding=3 cellSpacing=0 width="100%">
													<tr>
														<td>
															<table width="98%" cellPadding=3 cellSpacing=0 vAlign="top">
															<logic:present name="resultActivities" scope="request">
																<tr>
																	<td bgcolor="#dadada">
                                                                    <img id="activityRows_plus"  onclick="toggleResultsGroup('activityRows')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
																	<img id="activityRows_minus" onclick="toggleResultsGroup('activityRows')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
						                                            &nbsp;&nbsp;${fn:length(resultActivities)} <digi:trn>results found in Activities</digi:trn>
																	</td>
                                                                </tr>
																<tr>
																	<td width="100%">
                                                                    <div id="div_activityRows" style="display : none;width:100%;">
                                                                        <table width="100%" cellpadding="2" cellspacing="2" id="dataTable1">
                                                                        <logic:iterate id="item" name="resultActivities" indexId="index">
                                                                        <tr>
                                                                            <td>
                                                                            <digi:link module="search" href="/search.do?ampActivityId=${item.ampActivityId}"><bean:write name="item" property="objectName" /></digi:link><br />
                                                                            </td>
                                                                        </tr>
                                                                        </logic:iterate> 
                                                                        </table>
                                                                    </div>
    	                                                            </td>
                                                                </tr>
                                                                </logic:present>
																<logic:present name="resultTabs" scope="request">
																<tr>
																	<td bgcolor="#dadada">
                                                                    <img id="tabsRows_plus"  onclick="toggleResultsGroup('tabsRows')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
																	<img id="tabsRows_minus" onclick="toggleResultsGroup('tabsRows')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
																	&nbsp;&nbsp;${fn:length(resultTabs)} <digi:trn>results found in Tabs</digi:trn>
																	</td>
																</tr>
																<tr>
																	<td width="100%">
                                                                    <div id="div_tabsRows" style="display : none;width:100%;">
                                                                        <table width="100%" cellpadding="2" cellspacing="2" id="dataTable2">
                                                                            <logic:iterate id="item" name="resultTabs" indexId="index">
	                                                                        <tr>
                                                                               <td>
																					<a title="<digi:trn>Click here to view the tab</digi:trn>" href="/search/search.do?ampReportId=<bean:write name="item" property="ampReportId" />">
    																				<bean:write name="item" property="objectName" /></a><br />
                                                                                </td>
                                                                            </tr>
                                                                            </logic:iterate> 
                                                                        </table>
                                                                    </div>
    	                                                            </td>
                                                                </tr>
                                                                </logic:present>
																<logic:present name="resultReports" scope="request">
																<tr>
																	<td bgcolor="#dadada">
                                                                    <img id="reportsRows_plus"  onclick="toggleResultsGroup('reportsRows')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
																	<img id="reportsRows_minus" onclick="toggleResultsGroup('reportsRows')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
																	&nbsp;&nbsp;${fn:length(resultReports)} <digi:trn>results found in Reports</digi:trn>
																	</td>
																</tr>
																<tr>
																	<td width="100%">
                                                                    <div id="div_reportsRows" style="display : none;width:100%;">
                                                                        <table width="100%" cellpadding="2" cellspacing="2" id="dataTable3">
																		    <logic:iterate id="item" name="resultReports" indexId="index">
																			<tr>
																				<td>
																					<a onclick="return popup(this,'');" title="<digi:trn>Click here to view the Report</digi:trn>" href="/aim/viewNewAdvancedReport.do~view=reset~widget=false~ampReportId=<bean:write name="item" property="ampReportId" />">
                                                                                    <bean:write name="item" property="objectName" /></a><br />
																				</td>
																			</tr>
																		    </logic:iterate> 
                                                                        </table>
                                                                    </div>
    	                                                            </td>
                                                                </tr>
                                                                </logic:present>
																<logic:present name="resultResources" scope="request">
																<tr>
																	<td bgcolor="#dadada">
                                                                    <img id="resourcesRows_plus"  onclick="toggleResultsGroup('resourcesRows')" src="/TEMPLATE/ampTemplate/images/arrow_right.gif" align="absmiddle" style="float:left;"/>
																	<img id="resourcesRows_minus" onclick="toggleResultsGroup('resourcesRows')" src="/TEMPLATE/ampTemplate/images/arrow_down.gif" style="display:none;float:left;" align="absmiddle"/>
																	&nbsp;&nbsp;${fn:length(resultResources)} <digi:trn>results found in Resources</digi:trn>
																	</td>
																</tr>
																<tr>
																	<td width="100%">
                                                                    <div id="div_resourcesRows" style="display : none;width:100%;">
                                                                        <table width="100%" cellpadding="2" cellspacing="2" id="dataTable4">
																		    <logic:iterate id="item" name="resultResources" indexId="index">
																			<tr>
																				<td>
                                                                                <logic:equal name="item" property="webLink" value="">
																					<a onclick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name="item" property="uuid" />'" title="<digi:trn>Click here to download file</digi:trn>" >
                                                                                    <bean:write name="item" property="name" /></a><br />
                                                                                </logic:equal>
                                                                                <logic:notEqual name="item" property="webLink" value="">
																					<a href="<bean:write name="item" property="webLink" />" title="Click here to follow link" target="_blank">
                                                                                    <bean:write name="item" property="webLink" /></a><br />
                                                                                </logic:notEqual>
																				</td>
																			</tr>
																		    </logic:iterate> 
                                                                        </table>
                                                                    </div>
    	                                                            </td>
                                                                </tr>
                                                                </logic:present>
														</td>
													</tr>
											</td>
										</tr>
									</table>
</logic:present>
                                    </div>
								</td>
							</tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>

<script language="javascript">
function toggleResultsGroup(rowsName) {
	$('#'+rowsName+"_plus").toggle('fast');
	$('#'+rowsName+"_minus").toggle('fast');
	$('#div_'+rowsName).toggle('fast');
}

</script>