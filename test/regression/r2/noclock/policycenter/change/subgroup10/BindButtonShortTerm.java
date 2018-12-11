package regression.r2.noclock.policycenter.change.subgroup10;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.globaldatarepo.entities.Agents;
public class BindButtonShortTerm extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private String accountNumber;
	@SuppressWarnings("unused")
	private String policyNumber;
	private String agentUserName;
	private String agentPassword;
	// set values for change premium, change date,
	public double additionalPremium = 0;
	public Date changeDate = null;

	@Test
	public void generate() throws Exception {

		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		building.setClassClassification("");
		building.setClassCode("63611");
		ArrayList<AdditionalInterest> additionalInterestList = new ArrayList<AdditionalInterest>();
		additionalInterestList.add(new AdditionalInterest(ContactSubType.Company));
		building.setAdditionalInterestList(additionalInterestList);
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
		.withInsPersonOrCompany(ContactSubType.Company)
		.withInsCompanyName("TestPolicyChanges")
		.withPolOrgType(OrganizationType.Partnership)
		.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Motels))
		.withPolicyLocations(locationsList)
		.withPaymentPlanType(PaymentPlanType.Quarterly)
		.withDownPaymentType(PaymentType.Cash)
		.build(GeneratePolicyType.FullApp);

		accountNumber = myPolicyObj.accountNumber;
        policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();
		Agents agent = myPolicyObj.agentInfo;
		agentUserName = agent.getAgentUserName();
		agentPassword = agent.getAgentPassword();
	}

	/**
	 * DE2262
	 * 
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "generate" }, enabled = true)
	public void testBindButton() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// change expiration to less than 3 months
		new Login(driver).loginAndSearchJob(agentUserName, agentPassword, accountNumber);
		// edit policy transaction & hit OK
		new GuidewireHelpers(driver).clickEdit();
		new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		// change term type to other
		Date systemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.setTermLength(90, systemDate);

		// quote
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		// pick monthly payment plan and enter down payment
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		paymentPage.clickPaymentPlan(PaymentPlanType.Monthly);
		double downPaymentAmount = paymentPage.getDownPaymentAmount();
		paymentPage.clickAddDownPayment();
        paymentPage.setDownPaymentAmount(downPaymentAmount);
        paymentPage.setDownPaymentType(PaymentType.Cash);
        paymentPage.clickOk();

		WebElement button_GenericWorkorderBindOptionsArrow = new GuidewireHelpers(driver).find(By.xpath("//span[contains(@id, ':BindOptions-btnWrap')]"));
		String temp = button_GenericWorkorderBindOptionsArrow.getAttribute("unselectable");
		Assert.assertEquals(temp, "on", "The Bind button is enabled when it shouldn't be.");
	}

}
