/**
 * @author bmartin Aug 19, 2015
 * @notes Notes from Work Product: DE2887
 * Previewing ACORD 28 after quote, binding, issuance, canceled, and rewrite.
 */
package regression.r2.noclock.policycenter.documents;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
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

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;

public class PreviewACORD28 extends BaseTest {

    GeneratePolicy myPolicyObj = null;

    private String agentUserName;
    private String agentPassword;
    private String accountNumber;
    private String uwUserName;
    private String uwPassword;
    private String policyNumber;

    private WebDriver driver;

    /**
     * @throws Exception description = "Runs Policy Through Full App"
     */
    @Test(enabled = true, description = "Runs Policy Through Full App")
    public void generateFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        final int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy")
                - NumberUtils.generateRandomNumberInt(0, 50);

        final PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
                false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail) {
            {
                this.setAdditionalCoverageStuff(myLineAddCov);
            }
        };

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

        final PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding() {
            {
                this.setYearBuilt(yearTest);
                this.setClassClassification("storage");
                this.setAdditionalInterestList(new ArrayList<AdditionalInterest>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add(new AdditionalInterest(ContactSubType.Company) {
                            {
                                this.setAdditionalInterestType(
                                        AdditionalInterestType.Mortgagee);
                                this.setLoanContractNumber("LN34567");
                            }
                        });
                    }
                });
            }
        };

        locationsList.add(new PolicyLocation() {
            {
                this.setAddress(new AddressInfo(true));
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {
                    private static final long serialVersionUID = 1L;

                    {
                        this.add(loc1Bldg1);
                    }
                });
            }
        });

        locOneBuildingList.add(loc1Bldg1);

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));


        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Prevew ACORD28")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        this.agentUserName = myPolicyObj.agentInfo.getAgentUserName();
        this.agentPassword = myPolicyObj.agentInfo.getAgentPassword();
        this.accountNumber = myPolicyObj.accountNumber;

        System.out.println("#############\nFull Policy Quoted");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Agent: " + agentUserName + "\n#############");
    }

    /**
     * @throws Exception
     * @note description=
     * "Checks for the existence of ACORD 28 in the Forms section after the generation of a full application"
     */
    @Test(enabled = true, dependsOnMethods = {
            "generateFullApp"}, description = "Checks for the existence of ACORD 28 in the Forms section after the generation of a full application")
    public void afterFullACORD28Check() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login(agentUserName, agentPassword);

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchAccountsPC searchAccounts = new SearchAccountsPC(driver);
        searchAccounts.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuForms();

        GenericWorkorderForms myForms = new GenericWorkorderForms(driver);
        myForms.verifyFormExists_Obsolete("ACORD 28");
    }

    /**
     * @throws Exception description="Processes policy through Policy Bound"
     */
    @Test(enabled = true, dependsOnMethods = {
            "generateFullApp"}, description = "Processes policy through Policy Bound")
    public void generatePolicyBound() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);

        this.policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();

        System.out.println("PolicyBound \n");
    }

    /**
     * @throws Exception description="Issues Policy"
     */
    @Test(enabled = true, dependsOnMethods = {"generatePolicyBound"}, description = "Issues Policy")
    public void generatePolicyIssued() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);

        this.uwUserName = myPolicyObj.underwriterInfo.getUnderwriterUserName();
        this.uwPassword = myPolicyObj.underwriterInfo.getUnderwriterPassword();
        this.policyNumber = myPolicyObj.busOwnLine.getPolicyNumber();

        System.out.println("#############\nFull Policy Issued");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Under Writer: " + uwUserName);
        System.out.println("Agent: " + agentUserName + "\n#############");
    }

    /**
     */
    @Test(enabled = true, dependsOnMethods = {"generatePolicyIssued"}, description = "Cancel Policy")
    public void generatePolicyCancel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login(uwUserName, uwPassword);

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchAccountsPC searchAccounts = new SearchAccountsPC(driver);
        searchAccounts.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickPolicyNumber(policyNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, "Cancel to be re-written later", null,
                true);

    }

    /**
     * description="Rewrite Full Term Policy"
     */
    @Test(enabled = true, dependsOnMethods = {
            "generatePolicyCancel"}, description = "Rewrite Full Term Policy")
    public void generatePolicyRewrite() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login(uwUserName, uwPassword);

        TopMenuPC topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        SearchAccountsPC searchAccounts = new SearchAccountsPC(driver);
        searchAccounts.searchAccountByAccountNumber(accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickPolicyNumber(policyNumber);

        ActionsPC myActions = new ActionsPC(driver);
        myActions.click_Actions();
        myActions.click_RewriteFullTerm();

        GenericWorkorderQuote myGenQuote = new GenericWorkorderQuote(driver);
        myGenQuote.clickQuote();
        myGenQuote.clickIssuePolicy();

        GenericWorkorder myGenWork = new GenericWorkorder(driver);
        myGenWork.selectOKOrCancelFromPopup(OkCancel.OK);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

    @Test(enabled = true, dependsOnMethods = {
            "generatePolicyRewrite"}, description = "Rewrite Full Term Policy")
    public void generateRewriteIssued() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(uwUserName, uwPassword, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Rewrite for policy");

        UWActivityPC myUWAct = new UWActivityPC(driver);
        myUWAct.clickComplete();

        System.out.println("#############\nFull Policy Issued");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Under Writer: " + uwUserName);
        System.out.println("Agent: " + agentUserName + "\n#############");
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description Notes from Work Product: DE2887 Previewing ACORD 28 after
     * quote, binding, issuance, canceled, and rewrite.
     * @DATE Oct 19, 2015
     */
    @Test(dependsOnMethods = {"generateRewriteIssued"}, enabled = true)
    public void afterRewriteACORD28Check() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(uwUserName, uwPassword, accountNumber);

        AccountSummaryPC myAccSum = new AccountSummaryPC(driver);
        myAccSum.clickPolicyNumber(policyNumber);

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuToolsDocuments();

        PolicyDocuments myPolDoc = new PolicyDocuments(driver);
        myPolDoc.setDocumentName("ACORD28");
        myPolDoc.selectRelatedTo("Rewrite Full Term");
        myPolDoc.clickSearch();
        myPolDoc.verifyDocumentPreviewButton();

    }

}
