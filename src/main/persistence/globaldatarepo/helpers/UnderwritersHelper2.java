package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnderwritersHelper2 {
	
	public static Underwriters getRandomUnderwriter() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwTitleExpression = underwriterCriteriaRoot.get("underwriterTitle");
			Predicate uwTitleLike = builder.like(uwTitleExpression, "%Underwriter%");
			underwriterCriteria.select(underwriterCriteriaRoot).where(uwTitleLike);
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Underwriters getBoiseAreaUnderwriter() throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwTitle = underwriterCriteriaRoot.get("underwriterTitle");
			Expression<String> uwAccess = underwriterCriteriaRoot.get("underwriterAccess");
			Predicate uwTitleLike = builder.like(uwTitle, "%Underwriter%");
			Predicate uwAccessLike = builder.like(uwAccess, "%Boise Area%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwTitleLike);
			predicates.add(uwAccessLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Underwriters getPocatelloAreaUnderwriter() throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwTitle = underwriterCriteriaRoot.get("underwriterTitle");
			Expression<String> uwAccess = underwriterCriteriaRoot.get("underwriterAccess");
			Predicate uwTitleLike = builder.like(uwTitle, "%Underwriter%");
			Predicate uwAccessLike = builder.like(uwAccess, "%Pocatello Area%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwTitleLike);
			predicates.add(uwAccessLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static Underwriters getNorthernIdahoAreaUnderwriter() throws Exception {
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwTitle = underwriterCriteriaRoot.get("underwriterTitle");
			Expression<String> uwAccess = underwriterCriteriaRoot.get("underwriterAccess");
			Predicate uwTitleLike = builder.like(uwTitle, "%Underwriter%");
			Predicate uwAccessLike = builder.like(uwAccess, "%Northern Idaho Area%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwTitleLike);
			predicates.add(uwAccessLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
		
	}
	
	public static Underwriters getRandomUnderwriter(String uwline) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwLineExpression = underwriterCriteriaRoot.get("underwriterLine");
			Predicate uwLineLike = builder.like(uwLineExpression, "%" + uwline + "%");
			underwriterCriteria.select(underwriterCriteriaRoot).where(uwLineLike);
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Underwriters getUnderwriterByArea(String area) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwTitle = underwriterCriteriaRoot.get("underwriterTitle");
			Expression<String> uwAccess = underwriterCriteriaRoot.get("underwriterAccess");
			Predicate uwTitleLike = builder.like(uwTitle, "%Underwriter%");
			Predicate uwAccessLike = builder.like(uwAccess, "%" + area + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwTitleLike);
			predicates.add(uwAccessLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Underwriters getUnderwriterInfoByLineAndTitle(String underWriterLine, String underWriterTitle) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwLine = underwriterCriteriaRoot.get("underwriterLine");
			Expression<String> uwTitle = underwriterCriteriaRoot.get("underwriterTitle");
			Predicate uwLineLike = builder.like(uwLine, "%" + underWriterLine + "%");
			Predicate uwTitleLike = builder.like(uwTitle, "%" + underWriterTitle + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwLineLike);
			predicates.add(uwTitleLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
						
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			Underwriters underwriter = results.get(index);
			
			session.getTransaction().commit();
			
			return underwriter;
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static Underwriters getUnderwriterInfoByFirstLastName(String underWriterFirstName, String underWriterLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> firstName = underwriterCriteriaRoot.get("underwriterFirstName");
			Expression<String> lastName = underwriterCriteriaRoot.get("underwriterLastName");
			Predicate uwUserFirstNameLike = builder.like(firstName, "%" + underWriterFirstName + "%");
			Predicate uwUserLastNameLike = builder.like(lastName, "%" + underWriterLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwUserFirstNameLike);
			predicates.add(uwUserLastNameLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
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
	
	public static Underwriters getUnderwriterInfoByFullName(String underWriterFullName) throws Exception {
		String[] userNameArray = underWriterFullName.split(" ");
		String underWriterFirstName = userNameArray[0];
		String underWriterLastName = userNameArray[(userNameArray.length-1)];
		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> firstName = underwriterCriteriaRoot.get("underwriterFirstName");
			Expression<String> lastName = underwriterCriteriaRoot.get("underwriterLastName");
			Predicate uwUserFirstNameLike = builder.like(firstName, "%" + underWriterFirstName + "%");
			Predicate uwUserLastNameLike = builder.like(lastName, "%" + underWriterLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(uwUserFirstNameLike);
			predicates.add(uwUserLastNameLike);
			underwriterCriteria.select(underwriterCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
						
			session.getTransaction().commit();
			
			return results.get(0);
			
		} catch (Exception e) {			
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static Underwriters getUnderwriterByUserName(String underwriterUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Underwriters> underwriterCriteria = builder.createQuery(Underwriters.class);
			Root<Underwriters> underwriterCriteriaRoot = underwriterCriteria.from(Underwriters.class);
			Expression<String> uwUserName = underwriterCriteriaRoot.get("underwriterUserName");
			Predicate uwUserNameLike = builder.like(uwUserName, "%" + StringsUtils.specialCharacterEscape(underwriterUserName) + "%");
			underwriterCriteria.select(underwriterCriteriaRoot).where(uwUserNameLike);
			
			List<Underwriters> results = session.createQuery(underwriterCriteria).getResultList();
			
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
	
	public static void createNewUnderwriterUser( String underwriterFirstName, String underwriterLastName, String underwriterUserName, String underwriterPassword, String underwriterLine, String underwriterTitle, String underwriterAccess) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			Underwriters newUnderwriter = new Underwriters();
			newUnderwriter.setUnderwriterFirstName(underwriterFirstName);
			newUnderwriter.setUnderwriterLastName(underwriterLastName);
			newUnderwriter.setUnderwriterUserName(underwriterUserName);
			newUnderwriter.setUnderwriterPassword(underwriterPassword);
			newUnderwriter.setUnderwriterLine(underwriterLine);
			newUnderwriter.setUnderwriterTitle(underwriterTitle);
			//newUnderwriter.setUnderwriterAccess(underwriterAccess);

			session.save(newUnderwriter);
			
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
















