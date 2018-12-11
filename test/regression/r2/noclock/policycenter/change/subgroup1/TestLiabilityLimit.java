package regression.r2.noclock.policycenter.change.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.Property.SectionIIMedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;

/**
 * @Author skandibanda
 * @Requirement : DE4359 :PL - Section II liability limit not defaulting on
 * section III liability limit
 * @DATE Jan 23, 2017
 */
public class TestLiabilityLimit extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	@Test()
	public void createPLPropertyAutoPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locToAdd.setPlNumResidence(12);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_100_300_100);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = property;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "PropClass")
                .build(GeneratePolicyType.QuickQuote);
	}

	@Test(dependsOnMethods = "createPLPropertyAutoPolicy")
	public void validateSquireAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        GenericWorkorderSquireAutoCoverages_Coverages paCoverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages section2Covs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

		section2Covs.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);
		section2Covs.setMedicalLimit(SectionIIMedicalLimit.Limit_10000);

		String section2LiabilityLimit = section2Covs.getGeneralLiabilityLimit();
		String section2MedicalLimit = section2Covs.getMedicalLimit();

		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPACoverages();
		boolean testFailed = false;
		String errorMessage = "";

		if (!section2LiabilityLimit.equals(paCoverages.getLiabilityLimit())) {
			testFailed = true;
			errorMessage = errorMessage + "Expected General Liability Limit : " + section2LiabilityLimit
					+ " is not displayed.";

		}

		if (!section2MedicalLimit.equals(paCoverages.getMedicalLimit())) {
			testFailed = true;
			errorMessage = errorMessage + "Expected Medical Limit : " + section2MedicalLimit + " is not displayed.";
		}

		if (testFailed)
			Assert.fail(errorMessage);
	}
}
