package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.LHTable;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LHTableHelper {
	
	public LHTable getRandomPerson() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		List<LHTable> results = new ArrayList<LHTable>();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LHTable> zipCriteria = builder.createQuery(LHTable.class);
			Root<LHTable> zipCriteriaRoot = zipCriteria.from(LHTable.class);
			Expression<String> zipcode = zipCriteriaRoot.get("personCompany");
			Expression<String> lhNumber = zipCriteriaRoot.get("lhnumber");
			Predicate zipcodeLike = builder.like(zipcode, "person");
			Predicate lhNumberStarts = builder.like(lhNumber, "98%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(lhNumberStarts);
			predicates.add(zipcodeLike);
			zipCriteria.select(zipCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

			results = session.createQuery(zipCriteria).setMaxResults(1).getResultList();

			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LHTable address = results.get(index);

			session.getTransaction().commit();

			return address;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (!results.isEmpty()) {
				session.clear();
			}
			pf.close();
		}
	}

	public LHTable getRandomCompany() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
		PersistenceFactory pf = new PersistenceFactory();
		List<LHTable> results = new ArrayList<LHTable>();
		try {
			sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LHTable> personCompanyCriteria = builder.createQuery(LHTable.class);
			Root<LHTable> personCompanyCriteriaRoot = personCompanyCriteria.from(LHTable.class);
			Expression<String> lhNumber = personCompanyCriteriaRoot.get("lhnumber");
			Expression<String> personCompany = personCompanyCriteriaRoot.get("personCompany");
			Predicate lhNumberStarts = builder.like(lhNumber, "98%");
			Predicate personCompanyLike = builder.like(personCompany, "company");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(lhNumberStarts);
			predicates.add(personCompanyLike);
			personCompanyCriteria.select(personCompanyCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

			results = session.createQuery(personCompanyCriteria).getResultList();

			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LHTable address = results.get(index);

			session.getTransaction().commit();

			return address;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (!results.isEmpty()) {
				session.clear();
			}
			pf.close();
		}
	}
	
	public List<String> getListOfLienholderNumbers() throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        List<String> results = new ArrayList<String>();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LHTable> lhTableCriteria = builder.createQuery(LHTable.class);
			Root<LHTable> lhTableCriteriaRoot = lhTableCriteria.from(LHTable.class);
			Expression<String> lhNumber = lhTableCriteriaRoot.get("lhnumber");
			Predicate lhNumberStarts = builder.like(lhNumber, "98%");
			lhTableCriteria.select(lhTableCriteriaRoot).where(lhNumberStarts);

			for (LHTable queryResult : session.createQuery(lhTableCriteria).getResultList()) {
				results.add(queryResult.getLhnumber());
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
	
	public void removeTerminatedLienholders(List<String> listOfLienholdersToRemove) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            for (String lhToRemove : listOfLienholdersToRemove) {
	            CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<LHTable> lhTableCriteria = builder.createQuery(LHTable.class);
				Root<LHTable> lhTableCriteriaRoot = lhTableCriteria.from(LHTable.class);
				Expression<String> lhNumber = lhTableCriteriaRoot.get("lhnumber");
				Predicate lhNumberEquals = builder.equal(lhNumber, lhToRemove);
				lhTableCriteria.select(lhTableCriteriaRoot).where(lhNumberEquals);
				
				
				for (LHTable queryResult : session.createQuery(lhTableCriteria).getResultList()) {
					session.delete(queryResult);
				}
            }
            hibernateTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }
}
