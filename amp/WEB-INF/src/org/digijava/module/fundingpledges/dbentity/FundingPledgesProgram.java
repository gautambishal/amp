// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.fundingpledges.dbentity;

import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.Identifiable;

public class FundingPledgesProgram implements Identifiable{

    private Long id;
    private FundingPledges pledgeid;
    private AmpTheme program;
    private Float programpercentage;

    @Override public Object getIdentifier(){
        return this.id;
    }

    public int hashCode() {
        return program.getAmpThemeId().hashCode();
    }

    public boolean equals(Object oth) {
        return program.getAmpThemeId().equals(((FundingPledgesProgram)oth).program.getAmpThemeId());
    }

    @java.lang.SuppressWarnings("all")
    public Long getId() {
        return this.id;
    }

    @java.lang.SuppressWarnings("all")
    public FundingPledges getPledgeid() {
        return this.pledgeid;
    }

    @java.lang.SuppressWarnings("all")
    public AmpTheme getProgram() {
        return this.program;
    }

    @java.lang.SuppressWarnings("all")
    public Float getProgrampercentage() {
        return this.programpercentage;
    }

    @java.lang.SuppressWarnings("all")
    public void setId(final Long id) {
        this.id = id;
    }

    @java.lang.SuppressWarnings("all")
    public void setPledgeid(final FundingPledges pledgeid) {
        this.pledgeid = pledgeid;
    }

    @java.lang.SuppressWarnings("all")
    public void setProgram(final AmpTheme program) {
        this.program = program;
    }

    @java.lang.SuppressWarnings("all")
    public void setProgrampercentage(final Float programpercentage) {
        this.programpercentage = programpercentage;
    }
}
