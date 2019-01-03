package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CPUWQuestions;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CPUWQuestionsHelper {
	
	public static List<CPUWQuestions> getUWQuestionsClassCode(String classCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("classCode");
			Predicate classCodeLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(classCode) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(classCodeLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	public static CPUWQuestions getUWQuestionsClassCodeName(String classCodeName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("classCodeName");
			Predicate classCodeNameLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(classCodeName) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(classCodeNameLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	public static CPUWQuestions getUWQuestionsQuestionLabel(String label) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionLabel");
			Predicate questionLabelLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(label) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(questionLabelLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	public static CPUWQuestions getUWQuestionsQuestionParentCode(String parentCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionCode");
			Predicate parentQuestionCodeLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(parentCode) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(parentQuestionCodeLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	
	public static CPUWQuestions getUWQuestionByQuestionCode(String questionCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionCode");
			Predicate questionCodeLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(questionCode) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(questionCodeLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
			session.getTransaction().commit();
			
			return results.get(0);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			session.close();
		}
	}
	
	
	public static List<CPUWQuestions> getUWQuestionsByDependantQuestion(String dependantQuestion) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("dependentOnQuestion");
			Predicate dependantQuestionLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(dependantQuestion) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(dependantQuestionLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	
	public static CPUWQuestions getRandomClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {

            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> classCodeCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> classCodeCriteriaRoot = classCodeCriteria.from(CPUWQuestions.class);
			classCodeCriteria.select(classCodeCriteriaRoot);
			
			List<CPUWQuestions> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			CPUWQuestions classCode = results.get(index);
			
			session.getTransaction().commit();
			
			return classCode;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<CPUWQuestions> getAllQuestionsNonBlockUser() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> question = uwQuestionCriteriaRoot.get("failureMessage");
			Expression<String> question2 = uwQuestionCriteriaRoot.get("blockingAction");
			Predicate questionNotLike = builder.notLike(question, "-");
			Predicate question2Like = builder.like(question2, "-");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(questionNotLike);
			predicates.add(question2Like);
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	public static List<CPUWQuestions> getAllchildQuestions(String parentQuestionCode) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CPUWQuestions> uwQuestionCriteria = builder.createQuery(CPUWQuestions.class);
			Root<CPUWQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(CPUWQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("parentQuestionCode");
			Predicate parentQuestionCodeLike = builder.like(questionExpression, parentQuestionCode);
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(parentQuestionCodeLike);
			
			List<CPUWQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
						
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
	
	public static void createNewCPClassCode(List<CPUWQuestions> classCodeList) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			
			session.createQuery("delete from CPClassCodes").executeUpdate();
			
			for(CPUWQuestions uwQuestion : classCodeList) {
				session.save(uwQuestion);
			}
			
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




















