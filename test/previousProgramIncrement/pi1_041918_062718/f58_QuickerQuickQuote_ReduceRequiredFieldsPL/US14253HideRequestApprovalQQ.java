package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

import java.util.ArrayList;

/**
 * @Author nvadlamudi
 * @Requirement :US14253:  STRETCH:  Hide the request approval button at QQ
 * Acceptance Criteria: Ensure that the Request Approval button is not available on the Risk Analysis screen.
 * Should it be hidden?
 * Should it be grayed out?
 * With a tooltip?  <-- Management really likes this idea.
 * Ensure that the quote is not blocked by the Request Approval button not being available.
 * Ensure that the Request Approval button is still available on Full App and on a policy change.
 * @Description: Validate QQ - RiskANalysis page
 * @DATE May 15, 2018
 */
public class US14253HideRequestApprovalQQ extends BaseTest {
    private GeneratePolicy myQQPolObj;
    private String toolTip = "Request Approval not available due to no blocking UW Issues";
    private WebDriver driver;
    
    @Test(enabled = true)
    public void testCheckRequestApprovalSquireQQ() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(12);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_500000_CSL);
        myLiab.setMedicalLimit(SectionIIMedicalLimit.Limit_25000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("QQRApp", "US14253")
                .withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);

        new Login(driver).loginAndSearchSubmission(myQQPolObj.agentInfo.getAgentUserName(), myQQPolObj.agentInfo.getAgentPassword(),
                myQQPolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        Assert.assertTrue(risk.checkRequestApprovalDisabled() == true,
                "'Request Approval' button is Enabled in QQ");

        Assert.assertTrue(risk.getRequestApporvalToolTip().contains(toolTip),
                "'Request Approval' button tooltip '" + toolTip + "' is not displayed in QQ");

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);

        guidewireHelpers.setPolicyType(myQQPolObj, GeneratePolicyType.QuickQuote);

        guidewireHelpers.logout();
        myQQPolObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }
}
