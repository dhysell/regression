package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.Addresses;
import persistence.globaldatarepo.entities.TeritoryCodes;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class AddressHelper {


    public static Addresses getRandomAddress() throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
            Root<Addresses> countCriteriaRoot = countCriteria.from(Addresses.class);
            countCriteria.select(builder.count(countCriteriaRoot));
            Long countinTable = session.createQuery(countCriteria).getSingleResult();
            
            Random randomNumberGenerator = new Random();
            int randomNumberCount = randomNumberGenerator.nextInt(countinTable.intValue() - 1);
            
            CriteriaQuery<Addresses> addressCriteria = builder.createQuery(Addresses.class);
            Root<Addresses> addressCriteriaRoot = addressCriteria.from(Addresses.class);
            addressCriteria.select(addressCriteriaRoot);
            
            Addresses address = session.createQuery(addressCriteria).setFirstResult(randomNumberCount).setMaxResults(1).getSingleResult();

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

    public static Addresses getRandomAddressByZip(String zip) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        List<Addresses> results = new ArrayList<Addresses>();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Addresses> zipCriteria = builder.createQuery(Addresses.class);
            Root<Addresses> zipCriteriaRoot = zipCriteria.from(Addresses.class);
            Expression<String> zipcode = zipCriteriaRoot.get("zip");
            Predicate zipcodeLike = builder.like(zipcode, "%" + StringsUtils.specialCharacterEscape(zip) + "%");
            zipCriteria.select(zipCriteriaRoot).where(zipcodeLike);

            results = session.createQuery(zipCriteria).getResultList();

            if (results.isEmpty()) {
                TeritoryCodes territory = TeritoryCodesHelper.getAddressByZip(zip);
                return new Addresses("123 N Main", territory.getCity(), "Idaho", territory.getZip(), null, null, Integer.valueOf(territory.getCode()));
            }

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());
            Addresses address = results.get(index);

            session.getTransaction().commit();

            return address;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (!results.isEmpty()) {
                session.clear();
            }
            pf.close();
        }
    }

    public static void overwriteAddressInformation(HashMap<String, String> originalAddressInfo, HashMap<String, String> newAddressInfo) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Addresses> addressCriteria = builder.createQuery(Addresses.class);
            Root<Addresses> addressCriteriaRoot = addressCriteria.from(Addresses.class);
            List<Predicate> predicates = new ArrayList<Predicate>();
            for (Entry<String, String> hashMapEntry : originalAddressInfo.entrySet()) {
                Expression<String> expression = addressCriteriaRoot.get(hashMapEntry.getKey());
                Predicate expressionPredicateEquals = builder.equal(expression, hashMapEntry.getValue());
                predicates.add(expressionPredicateEquals);
            }
            addressCriteria.select(addressCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<Addresses> results = session.createQuery(addressCriteria).getResultList();

            if (results.size() != 1) {
                System.err.println("The Information Used for the SQL query was:");
                for (Entry<String, String> hashMapEntry : originalAddressInfo.entrySet()) {
                    System.err.println(hashMapEntry.getKey() + ":" + hashMapEntry.getValue());
                }
                throw new Exception("The query to the Database should have returned 1 and only 1 entry, but returned a different number. Cannot continue to overwrite entry.");
            }
            Addresses address = results.get(0);

            for (Entry<String, String> hashMapEntry : newAddressInfo.entrySet()) {
                switch (hashMapEntry.getKey()) {
                    case "address":
                        address.setAddress(hashMapEntry.getValue());
                        break;
                    case "city":
                        address.setCity(hashMapEntry.getValue());
                        break;
                    case "state":
                        address.setState(hashMapEntry.getValue());
                        break;
                    case "zip":
                        address.setZip(hashMapEntry.getValue());
                        break;
                    case "zip4":
                        address.setZip4(hashMapEntry.getValue());
                        break;
                    case "county":
                        address.setCounty(hashMapEntry.getValue());
                        break;
                    default:
                        break;
                }
            }

            session.update(address);
            hibernateTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static List<Addresses> getAddresses() throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Addresses> addressCriteria = builder.createQuery(Addresses.class);
            Root<Addresses> addressCriteriaRoot = addressCriteria.from(Addresses.class);
            addressCriteria.select(addressCriteriaRoot);

            List<Addresses> results = session.createQuery(addressCriteria).getResultList();

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

    public static void createNewAddress(Addresses address) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            session.beginTransaction();

            session.save(address);

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
