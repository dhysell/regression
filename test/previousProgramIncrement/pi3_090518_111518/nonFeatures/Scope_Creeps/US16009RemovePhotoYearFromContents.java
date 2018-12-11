package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.renewal.StartRenewal;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author swathiAkarapu
 * @Requirement US16009
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/246020410536">US16009</a>
 * @Description
 *Complete DE7557 first and/or simultaneously in the same iteration
 *
 * As a home office or county office user, I do not want the photo year field to appear for contents, as we don't require photos for contents and the field is not needed.
 *
 * Steps to get there:
 * Create a policy as an agent with sections I & II and with contents coverage
 * Create a policy as a PA with sections I & II and with contents coverage
 * Do policy change on existing policy adding contents coverage. Do as new and as removing homeowners then adding contents
 * Acceptance criteria:
 * Ensure that the photo year field does not appear for contents.
 * Ensure that no validation or UW issue appears related to contents for photo year not being filled in
 * Ensure that agent, PA, underwriter and coder can add or edit contents without getting the photo year field
 * Ensure that the photo year is cleared out on any Cov C items it may have been entered on
 * @DATE October 9, 2018
 */
public class US16009RemovePhotoYearFromContents extends BaseTest {

private   GeneratePolicy myPolicyObject= null;
private WebDriver driver;
    private  void draftGenerate() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("remove", "photoYear")
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    private  void generate() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("remove", "photoYear")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void verifyPhotoYearOnNewPolicy() throws Exception {
        draftGenerate();


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSquirePropertyPage.removeBuilding("1");
        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(repository.gw.enums.Property.PropertyTypePL.Contents);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);

        softAssert.assertFalse( pcPropertyConstructionPage.isPhotoYearExist(), "Photo Year Exist for Contents");
        pcPropertyConstructionPage.clickOk();


        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        boolean msPhotoYearFound = isMsPhotoYearFound(pcWorkOrder);
        Assert.assertFalse(msPhotoYearFound, "Validation for MS or Photo year was displayed and it shouldn't be after filling them in");
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        int buildingNo=propertyDetail.getSelectedBuildingNum();

        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        setContentsCoverage(buildingNo);


        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        boolean messageFound = false;

        for (int i = 0; i < pcRiskUnderwriterIssuePage.getUWIssuesList().size(); i++) {
            String currentUWIssueText = pcRiskUnderwriterIssuePage.getUWIssuesList().get(i).getText();
                if (currentUWIssueText.contains("Photo")) {
                    messageFound = true;
                    break;
                }
        }
        softAssert.assertFalse(messageFound, "Photo UW issue  exist for contents");
        softAssert.assertAll();
    }

    private boolean isMsPhotoYearFound(repository.pc.workorders.generic.GenericWorkorder pcWorkOrder) {
        boolean msPhotoYearFound = false;
        for (WebElement ele : pcWorkOrder.getValidationResultsList()) { // This validation will always show now after hitting okay without them filled
            if (ele.getText().contains("upload the MS") || ele.getText().contains("upload the photos"))
                msPhotoYearFound = true;
        }
        return msPhotoYearFound;
    }

    @Test(enabled = true)
    public void verifyPhotoYearOnPolicyChange() throws Exception {
        generate();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details pcPropertyDetailsPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction pcPropertyConstructionPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        SoftAssert softAssert = new SoftAssert();

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSquirePropertyPage.removeBuilding("1");
        pcSquirePropertyPage.clickAdd();
        pcPropertyDetailsPage.setPropertyType(repository.gw.enums.Property.PropertyTypePL.Contents);
        pcPropertyDetailsPage.clickPropertyConstructionTab();
        pcPropertyConstructionPage.setConstructionType(repository.gw.enums.Building.ConstructionTypePL.Frame);

        softAssert.assertFalse( pcPropertyConstructionPage.isPhotoYearExist(), "Photo Year Exist for Contents");
        pcPropertyConstructionPage.clickOk();


        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        boolean msPhotoYearFound = isMsPhotoYearFound(pcWorkOrder);
        Assert.assertFalse(msPhotoYearFound, "Validation for MS or Photo year was displayed and it shouldn't be after filling them in");
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        int buildingNo=propertyDetail.getSelectedBuildingNum();

        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        setContentsCoverage(buildingNo);


        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        boolean messageFound = false;
        for (int i = 0; i < pcRiskUnderwriterIssuePage.getUWIssuesList().size(); i++) {
            String currentUWIssueText = pcRiskUnderwriterIssuePage.getUWIssuesList().get(i).getText();
            if (currentUWIssueText.contains("Photo")) {
                messageFound = true;
                break;
            }
        }
        softAssert.assertFalse(messageFound, "Photo UW issue  exist for contents");
        softAssert.assertAll();
    }

    private void setContentsCoverage(int buildingNo) {
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        coverages.clickSpecificBuilding(1,buildingNo);
        coverages.setCoverageCLimit(15000);
        coverages.setCoverageCValuation(new PLPropertyCoverages());
        coverages.selectCoverageCCoverageType(repository.gw.enums.CoverageType.BroadForm);
    }

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        PLPolicyLocationProperty newProperty2 = new PLPolicyLocationProperty();
        newProperty2.setpropertyType(repository.gw.enums.Property.PropertyTypePL.Contents);
        Squire mySquire = new Squire();


        PolicyLocation newLocation2 = new PolicyLocation();
        ArrayList<PLPolicyLocationProperty> propertyLocationList2 = new ArrayList<PLPolicyLocationProperty>();
        propertyLocationList2.add(newProperty2);
        newLocation2.setPropertyList(propertyLocationList2);
        ArrayList<PolicyLocation> newLocationList = new ArrayList<PolicyLocation>();
         newLocationList.add(newLocation2);
        mySquire.propertyAndLiability.locationList = newLocationList;


        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withSquire(mySquire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -360))
                .withInsFirstLastName("contents", "renew")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void  verifyPropertyPhotosNotNeededOnContents() throws Exception {

          generatePolicy();

        new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(myPolicyObject.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        StartRenewal renewal = new StartRenewal(driver);
        renewal.waitForPreRenewalDirections();
        if (summaryPage.checkIfActivityExists("Pre-Renewal Review for Squire")) {
            summaryPage.clickViewPreRenewalDirection();

            boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
            if (preRenewalDirectionExists) {

                preRenewalPage.clickViewInPreRenewalDirection();

                if (preRenewalPage.checkPreRenewalDirectionWithExpectedCode("Property Photos Needed")) {
                    Assert.fail("Property Photos Needed: not required on Contents ");
                }
            }
        }


    }

    }
