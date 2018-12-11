package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.ClaimsFNOLData;

import java.time.LocalDateTime;

public class ClaimsDataHelper {

	public static void writeFNOL(String claimNumber, String policyNumber, String dol, String lob, String environment) {
		
		SessionFactory sessionFactory = null;
		Session session = null;	
		Transaction transaction = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();


			ClaimsFNOLData fnolData = new ClaimsFNOLData();
			fnolData.setClaimNumber(claimNumber);
			fnolData.setPolicyNumber(policyNumber);
			fnolData.setDateOfLoss(dol);
			fnolData.setTimeStamp(LocalDateTime.now().toString());
			fnolData.setLob(lob);
			fnolData.setEnvironment(environment);
	
			session.save(fnolData);
			
			transaction.commit();
			
		
		} catch (Exception e) {	
			e.printStackTrace();
			if (transaction!=null) transaction.rollback();
			//throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}

