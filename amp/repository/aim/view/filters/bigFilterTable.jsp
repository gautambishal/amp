<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
	
<bean:define id="elements" toScope="page" scope="request" name="reqElements" />
<bean:define id="propertyObj" toScope="page" scope="request" name="reqPropertyObj" />
<bean:define id="searchManagerId" toScope="page" scope="request" name="reqSearchManagerId" />
<bean:define id="searchFieldWidth" toScope="page" value="130px"/>
<logic:notEmpty scope="request" name="reqSearchFieldWidth">
	<bean:define id="searchFieldWidth" toScope="page">${reqSearchFieldWidth}</bean:define>
</logic:notEmpty>	

<c:set var="selectorHeaderSize" scope="page" value="11" />
<logic:notEmpty scope="request" name="reqSelectorHeaderSize">
	<c:set var="selectorHeaderSize" scope="page" value="${reqSelectorHeaderSize}" />
</logic:notEmpty>	
	<div class="grouping_selector_wrapper" style="float: left; width: 40%; padding: 0px; height: 98%;">
		<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; width: 100%; color: white; padding-top:5px; height: ${selectorHeaderSize}%; border: 1px solid #CCCCCC;border-bottom: 0px;">
			<div class="inside">
				<b class="ins_header"><digi:trn>Grouping Selector</digi:trn></b> 
			</div>
		</div>
		<div style="border: 1px solid #CCCCCC; height: ${100-selectorHeaderSize}%; width: 100%; background: white;">		
				<table style="width: 95%;margin-top: 15px;" align="center" class="inside" >
					<logic:iterate id="element" name="elements" scope="page">
						<tr style="cursor: pointer;"
							onclick="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
							onMouseover="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).markRow(false)" 
							onMouseout="getRowSelectorInstance(this, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).unmarkRow(false)">
							<td class="inside">
								<div class="selector_type_cont">
									<span style="float:left;"><digi:trn>${element.name}</digi:trn></span>
									<span style="float: right;">
										(${element.rootHierarchyListable.countDescendants-1})
										<button type="button" onclick="getRowSelectorInstance(this.parentNode, ${propertyObj}, new DivManager('${element.htmlDivId}', ${propertyObj}), true).toggleRow()" 
										style="display: none;">Fake</button>
									</span>
								</div>
							</td>
							
						</tr>
					</logic:iterate>
				</table>
		</div>
	</div>
	<div class="member_selector_wrapper" style="margin-left:40%; padding: 0px; height: 98%;background: white;">
		<div style="background-image:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);margin:0px; color: white; padding-top:5px; height: ${selectorHeaderSize}%;border: 1px solid #CCCCCC;border-bottom: 0px;">
				<div class="inside" style="float: left" >&nbsp;
					<b class="ins_header">
						<digi:trn>Member Selector</digi:trn>
					</b>
				</div>
				<div class="memberSelectorInputWrapper" style="float: right">
					<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="${searchManagerId}" type="text" style="margin-top:0px; width: ${searchFieldWidth};" class="inputx" />&nbsp;
					<button class="buttonx_sm" onclick="getSearchManagerInstanceById('${searchManagerId}').findPrev()" style="padding: 0px;" type="button">&lt;&lt;</button>
					<button class="buttonx_sm" onclick="getSearchManagerInstanceById('${searchManagerId}').findNext()" style="padding: 0px;" type="button">&gt;&gt;</button>
				</div>
		</div>
					<c:set var="displayProperty"> </c:set>
					<logic:iterate id="element" name="elements" scope="page">
						<div style="height: ${100-selectorHeaderSize}%; display:none; border: 1px solid #CCCCCC; overflow: auto; background: white;width:100%" id="${element.htmlDivId}">
							<bean:define id="reqEntityList" name="element" property="rootHierarchyListable.children" toScope="request" />
							<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
							<div class="hiddenNameDiv" style="display: none;"><digi:trn>${element.name}</digi:trn></div>
							<ul style="list-style-type: none;">
								<li>
									<input type="checkbox" onclick="toggleCheckChildren(this);buildLabels();" class="root_checkbox"/> 
										<span style="font-family: Arial; font-size: 12px;">
											<digi:trn><c:out value="${element.rootHierarchyListable.label}"/></digi:trn>
										</span>
										<div style="display:none">
											${element.rootHierarchyListable.additionalSearchString}
										</div>
									<%@include file="hierarchyLister.jsp" %>
								</li>
							</ul>
						</div>
						<c:set var="displayProperty"> display: none;</c:set>
					</logic:iterate>
	</div>
	
	