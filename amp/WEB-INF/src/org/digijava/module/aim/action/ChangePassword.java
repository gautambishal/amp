package org.digijava.module.aim.action ;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.ChangePasswordForm;
import org.digijava.module.um.util.DbUtil;

public class ChangePassword extends Action {

		  private static Logger logger = Logger.getLogger(ChangePassword.class);

		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws java.lang.Exception {


					 ChangePasswordForm cpForm = (ChangePasswordForm) form;
					 
					 ActionMessages errors = new ActionMessages();

					 logger.debug("In change password");

					 if (cpForm.getUserId() != null && cpForm.getOldPassword() != null && cpForm.getNewPassword() != null) {
								try {
										  if (DbUtil.isRegisteredEmail(cpForm.getUserId()) != true) {
													 errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.invalidUserId"));
													 saveErrors(request,errors);
													 return mapping.getInputForward();
										  }

										  if (DbUtil.isCorrectPassword(cpForm.getUserId(),cpForm.getOldPassword()) != true) {
													 errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.aim.invalidPassword"));
													 saveErrors(request,errors);
													 return mapping.getInputForward();
										  }
										  
										  DbUtil.updatePassword(cpForm.getUserId(),
																cpForm.getOldPassword(),
																cpForm.getNewPassword());
								} catch (Exception e) {
										  errors.add(ActionMessages.GLOBAL_MESSAGE,
																new ActionMessage("error.aim.cannotChangePassword"));
										  saveErrors(request,errors);
										  return mapping.getInputForward();
								}
								return mapping.findForward("success");
					 } else {
								return mapping.findForward("changePassword");
					 }
		  }
}
