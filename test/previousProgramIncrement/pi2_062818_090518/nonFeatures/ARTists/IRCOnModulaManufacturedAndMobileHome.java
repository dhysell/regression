package previousProgramIncrement.pi2_062818_090518.nonFeatures.ARTists;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author nvadlamudi
 * @Requirement: US15838: Make IRC unavailable on the UI for modular/manufactured and mobile homes
 * mobile homes
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/240199600500">This is the end goal for DE7592 which was the quick fix</a>
 * @Description: validate IRC is not available for properties and in submission and other jobs.
 * @DATE Jul 13, 2018
 */
public class IRCOnModulaManufacturedAndMobileHome extends BaseTest {
    private WebDriver driver;
	private GeneratePolicy myPolObj;

    @Test()
    public void testIRCFieldMobileManufacturedNewSubmission() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty property = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        property.setConstructionType(ConstructionTypePL.Frame);

        PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        property1.setConstructionType(ConstructionTypePL.MobileHome);

        PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        property2.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
        property3.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
        property4.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property5 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
        property5.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property6 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
        property6.setConstructionType(ConstructionTypePL.MobileHome);

        PLPolicyLocationProperty property7 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
        property7.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property8 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremisesCovE);
        property8.setConstructionType(ConstructionTypePL.MobileHome);

        PLPolicyLocationProperty property9 = new PLPolicyLocationProperty(PropertyTypePL.VacationHomeCovE);
        property9.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property10 = new PLPolicyLocationProperty(PropertyTypePL.VacationHomeCovE);
        property10.setConstructionType(ConstructionTypePL.MobileHome);

        PLPolicyLocationProperty property11 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
        property11.setConstructionType(ConstructionTypePL.ModularManufactured);

        PLPolicyLocationProperty property12 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstructionCovE);
        property12.setConstructionType(ConstructionTypePL.MobileHome);

        locOnePropertyList.add(property);
        locOnePropertyList.add(property1);
        locOnePropertyList.add(property2);
        locOnePropertyList.add(property3);
        locOnePropertyList.add(property4);
        locOnePropertyList.add(property5);
        locOnePropertyList.add(property6);
        locOnePropertyList.add(property7);
        locOnePropertyList.add(property8);
        locOnePropertyList.add(property9);
        locOnePropertyList.add(property10);
        locOnePropertyList.add(property11);
        locOnePropertyList.add(property12);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(15);

        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Artists", "IRC").isDraft()
                .withDownPaymentType(PaymentType.Cash).withPaymentPlanType(PaymentPlanType.Annual)
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission(myPolObj.agentInfo.getAgentUserName(),
                myPolObj.agentInfo.getAgentPassword(), myPolObj.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
                driver);
        String errorMessage = "";
        for (PLPolicyLocationProperty currentProperty : myPolObj.squire.propertyAndLiability.locationList.get(0)
                .getPropertyList()) {
            coverages.clickSpecificBuilding(1, currentProperty.getPropertyNumber());
            if (currentProperty.getPropertyNumber() == 1 && !coverages.IncreasedReplacementCostExists()) {
                errorMessage = errorMessage + "Expected Increased Replacement Cost  is 'Frame' Construction Type not displayed \n";
            }
            if (currentProperty.getPropertyNumber() > 1 && coverages.IncreasedReplacementCostExists()) {
                errorMessage = errorMessage + " Unexpected Increased Replacement Cost for "
                        + currentProperty.getConstructionType() + " Construction Type \n";
            }
        }
        Assert.assertTrue(errorMessage == "", "Account Number: '" + myPolObj.accountNumber + "' - New submission: " + errorMessage);
    }

    @Test()
    public void testIRCFieldMobileManufacturedPolicyChange() throws Exception {
    	 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
         driver = buildDriver(cf);

         ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
         ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
         PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
         property1.setConstructionType(ConstructionTypePL.MobileHome);

         PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
         property2.setConstructionType(ConstructionTypePL.ModularManufactured);

         locOnePropertyList.add(property1);
         locOnePropertyList.add(property2);
         PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
         locToAdd.setPlNumAcres(11);
         locToAdd.setPlNumResidence(15);

         locationsList.add(locToAdd);

         SquireLiability myLiab = new SquireLiability();
         myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

         SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
         myPropertyAndLiability.locationList = locationsList;
         myPropertyAndLiability.liabilitySection = myLiab;

         Squire mySquire = new Squire(SquireEligibility.City);
         mySquire.propertyAndLiability = myPropertyAndLiability;

          myPolObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                 .withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withPolOrgType(OrganizationType.Individual)
                 .withPolTermLengthDays(78).withInsFirstLastName("Artists", "IRC").isDraft()
                 .withDownPaymentType(PaymentType.Cash).withPaymentPlanType(PaymentPlanType.Annual)
                 .build(GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolObj.agentInfo.getAgentUserName(),
                myPolObj.agentInfo.getAgentPassword(), myPolObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

        // start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
                driver);
        String errorMessage = "";
        for (PLPolicyLocationProperty currentProperty : myPolObj.squire.propertyAndLiability.locationList.get(0)
                .getPropertyList()) {
            coverages.clickSpecificBuilding(1, currentProperty.getPropertyNumber());
            if (coverages.IncreasedReplacementCostExists()) {
                errorMessage = errorMessage + " Unexpected Increased Replacement Cost for "
                        + currentProperty.getConstructionType() + " Construction Type \n";
            }
        }
        driver.quit();
        Assert.assertTrue(errorMessage == "",
                "Account Number: '" + myPolObj.accountNumber + "' - Policy Change : " + errorMessage);

    }

    @Test(dependsOnMethods = {"testIRCFieldMobileManufacturedPolicyChange"})
    public void testIRCFieldMobileManufacturedRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolObj.squire.getPolicyNumber());
        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myPolObj);
        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
                driver);
        String errorMessage = "";
        for (PLPolicyLocationProperty currentProperty : myPolObj.squire.propertyAndLiability.locationList.get(0)
                .getPropertyList()) {
            coverages.clickSpecificBuilding(1, currentProperty.getPropertyNumber());
           if (coverages.IncreasedReplacementCostExists()) {
                errorMessage = errorMessage + " Unexpected Increased Replacement Cost for "
                        + currentProperty.getConstructionType() + " Construction Type \n";
            }
        }
        driver.quit();
        Assert.assertTrue(errorMessage == "",
                "Account Number: '" + myPolObj.accountNumber + "' - Renewal : " + errorMessage);

    }
}
