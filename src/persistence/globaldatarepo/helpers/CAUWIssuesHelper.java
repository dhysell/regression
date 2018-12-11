package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CAUWIssues;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CAUWIssuesHelper {
	
	public static CAUWIssues getRandomUWIssue() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			CAUWIssues uwIssue = results.get(index);
            
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
	
	public static List<CAUWIssues> getALLUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	
	public static List<CAUWIssues> getALLUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static CAUWIssues getUWIssueByRuleMessage(String ruleMessage) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> ruleMessageExpression = uwIssueCriteriaRoot.get("ruleMessage");
			Predicate ruleMessageLike = builder.like(ruleMessageExpression, "%" + StringsUtils.specialCharacterEscape(ruleMessage) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleMessageLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			CAUWIssues uwIssue = results.get(index);
            
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
	
	public static List<CAUWIssues> getUWByRuleToApply(String ruleToApply) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> ruleToApplyExpression = uwIssueCriteriaRoot.get("ruleToApply");
			Predicate ruleToApplyLike = builder.like(ruleToApplyExpression, "%" + StringsUtils.specialCharacterEscape(ruleToApply) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleToApplyLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CAUWIssues> getUWByBlockingType(String blockingType) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%" + StringsUtils.specialCharacterEscape(blockingType) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CAUWIssues> getInformationalUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			
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
	
	
	public static List<CAUWIssues> getInformationalUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CAUWIssues> getAllBlockQuoteReleaseUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	
	public static List<CAUWIssues> getAllBlockQuoteReleaseUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CAUWIssues> getAllBlockBindUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	
	public static List<CAUWIssues> getAllBlockBindUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CAUWIssues> uwIssueCriteria = builder.createQuery(CAUWIssues.class);
			Root<CAUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CAUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CAUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
