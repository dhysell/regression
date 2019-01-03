package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPUWIssues;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CPUWIssuesHelper {
	
	
	
	
	
	public static CPUWIssues getRandomUWIssue() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			CPUWIssues uwIssue = results.get(index);
            
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
	
	public static List<CPUWIssues> getALLUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	
	public static List<CPUWIssues> getALLUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static CPUWIssues getUWIssueByRuleMessage(String ruleMessage) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> ruleMessageExpression = uwIssueCriteriaRoot.get("ruleMessage");
			Predicate ruleMessageLike = builder.like(ruleMessageExpression, "%" + StringsUtils.specialCharacterEscape(ruleMessage) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleMessageLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			CPUWIssues uwIssue = results.get(index);
            
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
	
	public static List<CPUWIssues> getUWByRuleToApply(String ruleToApply) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> ruleToApplyExpression = uwIssueCriteriaRoot.get("applyRuleTo");
			Predicate ruleToApplyLike = builder.like(ruleToApplyExpression, "%" + StringsUtils.specialCharacterEscape(ruleToApply) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(ruleToApplyLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	public static List<CPUWIssues> getUWByBlockingType(String blockingType) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%" + StringsUtils.specialCharacterEscape(blockingType) + "%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CPUWIssues> getInformationalUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	
	public static List<CPUWIssues> getInformationalUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Non-Blocking%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CPUWIssues> getAllBlockQuoteReleaseUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	
	public static List<CPUWIssues> getAllBlockQuoteReleaseUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Quote Release%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
	
	public static List<CPUWIssues> getAllBlockBindUWIssues() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
						
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
	
	
	public static List<CPUWIssues> getAllBlockBindUWIssues(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWIssues> uwIssueCriteria = builder.createQuery(CPUWIssues.class);
			Root<CPUWIssues> uwIssueCriteriaRoot = uwIssueCriteria.from(CPUWIssues.class);
			Expression<String> blockingTypeExpression = uwIssueCriteriaRoot.get("result");
			Predicate blockingTypeLike = builder.like(blockingTypeExpression, "%Blocks Bind%");
			uwIssueCriteria.select(uwIssueCriteriaRoot).where(blockingTypeLike);
			
			List<CPUWIssues> results = session.createQuery(uwIssueCriteria).getResultList();
			
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
