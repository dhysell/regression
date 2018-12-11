package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FormatType;
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
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderGeneralLiabilityExposuresCPP;

public class GLUWQuestions_Checkboxes extends BaseTest {


    GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void checkCheckBoxBusininessConditions() throws Exception {

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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_70412.getValue()));
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_19795.getValue()));

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("GL_UW Issues")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        //VERIFY ONLY ONE CHECKBOX NEEDS TO BE CHECK
        new Login(driver).loginAndSearchSubmission(myPolicyObj);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuGLExposures();
        for (CPPGLExposureUWQuestions question : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0).getUnderWritingQuestions()) {
            for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
                if (childQuestion.getFormatType().equals(FormatType.BooleanCheckbox)) {
                    childQuestion.setCorrectAnswer("Not Checked");
                }
            }
        }
        GenericWorkorderGeneralLiabilityExposuresCPP exposuresPage = new GenericWorkorderGeneralLiabilityExposuresCPP(driver);
        exposuresPage.fillOutUnderwritingQuestionsQQ(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0), myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());
        new TableUtils(driver).setCheckboxInTableByText(exposuresPage.find(By.xpath("//label[contains(text(), 'Liquor - Clubs')]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), "Other", false);
        exposuresPage.clickNext();
        softAssert.assertFalse(exposuresPage.getValidationMessages().contains("At least 1 question associated with"));

        //VERIFY IF USER CHECKS AND UNCHECKS ALL CHECKBOXES USER IS STILL BLOCKED.
        for (CPPGLExposureUWQuestions question : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0).getUnderWritingQuestions()) {
            for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
                if (childQuestion.getFormatType().equals(FormatType.BooleanCheckbox)) {
                    childQuestion.setCorrectAnswer("Checked");
                }
            }
        }
        exposuresPage.fillOutUnderwritingQuestionsQQ(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0), myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());

        for (CPPGLExposureUWQuestions question : myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0).getUnderWritingQuestions()) {
            for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
                if (childQuestion.getFormatType().equals(FormatType.BooleanCheckbox)) {
                    childQuestion.setCorrectAnswer("Not Checked");
                }
            }
        }
        exposuresPage.fillOutUnderwritingQuestionsQQ(myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0), myPolicyObj.generalLiabilityCPP.getCPPGeneralLiabilityExposures());

        new TableUtils(driver).setCheckboxInTableByText(exposuresPage.find(By.xpath("//label[contains(text(), 'Liquor - Clubs')]/ancestor::div[@class = 'x-container x-container-default x-table-layout-ct']/ancestor::tr/following-sibling::tr/descendant::div[contains(@id, 'QuestionSetLV')]")), "Other", false);

        exposuresPage.clickNext();
        softAssert.assertFalse(exposuresPage.getValidationMessages().contains("At least 1 question associated with"));

        //VERIFY CHILD QUESTIONS DISPLAY AFTER CHECKBOX IS CHECKED.


        softAssert.assertAll();

    }


}











