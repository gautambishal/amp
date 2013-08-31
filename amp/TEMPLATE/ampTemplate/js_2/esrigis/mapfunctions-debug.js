dojo.require("dijit.dijit"); // Optimize: load dijit layer
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("esri.map");
dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileReadStore");
dojo.require("dijit.form.Button");
dojo.require("dijit.Toolbar");
dojo.require("esri.tasks.find");
dojo.require("esri.tasks.geometry");
dojo.require("esri.dijit.BasemapGallery");
dojo.require("esri.arcgis.utils");
dojo.require("dijit.TitlePane");
dojo.require("dijit.Menu");
dojo.require("esri.dijit.Legend");
dojo.require("esri.layers.FeatureLayer");
dojo.require("dojo.dnd.Moveable");
dojo.require("dojo.io.script");
dojo.require("esri.dijit.Print");

/*----variables---------*/
var map,geometryService, findTask, findParams;
var totallocations = 0;
var features = new Array();
var structures = new Array();
var structurespoint = new Array();
var timer_on = 0;
var activitiesarray = new Array();
var nationalactivitiesarray = new Array();
var loading;
var cL;
var cLs;
var basemapGallery;
var activitieson;
var structureson;
var maxExtent;
var basemap;
var highlightson;
var structureGraphicLayer;
var structureVisible=false;
/*---- Search  ----*/
var searchactive = new Boolean();
var searchdistance;
var foundstr = new Array();
var searchpoint;
var rangegraphicLayer;
/*-----------------Indicators Layers------------*/
var rooturl;
var basemapUrl;
var countrymapurl;
var nationalborderurl;
var COUNTY;
var DISTRICT;
var COUNT;
var GEO_ID;
var basemapsarray = new Array();
var indicatorLayerArray = new Array();
var indicatorMapArray = new Array();
donorfix="";
//Honduras
//donorfix = "BID,BM,BCIE,ACDI,UE,USAID,UNDP/PNUD,AECID,GIZ,JICA";
//Haiti
//donorfix = "UE,USAID,Ambassade du Japon,AECID,AFD,BM-EXTFUND,ACDI,Ambassade de Norvege,BID,Ambassade du Venezuela";
fixeddonorlist = donorfix.split(",");





/*
function print(){
	 var template = new esri.tasks.PrintTemplate();
	  template.exportOptions = {
	    width: 500,
	    height: 400,
	    dpi: 96
	  };
	  template.format = "SVG";
	  template.layout = "MAP_ONLY";
	  template.preserveScale = true;
	  
	  
	  var params = new esri.tasks.PrintParameters();
	  params.map = map;
	  params.template = template;
	  var printTask = new esri.tasks.CustomPrintTask("http://gis.devgateway.org/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task", {async:false});
	  printTask.execute(params, function(){alert("ok")},function(){alert("ERROR")});
}
*/

var MapConstants = {
		   "MapType": {
				"BASE_MAP" : 1,
				"MAIN_MAP" : 2,
				"GEOMETRY_SERVICE" : 4,
				"ARCGIS_API" : 5,
				"GEOLOCATOR_SERVICE" : 7,
				"BASEMAPS_ROOT" : 8,
				"NATIONAL_LAYER" : 9,
				"INDICATOR_LAYER" : 10			   
		   },
		   "MapSubType": {
			   "BASE" : 1,
			   "INDICATOR" : 2
		   }
		};
function init() {
	var xhrArgs = {
		url : "/esrigis/datadispatcher.do?getconfig=true",
		handleAs : "json",
		sync : true,
		load : function(jsonData) {
			dojo.forEach(jsonData, function(map) {
				if (map.mapSubType == MapConstants.MapSubType.INDICATOR)
				{
					indicatorLayerArray.push(map);
				}
				else
				{
					switch (map.mapType) {
					case MapConstants.MapType.BASE_MAP:
						basemapUrl = map.mapUrl;
						break;
					case MapConstants.MapType.MAIN_MAP:
						countrymapurl = map.mapUrl;
						COUNTY = map.countyField;
						DISTRICT = map.districtField;
						GEO_ID = map.geoIdField;
						COUNT = map.countField;
						break;
					case MapConstants.MapType.GEOMETRY_SERVICE:
						geometryServiceurl = map.mapUrl;
						break;
					case MapConstants.MapType.BASEMAPS_ROOT:
						rooturl = map.mapUrl;
						break;
					case MapConstants.MapType.NATIONAL_LAYER:
						nationalborderurl= map.mapUrl;
						break;
					default:
						break;
					}
				}
			});
		},
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);

	loading = dojo.byId("loadingImg");

	basemap = new esri.layers.ArcGISTiledMapServiceLayer(basemapUrl, {
		id : 'base'
	}); // Levels at which this layer will be visible);
	countrymap = new esri.layers.ArcGISDynamicMapServiceLayer(countrymapurl, {opacity : 0.50,id : 'countrymap'});

	/*
	 povertyratesmap = new esri.layers.FeatureLayer(povertyratesurl, { mode:
	 esri.layers.FeatureLayer.MODE_ONDEMAND,outFields: ["*"],
	 id:'indicator',opacity : 0.80, visible:false });
	 */
	for(var idx = 0; idx < indicatorLayerArray.length ; idx++){
		if(indicatorLayerArray[idx].mapUrl != ""){
			var currentMap = new esri.layers.ArcGISDynamicMapServiceLayer(indicatorLayerArray[idx].mapUrl,{opacity :0.80,visible:false, id:'indicator_'+indicatorLayerArray[idx].id});
			indicatorMapArray.push(currentMap);
		}
	}

	if (nationalborderurl){
		bordermap= new esri.layers.ArcGISDynamicMapServiceLayer(nationalborderurl,{opacity :0.90,visible:false,id:'border'});
	}
	if (geometryServiceurl){
		geometryService = new esri.tasks.GeometryService(geometryServiceurl);
	}
	
	esriConfig.defaults.io.proxyUrl = "/esrigis/esriproxy.do";

	var layerLoadCount = 0;
	if (basemap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, countrymap);
		}
	} else {
		dojo.connect(basemap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, countrymap);
			}
		});
	}
	if (countrymap.loaded) {
		layerLoadCount += 1;
		if (layerLoadCount === 2) {
			createMapAddLayers(basemap, countrymap);
		}
	} else {
		dojo.connect(countrymap, "onLoad", function(service) {
			layerLoadCount += 1;
			if (layerLoadCount === 2) {
				createMapAddLayers(basemap, countrymap);
			}
		});
	}
	for(var idx = 0; idx < indicatorLayerArray.length; idx++)
		var dnd = new dojo.dnd.Moveable(dojo.byId("indicator_legend_" + indicatorLayerArray[idx].id));
//	var dnd = new dojo.dnd.Moveable(dojo.byId("legendDiv"));
	var dnd = new dojo.dnd.Moveable(dojo.byId("selectedfilter"));
	var dnd = new dojo.dnd.Moveable(dojo.byId("structuresdiv"));
	
}

/**
 * Create a map, set the extent, and add the services to the map.
 * 
 * @param myService1
 * @param myService2
 */

function createMapAddLayers(myService1, myService2) {
	customLods = [
	       {"level" : 0,"resolution" : 19567.87924099992,"scale" : 7.3957190948944E7}, 
           {"level" : 1,"resolution" : 4891.96981024998,"scale" : 18489297.737236}, 
           {"level" : 2,"resolution" : 2445.98490512499,"scale" : 9244648.868618}, 
           {"level" : 3,"resolution" : 1222.99245256249,"scale" : 4622324.434309}, 
           {"level" : 4,"resolution" : 611.49622628138,"scale" : 2311162.217155}, 
           {"level" : 5,"resolution" : 305.748113140558,"scale" : 1155581.108577}, 
	       {"level" : 6,"resolution" : 152.874056570411,"scale" : 577790.554289}, 
	       {"level" : 7,"resolution" : 76.4370282850732,"scale" : 288895.277144}, 
	       {"level" : 8,"resolution" : 38.2185141425366,"scale" : 144447.638572}, 
	       {"level" : 9,"resolution" : 19.1092570712683,"scale" : 72223.819286}, 
	       {"level" : 10,"resolution" : 9.55462853563415,"scale" : 36111.909643}, 
	       {"level" : 11,"resolution" : 4.77731426794937,"scale" : 18055.954822} ];
		
		map = new esri.Map("map",{
			lods : customLods,
			extent : esri.geometry.geographicToWebMercator(myService2.fullExtent),
			"fadeOnZoom": true,
			"force3DTransforms": true,
			"navigationMode": "css-transforms"
		});
		dojo.connect(map, 'onLoad', function(map) {
		dojo.connect(dijit.byId('map'), 'resize', map,map.resize);
		dojo.byId('map_zoom_slider').style.top = '95px';
		getActivities(false);
		getStructures(false);
		/*printer = new esri.dijit.Print({
		    map: map,
		    url: "http://sampleserver6.arcgisonline.com/arcgis/rest/services/Utilities/PrintingTools/GPServer/Export%20Web%20Map%20Task"
		  }, dojo.byId("print_button"));
	    printer.startup();
	*/
	});
	
	dojo.connect(map, "onClick", doBuffer);
	dojo.connect(map, "onMouseMove", selectionFunction);

	map.addLayer(myService1);
	map.addLayer(myService2);
	
	if (nationalborderurl){
		map.addLayer(bordermap);
	}
	for(var idx = 0; idx < indicatorMapArray.length; idx++){
		map.addLayer(indicatorMapArray[idx]);
	}
	//dojo.connect(map, "onExtentChange", showExtent);
	
	createBasemapGalleryEsri();
	createBasemapGallery();
	maxExtent = map.extent;
	searchactive = false;
}

/**
 * 
 * @param id
 */
var indicatoractive = false;
function toggleindicatormap(id) {
	var layer = map.getLayer("indicator_" + id);
	var functionalayer = map.getLayer('countrymap');
	if (layer.visible) {
		layer.hide();
		$('#indicatorLegend > .legendContent').hide('slow');
		functionalayer.show();
		indicatoractive = false;
	} else if (!indicatoractive) {
		layer.show();
		$('#indicator_legend_' + id).show('slow');
		functionalayer.hide();
		indicatoractive = true;
	}
}

nationalactive=false;
function togglenational() {
	var layer = map.getLayer('border');
	var functionalayer = map.getLayer('countrymap');
	if (layer.visible) {
		layer.hide();
		$('#NationalDiv').hide('slow');
		functionalayer.show();
		nationalactive = false;
	} else if (!indicatoractive) {
		layer.show();
		$('#NationalDiv').show('slow');
		functionalayer.hide();
		nationalactive = true;
	}
}


/**
 * show map on load
 */
dojo.addOnLoad(init);

/**
 * 
 * @param evt
 */
function doBuffer(evt) {
	map.infoWindow.hide();
	if (searchactive && searchdistance) {
		showLoading();
		map.graphics.clear();
		var params = new esri.tasks.BufferParameters();
		params.geometries = [ evt.mapPoint ];

		// buffer in linear units such as meters, km, miles etc.
		params.distances = [ 1, searchdistance ];
		params.unit = esri.tasks.GeometryService.UNIT_KILOMETER;
		params.outSpatialReference = map.spatialReference;
		geometryService.buffer(params, showBuffer);
		findbydistance(evt);
	}
	if (addActivityMode) {
		MapFindPoint(implementationLevel[1], evt);
	}
}

/**
 * 
 */
function clearbuffer() {
	try {
		if (rangegraphicLayer) {
			map.removeLayer(map.getLayer("rangelayer"));
		}
		map.infoWindow.hide;
	} catch (e) {
		console.log(e);
	}
}

/**
 * Create a buffered point to show the search range
 * 
 * @param geometries
 */
function showBuffer(geometries) {
	var symbol = new esri.symbol.SimpleFillSymbol(
			esri.symbol.SimpleFillSymbol.STYLE_SOLID,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_DASH, new dojo.Color([
							112, 0, 0, 0.60 ]), 2), new dojo.Color([ 112, 0, 0,
					0.15 ]));
	try {
		if (rangegraphicLayer) {
			map.removeLayer(map.getLayer("rangelayer"));
		}
	} catch (e) {
		console.log(e);
	}
	rangegraphicLayer = esri.layers.GraphicsLayer({
		displayOnPan : false,
		id : "rangelayer",
		visible : true
	});
	dojo.forEach(geometries, function(geometry) {
		var graphic = new esri.Graphic(geometry, symbol);
		rangegraphicLayer.add(graphic);
	});

	map.addLayer(rangegraphicLayer);
	map.reorderLayer(map.getLayer("rangelayer"), 0);
}

/**
 * Iterate structures and query the geometry server to calculate the distance.
 * 
 * @param evt
 */
function findbydistance(evt) {
	searchpoint = evt;
	foundstr = [];
	var count = 0;
	if (structurespoint.length > 0) {
		var distParams = new esri.tasks.DistanceParameters();
		for ( var int = 0; int < structurespoint.length; int++) {
			distParams.distanceUnit = esri.tasks.GeometryService.UNIT_KILOMETER;
			distParams.geometry1 = evt.mapPoint;
			distParams.geometry2 = dojo.clone(structurespoint[int].geometry);

            delete structurespoint[int].geometry.attributes;
			//speed up request data removing not needed elements,Sebas
			distParams.geometry2.attributes={};			
			distParams.geodesic = true;

			geometryService.distance(distParams, function(distance) {
				if (distance <= searchdistance) {
					foundstr.push(structurespoint[count]);
				}
				count++;
				if (count + 1 > structurespoint.length) {
					showStInfoWindow();
				}
			});
		}
		
	} else {
		searchactive = false;
		hideLoading();
	}
}

/**
 * Show the search structures resutl in a infowindow
 */
function showStInfoWindow() {
	searchactive = false;
	alert('Ion!');
	hideTooltip();
	var content =
	    "<table border='0' width='100%' cellpadding='0' cellspacing='0' style='border: 1px solid gray;font-size: 10px;'>"
		+ "<tr>"
		+ "<td align='center' width='200px' style='border-right: 1px solid gray;border-bottom: 1px solid gray;padding: 4px;'><b>Name</b></td>"
		+ "<td align='center' width='100px' style='border-right: 1px solid gray;border-bottom: 1px solid gray;padding: 4px;'><b>Type</b></td>"
		+ "<td align='center' width='300px' style='border-bottom: 1px solid gray;padding: 4px;'><b>Activity</b></td>"
		+ "</tr>";
	if (map.infoWindow.isShowing) {
		map.infoWindow.hide();
	}
	map.infoWindow.setTitle("Structures");
	for ( var int = 0; int < foundstr.length; int++) {
		content = content
				+ "<tr><td style='border-right: 1px solid gray;border-bottom: 1px solid gray;padding: 3px;'>"
				+ foundstr[int].attributes["Structure Name"] + "</a></td>";
		content = content
				+ "<td align='left' style='border-right: 1px solid gray;border-bottom: 1px solid gray;padding: 3px;'>"
				+ foundstr[int].attributes["Structure Type"] + "</td>";
		content = content
				+ "<td style='border-bottom: 1px solid gray;padding: 3px;'>"
				+ foundstr[int].attributes["Activity"] + "</td></tr>";
	}
	content = content
			+ "<tr><td colspan='3'>"
			+ "<img hspace='2' onclick='ExportStructures()' vspace='2' style='cursor: pointer;' src='/TEMPLATE/ampTemplate/module/aim/images/xls_icon.jpg' border='0' alt='Export to Excel'"
			+ "</td></tr></table>";
	if (foundstr.length > 0) {
		map.infoWindow.setContent(content);
		map.infoWindow.resize(600, 200);
		map.infoWindow.show(searchpoint.screenPoint, map
				.getInfoWindowAnchor(searchpoint.screenPoint));
	} else {
		clearbuffer();
	}
	dojo.connect(map.infoWindow, "onHide", clearbuffer);
	hideLoading();
}

/**
 * 
 */
 var filterListDelimiter = " / ";
function getSelectedFilter() {
	$("#sfilterid").html("");
	var xhrArgs = {
		url : "/esrigis/datadispatcher.do?selectedfilter=true&rnd=" + new Date().getTime(),
		handleAs : "json",
		load : function(jsonData) {
			
			$("#sfilterid").append("<i>"+translate('Currency')+"</i>: ");
			$("#sfilterid").append(jsonData[0].currency);

			$("#sfilterid").append(" <i>| "+ translate('Year Start')+"</i> : ");
			$("#sfilterid").append(jsonData[0].startyear);
			
			$("#sfilterid").append(" <i>| "+ translate('End Year')+"</i> : ");
			$("#sfilterid").append(jsonData[0].endyear);
			
			$("#sfilterid").append(" <i>| "+ translate('Locations Fitler On')+"</i> : ");
			$("#sfilterid").append(translate(jsonData[0].locationfiltered));
			
			if (jsonData[0].projectstatus != '') {
				$("#sfilterid").append(" <i>| "+ translate('Status')+"</i> : ");
				dojo.forEach(jsonData[0].projectstatus, function(projectstatus) {
					$("#sfilterid").append(projectstatus + filterListDelimiter);
				});
			}
			if (jsonData[0].sector != '') {
				$("#sfilterid").append(" <i>| "+ translate('Primary Sector')+"</i> : ");
				dojo.forEach(jsonData[0].sector, function(sector) {
					$("#sfilterid").append(sector + filterListDelimiter);
				});
			}

			if (jsonData[0].financinginstrument != '') {
				$("#sfilterid").append(" <i>| "+ translate('Financing Instrument')+"</i> : ");
				dojo.forEach(jsonData[0].financinginstrument, function(financinginstrument) {
					$("#sfilterid").append(financinginstrument + filterListDelimiter);
				});
				
			}
			if (jsonData[0].typeofassistance != '') {
				$("#sfilterid").append(" <i>| "+ translate('Type of Assistance')+"</i> : ");
				dojo.forEach(jsonData[0].typeofassistance, function(typeofassistance) {
					$("#sfilterid").append(typeofassistance + filterListDelimiter);
				});
			}

			if (jsonData[0].onbudget == true) {
				$("#sfilterid").append(
						" <i>| "+ translate('Only on budget projects')+"</i> : True");
			}

			if (jsonData[0].organizationtype != '') {
				$("#sfilterid").append(" <i>| "+ translate('Organization Type')+"</i> : ");
				dojo.forEach(jsonData[0].organizationtype, function(organizationtype) {
					$("#sfilterid").append(organizationtype + " - ");
				});
			}

			if (jsonData[0].organizationgroup != '') {
				$("#sfilterid").append(" <i>| "+ translate('Organization Group')+"</i> : ");
				$("#sfilterid").append(jsonData[0].organizationgroup);
			}

			if (jsonData[0].selecteddonors.length > 0) {
				$("#sfilterid").append(" <i>| "+ translate('Donors')+"</i> : ");
			}
			dojo.forEach(jsonData[0].selecteddonors, function(donor) {
				$("#sfilterid").append(donor.donorname + filterListDelimiter);
			});

			if (jsonData[0].structuretypes.length > 0) {
				$("#sfilterid").append(" <i>| "+ translate('Structure Types')+"</i> : ");
			}

			dojo.forEach(jsonData[0].structuretypes, function(structures) {
				$("#sfilterid").append(structures + filterListDelimiter);
			});

		},
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
	$("#selectedfilter").show();
}

/**
 * Use the clear parameter to remove all activities fron the map
 * 
 * @param clear
 */
function getActivities(clear) {
	showLoading();
	if (clear && cL) {
		cL.clear();
		
	}

	var xhrArgs = {
		url : "/esrigis/datadispatcher.do?showactivities=true&rnd=" + new Date().getTime(),
		handleAs : "json",
		load : function(jsonData) {
			// For every item we received...
			totallocations = 0;
			features = [];
			activitiesarray = [];
			dojo.forEach(jsonData, function(activity) {
				activitiesarray.push(activity);
				MapFind(activity);
			});
			if (totallocations == 0) {
				timer_on = 1;
				drawpoints();
			}
		},
		error : function(error) {
			console.log(error);
		}
	}
	// Call the asynchronous xhrGet
	var deferred = dojo.xhrGet(xhrArgs);
	
}
/**
 * 
 */

function getNationalActivities() {
	if(!nationalactive){
		showLoading();
		var xhrArgs = {
			url : "/esrigis/datadispatcher.do?shownational=true",
			handleAs : "json",
			load : function(jsonData) {
				nationalactivitiesarray=[];
				// For every item we received...
				dojo.forEach(jsonData, function(activity) {
					nationalactivitiesarray.push(activity);
				});
				togglenational();
				filldatasourcetablenational();
				hideLoading();
			},
			error : function(error) {
				console.log(error);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	}else{
		togglenational();
	}
}


/**
 * 
 * @param activity
 */
var donorArray = new Array();
function MapFind(activity) {
	showLoading();
	dojo.forEach(activity.locations,function(location) {
		// If the location has lat and lon not needs to find the
		// point in the map
		if (location.islocated == false && countrymapurl) {
			findTask = new esri.tasks.FindTask(countrymapurl);
			// create find parameters and define known values
			findParams = new esri.tasks.FindParameters();
			findParams.returnGeometry = true;
			findParams.layerIds = [ 0, 1 ];
			findParams.contains = false;
			findParams.searchFields = [ "GEO_ID" ];
			execute(location.geoId);
			totallocations++;
		}else{
			if (location.exactlocation) {
				var pt = new esri.geometry.Point(
						location.exactlocation_lon,
						location.exactlocation_lat,
						new esri.SpatialReference({"wkid" : 4326}));
			} else {
				if(location.lon != '' && location.lat != '')
					var pt = new esri.geometry.Point(location.lon,location.lat,new esri.SpatialReference({"wkid" : 4326}));
				else{
					//console.log(location.name +' '+ activity.activityname);
					return true;
				}
			}
			// Create a graphic point based on the x y
			// coordinates wkid(Well-known ID) 4326 for
			// GCS_WGS_1984 projection
			var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([ 255, 0, 0, 0.5 ]));
			var attr = {"Temp" : "Temporal Attribute"};
			var infoTemplate = new esri.InfoTemplate("");
			var pgraphic = new esri.Graphic(pt, sms, attr,infoTemplate);

			dojo.forEach(activity.sectors, function(sector) {
				primarysector = sector.sectorName;
				primarysectorschema = sector.sectorScheme;
				primarypercentage = sector.sectorPercentage;
			});

			var donorname;
			var donorCode;
			dojo.forEach(activity.donors, function(donor) {
				if (!containsDonor(donor, donorArray)) {
					donorArray.push(donor);
				}
				if (donorname == null) {
					donorname = donor.donorname;
					donorCode = donor.donorCode;
				} else {
					if (donorname!=donor.donorname){
						donorname = donorname + "," + donor.donorname;
					}
				}
			});
			pgraphic.setAttributes({
						"Location" : '<b>' + location.name
								+ '</b>',
						"Commitments for this location" : '<b>'
								+ location.commitments
								+ '</b>',
						"Disbursements for this location" : '<b>'
								+ location.disbursements
								+ '</b>',
						"Code" : '' + donorCode + '',
						"id" :activity.id,
						"name":location.name
					});
			location.isdisplayed = true;
			features.push(pgraphic);
		}
	});
}

/**
 * 
 * @param searchText
 */
function execute(searchText) {
	// set the search text to find parameters
	findParams.searchText = searchText;
	findTask.execute(findParams, showResults);
	var t = setTimeout("drawpoints()", 10000);
	timer_on = 1;
}

/**
 * 
 * @param results
 */
function showResults(results) {
	// find results return an array of findResult.
	dojo.forEach(results,function(result) {
		var graphic = result.feature;
		// console.log("Found : " + result.layerName + "," +
		// result.foundFieldName + "," + result.value);
		var point = graphic.geometry.getExtent().getCenter();
		var sms = new esri.symbol.SimpleMarkerSymbol()
				.setStyle(
						esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE)
				.setColor(new dojo.Color([ 255, 0, 0, 0.5 ]));
		var attr = {
			"Location" : result.value
		};
		var infoTemplate = new esri.InfoTemplate("");
		var pgraphic = new esri.Graphic(point, sms, attr,
				infoTemplate);

		// Iterate over the activities array and assign the
		// attributes to each point
		var exit = false;
		for ( var int = 0; int < activitiesarray.length; int++) {
			var activity = activitiesarray[int];
			for ( var int2 = 0; int2 < activitiesarray[int].locations.length; int2++) {
				var loc = activitiesarray[int].locations[int2];
				if (loc.name == result.value
						&& loc.isdisplayed != true) {
					pgraphic.setAttributes({
						"Activity" : activity.activityname,
						"Location" : loc.name
					});
					loc.isdisplayed = true;
					exit = true;
				}
			}
			if (exit == true) {
				break;
			}
		}
		features.push(pgraphic);
		totallocations--;

		if (totallocations == 0) {
			drawpoints();
		}
	});
}

/**
 * 
 */
function drawpoints() {
	if (timer_on) {
		if (cL != null) {
			cL._features = [];
			cL.levelPointTileSpace = [];
		}
		// Create palette for individual donors
		var pointSymbolBank = new Array();
		var colorsused = 0;
		for ( var i = 0; i < donorArray.length; i++) {
			var pointObject;
			var acronym = donorArray[i].donorCode.latinise();
			   if(colorsused < colorsCualitative.length && ($.inArray(acronym, fixeddonorlist)>=0 || fixeddonorlist.length==1)){
				   pointObject = new esri.symbol.SimpleMarkerSymbol(
				   esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 15,
			       new esri.symbol.SimpleLineSymbol(
			       esri.symbol.SimpleLineSymbol.STYLE_SOLID,colorsCualitative[colorsused], 1), colorsCualitative[colorsused]);
				   colorsused++;
			   }else{
				  pointObject = new esri.symbol.SimpleMarkerSymbol(
			      esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 15,
			      new esri.symbol.SimpleLineSymbol(
			      esri.symbol.SimpleLineSymbol.STYLE_SOLID,
			      colorsCualitative[i], 1), new dojo.Color([255, 255, 255, 1]));
			   }
			   
			   pointSymbolBank[donorArray[i].donorCode] = pointObject;
		}
		
		
		// Add standard symbols
		pointSymbolBank["single"] = new esri.symbol.SimpleMarkerSymbol(
				esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 10,
				new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_SOLID,
						new dojo.Color([ 0, 0, 0, 1 ]), 1), new dojo.Color([
						255, 215, 0, 1 ]));

		pointSymbolBank["less16"] = new esri.symbol.SimpleMarkerSymbol(
				esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 20,
				new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_SOLID,
						new dojo.Color([ 0, 0, 0, 1 ]), 1), new dojo.Color([
						9, 75, 5, 1 ]));
		pointSymbolBank["less30"] = new esri.symbol.SimpleMarkerSymbol(
				esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 30,
				new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_NULL,
						new dojo.Color([ 0, 0, 0, 0 ]), 1), new dojo.Color([
						74, 9, 33, .85 ]));
		pointSymbolBank["less50"] = new esri.symbol.SimpleMarkerSymbol(
				esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 30,
				new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_NULL,
						new dojo.Color([ 0, 0, 0, 0 ]), 1), new dojo.Color([
						8, 2, 63, .90 ]));
		pointSymbolBank["over50"] = new esri.symbol.SimpleMarkerSymbol(
				esri.symbol.SimpleMarkerSymbol.STYLE_CIRCLE, 45,
				new esri.symbol.SimpleLineSymbol(
						esri.symbol.SimpleLineSymbol.STYLE_NULL,
						new dojo.Color([ 0, 0, 0 ]), 0), new dojo.Color([ 45,
						45, 40, 0.65 ]));
		var symbolBank = pointSymbolBank;
		
		if (map.getLayer('activitiesMap')){
			map.removeLayer(cL);
			cL=null;
		}
		cL = new esri.ux.layers.ClusterLayer(
				{
					displayOnPan : false,
					map : map,
					type:1,
					symbolBank : symbolBank,
					id : "activitiesMap",
					features : features,
					infoWindow :{
						template :
							new esri.InfoTemplate(
								translate('Activity Details'),
								"<table style='font-size: 11px;'>"
										+ "<tr><td><b>"+translate('Activity')+"<b></td><td> ${Activity}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Donors')+"<b></td><td>${Donors}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Location')+"<b></td><td>${Location}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Primary Sector')+"<b></td><td>${Primary Sector}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Total commitments')+"<b></td><td>${Total commitments}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Total disbursements')+"<b></td><td>${Total disbursements}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Commitments for this location')+"<b></td><td>${Commitments for this location}</td></tr>"
										+ "<tr><td nowrap><b>"+translate('Disbursements for this location')+"<b></td><td>${Disbursements for this location}</td></tr></table>"),
						width : 400,
						height : 260
					},
					flareLimit : 20,
					flareDistanceFromCenter : 30
				});
		map.addLayer(cL);
		showLegendClusterDonor(pointSymbolBank);
		timer_on = 0;
	}
}

var locations = new Array();
var implementationLevel = [ {
	name : "Region",
	mapId : "0",
	mapField : ""
}, {
	name : "Zone",
	mapId : "1",
	mapField : ""
} ];

function getHighlights(level) {
	if (highlightson){
		closeHide("highlightLegend");
		highlightson =false;
	}else{
		showLoading();
		implementationLevel[0].mapField = COUNTY;
		implementationLevel[1].mapField = DISTRICT;
		locations = new Array();
		closeHide("highlightLegend");
		$('#hlight').attr('onclick', '').unbind('click');
		$('#hlightz').attr('onclick', '').unbind('click');
	
		var xhrArgs = {
			url : "/esrigis/datadispatcher.do?showhighlights=true&level="
					+ implementationLevel[level].name,
			handleAs : "json",
			load : function(jsonData) {
				// For every item we received...
				dojo.forEach(jsonData, function(location) {
					locations.push(location);
				});
				MapFindLocation(implementationLevel[level]);
			},
			error : function(error) {
				// Error handler
			}
		};
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
		highlightson = true;
	}
}

var currentLevel;
function MapFindLocation(level) {
	showLoading();
	var queryTask = new esri.tasks.QueryTask(countrymapurl + '/' + level.mapId);
	var query = new esri.tasks.Query();
	query.where = level.mapField + " <> ''";
	query.outSpatialReference = {
		wkid : map.spatialReference.wkid
	};
	query.returnGeometry = true;
	query.outFields = [ COUNT, level.mapField, GEO_ID ];
	currentLevel = level;
	queryTask.execute(query, addResultsToMap);
}

/**
 * 
 * @param featureSet
 */
function addResultsToMap(featureSet) {
	var border = new esri.symbol.SimpleLineSymbol(
			esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([ 150,
					150, 150 ]), 1);
	var symbol = new esri.symbol.SimpleFillSymbol(
			esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, new dojo.Color([
					150, 150, 150, 0.5 ]));
	var colors = colorsOrange;
	var numRanges = colors.length;
	var localGraphicLayer = esri.layers.GraphicsLayer({
		displayOnPan : true,
		id : "highlightMap",
		visible : true
	});
	var typeFundingValue = getCheckedValue(document
			.getElementsByName("filter.transactionType"));
	var typeFunding = "commitments";
	switch (typeFundingValue) {
	case "0":
		typeFunding = "commitments";
		break;
	case "1":
		typeFunding = "disbursements";
		break;
	case "2":
		typeFunding = "expenditures";
		break;
	}

	// Using logarithmic scale
	var maxLog = Math.log(getMaxValue(locations, typeFunding));
	var minLog = Math.log(getMinValue(locations, typeFunding));

	var max = getMaxValue(locations, typeFunding);
	var min = getMinValue(locations, typeFunding);

	var breaksLog = (maxLog - minLog) / numRanges;
	var breaks = (max - min) / numRanges;

	var rangeColors = new Array();
	var renderer = new esri.renderer.ClassBreaksRenderer(symbol, COUNT);
	for ( var i = 0; i < numRanges; i++) {
		rangeColors.push([ parseFloat(min + (i * breaks)),
				parseFloat(min + ((i + 1) * breaks)) ]);
		renderer.addBreak(parseFloat(min + (i * breaks)), parseFloat(min
				+ ((i+2) * breaks)), new esri.symbol.SimpleFillSymbol(
				esri.symbol.SimpleFillSymbol.STYLE_SOLID, border, colors[i]));
	}

	dojo.forEach(featureSet.features, function(feature) {
		// Read current attributes and assign a new set
		var count = feature.attributes[COUNT];
		var county = feature.attributes[COUNTY];
		var district = feature.attributes[DISTRICT];
		var geoId = feature.attributes[GEO_ID];
		var names=new Array();
			names[0]=[county];
			names[1]=[district];
		
		feature.setAttributes({
			"COUNT" : count,
			"COMMITMENTSFMT" : 0,
			"DISBURSEMENTSFMT" : 0,
			"EXPENDITURESFMT" : 0,
			"COUNTY" : county,
			"DISTRICT" : district,
			"GEO_ID" : geoId,
			"NAME" : names[currentLevel.mapId]
		});
		feature.setInfoTemplate(new esri.InfoTemplate(translate('Funding'),
						translate(currentLevel.name) + ": ${NAME} <br/>"
						+ translate('Commitments')+": ${COMMITMENTSFMT}<br/> "
						+ translate('Disbursements')+": ${DISBURSEMENTSFMT}<br/> "
						+ translate('Expenditures')+": ${EXPENDITURESFMT}<br/> "

		));
		localGraphicLayer.add(feature);
	});

	localGraphicLayer = updateLocationAttributes(localGraphicLayer, typeFunding);
	localGraphicLayer.setRenderer(renderer);
	map.addLayer(localGraphicLayer);
	map.reorderLayer(map.getLayer("highlightMap"), 0);
	map.setExtent(map.extent.expand(1.01));
	hideLoading();
	var currencyCode;
	for ( var j = 0; j < locations.length; j++) {
		var currentLocation = locations[j];
		if (currentLocation.amountsCurrencyCode != "") {
			currencyCode = currentLocation.amountsCurrencyCode;
			break;
		}
	}

	showLegend(rangeColors, colors, typeFunding, currencyCode);
	$('#hlight').bind('click', function() {
		getHighlights(0);
	});
	$('#hlightz').bind('click', function() {
		getHighlights(1);
	});
}

function generate_colors_styling(color)
{
	if (dojo.isIE <= 8)
		return "style='background-color:rgb("+ color.toRgb() + "); -ms-filter:\"progid:DXImageTransform.Microsoft.Alpha(Opacity=" + Math.round(100 * color.a) + ")\";'"; // degraded view in IE<=8: no alpha
	
	return "style='background-color:rgba("+ color.toRgba() + ");'";
}


/**
 * 
 * @param rangeColors
 * @param colors
 */
function showLegend(rangeColors, colors, typeFunding, currencyCode) {
	var df = new DecimalFormat(currentFormat);
	var currencyString = currencyCode;
	//debugger;
	var htmlDiv = "";
	htmlDiv += "<div onclick='closeHide(\"highlightLegend\")' style='color:white;float:right;cursor:pointer;'>X</div>";
	htmlDiv += "<div class='legendHeader'>"+translate('Showing ' + typeFunding + ' for ' + currentLevel.name);
	htmlDiv +=  "<br/><hr/></div>";
	for ( var i = 0; i < rangeColors.length; i++) {
		htmlDiv += "<div class='legendContentContainer'>"
				+ "<div class='legendContentValue' " + generate_colors_styling(colors[i]) + "></div>" + "</div>"
				+ "<div class='legendContentLabel'>"
				+ df.format(Math.ceil(rangeColors[i][0])) + " "
				+ currencyString + " - "
				+ df.format(Math.floor(rangeColors[i][1])) + " "
				+ currencyString + " </div><br/>";
	}
	htmlDiv += "<div class='legendContentContainer'>"
			+ "<div class='legendContentValue' " + generate_colors_styling(new dojo.Color({r:201,g:195,b:197,a:0.8})) + "></div>"
			+ "</div>" + "<div class='legendContentLabel'>No Data</div><br/>";

	$('#highlightLegend').html(htmlDiv);
	$('#highlightLegend').show('slow');
	var dnd = new dojo.dnd.Moveable(dojo.byId("highlightLegend"));
}

/**
 * 
 * @param graphicLayer
 * @returns
 */
function updateLocationAttributes(graphicLayer, typeFunding) {
	var df = new DecimalFormat(currentFormat);
	var count = graphicLayer.graphics.length;
	for ( var i = 0; i < count; i++) {
		var g = graphicLayer.graphics[i];
		for ( var j = 0; j < locations.length; j++) {
			var currentLocation = locations[j];
			if (g.attributes["GEO_ID"] == currentLocation.geoId) {

				g.attributes[COUNT] = currentLocation[typeFunding];
				g.attributes["COMMITMENTSFMT"] = df
						.format(currentLocation.commitments)
						+ " " + currentLocation.amountsCurrencyCode;
				g.attributes["DISBURSEMENTSFMT"] = df
						.format(currentLocation.disbursements)
						+ " " + currentLocation.amountsCurrencyCode;
				g.attributes["EXPENDITURESFMT"] = df
						.format(currentLocation.expenditures)
						+ " " + currentLocation.amountsCurrencyCode;
				break;
			}
		}
	}
	return graphicLayer;
}

/**
 * 
 * @param clear
 */
function getStructures(clear) {
	if (clear) {
		try {
			structureGraphicLayer = null;
			structures = [];
			structurespoint=[];
			map.removeLayer(map.getLayer("structuresMap"));
		} catch (e) {
			console.log(e);
		}
	}
	if (structureGraphicLayer) {
		if (structureGraphicLayer.visible) {
			structureGraphicLayer.hide();
			cLs.hide();
			$('#structuresdiv').hide('slow');
			structureVisible=false;
		} else {
			structureGraphicLayer.show();
			cLs.show();
			map.infoWindow.resize(400, 250);
			$('#structuresdiv').show('slow');
			structureVisible=true;
		}
	} else {
		structureGraphicLayer = esri.layers.GraphicsLayer({
			displayOnPan : false,
			id : "structuresMap",
			visible : structureVisible
		});
		
		structureson = true;
		var xhrArgs = {
			url : "/esrigis/datadispatcher.do?showstructures=true&rnd=" + new Date().getTime(),
			handleAs : "json",
			load : function(jsonData) {
				dojo.forEach(jsonData, function(activity) {
					MapFindStructure(activity, structureGraphicLayer);
				});
				
				if (structurespoint.length>0){
					map.addLayer(structureGraphicLayer);
					map.setExtent(map.extent.expand(1.01));
					// Show Structures link
					dojo.style('structures', { 'display' : 'block' });
					if (dojo.byId('search')){
						dojo.style('search', { 'display' : 'block' });
					}
					CluterStructures();
					
				}
				
			},
			error : function(error) {
				console.log(error);
			}
			
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	}
}
function changeTabStructureInfo(info){
	//alert('Marinela!');
	if(info){
		$("#strImage").css("display", "none");
		$("#strInfo").css("display", "block");
//		$("#aStrImage").parent().css('visibility', 'visible');
//		$("#aStrInfo").parent().css('visibility', 'hidden');
		$("#aStrImage").css('display', 'inline-block');
		$("#aStrInfo").css('display', 'none');		
		$("#aStrInfo").addClass("selected");
		$("#aStrImage").removeClass("selected");
	}else{
		$("#strInfo").css("display", "none");
		$("#strImage").css("display", "block");
//		$("#aStrImage").parent().css('visibility', 'hidden');
//		$("#aStrInfo").parent().css('visibility', 'visible');
		
		$("#aStrImage").css('display', 'none');
		$("#aStrInfo").css('display', 'inline-block');		
		
		$("#aStrImage").addClass("selected");
		$("#aStrInfo").removeClass("selected");
	}
}

/**
 * 
 * @param activity
 * @param structureGraphicLayer
 */
function MapFindStructure(activity, structureGraphicLayer) {
	
	
	var noTabTemplate =	"<table style='font-size: 11px;'>"
		+ "<tr><td style='padding-right:20px;'><b>Name<b></td><td><b>${Structure Name}</b></td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>"+translate('Activity')+"<b></td><td style='margin-right:5px;'>${Activity}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Type<b></td><td>${Structure Type}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Description<b></td><td>${Structure Description}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Coordinates<b></td><td>${Coordinates}</td></tr></table>";

	var tabTemplate = "<div id='infotabs' class='infotab'>"

		+ "<a class='tab_vis_link' id='aStrInfo' href='#' onclick='changeTabStructureInfo(true);' style='display: none;'>Show Info</a>"
		+ "<a class='tab_vis_link' id='aStrImage' href='#' onclick='changeTabStructureInfo(false);'>Show Image</a>"
		
		+ "<div id='strInfo' ><br /><br />"+ noTabTemplate +"</div>"
		+ "<div id='strImage' align='center' style='display:none;' >${Structure Image}</div>"
		+ "</div>";
		
	var stinfoTemplate = null;
	
var structureID=1;
	dojo.forEach(activity.structures,function(structure) {
			var sms = new esri.symbol.PictureMarkerSymbol('/esrigis/structureTypeManager.do~action=displayIcon~id='+ structure.typeId, 21, 25);
			var pgraphic;
			description =  structure.description.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
			name =  structure.name.replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
			
			var previewActivityUrl ="/aim/viewActivityPreview.do~pageId=2~isPreview=1~activityId=" + activity.ampactivityid;
			//debugger;
			if(structure.hasImage){
				stinfoTemplate = new esri.InfoTemplate(
						"Structure Details",
						tabTemplate);
			}else{
				stinfoTemplate = new esri.InfoTemplate(
						"Structure Details",
						"<div class='infotab'>" + noTabTemplate + "</div>");
			}
			
			if (structure.shape == "") {
				var pt = new esri.geometry.Point(structure.lon,structure.lat, map.spatialReference);
				var transpt = esri.geometry.geographicToWebMercator(pt);
				var attr = {
					"Temp" : "Temporal Attribute"
				};
				pgraphic = new esri.Graphic(transpt, sms, attr,stinfoTemplate);
				pgraphic.setAttributes({
						"Structure Name" : structure.name,
						"Activity" : '<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+ activity.ampactivityid
									+ '~isPreview=1" target="_blank">'+ activity.activityname+ '</a>',
						"Structure Type" : structure.type,
						"Structure Description" : description,
						"Coordinates" : structure.lat + " , " + structure.lon,
						"Type_id" : structure.typeId,
						"Id":structureID++,
						"Structure Image" : '<a target="_blank" href="'+previewActivityUrl+'">' 
						+ '<img style="cursor:pointer;" src="/aim/displayStructureImage.do?structureId=' 
							+ structure.id + '" border="0"></a>'
						
					});
				structurespoint.push(pgraphic);
				//structures.push(pgraphic);
				
				
			} else {
				var jsonObject = eval('(' + structure.shape + ')');
				if (jsonObject.geometry != null) { 
					// If it's a complete Graphic object
					pgraphic = new esri.Graphic(jsonObject);
					pgraphic.setAttributes({
						"Structure Name" : name,
						"Structure Type" : structure.type,
						"Activity" : '<a href="/aim/viewActivityPreview.do~pageId=2~activityId='+ activity.ampactivityid
								+ '~isPreview=1" target="_blank">'+ activity.activityname+ '</a>',
						"Coordinates" : pgraphic.geometry.x+ " , "+ pgraphic.geometry.y,
                        "Structure Description" : description,
						"Structure Image" : '<a target="_blank" href="'+previewActivityUrl+'">' 
						+ '<img style="cursor:pointer;" src="/aim/displayStructureImage.do?structureId=' 
							+ structure.id + '" border="0"></a>'
					});
					pgraphic.setInfoTemplate(stinfoTemplate);
					// If it's a point, put the  appropriate  icon
					if (jsonObject.symbol.style == "esriSMSCircle"){
						pgraphic.setSymbol(sms);
					}
	
				} else {
					pt = new esri.geometry.Point(jsonObject);
					var infoTemplate = stinfoTemplate;
					var attr = {
						"Temp" : "Temporal Attribute"
					};
					pgraphic = new esri.Graphic(pt, sms, attr,infoTemplate);
					pgraphic.setAttributes({
						"Structure Name" : name,
						"Structure Type" : structure.type,
						"Activity" : '<a href="/aim/viewActivityPreview.do~pageId=2~activityId='
							+ activity.ampactivityid+ '~isPreview=1" target="_blank">'
							+ activity.activityname+ '</a>',
						"Coordinates" : pgraphic.geometry.x+ " , "+ pgraphic.geometry.y,
                        "Structure Description" : description,
						"Structure Image" : '<a target="_blank" href="'+previewActivityUrl+'">' 
						+ '<img style="cursor:pointer;" src="/aim/displayStructureImage.do?structureId=' 
							+ structure.id + '" border="0"></a>'
					});
	
				}
				structureGraphicLayer.add(pgraphic);
				structures.push(pgraphic);
			}
		});
}


function CluterStructures(){
	var pointSymbolBank = new Array();
	// Add standard symbols
	pointSymbolBank["single"] = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 20,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID,
					new dojo.Color([ 0, 0, 0, 1 ]), 1), new dojo.Color([
					204,102,102, 1 ]));

	pointSymbolBank["less16"] = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 25,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_SOLID,
					new dojo.Color([ 0, 0, 0, 1 ]), .5), new dojo.Color([204,102,102,  1 ]));
	pointSymbolBank["less30"] = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 30,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_NULL,
					new dojo.Color([ 0, 0, 0, 0 ]), 1), new dojo.Color([204,102,102,  .85 ]));
	pointSymbolBank["less50"] = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 35,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_NULL,
					new dojo.Color([ 0, 0, 0, 0 ]), 1), new dojo.Color([204,102,102,  .80 ]));
	pointSymbolBank["over50"] = new esri.symbol.SimpleMarkerSymbol(
			esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE, 40,
			new esri.symbol.SimpleLineSymbol(
					esri.symbol.SimpleLineSymbol.STYLE_NULL,
					new dojo.Color([ 0, 0, 0 ]), 0), new dojo.Color([ 204,102,102, .70  ]));
	var symbolBank = pointSymbolBank;
	
	if (map.getLayer('structurescluster')){
		map.removeLayer(cLs);
		cLs=null;
	}
	var infoTemplate =	"<table style='font-size: 11px;'>"
		+ "<tr><td style='padding-right:20px;'><b>Name<b></td><td><b>${Structure Name}</b></td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>"+translate('Activity')+"<b></td><td style='margin-right:5px;'>${Activity}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Type<b></td><td>${Structure Type}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Description<b></td><td>${Structure Description}</td></tr>"
		+ "<tr><td nowrap style='padding-right:20px;'><b>Coordinates<b></td><td>${Coordinates}</td></tr></table>";

	var tabTemplate = "<div id='infotabs'>"
		
		+ "<a class='tab_vis_link' id='aStrInfo' href='#' onclick='changeTabStructureInfo(true);' style='display: none;'>Show Info</a>"
		+ "<a class='tab_vis_link' id='aStrImage' href='#' onclick='changeTabStructureInfo(false);'>Show Image</a>"
		
		+ "<div id='strInfo'>"+ infoTemplate +"</div>"
		+ "<div id='strImage'>${Structure Image}</div>"
		+ "</div>";
		
	//debugger;
	if (structurespoint.length > 0){
	cLs = new esri.ux.layers.AmpCluster(
			{
				displayOnPan : false,
				map : map,
				type:2,
				symbolBank : symbolBank,
				id : "structurescluster",
				visible : structureVisible,
				features : structurespoint,
				onClusterExpand:function(){
					$("#clusterStructures").show();
				},
				infoWindow :{
					template :
						 new esri.InfoTemplate(
						"Structure Details",
						tabTemplate),
					width : 250,
					height : 250
				},
				flareLimit : 20,
				flareDistanceFromCenter : 30
			});
	map.addLayer(cLs);
}
}

function resetStructureCluster(){
	cLs.resetCluster();
	$("#clusterStructures").hide();
}





function structurestorequest() {
	var stjson = "{\"Structures\":[";

	for ( var int = 0; int < foundstr.length; int++) {
		if (stjson == "{\"Structures\":[") {
			stjson += JSON.stringify(foundstr[int].attributes);
		} else {
			stjson += "," + JSON.stringify(foundstr[int].attributes);
		}
	}
	stjson += "]}";
	return stjson;
}

/**
 * Section for adding activities
 */

var addActivityMode = false;
function addActivity() {
	addActivityMode = true;
}

/**
 * 
 * @param evt
 */
function selectionFunction(evt) {
	if (addActivityMode || searchactive) {
		var tooltipHolder = dojo.byId("tooltipHolder");
		tooltipHolder.innerHTML = translate('Select a point');
		tooltipHolder.style.display = "block";
		tooltipHolder.style.top = evt.pageY + 5 + "px";
		tooltipHolder.style.left = evt.pageX + 5 + "px";
	}
}

/**
 * 
 * @param results
 */
function showAddActivityInfoWindow(results) {
	hideTooltip();

	addActivityMode = false;
	var county = results.features[0].attributes[COUNTY];
	var district = results.features[0].attributes[DISTRICT];
	var geoId = results.features[0].attributes[GEO_ID];
	var content = "<table border='0' width='100%'>"
			+ "<tr>"
			+ "<td align='center' width='200px'><digi:trn>Name</digi:trn></td>"
			+ "<td align='center' width='100px'><input id=\"activityName\" type=\"text\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td align='center' width='200px'><digi:trn>Location</digi:trn></td>"
			+ "<td align='center' width='100px'><input type=\"text\" READONLY value=\""
			+ county
			+ ", "
			+ district
			+ "\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td align='center' width='200px'><digi:trn>Geo Id</digi:trn></td>"
			+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"geoId\" value=\""
			+ geoId
			+ "\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td align='center' width='200px'><digi:trn>Latitude</digi:trn></td>"
			+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"lat\" value=\""
			+ esri.geometry.webMercatorToGeographic(searchpoint.mapPoint).y
			+ "\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td align='center' width='200px'><digi:trn>Longitude</digi:trn></td>"
			+ "<td align='center' width='100px'><input type=\"text\" READONLY id=\"lon\" value=\""
			+ esri.geometry.webMercatorToGeographic(searchpoint.mapPoint).x
			+ "\"></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td align='right' width='300px'>"
			+ "<input type=\"button\" value=\"Create new activity\" onclick=\"submitActivity()\"></td>"
			+ "<td align='left' width='300px'><input type=\"button\" value=\"Cancel\" onclick=\"map.infoWindow.hide()\"></td>"
			+ "</tr>";
	if (map.infoWindow.isShowing) {
		map.infoWindow.hide();
	}
	map.infoWindow.setTitle("<digi:trn>Add new activity</digi:trn>");
	content = content + "</table>";
	map.infoWindow.setContent(content);
	map.infoWindow.resize(400, 300);
	map.infoWindow.show(searchpoint.screenPoint, map
			.getInfoWindowAnchor(searchpoint.screenPoint));
	// dojo.connect(map.infoWindow, "onHide", clearbuffer);
	hideLoading();
}

/**
 * 
 * @param level
 * @param evt
 */
function MapFindPoint(level, evt) {
	searchpoint = evt;
	showLoading();
	var queryTask = new esri.tasks.QueryTask(countrymapurl + '/' + level.mapId);
	var query = new esri.tasks.Query();
	query.geometry = evt.mapPoint;
	query.outSpatialReference = {
		wkid : map.spatialReference.wkid
	};
	query.returnGeometry = false;
	query.outFields = [ COUNTY, DISTRICT, GEO_ID ];
	queryTask.execute(query, showAddActivityInfoWindow);
}

/**
 * 
 */
function submitActivity() {
	var name, lat, lon, geoid;
	name = dojo.byId("activityName");
	lat = dojo.byId("lat");
	lon = dojo.byId("lon");
	geoId = dojo.byId("geoId");
	// Removing district, problem with Locations
	geoIdShort = geoId.value.substring(0, 4);
	window.open("/wicket/onepager/activity/new/name/" + name.value + "/lat/"
			+ lat.value + "/lon/" + lon.value + "/geoId/" + geoIdShort);

}

function showLegendClusterDonor(pointSymbolBank) {
	var htmlDiv = "";
	htmlDiv += "<div onclick=\"$('#legendcontainer').toggle('slow');\" style='color:white;float:right;cursor:pointer;'><img src='/TEMPLATE/ampTemplate/img_2/gis/minimize.gif'></div>";
	htmlDiv += "<div class='legendHeader'>";
	htmlDiv += translate('Point color reference');
	htmlDiv += "<br/><hr/></div><div id='legendcontainer'>";
	if (donorArray.length < 10) {
		for ( var i = 0; i < donorArray.length; i++) {
			htmlDiv += "<div class='legendContentContainer'>"
					+ "<div class='legendContentValue' " + generate_colors_styling(pointSymbolBank[donorArray[i].donorCode].color)
					+ "></div>" + "</div>"
					+ "<div class='legendContentLabel' title='"+donorArray[i].donorname+"'>"
					+ donorArray[i].donorCode + " </div><br/>";
		}
	} else if(fixeddonorlist.length>1){
		for ( var int = 0; int < fixeddonorlist.length; int++) {
			for ( var int2 = 0; int2 < donorArray.length; int2++) {
				var acronym =donorArray[int2].donorCode.latinise();
				if (acronym==fixeddonorlist[int]){
					htmlDiv += "<div class='legendContentContainer'>"
						+ "<div class='legendContentValue' " + generate_colors_styling(pointSymbolBank[donorArray[int2].donorCode].color)
						+ "></div>" + "</div>"
						+ "<div class='legendContentLabel' title='"+donorArray[int2].donorname+"'>"
						+ donorArray[int2].donorCode + " </div><br/>";
				}
			}
		}
	}else{
		for ( var i = 0; i < 10; i++) {
			htmlDiv += "<div class='legendContentContainer'>"
					+ "<div class='legendContentValue' " + generate_colors_styling(pointSymbolBank[donorArray[i].donorCode].color)
					+ "></div>" + "</div>"
					+ "<div class='legendContentLabel' title='"+donorArray[i].donorname+"'>"
					+ donorArray[i].donorCode + " </div><br/>";
		}
	}
	
	htmlDiv += "<div class='legendContentContainer'>"
		+ "<div class='legendContentValue' style='background-color:rgb(255,255,255);'></div>" 
		+"</div>"
		+ "<div class='legendContentLabel' title='"+translate('Others')+"'>"
		+ translate('Others') + " </div><br/>";
	
	htmlDiv += "</div>";
	$('#pointsLegend').html(htmlDiv);
	$('#pointsLegend').show('slow');
	var dnd = new dojo.dnd.Moveable(dojo.byId("pointsLegend"));
}



var results = new Array();
function sendText(value){
		var xhrArgs = {
			url : "/esrigis/datadispatcher.do?getmedia=true&searchtext="+value,
			handleAs : "json",
			sync : true,
			load : function(jsonData) {
				dojo.forEach(jsonData.response.datalayer.locations, function(location) {
					results.push(location);
				});
				placemedia();
			},
			error : function(error) {
				console.log(error);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
}
	
function placemedia(){
	for ( var int = 0; int < results.length; int++) {
		var pt = new esri.geometry.Point(results[int].latitude,results[int].longitude,map.spatialReference);
		var sms = new esri.symbol.SimpleMarkerSymbol().setStyle(esri.symbol.SimpleMarkerSymbol.STYLE_SQUARE).setColor(new dojo.Color([ 255, 0, 0, 0.5 ]));
		var attr = {"Temp" : "Temporal Attribute"};
		var infoTemplate = new esri.InfoTemplate("");
		var pgraphic = new esri.Graphic(pt, sms, attr,infoTemplate);
		map.graphics.add(pgraphic);
	}
}


//Get info windows content

function getContent(graphicAttributes, baseGraphic) {
    var attributes;

    var xhrArgs = {
    	url : "/esrigis/datadispatcher.do?getcontent=true&id="+graphicAttributes.id+"&name="+graphicAttributes.name,
        handleAs : "json",
        sync : true,
        load : function(attr) {
                dojo.forEach(attr[0].sectors, function(sector) {
    				primarysector = sector.sectorName;
    				primarysectorschema = sector.sectorScheme;
    				primarypercentage = sector.sectorPercentage;
    			});
                var donorname;
    			var donorCode;
    			dojo.forEach(attr[0].donors, function(donor) {
    				if (donorname == null) {
    					donorname = donor.donorname;
    					donorCode = donor.donorCode;
    				} else {
    					if (donorname!=donor.donorname){
    						donorname = donorname + "," + donor.donorname;
    					}
    				}
    			});
                attr=({
					"Activity" : '<a href="/aim/viewActivityPreview.do~pageId=2~activityId='
							+ attr[0].id
							+ '~isPreview=1" target="_blank">'
							+ attr[0].activityname
							+ '</a>',
					"Donors" : '<b>' + donorname + '</b>',
					"Location" : '<b>' + graphicAttributes.Location
							+ '</b>',
					"Primary Sector" : '<b>'
							+ primarysector + '</b>',
					"Total commitments" : '<b>'
							+ attr[0].commitments + ' '
							+ attr[0].currecycode + '</b>',
					"Total disbursements" : '<b>'
							+ attr[0].disbursements + ' '
							+ attr[0].currecycode + '</b>',
					"Commitments for this location" : '<b>'
							+ attr[0].commitmentsforlocation
							//+ graphicAttributes["Commitments for this location"]+ ' '
							+ ' '
							+ attr[0].currecycode + '</b>',
					"Disbursements for this location" : '<b>'
							+ attr[0].disbursementsforlocation
							//+ graphicAttributes["Disbursements for this location"]
							+ ' '
							+ attr[0].currecycode + '</b>',
					"Code" : '' + donorCode 
				});
            if (baseGraphic) {
                attr.baseGraphic = baseGraphic;
                attributes = attr;
            } else {
                attributes = attr;
            }

        },
        error : function(error) {
            console.log(error);
        }

    }
    var deferred = dojo.xhrGet(xhrArgs);
    return attributes;
}




