package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPCrimeClassCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CPCrimeClassCodesHelper {
	
	public static List<CPCrimeClassCodes> getAllCrimeClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPCrimeClassCodes> propertyClassCriteria = builder.createQuery(CPCrimeClassCodes.class);
			Root<CPCrimeClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPCrimeClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPCrimeClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
			session.getTransaction().commit();
			return results;
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static List<CPCrimeClassCodes> getAllCrimeClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPCrimeClassCodes> propertyClassCriteria = builder.createQuery(CPCrimeClassCodes.class);
			Root<CPCrimeClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPCrimeClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPCrimeClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
			session.getTransaction().commit();
			return results.subList(0, listSize);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static CPCrimeClassCodes getRandomClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPCrimeClassCodes> propertyClassCriteria = builder.createQuery(CPCrimeClassCodes.class);
			Root<CPCrimeClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPCrimeClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPCrimeClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			session.getTransaction().commit();
			return results.get(index);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static CPCrimeClassCodes getCrimeClassCodeByCode(String code) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPCrimeClassCodes> propertyClassCriteria = builder.createQuery(CPCrimeClassCodes.class);
			Root<CPCrimeClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPCrimeClassCodes.class);
			Expression<String> classCode = propertyClassCriteriaRoot.get("classCode");
			Predicate classCodeLike = builder.like(classCode, "%" + StringsUtils.specialCharacterEscape(code) + "%");
			propertyClassCriteria.select(propertyClassCriteriaRoot).where(classCodeLike);
			
			List<CPCrimeClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			session.getTransaction().commit();
			return results.get(index);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
