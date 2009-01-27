<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:include page="PrepareQuery.jsp"/>

<jp:mondrianQuery 
	id="query01" 
	dataSource="ampDS"  
	catalogUri="/WEB-INF/queries/AMP.xml"
	dynResolver="org.digijava.module.mondrian.query.SchemaManager"
>

SELECT {[Measures].[Raw Actual Commitments], [Measures].[Raw Actual Disbursements],[Measures].[Raw Planned Commitments], [Measures].[Raw Planned Disbursements]} ON COLUMNS,
{[Donor Dates].[All Periods]} ON ROWS 
FROM [Donor Funding]
</jp:mondrianQuery>





