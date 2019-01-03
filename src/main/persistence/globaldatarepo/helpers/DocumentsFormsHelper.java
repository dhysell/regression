package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GwDocumentEvents;
import persistence.globaldatarepo.entities.GwDocumentsDocsEventsXref;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentsFormsHelper {

	public static ArrayList<String> getListOfDocumentNamesForListOfEventNames(ArrayList<String> listOfEventNamesInDatabase) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder1 = session.getCriteriaBuilder();
			CriteriaQuery<GwDocumentEvents> documentEventsCriteria = builder1.createQuery(GwDocumentEvents.class);
			Root<GwDocumentEvents> documentEventsCriteriaRoot = documentEventsCriteria.from(GwDocumentEvents.class);
			Expression<String> namesInExpression = documentEventsCriteriaRoot.get("eventName");
			Predicate namesIn = namesInExpression.in(listOfEventNamesInDatabase);
			documentEventsCriteria.select(documentEventsCriteriaRoot).where(namesIn);
			
			List<GwDocumentEvents> results1 = session.createQuery(documentEventsCriteria).getResultList();
			
			CriteriaBuilder builder2 = session.getCriteriaBuilder();
			CriteriaQuery<GwDocumentsDocsEventsXref> documentEventsXRefCriteria = builder2.createQuery(GwDocumentsDocsEventsXref.class);
			Root<GwDocumentsDocsEventsXref> documentEventsXRefCriteriaRoot = documentEventsXRefCriteria.from(GwDocumentsDocsEventsXref.class);
			Expression<String> idsInExpression = documentEventsXRefCriteriaRoot.get("gwDocumentsEvents");
			Predicate idsIn = idsInExpression.in(results1);
			documentEventsXRefCriteria.select(documentEventsXRefCriteriaRoot).where(idsIn);
			
			List<GwDocumentsDocsEventsXref> results2 = session.createQuery(documentEventsXRefCriteria).getResultList();
						
			session.getTransaction().commit();
			
			ArrayList<String> toReturn = new ArrayList<String>();
			for(GwDocumentsDocsEventsXref ddexref : results2) {
				toReturn.add(ddexref.getGwDocumentsDocs().getDocName());
			}
			
			return toReturn;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
}
