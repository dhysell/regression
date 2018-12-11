package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;

import java.util.List;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/229683608520">US15411</a>
 * @Description As an agent, PA, CSR, SA, coder or underwriter I want to add any type of building on section I and on foundation type have a drop down with only 2 choices: 'Foundation' or 'No Foundation'.
 * <p>
 * Steps to get there:
 * <p>
 * Start policy with section I add different coverage A and coverage E buildings
 * See that choice of foundation on all buildings has only 2 choices: 'Foundation' and 'No Foundation'
 * <p>
 * Acceptance criteria:
 * <p>
 * Ensure that on new submission the only choices for coverage A and coverage E buildings on foundation drop down are 'Foundation' or 'No Foundation'
 * Ensure that on a policy change adding a new building the only choices for coverage A and coverage E buildings on foundation drop down are 'Foundation' or 'No Foundation'
 * Ensure that on all editable rewrite jobs when adding a new building the only choices for coverage A and coverage E buildings on foundation drop down are 'Foundation' or 'No Foundation'
 * @DATE June 28, 2018
 */

public class US15411_FoundationDropDown extends BaseTest {

    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void VerifyFoundationOnNewPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .isDraft()
                    .build(GeneratePolicyType.QuickQuote);


        new Login(driver).loginAndSearchAccountByAccountNumber("su", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);

        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(PropertyTypePL.ResidencePremises);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(ConstructionTypePL.Frame);
        List<String> foundationTypes = pcPropertyConstructionPage.getFoundationTypeValues();


        Assert.assertEquals(foundationTypes.size(), 4, "There should only be 3 foundation types + none");
        Assert.assertTrue(foundationTypes.contains("Foundation"), "This type of foundation is required, but was not found");
        Assert.assertTrue(foundationTypes.contains("No Foundation"), "This type of foundation is required, but was not found");
        Assert.assertTrue(foundationTypes.contains("Pier and Beam"), "This type of foundation is required, but was not found");
    }

    @Test(enabled = true)
    public void VerifyFoundationOnPolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
 
            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .build(GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchPolicyByAccountNumber("su", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        StartPolicyChange pcPolicyChange = new StartPolicyChange(driver);

        pcPolicyChange.startPolicyChange("This is a change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(PropertyTypePL.ResidencePremises);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(ConstructionTypePL.Frame);
        List<String> foundationTypes = pcPropertyConstructionPage.getFoundationTypeValues();


        Assert.assertEquals(foundationTypes.size(), 4, "There should only be 3 foundation types + none");
        Assert.assertTrue(foundationTypes.contains("Foundation"), "This type of foundation is required, but was not found");
        Assert.assertTrue(foundationTypes.contains("No Foundation"), "This type of foundation is required, but was not found");
        Assert.assertTrue(foundationTypes.contains("Pier and Beam"), "This type of foundation is required, but was not found");
    }


}




















