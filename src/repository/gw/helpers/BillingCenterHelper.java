package repository.gw.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class BillingCenterHelper extends BasePage {

    private WebDriver driver;

    public BillingCenterHelper(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //////////////////////////////////////
    ////////// Helper Methods ////////////
    //////////////////////////////////////

    private ARUsers getARManger() throws Exception{
        return ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
    }

    public void login(String userName, String password){
        new Login(driver).login(userName , password);
    }

    public String getPolicyNumberFromGeneratePolicyObject(GeneratePolicy generatePolicy){
        String policyNumber = "";
        switch (generatePolicy.productType) {
            case Businessowners:
                policyNumber = generatePolicy.busOwnLine.getPolicyNumber();
                break;
            case Squire:
                policyNumber = generatePolicy.squire.getPolicyNumber();
                break;
            case PersonalUmbrella:
                policyNumber = generatePolicy.squireUmbrellaInfo.getPolicyNumber();
                break;
            case StandardFire:
                policyNumber = generatePolicy.standardFire.getPolicyNumber();
                break;
            case StandardIM:
                policyNumber = generatePolicy.standardInlandMarine.getPolicyNumber();
                break;
            case StandardLiability:
                policyNumber = generatePolicy.standardLiability.getPolicyNumber();
                break;
            case Membership:
                policyNumber = generatePolicy.membership.getPolicyNumber();
                break;
            case CPP:
                policyNumber = generatePolicy.commercialPackage.getPolicyNumber();
                break;
        }
        return policyNumber;
    }

    public void clickOnRenewalPolicyPeriod(String policyNumber){
        new BCAccountSummary(driver).clickPolicyNumberInOpenPolicyStatusTable(policyNumber.concat("-2"));
    }

    public boolean waitUntilChargesFromPolicyContextArriveByContext(repository.gw.enums.TransactionType transactionType){
        new BCAccountMenu(driver).clickBCMenuCharges();
        return new AccountCharges(driver).waitUntilChargesFromPolicyContextArrive( 240 , transactionType);
    }

    public void waitUntilIssuanceChargesArrive(){
        Assert.assertTrue(waitUntilChargesFromPolicyContextArriveByContext(repository.gw.enums.TransactionType.Policy_Issuance),"Policy_Issuance charges not found, test can't continue");

    }

    public void waitUntilRenewalChargesArrive(){
        Assert.assertTrue(waitUntilChargesFromPolicyContextArriveByContext(repository.gw.enums.TransactionType.Renewal),"Renewal charges not found, test can't continue");
    }

    public void waitUntilCancellationChargesArrive(){
        Assert.assertTrue(waitUntilChargesFromPolicyContextArriveByContext(TransactionType.Cancellation),"Cancellation charges not found, test can't continue");

    }

    public boolean waitForIssuanceTroubleTicketsToArrive(){
        new BCPolicyMenu(driver).clickBCMenuTroubleTickets();
        return new BCCommonTroubleTicket(driver).waitForTroubleTicketsToArrive(240);
    }

    public boolean waitUntilBindPaymentsArrive(){
        new BCAccountMenu(driver).clickAccountMenuPaymentsPayments();
        return new AccountPayments(driver).waitUntilBindPaymentsArrive(240);
    }

    public void verifyIssuancePolicyPeriod_TroubleTicketAndBindPayment(GeneratePolicy generatePolicy){
        try {
            boolean trueOrFalse;
            new BCSearchPolicies(driver).searchPolicyByPolicyNumber(getPolicyNumberFromGeneratePolicyObject(generatePolicy));
            if (!(generatePolicy.downPaymentType == repository.gw.enums.PaymentType.ACH_EFT || generatePolicy.downPaymentType == PaymentType.Credit_Debit)) {
                trueOrFalse = waitForIssuanceTroubleTicketsToArrive();
            } else {
                new InfoBar(driver).clickInfoBarAccountNumber();
                trueOrFalse = waitUntilBindPaymentsArrive();
            }
            Assert.assertTrue(trueOrFalse , "Failed to verify the IssuanceTroubleTicket or Payment made on Bind. ");
            new InfoBar(driver).clickInfoBarAccountNumber();
        } catch (Exception e){
            Assert.fail("Failed to verify the IssuanceTroubleTicket or Payment made on Bind. ");
        }
    }


    public void loginAsRandomARUserAndVerifyIssuancePolicyPeriod(GeneratePolicy generatePolicy){
        try {
            login(getARManger().getUserName(),"gw");
            new BCSearchPolicies(driver).searchPolicyByPolicyNumber(getPolicyNumberFromGeneratePolicyObject(generatePolicy));
            new InfoBar(driver).clickInfoBarAccountNumber();
        } catch (Exception e){
            Assert.fail("Failed to verify the Issuance PolicyPeriod. ");
        }
    }

    public void  loginAsRandomARUser() throws  Exception{
        login(getARManger().getUserName(),"gw");

    }


    public void loginAsRandomARUserAndSearchAccount(GeneratePolicy generatePolicy){
        try {
            loginAsRandomARUser();
            new BCSearchAccounts(driver).searchAccountByAccountNumber(generatePolicy.accountNumber);
        } catch (Exception e){
            Assert.fail("Failed to locate Account with account #  " + generatePolicy.accountNumber);
        }
    }


    public void loginAndVerifyIssuancePolicyPeriodAsARUser(GeneratePolicy generatePolicy, ARUsers arUsers){
        try {
            login(arUsers.getUserName(),arUsers.getPassword());
            new BCSearchPolicies(driver).searchPolicyByPolicyNumber(getPolicyNumberFromGeneratePolicyObject(generatePolicy));
        } catch (Exception e){
            Assert.fail("Failed to verify the Issuance PolicyPeriod. ");
        }
    }


    public void loginAndSearchAccountByGeneratePolicyObject(String userName, String password, GeneratePolicy generatePolicy) {
        login(userName, password);
        new BCSearchAccounts(driver).searchAccountByAccountNumber(generatePolicy.accountNumber);
    }

    public void loginAndSearchPolicyByGeneratePolicyObject(String userName, String password, GeneratePolicy policy) {
        login(userName, password);
        new BCSearchPolicies(driver).searchPolicyByPolicyNumber(getPolicyNumberFromGeneratePolicyObject(policy));
    }


    public HashMap<String, Object> getVariablesToRunDelinquencyTestScenarios(double originalDelinquentAmountOrInvoiceDueAmount ) {
        repository.gw.enums.OpenClosed openOrClosed;
        String errorMsg;
        HashMap<String, Object> getValues = new HashMap<>();
        double payAmount;
        boolean delinquencyFound;

        String printMsg ;
        double payDelinquentAmountBeforeRounding;

        //switch (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
        switch (1) {
            case 1: case 5: case 9: case 13: case 17: case 21: case 25:
                //Making Payment for 100% of Delinquent amount
                delinquencyFound = false;
                payAmount = originalDelinquentAmountOrInvoiceDueAmount;
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for 100% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 100% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 2: case 6: case 10: case 14: case 18: case 22: case 26 : case 29:
                //Making Payment for less than 70% of Original Delinquent amount, which should exit delinquency
                delinquencyFound = true;
                payDelinquentAmountBeforeRounding = (originalDelinquentAmountOrInvoiceDueAmount)*0.68;
                payAmount= BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Open;
                errorMsg = "Delinquency closed. This not expected, it should be open because payment made is less than 70% of Original Delinquent amount - which is below Threshold";
                printMsg = "This test is running a scenario where it pays 68% Original Delinquent Amount and check if delinquent exits or not";
                break;

            case 3: case 7: case 11: case 15: case 19: case 23: case 27:
                //Making Payment for 70% of Delinquent amount
                delinquencyFound = false;
                payDelinquentAmountBeforeRounding = (originalDelinquentAmountOrInvoiceDueAmount)*0.70;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.UP).doubleValue();
                openOrClosed = repository.gw.enums.OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for 70% of Original Delinquent Amount which is not Expected" ;
                printMsg = "This test is running a scenario where it pays 70% Original Delinquent Amount and check if delinquent exits or not";
                break;

            default:
                //Making Payment for more than 70% of Delinquent amount
                delinquencyFound = false;
                payDelinquentAmountBeforeRounding = (originalDelinquentAmountOrInvoiceDueAmount)*0.72;
                payAmount = BigDecimal.valueOf(payDelinquentAmountBeforeRounding).setScale(2, RoundingMode.DOWN).doubleValue();
                openOrClosed = OpenClosed.Closed;
                errorMsg = "Delinquency still exists after making payment for more than 70% of Original Delinquent Amount which is not Expected";
                printMsg = "This test is running a scenario where it pays 72% Original Delinquent Amount and check if delinquent exits or not";
                break;
        }
        System.out.println(printMsg);
        getValues.put("delinquencyFound",delinquencyFound);
        getValues.put("payAmount", payAmount);
        getValues.put("openOrClosed", openOrClosed);
        getValues.put("errorMsg", errorMsg);

        return getValues;
    }

}
