package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.UserRegions;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserRegionsHelper {
	
	public static UserRegions getRandomUser() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> userCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> userCriteriaRoot = userCriteria.from(UserRegions.class);
			userCriteria.select(userCriteriaRoot);
			
			List<UserRegions> results = session.createQuery(userCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			UserRegions userRegion = results.get(index);
            
            session.getTransaction().commit();
            
            return userRegion;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static UserRegions getRandomUserByRegion(String region) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> userRegionExpression = regionCriteriaRoot.get("region");
			Predicate userRegionLike = builder.like(userRegionExpression, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			regionCriteria.select(regionCriteriaRoot).where(userRegionLike);
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
						
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
	
	public static UserRegions getRandomUserByRole(String role) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> userRoleExpression = regionCriteriaRoot.get("role");
			Predicate userRoleLike = builder.like(userRoleExpression, "%" + StringsUtils.specialCharacterEscape(role) + "%");
			regionCriteria.select(regionCriteriaRoot).where(userRoleLike);
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
						
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
	
	public static UserRegions getRandomUserByRoleAndRegion(String role, String region) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> roleExpression = regionCriteriaRoot.get("role");
			Expression<String> regionExpression = regionCriteriaRoot.get("region");
			Predicate roleNameLike = builder.like(roleExpression, "%" + StringsUtils.specialCharacterEscape(role) + "%");
			Predicate regionNameLike = builder.like(regionExpression, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(roleNameLike);
			predicates.add(regionNameLike);
			regionCriteria.select(regionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
			
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
	
	public static UserRegions getRegionUser(String firstName, String lastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> firstNameExpression = regionCriteriaRoot.get("firstName");
			Expression<String> lastNameExpression = regionCriteriaRoot.get("lastName");
			Predicate regionUserFirstNameLike = builder.like(firstNameExpression, "%" + StringsUtils.specialCharacterEscape(firstName) + "%");
			Predicate regionUserLastNameLike = builder.like(lastNameExpression, "%" + StringsUtils.specialCharacterEscape(lastName) + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(regionUserFirstNameLike);
			predicates.add(regionUserLastNameLike);
			regionCriteria.select(regionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
						
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
	
	public static UserRegions getRegionUser(String userName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> regionUserNameExpression = regionCriteriaRoot.get("userName");
			Predicate regionUserNameLike = builder.like(regionUserNameExpression, "%" + StringsUtils.specialCharacterEscape(userName) + "%");
			regionCriteria.select(regionCriteriaRoot).where(regionUserNameLike);
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
						
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
	
	public static UserRegions getRegionManager(String region) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> roleExpression = regionCriteriaRoot.get("role");
			Expression<String> regionExpression = regionCriteriaRoot.get("region");
			Predicate roleNameLike = builder.like(roleExpression, "%" + StringsUtils.specialCharacterEscape("Manager") + "%");
			Predicate regionNameLike = builder.like(regionExpression, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(roleNameLike);
			predicates.add(regionNameLike);
			regionCriteria.select(regionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
			
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
	
	
	public static UserRegions getRegionManagerByUser(String userName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> regionUserNameExpression = regionCriteriaRoot.get("userName");
			Predicate regionUserNameLike = builder.like(regionUserNameExpression, "%" + StringsUtils.specialCharacterEscape(userName) + "%");
			regionCriteria.select(regionCriteriaRoot).where(regionUserNameLike);
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return getRegionManager(results.get(0).getRegion());
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static UserRegions getRegionManagerByUser(String firstName, String lastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<UserRegions> regionCriteria = builder.createQuery(UserRegions.class);
			Root<UserRegions> regionCriteriaRoot = regionCriteria.from(UserRegions.class);
			Expression<String> firstNameExpression = regionCriteriaRoot.get("firstName");
			Expression<String> lastNameExpression = regionCriteriaRoot.get("lastName");
			Predicate regionUserFirstNameLike = builder.like(firstNameExpression, "%" + StringsUtils.specialCharacterEscape(firstName) + "%");
			Predicate regionUserLastNameLike = builder.like(lastNameExpression, "%" + StringsUtils.specialCharacterEscape(lastName) + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(regionUserFirstNameLike);
			predicates.add(regionUserLastNameLike);
			regionCriteria.select(regionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<UserRegions> results = session.createQuery(regionCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return getRegionManager(results.get(0).getRegion());
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	
	
	
	/*private static void createNewUserRegion(String firstName, String lastName,
			String userName, String password, String region, String role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);	
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			UserRegions userRegion = new UserRegions();
			userRegion.setFirstName(firstName);
			userRegion.setLastName(lastName);
			userRegion.setUserName(userName);
			userRegion.setPassword(password);
			userRegion.setRegion(region);
			userRegion.setRole(role);

			session.save(userRegion);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
		
	}*/

}
