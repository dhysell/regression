package regression.r2.noclock.policycenter.change.subgroup7;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

/**
 * @Author nvadlamudi
 * @Requirement :DE4378 - Payer is not correct
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 14, 2016
 */
public class TestSquireSectionOneAndTwoPayerDetails extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;

	@Test()
	public void testIssueSquirePolWithSectionOneAndTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);	

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

		SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.CirclePivots);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.squireFPP = squireFPP;


		Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
//				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Guy", "POLChange")					
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = {"testIssueSquirePolWithSectionOneAndTwo"})
	private void testPolicyChangeWithAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.squire.getPolicyNumber());

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

		Contact person = new Contact("Test"+StringsUtils.generateRandomNumberDigits(6), "AdditionalInt", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));

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
		additionalInterests.setPropertyAdditionalInterestAddressListing("New...");
		additionalInterests.setContactEditAddressLine1(address.getLine1());
		additionalInterests.setContactEditAddressCity(address.getCity());
		additionalInterests.setContactEditAddressState(address.getState());
		additionalInterests.setContactEditAddressZipCode(address.getZip());
		additionalInterests.setContactEditAddressAddressType(AddressType.Home);
        additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);

		additionalInterests.clickRelatedContactsTab();

		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

        propertyDetail.clickOk();


		sideMenu.clickSideMenuPayerAssignment();

		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, person.getLastName(), true, false);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);


		GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();
        quote.clickExpandedViewLink();
        String errorMessage = "";
		if(!quote.checkSectionICoveragePremiumPayerByName("Coverage A", person.getLastName())){
			errorMessage = errorMessage + "Coverage A Payer is not displayed with Additional Interest. \n";
		}

		if(!quote.checkSectionICoveragePremiumPayerByName("Coverage C", person.getLastName())){
			errorMessage = errorMessage + "Coverage C Payer is not displayed with Additional Interest. \n";
		}

		if(!quote.checkSectionICoveragePremiumPayerByName("Other Structures", person.getLastName())){
			errorMessage = errorMessage + "Other Structures Payer is not displayed with Additional Interest. \n";
		}

		if(!quote.checkSectionICoveragePremiumPayerByName("Sewage System Backup", person.getLastName())){
			errorMessage = errorMessage + "Sewage System Backup Payer is not displayed with Additional Interest. \n";
		}

		if(!quote.checkSectionICoveragePremiumPayerByName("Coverage B Loss Of Use", person.getLastName())){
			errorMessage = errorMessage + "Coverage B Loss Of Use Payer is not displayed with Additional Interest. \n";
		}

		if(!quote.checkSectionICoveragePremiumPayerByName("Refrigerated Products", person.getLastName())){
			errorMessage = errorMessage + "Refrigerated Products Payer is not displayed with Additional Interest. \n";
		}

		if(errorMessage != ""){
			Assert.fail(errorMessage);
		}

	}

}
