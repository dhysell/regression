package persistence.globaldatarepo.entities;

// Generated May 14, 2015 4:46:37 PM by Hibernate Tools 4.0.0

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;

/**
 * PcServiceAssociateMaster generated by hbm2java
 */
@Entity
@Table(name = "PC_ServiceAssociateMasterBAK", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class SAsBAK {

	private int id;
	private String servicingAssistantsName;
	private String sauserName;
	private String safirstName;
	private String salastName;
	private String saassociatedAgent;

	public SAsBAK() {
	}

	public SAsBAK(int id) {
		this.id = id;
	}

	public SAsBAK(int id, String servicingAssistantsName,
			String sauserName, String safirstName, String salastName,
			String saassociatedAgent) {
		this.id = id;
		this.servicingAssistantsName = servicingAssistantsName;
		this.sauserName = sauserName;
		this.safirstName = safirstName;
		this.salastName = salastName;
		this.saassociatedAgent = saassociatedAgent;
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

	@Column(name = "ServicingAssistantsName", length = 100)
	public String getServicingAssistantsName() {
		return this.servicingAssistantsName;
	}

	public void setServicingAssistantsName(String servicingAssistantsName) {
		this.servicingAssistantsName = servicingAssistantsName;
	}

	@Column(name = "SAUserName", length = 50)
	public String getSauserName() {
		return this.sauserName;
	}

	public void setSauserName(String sauserName) {
		this.sauserName = sauserName;
	}

	@Column(name = "SAFirstName", length = 50)
	public String getSafirstName() {
		return this.safirstName;
	}

	public void setSafirstName(String safirstName) {
		this.safirstName = safirstName;
	}

	@Column(name = "SALastName", length = 50)
	public String getSalastName() {
		return this.salastName;
	}

	public void setSalastName(String salastName) {
		this.salastName = salastName;
	}

	@Column(name = "SAAssociatedAgent", length = 100)
	public String getSaassociatedAgent() {
		return this.saassociatedAgent;
	}

	public void setSaassociatedAgent(String saassociatedAgent) {
		this.saassociatedAgent = saassociatedAgent;
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
			
			session.createQuery("DELETE from SAsBAK").executeUpdate();

			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void createNewSA(String serviceAssistantsName, String sauserName,
			String safirstName, String salastName, String saassociatedAgent) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			SAsBAK sa = new SAsBAK();
			sa.setServicingAssistantsName(serviceAssistantsName);
			sa.setSafirstName(safirstName);
			sa.setSalastName(salastName);
			sa.setSauserName(sauserName);
			sa.setSaassociatedAgent(saassociatedAgent);

			session.save(sa);
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
