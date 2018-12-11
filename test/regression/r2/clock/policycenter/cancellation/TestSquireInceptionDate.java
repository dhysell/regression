package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.PolicyLineType;
import persistence.enums.PolicyType;
import persistence.enums.SqlSigns;
import persistence.enums.TransactionType;
import persistence.guidewire.entities.ExistingPolicyLookUp;
import persistence.guidewire.helpers.ExistingPoliciesHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US9185: COMMON - Updates to Inception Date
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Inception%20Date.xlsx">Link Text</a>
 * @Description
 * @DATE Mar 31, 2017
 */
@QuarantineClass
public class TestSquireInceptionDate extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySquirePolicy, cancelPol, expiryPol;
	private String cancelPolicyNumber, expiryPolicyNumber;

	@Test()
	private void testIssueSquireAutoAndCancel() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        cancelPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withInsFirstLastName("SQ", "cancel")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(cancelPol.underwriterInfo.getUnderwriterUserName(),
                cancelPol.underwriterInfo.getUnderwriterPassword(), cancelPol.squire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

	}

	@Test(dependsOnMethods = { "testIssueSquireAutoAndCancel" })
	private void testIssueSquireAndExpired() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -90);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        expiryPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withPolEffectiveDate(newEff)
                .withPolTermLengthDays(90)
                .withInsFirstLastName("SQ", "cancel")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquireAndExpired" })
    private void moveClockMoreThan6Months() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 200);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
	}

	@Test(dependsOnMethods = { "moveClockMoreThan6Months" })
	private void testCreateSquireFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

		// Inland Marine
		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.RecreationalEquipment);

		// Rec Equip
		ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
		recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.AllTerrainVehicle, "500",
				PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.recEquipment_PL_IM = recVehicle;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myProperty;
        mySquire.inlandMarine = myInlandMarine;
        mySquire.squirePA = squirePersonalAuto;

        mySquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Test", "InceDate")
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testCreateSquireFA" })
	private void testValidateInceptionDate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		boolean testFailed = false;
		String errorMessage = "";
		new Login(driver).loginAndSearchSubmission(mySquirePolicy.agentInfo.getAgentUserName(),
				mySquirePolicy.agentInfo.getAgentPassword(), mySquirePolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);
		// Enter a cancelled (6months older) policy and validate the Inception
		// date

		Date newCancelDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -200);
		ArrayList<ExistingPolicyLookUp> policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PersonalAutoLinePL, TransactionType.Cancellation, newCancelDate, SqlSigns.LessThan);

		if (policies.size() > 0) {
			cancelPolicyNumber = policies.get(0).getPolicyNumber();
		} else {
            cancelPolicyNumber = this.cancelPol.squire.getPolicyNumber();
		}

		if (!polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, cancelPolicyNumber)) {
			testFailed = true;
			errorMessage = errorMessage + "Select button does not exists for cancelled - more than 6 months policy: \n";
		}

        if (!polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(this.cancelPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Policy - Inception Date for 6 months old cancelled transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, cancelPolicyNumber);

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(this.cancelPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Section III - Auto  - Inception Date for 6 months old cancelled transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		// Enter a expired (6months older) policy and validate the Inception
		// date

		Date newExpireDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -200);

		policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(),
				PolicyType.Squire, PolicyLineType.PersonalAutoLinePL, TransactionType.Cancellation, newExpireDate, SqlSigns.LessThan);

		if (policies.size() > 0) {
			expiryPolicyNumber = policies.get(0).getPolicyNumber();
		} else {

            this.expiryPolicyNumber = this.expiryPol.squire.getPolicyNumber();
		}

		if (!polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, expiryPolicyNumber)) {
			testFailed = true;
			errorMessage = errorMessage + "Select button does not exists for expired - more than 6 months policy: \n";
		}

        if (!polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(this.expiryPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Policy - Inception Date for 6 months old expired transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, expiryPolicyNumber);

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(this.expiryPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Section III - Auto  - Inception Date for 6 months old expired transfered policy is not correct! \n";
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = { "testValidateInceptionDate" })
    private void moveClockAgainMoreThan6Months() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 200);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
	}

	@Test(dependsOnMethods = { "moveClockAgainMoreThan6Months" })
	private void testValidateInceptionDateAfter396() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		boolean testFailed = false;
		String errorMessage = "";
		new Login(driver).loginAndSearchSubmission(mySquirePolicy.agentInfo.getAgentUserName(),
				mySquirePolicy.agentInfo.getAgentPassword(), mySquirePolicy.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		//Cancellation policy after 1 year
		if (!polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.cancelPolicyNumber)) {
			testFailed = true;
			errorMessage = errorMessage + "Select button does not exists for cancelled - more than 6 months policy: \n";
		}

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(this.cancelPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Policy - Inception Date for 6 months old cancelled transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, cancelPolicyNumber);

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(this.cancelPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Section III - Auto  - Inception Date for 6 months old cancelled transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);
		//Expired Policy 
		if (!polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.expiryPolicyNumber)) {
			testFailed = true;
			errorMessage = errorMessage + "Select button does not exists for expired - more than 6 months policy: \n";
		}

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(this.expiryPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Policy - Inception Date for 1 year old expired transfered policy is not correct! \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);
        new BasePage(driver).sendArbitraryKeys(Keys.TAB);

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, expiryPolicyNumber);

        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(this.expiryPol.squire.getEffectiveDate())) {
			testFailed = true;
			errorMessage = errorMessage
					+ "Section III - Auto  - Inception Date for 1 year old expired transfered policy is not correct! \n";
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}
}
