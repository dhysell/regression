package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.testng.helpers.DateAddSubtractOptions;
import com.idfbins.testng.helpers.DateUtils;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;


/**
* @Author sbroderick
* @Requirement When the PNI and ANI are related as spouse, then the marital status should populate on section 3
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B123B05E9-D5AB-44B6-9DCB-5CEB3498F6FF%7D&file=US16289%20-%20CM%20-%20When%20relating%20a%20PNI%20and%20ANI%20populate%20marital%20status%20for%20both%20contacts%20on%20section%20III.docx&action=default&mobileredirect=true">Related Contacts Carries to Section III</a>
* @Description 
* @DATE Oct 2, 2018
*/
public class US16289_PNIandANISpouseMaritalStatuswillPopulateOnSection3 extends BaseTest{


	@Test
	public void maritalStatusCarryOver() throws Exception {
		Contact pniContact = new Contact("Jake", "Zaugg", Gender.Male, DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -21));
		pniContact.setContactIsPNI(true);
		pniContact.setAddress(new AddressInfo(true));
		pniContact.setTaxIDNumber("989898989");
		ProductLineType policyType = ProductLineType.Squire;
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person);
		ani.setRelationshipToPNI(AdditionalNamedInsuredType.Spouse);
		ani.setRelatedContact(pniContact);
		ani.setRelationship(RelationshipsAB.Spouse);
		ani.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		List<String> pniNames = new SubmissionNewSubmission(driver).fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, pniContact.getLastName(), pniContact.getFirstName(), null, null, pniContact.getAddress().getCity(), pniContact.getAddress().getState(), pniContact.getAddress().getZip());
		pniContact.setLastName(pniNames.get(0));
		SubmissionCreateAccount createAcctPage = new SubmissionCreateAccount(driver);
		createAcctPage.createNewContact(pniContact);
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		String accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, policyType);
		System.out.println("Account Number = " + accountNumber);
		GenericWorkorderSquireEligibility squireEligibility = new GenericWorkorderSquireEligibility(driver);
		squireEligibility.clickFarmRevenue(false);
		SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuLineSelection();
		
		GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(true);
		lineSelection.checkPersonalPropertyLine(false);
		
		sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		policyInfoPage.addAdditionalNamedInsured(true, ani);
		policyInfoPage.setPolicyInfoRatingCounty("Bingham");
		
		sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuPADrivers();
		
		GenericWorkorderSquireAutoDrivers driversPage = new GenericWorkorderSquireAutoDrivers(driver);
		driversPage.addExistingDriver(pniContact.getLastName());
		GenericWorkorderSquireAutoDrivers_ContactDetail driverDetails = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		Assert.assertTrue(driverDetails.getSelectedMaritalStatus().equals(MaritalStatus.Married), "The drivers screen for the PNI should default to married.");
		driverDetails.clickCancel();
		driversPage = new GenericWorkorderSquireAutoDrivers(driver);
		driversPage.addExistingDriver(ani.getPersonLastName());
		driverDetails = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		Assert.assertTrue(driverDetails.getSelectedMaritalStatus().equals(MaritalStatus.Married), "The drivers screen for the ANI should default to married.");
    }
	
}
