package scratchpad.steve;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.administration.RolesPage;
import repository.driverConfiguration.Config;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

public class AbAdmin extends BaseTest {
    private WebDriver driver;
    ArrayList<String> allRoles = new ArrayList<>();
    
    private void populateAllRoles() {
		/*
		this.allRoles.add("Accounting Data Manager");
		this.allRoles.add("Agent Data Manager");
		this.allRoles.add("Claim Data Manager");
		this.allRoles.add("Client Application");
		this.allRoles.add("Contact Subtype Changer");
		this.allRoles.add("Data Change - Execute");
		this.allRoles.add("Data Change - Register");
		this.allRoles.add("Edit Any Contact");
		this.allRoles.add("Federation Data Manager");
		this.allRoles.add("Guidewire Service Representative");
		this.allRoles.add("IS Production Support");
		this.allRoles.add("IS Programmers");
		this.allRoles.add("Policy Data Manager");
		this.allRoles.add("Sales Data Manager");
		this.allRoles.add("SOAP Administrator");
		
*/		this.allRoles.add("Superuser");
		this.allRoles.add("Tax Controller");
		this.allRoles.add("Tax Controller Backup");
		this.allRoles.add("Tools View");
		this.allRoles.add("Training");
		this.allRoles.add("Transition Team");
		this.allRoles.add("Underwriting Data Manager");
		this.allRoles.add("User Admin");
		this.allRoles.add("User Admin View");
		this.allRoles.add("Viewer");
	}
	
	
    @Test(enabled = true)
	public void updateRoles() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
//		populateAllRoles();
		RolesPage rolesPage = new RolesPage(driver);
//		for(String role : this.allRoles) {
			rolesPage.updateRole("User Admin View");
			rolesPage.updateRole("Viewer");
//		}
	}
}
