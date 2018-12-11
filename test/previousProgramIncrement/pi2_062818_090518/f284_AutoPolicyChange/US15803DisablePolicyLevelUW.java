package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement :US15803: Disable/change UW Issues - Policy Level
 * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EfkD5tWiAwlAq85ITIpy3M8B9Hg5URkky9PGfOGjeHJQnA?e=u0CR2b">UW Issues to Change for Auto Issue</a>
 * @Description : Validate SQ002 in submission and policy change - expiration date change transaction.
 * @DATE Jul 20, 2018
 */
public class US15803DisablePolicyLevelUW extends BaseTest {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void testCheckPolicyLevelUWIssuesInSubmission() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;


        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("PolLevel", "Block")
                .isDraft()
                .build(GeneratePolicyType.FullApp);
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
                myPolicyObjPL.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        // SQ002 - Term type other
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoTermType("Other");

        // SQ016 - Prior losses
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
        risk.clickUWIssuesTab();
        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();


        softAssert.assertFalse(
                !uwIssues.isInList(PLUWIssues.TermTypeOther.getLongDesc()).equals(UnderwriterIssueType.BlockQuote),
                "Expected Blocking Quote : " + PLUWIssues.TermTypeOther.getShortDesc() + " is not displayed");

        softAssert.assertAll();
    }
}
