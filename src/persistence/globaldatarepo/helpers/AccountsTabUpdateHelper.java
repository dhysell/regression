package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AccountsTabUpdate;

import javax.persistence.criteria.*;
import java.util.List;


public class AccountsTabUpdateHelper {
	
	public static List<AccountsTabUpdate> getChangedAccounts() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AccountsTabUpdate> accountCriteria = builder.createQuery(AccountsTabUpdate.class);
			Root<AccountsTabUpdate> accountCriteriaRoot = accountCriteria.from(AccountsTabUpdate.class);
			accountCriteria.select(accountCriteriaRoot);
			
			List<AccountsTabUpdate> results = session.createQuery(accountCriteria).getResultList();
			
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
	
	public static List<AccountsTabUpdate> getChangedAccountsByAllUsersByDept(String status) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AccountsTabUpdate> changedAccountCriteria = builder.createQuery(AccountsTabUpdate.class);
			Root<AccountsTabUpdate> changedAccountCriteriaRoot = changedAccountCriteria.from(AccountsTabUpdate.class);
			Expression<String> accountChangeStatus = changedAccountCriteriaRoot.get("updateType");
			Predicate accountChangeStatusLike = builder.like(accountChangeStatus, "%" + status + "%");
			changedAccountCriteria.select(changedAccountCriteriaRoot).where(accountChangeStatusLike);
			
			List<AccountsTabUpdate> results = session.createQuery(changedAccountCriteria).getResultList();
			
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
