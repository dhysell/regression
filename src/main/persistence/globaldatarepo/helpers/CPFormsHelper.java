package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPForms;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CPFormsHelper {
	
	public static CPForms getCommercialPropertyFormByName(String formName) throws Exception {	
		System.out.println("TRYING TO GET FORM NAME:");
		System.out.println(formName);
		System.out.println("************************");
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("name");
			Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(formName) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			CPForms form = results.get(index);
			//increment use counter
			form.setUseCount(form.getUseCount() + 1);
			session.update(form);
            
            session.getTransaction().commit();
            
            return form;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static List<CPForms> getAllCommercialPropertyForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			formCriteria.select(formCriteriaRoot);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			            
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
	
	public static List<CPForms> getAllCommercialPropertyForm(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			formCriteria.select(formCriteriaRoot);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			
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
	
	
	
	public static CPForms getRandomCommercialPropertyForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			formCriteria.select(formCriteriaRoot);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			
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
	
	
	public static List<CPForms> getCommercialPropertyChangeForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("canFormChange");
			Predicate formNameLike = builder.like(formNameExpression, "Yes");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			            
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
	
	
	public static List<CPForms> getCommercialPropertyChangeForm(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPForms> formCriteria = builder.createQuery(CPForms.class);
			Root<CPForms> formCriteriaRoot = formCriteria.from(CPForms.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("canFormChange");
			Predicate formNameLike = builder.like(formNameExpression, "Yes");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<CPForms> results = session.createQuery(formCriteria).getResultList();
			
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
}
