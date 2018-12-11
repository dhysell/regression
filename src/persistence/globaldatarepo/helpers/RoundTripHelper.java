package persistence.globaldatarepo.helpers;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.PcRoundTrip;

import javax.persistence.criteria.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RoundTripHelper {public RoundTripHelper() {

}
public static PcRoundTrip getRoundTrip() throws Exception {	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -2);

	String dateToGet = dateFormat.format(cal.getTime());


	SessionFactory sessionFactory = null;
	Session session = null;
    PersistenceFactory pf = new PersistenceFactory();
	try {
        sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
		session = sessionFactory.openSession();

		session.beginTransaction();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<PcRoundTrip> orderCriteria = builder.createQuery(PcRoundTrip.class);
		Root<PcRoundTrip> orderCriteriaRoot = orderCriteria.from(PcRoundTrip.class);
		Expression<String> accountDateExpression = orderCriteriaRoot.get("accountDate");
		Predicate accountDateLike = builder.like(accountDateExpression, dateToGet);
		orderCriteria.select(orderCriteriaRoot).where(accountDateLike);
		
		List<PcRoundTrip> results = session.createQuery(orderCriteria).getResultList();
		
		session.getTransaction().commit();
		if(results.isEmpty()) {
			return null;
		} else {
			return results.get(results.size()-1);
		}

	} catch (Exception e) {			
		e.printStackTrace();
		throw new Exception(e.getMessage());
	} finally {
		session.clear();
        pf.close();
	}
}



public static void saveRoundTripAccount(int accountNumber, String userName) throws Exception {
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, 0);

	String dateToGet = dateFormat.format(cal.getTime());

	SessionFactory sessionFactory = null;
	Session session = null;
    PersistenceFactory pf = new PersistenceFactory();
	try {
        sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

		session = sessionFactory.openSession();

		session.beginTransaction();
		PcRoundTrip rtAccount = new PcRoundTrip();
		rtAccount.setAccountNumber(accountNumber);
		rtAccount.setAgentUserName(userName);
		rtAccount.setAccountDate(dateToGet);


		session.save(rtAccount);

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
