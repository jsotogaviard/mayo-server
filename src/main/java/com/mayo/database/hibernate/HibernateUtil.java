package com.mayo.database.hibernate;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mayo.MayoException;
 
public class HibernateUtil {
 
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new AnnotationConfiguration()
            		.configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static <T> List<T> list(T clazz) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();

		@SuppressWarnings("unchecked")
		List<T> employees = session.createQuery("from " + clazz.getClass().getName()).list();
		session.close();
		return employees;
	}
    
	@SuppressWarnings("unchecked")
	public static <T> List<T> search(T clazz, Map<String,Object> slicers) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();

		StringBuilder sb = new StringBuilder();
		sb.append("from ");
		sb.append(clazz.getClass().getName());
		sb.append(" where ");
		for (Entry<String, Object> entry : slicers.entrySet()) {
			sb.append(entry.getKey());
			sb.append(" = ");
			if (entry.getValue() instanceof String) {
				sb.append("'" + entry.getValue() + "'");
			} else {
				sb.append(entry.getValue());
			}
			sb.append(" AND ");
			
		}
		String sql = sb.substring(0, sb.length() - " AND ".length());
		Query query = session.createQuery(sql);
		return new ArrayList<T>(query.list());
	}
	
	public static <T> T searchOne(T clazz, Map<String,Object> slicers) {
		return getOne(search(clazz, slicers));
	}
	
	public static <T> T searchOneOrNone(T clazz, Map<String,Object> slicers) {
		return getOneOrNone(search(clazz, slicers));
	}
    
	public static <T> Object save(T employee) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		Serializable id = session.save(employee);
		session.getTransaction().commit();
		session.close();
		return id;

	}

	public static <T>  T update(T employee) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.merge(employee);
		session.getTransaction().commit();
		session.close();
		return employee;

	}
	
	public static <T> void delete(T employee) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		session.createQuery("delete from " + employee.getClass().getName()).executeUpdate();
		session.close();
	}

	public static <T> T getOneOrNone(List<T> list){
		Set<T> uniqueObjects = new HashSet<T>(list);
		if (uniqueObjects.size() > 1) {
			throw new RuntimeException(list + "");
		} else if(uniqueObjects.size() == 1){
			return uniqueObjects.iterator().next();
		} else {
			return null;	
		}
	}
	
	public static <T> T getOne(List<T> list){
		Set<T> uniqueObjects = new HashSet<T>(list);
		if (uniqueObjects.size() != 1) {
			throw new MayoException(list + "");
		} else {
			return uniqueObjects.iterator().next();
		}
	}
}

