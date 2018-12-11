package persistence.globaldatarepo.entities;

// Generated May 14, 2015 4:48:29 PM by Hibernate Tools 4.0.0

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;

/**
 * PcCsrsMaster generated by hbm2java
 */
@Entity
@Table(name = "PC_CSRsMasterBAK", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class CSRsBAK {

	private int id;
	private String csrfirstName;
	private String csrlastName;
	private String csruserName;
	private String csragency;
	private String csrregion;
	private String csrpassword;
	private String csrcounty;

	public CSRsBAK() {
	}

	public CSRsBAK(int id) {
		this.id = id;
	}

	public CSRsBAK(int id, String csrfirstName, String csrlastName,
			String csruserName, String csragency, String csrregion, String csrpassword) {
		this.id = id;
		this.csrfirstName = csrfirstName;
		this.csrlastName = csrlastName;
		this.csruserName = csruserName;
		this.csragency = csragency;
		this.csrregion = csrregion;
		this.csrpassword = csrpassword;
	}
	
	public CSRsBAK(int id, String csrfirstName, String csrlastName,
			String csruserName, String csragency, String csrregion, String csrpassword, String county) {
		this.id = id;
		this.csrfirstName = csrfirstName;
		this.csrlastName = csrlastName;
		this.csruserName = csruserName;
		this.csragency = csragency;
		this.csrregion = csrregion;
		this.csrpassword = csrpassword;
		this.csrcounty = county;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "CSRFirstName", length = 50)
	public String getCsrfirstName() {
		return this.csrfirstName;
	}

	public void setCsrfirstName(String csrfirstName) {
		this.csrfirstName = csrfirstName;
	}

	@Column(name = "CSRLastName", length = 50)
	public String getCsrlastName() {
		return this.csrlastName;
	}

	public void setCsrlastName(String csrlastName) {
		this.csrlastName = csrlastName;
	}

	@Column(name = "CSRUserName", length = 50)
	public String getCsruserName() {
		return this.csruserName;
	}

	public void setCsruserName(String csruserName) {
		this.csruserName = csruserName;
	}

	@Column(name = "CSRAgency", length = 50)
	public String getCsragency() {
		return this.csragency;
	}

	public void setCsragency(String csragency) {
		this.csragency = csragency;
	}

	@Column(name = "CSRRegion", length = 50)
	public String getCsrregion() {
		return csrregion;
	}

	public void setCsrregion(String csrregion) {
		this.csrregion = csrregion;
	}
	
	@Column(name = "CSRPassword", length = 20)
	public String getCsrPassword() {
		return csrpassword;
	}

	public void setCsrPassword(String csrpassword) {
		this.csrpassword = csrpassword;
	}

	public String getCsrcounty() {
		return csrcounty;
	}

	public void setCsrcounty(String csrcounty) {
		this.csrcounty = csrcounty;
	}
	
	@Transient
	public static void dropTableRows() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			session.createQuery("DELETE from CSRsBAK").executeUpdate();

			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void createNewCSR( String csrfirstName, String csrlastName,
			String csruserName, String csragency, String csrregion, String csrpassword, String csrcounty) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			CSRsBAK csr = new CSRsBAK();
			csr.setCsrfirstName(csrfirstName);
			csr.setCsrlastName(csrlastName);
			csr.setCsruserName(csruserName);
			csr.setCsragency(csragency);
			csr.setCsrregion(csrregion);
			csr.setCsrPassword(csrpassword);
			csr.setCsrcounty(csrcounty);

			session.save(csr);
			
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
