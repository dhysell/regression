package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.RegionCounty;

import javax.persistence.criteria.*;
import java.util.List;

public class RegionCountyHelper {

	public static RegionCounty getCountyInfo(String county) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<RegionCounty> countyCriteria = builder.createQuery(RegionCounty.class);
			Root<RegionCounty> countyCriteriaRoot = countyCriteria.from(RegionCounty.class);
			Expression<String> countyNameExpression = countyCriteriaRoot.get("countyName");
			Predicate countyNameLike = builder.like(countyNameExpression, "%" + county + "%");
			countyCriteria.select(countyCriteriaRoot).where(countyNameLike);
			
			List<RegionCounty> results = session.createQuery(countyCriteria).getResultList();
			
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
