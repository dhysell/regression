package scratchpad.bill.SQL;

import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testng.annotations.Test;

import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.entities.GLUnderwriterQuestions;
import persistence.globaldatarepo.helpers.UWQuestionsHelper;
import persistence.helpers.StringsUtils;
public class SplitClassCodeIfUWQuestionsInSQL {

    public static List<GLClassCodes> getGLClassCodeByCode() throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
            Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
            classCodeCriteria.select(classCodeCriteriaRoot);

            List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());

            GLClassCodes classCode = results.get(index);
            //increment class code usage
            classCode.setUseCounter(classCode.getUseCounter() + 1);
            session.update(classCode);

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

    public static List<GLUnderwriterQuestions> getGLClassCodeWithUWQuestions() throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GLUnderwriterQuestions> uwQuestionsCriteria = builder.createQuery(GLUnderwriterQuestions.class);
            Root<GLUnderwriterQuestions> uwQuestionsCriteriaRoot = uwQuestionsCriteria.from(GLUnderwriterQuestions.class);
            uwQuestionsCriteria.select(uwQuestionsCriteriaRoot);

            List<GLUnderwriterQuestions> results = session.createQuery(uwQuestionsCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());

            GLUnderwriterQuestions classCode = results.get(index);
            //increment class code usage
            session.update(classCode);

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

    public static GLClassCodes getGLClassCodeByCode(String code, int bunnyFoo) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GLClassCodes> classCodeCriteria = builder.createQuery(GLClassCodes.class);
            Root<GLClassCodes> classCodeCriteriaRoot = classCodeCriteria.from(GLClassCodes.class);
            Expression<String> classCodeExpression = classCodeCriteriaRoot.get("code");
            Predicate classCodeLike = builder.like(classCodeExpression, "%" + StringsUtils.specialCharacterEscape(code) + "%");
            classCodeCriteria.select(classCodeCriteriaRoot).where(classCodeLike);

            List<GLClassCodes> results = session.createQuery(classCodeCriteria).getResultList();

            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(results.size());

            GLClassCodes classCode = results.get(index);
            //increment class code usage
            //classCode.setHasQuestions(bunnyFoo);
            session.update(classCode);

            session.getTransaction().commit();

            return classCode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            session.clear();
            pf.close();
        }
    }

    @Test()
    public void setGLClassCodesWithoutQuestions() throws Exception {
        List<GLClassCodes> listGLClassCodes = getGLClassCodeByCode();

        for (GLClassCodes fooFoo : listGLClassCodes) {
            List<GLUnderwriterQuestions> listGLUWQuestions = UWQuestionsHelper.getUWQuestionsClassCode(fooFoo.getCode());
            if (listGLUWQuestions != null && !listGLUWQuestions.isEmpty()) {
                getGLClassCodeByCode(fooFoo.getCode(), 1);
            } else {
                getGLClassCodeByCode(fooFoo.getCode(), 0);
            }
        }

    }

}
