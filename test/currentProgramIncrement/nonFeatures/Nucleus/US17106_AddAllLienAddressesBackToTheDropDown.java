package currentProgramIncrement.nonFeatures.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Nov 21, 2018
* 
	Ensure that all inforce addresses for the specific lien are displayed
	Ensure that the addresses are selectable
	Ensure that the policy is updated with the selected office number
	Ensure that the payer assignment screen is updated with the newly selected office number
	Ensure that billing center receives the updated office number if new address is selected from the drop down 
	Ensure that the field office Agent, PA, SA, or CSR's do not have these drop downs. 
	
	This test also tests US17086 which is a hotfix for the same behavior with similarly written acceptance criteria.
 
*/
public class US17106_AddAllLienAddressesBackToTheDropDown extends BaseTest{

	private WebDriver driver;
	private GeneratePolicy myPolicy = null;
	private GenerateContact lienholder = null;
	
	@Test
    public void testGuidewireServiceRepsCanCreateLiens() throws Exception {
        AbUsers user = AbUserHelper.getRandomUserByTitle("Guidewire Service Rep");
        
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);
        ArrayList<AddressInfo> addresses = new ArrayList<>();
        for(int i = 0; i<3; i++ ) {
        	addresses.add(new AddressInfo(true));
        }
        
        this.lienholder = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withRoles(rolesToAdd)
                .withCompanyName("Robbery")
                .withAddresses(addresses)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

       this.driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
       
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(lienholder.companyName, addresses.get(0));
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

       this.myPolicy = new GeneratePolicy.Builder(this.driver)
               .withSquire(mySquire)
               .withProductType(ProductLineType.Squire)
               .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
               .withInsFirstLastName("Squire", "AdditionalInterest")
               .build(GeneratePolicyType.QuickQuote);
       
       ensurefieldOfficeDoesNotHaveAddressListDropdown();
       Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
       Login login = new Login(this.driver);
       login.loginAndSearchJob(uw.underwriterUserName, uw.underwriterPassword, this.myPolicy.accountNumber);

       GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
				
       SideMenuPC sideMenu = new SideMenuPC(driver);
       sideMenu.clickSideMenuSquirePropertyDetail();

       GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
       building.clickEdit();
		
       GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
       aiPage.clickBuildingsPropertyAdditionalInterestsLink(loc1Bldg1AddInterest.getCompanyName());
       String officeNumAddress = aiPage.getOfficeNumber();
       for(AddressInfo address : addresses) {
    	   Assert.assertTrue(aiPage.setAddressListing(address.getLine1()), "Each Lienholder address must be able to be selected.");
       }
       aiPage.setAddressListing(lienholder.addresses.get(this.lienholder.addresses.size()-1).getLine1());
       Assert.assertTrue(!aiPage.getOfficeNumber().equals(officeNumAddress), "The office Number in the address must change when a new address is selected.");
       aiPage.clickUpdate();
       building = new GenericWorkorderBuildings(driver);
       building.clickOK();
		
       sideMenu = new SideMenuPC(driver);
       sideMenu.clickSideMenuPayerAssignment();
       
       GenericWorkorderPayerAssignment payerAssign = new GenericWorkorderPayerAssignment(driver);
       ArrayList<String> payersList = payerAssign.getBillingOptions();
       boolean addressFound = false;
       for(String payer : payersList) {
    	   if(payer.contains(lienholder.addresses.get(this.lienholder.addresses.size()-1).getLine1())) {
    		   addressFound = true;
    	   }
       }
       Assert.assertTrue(addressFound, "The Lienholder listed in PayerAssignment should have the change of address.");       
	}
	
	public void ensurefieldOfficeDoesNotHaveAddressListDropdown() throws Exception {
		Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
		
		Login login = new Login(this.driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);
		
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
				
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
	
		GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
		building.clickEdit();
		
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		aiPage.clickBuildingsPropertyAdditionalInterestsLink(this.myPolicy.getLocationList(this.myPolicy).get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName());
		Assert.assertFalse(aiPage.setAddressListing(this.lienholder.addresses.get(0).getLine1()), "The agent has the ability to change the Lienholder address.");
		aiPage.clickUpdate();
		aiPage.clickOK();
		new GuidewireHelpers(driver).logout();
		
	}
	
	@Test
	public void testLienChangeChangesInBC() throws Exception {
		AbUsers user = AbUserHelper.getRandomUserByTitle("Guidewire Service Rep");
        
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);
        ArrayList<AddressInfo> addresses = new ArrayList<>();
        for(int i = 0; i<3; i++ ) {
        	addresses.add(new AddressInfo(true));
        }
        
        this.lienholder = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withRoles(rolesToAdd)
                .withCompanyName("Robbery")
                .withAddresses(addresses)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

       this.driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
       
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(lienholder.companyName, addresses.get(0));
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

       this.myPolicy = new GeneratePolicy.Builder(this.driver)
               .withSquire(mySquire)
               .withProductType(ProductLineType.Squire)
               .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
               .withInsFirstLastName("Squire", "AdditionalInterest")
               .withDownPaymentType(PaymentType.Cash)
               .withPaymentPlanType(PaymentPlanType.Annual)
               .build(GeneratePolicyType.PolicyIssued);
       		
       Login login = new Login(this.driver);
       login.loginAndSearchPolicy_asUW(myPolicy);
       
       StartPolicyChange policyChange = new StartPolicyChange(driver);
       policyChange.startPolicyChange("Change Lien Address", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
				
       SideMenuPC sideMenu = new SideMenuPC(driver);
       sideMenu.clickSideMenuSquirePropertyDetail();

       GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
       building.clickEdit();
		
       GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
       aiPage.clickBuildingsPropertyAdditionalInterestsLink(loc1Bldg1AddInterest.getCompanyName());
       String officeNumber = aiPage.getOfficeNumber();
       aiPage.setAddressListing(lienholder.addresses.get(this.lienholder.addresses.size()-1).getLine1());
       Assert.assertTrue(!aiPage.getOfficeNumber().equals(officeNumber), "The office Number in the address must change when a new address is selected.");
       officeNumber = aiPage.getOfficeNumber();
       aiPage.clickUpdate();
       String lienAcctNumber = aiPage.getAdditionalInterestAccountNumber(loc1Bldg1AddInterest.getCompanyName());
       System.out.println(lienAcctNumber.length()-2);
       String uiOffice = lienAcctNumber.substring(lienAcctNumber.length()-2);
       System.out.println("uiOffice number = "+ uiOffice + ". \r\n The officeNumAddress is " + officeNumber.substring(officeNumber.length()-2));
       System.out.println();
       Assert.assertTrue(uiOffice.equals(officeNumber.substring(officeNumber.length()-2)), "The Office Number must match the changed office number, which will be in BC.");       
       building = new GenericWorkorderBuildings(driver);
       building.clickOK();     
       
       sideMenu = new SideMenuPC(driver);
       sideMenu.clickSideMenuPayerAssignment();
       
       GenericWorkorderPayerAssignment payerAssign = new GenericWorkorderPayerAssignment(driver);
       ArrayList<String> payersList = payerAssign.getBillingOptions();
       boolean addressFound = false;
       for(String payer : payersList) {
    	   if(payer.contains(lienholder.addresses.get(this.lienholder.addresses.size()-1).getLine1())) {
    		   addressFound = true;
    	   }
       }
       Assert.assertTrue(addressFound, "The Lienholder listed in PayerAssignment should have the change of address.");
	}
}
