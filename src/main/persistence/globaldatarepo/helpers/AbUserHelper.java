package persistence.globaldatarepo.helpers;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbRole;
import persistence.globaldatarepo.entities.AbUsers;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbUserHelper {
	
	public static List<AbUsers> getAllUsers() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
			Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
			abUserCriteria.select(abUserCriteriaRoot);
			
			List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();
			
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
	
    public static List<AbUsers> getAllDeptUsers(String dept) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> departmentName = abUserCriteriaRoot.get("userDepartment");
            Predicate departmentNameLike = builder.like(departmentName, "%" + dept + "%");
            abUserCriteria.select(abUserCriteriaRoot).where(departmentNameLike);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

			session.getTransaction().commit();
			
			return results;
			
		} catch (Exception e) {			
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
		
	}
    
    public static AbUsers getRandomDeptUser(String dept) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> departmentName = abUserCriteriaRoot.get("userDepartment");
            Predicate departmentNameLike = builder.like(departmentName, "%" + dept + "%");
            abUserCriteria.select(abUserCriteriaRoot).where(departmentNameLike);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());
            session.getTransaction().commit();
            return results.get(index);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }
    
    public static AbUsers getRandomUserByTitle(String title) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> departmentName = abUserCriteriaRoot.get("userTitle");
            Predicate departmentNameLike = builder.like(departmentName, "%" + title + "%");
            abUserCriteria.select(abUserCriteriaRoot).where(departmentNameLike);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());
            session.getTransaction().commit();
            return results.get(index);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }
    
  //get user by username
    public static AbUsers getUserByUserName(String userName) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserName = abUserCriteriaRoot.get("userName");
            Predicate abUserNameEquals = builder.equal(abUserName, userName);
            abUserCriteria.select(abUserCriteriaRoot).where(abUserNameEquals);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

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
    
    public static AbUsers getUserByFirstLastName(String userFirstName, String userLastName) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserFirstName = abUserCriteriaRoot.get("firstName");
            Expression<String> abUserLastName = abUserCriteriaRoot.get("lastName");
            Predicate abUserFirstNameLike = builder.like(abUserFirstName, "%" + userFirstName + "%");
            Predicate abUserLastNameLike = builder.like(abUserLastName, "%" + userLastName + "%");
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(abUserFirstNameLike);
            predicates.add(abUserLastNameLike);
            abUserCriteria.select(abUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

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
    
    public static AbUsers getUserByFullName(String userFullName) throws Exception {
        String[] userNameArray = userFullName.split(" ");
        String userFirstName = userNameArray[0];
        String userLastName = userNameArray[(userNameArray.length - 1)];

        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserFirstName = abUserCriteriaRoot.get("firstName");
            Expression<String> abUserLastName = abUserCriteriaRoot.get("lastName");
            Predicate abUserFirstNameLike = builder.like(abUserFirstName, "%" + userFirstName + "%");
            Predicate abUserLastNameLike = builder.like(abUserLastName, "%" + userLastName + "%");
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(abUserFirstNameLike);
            predicates.add(abUserLastNameLike);
            abUserCriteria.select(abUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

            session.getTransaction().commit();

            return results.get(0);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }
	//**********ABRole and ABPermissions********************************************************************************************************************************************************************************************
	/*Assumptions:
	 * ABRole has a many to many relationship with ABUsers as well as with ABPermissions.
	 * If both sides of a many to many relationship exist in the database, it is assumed that the relationship is represented in the many to many table.
	 * 
	 * Users, Roles and Permissions have everything they need to be added to the database except the association with the method will create and for Roles and Permissions, id's.
	 * 
	 * */
	
	private static void addPermRoleUser(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();

			role.getAssociatedUsers().add(user);
			role.getAssociatedPermissions().add(perm);
			user.getAbRoles().add(role);
			perm.getAssociatedRoles().add(role);
			
			session.save(perm);
			session.save(role);
			session.save(user);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addPermRoleUpdateUser(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			user.getAbRoles().add(role);
			
			role.getAssociatedUsers().add(user);
			role.getAssociatedPermissions().add(perm);
			
			perm.getAssociatedRoles().add(role);

			session.save(perm);
			session.save(role);
			session.update(user);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addUserToRole(AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			user.getAbRoles().add(role);
			role.getAssociatedUsers().add(user);
			
			session.save(user);
			session.update(role);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addRoleToUser(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			user.getAbRoles().add(role);
			
			role.getAssociatedUsers().add(user);
			role.getAssociatedPermissions().add(perm);
			
			perm.getAssociatedRoles().add(role);
			
			session.save(role);
			session.update(user);
			session.update(perm);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addRoleUserUpdatePerm(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			user.getAbRoles().add(role);
			
			role.getAssociatedUsers().add(user);
			role.getAssociatedPermissions().add(perm);
			
			perm.getAssociatedRoles().add(role);
			
			session.save(role);
			session.save(user);
			session.update(perm);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addPermissionToRole(AbPermissions perm, AbRole role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);	
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			perm.getAssociatedRoles().add(role);
			role.getAssociatedPermissions().add(perm);
			
			session.save(perm);
			session.update(role);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addPermissionAndRole(AbPermissions perm, AbRole role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);	
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			perm.getAssociatedRoles().add(role);
			role.getAssociatedPermissions().add(perm);
			
			session.save(perm);
			session.save(role);
			session.getTransaction().commit();
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addPermRoleToJoinTable(AbPermissions perm, AbRole role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);			
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			perm.getAssociatedRoles().add(role);
			role.getAssociatedPermissions().add(perm);
			try {
				session.update(perm);
				session.update(role);
				session.getTransaction().commit();
			}catch(NonUniqueObjectException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
	
	private static void addUserRoleToJoinTable(AbRole role, AbUsers user) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);	
			
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			user.getAbRoles().add(role);
			role.getAssociatedUsers().add(user);
			try {
				session.update(user);
				session.update(role);
				session.getTransaction().commit();
			}catch(NonUniqueObjectException e) {
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}
		
	//******************************************* Methods to ascertain what data needs to be added *************************************************************************************************************************************************************************************
	
	public static boolean[] addPermRoleUserIfNotInDataBase(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
		boolean[] found = new boolean[3];
		boolean roleFound = false;
		if(role != null) {
			try{
				role = AbRoleHelper.getAbRole(role.getRole());
				roleFound = true;
			}catch(Exception e) {
				roleFound = false;
			}
		}
		boolean permFound = false;
		if(perm != null) {
			try {
				perm = AbPermissionsHelper.getPermission(perm.getPermission());
				permFound = true;
			}catch(Exception e) {
				permFound = false;
			}
		}
				
		boolean userFound = false;
		if(user != null) {
			try{
				user = getUserByUserName(user.getUserName());
				userFound = true;
			}catch(Exception e) {
				userFound = false;
			}
		}
		found[0] = permFound;
		found[1] = roleFound;
		found[2] = userFound;
		
		//determines if role exists in DB or is null
		if(found[1] == true) {
			roleExists(perm, role, user, found);
		}
		
		else if(role != null) {
			roleDoesNotExist(perm, role, user, found);
		}
		else {
			
		}
		return found;
	}		
	
	
	
	private static void roleExists(AbPermissions perm, AbRole role, AbUsers user, boolean[] found ) throws Exception {
		//It has been determined that the Role exists, so now just the Perm and the User.
		if(found[0] == false) {
			if(perm != null) {
				addPermissionToRole(perm, role);
			}
		} else {
			if(found[2] == false) {
				if(user != null) {
					//Add user to role and the permission to join table.
				}
			}
			//add perm
				addPermRoleToJoinTable(perm, role);
		}
		if(found[2] == false) {
			if(user != null) {
					addUserToRole(role, user);
			}
		} else {
			addUserRoleToJoinTable(role, user);
		}
	}
	
	private static void roleDoesNotExist(AbPermissions perm, AbRole role, AbUsers user, boolean[] found ) throws Exception {
		if(role != null) {
			if(found[0]) {
				if(!found[2]) {
					if(user != null) {
						addRoleUserUpdatePerm(perm, role, user);
					}
				} else {
					addRoleToUser(perm, role, user);
				}
			} else {
				if(perm!=null) {
					if(!found[2]) {
						if(user != null) {
							addPermRoleUser(perm, role, user);
						} else {
							addPermissionAndRole(perm, role);
						}

					} else {
						addPermRoleUpdateUser(perm, role, user);
					}
				} 				
			}
		}
	}
	
	private static void permissionsPerRoleDeleteRoles(String roleID) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			@SuppressWarnings("unchecked")
			List<Object[]> rows = (List<Object[]>)em.createNativeQuery("DELETE FROM AB_PermissionsPerRole WHERE RoleID = '+roleID+'").getResultList();			
			
			session.getTransaction().commit();
			 
			 
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}

/*  Every thing below is a merge after Ian implemented Persistence Factory on 3/7/18.
 	The merge did not do well and I am afraid I may have missed needed code.
 	The class after Ian completed his changes is below in case of need.

package persistence.globaldatarepo.helpers;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbPermissionsPerRole;
import persistence.globaldatarepo.entities.AbRole;
import persistence.globaldatarepo.entities.AbUserRole;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AbUserHelper {


    public static List<AbUsers> getAllUsersByDept(String dept) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> departmentName = abUserCriteriaRoot.get("userDepartment");
            Predicate departmentNameLike = builder.like(departmentName, "%" + dept + "%");
            abUserCriteria.select(abUserCriteriaRoot).where(departmentNameLike);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

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


    public static AbUsers getRandomDeptUser(String dept) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> departmentName = abUserCriteriaRoot.get("userDepartment");
            Predicate departmentNameLike = builder.like(departmentName, "%" + dept + "%");
            abUserCriteria.select(abUserCriteriaRoot).where(departmentNameLike);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());
            session.getTransaction().commit();
            return results.get(index);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }

    //get user by username
    public static AbUsers getUserByUserName(String userName) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserName = abUserCriteriaRoot.get("userName");
            Predicate abUserNameEquals = builder.equal(abUserName, userName);
            abUserCriteria.select(abUserCriteriaRoot).where(abUserNameEquals);

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

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

    public static AbUsers getUserByFirstLastName(String userFirstName, String userLastName) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserFirstName = abUserCriteriaRoot.get("firstName");
            Expression<String> abUserLastName = abUserCriteriaRoot.get("lastName");
            Predicate abUserFirstNameLike = builder.like(abUserFirstName, "%" + userFirstName + "%");
            Predicate abUserLastNameLike = builder.like(abUserLastName, "%" + userLastName + "%");
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(abUserFirstNameLike);
            predicates.add(abUserLastNameLike);
            abUserCriteria.select(abUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

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

    public static AbUsers getUserInfoByFullName(String userFullName) throws Exception {
        String[] userNameArray = userFullName.split(" ");
        String userFirstName = userNameArray[0];
        String userLastName = userNameArray[(userNameArray.length - 1)];

        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AbUsers> abUserCriteria = builder.createQuery(AbUsers.class);
            Root<AbUsers> abUserCriteriaRoot = abUserCriteria.from(AbUsers.class);
            Expression<String> abUserFirstName = abUserCriteriaRoot.get("firstName");
            Expression<String> abUserLastName = abUserCriteriaRoot.get("lastName");
            Predicate abUserFirstNameLike = builder.like(abUserFirstName, "%" + userFirstName + "%");
            Predicate abUserLastNameLike = builder.like(abUserLastName, "%" + userLastName + "%");
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(abUserFirstNameLike);
            predicates.add(abUserLastNameLike);
            abUserCriteria.select(abUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AbUsers> results = session.createQuery(abUserCriteria).getResultList();

            session.getTransaction().commit();

            return results.get(0);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }

    //**********ABRole and AbUsers******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
    private static AbUsers insertUser(AbUsers user) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
        return getUserByUserName(user.getUserName());
    }

    public static void setUserRole(AbRole role, Set<AbUsers> userSet) throws Exception {
        if (AbUserHelper.getAbRole(role.getRole()).getRoleId() < 1) {
            role = AbUserHelper.insertRole(role);
        }
        for (AbUsers user : userSet) {
            if (AbUserHelper.getUserByUserName(user.getUserName()) == null) {
                user = AbUserHelper.insertUser(user);
            }
            insertAbUserRole(role, user);
        }
    }

    private static void insertAbUserRole(AbRole role, AbUsers user) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();
            session.beginTransaction();

            AbUserRole userRole = new AbUserRole();
            userRole.setAbRole(role);
            userRole.setAbUsers(user);


            session.save(userRole);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }


    //**********ABRole and ABPermissions********************************************************************************************************************************************************************************************
    private static AbRole insertRole(AbRole role) throws Exception {

        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(role);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
        return getAbRole(role.getRole());
    }

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

    private static AbPermissions insertPermission(AbPermissions perm) throws Exception {

        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.save(perm);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
        return getPermission(perm.getPermission());
    }

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
            Predicate permNameLike = builder.like(permName, "%" + StringsUtils.specialCharacterEscape(perm) + "%");
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

    /*
        private static void insertRolePermSet(Set<AbPermissions> perms, AbRole role) throws Exception {
            for(AbPermissions perm : perms) {
                insertRolePerm(perm, role);
            }
        }
    */
/*
    private static void insertRolePerm(AbPermissions perm, AbRole role) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();
            session.beginTransaction();

            AbPermissionsPerRole permRole = new AbPermissionsPerRole(perm, role);

            session.save(permRole);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    //Ensure that the objects are attached to each other.
    public static void setRolePerm(AbRole role, Set<AbPermissions> permSet) throws Exception {
        try {
            AbUserHelper.getAbRole(role.getRole());
        } catch (Exception e) {
            role = AbUserHelper.insertRole(role);
        }
        for (AbPermissions perm : permSet) {
            try {
                AbUserHelper.getPermission(perm.getPermission());
            } catch (Exception e) {

                perm = AbUserHelper.insertPermission(perm);
            }

            insertRolePerm(perm, role);
        }
    }

    public static void addPermissionToRole(AbPermissions perm, AbRole role) throws Exception {
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
            Expression<String> roleNameExpression = roleCriteriaRoot.get("role");
            Predicate roleNameLike = builder.like(roleNameExpression, "%" + StringsUtils.specialCharacterEscape(role.getRole()) + "%");
            roleCriteria.select(roleCriteriaRoot).where(roleNameLike);

            List<AbRole> results = session.createQuery(roleCriteria).getResultList();

            AbRole selectedRole = results.get(0);

            AbPermissionsPerRole permPerRole = new AbPermissionsPerRole(perm, selectedRole);
            perm.getAbPermissionsPerRoles().add(permPerRole);
            selectedRole.getAbPermissionsPerRoles().add(permPerRole);

            session.save(perm);
            session.save(permPerRole);
            session.update(selectedRole);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }

    public static void addUserToRole(AbUsers user, AbRole role) throws Exception {
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
            Expression<String> roleNameExpression = roleCriteriaRoot.get("role");
            Predicate roleNameLike = builder.like(roleNameExpression, "%" + StringsUtils.specialCharacterEscape(role.getRole()) + "%");
            roleCriteria.select(roleCriteriaRoot).where(roleNameLike);

            List<AbRole> results = session.createQuery(roleCriteria).getResultList();

            AbRole selectedRole = results.get(0);

            AbUserRole userRole = new AbUserRole(selectedRole, user);
            user.getAbUserRoles().add(userRole);
            selectedRole.getAbUserRoles().add(userRole);

            session.save(user);
            session.save(userRole);
            session.update(selectedRole);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void setUsersRolesPermissions(ArrayList<AbPermissions> perms, AbRole role, ArrayList<AbUsers> users) throws Exception {
        setStuff(perms.get(0), role, users.get(0));
        role = getAbRole(role.getRole());
        if (!perms.isEmpty()) {
            for (int i = 1; i < perms.size(); i++) {
                addPermissionToRole(perms.get(i), role);
            }
        }
        if (!users.isEmpty()) {
            for (int i = 1; i < perms.size(); i++) {
                addUserToRole(users.get(i), role);
            }
        }
    }

    //I decided that because users and Permissions both have a many to many relationship with roles,
    //that the users and permissions will be added to each role to keep them straight.
    public static void setStuff(AbPermissions perm, AbRole role, AbUsers user) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();

        boolean roleFound = false;
        try {
            role = getAbRole(role.getRole());
            roleFound = true;
        } catch (Exception e) {
            roleFound = false;
        }
        if (!roleFound) {
            boolean permFound = false;
            if (perm != null) {
                try {
                    perm = getPermission(perm.getPermission());
                    permFound = true;
                } catch (Exception e) {
                    permFound = false;
                }
            }

            boolean userFound = false;
            if (user != null) {
                try {
                    user = getUserByUserName(user.getUserName());
                    userFound = true;
                } catch (Exception e) {
                    userFound = false;
                }
            }

            if (!roleFound && !permFound && userFound) {

                try {
                    sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
                    session = sessionFactory.openSession();
                    session.beginTransaction();
                    session.save(role);
                    session.save(perm);
                    session.save(user);

                    AbPermissionsPerRole permRole = new AbPermissionsPerRole(perm, role);
                    permRole.setAbPermissions(perm);
                    permRole.setAbRole(role);

                    perm.getAbPermissionsPerRoles().add(permRole);
                    role.getAbPermissionsPerRoles().add(permRole);

                    session.save(permRole);


                    AbUserRole userRole = new AbUserRole();
                    userRole.setAbRole(role);
                    userRole.setAbUsers(user);

                    role.getAbUserRoles().add(userRole);
                    user.getAbUserRoles().add(userRole);

                    session.save(userRole);

                    session.getTransaction().commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception(e.getMessage());
                } finally {
                    session.clear();
                    pf.close();
                }
            }
        }
    }
}
*/






