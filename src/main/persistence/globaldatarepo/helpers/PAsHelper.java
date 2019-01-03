package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.PAs;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class PAsHelper {

	public static PAs getRandomPA() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PAs> paCriteria = builder.createQuery(PAs.class);
			Root<PAs> paCriteriaRoot = paCriteria.from(PAs.class);
			paCriteria.select(paCriteriaRoot);
			
			List<PAs> results = session.createQuery(paCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			PAs pa = results.get(index);

			session.getTransaction().commit();

			return pa;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}

	}

	public static PAs getPAInfoByFirstLastName(String paFirstName, String paLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PAs> paCriteria = builder.createQuery(PAs.class);
			Root<PAs> paCriteriaRoot = paCriteria.from(PAs.class);
			Expression<String> paNameExpression = paCriteriaRoot.get("productionAssistantsName");
			Predicate paNameLike = builder.like(paNameExpression, "%" + paFirstName + " " + paLastName + "%");
			paCriteria.select(paCriteriaRoot).where(paNameLike);
			
			List<PAs> results = session.createQuery(paCriteria).getResultList();

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

	public static PAs getPAInfoByFullName(String paFullName) throws Exception {

		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PAs> paCriteria = builder.createQuery(PAs.class);
			Root<PAs> paCriteriaRoot = paCriteria.from(PAs.class);
			Expression<String> paNameExpression = paCriteriaRoot.get("productionAssistantsName");
			Predicate paNameLike = builder.like(paNameExpression, "%" + paFullName + "%");
			paCriteria.select(paCriteriaRoot).where(paNameLike);
			
			List<PAs> results = session.createQuery(paCriteria).getResultList();

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


	public static PAs getPAInfoByAgent(String agent) throws Exception {

		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PAs> paCriteria = builder.createQuery(PAs.class);
			Root<PAs> paCriteriaRoot = paCriteria.from(PAs.class);
			Expression<String> paAssociatedAgentExpression = paCriteriaRoot.get("paassociatedAgent");
			Predicate paAssociatedAgentLike = builder.like(paAssociatedAgentExpression, "%" + agent + "%");
			paCriteria.select(paCriteriaRoot).where(paAssociatedAgentLike);
			
			List<PAs> results = session.createQuery(paCriteria).getResultList();
			
			if(results.size() > 0) {
				session.getTransaction().commit();
				return results.get(0);
			} else {
				session.getTransaction().commit();
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

	public static void createNewPA(String productionAssistantsName, String pauserName, String pafirstName, String palastName, String paassociatedAgent) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();
			session.beginTransaction();

			PAs pa = new PAs();
			pa.setProductionAssistantsName(productionAssistantsName);
			pa.setPafirstName(pafirstName);
			pa.setPalastName(palastName);
			pa.setPauserName(pauserName);
			pa.setPaassociatedAgent(paassociatedAgent);

			session.save(pa);

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