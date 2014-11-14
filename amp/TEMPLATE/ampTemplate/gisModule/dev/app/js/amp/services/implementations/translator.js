// TODO: move this up a dir, and instantiate and attach to the app

var fs = require('fs');
var $ = require('jquery');
var Backbone = require('backbone');

function Translator() {
  'use strict';

  if (!(this instanceof Translator)) {
    throw new Error('Translator needs to be created with the `new` keyword.');
  }

  // this is the object that has all  the key value pairs for the widget.
  this._defaultKeys = JSON.parse(fs.readFileSync(__dirname +
    '/initial-translation-request.json', 'utf8'));
  this.availableLanguages = null;// backbone collection
  this.translations = {
    locales:{
      en:null
    }
  };

  this._promise = null;
  this._currentLng = 'tmp';
  this._firstGet = null;

  // TODO: add support for local storage with timestamp
  this.initTranslations = function get() {
    var self = this;


    // try web
    this._promise = this._getTranslationsFromAPI(self._defaultKeys)
      .fail(function(jqXHR, textStatus, errorThrown) {
        console.error('failed ', jqXHR, textStatus, errorThrown);
      });
  };


  this.getAvailableLanguages = function() {
    var deferred = $.Deferred();

    if (this.availableLanguages) {
      deferred.resolve(this.availableLanguages);
    } else {
      this._initAvailableLanguages().then(function() {
        deferred.resolve(this.availableLanguages);
      });
    }

    return deferred;
  };

  this._initAvailableLanguages = function() {
    this.availableLanguages = new Backbone.Collection([]);
    this.availableLanguages.url = '/rest/translations/languages';
    return this.availableLanguages.fetch();
  };


  // important to let the api know, so all responses are translated.
  this.setLanguage = function(lng) {
    this._currentLng = lng;
    return this._apiCall('/rest/translations/languages/' + lng, null, 'GET');
  };


  this.translateDOM = function(el) {
    var self = this;

    return this.getTranslations().then(function(data) {
      self._updateDom(el, data);
      return el;
    });
  };

  // update translateable elements in the dom
  this._updateDom = function(el, data) {
    $.each(data, function(key, value) {
      $(el).find('*[data-i18n="' + key + '"]').text(value);
    });
  };


  // Only do single request on launch.
  this.getTranslations = function() {
    // this way won't work with change languages mid way though.
    if (!this._firstGet) {
      this._firstGet = this._getTranslationsFromAPI(this._defaultKeys, this._currentLng);
    }

    return this._firstGet;
  };


  this._getTranslationsFromAPI = function(translateables, lng) {
    var self = this;
    var url = '/rest/translations/label-translations';

    return this._apiCall(url, translateables, 'POST').then(function(data) {
      //cache if we know the lng. TODO: get api to always return the lng.
      if (lng) {
        self.translations.locales[lng] = data;
      } else {
        // temp hack to do caching if API doesn't return current lng
        lng = this._currentLng;
        self.translations.locales[lng] = data;
      }

      return data;
    });
  };


  // helper to wrap api call
  this._apiCall = function(url, data, type) {
    var ajaxOptions = {
      headers: {
        // jscs:disable disallowQuotedKeysInObjects
        'Accept': 'application/json',
        'Content-Type': 'application/json'
        // jscs:enable disallowQuotedKeysInObjects
      },
      type: type,
      url: url,
      dataType: 'json'
    };
    if (data) {
      ajaxOptions.data = JSON.stringify(data);
    }

    return $.ajax(ajaxOptions);
  };

  this.initTranslations();
}

module.exports = Translator;
