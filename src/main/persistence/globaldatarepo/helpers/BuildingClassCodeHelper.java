package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.BuildingClassCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Random;

public class BuildingClassCodeHelper {

	
	public static List<BuildingClassCodes> getAllBuidlingClassCodes() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<BuildingClassCodes> arUserCriteria = builder.createQuery(BuildingClassCodes.class);
			Root<BuildingClassCodes> arUserCriteriaRoot = arUserCriteria.from(BuildingClassCodes.class);
			arUserCriteria.select(arUserCriteriaRoot);
			
			List<BuildingClassCodes> results = session.createQuery(arUserCriteria).getResultList();
			
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
	
	
	public static BuildingClassCodes getRandomClassCode() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<BuildingClassCodes> arUserCriteria = builder.createQuery(BuildingClassCodes.class);
			Root<BuildingClassCodes> arUserCriteriaRoot = arUserCriteria.from(BuildingClassCodes.class);
			arUserCriteria.select(arUserCriteriaRoot);
			
			List<BuildingClassCodes> results = session.createQuery(arUserCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			session.getTransaction().commit();
			return results.get(index);
			
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}
	
	public static BuildingClassCodes getBuildingClassCode(String classCode) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<BuildingClassCodes> buildingCriteria = builder.createQuery(BuildingClassCodes.class);
			Root<BuildingClassCodes> buildingCriteriaRoot = buildingCriteria.from(BuildingClassCodes.class);
			Expression<String> code = buildingCriteriaRoot.get("code");
			Predicate classCodeLike = builder.like(code, "%" + StringsUtils.specialCharacterEscape(classCode) + "%");
			buildingCriteria.select(buildingCriteriaRoot).where(classCodeLike);
			
			List<BuildingClassCodes> results = session.createQuery(buildingCriteria).getResultList();
			
			BuildingClassCodes buildingCode = results.get(0);
            
            session.getTransaction().commit();
            
            return buildingCode;
		} catch (Exception e) {	
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	
	
	
	
}



















