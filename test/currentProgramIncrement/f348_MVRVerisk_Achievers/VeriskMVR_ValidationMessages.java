package currentProgramIncrement.f348_MVRVerisk_Achievers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.ValidationRules_SectionIII;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import persistence.globaldatarepo.entities.VeriskMvr;
import persistence.globaldatarepo.helpers.Verisk_MVRHelpers;

public class VeriskMVR_ValidationMessages extends BaseTest {
	
	GeneratePolicy myPolicyObject;
	WebDriver driver;
    
	@Test
	public void verifyValidationMessges() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);
	    
	    List<ValidationRules_SectionIII> validations = new ArrayList<ValidationRules_SectionIII>();
	    validations.add(ValidationRules_SectionIII.AU020SRPHigherThan25);
	    validations.add(ValidationRules_SectionIII.AU068UnassignedMVRIncident);
		
	    SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        
        try {
        	//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			myPolicyObject = new GeneratePolicy.Builder(driver)
			        .withSquire(mySquire)
			        .withProductType(ProductLineType.Squire)
			        .withLineSelection(LineSelection.PersonalAutoLinePL)
			        .isDraft()
			        .build(GeneratePolicyType.FullApp);
			
			new Login(driver).loginAndSearchSubmission(myPolicyObject);
			
			for(ValidationRules_SectionIII rule : validations) {
				validateMessage(rule);
				
				
				
			}
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			//switch Verisk Score enabled back to false
			try {
				new GuidewireHelpers(driver).logout();
			} catch(Exception e) {
				//Already logged out.
			}
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskMVREnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}
	}
	
	
	private void validateMessage(ValidationRules_SectionIII rule) throws Exception {
		
		switch(rule) {
		case AU020SRPHigherThan25:
			Contact driverHighSRP = new Contact(Verisk_MVRHelpers.getVeriskTestCase("AB112101B"));
			for(VeriskMvr incident : driverHighSRP.getVeriskMVRReport()) {
				incident.setAssignedSRPIncident(SRPIncident.DrivingWithoutLicense.getValue());
			}
			myPolicyObject.squire.policyMembers.add(driverHighSRP);
			myPolicyObject.squire.squirePA.addToDriversList(driverHighSRP);
			new GenericWorkorderPolicyMembers(driver).addPolicyMember(driverHighSRP, null);
			try {
				new GenericWorkorderSquireAutoDrivers(driver).addNewDriver(driverHighSRP);
			} catch (GuidewireNavigationException e) {
				System.out.println(e);
			}
			break;
		case AU068UnassignedMVRIncident:
			Contact driverUnassignedIncidents = new Contact(Verisk_MVRHelpers.getVeriskTestCase("AB112101B"));
			break;
		}
		
		
		
		
	}
	
	
	
	

}











