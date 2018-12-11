package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.TownshipRange;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;


public class TownshipRangeHelper {
	
	public static List<TownshipRange> getAllTownshipRanges() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TownshipRange> townshipCriteria = builder.createQuery(TownshipRange.class);
			Root<TownshipRange> townshipCriteriaRoot = townshipCriteria.from(TownshipRange.class);
			townshipCriteria.select(townshipCriteriaRoot);
			
			List<TownshipRange> results = session.createQuery(townshipCriteria).getResultList();
			
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
	
	public static List<TownshipRange> getTownshipRangeForCounty(String county) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TownshipRange> countyCriteria = builder.createQuery(TownshipRange.class);
			Root<TownshipRange> countyCriteriaRoot = countyCriteria.from(TownshipRange.class);
			Expression<String> countyNameExpression = countyCriteriaRoot.get("county");
			Predicate countyNameLike = builder.like(countyNameExpression, "%" + StringsUtils.specialCharacterEscape(county) + "%");
			countyCriteria.select(countyCriteriaRoot).where(countyNameLike);
			
			List<TownshipRange> results = session.createQuery(countyCriteria).getResultList();
			
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
	
	public static TownshipRange getRandomTownshipRangeForCounty(String county) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TownshipRange> countyCriteria = builder.createQuery(TownshipRange.class);
			Root<TownshipRange> countyCriteriaRoot = countyCriteria.from(TownshipRange.class);
			Expression<String> countyNameExpression = countyCriteriaRoot.get("county");
			Predicate countyNameLike = builder.equal(countyNameExpression, StringsUtils.specialCharacterEscape(county));
			countyCriteria.select(countyCriteriaRoot).where(countyNameLike);
			
			List<TownshipRange> results = session.createQuery(countyCriteria).getResultList();
			
			session.getTransaction().commit();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			return results.get(index);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static void createTownshipRange(String county, String township, String townshipDirection, String range, String rangeDirection) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			TownshipRange townshipRange = new TownshipRange();
			townshipRange.setCounty(county);
			townshipRange.setTownship(township);
			townshipRange.setTownshipDirection(townshipDirection);
			townshipRange.setRange(range);
			townshipRange.setRangeDirection(rangeDirection);

			session.save(townshipRange);
			
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
