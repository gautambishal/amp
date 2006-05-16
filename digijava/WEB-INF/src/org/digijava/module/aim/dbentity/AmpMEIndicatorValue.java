package org.digijava.module.aim.dbentity;

import java.util.Date;

public class AmpMEIndicatorValue {
	private Long ampMeIndValId;
	private AmpActivity activityId;
	private AmpMEIndicators meIndicatorId;
	private float baseVal;  // BASE
	private Date baseValDate;
	private float actualVal;  // ACTUAL
	private Date actualValDate;
	private float targetVal; // TARGET
	private Date targetValDate;
	private String comments;
	
	private AmpIndicatorRiskRatings risk;
	/**
	 * @return Returns the activityId.
	 */
	public AmpActivity getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(AmpActivity activityId) {
		this.activityId = activityId;
	}
	/**
	 * @return Returns the ampMeIndValId.
	 */
	public Long getAmpMeIndValId() {
		return ampMeIndValId;
	}
	/**
	 * @param ampMeIndValId The ampMeIndValId to set.
	 */
	public void setAmpMeIndValId(Long ampMeIndValId) {
		this.ampMeIndValId = ampMeIndValId;
	}
	/**
	 * @return Returns the baseValDate.
	 */
	public Date getBaseValDate() {
		return baseValDate;
	}
	/**
	 * @param baseValDate The baseValDate to set.
	 */
	public void setBaseValDate(Date baseValDate) {
		this.baseValDate = baseValDate;
	}
	/**
	 * @return Returns the meIndicatorId.
	 */
	public AmpMEIndicators getMeIndicatorId() {
		return meIndicatorId;
	}
	/**
	 * @param meIndicatorId The meIndicatorId to set.
	 */
	public void setMeIndicatorId(AmpMEIndicators meIndicatorId) {
		this.meIndicatorId = meIndicatorId;
	}
	/**
	 * @return Returns the risk.
	 */
	public AmpIndicatorRiskRatings getRisk() {
		return risk;
	}
	/**
	 * @param risk The risk to set.
	 */
	public void setRisk(AmpIndicatorRiskRatings risk) {
		this.risk = risk;
	}
	/**
	 * @return Returns the targetValDate.
	 */
	public Date getTargetValDate() {
		return targetValDate;
	}
	/**
	 * @param targetValDate The targetValDate to set.
	 */
	public void setTargetValDate(Date targetValDate) {
		this.targetValDate = targetValDate;
	}
	/**
	 * @return Returns the baseVal.
	 */
	public float getBaseVal() {
		return baseVal;
	}
	/**
	 * @param baseVal The baseVal to set.
	 */
	public void setBaseVal(float baseVal) {
		this.baseVal = baseVal;
	}
	/**
	 * @return Returns the targetVal.
	 */
	public float getTargetVal() {
		return targetVal;
	}
	/**
	 * @param targetVal The targetVal to set.
	 */
	public void setTargetVal(float targetVal) {
		this.targetVal = targetVal;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the actualVal.
	 */
	public float getActualVal() {
		return actualVal;
	}
	/**
	 * @param actualVal The actualVal to set.
	 */
	public void setActualVal(float actualVal) {
		this.actualVal = actualVal;
	}
	/**
	 * @return Returns the actualValDate.
	 */
	public Date getActualValDate() {
		return actualValDate;
	}
	/**
	 * @param actualValDate The actualValDate to set.
	 */
	public void setActualValDate(Date actualValDate) {
		this.actualValDate = actualValDate;
	}
}