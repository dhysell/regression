package persistence.guidewire.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.guidewire.entities.AbAbcontact;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class TerminatedContactsHelper {
	
	public boolean isLienholderContactTerminated(String lienholderNumber) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewireAB8DEV);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			Query query = em.createNativeQuery("SELECT termination.TerminationDate" + 
					"FROM dbo.ab_abcontact contact join dbo.abx_Termination_FBM termination ON contact.ID = termination.ContactID" + 
					"WHERE contact.LienholderNumber_FBM = '" + lienholderNumber + "' and termination.TerminationDate is not null", AbAbcontact.class);
			
			boolean result = query.getResultList() != null;
            
            session.getTransaction().commit();
            
            return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public List<String> getTerminatedLienholderContacts(List<String> lienholderNumbersListToCheck) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        List<String> listOfTerminatedContacts = new ArrayList<String>();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GuidewireAB8DEV);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			for (String lienholderNumber : lienholderNumbersListToCheck) {
				EntityManager em = session.getEntityManagerFactory().createEntityManager();
				Query query = em.createNativeQuery("SELECT termination.TerminationDate " + 
						"FROM dbo.ab_abcontact contact join dbo.abx_Termination_FBM termination ON contact.ID = termination.ContactID " + 
						"WHERE contact.LienholderNumber_FBM = '" + lienholderNumber + "' and termination.TerminationDate is not null");
				
				if (query.getResultList().size() > 0) {
					listOfTerminatedContacts.add(lienholderNumber);
				}
			}
            
            session.getTransaction().commit();
            
            return listOfTerminatedContacts;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
