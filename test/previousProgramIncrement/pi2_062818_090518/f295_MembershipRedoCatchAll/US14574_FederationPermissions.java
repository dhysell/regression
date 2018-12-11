package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Topic;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Note;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountNotesPC;
import repository.pc.account.AccountNotesPC_NewNote;
import repository.pc.account.AccountSummaryPC;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.Calendar;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Sep 20, 2018
*/
public class US14574_FederationPermissions extends BaseTest {

    GeneratePolicy myPolObj;
    WebDriver driver;

    @Test
    public void validateFederationPermissions() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy();
        new Login(driver).loginAndSearchAccountByAccountNumber("dashton", "gw", myPolObj.accountNumber);
        Note myAccountNote = new Note(Topic.UnderwritingComments, "HI THERE", "Account", "HELLO FROM FEDERATION");
        new AccountNotesPC_NewNote(driver).createNewNote(myAccountNote);
        new AccountSummaryPC(driver).clickPolicyNumber(myPolObj.squire.getPolicyNumber());
        softAssert.assertTrue(new AccountSummaryPC(driver).finds(By.xpath("//div[contains(@id, ':PolicyFileAcceleratedMenuActions-body')]/descendant::span[(@class='x-tree-node-text ')]")).size() == 2);
        Note myPolicyNote = new Note(Topic.UnderwritingComments, "HI THERE", "Policy", "HELLO FROM FEDERATION");
        new AccountNotesPC_NewNote(driver).createNewNote(myPolicyNote);
        new InfoBar(driver).clickInfoBarAccountNumber();
        new SideMenuPC(driver).clickSideMenuToolsNotes();
        softAssert.assertTrue(new AccountNotesPC(driver).containsNote(new AccountNotesPC(driver).getAccountNotes(), myAccountNote), "ACCOUNT LEVEL NOTE DID NOT GET SAVED TO ACCOUNT NOTES.");
        softAssert.assertTrue(new AccountNotesPC(driver).containsNote(new AccountNotesPC(driver).getAccountNotes(), myPolicyNote), "POLICY LEVEL NOTE DID NOT GET SAVED TO ACCOUNT NOTES.");
        softAssert.assertAll();
    }

    private void generatePolicy() throws Exception {
    	Calendar calendar = Calendar.getInstance();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth % 2 == 0) {
			generateSquireAuto();
		} else {
			generateSquireProperty();
		}
    }


    private void generateSquireAuto() throws Exception {
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .build(GeneratePolicyType.PolicyIssued);
    }

    private void generateSquireProperty() throws Exception {
        PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(Property.PropertyTypePL.ResidencePremises);
        AdditionalInterest myAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        myAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        myProperty.getAdditionalInterestList().add(myAdditionalInterest);
        ArrayList<PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
        propertyList.add(myProperty);
        PolicyLocation myLocation = new PolicyLocation(propertyList);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(myLocation);
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationList;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;


        myPolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withSquire(mySquire)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .build(GeneratePolicyType.PolicyIssued);
    }
}
