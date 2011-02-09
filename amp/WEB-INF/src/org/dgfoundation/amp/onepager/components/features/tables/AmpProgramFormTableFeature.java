/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
*/
package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.dgfoundation.amp.onepager.components.fields.AbstractAmpAutoCompleteTextField;
import org.dgfoundation.amp.onepager.components.fields.AmpComboboxFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpDeleteLinkField;
import org.dgfoundation.amp.onepager.models.AmpThemeSearchModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.ProgramUtil;

/**
 * @author aartimon@dginternational.org
 * since Oct 21, 2010
 */
public class AmpProgramFormTableFeature extends AmpFormTableFeaturePanel <AmpActivity,AmpActivityProgram>{

	/**
	 * @param id
	 * @param fmName
	 * @param am
	 * @throws Exception
	 */
	public AmpProgramFormTableFeature(String id, String fmName,
			final IModel<AmpActivity> am, final String programSettingsString) throws Exception {
		super(id, am, fmName);
		final IModel<Set<AmpActivityProgram>> setModel=new PropertyModel<Set<AmpActivityProgram>>(am,"actPrograms");
		
		final AmpActivityProgramSettings programSettings = ProgramUtil.getAmpActivityProgramSettings(programSettingsString);

		AbstractReadOnlyModel<List<AmpActivityProgram>> listModel = new AbstractReadOnlyModel<List<AmpActivityProgram>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<AmpActivityProgram> getObject() {
				Set<AmpActivityProgram> allProgs = setModel.getObject();
				Set<AmpActivityProgram> specificProgs = new HashSet<AmpActivityProgram>();
				
				Iterator<AmpActivityProgram> it = allProgs.iterator();
				while (it.hasNext()) {
					AmpActivityProgram prog = (AmpActivityProgram) it.next();
					if (prog.getProgramSetting() != null && prog.getProgramSetting().getAmpProgramSettingsId() == programSettings.getAmpProgramSettingsId())
						specificProgs.add(prog);
				}
				
				return new ArrayList<AmpActivityProgram>(specificProgs);
			}
		};

		list = new ListView<AmpActivityProgram>("listProgs", listModel) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpActivityProgram> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new TextField<String>("percent",new PropertyModel<String>(item.getModel(), "programPercentage")));
				
				item.add(new Label("name", item.getModelObject().getProgram().getName()));
				
				AmpDeleteLinkField delProgram = new AmpDeleteLinkField(
						"delProgram", "Delete Program") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						setModel.getObject().remove(item.getModelObject());
						target.addComponent(listParent);
						list.removeAll();
					}
				};
				item.add(delProgram);
			}
		};
		list.setReuseItems(true);
		add(list);
		
		final AbstractAmpAutoCompleteTextField<AmpTheme> autoComplete = new AbstractAmpAutoCompleteTextField<AmpTheme>(AmpThemeSearchModel.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected String getChoiceValue(AmpTheme choice)
					throws Throwable {
				//transientBoolean used internally to flag the default theme
				if (choice.isTransientBoolean())
					return "<b>" +TranslatorUtil.getTranslatedText("Default program") + ":</b> " + choice.getName();
				else
					return choice.getName();
			}

			@Override
			public void onSelect(AjaxRequestTarget target,
					AmpTheme choice) {
				/*
				 * if the default program has been selected
				 * since it is a fake AmpTheme we need to load it from the db
				 */
				if (choice.isTransientBoolean()){
					AmpActivityProgramSettings aaps;
					try {
						aaps = ProgramUtil.getAmpActivityProgramSettings(programSettingsString);
					} catch (DgException e) {
						logger.error(e);
						return;
					}
					choice = aaps.getDefaultHierarchy();
				}
				
				AmpActivityProgram aap = new AmpActivityProgram();
				aap.setActivity(am.getObject());
				aap.setProgram(choice);
				aap.setProgramSetting(programSettings);
				
				setModel.getObject().add(aap);

				list.removeAll();
				target.addComponent(list.getParent());
			}

			@Override
			public Integer getChoiceLevel(AmpTheme choice) {
				int i=0;
				AmpTheme c = choice;
				while(c.getParentThemeId()!=null) {
					i++;
					c = c.getParentThemeId();
				}
				return i;

			}
		};
		//
		autoComplete.getModelParams().put(AmpThemeSearchModel.PARAM.PROGRAM_TYPE, programSettingsString); 
		final AmpComboboxFieldPanel<AmpTheme> searchOrgs=new AmpComboboxFieldPanel<AmpTheme>("search", "Add Program", autoComplete);
		add(searchOrgs);
	}

}
