package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LossHistoryType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

@QuarantineClass
public class BlockBinds extends BaseTest {

    GeneratePolicy myPolicyObj = null;

    private WebDriver driver;

    @Test(enabled = true)
    public void createPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        PolicyLocation myLocation = new PolicyLocation(new AddressInfo(true), locOneBuildingList);

        locationsList.add(myLocation);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Block Bind")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);
    }

    /**
     * @throws Exception
     * @Author jlarsen
     * @Requirement Requirement: 2.5.6.17, 2.5.2
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description We need this Block Bind:
     * "Applicant has prior losses. Refer to underwriting prior to binding to determine acceptability."
     * We should get this message when the loss history type is
     * Manually Entered or Attached. The Manually Entered part is
     * done on DE2363 When there was something entered on Prior
     * Losses tab - Manually Entered previously, but then user
     * changed it back to No Loss History, once the user requotes
     * the policy, the block bind should disappear. The block bind
     * message does not need to be special approve.
     * @DATE Oct 1, 2015
     */
    @Test(dependsOnMethods = {"createPolicy"})
    public void testBlockSubmitPriorLosses() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObj);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSupplemental();
        sideMenu.clickSideMenuModifiers();
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis_PriorLosses riskAnaylisys = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
        GenericWorkorderRiskAnalysis_UWIssues uwIssuestab = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnaylisys.clickPriorLossesCardTab();
        riskAnaylisys.setLossHistory(LossHistoryType.ManuallyEntered);
        riskAnaylisys.addLossHistory(new Date(), "Cow Hit Me", 1000, 1000, 1000, "Closed");

        // quote
        GenericWorkorder go = new GenericWorkorder(driver);
        go.clickGenericWorkorderQuote();

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = uwIssuestab.getUnderwriterIssues();
        if (!uwIssues.isInList("Applicant has prior losses. Refer to underwriting prior to submitting to determine acceptability.").equals(UnderwriterIssueType.BlockSubmit)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "Did not get block submit on account with Prior Losses.");
        }

        new GuidewireHelpers(driver).editPolicyTransaction();

        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        riskAnaylisys.clickPriorLossesCardTab();
        riskAnaylisys.removeLossHistory(0);
        riskAnaylisys.setLossHistory(LossHistoryType.NoLossHistory);
        go = new GenericWorkorder(driver);
        go.clickGenericWorkorderQuote();
        sideMenu.clickSideMenuRiskAnalysis();
        uwIssues = uwIssuestab.getUnderwriterIssues();
        if (!uwIssues.isInList("Applicant has prior losses. Refer to underwriting prior to submitting to determine acceptability.").equals(UnderwriterIssueType.NONE)) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "UW Issues did not clear when condition was removed.");
        }

    }

}
