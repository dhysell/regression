package regression.r2.noclock.policycenter.change.subgroup10;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderDeliveryOption;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import persistence.globaldatarepo.entities.Underwriters;
public class DeliveryOptions extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String accountNumber;
	private String uwUserName;
	private String uwPassword;
	// set values for change premium, change date,
	public Float additionalPremium = null;
	public Date changeDate = null;

	@Test
	public void generate() throws Exception {

		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		building.setClassClassification("");
		building.setClassCode("63611");
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		// customizing the BO Line, including additional insureds
		AddressInfo address = new AddressInfo();
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.Motels);
		ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
		additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company,
				"dafigudhfiuhdafg", AdditionalInsuredRole.CertificateHolderOnly, address));
		// boLine.setAdditonalInsuredBOLineList(additonalInsuredBOLineList);

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("TestPolicyChanges")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

		accountNumber = myPolicyObj.accountNumber;
		Underwriters underwriter = myPolicyObj.underwriterInfo;
		uwUserName = underwriter.getUnderwriterUserName();
		uwPassword = underwriter.getUnderwriterPassword();
	}

	/**
	 * DE2295 The defect is that when Delivery Options are added to a policy
	 * then bound, you can try to change the Delivery Options without a Change
	 * Policy job. This will test if the link to change the option has been
	 * removed, and therefore not editable.
	 * @throws Exception 
	 *
     */
	@Test(dependsOnMethods = { "generate" }, enabled = true)
    public void testDeliveryOptions() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Testing Delivery Options", null);

        GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		policyInfoPage.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);

		DeliveryOptionType optionType = DeliveryOptionType.Attention;

        GenericWorkorderDeliveryOption deliveryOptions = new GenericWorkorderDeliveryOption(driver);
		deliveryOptions.addDeliveryOption(optionType);
        policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
		policyInfoContactPage.clickUpdate();

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.quoteAndIssue();

		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);

        PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPrimaryNamedInsured();

        policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
		Assert.assertFalse(policyInfoContactPage.isOptionClickable(optionType),
				"The Delivery Option, after issuance, should not be editable.\nIf it is, please contact your system administrator to update the codebase.");

	}

}
