package previousProgramIncrement.pi3_090518_111518.nonFeatures.Achievers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.UnderwriterIssues_PL;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

public class US15309_RemoveAU018 extends BaseTest {
	
	private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    
    @Test(enabled=true)
    public void generatePolicyWithUMandUIMselected() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        
        List<UnderwriterIssues_PL> uwIssuesList = new ArrayList<UnderwriterIssues_PL>();
        uwIssuesList.add(UnderwriterIssues_PL.PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018);
        
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        Vehicle myVehicle = new Vehicle(VehicleType.PrivatePassenger);
        myVehicle.setVin("2GTEK19R3T1522833");
        myVehicle.setComprehensive(true);
        myVehicle.setCollision(true);
        squirePersonalAuto.getVehicleList().clear();
        squirePersonalAuto.addToVehiclesList(myVehicle);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .isDraft()
                .build(GeneratePolicyType.FullApp);
        
        new Login(driver).loginAndSearchSubmission(myPolicyObject);
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        new GenericWorkorder(driver).clickGenericWorkorderQuote();
        if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
        	new GenericWorkorderQuote(driver).clickPreQuoteDetails();
        }
        
        FullUnderWriterIssues uwissues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
        Assert.assertTrue(uwissues.isInList(UnderwriterIssues_PL.PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018.getLongDescription().replace("${Vehicle description}", myVehicle.getModelYear() + " " + myVehicle.getMake() + " " + myVehicle.getModel())).equals(UnderwriterIssueType.NONE), UnderwriterIssues_PL.PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018.name() + " WAS INFERED WHEN A VEHICLE OVER 20 YEARS OLD WITH COM & COLL. THIS UW ISSUES SHOULD HAVE BEEN REMOVED.");
    }

}
