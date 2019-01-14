package repository.driverConfiguration;

import com.idfbins.configuration.BaseConfiguration;
import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import gwclockhelpers.ApplicationOrCenter;
import persistence.config.PersistenceFactory;
import persistence.globaldatarepo.helpers.EnvironmentsHelper;
import services.enums.Broker;

import java.util.Map;

public class Config extends BaseConfiguration{

    private String env = System.getProperty("env", "dev");
    private String gwHost = System.getProperty("host", "local"); // change me to grid  or local.
    private ApplicationOrCenter center = ApplicationOrCenter.valueOf(System.getProperty("center", "PolicyCenter"));


    private Map<ApplicationOrCenter, Urls> environmentsMap = initEnvironmentsMap(env);
    private Broker mbConnDetails = setBroker(env);

    public Config() throws Exception {
    	setHost(gwHost);
        setHostUrl("http://10.100.102.227:4444/wd/hub");
    }

    public Config(String url) throws Exception {
    	setHost(gwHost);
        setUrl(url);
        setHostUrl("http://10.100.102.227:4444/wd/hub");
    }


    public Config(ApplicationOrCenter applicationOrCenter) throws Exception {
    	setHost(gwHost);
        this.center = applicationOrCenter;
        setUrl(environmentsMap.get(applicationOrCenter).getUrl());
        setHostUrl("http://10.100.102.227:4444/wd/hub");
    }

    public Config(ApplicationOrCenter applicationOrCenter, String env) throws Exception {
    	setHost(gwHost);
        this.center = applicationOrCenter;
        this.environmentsMap = initEnvironmentsMap(env);
        setUrl(environmentsMap.get(center).getUrl());
        setHostUrl("http://10.100.102.227:4444/wd/hub");
    }


    // This uses takes an environment and sets the broker accordingly.
    private Broker setBroker(String env) {
        if (env.toUpperCase().startsWith("DEV")) {
            return Broker.DEV;
        } else if (env.toUpperCase().startsWith("IT")) {
            return Broker.IT;
        } else if (env.toUpperCase().startsWith("QA")) {
            return Broker.QA;
        } else if (env.toUpperCase().startsWith("UAT")) {
            return Broker.UAT;
        } else if (env.toUpperCase().startsWith("PRD")) {
            return Broker.PRD;
        } else {
            return Broker.DEV;
        }
    }

    private Map<ApplicationOrCenter, Urls> initEnvironmentsMap(String env) throws Exception {
        new PersistenceFactory();
        Map<ApplicationOrCenter, Urls> envMap = new EnvironmentsHelper().getGuideWireEnvironments(env);
        if (envMap.size() == 0) {
            throw new NullPointerException("Could not load environment for Server: " + env);
        } else {
            return envMap;
        }
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public ApplicationOrCenter getCenter() {
        return center;
    }

    public void setCenter(ApplicationOrCenter center) {
        this.center = center;
    }

    public Broker getMbConnDetails() {
        return mbConnDetails;
    }

    public void setMbConnDetails(Broker mbConnDetails) {
        this.mbConnDetails = mbConnDetails;
    }

    public Map<ApplicationOrCenter, Urls> getEnvironmentsMap() {
        return environmentsMap;
    }

    public String getUrlOfCenter(ApplicationOrCenter center) {
        return environmentsMap.get(center).getUrl();
    }

}
