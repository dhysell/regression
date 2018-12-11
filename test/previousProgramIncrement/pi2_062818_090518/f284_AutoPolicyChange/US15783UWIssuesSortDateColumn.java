package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

import java.util.ArrayList;
import java.util.List;

/**
* @Author nvadlamudi
* @Requirement : US15783: UW Issues Sort and Date Column
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/237843468824">UW Issues Sort and Date Column</a>
* @Description : Validate UW issues are showing based on the creation date and date is shown 
* @DATE Aug 20, 2018
*/
@Test(groups = { "ClockMove" })
public class US15783UWIssuesSortDateColumn extends BaseTest {
    private WebDriver driver;
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void testRiskAnalysisUWIssuesSortingAndDateCreation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact student = new Contact();
        driversList.add(student);

        // vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        Vehicle vehicle2 = new Vehicle(VehicleTypePL.FarmTruck, "make new vin", 2003, "Oldie", "But Goodie");
        Vehicle vehicle3 = new Vehicle(VehicleTypePL.Motorcycle, "make new vin", 2003, "Oldie", "But Goodie");

        vehicleList.add(vehicle);
        vehicleList.add(vehicle2);
        vehicleList.add(vehicle3);

        // line auto coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // whole auto line
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);
        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL).withInsFirstLastName("Sub2", "UWIssue")
                .build(GeneratePolicyType.FullApp);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        // AU012 No Private Passenger or Pickups
        sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.removeVehicleByVehicleNumber(1);
        
        // AU043 Usage and Truck type for Semi-Trailer
        vehiclePage.editVehicleByType(VehicleTypePL.FarmTruck);
        vehiclePage.selectUsage(Usage.FarmUseWithOccasionalHire);
        vehiclePage.selectTruckType(VehicleTruckTypePL.UnderFour);
        vehiclePage.setOdometer(20000);
        vehiclePage.clickOK();

        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.SemiTrailer);
        vehiclePage.selectUsage(Usage.FarmUse);
        vehiclePage.selectTruckType(VehicleTruckTypePL.TractorType);
        vehiclePage.selectTrailer(TrailerTypePL.Semi);
        vehiclePage.selectGaragedAtZip("ID");
        vehiclePage.clickOK();
        
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
        new GuidewireHelpers(driver).logout();

        //Move clock and add more UW Issues         
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
        new GuidewireHelpers(driver).editPolicyTransaction();
        sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");		
		
        // AU086 Higher SRP unassigned
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(
                driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(
                myPolicyObjPL.squire.squirePA.getDriversList().get(0).getFirstName());
        paDrivers.clickSRPIncidentsTab();
        paDrivers.setNoProofInsuranceIncident(1);
        paDrivers.setInternationalDLIncident(1);
        paDrivers.setUnverifiableDrivingRecordIncident(1);
        paDrivers.clickOk();

        // AU091 CLUE Auto not ordered         
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
        @SuppressWarnings("serial")
        List<PLUWIssues> previousUWIssues = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.NoPrivatePassenferPickup);
                this.add(PLUWIssues.UsageAndTruckTypeSemiTrailer);
                this.add(PLUWIssues.MissingMSYear);
                this.add(PLUWIssues.MissingPhotoYear);
                this.add(PLUWIssues.CLUEAutoNotOrdered);
                this.add(PLUWIssues.CluePropertyNotOrder);
            }
        };

        
        @SuppressWarnings("serial")
        List<PLUWIssues> todayUWIssues = new ArrayList<PLUWIssues>() {
            {
                this.add(PLUWIssues.UnassignedDriverWithHighSRP);
                this.add(PLUWIssues.NonAllowedTickets);
                this.add(PLUWIssues.OrganizationTypeOfOther);
            }
        };

        String previousDate = DateUtils.dateFormatAsString("MM/dd/yyyy",DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -1));
       String errorMessage = "";
            boolean messageFound = false;
            for (PLUWIssues previousDay : previousUWIssues) { 
            	for (int i = 0; i < new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList().size(); i++) {
                    String currentUWIssueText = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList().get(i).getText();               
                    
	                if (currentUWIssueText.contains(previousDay.getShortDesc()) && currentUWIssueText.contains(previousDate) ) {
	                        messageFound = true;
	                        break;
	                 }
            	} 
            if (!messageFound) {
               errorMessage = errorMessage + "Expected UW Issues : " + previousDay + "'s Create Date is is not displayed as "+previousDate;
            }
        }
            
            
            messageFound = false;
            for (PLUWIssues todayDay : todayUWIssues) { 
            	for (int i = 0; i < new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList().size(); i++) {
                    String currentUWIssueText = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUWIssuesList().get(i).getText();               
                    
	                if (currentUWIssueText.contains(todayDay.getShortDesc()) && currentUWIssueText.contains(DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd/yyyy")) ) {
	                        messageFound = true;
	                        break;
	                 }
            	} 
            if (!messageFound) {
               errorMessage = errorMessage + "Expected UW Issues : " + todayDay + "'s Create Date is is not displayed as "+DateUtils.getCenterDateAsString(driver, ApplicationOrCenter.PolicyCenter, "MM/dd/yyyy");
            }
        }

        Assert.assertTrue(errorMessage=="", errorMessage);
        
    }

}
