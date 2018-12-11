package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;

import java.util.ArrayList;
import java.util.Date;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Sep 20, 2018
*/
public class US14355_OOSChangePayerAssignment extends BaseTest {

	GeneratePolicy mySqPolObj;

	@Test
	public void oosChangePayerAssignment() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -5);
		
		PLPolicyLocationProperty myProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
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
		

		mySqPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				.withPolEffectiveDate(newEff)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(mySqPolObj);
		new StartPolicyChange(driver).startPolicyChange("Changing something", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 5));
		mySqPolObj.squire.propertyAndLiability.locationList.get(0).setPlNumAcres(3);
		new GenericWorkorderSquirePropertyAndLiabilityLocation(driver).updateExistingLocation(mySqPolObj.squire.propertyAndLiability.locationList.get(0));
		new StartPolicyChange(driver).quoteAndIssue();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new StartPolicyChange(driver).startPolicyChange("Checking payer assignment membership uneditable", null);
		new SideMenuPC(driver).clickSideMenuPayerAssignment();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertFalse(new BasePage(driver).finds(By.xpath("//label[contains(text(), 'Unable to edit payer on membership dues in an out-of-sequence job. To change the payer, please create an in-sequence policy change.')]")).isEmpty(), 
				"MESSAGES INDICATING USER IS UNABLE TO MAKE A CHANGE TO MEMBERSHIP PAYER ASSIGNMENT ON AN OOS CHANGE");
		boolean uneditable = new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@class, 'g-title') and contains(text(), 'Membership Dues')]/ancestor::tr[1]/following-sibling::tr/descendant::div[contains(text(), \"" + myAdditionalInterest.getCompanyName() + "\")]/parent::td[not(contains(@class, 'g-cell-edit'))]")).isEmpty();
		softAssert.assertTrue(uneditable, "FIELD TO CHANGE MEMBERSHIP PAYER ON OOS POLICY CHANGE WAS EDITABLE WHEN IT SHOULD NOT BE.");
		softAssert.assertAll();
	}
}
