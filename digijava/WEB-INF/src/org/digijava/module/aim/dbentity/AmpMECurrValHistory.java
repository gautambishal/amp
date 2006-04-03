package org.digijava.module.aim.dbentity;

import java.util.Date;

public class AmpMECurrValHistory {
	private Long ampMECurrValHistoryId;
	private AmpMEIndicatorValue meIndValue;
	private Date currValueDate;
	private String comments;
	/**
	 * @return Returns the ampMECurrValHistoryId.
	 */
	public Long getAmpMECurrValHistoryId() {
		return ampMECurrValHistoryId;
	}
	/**
	 * @param ampMECurrValHistoryId The ampMECurrValHistoryId to set.
	 */
	public void setAmpMECurrValHistoryId(Long ampMECurrValHistoryId) {
		this.ampMECurrValHistoryId = ampMECurrValHistoryId;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the currValueDate.
	 */
	public Date getCurrValueDate() {
		return currValueDate;
	}
	/**
	 * @param currValueDate The currValueDate to set.
	 */
	public void setCurrValueDate(Date currValueDate) {
		this.currValueDate = currValueDate;
	}
	/**
	 * @return Returns the meIndValue.
	 */
	public AmpMEIndicatorValue getMeIndValue() {
		return meIndValue;
	}
	/**
	 * @param meIndValue The meIndValue to set.
	 */
	public void setMeIndValue(AmpMEIndicatorValue meIndValue) {
		this.meIndValue = meIndValue;
	}
	
}