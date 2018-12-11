package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQualification_GeneralLiability;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;

public class GLQualificationQuestions extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    SoftAssert softAssert = new SoftAssert();

    /**
     * @Author jlarsen
     * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description verify the correct blicking messages show for incorrect answers on the GL Qualifications page.
     * @DATE Apr 4, 2016
     */

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test//(enabled=false)
    public void generatePolicy() throws Exception {

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
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCodeHelper.getRandomGLClassCode().getCode()));

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
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyQualificationBlockUser() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();

        GenericWorkorder genwo = new GenericWorkorder(driver);

        GenericWorkorderQualification_GeneralLiability qual = new GenericWorkorderQualification_GeneralLiability(driver);
        qual.clickGL_HazardousMaterials(true);
        genwo.clickPolicyChangeNext();
        softAssert.assertTrue(validateMessage(qual.getValidationMessages(), "Applicant has operations, past or present, involving the storing, treating, discharging, applying, disposing, or transporting of hazardous materials. Risk is not eligible, contact Brokerage."),
                "Failed to block User when Does applicant have any operations, past or present, involving the storing, treating, discharging, applying, disposing, or transporting of hazardous materials?");
        qual.clickGL_HazardousMaterials(false);


        qual.clickGL_RecreationalActivities(true);
        genwo.clickPolicyChangeNext();
        softAssert.assertTrue(validateMessage(qual.getValidationMessages(), "Applicant operations include ski lifts, stables, marinas, or other similar recreational type activities. Risk is not eligible, contact Brokerage."),
                "Failed to block user when Do applicant operations include any ski lifts, stables, marinas, or other similar recreational type activities? was answered incorrectly");
        qual.clickGL_RecreationalActivities(false);


        qual.clickGL_Explosives(true);
        genwo.clickPolicyChangeNext();
        softAssert.assertTrue(validateMessage(qual.getValidationMessages(), "Applicant handles or sells explosives, firearms, fireworks, or ammunition. Risk is not eligible, contact Brokerage."),
                "Failed to block User when Does applicant handle or sell explosives, firearms, fireworks, or ammunition? was answered incorrectly");
        qual.clickGL_Explosives(false);


        qual.clickGL_RelatedToIndustry(true);
        genwo.clickPolicyChangeNext();
        softAssert.assertTrue(validateMessage(qual.getValidationMessages(), "Applicant handles or sells products related to the aircraft, space, railroad, nuclear, or medical industry. Risk is not eligible, contact Brokerage."),
                "Failed to block User when Does the applicant handle or sell any products related to the aircraft, space, railroad, nuclear, or medical industry? was answered incorrectly");
        qual.clickGL_RelatedToIndustry(false);

        softAssert.assertAll();


    }


    private boolean validateMessage(List<WebElement> messagesRecieved, String correctErrorMessage) {
        for (WebElement message : messagesRecieved) {
            if (message.getText().trim().equalsIgnoreCase(correctErrorMessage)) {
                return true;
            }
        }
        return false;
    }


}





















