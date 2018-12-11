package regression.r2.noclock.policycenter.renewaltransition.subgroup2;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US8742 : [Part II] PL - Policy Change During Renewal
 * @Description -  Issue policy,  Renew policy, create a policy change between day -50 to -22 and add Endorsements, Conditions  and exclusions
 * verify All declarations are suppressed (Insured and Additional Interest), also verify Endorsements, conditions and exclusions will infer but will print at day -21 ("date will print" date should be -21 days from renewal term)
 * @DATE Feb 08, 2017
 */
@QuarantineClass
public class TestPLPolicyChangeDuringRenewal extends BaseTest {

    private GeneratePolicy Squire_PolicyObj;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    public void testSquireIssuqnce() throws Exception {
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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("SQ", "ProdRules")
                .withPolTermLengthDays(40)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testSquireIssuqnce"})
    public void testRenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.renewPolicyManually(RenewalCode.Renew_Good_Risk, Squire_PolicyObj);

    }

    @Test(dependsOnMethods = {"testRenewalJob"})
    public void testPolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 40);

        //start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        //Edit property
        propertyDetails.clickViewOrEditBuildingButton(1);

        //Add Insured
        propertyDetails.addAddtionalInsured();
        propertyDetails.setAddtionalInsuredName("SteveJobs");
        propertyDetails.clickOk();

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverage.clickSectionIICoveragesTab();
        coverage.clickCoveragesExclusionsAndConditions();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exclusions = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);

        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        // 105 form
        exclusions.clickSpecialEndorsementForProperty105(descs);

        //280 form
        exclusions.clickCanineExclusionEndorsement280();

        // 205 form
        exclusions.clickSpecialEndorsementForLiability205(descs);

        //207 Form
        exclusions.clickVendorAsAdditionalInsuredEndorsement207(descs);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        StartPolicyChange change = new StartPolicyChange(driver);
        change.clickIssuePolicy();

    }

    @Test(dependsOnMethods = {"testPolicyChange"})
    public void testVerifyDeclarationAndDateWillPrint() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), Squire_PolicyObj.squire.getPolicyNumber());
        PolicySummary summaryPage = new PolicySummary(driver);
        summaryPage.clickCompletedTransactionByType(TransactionType.Policy_Change);
        SideMenuPC sidemenu = new SideMenuPC(driver);

        //Verify Declarations not displayed in Policy change
        sidemenu.clickSideMenuForms();
        GenericWorkorderForms forms = new GenericWorkorderForms(driver);
        boolean testFailed = false;
        String errorMessage = "";

        String[] unExpectedDeclaration = {"City Squire Policy Declarations"};

        for (String formsText : unExpectedDeclaration) {
            if (forms.getFormDescriptionsFromTable().contains(formsText)) {
                testFailed = true;
                errorMessage = errorMessage + "Unexpected declaration: " + unExpectedDeclaration + " is displayed.";
            }
        }

        //Verify "Date Will Print"  date -21 days from renewal term
        sidemenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Policy Change");
        docs.clickSearch();

        String[] documents = {"Special Endorsement for Liability", "Special Endorsement for Property", "Vendor as Additional Insured Endorsement", "Canine Exclusion Endorsement"};
        Date expectedDate = DateUtils.dateAddSubtract(Squire_PolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -21);
        String expectedPrintDate = DateUtils.dateFormatAsString("MM/dd/yyyy", expectedDate);

        for (String document : documents) {
            String printDate = docs.getDateWillPrintColoumnDate(document, "Date Will Print");
            if (!printDate.equals(expectedPrintDate)) {
                testFailed = true;
                errorMessage = errorMessage + "Documents Date Will Print Date " + printDate + " should be same as -21 days from renewal term. /n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }
}
