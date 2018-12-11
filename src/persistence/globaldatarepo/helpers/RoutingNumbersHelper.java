package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.RoutingNumbers;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class RoutingNumbersHelper {

	public static List<RoutingNumbers> getAllRoutingNumbers() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RoutingNumbers> routingNumberCriteria = builder.createQuery(RoutingNumbers.class);
			Root<RoutingNumbers> routingNumberCriteriaRoot = routingNumberCriteria.from(RoutingNumbers.class);
			routingNumberCriteria.select(routingNumberCriteriaRoot);
			
			List<RoutingNumbers> results = session.createQuery(routingNumberCriteria).getResultList();
            
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
	
	public static RoutingNumbers getRoutingNumber_Random() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RoutingNumbers> routingNumberCriteria = builder.createQuery(RoutingNumbers.class);
			Root<RoutingNumbers> routingNumberCriteriaRoot = routingNumberCriteria.from(RoutingNumbers.class);
			routingNumberCriteria.select(routingNumberCriteriaRoot);
			
			List<RoutingNumbers> results = session.createQuery(routingNumberCriteria).getResultList();
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
            
            session.getTransaction().commit();
            
            return results.get(index);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}	
	
	
	
public static void updateCsrRegion(String routingNumber, String institutionName, String address, String city, String stateCode, String zipCode, String zipCodeExtension, String telephoneAreaCode, String telephonePrefixNumber, String telephoneSuffixNumber)throws Exception{
		
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RoutingNumbers> csrCriteria = builder.createQuery(RoutingNumbers.class);
			Root<RoutingNumbers> csrCriteriaRoot = csrCriteria.from(RoutingNumbers.class);
			Expression<String> csrUserNameExpression = csrCriteriaRoot.get("routingNumber");
			Predicate csrUserNameLike = builder.like(csrUserNameExpression, routingNumber);
			csrCriteria.select(csrCriteriaRoot).where(csrUserNameLike);
			
			List<RoutingNumbers> results = session.createQuery(csrCriteria).getResultList();
			
			RoutingNumbers result = results.get(0);
			result.setInstitutionName(institutionName);
			result.setAddress(address);
			result.setCity(city);
			result.setStateCode(stateCode);
			result.setZipCode(zipCode);
			if(zipCodeExtension.equals("0000")) {
				result.setZipCodeExtension(null);
			} else {
				result.setZipCodeExtension(zipCodeExtension);
			}
			result.setTelephoneAreaCode(telephoneAreaCode);
			result.setTelephonePrefixNumber(telephonePrefixNumber);
			result.setTelephoneSuffixNumber(telephoneSuffixNumber);
		
			session.update(result);
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
