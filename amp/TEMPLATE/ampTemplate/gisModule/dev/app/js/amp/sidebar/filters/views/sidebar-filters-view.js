var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/filters-sidebar-template.html', 'utf8');

require('jquery-ui/draggable');

module.exports = BaseControlView.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',

  events:{
    'click .accordion-heading': 'newlaunchFilter'
  },

  template: _.template(Template),

  initialize:function(options) {
    var self = this;
    this.app = options.app;
    BaseControlView.prototype.initialize.apply(this, arguments);

    this.app.data.filter.loaded.then(function() {
      self.app.state.register(self, 'filters', {
        get: function() { return self.app.data.filter.serialize();},
        set: function(state) {
          self.app.data.filter.reset({silent: true});
          return self.app.data.filter.deserialize(state);
        },
        empty: null
      });
    });

  },


  render:function() {
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));
    this.app.data.filter.setElement(this.el.querySelector('#filter-popup')); //self.$('#filter-popup'));

    this._attachListeners();

    return this;
  },

  newlaunchFilter:function() {
    this.app.data.filter.showFilters(); // triggers stash of vars etc...
    this.$('#filter-popup').show();
  },

  _attachListeners: function() {
    var self = this;

    // could do better, but must close accordion or get weird states from bootstrap and manual filter showing....
    this.app.data.filter.on('cancel', function() {
      self.$('.accordion-body').collapse('hide');
    });

    // serialized obj is returned in event if needed.
    this.app.data.filter.on('apply', function() {
      //console.log('serialized', serialized.columnFilters);
      //only collapse ui if it's expanded...otherwise strange bootstrap behaviour.
      if (self.$('.accordion-body.collapse.in').length > 0) {
        self.$('.accordion-body').collapse('hide');
        $('#map-loading').show();
      }
    });
  }

});
