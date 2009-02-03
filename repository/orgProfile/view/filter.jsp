<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script langauage="JavaScript">
    var helpBody="<digi:trn key='orgProfile:helpBpdy'>Sector Breakdown,5 Largest Projects,Regional Breakdown, Paris Declaration are rendering  data of the previous fiscal year</digi:trn>";
    var helpTitle="<digi:trn key='orgProfile:helpTitle'>Organization Profile Help</digi:trn>";
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<style>

    .tableEven {
        background-color:#dbe5f1;
        font-size:8pt;
        padding:2px;
    }

    .tableOdd {
        background-color:#FFFFFF;
        font-size:8pt;!important
        padding:2px;
    }

    .Hovered {
        background-color:#a5bcf2;
    }

    
    .toolbar{
        width: 350px;
        background: #addadd;
        background-color: #addadd;
        padding: 3px 3px 3px 3px;
        position: relative;
        top: 10px;
        left: 10px;
        bottom: 100px;

    }
</style>

<script language="javascript">
  $(document).ready(function(){

        $("#org_group_dropdown_id").change(function () {
                    var orgGroupId=$('#org_group_dropdown_id option:selected').val();
                    var partialUrl=addActionToURL('getOrganizations.do');
                    var url=partialUrl+'?orgGroupId='+orgGroupId;
                    var async=new Asynchronous();
                    async.complete=buildOrgDropDown;
                    async.call(url);
                
                });
            });

  
    function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }
    
  

            function buildOrgDropDown(status, statusText, responseText, responseXML){
                var orgSelect=document.getElementById("org_select");
                orgSelect.innerHTML=responseText;
               
           }
    function setStripsTable(tableId, classOdd, classEven) {
        var tableElement = document.getElementById(tableId);
        rows = tableElement.getElementsByTagName('tr');
        for(var i = 0, n = rows.length; i < n; ++i) {
            if(i%2 == 0)
                rows[i].className = classEven;
            else
                rows[i].className = classOdd;
        }
        rows = null;
    }
    function setHoveredTable(tableId) {

        var tableElement = document.getElementById(tableId);
        if(tableElement){
            var className = 'Hovered',
            pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
            rows      = tableElement.getElementsByTagName('tr');

            for(var i = 0, n = rows.length; i < n; ++i) {
                rows[i].onmouseover = function() {
                    this.className += ' ' + className;
                };
                rows[i].onmouseout = function() {
                    this.className = this.className.replace(pattern, ' ');

                };
            }
            rows = null;
        }



    }
</script>
<digi:instance property="orgProfOrgProfileFilterForm"/>

<digi:form action="/showOrgProfile.do">

    <!-- this is for the nice tooltip widgets -->
    <DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
    <table border="0" align="left" class="toolbar">
        <tr>
            <td colspan="10" align="center">
                <b><digi:trn>Organization Group</digi:trn>:</b>
                <html:select property="orgGroupId" styleClass="inp-text"  styleId="org_group_dropdown_id">
                    <html:optionsCollection property="orgGroups" value="ampOrgGrpId" label="orgGrpName" />
                </html:select>
            </td>
            </tr>
            <tr>
            <td><b><digi:trn key="orgProfile:filer:Organization">Organization</digi:trn></b></td>
            <td>
                <div id="org_select">
                    <html:select property="org" styleClass="inp-text" styleId="org_dropdown_id">
                    <html:option value="-1"><digi:trn>All</digi:trn></html:option>
                    <html:optionsCollection property="organizations" value="ampOrgId" label="name" />
                </html:select>
                </div>
            </td>
            <td nowrap><b><digi:trn key="orgProfile:filer:fiscalCalendar">Fiscal Year</digi:trn></b></td>
            <td align="center">
                <html:select property="year" styleClass="inp-text">
                    <html:optionsCollection property="years" label="wrappedInstance" value="wrappedInstance" />
                </html:select>
            </td>



            <td nowrap><b><digi:trn key="orgProfile:filer:Currency">Currency</digi:trn></b></td>

            <td>
                <html:select property="currency" styleClass="inp-text">
                    <html:optionsCollection property="currencies"
                                            value="ampCurrencyId" label="currencyName" />

                </html:select>
            </td>

            <td nowrap>
                <html:checkbox  property="workspaceOnly"><b><digi:trn>Show data only from workspace</digi:trn> </b></html:checkbox>
            </td>
             <td>
                <html:select property="transactionType" styleClass="inp-text">
                    <html:option value="0">Comm</html:option>
                    <html:option value="1">Disb</html:option>
                </html:select>

            </td>
            <td align="center">
                <html:submit styleClass="buton" property="apply"><digi:trn key="orgProfile:filer:Apply">Apply</digi:trn></html:submit>
            </td>
            <td  align="left">
                <digi:img  src="module/widget/images/help1.gif" onmouseover="stm([helpTitle,helpBody],Style[13])" onmouseout="htm()"/>
            </td>
        </tr>
    </table>

    <br>



</digi:form>

