package persistence.globaldatarepo.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.entities.CA_Terms;
import persistence.helpers.StringsUtils;

import javax.persistence.criteria.*;
import java.util.List;

public class CATermsHelper {


    public static List<CA_Terms> getCommercialAutoTermByCoverage(String coverage) throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
        try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
            session = sessionFactory.openSession();

            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CA_Terms> formCriteria = builder.createQuery(CA_Terms.class);
            Root<CA_Terms> formCriteriaRoot = formCriteria.from(CA_Terms.class);
            Expression<String> formNameExpression = formCriteriaRoot.get("coverage");
            Predicate formNameLike = builder.like(formNameExpression, "%" + StringsUtils.specialCharacterEscape(coverage) + "%");
            formCriteria.select(formCriteriaRoot).where(formNameLike);

            List<CA_Terms> results = session.createQuery(formCriteria).getResultList();

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
