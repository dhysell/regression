package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AbPermissions;
import persistence.globaldatarepo.entities.AbPermissionsPerRole;
import persistence.globaldatarepo.entities.AbRole;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class AbPermissionsPerRoleHelper {

	public static AbPermissionsPerRole getPermissionsPerRole(AbPermissions perm, AbRole role) throws Exception {
		perm = AbPermissionsHelper.getPermission(perm.getPermission());
		role = AbRoleHelper.getAbRole(role.getRole());
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<AbPermissionsPerRole> permRoleCriteria = builder.createQuery(AbPermissionsPerRole.class);
			Root<AbPermissionsPerRole> permRoleCriteriaRoot = permRoleCriteria.from(AbPermissionsPerRole.class);
			Expression<String> permExpression = permRoleCriteriaRoot.get("abPermissions");
			Expression<String> roleExpression = permRoleCriteriaRoot.get("abRole");
			Predicate permIDLike = builder.like(permExpression, "%" + perm.getPermissionId() + "%");
			Predicate roleIDLike = builder.like(roleExpression, "%" + role.getRoleId() + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(permIDLike);
			predicates.add(roleIDLike);
			permRoleCriteria.select(permRoleCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<AbPermissionsPerRole> results = session.createQuery(permRoleCriteria).getResultList();
						
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
	 public static void main(String[] args) {

      Transaction transaction = null;
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
         transaction = session.beginTransaction();

         CriteriaBuilder builder = session.getCriteriaBuilder();

         // Using FROM and JOIN
         CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
         Root<Employee> empRoot = criteriaQuery.from(Employee.class);
         Root<Department> deptRoot = criteriaQuery.from(Department.class);
         criteriaQuery.multiselect(empRoot, deptRoot);
         criteriaQuery.where(builder.equal(empRoot.get("department"), deptRoot.get("id")));

         Query<Object[]> query=session.createQuery(criteriaQuery);
         List<Object[]> list=query.getResultList();
         for (Object[] objects : list) {
            Employee employee=(Employee)objects[0];
            Department department=(Department)objects[1];
            System.out.println("EMP NAME="+employee.getName()+"\t DEPT NAME="+department.getName());
         }
         transaction.commit();
      } catch (Exception e) {
         e.printStackTrace();
         if (transaction != null) {
            transaction.rollback();
         }
      }
   }
	 */
}
