package regression.r2.noclock.contactmanager.contact;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.vendortransfer.Transfer;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
 * @Author sbroderick
 * @Requirement History Items are required on all transfers.
 * @RequirementsLink <a href="http://projects.idfbins.com/contactcenter/Documents/Story%20Cards/CM8%20-%20ContactManager%20-%20Create%20Contact%20-%20Create%20new%20contact.xlsx">Transfer Requirements</a>
 * @DATE Sep 7, 2016
 */
@QuarantineClass
public class TransferContact extends BaseTest {
	private WebDriver driver;
	private AbUsers abUser = null;
	private String vendorFrom = "Auto";
	private GenerateContact myContactObj = null;
	private GeneratePolicy myPolicyObj = null;
	private AdvancedSearchResults vendorFromSearchResults = null;
	private String vendorNumber = null;
	private String vendorType = null;
	private String vendorfromName;
	
	public void createVendorToTranferTo() throws Exception{
		AbUsers claimsUser = AbUserHelper.getRandomDeptUser("Claims");
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(claimsUser)
				.withCompanyName("Demis Pick and Pull")
				.withUniqueName(true)
				.build(GenerateContactType.Company);
	}
	
	@Test
	public void transferVendor() throws Exception{
		createVendorToTranferTo();
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		new GuidewireHelpers(driver).logout();
		new Login(driver).login(abUser.getUserName(), abUser.getUserPassword());
        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		this.vendorFromSearchResults = searchMe.getRandomVendor(vendorFrom);
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		this.vendorfromName = basicsPage.getContactDetailsBasicsContactName();
		this.vendorNumber = basicsPage.getVendorNumber();
		this.vendorType = basicsPage.getVendorType();
        SidebarAB sideMenu = new SidebarAB(driver);
		sideMenu.initiateTransfer();
		Transfer vendorTransfer = new Transfer(driver);
		vendorTransfer.transferContactNoAddress(myContactObj.companyName, myContactObj.addresses.get(0).getLine1(), ContactRole.Vendor.getValue(), vendorfromName, this.vendorNumber, vendorType);	
		//Check history		

        PageLinks pageLinks = new PageLinks(driver);
		pageLinks.clickContactDetailsBasicsHistoryLink();

        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		historyPage.verifyHistoryNoChangeDetail("Contact Transfer", "Vendor Transfer from ("+this.vendorNumber +" " + vendorfromName, DateUtils.dateFormatAsString("ddd, MMM dd, yyyy", new Date()));
		
	}
	
	@Test(dependsOnMethods = {"lienTransfer"})
	public void acctTransfer() throws Exception{
				
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");

        GenerateContact transferToContactObj = new GenerateContact.Builder(driver)
				.withCreator(abUser)
				.withFirstLastName("Transfer", "Tothisaccount")
				.withGenerateAccountNumber(true)
				.build(GenerateContactType.Person);

		new GuidewireHelpers(driver).logout();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndSearchContact(abUser, myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(), myPolicyObj.pniContact.getAddress().getLine1(), myPolicyObj.pniContact.getAddress().getState());

        SidebarAB sideMenu = new SidebarAB(driver);
		sideMenu.initiateAcctTransfer();

		Transfer transferAcct = new Transfer(driver);
		transferAcct.transferAcct(transferToContactObj.lastName + ", " + transferToContactObj.firstName, transferToContactObj.addresses.get(0).getLine1()," ");
		
		//need to check history on both transfer to and transfer from.

        PageLinks pageLinks = new PageLinks(driver);
		pageLinks.clickContactDetailsBasicsHistoryLink();

        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		historyPage.verifyHistoryNoChangeDetail("Information Transferred", "Account Information Transferred to Test Guy.", DateUtils.dateFormatAsString("ddd, MMM dd, yyyy", new Date()));
		
	}


    public void policyForlienTransfer() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
//		location building list.
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
				
		AddressInfo addIntAddress = new AddressInfo();
		addIntAddress.setLine1("550 Kinderkamack Road");
		addIntAddress.setCity("Oradell");
		addIntAddress.setState(State.NewJersey);
		addIntAddress.setZip("07649");//-0711
		
//		AdditionalInterest		
		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <AdditionalInterest>();
		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Freedom Bank", addIntAddress);
		loc1Bld1AddInterest.setNewContact(CreateNew.Do_Not_Create_New);
		loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
		
		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("LH Test")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	
	@Test
    public void lienTransfer() throws Exception {
		policyForlienTransfer();
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        GenerateContact newLien = new GenerateContact.Builder(driver)
				.withCompanyName("New Lien")
				.build(GenerateContactType.Company);
        TopMenuAB topMenu = new TopMenuAB(driver);
		topMenu.clickSearchTab();
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchLienholderNumber(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());

        SidebarAB sideMenu = new SidebarAB(driver);
		sideMenu.initiateLienTransfer();

		Transfer transferAcct = new Transfer(driver);																										
		transferAcct.transferLien(newLien.companyName, newLien.addresses.get(0).getLine1(), myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName(),"Lienholder", myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0,6), new ArrayList<AddressInfo>());
		
		//Need to check History items from the transfer to and the transfer from
        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();
        ContactDetailsHistoryAB historyPage = new ContactDetailsHistoryAB(driver);
		String contactName = historyPage.getContactTitle();
		if(contactName.equals(newLien.companyName)) {
			String found = historyPage.verifyHistoryNoChangeDetail("Contact Transfer", "Lienholder Transfer from ("+myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0, 6).trim() +" "+ myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName(), DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
			if(found.contains("History item not found")) {
				Assert.fail("After a Lien Transfer the history item was not found. \r\n Lien Number = " +myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0, 6).trim() +".\r\n"+ myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName());
			}

            topMenu = new TopMenuAB(driver);
			topMenu.clickSearchTab();
            searchMe = new AdvancedSearchAB(driver);
			searchMe.searchLienholderNumber(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0,6));
            links = new PageLinks(driver);
			links.clickContactDetailsBasicsHistoryLink();
            historyPage = new ContactDetailsHistoryAB(driver);
			found = historyPage.verifyHistoryNoChangeDetail("Contact Transfer", "Lienholder Transfer to ("+myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0, 6) +" "+ newLien.companyName, DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
			if(found.contains("History item not found")) {
				Assert.fail("After a Lien Transfer the history item was not found");
			}	
		} else {
			String found = historyPage.verifyHistoryNoChangeDetail("Contact Transfer", "Lienholder Transfer to ("+myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0, 6) +" "+newLien.companyName, DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
			if(found.contains("History item not found")) {
				Assert.fail("After a Lien Transfer the history item was not found");
			}
            topMenu = new TopMenuAB(driver);
			topMenu.clickSearchTab();
            searchMe = new AdvancedSearchAB(driver);
			searchMe.searchLienholderNumber(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0,6));
            links = new PageLinks(driver);
			links.clickContactDetailsBasicsHistoryLink();
            historyPage = new ContactDetailsHistoryAB(driver);
			found = historyPage.verifyHistoryNoChangeDetail("Contact Transfer", "Lienholder Transfer from ("+myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber().substring(0, 6).trim() +" "+ myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getCompanyName(), DateUtils.dateFormatAsString("MMM d, yyyy", DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
			if(found.contains("History item not found")) {
				Assert.fail("After a Lien Transfer the history item was not found");
			}
		}
	}	
}
