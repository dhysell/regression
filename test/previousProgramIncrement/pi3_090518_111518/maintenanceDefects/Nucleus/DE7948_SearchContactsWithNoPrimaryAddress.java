package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Nucleus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.ab.scriptclasses.PCContactsNoPrimaryAddress;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchResultsReturnPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderLocationAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;


public class DE7948_SearchContactsWithNoPrimaryAddress extends BaseTest {
	
	private List<PCContactsNoPrimaryAddress> contactInfo = new ArrayList<>();

	@Test
	public void testSearchContactsWithNoPrimaryAddress() throws GuidewireException, Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter, "dev");
        WebDriver driver = buildDriver(cf);

        GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("PolicyInsuredOnly")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.getRandom())
                .build(GeneratePolicyType.QuickQuote);
        
        Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLocations();
        
        getLineItems();
        PolicyLocationAdditionalInsured newLocationInsured;
        int randomContactNumber;
        do {
        	randomContactNumber = NumberUtils.generateRandomNumberInt(0, this.contactInfo.size() - 1);
        }while(this.contactInfo.get(randomContactNumber).getFirstName().contains("NULL") && this.contactInfo.get(randomContactNumber).getName().contains("NULL"));

        if(contactInfo.get(randomContactNumber).getFirstName().equals("NULL")) {
        	newLocationInsured = new PolicyLocationAdditionalInsured(ContactSubType.Company, this.contactInfo.get(randomContactNumber).getName(), AdditionalInsuredRole.ManagersOrLessorsOrPremises, null);
        } else {
        	newLocationInsured = new PolicyLocationAdditionalInsured(ContactSubType.Person, this.contactInfo.get(randomContactNumber).getFirstName(),  this.contactInfo.get(randomContactNumber).getLastName(), AdditionalInsuredRole.ManagersOrLessorsOrPremises, null);
        }
              
        GenericWorkorderLocations locationsPage = new GenericWorkorderLocations(driver);
        locationsPage.clickLocationsLocationEdit(1);
        GenericWorkorderLocationAdditionalInsured addtlLocInsured = new GenericWorkorderLocationAdditionalInsured(driver);
        addtlLocInsured.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        SearchResultsReturnPC results;
        if(newLocationInsured.getCompanyOrPerson().equals(ContactSubType.Person)) {
        	results = searchPC.searchContactWithoutAddress(true, newLocationInsured.getPersonFirstName(), newLocationInsured.getPersonLastName(), CreateNew.Do_Not_Create_New);
        	Assert.assertTrue(results.isFound(), "Unable to find the contact "+newLocationInsured.getPersonFirstName()+" "+ newLocationInsured.getPersonLastName() +". This person should be in PC but not have an address. Please investigate.");
        } else {
        	results = searchPC.searchContactWithoutAddress(true, null, newLocationInsured.getCompanyName(), CreateNew.Do_Not_Create_New);
        	Assert.assertTrue(results.isFound(), "Unable to find the contact "+newLocationInsured.getCompanyName() +". This Company should be in PC but not have an address. Please investigate.");
        }
        
	}
	
	
	
/*
 * 
 * 					Below are methods to get a unique contact that does not have a primary address.
 * 
 */
	
	public void getLineItems() throws Exception {
		this.contactInfo = processInputFile("\\\\fbmsqa11\\testing_data\\test-documents\\PCContactsNoPrimaryAddr.csv");
	}
	
	private List<PCContactsNoPrimaryAddress> processInputFile(String inputFilePath) throws IOException {
	    List<PCContactsNoPrimaryAddress> inputList = new ArrayList<>();
	    try{
	      File inputF = new File(inputFilePath);
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
	      br.close();
	    } catch (IOException e) {
	    	System.out.println("An error occured while trying to process the file.");
	    	throw e;
	    }
	    return inputList;
	}
	
	private Function<String, PCContactsNoPrimaryAddress> mapToItem = (line) -> {
		ArrayList<PCContactsNoPrimaryAddress> lineItems = new ArrayList<PCContactsNoPrimaryAddress>();  
		String[] p = line.split(",");// a CSV has comma separated lines  
		PCContactsNoPrimaryAddress item = new PCContactsNoPrimaryAddress();
		  if(p.length>=4) {
			  item.setName(p[3]);
		  }
		  if(p.length>=3) {
			  String[] lastNameArray = p[2].split(" ");
			  item.setLastName(lastNameArray[0]);
		  }
		  if(p.length>=2) {
			  String[] firstNameArray = p[1].split(" ");
			  item.setFirstName(firstNameArray[0]);
		  }
		  if(p.length>=1) {
			  item.setPublicID(p[0]);
		  }
		  return item;	  
	};

}
