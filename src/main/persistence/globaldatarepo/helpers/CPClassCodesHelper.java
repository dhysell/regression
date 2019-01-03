package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPClassCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CPClassCodesHelper {
	
	public static List<CPClassCodes> getAllPropertyClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPClassCodes> propertyClassCriteria = builder.createQuery(CPClassCodes.class);
			Root<CPClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	
	public static List<CPClassCodes> getAllPropertyClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPClassCodes> propertyClassCriteria = builder.createQuery(CPClassCodes.class);
			Root<CPClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	
	public static CPClassCodes getRandomClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPClassCodes> propertyClassCriteria = builder.createQuery(CPClassCodes.class);
			Root<CPClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPClassCodes.class);
			propertyClassCriteria.select(propertyClassCriteriaRoot);
			
			List<CPClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	public static CPClassCodes getPropertyClassCodeByCode(String code) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPClassCodes> propertyClassCriteria = builder.createQuery(CPClassCodes.class);
			Root<CPClassCodes> propertyClassCriteriaRoot = propertyClassCriteria.from(CPClassCodes.class);
			Expression<String> classCode = propertyClassCriteriaRoot.get("classCode");
			Predicate classCodeLike = builder.like(classCode, "%" + StringsUtils.specialCharacterEscape(code) + "%");
			propertyClassCriteria.select(propertyClassCriteriaRoot).where(classCodeLike);
			
			List<CPClassCodes> results = session.createQuery(propertyClassCriteria).getResultList();
			
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
	
	
	public static void createNewCPClassCode(String classCode, String description, String restricted, String rated) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			CPClassCodes classCodeToCreate = new CPClassCodes();
			classCodeToCreate.setClassCode(classCode);
			classCodeToCreate.setDescription(description);
			classCodeToCreate.setRestricted(restricted);
			classCodeToCreate.setRated(rated);

			session.save(classCodeToCreate);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void createNewCPClassCode(List<CPClassCodes> classCodeList) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			session.createQuery("delete from CPClassCodes").executeUpdate();
			
			for(CPClassCodes classCode : classCodeList) {
				CPClassCodes classCodeToCreate = new CPClassCodes();
				classCodeToCreate.setClassCode(classCode.getClassCode());
				classCodeToCreate.setDescription(classCode.getDescription());
				classCodeToCreate.setRestricted(classCode.getRestricted());
				classCodeToCreate.setRated(classCode.getRated());

				session.save(classCodeToCreate);
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
