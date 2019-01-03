package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbRole;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class AbRoleHelper {
	public static AbRole getAbRole(String role) throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbRole> abRoleCriteria = builder.createQuery(AbRole.class);
			Root<AbRole> abRoleCriteriaRoot = abRoleCriteria.from(AbRole.class);
			Expression<String> abRoleName = abRoleCriteriaRoot.get("role");
			Predicate abUserNameLike = builder.like(abRoleName, "%" + StringsUtils.specialCharacterEscape(role) + "%");
			abRoleCriteria.select(abRoleCriteriaRoot).where(abUserNameLike);
			
			List<AbRole> results = session.createQuery(abRoleCriteria).getResultList();
			
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
	
public static AbRole getAbRoleByID(String roleID) throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbRole> abRoleCriteria = builder.createQuery(AbRole.class);
			Root<AbRole> abRoleCriteriaRoot = abRoleCriteria.from(AbRole.class);
			Expression<String> abRoleName = abRoleCriteriaRoot.get("roleID");
			Predicate abUserNameLike = builder.like(abRoleName, "%" + StringsUtils.specialCharacterEscape(roleID) + "%");
			abRoleCriteria.select(abRoleCriteriaRoot).where(abUserNameLike);
			
			List<AbRole> results = session.createQuery(abRoleCriteria).getResultList();
			
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
}
