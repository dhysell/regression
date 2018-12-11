package regression.r2.noclock.policycenter.change.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;

/**
 * @Author nvadlamudi
 * @Requirement : US7815: PL - Umbrella policy change - policy change review, quote, bind, & issue
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Policy%20Change/6.0%20-%20Policy%20Change.pptx">Link Text</a>
 * @Description
 * @DATE Nov 2, 2016
 */
@QuarantineClass
public class TestUmbrellaPolicyChangeReviewQuoteBindIssue extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySqPolicy;

	@Test()
	public void testIssueUmbrellaPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK,true,
				UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp",
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;


		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;
		mySquire.propertyAndLiability = myPropertyAndLiability;

        mySqPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("SUSAN", "REDWINE")
				.withANIList(listOfANIs)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

		mySqPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
		mySqPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella,GeneratePolicyType.PolicyIssued);	
	}


	@Test(dependsOnMethods = {"testIssueUmbrellaPol"})
	private void testQuoteSquirePolicyChange() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber("lbarber", mySqPolicy.agentInfo.getAgentPassword(), mySqPolicy.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day , 90);

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickExpirationDateChange();
        policyChangePage.setDescription("Testing Purpose");
        policyChangePage.clickPolicyChangeNext();
        policyChangePage.setExpirationDate(changeDate);

		policyChangePage.clickPolicyChangeNext();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
			sideMenu.clickSideMenuRiskAnalysis();
			riskAnalysis.approveAll_IncludingSpecial();
			riskAnalysis.Quote();
		}

        policyChangePage.clickIssuePolicy();
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber("lbarber", mySqPolicy.agentInfo.getAgentPassword(), mySqPolicy.squire.getPolicyNumber());


		//start policy change
        StartPolicyChange policyChangePages = new StartPolicyChange(driver);
		policyChangePages.startPolicyChange("First policy Change", currentSystemDate);

		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.setPolicyInfoBillingCounty("Ada");
		policyInfo.clickPolicyInfoPrimaryNamedInsured();

		//Modifying PNI
		AddressInfo newAddress = new AddressInfo();
		newAddress.setLine1("123 N Main St");
		newAddress.setCity("Pocatello");
		newAddress.setZip("83201");
		newAddress.setState(State.Idaho);
		newAddress.setType(AddressType.Home);
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListingIfNotExist(newAddress);
        GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
        namedInsured.setReasonForContactChange("Testing purpose Added");
		householdMember.clickUpdate();


		//Adding new ANI
		PolicyInfoAdditionalNamedInsured pANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp",
				AdditionalNamedInsuredType.Friend, new AddressInfo(true));
		pANI.setNewContact(CreateNew.Create_New_Always);			
		policyInfo.addAdditionalNamedInsured(mySqPolicy.basicSearch, pANI);

		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
		for (int currentMember = 1; currentMember <= household.getPolicyHouseholdMembersTableRowCount(); currentMember++) {
			if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains(pANI.getPersonFirstName())) {
				household.clickPolicyHouseHoldTableCell(currentMember, "Name");
				break;
			} else {
				continue;
			}
		}
        GenericWorkorderPolicyMembers householdMember1 = new GenericWorkorderPolicyMembers(driver);
		householdMember1.setDateOfBirth("01/01/1980");
		householdMember1.selectNotNewAddressListingIfNotExist(pANI.getAddress());
		householdMember1.clickRelatedContactsTab();
        householdMember1.clickOK();
		household.clickNext();
        if (new GuidewireHelpers(driver).errorMessagesExist() && (new GuidewireHelpers(driver).getErrorMessages().toString().contains("Discard Unsaved Change"))) {
			new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
			sideMenu.clickSideMenuHouseholdMembers();
			for (int currentMember = 1; currentMember <= household.getPolicyHouseholdMembersTableRowCount(); currentMember++) {
				if (household.getPolicyHouseHoldMemberTableCellValue(currentMember, "Name").contains(pANI.getPersonFirstName())) {
					household.clickPolicyHouseHoldTableCell(currentMember, "Name");
					break;
				} else {
					continue;
				}
			}			
			householdMember1.setDateOfBirth("01/01/1980");
			householdMember1.selectNotNewAddressListingIfNotExist(pANI.getAddress());
			householdMember1.clickRelatedContactsTab();
            householdMember1.clickOK();
			household.clickNext();
		}		
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverageScreen = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverageScreen.setCoverageALimit(200000);
		coverageScreen.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300_500_300);

		sideMenu.clickSideMenuPACoverages();
		GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setUninsuredCoverage(true, UninsuredLimit.ThreeHundred);

		quote = new GenericWorkorderQuote(driver);
		quote.clickSaveDraftButton();
		quote.clickQuote();
    }

	@Test(dependsOnMethods = {"testQuoteSquirePolicyChange"})
	private void testUmbrellaPolicyChanges() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber("lbarber", mySqPolicy.agentInfo.getAgentPassword(), mySqPolicy.squireUmbrellaInfo.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		String errorMessage = "";
		if (! policyInfo.getPolicyInfoUmbrellaMailingAddress().contains("123 N Main St")) {
			errorMessage = errorMessage + "The Umbrella Policy Address Line 1 does not match the Squire Policy Address.\n";
		}

		// Check Expiration Date is not Editable and Matches Squire
		Date expirationDateActual = policyInfo.getPolicyInfoExpirationDate();
		if (!mySqPolicy.squireUmbrellaInfo.getExpirationDate().equals(expirationDateActual)) {
			errorMessage = errorMessage + "The Umbrella Policy Expiration Date is changed with new Squire Policy change.";
		}

		sideMenu.clickSideMenuSquireUmbrellaCoverages();
		GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);

		if(!umbCovs.getUnderlyingLimit().contains(SectionIIGeneralLiabLimit.Limit_300_500_300.getValue())){
			errorMessage = errorMessage + "The Umbrella - Unlying Policy details limit is not matched \n";
		}

		umbCovs.clicResyncWithSquire();
        if (!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains("Syncing with the Squire policy finished successfully"))) {
			errorMessage = errorMessage + "Expected page validation : 'Syncing with the Squire policy finished successfully' is not displayed. /n";
		}

		sideMenu.clickSideMenuPolicyInfo();
		Date expirationDateActualNew = policyInfo.getPolicyInfoExpirationDate();
		if (!mySqPolicy.squireUmbrellaInfo.getExpirationDate().equals(expirationDateActualNew)) {
			errorMessage = errorMessage + "The Umbrella Policy Expiration Date is changed with new Squire Policy change after sync.";
		}

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();
        if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
			sideMenu.clickSideMenuRiskAnalysis();
			riskAnalysis.approveAll_IncludingSpecial();
			riskAnalysis.Quote();
		}

        policyChangePage.clickIssuePolicy();

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		if(errorMessage != "")
			Assert.fail(errorMessage);
	}
}