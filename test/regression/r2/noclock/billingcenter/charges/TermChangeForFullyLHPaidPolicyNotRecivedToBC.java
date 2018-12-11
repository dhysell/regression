package regression.r2.noclock.billingcenter.charges;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sgunda
 * @Description (DE6628) Term change on fully LH paid policy throws null pointer exception and Didn't receive term extension policy change
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/158116415016"></a>
 * @DATE Oct 09, 2017
 */
public class TermChangeForFullyLHPaidPolicyNotRecivedToBC extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ARUsers arUser = new ARUsers();
	private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
	private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
	private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
	private String lienholderNumber = null;
	private double costChg;
	private String descriptionForPolicyChange= "To test DE6628";
	
	@Test
	public void createFullyLHPaidPol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		int yearTest = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setYearBuilt(yearTest);
		loc1Bldg1.setClassClassification("storage");
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		AddressInfo addIntTest = new AddressInfo();
		addIntTest.setLine1("PO Box 711");
		addIntTest.setCity("Pocatello");
		addIntTest.setState(State.Idaho);
		addIntTest.setZip("83204");
		
		
		AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
		loc1Bld2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1Bld2AddInterest.setAddress(addIntTest);
		loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
				
		this.locOneBuildingList.add(loc1Bldg2);
		this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Term Extension")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
				
	}
	
	@Test(dependsOnMethods = {"createFullyLHPaidPol"})
	  private void changePolicyExpirationDate() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.busOwnLine.getPolicyNumber());
        Date changeDate = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -80);

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickExpirationDateChange();
		policyChangePage.setDescription(descriptionForPolicyChange);
		policyChangePage.clickPolicyChangeNext();
		policyChangePage.setExpirationDate(changeDate);

	  	policyChangePage.clickPolicyChangeNext();
		GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
			GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
			sideMenu.clickSideMenuRiskAnalysis();
			riskAnalysis.approveAll_IncludingSpecial();
			riskAnalysis.Quote();
		}

		policyChangePage.clickIssuePolicy();


        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();
		PolicySummary pcSum = new PolicySummary(driver);
		costChg=pcSum.getTransactionPremium(null, descriptionForPolicyChange);
		System.out.println("The cost change after policy change is  :"+costChg);
		 
	 }
	 
	 @Test(dependsOnMethods = {"changePolicyExpirationDate"})
	  private void verifyChargesRecivedToBC() throws Exception  {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.accountNumber);

         BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		 AccountCharges acctCharges = new AccountCharges(driver);
		Assert.assertTrue(acctCharges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPayer(240, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), TransactionType.Policy_Change, this.lienholderNumber), "Didn't receive term extension policy change after waiting for four minutes");
		
	 }
	 
	

}
