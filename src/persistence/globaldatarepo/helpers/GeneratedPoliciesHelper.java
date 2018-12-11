package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GeneratedPolicies;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class GeneratedPoliciesHelper {
	
	public static List<GeneratedPolicies> getPoliciesByUserName(String agentUserName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GeneratedPolicies> generatedPoliciesCriteria = builder.createQuery(GeneratedPolicies.class);
			Root<GeneratedPolicies> generatedPoliciesCriteriaRoot = generatedPoliciesCriteria.from(GeneratedPolicies.class);
			Expression<String> agentUserNameExpression = generatedPoliciesCriteriaRoot.get("agentUserName");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape(agentUserName) + "%");
			generatedPoliciesCriteria.select(generatedPoliciesCriteriaRoot).where(agentUserNameLike);
			
			List<GeneratedPolicies> results = session.createQuery(generatedPoliciesCriteria).getResultList();
			
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
	
	public static List<GeneratedPolicies> getAllPolicies() throws Exception{
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GeneratedPolicies> generatedPoliciesCriteria = builder.createQuery(GeneratedPolicies.class);
			Root<GeneratedPolicies> generatedPoliciesCriteriaRoot = generatedPoliciesCriteria.from(GeneratedPolicies.class);
			Expression<String> agentUserNameExpression = generatedPoliciesCriteriaRoot.get("accountNumber");
			Predicate agentUserNameLike = builder.like(agentUserNameExpression, "%" + StringsUtils.specialCharacterEscape("0") + "%");
			generatedPoliciesCriteria.select(generatedPoliciesCriteriaRoot).where(agentUserNameLike);
			
			List<GeneratedPolicies> results = session.createQuery(generatedPoliciesCriteria).getResultList();
			
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
	
	public static void createNewAgentPolicyData(String agentUserName, String accountNumber, String policyType, String environment, String policyStatus) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			GeneratedPolicies agentPolRow = new GeneratedPolicies();
			agentPolRow.setAgentUserName(agentUserName);
			agentPolRow.setAccountNumber(accountNumber);
			agentPolRow.setPolicyType(policyType);
			agentPolRow.setEnvironment(environment);
			agentPolRow.setPolicyStatus(policyStatus);

			session.save(agentPolRow);
			
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
