package regression.r2.noclock.policycenter.submission_misc.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.QuoteType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Names;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.NamesHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US12376: **HOT FIX** Agent can order insurance score at Quick Quote, US13069: Insurance (cbr) score fields need to be required
 * @RequirementsLink <a href="http://projects.idfbins.com/gwintegrations/Shared%20Documents/Story%20Cards/PC8%20-%20Common%20-%20Integrations%20-%20LexisNexis%20Reports.xlsx">PC8 - Common - Integrations - LexisNexis Reports </a>
 * @Description : Validating mandatory fields after selecting the name change and address change
 * @DATE Sep 12, 2017
 */
public class TestPLOrderInsuranceScoreInQQ extends BaseTest {
    public GeneratePolicy myPolicyObjPL = null;

    private WebDriver driver;

    @Test
    public void testOrderInsuranceScoreInQQFA() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));

        PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
        location1.setPlNumAcres(10);
        location1.setPlNumResidence(8);
        locationsList.add(location1);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("OrderInsurance", "QQReport")
                .withSquireEligibility(SquireEligibility.random())
                .withoutOrderingCreditReport()
                .build(GeneratePolicyType.QuickQuote);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchSubmission(this.myPolicyObjPL.agentInfo.getAgentUserName(), this.myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		myPolicyObjPL.squire.alwaysOrderCreditReport = true;
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.selectCreditReportIndividual(this.myPolicyObjPL.pniContact.getFirstName());
        String errorMessage = "";
        if (!creditReport.checkOrderInsuranceReportButton()) {
            errorMessage = "Order Insurance Report button not exists in QQ \n";
        }
        creditReport.fillOutCreditReport(myPolicyObjPL);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.performRiskAnalysisAndQuoteQQ(myPolicyObjPL);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();

        GenericWorkorderQualification qualifications = new GenericWorkorderQualification(driver);
        qualifications.fillOutFullAppQualifications(myPolicyObjPL);

        sideMenu.clickSideMenuPLInsuranceScore();
        if (creditReport.checkOrderInsuranceReportButton()) {
            errorMessage = "Order Insurance Report button exists in FA after selecting in QQ \n";
        }
        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    @Test(dependsOnMethods = {"testOrderInsuranceScoreInQQFA"})
    private void testValidateMandatoryInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters genericPLUnderwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchSubmission(genericPLUnderwriter.getUnderwriterUserName(), genericPLUnderwriter.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.clickEditInsuranceReport();
        creditReport.markLivedHere6Months(true);
        creditReport.sendArbitraryKeys(Keys.TAB);
        creditReport.markNameChanged6Months(true);
        creditReport.sendArbitraryKeys(Keys.TAB);
        creditReport.clickNext();
        String errorMessage = "";
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Address Line 1 : Missing required field \"Address Line 1\"")) {
            errorMessage = errorMessage + " Address Line 1 is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("City : Missing required field \"City\"")) {
            errorMessage = errorMessage + "City : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("State : Missing required field \"State\"")) {
            errorMessage = errorMessage + "State : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("ZIP Code : Missing required field \"ZIP Code\"")) {
            errorMessage = errorMessage + "ZIP Code : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("First Name : Missing required field \"First Name\"")) {
            errorMessage = errorMessage + "First Name : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Last Name : Missing required field \"Last Name\"")) {
            errorMessage = errorMessage + "Last Name : Missing required field is not displayed. \n";
        }

        AddressInfo newAddress = new AddressInfo();
        creditReport.setAddress(newAddress.getLine1());
        creditReport.setCity(newAddress.getCity());
        creditReport.setState(newAddress.getState());
        creditReport.setZip(newAddress.getZip());

        // name change details
        creditReport.setInsuranceFormerFirstName(myPolicyObjPL.pniContact.getFirstName() + "NEW");
        creditReport.setInsuranceFormerLastName("UpdatedNEW");
        creditReport.clickGenericWorkorderSaveDraft();
        creditReport.clickOrderReport();
        if (creditReport.getInsuranceStatus().contains("Error")) {
            if (creditReport.getInsuranceErrorDescription().isEmpty() || creditReport.getInsuranceErrorDescription() == null) {
                errorMessage = errorMessage + " Insurance Error description is not displayed. \n";
            }
        }


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }

    @Test()
    public void testOrderInsuranceScoreInQQ() throws Exception {
        Agents agent = AgentsHelper.getRandomAgent();
        Names name = NamesHelper.getRandomName();
        AddressInfo address = new AddressInfo();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());

        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        String firstNameIns = "OrderIns" + StringsUtils.generateRandomNumberDigits(9);
        newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Person, name.getLastName(), firstNameIns,
                "QQ" + StringsUtils.generateRandomNumberDigits(10), null, address.getCity(), address.getState(), address.getZip());
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.fillOutPrimaryAddressFields(address);
        createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
        createAccountPage.setDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -35));
        createAccountPage.clickCreateAccountUpdate();

        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
        selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, ProductLineType.StandardFire);

        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.setPolicySelection(true);
        eligibilityPage.clickNext();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
        policyInfoPage.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        policyInfoPage.setPolicyInfoRatingCounty("Bannock");
        new GuidewireHelpers(driver).clickNext();

        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.selectCreditReportIndividual(firstNameIns);
        String errorMessage = "";
        if (!creditReport.checkOrderInsuranceReportButton()) {
            errorMessage = "Order Insurance Report button not exists in QQ \n";
        }
        creditReport.markLivedHere6Months(true);
        createAccountPage.sendArbitraryKeys(Keys.TAB);
        creditReport.markNameChanged6Months(true);
        creditReport.sendArbitraryKeys(Keys.TAB);
        creditReport.clickNext();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Address Line 1 : Missing required field \"Address Line 1\"")) {
            errorMessage = errorMessage + " Address Line 1 is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("City : Missing required field \"City\"")) {
            errorMessage = errorMessage + "City : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("State : Missing required field \"State\"")) {
            errorMessage = errorMessage + "State : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("ZIP Code : Missing required field \"ZIP Code\"")) {
            errorMessage = errorMessage + "ZIP Code : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("First Name : Missing required field \"First Name\"")) {
            errorMessage = errorMessage + "First Name : Missing required field is not displayed. \n";
        }

        if (!guidewireHelpers.errorMessagesExist() && !guidewireHelpers.getFirstErrorMessage().contains("Last Name : Missing required field \"Last Name\"")) {
            errorMessage = errorMessage + "Last Name : Missing required field is not displayed. \n";
        }
        AddressInfo newAddress = new AddressInfo();
        creditReport.setAddress(newAddress.getLine1());
        creditReport.setCity(newAddress.getCity());
        creditReport.setState(newAddress.getState());
        creditReport.setZip(newAddress.getZip());

        // name change details
        creditReport.setInsuranceFormerFirstName(firstNameIns + "NEW");
        creditReport.setInsuranceFormerLastName("UpdatedNEW");
        creditReport.clickGenericWorkorderSaveDraft();
        creditReport.clickOrderReport();
        if (creditReport.getInsuranceStatus().contains("Error")) {
            if (creditReport.getInsuranceErrorDescription().isEmpty() || creditReport.getInsuranceErrorDescription() == null) {
                errorMessage = errorMessage + " Insurance Error description is not displayed. \n";
            }
        }


        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }


}
