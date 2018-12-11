package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.Building.OccupancyInterestType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.globaldatarepo.entities.Agents;

public class LossOfRentalValueCoverage extends BaseTest {

    private GeneratePolicy myPolicyObj;
    private String accountNumber;
    GenericWorkorderBuildings building;
    GenericWorkorderBuildingAdditionalCoverages buildAddtlCover;
    private String agentUsername;
    private String agentPassword;
    private WebDriver driver;

    @Test
    public void generate() throws Exception {

        // customizing location and building
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Loss RentalValue")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        accountNumber = myPolicyObj.accountNumber;
        Agents agent = myPolicyObj.agentInfo;
        agentUsername = agent.getAgentUserName();
        agentPassword = agent.getAgentPassword();
    }

    /**
     * @throws Exception
     * @author drichards
     * @Requirement US5547
     * @Link <a href="http:// ">Link Text</a>
     * @Description Tests if the Loss of Rental Value coverage is hidden when
     * certain interest types are selected. As of right now, this
     * fix is only in trunk.
     * @DATE Sep 23, 2015
     */
    @Test(dependsOnMethods = {"generate"}, enabled = true)
    public void testHideBP0593() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(agentUsername, agentPassword, accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        building = new GenericWorkorderBuildings(driver);
        building.clickBuildingsBuildingEdit(1);
        OccupancyInterestType interestType;

        // PNI owner = yes, area leased = 0-10, BO - Operator and Condo - Assoc;
        // coverage hidden
        building.setInsuredOwner(true);
        building.setSqFtPercentOccupied("75%-100%");
        building.setPercentAreaLeassedToOthers("0%-10%");

        interestType = OccupancyInterestType.BuildingOwnerOperator;
        building.setInterestType(interestType);
        // check
        isCoverageHidden(interestType.getValue());

        interestType = OccupancyInterestType.CondoAssociation;
        building.setInterestType(interestType);
        // check
        isCoverageHidden(interestType.getValue());

        // PNI owner = yes, area leased = 90-100, BO - Less Risk and Condo -
        // Assoc; coverage hidden
        building.setPercentAreaLeassedToOthers("90%-100%");
        interestType = OccupancyInterestType.BuildingOwnerLessorsRisk;
        building.setInterestType(interestType);
        // check
        isCoverageHidden(interestType.getValue());

        interestType = OccupancyInterestType.CondoAssociation;
        building.setInterestType(interestType);
        // check
        isCoverageHidden(interestType.getValue());

        // PNI owner = no, both interests coverage is there
        building.setInsuredOwner(false);
        interestType = OccupancyInterestType.CondoUnitOwner;
        building.setInterestType(interestType);
        // check
        doesCoverageExist(interestType.getValue());

        interestType = OccupancyInterestType.TenantOperator;
        building.setInterestType(interestType);
        // check
        doesCoverageExist(interestType.getValue());

        // check to see if the coverage stays
        building.setInsuredOwner(true);
        building.setSqFtPercentOccupied("75%-100%");
        building.setPercentAreaLeassedToOthers("0%-10%");

        interestType = OccupancyInterestType.BuildingOwnerOperator;
        building.setInterestType(interestType);
        // check
        try {
            isCoverageHidden(interestType.getValue());
        } catch (Exception e) {
            throw new Exception("The Loss of Rental Coverage is still visible when it shouldn't be for interest type "
                    + interestType.getValue());
        }

    }

    private void doesCoverageExist(String interest) throws Exception {
        building.clickAdditionalCoverages();
        buildAddtlCover = new GenericWorkorderBuildingAdditionalCoverages(driver);
        try {
            buildAddtlCover.setLossOfRentalValueLandlordAsDesignatedPayeeCoverage(null);
        } catch (NoSuchElementException nsee) {
            throw new NoSuchElementException(
                    "The Loss of Rental Coverage is not visible for the interest type " + interest, nsee);
        } catch (NullPointerException npe) {
        } catch (Exception e) {
            throw new Exception(
                    "Unspecified exception when checking for the Loss of Rental Coverage for interest type " + interest,
                    e);
        }
        building.clickBuildingDetails();
    }

    private void isCoverageHidden(String interest) throws Exception {
        building.clickAdditionalCoverages();
        GenericWorkorderBuildingAdditionalCoverages buildAddtlCover = new GenericWorkorderBuildingAdditionalCoverages(driver);
        try {
            buildAddtlCover.setLossOfRentalValueLandlordAsDesignatedPayeeCoverage(null);
        } catch (NoSuchElementException nsee) {
        } catch (NullPointerException npe) {
            throw new Exception(
                    "The Loss of Rental Value coverage should not be visible for the interest type " + interest);
        } catch (Exception e) {
            throw new Exception(
                    "Unspecified exception when checking for the Loss of Rental Coverage for interest type " + interest,
                    e);
        }
        building.clickBuildingDetails();
    }
}
