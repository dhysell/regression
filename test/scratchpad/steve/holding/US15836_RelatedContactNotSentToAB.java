package scratchpad.steve.holding;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;

import java.util.ArrayList;


public class US15836_RelatedContactNotSentToAB extends BaseTest{
	private GeneratePolicy myPolicy;
	private WebDriver driver;
	
	@Test
    public void relateContactToPNI() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);
	    createBop();
	    new Login(driver).loginAndSearchAccountByAccountNumber(myPolicy.agentInfo.agentUserName, myPolicy.agentInfo.agentPassword, myPolicy.accountNumber);
	    AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
	    accountSummaryPage.clickWorkOrderbySuffix("001");
	    SideMenuPC menu = new SideMenuPC(driver);
	    menu.clickSideMenuBusinessownersLine();
	    GenericWorkorderAdditionalNamedInsured pcAdditionalInsuredPage = new GenericWorkorderAdditionalNamedInsured(driver);
	    ArrayList<String> uiAiList = pcAdditionalInsuredPage.getAdditionalInsuredsName();
	    String uiAI = uiAiList.get(0);
	    if(!(uiAI.contains(myPolicy.busOwnLine.getAdditonalInsuredBOLineList().get(0).getPersonFirstName()) && uiAI.contains(myPolicy.busOwnLine.getAdditonalInsuredBOLineList().get(0).getPersonLastName()))){
	    	Assert.fail("The Additional Insured should be added to the Policy");
	    }
	    menu = new SideMenuPC(driver);
	    menu.clickSideMenuLocations();
	    pcAdditionalInsuredPage = new GenericWorkorderAdditionalNamedInsured(driver);
	    ArrayList<String> locationsAI = pcAdditionalInsuredPage.getAdditionalInsuredsName();
	    if(!(locationsAI.get(0).contains(myPolicy.busOwnLine.locationList.get(0).getAdditionalInsuredLocationsList().get(0).getPersonFirstName()) && 
	    		locationsAI.get(0).contains(myPolicy.busOwnLine.locationList.get(0).getAdditionalInsuredLocationsList().get(0).getPersonLastName()))) {
	    	Assert.fail("The Additional Insured should be added to the policy");
	    }	
	}

    public void createBop() throws Exception {
			
	    
		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		
		PolicyBusinessownersLineAdditionalInsured boAdditionalInsured = new PolicyBusinessownersLineAdditionalInsured();
		boAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		boAdditionalInsured.setRelationshipToPrimaryNamedInsured(true);
		boAdditionalInsured.setCompanyOrPerson(ContactSubType.Person);
		boAdditionalInsured.setRelationship(RelationshipsAB.Spouse);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<>();
		additonalInsuredBOLineList.add(boAdditionalInsured);
		
		myline.setAdditonalInsuredBOLineList(additonalInsuredBOLineList);
		myline.setAdditionalCoverageStuff(myLineAddCov);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setClassClassification("storage");
		loc1Bldg1.setUsageDescription("Insured Building");
	
		locOneBuildingList.add(loc1Bldg1);
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(), locOneBuildingList);
		
		PolicyLocationAdditionalInsured locationAddInsured = new PolicyLocationAdditionalInsured();
		locationAddInsured.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		locationAddInsured.setCompanyOrPerson(ContactSubType.Company);
		locationAddInsured.setRelationshipToPrimaryNamedInsured(true);
		locationAddInsured.setRelationship(RelationshipsAB.Subsidiary);
		ArrayList<PolicyLocationAdditionalInsured> additionalInsuredLocationsList = new ArrayList<>();
		additionalInsuredLocationsList.add(locationAddInsured);
		
		location1.setAdditionalInsuredLocationsList(additionalInsuredLocationsList);
		locationsList.add(location1);

		this.myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("New", "Pni")
				.withPolOrgType(OrganizationType.Individual)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
	}	
}
