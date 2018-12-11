package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCPClassCodeUWQuestions;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="projects.idfbins.com/policycenter/To Be Process/Commercial Package Policy (CPP)/Guidewire - Property/WCIC Commercial Property-Product-Model.xlsx">Commercial Property Product Model</a>
 * @Description This test class is verify the correct UW Issue gets generated when specific questions are answered incorrectly.
 * the class code list is defined at the bottom of the class and every day a random set to pulled.
 * Question sets are pulled from the database Table CPUWQuestions
 * there is a flag on the Buildings class that will allow the test to answer only the questions that have UW Issues to the correct answer to generate that UW Issue.
 * @DATE Apr 27, 2017
 */
@QuarantineClass
public class CPPropertyClassCodeUWIssues extends BaseTest {

    public GeneratePolicy myPolicyObj = null;
    boolean testFailed = false;
    String failureList = "";
    List<String> classCodeList = new ArrayList<String>();
    SoftAssert softAssert = new SoftAssert();

    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement
     * @Description Generate test policy
     * @DATE Apr 27, 2017
     */
    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        System.out.println("**CLASS CODES UNDER TEST**");
        while (classCodeList.size() < 3) {
            String classCode = uwIssuesList.get(NumberUtils.generateRandomNumberInt(0, uwIssuesList.size() - 1));
            if (!classCodeList.contains(classCode)) {
                classCodeList.add(classCode);
                System.out.println(classCode);
            }
        }


        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();
        locationList.add(new PolicyLocation(pniAddress, true));

        //COMMERCIAL PROPERTY LINE
        //Property Line Coverages
        CPPCommercialPropertyLine_Coverages propertyLineCoverages = new CPPCommercialPropertyLine_Coverages();
        //Property Line Exclusions and conditions
        CPPCommercialPropertyLine_ExclusionsConditions propertyLineEclusionsConditions = new CPPCommercialPropertyLine_ExclusionsConditions();
        //property line object
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
        commercialPropertyLine.setPropertyLineCoverages(propertyLineCoverages);
        commercialPropertyLine.setPropertyLineExclusionsConditions(propertyLineEclusionsConditions);


        //COMMERCIAL PROPERTY PROPERTY
        // BUILDING LIST
        ArrayList<CPPCommercialProperty_Building> buildingList = new ArrayList<CPPCommercialProperty_Building>();
        for (String classCode : classCodeList) {
            CPPCommercialProperty_Building buildingToAdd = new CPPCommercialProperty_Building(classCode);
            if (classCode.equals("1150")) {
                List<PropertyCoverages> buildingCoverageList = new ArrayList<PropertyCoverages>();
                buildingCoverageList.add(PropertyCoverages.BuildingCoverage);
                buildingToAdd.getCoverages().setBuildingCoveragesList(buildingCoverageList);
            }
            if (classCode.equals("2459(2)")) { //this class code cannot have Equipment breakdown
                buildingToAdd.getAdditionalCoverages().setEquipmentBreakdownEnhancementEndorsementID_CP_31_1002(false);
            }
            buildingToAdd.setGenerateUWIssuesFromQuestions(true); //flag to generate UW Issues off of Class Code UW Questions.
            buildingList.add(buildingToAdd);
        }


        //PROPERTY LIST
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>();
        CPPCommercialPropertyProperty commercialProperty = new CPPCommercialPropertyProperty();
        commercialProperty.setAddress(pniAddress);
        commercialProperty.setCPPCommercialProperty_Building_List(buildingList);
        commercialPropertyList.add(commercialProperty);


        //COMMERCIAL PROPERTY OBJECT
        CPPCommercialProperty commercialPropertyObject = new CPPCommercialProperty();
        commercialPropertyObject.setCommercialPropertyLine(commercialPropertyLine);
        commercialPropertyObject.setCommercialPropertyList(commercialPropertyList);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialPropertyObject)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("UWIssues Property")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }//end generateQQPolicy()


    /**
     * @Author jlarsen
     * @Requirement
     * @Description This test class ensures that all the wizardsteps are visited to allow for quoting.
     * Quotes the policy.
     * Then grabs all the UW Issues on the Risk Analysis page.
     * then cycles thru all the Class Code UW Questions verifying that all required UW Issues were generated.
     * @DATE Apr 27, 2017
     */
    @Test(dependsOnMethods = {"generateQQPolicy"})
    public void verfiyGenerateUWIssues() throws Exception {

        testFailed = false;
        failureList = "QUESTION UNDERWRITER ISSUE FAILURE LIST:\n\n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCPCommercialPropertyLine();
        sideMenu.clickSideMenuCPProperty();
        sideMenu.clickSideMenuCPModifiers();
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorder genWO = new GenericWorkorder(driver);
        genWO.clickGenericWorkorderQuote();

        if (!genWO.finds(By.xpath("//span[contains(text(), 'Pre-Quote Issues')]")).isEmpty()) {
            GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
            quote.clickPreQuoteDetails();
        }

        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
        FullUnderWriterIssues allUWIssues = riskAnaysis.getUnderwriterIssues();

        for (CPPCommercialPropertyProperty location : myPolicyObj.commercialPropertyCPP.getCommercialPropertyList()) {
            for (CPPCommercialProperty_Building building : location.getCPPCommercialProperty_Building_List()) {
                for (CPPCPClassCodeUWQuestions question : building.getUwQuestionList()) {
                    if (question.getUwIssueType() != null) {

                        System.out.println("Location: " + location.getPropertyLocationNumber() + " Property: " + building.getNumber() + " " + question.getFailureMessage());

                        UnderwriterIssueType returnedType = allUWIssues.isInList("Location: " + location.getPropertyLocationNumber() + " Property: " + building.getNumber() + " " + question.getFailureMessage());

                        if (returnedType == null) {
                            softAssert.fail("\nUW ISSUE FOR QUESTION: " + question.getClassCode() + " " + question.getQuestionText() + " DID NOT GENERATE!");
                        } else {
                            softAssert.assertTrue(returnedType.equals(question.getUwIssueType()), "\nUW ISSUE FOR QUESTION: " + question.getClassCode() + " " + question.getQuestionText() + " GENERATED THE WRONG TYPE! \nFound: " + returnedType + " \nExpected: " + question.getUwIssueType());
                        }//end else
                    }//end if
                }//end for
            }//end for
        }//end fort

        testFailed();
    }


    private void testFailed() {
        softAssert.assertAll();
    }

    @SuppressWarnings("serial")
    List<String> uwIssuesList = new ArrayList<String>() {{
        this.add("0921(1)");
        this.add("2400(1)");
        this.add("0841");
        this.add("1150");
        this.add("0567(15)");
        this.add("0931(3)");
        this.add("0562(2)");
        this.add("0570(7)");
        this.add("0580(2)");
        this.add("0570(13)");
        this.add("0570(14)");
        this.add("0196");
        this.add("0742");
        this.add("0743");
        this.add("0744");
        this.add("0520(9)");
        this.add("0911(1)");
        this.add("0912(1)");
        this.add("0913(2)");
        this.add("0565(2)");
        this.add("6900(12)");
        this.add("0563(14)");
        this.add("0922(26)");
        this.add("0933(8)");
        this.add("6850(27)");
        this.add("0932(2)");
        this.add("0933(3)");
        this.add("0562(3)");
        this.add("0567(71)");
        this.add("1051(3)");
        this.add("0580(3)");
        this.add("0570(20)");
        this.add("0922(10)");
        this.add("3959(7)");
        this.add("3959(10)");
        this.add("0844(23)");
        this.add("0922(36)");
        this.add("0570(29)");
        this.add("0520(11)");
        this.add("3009(11)");
        this.add("0570(27)");
        this.add("0844(20)");
        this.add("0921(23)");
        this.add("0844(19)");
        this.add("0545");
        this.add("1230(2)");
        this.add("1211(3)");
        this.add("1220");
        this.add("1213");
        this.add("1212");
        this.add("6850(48)");
        this.add("2459(2)");
    }};


}//END CPPropertyClassCodeUWIssues






























