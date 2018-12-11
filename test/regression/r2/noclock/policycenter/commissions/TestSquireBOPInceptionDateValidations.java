package regression.r2.noclock.policycenter.commissions;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
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
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.PolicyLineType;
import persistence.enums.PolicyType;
import persistence.enums.SqlSigns;
import persistence.enums.TransactionType;
import persistence.guidewire.entities.ExistingPolicyLookUp;
import persistence.guidewire.helpers.ExistingPoliciesHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US9185: Update to Inception Date
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/33274298124d/detail/userstory/62605159081">
 * Link Text</a>
 * @Description
 * @DATE Mar 30, 2017
 */
@QuarantineClass
public class TestSquireBOPInceptionDateValidations extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySquirePolicy, myPolicyObjBOP;
	private GeneratePolicy newValidPol;
	private GeneratePolicy validBOPPol;	
	
	@Test
	private void testIssueBackDatedSquirePol() throws Exception {
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
                PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand is a GOOBER"));


		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -10);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        newValidPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withInsFirstLastName("Test", "Initial")
                .withPolEffectiveDate(newEff)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = {"testIssueBackDatedSquirePol"})
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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

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

		Date valExpectedInceptionDate;
		String polNumber;
		// Enter a valid policy with section 1 and validate the Inception date
		ArrayList<ExistingPolicyLookUp> policies = ExistingPoliciesHelper.getRandomPolicyByType(
				cf.getEnv().toUpperCase(), PolicyType.Squire,
				PolicyLineType.PropertyAndLiabilityLinePL, TransactionType.Submission, new Date(), SqlSigns.LessThan);
		
		if (policies.size() > 0) {
			valExpectedInceptionDate = policies.get(0).getperiodStart();
			polNumber = policies.get(0).getPolicyNumber();
		} else {
            valExpectedInceptionDate = this.newValidPol.squire.getEffectiveDate();
            polNumber = this.newValidPol.squire.getPolicyNumber();
		}

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, polNumber);

		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "Expected Policy Inception Date: " + valExpectedInceptionDate
					+ "is not displayed.  \n";
		}

		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property)
				.equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Section I Inception Date: " + valExpectedInceptionDate
					+ "is displayed.  \n";
		}

		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability)
				.equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Section II Inception Date: " + valExpectedInceptionDate
					+ "is displayed.  \n";
		}

		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Section III Inception Date: " + valExpectedInceptionDate
					+ "is displayed.  \n";
		}

		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine)
				.equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Section IV Inception Date: " + valExpectedInceptionDate
					+ "is displayed.  \n";
		}

		polInfo.setTransferedFromAnotherPolicy(false);
		polInfo.setTransferedFromAnotherPolicy(true);

		// Enter a valid policy with section 2 and validate the Inception date
		policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(),
				PolicyType.StandardFL, PolicyLineType.StandardLiabilityPL, TransactionType.Submission, new Date(),
				SqlSigns.LessThan);

		if (policies.size() > 0) {
			valExpectedInceptionDate = policies.get(0).getperiodStart();
			polNumber = policies.get(0).getPolicyNumber();
		}

		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Liability, polNumber);
		
		if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability)
				.equals(valExpectedInceptionDate)) {
			testFailed = true;
			errorMessage = errorMessage + "UnExpected Section II Inception Date: " + valExpectedInceptionDate
					+ "is displayed.  \n";
		}
		
		// Enter a valid policy with section 3 and validate the Inception date
		policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(),
				PolicyType.Squire, PolicyLineType.PersonalAutoLinePL, TransactionType.Issuance, new Date(),
				SqlSigns.LessThan);
		ExistingPolicyLookUp newInfo;
		if (policies.size() > 0) {
			newInfo = policies.get(0);
			polInfo.setTransferedFromAnotherPolicy(false);
			polInfo.setTransferedFromAnotherPolicy(true);
			polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, newInfo.getPolicyNumber());
			Date val3PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
			Date val3Section1InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto);
			Date val3ExpectedPolInceptionDate = newInfo.getperiodStart();
			Date val3ExpectedSection1InceptionDate = newInfo.getperiodStart();

			if (val3PolInceptionDate.equals(val3ExpectedPolInceptionDate) && (val3Section1InceptionDate.equals(val3ExpectedSection1InceptionDate))) {
				getQALogger().info("Expected Policy Inception Date : " + val3PolInceptionDate
						+ " and Expected Section 3 Inception Date : " + val3Section1InceptionDate + " are displayed.");
			} else {
				testFailed = true;
				errorMessage = errorMessage + "Expected Policy Inception Date: " + val3ExpectedPolInceptionDate
						+ " Expected Section 3 Inception Date : " + val3ExpectedSection1InceptionDate
						+ " are not displayed.  \n";
			}

		}

		// Enter a valid policy with section 4 and validate the Inception date
		policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(),
				PolicyType.StandardIM, PolicyLineType.StandardInlandMarine, TransactionType.Issuance, new Date(),
				SqlSigns.LessThan);
		if (policies.size() > 0) {
			newInfo = policies.get(0);
			polInfo.setTransferedFromAnotherPolicy(false);
			polInfo.setTransferedFromAnotherPolicy(true);
			polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, newInfo.getPolicyNumber());
			Date val4PolInceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy);
			Date val4Section4InceptionDate = polInfo
					.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine);

			Date val4ExpectedPolInceptionDate = newInfo.getperiodStart();
			Date val4ExpectedSection4InceptionDate = newInfo.getperiodStart();

			if (val4PolInceptionDate.equals(val4ExpectedPolInceptionDate) && (val4Section4InceptionDate.equals(val4ExpectedSection4InceptionDate))) {
				getQALogger().info("Expected Policy Inception Date : " + val4PolInceptionDate
						+ " and Expected Section 4 Inception Date : " + val4Section4InceptionDate + " are displayed.");
			} else {
				testFailed = true;
				errorMessage = errorMessage + "Expected Policy Inception Date: " + val4ExpectedPolInceptionDate
						+ " Expected Section 4 Inception Date : " + val4ExpectedSection4InceptionDate
						+ " are not displayed.  \n";
			}
		}

		// Enter a specific section Inception date and validate the Inception
		// date
		policies = ExistingPoliciesHelper.getRandomPolicyByType(cf.getEnv().toUpperCase(),
				PolicyType.Squire, PolicyLineType.PersonalAutoLinePL, TransactionType.Issuance, new Date(),
				SqlSigns.LessThan);
		if (policies.size() > 0) {
			newInfo = policies.get(0);
			polInfo.setTransferedFromAnotherPolicy(false);
			polInfo.setTransferedFromAnotherPolicy(true);
			polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Auto, newInfo.getPolicyNumber());
			Date section3InceptionDate = polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto);
			Date expectedSection3InceptionDate = newInfo.getperiodStart();

			if (section3InceptionDate.equals(expectedSection3InceptionDate)) {
				getQALogger().info("Expected Section 3 Inception Date : " + section3InceptionDate + " are displayed.");
			} else {
				testFailed = true;
				errorMessage = errorMessage + " Expected Section 3 Inception Date : " + expectedSection3InceptionDate
						+ " are not displayed.  \n";
			}
		}

		if (testFailed) {
			Assert.fail(errorMessage);
		}

	}
	
	@Test()
	private void testIssueBOPPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -10);

        validBOPPol = new GeneratePolicy.Builder(driver)
                .withInsCompanyName("Test BOP")
                .withBusinessownersLine()
				.withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = {"testIssueBOPPol"})
	private void testCreateBOPFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObjBOP = new GeneratePolicy.Builder(driver)
                .withInsCompanyName("Test BOP")
                .withBusinessownersLine()
				.build(GeneratePolicyType.FullApp);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		boolean testFailed = false;
		String errorMessage = "";
		new Login(driver).loginAndSearchSubmission(myPolicyObjBOP.agentInfo.getAgentUserName(),myPolicyObjBOP.agentInfo.getAgentPassword(), myPolicyObjBOP.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		polInfo.setTransferedFromAnotherPolicy(true);
		polInfo.setPolicyOutOfForceMoreThan6MonthsRadio(false);
        String polNumber;
		// entering a valid policy number
		ArrayList<ExistingPolicyLookUp> policies = ExistingPoliciesHelper.getRandomPolicyByType(
				cf.getEnv().toUpperCase(), PolicyType.Businessowners,
				PolicyLineType.Businessowners, TransactionType.Issuance, new Date(), SqlSigns.LessThan);
		
		if (policies.size() > 0) {
			polNumber = policies.get(0).getPolicyNumber();
		}else{
            polNumber = this.validBOPPol.busOwnLine.getPolicyNumber();
		}
		
		if(!polInfo.setPreviousPolicyNumberBOP(polNumber)){
			testFailed = true;
			errorMessage = "BOP Transfered from another valid policy is not selected";
		}
		

		if (testFailed) {
			Assert.fail(errorMessage);
		}
	}

}
