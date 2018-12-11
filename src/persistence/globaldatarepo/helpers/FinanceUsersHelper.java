package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.FinanceUserRole;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.FinanceUsers;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FinanceUsersHelper {

	public static FinanceUsers getRandomFinanceUser() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FinanceUsers> financeUsersCriteria = builder.createQuery(FinanceUsers.class);
			Root<FinanceUsers> financeUsersCriteriaRoot = financeUsersCriteria.from(FinanceUsers.class);
			financeUsersCriteria.select(financeUsersCriteriaRoot);
			
			List<FinanceUsers> results = session.createQuery(financeUsersCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			FinanceUsers financeUser = results.get(index);

			session.getTransaction().commit();

			return financeUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}

	}
	
	public static FinanceUsers getFinanceUserByName(String financeUserFirstName, String financeUserLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FinanceUsers> financeUsersCriteria = builder.createQuery(FinanceUsers.class);
			Root<FinanceUsers> financeUsersCriteriaRoot = financeUsersCriteria.from(FinanceUsers.class);
			Expression<String> firstName = financeUsersCriteriaRoot.get("firstName");
			Expression<String> lastName = financeUsersCriteriaRoot.get("lastName");
			Predicate financeUserFirstNameLike = builder.like(firstName, "%" + financeUserFirstName + "%");
			Predicate financeUserLastNameLike = builder.like(lastName, "%" + financeUserLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(financeUserFirstNameLike);
			predicates.add(financeUserLastNameLike);
			financeUsersCriteria.select(financeUsersCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<FinanceUsers> results = session.createQuery(financeUsersCriteria).getResultList();
			
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
	
	public static FinanceUsers getFinanceUserByUserName(String financeUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FinanceUsers> financeUsersCriteria = builder.createQuery(FinanceUsers.class);
			Root<FinanceUsers> financeUsersCriteriaRoot = financeUsersCriteria.from(FinanceUsers.class);
			Expression<String> financeUserNameExpression = financeUsersCriteriaRoot.get("userName");
			Predicate financeUserNameLike = builder.like(financeUserNameExpression, "%" + StringsUtils.specialCharacterEscape(financeUserName) + "%");
			financeUsersCriteria.select(financeUsersCriteriaRoot).where(financeUserNameLike);
			
			List<FinanceUsers> results = session.createQuery(financeUsersCriteria).getResultList();
			
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
	
	public static FinanceUsers getRandomFinanceUserByRole (FinanceUserRole role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<FinanceUsers> financeUsersCriteria = builder.createQuery(FinanceUsers.class);
			Root<FinanceUsers> financeUsersCriteriaRoot = financeUsersCriteria.from(FinanceUsers.class);
			Expression<String> financeUserRole = financeUsersCriteriaRoot.get("userRole");
			Predicate financeUserRoleEquals = builder.equal(financeUserRole, role.getValue());
			financeUsersCriteria.select(financeUsersCriteriaRoot).where(financeUserRoleEquals);
			
			List<FinanceUsers> results = session.createQuery(financeUsersCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			FinanceUsers financeUser = results.get(index);

			session.getTransaction().commit();

			return financeUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
