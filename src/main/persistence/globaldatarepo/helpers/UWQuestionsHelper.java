package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GLUnderwriterQuestions;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UWQuestionsHelper {
	
	public static List<GLUnderwriterQuestions> getUWQuestionsClassCode(String classCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("classCode");
			Expression<String> questionExpression2 = uwQuestionCriteriaRoot.get("parentQuestionCode");
			Predicate classCodeLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(classCode) + "%");
			Predicate notchildquestion = builder.like(questionExpression2, "-");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(classCodeLike);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(classCodeLike);
			predicates.add(notchildquestion);
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
						
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
	
	public static GLUnderwriterQuestions getUWQuestionsClassCodeName(String classCodeName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("classCodeName");
			Predicate classCodeNameLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(classCodeName) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(classCodeNameLike);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
						
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
	
	public static GLUnderwriterQuestions getUWQuestionsQuestionLabel(String label) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionLabel");
			Predicate questionLabelLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(label) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(questionLabelLike);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
						
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
	
	public static GLUnderwriterQuestions getUWQuestionsQuestionParentCode(String parentCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionCode");
			Predicate parentQuestionCodeEquals = builder.equal(questionExpression, StringsUtils.specialCharacterEscape(parentCode));
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(parentQuestionCodeEquals);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
						
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
	
	
	public static GLUnderwriterQuestions getUWQuestionByQuestionCode(String questionCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("questionCode");
			Predicate questionCodeLEquals = builder.equal(questionExpression, StringsUtils.specialCharacterEscape(questionCode));
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(questionCodeLEquals);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	
	public static List<GLUnderwriterQuestions> getUWQuestionsByDependantQuestion(String dependantQuestion) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> questionExpression = uwQuestionCriteriaRoot.get("dependentOnQuestion");
			Predicate dependantQuestionLike = builder.like(questionExpression, "%" + StringsUtils.specialCharacterEscape(dependantQuestion) + "%");
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(dependantQuestionLike);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();
			
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
	
	
	public static GLUnderwriterQuestions getRandomClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> classCodeCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> classCodeCriteriaRoot = classCodeCriteria.from(GLUnderwriterQuestions.class);
			classCodeCriteria.select(classCodeCriteriaRoot);
			
			List<GLUnderwriterQuestions> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			GLUnderwriterQuestions classCode = results.get(index);
			
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
	
	public static List<GLUnderwriterQuestions> getChildrenQuestions(String questionCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLUnderwriterQuestions> uwQuestionCriteria = builder.createQuery(GLUnderwriterQuestions.class);
			Root<GLUnderwriterQuestions> uwQuestionCriteriaRoot = uwQuestionCriteria.from(GLUnderwriterQuestions.class);
			Expression<String> blockingTypeExpression = uwQuestionCriteriaRoot.get("parentQuestionCode");
			Predicate blockingTypeEquals = builder.equal(blockingTypeExpression, questionCode);
			uwQuestionCriteria.select(uwQuestionCriteriaRoot).where(blockingTypeEquals);
			
			List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionCriteria).getResultList();		
			
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
	
	
	

}




















