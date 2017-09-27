package org.digijava.module.dataExchange.util;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpInterchangeableResult;
import org.hibernate.Query;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/17/14
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {

    public static List<AmpInterchangeableResult> getInterchangeResult(Date date, Integer offset, Integer pageSize, String order) {
        String query = "SELECT r from " + AmpInterchangeableResult.class.getName() + " r ";
        if(date != null) {
            query += "WHERE r.date = :date ";
        }
        if(order != null) {
            query += "order by r." + order;
        }
        Query qry = PersistenceManager.getRequestDBSession().createQuery(query);
        if(date != null) {
            qry.setParameter("date", date);
        }
        if(pageSize != -1) {
            qry.setMaxResults(pageSize);
        }
        qry.setFirstResult(offset);

        return qry.list();
    }

    public static Integer getInterchangeResultCount(Date date) {
        String query = "SELECT count(r) from " + AmpInterchangeableResult.class.getName() + " r ";
        if(date != null) {
            query += "WHERE r.date = :date";
        }
        Query qry = PersistenceManager.getRequestDBSession().createQuery(query);
        if(date != null) {
            qry.setParameter("date", date);
        }
        return (Integer) qry.list().get(0);
    }

    public static void deleteResult(Set<AmpInterchangeableResult> results) {
        for (AmpInterchangeableResult result: results) {
            PersistenceManager.getRequestDBSession().delete(result);
        }

    }
}
