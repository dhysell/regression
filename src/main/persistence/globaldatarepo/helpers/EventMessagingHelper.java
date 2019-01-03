package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.EventMessaging;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

public class EventMessagingHelper {

	public static void createNewEventMessagingData(Date dateTimeToCheck, String server, String center, String destinationName, String destinationStatus, int destinationFailedNum, int destinationRetryErrNum, int destinationInFlightNum, int destinationUnsentNum, int destinationBatchedNum, int destinationAwaitingRetryNum) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			EventMessaging eMessRow = new EventMessaging();
			eMessRow.setDateTimeOfCheck(dateTimeToCheck);
			eMessRow.setServer(server);
			eMessRow.setCenter(center);
			eMessRow.setDestinationName(destinationName);
			eMessRow.setDestinationStatus(destinationStatus);
			eMessRow.setDestinationFailedNum(destinationFailedNum);
			eMessRow.setDestinationRetryErrNum(destinationRetryErrNum);
			eMessRow.setDestinationInFlightNum(destinationInFlightNum);
			eMessRow.setDestinationUnsentNum(destinationUnsentNum);
			eMessRow.setDestinationBatchedNum(destinationBatchedNum);
			eMessRow.setDestinationAwaitingRetryNum(destinationAwaitingRetryNum);		

			session.save(eMessRow);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<EventMessaging> getEventMessagingByDateTimeStamp(Date timeToGet) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<EventMessaging> eventMessagingCriteria = builder.createQuery(EventMessaging.class);
			Root<EventMessaging> eventMessagingCriteriaRoot = eventMessagingCriteria.from(EventMessaging.class);
			Expression<Date> dateTimeOfCheck = eventMessagingCriteriaRoot.get("dateTimeOfCheck");
			Predicate dateTimeOfCheckEquals = builder.equal(dateTimeOfCheck, timeToGet);
			eventMessagingCriteria.select(eventMessagingCriteriaRoot).where(dateTimeOfCheckEquals);
			
			List<EventMessaging> eMessObjs = session.createQuery(eventMessagingCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return eMessObjs;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
