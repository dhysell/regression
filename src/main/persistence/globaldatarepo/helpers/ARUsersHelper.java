package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ARUsersHelper {

	public static ARUsers getRandomARUser() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			
			Expression<String> arUserRole = arUserCriteriaRoot.get("userRole");
			Predicate arUserRoleNotEqual = builder.notEqual(arUserRole, "BCViewOnly");
			Predicate pcUserRoleNotEqual = builder.notEqual(arUserRole, "ViewOnly");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(arUserRoleNotEqual);
			predicates.add(pcUserRoleNotEqual);
			arUserCriteria.select(arUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			ARUsers arUser = results.get(index);
			
			session.getTransaction().commit();

			return arUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}

	}
	
	public static ARUsers getARUserByName(String arUserFirstName, String arUserLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			Expression<String> firstName = arUserCriteriaRoot.get("firstName");
			Expression<String> lastName = arUserCriteriaRoot.get("lastName");
			Predicate arUserFirstNameLike = builder.like(firstName, "%" + arUserFirstName + "%");
			Predicate arUserLastNameLike = builder.like(lastName, "%" + arUserLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(arUserFirstNameLike);
			predicates.add(arUserLastNameLike);
			arUserCriteria.select(arUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
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
	
	public static ARUsers getARUserByUserName(String arUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			Expression<String> arUserNameExpression = arUserCriteriaRoot.get("userName");
			Predicate arUserNameLike = builder.like(arUserNameExpression, "%" + StringsUtils.specialCharacterEscape(arUserName) + "%");
			arUserCriteria.select(arUserCriteriaRoot).where(arUserNameLike);
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
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
	
	public static ARUsers getRandomARUserByRole (ARUserRole role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			Expression<String> arUserRole = arUserCriteriaRoot.get("userRole");
			Predicate arUserRoleEquals = builder.equal(arUserRole, role.getValue());
			arUserCriteria.select(arUserCriteriaRoot).where(arUserRoleEquals);
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			ARUsers arUser = results.get(index);

			session.getTransaction().commit();

			return arUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static ARUsers getRandomARUserByCompany (ARCompany company) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			Expression<String> arUserRole = arUserCriteriaRoot.get("userRole");
			Expression<String> arCompany = arUserCriteriaRoot.get("company");
			Predicate arUserRoleNotEqual = builder.notEqual(arUserRole, "BCViewOnly");
			Predicate arCompanyEquals = builder.equal(arCompany, company.getValue());
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(arUserRoleNotEqual);
			predicates.add(arCompanyEquals);
			arUserCriteria.select(arUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			ARUsers arUser = results.get(index);

			session.getTransaction().commit();

			return arUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static ARUsers getRandomARUserByRoleAndCompany (ARUserRole role ,ARCompany company) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ARUsers> arUserCriteria = builder.createQuery(ARUsers.class);
			Root<ARUsers> arUserCriteriaRoot = arUserCriteria.from(ARUsers.class);
			Expression<String> arUserRole = arUserCriteriaRoot.get("userRole");
			Expression<String> arCompany = arUserCriteriaRoot.get("company");
			Predicate arUserRoleEquals = builder.equal(arUserRole, role.getValue());
			Predicate arCompanyEquals = builder.equal(arCompany, company.getValue());
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(arUserRoleEquals);
			predicates.add(arCompanyEquals);
			arUserCriteria.select(arUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<ARUsers> results = session.createQuery(arUserCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			ARUsers arUser = results.get(index);

			session.getTransaction().commit();

			return arUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
