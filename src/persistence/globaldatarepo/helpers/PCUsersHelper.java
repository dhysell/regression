package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.PC_Users;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class PCUsersHelper {
	
	
	public static List<PC_Users> getAllPCUsers() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PC_Users> pcUserCriteria = builder.createQuery(PC_Users.class);
			Root<PC_Users> pcUserCriteriaRoot = pcUserCriteria.from(PC_Users.class);
			pcUserCriteria.select(pcUserCriteriaRoot);
			
			List<PC_Users> results = session.createQuery(pcUserCriteria).getResultList();

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
	
	public static void createNewPCUser(String firstName, String middleName, String lastName, String userName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();
			session.beginTransaction();

			PC_Users pcUser = new PC_Users(firstName.trim(), (middleName != null) ? middleName.trim() : middleName, lastName.trim(), userName.trim());

			session.save(pcUser);

			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<PC_Users> getPCUsersByJob(String job) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PC_Users> pcUserCriteria = builder.createQuery(PC_Users.class);
			Root<PC_Users> pcUserCriteriaRoot = pcUserCriteria.from(PC_Users.class);
			Expression<String> jobTitle = pcUserCriteriaRoot.get("jobTitle");
			Predicate jobTitleLike = builder.like(jobTitle, "%" + StringsUtils.specialCharacterEscape(job) + "%");
			pcUserCriteria.select(pcUserCriteriaRoot).where(jobTitleLike);
			
			List<PC_Users> results = session.createQuery(pcUserCriteria).getResultList();

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











