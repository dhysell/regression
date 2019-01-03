package persistence.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import persistence.enums.HibernateConfigs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceFactory {

    private PersistenceFactory instance;
	private Map<HibernateConfigs, SessionFactory> sessionFactoryMap;

	public PersistenceFactory() {
		sessionFactoryMap = new HashMap<HibernateConfigs, SessionFactory>();
		
		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		if(callerClassName.equals("config.Configuration")) {
			try {
				exportJarResource("qawizproglobaldatarepository.hibernate.cfg.xml");
				exportJarResource("guidewireAB8DEV.hibernate.cfg.xml");
				exportJarResource("guidewirePC8DEV.hibernate.cfg.xml");
				exportJarResource("guidewirePC8DEV2.hibernate.cfg.xml");
				exportJarResource("guidewirePC8IT.hibernate.cfg.xml");
				exportJarResource("guidewirePC8IT2.hibernate.cfg.xml");
				exportJarResource("guidewirePC8QA.hibernate.cfg.xml");
				exportJarResource("guidewirePC8QA2.hibernate.cfg.xml");
				exportJarResource("guidewirePC8REGR01.hibernate.cfg.xml");
				exportJarResource("guidewirePC8REGR02.hibernate.cfg.xml");
				exportJarResource("guidewirePC8REGR03.hibernate.cfg.xml");
				exportJarResource("guidewirePC8REGR04.hibernate.cfg.xml");
				exportJarResource("guidewirePC8UAT.hibernate.cfg.xml");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

    public PersistenceFactory getInstance() {
		if (instance == null) {
			instance = new PersistenceFactory();
		}
		return instance;
	}
	
	private void generateSessionFactory(HibernateConfigs config){
		Configuration configuration = new Configuration();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(config.getPathValue()).build();

		sessionFactoryMap.put(config, configuration.buildSessionFactory(serviceRegistry));
	}

	public SessionFactory getSessionFactory(HibernateConfigs config) {
		if(sessionFactoryMap == null || !sessionFactoryMap.containsKey(config)){
			generateSessionFactory(config);
		}
		Logger log = Logger.getLogger("org.hibernate");
	    log.setLevel(Level.OFF);
		return sessionFactoryMap.get(config);
	}
	
	public void close(){
		for(Map.Entry<HibernateConfigs, SessionFactory> entry : sessionFactoryMap.entrySet()){
			entry.getValue().close();//closes session
			sessionFactoryMap.remove(entry.getKey());//removes session from map
		}
		instance = null;
	}

    public String exportJarResource(String resourceName) throws Exception {
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String jarFolder;
		String firstFilePath = "";
		String fullFilePath = "";
		try {
			stream = PersistenceFactory.class.getResourceAsStream(resourceName);
			if (stream == null) {
				throw new Exception("Cannot get resource \"" + resourceName + "\" from .JAR file.");
			}
			jarFolder = new File(PersistenceFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
			firstFilePath = jarFolder + "/src/persistence/config/";
			File fFirst = new File(firstFilePath);
			if (!fFirst.exists()) {
				fFirst.mkdirs();
			}
			fullFilePath = firstFilePath + resourceName;
			File f = new File(fullFilePath);
			if (!f.exists()) {
				int readBytes;
				byte[] buffer = new byte[4096];

				resStreamOut = new FileOutputStream(fullFilePath);
				while ((readBytes = stream.read(buffer)) > 0) {
					resStreamOut.write(buffer, 0, readBytes);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			stream.close();
			if (resStreamOut != null) {
				resStreamOut.close();
			}
		}
		if (resStreamOut != null) {
			System.out.println("Resource successfully extracted to: " + fullFilePath);
		} else {
			System.out.println("Resource already exists at: " + fullFilePath);
		}
		return fullFilePath;
	}
	
}
