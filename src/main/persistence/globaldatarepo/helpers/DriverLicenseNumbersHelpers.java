package persistence.globaldatarepo.helpers;

import com.idfbins.enums.State;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.DriverLicenseNumbers;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class DriverLicenseNumbersHelpers {

	public static DriverLicenseNumbers getDLNumber(State state) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DriverLicenseNumbers> dlNumberCriteria = builder.createQuery(DriverLicenseNumbers.class);
			Root<DriverLicenseNumbers> dlNumberCriteriaRoot = dlNumberCriteria.from(DriverLicenseNumbers.class);
			Expression<String> stateExpression = dlNumberCriteriaRoot.get("state");
			Predicate stateEquals = builder.equal(stateExpression, state.getName());
			dlNumberCriteria.select(dlNumberCriteriaRoot).where(stateEquals);
			
			List<DriverLicenseNumbers> results = session.createQuery(dlNumberCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			DriverLicenseNumbers agent = results.get(index);
            
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
	
	public static List<DriverLicenseNumbers> getDLNumbers(State state) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DriverLicenseNumbers> dlNumberCriteria = builder.createQuery(DriverLicenseNumbers.class);
			Root<DriverLicenseNumbers> dlNumberCriteriaRoot = dlNumberCriteria.from(DriverLicenseNumbers.class);
			Expression<String> stateExpression = dlNumberCriteriaRoot.get("state");
			Predicate stateEquals = builder.equal(stateExpression, state.getName());
			dlNumberCriteria.select(dlNumberCriteriaRoot).where(stateEquals);
			
			List<DriverLicenseNumbers> results = session.createQuery(dlNumberCriteria).getResultList();
			            
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
	
	public static DriverLicenseNumbers getRandomDLNumber() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DriverLicenseNumbers> dlNumberCriteria = builder.createQuery(DriverLicenseNumbers.class);
			Root<DriverLicenseNumbers> dlNumberCriteriaRoot = dlNumberCriteria.from(DriverLicenseNumbers.class);
			dlNumberCriteria.select(dlNumberCriteriaRoot);
			
			List<DriverLicenseNumbers> results = session.createQuery(dlNumberCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			DriverLicenseNumbers agent = results.get(index);
            
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
	
	public static List<DriverLicenseNumbers> getRandomDLNumberList() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DriverLicenseNumbers> dlNumberCriteria = builder.createQuery(DriverLicenseNumbers.class);
			Root<DriverLicenseNumbers> dlNumberCriteriaRoot = dlNumberCriteria.from(DriverLicenseNumbers.class);
			dlNumberCriteria.select(dlNumberCriteriaRoot);
			
			List<DriverLicenseNumbers> results = session.createQuery(dlNumberCriteria).getResultList();
            
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
