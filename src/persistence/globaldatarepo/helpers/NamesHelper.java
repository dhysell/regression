
package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Names;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Random;

public class NamesHelper {
	

	public static Names getRandomName() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Names> wordCriteria = builder.createQuery(Names.class);
			Root<Names> wordCriteriaRoot = wordCriteria.from(Names.class);
			wordCriteria.select(wordCriteriaRoot);
			
			List<Names> results = session.createQuery(wordCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			String firstName = results.get(index).getWord();
			randomGenerator = new Random();
			index = randomGenerator.nextInt(results.size());
			String lastName = results.get(index).getWord();
			
			Names name = new Names();
			name.setFirstName(firstName.trim());
			name.setLastName(lastName.trim());
			name.setCompanyName(firstName.trim() + " " + lastName.trim());
			name.setWord(firstName.trim());
			
			
			session.getTransaction().commit();
			
			return name;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}