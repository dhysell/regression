package regression.r2.noclock.policycenter.rolespermissions;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.ParkingLotSidewalkCharacteristics;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.PDDeductibleAmount;
import repository.gw.enums.BusinessownersLine.PremisesMedicalExpense;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderLocationsAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.helpers.CSRsHelper;

/**
 * @Author sbroderick
 * @Requirement These test methods ensure the CSR can perform his or her job.
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Admin%20Tab/CSR%20and%20SA%20Permissions%20NEW.docx"> CSR Permissions</a>
 * @Description
 * @DATE May 10, 2016
 */


@QuarantineClass
public class CSRPermissions extends BaseTest {

    private CSRs csr;
    private GeneratePolicy myPolicy;
    private PolicyInfoAdditionalNamedInsured ani;
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void go() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() throws Exception {
//
    }

    @Test(description = "Generates Policy", enabled = true)
    public void generatePolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        // GENERATE POLICY
        this.myPolicy = new GeneratePolicy.Builder(driver).withPolOrgType(OrganizationType.Joint_Venture)
                .withInsCompanyName("CSR Policy Change").withPolicyLocations(locationsList).withPolTermLengthDays(360)
                .withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Credit_Debit)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .build(GeneratePolicyType.PolicyIssued);

        this.csr = getCSR();
        this.ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person);
    }

    private CSRs getCSR() throws Exception {
        String region = myPolicy.agentInfo.getAgentRegion().substring(0, 8);
        CSRs csr = CSRsHelper.getCSRByRegion(region);
        this.csr = csr;
        return csr;
    }

    public void bindPolicyChange(ChangeReason reason) throws GuidewireException {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        StartPolicyChange bindChange = new StartPolicyChange(driver);
        bindChange.quoteAndSubmit(reason, "Change Me");
        GenericWorkorderComplete info = new GenericWorkorderComplete(driver);
        info.clickViewYourPolicy();
        if (!getActivityAssignment("Submitted policy change").contains(myPolicy.underwriterInfo.getUnderwriterFirstName() + " " + myPolicy.underwriterInfo.getUnderwriterLastName())) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "Submitted policy changes should go to the home office.");
        }
    }

    public void requestApproval() throws GuidewireException {
        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();
        boolean bindReady = genericWO.isSubmittable();
        if (bindReady) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "Bind should not be available given the type of Policy Change.");
        }
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis requestApproval = new GenericWorkorderRiskAnalysis(driver);
        requestApproval.requestApproval(this.myPolicy.underwriterInfo);
        GenericWorkorderComplete policyChangeComplete = new GenericWorkorderComplete(driver);
        policyChangeComplete.clickViewYourPolicy();
        if (!(getActivityAssignment("Review and bind ").contains(myPolicy.agentInfo.getAgentLastName()))) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "Request Approval policy changes should go to the Agent.");
        }
    }

    public void completeActivity() {
        try {
            String user = getActivityAssignment("Review and ");
            String[] splitName = user.split("\\s+");
            new GuidewireHelpers(driver).logout();
            Login login = new Login(driver);
            if (myPolicy.agentInfo.getAgentLastName().contains(splitName[splitName.length - 1])) {
                login.loginAndSearchPolicyByAccountNumber(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);
            } else {
                login.loginAndSearchPolicyByAccountNumber(myPolicy.underwriterInfo.getUnderwriterUserName(), myPolicy.underwriterInfo.getUnderwriterPassword(), myPolicy.accountNumber);
            }
            PolicySummary summaryPage = new PolicySummary(driver);
            summaryPage.clickActivity("Review and ");
            ActivityPopup activity = new ActivityPopup(driver);
            activity.clickActivityComplete();
        } catch (Exception e) {

        }
        new GuidewireHelpers(driver).logout();
    }

    public String getActivityAssignment(String activityName) throws GuidewireException {
        PolicySummary summaryPage = new PolicySummary(driver);
        boolean found = summaryPage.checkIfActivityExists(activityName);
        if (!found) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "The activity: " + activityName + " was not found on the policy");
        }
        return summaryPage.getActivityAssignment(activityName);
    }

    public void issueChange() {

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicy.underwriterInfo.getUnderwriterUserName(), myPolicy.underwriterInfo.getUnderwriterPassword(), myPolicy.accountNumber);
        PolicySummary summaryPage = new PolicySummary(driver);
        int lcv = 0;
        do {
            summaryPage.clickActivity("Submitted policy change");
            StartPolicyChange issueChange = new StartPolicyChange(driver);
            issueChange.clickIssuePolicy();
        } while (summaryPage.checkIfActivityExists("Review and ") && lcv < 10);
    }

    //Completes activity and starts Policy Change.
    public void startChange(String changeDescription) throws Exception {
        Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(csr.getCsruserName(), csr.getCsrPassword(), myPolicy.accountNumber);
        completeActivity();
        login.loginAndSearchPolicyByAccountNumber(csr.getCsruserName(), csr.getCsrPassword(), myPolicy.accountNumber);
        StartPolicyChange startChange = new StartPolicyChange(driver);
        startChange.startPolicyChange(changeDescription, centerDate);
    }

    @Test(description = "This test ensures CSR's can initiate Expiration Date Changes", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void expirationDateChange() throws GuidewireException {
        Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(csr.getCsruserName(), csr.getCsrPassword(), myPolicy.accountNumber);
        completeActivity();
        login.loginAndSearchPolicyByAccountNumber(csr.getCsruserName(), csr.getCsrPassword(), myPolicy.accountNumber);
//        StartPolicyChange startChange = new StartPolicyChange(driver);
        changeExpirationDateCSR(DateUtils.dateAddSubtract(centerDate, DateAddSubtractOptions.Month, 5), "Expiration Date Change");
        requestApproval();
        new GuidewireHelpers(driver).logout();

        issueChange();
    }

    /*
     *    Policy Changes
     *
     * */
    
    public void changeExpirationDateCSR(Date expDate, String description) {
    	StartPolicyChange startChange = new StartPolicyChange(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickExpirationDateChange();
        startChange.setDescription(description);
        startChange.clickPolicyChangeNext();
        startChange.setExpirationDate(expDate);
    }
    
    
    
    
    
    
    

    /**
     * @throws Exception
     * @Author sbroderick
     * @Description: This test ensures CSR's can bind PolicyChanges on the Policy Info Page.
     * @DATE May 10, 2016
     */
    @Test(description = "changeDBAMailingAddressBusinessYear", enabled = true, dependsOnMethods = {"policyInfoBindChanges"})
    public void changeDBAMailingAddressBusinessYear() throws Exception {
        startChange("PolicyInfo Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfoChange = new GenericWorkorderPolicyInfo(driver);
        myPolicy.yearBusinessStarted = "1880";
		policyInfoChange.fillOutBusinessAndOperations(myPolicy);
        policyInfoChange.addDBA("The DBA");
        policyInfoChange = new GenericWorkorderPolicyInfo(driver);
        policyInfoChange.addNewAddress(new AddressInfo(true), "Change Mailing Address");
        policyInfoChange = new GenericWorkorderPolicyInfo(driver);
        policyInfoChange.setMembershipDues(ani.getPersonLastName(), true);
        bindPolicyChange(ChangeReason.MultipleChanges);
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Description: This test ensures CSR's can bind PolicyChanges on the Policy Info Page.
     * @DATE May 10, 2016
     */
    @Test(description = "Add ani and change description of Operations so CSR can Request Approval", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void policyInfoBindChanges() throws Exception {
        startChange("PolicyInfo Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfoChange = new GenericWorkorderPolicyInfo(driver);
        policyInfoChange.addAdditionalNamedInsured(true, ani);
		policyInfoChange.fillOutBusinessAndOperations(myPolicy);
//		policyInfoChange.setPolicyInfoDescriptionOfOperations("Make Money");
        requestApproval();
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /*
     * Businessowners Line Policy Changes
     * */

    /**
     * @throws Exception 
     * @Author sbroderick
     * @Description: This test ensures CSR's can bind PolicyChanges on the Businessowners Line Page.
     * @DATE May 10, 2016
     */
    @Test(description = "Small Business Type, PD Deductible, limits, Premises Medicalchange Description of Operations", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void businessOwnersLineRequestApprovalChanges() throws Exception {
        startChange("Businessowners Line Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuBusinessownersLine();
        GenericWorkorderBusinessownersLineIncludedCoverages boLinePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLinePage.setSmallBusinessType(SmallBusinessType.Offices);
        myPolicy.busOwnLine.setLiabilityLimits(LiabilityLimits.Two000_4000_4000);
        myPolicy.busOwnLine.setPdDeductible(PDDeductibleAmount.PD1000);
        myPolicy.busOwnLine.setMedicalLimit(PremisesMedicalExpense.Exp10000);
        boLinePage.filOutLiabilityCoverages(myPolicy.busOwnLine);


        boLinePage.clickBusinessownersLine_AdditionalCoverages();
        GenericWorkorderBusinessownersLineAdditionalCoverages boAddCoverages = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
        myPolicy.busOwnLine.getAdditionalCoverageStuff().setElectronicDataCoverage(true);
        boAddCoverages.fillOutOtherPropertyCoverages(myPolicy.busOwnLine);

        requestApproval();
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Description: This test ensures CSR's can bind PolicyChanges on the Policy Info Page.
     * @DATE May 10, 2016
     */
    @Test(description = "change Description of Operations", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void businessOwnersLineBindChanges() throws Exception {
        startChange("Businessowners Line Changes");
        PolicyBusinessownersLineAdditionalInsured aiBoLine = new PolicyBusinessownersLineAdditionalInsured();
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuBusinessownersLine();
        GenericWorkorderBusinessownersLineIncludedCoverages boLinePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLinePage.addAdditionalInsureds(true, aiBoLine);
        bindPolicyChange(ChangeReason.AdditionalInsuredAdded);
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /*
     * Location Policy Changes
     * */


    @Test(description = "This test ensures CSR's can initiate Location page changes", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void locationRequestApprovalChanges() throws Exception {
        startChange("Location Page Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuLocations();
        GenericWorkorderLocations locationPage = new GenericWorkorderLocations(driver);
        locationPage.clickLocationsLocationEdit(1);
        locationPage.clickLocationsAdditionalCoverages();
        GenericWorkorderLocationsAdditionalCoverages locationAddCoverages = new GenericWorkorderLocationsAdditionalCoverages(driver);
        locationAddCoverages.setOutdoorSignCheckbox(true);
        locationAddCoverages.setOutdoorSignLimit(5000);
        locationPage = new GenericWorkorderLocations(driver);
        locationPage.clickLocationsOk();
        requestApproval();
        new GuidewireHelpers(driver).logout();
        issueChange();
    }


    @Test(description = "Locations Page Bind Changes", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void locationBindChanges() throws Exception {
        float grossReciepts = 1990000;
        startChange("Locations Page Bind Changes");
        PolicyLocationAdditionalInsured locationInsured = new PolicyLocationAdditionalInsured();
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuLocations();
        GenericWorkorderLocations locationPage = new GenericWorkorderLocations(driver);
        locationPage.clickPrimaryLocationEdit();
        locationPage.setLocationsAnnualGrossReceipts(grossReciepts);
        locationPage = new GenericWorkorderLocations(driver);
        locationPage.clickLocationsOk();
        locationPage.addAdditionalInsuredLocation(true, locationInsured);
        bindPolicyChange(ChangeReason.AdditionalInsuredAdded);
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /**
     * @throws Exception 
     * @Author sbroderick
     * @Description: This test ensures CSR's can initate changes on the buildings page.
     * @DATE May 10, 2016
     */
    @Test(description = "Buildings Page Changes", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void buildingsRequestApprovalChanges() throws Exception {
        startChange("Building Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuBuildings();
        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        buildingsPage.clickBuildingsBuildingEdit(1);
        buildingsPage.setBuildingPersonalPropertyLimit(10000);
        buildingsPage.setExteriorHousekeeping(HouseKeepingMaint.Average);
        buildingsPage.setInteriorHousekeeping(HouseKeepingMaint.Average);
        ArrayList<ParkingLotSidewalkCharacteristics> parkinglotCharacteristics = new ArrayList<ParkingLotSidewalkCharacteristics>();
        parkinglotCharacteristics.add(ParkingLotSidewalkCharacteristics.WellLitAtNight);
        buildingsPage.setParkingLotSidewalkCharacteristics(parkinglotCharacteristics);
        buildingsPage.setTotalArea("3001");
        buildingsPage.setRoofingType(RoofingType.ShakesWood);
        buildingsPage.setWiringUpdateDescription("Remodel");
        buildingsPage.setHeatingUpdateDescription("Remodel");
        buildingsPage.setPlumbingUpdateDescription("Remodel");
        buildingsPage.setExistingDamage(true);
        buildingsPage.setDamageDescription("Damage");
        buildingsPage.setInsuredPropertyWithin100Feet(true);
        buildingsPage.setInsuredWithinHundredFeetPolicyHolderName("Cor Hofman");
        buildingsPage.setInsuredWithinHundredFeetPolicyNumber("Your Mom");
        buildingsPage.clickAdditionalCoverages();
        GenericWorkorderBuildingAdditionalCoverages buildingAddCoverages = new GenericWorkorderBuildingAdditionalCoverages(driver);
        buildingAddCoverages.setAccountsReceivableOptionalCoverage(new PolicyLocationBuilding());
        buildingsPage = new GenericWorkorderBuildings(driver);
        buildingsPage.clickOK();
        requestApproval();
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Description: This test ensures CSR's can bind the change to add an Additional Interest.
     * @DATE May 10, 2016
     */
    @Test(description = " This test ensures CSR's can bind the change to add an Additional Interest.", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void buildingBindChange() throws Exception {
        startChange("Add Lienholder");
        AdditionalInterest lienholder = new AdditionalInterest();
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuBuildings();
        GenericWorkorderBuildings buildingsPage = new GenericWorkorderBuildings(driver);
        buildingsPage.clickBuildingsBuildingEdit(1);
        buildingsPage = new GenericWorkorderBuildings(driver);
        buildingsPage.addAdditionalInterest(true, lienholder);
        buildingsPage = new GenericWorkorderBuildings(driver);
        buildingsPage.clickOK();
        bindPolicyChange(ChangeReason.Other);
        new GuidewireHelpers(driver).logout();
        issueChange();
    }

    /**
     * @throws Exception
     * @Author sbroderick
     * @Description: This test ensures CSR's cannot change PNI Name from the Policy Info Page.
     * @DATE May 10, 2016
     */
    @Test(description = "Attempt to change name from the PolicyInfo Page.", enabled = true, dependsOnMethods = {"generatePolicy"})
    public void initiateNameChange() throws Exception {
        startChange("PolicyInfo Changes");
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfoChange = new GenericWorkorderPolicyInfo(driver);
        policyInfoChange.clickPolicyInfoPrimaryNamedInsured();
        String pniNameRole = policyInfoChange.getPNINameRoleAttribute();
        if (!pniNameRole.equals("textbox")) {
            Assert.fail(driver.getCurrentUrl() + myPolicy.accountNumber + "Ensure the PNI name on this policy is not editable by the CSR.");
        }
        new GuidewireHelpers(driver).logout();
    }


}
