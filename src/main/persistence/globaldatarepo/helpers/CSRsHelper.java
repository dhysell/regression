package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CSRs;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CSRsHelper {
	
	public static CSRs getRandomCSR() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			csrCriteria.select(csrCriteriaRoot);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			CSRs csr = results.get(index);
			
			session.getTransaction().commit();
			
			return csr;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static List<CSRs> getAllCSRs() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			csrCriteria.select(csrCriteriaRoot);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static CSRs getCSRInfoByFirstLastName(String csrFirstName, String csrLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> firstName = csrCriteriaRoot.get("csrfirstName");
			Expression<String> lastName = csrCriteriaRoot.get("csrlastName");
			Predicate csrUserFirstNameLike = builder.like(firstName, "%" + csrFirstName + "%");
			Predicate csrLastNameLike = builder.like(lastName, "%" + csrLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(csrUserFirstNameLike);
			predicates.add(csrLastNameLike);
			csrCriteria.select(csrCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static CSRs getCSRInfoByFullName(String csrFullName) throws Exception {
		String[] userNameArray = csrFullName.split(" ");
		String csrFirstName = userNameArray[0];
		String csrLastName = userNameArray[1];
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> firstName = csrCriteriaRoot.get("csrfirstName");
			Expression<String> lastName = csrCriteriaRoot.get("csrlastName");
			Predicate csrUserFirstNameLike = builder.like(firstName, "%" + csrFirstName + "%");
			Predicate csrLastNameLike = builder.like(lastName, "%" + csrLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(csrUserFirstNameLike);
			predicates.add(csrLastNameLike);
			csrCriteria.select(csrCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static CSRs getCSRByUserName(String csrUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> csrUserNameExpression = csrCriteriaRoot.get("csruserName");
			Predicate csrUserNameLike = builder.like(csrUserNameExpression, "%" + StringsUtils.specialCharacterEscape(csrUserName) + "%");
			csrCriteria.select(csrCriteriaRoot).where(csrUserNameLike);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static List<CSRs> getAllCSRinAgency(String agency) throws Exception{
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> csrAgencyExpression = csrCriteriaRoot.get("csragency");
			Predicate csrAgencyLike = builder.like(csrAgencyExpression, "%" + StringsUtils.specialCharacterEscape(agency) + "%");
			csrCriteria.select(csrCriteriaRoot).where(csrAgencyLike);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
            
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
	
	public static CSRs getCSRByAgency(String agency) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> csrAgencyExpression = csrCriteriaRoot.get("csragency");
			Predicate csrAgencyLike = builder.like(csrAgencyExpression, "%" + StringsUtils.specialCharacterEscape(agency) + "%");
			csrCriteria.select(csrCriteriaRoot).where(csrAgencyLike);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static CSRs getCSRByRegion(String region) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> csrRegionExpression = csrCriteriaRoot.get("csrregion");
			Predicate csrRegionLike = builder.like(csrRegionExpression, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			csrCriteria.select(csrCriteriaRoot).where(csrRegionLike);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
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
	
	public static void createNewCSR( String csrfirstName, String csrlastName,
			String csruserName, String csragency, String csrregion, String csrpassword, String csrcounty) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			CSRs csr = new CSRs();
			csr.setCsrfirstName(csrfirstName);
			csr.setCsrlastName(csrlastName);
			csr.setCsruserName(csruserName);
			csr.setCsragency(csragency);
			csr.setCsrregion(csrregion);
			csr.setCsrPassword(csrpassword);
			csr.setCsrcounty(csrcounty);

			session.save(csr);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void updateCsrRegion(CSRs csr, String region)throws Exception{
		
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CSRs> csrCriteria = builder.createQuery(CSRs.class);
			Root<CSRs> csrCriteriaRoot = csrCriteria.from(CSRs.class);
			Expression<String> csrUserNameExpression = csrCriteriaRoot.get("csruserName");
			Predicate csrUserNameLike = builder.like(csrUserNameExpression, "%" + StringsUtils.specialCharacterEscape(csr.getCsruserName()) + "%");
			csrCriteria.select(csrCriteriaRoot).where(csrUserNameLike);
			
			List<CSRs> results = session.createQuery(csrCriteria).getResultList();
			
			CSRs csrResults = results.get(0);
			csrResults.setCsrregion(region);
		
			session.update(csrResults);
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