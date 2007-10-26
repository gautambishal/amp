<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/advanceReportManager.js"/>"></script>
<script language="JavaScript">

function moveUp(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the column Up ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveUpMeasure" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

}

function moveDown(val)
{
	if(document.aimAdvancedReportForm.removeColumns.length == undefined)
		alert("Cannot move the  column Down ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveDownMeasure" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function moveUpAdjType(val)
{

	if(document.aimAdvancedReportForm.removeAdjustType.length == undefined)
		alert("Cannot move the column Up ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveUpAdjustType" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

}
function moveDownAdjType(val)
{
	if(document.aimAdvancedReportForm.removeAdjustType.length == undefined)
		alert("Cannot move the  column Down ");
	else
	{
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=MoveDownAdjustType" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.moveColumn.value = val;
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}

}
function check(){
	if(document.aimAdvancedReportForm.removeColumns == null){
		alert(" Please select a Measure to generate report ");
		return false
	}
	return true;
}
function gotoStep() {
	if (check()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=4" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*added here*/
function backStep() {
	if (check()){
		<digi:context name="step" property="context/module/moduleinstance/advancedReportManager.do?check=SelectRows" />
		document.aimAdvancedReportForm.action = "<%= step %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}
/*ended here*/

function addColumn()
{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=AddMeasure" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}

function deleteColumn()
{
	if(document.aimAdvancedReportForm.removeColumns == null)
		alert(" Please select columns to remove");
	else
	{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=DeleteMeasure" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function addAdjustType()
{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=AddAdjustType" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
}
function deleteAdjustType()
{
	if(document.aimAdvancedReportForm.removeAdjustType == null)
		alert(" Please select columns to remove");
	else
	{
		<digi:context name="advReport" property="context/module/moduleinstance/advancedReportManager.do?check=DeleteAdjustType" />
		document.aimAdvancedReportForm.action = "<%= advReport %>";
		document.aimAdvancedReportForm.target = "_self";
		document.aimAdvancedReportForm.submit();
	}
}

function checkUncheckAll() {
     var items=document.aimAdvancedReportForm.selectedColumns;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.selectedColumns[i].checked = document.aimAdvancedReportForm.checkall.checked;
		}
}

function checkUncheckAll2() {
     var items=document.aimAdvancedReportForm.removeColumns;
		for(i=0; i<items.length; i++){
			document.aimAdvancedReportForm.removeColumns[i].checked = document.aimAdvancedReportForm.checkall2.checked;
		}
}

</script>

<digi:instance property="aimAdvancedReportForm" />
<digi:form action="/advancedReportManager.do" method="post">


<html:hidden property="moveColumn"/>

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
<TR>
	<TD>
		<jsp:include page="teamPagesHeader.jsp" flush="true" />
	</TD>
</TR>
<TR>
	<TD width="100%" vAlign="top" align="left">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="770" vAlign="top" align="left" border=0>
			<tr>
				<td class=r-dotted-lg align=left vAlign=top >	&nbsp;</td>
				<td>
					<table>
						<tr>
							<td>
								<table cellPadding=5 cellSpacing=0 width="100%">
									<tr>
										<td height=33><span class=crumb>
											<c:set var="translation">
											<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
											</c:set>
											<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
											<digi:trn key="aim:portfolio">
												Portfolio
											</digi:trn>
											</digi:link>&nbsp;&gt;&nbsp;

											<c:set var="translation">
											<digi:trn key="aim:clickToGotoStep1">Click here to goto Step 1</digi:trn>
											</c:set>
											<digi:link href="/advancedReportManager.do?check=forward" styleClass="comment" title="${translation}" >
											<digi:trn key="aim:reportBuilder:selectcolumn">
												Report Builder : Select Column
											</digi:trn>
											&gt;&gt;
											</digi:link>&nbsp;&nbsp;

											<digi:link href="/advancedReportManager.do?check=SelectMeasures" styleClass="comment" title="${translation}" >
											<digi:trn key="aim:reportBuilder:selectrows">
												Report Builder : Select Rows
											</digi:trn>
											&gt;&gt;
											</digi:link>&nbsp;&nbsp;

											<digi:trn key="aim:reportBuilder:selectmeasures">
												Report Builder : Select Measure
											</digi:trn>
										</td>
									</tr>
								</table>
							</td>
						</tr>
		 				<tr>

							<td height=16 vAlign=right align=center>
								<span class=subtitle-blue>

								<digi:trn key="aim:reportBuilder:selectmeasure">
												Report Builder : Select Measures
								</digi:trn>
								</span>
							</td>
						</tr>
						<tr colspan="2">
							<td class=box-title align="right" valign="top">
								<img src="module/aim/images/arrow-014E86.gif">
								<digi:trn key="aim:reportBuilder:ReportType">
								 Report Type :
								</digi:trn>


								<bean:write name="aimAdvancedReportForm" property="arReportType"/>
							<td>
						</tr>
						<TR>
							<TD vAlign="top" align="center">
								<TABLE width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4"
								class="box-border-nopadding">
									<TR>
										<TD bgcolor="#f4f4f4">
											<TABLE width="100%" cellSpacing=1 cellPadding=0 vAlign="top" align="left" bgcolor="#f4f4f4">
												<jsp:include page="AdvancedReportManagerMenu.jsp" flush="true"/>
												<TR bgColor=#f4f4f2>
													<TD vAlign="top" align="left" width="100%"></TD>
												</TR>
												<TR bgColor=#f4f4f2>
													<TD vAlign="top" align="center" width="100%" bgColor=#f4f4f2>
														<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2 >
															<TR>
																<TD width="100%" bgcolor="#f4f4f2" align="center"  valign=top>
																	<TABLE width="100%" cellPadding="2" cellSpacing="2" vAlign="top"
																	align="center" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
																		<TR>
																			<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																				<TABLE width="100%" cellPadding="3" cellSpacing="1" vAlign="top"
																				align="left" bgcolor="#eeeeee" >
																					<tr height=10>	<td>	</td>	</tr>
																					<TR bgcolor="#eeeeee">
																						<td align="center" class=box-title>
																						  <digi:trn key="aim:reportBuilder:AvailableMeasures">
																							Available Measures
																						  </digi:trn>
																						</td>
																					</tr>
																					<tr height="10">
																						<td align="right">
																						  <digi:trn key="aim:reportBuilder:SelectAll">
																							Select All
																						  </digi:trn>
																							<input type="checkbox" name="checkall" onclick="checkUncheckAll();">
																							</input>
																						</td>
																					</tr>
																					<TR>
																						<TD>
																							<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top"
																							align="top" bgcolor="#aaaaaa" border=0>
																								<c:if test="${!empty aimAdvancedReportForm.ampMeasures}">
																								<logic:iterate name="aimAdvancedReportForm" id="ampMeasures"
																								property="ampMeasures" >
																								<TR bgcolor="#ffffff">
																									<td align="left" width="98%" valign=top>
																									<digi:trn key="aim:reportBuilder:${ampMeasures.aliasName}">
																										<c:out value="${ampMeasures.aliasName}"/>
																									 </digi:trn>
																									</td>
																									<td align="right">
																										<html:multibox property="selectedColumns">
																										<c:out value="${ampMeasures.measureId}"/>
																									    </html:multibox>
																									</td>
																								</tr>
																								</logic:iterate>
																								</c:if>
																							</TABLE>
																						</TD>
																					</TR>
																					<tr>
																						<c:if test="${empty aimAdvancedReportForm.ampMeasures}">
																						<td>
																							<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0
																							vAlign="top" align="center" bgcolor="#f4f4f2">
																								<tr bgcolor="#eeeeee">
																									<td class=box-title align=center>
																						  <digi:trn key="aim:reportBuilder:Nomeasures">
																											No measures available to add
																						  </digi:trn>
																									</td>
																								</tr>
																							</table>
																						</td>
																						</c:if>
																					</tr>
																				</TABLE>
																			</TD>
																			<TD width="50%" vAlign="top" align="left" bgcolor="#eeeeee">
																				<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top"
																				align="top" bgcolor="#eeeeee" border=0>
																					<tr height=10><td></td></tr>
																					<TR bgcolor="#eeeeee">
																						<td align="center" class=box-title>
																						 
																						<digi:trn key="aim:reportBuilder:SelectedMeasur">
																							Selected Measures
																						  </digi:trn>

																						</td>
																					</tr>
																					<tr height=10>
																						<td align="right">
																					<digi:trn key="aim:reportBuilder:SelectAll">
																								Select All
																						  </digi:trn>
																							<input type="checkbox" name="checkall2" onclick="checkUncheckAll2(this);">
																							</input>
																						</td>
																					</tr>
																					<TR>
																						<c:if test="${!empty aimAdvancedReportForm.addedMeasures}">
																						<TD>
																						<TABLE width="100%" cellPadding=2 cellSpacing=1 vAlign="top"
																						align="top" bgcolor="#aaaaaa" border=0>
																							<c:if test="${!empty aimAdvancedReportForm.addedMeasures}">
																							<logic:iterate name="aimAdvancedReportForm" id="addedMeasures"
																							property="addedMeasures" >
																							<tr bgcolor=#ffffff>
																								<td align="left" width="98%">
																								<digi:trn key="aim:reportBuilder:${addedMeasures.aliasName}">
																										<c:out value="${addedMeasures.aliasName}"/>
																							 </digi:trn>
																								</td>
																								<td align="right">
																									<html:multibox property="removeColumns" >
																										<c:out value="${addedMeasures.measureId}"/>
																									</html:multibox>
																								</td>
																								<td align="right">
																									<IMG alt="Move Up"  height=10
																									src="../ampTemplate/images/up-arrow.jpg" width=10
																									onclick="moveUp(<c:out value='${addedMeasures.measureId}' />)">
																									<IMG alt="Move Down" styleClass="test" height=10
																									src="../ampTemplate/images/down-arrow.jpg" width=10
																									onclick="moveDown(<c:out value='${addedMeasures.measureId}' />)">
																								</td>
																							</tr>
																							</logic:iterate>
																							</c:if>
																						</TABLE>
																						</TD>
																						</c:if>

																						<c:if test="${empty aimAdvancedReportForm.addedMeasures}">
																						<td >
																							<TABLE width="100%" height="100" cellPadding=2 cellSpacing=0
																							vAlign="top" align="center" bgcolor="#f4f4f2">
																								<tr bgcolor="#eeeeee">
																									<td class=box-title align=center>
																									<digi:trn key="aim:reportBuilder:NOmeasuresselected ">
																									No measures selected
																						  </digi:trn>

																									</td>
																								</tr>
																							</table>
																						</td>
																						</c:if>
																					</TR>
																				</TABLE>
																			</TD>
																		</TR>
																		<TR><TD colspan="2">
																			<table width="638" bgColor=#f4f4f2 class="box-border-nopadding" border=0>
																				<tr align="center">
																					<td align="center" class="box-title" rowspan="3" width="300">
																						&nbsp;&nbsp;&nbsp;&nbsp;
																						<digi:trn key="aim:AnnualReport:Kindreport">
																								Kind of report
																							</digi:trn>
																					</td>
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:radio property="reportOption" value="A">
																							<digi:trn key="aim:AnnualReport">
																								Annual
																							</digi:trn>
																						</html:radio>
																					</td>
																				<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:radio property="reportOption" value="Q">
																							<digi:trn key="aim:QuarterlyReport">
																								Quarterly
																							</digi:trn>
																						</html:radio>
																					</td>
																				</tr>
																				<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:radio property="reportOption" value="M">
																							<digi:trn key="aim:MonthlyReport">
																								Monthly
																							</digi:trn>
																						</html:radio>
																					</td>
																				</tr>

																				<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="hideActivities">
																							<digi:trn key="aim:summaryReport">
																								Summary Report
																							</digi:trn>
																						</html:checkbox>
																					</td>
																				</tr>

																			<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="drilldownTab">
																							<digi:trn key="aim:drilldownTab">
																							Show as a drilldown in My Tabs
																							</digi:trn>
																						</html:checkbox>
																					</td>
																				</tr>

																		<tr align="center">
																					<td width="300" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<html:checkbox property="publicReport">
																							<b><digi:trn key="aim:publicReport">
																							Public Report
																							</digi:trn>
																							</b>
																						</html:checkbox>
																						<input type="hidden" value="hidden" name="wasSelectMeasuresStep"/>
																					</td>
																				</tr>

																			</table>
																		</TD></TR>
																	</TABLE>
																</TD>
															</TR>
															<tr align="center">
																<td align="center">
																	<html:button  styleClass="dr-menu" property="submitButton"  onclick="addColumn()">
																		 <digi:trn key="btn:add">Add</digi:trn> >>
																	</html:button>
																	<html:button  styleClass="dr-menu" property="submitButton"  onclick="deleteColumn()">
																		<< <digi:trn key="btn:remove">Remove</digi:trn>
																	</html:button>
																</td>
															</tr>	<!-- Add and Remove Button-->
															<tr>
																<td height=30>	&nbsp;</td>	</tr>
															<tr>
																<td align="right" colspan="2">
																	<html:hidden name="stepName" property="stepName" value="SelectMeasures" />
																	<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:backStep()">
																		<< <digi:trn key="btn:previous">Previous</digi:trn>
																	</html:button>
																	<c:set var="message">
																		<digi:trn key="aim:reports:DataNotSaved">Do you really want to quit Report Generator? \nWarning: All your Current Data Will be Lost... press OK to QUIT Report Generator.</digi:trn>
																	</c:set>
																	<html:button  styleClass="dr-menu" property="submitButton"  onclick="return quitAdvRptMngr('${message}')">
																		<digi:trn key="btn:cancel">Cancel</digi:trn>
																	</html:button>
																	<html:button  styleClass="dr-menu" property="submitButton"  onclick="javascript:gotoStep()">
																		<digi:trn key="btn:next">Next</digi:trn> >>
																	</html:button>
																</td>
															</tr>
														</TABLE>
													</TD>
												</TR>
											</TABLE>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</table>
				</td>
				<td class=r-dotted-lg align=left vAlign=top >&nbsp;
				</td>
			</tr>
		</table>
	</td>
</TR>
</TABLE>
</digi:form>



