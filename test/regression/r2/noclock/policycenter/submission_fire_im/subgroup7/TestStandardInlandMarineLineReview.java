package regression.r2.noclock.policycenter.submission_fire_im.subgroup7;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import repository.driverConfiguration.Config;
import repository.gw.enums.CoverageType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderStandardIMLineReview;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

@QuarantineClass
public class TestStandardInlandMarineLineReview extends BaseTest {

    public GeneratePolicy myPolicyObjSIM = null;
    private WebDriver driver;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
    }



    @Test
    public void testStandardIMLineReview() throws Exception {
        myPolicyObjSIM = createStandardIMPolicy(GeneratePolicyType.QuickQuote);
        Agents agent = myPolicyObjSIM.agentInfo;

        // Login and select the same account
        new Login(driver).loginAndSearchSubmission(agent.getAgentUserName(), agent.getAgentPassword(), myPolicyObjSIM.accountNumber);

        // Select full app
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();

        // Opening IM Line Review page
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyReview();//clickSideMenuStandardIMLineReview();

        GenericWorkorderStandardIMLineReview lineReview = new GenericWorkorderStandardIMLineReview(driver);

        // Validations
        validateIMLineReviewFarmEquipment(lineReview, myPolicyObjSIM.squire.inlandMarine.farmEquipment.get(0));
        validateIMLineReviewPersonalProperty(lineReview, myPolicyObjSIM.squire.inlandMarine.personalProperty_PL_IM.get(0));

        // Add & remove farm equipment and Validate Line Review page
        addRemoveFarmEquipmentValidateLineReview(lineReview);

    }

    /**
     * @param lineReview
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Adding Farm Equipment, Removing Farm Equipment and
     * validating line Review page
     * @DATE Mar 25, 2016
     */
    private void addRemoveFarmEquipmentValidateLineReview(GenericWorkorderStandardIMLineReview lineReview) throws Exception {
        boolean testFailed = false;
        String errorMessage = "";

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuStandardIMFarmEquipment();

        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);

        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
        farmEquipmentPage.addFarmEquip(myPolicyObjSIM.basicSearch, imFarmEquip);

        // Opening line review
        sideMenu.clickSideMenuPolicyReview();
        if (lineReview.getFarmEquipmentsCount() > 2)
            System.out.println("Newly added farm equipment is shown in the Line review page");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Newly added Farm Equipment is not available in the Line Review page.";
        }

        // Removing farm equipment
        sideMenu.clickSideMenuStandardIMFarmEquipment();
        farmEquipmentPage.clickFarmEqipmentTableSpecificCheckbox(2, true);
        farmEquipmentPage.ClickRemove();
        sideMenu.clickSideMenuPolicyReview();
        if (lineReview.getFarmEquipmentsCount() <= 2)
            System.out.println("Newly added farm equipment is removed from Line review page");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Newly added Farm Equipment is not removed from Line Review page.";
        }

        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    /**
     * @param lineReview
     * @param personalProperty
     * @Author nvadlamudi
     * @Description : Validate Line review with personal property details
     * @DATE Mar 25, 2016
     */
    private void validateIMLineReviewPersonalProperty(GenericWorkorderStandardIMLineReview lineReview, PersonalProperty personalProperty) {
        boolean testFailed = false;
        String errorMessage = "";
        lineReview.clickPersonalPropertyTab();

        if ("Tools".equals(lineReview.getPersonalPropertyTableCellByColumnName(1, "Type")))
            System.out.println("Expected Personal Property Type : Tools is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Type : Tools is not displayed.";
        }

        int currentLimit = personalProperty.getScheduledItems().get(0).getLimit();
        if (("" + currentLimit).contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Limit")))
            System.out.println("Expected Personal Property Limit : " + currentLimit + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Limit : " + currentLimit + " is not displayed.";
        }

        if (personalProperty.getDeductible().getValue().contains(lineReview.getPersonalPropertyTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected Personal Property Deductible : " + personalProperty.getDeductible().getValue() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    /**
     * @param lineReview
     * @param farmThing
     * @Author nvadlamudi
     * @Description : Validate Line Review page with Farm Equipment
     * @DATE Mar 25, 2016
     */
    private void validateIMLineReviewFarmEquipment(GenericWorkorderStandardIMLineReview lineReview, FarmEquipment farmThing) {
        boolean testFailed = false;
        String errorMessage = "";
        // lineReview.clickFarmEquipmentTab();

        if (farmThing.getIMFarmEquipmentType().getValue().contains(lineReview.getFarmEquipmentTableCellByColumnName(1, "Type"))) {
            System.out.println("Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is displayed.");
        } else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is not displayed.";
        }
        if (farmThing.getDeductible().getValue().contains(lineReview.getFarmEquipmentTableCellByColumnName(1, "Deductible")))
            System.out.println("Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm equipment deductible " + farmThing.getDeductible().getValue() + " is not displayed.";
        }

        double farmEquipmentLimit = farmThing.getScheduledFarmEquipment().get(0).getLimit();
        double farmEquipmentScheduleLimit = Double.parseDouble(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Limit"));

        if (farmEquipmentLimit - farmEquipmentScheduleLimit == 0)
            System.out.println("Expected farm Equipment Limit : " + farmEquipmentLimit + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Limit : " + farmEquipmentLimit + " is not displayed.";
        }

        if ((farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake()).contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Year/Make")))
            System.out.println("Expected farm Equipment Year Make: " + farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Year Make: " + farmThing.getScheduledFarmEquipment().get(0).getYear() + " " + farmThing.getScheduledFarmEquipment().get(0).getMake() + " is not displayed.";
        }

        if (farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment().contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "Type of Equipment")))
            System.out.println("Expected farm Equipment Schedule type: " + farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment Schedule type: " + farmThing.getScheduledFarmEquipment().get(0).getTypeOfEquipment() + " is not displayed.";
        }

        if (farmThing.getScheduledFarmEquipment().get(0).getVin().contains(lineReview.getFarmEquipmentVehicleTableCellByColumnName(1, "VIN/Serial #")))
            System.out.println("Expected farm Equipment VIN: " + farmThing.getScheduledFarmEquipment().get(0).getVin() + " is displayed.");
        else {
            testFailed = true;
            errorMessage = errorMessage + "Expected farm Equipment VIN: " + farmThing.getScheduledFarmEquipment().get(0).getVin() + " is not displayed.";
        }
        if (testFailed)
            Assert.fail(driver.getCurrentUrl() + errorMessage);
    }

    /**
     * @param policyType
     * @return
     * @throws Exception
     * @Author nvadlamudi
     * @Description : Creating Standard Farm Equipment policy
     * @DATE Mar 25, 2016
     */
    private GeneratePolicy createStandardIMPolicy(GeneratePolicyType policyType) throws Exception {

        ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
        imCoverages.add(InlandMarine.FarmEquipment);
        imCoverages.add(InlandMarine.PersonalProperty);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);

        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        // Personal Property
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.MedicalSuppliesAndEquipment);
        pprop.setDeductible(PersonalPropertyDeductible.Ded25);

        // Vin helper
        Vin vin = VINHelper.getRandomVIN();
        boolean printRequestXMLToConsole = false;
        boolean printResponseXMLToConsole = false;
        ServiceVINValidation testService = new ServiceVINValidation();
        ValidateVINResponse testResponse = testService.validateVIN2(testService.setUpTestValidateVINRequest(vin.getVin()), new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

        pprop.setYear(testResponse.getYear());
        pprop.setMake(testResponse.getMake());
        pprop.setModel(testResponse.getModel());
        pprop.setVinSerialNum(vin.getVin());
        pprop.setDescription("Testdescription");
        pprop.setLimit(1234);

        ArrayList<String> ppropAdditIns = new ArrayList<String>();
        ppropAdditIns.add("First Guy");
        pprop.setAdditionalInsureds(ppropAdditIns);

        PersonalPropertyScheduledItem scheduledItem = new PersonalPropertyScheduledItem();
        scheduledItem.setLimit(1000);
        scheduledItem.setDescription("Testing Scheduled Item");
        ArrayList<PersonalPropertyScheduledItem> scheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        scheduledItems.add(scheduledItem);
        pprop.setScheduledItems(scheduledItems);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ArrayList<PersonalProperty> msaeList = new ArrayList<PersonalProperty>();
        msaeList.add(pprop);
        ppropList.setMedicalSuppliesAndEquipment(msaeList);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = imCoverages;
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        myPolicyObjSIM = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Test", "Stdimlinerev")
                .build(policyType);

        return myPolicyObjSIM;
    }

}
