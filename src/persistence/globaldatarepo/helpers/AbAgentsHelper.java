package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbAgents;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class AbAgentsHelper {

	public static List<AbAgents> getAllAgents() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbAgents> agentCriteria = builder.createQuery(AbAgents.class);
			Root<AbAgents> agentCriteriaRoot = agentCriteria.from(AbAgents.class);
			agentCriteria.select(agentCriteriaRoot);
			
			List<AbAgents> results = session.createQuery(agentCriteria).getResultList();
            
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
	
	public static List<AbAgents> getActiveAgents() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbAgents> agentCriteria = builder.createQuery(AbAgents.class);
			Root<AbAgents> agentCriteriaRoot = agentCriteria.from(AbAgents.class);
			Expression<String> activeAgent = agentCriteriaRoot.get("terminationdate");
			Predicate activeAgentLike = builder.like(activeAgent, "%" + StringsUtils.specialCharacterEscape(" 00-00-0000") + "%");
			agentCriteria.select(agentCriteriaRoot).where(activeAgentLike);
			
			List<AbAgents> results = session.createQuery(agentCriteria).getResultList();
            
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
	
	public static List<AbAgents> getInactiveAgents() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbAgents> agentCriteria = builder.createQuery(AbAgents.class);
			Root<AbAgents> agentCriteriaRoot = agentCriteria.from(AbAgents.class);
			Expression<String> inactiveAgent = agentCriteriaRoot.get("terminationdate");
			Predicate inactiveAgentLike = builder.notLike(inactiveAgent, "%" + StringsUtils.specialCharacterEscape(" 00-00-0000") + "%");
			agentCriteria.select(agentCriteriaRoot).where(inactiveAgentLike);
			
			List<AbAgents> results = session.createQuery(agentCriteria).getResultList();
            
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
	
	public static ArrayList<AbAgents> getInactiveAgentsAsOfYear(int year) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbAgents> agentCriteria = builder.createQuery(AbAgents.class);
			Root<AbAgents> agentCriteriaRoot = agentCriteria.from(AbAgents.class);
			Expression<String> inactiveAgent = agentCriteriaRoot.get("terminationdate");
			Predicate inactiveAgentLike = builder.notLike(inactiveAgent, "%" + StringsUtils.specialCharacterEscape(" 00-00-0000") + "%");
			agentCriteria.select(agentCriteriaRoot).where(inactiveAgentLike);
			
			List<AbAgents> results = session.createQuery(agentCriteria).getResultList();
            
            session.getTransaction().commit();
            
            ArrayList<AbAgents> termAgents = new ArrayList<AbAgents>();
            int mainFrameTermYear = 0;
            for(AbAgents agent : results) {
            	mainFrameTermYear = Integer.parseInt(agent.getTerminationdate().substring(agent.getTerminationdate().length()-4));
            	if(mainFrameTermYear >= year) {
            		termAgents.add(agent);
            	}
            }
            
            return termAgents;
            
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
