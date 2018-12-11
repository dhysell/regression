package regression.r2.noclock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RenewalCode;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US4784 [Part III] PL - Quote Cancellation
 * @Description : Issuing Squire with I, II, III &IV and Standard Fire policies, create cancel quote and validate the premium amounts
 * under Cost Change details tab
 * @DATE Oct 06,2016
 */
@QuarantineClass
public class TestPLQuoteCancellation extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj, stdFireLiab_Fire_PolicyObj,stdFireRenewalPolicyObj;
	private GeneratePolicy stdIMPolicyObj;
	private Underwriters uw;

	@Test()
	public void testStdFire_GenerateRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -285);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
		locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
		propertyLocation.setPlNumAcres(100);
		propertyLocation.setPlNumResidence(25);
		locationsList.add(propertyLocation);

        stdFireRenewalPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withInsFirstLastName("StdFire","Renewal")
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
//				.withPolTermLengthDays(80)	
				.withPolEffectiveDate(newEff)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

	}

	//Forms Inference Adverse Action Letter form check for Renewal job
	@Test(dependsOnMethods = { "testStdFire_GenerateRenewal" })
	public void testStdFire_RenewalJob() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// Login with UW
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireRenewalPolicyObj.standardFire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
		renewal.quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, stdFireRenewalPolicyObj);
	} 		
}
