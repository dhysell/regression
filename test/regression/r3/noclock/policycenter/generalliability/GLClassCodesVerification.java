package regression.r3.noclock.policycenter.generalliability;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.globaldatarepo.entities.GLClassCodes;
import persistence.globaldatarepo.helpers.GLClassCodeHelper;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description Ensure GL class codes have the correct description and basis type. grabs a random 10 from 704 total. :(
 * @DATE Dec 15, 2015
 */
public class GLClassCodesVerification extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    List<GLClassCodes> classCodes = new ArrayList<GLClassCodes>();
    List<GLClassCodes> faildClassCodes = new ArrayList<GLClassCodes>();

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {

        for (int i = 0; i < 10; i++) {
            GLClassCodes classCode = GLClassCodeHelper.getRandomGLRestrictedClassCode(false);
            System.out.println("Testing ClassCode: " + classCode.getCode() + " | " + classCode.getClassification());
            classCodes.add(classCode);
        }

        // LOCATIONS
        final ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(new ArrayList<CPPGeneralLiabilityExposures>() {{
                this.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCodeHelper.getRandomGLClassCode().getCode(), "the fat cat", 1));
            }});
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
                .withInsCompanyName("GLClassCodes")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyClassCodes() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        boolean testFailed = false;

        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuGLExposures();
        for (GLClassCodes code : classCodes) {
            sideMenu.find(By.xpath("//div[contains(@id, 'GL_ExposureUnitsLV-body')]/div/table/tbody/child::tr[last()]/child::td[5]/div")).click();

            try {
                GenericWorkorderBuildings classCode = new GenericWorkorderBuildings(driver);
                classCode.selectFirstBuildingCodeResultClassCode(code.getCode());
            } catch (Exception e) {
                testFailed = true;
                driver.findElement(By.xpath("//a[contains(@id, 'GLClassCodeSearchPopup:__crumb__')]")).click();
                System.out.println("FAILED ON SEARCH:  " + code.getCode() + " : " + code.getClassification());
                faildClassCodes.add(code);
                continue;
            }

            // get description
            String desc = driver.findElement(By.xpath("//div[contains(@id, 'GL_ExposureUnitsLV-body')]/div/table/tbody/child::tr[last()]/child::td[6]/div")).getText();
            if (!desc.replace("�", "-").equalsIgnoreCase(code.getClassification().replace("�", "-"))) {

                testFailed = true;
                System.out.println("FAILED ON DESCRIPTION:  " + code.getCode() + " : " + code.getClassification() + " | descriptionUI: " + desc);
                faildClassCodes.add(code);
                continue;
            }
        }

        if (testFailed) {
            for (GLClassCodes failed : faildClassCodes) {
                System.out.println("FAILED:  " + failed.getCode() + " : " + failed.getClassification());
            }
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "TEST FAILED. SEE STACK TRACE.");
        }


    }

}
