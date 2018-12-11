package regression.r2.noclock.policycenter.change.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement :US9668: PL - Changes to Cov D Commodities and Live stock, FPP Additional Interest, and Section IV Farm Equipment Additional Interest
 * @Description : Add FPP Additional interest for Section I&II Property coverages and user can able that under Farm Equipment Section IV,
 * also validate UW issues will trigger if we made any changes under policy change
 * @DATE Dec 06, 2016
 */
@QuarantineClass
public class TestFPPAdditionalInterestInSectionOneAndTwoSectionFour extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL = null;	
	private Underwriters uw;

	@Test ()
	public void testGenerateSquireFullApp() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);		

		SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Cows, FPPFarmPersonalPropertySubTypes.Tractors,FPPFarmPersonalPropertySubTypes.Seed);		
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);	

		// Livestock
		Livestock livestock = new Livestock();
		livestock.setType(LivestockType.Livestock);
		livestock.setDeductible(LivestockDeductible.Ded100);	

		LivestockScheduledItem livSchedItem2 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Bull, "Test Alpaca 1", "TAG123", "Brand1", "Breed1", 1000);
		ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();

		livSchedItems.add(livSchedItem2);
		livestock.setScheduledItems(livSchedItems);
		LivestockList allLivestock = new LivestockList(livestock, null, null);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);
		imTypes.add(InlandMarine.Livestock);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100000_CSL);

		SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
		livestockSectionIICowCoverage.setQuantity(100);
		livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Cow);	

		SquireLiablityCoverageLivestockItem livestockSectionIIHorseCoverage = new SquireLiablityCoverageLivestockItem();

		livestockSectionIIHorseCoverage.setType(LivestockScheduledItemType.Horse);
		livestockSectionIIHorseCoverage.setQuantity(100);

		ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
		coveredLivestockItems.add(livestockSectionIICowCoverage);
		
		SectionIICoverages livestockcoverages = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

		myLiab.getSectionIICoverageList().add(livestockcoverages);

		SquirePropertyAndLiability property = new SquirePropertyAndLiability();
		property.locationList = locationsList;
		property.liabilitySection = myLiab;
		
		
		
		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = property;
		mySquire.inlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
		mySquire.propertyAndLiability.squireFPP = squireFPP;
		mySquire.inlandMarine.farmEquipment = allFarmEquip;
		mySquire.inlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
				.withInsFirstLastName("SQ", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);		
	}	

	public GeneratePolicy getFppPolicyObj(){
		return this.mySQPolicyObjPL;
	}

	@Test (dependsOnMethods = {"testGenerateSquireFullApp"})
	public void verifyAdditionalInterestScenarios() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(mySQPolicyObjPL.agentInfo.getAgentUserName(), mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();

		//Acceptance Criteria 2 : User will not be required to add scheduled item if an additional interest is added to FPP

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickFarmPersonalProperty();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
		fppCovs.clickFPPAdditionalInterests();

		AddressInfo bankAddress = new AddressInfo();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("AILoc2Bldg1", "Person", bankAddress);
		loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc2Bldg1AddInterest.setAddress(bankAddress);		
		loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(driver);
		search.searchForContact(mySQPolicyObjPL.basicSearch, loc2Bldg1AddInterest);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
		additionalInterests.setAddressListing("New...");
        additionalInterests.setContactEditAddressLine1(loc2Bldg1AddInterest.getAddress().getLine1());
		additionalInterests.setContactEditAddressCity(loc2Bldg1AddInterest.getAddress().getCity());
        additionalInterests.setContactEditAddressState(loc2Bldg1AddInterest.getAddress().getState());
        additionalInterests.setContactEditAddressZipCode(loc2Bldg1AddInterest.getAddress().getZip());
		additionalInterests.setContactEditAddressAddressType(loc2Bldg1AddInterest.getAddress().getType());
		additionalInterests.setAdditionalInterestsLoanNumber(loc2Bldg1AddInterest.getLoanContractNumber());
        additionalInterests.clickRelatedContactsTab();
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();

		coverages.clickNext();

        ErrorHandlingHelpers error = new ErrorHandlingHelpers(driver);
		if(!error.areThereErrorMessages()) 
			Assert.fail("User will not be required to add scheduled item if an additional interest is added to FPP");

		//Acceptance Criteria 3 :User will be required to add additional interest if scheduled item is created in FPP Additional Interest tab		
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickFarmPersonalProperty();
		fppCovs.clickFPPAdditionalInterests();

        GenericWorkorderSquireFPPAdditionalInterest fppAddInterest = new GenericWorkorderSquireFPPAdditionalInterest(driver);

		fppAddInterest.clickAdd(FarmPersonalPropertyTypes.Machinery);
		fppAddInterest.selectItemType(FPPFarmPersonalPropertySubTypes.Tractors);
        fppAddInterest.setDescriptionValueSerialNum(FPPFarmPersonalPropertySubTypes.Tractors, 1, "Testing Purpose", 100, "13324435435");
		coverages.clickNext();

		if(!error.areThereErrorMessages()) 
			Assert.fail("User should not required to add scheduled item if an additional interest is added to FPP");			
		fppAddInterest.addAdditionalInterest(FarmPersonalPropertyTypes.Machinery,loc2Bldg1AddInterest);

		coverages.clickNext();		

		//Acceptance Criteria 4 :User will be able to add additional interest on the Section IV Farm Equipment 
		sideMenu.clickSideMenuIMFarmEquipment();
        GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		farmEquipment.clickEditButton();

		additionalInterests.addExistingAdditionalInterest(loc2Bldg1AddInterest.getPersonFirstName());
		additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lessor");
		additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();


		String fppAdditionalInterestName = loc2Bldg1AddInterest.getPersonFirstName()+" "+loc2Bldg1AddInterest.getPersonLastName();
		String farmEquipmentAdditionalInterestName = farmEquipment.getFarmEquipmentAdditionalInterestValues(1, "Name");

		if(!fppAdditionalInterestName.equals(farmEquipmentAdditionalInterestName))
			Assert.fail("User will be able to add additional interest on the Section IV Farm Equipment");

		farmEquipment.clickOk();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);

		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
		paymentPage.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);

		sideMenu.clickSideMenuRiskAnalysis();

		GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		riskAnaysis.handleBlockSubmit(mySQPolicyObjPL);
		SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuQuote();
		paymentPage.SubmitOnly();

		new GuidewireHelpers(driver).logout();

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickCloseWorkSheet();

		SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyDetail();

		sidemenu.clickSideMenuSquirePropertyCoverages();

		coverages.clickFarmPersonalProperty();
		coverages.setFarmPersonalPropertyRisk("A");
		sidemenu.clickSideMenuQualification();

		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        sidemenu.clickSideMenuQuote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		quotePage.issuePolicy(IssuanceType.NoActionRequired);
		new GuidewireHelpers(driver).logout();
	}


	@Test(dependsOnMethods = { "verifyAdditionalInterestScenarios" })
	public void validaePolicyChangeUWActivities() throws Exception{

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber );


		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();

		//Acceptance Criteria 1 :User will be able to add/edit/remove Livestock and Commodities coverage and the scheduled item on Policy Change and Renewal  
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickFarmPersonalProperty();
		//Add live stock
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
		fppCovs.addItem(FPPFarmPersonalPropertySubTypes.Horses, 1, 100, "Testing Purpose");		

		//Remove Livestock
		fppCovs.removeItem(FarmPersonalPropertyTypes.Livestock, 2);

        //Edit Livestock
		fppCovs.setValue(FarmPersonalPropertyTypes.Livestock, 1, "Value", "Value", "20");	

		fppCovs.unselectCoverages(FarmPersonalPropertyTypes.Commodities);		
		coverages.clickNext();
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();		
		sideMenu.clickSideMenuRiskAnalysis();

		//Acceptance Criteria 7 :UW issue will trigger if Livestock or Commodities on FPP is changed/removed 
		//validate UW block quote

		FullUnderWriterIssues allUWIssues = riskAnalysis.getUnderwriterIssues();
		if(allUWIssues.isInList("Removing or Change to Commodities").equals(UnderwriterIssueType.NONE)) {
			Assert.fail("Expected error Bind Issue : Removing or Change to Commodities is not displayed.");
		}
		
		
		if(allUWIssues.isInList("Removing or Change to Livestock").equals(UnderwriterIssueType.NONE)) {
			Assert.fail("Expected error Submit Issue : Removing or Change to Livestock is not displayed.");
		}
	}

}
