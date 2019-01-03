package persistence.globaldatarepo.helpers;

import com.idfbins.hibernate.qa.guidewire.environments.Envs;
import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import gwclockhelpers.ApplicationOrCenter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvironmentsHelper {

    public Map<ApplicationOrCenter, Urls> getGuideWireEnvironments(String environment) throws Exception {
		Map<ApplicationOrCenter, Urls> environmentsMap = new HashMap<ApplicationOrCenter, Urls>();

		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();

			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			Query query = em.createNativeQuery("SELECT GWEnvs_Urls.UrlsID, GWEnvs_Urls.Url, GWEnvs_Urls.SVNBranch, GWEnvs_Urls.SVNRevision, " +
					"GWEnvs_Urls.LastBuildID, GWEnvs_Urls.LastBuildLink, GWEnvs_Urls.JenkinsDeployJobUrl, GWEnvs_Urls.LogPath, GWEnvs_Urls.RoundtripDocuments, " +
                    "GWEnvs_Urls.EnvsID, GWEnvs_UrlTypes.UrlTypesID, GWEnvs_UrlTypes.Name FROM GWEnvs_Urls " +
                    "LEFT JOIN GWEnvs_UrlTypes ON GWEnvs_Urls.UrlTypesID = GWEnvs_UrlTypes.UrlTypesID " +
					"INNER JOIN GWEnvs_Envs ON GWEnvs_Urls.EnvsID = GWEnvs_Envs.EnvsID AND GWEnvs_Envs.Name = '" + environment + "'", Urls.class);
			@SuppressWarnings("unchecked")
			List<Urls> rows = query.getResultList();

            session.getTransaction().commit();

            for(Urls qaEnvironment : rows){
				String appStr = qaEnvironment.getUrlTypes().getName();
				ApplicationOrCenter toInput = ApplicationOrCenter.getApplicationOrCenterFromStrValue(appStr);
				environmentsMap.put(toInput, qaEnvironment);
			}

        } catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
        }

		return environmentsMap;

    }

    public String getEnvironmentFromURL(String URL) throws Exception {
		SessionFactory sessionFactory = null;
		Session session = null;
        PersistenceFactory pf = new PersistenceFactory();
		try {
            sessionFactory = pf.getSessionFactory(HibernateConfigs.GlobalDataRepository);
			session = sessionFactory.openSession();

			session.beginTransaction();
			
			String modifiedURL = "";
			if (URL.endsWith(".do")) {
				String[] urlSplit = URL.split("/");
				for (int i = 0; i < (urlSplit.length - 1); i++) {
					modifiedURL = modifiedURL + urlSplit[i];
					modifiedURL = modifiedURL + "/";
				}
			} else {
				modifiedURL = URL;
			}

			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			Query query = em.createNativeQuery("SELECT GWEnvs_Envs.EnvsID, GWEnvs_Envs.Name, GWEnvs_Envs.ClockMove, GWEnvs_Envs.PortalAuthAdminConsoleUrl, " +
					"GWEnvs_Envs.EnvTypesID, GWEnvs_Envs.DocumentRoundTrip, GWEnvs_Urls.UrlsID, GWEnvs_Urls.Url FROM GWEnvs_Envs  " +
                    "JOIN GWEnvs_Urls ON GWEnvs_Urls.EnvsID = GWEnvs_Envs.EnvsID " +
					"WHERE GWEnvs_Urls.Url = '" + modifiedURL + "'", Envs.class);
			@SuppressWarnings("unchecked")
			List<Envs> results = query.getResultList();

            session.getTransaction().commit();
            
            return results.get(0).getName();
            
        } catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			session.clear();
            pf.close();
        }
    }
}
