package repository.listeners;

import com.idfbins.testng.persistence.PersistenceFactory;
import com.idfbins.testng.persistence.datasources.TestApplication;
import com.idfbins.testng.persistence.datasources.TestSuite;
import com.idfbins.testng.persistence.helpers.RegressionDashboardHelpers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.DateUtils;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalRegressionDashboardHelpers extends RegressionDashboardHelpers {

    public static TestSuite createTestSuiteIfNotExistsWithinTimePeriod(String suiteName, TestApplication relatedTestApplication, DateAddSubtractOptions changeFormat, int amountToChange) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory persistenceFactory = new PersistenceFactory();
        try {
            sessionFactory = persistenceFactory.getSessionFactory();
            session = sessionFactory.openSession();

            session.beginTransaction();

            Date today = new Date();
            Date todayMinus24 = DateUtils.dateAddSubtract(today, changeFormat, amountToChange);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TestSuite> suiteCriteria = builder.createQuery(TestSuite.class);
            Root<TestSuite> suiteCriteriaRoot = suiteCriteria.from(TestSuite.class);
            Expression<String> name = suiteCriteriaRoot.get("name");
            Expression<String> testApplication = suiteCriteriaRoot.get("testApplication");
            Expression<Date> startTime = suiteCriteriaRoot.get("startTime");
            Predicate suiteNameEquals = builder.equal(name, suiteName);
            Predicate relatedTestApplicationEquals = builder.equal(testApplication, relatedTestApplication);
            Predicate dateRange = builder.between(startTime, builder.literal(todayMinus24), builder.literal(today));
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(suiteNameEquals);
            predicates.add(relatedTestApplicationEquals);
            predicates.add(dateRange);
            suiteCriteria.select(suiteCriteriaRoot).where(predicates.toArray(new Predicate[]{}));

            List<TestSuite> results = session.createQuery(suiteCriteria).getResultList();

            TestSuite toReturn;
            if (results.size() > 0) {
                toReturn = results.get(0);
                System.out.println("Using Regression Suite that Starts at: " + toReturn.getStartTime());
            } else {
                toReturn = new TestSuite();
                toReturn.setVersion(new BigDecimal(1));
                toReturn.setName(suiteName);
                toReturn.setTestApplication(relatedTestApplication);
            }

            session.save(toReturn);

            session.getTransaction().commit();

            return toReturn;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            persistenceFactory.close();
        }
    }

}
