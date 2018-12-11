package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.APlusAuto;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class APlusAutoHelper {
	
	
	private static APlusAuto getRandomAPlusAuto_SingleRow() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<APlusAuto> agentCriteria = builder.createQuery(APlusAuto.class);
			Root<APlusAuto> agentCriteriaRoot = agentCriteria.from(APlusAuto.class);
			Expression<String> outOfSync = agentCriteriaRoot.get("column41"); // where SSN ="" one are not working so filtered the SSN Rows
			Predicate inSync = builder.notLike(outOfSync,"");
    		agentCriteria.select(agentCriteriaRoot).where(inSync);
			
			List<APlusAuto> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			APlusAuto agent = results.get(index);
            
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
	
	public static List<APlusAuto> getAPlusAutoTestCase() throws Exception {
		APlusAuto randomTestCase = getRandomAPlusAuto_SingleRow();
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<APlusAuto> agentCriteria = builder.createQuery(APlusAuto.class);
			Root<APlusAuto> agentCriteriaRoot = agentCriteria.from(APlusAuto.class);
			Expression<String> agentRegion = agentCriteriaRoot.get("column0");
			Predicate agentRegionLike = builder.like(agentRegion,  randomTestCase.getColumn0() );
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			List<APlusAuto> results = session.createQuery(agentCriteria).getResultList();
            
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
