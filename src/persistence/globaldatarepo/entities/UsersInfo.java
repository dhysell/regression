package persistence.globaldatarepo.entities;
// Generated Nov 29, 2018 1:09:51 PM by Hibernate Tools 5.2.10.Final

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;

/**
 * UsersInfo generated by hbm2java
 */
@Entity
@Table(name = "Users_Info", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class UsersInfo {

	private String username;
	private String firstName;
	private String middleName;
	private String lastName;
	private String preferedName;
	private String suffix;
	private String prefix;
	private String password;
	private String jobTitle;
	private String number;

	public UsersInfo() {
	}

	public UsersInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public UsersInfo(String username, String firstName, String middleName, String lastName, String preferedName,
			String suffix, String prefix, String password, String jobTitle, String number) {
		this.username = username;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.preferedName = preferedName;
		this.suffix = suffix;
		this.prefix = prefix;
		this.password = password;
		this.jobTitle = jobTitle;
		this.number = number;
	}

	@Id

	@Column(name = "USERNAME", unique = true, nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "FIRST_NAME", length = 200)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MIDDLE_NAME", length = 200)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LAST_NAME", length = 200)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "PREFERED_NAME", length = 200)
	public String getPreferedName() {
		return this.preferedName;
	}

	public void setPreferedName(String preferedName) {
		this.preferedName = preferedName;
	}

	@Column(name = "SUFFIX", length = 50)
	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "PREFIX", length = 50)
	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "PASSWORD", nullable = false, length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "JOB_TITLE", length = 200)
	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	@Column(name = "NUMBER", length = 50)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	
	@Transient
	public static void updateUsersInfoTable(UsersInfo info) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();

			session.beginTransaction();
			session.save(info);

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
