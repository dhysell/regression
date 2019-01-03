package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.StandardAddresses;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class StandardAddressesHelper {
	
	public static StandardAddresses getRandomStandardAddress() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<StandardAddresses> addressCriteria = builder.createQuery(StandardAddresses.class);
			Root<StandardAddresses> addressCriteriaRoot = addressCriteria.from(StandardAddresses.class);
			Expression<Boolean> addressStandardizableExpression = addressCriteriaRoot.get("standardizable");
			Predicate isAddressStandardizable = builder.isTrue(addressStandardizableExpression);
			addressCriteria.select(addressCriteriaRoot).where(isAddressStandardizable);
			
			List<StandardAddresses> results = session.createQuery(addressCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			StandardAddresses address = results.get(index);
			
			session.getTransaction().commit();
			
			return address;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static StandardAddresses getRandomNONStandardAddress() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<StandardAddresses> addressCriteria = builder.createQuery(StandardAddresses.class);
			Root<StandardAddresses> addressCriteriaRoot = addressCriteria.from(StandardAddresses.class);
			Expression<Boolean> addressStandardizableExpression = addressCriteriaRoot.get("standardizable");
			Predicate isAddressNotStandardizable = builder.isFalse(addressStandardizableExpression);
			addressCriteria.select(addressCriteriaRoot).where(isAddressNotStandardizable);
			
			List<StandardAddresses> results = session.createQuery(addressCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			StandardAddresses address = results.get(index);
			
			session.getTransaction().commit();
			
			return address;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static void createStandardAddress(StandardAddresses address ) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			StandardAddresses standardAddress = new StandardAddresses();
			standardAddress.setAddress(address.getAddress());
			standardAddress.setCity(address.getCity());
			standardAddress.setCounty(address.getCounty());
			standardAddress.setStandardizable(address.getStandardizable());
			standardAddress.setState(address.getState());
			standardAddress.setZip(address.getZip());
			standardAddress.setZip4(address.getZip4());

			session.save(standardAddress);
			
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
