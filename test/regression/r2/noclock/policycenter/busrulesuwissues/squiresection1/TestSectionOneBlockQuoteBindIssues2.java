package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockList;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquireLiablityCoverageLivestockItem;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_Livestock;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :Squire-Section I-Product-Model
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">
 * Squire-Section I-Product-Model</a>
 * @Description :Section I - Quote Bind issues rules are added
 * @DATE Sep 6, 2017
 */
public class TestSectionOneBlockQuoteBindIssues2 extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	private GeneratePolicy myIssuePolObjPL;


	@Test()
	private void testIssueSquirePolWithSectionI() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireEligibility squireType = new GuidewireHelpers(driver).getRandomEligibility();

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setOwner(true);
		property1.setConstructionType(ConstructionTypePL.Frame);
		property1.setNumberOfUnits(NumberOfUnits.OneUnit);

		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		property2.setOwner(true);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property3.setOwner(true);
		property3.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
		property4.setOwner(true);
		property4.setConstructionType(ConstructionTypePL.Frame);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(18);
		locationsList.add(location1);
		
		
		SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
		livestockSectionIICowCoverage.setQuantity(1200);
		livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Horse);

		ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
		SectionIICoverages livestockCoverage = new SectionIICoverages(SectionIICoveragesEnum.Livestock, coveredLivestockItems);

		SquireLiability liability = new SquireLiability();
		liability.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_100);
		liability.getSectionIICoverageList().add(livestockCoverage);
		
		
		
		

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.Horses);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

		ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
		imCoverages.add(InlandMarine.Livestock);

		// Live Stock
		Livestock livestock = new Livestock();
		livestock.setType(LivestockType.Livestock);
		livestock.setDeductible(LivestockDeductible.Ded100);

        LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Horse, "Test Alpaca 1", 1000);
		ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
		livSchedItem2AdditInsureds.add("Sched Item Guy1");

		ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
		livSchedItems.add(livSchedItem1);

		livestock.setScheduledItems(livSchedItems);
		ArrayList<String> livAdditInsureds = new ArrayList<String>();
		livAdditInsureds.add("Cor Hofman");
		livestock.setAdditionalInsureds(livAdditInsureds);

		Livestock deathOfLivestock = null;

		// Add fourH stuff here
		Livestock fourH = null;

		LivestockList allLivestock = new LivestockList(livestock, deathOfLivestock, fourH);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Add", AdditionalNamedInsuredType.Affiliate,
				new AddressInfo(false));
		ani.setNewContact(CreateNew.Create_New_Always);
		PolicyInfoAdditionalNamedInsured aniRemove = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Remove", AdditionalNamedInsuredType.Affiliate,
				new AddressInfo(false));
		aniRemove.setNewContact(CreateNew.Create_New_Always);

		listOfANIs.add(ani);
		listOfANIs.add(aniRemove);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = liability;
        property.squireFPP = squireFPP;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();

        Squire mySquire = new Squire(squireType);
        mySquire.propertyAndLiability = property;
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.inlandMarine = myInlandMarine;

        myIssuePolObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withANIList(listOfANIs)
                .withInsFirstLastName("Change", "validations")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePolWithSectionI" })
	public void testAddBlockQuoteIssuesBindForPolicyChange() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myIssuePolObjPL.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        // add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// PR022 - Removing or Change to Livestock
		sideMenu.clickSideMenuIMLivestock();
        GenericWorkorderSquireInlandMarine_Livestock livestock = new GenericWorkorderSquireInlandMarine_Livestock(driver);
		livestock.removeLivestockByType(LivestockType.Livestock);
        sideMenu.clickSideMenuIMCoveragePartSelection();
        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(false, LivestockType.Livestock.getValue());

		// PR054 - Removing section
		sideMenu.clickSideMenuLineSelection();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkSquireInlandMarine(false);

		// PR056 - ANI change to policy
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickANIContact("Add");
		GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
		ani.setAdditionalInsuredLastName("Add Edit");
        ani.clickUpdate();

		// PR057 - PNI or ANI removed
		polInfo.removeANI("Remove");

		// PR059 - Policy member contact change
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickPolicyHolderMembersByName(this.myIssuePolObjPL.pniContact.getFirstName());
		ani.setAdditionalInsuredMiddleName("M");
        polInfo.setReasonForContactChange("Testing purpose");
        ani.clickUpdate();

		// PR064 - Property detail change
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
		propertyDetail.setDwellingVacantRadio(true);
		propertyDetail.setUnits(NumberOfUnits.TwoUnits);
		propertyDetail.clickOk();

		// PR060 - New property added
		PLPolicyLocationProperty contentProperty = new PLPolicyLocationProperty(PropertyTypePL.Contents);
		contentProperty.setOwner(true);
		contentProperty.setConstructionType(ConstructionTypePL.Frame);
		sideMenu.clickSideMenuSquirePropertyDetail();

		propertyDetail.fillOutPropertyDetails_QQ(myIssuePolObjPL.basicSearch, contentProperty);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setFoundationType(contentProperty.getFoundationType());
		constructionPage.setConstructionType(contentProperty.getConstructionType());
		propertyDetail.clickPropertyInformationDetailsTab();
		propertyDetail.AddExistingOwner();
		propertyDetail.clickOk();
		propertyDetail.clickNext();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Contents));
		coverages.setCoverageCLimit(251000);
		coverages.setCoverageCValuation(contentProperty.getPropertyCoverages());
		coverages.selectCoverageCCoverageType(CoverageType.BroadForm);

		// PR061 - Existing property removed
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.removeBuilding("4");

		// PR068 - Coverage change
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(200200);

		// PR067 - Section I deductible increase
		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);

		// PR069 - Liability limit on F&R
		if (!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			sideMenu.clickSideMenuSquirePropertyCoverages();
			coverages.clickFarmPersonalProperty();
		}

		// PR070 - Section II coverage decrease
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_50);
		addCoverage.clickLiveStockCheckBox();

		// PR071 - Access Yes removed
		coverages.clickCoveragesExclusionsAndConditions();
        if (!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			coverages.clickAccessYesEndorsement209();
		}

		// PR065 - Property Additional insured change
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.addAddtionalInsured();
		propertyDetail.setAddtionalInsuredName("TestJobs");
		propertyDetail.clickOk();
		
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//		propertyLocations.setAcres(10);
		propertyLocations.clickOK();
		
		boolean testFailed = false;
		String errorMessage = "";
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		if(!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)){
			if (!risk.getValidationMessagesText().contains(
					"The horses quantity on Section II is less than the sum of horses, mules, donkeys, llamas, and/or alpacas on FPP and/or Section IV")) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Message for section II livestock is not displayed.\n";
			} else {
				sideMenu.clickSideMenuSquirePropertyCoverages();
				coverages.clickSectionIICoveragesTab();

                addCoverage.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
				addCoverage.setLivestockTypeAndQuantity(LivestockScheduledItemType.Horse, 2);
				sideMenu.clickSideMenuRiskAnalysis();
				risk.Quote();
            }
		}

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

        @SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockBind = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.SectionIDed10KHigh);
				this.add(PLUWIssues.HighPropLimitGreater250K);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MoreThan1RentalUnit);
				this.add(PLUWIssues.AccessYesEndoAdded);
				this.add(PLUWIssues.VacantProperty);
				this.add(PLUWIssues.ANIChange);	
				this.add(PLUWIssues.SectionIDedIncrease);	
				this.add(PLUWIssues.Coveragechange);
				this.add(PLUWIssues.IMCoverageChange);				
				this.add(PLUWIssues.AContactChanged);
				this.add(PLUWIssues.PropertyAddInsuredchange);
				this.add(PLUWIssues.NewPropertyAdded);
				this.add(PLUWIssues.PropertyDetailChange);
				this.add(PLUWIssues.ExistingPropRemoved);
				this.add(PLUWIssues.RemoveSection);
				this.add(PLUWIssues.PropertyAddInsuredchange);
			}
		};

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		
		for (PLUWIssues uwBlockBindExpected : sqSectionIBlockBind) {
			if (!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Bind Bind : " + uwBlockBindExpected.getShortDesc()
						+ " is not displayed.\n";
			}
		}

		for (PLUWIssues uwIssueExpected : sqSectionIBlockIssue) {			
			if(uwIssueExpected.equals(PLUWIssues.MoreThan1RentalUnit) && this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)){
				continue;
			}
			
			if (this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City) && uwIssueExpected.getShortDesc().equals(PLUWIssues.AccessYesEndoAdded.getShortDesc())){
				continue;
			}
			
			if (!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Block Issue : " + uwIssueExpected.getShortDesc()
						+ " is not displayed.\n";
			}
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}
}
