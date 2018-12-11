package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.IMUWQuestions;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class IMUWQuestionsHelper {
	
	public static List<IMUWQuestions> getUWQuestionsCoveragePart(String coveragePart) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IMUWQuestions> imuwQuestionCriteria = builder.createQuery(IMUWQuestions.class);
			Root<IMUWQuestions> imuwQuestionCriteriaRoot = imuwQuestionCriteria.from(IMUWQuestions.class);
			Expression<String> coverageQuestion = imuwQuestionCriteriaRoot.get("locationOfQuestion");
			Predicate coverageQuestionLike = builder.like(coverageQuestion, coveragePart);
			imuwQuestionCriteria.select(imuwQuestionCriteriaRoot).where(coverageQuestionLike);
			
			List<IMUWQuestions> results = session.createQuery(imuwQuestionCriteria).getResultList();
						
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
	
	public static List<IMUWQuestions> getUWQuestionsCoverageParts(List<String> coverageParts) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IMUWQuestions> imuwQuestionCriteria = builder.createQuery(IMUWQuestions.class);
			Root<IMUWQuestions> imuwQuestionCriteriaRoot = imuwQuestionCriteria.from(IMUWQuestions.class);
			Expression<String> coverageQuestion = imuwQuestionCriteriaRoot.get("locationOfQuestion");
			
			List<IMUWQuestions> results = new ArrayList<>();
			
			for (String coveragePart : coverageParts) {
				Predicate coverageQuestionLike = builder.like(coverageQuestion, coveragePart);
				imuwQuestionCriteria.select(imuwQuestionCriteriaRoot).where(coverageQuestionLike);
				results.addAll(session.createQuery(imuwQuestionCriteria).getResultList());
			}

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
	
	public static IMUWQuestions getUWQuestionWithQuesitonCode(String questionCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<IMUWQuestions> imuwQuestionCriteria = builder.createQuery(IMUWQuestions.class);
			Root<IMUWQuestions> imuwQuestionCriteriaRoot = imuwQuestionCriteria.from(IMUWQuestions.class);
			Expression<String> coverageQuestionCode = imuwQuestionCriteriaRoot.get("questionCode");
			Predicate coverageQuestionCodeLike = builder.like(coverageQuestionCode, questionCode);
			imuwQuestionCriteria.select(imuwQuestionCriteriaRoot).where(coverageQuestionCodeLike);
			
			List<IMUWQuestions> results = session.createQuery(imuwQuestionCriteria).getResultList();
						
			session.getTransaction().commit();
			
			return results.get(0);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}

}
