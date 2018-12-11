package regression.r2.noclock.policycenter.change.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;

/**
 * @Author skandibanda
 * @Requirement : DE4222 FA Existing Additional Interest Behaving Wrong
 * @Description - Issuing a squire policy with addtional interest line holder and add a policy change, select the LH from add existing,
 * compare the LH address from submission and  Policy change
 * @DATE Nov 18, 2016
 */
public class TestPolicyChangeAddExistingAdditionalInterest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	@Test()
	public void testGenerateSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		
		ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg2AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Bldg2AddInterest.setFirstMortgage(true);
		loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "AdditionalInterest")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}

	@Test (dependsOnMethods = {"testGenerateSquirePol"})
	public void verifyPolicyChangeAdditional() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.squire.getPolicyNumber());
		
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);

        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetails = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();

		//Adding property 
		propertyDetails.clickAdd();
		propertyDetails.setPropertyType(PropertyTypePL.DwellingPremises);

		//Adding Existing Additional Interest
		propertyDetails.addExistingAdditionalInterest(myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName());
        String aiCompany = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName();
		String aiAddress1 = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getLine1();
		String aiCity = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getCity();
		String aiAddressListing = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getAddresslisting();		
		String aiState = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getState().toString();
		String aiZip = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getZip();
		String aiCounty = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAddress().getCounty();

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		String aiName = additionalInterests.getAdditionalInterestsName();
        String aiPolicyChangeAddress1 = additionalInterests.getBuildingsPropertyAdditionalInterestsLienHolderAddressLine1();
		String aiPolicyChangeZip = additionalInterests.getZip();
		String aiPolicyChangeState = additionalInterests.getState();
		String aiPolicyChangeCity = additionalInterests.getCity();
		String aiPolicyChangeCounty = additionalInterests.getCounty();        
		String aiPolicyChangeAddressListing = additionalInterests.getPropertyAdditionalInterestAddressListing();        

		//Comparing Submission Additional Interest address with Policy change Add Existing Additional Interest address
		String errormessage = "";
		if(!aiCompany.equals(aiName))
			errormessage = errormessage + "Policy Change AI Company" +  " " + aiName +  " not same as  Submission AI Company" + " " + aiCompany +"\n";

		if(!aiAddress1.equals(aiPolicyChangeAddress1))
			errormessage = errormessage + "Policy Change AI Address1" +  " " + aiPolicyChangeAddress1 +  " not same as  Submission AI Address1" + " " + aiAddress1 +"\n";

		if(!aiCity.equals(aiPolicyChangeCity))
			errormessage = errormessage + "Policy Change AI City" +  " " + aiPolicyChangeCity +  " not same as  Submission AI City" + " " + aiCity +"\n";

		if(!aiState.equals(aiPolicyChangeState))
			errormessage = errormessage + "Policy Change AI State" +  " " + aiPolicyChangeState +  " not same as  Submission AI State" + " " + aiState +"\n";

		if(!aiZip.equals(aiPolicyChangeZip))
			errormessage = errormessage + "Policy Change AI Zip" +  " " + aiPolicyChangeZip +  " not same as  Submission AI Zip" + " " + aiZip +"\n";

		if(!aiCounty.equals(aiPolicyChangeCounty))
			errormessage = errormessage + "Policy Change AI County" +  " " + aiPolicyChangeCounty +  " not same as  Submission AI County" + " " + aiCounty +"\n";

		if(!aiAddressListing.equals(aiPolicyChangeAddressListing))
			errormessage = errormessage + "Policy Change AI AddressListing" +  " " + aiPolicyChangeAddressListing +  " not same as  Submission AI AddressListing" + " " + aiAddressListing +"\n";

		if(errormessage != "")
			Assert.fail(errormessage);
	}
}
