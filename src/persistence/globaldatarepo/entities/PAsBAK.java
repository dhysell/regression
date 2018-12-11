package persistence.globaldatarepo.entities;

// Generated May 14, 2015 4:39:55 PM by Hibernate Tools 4.0.0

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;

/**
 * PcProductionAssistantMaster generated by hbm2java
 */
@Entity
@Table(name = "PC_ProductionAssistantMasterBAK", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class PAsBAK {

	private int id;
	private String productionAssistantsName;
	private String pauserName;
	private String pafirstName;
	private String palastName;
	private String paassociatedAgent;

	public PAsBAK() {
	}

	public PAsBAK(int id) {
		this.id = id;
	}

	public PAsBAK(int id, String productionAssistantsName,
			String pauserName, String pafirstName, String palastName,
			String paassociatedAgent) {
		this.id = id;
		this.productionAssistantsName = productionAssistantsName;
		this.pauserName = pauserName;
		this.pafirstName = pafirstName;
		this.palastName = palastName;
		this.paassociatedAgent = paassociatedAgent;
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

	@Column(name = "ProductionAssistantsName", length = 100)
	public String getProductionAssistantsName() {
		return this.productionAssistantsName;
	}

	public void setProductionAssistantsName(String productionAssistantsName) {
		this.productionAssistantsName = productionAssistantsName;
	}

	@Column(name = "PAUserName", length = 50)
	public String getPauserName() {
		return this.pauserName;
	}

	public void setPauserName(String pauserName) {
		this.pauserName = pauserName;
	}

	@Column(name = "PAFirstName", length = 50)
	public String getPafirstName() {
		return this.pafirstName;
	}

	public void setPafirstName(String pafirstName) {
		this.pafirstName = pafirstName;
	}

	@Column(name = "PALastName", length = 50)
	public String getPalastName() {
		return this.palastName;
	}

	public void setPalastName(String palastName) {
		this.palastName = palastName;
	}

	@Column(name = "PAAssociatedAgent", length = 100)
	public String getPaassociatedAgent() {
		return this.paassociatedAgent;
	}

	public void setPaassociatedAgent(String paassociatedAgent) {
		this.paassociatedAgent = paassociatedAgent;
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
			
			session.createQuery("DELETE from PAsBAK").executeUpdate();

			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void createNewPA(String productionAssistantsName, String pauserName, String pafirstName, String palastName, String paassociatedAgent) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();
			session.beginTransaction();

			PAsBAK pa = new PAsBAK();
			pa.setProductionAssistantsName(productionAssistantsName);
			pa.setPafirstName(pafirstName);
			pa.setPalastName(palastName);
			pa.setPauserName(pauserName);
			pa.setPaassociatedAgent(paassociatedAgent);

			session.save(pa);

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