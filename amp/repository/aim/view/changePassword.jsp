<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimChangePasswordForm" />
<script language="JavaScript">
function isVoid(name){
	if ( name == null ||name.length==0){
    	return true;
    }		
	if (!isNaN(name)){
    	return 2;
    }		
	return false;		
}

function validate(){
        var email = document.aimChangePasswordForm.userId.value;
        var oldpassword = document.aimChangePasswordForm.oldPassword.value;
        var password = document.aimChangePasswordForm.newPassword.value;
        var passwordConfirmation = document.aimChangePasswordForm.confirmNewPassword.value;
        var errors=new Array();
        if(isVoid(email)){
        	errors.push('<digi:trn jsFriendly="true">Email is required</digi:trn>');
        }
        else{
        	 var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
     		 if(!reg.test(email)){
     			errors.push('<digi:trn jsFriendly="true">you must enter Valid email please check in</digi:trn>');
     		 }
        }
        if(isVoid(oldpassword)){
        	errors.push('<digi:trn jsFriendly="true">Old password is required</digi:trn>');
        }
        if(isVoid(password)){
        	errors.push('<digi:trn jsFriendly="true">New password is required</digi:trn>');
        }  
        if(password != passwordConfirmation){
        	errors.push('<digi:trn jsFriendly="true">Passwords in both fields must be the same</digi:trn>');
        }
        if(errors.length>0){
        	 alert(errors.join('\n'));
        	 return false;
        }
        return true;
	}

	</script>

<digi:form action="/changePassword.do" method="post" onsubmit="return validate();">
<div class="reg_form_container">
<div class="home_sec_title"><digi:trn key="aim:changePassword">
						Change Password
						</digi:trn></div>
						
<table width="100%" valign="top" align="left" cellpadding="0" cellspacing="0" border="0">
<tr>
<td width="100%" valign="top" align="left">
<table bgColor=#ffffff border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td align=left valign="top" width="60%">
			<digi:errors/>
		</td>
	</tr>
	<tr>
		<td width="5%">&nbsp;
		</td>
		<td align=left valign="top" width="60%"><br>
			<table border="0" cellPadding=5 cellspacing="0" width="100%">
				
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap width="40%" valign=top>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:email">
							UserId:
						</digi:trn>
					</td>
					</td>
					<td align="left">
						<html:text property="userId" size="20" /><br>
						<font color="red" style="font-size:11px;">
						<digi:trn key="aim:userIdExample1">
						e.g. yourname@emailaddress.com
						</digi:trn>
						</font>
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:oldPassword">
						Old Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="oldPassword" size="20" />
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:newPassword">
						New Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="newPassword" size="20" />
					</td>
				</tr>
				<tr>
					<td width="3">&nbsp;</td>
					<td align=right class=f-names noWrap>
<!--						<digi:img src="module/aim/images/arrow-th-BABAB9.gif" width="16"/>-->
						<digi:trn key="aim:confirmNewPassword">
						Confirm new Password:
						</digi:trn>
					</td>
					<td align="left">
						<html:password property="confirmNewPassword" size="20" />
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td align="left">

						<html:submit styleClass="buttonx"><digi:trn key="btn:submit">Submit</digi:trn></html:submit>
					</td>
				</tr>

				<tr>
					<td colspan=3>&nbsp;</td>
				</tr>

			</table>
		</td>
		<td width="5%">&nbsp;
		</td>
		<td bgcolor="#dbe5f1" valign="top" width="30%">
	      <table align="center" border="0" cellPadding=3 cellspacing="0" width="90%">
        		<tr>
		          <td valign="top">&nbsp;</td>
        		</tr>
        		<tr>
	          	<td valign="top">
                <span class="formnote">
						<digi:trn key="aim:loginWarning">
						 You are signing-in to one or more secure applications for
        			    official business. You have been granted the right to access these
          		 	 applications and the information contained in them to facilitate
           			 your official business. Your accounts and passwords are your
						 responsibility. Do not share them with anyone.
						 </digi:trn>
						<BR><BR>
				</span>
          		</td>
  				</tr>
        		<tr>
          		<td valign="top">&nbsp;</td>
  				</tr>
	      </table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</div>
</digi:form>



