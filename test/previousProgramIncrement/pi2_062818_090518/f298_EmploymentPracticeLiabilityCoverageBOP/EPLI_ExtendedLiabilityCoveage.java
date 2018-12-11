package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import scratchpad.evan.SideMenuPC;

import java.util.List;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description VERIFY EPLI EXTENDED COVERAGE IS AVAILABLE AFTER CANELATION
* @DATE Sep 20, 2018
*/
@Test(groups= {"ClockMove"})
public class EPLI_ExtendedLiabilityCoveage extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void extendedLiabilityCoveage() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		if(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter).before(DateUtils.convertStringtoDate("10/30/2018", "MM/dd/yyyy"))) {
	        ClockUtils.setCurrentDates(driver, DateUtils.convertStringtoDate("10/30/2018", "MM/dd/yyyy"));
	    }

		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();
		boLine.locationList.add(new PolicyLocation());
		boLine.locationList.get(0).getBuildingList().add(new PolicyLocationBuilding());
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand);
		boLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_deductible(EmploymentPracticesLiabilityInsurance_Deductible.TenThousand);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAdvancedSearch()
				.withBusinessownersLine(boLine)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -25))
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		myPolicyObj.cancelPolicy(CancellationSourceReasonExplanation.Photos, "NIENIERNIER YOU DON'T GOT NO POLICY :)", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new StartPolicyChange(driver).startPolicyChange("ADDING EXTENDED LIABILITY", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1));
		new SideMenuPC(driver).clickSideMenuBusinessownersLine();
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).clickBusinessownersLine_AdditionalCoverages();
		Assert.assertTrue(new GuidewireHelpers(driver).isElectable("Supplemental Extended Reporting Period Endorsement IDCW 31 0002"), "Supplemental Extended Reporting Period Endorsement IDCW 31 0002 WAS NOT AVAILABLE VIA POLICY CHANGE ON A CANCELED POLICY");
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).checkSupplementalExtendedReportingPeriodEndorsement(true);
		new StartPolicyChange(driver).clickGenericWorkorderQuote();
		new SideMenuPC(driver).clickSideMenuForms();
		List<String> cancelForms = new GenericWorkorderForms(driver).getFormDescriptionsFromTable();
		softAssert.assertTrue(cancelForms.contains("Supplemental Extended Reporting Period Endorsement"), "Supplemental Extended Reporting Period Endorsement | DID NOT GENERATE WHEN ADDED TO A CANCELED POLICY");
		//16108
		softAssert.assertFalse(cancelForms.contains("Businessowners Policy Declarations"), "Businessowners Policy Declarations | GENERATED INCORRECTLY WHEN Supplemental Extended Reporting Period Endorsement WAS ADDED TO A CANCELED POLICY");
		softAssert.assertFalse(cancelForms.contains("Change on Cancelled Policy"), "Change on Cancelled Policy | GENERATED INCORRECTLY WHEN Supplemental Extended Reporting Period Endorsement ADDED TO A CANCELED POLICY");
		new SideMenuPC(driver).clickSideMenuQuote();
		String supplementalPremium = new GenericWorkorderQuote(driver).getDescriptionPremium("Supplemental Extended Reporting Period Endorsement IDCW 31 0002").replace("$", "");
		new StartPolicyChange(driver).clickIssuePolicy();
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.BillingCenter));
		ARUsers arUser = ARUsersHelper.getRandomARUser();
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		new BCAccountMenu(driver).clickBCMenuCharges();
		boolean bcCharges = new AccountCharges(driver).verifyCharges(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), ChargeCategory.Premium, new GuidewireHelpers(driver).getPolicyNumber(myPolicyObj), Double.valueOf(supplementalPremium),null,null,null,null,null);
		softAssert.assertTrue(bcCharges, "SUPPLEMENTAL PREMIUM CHARGE DID NOT MAKE IT TO BILLING CENTER :(");
		softAssert.assertAll();
		
	}
}
