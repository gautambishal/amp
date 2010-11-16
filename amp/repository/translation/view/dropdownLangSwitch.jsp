
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
		<html:select property="referUrl" onchange="SwithLanguage(this)">
			<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
				<bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String"/>
				<html:option value='<%= langReferUrl %>'>
					<digi:trn key="aim:${languages.langName}"><bean:write name="languages" property="langName"/></digi:trn>
				</html:option>
			</logic:iterate>
		</html:select>
	</logic:present>
	<!-- 
	<digi:instance property="translationForm"/>
	<logic:present name="translationForm" property="languages">
	<ul class="wks_menu" id="wks_menu">
		<li style="margin-right: 0px;padding-left: 0px;padding-right: 0px;height: 0px;text-transform: uppercase;">
			<a href="">
				LANGUAGE						
			</a>
			<ul style="width: auto;">
				<logic:iterate id="languages" name="translationForm" property="languages" type="org.digijava.module.translation.form.TranslationForm.TranslationInfo">
				<bean:define id="langReferUrl" name="languages" property="referUrl" type="java.lang.String"/>						
                 <li>
					<a href="" >
                    	<digi:trn key="aim:${languages.langName}"><bean:write name="languages" property="langName"/></digi:trn>
                    </a>
                  </li>
				</logic:iterate>
		</ul>
		</li>
	</ul>
	</logic:present>
	 -->
<script>function SwithLanguage(obj) {
  var refer = obj.value;
  document.location.href = refer;
}
</script>
