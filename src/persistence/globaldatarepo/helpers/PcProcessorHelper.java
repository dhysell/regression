package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.PcProcessor;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PcProcessorHelper {
	
	public static PcProcessor getRandomProcessor() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PcProcessor> processorCriteria = builder.createQuery(PcProcessor.class);
			Root<PcProcessor> processorCriteriaRoot = processorCriteria.from(PcProcessor.class);
			processorCriteria.select(processorCriteriaRoot);
			
			List<PcProcessor> results = session.createQuery(processorCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			PcProcessor processorUser = results.get(index);

			session.getTransaction().commit();

			return processorUser;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}

	}
	
	public static PcProcessor getProcessorByUserName(String userName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PcProcessor> processorCriteria = builder.createQuery(PcProcessor.class);
			Root<PcProcessor> processorCriteriaRoot = processorCriteria.from(PcProcessor.class);
			Expression<String> processorUserName = processorCriteriaRoot.get("processorUserName");
			Predicate processorUserNameEquals = builder.equal(processorUserName, userName);
			processorCriteria.select(processorCriteriaRoot).where(processorUserNameEquals);
			
			List<PcProcessor> results = session.createQuery(processorCriteria).getResultList();
			
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
	
	public static PcProcessor getProcessorByFirstLastName(String userFirstName, String userLastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<PcProcessor> processorCriteria = builder.createQuery(PcProcessor.class);
			Root<PcProcessor> processorCriteriaRoot = processorCriteria.from(PcProcessor.class);
			Expression<String> firstName = processorCriteriaRoot.get("processorFirstName");
			Expression<String> lastName = processorCriteriaRoot.get("processorLastName");
			Predicate processorUserFirstNameLike = builder.like(firstName, "%" + userFirstName + "%");
			Predicate processorUserLastNameLike = builder.like(lastName, "%" + userLastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(processorUserFirstNameLike);
			predicates.add(processorUserLastNameLike);
			processorCriteria.select(processorCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<PcProcessor> results = session.createQuery(processorCriteria).getResultList();
			
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
	
	public static PcProcessor getUserInfoByFullName(String userFullName) throws Exception {
		String[] userNameArray = userFullName.split(" ");
		String underWriterFirstName = userNameArray[0];
		String underWriterLastName = userNameArray[(userNameArray.length-1)];
		return getProcessorByFirstLastName(underWriterFirstName, underWriterLastName);		
	}
	
	public static void createNewProcessor(String userName, String firstName, String lastName, String password) throws Exception{
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			PcProcessor processor = new PcProcessor();
			processor.setProcessorUserName(userName);
			processor.setProcessorFirstName(firstName);
			processor.setProcessorLastName(lastName);
			processor.setProcessorPassword(password);
			
			session.save(processor);
			
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
