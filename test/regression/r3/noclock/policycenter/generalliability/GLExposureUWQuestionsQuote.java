package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BlockingAction;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLExposureUWQuestions;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.GLClassCodes;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description This Class is to test the Exposure UW Questions and Validations for every Exposure Class Code(Eventually).
 * the 1400+ Questions are broken up into groups of 100 Per ClassCode. then per the day of the week and Even or Odd day it chooses what set to test.
 * Theory is that every Question will be tested twice per month.
 * @DATE Jun 8, 2016
 */
@QuarantineClass
public class GLExposureUWQuestionsQuote extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
    List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();
    List<GLClassCode> exposureClassCodes = new ArrayList<GLClassCode>();

    boolean testFailed = false;
    String failureList = "Questions that failed thier validation message:  \n";

    private WebDriver driver;

    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Create QQ Policy to test with.
     * @DATE Jun 8, 2016
     */
    @SuppressWarnings("serial")
    @Test//(enabled=false)
    public void createUWQuestionQQPolicy() throws Exception {

        setExposureClasscodes();

        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_18206.getValue()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("UW Questions")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());

    }//end createUWQuestionQQPolicy()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test All Questions requited at QQ with associated User Validations
     * @DATE Jun 8, 2016
     */
    @Test(dependsOnMethods = {"createUWQuestionQQPolicy"}, groups = {"QuickQuoteGL2"})
    public void validateUWExposureQuestionsQQ() throws Exception {

        testFailed = false;
        failureList = "Questions that failed thier validation message:  \n";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        validateUWQuestions();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + "FAILED TO ANSWER QUESTIONS. " + failureList);
        }
    }//end validateUWExposureQuestionsQQ()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Convert QQ policy to Full App.
     * @DATE Jun 8, 2016
     */
    @Test(dependsOnMethods = {"createUWQuestionQQPolicy"}, dependsOnGroups = {"QuickQuoteGL2"}, alwaysRun = true)
    public void convertToFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj.convertTo(driver, GeneratePolicyType.FullApp);
    }//end convertToFullApp()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test All Questions requited at FullApp with associated User Validations
     * @DATE Jun 8, 2016
     */
    @Test(dependsOnMethods = {"convertToFullApp"}, groups = {"FullAppGL2"})
    public void validateUWExposureQuestionsFullApp() throws Exception {

        testFailed = false;
        failureList = "Questions that failed thier validation message:  \n";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        validateUWQuestions();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + " FAILED TO ANSWER QUESTIONS. " + failureList);
        }
    }//end validateUWExposureQuestionsFullApp()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Sets all existing Questions to Basic so no errors when trying to Bind and Issue the policy.
     * @DATE Jun 8, 2016
     */
    @Test(dependsOnMethods = {"convertToFullApp"}, dependsOnGroups = {"FullAppGL2"})
    public void convertToPolicyBound() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.clickLocationSpecificQuestionsTab();
        for (CPPGeneralLiabilityExposures exposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            exposuresPage.fillOutBasicUWQuestionsQQ(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            exposuresPage.fillOutBasicUWQuestionsFullApp(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        }//end for

        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderQuote();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            risk.clickUWIssuesTab();
            GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
            if (quotePage.hasBlockQuoteRelease()) {
                GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
                riskAnaysis.handleBlockSubmit(myPolicyObj);
                SideMenuPC sideMenuStuff = new SideMenuPC(driver);
                sideMenuStuff.clickSideMenuQuote();
            }//end if hasBlockQuoteRelease()
        }//end if isPreQuoteDisplayed()


        guidewireHelpers.logout();

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);
    }//end convertToPolicyBound()


    @Test(dependsOnMethods = {"convertToPolicyBound"})
    public void convertToPolicyPolicyIssued() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }//end convertToPolicyPolicyIssued()


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description test all Exposure questions and validations during a policy change.
     * if a class code fails test will logout to reset the policy, log in, and start a new policy change.
     * @DATE Jun 8, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"convertToPolicyPolicyIssued"})
    public void testValidationsOnPolicyChange() throws Exception {

        testFailed = false;
        failureList = "Questions that failed thier validation message:  \n";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.startPolicyChange("To Validate UW Questions", null);

        for (GLClassCode classCode : exposureClassCodes) {
            try {
                if (classCode.getValue().equals("LoggOut")) {
                    guidewireHelpers.logout();
                    login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                    policyChange.startPolicyChange("To Validate UW Questions", null);
                    continue;
                }//end if

                SideMenuPC sideMenu = new SideMenuPC(driver);
                sideMenu.clickSideMenuGLExposures();
                GenericWorkorder genwo = new GenericWorkorder(driver);
                GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
                exposuresPage.clickExposureDetialsTab();
                exposuresPage.selectAll();
                exposuresPage.clickRemove();
                CPPGeneralLiabilityExposures exposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode.getValue());
                exposuresPage.clickAdd();
                myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                    this.add(exposure);
                }});
                exposuresPage.addExposure(exposure);
                exposuresPage.clickUnderwritingQuestionsTab();
                exposuresPage.clickLocationSpecificQuestionsTab();
                exposuresPage.fillOutUnderwritingQuestionsQQ(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
                    exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                }
                genwo.clickGenericWorkorderSaveDraft();
                if (exposuresPage.getInvalidQuestions() != null) {
                    testFailed = true;
                    failureList = failureList + "A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode + "\n";
                    System.out.println("A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode);
                    continue;
                }//end if

                sideMenu.clickSideMenuGLCoverages();//wait for page to load
                GenericWorkorderGeneralLiabilityCoveragesCPP coveragesPage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                coveragesPage.fillOutGeneralLiabilityCoverages(myPolicyObj);

                verifyBlockUserQuestions(exposure);
            } catch (Exception e) {
                System.out.println("FAILED SOMETHING FOR CLASS CODE: " + classCode);
                testFailed = true;
                failureList = failureList + "A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode + "\n";
                guidewireHelpers.logout();
                login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                policyChange.startPolicyChange("To Validate UW Questions", null);
            }//END CATCH
        }//END FOR

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + "FAILED TO ANSWER QUESTIONS. " + failureList);
        }
    }//end testValidationsOnPolicyChange()


    /*
     * this will loop thru all the class codes one by one and set every question, then check all page validations.
     * if one fails it is Caught, noted, and the test logs out and logs the user back in to reset the policy to a working state.
     * The logout list Items are to logout the user and log them back in to clear the server session memory.
     */
    @SuppressWarnings("serial")
    private void validateUWQuestions() throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        for (GLClassCode classCode : exposureClassCodes) {
            //			try {
            if (classCode.getValue().equals("LoggOut")) {
                guidewireHelpers.logout();
                login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                guidewireHelpers.editPolicyTransaction();
                continue;
            }

            SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuGLExposures();
            GenericWorkorder genwo = new GenericWorkorder(driver);
            GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
            exposuresPage.clickExposureDetialsTab();
            exposuresPage.selectAll();
            exposuresPage.clickRemove();
            CPPGeneralLiabilityExposures exposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode.getValue());
            exposuresPage.clickAdd();
            myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                this.add(exposure);
            }});
            exposuresPage.addExposure(exposure);

            exposuresPage.clickLocationSpecificQuestionsTab();
            exposuresPage.fillOutUnderwritingQuestionsQQ(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
                exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }
            genwo.clickGenericWorkorderSaveDraft();
            if (exposuresPage.getInvalidQuestions() != null) {
                testFailed = true;
                failureList = failureList + "A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode + "\n";
                System.out.println("A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode);
                continue;
            }

            sideMenu.clickSideMenuGLCoverages();//wait for page to load
            GenericWorkorderGeneralLiabilityCoveragesCPP coveragesPage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
            coveragesPage.fillOutGeneralLiabilityCoverages(myPolicyObj);


            verifyBlockUserQuestions(exposure);
        }//END FOR
    }//END validateUWQuestions()


    private void verifyBlockUserQuestions(CPPGeneralLiabilityExposures exposure) {
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorder genwo = new GenericWorkorder(driver);

        for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
            GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
            if (question.getBlockingAction().equals(BlockingAction.Blockuser)) {
                sideMenu.clickSideMenuGLExposures();
                exposuresPage.clickLocationSpecificQuestionsTab();

                //set question to generate block user condition
                glUWQuestions.setUnderwritingQuestion(exposure, question, glUWQuestions.getIncorrectAnswer(question));
                genwo.clickGenericWorkorderQuote();
                boolean found = false;

                for (String message : exposuresPage.getValidationMessages()) {
                    if (message.replaceAll("  ", " ").equals(question.getFailureMessage().replaceAll("  ", " "))) {
                        found = true;
                        break;
                    }//end if
                }//end for

                if (!found) {
                    testFailed = true;
                    failureList = failureList + "FAILIED TO BLOCK USER ON QUOTE: " + question.getQuestionText() + " | " + question.getFailureMessage() + "\n";
                }//end if

                //clear validation messages if any
                if (!driver.findElements(By.xpath("//Span[contains(text(), 'Clear')]")).isEmpty()) {
                    genwo.clickClear();
                }
                GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
                switch (driver.findElement(By.xpath("//span[contains(@class, 'g-title')]")).getText()) {
                    case "Exposures":
                    case "Coverages":
                        break;
                    case "Quote":
                    case "Risk Analysis":
                    case "Pre-Quote Issues":
                        guidewireHelpers.editPolicyTransaction();
                        break;
                }//end switch
                guidewireHelpers.editPolicyTransaction();
                glUWQuestions.setUnderwritingQuestion(exposure, question);
            }//END IF
        }//END FOR

    }//END verifyBlockUserQuestions()


    @SuppressWarnings("serial")
    List<GLClassCode> sundayList1 = new ArrayList<GLClassCode>() {{
        //questions 1-100
        this.add(GLClassCode.GL_10060);
        this.add(GLClassCode.GL_10071);
        this.add(GLClassCode.GL_10073);
        this.add(GLClassCode.GL_40111);
        this.add(GLClassCode.GL_10110);
        this.add(GLClassCode.GL_10115);
        this.add(GLClassCode.GL_10117);
        this.add(GLClassCode.GL_10145);
        this.add(GLClassCode.GL_10146);
        this.add(GLClassCode.GL_10150);
        this.add(GLClassCode.GL_10151);
        this.add(GLClassCode.GL_10220);
        this.add(GLClassCode.GL_10255);
        this.add(GLClassCode.GL_10256);
        this.add(GLClassCode.GL_10331);
        this.add(GLClassCode.GL_10332);
        this.add(GLClassCode.GL_11039);
        this.add(GLClassCode.GL_11138);
        this.add(GLClassCode.GL_11208);
        this.add(GLClassCode.GL_11201);
        this.add(GLClassCode.GL_11222);
        this.add(GLClassCode.GL_11258);
        this.add(GLClassCode.GL_11259);
        this.add(GLClassCode.GL_11288);
        this.add(GLClassCode.GL_12393);
        this.add(GLClassCode.GL_13314);
        this.add(GLClassCode.GL_13351);
        this.add(GLClassCode.GL_13352);
        this.add(GLClassCode.GL_13410);
        this.add(GLClassCode.GL_13461);
        this.add(GLClassCode.GL_13621);
        this.add(GLClassCode.GL_13670);
        this.add(GLClassCode.GL_13716);
        this.add(GLClassCode.GL_13720);
        this.add(GLClassCode.GL_14279);
        this.add(GLClassCode.GL_15060);
        this.add(GLClassCode.GL_15061);
        this.add(GLClassCode.GL_15063);
        this.add(GLClassCode.GL_15300);
        this.add(GLClassCode.GL_15405);
        this.add(GLClassCode.GL_15699);
        this.add(GLClassCode.GL_16404);
        this.add(GLClassCode.GL_16676);
        this.add(GLClassCode.GL_16694);
        this.add(GLClassCode.GL_16705);
        this.add(GLClassCode.GL_16722);
        this.add(GLClassCode.GL_16750);
        this.add(GLClassCode.GL_16751);
        this.add(GLClassCode.GL_16881);
        this.add(GLClassCode.GL_16890);
        this.add(GLClassCode.GL_18206);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> sundayList2 = new ArrayList<GLClassCode>() {{
        //questions 1300 - on
        this.add(GLClassCode.GL_16915);
        this.add(GLClassCode.GL_16916);
        this.add(GLClassCode.GL_16930);
        this.add(GLClassCode.GL_16931);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> mondayList = new ArrayList<GLClassCode>() {{
        //question 100-200
        this.add(GLClassCode.GL_18206);
        this.add(GLClassCode.GL_18437);
        this.add(GLClassCode.GL_18438);
        this.add(GLClassCode.GL_18501);
        this.add(GLClassCode.GL_18507);
        this.add(GLClassCode.GL_18920);
        this.add(GLClassCode.GL_19007);
        this.add(GLClassCode.GL_19051);
        this.add(GLClassCode.GL_40059);
        this.add(GLClassCode.GL_40061);
        this.add(GLClassCode.GL_40115);
        this.add(GLClassCode.GL_40117);
        this.add(GLClassCode.GL_41650);
        this.add(GLClassCode.GL_41667);
        this.add(GLClassCode.GL_41668);
        this.add(GLClassCode.GL_41669);
        this.add(GLClassCode.GL_41670);
        this.add(GLClassCode.GL_41675);
        this.add(GLClassCode.GL_41677);
        this.add(GLClassCode.GL_44070);
        this.add(GLClassCode.GL_44276);
        this.add(GLClassCode.GL_44277);
        this.add(GLClassCode.GL_44280);
        this.add(GLClassCode.GL_45190);
        this.add(GLClassCode.GL_45192);
        this.add(GLClassCode.GL_45450);
        this.add(GLClassCode.GL_45678);
        this.add(GLClassCode.GL_45819);
        this.add(GLClassCode.GL_45901);
        this.add(GLClassCode.GL_46202);
        this.add(GLClassCode.GL_46426);
        this.add(GLClassCode.GL_46427);
        this.add(GLClassCode.GL_46604);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> mondayList2 = new ArrayList<GLClassCode>() {{
        //questions 700 - 800
        this.add(GLClassCode.GL_70412);
        this.add(GLClassCode.GL_58161);
        this.add(GLClassCode.GL_50911);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> tuesdayList = new ArrayList<GLClassCode>() {{
        //questions 200-300
        this.add(GLClassCode.GL_46604);
        this.add(GLClassCode.GL_46671);
        this.add(GLClassCode.GL_46881);
        this.add(GLClassCode.GL_46773);
        this.add(GLClassCode.GL_47050);
        this.add(GLClassCode.GL_47051);
        this.add(GLClassCode.GL_47052);
        this.add(GLClassCode.GL_47146);
        this.add(GLClassCode.GL_47147);
        this.add(GLClassCode.GL_47367);
        this.add(GLClassCode.GL_47474);
        this.add(GLClassCode.GL_48557);
        this.add(GLClassCode.GL_48558);
        this.add(GLClassCode.GL_48600);
        this.add(GLClassCode.GL_48636);
        this.add(GLClassCode.GL_48727);
        this.add(GLClassCode.GL_49181);
        this.add(GLClassCode.GL_49451);
        this.add(GLClassCode.GL_49452);
        this.add(GLClassCode.GL_51896);
        this.add(GLClassCode.GL_51350);
        this.add(GLClassCode.GL_51351);
        this.add(GLClassCode.GL_51352);
        this.add(GLClassCode.GL_51999);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> tuesdayList2 = new ArrayList<GLClassCode>() {{
        //questions 800 - 900
        this.add(GLClassCode.GL_50911);
        this.add(GLClassCode.GL_59211);
        this.add(GLClassCode.GL_58165);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> wednessdayList = new ArrayList<GLClassCode>() {{
        //questions 300-400
        this.add(GLClassCode.GL_51999);
        this.add(GLClassCode.GL_52002);
        this.add(GLClassCode.GL_53001);
        this.add(GLClassCode.GL_53732);
        this.add(GLClassCode.GL_53733);
        this.add(GLClassCode.GL_55371);
        this.add(GLClassCode.GL_56391);
        this.add(GLClassCode.GL_56760);
        this.add(GLClassCode.GL_56916);
        this.add(GLClassCode.GL_57001);
        this.add(GLClassCode.GL_57002);
        this.add(GLClassCode.GL_58397);
        this.add(GLClassCode.GL_58408);
        this.add(GLClassCode.GL_58409);
        this.add(GLClassCode.GL_58456);
        this.add(GLClassCode.GL_58457);
        this.add(GLClassCode.GL_58458);
        this.add(GLClassCode.GL_58759);
        this.add(GLClassCode.GL_58922);
        this.add(GLClassCode.GL_59057);
        this.add(GLClassCode.GL_59058);
        this.add(GLClassCode.GL_59223);
        this.add(GLClassCode.GL_59482);
        this.add(GLClassCode.GL_59713);
        this.add(GLClassCode.GL_59722);
        this.add(GLClassCode.GL_59947);
        this.add(GLClassCode.GL_59984);
        this.add(GLClassCode.GL_60010);
        this.add(GLClassCode.GL_60011);
        this.add(GLClassCode.GL_61226);
        this.add(GLClassCode.GL_61227);
        this.add(GLClassCode.GL_62003);
        this.add(GLClassCode.GL_63218);
        this.add(GLClassCode.GL_63217);
        this.add(GLClassCode.GL_63220);
        this.add(GLClassCode.GL_63219);
        this.add(GLClassCode.GL_67512);
        this.add(GLClassCode.GL_67513);
        this.add(GLClassCode.GL_67634);
        this.add(GLClassCode.GL_67635);
        this.add(GLClassCode.GL_68439);
        this.add(GLClassCode.GL_68500);
        this.add(GLClassCode.GL_68604);
        this.add(GLClassCode.GL_68606);
        this.add(GLClassCode.GL_68707);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> wednessdayList2 = new ArrayList<GLClassCode>() {{
        //questions 900 - 1000
        this.add(GLClassCode.GL_58165);
        this.add(GLClassCode.GL_58166);
        this.add(GLClassCode.GL_16819);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> thursdayList = new ArrayList<GLClassCode>() {{
        //questions 400 - 500
        this.add(GLClassCode.GL_68707);
        this.add(GLClassCode.GL_68706);
        this.add(GLClassCode.GL_91127);
        this.add(GLClassCode.GL_94381);
        this.add(GLClassCode.GL_91177);
        this.add(GLClassCode.GL_91179);
        this.add(GLClassCode.GL_91302);
        this.add(GLClassCode.GL_91302);
        this.add(GLClassCode.GL_91551);
        this.add(GLClassCode.GL_91577);
        this.add(GLClassCode.GL_91581);
        this.add(GLClassCode.GL_91583);
        this.add(GLClassCode.GL_91585);
        this.add(GLClassCode.GL_91591);
        this.add(GLClassCode.GL_91636);
        this.add(GLClassCode.GL_91805);
        this.add(GLClassCode.GL_92101);
        this.add(GLClassCode.GL_92215);
        this.add(GLClassCode.GL_94007);
        this.add(GLClassCode.GL_94225);
        this.add(GLClassCode.GL_94444);
        this.add(GLClassCode.GL_95357);
        this.add(GLClassCode.GL_95410);
        this.add(GLClassCode.GL_95647);
        this.add(GLClassCode.GL_95648);
        this.add(GLClassCode.GL_96409);
        this.add(GLClassCode.GL_96702);
        this.add(GLClassCode.GL_96703);
        this.add(GLClassCode.GL_96816);
        this.add(GLClassCode.GL_97002);
        this.add(GLClassCode.GL_97003);
        this.add(GLClassCode.GL_97047);
        this.add(GLClassCode.GL_97050);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> thursdayList2 = new ArrayList<GLClassCode>() {{
        //questions 1000 - 1100
        this.add(GLClassCode.GL_16819);
        this.add(GLClassCode.GL_16820);
        this.add(GLClassCode.GL_16900);
        this.add(GLClassCode.GL_16901);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> fridayList = new ArrayList<GLClassCode>() {{
        //questions 500 - 600
        this.add(GLClassCode.GL_97050);
        this.add(GLClassCode.GL_97220);
        this.add(GLClassCode.GL_97222);
        this.add(GLClassCode.GL_97223);
        this.add(GLClassCode.GL_97652);
        this.add(GLClassCode.GL_97653);
        this.add(GLClassCode.GL_98303);
        this.add(GLClassCode.GL_98306);
        this.add(GLClassCode.GL_98309);
        this.add(GLClassCode.GL_98482);
        this.add(GLClassCode.GL_98483);
        this.add(GLClassCode.GL_98502);
        this.add(GLClassCode.GL_98555);
        this.add(GLClassCode.GL_98597);
        this.add(GLClassCode.GL_98598);
        this.add(GLClassCode.GL_91340);
        this.add(GLClassCode.GL_91342);
        this.add(GLClassCode.GL_98640);
        this.add(GLClassCode.GL_98751);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> fridayList2 = new ArrayList<GLClassCode>() {{
        //questions 1100 - 1200
        this.add(GLClassCode.GL_16901);
        this.add(GLClassCode.GL_16902);
        this.add(GLClassCode.GL_16905);
        this.add(GLClassCode.GL_16906);
    }};

    @SuppressWarnings("serial")
    List<GLClassCode> saturdayList = new ArrayList<GLClassCode>() {{
        //questions 600 - 700
        this.add(GLClassCode.GL_98751);
        this.add(GLClassCode.GL_98820);
        this.add(GLClassCode.GL_98884);
        this.add(GLClassCode.GL_98967);
        this.add(GLClassCode.GL_99080);
        this.add(GLClassCode.GL_99303);
        this.add(GLClassCode.GL_99310);
        this.add(GLClassCode.GL_99315);
        this.add(GLClassCode.GL_99321);
        this.add(GLClassCode.GL_99505);
        this.add(GLClassCode.GL_99506);
        this.add(GLClassCode.GL_99507);
        this.add(GLClassCode.GL_99600);
        this.add(GLClassCode.GL_99614);
        this.add(GLClassCode.GL_99709);
        this.add(GLClassCode.GL_99718);
        this.add(GLClassCode.GL_99746);
        this.add(GLClassCode.GL_99777);
        this.add(GLClassCode.GL_99793);
        this.add(GLClassCode.GL_99917);
        this.add(GLClassCode.GL_99938);
        this.add(GLClassCode.GL_99943);
        this.add(GLClassCode.GL_99946);
        this.add(GLClassCode.GL_99948);
        this.add(GLClassCode.GL_99969);
        this.add(GLClassCode.GL_70412);


    }};

    @SuppressWarnings("serial")
    List<GLClassCode> saturdayList2 = new ArrayList<GLClassCode>() {{
        //questions 1200 - 1300
        //		this.add(GLClassCode.GL_16906);
        //		this.add(GLClassCode.GL_16910);
        //		this.add(GLClassCode.GL_16911);
        //		this.add(GLClassCode.GL_16915);

        //		this.add(GLClassCode.GL_16916);
        //		this.add(GLClassCode.GL_16930);
        //		this.add(GLClassCode.GL_16931);
        //questions 1300 +
        this.add(GLClassCode.GL_16915);
        this.add(GLClassCode.GL_16916);
        this.add(GLClassCode.GL_16930);
        this.add(GLClassCode.GL_16931);
        this.add(GLClassCode.GL_45771);
    }};


    private void setExposureClasscodes() {
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                if (evenOddDay()) {
                    exposureClassCodes = sundayList1;
                } else {
                    exposureClassCodes = sundayList2;
                }
                break;
            case 2:
                if (evenOddDay()) {
                    exposureClassCodes = mondayList;
                } else {
                    exposureClassCodes = mondayList2;
                }
                break;
            case 3:
                if (evenOddDay()) {
                    exposureClassCodes = tuesdayList;
                } else {
                    exposureClassCodes = tuesdayList2;
                }
                break;
            case 4:
                if (evenOddDay()) {
                    exposureClassCodes = wednessdayList;
                } else {
                    exposureClassCodes = wednessdayList2;
                }
                break;
            case 5:
                if (evenOddDay()) {
                    exposureClassCodes = thursdayList;
                } else {
                    exposureClassCodes = thursdayList2;
                }
                break;
            case 6:
                if (evenOddDay()) {
                    exposureClassCodes = fridayList;
                } else {
                    exposureClassCodes = fridayList2;
                }
                break;
            case 7:
                if (evenOddDay()) {
                    exposureClassCodes = saturdayList;
                } else {
                    exposureClassCodes = saturdayList2;
                }
                break;
        }//END SWITCH

    }


    private boolean evenOddDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth % 2 == 0;
    }
}
























