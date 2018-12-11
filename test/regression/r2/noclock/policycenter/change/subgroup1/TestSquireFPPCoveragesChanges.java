package regression.r2.noclock.policycenter.change.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;

/**
* @Author nvadlamudi
* @Requirement : DE5377: Policy change quote screen missing coverages
* @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/110345415396"></a>
* @Description 
* @DATE Jun 2, 2017
*/
public class TestSquireFPPCoveragesChanges extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	
	@Test ()
	public void testIssueSquirePolWithSectionOneAndTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);	

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors, FPPFarmPersonalPropertySubTypes.CirclePivots);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withCreateNew(CreateNew.Create_New_Always)				
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Guy", "POLChange")					
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);			
	} 
	
	@Test(dependsOnMethods = {"testIssueSquirePolWithSectionOneAndTwo"})
	private void testPolicyChangeWithAdditionalInterests() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(driver);
		fppCovs.checkCoverageD(false);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
        sideMenu.clickSideMenuQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickCostChangeDetails();
        if (!quote.checkCostChangeDetailsAdditionalSectionOneCoveragesTab()) {
			Assert.fail("Expected Additional Section 1 coverages tab is not displayed. ");
		}
		quote.clickCostChangeDetailsAdditionalSectionOneCoveragesTab();

        for(int i=0; i< this.myPolicyObj.squire.propertyAndLiability.squireFPP.getItems().size();i++){
			String fppCoverageName = this.myPolicyObj.squire.propertyAndLiability.squireFPP.getItems().get(i).getType().getParentType().getValue();
			if(!quote.checkCostChangeDetailsAdditionalSectionOneCoveragesByType(fppCoverageName)){
				Assert.fail("Expected FPP coverage type : "+fppCoverageName + " is not displayed." );
			}
		}		
	}
}
