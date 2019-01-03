package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CityCounty;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class CityCountyHelper {
	
	public static CityCounty getCounty(String city) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CityCounty> cityCriteria = builder.createQuery(CityCounty.class);
			Root<CityCounty> cityCriteriaRoot = cityCriteria.from(CityCounty.class);
			Expression<String> cityName = cityCriteriaRoot.get("city");
			Predicate cityNameLike = builder.like(cityName, "%" + StringsUtils.specialCharacterEscape(city) + "%");
			cityCriteria.select(cityCriteriaRoot).where(cityNameLike);
			
			List<CityCounty> results = session.createQuery(cityCriteria).getResultList();
			
			CityCounty county = results.get(0);
            
            session.getTransaction().commit();
            
            return county;
		} catch (Exception e) {	
			System.out.println("####################");
			System.out.println("Could not find City: " + city + " in the CityCounty table.");
			System.out.println("####################");
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}
