package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description EPLI AVAILABILITY RULES
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI extends BaseTest {
    public GeneratePolicy myPolicyObj = null;
    private WebDriver driver;

    @Test
    public void epli() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine();

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .isDraft()
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission("atubbs", "gw", myPolicyObj.accountNumber);
        new SideMenuPC(driver).clickSideMenuBusinessownersLine();
        new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).clickBusinessownersLine_AdditionalCoverages();
        softAssert.assertFalse(new GenericWorkorder(driver).finds(By.xpath("//label[contains(text(), 'Hand rated')]")).isEmpty(), "HAND RATED WAS NOT VISIBLE FOR UNDERWRITING");
        myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);
        new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
        softAssert.assertTrue(new GenericWorkorder(driver).finds(By.xpath("//div[contains(text(), 'Third Party Violations')]")).isEmpty(), "THIRD PARTY WAS VISIBLE AFTER EPLI WAS UNSELECTED");
        new SideMenuPC(driver).clickSideMenuLocations();
        new SideMenuPC(driver).clickSideMenuBusinessownersLine();
        new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).clickBusinessownersLine_ExclusionsConditions();
        softAssert.assertFalse(new GenericWorkorder(driver).finds(By.xpath("//div[contains(text(), 'Employment - Related Practices Exclusion BP 04 17')]")).isEmpty(), "Employment - Related Practices Exclusion BP 04 17 WAS NOT REQUIRED WHEN EPLI WAS NOT SELECTED");
        softAssert.assertAll();
    }
}

