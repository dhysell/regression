package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.gw.enums.BlockingAction;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPGLExposureUWQuestions;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityCoveragesCPP;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import persistence.globaldatarepo.entities.GLClassCodes;
import regression.r3.noclock.policycenter.generalliability.glexposureuwquestionsNext.GLExposureUWQuestions_Change_Next;
import regression.r3.noclock.policycenter.generalliability.glexposureuwquestionsNext.GLExposureUWQuestions_FA_Next;
import regression.r3.noclock.policycenter.generalliability.glexposureuwquestionsNext.GLExposureUWQuestions_QQ_Next;

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
@SuppressWarnings("serial")
public class GLExposureUWQuestionsNext extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
    List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();
    List<String> exposureClassCodes = new ArrayList<String>();

    boolean testFailed = false;
    String failureList = "Questions that failed thier validation message:  \n";

    private WebDriver driver;


    @Test
    public void runGLExposureUWQuestionsTest() throws Exception {

        if (evenOddDay()) {
            GLExposureUWQuestions_QQ_Next quickQuote_Next = new GLExposureUWQuestions_QQ_Next();
            quickQuote_Next.validateUWExposureQuestionsQQ();
        } else if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) % 3 == 0) {
            GLExposureUWQuestions_FA_Next fullApp_Next = new GLExposureUWQuestions_FA_Next();
            fullApp_Next.validateUWExposureQuestionsFA();
        } else {
            GLExposureUWQuestions_Change_Next policyChange_Next = new GLExposureUWQuestions_Change_Next();
            policyChange_Next.validateUWExposureQuestionsCreatePolicy();
            policyChange_Next.testValidationsOnPolicyChange();
        }
    }


    /*
     * this will loop thru all the class codes one by one and set every question, then check all page validations.
     * if one fails it is Caught, noted, and the test logs out and logs the user back in to reset the policy to a working state.
     * The logout list Items are to logout the user and log them back in to clear the server session memory.
     */
    public String validateUWQuestions(GeneratePolicy policy, List<String> exposureClassCodes) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);

        for (String classCode : exposureClassCodes) {
            if (classCode.equals("LoggOut")) {
                guidewireHelpers.logout();
                login.loginAndSearchSubmission(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(), policy.accountNumber);
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
            CPPGeneralLiabilityExposures exposure = new CPPGeneralLiabilityExposures(policy.commercialPackage.locationList.get(0), classCode);
            exposuresPage.clickAdd();
            policy.generalLiabilityCPP.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                this.add(exposure);
            }});
            exposuresPage.addExposure(exposure);
            //When you have one of the following class codes selected: 16910, 16911, 16915, 16916, 16930, or 16931, the user needs to be stopped on the wizard step until class code 58161 has been added to the same location.
            switch (exposure.getClassCode()) {
                case "58165":
                    policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0).getUnderWritingQuestions().get(5).getChildrenQuestions().get(0).setCorrectAnswer("Yes");
                    CPPGeneralLiabilityExposures exposure2 = new CPPGeneralLiabilityExposures(exposure.getLocation(), "16901");
                    policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(exposure2);
                    exposuresPage.clickAdd();
                    exposuresPage.addExposure(exposure2);
                    exposuresPage.clickLocationSpecificQuestionsTab();
                    exposuresPage.fillOutUnderwritingQuestionsQQ(exposure2, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    if (!guidewireHelpers.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                        exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure2, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    }
                    break;
                case "16910":
                case "16911":
                case "16915":
                case "16916":
                case "16930":
                case "16931":
                    CPPGeneralLiabilityExposures exposure3 = new CPPGeneralLiabilityExposures(exposure.getLocation(), "58161");
                    policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(exposure3);
                    exposuresPage.clickAdd();
                    exposuresPage.addExposure(exposure3);
                    exposuresPage.clickLocationSpecificQuestionsTab();
                    exposuresPage.fillOutUnderwritingQuestionsQQ(exposure3, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    if (!guidewireHelpers.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                        exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure3, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
                    }
                    break;
                case "51999":
                    exposure.getUnderWritingQuestions().get(2).setCorrectAnswer("Checked");
                    break;
            }
            exposuresPage.clickLocationSpecificQuestionsTab();
            exposuresPage.fillOutUnderwritingQuestionsQQ(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (!guidewireHelpers.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }
            genwo.clickGenericWorkorderSaveDraft();
            if (exposuresPage.getInvalidQuestions() != null) {
                testFailed = true;
                failureList = failureList + "A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode + "\n";
                System.out.println("A QUESTION DIDN'T GET FILLED OUT FOR CLASS CODE: " + classCode);
                continue;
            }

            //CHECK BLOCK USER QUESTIONS
            for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
                //if question is FA question and currect polity type equal QQ don't try to fill out.
                if (!(question.getRequiredAt().equals(GeneratePolicyType.FullApp) && guidewireHelpers.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote))) {
                    if (question.getBlockingAction().equals(BlockingAction.Blockuser)) {
                        GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);

                        glUWQuestions.setUnderwritingQuestion(exposure, question, glUWQuestions.getIncorrectAnswer(question));
                        exposuresPage.clickNext();
                        boolean found = false;
                        for (String message : exposuresPage.getValidationMessages()) {
                            if (message.replaceAll("  ", " ").replaceAll("–", "-").replace("\"", "").trim().equals(question.getFailureMessage().replaceAll("  ", " ").replaceAll("–", "-").replace("\"", "").trim())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            testFailed = true;
                            failureList = failureList + "FAILIED TO BLOCK USER ON NEXT: " + question.getQuestionText() + "for class code: " + question.getClassCode() + "\nLooked for error message: " + question.getFailureMessage();
                            found = false;
                        }
                        if (!driver.findElements(By.xpath("//span[contains(text(), 'Exposures')]")).isEmpty()) {
                            glUWQuestions.setUnderwritingQuestion(exposure, question);
                        } else {
                            GenericWorkorderGeneralLiabilityCoveragesCPP coveragesPage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
                            coveragesPage.fillOutGeneralLiabilityCoverages(policy);

                            //get back to Location Specific tab
                            sideMenu.clickSideMenuGLExposures();
                            exposuresPage.clickLocationSpecificQuestionsTab();
                        }
                    }
                }
            }
        }//END FOR

        for (CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
            GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
            exposuresPage.clickLocationSpecificQuestionsTab();
            exposuresPage.fillOutUnderwritingQuestionsQQ(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            if (!guidewireHelpers.getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
                exposuresPage.fillOutUnderwritingQuestionsFULLAPP(exposure, policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
            }
        }
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLCoverages();

        GenericWorkorderGeneralLiabilityCoveragesCPP coveragesPage = new GenericWorkorderGeneralLiabilityCoveragesCPP(driver);
        coveragesPage.fillOutGeneralLiabilityCoverages(policy);

        return failureList;

    }

    List<String> sundayList1 = new ArrayList<String>() {{
        //questions 1-100
        this.add("10060");
        this.add("10071");
        this.add("10073");
        this.add("40111");
        this.add("10110");
        this.add("10115");
        this.add("10117");
        this.add("10145");
        this.add("10146");
        this.add("10150");
        this.add("10151");
        this.add("10220");
        this.add("10255");
        this.add("10256");
        this.add("10331");
        this.add("10332");
        this.add("11039");
        this.add("11138");
        this.add("11208");
        this.add("11201");
        this.add("11222");
        this.add("11258");
        this.add("11259");
        this.add("11288");
        this.add("12393");
        this.add("13314");
        this.add("13351");
        this.add("LoggOut");
        this.add("13352");
        this.add("13410");
        this.add("13461");
        this.add("13621");
        this.add("13670");
        this.add("13716");
        this.add("13720");
        this.add("14279");
        this.add("15060");
        this.add("15061");
        this.add("15063");
        this.add("15300");
        this.add("15405");
        this.add("15699");
        this.add("16404");
        this.add("16676");
        this.add("16694");
        this.add("16705");
        this.add("16722");
        this.add("16750");
        this.add("16751");
        this.add("16881");
        this.add("16890");
        this.add("18206");
    }};

    List<String> sundayList2 = new ArrayList<String>() {{
        //questions 1300 - on
        this.add("16915");
        this.add("16916");
        this.add("16930");
        this.add("16931");
    }};

    List<String> mondayList = new ArrayList<String>() {{
        //question 100-200
        this.add("18206");
        this.add("18437");
        this.add("18438");
        this.add("18501");
        this.add("18507");
        this.add("18920");
        this.add("19007");
        this.add("19051");
        this.add("40059");
        this.add("40061");
        this.add("40115");
        this.add("40117");
        this.add("41650");
        this.add("41667");
        this.add("41668");
        this.add("41669");
        this.add("41670");
        this.add("41675");
        this.add("LoggOut");
        this.add("41677");
        this.add("44070");
        this.add("44276");
        this.add("44277");
        this.add("44280");
        this.add("45190");
        this.add("45192");
        this.add("45450");
        this.add("45678");
        this.add("45819");
        this.add("45901");
        this.add("46202");
        this.add("46426");
        this.add("46427");
        this.add("46604");


    }};

    List<String> mondayList2 = new ArrayList<String>() {{
        //questions 700 - 800
        this.add("70412");
        this.add("58161");
        this.add("50911");
    }};

    List<String> tuesdayList = new ArrayList<String>() {{
        //questions 200-300
        this.add("46604");
        this.add("46671");
        this.add("46881");
        this.add("46773");
        this.add("47050");
        this.add("47051");
        this.add("47052");
        this.add("47146");
        this.add("47147");
        this.add("47367");
        this.add("LoggOut");
        this.add("47474");
        this.add("48557");
        this.add("48558");
        this.add("48600");
        this.add("48636");
        this.add("48727");
        this.add("49181");
        this.add("49451");
        this.add("49452");
        this.add("51896");
        this.add("51350");
        this.add("51351");
        this.add("51352");
        this.add("51999");


    }};

    List<String> tuesdayList2 = new ArrayList<String>() {{
        //questions 800 - 900
        this.add("59211");
        this.add("58165");
    }};

    List<String> wednessdayList = new ArrayList<String>() {{
        //questions 300-400
        this.add("51999");
        this.add("52002");
        this.add("53001");
        this.add("53732");
        this.add("53733");
        this.add("55371");
        this.add("56391");
        this.add("56760");
        this.add("56916");
        this.add("57001");
        this.add("57002");
        this.add("58397");
        this.add("58408");
        this.add("58409");
        this.add("58456");
        this.add("58457");
        this.add("58458");
        this.add("58759");
        this.add("58922");
        this.add("LoggOut");
        this.add("59057");
        this.add("59058");
        this.add("59223");
        this.add("59482");
        this.add("59713");
        this.add("59722");
        this.add("59947");
        this.add("59984");
        this.add("60010");
        this.add("60011");
        this.add("61226");
        this.add("61227");
        this.add("62003");
        this.add("63218");
        this.add("63217");
        this.add("63220");
        this.add("63219");
        this.add("67512");
        this.add("67513");
        this.add("67634");
        this.add("67635");
        this.add("68439");
        this.add("68500");
        this.add("68604");
        this.add("68606");
        this.add("68707");


    }};

    List<String> wednessdayList2 = new ArrayList<String>() {{
        //questions 900 - 1000
        this.add("58165");
        this.add("58166");
        this.add("16819");
    }};

    List<String> thursdayList = new ArrayList<String>() {{
        //questions 400 - 500
        this.add("68707");
        this.add("68706");
        this.add("91127");
        this.add("94381");
        this.add("91177");
        this.add("91179");
        this.add("91302");
        this.add("91302");
        this.add("91551");
        this.add("91577");
        this.add("91581");
        this.add("91583");
        this.add("91585");
        this.add("91591");
        this.add("91636");
        this.add("91805");
        this.add("92101");
        this.add("LoggOut");
        this.add("92215");
        this.add("94007");
        this.add("94225");
        this.add("94444");
        this.add("95357");
        this.add("95410");
        this.add("95647");
        this.add("95648");
        this.add("96409");
        this.add("96702");
        this.add("96703");
        this.add("96816");
        this.add("97002");
        this.add("97003");
        this.add("97047");
        this.add("97050");


    }};

    List<String> thursdayList2 = new ArrayList<String>() {{
        //questions 1000 - 1100
        this.add("16819");
        this.add("16820");
        this.add("16900");
        this.add("16901");
    }};

    List<String> fridayList = new ArrayList<String>() {{
        //questions 500 - 600
        this.add("97050");
        this.add("97220");
        this.add("97222");
        this.add("97223");
        this.add("97652");
        this.add("97653");
        this.add("98303");
        this.add("98306");
        this.add("98309");
        this.add("LoggOut");
        this.add("98482");
        this.add("98483");
        this.add("98502");
        this.add("98555");
        this.add("98597");
        this.add("98598");
        this.add("91340");
        this.add("91342");
        this.add("98640");
        this.add("98751");


    }};

    List<String> fridayList2 = new ArrayList<String>() {{
        //questions 1100 - 1200
        this.add("16901");
        this.add("16902");
        this.add("16905");
        this.add("16906");
    }};

    List<String> saturdayList = new ArrayList<String>() {{
        //questions 600 - 700
        this.add("98751");
        this.add("98820");
        this.add("98884");
        this.add("98967");
        this.add("99080");
        this.add("99303");
        this.add("99310");
        this.add("99315");
        this.add("99321");
        this.add("99505");
        this.add("99506");
        this.add("LoggOut");
        this.add("99507");
        this.add("99600");
        this.add("99614");
        this.add("99709");
        this.add("99718");
        this.add("99746");
        this.add("99777");
        this.add("99793");
        this.add("99917");
        this.add("99938");
        this.add("99943");
        this.add("99946");
        this.add("99948");
        this.add("99969");
        this.add("70412");


    }};

    List<String> saturdayList2 = new ArrayList<String>() {{
        //questions 1200 - 1300
        //		this.add("16906");
        //		this.add("16910");
        //		this.add("16911");
        //		this.add("16915");

        //		this.add("16916");
        //		this.add("16930");
        //		this.add("16931");
        //questions 1300 +
        this.add("16915");
        this.add("16916");
        this.add("16930");
        this.add("16931");
        this.add("45771");
    }};


    public List<String> setExposureClasscodes() {
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
        return exposureClassCodes;
    }


    private boolean evenOddDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth % 2 == 0;
    }
}
























