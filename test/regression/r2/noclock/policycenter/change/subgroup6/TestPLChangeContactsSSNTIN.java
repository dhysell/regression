package regression.r2.noclock.policycenter.change.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.PolicyChangeReview;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
* @Author nvadlamudi
* @Requirement : US11575: COMMON - Mask/hide SSN & TIN on Policy Change Review screen
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20Change%20-%20Initiate%20Policy%20Change.xlsx">PC8 - Common - Change - Initiate policy Change</a>
* @Description : Validate Change Review for SSN and TIN changes
* @DATE Oct 16, 2017
*/
public class TestPLChangeContactsSSNTIN extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;

	@Test()
	public void testIssueSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumResidencePremise));

		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

		// ANI
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Last"+StringsUtils.generateRandomNumberDigits(5),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(false));
		ani.setHasMembershipDues(true);
		ani.setNewContact(CreateNew.Create_New_Always);
		listOfANIs.add(ani);

        myPolicyObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL).withInsFirstLastName("Change", "LineReview").withANIList(listOfANIs)
				.withSocialSecurityNumber(StringsUtils.generateRandomNumberDigits(9)).withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePol" })
	public void editPolicyChangesSectionOneandTwo() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
                myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
	
		//PNI changes
		polInfo.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		String pniSSN = StringsUtils.generateRandomNumberDigits(9);
		editANIPage.setEditAdditionalNamedInsuredSSN(pniSSN);
		editANIPage.clickEditAdditionalNamedInsuredUpdate();
        //ANI Changes
		polInfo.clickANIContact(myPolicyObj.aniList.get(0).getPersonFirstName());
        String aniTIN = StringsUtils.generateRandomNumberDigits(9);
		editANIPage.setEditAdditionalNamedInsuredTIN(aniTIN);
		editANIPage.clickEditAdditionalNamedInsuredUpdate();
		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
		sideMenu.clickSideMenuPolicyChangeReview();
        PolicyChangeReview policyChangeReview = new PolicyChangeReview(driver);
		policyChangeReview.expandAllTreeNodes();
		String changeSSN = "###-##-"+pniSSN.substring(5, pniSSN.length());
		String changeTaxID = "##-###"+aniTIN.substring(5, aniTIN.length());
		
		String errorMessage = "";
		if(!policyChangeReview.getPolicyChangeValue("SSN").contains(changeSSN)){
			errorMessage = "Expected SSN : "+ policyChangeReview.getPolicyChangeValue("SSN")+ " is not formatted as " +changeSSN;
		}

		if(!policyChangeReview.getPolicyChangeValue("Tax ID").contains(changeTaxID)){
			errorMessage = "Expected Tax ID : "+ policyChangeReview.getPolicyChangeValue("Tax ID")+ " is not formatted as " +changeTaxID;
		}
		
		if(errorMessage != ""){
			Assert.fail(errorMessage);
		}

	}

}
