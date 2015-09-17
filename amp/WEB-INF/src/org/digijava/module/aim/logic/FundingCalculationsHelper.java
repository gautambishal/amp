// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.aim.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils.Null;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.FundingInformationItem;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.RegionalFundingsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import com.sun.istack.logging.Logger;

public class FundingCalculationsHelper {
	private static Logger logger = Logger.getLogger(FundingCalculationsHelper.class);
	List<FundingDetail> fundDetailList = new ArrayList<FundingDetail>();
	DecimalWraper totPlanDisb = new DecimalWraper();
	DecimalWraper totPlannedComm = new DecimalWraper();
	DecimalWraper totPlannedExp = new DecimalWraper();
	DecimalWraper totPlannedDisbOrder = new DecimalWraper();
	DecimalWraper totPlannedReleaseOfFunds = new DecimalWraper();
	DecimalWraper totPlannedEDD = new DecimalWraper();
	DecimalWraper totActualComm = new DecimalWraper();
	DecimalWraper totActualDisb = new DecimalWraper();
	DecimalWraper totActualExp = new DecimalWraper();
	DecimalWraper totActualDisbOrder = new DecimalWraper();
	DecimalWraper totActualReleaseOfFunds = new DecimalWraper();
	DecimalWraper totActualEDD = new DecimalWraper();
	DecimalWraper totPipelineDisb = new DecimalWraper();
	DecimalWraper totPipelineComm = new DecimalWraper();
	DecimalWraper totPipelineExp = new DecimalWraper();
	DecimalWraper totPipelineDisbOrder = new DecimalWraper();
	DecimalWraper totPipelineReleaseOfFunds = new DecimalWraper();
	DecimalWraper totPipelineEDD = new DecimalWraper();
	DecimalWraper totOdaSscComm = new DecimalWraper();
	DecimalWraper totBilateralSscComm = new DecimalWraper();
	DecimalWraper totTriangularSscComm = new DecimalWraper();
	
	/**
	 * DO NOT CALCULATE SSC STUFF HERE!
	 */
	DecimalWraper totalCommitments = new DecimalWraper();
	DecimalWraper unDisbursementsBalance = new DecimalWraper();
	DecimalWraper totalMtef = new DecimalWraper();
	DecimalWraper totalPledged = new DecimalWraper();
	
	boolean debug;
	
	/**
	 * extracts all the donor funding + MTEF funding from a source and adds them into a single source; then calculates the totals <br />
	 * also resets the internal {@link #getFundDetailList()}
	 * @param fundingSource
	 * @param userCurrencyCode
	 */
	public void doCalculations(AmpFunding fundingSource, String userCurrencyCode) {
		ArrayList<FundingInformationItem> funding = new ArrayList<FundingInformationItem>();
		if (fundingSource.getFundingDetails() != null) funding.addAll(fundingSource.getFundingDetails());
		if (fundingSource.getMtefProjections() != null) funding.addAll(fundingSource.getMtefProjections());
		boolean updateTotals = fundingSource.isCountedInTotals();
		doCalculations(funding, userCurrencyCode, updateTotals);
	}
	
	/**
	 * also resets the internal {@link #getFundDetailList()}
	 * @param details
	 * @param userCurrencyCode
	 * @param updateTotals - if false, then only fundDetailList will be built, without updating the totals
	 */
	public void doCalculations(Collection<? extends FundingInformationItem> details, String userCurrencyCode, boolean updateTotals) {
		//Iterator<? extends FundingInformationItem> fundDetItr = details.iterator();
		fundDetailList = new ArrayList<FundingDetail>();
		int indexId = 0;
		String toCurrCode = Constants.DEFAULT_CURRENCY;
		AmpCategoryValue actualAdjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getAmpCategoryValueFromDB();
		if (actualAdjustmentType == null) {
			throw new RuntimeException("ACTUAL adjustment type not found in the database");
		}
		String decimalSeparatorStr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
		char decimalSeparatorChar = decimalSeparatorStr == null ? '.' : (decimalSeparatorStr.isEmpty() ? '.' : decimalSeparatorStr.charAt(0));
		for (FundingInformationItem fundDet:details)
		{
			AmpCategoryValue adjType = actualAdjustmentType;
			if (fundDet.getAdjustmentType() != null)
				adjType = fundDet.getAdjustmentType();
			FundingDetail fundingDetail = new FundingDetail();
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
			fundingDetail.setFundDetId(fundDet.getDbId());
			
//			String baseCurrCode		= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
			if (fundDet.getFixedExchangeRate() != null && fundDet.getFixedExchangeRate().doubleValue() != 1) {
				// We cannot use FormatHelper.formatNumber as this might roundup our number (and this would be very wrong)
				fundingDetail.setFixedExchangeRate((fundDet.getFixedExchangeRate().toString()).replace('.', decimalSeparatorChar));
				fundingDetail.setUseFixedRate(true);
			}
			fundingDetail.setIndexId(indexId++);
			fundingDetail.setAdjustmentTypeName(fundDet.getAdjustmentType());
			if(fundDet.getTransactionType().equals(Constants.MTEFPROJECTION)){
				fundingDetail.setProjectionTypeName(((AmpFundingMTEFProjection)fundDet).getProjected());	
			}
			fundingDetail.setContract(fundDet.getContract());
			java.sql.Date dt = new java.sql.Date(fundDet.getTransactionDate().getTime());
			
			Double fixedExchangeRate = fundDet.getFixedExchangeRate();
			if (fixedExchangeRate != null && (Math.abs(fixedExchangeRate.doubleValue()) < 1.0E-15))
				fixedExchangeRate = null;
			double frmExRt;
			if (fixedExchangeRate == null) {
				frmExRt = Util.getExchange(fundDet.getAmpCurrencyId().getCurrencyCode(), dt);
			} else {
				frmExRt = fixedExchangeRate;
			}
			double toExRt;
			if (userCurrencyCode != null)
				toCurrCode = userCurrencyCode;
			if (fundDet.getAmpCurrencyId().getCurrencyCode().equalsIgnoreCase(toCurrCode)) {
				toExRt = frmExRt;
			} else {
				toExRt = Util.getExchange(toCurrCode, dt);
			}
			DecimalWraper amt = CurrencyWorker.convertWrapper(fundDet.getTransactionAmount().doubleValue(), frmExRt, toExRt, dt);
			if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				fundingDetail.setClassification(fundDet.getExpCategory());
			}
		    fundingDetail.setCurrencyCode(toCurrCode);
		    AmpCurrency curr = CurrencyUtil.getAmpcurrency(toCurrCode);
		    if(curr != null) {
		    	fundingDetail.setCurrencyName(curr.getCountryName());
		    }
			fundingDetail.setTransactionAmount(CurrencyWorker.convert(FeaturesUtil.applyThousandsForVisibility(amt.doubleValue()).doubleValue(), 1, 1));
			fundingDetail.setTransactionDate(DateConversion.ConvertDateToString(fundDet.getTransactionDate()));
			fundingDetail.setFiscalYear(DateConversion.convertDateToFiscalYearString(fundDet.getTransactionDate()));
			fundingDetail.setCapitalPercent(fundDet.getCapitalSpendingPercentage());
			fundingDetail.setReportingDate(fundDet.getReportingDate());
			fundingDetail.setRecipientOrganisation(fundDet.getRecipientOrg());
			fundingDetail.setRecipientOrganisationRole(fundDet.getRecipientRole());
			fundingDetail.setTransactionType(fundDet.getTransactionType().intValue());
			fundingDetail.setDisbOrderId(fundDet.getDisbOrderId());
			if (fundDet.getPledgeid() != null) {
				fundingDetail.setPledge(fundDet.getPledgeid().getId());
				fundingDetail.setAttachedPledgeName(fundDet.getPledgeid().getEffectiveName());
			}
			// TOTALS
			if (updateTotals) addToTotals(adjType, fundDet, amt);
			fundDetailList.add(fundingDetail);
			fundingDetail.setDisasterResponse(fundDet.getDisasterResponse());
		}
		totalCommitments = Logic.getInstance().getTotalDonorFundingCalculator().getTotalCommtiments(totPlannedComm, totActualComm, totPipelineComm);
		unDisbursementsBalance = Logic.getInstance().getTotalDonorFundingCalculator().getunDisbursementsBalance(totalCommitments, totActualDisb);
		
	}
	
	protected void addToTotals(AmpCategoryValue adjType, FundingInformationItem fundDet, DecimalWraper amt) {
		/**
		 * no adjustment type for MTEF transactions or PLEDGED amounts, so this "if" is outside the PLANNED / ACTUAL / PIPELINE branching if's
		 */
		if (fundDet.getTransactionType().intValue() == Constants.MTEFPROJECTION) {
			totalMtef.add(amt);
			return;
		}
		if (fundDet.getTransactionType().intValue() == Constants.PLEDGE){
			totalPledged.add(amt);
		}
		if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
			//fundingDetail.setAdjustmentTypeName("Planned");
			if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totPlanDisb.add(amt);
				//totPlanDisb.setCalculations(totPlanDisb.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totPlannedComm.add(amt);
				//totPlannedComm.setCalculations(totPlannedComm.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPlannedExp.add(amt);
				//totPlannedExp.setCalculations(totPlannedExp.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPlannedDisbOrder.add(amt);
				//totPlannedDisbOrder.setCalculations(totPlannedDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPlannedReleaseOfFunds.add(amt);
				//totPlannedReleaseOfFunds.setCalculations(totPlannedReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPlannedEDD.add(amt);
				//totPlannedEDD.setCalculations(totPlannedEDD.getCalculations() + " + " + amt.getCalculations());
			}
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
			//fundingDetail.setAdjustmentTypeName("Actual");
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totActualComm.add(amt);
				//totActualComm.setCalculations(totActualComm.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totActualDisb.add(amt);
				//totActualDisb.setCalculations(totActualDisb.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totActualExp.add(amt);
				//totActualExp.setCalculations(totActualExp.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totActualDisbOrder.add(amt);
				//totActualDisbOrder.setCalculations(totActualDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totActualReleaseOfFunds.add(amt);
				//totActualReleaseOfFunds.setCalculations(totActualReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totActualEDD.add(amt);
				//totActualEDD.setCalculations(totActualEDD.getCalculations() + " + " + amt.getCalculations());
			}
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_PIPELINE.getValueKey())) {
			// fundingDetail.setAdjustmentTypeName("Pipeline");
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) {
				totPipelineComm.add(amt);
				// totPipelineComm.setCalculations(totPipelineComm.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT) {
				totPipelineDisb.add(amt);
				//totPipelineDisb.setCalculations(totPipelineDisb.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.EXPENDITURE) {
				totPipelineExp.add(amt);
				//totPipelineExp.setCalculations(totPipelineExp.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.DISBURSEMENT_ORDER) {
				totPipelineDisbOrder.add(amt);
				//totPipelineDisbOrder.setCalculations(totPipelineDisbOrder.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.RELEASE_OF_FUNDS) {
				totPipelineReleaseOfFunds.add(amt);
				//totPipelineReleaseOfFunds.setCalculations(totPipelineReleaseOfFunds.getCalculations() + " + " + amt.getCalculations());
			} else if (fundDet.getTransactionType().intValue() == Constants.ESTIMATED_DONOR_DISBURSEMENT) {
				totPipelineEDD.add(amt);
				//totPipelineEDD.setCalculations(totPipelineEDD.getCalculations() + " + " + amt.getCalculations());
			}
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_ODA_SSC.getValueKey())) {
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) totOdaSscComm.add(amt);
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_BILATERAL_SSC.getValueKey())) {
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) totBilateralSscComm.add(amt);
		} else if (adjType.getValue().equals(CategoryConstants.ADJUSTMENT_TYPE_TRIANGULAR_SSC.getValueKey())) {
			if (fundDet.getTransactionType().intValue() == Constants.COMMITMENT) totTriangularSscComm.add(amt);
		}
	}
	
	@java.lang.SuppressWarnings("all")
	public FundingCalculationsHelper() {
	}
	
	@java.lang.SuppressWarnings("all")
	public List<FundingDetail> getFundDetailList() {
		return this.fundDetailList;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlanDisb() {
		return this.totPlanDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlannedComm() {
		return this.totPlannedComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlannedExp() {
		return this.totPlannedExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlannedDisbOrder() {
		return this.totPlannedDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlannedReleaseOfFunds() {
		return this.totPlannedReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPlannedEDD() {
		return this.totPlannedEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualComm() {
		return this.totActualComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualDisb() {
		return this.totActualDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualExp() {
		return this.totActualExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualDisbOrder() {
		return this.totActualDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualReleaseOfFunds() {
		return this.totActualReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotActualEDD() {
		return this.totActualEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineDisb() {
		return this.totPipelineDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineComm() {
		return this.totPipelineComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineExp() {
		return this.totPipelineExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineDisbOrder() {
		return this.totPipelineDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineReleaseOfFunds() {
		return this.totPipelineReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotPipelineEDD() {
		return this.totPipelineEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotOdaSscComm() {
		return this.totOdaSscComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotBilateralSscComm() {
		return this.totBilateralSscComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotTriangularSscComm() {
		return this.totTriangularSscComm;
	}
	
	/**
	 * DO NOT CALCULATE SSC STUFF HERE!
	 */
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotalCommitments() {
		return this.totalCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getUnDisbursementsBalance() {
		return this.unDisbursementsBalance;
	}
	
	@java.lang.SuppressWarnings("all")
	public DecimalWraper getTotalMtef() {
		return this.totalMtef;
	}
	
	public DecimalWraper getTotalPledged(){
		return this.totalPledged;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean isDebug() {
		return this.debug;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setFundDetailList(final List<FundingDetail> fundDetailList) {
		this.fundDetailList = fundDetailList;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlanDisb(final DecimalWraper totPlanDisb) {
		this.totPlanDisb = totPlanDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlannedComm(final DecimalWraper totPlannedComm) {
		this.totPlannedComm = totPlannedComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlannedExp(final DecimalWraper totPlannedExp) {
		this.totPlannedExp = totPlannedExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlannedDisbOrder(final DecimalWraper totPlannedDisbOrder) {
		this.totPlannedDisbOrder = totPlannedDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlannedReleaseOfFunds(final DecimalWraper totPlannedReleaseOfFunds) {
		this.totPlannedReleaseOfFunds = totPlannedReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPlannedEDD(final DecimalWraper totPlannedEDD) {
		this.totPlannedEDD = totPlannedEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualComm(final DecimalWraper totActualComm) {
		this.totActualComm = totActualComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualDisb(final DecimalWraper totActualDisb) {
		this.totActualDisb = totActualDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualExp(final DecimalWraper totActualExp) {
		this.totActualExp = totActualExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualDisbOrder(final DecimalWraper totActualDisbOrder) {
		this.totActualDisbOrder = totActualDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualReleaseOfFunds(final DecimalWraper totActualReleaseOfFunds) {
		this.totActualReleaseOfFunds = totActualReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotActualEDD(final DecimalWraper totActualEDD) {
		this.totActualEDD = totActualEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineDisb(final DecimalWraper totPipelineDisb) {
		this.totPipelineDisb = totPipelineDisb;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineComm(final DecimalWraper totPipelineComm) {
		this.totPipelineComm = totPipelineComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineExp(final DecimalWraper totPipelineExp) {
		this.totPipelineExp = totPipelineExp;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineDisbOrder(final DecimalWraper totPipelineDisbOrder) {
		this.totPipelineDisbOrder = totPipelineDisbOrder;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineReleaseOfFunds(final DecimalWraper totPipelineReleaseOfFunds) {
		this.totPipelineReleaseOfFunds = totPipelineReleaseOfFunds;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotPipelineEDD(final DecimalWraper totPipelineEDD) {
		this.totPipelineEDD = totPipelineEDD;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotOdaSscComm(final DecimalWraper totOdaSscComm) {
		this.totOdaSscComm = totOdaSscComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotBilateralSscComm(final DecimalWraper totBilateralSscComm) {
		this.totBilateralSscComm = totBilateralSscComm;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotTriangularSscComm(final DecimalWraper totTriangularSscComm) {
		this.totTriangularSscComm = totTriangularSscComm;
	}
	
	/**
	 * DO NOT CALCULATE SSC STUFF HERE!
	 */
	@java.lang.SuppressWarnings("all")
	public void setTotalCommitments(final DecimalWraper totalCommitments) {
		this.totalCommitments = totalCommitments;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setUnDisbursementsBalance(final DecimalWraper unDisbursementsBalance) {
		this.unDisbursementsBalance = unDisbursementsBalance;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setTotalMtef(final DecimalWraper totalMtef) {
		this.totalMtef = totalMtef;
	}
	
	@java.lang.SuppressWarnings("all")
	public void setDebug(final boolean debug) {
		this.debug = debug;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof FundingCalculationsHelper)) return false;
		final FundingCalculationsHelper other = (FundingCalculationsHelper)o;
		if (!other.canEqual((java.lang.Object)this)) return false;
		final java.lang.Object this$fundDetailList = this.getFundDetailList();
		final java.lang.Object other$fundDetailList = other.getFundDetailList();
		if (this$fundDetailList == null ? other$fundDetailList != null : !this$fundDetailList.equals(other$fundDetailList)) return false;
		final java.lang.Object this$totPlanDisb = this.getTotPlanDisb();
		final java.lang.Object other$totPlanDisb = other.getTotPlanDisb();
		if (this$totPlanDisb == null ? other$totPlanDisb != null : !this$totPlanDisb.equals(other$totPlanDisb)) return false;
		final java.lang.Object this$totPlannedComm = this.getTotPlannedComm();
		final java.lang.Object other$totPlannedComm = other.getTotPlannedComm();
		if (this$totPlannedComm == null ? other$totPlannedComm != null : !this$totPlannedComm.equals(other$totPlannedComm)) return false;
		final java.lang.Object this$totPlannedExp = this.getTotPlannedExp();
		final java.lang.Object other$totPlannedExp = other.getTotPlannedExp();
		if (this$totPlannedExp == null ? other$totPlannedExp != null : !this$totPlannedExp.equals(other$totPlannedExp)) return false;
		final java.lang.Object this$totPlannedDisbOrder = this.getTotPlannedDisbOrder();
		final java.lang.Object other$totPlannedDisbOrder = other.getTotPlannedDisbOrder();
		if (this$totPlannedDisbOrder == null ? other$totPlannedDisbOrder != null : !this$totPlannedDisbOrder.equals(other$totPlannedDisbOrder)) return false;
		final java.lang.Object this$totPlannedReleaseOfFunds = this.getTotPlannedReleaseOfFunds();
		final java.lang.Object other$totPlannedReleaseOfFunds = other.getTotPlannedReleaseOfFunds();
		if (this$totPlannedReleaseOfFunds == null ? other$totPlannedReleaseOfFunds != null : !this$totPlannedReleaseOfFunds.equals(other$totPlannedReleaseOfFunds)) return false;
		final java.lang.Object this$totPlannedEDD = this.getTotPlannedEDD();
		final java.lang.Object other$totPlannedEDD = other.getTotPlannedEDD();
		if (this$totPlannedEDD == null ? other$totPlannedEDD != null : !this$totPlannedEDD.equals(other$totPlannedEDD)) return false;
		final java.lang.Object this$totActualComm = this.getTotActualComm();
		final java.lang.Object other$totActualComm = other.getTotActualComm();
		if (this$totActualComm == null ? other$totActualComm != null : !this$totActualComm.equals(other$totActualComm)) return false;
		final java.lang.Object this$totActualDisb = this.getTotActualDisb();
		final java.lang.Object other$totActualDisb = other.getTotActualDisb();
		if (this$totActualDisb == null ? other$totActualDisb != null : !this$totActualDisb.equals(other$totActualDisb)) return false;
		final java.lang.Object this$totActualExp = this.getTotActualExp();
		final java.lang.Object other$totActualExp = other.getTotActualExp();
		if (this$totActualExp == null ? other$totActualExp != null : !this$totActualExp.equals(other$totActualExp)) return false;
		final java.lang.Object this$totActualDisbOrder = this.getTotActualDisbOrder();
		final java.lang.Object other$totActualDisbOrder = other.getTotActualDisbOrder();
		if (this$totActualDisbOrder == null ? other$totActualDisbOrder != null : !this$totActualDisbOrder.equals(other$totActualDisbOrder)) return false;
		final java.lang.Object this$totActualReleaseOfFunds = this.getTotActualReleaseOfFunds();
		final java.lang.Object other$totActualReleaseOfFunds = other.getTotActualReleaseOfFunds();
		if (this$totActualReleaseOfFunds == null ? other$totActualReleaseOfFunds != null : !this$totActualReleaseOfFunds.equals(other$totActualReleaseOfFunds)) return false;
		final java.lang.Object this$totActualEDD = this.getTotActualEDD();
		final java.lang.Object other$totActualEDD = other.getTotActualEDD();
		if (this$totActualEDD == null ? other$totActualEDD != null : !this$totActualEDD.equals(other$totActualEDD)) return false;
		final java.lang.Object this$totPipelineDisb = this.getTotPipelineDisb();
		final java.lang.Object other$totPipelineDisb = other.getTotPipelineDisb();
		if (this$totPipelineDisb == null ? other$totPipelineDisb != null : !this$totPipelineDisb.equals(other$totPipelineDisb)) return false;
		final java.lang.Object this$totPipelineComm = this.getTotPipelineComm();
		final java.lang.Object other$totPipelineComm = other.getTotPipelineComm();
		if (this$totPipelineComm == null ? other$totPipelineComm != null : !this$totPipelineComm.equals(other$totPipelineComm)) return false;
		final java.lang.Object this$totPipelineExp = this.getTotPipelineExp();
		final java.lang.Object other$totPipelineExp = other.getTotPipelineExp();
		if (this$totPipelineExp == null ? other$totPipelineExp != null : !this$totPipelineExp.equals(other$totPipelineExp)) return false;
		final java.lang.Object this$totPipelineDisbOrder = this.getTotPipelineDisbOrder();
		final java.lang.Object other$totPipelineDisbOrder = other.getTotPipelineDisbOrder();
		if (this$totPipelineDisbOrder == null ? other$totPipelineDisbOrder != null : !this$totPipelineDisbOrder.equals(other$totPipelineDisbOrder)) return false;
		final java.lang.Object this$totPipelineReleaseOfFunds = this.getTotPipelineReleaseOfFunds();
		final java.lang.Object other$totPipelineReleaseOfFunds = other.getTotPipelineReleaseOfFunds();
		if (this$totPipelineReleaseOfFunds == null ? other$totPipelineReleaseOfFunds != null : !this$totPipelineReleaseOfFunds.equals(other$totPipelineReleaseOfFunds)) return false;
		final java.lang.Object this$totPipelineEDD = this.getTotPipelineEDD();
		final java.lang.Object other$totPipelineEDD = other.getTotPipelineEDD();
		if (this$totPipelineEDD == null ? other$totPipelineEDD != null : !this$totPipelineEDD.equals(other$totPipelineEDD)) return false;
		final java.lang.Object this$totOdaSscComm = this.getTotOdaSscComm();
		final java.lang.Object other$totOdaSscComm = other.getTotOdaSscComm();
		if (this$totOdaSscComm == null ? other$totOdaSscComm != null : !this$totOdaSscComm.equals(other$totOdaSscComm)) return false;
		final java.lang.Object this$totBilateralSscComm = this.getTotBilateralSscComm();
		final java.lang.Object other$totBilateralSscComm = other.getTotBilateralSscComm();
		if (this$totBilateralSscComm == null ? other$totBilateralSscComm != null : !this$totBilateralSscComm.equals(other$totBilateralSscComm)) return false;
		final java.lang.Object this$totTriangularSscComm = this.getTotTriangularSscComm();
		final java.lang.Object other$totTriangularSscComm = other.getTotTriangularSscComm();
		if (this$totTriangularSscComm == null ? other$totTriangularSscComm != null : !this$totTriangularSscComm.equals(other$totTriangularSscComm)) return false;
		final java.lang.Object this$totalCommitments = this.getTotalCommitments();
		final java.lang.Object other$totalCommitments = other.getTotalCommitments();
		if (this$totalCommitments == null ? other$totalCommitments != null : !this$totalCommitments.equals(other$totalCommitments)) return false;
		final java.lang.Object this$unDisbursementsBalance = this.getUnDisbursementsBalance();
		final java.lang.Object other$unDisbursementsBalance = other.getUnDisbursementsBalance();
		if (this$unDisbursementsBalance == null ? other$unDisbursementsBalance != null : !this$unDisbursementsBalance.equals(other$unDisbursementsBalance)) return false;
		final java.lang.Object this$totalMtef = this.getTotalMtef();
		final java.lang.Object other$totalMtef = other.getTotalMtef();
		if (this$totalMtef == null ? other$totalMtef != null : !this$totalMtef.equals(other$totalMtef)) return false;
		if (this.isDebug() != other.isDebug()) return false;
		return true;
	}
	
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FundingCalculationsHelper;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $fundDetailList = this.getFundDetailList();
		result = result * PRIME + ($fundDetailList == null ? 0 : $fundDetailList.hashCode());
		final java.lang.Object $totPlanDisb = this.getTotPlanDisb();
		result = result * PRIME + ($totPlanDisb == null ? 0 : $totPlanDisb.hashCode());
		final java.lang.Object $totPlannedComm = this.getTotPlannedComm();
		result = result * PRIME + ($totPlannedComm == null ? 0 : $totPlannedComm.hashCode());
		final java.lang.Object $totPlannedExp = this.getTotPlannedExp();
		result = result * PRIME + ($totPlannedExp == null ? 0 : $totPlannedExp.hashCode());
		final java.lang.Object $totPlannedDisbOrder = this.getTotPlannedDisbOrder();
		result = result * PRIME + ($totPlannedDisbOrder == null ? 0 : $totPlannedDisbOrder.hashCode());
		final java.lang.Object $totPlannedReleaseOfFunds = this.getTotPlannedReleaseOfFunds();
		result = result * PRIME + ($totPlannedReleaseOfFunds == null ? 0 : $totPlannedReleaseOfFunds.hashCode());
		final java.lang.Object $totPlannedEDD = this.getTotPlannedEDD();
		result = result * PRIME + ($totPlannedEDD == null ? 0 : $totPlannedEDD.hashCode());
		final java.lang.Object $totActualComm = this.getTotActualComm();
		result = result * PRIME + ($totActualComm == null ? 0 : $totActualComm.hashCode());
		final java.lang.Object $totActualDisb = this.getTotActualDisb();
		result = result * PRIME + ($totActualDisb == null ? 0 : $totActualDisb.hashCode());
		final java.lang.Object $totActualExp = this.getTotActualExp();
		result = result * PRIME + ($totActualExp == null ? 0 : $totActualExp.hashCode());
		final java.lang.Object $totActualDisbOrder = this.getTotActualDisbOrder();
		result = result * PRIME + ($totActualDisbOrder == null ? 0 : $totActualDisbOrder.hashCode());
		final java.lang.Object $totActualReleaseOfFunds = this.getTotActualReleaseOfFunds();
		result = result * PRIME + ($totActualReleaseOfFunds == null ? 0 : $totActualReleaseOfFunds.hashCode());
		final java.lang.Object $totActualEDD = this.getTotActualEDD();
		result = result * PRIME + ($totActualEDD == null ? 0 : $totActualEDD.hashCode());
		final java.lang.Object $totPipelineDisb = this.getTotPipelineDisb();
		result = result * PRIME + ($totPipelineDisb == null ? 0 : $totPipelineDisb.hashCode());
		final java.lang.Object $totPipelineComm = this.getTotPipelineComm();
		result = result * PRIME + ($totPipelineComm == null ? 0 : $totPipelineComm.hashCode());
		final java.lang.Object $totPipelineExp = this.getTotPipelineExp();
		result = result * PRIME + ($totPipelineExp == null ? 0 : $totPipelineExp.hashCode());
		final java.lang.Object $totPipelineDisbOrder = this.getTotPipelineDisbOrder();
		result = result * PRIME + ($totPipelineDisbOrder == null ? 0 : $totPipelineDisbOrder.hashCode());
		final java.lang.Object $totPipelineReleaseOfFunds = this.getTotPipelineReleaseOfFunds();
		result = result * PRIME + ($totPipelineReleaseOfFunds == null ? 0 : $totPipelineReleaseOfFunds.hashCode());
		final java.lang.Object $totPipelineEDD = this.getTotPipelineEDD();
		result = result * PRIME + ($totPipelineEDD == null ? 0 : $totPipelineEDD.hashCode());
		final java.lang.Object $totOdaSscComm = this.getTotOdaSscComm();
		result = result * PRIME + ($totOdaSscComm == null ? 0 : $totOdaSscComm.hashCode());
		final java.lang.Object $totBilateralSscComm = this.getTotBilateralSscComm();
		result = result * PRIME + ($totBilateralSscComm == null ? 0 : $totBilateralSscComm.hashCode());
		final java.lang.Object $totTriangularSscComm = this.getTotTriangularSscComm();
		result = result * PRIME + ($totTriangularSscComm == null ? 0 : $totTriangularSscComm.hashCode());
		final java.lang.Object $totalCommitments = this.getTotalCommitments();
		result = result * PRIME + ($totalCommitments == null ? 0 : $totalCommitments.hashCode());
		final java.lang.Object $unDisbursementsBalance = this.getUnDisbursementsBalance();
		result = result * PRIME + ($unDisbursementsBalance == null ? 0 : $unDisbursementsBalance.hashCode());
		final java.lang.Object $totalMtef = this.getTotalMtef();
		result = result * PRIME + ($totalMtef == null ? 0 : $totalMtef.hashCode());
		result = result * PRIME + (this.isDebug() ? 79 : 97);
		return result;
	}
	
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FundingCalculationsHelper(fundDetailList=" + this.getFundDetailList() + ", totPlanDisb=" + this.getTotPlanDisb() + ", totPlannedComm=" + this.getTotPlannedComm() + ", totPlannedExp=" + this.getTotPlannedExp() + ", totPlannedDisbOrder=" + this.getTotPlannedDisbOrder() + ", totPlannedReleaseOfFunds=" + this.getTotPlannedReleaseOfFunds() + ", totPlannedEDD=" + this.getTotPlannedEDD() + ", totActualComm=" + this.getTotActualComm() + ", totActualDisb=" + this.getTotActualDisb() + ", totActualExp=" + this.getTotActualExp() + ", totActualDisbOrder=" + this.getTotActualDisbOrder() + ", totActualReleaseOfFunds=" + this.getTotActualReleaseOfFunds() + ", totActualEDD=" + this.getTotActualEDD() + ", totPipelineDisb=" + this.getTotPipelineDisb() + ", totPipelineComm=" + this.getTotPipelineComm() + ", totPipelineExp=" + this.getTotPipelineExp() + ", totPipelineDisbOrder=" + this.getTotPipelineDisbOrder() + ", totPipelineReleaseOfFunds=" + this.getTotPipelineReleaseOfFunds() + ", totPipelineEDD=" + this.getTotPipelineEDD() + ", totOdaSscComm=" + this.getTotOdaSscComm() + ", totBilateralSscComm=" + this.getTotBilateralSscComm() + ", totTriangularSscComm=" + this.getTotTriangularSscComm() + ", totalCommitments=" + this.getTotalCommitments() + ", unDisbursementsBalance=" + this.getUnDisbursementsBalance() + ", totalMtef=" + this.getTotalMtef() + ", debug=" + this.isDebug() + ")";
	}
}