package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoCommercialAutoLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoStateInfoCPP;
import repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto;
import repository.pc.workorders.generic.GenericWorkorderVehicles;

public class CAExclusionAndConditions extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;
    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                    this.setVehicleType(VehicleType.Miscellaneous);
                    this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                    this.setMotorHomeHaveLivingQuarters(true);
                    this.setMotorHomeContentsCoverage(true);
                    this.setComprehensive(true);
                    this.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                }});
            }
        };

        final ArrayList<Contact> personList = new ArrayList<Contact>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Contact() {{
                    this.setGender(Gender.randomGender());
                    this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
                }});
                this.add(new Contact() {{
                    this.setGender(Gender.randomGender());
                    this.setExcludedDriver(true);
                }});
            }
        };

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);

        }};

        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CA E&C")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void validateExclusionsAndConditions() throws Exception {
        boolean testFailed = false;
        String failureList = "";
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP comauto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        comauto.clickExclutionsAndConditionsTab();

        //CA Line Exclusions and Conditions


        //Amphibious Vehicles CA 23 97
        if (!guidewireHelpers.isElectable("Amphibious Vehicles CA 23 97")) {
            testFailed = true;
            failureList = failureList + "Amphibious Vehicles CA 23 97 was NOT Electable \n";
        }

        //Employee Hired Autos CA 20 54
        if (!guidewireHelpers.isElectable("Employee Hired Autos CA 20 54")) {
            testFailed = true;
            failureList = failureList + "Employee Hired Autos CA 20 54 was NOT Electable \n";
        }

        //Exclusion Of Federal Employees Using Autos In Government Business CA 04 42
        if (!guidewireHelpers.isElectable("Exclusion Of Federal Employees Using Autos In Government Business CA 04 42")) {
            testFailed = true;
            failureList = failureList + "Exclusion Of Federal Employees Using Autos In Government Business CA 04 42 was NOT Electable \n";
        }

        //Explosives CA 23 01
        if (!guidewireHelpers.isElectable("Explosives CA 23 01")) {
            testFailed = true;
            failureList = failureList + "Explosives CA 23 01 was NOT Electable \n";
        }

        //Professional Services Not Covered CA 20 18
        //Public Transportation Autos CA 24 02
        boolean isSpecial = false;
        boolean isPublic = false;
        for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
            switch (vehicle.getBodyType()) {
                case FuneralLimo:
                case Hearse:
                case FlowerCar:
                case ChurchBus:
                case MotelCourtesyBus:
                    isSpecial = true;
                    break;
                default:
                    break;
            }
            switch (vehicle.getVehicleType()) {
                case PublicTransportation:
                    isPublic = true;
                    break;
                default:
                    break;
            }
        }
        if (isSpecial) {
            if (!guidewireHelpers.isRequired("Professional Services Not Covered CA 20 18")) {
                testFailed = true;
                failureList = failureList + "Professional Services Not Covered CA 20 18 was NOT Required \n";
            }
        } else {
            if (!guidewireHelpers.isElectable("Professional Services Not Covered CA 20 18")) {
                testFailed = true;
                failureList = failureList + "Professional Services Not Covered CA 20 18 was NOT Electable \n";
            }
        }
        if (isPublic) {
            if (!guidewireHelpers.isRequired("Public Transportation Autos CA 24 02")) {
                testFailed = true;
                failureList = failureList + "Public Transportation Autos CA 24 02 was NOT Required \n";
            }
        }


        //Wrong Delivery Of Liquid Products CA 23 05
        //Transport Milk - NO
        if (!guidewireHelpers.isElectable("Wrong Delivery Of Liquid Products CA 23 05")) {
            testFailed = true;
            failureList = failureList + "Wrong Delivery Of Liquid Products CA 23 05 was NOT Electable \n";
        }
        //Transport Milk - YES
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification_CommercialAuto qual = new GenericWorkorderQualification_CommercialAuto(driver);
        qual.clickCA_TransportMilk(true);
        sideMenu.clickSideMenuCACommercialAutoLine();
        comauto.clickExclutionsAndConditionsTab();
        if (!guidewireHelpers.isRequired("Wrong Delivery Of Liquid Products CA 23 05")) {
            testFailed = true;
            failureList = failureList + "Wrong Delivery Of Liquid Products CA 23 05 was NOT Required \n";
        }

        sideMenu.clickSideMenuQualification();
        qual.clickCA_TransportMilk(false);
        sideMenu.clickSideMenuCACommercialAutoLine();
        comauto.clickExclutionsAndConditionsTab();

        //Silica Or Silica-related Dust Exclusion For Covered Autos Exposure CA 23 94
        if (!guidewireHelpers.isRequired("Silica Or Silica-related Dust Exclusion For Covered Autos Exposure CA 23 94")) {
            testFailed = true;
            failureList = failureList + "Silica Or Silica-related Dust Exclusion For Covered Autos Exposure CA 23 94 was NOT Required \n";
        }

        //Liability Coverage For Recreational Or Personal Use Trailers IDCA 31 3005
        if (myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().isLiability()) {
            if (!guidewireHelpers.isRequired("Liability Coverage For Recreational Or Personal Use Trailers IDCA 31 3005")) {
                testFailed = true;
                failureList = failureList + "Liability Coverage For Recreational Or Personal Use Trailers IDCA 31 3005 was NOT Required \n";
            }
        }

        //Designated Insured For Covered Autos Liability Coverage CA 20 48


        //Loss Payable Clause IDCA 31 3001


        //Waiver Of Transfer Of Rights Of Recovery Against Others To Us (Waiver Of Subrogation) CA 04 44

        //Excluded Driver Endorsement IDCA 31 3007
        for (Contact driver : myPolicyObjCPP.commercialAutoCPP.getDriversList()) {
            if (driver.isExcludedDriver()) {
                if (!guidewireHelpers.isRequired("Excluded Driver Endorsement IDCA 31 3007")) {
                    testFailed = true;
                    failureList = failureList + "Excluded Driver Endorsement IDCA 31 3007 was NOT Required \n";
                }
                break;
            }
        }


        //Mobile Homes Contents Not Covered IDCA 31 3003(on Vehicles E&C)
        //Removal Of Property Damage Coverage IDCA 31 3006(on Vehicles E&C)
        sideMenu.clickSideMenuCAVehicles();
        GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(driver);
        for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
            switch (vehicle.getBodyType()) {
                case MotorHomesMoreThan22Feet:
                case MotorHomesUpTo22Feet:
                case TrailerEquippedAsLivingQuarters:
                    vehicles.editVehicleByVin(vehicle.getVin());
                    vehicles.clickVehicleExclusionsAndConditionsTab();
                    if (vehicle.isMotorHomeHaveLivingQuarters() && !vehicle.isMotorHomeContentsCoverage()) {
                        if (!guidewireHelpers.isRequired("Mobile Homes Contents Not Covered IDCA 31 3003")) {
                            testFailed = true;
                            failureList = failureList + "Mobile Homes Contents Not Covered IDCA 31 3003 was NOT Required \n";
                        }
                    }
                    vehicles.clickOK();
                    break;
                default:
                    break;
            }
        }

        //Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40(on vehicles E&C)
        vehicles.editVehicleByVin(myPolicyObjCPP.commercialAutoCPP.getVehicleList().get(0).getVin());
        vehicles.clickVehicleExclusionsAndConditionsTab();
        if (!guidewireHelpers.isElectable("Exclusion or Excess Coverage Hazards Otherwise Insured CA 99 40")) {
            testFailed = true;
            failureList = failureList + "Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40 was NOT Electable \n";
        }

        vehicles.clickCancel();


        //Rolling Stores CA 23 04
        sideMenu.clickSideMenuQualification();
        qual.clickCA_RetailBasis(true);
        sideMenu.clickSideMenuCACommercialAutoLine();
        comauto.clickExclutionsAndConditionsTab();
        if (!guidewireHelpers.isRequired("Rolling Stores CA 23 04")) {
            testFailed = true;
            failureList = failureList + "Rolling Stores CA 23 04 was NOT Required \n";
        }
        sideMenu.clickSideMenuQualification();
        qual.clickCA_RetailBasis(false);
        sideMenu.clickSideMenuCACommercialAutoLine();
        comauto.clickExclutionsAndConditionsTab();


        //Idaho Changes CA 01 18
        sideMenu.clickSideMenuCAStateInfo();
        GenericWorkorderCommercialAutoStateInfoCPP state = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
        state.clickExclusionsandconditionsTab();
        if (!guidewireHelpers.isRequired("Idaho Changes CA 01 18")) {
            testFailed = true;
            failureList = failureList + "Idaho Changes CA 01 18 was NOT Required \n";
        }

        //UW ONLY
        //Out Of State Vehicle Exclusion IDCA 31 3011
        //Commercial Auto Manuscript Endorsement IDCA 31 3013
        //Condition
        //Exclusion


        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureList);
        }


    }


}




















