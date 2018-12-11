package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BlockingAction;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCPClassCodeUWQuestions;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_Details;
import repository.pc.workorders.generic.GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions;
import repository.pc.workorders.generic.GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="projects.idfbins.com/policycenter/To Be Process/Commercial Package Policy (CPP)/Guidewire - Property/WCIC Commercial Property-Product-Model.xlsx">Commercial Property Product Model</a>
 * @Description this test class will generate a policy and then verify all Class Code UW Questions that have a Block User validation execute correctly.
 * @DATE Apr 27, 2017
 */
@QuarantineClass
public class CPPropertyClassCodeUWQuestionsNext extends BaseTest {


    SoftAssert softAssert = new SoftAssert();
    public GeneratePolicy myPolicyObj = null;
    List<String> classCodeList = null;

    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @Description Test to generate working policy.
     * @DATE Apr 27, 2017
     */
    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(new PolicyLocation(pniAddress, true));
            }
        };

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine() {{
            this.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages() {{
                //SET COMMERCIAL PROPERTY LINE COVERAGES HERE
            }});
            this.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions() {{
                //SET COMMERCIAL PROPERTY LINE EXCLUSIONS CONDITIONS HERE
            }});
        }};

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(new CPPCommercialPropertyProperty() {{
                    this.setAddress(pniAddress);
                    this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {
                        /**
                         *
                         */
                        private static final long serialVersionUID = 1L;

                        {
                            this.add(new CPPCommercialProperty_Building("0567(71)") {{
                                this.getAdditionalCoverages().setEquipmentBreakdownEnhancementEndorsementID_CP_31_1002(false);
                                //SET BUILDING STUFF HERE
                            }});
                        }
                    });
                }});
            }
        };

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty() {{
            this.setCommercialPropertyLine(commercialPropertyLine);
            this.setCommercialPropertyList(commercialPropertyList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Next Property")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }//end generateQQPolicy()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @Description this test class cycles thru all class codes for the day and verifies that all class code UW Questions are:
     * present, worded correctly, are of the correct type, and under the correct parent question.
     * @DATE Apr 27, 2017
     */
    //jlarsen 4/27/2017
    //CLASS DISABLED BECAUSE WE MAY NOT NEED TO USE IT AS ALL TEH QUESITONS ARE TESTED IN VARIOUS OTHER CLASSES.
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = false)
    public void verifyUWQuestionsQQ() throws Exception {

        softAssert = new SoftAssert();
        CPPCPClassCodeUWQuestions failedClassCodeQuestion = null;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        GenericWorkorderCommercialPropertyPropertyCPP property = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetails = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions propertyUnderwritingQuestions = new GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);

        CPPCommercialProperty_Building theBuilding = new CPPCommercialProperty_Building("0921(1)");
        myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theBuilding);
        property.removeAllProperties();
        property.addPropertyQQ(true, theBuilding);
        property.editPropertyByNumber(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getNumber());
        for (String classcode : classCodeList) {
            try {
                property.clickDetailsTab();
                theBuilding.setClassCode(classcode);
                propertyDetails.selectFirstBuildingCodeResultClassCode(theBuilding.getClassCode());
                property.clickUnderwritingQuestionsTab();
                softAssert.assertNotNull(propertyUnderwritingQuestions.fillOutClassCodeUWQuestions(theBuilding), classcode + " failed to fill out quesiton: " + failedClassCodeQuestion.getQuestionText());
//				if(propertyUnderwritingQuestions.fillOutClassCodeUWQuestions(theBuilding) != null) {
//					System.out.println(classcode + " failed to fill out quesiton: " + failedClassCodeQuestion.getQuestionText() + "\n");
//					testFailed = true;
//					failureList = failureList + classcode + " failed to fill out quesiton: " + failedClassCodeQuestion.getQuestionText() + "\n";
//					failedClassCodeQuestion = null;
//				}
            } catch (Exception e) {
                softAssert.fail(classcode + " UNKOWN FAILURE");
                System.out.println("EXCEPTION CAUGHT!!");
            }
        }

        softAssert.assertAll();

    }//end verifyQQQuestions()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @Description this test class cycles thru class codes that are defined by the day at the bottom of the class.
     * for every Class Code for the day the test will edit the building details with the class code.
     * Answer all Questions
     * then for every class code question that has a block use validation the test sets the required answer. click ok. checks the validation then resets the answer.
     * @DATE Apr 27, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"}, enabled = true)
    public void verifyUWQuestionsBlockUser() throws Exception {

        softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPProperty();

        GenericWorkorderCommercialPropertyPropertyCPP property = new GenericWorkorderCommercialPropertyPropertyCPP(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_Details propertyDetails = new GenericWorkorderCommercialPropertyPropertyCPP_Details(driver);
        GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions propertyUnderwritingQuestions = new GenericWorkorderCommercialPropertyPropertyCPP_UnderwritingQuestions(driver);

        CPPCommercialProperty_Building theBuilding = myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0);
        property.editPropertyByNumber(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getNumber());
        for (String classcode : blockUserClassCodes) {
            property.clickDetailsTab();
            theBuilding.setClassCode(classcode);
            propertyDetails.selectFirstBuildingCodeResultClassCode(theBuilding.getClassCode());
            property.clickUnderwritingQuestionsTab();
            //				property.fillOutBasicClassCodeUWQuestionQQ(theBuilding);
            CPPCPClassCodeUWQuestions failedQuestion = propertyUnderwritingQuestions.fillOutClassCodeUWQuestions(theBuilding);
            if (failedQuestion != null) {
                failedQuestion = propertyUnderwritingQuestions.fillOutClassCodeUWQuestions(theBuilding);
                softAssert.assertNotNull(propertyUnderwritingQuestions.fillOutClassCodeUWQuestions(theBuilding), classcode + " failed on a question " + failedQuestion.getQuestionText());
                if (failedQuestion != null) {
                    continue;
                }
            }

            for (CPPCPClassCodeUWQuestions question : theBuilding.getUwQuestionList()) {
                if (question.getBlockingAction().equals(BlockingAction.Blockuser)) {
                    GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty uwQuestionsPage = new GenericWorkorderCommercialUnderwritingQuestion_CommercialProperty(driver);
                    uwQuestionsPage.setUnderwritingQuestion(question, !question.getCorrectAnswer().equals("Yes"));//invert correct answer
                    property.clickOK();
                    softAssert.assertFalse(!property.finds(By.xpath("//span[contains(@id, ':CPBuildingsScreen:ttlBar') and (text()='Property')]")).isEmpty(), "Question code: " + question.getQuestionCode() + " did not block user on screen and went to main property page.");
                    if (!property.finds(By.xpath("//span[contains(@id, ':CPBuildingsScreen:ttlBar') and (text()='Property')]")).isEmpty()) {
                        property.editPropertyByNumber(myPolicyObj.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getNumber());
                        break;
                    }

                    softAssert.assertFalse(!guidewireHelpers.containsErrorMessage(question.getFailureMessage()), "\nQuestion code: " + question.getQuestionCode() + " did not match Blocking User Message.");
                    uwQuestionsPage.setUnderwritingQuestion(question, question.getCorrectAnswer());//reset correct answer
                    clickClear();
                }//end if
            }//end for
        }//end for

        softAssert.assertAll();

    }//end verifyQQQuestions()


    private void clickClear() {
        if (!driver.findElements(By.xpath("//span[contains(@id, 'WebMessageWorksheet_ClearButton-btnEl')]")).isEmpty()) {
            new BasePage(driver).clickWhenClickable(By.xpath("//span[contains(@id, 'WebMessageWorksheet_ClearButton-btnEl')]"));
        }
    }


    List<String> classCodeList_Sunday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            this.add("0921(1)");
            this.add("2400(1)");
            this.add("0841");
            this.add("1150");
            this.add("0567(15)");
            this.add("0931(3)");
            this.add("0562(2)");
            this.add("0562(2)");
            this.add("0570(7)");
            this.add("0580(2)");

        }
    };

    List<String> classCodeList_Monday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0563(12)");
            this.add("0570(13)");
            this.add("0570(14)");
            this.add("0532(13)");
            this.add("0852(1)");
            this.add("0852(2)");
            this.add("0196");
            this.add("2300(3)");
            this.add("0742");
            this.add("0321");
            this.add("0322");

        }
    };

    List<String> classCodeList_Tuesday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0743");
            this.add("0744");
            this.add("0520(9)");
            this.add("0912(1)");
            this.add("0913(2)");
            this.add("0323");
            this.add("0311");

        }
    };

    List<String> classCodeList_Wednessday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0565(2)");
            this.add("6900(12)");
            this.add("0570(16)");
            this.add("0563(14)");
            this.add("0922(26)");
            this.add("0933(8)");
            this.add("6850(27)");
            this.add("0932(2)");
            this.add("0562(3)");
            this.add("0702(5)");
            this.add("0702(7)");
            this.add("0567(71)");
            this.add("1051(3)");
            this.add("0313");
            this.add("0580(3)");
            this.add("0570(20)");
            this.add("0570(25)");
            this.add("0312");


        }
    };

    List<String> classCodeList_Thursday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0922(10)");
            this.add("0921(21)");
            this.add("3959(7)");
            this.add("3959(10)");
            this.add("0844(23)");
            this.add("0922(36)");
            this.add("0570(29)");
            this.add("0520(11)");
            this.add("3009(11)");
            this.add("0570(27)");
            this.add("0844(20)");
            this.add("0197");
            this.add("0198");

        }
    };

    List<String> classCodeList_Friday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0921(23)");
            this.add("2800(12)");
            this.add("3009(12)");
            this.add("0844(19)");
            this.add("0532(18)");
            this.add("0545");

        }
    };

    List<String> classCodeListSaturday = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0542(5)");
            this.add("1230(2)");
            this.add("1211(3)");
            this.add("1220");
            this.add("1213");
            this.add("1212");
            this.add("6850(48)");
            this.add("2459(2)");
            this.add("0580(1)");

        }
    };

    //jlarsen 10/31/2017
    //SOME CLASS CODES COMMENTED OUT CUS THEY CONTAIN A ' THIS FAILS AFTER RUN THRU THE XPATH ESCAPE SPECIAL CHAR STRING IS RUN ON IT.
    List<String> blockUserClassCodes = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {

            this.add("0323");//question gets removed on OK
            this.add("0913(2)");
            this.add("0702(5)"); //question answer disapears on OK
            this.add("3959(7)");
            this.add("0580(3)");
//		this.add("0198");//question gets removed on OK
            this.add("0580(3)");
            this.add("0570(20)");
            this.add("0570(25)");//question answer dissappears on OK
            this.add("0921(21)");
            this.add("0702(7)");
            this.add("0580(2)");
            this.add("3959(10)");
            this.add("0322"); //question gets removed on OK
            this.add("0844(23)");
            this.add("0321");
            this.add("0844(20)");
            this.add("0580(1)");
            this.add("0844(19)");
            this.add("0545");
            this.add("0542(5)");
            this.add("0311");//question gets removed on OK
            this.add("1230(2)");
            this.add("6850(48)");
            this.add("2459(2)");
            this.add("0532(13)");//question answer dissappears after clicking ok
            this.add("2400(1)");
            this.add("0841");//first and second question answers dissappear on answering second question.
//		this.add("1150");
            this.add("0570(7)");
            this.add("0852(1)");
//		this.add("0197");//question gets removed on OK
//		this.add("0196");
            this.add("2300(3)");//question answer dissapears on OK
            this.add("0313");//question gets removed on OK
            this.add("0742");
            this.add("0312");//question gets removed on OK
            this.add("0743");
            this.add("0912(1)");
            this.add("0744");
            this.add("0852(2)");


        }
    };


}//END OF FILE






















