package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class InsuranceScoreTestCasesHelper {
	
	public static InsuranceScoreTestCases getRandomContact_InsufficientInfo() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InsuranceScoreTestCases> agentCriteria = builder.createQuery(InsuranceScoreTestCases.class);
			Root<InsuranceScoreTestCases> agentCriteriaRoot = agentCriteria.from(InsuranceScoreTestCases.class);
			Expression<String> outOfSync = agentCriteriaRoot.get("score1");
			Predicate agentRegionLike = builder.notLike(outOfSync, "+%");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			List<InsuranceScoreTestCases> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			InsuranceScoreTestCases agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static InsuranceScoreTestCases getRandomContact_BySSN(String ssn) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InsuranceScoreTestCases> agentCriteria = builder.createQuery(InsuranceScoreTestCases.class);
			Root<InsuranceScoreTestCases> agentCriteriaRoot = agentCriteria.from(InsuranceScoreTestCases.class);
			Expression<String> outOfSync = agentCriteriaRoot.get("ssn");
			Predicate agentRegionLike = builder.like(outOfSync, ssn);
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			List<InsuranceScoreTestCases> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			InsuranceScoreTestCases agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static InsuranceScoreTestCases getRandomContact() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InsuranceScoreTestCases> agentCriteria = builder.createQuery(InsuranceScoreTestCases.class);
			Root<InsuranceScoreTestCases> agentCriteriaRoot = agentCriteria.from(InsuranceScoreTestCases.class);
			Expression<String> outOfSync = agentCriteriaRoot.get("score1");
			Predicate agentRegionLike = builder.like(outOfSync, "+%");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			List<InsuranceScoreTestCases> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			InsuranceScoreTestCases agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static InsuranceScoreTestCases getRandomContact_withDOB() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InsuranceScoreTestCases> agentCriteria = builder.createQuery(InsuranceScoreTestCases.class);
			Root<InsuranceScoreTestCases> agentCriteriaRoot = agentCriteria.from(InsuranceScoreTestCases.class);
			Expression<String> outOfSync = agentCriteriaRoot.get("dobasof102017");
			Predicate agentRegionLike = builder.like(outOfSync, "1%");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			Expression<String> outOfSync2 = agentCriteriaRoot.get("score1");
			Predicate agentRegionLike2 = builder.notLike(outOfSync2, "+");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike2);
			
			List<InsuranceScoreTestCases> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			InsuranceScoreTestCases agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	
	public static List<InsuranceScoreTestCases> getAllGeneralLiabilityForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<InsuranceScoreTestCases> formCriteria = builder.createQuery(InsuranceScoreTestCases.class);
			Root<InsuranceScoreTestCases> formCriteriaRoot = formCriteria.from(InsuranceScoreTestCases.class);
			formCriteria.select(formCriteriaRoot);
			
			List<InsuranceScoreTestCases> results = session.createQuery(formCriteria).getResultList();
			            
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
