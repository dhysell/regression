package regression.r2.noclock.policycenter.submission_misc.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;

/**
 * @Author skandibanda
 * @Requirement : US8842: PL - Section level Additional insured
 * @Description : Checking for question Is this a Trust? under New Policy Member
 * screen and also checking for additional insured count on Section
 * II Coverages when additional insured members added with
 * selection of additional insured YES, Trust is YES/NO and one of
 * the options selected yes
 * @DATE Aug 23, 2016
 */
public class TestSectionLevelAdditionalInsured2 extends BaseTest {
    private GeneratePolicy sLiabPol;

    /**
     * @Author sbroderick
     * @Requirement : US8733: Pl - Add/Move Additional Insured to each section
     * @Description : need to assign the contacts that are section level
     * additional insureds as "Additional Insured" role.
     * @DATE Sept 7, 2016
     */
    @Test()
    private void createStandardLiabPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        ArrayList<Contact> policyMembers = new ArrayList<Contact>();
        Contact propertyAI = new Contact();
        ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
        aiLines.add(LineSelection.StandardLiabilityPL);
        propertyAI.setAdditionalInsured(aiLines);
        policyMembers.add(propertyAI);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(5);
        locationsList.add(propLoc);

        ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
        productLines.add(LineSelection.StandardFirePL);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);
        myStandardLiability.setPolicyMembers(policyMembers);

        sLiabPol = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("stdLiability", "Dues")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchJob(sLiabPol.agentInfo.getAgentUserName(), sLiabPol.agentInfo.getAgentPassword(), sLiabPol.accountNumber);
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuPolicyReview();
        GenericWorkorderPolicyReviewPL policyReview = new GenericWorkorderPolicyReviewPL(driver);
        ArrayList<String> additionalInsureds = policyReview.getAdditionalInsureds();
        if (!additionalInsureds.get(0).contains(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
            Assert.fail(driver.getCurrentUrl() + sLiabPol.accountNumber + "The only Additional Insured created during generate should be on the App.");
        }

    }

}
