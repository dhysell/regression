package regression.r2.noclock.policycenter.cancellation;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.PendingCancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that AR personnel have the permissions required to create a pending cancel on a bound, but not yet issued policy.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/152941626880/tasks">Rally Defect DE6497</a>
* @Description 
* @DATE Sep 19, 2017
*/
public class TestCancellationOnBoundPolicyByAR extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPLPolicyObj = null;
	public ARUsers plARUser = null;
	
	public GeneratePolicy myBOPPolicyObj = null;
	public ARUsers bopARUser = null;

	@Test
    public void generatePLPolicy() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = property;
		

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPLPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Non-Pay6", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
	}
	
	@Test(dependsOnMethods = { "generatePLPolicy" })
	public void attemptToCancelPLPolicyAsARUser() throws Exception {
		this.plARUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical_Advanced, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.plARUser.getUserName(), this.plARUser.getPassword(), this.myPLPolicyObj.squire.getPolicyNumber());
		
		try {
            StartCancellation cancellation = new StartCancellation(driver);
			cancellation.pendingCancelPolicy(PendingCancellationSourceReasonExplanation.NoPaymentReceived, "No payment for AR", null,	true, 100.00);
		} catch (Exception e) {
			Assert.fail("Unable to cancel a bound policy as an AR user. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test
    public void generateBOPPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		
		int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

		PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
		PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
		myline.setAdditionalCoverageStuff(myLineAddCov);

		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(randomYear);
		loc1Bldg1.setClassClassification("storage");

		locOneBuildingList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        this.myBOPPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Policy Cancellation")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(myline)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
	}
	
	@Test(dependsOnMethods = { "generateBOPPolicy" })
	public void attemptToCancelBOPPolicyAsARUser() throws Exception {
		this.bopARUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.bopARUser.getUserName(), this.bopARUser.getPassword(), this.myBOPPolicyObj.busOwnLine.getPolicyNumber());
		
		try {
            StartCancellation cancellation = new StartCancellation(driver);
			cancellation.pendingCancelPolicy(PendingCancellationSourceReasonExplanation.NoPaymentReceived, "No payment for AR", null,	true, 100.00);
		} catch (Exception e) {
			Assert.fail("Unable to cancel a bound policy as an AR user. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
