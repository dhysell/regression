package scratchpad.evan.PIPlaceholder;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import services.broker.objects.lexisnexis.cbr.response.actual.InquirySubjectType.Vehicle;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/191432207976">US13931 F281 - Update Coverage A classes and availabilities</a>
 * @Description
As a PolicyCenter user I want to be able to select only the allowable building types, construction and foundations as referred to here.  **waiting for more details and okay from Lisa. Lara and Erin submitted wanted options to her for review** (items that need checked and updated)

Steps to get there:

    Create squire policy with Coverage A buildings: Residence, Vacation Home, Dwelling, Dwelling Under Construction.
    Make sure each has foundation type of Pier and Beam.
    Should default to Broad Form (no SF allowed)
    Should receive UW block issue 

Acceptance Criteria:

    Each building with the Pier and Beam foundation should have the block issue
    Other items to be added after Lisa reviews

Also need to create a block issue:

    If Coverage A Habitable Building (Residence, Vacation Home, Dwelling, Dwelling Under Construction) has foundation type of Pier and Beam need a block issue. 
    Message should read: "There is a Coverage A building (add location) with Pier and Beam foundation"



 * @DATE July 19, 2018
 */

public class US13931_CoverageAClassPierBeamBlock2_3 extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        PLPolicyLocationProperty newProperty1 = new PLPolicyLocationProperty();
        newProperty1.setConstructionType(ConstructionTypePL.ModularManufactured);
        newProperty1.setFoundationType(FoundationType.PierAndBeam);
        
        PLPolicyLocationProperty newProperty2 = new PLPolicyLocationProperty();
        newProperty2.setpropertyType(PropertyTypePL.DwellingPremises);
        newProperty2.setConstructionType(ConstructionTypePL.ModularManufactured);
        newProperty2.setFoundationType(FoundationType.PierAndBeam);
        
//        PLPolicyLocationProperty newProperty3 = new PLPolicyLocationProperty();
//        newProperty3.setpropertyType(PropertyTypePL.DwellingUnderConstruction);
//        newProperty3.setFoundationType(FoundationType.PierAndBeam);
        
        PLPolicyLocationProperty newProperty4 = new PLPolicyLocationProperty();
        newProperty4.setpropertyType(PropertyTypePL.VacationHome);
        newProperty4.setConstructionType(ConstructionTypePL.ModularManufactured);
        newProperty4.setFoundationType(FoundationType.PierAndBeam);
        
        
        Squire mySquire = new Squire();
        PolicyLocation newLocation = new PolicyLocation();
        ArrayList<PLPolicyLocationProperty> propertyLocationList1 = new ArrayList<PLPolicyLocationProperty>();
        propertyLocationList1.add(newProperty1);
        propertyLocationList1.add(newProperty2);
//        propertyLocationList1.add(newProperty3);
        propertyLocationList1.add(newProperty4);
        newLocation.setPropertyList(propertyLocationList1);
        ArrayList<PolicyLocation> LocationList1 = new ArrayList<PolicyLocation>();
        LocationList1.add(newLocation);
        mySquire.propertyAndLiability.locationList = LocationList1;
        
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withSquire(mySquire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .isDraft()
                .build(GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void VerifyBlockOnPierAndBeam() {

        try {
            generatePolicy();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to generate policy. Test cannot continue.");
        }

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderModifiers pcModifiersPage = new GenericWorkorderModifiers(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        //GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
        GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);
        Vehicle myVehicle = new Vehicle();
        SoftAssert softAssert = new SoftAssert();
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
        PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
        GenericWorkorderSquirePropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        ActionsPC pcActionsMenu = new ActionsPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver);
        ActivityPopup pcActivityPopup = new ActivityPopup(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyAndLiabilityPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        
        
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();       
        
        // Acceptance criteria:
        softAssert.assertTrue(gwHelpers.isRequired("There is a Coverage A building (add location) with Pier and Beam foundation"), "Pier & Beam Validation should be present, but isn't");
        
        softAssert.assertAll();
    }


}




















