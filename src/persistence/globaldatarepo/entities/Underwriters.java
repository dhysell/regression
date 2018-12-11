package persistence.globaldatarepo.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.helpers.StringsUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@Entity
@Table(name = "PC_UnderwritersMaster", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Underwriters {

	public int id;
	public String underwriterFirstName;
	public String underwriterLastName;
	public String underwriterUserName;
	public String underwriterPassword;
	public String underwriterLine;
	public String underwriterTitle;
	//public String underwriterAccess;

	public Underwriters() {
	}

	public Underwriters(int id, String uwfirstName) {
		this.id = id;
		this.underwriterFirstName = uwfirstName;
	}

	public Underwriters(int id, String uwFirstName, String uwLastName,
			String uwUserName, String uwPassword, String uwLine, String uwTitle) {
		this.id = id;
		this.underwriterFirstName = uwFirstName;
		this.underwriterLastName = uwLastName;
		this.underwriterUserName = uwUserName;
		this.underwriterPassword = uwPassword;
		this.underwriterLine = uwLine;
		this.underwriterTitle = uwTitle;
	}
	
	public Underwriters(String uwFirstName, String uwLastName,
			String uwUserName, String uwPassword, String uwLine, String uwTitle) {
		this.underwriterFirstName = uwFirstName;
		this.underwriterLastName = uwLastName;
		this.underwriterUserName = uwUserName;
		this.underwriterPassword = uwPassword;
		this.underwriterLine = uwLine;
		this.underwriterTitle = uwTitle;
	}
	
	public Underwriters(String uwFirstName, String uwLastName,
			String uwUserName, String uwPassword) {
		this.underwriterFirstName = uwFirstName;
		this.underwriterLastName = uwLastName;
		this.underwriterUserName = uwUserName;
		this.underwriterPassword = uwPassword;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "UWFirstName", nullable = false, length = 50)
	public String getUnderwriterFirstName() {
		return this.underwriterFirstName;
	}

	public void setUnderwriterFirstName(String uwfirstName) {
		this.underwriterFirstName = uwfirstName;
	}

	@Column(name = "UWLastName", length = 50)
	public String getUnderwriterLastName() {
		return this.underwriterLastName;
	}

	public void setUnderwriterLastName(String uwlastName) {
		this.underwriterLastName = uwlastName;
	}

	@Column(name = "UWUserName", length = 50)
	public String getUnderwriterUserName() {
		return this.underwriterUserName;
	}

	public void setUnderwriterUserName(String uwuserName) {
		this.underwriterUserName = uwuserName;
	}
	
	@Column(name = "UWPassword", length = 50)
	public String getUnderwriterPassword() {
		return this.underwriterPassword;
	}

	public void setUnderwriterPassword(String uwpassword) {
		this.underwriterPassword = uwpassword;
	}
	
	@Column(name = "UWLine", length = 50)
	public String getUnderwriterLine() {
		return this.underwriterLine;
	}

	public void setUnderwriterLine(String uwLine) {
		this.underwriterLine = uwLine;
	}
	
	@Column(name = "UWTitle", length = 50)
	public String getUnderwriterTitle() {
		return this.underwriterTitle;
	}

	public void setUnderwriterTitle(String uwTitle) {
		this.underwriterTitle = uwTitle;
	}
	
//	@Column(name = "UWAccess", length = 100)
//	public String getUnderwriterAccess() {
//		return underwriterAccess;
//	}
//
//	public void setUnderwriterAccess(String underwriterAccess) {
//		this.underwriterAccess = underwriterAccess;
//	}
	
	
	
	
	@Transient
	public static Underwriters getUnderwriterByCounty(String county) throws Exception {	
		
		
		String uwName = "";
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegionCounty> underwriterCriteria = builder.createQuery(RegionCounty.class);
			Root<RegionCounty> underwriterCriteriaRoot = underwriterCriteria.from(RegionCounty.class);
			Expression<String> underwriterCounty = underwriterCriteriaRoot.get("countyName");
			Predicate underwriterCountyLike = builder.like(underwriterCounty, "%" + StringsUtils.specialCharacterEscape(county) + "%");
			underwriterCriteria.select(underwriterCriteriaRoot).where(underwriterCountyLike);
			
			List<RegionCounty> results = session.createQuery(underwriterCriteria).getResultList();
			
			session.getTransaction().commit();
			
			uwName = results.get(0).getUnderWriter();
			
			return UnderwritersHelper.getUnderwriterInfoByFullName(uwName);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	

}
