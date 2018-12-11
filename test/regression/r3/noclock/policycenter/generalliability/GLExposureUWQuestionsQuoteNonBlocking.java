package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
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
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.GLClassCodes;

/**
 * @Author rlonardo
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description This Class is to test the Exposure UW Questions and Validations for every non-user-blocking Exposure Class Code(Eventually).
 * the 1400+ Questions are broken up into groups of 100 Per ClassCode. then per the day of the week and Even or Odd day it chooses what set to test.
 * Theory is that every Question will be tested twice per month.
 * @DATE April 10, 2017
 */
@QuarantineClass
public class GLExposureUWQuestionsQuoteNonBlocking extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
    List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();
    List<GLClassCode> exposureClassCodes = new ArrayList<GLClassCode>();
    CPPGeneralLiabilityExposures currentExposure;

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

    /*
     * this will loop thru all the class codes one by one and set every question, then check all page validations.
     * if one fails it is Caught, noted, and the test logs out and logs the user back in to reset the policy to a working state.
     * The logout list Items are to logout the user and log them back in to clear the server session memory.
     */
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
            currentExposure = exposure;
            exposuresPage.clickAdd();

            ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
            exposures.add(exposure);

            //get dependent exposures
            ArrayList<GLClassCode> dependentClassCodes = getDependentClassCodes(classCode);
            for (GLClassCode glCode : dependentClassCodes) {
                exposures.add(new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), glCode.getValue()));
            }

            myPolicyObj.generalLiabilityCPP.setCPPGeneralLiabilityExposures(exposures);

            for (CPPGeneralLiabilityExposures glExposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
                exposuresPage.addExposure(glExposure);
            }

            exposuresPage.clickLocationSpecificQuestionsTab();
            for (CPPGeneralLiabilityExposures glExposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
                exposuresPage.fillOutUnderwritingQuestionsQQ(glExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }
            if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.QuickQuote)) {
                for (CPPGeneralLiabilityExposures glExposure : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
                    exposuresPage.fillOutUnderwritingQuestionsFULLAPP(glExposure, myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                }
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

            verifyNonBlockUserQuestions(exposure);
        }//END FOR
    }//END validateUWQuestions()


    private void verifyNonBlockUserQuestions(CPPGeneralLiabilityExposures exposure) {
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorder genwo = new GenericWorkorder(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
            GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
            if (!question.getBlockingAction().equals(BlockingAction.Blockuser) && !question.getFailureMessage().equals("-")) {
                sideMenu.clickSideMenuGLExposures();
                exposuresPage.clickLocationSpecificQuestionsTab();

                //set question with children to generate UW issue condition
                glUWQuestions.setUnderwritingQuesiton_AndChildQuestions(exposure, question, glUWQuestions.getIncorrectAnswer(question));
                genwo.clickGenericWorkorderQuote();
                boolean foundShort = false;
                boolean foundLong = false;

                // On risk analysis page, check for message, short (take into account Ted's fix for over 250 characters, it drops the last words and adds a '...')
                // and long messages
                FullUnderWriterIssues allIssues = getUWIssuesWithButtons();
                List<UnderwriterIssue> issues = allIssues.getInformationalList();
                issues.addAll(allIssues.getBlockSubmitList());
                issues.addAll(allIssues.getBlockQuoteReleaseList());

                for (UnderwriterIssue issue : issues) {
                    foundShort = validateShortUWMessage(question, issue);
                    foundLong = validateLongUWMessage(question, issue);
                    if (foundShort || foundLong) {
                        break;
                    }
                }

                // Add failures if they occurred, edit policy transaction / setup for next question
                if (!foundShort) {
                    testFailed = true;
                    failureList = failureList + "FAILED TO FIND SHORT MESSAGE ON QUESTION CODE " + question.getQuestionCode() + ": " + question.getQuestionText() + " | " + question.getFailureMessage() + "\n";
                }//end if

                if (!foundLong) {
                    testFailed = true;
                    failureList = failureList + "FAILED TO FIND LONG MESSAGE ON QUESTION CODE " + question.getQuestionCode() + ": " + question.getQuestionText() + " | " + question.getFailureMessage() + "\n";
                }//end if

                switch (new BasePage(driver).find(By.xpath("//span[contains(@class, 'g-title')]")).getText()) {
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
                sideMenu.clickSideMenuGLExposures();
                glUWQuestions.setUnderwritingQuestion(exposure, question);
            }//END IF
        }//END FOR

    }//END verifyBlockUserQuestions()

    private boolean validateLongUWMessage(CPPGLExposureUWQuestions uwQuestion, UnderwriterIssue issueToTest) {

        return issueToTest.getLongDescription().replace("  ", " ").trim().contains("Location " + currentExposure.getLocation().getNumber() + ": Class Code: " + currentExposure.getClassCode() + ". " + uwQuestion.getFailureMessage().replace("  ", " ").trim());

    }

    private boolean validateShortUWMessage(CPPGLExposureUWQuestions uwQuestion, UnderwriterIssue issueToTest) {

        String messageToCheck = "Location " + currentExposure.getLocation().getNumber() + ": Class Code: " + currentExposure.getClassCode() + ". " + uwQuestion.getFailureMessage().replace("  ", " ").trim();

        // Cut message if over 255 characters
        if (messageToCheck.length() > 255) {

            messageToCheck = messageToCheck.substring(0, 230); //TODO, this is a temporary fix for long messages, need to make one that is more robust
        }

        if (issueToTest.getShortDescription().replace("  ", " ").trim().contains(messageToCheck)) {

            if (!messageToCheck.endsWith("...")) {
                testFailed = true;
                failureList += "SHORT MESSAGE FOR QUESTION CODE: " + uwQuestion.getQuestionCode() + ", IS LARGER THAN 255 CHARACTERS AND DOES NOT END WITH '...'\n";
            }

            return true;
        }

        return false;
    }

    /**
     * @return
     * @Author rlonardo
     * @Description Returns a list of class codes that the given class code depends on
     * @DATE April 24, 2017
     */
    private ArrayList<GLClassCode> getDependentClassCodes(GLClassCode classCode) {

        ArrayList<GLClassCode> dependentCodes = new ArrayList<>();

        switch (classCode) {
            case GL_16910:
            case GL_16911:
            case GL_16915:
            case GL_16916:
            case GL_16930:
            case GL_16931:
                dependentCodes.add(GLClassCode.GL_58161);
                break;
            default:
                break;
        }

        return dependentCodes;

    }

    /**
     * @Author jlarsen
     * @Description Creates the FullUnderWriterIssues with all the UW Issues on the Risk Analysis page.
     * Does not get what Buttons are available.
     * @DATE Sep 9, 2016
     * @return
     *//*
	private FullUnderWriterIssues getUWIssues() {

		ISideMenu sideMenu = SideMenuFactory.getMenu();
		//quote the policy and check UW issues
		GenericWorkorder genwo = new GenericWorkorder(driver);
		genwo.clickGenericWorkorderQuote();
				GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
					}
		sideMenu.clickSideMenuRiskAnalysis();

		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		return risk.getUnderwriterIssues();
	}*/

    /**
     * @return
     * @Author jlarsen, rlonardo
     * @Description Creates the FullUnderWriterIssues with all the UW Issues on the Risk Analysis page.
     * Sets what buttons are available.
     * Much slower process. use getUWIssues() if you don't need to know about the buttons.
     * Modified to assume already on pre-quote page
     * @DATE Sep 9, 2016
     */
    private FullUnderWriterIssues getUWIssuesWithButtons() {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        //quote the policy and check UW issues

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        return risk.getUnderwriterIssuesWithButtons();
    }

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
