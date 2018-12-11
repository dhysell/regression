package regression.r2.noclock.policycenter.change.subgroup2;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.PolicyChangeReview;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement :US9999: [Part II] PC - OOS change future most payer
 * @Description : Issued a Squire policy, added LH(Lien Holder) with first mortgage in policy change and bound the change
 * and create second change with same effective of first one and then remove LH and bound the change, add third OOS change and add the same LH,bind the change
 * verify the OFP(override the future payer) check box available to specific users in OOS change, also verify charges came through billing center for OOS changes
 * Verify OFP check box not available for future changes
 * @DATE Mar 14, 2017
 */
@QuarantineClass
public class TestOOSchangeFutureMostPayer extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squirePolicyObj;
	private Underwriters uw;
	String companyName = "Seterus INC";

	@Test 
	public void testIssueSquirePolWithSectionOneAndTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        squirePolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)				
				.withInsFirstLastName("Guy", "POLChange")					
				.withPolicyLocations(locationsList)
				.withPolOrgType(OrganizationType.Individual)				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	} 

	@Test(dependsOnMethods = {"testIssueSquirePolWithSectionOneAndTwo"})
	private void testPolicyChangeWithAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);

		//Add additional Interest
		addAdditionalInterest();		
		propertyDetail.clickOk();

		sideMenu.clickSideMenuPayerAssignment();

		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, companyName,true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();
        policyChangePage.clickIssuePolicy();

	}

	@Test(dependsOnMethods = {"testPolicyChangeWithAdditionalInterests"})
	private void testSecondPolicyChangeWithoutAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Second policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);

		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.removeAdditionalInterest(companyName);
		propertyDetail.clickOk();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();
        policyChangePage.clickIssuePolicy();

	}


	@Test(dependsOnMethods = {"testSecondPolicyChangeWithoutAdditionalInterests"})
	private void testOOSPolicyChangeWithAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 5);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("OOS policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);

		//Add additional Interest
		addAdditionalInterest();			
		propertyDetail.clickOk();	
		sideMenu.clickSideMenuSquirePropertyCoverages();


		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, companyName, true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);	

		//Verify OFP check box available for OOS Policy change
		if(payerAssignment.verifySectionIPropertyOFPCheckboxExists())
			payerAssignment.setCheckBoxInSectionIPropertyTable(1, 1);

		sideMenu.clickSideMenuPolicyChangeReview();
		PolicyChangeReview policyChangeReview = new PolicyChangeReview(driver);
		policyChangeReview.clickChangeConflictsTab();
		policyChangeReview.clickOverrideAllButton();
		policyChangeReview.clickSubmitButton();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();		

		policyChangePage.clickIssuePolicy();

	}


	//Verify Charges from OOS Policy Change displayed in Billing Center Charges
	@Test(dependsOnMethods = {"testOOSPolicyChangeWithAdditionalInterests"})
	private void testVerifyBCCharges() throws Exception {
		ARUsers bcLogin = ARUsersHelper.getRandomARUser();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(bcLogin.getUserName(), bcLogin.getPassword(), squirePolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
		if (!charges.waitUntilChargesFromTransactionDescriptionArrive(120, "OOS policy Change")) {
			Assert.fail("The charges from the OOS policy change didn't come over after waiting for 2 minutes");
		}
	}

	//Verify OFP check box not available for future Policy change
	@Test(dependsOnMethods = {"testVerifyBCCharges"})
	private void testVerifyOFPCheckboxNotExistsForFuturePoicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


        //add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 12);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Future policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		
		sideMenu.clickSideMenuPayerAssignment();
		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);

		//Verify OFP check box not available for future Policy change
		if(payerAssignment.verifySectionIPropertyOFPCheckboxExists())
			Assert.fail("OFP check box should not exists for future date policy change.");
	}


	private void addAdditionalInterest() throws Exception {
		AddressInfo address = new AddressInfo("PO Box 961299","","Fort Worth", State.Texas, "76161-0299", CountyIdaho.Ada, "United States", AddressType.Home);
		ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(
				ContactSubType.Company);
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

		loc2Bldg1AddInterest.setCompanyName(companyName);
		loc2Bldg1AddInterest.setAddress(address);
		loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);

		SquireFPP squireFPP = new SquireFPP();
		squireFPP.setAdditionalInterests(loc2Bldg1AdditionalInterests);
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();

		SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchForContact(true, loc2Bldg1AddInterest);	

		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
		additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage();

		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
    }
}
