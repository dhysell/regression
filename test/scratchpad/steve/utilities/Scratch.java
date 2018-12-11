//Steve Broderick
//This tests initiates a phone Change in AB and checks PC.

package scratchpad.steve.utilities;

import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.ab.scriptclasses.PCContactsNoPrimaryAddress;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;

@SuppressWarnings("unused")
public class Scratch extends BaseTest {

  
    private String firstName = "Pcmember";
    private String lastName = "Defects-180906110303";
    private String account = "295721";
    
    private AddressInfo address = new AddressInfo("3696 South 6800 West", "Salt Lake City", State.Utah, "84128-3420");
    private AbUsers user = null;
    private GenerateContact myContactObj = null;
    private String agentUserName = "cpackard";
    private String password = "gw";
    private Underwriters uw;
    private String abAcct = "273630";
    private String policyAcctNum = "294997";
    private String policyNum = "01-272944-01";
    private Date polEffectiveDate;
    private String myCSR = "aarchuleta";
    private List<String> propertyExclusions = new ArrayList<String>();
    private List<String> lineLevelExclusions = new ArrayList<String>();
    private Vin vin = null;
    private GeneratePolicy myPolicyObj;
    private String addressLine1 = "1292 El Rancho Blvd";
    private ARUsers arUser = null;
    private AddressInfo addressInfo = null;
    private String businessPhoneFormatted = "208-239-4369 x987";
    //283740 = account number to find lien to transfer
    private String lienNum = "985501";
    private String lienCompanyName = "Additional Inter-180116093910";
    private String newLienName = "New Lien";
    private AddressInfo newLienAddress = new AddressInfo();
    ArrayList<DeliveryOptionType> deliveryOptions = new ArrayList<>();
    
    private String pniFirstName = "Mr";
    private String pniLastName = "Pni-181003080524";

    private WebDriver driver;

    private String env = "dev";
    
    private List<PCContactsNoPrimaryAddress> contactInfo = new ArrayList<>();
    
    private AddressInfo correctAddress = new AddressInfo("800 SW 1ST St Trlr 6", "Grangeville", State.Idaho, "83530");
    	
    	@Test
        public void testGaragedAtAddress() throws Exception {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            WebDriver driver = buildDriver(cf);
                    
            SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.squirePA = squirePersonalAuto;

            LineSelection[] policyLines = new LineSelection[2];
            policyLines[0] = LineSelection.PersonalAutoLinePL;
            policyLines[1] = LineSelection.PropertyAndLiabilityLinePL;
            
            GeneratePolicy myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(policyLines)
                    .build(GeneratePolicyType.QuickQuote);
            
            new Login(driver).loginAndSearchSubmission(myPolicyObject);
            new GuidewireHelpers(driver).editPolicyTransaction();
            SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuPAVehicles();
            
            GenericWorkorderVehicles_Details vehicleDetailsPage = new GenericWorkorderVehicles_Details(driver);
            AddressInfo newGaragedAt = new AddressInfo(true);
            vehicleDetailsPage.setGaragedAt(newGaragedAt);
            Assert.assertTrue(vehicleDetailsPage.getGaragedAt().contains(newGaragedAt.getLine1()), "The user was unable to enter a new Garaged At Location.");
            vehicleDetailsPage.clickOK();
            
            sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuPolicyInfo();
            
            GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
            String mailingAddress = policyInfoPage.getPolicyInfoUmbrellaMailingAddress();
            Assert.assertTrue(mailingAddress.contains(myPolicyObject.pniContact.getAddress().getLine1()), "The PNI's mailing address does not match what was entered.  Ensure that the change at Garaged at location did not bork the mailing address.");
        }
}	