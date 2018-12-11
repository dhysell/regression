package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.FormsCA;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CAFormsHelpers {
	
	
	public static FormsCA getCommercialAutoFormByName(String formName) throws Exception {
		System.out.println(formName);
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> formCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> formCriteriaRoot = formCriteria.from(FormsCA.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("name");
			Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(formName) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<FormsCA> results = session.createQuery(formCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			FormsCA form = results.get(index);
            
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
	
	
	public static List<FormsCA> getAllCommercialAutoForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> arUserCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> arUserCriteriaRoot = arUserCriteria.from(FormsCA.class);
			arUserCriteria.select(arUserCriteriaRoot);
			
			List<FormsCA> results = session.createQuery(arUserCriteria).getResultList();
            
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
	
	public static List<FormsCA> getAllCommercialAutoForm(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> arUserCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> arUserCriteriaRoot = arUserCriteria.from(FormsCA.class);
			arUserCriteria.select(arUserCriteriaRoot);
			
			List<FormsCA> results = session.createQuery(arUserCriteria).getResultList();
			
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
	
	
	
	public static FormsCA getRandomCommercialAutoForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> arUserCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> arUserCriteriaRoot = arUserCriteria.from(FormsCA.class);
			arUserCriteria.select(arUserCriteriaRoot);
			
			List<FormsCA> results = session.createQuery(arUserCriteria).getResultList();
			
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
	
	
	public static List<FormsCA> getCommercialAutoChangeForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> formCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> formCriteriaRoot = formCriteria.from(FormsCA.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("canFormChange");
			Predicate formNameLike = builder.like(formNameExpression, "Yes");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<FormsCA> results = session.createQuery(formCriteria).getResultList();
            
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
	
	
	public static List<FormsCA> getCommercialAutoChangeForm(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FormsCA> formCriteria = builder.createQuery(FormsCA.class);
			Root<FormsCA> formCriteriaRoot = formCriteria.from(FormsCA.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("canFormChange");
			Predicate formNameLike = builder.like(formNameExpression, "Yes");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<FormsCA> results = session.createQuery(formCriteria).getResultList();
			
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
