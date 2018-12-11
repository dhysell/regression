package regression.r2.noclock.policycenter.change.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AddressType;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLLocationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4287 Removed barn ONCE but it was removed from each location
 * @Description :
 * @DATE Jan 16, 2017
 */
public class TestRemovalOfBarnFromEachLocation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private Underwriters uw;

	@Test
	public void testGenerateSquirePolicy() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));		
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locToAdd.setPlNumResidence(6);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);

		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.inlandMarine = myInlandMarine;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Guy", "Inlandmarineedit")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);

	}

	@Test(dependsOnMethods = {"testGenerateSquirePolicy"})
	public void testAddMultipleLocationsAndProperties() throws Exception{

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		AddressInfo address = new AddressInfo("6315 W YORK ST","","BOISE", State.Idaho, "837047573", CountyIdaho.Ada, "United States", AddressType.Home);

		for(int i=1;  i < 4; i++){
			sidemenu.clickSideMenuLocations();
			int locnum = location.addNewOrSelectExistingLocationFA(PLLocationType.Address, address, 12, 1);	
			sidemenu.clickSideMenuSquirePropertyDetail();
			prop.highLightPropertyLocationByNumber(locnum-1);
			addPLSectionProperties(locnum);
		}

        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.performRiskAnalysisAndQuote(myPolicyObj);
        sidemenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);

        GenericWorkorder workorder = new GenericWorkorder(driver);
		workorder.clickGenericWorkorderSubmitOptionsSubmit();
		new GuidewireHelpers(driver).logout();		

	}

	@Test(dependsOnMethods = {"testAddMultipleLocationsAndProperties"})
	public void testIssueSquirePolicy() throws Exception{

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickCloseWorkSheet();
        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		for(int i = 2;  i < 5;  i++){
			prop.highLightPropertyLocationByNumber(i);
            prop.clickViewOrEditBuildingButton(i);
			prop.setRisk("A");
			prop.clickOk();
        }
		sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.approveAll();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
		sidemenu.clickSideMenuQuote();

        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuForms();
        sideMenuStuff = new SideMenuPC(driver);
		sideMenuStuff.clickSideMenuQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		quotePage.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();
	}

	@Test(dependsOnMethods = {"testIssueSquirePolicy"})
	public void testPolicyChangeBound() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyDetail();
		sidemenu.clickSideMenuLocations();
		AddressInfo address = new AddressInfo("6315 W YORK ST","","BOISE", State.Idaho, "837047573", CountyIdaho.Ada, "United States", AddressType.Home);
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		int locnum = location.addNewOrSelectExistingLocationFA(PLLocationType.Address, address, 12, 1);	
		sidemenu.clickSideMenuSquirePropertyDetail();
		prop.highLightPropertyLocationByNumber(locnum-1);
		addPLSectionProperties(locnum);

		sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        policyChangePage.clickIssuePolicy();
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();

			sidemenu.clickSideMenuRiskAnalysis();
			riskAnalysis.approveAll_IncludingSpecial();
			policyChangePage.clickIssuePolicy();
		}		

	}
	
	@Test(dependsOnMethods = {"testPolicyChangeBound"})
	public void testValidationAfterBarnRemoved() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail prop = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		
		sidemenu.clickSideMenuSquirePropertyDetail();
		prop.highLightPropertyLocationByNumber(2);
        prop.setCheckBoxByRowInPropertiesTable(1, true);
		prop.clickRemoveProperty();

		sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickCostChangeDetails();
		String errorMessage = " ";
		for(int i = 2; i < 5 ; i++){
		if(quote.getPropertiesMultipleTablesDescriptionColoumFirstRowText(2).contains("Removed"))		
		errorMessage = errorMessage + "If Location 2 Barn Property removed location" + (i+1) + "barn prperties Should not be removed";
		}
		
		if(!errorMessage.equals(" "))
			Assert.fail(errorMessage);
	}

	private void addPLSectionProperties(int number) throws Exception {
        SideMenuPC sideMenu = new SideMenuPC(driver);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.highLightPropertyLocationByNumber(number-1);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.Barn);
		propertyDetail.fillOutPropertyDetails_FA(property1);
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
        constructionPage.setCoverageAPropertyDetailsFA(property1);

		propertyDetail.clickOk();	
		int BuildingNumber = propertyDetail.getSelectedBuildingNum();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(number, BuildingNumber);		
		coverages.setCoverageELimit(100200);
		coverages.setCoverageECoverageType(CoverageType.BroadForm);	
		coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);

	}

}
