package scratchpad.steve.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.administration.AdminMenu;
import repository.ab.administration.NewUser;
import repository.ab.administration.UsersPage;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.PhoneType;
import repository.gw.generate.ab.UserProfile;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class Users extends BaseTest {
    private String environment = "UAT";
    private UserProfile guidewireServiceRep;
    private AbUsers abUser = null;
    private WebDriver driver;

    @Test(enabled = true)
    public void userInput() throws Exception {
    	 ArrayList<String> environments = new ArrayList<String>();
    	 environments.add("UAT");
 //   	 environments.add("DEV");
//    	 environments.add("IT");
//    	 environments.add("QA");
    	 
    	 for(String environment : environments) {
    		 this.environment = environment;
    		 setUserProfiles();
    	 }
    }
    
    public void setUserProfiles() throws Exception {
        ArrayList<String> environments = new ArrayList<String>();
    	
    	//UserProfile:
        String firstName = "Michelle";
        String lastName = "Roberts";
        String userName = "mroberts";
        String password = "gw";
        boolean activeDirectory = false;
        boolean active = true;
        ArrayList<String> roles = new ArrayList<>();
        roles.add("Transition Team");

        ArrayList<String> group = new ArrayList<>();
        group.add("Underwriting - Personal");
        String jobTitle = "Transition Specialist";
        String department = "UW/Personal";
        AddressInfo address = new AddressInfo("275 Tierra Vista Dr.", "Pocatello", State.Idaho, "83201");
        String email = userName + "@idfbins.com";
        Map<String, PhoneType> phoneNumbers = new HashMap<>();
        phoneNumbers.put("(208) 232-7915", PhoneType.Work);
        this.guidewireServiceRep = new UserProfile(firstName, lastName, userName, password, activeDirectory, active, roles, group, jobTitle, department, address, email, phoneNumbers);
		
		
		/*//Second user to input
		firstName = "Steve";
		lastName = "Broderick";
		userName = "sbroderick";
		password = "gw";
		activeDirectory = false;
		active = true;
		roles = new ArrayList<>();
		roles.add("Viewer");
		
		group = new ArrayList<>(); 
		group.add("IS Programmers");
		jobTitle = "Guidewire Service Representative"; 
		department = "UW/Personal";
		address = new AddressInfo("275 Tierra Vista Dr.", "Pocatello", State.Idaho, "83201"); 
		email = "sbroderick@idfbins.com";  Ap
		phoneNumbers = new HashMap<>();
		phoneNumbers.put("2082394369" ,PhoneType.Work);										
		UserProfile guidewireServiceRep2 = new UserProfile(firstName, lastName, userName, password, activeDirectory, active, roles, group, jobTitle, department, address, "sbroderick@idfbins.com", phoneNumbers);
		users.add(guidewireServiceRep2);*/
        searchUser(guidewireServiceRep);
    }

    public void searchUser(UserProfile guidewireServiceRep) throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager, this.environment);
        driver = buildDriver(cf);
        this.abUser = AbUserHelper.getUserByUserName("sbroderick");
        Login logMeIn = new Login(driver);
        logMeIn.login(this.abUser.getUserName(), this.abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickAdministrationTab();
        AdminMenu adminPage = new AdminMenu(driver);
        adminPage.getUsersPage();
        UsersPage userPage = new UsersPage(driver);
        if (userPage.getSearchResultsUserNames(guidewireServiceRep.getUserName()).size() < 1) {
            createUser(guidewireServiceRep);
        } else {
            System.out.println(guidewireServiceRep.getFirstName() + " " + guidewireServiceRep.getLastName() + " is already created.");
        }
    }
//	}

    public void createUser(UserProfile user) {
        SidebarAB sideMenu = new SidebarAB(driver);
        sideMenu.initiateNewUser();
        NewUser newUser = new NewUser(driver);
        newUser.inputNewUser(user);
    }
}
