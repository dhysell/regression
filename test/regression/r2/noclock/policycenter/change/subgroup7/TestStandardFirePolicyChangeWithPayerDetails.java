package regression.r2.noclock.policycenter.change.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

/**
 * @Author nvadlamudi
 * @Requirement :DE4236: Policy not displaying earned premium on policy premium
 * for standard Fire
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 21, 2016
 */
@QuarantineClass
public class TestStandardFirePolicyChangeWithPayerDetails extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj, anotherSFirePolObj;

	@Test()
	public void testGenerateStandardLiabilityIssuance() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        myPolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("STDFire", "PayerAss").withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testGenerateStandardLiabilityIssuance" })
	private void testPolicyChangeWithAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
				myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 2);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);

		Contact person = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "AdditionalInt", Gender.Male,
				DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

		loc2Bldg1AddInterest.setPersonFirstName(person.getFirstName());
		loc2Bldg1AddInterest.setPersonLastName(person.getLastName());
		loc2Bldg1AddInterest.setAddress(new AddressInfo());

		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
		SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchForContact(myPolicyObj.basicSearch, loc2Bldg1AddInterest);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
        AddressInfo address = new AddressInfo();
		additionalInterests.setPropertyAdditionalInterestAddressListing("New");
		additionalInterests.setContactEditAddressLine1(address.getLine1());
		additionalInterests.setContactEditAddressCity(address.getCity());
		additionalInterests.setContactEditAddressState(address.getState());
		additionalInterests.setContactEditAddressZipCode(address.getZip());
		additionalInterests.setContactEditAddressAddressType(AddressType.Home);
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);

		additionalInterests.clickRelatedContactsTab();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        propertyDetail.clickOk();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(400000);
        coverages.setCoverageCLimit(200000);
        sideMenu.clickSideMenuPayerAssignment();

		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, person.getLastName(), true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();

		String errorMessage = "";
		if (!quote.checkSectionICoveragePremiumPayerByName("Coverage A", person.getLastName())) {
			errorMessage = errorMessage + "Coverage A Payer is not displayed with Additional Interest. \n";
		}

		if (!quote.checkSectionICoveragePremiumPayerByName("Coverage C", person.getLastName())) {
			errorMessage = errorMessage + "Coverage C Payer is not displayed with Additional Interest. \n";
		}

		if (!quote.checkSectionICoveragePremiumPayerByName("Limited Fungi, Wet or Dry Rot, or Bacteria Coverage",
				person.getLastName())) {
			errorMessage = errorMessage
					+ "Limited Fungi, Wet or Dry Rot, or Bacteria Coverage Payer is not displayed with Additional Interest. \n";
		}

		if (!quote.checkPropertyCoverageDetailsByColumnNameValue("Coverage A", "Eff Date",
				DateUtils.dateFormatAsString("MM/dd/yyyy", changeDate))) {
			errorMessage = errorMessage + "Expected change date for Coverage A is not displayed.\n";
		}

		if (!quote.checkPropertyCoverageDetailsByColumnNameValue("Coverage C", "Eff Date",
				DateUtils.dateFormatAsString("MM/dd/yyyy", changeDate))) {
			errorMessage = errorMessage + "Expected change date for Coverage C is not displayed.\n";
		}

		if (errorMessage != "") {
			Assert.fail(errorMessage);
		}
	}

	@Test
	private void testStandardFireWithPayer() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");

		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		property.setBuildingAdditionalInterest(loc1LNBldg1AddInterest);
		locOnePropertyList.add(property);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumDwellingPremises);
		locOnePropertyList.add(property1);
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        anotherSFirePolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsFirstLastName("STDFire", "PayerIn")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"testStandardFireWithPayer"})
	private void testPolicyChangeRemovingPayer() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(anotherSFirePolObj.agentInfo.getAgentUserName(),
                anotherSFirePolObj.agentInfo.getAgentPassword(), anotherSFirePolObj.standardFire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayerAssignment();

		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1,"Insured", true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
		payerAssignment.setPayerAssignmentBillMembershipDues(anotherSFirePolObj.pniContact.getLastName(), true, false, "Insured");
		
		
		if(!payerAssignment.getPayerAssignmentBillMembershipDues(anotherSFirePolObj.pniContact.getLastName(), true, false).contains("Insured")){
			Assert.fail("Expected Membership dues is not displayed. ");
		}
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();

		sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
		change.clickSubmitPolicyChange();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();
		
	}
}
