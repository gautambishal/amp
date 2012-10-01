package org.digijava.module.visualization.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.helper.DashboardFilter;

public class DashboardForm extends ActionForm {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private String dashboardName;
	private List<AmpDashboardGraph> dashGraphList;
	private List<AmpGraph> graphList;
	private List<AmpDashboard> dashboardList;
	private AmpDashboard dashboard;
	private Long dashboardId;
	private DashboardFilter filter; 
	private int baseType;
	
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	public List<AmpDashboardGraph> getDashGraphList() {
		return dashGraphList;
	}
	public void setDashGraphList(List<AmpDashboardGraph> dashGraphList) {
		this.dashGraphList = dashGraphList;
	}
	public List<AmpGraph> getGraphList() {
		return graphList;
	}
	public void setGraphList(List<AmpGraph> graphList) {
		this.graphList = graphList;
	}
	public List<AmpDashboard> getDashboardList() {
		return dashboardList;
	}
	public void setDashboardList(List<AmpDashboard> dashboardList) {
		this.dashboardList = dashboardList;
	}
	public AmpDashboard getDashboard() {
		return dashboard;
	}
	public void setDashboard(AmpDashboard dashboard) {
		this.dashboard = dashboard;
	}
	public Long getDashboardId() {
		return dashboardId;
	}
	public void setDashboardId(Long dashboardId) {
		this.dashboardId = dashboardId;
	}
	public DashboardFilter getFilter() {
		return filter;
	}
	public void setFilter(DashboardFilter filter) {
		this.filter = filter;
	}
	public int getBaseType() {
		return baseType;
	}
	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}
	
}
