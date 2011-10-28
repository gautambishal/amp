<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.form.RegionalFundingForm"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script type="text/javascript">

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   document.aimRegionalFundingForm.action = "<%=addUrl%>~pageId=1~step=4~action=edit~surveyFlag=true~activityId=" + id;
	document.aimRegionalFundingForm.target = "_self";
   document.aimRegionalFundingForm.submit();
}


YAHOOAmp.namespace("YAHOOAmp.amp");

var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
	width:"940px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

var responseSuccess = function(o){
	/* Please see the Success Case section for more
	* details on the response object's properties.
	* o.tId
	* o.status
	* o.statusText
	* o.getResponseHeader[ ]
	* o.getAllResponseHeaders
	* o.responseText
	* o.responseXML
	* o.argument
	*/
	var response = o.responseText; 
	var content = document.getElementById("myContentContent");
	//response = response.split("<!")[0];
	content.innerHTML = response;
	//content.style.visibility = "visible";
	
	showContent();
}

function showPanelLoading(msg){
	   var content = document.getElementById("myContentContent");
	   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
	   "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
	   showContent();
	 }
	 
var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
	//alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function showContent(){
	var element = document.getElementById("myContent");
	element.style.display = "inline";
	if (panelStart < 1){
		myPanel.setBody(element);
	}
	if (panelStart < 2){
		document.getElementById("myContent").scrollTop=0;
		myPanel.show();
		panelStart = 2;
	}
}

function preview(id)
{
	showPanelLoading();
	var postString="&pageId=2&activityId=" + id+"&isPreview=1&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg='\n<digi:trn>Activity Preview</digi:trn>';
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin();

function expandAll() {
   
	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {

	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}

</script>

<digi:errors/>
<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
<digi:instance property="aimRegionalFundingForm" />

<digi:form action="/viewRegionalFundingBreakdown.do" method="post">

<html:hidden property="ampActivityId" />
<html:hidden property="regionId" />
<html:hidden property="tabIndex" />

<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
	<TR>
		<TD vAlign="top" align="center">
			<!-- contents -->
			<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
				<TR>
					<TD bgcolor="#f4f4f4">
						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4">
							<TR bgColor=#f4f4f2>
      	      					<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												<SPAN class=crumb>
													<jsp:useBean id="url" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${url}" property="ampActivityId">
														<bean:write name="aimRegionalFundingForm" property="ampActivityId"/>
													</c:set>
													<c:set target="${url}" property="tabIndex" value="5"/>
													<c:set var="translation">
															<digi:trn key="aim:clickToViewRegionalFunding">Click here to view regional funding</digi:trn>
													</c:set>
													<digi:link href="/viewRegionalFundingBreakdown.do" name="url"
													styleClass="comment" title="${translation}" >
													<digi:trn key="aim:regionalFunding">Regional Funding</digi:trn>
													</digi:link>&nbsp;&gt;&nbsp;
													<digi:trn key="aim:actOverview">Overview</digi:trn>
												</SPAN>
											</TD>
											<TD align="right">&nbsp;
												
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>

							<TR bgColor=#f4f4f2>
      	      					<TD align=left>
									<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
										<TR>
											<TD align="left">
												Currency :
												<html:select property="currency" styleClass="dr-menu">
  		       		        					<html:optionsCollection name="aimRegionalFundingForm"
													property="currencies" value="currencyCode" label="currencyName"/>
												</html:select>&nbsp;&nbsp;&nbsp;
												<c:if test="${aimRegionalFundingForm.calFilter == true}">
												Calendar :
												<html:select property="calFilterValue" styleClass="dr-menu">
				  		       		        	<html:optionsCollection name="aimRegionalFundingForm"
													property="fiscalCalendars" value="ampFiscalCalId" label="name"/>
												</html:select>
												</c:if>
												<html:submit value="GO!" styleClass="dr-menu"/>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>

							<TR bgColor=#f4f4f2>
								<TD vAlign="top" align="center" width="750">
									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top" align="center" bgColor=#f4f4f2>
										<TR>
											<TD width="750" bgcolor="#F4F4F2">
                                                <TABLE cellSpacing=0 cellPadding=0 bgColor=#f4f4f2 border=0>
                                                <TR bgColor=#f4f4f2>
                                                    <TD width="7" height="17">
                                                    <img src="/TEMPLATE/ampTemplate/css/boxleftcorner.gif" border="0"  /><br />
                                                    </TD>
                                                    <TD bgColor=#ccdbff height="17" style="padding:4px 4px 4px 4px;">
                                                    <strong>
                                                        <digi:trn>Regional Funding</digi:trn>
                                                    </strong>
                                                    </TD>
                                                    <TD width="7" height="17">
                                                    <img src="/TEMPLATE/ampTemplate/css/boxrightcorner.gif" border="0"  /><br />
                                                    </TD>
                                                </TR>
                                                </TABLE>
											</TD>
										</TR>
										<TR>
											<TD width="750" bgcolor="#F4F4F2" align="center" style="border:1px solid #ccdbff;">
                                            <br />
												<TABLE width="750"  border="0" cellpadding="4" cellspacing="1">
				                 					<TR bgcolor="#DDDDDB" >
	            						            	<TD>
	            						            		<digi:trn key="aim:region">Region</digi:trn>
	            						            	</TD>
	            						            	<field:display name="Total Committed" feature="Commitments">
															<TD>
																<digi:trn key="aim:totalCommitted">Total Committed</digi:trn>
															</TD>
														</field:display>
														<field:display name="Total Disbursed" feature="Disbursement">
								                         	<TD>
								                         		<digi:trn key="aim:totalDisbursed">Total Disbursed</digi:trn>
								                         	</TD>
														</field:display>
														<field:display name="Undisbursed Funds" feature="Funding Information">
															<TD>
																<digi:trn key="aim:unDisbursedFunds">Undisbursed Funds</digi:trn>
															</TD>
														</field:display>
														<field:display name="Total Expended" feature="Expenditures">
								                         	<TD>
								                         		<digi:trn key="aim:totalExpended">Total Expended</digi:trn>
								                         	</TD>
														</field:display>
														<field:display name="Unexpended Funds" feature="Funding Information">
															<TD>
																<digi:trn key="aim:unExpendedFunds">Unexpended Funds</digi:trn>
															</TD>
														</field:display>
													</TR>
			                    					<logic:notEmpty name="aimRegionalFundingForm" property="regionalFundings">
														<logic:iterate name="aimRegionalFundingForm" property="regionalFundings" id="fd"
			  	                   							type="org.digijava.module.aim.helper.RegionalFunding">
															<TR valign="top" bgcolor="#f4f4f2">
					    	           							<TD>
																	<c:set target="${url}" property="regionId">
																		<bean:write name="fd" property="regionId"/>
																	</c:set>
																	<digi:link href="/viewRegFundDetails.do" name="url">
																	<bean:write name="fd" property="regionName"/></digi:link>
																</TD>
																<field:display name="Total Committed" feature="Commitments">
													                <TD align="right">
													                <%=FormatHelper.formatNumber(fd.getTotCommitments())  %>
													                </TD>
													            </field:display>
																<field:display name="Total Disbursed" feature="Disbursement">
													                <TD align="right">
													                	<%=FormatHelper.formatNumber(fd.getTotDisbursements())  %>
												
													                </TD>
													            </field:display>
																<field:display name="Undisbursed Funds" feature="Funding Information">
											      			        <TD align="right">
											      			        <%=FormatHelper.formatNumber(fd.getTotUnDisbursed())  %>
											      			        	
											      			        </TD>
											      			    </field:display>
																<field:display name="Total Expended" feature="Expenditures">
														            <TD align="right">
														            <%=FormatHelper.formatNumber(fd.getTotExpenditures())  %>
														            </TD>
														        </field:display>
																<field:display name="Unexpended Funds" feature="Funding Information">
											            		    <TD align="right">
											            		    	
											            		    	 <%=FormatHelper.formatNumber(fd.getTotUnExpended())  %>
											            		    </TD>
											            		</field:display>
															</TR>
														</logic:iterate>
													</logic:notEmpty>
		                  							<TR valign="top" class="note">
														<TD>
															<digi:trn key="aim:total">Total</digi:trn>
														</TD>
														<field:display name="Total Committed" feature="Commitments">
															<TD align="right">
															<% %>
															<%=FormatHelper.formatNumber(((RegionalFundingForm) pageContext.getAttribute("aimRegionalFundingForm")).getTotCommitments())%>
															</TD>
														</field:display>
														<field:display name="Total Disbursed" feature="Disbursement">
															<TD align="right">
																<%//=FormatHelper.formatNumber(((RegionalFundingForm) pageContext.getAttribute("aimRegionalFundingForm")).getTotDisbursements())%>
															</TD>
														</field:display>
														<field:display name="Undisbursed Funds" feature="Funding Information">
															<TD align="right">
															<%=FormatHelper.formatNumber(((RegionalFundingForm) pageContext.getAttribute("aimRegionalFundingForm")).getTotUnDisbursed())%>
														</TD>
														</field:display>
														<field:display name="Total Expended" feature="Expenditures">
															<TD align="right">
															<%=FormatHelper.formatNumber(((RegionalFundingForm) pageContext.getAttribute("aimRegionalFundingForm")).getTotExpenditures())%>
									
															</TD>
														</field:display>
														<field:display name="Unexpended Funds" feature="Funding Information">
															<TD align="right">
															<%=FormatHelper.formatNumber(((RegionalFundingForm) pageContext.getAttribute("aimRegionalFundingForm")).getTotUnExpended())%>
															</TD>
														</field:display>
													</TR>
												</TABLE>
                                            <br />
											</TD>
										</TR>
										<TR>
											<TD>
<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
													<FONT color=blue>*
													<digi:trn key="aim:allTheAmountsInThousands">

													All the amounts are in thousands (000)</digi:trn>
													</FONT>
</gs:test>
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
</TABLE>
</digi:form>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>