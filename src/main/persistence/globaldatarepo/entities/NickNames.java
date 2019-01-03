package persistence.globaldatarepo.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

@Entity  //For the DB entries, I used this site https://en.wiktionary.org/wiki/Appendix:English_given_names
@Table(name = "NickNames", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class NickNames {
	
		private int id;
		private String givenName;
		private String hypocoristic;
	
		public NickNames() {
		}
	
		public NickNames(String _givenName, String _hypocoristic ) {
			this.givenName = _givenName;
			this.hypocoristic = _hypocoristic;
		}
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "NickNameID", unique = true, nullable = false)
		public int getNickNameID() {
			return this.id;
		}
		
		public void setNickNameID(int _id) {
			this.id = _id;
		}
		
		@Column(name = "Hypocoristics")
		public String getHypocoristic() {
			return this.hypocoristic;
		}
	
		public void setHypocoristic(String _hypocoristic) {
			this.hypocoristic = _hypocoristic;
		}
		
		@Column(name = "GivenName")
		public String getGivenName() {
			return this.givenName;
		}
	
		public void setGivenName(String _givenName) {
			this.givenName = _givenName;
		}
		
		@Transient
		public static List<NickNames> getHypocoristicsOfGivenName(String givenName) throws Exception {
			SessionFactory sessionFactory = null;
			Session session = null;
	        PersistenceFactory pf = new PersistenceFactory();
			try {
	            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
				session = sessionFactory.openSession();

				session.beginTransaction();
				
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<NickNames> nameCriteria = builder.createQuery(NickNames.class);
				Root<NickNames> nameCriteriaRoot = nameCriteria.from(NickNames.class);
				Expression<String> givenNames = nameCriteriaRoot.get("givenName");
				Predicate givenNameEquals = builder.equal(givenNames, givenName);
				nameCriteria.select(nameCriteriaRoot).where(givenNameEquals);
				
				List<NickNames> results = session.createQuery(nameCriteria).getResultList();
	            
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
		
		@Transient
		public static NickNames getRandomName() throws Exception {
			SessionFactory sessionFactory = null;
			Session session = null;
	        PersistenceFactory pf = new PersistenceFactory();
			try {
	            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
				session = sessionFactory.openSession();

				session.beginTransaction();
				
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<NickNames> nameCriteria = builder.createQuery(NickNames.class);
				Root<NickNames> nameCriteriaRoot = nameCriteria.from(NickNames.class);
//				Expression<String> givenNames = nameCriteriaRoot.get("givenName");
//				Predicate givenNameEquals = builder.equal(givenNames, givenName);
				nameCriteria.select(nameCriteriaRoot);
				
				List<NickNames> results = session.createQuery(nameCriteria).getResultList();
	            
	            session.getTransaction().commit();
	            Random r = new Random();
	            return results.get(r.nextInt(results.size()+1));
			} catch (Exception e) {			
				e.printStackTrace();
				throw new Exception(e.getMessage());
			} finally {
				session.clear();
	            pf.close();
			}
		}
		
/*		
		@Transient
		public void setFirstName(String fName) {
			this.firstName = fName;
		}
		
		@Transient
		public String getLastName() {
			return lastName;
		}
		
		@Transient
		public void setLastName(String lName) {
			this.lastName = lName;
		}
		
		@Transient
		public String getCompanyName() {
			return companyName;
		}
		
		@Transient
		public void setCompanyName(String cName) {
			this.companyName = cName;
		}
*/		
		
	}
