package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.VeriskMvr;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Verisk_MVRHelpers {

	
	
	enum dlNumber {
		AB112100A,
		AB112101B,
		AB112102A,
		AB112103B,
		AB112104A,
		AB112105B,
		AB112106A,
		AB112107B,
		AB112108A,
		AB112109B,
		AB112114A,
		AB112115B,
		AB112116A,
		AB112117B,
		AB112118A,
		AB112119B,
		AB112120A,
		AB112121B,
		AB112122A,
		AB112123B,
		AB112124A,
		AB112125A,
		AB112125B,
		AB112110A,
		AB112111B,
		AB112112A,
		AB112113B,
		AB123456A,
		AB123456B;
		
		
		private static final List<dlNumber> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final Random RANDOM = new Random();

		public static dlNumber random()  {
			return VALUES.get(RANDOM.nextInt(VALUES.size()));
		}
	}
	
	public static List<VeriskMvr> getVeriskTestCase() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<VeriskMvr> pcUserCriteria = builder.createQuery(VeriskMvr.class);
			Root<VeriskMvr> pcUserCriteriaRoot = pcUserCriteria.from(VeriskMvr.class);
			Expression<String> jobTitle = pcUserCriteriaRoot.get("driverLicense");
			Predicate jobTitleLike = builder.like(jobTitle, "%" + StringsUtils.specialCharacterEscape(dlNumber.random().name()) + "%");
			pcUserCriteria.select(pcUserCriteriaRoot).where(jobTitleLike);
			
			List<VeriskMvr> results = session.createQuery(pcUserCriteria).getResultList();

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
	
	public static List<VeriskMvr> getVeriskTestCase(String dlNumber) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<VeriskMvr> pcUserCriteria = builder.createQuery(VeriskMvr.class);
			Root<VeriskMvr> pcUserCriteriaRoot = pcUserCriteria.from(VeriskMvr.class);
			Expression<String> jobTitle = pcUserCriteriaRoot.get("driverLicense");
			Predicate jobTitleLike = builder.like(jobTitle, "%" + dlNumber + "%");
			pcUserCriteria.select(pcUserCriteriaRoot).where(jobTitleLike);
			
			List<VeriskMvr> results = session.createQuery(pcUserCriteria).getResultList();

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
