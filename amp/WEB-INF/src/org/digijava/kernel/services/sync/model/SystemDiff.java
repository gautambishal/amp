package org.digijava.kernel.services.sync.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Octavian Ciubotaru
 */
public class SystemDiff {

    @JsonProperty @JsonSerialize(using = ISO8601TimeStampSerializer.class)
    private Date timestamp;

    @JsonProperty("global-settings")
    private boolean globalSettings;

    @JsonProperty
    private boolean workspaces;

    @JsonProperty("workspace-settings")
    private boolean workspaceSettings;

    @JsonProperty("workspace-members")
    private ListDiff<Long> workspaceMembers;

    @JsonProperty
    private ListDiff<Long> users;

    @JsonProperty
    private ListDiff<String> activities;

    @JsonProperty("possible-values-fields")
    private List<String> possibleValuesFields;

    @JsonProperty
    private boolean translations;

    @JsonProperty("exchange-rates")
    private boolean exchangeRates;

    public void updateTimestamp(Date timestamp) {
        if (this.timestamp == null || (timestamp != null && this.timestamp.before(timestamp))) {
            this.timestamp = timestamp;
        }
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setGlobalSettings(boolean globalSettings) {
        this.globalSettings = globalSettings;
    }

    public void setWorkspaces(boolean workspaces) {
        this.workspaces = workspaces;
    }

    public void setWorkspaceSettings(boolean workspaceSettings) {
        this.workspaceSettings = workspaceSettings;
    }

    public void setWorkspaceMembers(ListDiff<Long> workspaceMembers) {
        this.workspaceMembers = workspaceMembers;
    }

    public void setUsers(ListDiff<Long> users) {
        this.users = users;
    }

    public void setActivities(ListDiff<String> activities) {
        this.activities = activities;
    }

    public void setTranslations(boolean translations) {
        this.translations = translations;
    }

    public void setPossibleValuesFields(List<String> possibleValuesFields) {
        this.possibleValuesFields = possibleValuesFields;
    }

    public void setExchangeRates(boolean exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}
