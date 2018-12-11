package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
 * @Author nvadlamudi
 * @Requirement : US15326: Unrequire Qualification questions on Umbrella QQ
 * US14795: Eliminate UW issues @ QQ, PL - UB008
 * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EXLlYMFF2E9OpE1cLcKmIBMBBHXDaL_CyJDsunqadnE76w?e=q0z3J6">US14795 PL QQ UW Issues to Disable</a>
 * @Description : Removing Qualification page from QQ and validating UB008 is not showing in QQ
 * @DATE Jun 6, 2018
 */
public class UmbrellaQQUWIssuesAndQualPageCheck extends BaseTest {
    private WebDriver driver;
    private GeneratePolicy myQQPolObj;

    @Test
    public void testUmbrellaQQUWIssues() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(property1);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(12);
        locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.squirePA = squirePersonalAuto;

        myQQPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPolOrgType(OrganizationType.Individual).withLineSelection(LineSelection.PropertyAndLiabilityLinePL,
                        LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.QuickQuote);
        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_3000000);

        myQQPolObj.squireUmbrellaInfo = squireUmbrellaInfo;
        myQQPolObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.QuickQuote);
        new Login(driver).loginAndSearchSubmission(myQQPolObj);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        boolean testPassed = true;
        String errorMessage = "QuickQuote - Umbrella - UW Issues for the account : " + myQQPolObj.accountNumber;
        FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
        if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getInformationalList().size() > 0
                || allUWIssues.getBlockSubmitList().size() > 0) {
            testPassed = false;
            errorMessage = errorMessage
                    + "Unexpected Block Quote, Block Submit and Informatonal UW Issues displayed in QQ. \n";
        }

        Assert.assertTrue(testPassed, errorMessage);


    }
}
