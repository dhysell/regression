package repository.gw.helpers;

import com.idfbins.hibernate.qa.guidewire.environments.Urls;
import gwclockhelpers.ApplicationOrCenter;
import gwclockhelpers.ClockEnvironmentsHelper;
import gwclockhelpers.DebugToolsAPIHelper;
import org.openqa.selenium.WebDriver;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;

import java.util.*;

public class ClockUtils {

	private static List<Urls> environmentsPrep(WebDriver driver) throws Exception {		
		String environment = new GuidewireHelpers(driver).getEnvironment();
		
		return new ClockEnvironmentsHelper().getUrlsListBasedOnEnvironment(environment);
	}
	
	private static List<Urls> environmentsPrep(Config webDriverConfiguration) throws Exception {		
		String environment = webDriverConfiguration.getEnv();
		
		return new ClockEnvironmentsHelper().getUrlsListBasedOnEnvironment(environment);
	}
	
	private static boolean validateAbilityToMoveClock(List<Urls> environmentsList) {
		boolean canMoveClock = true;
		for(Urls environment : environmentsList){
			if (!environment.getEnvs().isClockMove()) {
				canMoveClock = false;
				break;
			}
		}
		return canMoveClock;
	}
	
	public static Map<ApplicationOrCenter, Date> getCurrentDates(WebDriver driver) throws Exception {
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(driver);
		for(Urls environment : environmentsList){
			String applicationString = environment.getUrlTypes().getName();
			ApplicationOrCenter applicationOrCenterEnum = ApplicationOrCenter.getApplicationOrCenterFromStrValue(applicationString);
			environmentsURLs.put(applicationOrCenterEnum, environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		return clock.getCurrentDates();
	}
	
	public static Map<ApplicationOrCenter, Date> getCurrentDates(Config webDriverConfiguration) throws Exception {
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(webDriverConfiguration);
		for(Urls environment : environmentsList){
			String serverStr = environment.getUrlTypes().getName();
			ApplicationOrCenter appToInput = ApplicationOrCenter.getApplicationOrCenterFromStrValue(serverStr);
			environmentsURLs.put(appToInput, environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		return clock.getCurrentDates();
	}
	
	public static Map<ApplicationOrCenter, Date> getCurrentDates(Map<ApplicationOrCenter, String> environmentsHashMap) throws Exception {
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsHashMap);
		
		return clock.getCurrentDates();
	}
	
	public static void setCurrentDates(WebDriver driver, Date dateToSet) throws Exception {	
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(driver);
		if (!validateAbilityToMoveClock(environmentsList)) {
			throw new Exception("The server in which you are trying to move the clock has its clock move flag set to false. Clock moves cannot be made in this server until the flag in the database is set to true.");
		}
		
		for(Urls environment : environmentsList){
			environmentsURLs.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue(environment.getUrlTypes().getName()), environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		String compName = getComputerName();
		clock.setCurrentDates(dateToSet, "REGRESSION TEST AT " + compName);
	}
	
	public static void setCurrentDates(Config webDriverConfiguration, Date dateToSet) throws Exception {	
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(webDriverConfiguration);
		if (!validateAbilityToMoveClock(environmentsList)) {
			throw new Exception("The server in which you are trying to move the clock has its clock move flag set to false. Clock moves cannot be made in this server until the flag in the database is set to true.");
		}
		
		for(Urls environment : environmentsList){
			environmentsURLs.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue(environment.getUrlTypes().getName()), environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		String compName = getComputerName();
		clock.setCurrentDates(dateToSet, "REGRESSION TEST AT " + compName);
	}
	
	public static void setCurrentDates(WebDriver driver, repository.gw.enums.DateAddSubtractOptions changeFormat, int amountToChange) throws Exception {
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(driver);
		if (!validateAbilityToMoveClock(environmentsList)) {
			throw new Exception("The server (" + environmentsList.get(0).getEnvs().getName() + ") in which you are trying to move the clock has its clock move flag set to false. Clock moves cannot be made in this server until the flag in the database is set to true.");
		}
		
		for(Urls environment : environmentsList){
			environmentsURLs.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue(environment.getUrlTypes().getName()), environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		Date newDate = repository.gw.helpers.DateUtils.dateAddSubtract(getCurrentDates(environmentsURLs).get(ApplicationOrCenter.PolicyCenter), changeFormat, amountToChange);
		
		String compName = getComputerName();
		clock.setCurrentDates(newDate, "REGRESSION TEST AT " + compName);
	}
	
	public static void setCurrentDates(Config webDriverConfiguration, DateAddSubtractOptions changeFormat, int amountToChange) throws Exception {
		Map<ApplicationOrCenter, String> environmentsURLs = new HashMap<ApplicationOrCenter, String>();
		
		List<Urls> environmentsList = environmentsPrep(webDriverConfiguration);
		if (!validateAbilityToMoveClock(environmentsList)) {
			throw new Exception("The server (" + environmentsList.get(0).getEnvs().getName() + ") in which you are trying to move the clock has its clock move flag set to false. Clock moves cannot be made in this server until the flag in the database is set to true.");
		}
		
		for(Urls environment : environmentsList){
			environmentsURLs.put(ApplicationOrCenter.getApplicationOrCenterFromStrValue(environment.getUrlTypes().getName()), environment.getUrl());
		}
		
		DebugToolsAPIHelper clock = new DebugToolsAPIHelper(environmentsURLs);
		
		Date newDate = DateUtils.dateAddSubtract(getCurrentDates(environmentsURLs).get(ApplicationOrCenter.PolicyCenter), changeFormat, amountToChange);
		
		String compName = getComputerName();
		clock.setCurrentDates(newDate, "REGRESSION TEST AT " + compName);
	}
	
	public static ArrayList<String> getAllClockMovingEnvironments() throws Exception {
		return new ClockEnvironmentsHelper().getAllClockMovingEnvironments();
	}
	
	private static String getComputerName() {
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME") + ".idfbins.com";
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME") + ".idfbins.com";
	    else
	        return "UNKNOWN.idfbins.com";
	}
}
