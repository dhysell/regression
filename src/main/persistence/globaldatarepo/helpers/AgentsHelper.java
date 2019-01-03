package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Agents;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AgentsHelper {
	
	public static Agents getRandomAgent() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<Boolean> outOfSync = agentCriteriaRoot.get("outOfSync");
			Predicate inSync = builder.isFalse(outOfSync);
			agentCriteria.select(agentCriteriaRoot).where(inSync);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
            Agents agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}	
		
	public static Agents getRandomAgentInRegion(String region) throws Exception {				
		List<Agents> results = getAllAgentsInRegion(region);
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(results.size());
		
        Agents agent = results.get(index);
        
        return agent;
	}
	
	public static List<Agents> getAllAgentsInRegion(String region) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentRegion = agentCriteriaRoot.get("agentRegion");
			Predicate agentRegionLike = builder.like(agentRegion, "%" + StringsUtils.specialCharacterEscape(region) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentRegionLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
            
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
	
	public static List<Agents> getAllAgents() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			agentCriteria.select(agentCriteriaRoot);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
            
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
	
	public static Agents getAgentByName(String agentFirstName, String agentLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> firstName = agentCriteriaRoot.get("agentFirstName");
			Expression<String> lastName = agentCriteriaRoot.get("agentLastName");
			Predicate agentFirstNameLike = builder.like(firstName, "%" + agentFirstName + "%");
			Predicate agentLastNameLike = builder.like(lastName, "%" + agentLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(agentFirstNameLike);
			predicates.add(agentLastNameLike);
			agentCriteria.select(agentCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
						
			session.getTransaction().commit();
			
			if(results.isEmpty()) {
				return null;
			} else {
				return results.get(0);
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Agents getAgentByFullName(String fullName) throws Exception {
		String[] splitName = persistence.helpers.StringsUtils.getStringSplitFromFullName(fullName);
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> firstName = agentCriteriaRoot.get("agentFirstName");
			Expression<String> lastName = agentCriteriaRoot.get("agentLastName");
			Predicate agentFirstNameLike = builder.like(firstName, "%" + StringsUtils.specialCharacterEscape(splitName[0]) + "%");
			Predicate agentLastNameLike = builder.like(lastName, "%" + StringsUtils.specialCharacterEscape(splitName[splitName.length-1]) + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(agentFirstNameLike);
			predicates.add(agentLastNameLike);
			agentCriteria.select(agentCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
						
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
	
	
	public static Agents getAgentByUserName(String agentUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentUserNameExpression = agentCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentUserNameLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
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
	
	public static Agents getRandomAgentFromCounty(String county) throws Exception {
		return getAgentsBasedOnCriteria(county , "agentCounty", true);
	}

	public static Agents getRandomAgentOutSideCounty(String county) throws Exception {
		return getAgentsBasedOnCriteria(county , "agentCounty", false);
	}

	private static Agents getAgentsBasedOnCriteria(String whereValue , String column , boolean isLike) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Predicate predicate=null;
			if(isLike){
				predicate = getPredicateLike(whereValue, builder, agentCriteriaRoot, column);
			}else {
				 predicate = getPredicateNotLike(whereValue, builder, agentCriteriaRoot, column);
			}
			agentCriteria.select(agentCriteriaRoot).where(predicate);

			List<Agents> results = session.createQuery(agentCriteria).getResultList();

			session.getTransaction().commit();
			int randomNumber = (int) Math.random() * results.size();
			return results.get(randomNumber);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
			pf.close();
		}
	}

	private static Predicate getPredicateNotLike(String whereValue, CriteriaBuilder builder, Root<Agents> agentCriteriaRoot, String column) {
		Expression<String> expression = agentCriteriaRoot.get(column);
		return builder.notLike(expression, "%" + StringsUtils.specialCharacterEscape(whereValue) + "%");
	}
	private static Predicate getPredicateLike(String whereValue, CriteriaBuilder builder, Root<Agents> agentCriteriaRoot, String column) {
		Expression<String> expression = agentCriteriaRoot.get(column);
		return builder.like(expression, "%" + StringsUtils.specialCharacterEscape(whereValue) + "%");
	}

	public static void createNewAgentsUser(String agentNum, String agentFirstName, String agentMiddleName,
			String agentLastName, String agentUserName, String agentPassword, String agentRegion, String agentPA, String agentSA, String county ,String preferredName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			Agents agent = new Agents();
			agent.setAgentNum(agentNum);
			agent.setAgentFirstName(agentFirstName);
			agent.setAgentMiddleName(agentMiddleName);
			agent.setAgentLastName(agentLastName);
			agent.setAgentUserName(agentUserName);
			agent.setAgentPassword("gw");
			agent.setAgentPA(agentPA);
			agent.setAgentSA(agentSA);
			agent.setAgentRegion(agentRegion);
			agent.setAgentCounty(county);
			agent.setAgentPreferredName(preferredName);

			session.save(agent);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void incrementAgentUsedForPolicyCreation(String agentUserName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentUserNameExpression = agentCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentUserNameLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Agents selectedAgent = results.get(0);
			
			selectedAgent.setAgentUsedForPolicyCreation(selectedAgent.getAgentUsedForPolicyCreation() + 1);
			
			session.update(selectedAgent);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void setAgentUsedForPolicyCreation(String agentUserName, int valueToSet) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentUserNameExpression = agentCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentUserNameLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Agents selectedAgent = results.get(0);
			
			selectedAgent.setAgentUsedForPolicyCreation(valueToSet);
			
			session.update(selectedAgent);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void resetAgentUsedForPolicyCreationValueToZero() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
			List<Agents> AgentsToWorkThrough = getAllAgents();
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			for (Agents agent : AgentsToWorkThrough) {
				agent.setAgentUsedForPolicyCreation(0);
				session.update(agent);
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
	
	public static void setAgentPolicyTypeCreated(String agentUserName, int valueToSet) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentUserNameExpression = agentCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentUserNameLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Agents selectedAgent = results.get(0);
			
			selectedAgent.setAgentPolicyTypeCreated(valueToSet);
			
			session.update(selectedAgent);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void resetAgentPolicyTypeCreatedValueToZero() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
			List<Agents> AgentsToWorkThrough = getAllAgents();
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			for (Agents agent : AgentsToWorkThrough) {
				agent.setAgentPolicyTypeCreated(0);
				session.update(agent);
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
	
	public static void incrementAgentClaimedByVM(String agentUserName) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentUserNameExpression = agentCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentUserNameLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Agents selectedAgent = results.get(0);
			
			selectedAgent.setAgentClaimedByVM(selectedAgent.getAgentUsedForPolicyCreation() + 1);
			
			session.update(selectedAgent);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static void resetAgentClaimedByVMValueToZero() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
			List<Agents> AgentsToWorkThrough = getAllAgents();
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			for (Agents agent : AgentsToWorkThrough) {
				agent.setAgentClaimedByVM(0);
				session.update(agent);
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
	
	public static Agents getAgentWithSA() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentSA = agentCriteriaRoot.get("agentSA");
			Predicate hasSA = builder.isNotNull(agentSA);
			agentCriteria.select(agentCriteriaRoot).where(hasSA);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
            Agents agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Agents getAgentWithPA() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentPA = agentCriteriaRoot.get("agentPA");
			Predicate hasPAA = builder.isNotNull(agentPA);
			agentCriteria.select(agentCriteriaRoot).where(hasPAA);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
            Agents agent = results.get(index);
            
            session.getTransaction().commit();
            
            return agent;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static ArrayList<String> getAllAgentUsernames() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			agentCriteria.select(agentCriteriaRoot);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			ArrayList<String> names = new ArrayList<String>();
			for(Agents agent : results) {
				names.add(agent.getAgentUserName());
			}
			
			session.getTransaction().commit();
			
			return names;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void updateAgentCounty(Agents agent, String agentCounty)throws Exception{
		
		SessionFactory sessionFactory = null;
		Session session = null;
		Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();

			hibernateTransaction = session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentNumber = agentCriteriaRoot.get("agentNum");
			Predicate agentNumberLike = builder.like(agentNumber, "%" + StringsUtils.specialCharacterEscape(agent.getAgentNum()) + "%");
			agentCriteria.select(agentCriteriaRoot).where(agentNumberLike);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
			Agents agentResults = results.get(0);
			agentResults.setAgentCounty(agentCounty);
		
			session.update(agentResults);
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			if (hibernateTransaction!=null) hibernateTransaction.rollback();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	public static Agents getAgentByAgentNumber(String producercode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Agents> agentCriteria = builder.createQuery(Agents.class);
			Root<Agents> agentCriteriaRoot = agentCriteria.from(Agents.class);
			Expression<String> agentNumber = agentCriteriaRoot.get("agentNum");
			Predicate agentNumberEquals = builder.equal(agentNumber, producercode);
			agentCriteria.select(agentCriteriaRoot).where(agentNumberEquals);
			
			List<Agents> results = session.createQuery(agentCriteria).getResultList();
			
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
