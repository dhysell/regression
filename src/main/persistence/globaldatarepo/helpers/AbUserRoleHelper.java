package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbRole;
import persistence.globaldatarepo.entities.AbUserRole;
import persistence.globaldatarepo.entities.AbUsers;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AbUserRoleHelper {

	public static AbUserRole getAbUserRole(AbRole role, AbUsers user) throws Exception {
		
		role = AbRoleHelper.getAbRole(role.getRole());
		user = AbUserHelper.getUserByUserName(user.getUserName());
		
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbUserRole> userRoleCriteria = builder.createQuery(AbUserRole.class);
			Root<AbUserRole> userRoleCriteriaRoot = userRoleCriteria.from(AbUserRole.class);
			Expression<String> roleExpression = userRoleCriteriaRoot.get("abRole");
			Expression<String> userExpression = userRoleCriteriaRoot.get("abUsers");
			Predicate roleID = builder.like(roleExpression, "%" + role.getRoleId() + "%");
			Predicate userID = builder.like(userExpression, "%" + user.getUserName() + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(roleID);
			predicates.add(userID);
			userRoleCriteria.select(userRoleCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<AbUserRole> results = session.createQuery(userRoleCriteria).getResultList();
						
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
	
	public static boolean removeUserRoles(String roleName) throws Exception {
		AbRole abRole = null;
		try{
			abRole = AbRoleHelper.getAbRole(roleName);
		}catch(Exception e){
			return false;
		}
		
		removeUserRoles(abRole.getRoleId());	
		return true;
	}
	
	private static int removeUserRoles(int roleID) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			int rows = em.createNativeQuery("DELETE FROM AB_UserRole WHERE RoleID = '"+roleID+"'").executeUpdate();
			session.getTransaction().commit();
			return rows; 
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
}
