package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.ActivityPatterns;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class ActivityPatternsHelper {

	
	public static ActivityPatterns getActivityBySubject(String subject) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ActivityPatterns> formCriteria = builder.createQuery(ActivityPatterns.class);
			Root<ActivityPatterns> formCriteriaRoot = formCriteria.from(ActivityPatterns.class);
			Expression<String> formNameRestriction = formCriteriaRoot.get("userName");
			Predicate formNameRestrictionLike = builder.like(formNameRestriction, "%" + StringsUtils.specialCharacterEscape(subject) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameRestrictionLike);
			
			List<ActivityPatterns> results = session.createQuery(formCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			ActivityPatterns form = results.get(index);
            
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
	
	public static ActivityPatterns getActivityByDescription(String description) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ActivityPatterns> formCriteria = builder.createQuery(ActivityPatterns.class);
			Root<ActivityPatterns> formCriteriaRoot = formCriteria.from(ActivityPatterns.class);
			Expression<String> formNameRestriction = formCriteriaRoot.get("description");
			Predicate formNameRestrictionLike = builder.like(formNameRestriction, "%" + StringsUtils.specialCharacterEscape(description) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameRestrictionLike);
			
			List<ActivityPatterns> results = session.createQuery(formCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			ActivityPatterns form = results.get(index);
            
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
	
	
	public static List<ActivityPatterns> getAllActivities() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ActivityPatterns> formCriteria = builder.createQuery(ActivityPatterns.class);
			Root<ActivityPatterns> formCriteriaRoot = formCriteria.from(ActivityPatterns.class);
			formCriteria.select(formCriteriaRoot);
			
			List<ActivityPatterns> results = session.createQuery(formCriteria).getResultList();
            
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
}
