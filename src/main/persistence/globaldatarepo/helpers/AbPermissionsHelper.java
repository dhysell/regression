package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbRole;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

public class AbPermissionsHelper {
	
	
	public static AbPermissions getPermission(String perm) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbPermissions> permCriteria = builder.createQuery(AbPermissions.class);
			Root<AbPermissions> permCriteriaRoot = permCriteria.from(AbPermissions.class);
			Expression<String> permName = permCriteriaRoot.get("permission");
			Predicate permNameLike = builder.equal(permName, perm.trim());
			permCriteria.select(permCriteriaRoot).where(permNameLike);
			
			List<AbPermissions> results = session.createQuery(permCriteria).getResultList();
			
			AbPermissions permission = results.get(0);
            
            session.getTransaction().commit();
            
            return permission;
		} catch (Exception e) {	
			System.out.println("####################");
			System.out.println("Could not find permission: " + perm + " in the AbPermissions table.");
			System.out.println("####################");
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	public static List<AbPermissions> getPermissionsForRole(AbRole role) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
/*			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbPermissions> permCriteria = builder.createQuery(AbPermissions.class);
			Root<AbPermissions> agentCriteriaRoot = permCriteria.from(AbPermissions.class);
			Expression<String> agentRegion = agentCriteriaRoot.get("agentRegion");
			Predicate agentRegionLike = builder.like(agentRegion, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			SELECT *
FROM dbo.ab_user U
INNER JOIN dbo.ab_userrole UR ON UR.UserID = U.ID
INNER JOIN dbo.ab_role R ON R.ID = UR.RoleID
INNER JOIN dbo.ab_credential CR ON CR.ID = U.CredentialID
INNER JOIN dbo.ab_privilege P ON P.RoleID = R.ID
INNER JOIN dbo.abtl_systempermissiontype SPT ON SPT.ID = P.Permission

*/			Query query = session.createQuery("Select FROM ABPermissions perm join perm.associatedRoles assocRole where perm.permissionId = "+ role.getRoleId());
			@SuppressWarnings("unchecked")
			List<AbPermissions> results = query.getResultList();
            
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