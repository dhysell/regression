package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.TransitionRenewalTeam;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransitionRenewalTeamHelper {

	public static TransitionRenewalTeam getRandomTRMember() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitionRenewalTeam> trCriteria = builder.createQuery(TransitionRenewalTeam.class);
			Root<TransitionRenewalTeam> trCriteriaRoot = trCriteria.from(TransitionRenewalTeam.class);
			Expression<Boolean> trManager = trCriteriaRoot.get("trmanager");
			Predicate trManagerIsFalse = builder.isFalse(trManager);
			trCriteria.select(trCriteriaRoot).where(trManagerIsFalse);
			
			List<TransitionRenewalTeam> results = session.createQuery(trCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			TransitionRenewalTeam trMember = results.get(index);
            
            session.getTransaction().commit();
            
            return trMember;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static TransitionRenewalTeam getRandomTRMemberWithLoadFactorGreaterThanZero() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitionRenewalTeam> trCriteria = builder.createQuery(TransitionRenewalTeam.class);
			Root<TransitionRenewalTeam> trCriteriaRoot = trCriteria.from(TransitionRenewalTeam.class);
			Expression<Integer> loadFactor = trCriteriaRoot.get("trloadFactor");
			Predicate loadFactorGreaterThanZero = builder.greaterThan(loadFactor, 0);
			trCriteria.select(trCriteriaRoot).where(loadFactorGreaterThanZero);
			
			List<TransitionRenewalTeam> results = session.createQuery(trCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			TransitionRenewalTeam trMember = results.get(index);
            
            session.getTransaction().commit();
            
            return trMember;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static List<TransitionRenewalTeam> getTRMembersWithLoadFactorGreaterThanZero() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitionRenewalTeam> trCriteria = builder.createQuery(TransitionRenewalTeam.class);
			Root<TransitionRenewalTeam> trCriteriaRoot = trCriteria.from(TransitionRenewalTeam.class);
			Expression<Integer> loadFactor = trCriteriaRoot.get("trloadFactor");
			Predicate loadFactorGreaterThanZero = builder.greaterThan(loadFactor, 0);
			trCriteria.select(trCriteriaRoot).where(loadFactorGreaterThanZero);
			
			List<TransitionRenewalTeam> results = session.createQuery(trCriteria).getResultList();
			            
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
	
	public static TransitionRenewalTeam getTRInfoByFirstLastName(String trFirstName, String trLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TransitionRenewalTeam> trUserCriteria = builder.createQuery(TransitionRenewalTeam.class);
			Root<TransitionRenewalTeam> trUserCriteriaRoot = trUserCriteria.from(TransitionRenewalTeam.class);
			Expression<String> firstName = trUserCriteriaRoot.get("trfirstName");
			Expression<String> lastName = trUserCriteriaRoot.get("trlastName");
			Predicate trUserFirstNameLike = builder.like(firstName, "%" + trFirstName + "%");
			Predicate trUserLastNameLike = builder.like(lastName, "%" + trLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(trUserFirstNameLike);
			predicates.add(trUserLastNameLike);
			trUserCriteria.select(trUserCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<TransitionRenewalTeam> results = session.createQuery(trUserCriteria).getResultList();
			
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
	
	public static void createNewTR(String trfirstName, String trlastName, String truserName, String trpassword, boolean trmember, boolean trmanager, int trloadFactor) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			TransitionRenewalTeam tr = new TransitionRenewalTeam();
			tr.setTrfirstName(trfirstName);
			tr.setTrlastName(trlastName);
			tr.setTruserName(truserName);
			tr.setTrpassword(trpassword);
			tr.setTrmember(trmember);
			tr.setTrmanager(trmanager);
			tr.setTrloadFactor(trloadFactor);

			session.save(tr);
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
