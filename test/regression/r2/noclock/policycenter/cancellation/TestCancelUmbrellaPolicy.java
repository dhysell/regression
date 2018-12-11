package regression.r2.noclock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE4434 : Unable to cancel umbrella after Cancelled squire
 * @Description - 1. User should be able to flat cancel the umbrella policy after canceling the squire policy.
 * 2. User should be able to cancel Umbrella policy without errors
 * @DATE Dec 14, 2016
 */
@QuarantineClass
public class TestCancelUmbrellaPolicy extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy squirePolicyObj, squirePolicy;
	private Underwriters uw ;
	
	//City Squire with umbrella, effective one day in the future. Bind and issue both	
	@Test()
	public void generateUmbrellaPolicy() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1);		

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
		
		SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
		liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = property;
        mySquire.setEffectiveDate(newEff);

        squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Guy", "Citysqwumb")
				.withProductType(ProductLineType.Squire)
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squirePolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        squirePolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
	}

	//Flat Cancel the squire Policy 
	@Test(dependsOnMethods = { "generateUmbrellaPolicy" })
	public void testCancelSquirePolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicy.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1);

		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.CloseDidNotHappen,comment , currentDate, true);

	}

	//Flat Cancel the Umbrella Policy and validate  User should be able to flat cancel the umbrella policy after canceling the squire policy.
	@Test(dependsOnMethods = { "testCancelSquirePolicy" })
	public void testCancelUmbrella() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicy.squireUmbrellaInfo.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1);

		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.CloseDidNotHappen,comment , currentDate, true);

        validatePolicyStatus(squirePolicy, PolicyTermStatus.Canceled, PolicyTermStatus.Canceled);
	}
	
   //Scenario#2 - Create and issue Squire and Umbrella
	@Test ()
	public void generateSquireUmbrellaPolicy() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
		SectionIICoverages sectionIIcoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 1);
		liabilitySection.getSectionIICoverageList().add(sectionIIcoverage);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = property;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Guy", "Citysqwumb")
				.withProductType(ProductLineType.Squire)
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squirePolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
        squirePolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

	}

	//Cancel Umbrella Policy and validate user should be able to cancel policy without errors
	@Test(dependsOnMethods = { "generateSquireUmbrellaPolicy" })
	public void testCancelUmbrellaPolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.squireUmbrellaInfo.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment , currentDate, true);

        validatePolicyStatus(squirePolicyObj, PolicyTermStatus.InForce, PolicyTermStatus.Canceled);

	}	

	//Check Policy Status
    private void validatePolicyStatus(GeneratePolicy squirePol, PolicyTermStatus squstatus, PolicyTermStatus umbstatus) throws Exception	{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(squirePol.agentInfo.getAgentUserName(), squirePol.agentInfo.getAgentPassword(), squirePol.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        if (!summary.verifyPolicyStatusInPolicyCenter(squirePol.squire.getPolicyNumber(), squstatus, 100))
            Assert.fail("Policy in the account : " + squirePol.squire.getPolicyNumber() + " is not " + squstatus);
        if (!summary.verifyPolicyStatusInPolicyCenter(squirePol.squireUmbrellaInfo.getPolicyNumber(), umbstatus, 100))
            Assert.fail("Policy in the account : " + squirePol.squireUmbrellaInfo.getPolicyNumber() + " is not " + umbstatus);
	} 

}
