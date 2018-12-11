package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CA_TermOptions;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class CATermOptionsHelper {

	
	public static List<CA_TermOptions> getTermOptionsByCoverage(String coverage) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CA_TermOptions> formCriteria = builder.createQuery(CA_TermOptions.class);
			Root<CA_TermOptions> formCriteriaRoot = formCriteria.from(CA_TermOptions.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("coverage");
			Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(coverage) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<CA_TermOptions> results = session.createQuery(formCriteria).getResultList();
            
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
	
	public static List<CA_TermOptions> getTermOptionsByCoverageTerm(String coverageTerm) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<CA_TermOptions> formCriteria = builder.createQuery(CA_TermOptions.class);
			Root<CA_TermOptions> formCriteriaRoot = formCriteria.from(CA_TermOptions.class);
			Expression<String> formNameExpression = formCriteriaRoot.get("coverageTerm");
			Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(coverageTerm) + "%");
			formCriteria.select(formCriteriaRoot).where(formNameLike);
			
			List<CA_TermOptions> results = session.createQuery(formCriteria).getResultList();
            
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
}
