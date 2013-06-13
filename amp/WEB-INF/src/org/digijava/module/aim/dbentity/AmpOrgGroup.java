package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorGroupDimension;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;

import com.rc.retroweaver.runtime.Arrays;

@TranslatableClass
public class AmpOrgGroup implements Serializable, Comparable, Identifiable, ARDimensionable, HierarchyListable,OrgProfileValue
{
	private Long ampOrgGrpId;
	@TranslatableField
	private String orgGrpName;
	private String orgGrpCode;
	private AmpLevel levelId;	// defunct
	//private Set organizations; 
	
	private AmpOrgType orgType;	// a mandatory field, added for donor-access
	
	
	private boolean translateable	= true;
    // this field is saved in Organization Dashboard and not from organization manager in admin
    private String orgGrpBackground;
    /*
     *this field is saved in Organization Dashboard and not from organization manager in admin
     * don't confuse it with Description field
     */
    private String orgGrpDescription;
    // this field is saved in  Organization Dashboard and not from organization manager in admin
    private String orgGrpKeyAreas;
	
	/**
	 * @return Returns the ampOrgGrpId.
	 */
	public Long getAmpOrgGrpId() {
		return ampOrgGrpId;
	}
	/**
	 * @param ampOrgGrpId The ampOrgGrpId to set.
	 */
	public void setAmpOrgGrpId(Long ampOrgGrpId) {
		this.ampOrgGrpId = ampOrgGrpId;
	}
	/**
	 * @return Returns the levelId.
	 */
	public AmpLevel getLevelId() {
		return levelId;
	}
	/**
	 * @param levelId The levelId to set.
	 */
	public void setLevelId(AmpLevel levelId) {
		this.levelId = levelId;
	}
	/**
	 * @return Returns the orgGrpCode.
	 */
	public String getOrgGrpCode() {
		return orgGrpCode;
	}
	/**
	 * @param orgGrpCode The orgGrpCode to set.
	 */
	public void setOrgGrpCode(String orgGrpCode) {
		this.orgGrpCode = orgGrpCode;
	}
	/**
	 * @return Returns the orgGrpName.
	 */
	public String getOrgGrpName() {
		return orgGrpName;
	}
	/**
	 * @param orgGrpName The orgGrpName to set.
	 */
	public void setOrgGrpName(String orgGrpName) {
		this.orgGrpName = orgGrpName;
	}

	/**
	 * @return Returns the orgType.
	 */
	public AmpOrgType getOrgType() {
		return orgType;
	}
	/**
	 * @param orgType The orgType to set.
	 */
	public void setOrgType(AmpOrgType orgType) {
		this.orgType = orgType;
	}
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.orgGrpName.trim().toLowerCase().compareTo(((AmpOrgGroup)arg0).getOrgGrpName().trim().toLowerCase());
		//return 0;
	}
	public Object getIdentifier() {
		return this.getAmpOrgGrpId();
	}
	public String toString() {
		return this.getOrgGrpName();
	}
	public Class getDimensionClass() {
	    return DonorGroupDimension.class;
	}
	
	@Override
	public Collection<AmpOrgGroup> getChildren() {
		return null;
	}
	@Override
	public int getCountDescendants() {
		return 1;
	}
	@Override
	public String getLabel() {
		return this.orgGrpName;
	}
	@Override
	public String getUniqueId() {
        return String.valueOf(this.ampOrgGrpId.longValue());
	}
	@Override
	public boolean getTranslateable() {
		return translateable;
	}
	@Override
	public void setTranslateable(boolean translateable) {
		this.translateable = translateable;
	}

    public String getAdditionalSearchString() {
        return this.orgGrpCode;
    }
    public List<ValueTranslatabePair> getValuesForOrgReport(){
    	List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
    	ValueTranslatabePair value=new ValueTranslatabePair(Arrays.asList(new String[]{getOrgGrpName()}),false);
    	values.add(value);
    	return values;
    }
	@Override
	public String[] getSubHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getOrgGrpBackground() {
		return orgGrpBackground;
	}
	public void setOrgGrpBackground(String orgGrpBackground) {
		this.orgGrpBackground = orgGrpBackground;
	}
	public String getOrgGrpDescription() {
		return orgGrpDescription;
	}
	public void setOrgGrpDescription(String orgGrpDescription) {
		this.orgGrpDescription = orgGrpDescription;
	}
	public String getOrgGrpKeyAreas() {
		return orgGrpKeyAreas;
	}
	public void setOrgGrpKeyAreas(String orgKeyAreas) {
		this.orgGrpKeyAreas = orgKeyAreas;
	}
}
