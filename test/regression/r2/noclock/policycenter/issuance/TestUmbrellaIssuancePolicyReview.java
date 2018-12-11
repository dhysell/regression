package regression.r2.noclock.policycenter.issuance;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.PolicyChangeReview;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : Story Card PC8 - Umbrella - Policy Review
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Umbrella/PC8%20-%20Umbrella%20-%20Policy%20Review.xlsx">PL PC Umbrella</a>
 * @Description Creates a policy to Bound then has the Underwriter check for the existence of the Policy Review screen on Issuance.
 * @DATE Oct 3, 2016
 */
@QuarantineClass
public class TestUmbrellaIssuancePolicyReview extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySqPolicy;
	private Underwriters uw;

	@Test()
	public void testBoundUmbrellaPol() throws Exception {
	 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	 driver = buildDriver(cf);
	  
	ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
	locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
	PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
	locToAdd.setPlNumAcres(11);
	locationsList.add(locToAdd);
	
	SquireLiability myLiab = new SquireLiability();
	myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
	
	SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK,true,
		UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
	SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
	squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        mySqPolicy = new GeneratePolicy.Builder(driver)
            .withSquire(mySquire)
			.withProductType(ProductLineType.Squire)
            .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
			.withInsFirstLastName("SUSAN", "REDWINE")
			.withPaymentPlanType(PaymentPlanType.Annual)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
	squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        mySqPolicy.squireUmbrellaInfo = squireUmbrellaInfo;
        mySqPolicy.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicySubmitted);
    }
		
  @Test(dependsOnMethods = {"testBoundUmbrellaPol"})
  public void testUmbrellaIssuancePolicyReview() throws Exception {
	 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	 driver = buildDriver(cf);
	 uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

      new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolicy.squireUmbrellaInfo.getPolicyNumber());
      PolicyMenu policyMenu = new PolicyMenu(driver);
	  policyMenu.clickMenuActions();
      policyMenu.clickIssuePolicy();
      SideMenuPC sideMenu = new SideMenuPC(driver);
	  
	  sideMenu.clickSideMenuSquireUmbrellaCoverages();
      GenericWorkorderSquireUmbrellaCoverages umbCovs = new GenericWorkorderSquireUmbrellaCoverages(driver);
		umbCovs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);
	  
	   sideMenu.clickSideMenuPolicyReview();

	  PolicyChangeReview policyChangeReview = new PolicyChangeReview(driver);
		if(policyChangeReview.PolicyReviewRowCount() < 1){
			Assert.fail("Policy Review page is not displayed.");
		}
  }
  
  public GeneratePolicy getMyPersonalUmbrellaPolicy(){
      return this.mySqPolicy;
  }
  
}
