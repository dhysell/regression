package regression.r2.noclock.policycenter.submission_property.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLLocationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;

/**
 * @Author skandibanda
 * @Requirement :US7559: Separate Scheduled Items
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Apr 29, 2016
 */
public class TestSquirePropertyLiabilityPollutionCoverages extends BaseTest {
    private GeneratePolicy propertyLiabilityPol = null;

    private WebDriver driver;

    @Test(dependsOnMethods = "creatingPropertyLiabilityPolicy")
    public void validatePollutionCoverages() throws Exception {
        boolean testFailed = false;
        String errorMessage = "";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(propertyLiabilityPol.agentInfo.getAgentUserName(), propertyLiabilityPol.agentInfo.getAgentPassword(), propertyLiabilityPol.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();
        SideMenuPC sideBar = new SideMenuPC(driver);
        sideBar.clickSideMenuSquirePropertyDetail();
        sideBar.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

        int totalNoAces = section2Covs.getTotalAcres();
        String thirdPartyAcres = section2Covs.getPollutionLiabilityTableByRowColumnName(1, "Quantity");
        section2Covs.clickPollutionLiabilityTableCheckbox(2, true);
        String firstPartyAcres = section2Covs.getPollutionLiabilityTableByRowColumnName(2, "Quantity");
        section2Covs.clicknext();

        if (totalNoAces != Integer.parseInt(firstPartyAcres) || totalNoAces != Integer.parseInt(thirdPartyAcres)) {
            testFailed = true;
            errorMessage = errorMessage + "Total number of acres displayed on the Section II page is not matched";
        }

        sideBar.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        propertyLocations.clickEditLocation(1);
        int currentLocationsAcres = propertyLocations.getAcres();
        if (currentLocationsAcres != Integer.parseInt(firstPartyAcres) || currentLocationsAcres != Integer.parseInt(thirdPartyAcres)) {
            testFailed = true;
            errorMessage = errorMessage + "Total number of acres displayed on the Locations page is not matched";
        }
        int newAcres = 10;
//        propertyLocations.setAcres(newAcres);
        propertyLocations.clickStandardizeAddress();
        propertyLocations.clickOK();

        //adding new location
        AddressInfo newAddress = new AddressInfo();
        propertyLocations.addNewOrSelectExistingLocationFA(PLLocationType.Address, newAddress, newAcres, 5);
        sideBar.clickSideMenuSquirePropertyCoverages();
        coverages.clickSectionIICoveragesTab();

        totalNoAces = section2Covs.getTotalAcres();
        thirdPartyAcres = section2Covs.getPollutionLiabilityTableByRowColumnName(1, "Quantity");
        firstPartyAcres = section2Covs.getPollutionLiabilityTableByRowColumnName(2, "Quantity");

        if ((newAcres + newAcres) != Integer.parseInt(firstPartyAcres) || (newAcres + newAcres) != Integer.parseInt(thirdPartyAcres) || (newAcres + newAcres) != totalNoAces) {
            testFailed = true;
            errorMessage = errorMessage + "Newly added acres displayed on the Locations page is not matched";
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);

    }

    @Test()
    public void creatingPropertyLiabilityPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation resPrem = new PolicyLocation(locOnePropertyList);
        resPrem.setPlNumAcres(11);
        resPrem.setPlNumResidence(5);
        locationsList.add(resPrem);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        propertyLiabilityPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "PollCov")
                .build(GeneratePolicyType.FullApp);

    }
}
