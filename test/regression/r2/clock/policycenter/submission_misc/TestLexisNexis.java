package regression.r2.clock.policycenter.submission_misc;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillClueADDSubjectType;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillReport;
import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.ResultVehicleType;
import repository.driverConfiguration.Config;
import repository.gw.enums.CommuteType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquirePropertyLiabilityExclusionsConditions;
import repository.gw.generate.custom.UIAutoClaimReported;
import repository.gw.generate.custom.UIPersonalPrefillPartyReported;
import repository.gw.generate.custom.UIPersonalPrefillVehicleReported;
import repository.gw.generate.custom.UIPropertyClaimReported;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
@QuarantineClass
public class TestLexisNexis extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy cbrPolicy = null;
	private GeneratePolicy prefillPolicy = null;
	private GeneratePolicy clueAutoPolicy = null;
	private GeneratePolicy cluePropertyPolicy = null;

	private GeneratePolicy generateSquirePropertyPolicy(GeneratePolicyType quoteType, boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();

		SquirePropertyLiabilityExclusionsConditions myPropLiabExcCond = new SquirePropertyLiabilityExclusionsConditions();

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = myLiab;
        myProperty.propLiabExclusionsConditions = myPropLiabExcCond;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myProperty;

        GeneratePolicy polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)		
				.withLexisNexisData(person, prefillPersonal, prefillCommercial, insuranceScore, mvr, clueAuto, clueProperty)
				.build(quoteType);

		return polToReturn;
	}

	private GeneratePolicy generateSquireAutoOnlyPolicy(GeneratePolicyType quoteType, boolean person, boolean prefillPersonal, boolean prefillCommercial, boolean insuranceScore, boolean mvr, boolean clueAuto, boolean clueProperty) throws Exception {

		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle v1 = new Vehicle();
		v1.setVehicleTypePL(VehicleTypePL.LocalService1Ton);
		v1.setVin("make new vin");

		v1.setEmergencyRoadside(true);
		v1.setAdditionalLivingExpense(true);
		v1.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
		vehicleList.add(v1);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy polToReturn = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withLexisNexisData(person, prefillPersonal, prefillCommercial, insuranceScore, mvr, clueAuto, clueProperty)
				.build(quoteType);

		return polToReturn;
	}

	@Test(enabled = true)
	public void testGenerate_CLUEProperty() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		this.cluePropertyPolicy = generateSquirePropertyPolicy(GeneratePolicyType.FullApp, true, false, false, false, false, false, true);
		new Login(driver).loginAndSearchSubmission(this.cluePropertyPolicy.agentInfo.getAgentUserName(), this.cluePropertyPolicy.agentInfo.getAgentPassword(), this.cluePropertyPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCLUE();

		ArrayList<UIPropertyClaimReported> toReturn = this.cluePropertyPolicy.cluePropertyReport.getClaimsReported();
		boolean testFailed = false;
		String errorMessage = "";
		
		if(toReturn.size() > 0) {
			getQALogger().info("Prefill Broker Parties Reported Count: " + toReturn.size());

			for(int i = 0; i < toReturn.size(); i++) {
				boolean valueFound = false;
				String claimDate = toReturn.get(i).getClaimDate();
				String policyHolderName = toReturn.get(i).getPolicyHolderName();
				String policyNum = toReturn.get(i).getPropertyPolicyNumber();
				String paymentAmount = toReturn.get(i).getPayoutsReserves().get(0).getPaymentAmount();
				for(UIPropertyClaimReported currentProp : this.cluePropertyPolicy.cluePropertyReport.getClaimsReported()) {
					if(currentProp.getClaimDate().contains(claimDate) && currentProp.getPolicyHolderName().contains(policyHolderName) && currentProp.getPropertyPolicyNumber().contains(policyNum) && currentProp.getPayoutsReserves().get(0).getPaymentAmount().contains(paymentAmount)) {
						valueFound = true;
						break;
					} else {
						continue;
					}
				}
				if(!valueFound) {
					testFailed = true;
					errorMessage = errorMessage + "\n ERROR: Expected Broker Personal Property Report " + claimDate + "  " + policyNum + " is not displayed on the UI";
				}

			}
		}
		if (testFailed){
			Assert.fail(errorMessage);
		}
	}
	@Test(enabled = true)
	public void testGenerate_CLUEAuto() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		this.clueAutoPolicy = generateSquireAutoOnlyPolicy(GeneratePolicyType.FullApp, true, false, false, false, false, true, false);	

		new Login(driver).loginAndSearchSubmission(this.clueAutoPolicy.agentInfo.getAgentUserName(), this.clueAutoPolicy.agentInfo.getAgentPassword(), this.clueAutoPolicy.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuClueAuto();
		ArrayList<UIAutoClaimReported> toReturn = this.clueAutoPolicy.clueAutoReport.getClaimsReported();
		boolean testFailed = false;
		String errorMessage = "";
		if(toReturn.size() > 0) {
			getQALogger().info("Prefill Broker Parties Reported Count: " + toReturn.size());

			for(int i = 0; i < toReturn.size(); i++) {
				boolean valueFound = false;
				String claimDate = toReturn.get(i).getClaimDate();
				String policyHolderName = toReturn.get(i).getPolicyHolderName();
				String paymentAmount = toReturn.get(i).getPayoutsReserves().get(0).getPaymentAmount();
				for(UIAutoClaimReported currentProp : this.clueAutoPolicy.clueAutoReport.getClaimsReported()) {
					if (currentProp.getClaimDate().contains(claimDate) && currentProp.getPolicyHolderName().contains(policyHolderName) && currentProp.getPayoutsReserves().get(0).getPaymentAmount().contains(paymentAmount)) {
						valueFound = true;
						break;
					} else {
						continue;
					}
				}

				if(!valueFound) {
					testFailed = true;
					errorMessage = errorMessage + "\n ERROR: Expected Broker Personal Auto Report " + claimDate + "  " + policyHolderName + " is not displayed on the UI";
				}

			}
		}
		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(enabled = true)
	public void testGenerate_CBR() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.cbrPolicy = generateSquireAutoOnlyPolicy(GeneratePolicyType.FullApp, true, false, false, true, false, false, false);

	}

	@Test(enabled = true, dependsOnMethods = { "testGenerate_CBR" })
	public void testCBR_ValidateQuotePremiumDiscounts() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(this.cbrPolicy.agentInfo.getAgentUserName(), this.cbrPolicy.agentInfo.getAgentPassword(), this.cbrPolicy.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPLInsuranceScore();
        //Note:Credit Insurance Score is no longer available in the quote Screen
		GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
		if (this.cbrPolicy.squire.insuranceScoreReport.getUiInsuranceScore() == insScore.getInsuranceScore()) {
			getQALogger().info("Expected Credit Score : " + this.cbrPolicy.squire.insuranceScoreReport.getUiInsuranceScore());
		} else {
			Assert.fail("Unexpected Credit Score : " + insScore.getInsuranceScore());
		}

	}

	@Test(enabled = true)
	public void testGenerate_Prefill() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		this.prefillPolicy = generateSquireAutoOnlyPolicy(GeneratePolicyType.QuickQuote, true, true, false, false, false, false, false);
	}

	@Test(enabled = true, dependsOnMethods = "testGenerate_Prefill")
	public void testPrefill_AddDriversVehicles_PageValidations_PrefillBrokerValidations() throws Exception {
		boolean testFailed = false;
		boolean valueFound = false;
		String errorMessage = "";
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(this.prefillPolicy.agentInfo.getAgentUserName(), this.prefillPolicy.agentInfo.getAgentPassword(), this.prefillPolicy.accountNumber);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		DataprefillReport brokerReport = this.prefillPolicy.prefillReport.getBrokerReport();

		if (brokerReport.getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery() != null) {
			getQALogger().info("Prefill Broker Parties Reported Count: " + brokerReport.getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().size());

			for (int i = 0; i < brokerReport.getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().size(); i++) {
				valueFound = false;
				DataprefillClueADDSubjectType person = brokerReport.getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().get(i);
				String firstName = person.getName().getFirst();
				String lastName = person.getName().getLast();
				getQALogger().info("Prefill UI Parties Reported Count: " + this.prefillPolicy.prefillReport.getPartiesReported().size());
				for (UIPersonalPrefillPartyReported currentDisplay : this.prefillPolicy.prefillReport.getPartiesReported()) {
					if (currentDisplay.getConsumerName().contains(firstName) && currentDisplay.getConsumerName().contains(lastName)) {
						valueFound = true;
						break;
					} else {
						continue;
					}
				}
				if (!valueFound) {
					testFailed = true;
					errorMessage = errorMessage + "\n ERROR: Expected Broker Prefill Party Reported: " + firstName + "  " + lastName + " is not displayed on the UI";
				}
			}
		} else {
			getQALogger().info("No additional contacts in the broker object to add");
		}

		// vehicle Comparison
		boolean vehFound = false;
		try {
			for (int i = 0; i < brokerReport.getReport().getDataprefillVin().getDiscoveredVehicles().getVehicle().size(); i++) {
				vehFound = false;
				ResultVehicleType vehicle = brokerReport.getReport().getDataprefillVin().getDiscoveredVehicles().getVehicle().get(i);
				String vMake = vehicle.getMake();
				String vModel = vehicle.getModel();
				String vVin = vehicle.getVin();
				for (UIPersonalPrefillVehicleReported currentDisplayedVehicle : this.prefillPolicy.prefillReport.getVehiclesReported()) {
					if (currentDisplayedVehicle.getMake().equals(vMake) && currentDisplayedVehicle.getModel().equals(vModel) && currentDisplayedVehicle.getVin().equals(vVin)) {
						vehFound = true;
						break;
					} else {
						continue;
					}
				}

				if (!vehFound) {
					testFailed = true;
					errorMessage = errorMessage + "\n ERROR: Expected Broker Prefill Vehicle : " + vMake + "  " + vModel + " is not displayed on the UI";
				}
			}
		} catch (Exception e) {
			getQALogger().info("There is a error in the vehicle object");
		}


		// selecting existing additional insured
        String nameToPick = "";
		ArrayList<UIPersonalPrefillPartyReported> parties = this.prefillPolicy.prefillReport.getPartiesReported();
		for (int i = 0; i < parties.size(); i++) {
			String nameFound = parties.get(i).getConsumerName();
			if (!nameFound.equals(this.prefillPolicy.pniContact.getFirstName() + " " + this.prefillPolicy.pniContact.getLastName())) {
				nameToPick = nameFound;
			}
		}

		System.out.println(nameToPick);

		if (!nameToPick.equals("")) {
			polInfo.selectAddExistingPrefillAdditionalInsured(nameToPick);
			//polInfo.selectAddExistingOtherContactAdditionalInsured(nameToPick);
            GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
			ani.selectAdditionalInsuredRelationship(RelationshipToInsured.Friend);
            ani.selectAddtionalInsuredAddress(this.prefillPolicy.pniContact.getAddress().getLine1());
			// householdMember.selectAddtionalInsuredAddress(addressTemp);
            ani.clickUpdate();

			if (ani.isExistsReturnToEditAdditionalInsured()) {
				ani.clickSelectMatchingContact();
				ani.setReasonForContactChange("Because from prefill");
				ani.clickUpdate();
			}
		}
		polInfo.selectMembershipDuesCounty(nameToPick, "Ada");
		// Section III Auto - vehicles prefill
        sideMenu = new SideMenuPC(driver);
		if (!nameToPick.equals("")) {
			sideMenu.clickSideMenuPADrivers();

			// Adding the same policy member to driver list
            GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
			paDrivers.addExistingDriver(nameToPick);
            paDrivers.selectMaritalStatus(MaritalStatus.Married);
            paDrivers.selectGender(Gender.Female);
			paDrivers.setOccupation("Software");
			paDrivers.setLicenseNumber("AB123456C");
			State state = State.Idaho;
			paDrivers.selectLicenseState(state);
			paDrivers.selectCommuteType(CommuteType.WorkFromHome);
            //			paDrivers.clickSelfReportingTab();
            paDrivers.clickOk();
		}

		// adding new vehicle
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles vehicleDetails = new GenericWorkorderVehicles(driver);
		int fromPrefillVeh = vehicleDetails.addFromPrefillVehicles();
		vehicleDetails.clickNext();
		ErrorHandling getBanner = new ErrorHandling(driver);
		int numErrorHandlingMsgs = getBanner.text_ErrorHandlingValidationResults().size();
		if (numErrorHandlingMsgs < 2) {
			if (getBanner.getValidationMessages().size() < 2) {
				testFailed = true;
				errorMessage = errorMessage + "\n ERROR: When adding from prefill, the user must modify the Commuting Miles and Assign a driver and the page level validation did not come up";
			}
		}

        int numVehAdded = vehicleDetails.getNumVehiclesAdded();
		int totalVehiclesMinusManuallyAddedOne = numVehAdded - 1;

		if (totalVehiclesMinusManuallyAddedOne != fromPrefillVeh) {
			testFailed = true;
			errorMessage = errorMessage + "\n ERROR: Num vehicles in Prefill does not match num vehicles added from prefill";
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

	// move clock for 34 days and order prefill report
//	@Test(enabled = true, dependsOnMethods = "testPrefill_AddDriversVehicles_PageValidations_PrefillBrokerValidations")
	public void testPrefill_clockMoveValidations() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 34));
		Date newSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		if (newSystemDate.after(currentSystemDate)) {
			new Login(driver).loginAndSearchSubmission(this.prefillPolicy.agentInfo.getAgentUserName(), this.prefillPolicy.agentInfo.getAgentPassword(), this.prefillPolicy.accountNumber);

            SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPolicyInfo();

			GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
			polInfo.orderPrefill();
			polInfo.clickReturnToOrderPrefillReport();

		} else {
			Assert.fail("ERROR: Clock move has not happened and cannot reorder prefill");
		}

	}	
}
