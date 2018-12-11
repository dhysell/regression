package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Contacts;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ContactsHelpers {
	
	public static Contacts getRandomContact() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactsCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactsCriteriaRoot = contactsCriteria.from(Contacts.class);
			contactsCriteria.select(contactsCriteriaRoot);
			
			List<Contacts> results = session.createQuery(contactsCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			Contacts contact = results.get(index);
            
            session.getTransaction().commit();
            
            return contact;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	public static Contacts getRandomContact(String companyOrPerson) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		//Criterion companyPerson = Restrictions.eq("contactIsCompany", Boolean.TRUE);
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactsCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactsCriteriaRoot = contactsCriteria.from(Contacts.class);
			Expression<Boolean> companyPerson = contactsCriteriaRoot.get("contactIsCompany");
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(companyOrPerson.equalsIgnoreCase("company")){
				Predicate companyContact = builder.isTrue(companyPerson);
				predicates.add(companyContact);
			} else {
				Predicate personContact = builder.isFalse(companyPerson);
				predicates.add(personContact);
			}
			contactsCriteria.select(contactsCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Contacts> results = session.createQuery(contactsCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			Contacts contact = results.get(index);
            
            session.getTransaction().commit();
            
            return contact;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Contacts getRandomPersonOrCompWithRole(String companyOrPerson, String role) throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		//Criterion companyPerson = Restrictions.eq("contactIsCompany", Boolean.TRUE);
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactsCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactsCriteriaRoot = contactsCriteria.from(Contacts.class);
			Expression<Boolean> companyPerson = contactsCriteriaRoot.get("contactIsCompany");
			Expression<String> contactRole = contactsCriteriaRoot.get("contactRoles");
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(companyOrPerson.equalsIgnoreCase("company")){
				Predicate companyContact = builder.isTrue(companyPerson);
				predicates.add(companyContact);
			} else {
				Predicate personContact = builder.isFalse(companyPerson);
				predicates.add(personContact);
			}
			Predicate contactRoleLike = builder.like(contactRole, "%" + StringsUtils.specialCharacterEscape(role) + "%");
			predicates.add(contactRoleLike);
			
			contactsCriteria.select(contactsCriteriaRoot).where(predicates.toArray(new Predicate[]{}));
			
			List<Contacts> results = session.createQuery(contactsCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
			Contacts contact = results.get(index);
            
            session.getTransaction().commit();
            
            return contact;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static Contacts getRandomContactWithRole(String role) throws Exception {		
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactCriteriaRoot = contactCriteria.from(Contacts.class);
			Expression<String> contactRole = contactCriteriaRoot.get("contactRoles");
			Predicate contactRoleLike = builder.like(contactRole, "%" + StringsUtils.specialCharacterEscape(role) + "%");
			contactCriteria.select(contactCriteriaRoot).where(contactRoleLike);
			
			List<Contacts> results = session.createQuery(contactCriteria).getResultList();
			
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(results.size());
			
            Contacts contact = results.get(index);
            
            session.getTransaction().commit();
            
            return contact;
            
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	public static List<Contacts> getContactList() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactsCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactsCriteriaRoot = contactsCriteria.from(Contacts.class);
			contactsCriteria.select(contactsCriteriaRoot);
			
			List<Contacts> results = session.createQuery(contactsCriteria).getResultList();
			
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
	
	
	
	public static void createNewContact(String contactName, String contactAddressLine1,
			String contactCity, String contactState, String contactZip,
			String contactNumber, boolean contactIsCompany, String contactRoles, String phone) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			
			session = sessionFactory.openSession();
			
			session.beginTransaction();
			Contacts contact = new Contacts();
			contact.setContactAddressLine1(contactAddressLine1);
			contact.setContactCity(contactCity);
			contact.setContactIsCompany(contactIsCompany);
			contact.setContactName(contactName);
			contact.setContactNumber(contactNumber);
			contact.setContactRoles(contactRoles);
			contact.setContactState(contactState);
			contact.setContactZip(contactZip);
			if(phone.equals("") || phone.equals(" ") || phone.equals("555-555-5555")) {
				contact.setContactPhone(null);
			} else {
				contact.setContactPhone(phone);
			}
			

			session.save(contact);
			
			session.getTransaction().commit();
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
		
	}

    public static void createnewContactFromList(List<Contacts> contactsList) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            session.beginTransaction();

            for (Contacts contact : contactsList) {
                session.save(contact);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }
	
	
	public static Contacts getContactWithPhone() throws Exception {	
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Contacts> contactsCriteria = builder.createQuery(Contacts.class);
			Root<Contacts> contactsCriteriaRoot = contactsCriteria.from(Contacts.class);
			contactsCriteria.select(contactsCriteriaRoot);
			
			List<Contacts> results = session.createQuery(contactsCriteria).getResultList();
			
			ArrayList<Contacts> phoneResults = new ArrayList<Contacts>();
            session.getTransaction().commit();
            for(int x=0; x<results.size(); x++){
            	if(!(results.get(x).getContactPhone() == null)){
            		if(!(results.get(x).getContactPhone().equals(""))){
            			if(results.get(x).getContactPhone() != null){
            				phoneResults.add(results.get(x));
            			}
            		}
            	}
            }
            Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(phoneResults.size());
			Contacts contact = phoneResults.get(index);
            
            return contact;
		} catch (Exception e) {			
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
		}
	}
	
	
	
	
	
	
	
}














