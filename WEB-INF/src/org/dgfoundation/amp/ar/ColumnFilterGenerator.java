/**
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpColumnsFilters;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.util.Identifiable;

/**
 * @author mihai
 * 
 */
public class ColumnFilterGenerator {

	protected static Logger logger = Logger
			.getLogger(ColumnFilterGenerator.class);

	/**
	 * Attaches hard coded filters for hard coded columns. The FUNDING columns
	 * are hard coded, because they do not appear in amp_columns table,
	 * therefore they cannot have persisted AmpColumnsFilters, thus the objects
	 * will be created manually
	 * 
	 * @param c
	 *            the object to which the hard coded filters will be attached
	 */
	public static void attachHardcodedFilters(AmpColumns c) {
		c.setFilters(new HashSet<AmpColumnsFilters>());
		if (ArConstants.VIEW_DONOR_FUNDING.equals(c.getExtractorView())) {
			// TODO: example here of how to add hardcoded filters for hardcoded
			// FUNDING columns
			// AmpColumnsFilters acf=new
			// AmpColumnsFilters(c,"donorGroups","donor_group_id");
			// c.getFilters().add(acf);
			AmpColumnsFilters acf = new AmpColumnsFilters(c,"donorGroups","org_grp_id");
			c.getFilters().add(acf);
			AmpColumnsFilters acf2 = new AmpColumnsFilters(c,"donorTypes","org_type_id");
			c.getFilters().add(acf2);
			AmpColumnsFilters acf3= new AmpColumnsFilters(c,"financingInstruments","financing_instrument_id");
			c.getFilters().add(acf3);
			AmpColumnsFilters acf4= new AmpColumnsFilters(c,"typeOfAssistance","terms_assist_id");
			c.getFilters().add(acf4);
		}
		if (ArConstants.VIEW_CONTRIBUTION_FUNDING.equals(c.getExtractorView())) {
			//TODO: add filters here
			AmpColumnsFilters acf = new AmpColumnsFilters(c,"donorGroups","org_grp_id");
			c.getFilters().add(acf);
			AmpColumnsFilters acf1= new AmpColumnsFilters(c,"typeOfAssistance","terms_assist_id");
			c.getFilters().add(acf1);
		}
		if (ArConstants.VIEW_COMPONENT_FUNDING.equals(c.getExtractorView())) {
			//TODO: add filters here	
			//AmpColumnsFilters acf = new AmpColumnsFilters(c,"regions","amp_component_id");
			//c.getFilters().add(acf);
		}
		if (ArConstants.VIEW_REGIONAL_FUNDING.equals(c.getExtractorView())) {
			//TODO: add filters here
			AmpColumnsFilters acf = new AmpColumnsFilters(c,"regionSelected","region_id");
			c.getFilters().add(acf);
		}
	}

	/**
	 * Helper method to create the sql clause for only one property of the
	 * filter bean.
	 * 
	 * @see ColumnFilterGenerator#generateColumnFilterSQLClause(AmpARFilter,
	 *      AmpColumns, boolean)
	 * @param property
	 *            the property for which the clause is generated
	 * @param viewFieldName
	 *            the sql view field name (column name is sql view)
	 * @return the logical clause for this property
	 */
	private static String generatePropertyFilterSQLClause(Object property,
			String viewFieldName) {
		if (property instanceof Collection)
			return viewFieldName
					+ " IN ("
					+ org.dgfoundation.amp.Util
							.toCSString((Collection) property) + ")";
		if (property instanceof String)
			return viewFieldName + "='" + property + "'";
		if (property instanceof Identifiable)
			return generatePropertyFilterSQLClause(((Identifiable) property)
					.getIdentifier(), viewFieldName);
		return viewFieldName + "=" + property;
	}

	/**
	 * Generates the column filter sql query for a given column, by reading the
	 * mapped properties that can affect this column, related with the filter
	 * bean. Then the bean is queried to get the values for those properties and
	 * the SQL clause is generated
	 * 
	 * @param f
	 *            the filter bean as it is generated by the filter form
	 * @param c
	 *            the column for which the filtering SQL clause is created
	 * @param exclusive
	 *            if true, it will generate an SQL clause linked with AND
	 *            operators , otherwise it will use OR
	 * @return the complete SQL logical clause
	 */
	public static String generateColumnFilterSQLClause(AmpARFilter f, AmpColumns c,
			boolean exclusive) {
		// get all bindings between this column and possible filter properties:
		StringBuffer sb = new StringBuffer("");
		Set<AmpColumnsFilters> filters = c.getFilters();
		if (filters != null) {
			Iterator<AmpColumnsFilters> i = filters.iterator();
			while (i.hasNext()) {
				AmpColumnsFilters cf = (AmpColumnsFilters) i.next();
				try {
					Object property = PropertyUtils.getSimpleProperty(f, cf
							.getBeanFieldName());
					if (property == null)
						continue;
					sb.append((exclusive ? " AND " : " OR ")
							+ generatePropertyFilterSQLClause(property, cf
									.getViewFieldName()));
				} catch (IllegalAccessException e) {
					logger.error(e);
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					logger.error(e);
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		return sb.toString();

	}
	
	
	
	/**
	 * Append filter retrievable columns to list. those are columns that appear
	 * in the current filter as selections and for filtering to be correctly
	 * applied the content of the columns has to be present (it holds extra
	 * metadata like percentages, etc...)
	 * 
	 * @see http://bugs.digijava.org/jira/browse/AMP-3454?focusedCommentId=39811#action_39811
	 * @param extractable
	 *            the list of already extractable columns
	 * @param filter
	 * @return the no of cols added
	 */
	public static List<String> appendFilterRetrievableColumns(
			List<AmpReportColumn> extractable, AmpARFilter filter) {
		// check which columns are selected in the filter and have attached
		// columns that are filter retrievable
		List<String> addedColumnNames=new ArrayList<String>();
		TreeSet<String> colNames = new TreeSet<String>();
		Iterator<AmpReportColumn> iterator2 = extractable.iterator();
		while (iterator2.hasNext()) {
			AmpReportColumn elem = (AmpReportColumn) iterator2.next();
			colNames.add(elem.getColumn().getColumnName());
		}
		
		try {
			Query query;
			Session session = PersistenceManager.getRequestDBSession();
			query = session.createQuery("from "
					+ AmpColumnsFilters.class.getName());
			Iterator i = query.list().iterator();
			while (i.hasNext()) {
				AmpColumnsFilters cf = (AmpColumnsFilters) i.next();
				
				if(colNames.contains(cf.getColumn().getColumnName())) continue;
				
				Object property = PropertyUtils.getSimpleProperty(filter, cf
						.getBeanFieldName());
				if (property == null)
					continue;
				
				if(cf.getColumn().getFilterRetrievable()!=null && cf.getColumn().getFilterRetrievable().booleanValue()) {
					AmpReportColumn arc = new AmpReportColumn();
					arc.setColumn(cf.getColumn());
					arc.setOrderId(new String("1"));
					logger.info("Adding additional column "+cf.getColumn().getColumnName()+" because selected filter "+cf.getBeanFieldName()+" is filterRetrievable");
					extractable.add(arc);
					addedColumnNames.add(arc.getColumn().getColumnName());
				}
				
			}
	
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ARUtil.logger.error(e);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ARUtil.logger.error(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ARUtil.logger.error(e);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ARUtil.logger.error(e);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ARUtil.logger.error(e);
		}
		return addedColumnNames;
	}
}
