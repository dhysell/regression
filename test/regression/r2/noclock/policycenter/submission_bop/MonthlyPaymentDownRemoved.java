package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPayment;

/**
 * @author bmartin Aug 5, 2015
 * @notes This test will check to make sure the line "(At Least One Required)" is removed
 * from the payment info section with a monthly payment plan selected within
 * Policy Center.
 */
public class MonthlyPaymentDownRemoved extends BaseTest {

    private String agentUserName;
    private String agentPassword;
    private String accountNumber;
    private WebDriver driver;

    @Test()
    public void testBasicBindInsuredOnly() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        AddressInfo addIntTest = new AddressInfo();

		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
        loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc1Bld1AddInterest.setAddress(addIntTest);

        locOneBuildingList.add(loc1Bldg1);

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        GeneratePolicy myPolicyObj = new GeneratePolicy
                .Builder(driver)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Test Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        this.agentUserName = myPolicyObj.agentInfo.getAgentUserName();
        this.agentPassword = myPolicyObj.agentInfo.getAgentPassword();
        this.accountNumber = myPolicyObj.accountNumber;

        Login myloginPage = new Login(driver);
        myloginPage.login(agentUserName, agentPassword);

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchAccountsPC searchAccounts = new SearchAccountsPC(driver);
        searchAccounts.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuPayment();

        GenericWorkorderPayment myPaymentPage = new GenericWorkorderPayment(driver);
        myPaymentPage.clickPaymentPlan(PaymentPlanType.Monthly);
        myPaymentPage.verifyMonthlyRequiredPaymentText();

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + agentUserName + "\n#############");

    }
}
