package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.SAs;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class SAsHelper {

	public static SAs getRandomSA() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SAs> saCriteria = builder.createQuery(SAs.class);
			Root<SAs> saCriteriaRoot = saCriteria.from(SAs.class);
			saCriteria.select(saCriteriaRoot);
			
			List<SAs> results = session.createQuery(saCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			SAs sa = results.get(index);
            
            session.getTransaction().commit();
            
            return sa;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static SAs getSAInfoByFirstLastName(String saFirstName, String saLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SAs> saCriteria = builder.createQuery(SAs.class);
			Root<SAs> saCriteriaRoot = saCriteria.from(SAs.class);
			Expression<String> saNameExpression = saCriteriaRoot.get("servicingAssistantsName");
			Predicate saNameLike = builder.like(saNameExpression, "%" + saFirstName + " " + saLastName + "%");
			saCriteria.select(saCriteriaRoot).where(saNameLike);
			
			List<SAs> results = session.createQuery(saCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return results.get(0);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static SAs getSAInfoByAgent(String agentName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SAs> saCriteria = builder.createQuery(SAs.class);
			Root<SAs> saCriteriaRoot = saCriteria.from(SAs.class);
			Expression<String> saAssociatedAgentExpression = saCriteriaRoot.get("saassociatedAgent");
			Predicate saAssociatedAgentLike = builder.like(saAssociatedAgentExpression, "%" + agentName + "%");
			saCriteria.select(saCriteriaRoot).where(saAssociatedAgentLike);
			
			List<SAs> results = session.createQuery(saCriteria).getResultList();
			
			session.getTransaction().commit();
			if (results != null && results.size() > 0) {
				return results.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}	
	
	public static SAs getSAInfoByFullName(String saFullName) throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SAs> saCriteria = builder.createQuery(SAs.class);
			Root<SAs> saCriteriaRoot = saCriteria.from(SAs.class);
			Expression<String> saNameExpression = saCriteriaRoot.get("servicingAssistantsName");
			Predicate saNameLike = builder.like(saNameExpression, "%" + saFullName + "%");
			saCriteria.select(saCriteriaRoot).where(saNameLike);
			
			List<SAs> results = session.createQuery(saCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return results.get(0);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	
	public static void createNewSA(String serviceAssistantsName, String sauserName,
			String safirstName, String salastName, String saassociatedAgent) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			SAs sa = new SAs();
			sa.setServicingAssistantsName(serviceAssistantsName);
			sa.setSafirstName(safirstName);
			sa.setSalastName(salastName);
			sa.setSauserName(sauserName);
			sa.setSaassociatedAgent(saassociatedAgent);

			session.save(sa);
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
