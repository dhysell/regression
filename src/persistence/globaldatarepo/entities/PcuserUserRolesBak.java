package persistence.globaldatarepo.entities;
// default package
// Generated Jun 12, 2017 1:43:00 PM by Hibernate Tools 5.2.0.Beta1

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PcuserUserRolesBak generated by hbm2java
 */
@Entity
@Table(name = "PCUser_UserRolesBAK", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class PcuserUserRolesBak {

	private Integer id;
	private String userName;
	private String role;

	public PcuserUserRolesBak() {
	}

	public PcuserUserRolesBak(String userName, String role) {
		this.userName = userName;
		this.role = role;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "UserName", nullable = false, length = 100)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "Role", nullable = false, length = 100)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Transient
	public static void createNewUserRole(String userName, String role) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();
			session.beginTransaction();

			PcuserUserRolesBak pcRole = new PcuserUserRolesBak(userName, role);

			session.save(pcRole);

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
