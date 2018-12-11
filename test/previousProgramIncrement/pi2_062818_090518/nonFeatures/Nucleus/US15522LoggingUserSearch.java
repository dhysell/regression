package previousProgramIncrement.pi2_062818_090518.nonFeatures.Nucleus;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;

import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;

/**
* @Author sbroderick
* @Requirement AS Dave S I want to have user searches logged so that I can have documentation of users searching using Solr. 
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/sites/TeamNucleus/Shared%20Documents/Forms/AllItems.aspx?id=%2Fsites%2FTeamNucleus%2FShared%20Documents%2FGeneral%2FDocumentation%2FPDF%2FUS15522%20-%20CM%20-%20Logging%20User%20Search%2Epdf&parent=%2Fsites%2FTeamNucleus%2FShared%20Documents%2FGeneral%2FDocumentation%2FPDF&p=true&slrid=f0607b9e-40d1-6000-2e0c-b3bf71f6767f">Documentation</a>
* @Description 
* @DATE Jul 16, 2018
*/
public class US15522LoggingUserSearch extends BaseTest {
//	private AdditionalInterest loc1Property1AdditionalInterest = null;
	private String fileName = null;
	private WebDriver driver = null;


/*  Per requirements we nixed the search Logs for AB
	@Test
	public void searchAB() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);		
		
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
				.withInsFirstLastName("Squire", "Policyholder")
				.build(GeneratePolicyType.QuickQuote);
		
        driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        loc1Bldg1AdditionalInterests.add(loc1Property1AdditionalInterest);
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        SearchResultsReturnPC searchResults = searchPC.searchContactWithAddress(null, loc1Property1AdditionalInterest.getCompanyName(), loc1Property1AdditionalInterest.getAddress().getLine1(),  null, null, null, CreateNew.Do_Not_Create_New);
        searchResults.getSelectToClick().click();
        driver.quit();
        cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        this.fileName = "\\\\"+cf.getUrl().substring(7,13) +"\\tmp\\FBMSolr\\FBMSolr.log";
        
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        int index = content.indexOf("Additional Interest Search");
        if(index<0) {
        	Assert.fail("The search was not logged in the ab logs.");
        }
        driver.quit();
    }
*/
	
	@Test
	public void searchPC() throws Exception {
		Contacts contact = ContactsHelpers.getRandomContact();
		Agents agent = AgentsHelper.getRandomAgent();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        if (new Login(driver).accountLocked()) {
        	agent = new Login(driver).loginAsRandomAgent();
        }
        new TopMenuPolicyPC(driver).clickNewSubmission();
        if(contact.isContactIsCompany()) {
        	new SubmissionNewSubmission(driver).searchAddressBookByCompanyName(true, contact.getContactName(), contact.getContactAddressLine1(), contact.getContactCity(), State.valueOfName(contact.getContactState()), contact.getContactZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
        } else {
        	ArrayList<String> nameArray = StringsUtils.lastFirstMiddleInitialNameParser(contact.getContactName());
        	String contactFirstName = nameArray.get(0);
        	String contactLastName = nameArray.get(nameArray.size()-1);
        	new SubmissionNewSubmission(driver).searchAddressBookByFirstLastName(true, contactFirstName, contactLastName, new AddressInfo(contact.getContactAddressLine1(), contact.getContactCity(), State.valueOfName(contact.getContactState()), contact.getContactZip()), CreateNew.Create_New_Only_If_Does_Not_Exist);
        }
        String[] hostURL = cf.getUrl().split("/");        
        if(hostURL[0]==null || hostURL[0].equals("http:")) {
        	if(hostURL[2].contains(":")) {
        		String[] url = hostURL[2].split(":");
        		this.fileName = "\\\\"+url[0] +"\\tmp2\\FBMSolr\\FBMSolr.log";
        	} else {
        		this.fileName = "\\\\"+hostURL[2] +"\\tmp\\FBMSolr\\FBMSolr.log";
        	}
        } else {
        	if(hostURL[0].contains(":")) {
        		String[] url = hostURL[2].split(":");
        		this.fileName = "\\\\"+url[0]+"\\tmp2\\FBMSolr\\FBMSolr.log";
        	} else {
        		this.fileName = "\\\\"+hostURL[0]+"\\tmp\\FBMSolr\\FBMSolr.log";
        	}
        }
        
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        int index = content.indexOf("FBMSolr - Contact Search");
        if(index<0) {
        	Assert.fail("The search was not logged in the ab logs.");
        }
        driver.quit();
	}

}
