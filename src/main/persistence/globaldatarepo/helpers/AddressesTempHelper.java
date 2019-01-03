package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AddressesTemp;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class AddressesTempHelper {
	public static AddressesTemp getRandomAddress() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AddressesTemp> addressCriteria = builder.createQuery(AddressesTemp.class);
			Root<AddressesTemp> addressCriteriaRoot = addressCriteria.from(AddressesTemp.class);
			addressCriteria.select(addressCriteriaRoot);
			
			List<AddressesTemp> results = session.createQuery(addressCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			AddressesTemp address = results.get(index);
			
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
	
	public static AddressesTemp getRandomAddressZip(String zip) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AddressesTemp> zipCriteria = builder.createQuery(AddressesTemp.class);
			Root<AddressesTemp> zipCriteriaRoot = zipCriteria.from(AddressesTemp.class);
			Expression<String> zipcode = zipCriteriaRoot.get("zip");
			Predicate zipcodeLike = builder.like(zipcode, "%" + StringsUtils.specialCharacterEscape(zip) + "%");
			zipCriteria.select(zipCriteriaRoot).where(zipcodeLike);
			
			List<AddressesTemp> results = session.createQuery(zipCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			AddressesTemp address = results.get(index);
			
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
	
	public static List<AddressesTemp> getAddresses() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AddressesTemp> addressCriteria = builder.createQuery(AddressesTemp.class);
			Root<AddressesTemp> addressCriteriaRoot = addressCriteria.from(AddressesTemp.class);
			addressCriteria.select(addressCriteriaRoot);
			
			List<AddressesTemp> results = session.createQuery(addressCriteria).getResultList();
			
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
	
	public static void createNewCodedAddress(String address, String city, String state, String zip, String zip4, String county, String teritoryCode) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			AddressesTemp code = new AddressesTemp();
			code.setAddress(address);
			code.setCity(city);
			code.setState(state);
			code.setZip(zip);
			code.setZip4(zip4);
			code.setCounty(county);
			code.setTeritoryCode(teritoryCode);
			

			session.save(code);
			
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
