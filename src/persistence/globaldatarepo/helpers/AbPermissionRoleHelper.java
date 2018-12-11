package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbPermissionsPerRole;
import persistence.globaldatarepo.entities.AbRole;

import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.util.List;


public class AbPermissionRoleHelper {

	public static AbRole getRole(String roleName) throws Exception {
			
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbRole> roleCriteria = builder.createQuery(AbRole.class);
			Root<AbRole> roleCriteriaRoot = roleCriteria.from(AbRole.class);
			Expression<String> role = roleCriteriaRoot.get("role");
			Predicate roleNameEquals = builder.equal(role, roleName);
			roleCriteria.select(roleCriteriaRoot).where(roleNameEquals);
			
			List<AbRole> results = session.createQuery(roleCriteria).getResultList();
			
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
/*	
	//The Reason I am returning the new AbRole is so that the automatically generated primary key for the newly inserted role can be utilized.
		private static AbPermissions insertPermission(String permissionName, String permissionCode, String permissionDescription) throws Exception {
			
				SessionFactory sessionFactory = null;
				Session session = null;
            PersistenceFactory pf = new PersistenceFactory();
				try {
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
					
					session = sessionFactory.openSession();
					session.beginTransaction();
					
					AbPermissions perm = new AbPermissions();
					perm.setPermission(permissionName);
					perm.setPermissionCode(permissionCode);
					perm.setPermissionDescription(permissionDescription);
					
					session.save(perm);
					session.getTransaction().commit();
					
					
				} catch (Exception e) {			
					e.printStackTrace();
					throw new Exception(e.getMessage());
				} finally {
					session.clear();
                    pf.close();
				}
				
				return getPermission(permissionName);
		}
*/		
//		private static void insertPermissionsPerRole(AbRole role, AbPermissions permission) throws Exception {
//			
//			SessionFactory sessionFactory = null;
//			Session session = null;
//            PersistenceFactory pf = new PersistenceFactory();
//			try {
//                sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
//				
//				session = sessionFactory.openSession();
//				session.beginTransaction();
//				
//				AbPermissionsPerRole permRole = new AbPermissionsPerRole(permission, role);				
//				session.save(permission);
//				session.save(role);
//				
//				
//				session.save(permRole);
//				session.getTransaction().commit();
//				
//				
//			} catch (Exception e) {			
//				e.printStackTrace();
//				throw new Exception(e.getMessage());
//			} finally {
//				session.clear();
//                pf.close();
//			}
//	}
		
		public static AbPermissions getPermission(String _permissionName) throws Exception {
				
			SessionFactory sessionFactory = null;
			Session session = null;
            PersistenceFactory pf = new PersistenceFactory();
			try {
                sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
				session = sessionFactory.openSession();

				session.beginTransaction();
				
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<AbPermissions> permissionCriteria = builder.createQuery(AbPermissions.class);
				Root<AbPermissions> permissionCriteriaRoot = permissionCriteria.from(AbPermissions.class);
				Expression<String> permission = permissionCriteriaRoot.get("permission");
				Predicate permissionNameEquals = builder.equal(permission, _permissionName);
				permissionCriteria.select(permissionCriteriaRoot).where(permissionNameEquals);
				
				List<AbPermissions> results = session.createQuery(permissionCriteria).getResultList();
				
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

		public static boolean removeRolePermissions(String role) throws Exception {
			AbRole abRole = null;
			try{
				abRole = AbRoleHelper.getAbRole(role);
			}catch(Exception e){
				return false;
			}
			
			permissionsPerRoleDeleteRoles(abRole.getRoleId());	
			return true;
		}
		
		@Transient
		private static void permissionsPerRoleDeleteRoles(int roleID) throws Exception {
			SessionFactory sessionFactory = null;
			Session session = null;
	        PersistenceFactory pf = new PersistenceFactory();
	        
			try {
	            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
				session = sessionFactory.openSession();

				session.beginTransaction();
				EntityManager em = session.getEntityManagerFactory().createEntityManager();
				CriteriaBuilder builder = em.getCriteriaBuilder();
				CriteriaDelete<AbPermissionsPerRole> permRoleCriteria = builder.createCriteriaDelete(AbPermissionsPerRole.class);
				Root<AbPermissionsPerRole> permRoleCriteriaRoot = permRoleCriteria.from(AbPermissionsPerRole.class);
				permRoleCriteria.where(builder.equal(permRoleCriteriaRoot.get("permissionRoleId"),roleID));
				em.createQuery(permRoleCriteria).executeUpdate();								
				session.getTransaction().commit();
				 
			} catch (Exception e) {			
				e.printStackTrace();
				throw new Exception(e.getMessage());
			} finally {
				session.clear();
	            pf.close();
			}
		}
			
		//The only way to get a role in the db is to add the permissions.
		//The only way to get permissions in the db is to associate the permissions to the role.
/*		public static void insertRoleAndPermissions(AbRole _role, ArrayList<AbPermissions> permissions) throws Exception {
			try {
				if(getRole(_role.getRole())==null){
					_role  = insertRole(_role.getRole(), _role.getRoleDescription());
				}
			}
			catch(Exception e) {
				
			}
			addPermissionToRole(_role, permissions);		
		}
		
		public static void addPermissionToRole(AbRole role, ArrayList<AbPermissions> permissions) throws Exception {
			AbPermissions permWithID = null;
			for(AbPermissions permission : permissions) {
				try{
					permWithID = getPermission(permission.getPermission());	
				}catch(Exception e) {
					permWithID = insertPermission(permission.getPermission(), permission.getPermissionCode(), permission.getPermissionDescription());
					insertPermissionsPerRole(role, permWithID);
				}
				
			
			}
		}	
	*/	
}
