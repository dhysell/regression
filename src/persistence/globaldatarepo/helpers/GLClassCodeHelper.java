package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GLClassCodeHelper {
	
	public static GLClassCodes getSpecifiedClassCode(boolean contractor, boolean uwQuestions, boolean restricted, boolean premiumBasePlus) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
//		contractor = true; 
//		uwQuestions = true;
//		restricted = true;
//		premiumBasePlus = true;
		
		
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> classCodeExpression = classCodeCriteriaRoot.get("code");
			Expression<Boolean> hasQuestions = classCodeCriteriaRoot.get("hasQuestions");
			Expression<String> premiumBaseExpression = classCodeCriteriaRoot.get("premBase");
			Expression<String> restrictedClassCodesExpression = classCodeCriteriaRoot.get("restricted");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			if(!contractor){
				Predicate classCodeNotLike = builder.notLike(classCodeExpression, "9%");
				predicates.add(classCodeNotLike);
			} else {
				Predicate classCodeLike = builder.like(classCodeExpression, "9%");
				predicates.add(classCodeLike);
			}
			
			if(!uwQuestions){
				Predicate doesNotHaveUWQuestions = builder.isFalse(hasQuestions);
				predicates.add(doesNotHaveUWQuestions);
			} else {
				Predicate hasUWQuestions = builder.isTrue(hasQuestions);
				predicates.add(hasUWQuestions);
			}
			
			if(!restricted){
				Predicate restrictedClassCodesNotLike = builder.notLike(restrictedClassCodesExpression, "_");
				predicates.add(restrictedClassCodesNotLike);
			} else {
				Predicate restrictedClassCodesLike = builder.like(restrictedClassCodesExpression, "_");
				predicates.add(restrictedClassCodesLike);
			}
			
			if(!premiumBasePlus){
				Predicate premiumBaseNotLike = builder.notLike(premiumBaseExpression, "%+%");
				predicates.add(premiumBaseNotLike);
			} else {
				Predicate premiumBaseLike = builder.like(premiumBaseExpression, "%+%");
				predicates.add(premiumBaseLike);
			}
			classCodeCriteria.select(classCodeCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			classCodeCriteria.orderBy(builder.asc(classCodeCriteriaRoot.get("useCounter")));
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomGLClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			classCodeCriteria.select(classCodeCriteriaRoot);
			classCodeCriteria.orderBy(builder.desc(classCodeCriteriaRoot.get("useCounter")));
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size()/2);
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getGLClassCodeByCode(String code) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeLike = builder.like(codeExpression, "%" + StringsUtils.specialCharacterEscape(code) + "%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static List<GLClassCodes> getGLContractorClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeLike = builder.like(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			            
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
	
	
	public static List<GLClassCodes> getGLContractorClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeLike = builder.like(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
            session.getTransaction().commit();
            
            return results.subList(0, listSize);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<GLClassCodes> getGLNon_ContractorClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeNotLike = builder.notLike(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeNotLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
            
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
	
	
	public static List<GLClassCodes> getGLNon_ContractorClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeNotLike = builder.notLike(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeNotLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
            session.getTransaction().commit();
            
            return results.subList(0, listSize);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static GLClassCodes getRandomGLContractorClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeLike = builder.like(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomGLNon_ContractorClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate codeNotLike = builder.notLike(codeExpression, "9%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeNotLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomGLNon_ContractorClassCode_Without_UWQuestions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<Boolean> hasQuestions = classCodeCriteriaRoot.get("hasQuestions");
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate hasQuestionsIsFalse = builder.isFalse(hasQuestions);
			Predicate codeNotLike = builder.notLike(codeExpression, "9%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(hasQuestionsIsFalse);
			predicates.add(codeNotLike);
			classCodeCriteria.select(classCodeCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomGL_ContractorClassCode_Without_UWQuestions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<Boolean> hasQuestions = classCodeCriteriaRoot.get("hasQuestions");
			Expression<String> codeExpression = classCodeCriteriaRoot.get("code");
			Predicate hasQuestionsIsFalse = builder.isFalse(hasQuestions);
			Predicate codeLike = builder.like(codeExpression, "9%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(hasQuestionsIsFalse);
			predicates.add(codeLike);
			classCodeCriteria.select(classCodeCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static List<GLClassCodes> getGLRestrictedClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("restricted");
			Predicate codeLike = builder.like(codeExpression, "R");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			            
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
	
	
	public static List<GLClassCodes> getGLRestrictedClassCodes(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("restricted");
			Predicate codeLike = builder.like(codeExpression, "R");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
            session.getTransaction().commit();
            
            return results.subList(0, listSize);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static GLClassCodes getRandomGLRestrictedClassCode(boolean yesno) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> classCodeExpression = classCodeCriteriaRoot.get("restricted");
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(yesno){
				Predicate classCodeLike = builder.like(classCodeExpression, "R");
				predicates.add(classCodeLike);
			} else {
				Predicate classCodeNotLike = builder.notLike(classCodeExpression, "R");
				predicates.add(classCodeNotLike);
			}
			classCodeCriteria.select(classCodeCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	
	
	public static List<GLClassCodes> getGLPremiumBaseClassCodes(String premiumBase) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("premBase");
			Predicate codeLike = builder.like(codeExpression, premiumBase);
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
            
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
	
	
	public static GLClassCodes getRandomGLPremiumBaseClassCode(String premiumBase) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("premBase");
			Predicate codeLike = builder.like(codeExpression, premiumBase);
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	
	public static GLClassCodes getRandomPlusGLPremiumBaseClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("premBase");
			Predicate codeLike = builder.like(codeExpression, "%+%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomNon_PlusGLPremiumBaseClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<String> codeExpression = classCodeCriteriaRoot.get("premBase");
			Predicate codeNotLike = builder.notLike(codeExpression, "%+%");
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeNotLike);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static GLClassCodes getRandomGLClassCodeWithoutUWQuestions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<Boolean> codeExpression = classCodeCriteriaRoot.get("hasQuestions");
			Predicate codeIsFalse = builder.isFalse(codeExpression);
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeIsFalse);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	public static boolean getGLContractorWithNoUWQuestions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<Boolean> codeExpression = classCodeCriteriaRoot.get("noContractorQuestions");
			Predicate codeIsTrue = builder.isTrue(codeExpression);
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeIsTrue);
			
			boolean results = session.createQuery(classCodeCriteria).getResultList() != null;
            
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
	
	public static GLClassCodes getRandomGLClassCode_With_UWQuestions() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			Expression<Boolean> codeExpression = classCodeCriteriaRoot.get("hasQuestions");
			Predicate codeIsTrue = builder.isTrue(codeExpression);
			classCodeCriteria.select(classCodeCriteriaRoot).where(codeIsTrue);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			GLClassCodes classCode = results.get(index);
			//increment class code useage
			classCode.setUseCounter(classCode.getUseCounter() + 1);
			session.update(classCode);
            
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
	
	
	public static List<GLClassCodes> getGLClassCodeList() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			classCodeCriteria.select(classCodeCriteriaRoot);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
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
	
	
	public static List<GLClassCodes> getGLClassCodeList(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
			Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
			classCodeCriteria.select(classCodeCriteriaRoot);
			
			List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
			session.getTransaction().commit();
			
			return results.subList(0, listSize);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
//	public static void updateGLClassCode(int id, String basisType) throws Exception {
//		SessionFactory sessionFactory = null;
//		Session session = null;
//		try {
//			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);	
//			session = sessionFactory.openSession();
//			
//			session.beginTransaction();
//			
//			Criteria classID = session.createCriteria(GlclassCodeBak2.class);
//			Criterion idCriteria = Restrictions.eq("id", id);
//			classID.add(idCriteria);
//			
//			@SuppressWarnings("unchecked")
//			List<GlclassCodeBak2> results = classID.list();
//			
//			GlclassCodeBak2 classCode = results.get(0);
//			classCode.set(basisType);
//
//			session.update(classCode);
//			
//			session.getTransaction().commit();
//		} catch (Exception e) {			
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		} finally {
//			session.clear();
//			pf.close();
//		}
//	}
	
	
	
	
}




















