package persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;

import gwclockhelpers.ApplicationOrCenter;
import gwclockhelpers.ClockEnvironmentsHelper;
import persistence.config.PersistenceFactory;
import persistence.enums.HibernateConfigs;
import persistence.globaldatarepo.helpers.EnvironmentsHelper;

public class EnvironmentsTest {
	
	public static SessionFactory sessionFactory;

	@BeforeClass
	public static void beforeClass() throws Exception {
		// gets a session from SessionManager.
        sessionFactory = new PersistenceFactory().getSessionFactory(HibernateConfigs.GlobalDataRepository);
	}
	
	@Test
	public void testGetEnviornment() throws Exception {
        Map<ApplicationOrCenter, Urls> environmentsMap = new EnvironmentsHelper().getGuideWireEnvironments("DEV");
		for (Map.Entry<ApplicationOrCenter, Urls> entry : environmentsMap.entrySet()) {
			System.out.println(entry.getValue().getUrl());
		}
		System.out.println();
		
		List<Urls> environmentsList = new ClockEnvironmentsHelper().getUrlsListBasedOnEnvironment("DEV");
		for(Urls myUrl : environmentsList) {
			System.out.println(myUrl.getUrl());
		}
		System.out.println();
		
		Map<ApplicationOrCenter, String> environmentsStringMap = new ClockEnvironmentsHelper().getURLsMapBasedOnEnvironment("DEV");
		for (Map.Entry<ApplicationOrCenter, String> entry : environmentsStringMap.entrySet()) {
			System.out.println(entry.getValue());
		}
		System.out.println();
		
		ArrayList<String> clockMovingList = new ClockEnvironmentsHelper().getAllClockMovingEnvironments();
		for(String myStr : clockMovingList) {
			System.out.println(myStr);
		}
		
		
	}
	
}
