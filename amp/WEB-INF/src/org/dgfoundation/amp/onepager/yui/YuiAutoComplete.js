YAHOO.widget.WicketDataSource = function(callbackUrl) {
    this.callbackUrl = callbackUrl;
    this.responseArray = [];
    this.transactionId = 0;
};

YAHOO.widget.WicketDataSource.prototype = new YAHOO.util.LocalDataSource();

YAHOO.widget.WicketDataSource.prototype.makeConnection = function(oRequest, oCallback, oCaller) {
    var tId = this.transactionId++;
    this.fireEvent("requestEvent", {tId: tId, request: oRequest, callback: oCallback, caller: oCaller});
    var _this = this;
    var onWicketSuccessFn = function() {
        _this.handleResponse(oRequest, _this.responseArray, oCallback, oCaller, tId);
    };    
    wicketAjaxGet(this.callbackUrl + '&q=' + oRequest, onWicketSuccessFn);
};

YAHOO.widget.WicketAutoComplete = function(inputId, callbackUrl, containerId, toggleButtonId) {
    this.dataSource = new YAHOO.widget.WicketDataSource(callbackUrl);
    this.autoComplete = new YAHOO.widget.AutoComplete(inputId, containerId, this.dataSource);
    this.autoComplete.prehighlightClassName = "yui-ac-prehighlight";
    this.autoComplete.useShadow = true;
    this.autoComplete.animVert=.01;
    this.autoComplete.animHoriz=.01;
    this.autoComplete.animSpeed=0.5;
    this.autoComplete.minQueryLength=0;
    this.autoComplete.queryDelay=0;
    this.autoComplete.forceSelection=true;
    this.autoComplete.maxResultsDisplayed = 1000;
    this.autoComplete.formatResult = function(oResultData, sQuery, sResultMatch) {
        return oResultData;
    };
    
    var autoComplete=this.autoComplete;
  
    //handler for selected items
    this.itemSelectHandler = function(sType, aArgs) {
//    	YAHOO.log(sType); // this is a string representing the event;
//    				      // e.g., "itemSelectEvent"
//    	var oMyAcInstance = aArgs[0]; // your AutoComplete instance
//    	var elListItem = aArgs[1]; // the <li> element selected in the suggestion container
    	var oData = aArgs[2]; // object literal of data for the result
    	callWicket(oData); //calls wicket behavior with the selected item
    };
    this.autoComplete.itemSelectEvent.subscribe(this.itemSelectHandler);//subscribes this handler to YUI autocomplete

    // Breakfast combobox
    var bToggler = YAHOO.util.Dom.get(toggleButtonId);
    var oPushButtonB = new YAHOO.widget.Button({container:bToggler});
    var toggleB = function(e) {
        //YAHOO.util.Event.stopEvent(e);
        if(!YAHOO.util.Dom.hasClass(bToggler, "open")) {
            YAHOO.util.Dom.addClass(bToggler, "open");
        }
        
        // Is open
        if(autoComplete.isContainerOpen()) {
        	autoComplete.collapseContainer();
        }
        // Is closed
        else {
        	autoComplete.getInputEl().focus(); // Needed to keep widget active
            setTimeout(function() { // For IE
            	autoComplete.sendQuery("");
            },0);
        }
    };
    oPushButtonB.on("click", toggleB);
    autoComplete.containerCollapseEvent.subscribe(function(){YAHOO.util.Dom.removeClass(bToggler, "open");});

};

