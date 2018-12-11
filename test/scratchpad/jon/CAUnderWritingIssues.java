package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

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
import repository.gw.enums.Vehicle.BusinessUseClass;
import repository.gw.enums.Vehicle.LongDistanceOptions;
import repository.gw.enums.Vehicle.Radius;
import repository.gw.enums.Vehicle.SecondaryClass;
import repository.gw.enums.Vehicle.SecondaryClassType;
import repository.gw.enums.Vehicle.SizeClass;
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
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoDriver;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoStateInfoCPP;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles;

public class CAUnderWritingIssues extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;
    private WebDriver driver;

    @SuppressWarnings("serial")
    public List<String> otherCarCoverages = new ArrayList<String>() {{
        this.add("Comprehensive");
        this.add("Liability");
        this.add("Medical Payments");
        this.add("Underinsured Motorists");
        this.add("Uninsured Motorists");
        this.add("Collision");
    }};

    @SuppressWarnings("serial")
    List<String> usIssueList = new ArrayList<String>() {{
        this.add("${Vehicle Number, Year, Make, Model, VIN} ong distance option set to Denver. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.");
        this.add("${Vehicle Number, Year, Make, Model, VIN} long distance option set to Portland. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.");
        this.add("Applicant/insured has rejected Idaho Uninsured and Underinsured Motorist coverage complete rejection form IDCA 10 0002 and submit to underwriting.");
    }};




    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList) {{
            this.setNonSpecificLocation(true);
        }});

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }
        };

        final ArrayList<Contact> personList = new ArrayList<Contact>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                    this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
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
        setRandomUWIssues(commercialAuto);

        // GENERATE POLICY
        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CA UWIssues")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyUWIssues() throws Exception {
        boolean testFailed = false;
        String failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);


        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);

        sideMenu.clickSideMenuRiskAnalysis();
        List<String> recievedUWIssues = risk.getUWIssuesListLongDescription();

        for (String issue : usIssueList) {
            if (!verifyUWIssue(issue, recievedUWIssues)) {
                testFailed = true;
                failureString = issue + " \n";
                System.out.println(failureString);
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + " " + myPolicyObjCPP.accountNumber + " " + failureString);
        }
    }


    private boolean verifyUWIssue(String uwIssue, List<String> uwIssueList) {
        for (String longDescription : uwIssueList) {
            if (uwIssue.contains(longDescription)) {
                return true;
            }
        }
        return false;
    }


    private void setRandomUWIssues(CPPCommercialAuto myCommAutoObject) {

        for (String issue : usIssueList) {
            setBlockBindUWIssue(issue, myCommAutoObject);
        }
    }

    @SuppressWarnings("unused")
    private String setBlockBindUWIssue(String uwIssue, CPPCommercialAuto myCommAutoObject) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderCommercialAutoCommercialAutoLineCPP comAutoLine = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(driver);
        GenericWorkorderCommercialAutoStateInfoCPP stateInfo = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
        GenericWorkorderCommercialAutoDriver drivers = new GenericWorkorderCommercialAutoDriver(driver);

        switch (uwIssue) {
            case "Applicant/insured has rejected Idaho Uninsured and Underinsured Motorist coverage complete rejection form IDCA 10 0002 and submit to underwriting.":
                //If customer choose to reject coverage. (does not include hired symbol 8 or non-owned auto symbol 9) Then display the message.
//			//remove UIM UM 
                if (new GuidewireHelpers(driver).getRandBoolean()) {
                    myCommAutoObject.getCPP_CAStateInfo().setUnderinsuredMotoristCA3118(false);
                } else {
                    myCommAutoObject.getCPP_CAStateInfo().setUninsuredMotoristCA3115(false);
                }
                break;
//		case "${Vehicle Number, Year, Make, Model, VIN} Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40 has been selected.  Provide underwriting details as to the other coverage in place and reason that WC is not insuring the autos.":
            case "Exclusion Or Excess Coverage Hazards Otherwise Insured CA 99 40 has been selected.  Provide underwriting details as to the other coverage in place and reason that WC is not insuring the autos.":
                //If the CA 99 40 endorsement is selected display the message.
                //vehicle E&C
                break;
            case "There are vehicles over 45,001 lbs. and drivers under 25. Please submit to underwriting for consideration.":
                //If there are any Extra-heavy Trucks (GVW) over 45,000 pounds and any drivers under 25. If there are then display the message in the Risk Analysis
                Vehicle myHeavyVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.Local0To100Miles);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(myHeavyVehicle);
                Contact youngPerson = new Contact() {{
                    this.setAge(driver, 23);
                }};
                myCommAutoObject.getDriversList().add(youngPerson);
                break;
            case "Please add drivers to the policy.":
                //A driver is required at Full Application when a there is a Auto and/or Garagekeepers on the policy.
                break;
            case "${Vehicle Number, Year, Make, Model, VIN} long distance option set to Denver. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.":
                //When Long Distance Option of Denver is selected display the message.
                Vehicle myDenverVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.LongDistancesOver301Miles);
                    this.setLongDistanceOptions(LongDistanceOptions.Denver);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(myDenverVehicle);
                break;
            case "${Vehicle Number, Year, Make, Model, VIN} long distance option set to Portland. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.":
                //When Long Distance Option of Portland is selected display the message.
                Vehicle myPortlandVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.LongDistancesOver301Miles);
                    this.setLongDistanceOptions(LongDistanceOptions.Portland);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(myPortlandVehicle);
                break;
            case "${Vehicle Number, Year, Make, Model, VIN} long distance option set to Salt Lake. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.":
                //When Long Distance Option of Salt Lake is selected display the message.
                Vehicle mySaltLakeVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.LongDistancesOver301Miles);
                    this.setLongDistanceOptions(LongDistanceOptions.SaltLakeCity);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(mySaltLakeVehicle);
                break;
            case "${Vehicle Number, Year, Make, Model, VIN} long distance option set to Pacific. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.":
                //When Long Distance Option of Pacific is selected display the message.
                Vehicle myPacificVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.LongDistancesOver301Miles);
                    this.setLongDistanceOptions(LongDistanceOptions.Pacific);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(myPacificVehicle);
                break;
            case "${Vehicle Number, Year, Make, Model, VIN} long distance option set to Mountain. Provide underwriting with number of trips and products transported. Reinsurance limitations must be considered and addressed.":
                //When Long Distance Option of Mountain is selected display the message.
                Vehicle myMountainVehicle = new Vehicle() {{
                    this.setVehicleType(VehicleType.Trucks);
                    this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                    this.setRadius(Radius.LongDistancesOver301Miles);
                    this.setLongDistanceOptions(LongDistanceOptions.Mountain);
                    this.setBusinessUseClass(BusinessUseClass.CommercialUse);
                    this.setSecondaryClassType(SecondaryClassType.Truckers);
                    this.setSecondaryClass(SecondaryClass.CommonCarriers);
                }};
                myCommAutoObject.getVehicleList().add(myMountainVehicle);
                break;
        }//END SWITCH

        return null;
    }//END BLOCK BIND


}



















