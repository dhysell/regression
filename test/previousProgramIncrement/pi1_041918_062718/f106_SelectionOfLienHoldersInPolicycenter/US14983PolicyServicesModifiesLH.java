package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

import java.util.ArrayList;


/**
* @Author sbroderick
* @Requirement Policy Services needs permissions in ContactManager to be able to modify Lienholders.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/214521113572">Add permissions to Policy Services to add/change lienholders </a>
* @Description Specific Policy Services users can edit the Lienholder on a submission in Draft status.
* @DATE Apr 24, 2018
*/
public class US14983PolicyServicesModifiesLH extends BaseTest {
	private WebDriver driver;
	@Test
	public void testPolicyServicesModifiesLien() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Bldg1AddInterest.setFirstMortgage(true);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
        
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);		
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);
		
		locOnePropertyList.add(loc1Bldg1);			
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		
		
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "AdditionalInterest")
				.build(GeneratePolicyType.QuickQuote);
		
        driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		login = new Login(driver);
		//Must fix this when more policyServices have this permission.
		AbUsers abUser = AbUserHelper.getUserByUserName("dorozco");
		login.loginAndSearchJob(abUser.getUserName(), abUser.getUserPassword(), myPolicyObjPL.accountNumber);
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();

        GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
        aiPage.removeAdditionalInterest(loc1Bldg1AddInterest.getCompanyName());
        aiPage.clickOK();
        aiPage.clickEdit();
        
        AdditionalInterest newAI = new AdditionalInterest(ContactSubType.Company);
        newAI.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
        newAI.setFirstMortgage(true);
        newAI.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIProperty);

        aiPage = new GenericWorkorderAdditionalInterests(driver);
        aiPage.fillOutAdditionalInterest(true, newAI);

//        aiPage = new GenericWorkorderAdditionalInterests(driver);
//        String additionaInterest = aiPage.getAdditionalInterestsName();

        aiPage = new GenericWorkorderAdditionalInterests(driver);
        String additionalInterest = aiPage.getAdditionalInterestNameFromTable(newAI.getCompanyName());
        
        if(!additionalInterest.contains(newAI.getCompanyName())) {
        	Assert.fail("The Additional Interest should be change after a policy Services employee changes the interest. \r\n Old Lienholder = " + loc1Bldg1AddInterest.getCompanyName() +".  \r\n New Additional Interest should be: " +newAI.getCompanyName()+".");
        }
	}
}
