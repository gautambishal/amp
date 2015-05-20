var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var StateLoadError = require('amp-state/index').StateLoadError;

var Header = require('./header');
var Controls = require('./controls');
var ChartsView = require('./charts');
var Charts = require('../models/charts-collection');
var Footer = require('./footer');

var TopsChart = require('../models/chart-tops');
var PredictabilityChart = require('../models/chart-aid-predictability');
var FundingTypeChart = require('../models/chart-funding-type');

var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));
var modalTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/modal.html', 'UTF-8'));

var EnabledChartsCollection = require('../models/enabled-charts-collection');

module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;    

    this.header = new Header({ app: this.app });    
    this.app.settings.load();  // maybe should go in render or something
                               // but we already do other fetches on init so...
    this.controls = new Controls({ app: this.app });

    // AMP-19545: We instantiate the collection of enabled charts (from FM) and use it to enable or not each chart.
    var enabledChartsFM = new EnabledChartsCollection();
    enabledChartsFM.fetchData();
    if(enabledChartsFM.models[0].get('error') !== undefined) {
    	// The same endpoint will send an error if 'DASHBOARDS' is not active in the Feature Manager.
    	window.location = '/';
    }
    var col = [];
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Donors'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Donor Agencies', big: false, view: 'bar' },
  	          { app: this.app, url: '/rest/dashboard/tops/do' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Donor Group'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Donor Groups', big: false, view: 'bar' },
  	          { app: this.app, url: '/rest/dashboard/tops/dg' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Regions'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Regions', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/re' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Top Sectors'})) {
    	col.push(new TopsChart(
  	          { name: 'Top Sectors', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/ps' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Aid Predictability'})) {
    	col.push(new PredictabilityChart(
  	          { name: 'Aid Predictability' },
	          { app: this.app, url: '/rest/dashboard/aid-predictability' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Funding Type'})) {
    	col.push(new FundingTypeChart(
  	          { name: 'Funding Type' },
	          { app: this.app, url: '/rest/dashboard/ftype' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Responsible Organizations'})) {
    	col.push(new TopsChart(
  	          { name: 'Responsible Organizations', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/ro' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Beneficiary Agencies'})) {
    	col.push(new TopsChart(
  	          { name: 'Beneficiary Agencies', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/ba' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Executing Agencies'})) {
    	col.push(new TopsChart(
  	          { name: 'Executing Agencies', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/ea' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Implementing Agencies'})) {
    	col.push(new TopsChart(
  	          { name: 'Implementing Agencies', big: false, view: 'bar' },
	          { app: this.app, url: '/rest/dashboard/tops/ia' }));
    }
    if(_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function(item) {return item ===  'Peace-building and State-building Goals'})) {
    	col.push(new TopsChart(
    			{ name: 'Peace-building and State-building Goals', big: true, showCategoriesInfo: true, view: 'pie' },
    			{ app: this.app, url: '/rest/dashboard/ndd' }));
    }
       
    var chartsCollection = new Charts(col, { app: this.app });
    this.charts = new ChartsView({
      app: this.app,
      collection: chartsCollection
    });

    this.footer = new Footer({ app: this.app });
  },

  render: function() {
    this.$el.html(template());
    this.$('.container').html([
      this.header.render().el,
      this.controls.render().el,
      this.charts.render().el,
      this.footer.render().el
    ]);
    return this;
  },

  modal: function(title, options) {
    options = _({
      title: title,
      id: _.uniqueId('modal')
    }).extend(options);
    this.$el.parent().append(modalTemplate({m: options}));
    var thisModal = this.$el.parent().find('#' + options.id);
    if (options.bodyEl) { thisModal.find('.modal-body').html(options.bodyEl); }
    thisModal.modal();
    return thisModal[0];  // the actual DOM element
  }

});
