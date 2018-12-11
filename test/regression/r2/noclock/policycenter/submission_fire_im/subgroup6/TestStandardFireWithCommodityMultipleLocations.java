package regression.r2.noclock.policycenter.submission_fire_im.subgroup6;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLLocationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;

/**
 * @Author nvadlamudi
 * @Requirement :DE4127: "Add Existing Location" functionality does not enter the location selected .
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 1, 2016
 */
public class TestStandardFireWithCommodityMultipleLocations extends BaseTest {
    private GeneratePolicy stdFireLiab_Squire_PolicyObj;
    private AddressInfo newAddress, newAddress2;


    private WebDriver driver;


    @Test()
    public void testCreateSquirePolWithMultipleLocations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);

        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("SQ", "MultiLocs")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .build(GeneratePolicyType.FullApp);


        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchSubmission(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();

        newAddress = new AddressInfo();
        newAddress.setLine1("123 Main St");
        newAddress.setCity("Pocatello");
        newAddress.setZip("83201");
        newAddress.setCounty(CountyIdaho.Bannock);

        addLocation(newAddress);

        newAddress2 = new AddressInfo();
        newAddress2.setLine1("1085 Poplar Chase Lane");
        newAddress2.setCity("Boise");
        newAddress2.setZip("83709");
        newAddress2.setCounty(CountyIdaho.Boise);

        addLocation(newAddress2);

        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        } else {
            sideMenu.clickSideMenuRiskAnalysis();
        }

        guidewireHelpers.logout();

    }

    @Test(dependsOnMethods = {"testCreateSquirePolWithMultipleLocations"})
    public void testQuoteStandardFire() throws Exception {
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.Potatoes));
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locationsList1.add(locToAdd1);

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setStdFireCommodity(true);
        myStandardFire.setLocationList(locationsList1);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
        stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testQuoteStandardFire"})
    public void validateStandardFireLocations() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(stdFireLiab_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFireLiab_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFireLiab_Squire_PolicyObj.accountNumber);
        String errorMessage = "";
        new GuidewireHelpers(driver).editPolicyTransaction();


        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        location.addNewOrSelectExistingLocationFA(PLLocationType.Address, newAddress, 2, 2);
        location.addNewOrSelectExistingLocationFA(PLLocationType.Address, newAddress2, 2, 2);
        //validating the locations


        location.clickEditLocation(2);
        if (!location.getAddressLine1().contains(newAddress.getLine1()) || !location.getCity().contains(newAddress.getCity())) {
            errorMessage = errorMessage + "Expected address :" + newAddress.getLine1() + " not found. \n";
        }
        location.clickOK();


        location.clickEditLocation(3);
        if (!location.getAddressLine1().contains(newAddress2.getLine1()) || !location.getCity().contains(newAddress2.getCity())) {
            errorMessage = errorMessage + "Expected address :" + newAddress2.getLine1() + " not found. \n";
        }
        location.clickOK();

        location.clickEditLocation(1);
        if (!location.getAddressLine1().contains(this.stdFireLiab_Squire_PolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getLine1()) || !location.getCity().contains(this.stdFireLiab_Squire_PolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getCity())) {
            errorMessage = errorMessage + "Expected address :" + this.stdFireLiab_Squire_PolicyObj.squire.propertyAndLiability.locationList.get(0).getAddress().getLine1() + " not found. \n";
        }
        location.clickOK();

        if (!errorMessage.equals("")) {
            Assert.fail(errorMessage);
        }


    }

    private void addLocation(AddressInfo newAddress) {
        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        location.clickNewLocation();
        location.selectLocationAddress("New");
        location.addLocationInfoFA(newAddress, 2, 2);
        location.clickStandardizeAddress();
        try {
            location.clickLocationInformationOverride();
        } catch (Exception e) {
        }
        location.clickOK();
    }

}
