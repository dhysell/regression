package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Measurement;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

/**
 * @Author nvadlamudi
 * @Requirement :Squire-Section I-Product-Model
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">
 * Squire-Section I-Product-Model</a>
 * @Description :Section I - Quote Bind issues rules are added
 * @DATE Sep 6, 2017
 */
public class TestSectionOneBlockQuoteBindIssues extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;

	@Test
	public void testGenerateSquireWithSectionOne() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

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

		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
		property4.setOwner(true);
		property4.setConstructionType(ConstructionTypePL.Frame);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
		property5.setOwner(true);
		property5.setConstructionType(ConstructionTypePL.Frame);
		property5.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(18);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.squireFPP = squireFPP;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.propertyAndLiability = property;
        mySquire.squirePA = new SquirePersonalAuto();

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Validation", "One")
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionOne" })
	private void testAddBlockBindQuoteIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

		boolean testFailed = false;
		String errorMessage = "";
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// PR001 - $10,000 deductible
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);

		// PR002 - Property value greater than $750,000
		PLPolicyLocationProperty contentProperty = new PLPolicyLocationProperty(PropertyTypePL.Contents);
		contentProperty.setOwner(true);
		contentProperty.setConstructionType(ConstructionTypePL.Frame);
		sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.fillOutPropertyDetails_FA(contentProperty);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setFoundationType(contentProperty.getFoundationType());
		constructionPage.setConstructionType(contentProperty.getConstructionType());
		propertyDetail.clickPropertyInformationDetailsTab();
		propertyDetail.AddExistingOwner();
		propertyDetail.clickOk();
		propertyDetail.clickNext();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Contents));
		coverages.setCoverageCLimit(251000);
		coverages.setCoverageCValuation(contentProperty.getPropertyCoverages());
		coverages.selectCoverageCCoverageType(CoverageType.BroadForm);

		// PR003 - Only 4 Rental Units Allowed
		if (!this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
			sideMenu.clickSideMenuSquirePropertyDetail();
			propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
            propertyDetail.setUnits(NumberOfUnits.FourUnits);
			propertyDetail.clickOk();
		}

		// PR004 - More than 1 rental unit per property
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOk();

		// PR005 - Additional gun coverage high limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.setGunIncreasedTheftLimit(20200);

		// PR006 - Cov A property prior to 1954
		sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.setYearBuilt(1950);
		propertyDetail.clickOk();

		// PR007 - Property over 20 years and Cov E
		sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremisesCovE);
        propertyDetail.clickPropertyConstructionTab();
		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		constructionPage.setYearBuilt(yearField - 25);
		propertyDetail.clickOk();
		propertyDetail.clickNext();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.ResidencePremisesCovE));
        coverages.setCoverageECoverageType(CoverageType.Peril_1);
		coverages.setCoverageELimit(850000);

		// PR008 - Add property over $1,500,000
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(200000);

		// PR009 - Adding Endorsement 209
		coverages.clickCoveragesExclusionsAndConditions();
		if (!this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
			coverages.clickAccessYesEndorsement209();
		}

		// PR010 - Vacant property
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.setDwellingVacantRadio(true);
		propertyDetail.clickOk();

		// PR085 - Trellis with Weight of Ice and Snow
		PLPolicyLocationProperty trelliProperty = new PLPolicyLocationProperty(PropertyTypePL.Trellis);
		trelliProperty.setConstructionType(ConstructionTypePL.Frame);
		propertyDetail.fillOutPropertyDetails_FA(trelliProperty);
		constructionPage.setConstructionType(trelliProperty.getConstructionType());
        constructionPage.setYearBuilt(trelliProperty.getYearBuilt());
		constructionPage.setSquareFootage(trelliProperty.getSquareFootage());
		constructionPage.setMeasurement(Measurement.SQFT);
		propertyDetail.clickOk();
		propertyDetail.clickNext();
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Trellis));
        coverages.setCoverageELimit(125000);
		coverages.setCoverageECoverageType(CoverageType.BroadForm);

		// PR090 - Defensible or not
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
        propertyDetail.clickPropertyConstructionTab();
		constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.setDefensibleSpace(false);
		protectionPage.clickOK();

		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//		propertyLocations.setAcres(10);
		propertyLocations.clickOK();
		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
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
				this.add(PLUWIssues.HighPropLimitGreater750K);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.Only4RentalAllowed);
				this.add(PLUWIssues.MoreThan1RentalUnit);
				this.add(PLUWIssues.GunCoverage20KMore);
				this.add(PLUWIssues.CovABuildEarlier1954);
				this.add(PLUWIssues.AccessYesEndoAdded);
				this.add(PLUWIssues.VacantProperty);			
				this.add(PLUWIssues.DefensibleNot);
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
			
			if(uwIssueExpected.getShortDesc().contains("rental") && this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
				continue;
			}
			
			if(uwIssueExpected.getShortDesc().contains("4 Rental") && this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
				continue;
			}
			
			if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City) && uwIssueExpected.getShortDesc().contains("Access Yes endorsement added")){
				continue;
			}
			if (!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Bind Bind : " + uwIssueExpected.getShortDesc()
						+ " is not displayed.\n";
			}
		}
		
		if(!uwIssues.isInList(PLUWIssues.CluePropertyNotOrder.getLongDesc()).equals(UnderwriterIssueType.AlreadyApproved)){
			testFailed = true;
			errorMessage = errorMessage + "Expected error Already Aprroved : " + PLUWIssues.CluePropertyNotOrder.getShortDesc()
					+ " is not displayed.\n";
		} 

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

//	@Test()
//	private void testIssueSquirePolWithSectionI() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		SquireEligibility squireType = (getRandBoolean()) ? SquireEligibility.City
//				: ((getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);
//
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
//		property1.setOwner(true);
//		property1.setConstructionType(ConstructionTypePL.Frame);
//		property1.setNumberOfUnits(NumberOfUnits.OneUnit);
//
//		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
//		property2.setOwner(true);
//		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
//		property3.setOwner(true);
//		property3.setConstructionType(ConstructionTypePL.Frame);
//
//		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
//		property4.setOwner(true);
//		property4.setConstructionType(ConstructionTypePL.Frame);
//
//		locOnePropertyList.add(property1);
//		locOnePropertyList.add(property2);
//		locOnePropertyList.add(property3);
//		locOnePropertyList.add(property4);
//
//		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
//		location1.setPlNumAcres(10);
//		location1.setPlNumResidence(18);
//		locationsList.add(location1);
//
//		SquireLiability liability = new SquireLiability();
//		liability.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_100);
//		liability.setCoverageLivestock(true);
//		SquireLiablityCoverageLivestockItem livestockSectionIICowCoverage = new SquireLiablityCoverageLivestockItem();
//		livestockSectionIICowCoverage.setQuantity(1200);
//		livestockSectionIICowCoverage.setType(LivestockScheduledItemType.Horse);
//
//		ArrayList<SquireLiablityCoverageLivestockItem> coveredLivestockItems = new ArrayList<SquireLiablityCoverageLivestockItem>();
//		coveredLivestockItems.add(livestockSectionIICowCoverage);
//
//		liability.setCoverageLivestockItems(coveredLivestockItems);
//
//        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.Horses);
//		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
//		squireFPP.setDeductible(FPPDeductible.Ded_1000);
//
//		ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
//		imCoverages.add(InlandMarine.Livestock);
//
//		// Live Stock
//		Livestock livestock = new Livestock();
//		livestock.setType(LivestockType.Livestock);
//		livestock.setDeductible(LivestockDeductible.Ded100);
//
//        LivestockScheduledItem livSchedItem1 = new LivestockScheduledItem(livestock.getType(), LivestockScheduledItemType.Horse, "Test Alpaca 1", 1000);
//		ArrayList<String> livSchedItem2AdditInsureds = new ArrayList<String>();
//		livSchedItem2AdditInsureds.add("Sched Item Guy1");
//
//		ArrayList<LivestockScheduledItem> livSchedItems = new ArrayList<LivestockScheduledItem>();
//		livSchedItems.add(livSchedItem1);
//
//		livestock.setScheduledItems(livSchedItems);
//		ArrayList<String> livAdditInsureds = new ArrayList<String>();
//		livAdditInsureds.add("Cor Hofman");
//		livestock.setAdditionalInsureds(livAdditInsureds);
//
//		Livestock deathOfLivestock = null;
//
//		// Add fourH stuff here
//		Livestock fourH = null;
//
//		LivestockList allLivestock = new LivestockList(livestock, deathOfLivestock, fourH);
//
//		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
//		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
//				"Test" + StringsUtils.generateRandomNumberDigits(8), "Add", AdditionalNamedInsuredType.Affiliate,
//				new AddressInfo(false));
//		ani.setNewContact(CreateNew.Create_New_Always);
//		PolicyInfoAdditionalNamedInsured aniRemove = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
//				"Test" + StringsUtils.generateRandomNumberDigits(8), "Remove", AdditionalNamedInsuredType.Affiliate,
//				new AddressInfo(false));
//		aniRemove.setNewContact(CreateNew.Create_New_Always);
//
//		listOfANIs.add(ani);
//		listOfANIs.add(aniRemove);
//
//        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
//        property.locationList = locationsList;
//        property.liabilitySection = liability;
//        property.squireFPP = squireFPP;
//
//        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
//        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imCoverages;
//        myInlandMarine.livestock_PL_IM = allLivestock.getAllLivestockAsList();
//
//        Squire mySquire = new Squire(squireType);
//        mySquire.propertyAndLiability = property;
//        mySquire.squirePA = new SquirePersonalAuto();
//        mySquire.inlandMarine = myInlandMarine;
//
//        myIssuePolObjPL = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//                .withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
//                .withANIList(listOfANIs)
//                .withInsFirstLastName("Change", "validations")
//                .withPaymentPlanType(PaymentPlanType.Annual)
//                .withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);
//	}
//
//	@Test(dependsOnMethods = { "testIssueSquirePolWithSectionI" })
//	public void testAddBlockQuoteIssuesBindForPolicyChange() throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
//				Underwriter.UnderwriterTitle.Underwriter);
//		loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
//                myIssuePolObjPL.squire.getPolicyNumber());
//
//		Date currentSystemDate = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);
//		//		// add 10 days to current date
//		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
//
//		// start policy change
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
//		policyChangePage.startPolicyChange("First policy Change", changeDate);
//		//        SideMenuPC sideMenu = new SideMenuPC(driver);
//
//		// PR022 - Removing or Change to Livestock
//		sideMenu.clickSideMenuIMLivestock();
//		//        GenericWorkorderIMLivestock livestock = new GenericWorkorderIMLivestock();
//		livestock.removeLivestockByType(LivestockType.Livestock);
//		//		sideMenu.clickSideMenuIMCoveragePartSelection();
//        GenericWorkorderStandardIMCoverageSelection imSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
//		imSelection.checkCoverage(false, LivestockType.Livestock.getValue());
//
//		// PR054 - Removing section
//		sideMenu.clickSideMenuLineSelection();
//        GenericWorkorderLineSelectionCPP lineSelection = new GenericWorkorderLineSelectionCPP();
//		lineSelection.checkSquireInlandMarine(false);
//
//		// PR056 - ANI change to policy
//		sideMenu.clickSideMenuPolicyInfo();
//        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
//		polInfo.clickANIContact("Add");
//		GWCommon common = new GWCommon();
//		//		common.setLastName("Add Edit");
//		//		common.clickUpdate();
//		//
//		// PR057 - PNI or ANI removed
//		polInfo.removeANI("Remove");
//
//		// PR059 - Policy member contact change
//		sideMenu.clickSideMenuHouseholdMembers();
//        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
//		hmember.clickPolicyHolderMembersByName(this.myIssuePolObjPL.pniContact.getFirstName());
//		//		common.setMiddleName("M");
//		sendArbitraryKeys(Keys.TAB);
//		//		polInfo.setReasonForContactChange("Testing purpose");
//		common.clickUpdate();
//
//		// PR064 - Property detail change
//		sideMenu.clickSideMenuSquirePropertyDetail();
//        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
//		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
//		propertyDetail.setDwellingVacantRadio(true);
//		propertyDetail.setUnits(NumberOfUnits.TwoUnits);
//		propertyDetail.clickOk();
//
//		// PR060 - New property added
//		PLPolicyLocationProperty contentProperty = new PLPolicyLocationProperty(PropertyTypePL.Contents);
//		contentProperty.setOwner(true);
//		contentProperty.setConstructionType(ConstructionTypePL.Frame);
//		sideMenu.clickSideMenuSquirePropertyDetail();
//		//
//		propertyDetail.setPropertyDetailsFA(contentProperty);
//        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
//		constructionPage.setFoundationType(contentProperty.getFoundationType());
//		constructionPage.setConstructionType(contentProperty.getConstructionType());
//		propertyDetail.clickPropertyInformationDetailsTab();
//		propertyDetail.AddExistingOwner();
//		propertyDetail.clickOk();
//		propertyDetail.clickNext();
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
//		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Contents));
//		coverages.setCoverageCLimit(251000);
//		coverages.setCoverageCValuation(contentProperty.getPropertyCoverages());
//		coverages.selectCoverageCCoverageType(CoverageType.BroadForm);
//
//		// PR061 - Existing property removed
//		sideMenu.clickSideMenuSquirePropertyDetail();
//		propertyDetail.removeBuilding("4");
//
//		// PR068 - Coverage change
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//		coverages.clickSpecificBuilding(1, 1);
//		coverages.setCoverageALimit(200200);
//
//		// PR067 - Section I deductible increase
//		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);
//
//		// PR069 - Liability limit on F&R
//		if (!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
//			sideMenu.clickSideMenuSquirePropertyCoverages();
//			coverages.clickFarmPersonalProperty();
//		}
//
//		// PR070 - Section II coverage decrease
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//		coverages.clickSectionIICoveragesTab();
//        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
//		addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_50);
//		addCoverage.clickLiveStockCheckBox();
//
//		// PR071 - Access Yes removed
//		coverages.clickCoveragesExclusionsAndConditions();
//		//		if (!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
//			coverages.clickAccessYesEndorsement209();
//		}
//
//		// PR065 - Property Additional insured change
//		sideMenu.clickSideMenuSquirePropertyDetail();
//		propertyDetail.clickViewOrEditBuildingButton(1);
//		propertyDetail.addAddtionalInsured();
//		propertyDetail.setAddtionalInsuredName("TestJobs");
//		propertyDetail.clickOk();
//
//		boolean testFailed = false;
//		String errorMessage = "";
//		sideMenu.clickSideMenuRiskAnalysis();
//		//        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//		risk.Quote();
//		//
//		if(!this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City)){
//			if (!risk.getValidationMessagesText().contains(
//					"The horses quantity on Section II is less than the sum of horses, mules, donkeys, llamas, and/or alpacas on FPP and/or Section IV")) {
//				testFailed = true;
//				errorMessage = errorMessage + "Expected error Message for section II livestock is not displayed.\n";
//			} else {
//				sideMenu.clickSideMenuSquirePropertyCoverages();
//				coverages.clickSectionIICoveragesTab();
//				//				addCoverage.addCoverages(SectionIICoverages.Livestock);
//				addCoverage.setLivestockTypeAndQuantity(LivestockScheduledItemType.Horse, 2);
//				sideMenu.clickSideMenuRiskAnalysis();
//				risk.Quote();
//				//			}
//		}
//
//        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
//		if (quote.isPreQuoteDisplayed()) {
//			quote.clickPreQuoteDetails();
//		}
//		sideMenu.clickSideMenuRiskAnalysis();
//		//
//		@SuppressWarnings("serial")
//		List<PLUWIssues> sqSectionIBlockBind = new ArrayList<PLUWIssues>() {
//			{
//				this.add(PLUWIssues.SectionIDed10KHigh);
//				this.add(PLUWIssues.HighPropLimitGreater250K);
//			}
//		};
//
//		@SuppressWarnings("serial")
//		List<PLUWIssues> sqSectionIBlockIssue = new ArrayList<PLUWIssues>() {
//			{
//				this.add(PLUWIssues.MoreThan1RentalUnit);
//				this.add(PLUWIssues.AccessYesEndoAdded);
//				this.add(PLUWIssues.VacantProperty);
//				this.add(PLUWIssues.ANIChange);	
//				this.add(PLUWIssues.SectionIDedIncrease);	
//				this.add(PLUWIssues.Coveragechange);
//				this.add(PLUWIssues.IMCoverageChange);				
//				this.add(PLUWIssues.AContactChanged);
//				this.add(PLUWIssues.PropertyAddInsuredchange);
//				this.add(PLUWIssues.NewPropertyAdded);
//				this.add(PLUWIssues.PropertyDetailChange);
//				this.add(PLUWIssues.ExistingPropRemoved);
//				this.add(PLUWIssues.RemoveSection);
//				this.add(PLUWIssues.PropertyAddInsuredchange);
//			}
//		};
//
//		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//		
//		for (PLUWIssues uwBlockBindExpected : sqSectionIBlockBind) {
//			if (!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit)) {
//				testFailed = true;
//				errorMessage = errorMessage + "Expected error Bind Bind : " + uwBlockBindExpected.getShortDesc()
//						+ " is not displayed.\n";
//			}
//		}
//
//		for (PLUWIssues uwIssueExpected : sqSectionIBlockIssue) {			
//			if(uwIssueExpected.equals(PLUWIssues.MoreThan1RentalUnit) && this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)){
//				continue;
//			}
//			
//			if (this.myIssuePolObjPL.squire.squireEligibility.equals(SquireEligibility.City) && uwIssueExpected.getShortDesc().equals(PLUWIssues.AccessYesEndoAdded.getShortDesc())){
//				continue;
//			}
//			
//			if (!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance)) {
//				testFailed = true;
//				errorMessage = errorMessage + "Expected error Block Issue : " + uwIssueExpected.getShortDesc()
//						+ " is not displayed.\n";
//			}
//		}
//
//		if (testFailed) {
//			Assert.fail(errorMessage);
//		}
//	}
	
	
//	@Test ()
//	public void testTrellisedHopsUWIssue()  throws Exception {
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//		locationsList.add(locToAdd);
//
//		SquireLiability myLiab = new SquireLiability();
//		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
//
//        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//        myPropertyAndLiability.locationList = locationsList;
//        myPropertyAndLiability.liabilitySection = myLiab;
//
//        Squire mySquire = new Squire(SquireEligibility.Country);
//        mySquire.propertyAndLiability = myPropertyAndLiability;
//
//		this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
//                .withSquire(mySquire)
//				.withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
//				.withInsFirstLastName("SQ", "Hops")
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.FullApp);
//
//		//standard fire
//		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
//		locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.TrellisedHops));
//		PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);
//
//		locToAdd1.setPlNumAcres(11);
//		locToAdd1.setPlNumResidence(5);;
//		locationsList1.add(locToAdd1);
//
//		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
//		propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
//
//        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
//        myStandardFire.setLocationList(locationsList1);
//        myStandardFire.commodity = true;
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//
//        stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
//        stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
//        stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
//		
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//        loginAndSearchSubmission(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(),
//                stdFireLiab_Squire_PolicyObj.accountNumber);
//		//		this.editPolicyTransaction();
//
//		boolean testFailed = false;
//		String errorMessage = "";
//        SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//		//		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
//		coverages.setCoverageELimit(200000);
//		
//		sideMenu.clickSideMenuRiskAnalysis();
//		//        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//		risk.Quote();
//		//
//        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
//		if (quote.isPreQuoteDisplayed()) {
//			quote.clickPreQuoteDetails();
//		}
//		sideMenu.clickSideMenuRiskAnalysis();
//		//
//		@SuppressWarnings("serial")
//		List<PLUWIssues> sqSectionIBlockBind = new ArrayList<PLUWIssues>() {
//			{
//				this.add(PLUWIssues.TrellisedHopExists);
//			}
//		};
//
//		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//		
//		for (PLUWIssues uwBlockBindExpected : sqSectionIBlockBind) {
//			if (!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.AlreadyApproved)) {
//				testFailed = true;
//				errorMessage = errorMessage + "Expected error Bind Bind : " + uwBlockBindExpected.getShortDesc()
//						+ " is not displayed.\n";
//			}
//		}
//		
//		if (testFailed) {
//			Assert.fail(errorMessage);
//		}		
//	}
//	
	
	
}
