package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.LienHolder;

public class LienHolderHelper {

	
	
	public static void saveLeinHolderInfo(String accountNumber, String lhName) throws Exception {


		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

			session = sessionFactory.openSession();

			session.beginTransaction();
			LienHolder lhAccount = new LienHolder();
			lhAccount.setLienHolderName(lhName);
			lhAccount.setLienHolderNumber(accountNumber);

			session.save(lhAccount);

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
