package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Spoilage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Random;

public class SpoilageHelper {
	
	public static Spoilage getRandomSpoilage() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Spoilage> spoilageCriteria = builder.createQuery(Spoilage.class);
			Root<Spoilage> spoilageCriteriaRoot = spoilageCriteria.from(Spoilage.class);
			spoilageCriteria.select(spoilageCriteriaRoot);
			
			List<Spoilage> results = session.createQuery(spoilageCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			Spoilage spoilage = results.get(index);
            
            session.getTransaction().commit();
            
            return spoilage;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}	
	
}
