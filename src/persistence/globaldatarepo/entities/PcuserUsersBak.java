package persistence.globaldatarepo.entities;
// default package
// Generated Jun 12, 2017 1:37:05 PM by Hibernate Tools 5.2.0.Beta1

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * PcuserUsersBak generated by hbm2java
 */
@Entity
@Table(name = "PCUser_UsersBAK", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class PcuserUsersBak {

	private String userName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String suffix;
	private String alternateName;
	private String jobTitle;
	private String active;
	private String userType;

	public PcuserUsersBak() {
	}

	public PcuserUsersBak(String userName, String firstName, String lastName) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public PcuserUsersBak(String userName, String firstName, String middleName, String lastName, String suffix, String alternateName, String jobTitle, String active, String userType) {
		this.userName = userName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.alternateName = alternateName;
		this.jobTitle = jobTitle;
		this.active = active;
		this.userType = userType;
	}

	@Id

	@Column(name = "UserName", unique = true, nullable = false, length = 100)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "FirstName", nullable = false, length = 100)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MiddleName", length = 100)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LastName", nullable = false, length = 100)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "Suffix", length = 50)
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "AlternateName", length = 200)
	public String getAlternateName() {
		return this.alternateName;
	}

	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}

	@Column(name = "JobTitle", length = 100)
	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(name = "Active", length = 50)
	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Column(name = "UserType", length = 100)
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	@Transient
	public static void createNewUser(String userName, String firstName, String middleName, String lastName, String suffix, String alternateName, String jobTitle, String active, String userType) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();
			session.beginTransaction();

			PcuserUsersBak pcRole = new PcuserUsersBak(userName, firstName, middleName, lastName, suffix, alternateName, jobTitle, active, userType);

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
	
	@Transient
	public static void updateUser(String userName, String firstName, String middleName, String lastName, String suffix, String alternateName, String jobTitle, String active, String userType) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PcuserUsersBak> pcuserUsersBakCriteria = builder.createQuery(PcuserUsersBak.class);
			Root<PcuserUsersBak> pcuserUsersBakCriteriaRoot = pcuserUsersBakCriteria.from(PcuserUsersBak.class);
			Expression<String> pcUsername = pcuserUsersBakCriteriaRoot.get("userName");
			Predicate pcUserNameEquals = builder.equal(pcUsername, userName);
			pcuserUsersBakCriteria.select(pcuserUsersBakCriteriaRoot).where(pcUserNameEquals);
			
			List<PcuserUsersBak> results = session.createQuery(pcuserUsersBakCriteria).getResultList();
			
			PcuserUsersBak user = results.get(0);
			user.setFirstName(firstName);
			user.setMiddleName(middleName);
			user.setLastName(lastName);
			user.setSuffix(suffix);
			user.setAlternateName(alternateName);
			user.setJobTitle(jobTitle);
			user.setActive(active);
			user.setUserType(userType);

			session.update(user);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	@Transient
	public static List<PcuserUsersBak> getAllUsers() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PcuserUsersBak> formCriteria = builder.createQuery(PcuserUsersBak.class);
			Root<PcuserUsersBak> pcuserUsersBakCriteriaRoot = formCriteria.from(PcuserUsersBak.class);
			formCriteria.select(pcuserUsersBakCriteriaRoot);
			
			List<PcuserUsersBak> results = session.createQuery(formCriteria).getResultList();
            
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
