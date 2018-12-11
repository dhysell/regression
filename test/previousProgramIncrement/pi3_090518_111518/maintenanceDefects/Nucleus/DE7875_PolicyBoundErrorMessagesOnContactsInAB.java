package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.administration.AdminMenu;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueuesSafeOrderObjectLinkOptions;
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
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class DE7875_PolicyBoundErrorMessagesOnContactsInAB extends BaseTest{
	private WebDriver driver;
	private GeneratePolicy myPolicy;
	
	@Test
	public void testStuckPolicyBoundMessages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		
        generatePolicy();
				
		AbUsers user = AbUserHelper.getRandomDeptUser("su");
		AdminMenu adminMenu = new AdminMenu(driver);
		adminMenu.getToEventMessages(user);
		AdminEventMessages eventMessages = new AdminEventMessages(driver);
		eventMessages.clickMessageQueue(MessageQueue.Contact_Message_Transport_PC);
		eventMessages.clickSafeOrderObjectLink(MessageQueuesSafeOrderObjectLinkOptions.Non_Safe_Ordered_Messages);
		ArrayList<String> nonSafeStuckMessages = eventMessages.getNonSafeOrderedMessagesPayload();
		for(String message : nonSafeStuckMessages) {
			if(message.contains(this.myPolicy.accountNumber)) {
				Assert.fail("A nonSafeOrdered Message should not appear for the issued policy on account "+myPolicy.accountNumber+".");
			}
		}
				
	}
	
	public void generatePolicy() throws Exception {
		        
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
		locationAddInsured.setNewContact(CreateNew.Create_New_Always);
		locationAddInsured.setCompanyOrPerson(ContactSubType.Person);
		locationAddInsured.setRelationshipToPrimaryNamedInsured(true);
		locationAddInsured.setRelationship(RelationshipsAB.ChildWard);
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
				.build(GeneratePolicyType.PolicyIssued);	
		
	}	
}
