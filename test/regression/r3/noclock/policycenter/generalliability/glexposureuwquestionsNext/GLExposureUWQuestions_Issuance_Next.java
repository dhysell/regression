package regression.r3.noclock.policycenter.generalliability.glexposureuwquestionsNext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;
import persistence.globaldatarepo.entities.GLClassCodes;

public class GLExposureUWQuestions_Issuance_Next extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
    List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();
    List<String> exposureClassCodes = new ArrayList<String>();


    boolean testFailed = false;
    String failureList = "Questions that failed thier validation message:  \n";

    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Test All Questions with associated User Validations
     * @DATE Jun 8, 2016
     */
    @Test
    public void validateUWExposureQuestionsCreatePolicy() throws Exception {


        ArrayList<PolicyLocationBuilding> buildingList = new ArrayList<PolicyLocationBuilding>();
        buildingList.add(new PolicyLocationBuilding());

        PolicyLocation policyLocation = new PolicyLocation(new AddressInfo(true), false);
        policyLocation.setBuildingList(buildingList);

        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>();
        locationsLists.add(policyLocation);

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_18206.getValue()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability();
        generalLiability.setCPPGeneralLiabilityExposures(exposures);

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
                .build(GeneratePolicyType.PolicyIssued);

    }


    /**
     * @Author jlarsen
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description test all Exposure questions and validations during a policy change.
     * if a class code fails test will logout to reset the policy, log in, and start a new policy change.
     * @DATE Jun 8, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"validateUWExposureQuestionsCreatePolicy"})
    public void testValidationsOnPolicyChange() throws Exception {

        testFailed = false;
        failureList = "Questions that failed thier validation message:  \n";
        exposureClassCodes = setExposureClasscodes();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);

        login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.startPolicyChange("To Validate UW Questions", null);

        for (String classCode : exposureClassCodes) {
            try {
                if (classCode.equals("LoggOut")) {
                    guidewireHelpers.logout();
                    login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
                    policyChange.startPolicyChange("To Validate UW Questions", null);
                    continue;
                }

                SideMenuPC sideMenu = new SideMenuPC(driver);
                sideMenu.clickSideMenuGLExposures();
                GenericWorkorder genwo = new GenericWorkorder(driver);
                GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
                exposuresPage.clickExposureDetialsTab();
                exposuresPage.selectAll();
                exposuresPage.clickRemove();
                CPPGeneralLiabilityExposures exposure = new CPPGeneralLiabilityExposures(myPolicyObj.commercialPackage.locationList.get(0), classCode);
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
                }

                //CHECK BLOCK USER QUESTIONS
                for (CPPGLExposureUWQuestions question : exposure.getUnderWritingQuestions()) {
                    GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability glUWQuestions = new GenericWorkorderCommercialUnderwritingQuestion_GeneralLiability(driver);
                    if (question.getBlockingAction().equals(BlockingAction.Blockuser)) {
                        glUWQuestions.setUnderwritingQuestion(exposure, question, glUWQuestions.getIncorrectAnswer(question));
                        exposuresPage.clickNext();
                        boolean found = false;
                        for (String message : exposuresPage.getValidationMessages()) {
                            if (message.replaceAll("  ", " ").equals(question.getFailureMessage().replaceAll("  ", " "))) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            testFailed = true;
                            failureList = failureList + "FAILIED TO BLOCK USER: " + question.getQuestionText() + " | " + question.getFailureMessage() + "\n";
                            found = false;
                        }
                        if (!driver.findElements(By.xpath("//span[contains(text(), 'Exposures')]")).isEmpty()) {
                            glUWQuestions.setUnderwritingQuestion(exposure, question);
                        } else {
                            //get back to UW Questions tab
                            sideMenu.clickSideMenuGLExposures();
                            exposuresPage.clickLocationSpecificQuestionsTab();
                        }
                    }
                }
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
    }


    @SuppressWarnings("serial")
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

    List<String> sundayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 1300 - on
            this.add("16915");
            this.add("16916");
            this.add("16930");
            this.add("16931");
        }
    };

    List<String> mondayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> mondayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 700 - 800
            this.add("70412");
            this.add("58161");
            this.add("50911");
        }
    };

    List<String> tuesdayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> tuesdayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 800 - 900
            this.add("59211");
            this.add("58165");
        }
    };

    List<String> wednessdayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> wednessdayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 900 - 1000
            this.add("58165");
            this.add("58166");
            this.add("16819");
        }
    };

    List<String> thursdayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> thursdayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 1000 - 1100
            this.add("16819");
            this.add("16820");
            this.add("16900");
            this.add("16901");
        }
    };

    List<String> fridayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> fridayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 1100 - 1200
            this.add("16901");
            this.add("16902");
            this.add("16905");
            this.add("16906");
        }
    };

    List<String> saturdayList = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
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


        }
    };

    List<String> saturdayList2 = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            //questions 1300 +
            this.add("16915");
            this.add("16916");
            this.add("16930");
            this.add("16931");
            this.add("45771");
        }
    };


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
