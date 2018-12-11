package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Glforms;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GLFormsHelpers {
	
	public static Glforms getGeneralLiabilityFormByName(String formName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Glforms> formCriteria = builder.createQuery(Glforms.class);
			Root<Glforms> formCriteriaRoot = formCriteria.from(Glforms.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("name");
			Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(formName) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<Glforms> results = session.createQuery(formCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			Glforms form = results.get(index);
            
            session.getTransaction().commit();
            
            return form;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static List<Glforms> getAllGeneralLiabilityForm() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Glforms> formCriteria = builder.createQuery(Glforms.class);
			Root<Glforms> formCriteriaRoot = formCriteria.from(Glforms.class);
			formCriteria.select(formCriteriaRoot);
			
			List<Glforms> results = session.createQuery(formCriteria).getResultList();
			            
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
	
	
	public static List<Glforms> getAllGeneralLiabilityForm(int listSize) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Glforms> formCriteria = builder.createQuery(Glforms.class);
			Root<Glforms> formCriteriaRoot = formCriteria.from(Glforms.class);
			formCriteria.select(formCriteriaRoot);
			
			List<Glforms> results = session.createQuery(formCriteria).getResultList();
			
			Collections.shuffle(results);
			if(listSize > results.size()) {
				listSize = results.size();
			}
			
            session.getTransaction().commit();
            
            return results.subList(0, listSize);
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
}
