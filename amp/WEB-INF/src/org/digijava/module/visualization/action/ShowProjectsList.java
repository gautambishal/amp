package org.digijava.module.visualization.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.visualization.form.VisualizationForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.digijava.module.visualization.util.DashboardUtil;
import org.digijava.module.visualization.util.DbUtil;

public class ShowProjectsList extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		VisualizationForm visualizationForm = (VisualizationForm)form;
		DashboardFilter filter = visualizationForm.getFilter();
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String startYear = request.getParameter("startYear");
		String endYear = request.getParameter("endYear");
		String locale = RequestUtils.getNavigationLanguage(request).getCode();
        String siteId = RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String itemName = "";
		
		int startYearInt = 0;
		if (startYear.contains("-")) {
			startYearInt = Integer.parseInt(startYear.substring(startYear.lastIndexOf("-")+1,startYear.lastIndexOf("-")+3))+2000-1;
		} else {
			startYearInt = Integer.parseInt(startYear);
		}

		int endYearInt = 0;
		if (endYear == null || endYear.equals("null") || endYear.equals("undefined")){
			endYearInt = startYearInt;
		} else {
			if (endYear.contains("-")) {
				endYearInt = Integer.parseInt(endYear.substring(endYear.lastIndexOf("-")+1,endYear.lastIndexOf("-")+3))+2000-2;
			} else {
				endYearInt = Integer.parseInt(endYear);
			}
		}
		Long[] ids = null;
		//AidPredictability contains text instead of db ids.
		if (!type.equals("AidPredictability")) {
			if (id==null || id.length()==0) {
				return null;
			} else if (id.contains("-")){
				String[] strArr = id.split("-");
				ids = new Long[strArr.length];
				for (int i = 0; i < strArr.length; i++) {
					ids[i] = Long.parseLong(strArr[i]);
				}
			} else if (id.equalsIgnoreCase("Actual") || id.equalsIgnoreCase("Planned")){
				ids = null;
			}
			else
			{
				ids = new Long[1];
				ids[0] = Long.parseLong(id);
			}
		}
		
        JSONObject root = new JSONObject();
	    JSONArray children = new JSONArray();
	    JSONObject child = new JSONObject();
	    
	    Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (startYearInt!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYearInt);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYearInt);
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
    	}
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
        
        List<AmpActivityVersion> activities = null;
        Map<String, Map<AmpActivityVersion, BigDecimal>> itemProjectsList = new TreeMap<String, Map<AmpActivityVersion, BigDecimal>>();
        if (type.equals("RegionProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				if(!long1.equals(0l)){
					itemName = LocationUtil.getAmpLocationByCVLocation(long1).getLocation().getName();
				} else {
					try {
			        	itemName = TranslatorWorker.translateText("Unallocated", locale, siteId);
					} catch (WorkerException e) {
						itemName = "Unallocated";
					}
				}
				Long[] id1 = {long1};
				Long[] temp = filter.getSelLocationIds();
				filter.setSelLocationIds(id1);
				activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				filter.setSelLocationIds(temp);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
	    }
		if (type.equals("SectorProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = SectorUtil.getAmpSector(long1).getName();
				Long[] id1 = {long1};
				Long[] temp = filter.getSelSectorIds();
				filter.setSelSectorIds(id1);
				activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				filter.setSelSectorIds(temp);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("OrganizationProfile")){
			for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getOrganisation(long1).getName();
				Long[] id1 = {long1};
				Long[] temp = filter.getOrgIds();
				filter.setOrgIds(id1);
				activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				filter.setOrgIds(temp);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("NPOProfile")||type.equals("ProgramProfile")){
	    	for (int i = 0; i < ids.length; i++) {
				Long long1 = ids[i];
				itemName = DbUtil.getProgramById(long1).getName();
				Long[] id1 = {long1};
				filter.setSelProgramIds(id1);
				activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
				filter.setSelProgramIds(null);
				itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, ids[i].toString(), startYearInt, endYearInt));
			}
			visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("Fundings")){
			try {
				switch (Integer.parseInt(id)) {
				case Constants.COMMITMENT:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Commitments", locale, siteId);		
					break;
				case Constants.DISBURSEMENT:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Disbursements", locale, siteId);
					break;
				case Constants.EXPENDITURE:
					itemName = TranslatorWorker.translateText("ODA Historical Trend - Expenditures", locale, siteId);
					break;
				default:
					break;
				}
			} catch (WorkerException e) {
				itemName = "ODA Historical Trend";
			}
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidPredictability")){
    		HardCodedCategoryValue adjustmentType;
    		
    		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())){
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
    			itemName = TranslatorWorker.translateText("Aid Predictability - Actual", locale, siteId);
    		} else {
    			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
    			itemName = TranslatorWorker.translateText("Aid Predictability - Planned", locale, siteId);
    		}
            activities = DbUtil.getActivityList(filter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType);
            itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidType")){
			Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
	        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
				AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
				if (ampCategoryValue.getId()== Long.parseLong(id)) {
					itemName = TranslatorWorker.translateText("Aid Type - " + ampCategoryValue.getValue(), locale, siteId);
				}
			}
			activities = DbUtil.getActivityList(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		if (type.equals("AidModality")){
			Collection<AmpCategoryValue> categoryValues = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
	        for (Iterator iterator = categoryValues.iterator(); iterator.hasNext();) {
				AmpCategoryValue ampCategoryValue = (AmpCategoryValue) iterator.next();
				if (ampCategoryValue.getId()== Long.parseLong(id)) {
					itemName = TranslatorWorker.translateText("Aid Type - " + ampCategoryValue.getValue(), locale, siteId);
				}
			}
			activities = DbUtil.getActivityList(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
            itemProjectsList.put(itemName, getActivitiesValues(activities, filter, type, id, startYearInt, endYearInt));
            visualizationForm.setItemProjectsList(itemProjectsList);
		}
		/*if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new HashMap<AmpActivityVersion, BigDecimal>();
	        BigDecimal totalSum = BigDecimal.ZERO;
	        while(it.hasNext()){
	        	AmpActivityVersion act = it.next();
				newFilter.setActivityId(act.getAmpActivityId());
	        	DecimalWraper fundingCal = null;
	        	if (type.equals("FundingChart")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, Integer.parseInt(id), Constants.ACTUAL);
	        	} else if (type.equals("AidPredictability")){
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Integer.parseInt(id));
	        	} else {
	        		fundingCal = DbUtil.getFunding(newFilter, startDate, endDate, null, null, filter.getTransactionType(), Constants.ACTUAL);
	        	}
	        	BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        	totalSum = totalSum.add(total);
	        	itemProjectsList.put(act, total);
			}
	        visualizationForm.setItemProjectsList(itemProjectsList);
		}*/
		return mapping.findForward("forward");

	}
	
	private Map<AmpActivityVersion, BigDecimal> getActivitiesValues (List<AmpActivityVersion> activities, DashboardFilter filter, String type, String id, int startYearInt, int endYearInt)
	throws java.lang.Exception {
		 Date startDate = null;
        Date endDate = null;
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        if (startYearInt!=0) {
        	startDate = DashboardUtil.getStartDate(fiscalCalendarId, startYearInt);
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, endYearInt);
    	} else {
    		startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
            endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
    	}
        BigDecimal divideByMillionDenominator = new BigDecimal(1000000);
        if ("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {
            divideByMillionDenominator = new BigDecimal(1000);
        }
	    if(activities.size() > 0){
	        Iterator<AmpActivityVersion> it = activities.iterator();
	        Map<AmpActivityVersion, BigDecimal> itemProjectsList = new TreeMap<AmpActivityVersion, BigDecimal>();
	        BigDecimal totalSum = BigDecimal.ZERO;
	        while(it.hasNext()){
	        	AmpActivityVersion act = it.next();
	        	Long temp = filter.getActivityId();
	        	filter.setActivityId(act.getAmpActivityId());
	        	DecimalWraper fundingCal = null;
	        	if (type.equals("Fundings")){
	        		fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, Integer.parseInt(id), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("AidPredictability")){
	        		HardCodedCategoryValue adjustmentType;
	        		
	        		if(id.equalsIgnoreCase(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()))
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_ACTUAL;
	        		else
	        			adjustmentType = CategoryConstants.ADJUSTMENT_TYPE_PLANNED;
	        		fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), adjustmentType);
	        	} else if (type.equals("AidType")){
	        		fundingCal = DbUtil.getFunding(filter, startDate, endDate, Long.parseLong(id), null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("AidModality")){
	        		fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, Long.parseLong(id), filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	} else if (type.equals("SectorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		filter.setSelSectorIds(id1);
					fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					filter.setSelSectorIds(null);
	        	} else if (type.equals("NPOProfile")||type.equals("ProgramProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		filter.setSelProgramIds(id1);
					fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					filter.setSelProgramIds(null);
	        	} else if (type.equals("RegionProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		filter.setSelLocationIds(id1);
					fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					filter.setSelLocationIds(null);
	        	} else if (type.equals("DonorProfile")){
	        		Long[] id1 = {Long.parseLong(id)};
	        		filter.setOrgIds(id1);
					fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
					filter.setOrgIds(null);
	        	} else {
	        		fundingCal = DbUtil.getFunding(filter, startDate, endDate, null, null, filter.getTransactionType(), CategoryConstants.ADJUSTMENT_TYPE_ACTUAL);
	        	}
	        	BigDecimal total = fundingCal.getValue().divide(divideByMillionDenominator).setScale(filter.getDecimalsToShow(), RoundingMode.HALF_UP);
	        	totalSum = totalSum.add(total);
	        	itemProjectsList.put(act, total);
	        	filter.setActivityId(temp);
	        	
			}
	        return itemProjectsList;
		}
		return null;
	}
}
