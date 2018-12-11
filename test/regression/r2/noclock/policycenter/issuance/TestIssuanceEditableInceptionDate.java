package regression.r2.noclock.policycenter.issuance;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US13033: Make section inception dates uneditable in the issuance job
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Inception%20Date.xlsx">PC8 � Squire � QuoteApplication � Inception Date</a>
 * @Description : validating the Issuance policy level inception date is editable and remaining section level is blank, not editable
 * @DATE Nov 9, 2017
 */
@QuarantineClass
public class TestIssuanceEditableInceptionDate extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy SquirePolicyObjPL;
	private GeneratePolicy initSquirePolObjPL;

	@Test()
	public void testInitialBoundSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

		// Farm Equipment
		IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
				"Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
		FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -8);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        initSquirePolObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Inception", "Initial")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testInitialBoundSquirePol" })
	public void testBoundSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

		// Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.SpecialForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip);

		ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
		imTypes.add(InlandMarine.FarmEquipment);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        SquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withAgent(initSquirePolObjPL.agentInfo)
                .withInsFirstLastName("Inception", "Editable")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
	}

	@Test(dependsOnMethods = { "testBoundSquirePol" })
	private void testCheckInceptionDateSectionEditable() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				SquirePolicyObjPL.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickCloseWorkSheet();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		String errorMessage = "";
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.setTransferedFromAnotherPolicy(true);

		// Entering valid policy number
		polInfo.setCheckBoxInInceptionDateByRow(1, true);
        polInfo.setInceptionDatePolicyNumberDirectly(InceptionDateSections.Policy, initSquirePolObjPL.squire.getPolicyNumber());


        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Policy).equals(this.initSquirePolObjPL.squire.getEffectiveDate())) {
			errorMessage = errorMessage + "Policy Inception Date is not displayed as Initial policy effective Date \n";
        }
        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Property).equals(this.initSquirePolObjPL.squire.getEffectiveDate()) && !polInfo.getInceptionPolicyNumberBySelection(InceptionDateSections.Property).trim().isEmpty()) {
			errorMessage = errorMessage + "Property Inception date & policy Number is not blank in Issuance job \n";
        }
        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Liability).equals(this.initSquirePolObjPL.squire.getEffectiveDate()) && !polInfo.getInceptionPolicyNumberBySelection(InceptionDateSections.Liability).trim().isEmpty()) {
			errorMessage = errorMessage + "Liability Inception date & policy Number is not blank in Issuance job \n";
        }
        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.Auto).equals(this.initSquirePolObjPL.squire.getEffectiveDate()) && !polInfo.getInceptionPolicyNumberBySelection(InceptionDateSections.Auto).trim().isEmpty()) {
			errorMessage = errorMessage + "Auto Inception date & policy Number is not blank in Issuance job \n";
		}
        if (polInfo.getInceptionDateFromTableBySelection(InceptionDateSections.InlandMarine).equals(this.initSquirePolObjPL.squire.getEffectiveDate()) && !polInfo.getInceptionPolicyNumberBySelection(InceptionDateSections.InlandMarine).trim().isEmpty()) {
			errorMessage = errorMessage + "IM Inception date & policy Number is not blank in Issuance job \n";
		}

        if (polInfo.checkCheckBoxBeforeInceptionDate(InceptionDateSections.Property)) {
			errorMessage = errorMessage + "Property Inception date checkbox displayed in Issuance job \n";
		}
		if(polInfo.checkCheckBoxBeforeInceptionDate(InceptionDateSections.Liability)){
			errorMessage = errorMessage + "Liability Inception date checkbox displayed in Issuance job \n";
		}
		if(polInfo.checkCheckBoxBeforeInceptionDate(InceptionDateSections.Auto)){
			errorMessage = errorMessage + "Auto Inception date checkbox displayed in Issuance job \n";
		}

        if (polInfo.checkCheckBoxBeforeInceptionDate(InceptionDateSections.InlandMarine)) {
			errorMessage = errorMessage + "IM Inception date checkbox displayed in Issuance job \n";
		}
		if(errorMessage !=""){
            Assert.fail(errorMessage);

        }
	}


}
