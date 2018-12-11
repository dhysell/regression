package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPSpecialClassCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CPSpecialClassCodesHelper {
	
	public static List<CPSpecialClassCodes> getAllSpecialClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPSpecialClassCodes> propertyClassCriteria = builder.createQuery(CPSpecialClassCodes.class);
			Root<CPSpecialClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPSpecialClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPSpecialClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	
	public static List<CPSpecialClassCodes> getAllSpecialClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPSpecialClassCodes> propertyClassCriteria = builder.createQuery(CPSpecialClassCodes.class);
			Root<CPSpecialClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPSpecialClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPSpecialClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	
	public static CPSpecialClassCodes getRandomSpecialClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPSpecialClassCodes> propertyClassCriteria = builder.createQuery(CPSpecialClassCodes.class);
			Root<CPSpecialClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPSpecialClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPSpecialClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			while(results.get(index).getClassStatus().equals("-")) {
				index = randomGenerator.nextInt(results.size());
			}
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
	
	public static CPSpecialClassCodes getSpecialClassCodeByCode(String code) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPSpecialClassCodes> propertyClassCriteria = builder.createQuery(CPSpecialClassCodes.class);
			Root<CPSpecialClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPSpecialClassCodes.class);
			Expression<String> classCode = propertyClassCriteriaRoot.get("classCode");
			Predicate classCodeLike = builder.like(classCode, "%" + StringsUtils.specialCharacterEscape(code) + "%");
			propertyClassCriteria.select(propertyClassCriteriaRoot).where(classCodeLike);
			
			List<CPSpecialClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
