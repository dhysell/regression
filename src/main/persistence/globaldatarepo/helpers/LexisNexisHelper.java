package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.helpers.DateAddSubtractOptions;
import persistence.helpers.DateUtils;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LexisNexisHelper {

	public static LexisNexis getCustomerByName(String firstName, String middleName, String lastName) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			if(lastName == null) {
				throw new Exception("ERROR: LastName can't be null");
			}
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<String> firstNameExpression = lexisNexisCriteriaRoot.get("firstName");
			Expression<String> middleNameExpression = lexisNexisCriteriaRoot.get("middleName");
			Expression<String> lastNameExpression = lexisNexisCriteriaRoot.get("lastName");
			Predicate customerFirstNameLike = builder.like(firstNameExpression, "%" + firstName + "%");
			Predicate customerLastNameLike = builder.like(lastNameExpression, "%" + lastName + "%");
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(customerFirstNameLike);
			predicates.add(customerLastNameLike);
			if(middleName != null) {
				Predicate customerMiddleNameLike = builder.like(middleNameExpression, "%" + middleName + "%");
				predicates.add(customerMiddleNameLike);
			}
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
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
	
	public static LexisNexis getCustomerBySSN(String ssn) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<String> ssnExpression = lexisNexisCriteriaRoot.get("ssn");
			Predicate ssnLike = builder.like(ssnExpression, "%" + StringsUtils.specialCharacterEscape(ssn) + "%");
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(ssnLike);
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
						
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
	
	public static LexisNexis getRandomCustomer(Date currDate, boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		if(prefillPersonal && prefillCommercial) {
			throw new Exception("ERROR: You can't have both personal and Commercial prefill true... Sorry not sorry");
		}
		
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
					
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<String> firstNameExpression = lexisNexisCriteriaRoot.get("firstName");
			Expression<String> middleNameExpression = lexisNexisCriteriaRoot.get("middleName");
			Expression<String> lastNameExpression = lexisNexisCriteriaRoot.get("lastName");
			Expression<Boolean> prefillPersonalExpression = lexisNexisCriteriaRoot.get("autoDataPrefill");
			Expression<Boolean> prefillCommercialExpression = lexisNexisCriteriaRoot.get("commercialPrefill");
			Expression<Boolean> cbrExpression = lexisNexisCriteriaRoot.get("cbr");
			Expression<Integer> mvrExpression = lexisNexisCriteriaRoot.get("mvrNumEntriesOnDrivingRecord");
			Expression<Integer> clueAutoExpression = lexisNexisCriteriaRoot.get("clueAutoNumClaimsForDriver");
			Expression<Integer> cluePropertyExpression = lexisNexisCriteriaRoot.get("cluePropertyNumClaimsForDriver");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			predicates.add(dateLessThanOrEqual);
			
			if(prefillPersonal) {
				Predicate prefillPersonalTrue = builder.isTrue(prefillPersonalExpression);
				predicates.add(prefillPersonalTrue);
			}
			
			if(prefillCommercial) {
				Predicate prefillCommercialTrue = builder.isTrue(prefillCommercialExpression);
				predicates.add(prefillCommercialTrue);
				if(!person) {
					Predicate firstNameNull = builder.isNull(firstNameExpression);
					predicates.add(firstNameNull);
					Predicate middleNameNull = builder.isNull(middleNameExpression);
					predicates.add(middleNameNull);
				} else {
					Predicate firstNameNotNull = builder.isNotNull(firstNameExpression);
					predicates.add(firstNameNotNull);
					Predicate lastNameNotNull = builder.isNotNull(lastNameExpression);
					predicates.add(lastNameNotNull);
				}	
			}
			
			if(insuranceScore) {
				Predicate cbrTrue = builder.isTrue(cbrExpression);
				predicates.add(cbrTrue);
			}
			
			if(mvr) {
				Predicate mvrTrue = builder.greaterThan(mvrExpression, 0);
				Predicate mvrNotNull = builder.isNotNull(mvrExpression);
				predicates.add(mvrTrue);
				predicates.add(mvrNotNull);
			}
			
			if(clueAuto) {
				Predicate clueAutoTrue = builder.greaterThan(clueAutoExpression, 0);
				Predicate clueAutoNotNull = builder.isNotNull(clueAutoExpression);
				predicates.add(clueAutoTrue);
				predicates.add(clueAutoNotNull);
			}
			
			if(clueProperty) {
				Predicate cluePropTrue = builder.greaterThan(cluePropertyExpression, 0);
				Predicate cluePropNotNull = builder.isNotNull(cluePropertyExpression);
				predicates.add(cluePropTrue);
				predicates.add(cluePropNotNull);
			}
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static LexisNexis getRandomCustomerCBR(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Boolean> cbrExpression = lexisNexisCriteriaRoot.get("cbr");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			predicates.add(dateLessThanOrEqual);
			
			Predicate cbrTrue = builder.isTrue(cbrExpression);
			predicates.add(cbrTrue);
						
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<LexisNexis> getCustomersCBR(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Boolean> cbrExpression = lexisNexisCriteriaRoot.get("cbr");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			predicates.add(dateLessThanOrEqual);
			
			Predicate cbrTrue = builder.isTrue(cbrExpression);
			predicates.add(cbrTrue);
						
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
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
	
	public static LexisNexis getRandomCustomerMVR(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Integer> mvrExpression = lexisNexisCriteriaRoot.get("mvrNumEntriesOnDrivingRecord");
			Expression<String> dlNumberExpression = lexisNexisCriteriaRoot.get("dlnumber");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			Predicate mvrTrue = builder.greaterThan(mvrExpression, 0);
			Predicate mvrNotNull = builder.isNotNull(mvrExpression);
			Predicate licenseNotNull = builder.isNotNull(dlNumberExpression);
			predicates.add(dateLessThanOrEqual);
			predicates.add(mvrTrue);
			predicates.add(mvrNotNull);
			predicates.add(licenseNotNull);
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<LexisNexis> getCustomersMVR(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Integer> mvrExpression = lexisNexisCriteriaRoot.get("mvrNumEntriesOnDrivingRecord");
			Expression<String> dlNumberExpression = lexisNexisCriteriaRoot.get("dlnumber");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			Predicate mvrTrue = builder.greaterThan(mvrExpression, 0);
			Predicate mvrNotNull = builder.isNotNull(mvrExpression);
			Predicate licenseNotNull = builder.isNotNull(dlNumberExpression);
			predicates.add(dateLessThanOrEqual);
			predicates.add(mvrTrue);
			predicates.add(mvrNotNull);
			predicates.add(licenseNotNull);
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
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
	
	public static LexisNexis getRandomCustomerCLUEAuto(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Integer> clueAutoExpression = lexisNexisCriteriaRoot.get("clueAutoNumClaimsForDriver");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			Predicate mvrTrue = builder.greaterThan(clueAutoExpression, 0);
			Predicate mvrNotNull = builder.isNotNull(clueAutoExpression);
			predicates.add(dateLessThanOrEqual);
			predicates.add(mvrTrue);
			predicates.add(mvrNotNull);
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static LexisNexis getRandomCustomerCLUEProperty(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Integer> clueAutoExpression = lexisNexisCriteriaRoot.get("clueAutoNumClaimsForDriver");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			Predicate mvrTrue = builder.greaterThan(clueAutoExpression, 0);
			Predicate mvrNotNull = builder.isNotNull(clueAutoExpression);
			predicates.add(dateLessThanOrEqual);
			predicates.add(mvrTrue);
			predicates.add(mvrNotNull);
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static LexisNexis getRandomCustomerPrefillPersonal(Date currDate) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<Boolean> prefillPersonalExpression = lexisNexisCriteriaRoot.get("autoDataPrefill");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			Predicate prefillIsTrue = builder.isTrue(prefillPersonalExpression);
			predicates.add(dateLessThanOrEqual);
			predicates.add(prefillIsTrue);
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static LexisNexis getRandomCustomerPrefillCommercial(Date currDate, boolean company) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			Date currentDateMinus18Years = DateUtils.dateAddSubtract(currDate, DateAddSubtractOptions.Year, -18);
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LexisNexis> lexisNexisCriteria = builder.createQuery(LexisNexis.class);
			Root<LexisNexis> lexisNexisCriteriaRoot = lexisNexisCriteria.from(LexisNexis.class);
			Expression<Date> dateExpression = lexisNexisCriteriaRoot.get("dob");
			Expression<String> firstNameExpression = lexisNexisCriteriaRoot.get("firstName");
			Expression<String> middleNameExpression = lexisNexisCriteriaRoot.get("middleName");
			Expression<String> lastNameExpression = lexisNexisCriteriaRoot.get("lastName");
			Expression<Boolean> prefillCommercialExpression = lexisNexisCriteriaRoot.get("commercialPrefill");
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			Predicate dateLessThanOrEqual = builder.lessThanOrEqualTo(dateExpression, currentDateMinus18Years);
			predicates.add(dateLessThanOrEqual);
			
			Predicate prefillCommercialTrue = builder.isTrue(prefillCommercialExpression);
			predicates.add(prefillCommercialTrue);
			if(company) {
				Predicate firstNameNull = builder.isNull(firstNameExpression);
				predicates.add(firstNameNull);
				Predicate middleNameNull = builder.isNull(middleNameExpression);
				predicates.add(middleNameNull);
			} else {
				Predicate firstNameNotNull = builder.isNotNull(firstNameExpression);
				predicates.add(firstNameNotNull);
				Predicate lastNameNotNull = builder.isNotNull(lastNameExpression);
				predicates.add(lastNameNotNull);
			}
			
			lexisNexisCriteria.select(lexisNexisCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<LexisNexis> results = session.createQuery(lexisNexisCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			LexisNexis ret = results.get(index);
			
			session.getTransaction().commit();
			
			return ret;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
}
