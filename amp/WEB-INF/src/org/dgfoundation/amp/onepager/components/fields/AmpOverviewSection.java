package org.dgfoundation.amp.onepager.components.fields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.RangeValidator;
import org.dgfoundation.amp.onepager.AmpAuthWebSession;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.AmpRequiredComponentContainer;
import org.dgfoundation.amp.onepager.components.features.sections.AmpIdentificationFormSectionFeature;
import org.dgfoundation.amp.onepager.components.features.tables.AmpOverallFundingTotalsTable;
import org.dgfoundation.amp.onepager.events.OverallFundingTotalsEvents;
import org.dgfoundation.amp.onepager.events.UpdateEventBehavior;
import org.dgfoundation.amp.onepager.models.AmpCategoryValueByKeyModel;
import org.dgfoundation.amp.onepager.util.ActivityUtil;
import org.dgfoundation.amp.onepager.util.AmpFMTypes;
import org.dgfoundation.amp.onepager.util.AttributePrepender;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpOverviewSection extends AmpComponentPanel<Void> implements AmpRequiredComponentContainer {
	private static final long serialVersionUID = 3042844165981373432L;
	protected IndicatingAjaxLink button;
	private List<FormComponent<?>> requiredFormComponents = new ArrayList<FormComponent<?>>();

	public AmpOverviewSection(String id, String fmName, IModel<AmpActivityVersion> am) throws Exception {
		super(id, fmName);
		
		//create a propose project cost section

        AmpAuthWebSession wicketSession = (AmpAuthWebSession) org.apache.wicket.Session.get();
        TeamMember tm = wicketSession.getCurrentMember();
		
		final WebMarkupContainer wmc = new WebMarkupContainer("fundingOverviewContainter");
		wmc.setOutputMarkupId(true);
		add(wmc);
		
		if (FeaturesUtil
				.getGlobalSettingValueBoolean(GlobalSettingsConstants.ACTIVITY_FORM_FUNDING_SECTION_DESIGN)) {
					wmc.add(new AttributeAppender("class", new Model<String>("fundingOverviewDiv"), ""));
		} else {
			wmc.add(new AttributeAppender("class", new Model<String>("fundingOverviewDiv fundingOverviewDivNoTabs"), ""));
		}
		
		
		final IModel<Set<AmpFundingAmount>> costsModel = new PropertyModel<Set<AmpFundingAmount>>(am,
				"costAmounts");
		if (costsModel.getObject() == null)
			costsModel.setObject(new HashSet<AmpFundingAmount>());
		
        AmpProposedProjectCost propProjCost = new AmpProposedProjectCost(
				"propProjCost", "Proposed Project Cost", am, costsModel, AmpFundingAmount.FundingType.PROPOSED);
        propProjCost.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
		wmc.add(propProjCost);
		getRequiredFormComponents().addAll(
				propProjCost.getRequiredFormComponents());
		
		AmpProjectCost revisedProjCost = new AmpProjectCost(
				"revisedProjCost", "Revised Project Cost", am, costsModel, AmpFundingAmount.FundingType.REVISED);
		revisedProjCost.add(new AttributePrepender("data-is_tab", new Model<String>("true"), ""));
		wmc.add(revisedProjCost);
		getRequiredFormComponents().addAll(
				revisedProjCost.getRequiredFormComponents());
		
		RangeValidator<Integer> rangeValidator = new RangeValidator<Integer>(1,
				10);
		AttributeModifier attributeModifier = new AttributeModifier("size",
				new Model(1));
		AmpTextFieldPanel<Integer> fundingSourcesNumberPanel = new AmpTextFieldPanel<Integer>(
				"fundingSourcesNumber", new PropertyModel<Integer>(am,
						"fundingSourcesNumber"),
				CategoryConstants.FUNDING_SOURCES_NUMBER_NAME,
				AmpFMTypes.MODULE);
		fundingSourcesNumberPanel.getTextContainer().add(rangeValidator);
		fundingSourcesNumberPanel.getTextContainer().add(attributeModifier);
		wmc.add(fundingSourcesNumberPanel);

		AmpCategorySelectFieldPanel typeOfCooperation = new AmpCategorySelectFieldPanel("typeOfCooperation",
				CategoryConstants.TYPE_OF_COOPERATION_KEY,
				new AmpCategoryValueByKeyModel(new PropertyModel<Set<AmpCategoryValue>>(am, "categories"),
						CategoryConstants.TYPE_OF_COOPERATION_KEY),
				CategoryConstants.TYPE_OF_COOPERATION_NAME, true, false, null, AmpFMTypes.MODULE);
		wmc.add(typeOfCooperation);

		AmpCategorySelectFieldPanel typeOfImplementation = new AmpCategorySelectFieldPanel("typeOfImplementation",
				CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY,
				new AmpCategoryValueByKeyModel(new PropertyModel<Set<AmpCategoryValue>>(am, "categories"),
						CategoryConstants.TYPE_OF_IMPLEMENTATION_KEY),
				CategoryConstants.TYPE_OF_IMPLEMENTATION_NAME, true, false, null, AmpFMTypes.MODULE);
		wmc.add(typeOfImplementation);

		AmpCategorySelectFieldPanel modalities = new AmpCategorySelectFieldPanel("modalities",
				CategoryConstants.MODALITIES_KEY,
				new AmpCategoryValueByKeyModel(new PropertyModel<Set<AmpCategoryValue>>(am, "categories"),
						CategoryConstants.MODALITIES_KEY),
				CategoryConstants.MODALITIES_NAME, true, false, null, AmpFMTypes.MODULE);

		modalities.getChoiceContainer().add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			{
				updateOtherInfo();
			}

			private void toggleOtherInfo(boolean b) {
				if (this.getFormComponent() != null) {
				this.getFormComponent().getParent().getParent().getParent().getParent().getParent().getParent()
						.visitChildren(AmpIdentificationFormSectionFeature.class,
						new IVisitor<AmpIdentificationFormSectionFeature, Void>() {
							@Override
							public void component(AmpIdentificationFormSectionFeature component,
												  IVisit<Void> visit) {
								component.get("otherInfo").setVisible(b);
								visit.dontGoDeeper();
							}
						});
				}
			}

			private void updateFields(AjaxRequestTarget target) {
				this.getFormComponent().getParent().getParent().getParent().getParent().getParent().getParent()
						.visitChildren(AmpIdentificationFormSectionFeature.class,
								new IVisitor<AmpIdentificationFormSectionFeature, Void>() {
									@Override
									public void component(AmpIdentificationFormSectionFeature component,
														  IVisit<Void> visit) {
										target.add(component.get("otherInfo"));
										target.add(component.get("otherInfo").getParent());
										visit.dontGoDeeper();
									}
								});
			}

			private void updateOtherInfo() {
				toggleOtherInfo(AmpIdentificationFormSectionFeature.isOtherInfoVisible(am));
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateOtherInfo();
				updateFields(target);
			}
		});
		wmc.add(modalities);
		
		AmpOverallFundingTotalsTable overallFunding = new AmpOverallFundingTotalsTable(
				"overallFunding", "Overall Funding Totals", new PropertyModel<Set<AmpFunding>>(am, "funding"));
		overallFunding.add(UpdateEventBehavior.of(OverallFundingTotalsEvents.class));

		wmc.add(overallFunding);
	}
	 
	/**
	 * Return a list of FormComponent that are marked as required for this panel
	 * 
	 * @return List<FormComponent<?>> with the FormComponent
	 */
	public List<FormComponent<?>> getRequiredFormComponents() {
		return requiredFormComponents;
	}
}


