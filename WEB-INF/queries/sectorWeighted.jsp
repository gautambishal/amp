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
select NON EMPTY {[Measures].[Raw Actual Commitments], [Measures].[Raw Actual Disbursements], [Measures].[Raw Actual Expenditures], [Measures].[Sector Percentage]} ON COLUMNS,
  NON EMPTY {([Primary Sector].[All Primary Sectors], [Activity].[All Activities])} ON ROWS
from [Donor Funding Weighted]
</jp:mondrianQuery>




<c:set var="title01" scope="session">Donor Funding Amounts Weighted to Sector Percentages</c:set>
