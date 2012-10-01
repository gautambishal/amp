<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<digi:instance property="datadispatcherform" />
<html>
  
  <head>
  	<title><digi:trn>Aid Management Platform - Advanced GIS</digi:trn></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=7" /> 
    <!--The viewport meta tag is used to improve the presentation and behavior of the samples 
      on iOS devices-->
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
    <title>
    </title>
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dijit/themes/soria/soria.css">
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dojox/grid/resources/Grid.css"> 
    <link rel="stylesheet" type="text/css" href="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dojox/grid/resources/SoriaGrid.css"> 
    <digi:ref href="/TEMPLATE/ampTemplate/css_2/amp.css" type="text/css" rel="stylesheet" />
   	<digi:ref href="/TEMPLATE/ampTemplate/css_2/mapstyles.css" type="text/css" rel="stylesheet" />
   	
  
    <script type="text/javascript">
      var djConfig = {
        parseOnLoad: true
      };
    </script>
    <!-- Map Scripts -->
    <script type="text/javascript" src="<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/?v=3.0"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/amp/DecimalFormat.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/maputils.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/mapfunctions-debug.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/Ext.util.DelayedTask-nsRemoved.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/esri.ux.layers.ClusterLayer-debug.js"/>"></script>
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/esrigis/basemapgallery.js"/>"></script>
   	
   	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/menu/assets/skins/sam/menu.css"> 
   	
   	 <style type="text/css">
      @import "<c:out value="${datadispatcherform.apiurl}"/>/jsapi/arcgis/3.0/js/dojo/dijit/themes/claro/claro.css";
      .zoominIcon { background:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomin.png) no-repeat right; width:16px; height:16px;}
      .zoomoutIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_zoomout.png); width:16px; height:16px; }
      .zoomfullextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_fullextent.png); width:16px; height:16px; }
      .zoomprevIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_previous.png); width:16px; height:16px; }
      .zoomnextIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_next.png); width:16px; height:16px; }
      .panIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_pan.png); width:16px; height:16px; }
      .deactivateIcon { background-image:url(/TEMPLATE/ampTemplate/img_2/gis/nav_decline.png); width:16px; height:16px; }
    </style>
    
<script type="text/javascript">
	$(function(){
  		$('#filterbtn').click(function(){
			if (filterenable()){
     			$('#filterdiv').toggle();
			}else{
				alert('If you click filters, you will lose all your previus filter from reports - Filter is disable');
			}
     	});
	});
	$(function(){
  		$('#toolsbtn').click(function(){
     		$('#navToolbar').toggle();
     	});
	});

	$(function(){
  		$('#search').click(function(){
     		$('#distancediv').toggle();
     	});
	});

	$(function(){
  		$('#datasource').click(function(){
     		$('#sourcediv').toggle();
     		filldatasourcetable();
     	});
	});
	
	$(function(){
  		$('#nationalp').click(function(){
     		$('#sourcediv').toggle();
     		filldatasourcetablenational();
     	});
	});
	
	
	$(function(){
  		$('#sbyd').click(function(){
  	  		var value = $('#distance').val();
  	  		if (isNumber(value)){
  				searchdistance = value;
     			$('#distancediv').toggle();
     			searchactive = true;
  	  		}else{
				alert('<digi:trn>"The value must be numeric and positive"</digi:trn>');
  	  	  	}
     	});
	});
	
	$(function(){
  		$('#basemap').click(function(){
     		$('#basemapGalleryesri').toggle();
     	});
	});
	
	$(function(){
  		$('#basemaplocal').click(function(){
     		$('#basemapGallery').toggle();
     	});
	});
	$(function(){
  		$('#minmenu').click(function(){
  			$('#divmenucontent').toggle('slow');
     	});
	});
	
	$(function(){
  		$('#mediasearch').click(function(){
  			$('#mediasearchdiv').toggle('slow');
     	});
	});
	$(function(){
  		$('#mediago').click(function(){
  			sendText(document.getElementById('searchtext').value);
     	});
	});
	
	var currentFormat = "<%=org.digijava.module.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.GlobalSettingsConstants.NUMBER_FORMAT) %>";
	
	function translate(text) {
        
		if(text == "Commitments") {
            value = "<digi:trn>Commitments</digi:trn>";
            return value;
         }
        if(text == "Disbursements") {
            value = "<digi:trn>Disbursements</digi:trn>";
            return value;
         }
        if(text == "Expenditures") {
            value = "<digi:trn>Expenditures</digi:trn>";
            return value;
         }
        if(text == "Point color reference") {
            value = "<digi:trn>Point color reference</digi:trn>";
            return value;
         }
        if(text == "Activity Details") {
            value = "<digi:trn>Activity Details</digi:trn>";
            return value;
         }
        if(text == "Activity") {
            value = "<digi:trn>Activity</digi:trn>";
            return value;
         }
        if(text == "Donors") {
            value = "<digi:trn>Donors</digi:trn>";
            return value;
         }
        if(text == "Primary Sector") {
            value = "<digi:trn>Primary Sector</digi:trn>";
            return value;
         }
        if(text == "Location") {
            value = "<digi:trn>Location</digi:trn>";
            return value;
         }
        if(text == "Total commitments") {
            value = "<digi:trn>Total commitments</digi:trn>";
            return value;
         }
        if(text == "Total disbursements") {
            value = "<digi:trn>Total disbursements</digi:trn>";
            return value;
         }
        if(text == "Commitments for this location") {
            value = "<digi:trn>Commitments for this location</digi:trn>";
            return value;
         }
        if(text == "Disbursements for this location") {
            value = "<digi:trn>Disbursements for this location</digi:trn>";
            return value;
         }
        if(text == "Region") {
            value = "<digi:trn>Region</digi:trn>";
            return value;
         }
        if(text == "zone") {
            value = "<digi:trn>zone</digi:trn>";
            return value;
         }
        if(text == "Funding") {
            value = "<digi:trn>Funding</digi:trn>";
            return value;
         }
        if(text == "Showing commitments for Region") {
            value = "<digi:trn>Showing commitments for Region</digi:trn>";
            return value;
         }
        if(text == "Showing commitments for Region") {
            value = "<digi:trn>Showing commitments for Zone</digi:trn>";
            return value;
         }
        if(text == "Currency") {
            value = "<digi:trn>Currency</digi:trn>";
            return value;
         }
        if(text == "Fiscal Year Start") {
            value = "<digi:trn>Fiscal Year Start</digi:trn>";
            return value;
         }
        if(text == "Status") {
            value = "<digi:trn>Status</digi:trn>";
            return value;
         }
        if(text == "Financing Instrument") {
            value = "<digi:trn>Financing Instrument</digi:trn>";
            return value;
         }
        if(text == "Type of Assistance") {
            value = "<digi:trn>Type of Assistance</digi:trn>";
            return value;
         }
        if(text == "Only on budget projects") {
            value = "<digi:trn>Only on budget projects</digi:trn>";
            return value;
         }
        if(text == "Organization Type") {
            value = "<digi:trn>Organization Type</digi:trn>";
            return value;
         }
        if(text == "Structure Types") {
            value = "<digi:trn>Structure Types</digi:trn>";
            return value;
         }
        if(text == "Organization Group") {
            value = "<digi:trn>Organization Group</digi:trn>";
            return value;
         }
        if(text == "Others") {
            value = "<digi:trn>Others</digi:trn>";
            return value;
         }
        if(text == "Select a point") {
            value = "<digi:trn>Select a point</digi:trn>";
            return value;
         }
        return text;
    	}
	
</script>

 	<!-- Filter Styles -->
   	<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />
	
	<!-- Filter Scripts-->
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
	<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"/>"></script> 
	<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"/>"></script> 
	
		
	
 </head> 
  <body class="soria gisbody">
   <img id="loadingImg" src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif" style="position:absolute;left:50%;top:50%; z-index:200;border: 8px solid white;-moz-border-radius: 8px;" />
    <div id="mainWindow" dojotype="dijit.layout.BorderContainer" design="headline" gutters="false" style="width:100%; height:100%;">
  		<div id="map" dojotype="dijit.layout.ContentPane" class="roundedCorners" region="center">
       </div>
        <div id="basemapGallery region="center" ></div>
       	<div id="basemapGalleryesri" region="center"></div>
       <div class="headerBackground"> </div>
       <div class="header"style="float:left;">
	 		<div style="margin-left:-85px;padding:8px 0px 0px 0px;">
				<img src="/TEMPLATE/ampTemplate/img_2/amp_trans.png" align=left>
				<div class="amp_label">&nbsp;<digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></div>
			</div>
        </div>
        <div class="headerContent" align="right" style="vertical-align: middle; position:relative;" id="mainmenu">
      	<table cellspacing="5px" cellpadding="5px" style="height: 100%;">
				<tbody>
					<tr>
						<td id="toolsbtn" class="mapMenuItem" valign="middle" align="left" style="cursor: pointer;"><digi:trn>Navigation</digi:trn></td>
						<td id="filterbtn" class="mapMenuItem" valign="middle" align="left" style="cursor: pointer;"><digi:trn>Filter</digi:trn></td>
						
						<field:display name="Use Esri Online Maps" feature="Select Base Map">
							<td id="basemap" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>" style="background:#fff;border:1px solid #fff;">
							</td>
						</field:display>
						<field:display name="Use Local Base Maps" feature="Select Base Map">
							<td id="basemaplocal" valign="middle" align="center" style="cursor: pointer;">
								<img src="/TEMPLATE/ampTemplate/img_2/imgBaseMap.png" align=left height="20px" width="20px" alt="<digi:trn>Select base Map</digi:trn>" style="background:#fff;border:1px solid #fff;">
							</td>
						</field:display>
					</tr>
			</table>
			<div id="mainGisMenu">
				<img src='/TEMPLATE/ampTemplate/img_2/gis/minimize.gif' id="minmenu" style="margin-right:5px;cursor: pointer;">
				<div class="gisBoxHeader">
					<h3 style="line-height:1em;"><digi:trn>Tools</digi:trn></h3>
	            </div>
	            <div id="divmenucontent">
		            <ul>
		            	<feature:display name="Search  Structures" module="Map Module">
		              		<li class="mapMenuItem"  id="search" style="cursor: pointer;"><digi:trn>Search  Structures</digi:trn></li>
		              	</feature:display>
		              	<feature:display name="Highlight regions" module="Map Module">
							<li id="hlight" align="left" onclick="getHighlights(0);" style="cursor: pointer;"><digi:trn>Highlight regions</digi:trn></li>
						</feature:display>
						<feature:display name="Highlight Zones" module="Map Module">
							<li id="hlightz" onclick="getHighlights(1);" style="cursor: pointer;"><digi:trn>Highlight Zones</digi:trn></li>
						</feature:display>
						<feature:display name="Add activity" module="Map Module">
							<li id="add" onclick="addActivity();" style="cursor: pointer;"><digi:trn>Add Activity</digi:trn></li>
						</feature:display>
						<!-- 
						<li onclick="getActivities(true);" style="cursor: pointer;"><digi:trn>Activities</digi:trn></li>
						-->
						<feature:display name="Structures" module="Map Module">
							<li id="structures" onclick="getStructures(false);" style="cursor: pointer;"><digi:trn>Structures</digi:trn></li>
						</feature:display>
						<feature:display name="Use Indicators Maps" module="Map Module">
							<li id="povmap" onclick="toggleindicatormap('indicator');" style="cursor: pointer;"><digi:trn>Poverty Map</digi:trn></li>
							<li id="censusmap" onclick="toggleindicatormap('census');" style="cursor: pointer;"><digi:trn>Census Map</digi:trn></li>
						</feature:display>
						<li id="datasource" style="cursor: pointer;"><digi:trn>Data Source</digi:trn></li>
						<feature:display name="Media Search" module="Map Module">
							<li id="mediasearch" style="cursor: pointer;"><digi:trn>Media Search</digi:trn></li>
						</feature:display>
						<feature:display name="Show National" module="Map Module">
							<li id="shownational" onclick="getNationalActivities();" style="cursor: pointer;"><digi:trn>Show National</digi:trn></li>
						</feature:display>
				     </ul>
			     </div>
		    </div>
        	<div style="background:url(/TEMPLATE/ampTemplate/img_2/gis/shade.png) no-repeat center top;height:10px;width:100%;border-top:1px solid #fff;"></div>
		</div>
		<div id="filterdiv" style="position:absolute;z-Index:100;top:50px;display: none;cursor: pointer;left:50%">
 			<jsp:include page="filter.jsp" flush="true"></jsp:include>
 		</div>
 		<div class='legendHeader' id="fakecolor">Donor Legend<br/><hr/></div>
 		<div id="pointsLegend" class="legendContent" style="rigth:10px;top:421px;"></div>
        <div id="highlightLegend" class="legendContent" style="left:240px;"></div>
        <div id="legendDiv" class="legendContent" style="top:585px;left:65px;width: 165px;">
        	<img src="/TEMPLATE/ampTemplate/img_2/gis/legend-poverty.jpg">	
        </div>
        <div id="poplegendDiv" class="legendContent" style="top:570px;left:65px;width: 180px;">
        	<img src="/TEMPLATE/ampTemplate/img_2/gis/population-legend.jpg">	
        </div>
        <div id="NationalDiv" class="legendContent" style="top:310px;left:74%;font-size: 10px;width: 170px;">
        	<p id="nationalp">
        		<digi:trn>Click here to see national projects</digi:trn>
        	</p>
        </div>
       
        <div class="usaidlogo">
        	<table>
        		<tr>
        			<td align="right" style="font-size: 11px;color: white;">
        				<b>
        					<digi:trn>Funding Provided By</digi:trn>
        				</b> 
        			</td>
        		</tr>
        		<tr>
        			<td>
        				<img alt="USAID" src="/TEMPLATE/ampTemplate/img_2/gis/usaid_horizontal_150.png" border="0">
        			</td>
        		</tr>
        	</table>
        </div>
        
        <!-- Filter -->
        <div id="selectedfilter" class="legendContent" style="top:80px;left:100px;display:none;width: 35%;"> 
        	<div onclick="$('#selectedfilter').hide('slow');" style="color:white;float:right;cursor:pointer;">X</div>
        	<div class="legendHeader"><digi:trn>Selected Filters</digi:trn><br/><hr/></div>
        	<table width="90%" cellspacing="0" cellpadding="0" border="0">
        		<tbody>
					<tr>
						<td valign="top" style="font-size:11px;font-family:Arial,Helvetica,sans-serif" id="sfilterid">
						</td>
					</tr>
					<tr>
						<td valign="top" style="font-size:11px;font-family:Arial,Helvetica,sans-serif">
					</tr>
				</tbody>
        	</table>
        </div>
        <!-- Data Source -->
        <div id="sourcediv" class="legendContent" style="top:55px;left:30px;width: 70%;display:none;height: 400px;"> 
        	<div onclick="$('#sourcediv').hide('slow');" style="color:white;float:right;cursor:pointer;">X</div>
        	<div class="legendHeader"><digi:trn>Data Source</digi:trn><br/><hr/></div>
        	<table id="sourceheader" width="95%" cellspacing="0" cellpadding="0" border="0" style="font-size:11px;font-family:Arial,Helvetica,sans-serif;padding-right: 5px;">
        		<tbody>
					<tr>
						<td valign="top" style="font-weight: bolder;width: 60%;">
							<digi:trn>Activity Name</digi:trn>
						</td>
						<td valign="top" style="font-weight: bolder;width: 20%;">
							<digi:trn>Activity Id</digi:trn>
						</td>
						<td valign="top" style="font-weight: bolder;">
							<digi:trn>Donors</digi:trn>
						</td>
					</tr>
				</tbody>
        	</table>
        	<div style="overflow-y: scroll;height: 350px;">
        	<table id="sourcecontent" width="97%" cellspacing="0" cellpadding="0" border="0" style="font-size:11px;font-family:Arial,Helvetica,sans-serif;padding-right: 5px;">
        		
        	</table>
        	</div>
        </div>
        <!-- Search Structures-->
        <div id="distancediv" class="searchContent">
        	<table>
        		<tr>
        			<td style="color: white;">
        				<digi:trn>Distance in Km</digi:trn>
        			</td>
        			<td>
        				<input type="text" id="distance" style="width: 50px;"/> 
        			</td>
        			<td>
        				<c:set var="trnGoBtn">
                        	<digi:trn key="aim:btnClose">Go</digi:trn>
                       </c:set>
        				<input type="button" id="sbyd" width="5px" value="${trnGoBtn}"/> 
        			</td>
        		</tr>
        	</table>
        </div>
        
         <!-- Search text-->
        <feature:display name="Media Search" module="Map Module">
        <div id="mediasearchdiv" class="searchContent">
        	<table>
        		<tr>
        			<td style="color: white;">
        				<digi:trn>Text</digi:trn>
        			</td>
        			<td>
        				<input type="text"  name="formInput" id="searchtext"></input>
        				<!-- input type="text" id="textsearch" style="width: 100px;"/--> 
        			</td>
        			<td>
        				<button type="submit" data-dojo-type="dijit.form.Button" id="mediago">Send it!</button>
        			</td>
        			
        		</tr>
        	</table>
        </div>
        </feature:display>	
        <div id="navToolbar" dojoType="dijit.Toolbar" region="leading" style="z-Index:999;display: none;">
        <div class="toolscontainer" style="margin:5px 0px 0px 0px;">
        	<div class="gisBoxHeader">
			  	<h3><digi:trn>Tools panel</digi:trn></h3><a href="#"></a>
            </div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomin" iconClass="zoominIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_IN);"><digi:trn>Zoom In</digi:trn></div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomout" iconClass="zoomoutIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.ZOOM_OUT);"><digi:trn>Zoom Out</digi:trn></div>
			<div class="mapButton" dojoType="dijit.form.Button" id="zoomfullext" iconClass="zoomfullextIcon" onClick="navToolbar.zoomToFullExtent();"><digi:trn>Full Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="zoomprev" iconClass="zoomprevIcon" onClick="navToolbar.zoomToPrevExtent();"><digi:trn>Prev Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="zoomnext" iconClass="zoomnextIcon" onClick="navToolbar.zoomToNextExtent();"><digi:trn>Next Extent</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="pan" iconClass="panIcon" onClick="navToolbar.activate(esri.toolbars.Navigation.PAN);"><digi:trn>Pan</digi:trn></div>
		    <div class="mapButton" dojoType="dijit.form.Button" id="deactivate" iconClass="deactivateIcon" onClick="navToolbar.deactivate()"><digi:trn>Deactivate</digi:trn></div>
		</div></div>
    </div>  
	<div class="tooltip" style="position: absolute; display: block;z-index:100;" id="tooltipHolder"></div>

  </body>
</html>
