package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class PermissionsHelper {



	///////////////
	// SU
	///////////////

	public static List<Permissions_SU> getAllSUPermissions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_SU> permissionCriteria = builder.createQuery(Permissions_SU.class);
			Root<Permissions_SU> permissionCriteriaRoot = permissionCriteria.from(Permissions_SU.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_SU> results = session.createQuery(permissionCriteria).getResultList();
			
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

	public static Permissions_SU getRandomSUPermission() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_SU> permissionCriteria = builder.createQuery(Permissions_SU.class);
			Root<Permissions_SU> permissionCriteriaRoot = permissionCriteria.from(Permissions_SU.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_SU> results = session.createQuery(permissionCriteria).getResultList();
			
			Permissions_SU permission = results.get(new Random().nextInt(results.size()));			
			permission.setDateLastUsed(getDate());
			session.update(permission);

			session.getTransaction().commit();
			return permission;

		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}


	///////////////
	// AGENT
	///////////////

	public static List<Permissions_Agent> getAllAgentPermissions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_Agent> permissionCriteria = builder.createQuery(Permissions_Agent.class);
			Root<Permissions_Agent> permissionCriteriaRoot = permissionCriteria.from(Permissions_Agent.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_Agent> results = session.createQuery(permissionCriteria).getResultList();
			
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

	public static Permissions_Agent getRandomAgentPermission() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_Agent> permissionCriteria = builder.createQuery(Permissions_Agent.class);
			Root<Permissions_Agent> permissionCriteriaRoot = permissionCriteria.from(Permissions_Agent.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_Agent> results = session.createQuery(permissionCriteria).getResultList();

			Permissions_Agent permission = results.get(new Random().nextInt(results.size()));
			permission.setDateLastUsed(getDate());
			session.update(permission);

			session.getTransaction().commit();
			return permission;

		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}



	///////////////
	// CSR
	///////////////
	public static List<Permissions_CSR> getAllCSRPermissions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_CSR> permissionCriteria = builder.createQuery(Permissions_CSR.class);
			Root<Permissions_CSR> permissionCriteriaRoot = permissionCriteria.from(Permissions_CSR.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_CSR> results = session.createQuery(permissionCriteria).getResultList();
			
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

	public static Permissions_CSR getRandomCSRPermission() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_CSR> permissionCriteria = builder.createQuery(Permissions_CSR.class);
			Root<Permissions_CSR> permissionCriteriaRoot = permissionCriteria.from(Permissions_CSR.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_CSR> results = session.createQuery(permissionCriteria).getResultList();

			Permissions_CSR permission = results.get(new Random().nextInt(results.size()));
			permission.setDateLastUsed(getDate());
			session.update(permission);

			session.getTransaction().commit();
			return permission;

		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}



	///////////////
	// UNDERWRITER
	///////////////
	public static List<Permissions_Underwriter> getAllUnderwriterPermissions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_Underwriter> permissionCriteria = builder.createQuery(Permissions_Underwriter.class);
			Root<Permissions_Underwriter> permissionCriteriaRoot = permissionCriteria.from(Permissions_Underwriter.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_Underwriter> results = session.createQuery(permissionCriteria).getResultList();
			
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

	public static Permissions_Underwriter getRandomUnderwriterPermission() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_Underwriter> permissionCriteria = builder.createQuery(Permissions_Underwriter.class);
			Root<Permissions_Underwriter> permissionCriteriaRoot = permissionCriteria.from(Permissions_Underwriter.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_Underwriter> results = session.createQuery(permissionCriteria).getResultList();

			Permissions_Underwriter permission = results.get(new Random().nextInt(results.size()));
			permission.setDateLastUsed(getDate());
			session.update(permission);

			session.getTransaction().commit();
			return permission;

		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}

	///////////////
	// SA
	///////////////
	public static List<Permissions_SA> getAllSAPermissions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_SA> permissionCriteria = builder.createQuery(Permissions_SA.class);
			Root<Permissions_SA> permissionCriteriaRoot = permissionCriteria.from(Permissions_SA.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_SA> results = session.createQuery(permissionCriteria).getResultList();
			
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

	public static Permissions_SA getRandomSAPermission() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permissions_SA> permissionCriteria = builder.createQuery(Permissions_SA.class);
			Root<Permissions_SA> permissionCriteriaRoot = permissionCriteria.from(Permissions_SA.class);
			permissionCriteria.select(permissionCriteriaRoot);
			
			List<Permissions_SA> results = session.createQuery(permissionCriteria).getResultList();

			Permissions_SA permission = results.get(new Random().nextInt(results.size()));
			permission.setDateLastUsed(getDate());
			session.update(permission);

			session.getTransaction().commit();
			return permission;

		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}



	public static void savePermission(String permission, String permissionCode, String role) throws Exception {

		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();

			session.beginTransaction();
			switch(role) {
				case "SU":
					Permissions_SU suPermission = new Permissions_SU(permission, permissionCode);
					session.save(suPermission);
					break;
				case "AGENT":
					Permissions_Agent agentPermission = new Permissions_Agent(permission, permissionCode);
					session.save(agentPermission);
					break;
				case "SA":
					Permissions_SA saPermission = new Permissions_SA(permission, permissionCode);
					session.save(saPermission);
					break;
				case "UNDERWRITER":
					Permissions_Underwriter underwriterPermission = new Permissions_Underwriter(permission, permissionCode);
					session.save(underwriterPermission);
					break;
				case "CSR":
					Permissions_CSR csrPermission = new Permissions_CSR(permission, permissionCode);
					session.save(csrPermission);
					break;

			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}

	}


	private static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);

		return dateFormat.format(cal.getTime());

	}

}



















