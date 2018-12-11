package regression.r2.noclock.policycenter.submission_misc.subgroup9;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;

/**
 * @Author skandibanda
 * @Requirement : DE4337: Quote Validation message not displayed and NullPointer received
 * @Description :
 * @DATE Jan 26, 2017
 */
public class TestPCQuoteValidationMessage extends BaseTest {
    private GeneratePolicy myPolicyObject;
    private WebDriver driver;

    @Test()
    public void testGenerateStandardFireFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Pol", "Review")
                .withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);

        new GuidewireHelpers(driver).logout();

        PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());

        ArrayList<PolicyLocation> locationsListBOP = new ArrayList<PolicyLocation>();
        locationsListBOP.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        basicBOLine.locationList = locationsListBOP;

        myPolicyObject.busOwnLine = basicBOLine;
        myPolicyObject.StdFireBOP = true;
        myPolicyObject.lineSelection.add(LineSelection.Businessowners);
        myPolicyObject.addLineOfBusiness(ProductLineType.Businessowners, GeneratePolicyType.QuickQuote);

    }

    @Test(dependsOnMethods = {"testGenerateStandardFireFullApp"})
    public void validatePCCode() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickWorkOrderbyProduct(ProductLineType.Businessowners);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
        //Businessowners Manual Pubic protection class code validation result
        sideMenuStuff.clickSideMenuLocations();
        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        locations.clickLocationsLocationEdit(1);
        locations.setLocationsLocationAddress("New...");
        locations.setLocationsAddressLine1("752 2nd AVE");

        locations.setLocationsCity("Twin Falls");
        locations.setLocationsZipCode("83301");
        locations.locationStandardizeAddress();
        locations.clickLocationsOk();
        risk.Quote();

        boolean testFailed = false;
        ErrorHandling validationResults = new ErrorHandling(driver);

        String validationResult = validationResults.text_ErrorHandlingValidationResults().get(0).getText();
        String errorMessage = "";

        if (!validationResult.contains("Must enter a Manual Protection Class Code for Loc# 1/Bld# 1")) {
            testFailed = true;
            errorMessage = errorMessage + "Must enter a Manual Protection Class Code for Loc# 1/Bld# 1 Errormessage should be displayed.";
        }

        guidewireHelpers.logout();

        //StdFire Manual Pubic protection class code validation result
        new Login(driver).loginAndSearchJob(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        sideMenuStuff.clickSideMenuPropertyLocations();

        guidewireHelpers.editPolicyTransaction();

        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
//        propertyLocations.setAddressLine1("320 Ronglyn AVE");
//        propertyLocations.setCity("Idaho Falls");
//        propertyLocations.setZipCode("834014039");
        propertyLocations.clickStandardizeAddress();
        propertyLocations.clickOK();
        risk.Quote();

        if (!validationResult.contains("Must enter a Manual Protection Class Code for Loc# 1/Bld# 1")) {
            testFailed = true;
            errorMessage = errorMessage + "Must enter a Manual Protection Class Code for Loc# 1/Bld# 1 Errormessage should be displayed.";
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }
}
