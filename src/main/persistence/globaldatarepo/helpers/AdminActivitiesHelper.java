package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.AdminActivities;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AdminActivitiesHelper {

    public static void createNewAgentPolicyData(String agentUserName, String accountNumber, String policyType, String environment, String policyStatus, String requestApprovalActivity, String bindActivity, String docActivity, String uwChangeAct, String AssistantAct, String renewalAct) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            AdminActivities activities = new AdminActivities();
            activities.setAgent(agentUserName);
            activities.setAccountNumber(accountNumber);
            activities.setPolicyType(policyType);
            activities.setEnvironment(environment);
            activities.setPolicyStatus(policyStatus);
            activities.setRequestApprovalActivity(requestApprovalActivity);
            activities.setBindActivity(bindActivity);
            activities.setDocumentsActivity(docActivity);
            activities.setUwchangeActivity(uwChangeAct);
            activities.setAssistantChangeActivity(AssistantAct);
            activities.setRenewalUw(renewalAct);

            session.save(activities);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void addAcctInfo(String agentUserName, String accountNumber, String policyType, String environment, String policyStatus) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();
            AdminActivities activities = new AdminActivities();
            activities.setAgent(agentUserName);
            activities.setAccountNumber(accountNumber);
            activities.setPolicyType(policyType);
            activities.setEnvironment(environment);
            activities.setPolicyStatus(policyStatus);

            session.save(activities);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateRequestApprovalActivity(String accountNumber, String requestApprovalActivity) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities reqAct = results.get(0);
            reqAct.setRequestApprovalActivity(requestApprovalActivity);

            session.update(reqAct);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateBoundActivity(String accountNumber, String policyStatus, String boundActivity) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities bindAct = results.get(0);
            bindAct.setPolicyStatus(policyStatus);
            bindAct.setBindActivity(boundActivity);

            session.update(bindAct);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateIssueActivity(String accountNumber, String policyStatus) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities issueStatus = results.get(0);
            issueStatus.setPolicyStatus(policyStatus);

            session.update(issueStatus);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateDocActivity(String accountNumber, String documentsActivity) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities docActivity = results.get(0);
            docActivity.setDocumentsActivity(documentsActivity);

            session.update(docActivity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateChangeActivity(String accountNumber, String uwChange) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities changeActivity = results.get(0);
            changeActivity.setUwchangeActivity(uwChange);

            session.update(changeActivity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateAssistantChangeActivity(String accountNumber, String assistantChange) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities changeActivity = results.get(0);
            changeActivity.setAssistantChangeActivity(assistantChange);

            session.update(changeActivity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void updateWithCSRActivity(String accountNumber, String csr) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities csrActivity = results.get(0);
            csrActivity.setAccountNumber(results.get(0).getAccountNumber());
            csrActivity.setAgent(results.get(0).getAgent());
            csrActivity.setPolicyType(results.get(0).getPolicyType());
            csrActivity.setEnvironment(results.get(0).getEnvironment());
            csrActivity.setPolicyStatus(results.get(0).getPolicyStatus());
            csrActivity.setRequestApprovalActivity(results.get(0).getRequestApprovalActivity());
            csrActivity.setBindActivity(results.get(0).getBindActivity());
            csrActivity.setDocumentsActivity(results.get(0).getDocumentsActivity());
            csrActivity.setUwchangeActivity(results.get(0).getUwchangeActivity());
            csrActivity.setAssistantChangeActivity(results.get(0).getAssistantChangeActivity());
            csrActivity.setRenewalUw(results.get(0).getRenewalUw());
            csrActivity.setCsr(csr);
            csrActivity.setCsractivity(new Date());

            session.update(csrActivity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static AdminActivities getPolicyRowByAgent(String agentUsername) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> agentCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> agentCriteriaRoot = agentCriteria.from(AdminActivities.class);
            Expression<String> agentName = agentCriteriaRoot.get("agent");
            Expression<String> policyNumber = agentCriteriaRoot.get("bindActivity");
            Predicate agentNameLike = builder.like(agentName, "%" + StringsUtils.specialCharacterEscape(agentUsername) + "%");
            Predicate policyNumberNotNull = builder.isNotNull(policyNumber);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(agentNameLike);
            predicates.add(policyNumberNotNull);
            agentCriteria.select(agentCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AdminActivities> results = session.createQuery(agentCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());

            AdminActivities activityRecord = results.get(index);

            session.getTransaction().commit();

            return activityRecord;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }

    }

    public static List<AdminActivities> getPoliciesRowByAgent(String agentUsername) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> agentCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> agentCriteriaRoot = agentCriteria.from(AdminActivities.class);
            Expression<String> agentName = agentCriteriaRoot.get("agent");
            Expression<String> policyNumber = agentCriteriaRoot.get("bindActivity");
            Predicate agentNameLike = builder.like(agentName, "%" + StringsUtils.specialCharacterEscape(agentUsername) + "%");
            Predicate policyNumberNotNull = builder.isNotNull(policyNumber);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(agentNameLike);
            predicates.add(policyNumberNotNull);
            agentCriteria.select(agentCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AdminActivities> results = session.createQuery(agentCriteria).getResultList();

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

    public static void updateWithAgentActivity(String accountNumber, Date activityDate) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> getAccountCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> getAccountCriteriaRoot = getAccountCriteria.from(AdminActivities.class);
            Expression<String> acct = getAccountCriteriaRoot.get("accountNumber");
            Predicate departmentNameLike = builder.like(acct, "%" + StringsUtils.specialCharacterEscape(accountNumber) + "%");
            getAccountCriteria.select(getAccountCriteriaRoot).where(departmentNameLike);

            List<AdminActivities> results = session.createQuery(getAccountCriteria).getResultList();

            AdminActivities agentActivity = results.get(0);
            agentActivity.setAgentEscalated(activityDate);

            session.update(agentActivity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static void addCsrActivityData(String agentUserName, String accountNumber, String policyType, String environment, String policyStatus, String csr, Date csrActivityDate) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction hibernateTransaction = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);

            session = sessionFactory.openSession();

            hibernateTransaction = session.beginTransaction();

            AdminActivities activities = new AdminActivities();
            activities.setAgent(agentUserName);
            activities.setAccountNumber(accountNumber);
            activities.setPolicyType(policyType);
            activities.setEnvironment(environment);
            activities.setPolicyStatus(policyStatus);
            activities.setCsr(csr);
            activities.setCsractivity(csrActivityDate);


            session.save(activities);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (hibernateTransaction != null) hibernateTransaction.rollback();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    public static List<AdminActivities> getBoundPolicies(String region) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> agentCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> agentCriteriaRoot = agentCriteria.from(AdminActivities.class);
            Expression<String> agentName = agentCriteriaRoot.get("agent");
            Expression<String> policyNumber = agentCriteriaRoot.get("bindActivity");
            Predicate agentIsNotNull = builder.isNotNull(agentName);
            Predicate policyNumberNotNull = builder.isNotNull(policyNumber);
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(agentIsNotNull);
            predicates.add(policyNumberNotNull);
            agentCriteria.select(agentCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<AdminActivities> results = session.createQuery(agentCriteria).getResultList();

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

    public static List<AdminActivities> getCSRActivities(String csrUsername) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AdminActivities> csrCriteria = builder.createQuery(AdminActivities.class);
            Root<AdminActivities> csrCriteriaRoot = csrCriteria.from(AdminActivities.class);
            Expression<String> csr = csrCriteriaRoot.get("csr");
            Predicate csrLike = builder.like(csr, "%" + StringsUtils.specialCharacterEscape(csrUsername) + "%");
            csrCriteria.select(csrCriteriaRoot).where(csrLike);

            List<AdminActivities> results = session.createQuery(csrCriteria).getResultList();

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
