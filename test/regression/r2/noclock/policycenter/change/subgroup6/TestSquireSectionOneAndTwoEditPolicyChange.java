package regression.r2.noclock.policycenter.change.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.ElectricalSystem;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.Garage;
import repository.gw.enums.Property.KitchenBathClass;
import repository.gw.enums.Property.NumberOfStories;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.Plumbing;
import repository.gw.enums.Property.PrimaryHeating;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.RoofType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.Property.SprinklerSystemType;
import repository.gw.enums.Property.Wiring;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;
import com.idfbins.enums.OkCancel;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartNameChange;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author skandibanda
 * @Requirement :US7554: [Part III] PL - Edit Policy Change - Section I & II
 * @RequirementsLink <a href="http:// "rally1.rallydev.com/#/33274298124d/detail/userstory/54446076756</a>
 * @Description : Issue Squire Section I&II and add policy change and validate UW issues
 * @DATE June 24, 2016
 */
@QuarantineClass
public class TestSquireSectionOneAndTwoEditPolicyChange extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private Agents agent;

	@Test ()
	public void testGenerateSquireAutoIssuance() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);			

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty prop1 = new PLPolicyLocationProperty();
		prop1.setpropertyType(PropertyTypePL.ResidencePremises);
		prop1.setConstructionType(ConstructionTypePL.Frame);
		prop1.setOwner(true);
		
		PLPolicyLocationProperty prop2 = new PLPolicyLocationProperty();
		prop2.setpropertyType(PropertyTypePL.CondominiumVacationHome);
		prop2.setDwellingVacant(true);
		prop2.setNumberOfUnits(NumberOfUnits.FourUnits);
		prop2.setPropertyCoverages(propertyCoverages);
		prop2.setOwner(true);
		
		PLPolicyLocationProperty prop3 = new PLPolicyLocationProperty();
		prop3.setpropertyType(PropertyTypePL.CondominiumResidencePremise);
		prop3.setDwellingVacant(true);
		prop3.setNumberOfUnits(NumberOfUnits.FourUnits);
		prop3.setPropertyCoverages(propertyCoverages);
		prop3.setOwner(true);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.CirclePivots);
		
		locOnePropertyList.add(prop1);
		locOnePropertyList.add(prop2);
		locOnePropertyList.add(prop3);
		
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(12);
		locToAdd.setPlNumResidence(12);
		locationsList.add(locToAdd);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withAgent(agent)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Guy", "Auto")					
				.withPolicyLocations(locationsList)
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	} 

	@Test (dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void verifyEditPolicyChangesSquireAuto() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

		//removing Property&Liability Line selection
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles Vehicles = new GenericWorkorderVehicles(driver);
		Vehicles.removeAllVehicles();
		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.setCheckBoxInDriverTableByDriverName("Guy Auto");
		paDrivers.clickRemoveButton();

        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		sideMenu.clickSideMenuLineSelection();
		lineSelection.checkPersonalAutoLine(false);

		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		//add ANI
		PolicyInfoAdditionalNamedInsured pANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp",
				AdditionalNamedInsuredType.Friend, new AddressInfo(true));
		pANI.setNewContact(CreateNew.Create_New_Always);			
		policyInfo.addAdditionalNamedInsured(myPolicyObj.basicSearch, pANI);

		PolicyInfoAdditionalNamedInsured pANI2 = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test"+StringsUtils.generateRandomNumberDigits(8), "Comp2",
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
		pANI2.setNewContact(CreateNew.Create_New_Always);			
		policyInfo.addAdditionalNamedInsured(myPolicyObj.basicSearch, pANI2);

		//Remove ANI
		String  ContactName = pANI.getPersonFirstName()+" "+pANI.getPersonLastName();		
		policyInfo.removeANI(ContactName);		

		new GuidewireHelpers(driver).clickNext();

		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		
		int row = hmember.getPolicyHouseholdMembersTableRow("Test");
		hmember.clickPolicyHouseHoldTableCell(row, "Name");
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		householdMember.setDateOfBirth("01/01/1980");
		householdMember.selectNotNewAddressListingIfNotExist(pANI.getAddress());
		householdMember.clickOK();

		//Remove PolicyMember and ANI		
		hmember.clickRemoveMember(pANI2.getPersonLastName());
		sideMenu.clickSideMenuPolicyInfo();
		policyInfo.removeANI(pANI2.getPersonFirstName()+" "+pANI2.getPersonLastName());

		//Locations 
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(12);
		propertyLocations.clickOK();

		quote.clickSaveDraftButton();

		//Property
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

		//Adding property with Owner name
		propertyDetails.clickAdd();
		propertyDetails.setPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetails.AddExistingOwner();
        propertyDetails.setDwellingVacantRadio(false);
		propertyDetails.setUnits(NumberOfUnits.OneUnit);
		propertyDetails.setWoodFireplaceRadio(false);
		propertyDetails.setSwimmingPoolRadio(false);
		propertyDetails.setWaterLeakageRadio(false);
		propertyDetails.setExoticPetsRadio(false);
		propertyDetails.clickPropertyConstructionTab();
		enterConstructionProptectionPageDetails();
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 4);
		coverages.setCoverageALimit(200200);
		coverages.selectCoverageCCoverageType(CoverageType.BroadForm);
		coverages.selectCoverageCCoverageType(CoverageType.SpecialForm);		
		quote.clickSaveDraftButton();

		sideMenu.clickSideMenuPropertyLocations();

		//Removing property
		sideMenu.clickSideMenuSquirePropertyDetail();			
		propertyDetails.setCheckBoxByRowInPropertiesTable(1, true);
		propertyDetails.clickRemoveProperty();

		//Edit property		
		propertyDetails.clickViewOrEditBuildingButton(3);
		propertyDetails.setUnits(NumberOfUnits.ThreeUnits);

		//Add Insured
		propertyDetails.addAddtionalInsured();
		propertyDetails.setAddtionalInsuredName("SteveJobs");		
		propertyDetails.clickOk();		
		quote.clickSaveDraftButton();

		//Coverages		
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);

		//Changing Section1Deductible
		coverage.selectSectionIDeductible(SectionIDeductible.FiveThousand);

		//Changing Property Coverage	
		coverage.setCoverageALimit(200200);		

		//FPP Coverage Reduced

		GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
        fppCovs.clickFarmPersonalProperty();
        fppCovs.selectCoverageType(FPPCoverageTypes.BlanketExclude);
		fppCovs.selectDeductible(FPPDeductible.Ded_1000);	

		//Changing SectionIIDeductible
		fppCovs.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
		section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
		section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_2000);					

		//Access Yes added		
		GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions exc = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
		exc.clickCoveragesExclusionsAndConditions();
		exc.checkAccessYesEndorsement209(true);	
		quote.clickSaveDraftButton();

		quote.clickQuote();

		verifyUWIssuesBlockBindIssuance();

	}

	public void verifyUWIssuesBlockBindIssuance() {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		try{
			riskAnalysis.clickReturnRiskAnalysisPage();
		}catch(Exception e){}

		//validate UW block bind and block issuance
		boolean testFailed = false;
		String errorMessage = "";

		String[] expectedUWMessages = { "ANI change to policy","Section I deductible increase",
				"Homeowners Coverage Changed","New property added","Property detail change",
				"Existing property removed","Access Yes endorsement added.",
				"Property Additional insured change","Removing Section","Section II coverage decrease"};

		boolean  messageFound = false;

		for (String uwIssue : expectedUWMessages) {
			messageFound = false;
			for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
				String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
				if ((!currentUWIssueText.contains("Blocking Issuance"))) {

					if (currentUWIssueText.contains(uwIssue)) {
						messageFound = true;
						break;
					}
				}
			}
			if (!messageFound) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);

	}

	private void enterConstructionProptectionPageDetails() throws GuidewireNavigationException {
		//Enter construction details
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setYearBuilt(1993);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setBathClass(KitchenBathClass.Basic);
		constructionPage.setSquareFootage(1700);
		constructionPage.setStories(NumberOfStories.One);
		constructionPage.setBasementFinished("90");
		constructionPage.setGarage(Garage.AttachedGarage);
		constructionPage.setLargeShed(false);
		constructionPage.setCoveredPorches(false);
		constructionPage.setFoundationType(FoundationType.FullBasement);
		constructionPage.setRoofType(RoofType.WoodShingles);
		constructionPage.setPrimaryHeating(PrimaryHeating.Gas);
		constructionPage.setPlumbing(Plumbing.Copper);
		constructionPage.setWiring(Wiring.Copper);
		constructionPage.setElectricalSystem(ElectricalSystem.CircuitBreaker);
		constructionPage.setAmps(100);
		constructionPage.setKitchenClass(KitchenBathClass.Basic);
		constructionPage.clickProtectionDetailsTab();

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.setSprinklerSystemType(SprinklerSystemType.Full);
		protectionPage.setFireExtinguishers(true);
		protectionPage.setSmokeAlarm(true);
		protectionPage.setNonSmoker(true);
		protectionPage.setDeadBoltLocks(true);
		protectionPage.setDefensibleSpace(true);
		protectionPage.clickOK();
	}	

	@Test (dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void verifyEditPolicyChangesSquire() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


        //add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
//        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		startExpiryDateChange("First policy Change", changeDate);
        // Less than 18 months
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorder expireDate = new GenericWorkorder(driver);
		Date expiryDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 15);	
		expireDate.setExpirationDate(expiryDate);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();
		
		sideMenu.clickSideMenuRiskAnalysis();

		//validate UW block bind and block issuance
		boolean testFailed = false;
		String errorMessage = "";
		
        //Note : UW validation messages will not display when policy term type is other
		
		/*String[] expectedUWMessages = { "Squire Term Type is Other",
		"Expiry Date Change"};

		boolean  messageFound = false;

		for (String uwIssue : expectedUWMessages) {
			messageFound = false;
			for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
				String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
				if ((!currentUWIssueText.contains("Blocking Issuance")) && (!currentUWIssueText.contains("Blocking Quote"))){

					if (currentUWIssueText.contains(uwIssue)) {
						messageFound = true;
						break;
					}
				}
			}
			if (!messageFound) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Quote : " + uwIssue + " is not displayed.";
			}
		}
		if (testFailed)
			Assert.fail(errorMessage);
*/		
		sideMenu.clickSideMenuPolicyInfo();
		new GuidewireHelpers(driver).editPolicyTransaction();
		Date expiryDateMorethan18 = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Month, 20);
		expireDate.setExpirationDate(expiryDateMorethan18);			
		quote.clickQuote();  

		// More than 18 months 
        ErrorHandling getBanner = new ErrorHandling(driver);
		int numErrorHandlingMsgs = getBanner.text_ErrorHandlingValidationResults().size();		
		if (numErrorHandlingMsgs < 2) {			
			if (getBanner.getValidationMessages().size() < 2) {
				testFailed = true;
				errorMessage = errorMessage + "Expiration date must be less than 18 months message displayed";
			}
		}
		if (testFailed) {
			Assert.fail(errorMessage);
		}  

	}
	
	
	
	public void startExpiryDateChange(String description, Date date) {
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickExpirationDateChange();
        if (date == null) {
            date = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        }
        policyChangePage.setEffectiveDate(date);
        policyChangePage.setDescription(description);
        if (date != null) {
        	policyChangePage.setEffectiveDate(date);
        } else {
        	policyChangePage.setEffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        }
        policyChangePage.clickDescription();
        long endTime = new Date().getTime() + 10000;
        do {
        	policyChangePage.clickPolicyChangeNext();
            try {
            	policyChangePage.selectOKOrCancelFromPopup(OkCancel.OK);
            } catch (Exception e) {

            }
        } while (policyChangePage.finds(By.xpath("//span[contains(@id, 'PolicyChangeWizard_PolicyInfoScreen:ttlBar')]")).size() <= 0 && new Date().getTime() < endTime);

    }
	
	
	
	
	
	
	

	@Test (dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void changingPNIName() throws Exception {
		
		String contactFirstName = "Ayaan";
		
		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.squire.getPolicyNumber());

		//start policy  Name change
		StartNameChange nameChange = new StartNameChange(driver);
        nameChange.sendActivityToContactManager(myPolicyObj, GenerateContactType.Person, "Ayaan", myPolicyObj.pniContact.getLastName(), "Spelling Mistake");
        
        driver.quit();
        cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");
        ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
        detailsPage.getToContactPage(user, myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName(), myPolicyObj.pniContact.getAddress().getLine1(), State.Idaho);
        detailsPage.updateContactName(contactFirstName, myPolicyObj.pniContact.getLastName());
        
		boolean testFailed = false;
		String errorMessage = "";
		String chngedName= nameChange.verifyRowExistsRecentNotesTable();

		if(!chngedName.contains("Ayaan")){
			testFailed = true;
			errorMessage = "Name not changed";
		}
		if(testFailed){
			Assert.fail(errorMessage);
		}

	}

}