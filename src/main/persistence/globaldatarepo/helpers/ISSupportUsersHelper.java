package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.ISSupportUsers;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Random;

public class ISSupportUsersHelper {

	
	
	public static ISSupportUsers getRandomUser() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ISSupportUsers> uwIssueCriteria = builder.createQuery(ISSupportUsers.class);
			Root<ISSupportUsers> uwIssueCriteriaRoot = uwIssueCriteria.from(ISSupportUsers.class);
			uwIssueCriteria.select(uwIssueCriteriaRoot);
			
			List<ISSupportUsers> results = session.createQuery(uwIssueCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			ISSupportUsers uwIssue = results.get(index);
            
            session.getTransaction().commit();
            
            return uwIssue;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
