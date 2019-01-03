package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GLUWIssues;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GLUWIssuesHelper {
	
	public static GLUWIssues getRandomUWIssue() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLUWIssues uwIssue = results.get(index);
            
            session.getTransaction().commit();
            
            return uwIssue;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<GLUWIssues> getALLUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static GLUWIssues getUWIssueByRuleMessage(String ruleMessage) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> ruleMessageExpression = uwIssueCriteriaRoot.get("ruleMessage");
			Predicate ruleMessageLike = builder.like(ruleMessageExpression, "%" + StringsUtils.specialCharacterEscape(ruleMessage) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleMessageLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLUWIssues uwIssue = results.get(index);
            
            session.getTransaction().commit();
            
            return uwIssue;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<GLUWIssues> getUWByRuleToApply(String ruleToApply) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> ruleToApplyExpression = uwIssueCriteriaRoot.get("ruleToApply");
			Predicate ruleToApplyLike = builder.like(ruleToApplyExpression, "%" + StringsUtils.specialCharacterEscape(ruleToApply) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleToApplyLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<GLUWIssues> getUWByBlockingType(String blockingType) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%" + StringsUtils.specialCharacterEscape(blockingType) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<GLUWIssues> getInformationalUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	
	public static List<GLUWIssues> getInformationalUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<GLUWIssues> getAllBlockQuoteReleaseUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	public static List<GLUWIssues> getAllBlockQuoteReleaseUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<GLUWIssues> getAllBlockBindUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	
	public static List<GLUWIssues> getAllBlockBindUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUWIssues> uwIssueCriteria = builder.createQuery(GLUWIssues.class);
			Root<GLUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(GLUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("resultAndBlockingPoints");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<GLUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
