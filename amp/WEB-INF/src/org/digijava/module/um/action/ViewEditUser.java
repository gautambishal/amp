package org.digijava.module.um.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.UserLangPreferences;
import org.digijava.kernel.entity.UserPreferences;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpUserExtension;
import org.digijava.module.aim.dbentity.AmpUserExtensionPK;
import org.digijava.module.aim.helper.CountryBean;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.um.form.ViewEditUserForm;
import org.digijava.module.um.util.AmpUserUtil;
import org.digijava.module.um.util.DbUtil;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.Constants;

public class ViewEditUser extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ViewEditUserForm uForm = (ViewEditUserForm) form;
        User user = null;
        HttpSession session = request.getSession();
        String isAmpAdmin = (String) session.getAttribute("ampAdmin");
        ActionErrors errors = new ActionErrors();
        Site curSite = RequestUtils.getSite(request);
        UserLangPreferences langPref = null;
        Long userId = uForm.getId();

        if (userId != null) {
            user = UserUtils.getUser(userId);
        } else if (uForm.getEmail() != null) {
            user = UserUtils.getUserByEmail(uForm.getEmail());
        }else{
            return mapping.findForward("forward");
        }


        try {
            langPref = UserUtils.getUserLangPreferences(user, curSite);
        } catch (DgException ex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(ex.getMessage()));
            saveErrors(request, errors);
        }

        Boolean isBanned = uForm.getBan();
        if (isBanned != null) {
            if (isAmpAdmin.equalsIgnoreCase("yes")) {
            	if (isBanned) {
            		List ampTeamMembers	= TeamMemberUtil.getAmpTeamMembersbyDgUserId(userId);
            		if ( ampTeamMembers != null && ampTeamMembers.size() == 0 ) {
            			user.setBanned(true);
            			DbUtil.updateUser(user);
            		}
            		if ( ampTeamMembers != null && ampTeamMembers.size() > 0 ) {
            			String teamNames	= "";
            			Iterator iter		= ampTeamMembers.iterator();
            			while ( iter.hasNext() ) {
	            			AmpTeamMember atm	= (AmpTeamMember) iter.next();
	            			AmpTeam team		= atm.getAmpTeam();
	            			if (team != null && team.getName() != null)  {
	            				if (teamNames.length() == 0)
	            					teamNames	+= "'" + team.getName() + "'";
	            				else
	            					teamNames	+= ", '" + team.getName() + "'";
	            			}
            			}
            			errors.add("title",
                                new ActionError("error.um.userIsInTeams", teamNames));
            		}
            		if ( ampTeamMembers == null ) {
            			errors.add("title",
                                new ActionError("error.um.errorBanning"));
            		}
            	}
            	else {
            		user.setBanned(false);
            		DbUtil.updateUser(user);
            	}
                /*user.setBanned(isBanned);
                if (isBanned) {
                    Site site = RequestUtils.getSite(request);
                    List teamMembersId = TeamMemberUtil.getTeamMemberbyUserId(userId);
                    if (teamMembersId != null && teamMembersId.size() != 0) {
                        Long[] memberToRemove = new Long[teamMembersId.size()];
                        teamMembersId.toArray(memberToRemove);
                        TeamMemberUtil.removeTeamMembers(memberToRemove, site.getId());
                    }

                }
                DbUtil.updateUser(user);*/
            } else {
                errors.add(ActionErrors.GLOBAL_ERROR,
                           new ActionError("error.um.banUserInvalidPermission"));
            }
            resetViewEditUserForm(uForm);
            saveErrors(request, errors);
            return mapping.findForward("saved");
        }
        if (uForm.getEvent() == null) {

            Locale navLang = RequestUtils.getNavigationLanguage(request);
            if (navLang != null) {
                Collection<CountryBean> countrieCol = org.digijava.module.aim.util.DbUtil.getTranlatedCountries(request);
                if (countrieCol != null) {
                    uForm.setCountries(countrieCol);
                }

                Collection userLangs = TrnUtil.getSortedUserLanguages(request);
                uForm.setLanguages(userLangs);
            }

            Collection<AmpOrgType> orgTypeCol = DbUtil.getAllOrgTypes();
            if (orgTypeCol != null) {
                uForm.setOrgTypes(orgTypeCol);
            }

            uForm.setMailingAddress(null);
            uForm.setFirstNames(null);
            uForm.setLastName(null);
            uForm.setName(null);
            uForm.setUrl(null);
            uForm.setSelectedCountryIso(null);
            uForm.setSelectedLanguageCode(null);
            uForm.setSelectedOrgName(null);

            uForm.setSelectedOrgGroupId(null);
            uForm.setSelectedOrgTypeId(null);
            uForm.setSelectedCountryIso(null);
            uForm.setAssignedOrgId(null);

            uForm.setId(null);
            uForm.setEmail(null);
            uForm.setConfirmNewPassword(null);
            uForm.setNewPassword(null);
            uForm.setDisplaySuccessMessage(null);

            if (user != null) {
                uForm.setMailingAddress(user.getAddress());
                AmpUserExtension userExt = AmpUserUtil.getAmpUserExtension(user);

                if (user.getCountry() != null) {
                    uForm.setSelectedCountryIso(user.getCountry().getIso());
                }

                uForm.setId(user.getId());
                uForm.setEmail(user.getEmail());
                uForm.setFirstNames(user.getFirstNames());
                uForm.setId(user.getId());
                uForm.setLastName(user.getLastName());
                uForm.setName(user.getName());
                uForm.setUrl(user.getUrl());
                uForm.setAssignedOrgId(user.getAssignedOrgId());
                if(user.getAssignedOrgId()!=null) {
                    uForm.setOrgs(new ArrayList<AmpOrganisation>());
                    AmpOrganisation organization = org.digijava.module.aim.util.DbUtil.getOrganisation(user.getAssignedOrgId());
                    if(organization != null){
                    	uForm.getOrgs().add(organization);
                    }

                }


                Locale language = null;
                if (langPref == null) {
                    language = user.getRegisterLanguage();
                } else {
                    language = langPref.getAlertsLanguage();
                }

                uForm.setSelectedLanguageCode(language.getCode());
                uForm.setSelectedOrgName(user.getOrganizationName());

                if (userExt!=null){
                	if (userExt.getOrgGroup()!=null){
                		uForm.setSelectedOrgGroupId(userExt.getOrgGroup().getAmpOrgGrpId());
                	}
                	if (userExt.getOrgType()!=null){
                		uForm.setSelectedOrgTypeId(userExt.getOrgType().getAmpOrgTypeId().toString());
                	}
                	if (userExt.getOrganization()!=null){
                		uForm.setSelectedOrgName(userExt.getOrganization().getName());
                		uForm.setSelectedOrgId(userExt.getOrganization().getAmpOrgId());
                	}

                }

//                if (user.getOrganizationName() != null &&
//                    user.getOrganizationName().length() != 0) {
//                    Collection<AmpOrganisation> orgCol = org.digijava.module.aim.util.DbUtil.getAllOrganisation();
//
//                    AmpOrganisation orgnisation = null;
//                    if (orgCol != null) {
//                        for (Iterator iter = orgCol.iterator(); iter.hasNext(); ) {
//                            orgnisation = (AmpOrganisation) iter.next();
//                            if (orgnisation.getName().equals(uForm.getSelectedOrgName())) {
//                                break;
//                            }
//                        }
//                    }
//
//                    Collection<AmpOrgGroup> orgGrpCol = DbUtil.getAllOrgGroup();
//                    AmpOrgGroup orgGroup = null;
//                    if (orgGrpCol != null && orgnisation != null) {
//                        for (Iterator orgGroupIter = orgGrpCol.iterator();
//                             orgGroupIter.hasNext(); ) {
//                            orgGroup = (AmpOrgGroup) orgGroupIter.next();
//                            if (orgGroup != null && orgnisation.getOrgGrpId() != null &&
//                                orgGroup.getAmpOrgGrpId().equals(orgnisation.getOrgGrpId().getAmpOrgGrpId())) {
//
//                                uForm.setSelectedOrgGroupId(orgGroup.getAmpOrgGrpId());
//                                if (orgGroup.getOrgType() != null) {
//                                    uForm.setSelectedOrgTypeId(orgGroup.getOrgType().getAmpOrgTypeId().toString());
//                                }
//                                break;
//                            }
//                        }
//                    }
//
                    uForm.setOrgTypes(DbUtil.getAllOrgTypes());
                    if (uForm.getSelectedOrgTypeId() != null) {
                        uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.getSelectedOrgTypeId())));
                    }
                    uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
//                }
            }
        } else {
            if (uForm.getEvent().equalsIgnoreCase("save")) {
                if (user != null) {
                	// TODO ideally, user, userLangPreferences, and userExtension should be saved in one transaction.
                	AmpUserExtension userExt=AmpUserUtil.getAmpUserExtension(user);
                	if (userExt==null){
                		userExt=new AmpUserExtension(new AmpUserExtensionPK(user));
                	}

                    if (userExt!=null){
            			AmpOrgType orgType=org.digijava.module.aim.util.DbUtil.getAmpOrgType(new Long(uForm.getSelectedOrgTypeId()));
            			userExt.setOrgType(orgType);
            			AmpOrgGroup orgGroup=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(uForm.getSelectedOrgGroupId());
            			userExt.setOrgGroup(orgGroup);
            			AmpOrganisation organ = org.digijava.module.aim.util.DbUtil.getOrganisation(uForm.getSelectedOrgId());
            			userExt.setOrganization(organ);
            			AmpUserUtil.saveAmpUserExtension(userExt);
                    }

                    user.setCountry(org.digijava.module.aim.util.DbUtil.getDgCountry(uForm.getSelectedCountryIso()));
                    user.setEmail(uForm.getEmail());
                    user.setFirstNames(uForm.getFirstNames());
                    user.setLastName(uForm.getLastName());
                    user.setAddress(uForm.getMailingAddress());
                    user.setOrganizationName(uForm.getSelectedOrgName());

                    user.setAssignedOrgId(uForm.getAssignedOrgId());

                    user.setUrl(uForm.getUrl());

                    SiteDomain siteDomain = (SiteDomain) request.getAttribute(Constants.CURRENT_SITE);
                    UserLangPreferences userLangPreferences = new UserLangPreferences(user, DgUtil.getRootSite(siteDomain.getSite()));

                    Locale language = new Locale();
                    language.setCode(uForm.getSelectedLanguageCode());

                    userLangPreferences.setAlertsLanguage(language);
                    userLangPreferences.setNavigationLanguage(RequestUtils.getNavigationLanguage(request));

                    user.setUserLangPreferences(userLangPreferences);

                    DbUtil.updateUser(user);

                    resetViewEditUserForm(uForm);
                    return mapping.findForward("saved");
                }

            } else {
                if (uForm.getEvent().equalsIgnoreCase("changePassword")) {

                    String newPassword = uForm.getNewPassword();
                    String confirmNewPassword = uForm.getConfirmNewPassword();
                    if (confirmNewPassword == null || newPassword == null ||
                        newPassword.trim().length() == 0 ||
                        confirmNewPassword.trim().length() == 0) {
                        errors.add(ActionErrors.GLOBAL_ERROR,
                                   new ActionError("error.update.blankFields"));
                        saveErrors(request, errors);
                    } else {
                        if (!newPassword.equals(confirmNewPassword)) {
                            errors.add(ActionErrors.GLOBAL_ERROR,
                                       new ActionError("error.update.noPasswordMatch"));
                            saveErrors(request, errors);

                        } else {
                            UserUtils.setPassword(user, newPassword);
                            DbUtil.updateUser(user);
                            uForm.setDisplaySuccessMessage(true);
                        }

                    }

                }

                else if (uForm.getEvent().equalsIgnoreCase("typeSelected")) {
                    uForm.setOrgGroups(DbUtil.getOrgGroupByType(Long.valueOf(uForm.getSelectedOrgTypeId())));
                    if (uForm.getOrgs() != null && uForm.getOrgs().size() != 0) {
                        uForm.getOrgs().clear();
                    }
                } else if (uForm.getEvent().equalsIgnoreCase("groupSelected")) {
                    uForm.setOrgs(DbUtil.getOrgByGroup(uForm.getSelectedOrgGroupId()));
                }
            }
        }
        resetViewEditUserForm(uForm);
        return mapping.findForward("forward");
    }

    public ViewEditUser() {
    }

    public void resetViewEditUserForm(ViewEditUserForm uForm) {
        if (uForm != null) {
            uForm.setBan(null);
            uForm.setEvent(null);
            uForm.setNewPassword(null);
            uForm.setConfirmNewPassword(null);
        }
    }
}
