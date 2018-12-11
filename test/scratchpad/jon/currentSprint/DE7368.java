package scratchpad.jon.currentSprint;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PreRenewalDirectionExplanation;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyPreRenewalDirections;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.workorders.renewal.StartRenewal;

/**
 * @Author jlarsen
 * @Requirement Scenario 1
 * Create a squire policy
 * Have the properties all have the same category code (all A, all B)
 * Quote
 * Check that the calculated risk is around at least 2,000,000 or higher (if not, adjust the values of the properties so that it is) and that there are at least 5 buildings.
 * It can be any amount as long as between all of the buildings it adds up to 10 million or more (i.e. 5,000,000 and 2 buildings, 1,000,000 and 10 buildings)
 * Submit, issue
 * Start the renewal
 * Get a warning that there are pre renewal directions on the policy.
 * Actual: There is a pre renewal direction for the category code/calculated risk being over 10 million
 * Expected: No pre renewal direction for the category code/calculated risk being over 10 million.
 * <p>
 * Note: This is only happening for the pre renewal direction, not for the UW issue for $10 million policy (SQ041 Policy Level Forms Product Model)
 * <p>
 * Scenario 2
 * Create a squire policy
 * Have the properties all have the same category code (all A, all B)
 * Quote, submit issue
 * Start the renewal
 * Actual: The 2% inflation guard is applied to coverage A building, but not calculated on the category code/calculated risk when the renewal job is started (but not quoted).
 * Expected: The 2% inflation guard should be applied to both the coverage A buildings and calculated on the category code/calculated risk when the renewal job is started (but not quoted).
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/defect/208137528176">DE7368</a>
 * @Description Category code over 10 million pre renewal direction adding all of the same risk together
 * @DATE Apr 19, 2018
 */
public class DE7368 extends BaseTest {

    public GeneratePolicy myPolicyObject = null;

    private WebDriver driver;

    @Test(enabled = false)
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        PLPolicyLocationProperty propertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyOne.getPropertyCoverages().getCoverageA().setLimit(2000000);
        PLPolicyLocationProperty propertyTwo = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyTwo.getPropertyCoverages().getCoverageA().setLimit(2000000);
        PLPolicyLocationProperty propertyThree = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyThree.getPropertyCoverages().getCoverageA().setLimit(2000000);
        PLPolicyLocationProperty propertyFour = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyFour.getPropertyCoverages().getCoverageA().setLimit(2000000);
        PLPolicyLocationProperty propertyFive = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertyFive.getPropertyCoverages().getCoverageA().setLimit(2000000);
        PLPolicyLocationProperty propertySix = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        propertySix.getPropertyCoverages().getCoverageA().setLimit(2000000);

        locOnePropertyList.add(propertyOne);
        locOnePropertyList.add(propertyTwo);
        locOnePropertyList.add(propertyThree);
        locOnePropertyList.add(propertyFour);
        locOnePropertyList.add(propertyFive);
        locOnePropertyList.add(propertySix);
        locationsList.add(new PolicyLocation(locOnePropertyList));
        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("PreRenewal", "DE7368")
                .withPolOrgType(OrganizationType.Individual)
                .withPolTermLengthDays(79)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = false)
    public void verifyPreRenewalDirection() throws Exception {


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
        new StartRenewal(driver).startRenewal();
        new InfoBar(driver).clickInfoBarPolicyNumber();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.clickViewPreRenewalDirections();
        PolicyPreRenewalDirections myPreRenewalDirections = preRenewalPage.getPreRenewalDirections();
        if (myPreRenewalDirections.isAPreRenewalDirection(PreRenewalDirectionExplanation.CategoryCodeValuationOver10Million)) {
            Assert.fail("Pre-Renewal direction: " + PreRenewalDirectionExplanation.CategoryCodeValuationOver10Million.getCode() + " was present when no single property was over 10 Million Dollars");
        }


    }


}
