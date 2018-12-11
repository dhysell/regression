package regression.r2.noclock.policycenter.submission_misc.subgroup7;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestUmbrella extends BaseTest {

    private GeneratePolicy squireFullApp;

    private WebDriver driver;

    @Test()
    public void testCreateUnderlyingCitySquireFullApp_PropertyAndLiability() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);

        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
        liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Guy", "Citysqwumb")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

    }

    @Test(dependsOnMethods = {"testCreateUnderlyingCitySquireFullApp_PropertyAndLiability"})
    public void testCreateUmbrellaPolicy_CitySquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testCreateUmbrellaPolicy_CitySquire"})
    public void testUmbrellaQualificationsValidationMessages_PolicyInfoPage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setPL_UmbrellaPolicyAutoCoverAll(false);
        quals.answerFollowupDiv("Self Insure 1 car", "underlying policy cover all owned or leased automobiles",
                "Explain");
        quals.setPL_UmbrellaPolicyHagerty(true);
        quals.setPL_UmbrellaPolicyCoverToys(false);
        quals.setPL_UmbrellaPolicyOutOfStateOperations(true);
        quals.answerFollowupDiv("Out of State Operation", "there any out-of-state operations", "Explain");
        quals.setPL_UmbrellaPolicyLiabilityLosses(true);
        quals.answerFollowupDiv("Big Money coming my way",
                "liability losses paid or now reserved in amounts greater than", "Explain");
        quals.clickQualificationNext();

        quals = new GenericWorkorderQualification(driver);
        List<WebElement> nonBlockingMessages = quals.getValidationMessages();
        ArrayList<String> requiredMessages = new ArrayList<>();
        requiredMessages
                .add("Liability losses greater than $50,000 as respects accidents during the past five (5) years.");
        requiredMessages.add("Out-of-state operations.");
        requiredMessages.add("Add Endorsement 217 to Umbrella policy.");
        requiredMessages.add("Underlying policy does not cover all owned or leased automobiles.");
        requiredMessages.add("Add all items to section II liability of underlying Squire policy.");

        boolean found = false;
        for (WebElement nonBlockingMessage : nonBlockingMessages) {
            String messageFromUI = nonBlockingMessage.getText();
            if (requiredMessages.contains(messageFromUI)) {
                found = true;
            }
            if (!found) {
                Assert.fail(
                        "Could not find the UI message: \"" + messageFromUI + "\" in the list of expected messages");
            } else {
                found = false;
            }
        }

        quals.setUmbrellaQuestionsFavorably();
        quals.clickQualificationNext();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        // Check Org Type Matches Squire
        String orgTypeExpected = squireFullApp.polOrgType.getValue();
        String orgTypeActual = policyInfo.getPolicyInfoOrganizationType();
        if (!orgTypeExpected.equals(orgTypeActual)) {
            Assert.fail("The Umbrella Org Type does not match the Squire Policy Org Type.");
        }

        // Check the Name that it matches Squire
        String policyHolderNameExpected = squireFullApp.pniContact.getFirstName() + " " + squireFullApp.pniContact.getLastName();
        String policyHolderNameActual = policyInfo.getPolicyInfoName();
        if (!policyHolderNameExpected.equals(policyHolderNameActual)) {
            Assert.fail("The insureds name does not match the Squire Policy.");
        }

        // Check Mailing Address Matches Squire
        String addressLine1Expected = squireFullApp.pniContact.getAddress().getLine1();
        String addressLine1Actual = policyInfo.getPolicyInfoUmbrellaMailingAddress();
        if (!addressLine1Actual.contains(addressLine1Expected)) {
            Assert.fail("The Umbrella Policy Address Line 1 does not match the Squire Policy Address.");
        }

        // Check effective date Matches Squire
        if (!squireFullApp.squire.getEffectiveDate().equals(policyInfo.getPolicyInfoEffectiveDate())) {
            Assert.fail("The Umbrella Policy Effective Date does not match the Squire Policy Effective Date.");
        }

        // Check Expiration Date is not Editable and Matches Squire
        if (!squireFullApp.squire.getExpirationDate().equals(policyInfo.getPolicyInfoExpirationDate())) {
            Assert.fail("The Umbrella Policy Expiration Date does not match the Squire Policy Expiration Date.");
        }

        // Check Underlying Policy is not Editable and Matches Squire
        String underlyingPolicyNumExpected = squireFullApp.squire.getPolicyNumber();
        String underlyingPolicyNumActual = policyInfo.getPolicyInfoPolicyNumber();
        if (!underlyingPolicyNumExpected.equals(underlyingPolicyNumActual)) {
            Assert.fail("The Umbrella Policy Number does not match the Squire Policy Number.");
        }

        // Check Agent is not Editable and Matches Squire
        String agentExpected = squireFullApp.agentInfo.agentPreferredName + " (" + squireFullApp.agentInfo.getAgentNum() + ")";
        /*String agentExpected = squireFullApp.agentInfo.getAgentFirstName() + " " + squireFullApp.agentInfo.getAgentLastName()*/
        String agentActual = policyInfo.getPolicyInfoAgent();
        if (!agentExpected.equals(agentActual)) {
            Assert.fail("The Umbrella Policy Agent does not match the Squire Policy Agent.");
        }
    }

    public GeneratePolicy getUmbrellaPolicy() {
        return this.squireFullApp;
    }

}
