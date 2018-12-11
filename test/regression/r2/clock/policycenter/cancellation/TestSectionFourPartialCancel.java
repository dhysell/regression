package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationStatus;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.ClassIIICargoTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.CSRs;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.CSRsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author sbroderick
 * @Requirement This hot-feature allows UW to remove Sections I and II on a partial cancel so that IM remains without section I and II.
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20%20-%20line%20selection%20screen.xlsx">Story Card</a>
 * @Description This story will allow an UW to remove Sections I and II while Section IV remains on the policy.
 * CSR's, Agents, and Training should not be able to remove Sections I and II without removing section IV first.
 * @DATE Dec 15, 2017
 */
@QuarantineClass
public class TestSectionFourPartialCancel extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private String policyChangeDescription = "Remove Lienholder";
	private double delinquentAmount;

	//Used as a template: TestLHPartialCancelSquire partialCancel = new TestLHPartialCancelSquire();
	@Test
	public void testPartialCancelWithSectionIV() throws Exception {
        generatePolicy();
		moveClocks();
		runInvoiceDueAndCheckDelinquency();
		verifyPartialNonPayCancelInPolicyCenter();
		moveClocks2();
		removeLienholderCoveragesInPolicyCenter();		
	}
	

	public void generatePolicy() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Vin vin = VINHelper.getRandomVIN();
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location1Property1.setBuildingAdditionalInterest(loc1Property1AdditionalInterest);
		locOnePropertyList.add(location1Property1);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		AdditionalInterest loc2Property1AdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		loc2Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc2Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPolicyLocationProperty location2Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		location2Property1.setBuildingAdditionalInterest(loc2Property1AdditionalInterest);
		locTwoPropertyList.add(location2Property1);
		locationsList.add(new PolicyLocation(locTwoPropertyList, new AddressInfo(true)));
		
		SquireLiability liabilitySection = new SquireLiability();
		
		/*SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);*/
		
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.Cargo);
		imTypes.add(InlandMarine.FarmEquipment);

		//Cargo
		Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
		cargoTrailer1.setLimit("3123");
		ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
		cargoTrailers.add(cargoTrailer1);
		ArrayList<String> cargoAddInsured = new ArrayList<String>();
		cargoAddInsured.add("Cor Hofman");

		SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassIII, ClassIIICargoTypes.Milk, 100, 1000, "Brenda Swindle", false, cargoTrailers, cargoAddInsured);
		SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
		ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
		cargoList.add(cargo);
		cargoList.add(stuff);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);

		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);
		
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact();
		driversList.add(person);
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
		vehicleList.add(vehicle);
		
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);
		
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = liabilitySection;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.farmEquipment = allFarmEquip;
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Guy", "Inlandmarineedit")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Credit_Debit)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}


	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.busOwnLine.locationList.get(1).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber(), null, targetDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		}

        this.delinquentAmount = delinquency.getDelinquentAmount(OpenClosed.Open, null, myPolicyObj.squire.getPolicyNumber(), DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));

		new GuidewireHelpers(driver).logout();
	}
	

	public void verifyPartialNonPayCancelInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
		boolean partialNonPayCancelFound = summaryPage.verifyPendingCancellation(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 20), CancellationStatus.Cancelling, true, this.delinquentAmount, 120);
		if (!partialNonPayCancelFound) {
			Assert.fail("The policy did not get a Partial Nonpay Cancellation activity from BC after waiting for 2 minutes.");
		}
		new GuidewireHelpers(driver).logout();
	}


    public void moveClocks2() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
	}
	
	public void attemptRemoveLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		CSRs csr = CSRsHelper.getRandomCSR();
		new Login(driver).login(csr.getCsruserName(), csr.getCsrPassword());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);

        TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();

        SearchPoliciesPC searchPoliciesPage = new SearchPoliciesPC(driver);
        searchPoliciesPage.searchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());

		//verify that policy is cancelled in PC
		//gather charges that were sent to BC

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange(this.policyChangeDescription, DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.clickBuildingsLocationsRow(2);
        if (buildings.removeBuildingByBldgNumber(1)) {
			Assert.fail("CSR's should not be able to remove buildings on PolicyChange.");
		}
		
		new GuidewireHelpers(driver).logout();
		
	}

	public void removeLienholderCoveragesInPolicyCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Underwriters uw = UnderwritersHelper.getRandomUnderwriter();
		new Login(driver).login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Partial_Nonpay_Cancel_Documents);

        TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();

        SearchPoliciesPC searchPoliciesPage = new SearchPoliciesPC(driver);
        searchPoliciesPage.searchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());

		//verify that policy is cancelled in PC
		//gather charges that were sent to BC

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange(this.policyChangeDescription, DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();


        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.clickBuildingsLocationsRow(2);
        buildings.removeBuildingByBldgNumber(1);
		
		buildings.clickBuildingsLocationsRow(1);
        buildings.removeBuildingByBldgNumber(1);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLocations();

        GenericWorkorderLocations locations = new GenericWorkorderLocations(driver);
		locations.removeLocationByLocNumber(2);
		locations.removeLocationByLocNumber(1);

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();

        GenericWorkorderLineSelection selectLine = new GenericWorkorderLineSelection(driver);
		selectLine.checkPersonalPropertyLine(false);

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();

        SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
		policySearch.searchPolicyByAccountNumber(myPolicyObj.accountNumber);

       new GuidewireHelpers(driver).logout();
	}
}
