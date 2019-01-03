package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.TeritoryCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeritoryCodesHelper {
	
	public static List<TeritoryCodes> getTeritoryCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TeritoryCodes> territoryCodeCriteria = builder.createQuery(TeritoryCodes.class);
			Root<TeritoryCodes> territoryCodeCriteriaRoot = territoryCodeCriteria.from(TeritoryCodes.class);
			territoryCodeCriteria.select(territoryCodeCriteriaRoot);
			
			List<TeritoryCodes> results = session.createQuery(territoryCodeCriteria).getResultList();
			
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
	
	public static TeritoryCodes getRandomAddress() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TeritoryCodes> territoryCodeCriteria = builder.createQuery(TeritoryCodes.class);
			Root<TeritoryCodes> territoryCodeCriteriaRoot = territoryCodeCriteria.from(TeritoryCodes.class);
			territoryCodeCriteria.select(territoryCodeCriteriaRoot);
			
			List<TeritoryCodes> results = session.createQuery(territoryCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			TeritoryCodes address = results.get(index);
			
			session.getTransaction().commit();
			
			return address;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static TeritoryCodes getAddressByZip(String zip) throws Exception {	
		SessionFactory sessionFactoryTerritory = null;
		Session sessionTerritory = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactoryTerritory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			sessionTerritory = sessionFactoryTerritory.openSession();

			sessionTerritory.beginTransaction();
			
			CriteriaBuilder builder = sessionTerritory.getCriteriaBuilder();
			CriteriaQuery<TeritoryCodes> zipCriteria = builder.createQuery(TeritoryCodes.class);
			Root<TeritoryCodes> zipCriteriaRoot = zipCriteria.from(TeritoryCodes.class);
			Expression<String> zipCodeExpression = zipCriteriaRoot.get("zip");
			Predicate zipCodeLike = builder.like(zipCodeExpression, "%" + StringsUtils.specialCharacterEscape(zip) + "%");
			zipCriteria.select(zipCriteriaRoot).where(zipCodeLike);
			
			List<TeritoryCodes> results = sessionTerritory.createQuery(zipCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			TeritoryCodes teritoryCode = results.get(index);
            
            sessionTerritory.getTransaction().commit();
            
            return teritoryCode;
		} catch (Exception e) {		
			e.printStackTrace();
			throw new Exception(e.getMessage() + "WITH ZIP CODE:  " + zip);
		} finally {
			sessionTerritory.clear();
            pf.close();
		}
	}
	
	public static TeritoryCodes getAddressByCode(String code, boolean notCondition) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<TeritoryCodes> territoryCodeCriteria = builder.createQuery(TeritoryCodes.class);
			Root<TeritoryCodes> territoryCodeCriteriaRoot = territoryCodeCriteria.from(TeritoryCodes.class);
			Expression<String> territoryCodeExpression = territoryCodeCriteriaRoot.get("code");
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(notCondition){
				Predicate territoryCodeLike = builder.like(territoryCodeExpression, "%" + StringsUtils.specialCharacterEscape(code) + "%");
				predicates.add(territoryCodeLike);
			} else {
				Predicate territoryCodeNotLike = builder.notLike(territoryCodeExpression, "%" + StringsUtils.specialCharacterEscape(code) + "%");
				predicates.add(territoryCodeNotLike);
			}
			territoryCodeCriteria.select(territoryCodeCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<TeritoryCodes> results = session.createQuery(territoryCodeCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			TeritoryCodes myTeritoryCode = results.get(index);
            
            session.getTransaction().commit();
            
            return myTeritoryCode;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
}












