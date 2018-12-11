package regression.r3.clock.billingcenter.other;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonHistory;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

@QuarantineClass
public class InvoiceSteamsOverrideAndLeadPolicyMarkersTest extends BaseTest {
    private GeneratePolicy cppPolicyObj, bopPolicyObj;
    private Agents agent;
    private BCAccountMenu acctMenu;
    private AccountPolicies acctPolicy;
    private BCPolicySummary pcSum;
    private PolicySummaryInvoicingOverrides chgStream;
    private ARUsers arUser = new ARUsers();
    private String historyItem;
    private String mySwitch;
    private String warningMsg;
    private String warningMsgShouldBe = "The invoice stream you are trying to override to has a policy with a different expiration date";

    private WebDriver driver;

    private ArrayList<String> bopPolicyAttribute = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("same as CPP");
            this.add("different effective date");
        }
    };

    private void verifyHistory(String historyItem) throws GuidewireException {
        BCPolicyMenu bcMenu = new BCPolicyMenu(driver);
        bcMenu.clickBCMenuHistory();
        BCCommonHistory pcHistory = new BCCommonHistory(driver);
        //need to pass the myPolicyObj.effectiveDate in the future
        if (!pcHistory.verifyHistoryTable(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), historyItem)) {
            throw new GuidewireException("BillingCenter: ", "Doesn't find the Overriden history.");
        }
    }

    //generate two policies on same account
    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine());
            this.setVehicleList(new ArrayList<Vehicle>() {{
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }});
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(new ArrayList<Contact>() {{
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                }});
            }});
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        cppPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("OverrideReversalInstallmentTest")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println("company name is " + cppPolicyObj.pniContact.getCompanyName());
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        Date bopEffectiveDate;
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(cppPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(cppPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(cppPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(cppPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(cppPolicyObj.pniContact.getAddress().getZip());
        int randomNumber = NumberUtils.generateRandomNumberInt(3, 30);
        //if randomNumber is odd, generate BOP policy with different effective date
        //if randomNumber is even, generate BOP policy with same effective date as the CPP

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        if (randomNumber % 2 == 0) {
            bopEffectiveDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
            mySwitch = bopPolicyAttribute.get(0);

        } else {
            // odd number, generate BOP with different effective date (its insuredName is the same as CPP Policy)
            bopEffectiveDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 2);
            mySwitch = bopPolicyAttribute.get(1);
        }
        this.bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withPolEffectiveDate(bopEffectiveDate)
                .withInsPersonOrCompany(cppPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(cppPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(cppPolicyObj.polOrgType)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withMembershipDuesOnAllInsureds()
                .withPaymentPlanType(cppPolicyObj.paymentPlanType)
                .withDownPaymentType(cppPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void overrideAndVerify() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        //if BOP has a different expiration date from CPP, verify the warning
        if (mySwitch.equals(bopPolicyAttribute.get(1))) {
            pcSum = new BCPolicySummary(driver);
            pcSum.updateInvoicingOverride();
            chgStream = new PolicySummaryInvoicingOverrides(driver);
            chgStream.selectOverridingInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
            chgStream.clickNext();
            try {
                warningMsg = new GuidewireHelpers(driver).getFirstErrorMessage();
                if (!warningMsg.contains(warningMsgShouldBe))
                    throw new GuidewireException("BillingCenter: ", "got incorrect banner message. It should be '" + warningMsgShouldBe + "'.");
            } catch (Exception e) {
                throw new GuidewireException("BillingCenter: ", "Alert doesn't exist when it should.");
            }
            //if BOP has same expiration date as CPP, override BOP with CPP
        } else {
            pcSum = new BCPolicySummary(driver);
            pcSum.overrideInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        }
    }

    @Test(dependsOnMethods = {"overrideAndVerify"})
    public void verifyOverridenPolicyHistoryAndMarker() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        if (mySwitch.equals(bopPolicyAttribute.get(0))) {
            historyItem = "Invoice Stream for Policy " + bopPolicyObj.busOwnLine.getPolicyNumber() + " changed from " + bopPolicyObj.busOwnLine.getPolicyNumber() + " (Monthly) to " + cppPolicyObj.commercialPackage.getPolicyNumber();
            new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
            //verify that BOP is overridden by CPP on Account Policies screen
            acctMenu = new BCAccountMenu(driver);
            acctMenu.clickAccountMenuPolicies();
            acctPolicy = new AccountPolicies(driver);
            if (!acctPolicy.verifyInvoiceStream(bopPolicyObj.busOwnLine.getPolicyNumber(), cppPolicyObj.commercialPackage.getPolicyNumber())) {
                throw new GuidewireException("BillingCenter: ", "Overriden is not correct.");
            }
            acctPolicy.clickPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
            //verify the overridden flag on BOP policy's summary screen
            pcSum = new BCPolicySummary(driver);
            if (!pcSum.policyOverridenFlagExist()) {
                throw new GuidewireException("BillingCenter: ", "Overriden flag doesn't display.");
            }
            //verify the overridden history
            verifyHistory(historyItem);
        }
    }

    @Test(dependsOnMethods = {"verifyOverridenPolicyHistoryAndMarker"})
    public void verifyLeadPolicyMarker() throws Exception {
        if (mySwitch.equals(bopPolicyAttribute.get(0))) {
            Config cf = new Config(ApplicationOrCenter.BillingCenter);
            driver = buildDriver(cf);
            new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
            pcSum = new BCPolicySummary(driver);
            if (!pcSum.policyLeadFlagExist()) {
                throw new GuidewireException("BillingCenter: ", "Lead Policy flag doesn't display.");
            }
        }
    }

    @Test(dependsOnMethods = {"verifyLeadPolicyMarker"})
    public void removeOverridenAndVerifyHistory() throws Exception {
        if (mySwitch.equals(bopPolicyAttribute.get(0))) {
            Config cf = new Config(ApplicationOrCenter.BillingCenter);
            driver = buildDriver(cf);
            historyItem = "Invoice Stream for Policy " + bopPolicyObj.busOwnLine.getPolicyNumber() + " changed from " + cppPolicyObj.commercialPackage.getPolicyNumber() + " (Monthly) to " + bopPolicyObj.busOwnLine.getPolicyNumber();
            new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
            //remove the override
            pcSum = new BCPolicySummary(driver);
            pcSum.reverseInvoiceStreamOverride();
            //verify the history
            verifyHistory(historyItem);
        }
    }

    //super user has the permission to edit Overriding Payer
    @Test(dependsOnMethods = {"overrideAndVerify"})
    public void verifyOverridingPayerOnSuperUser() throws Exception {
        if (mySwitch.equals(bopPolicyAttribute.get(0))) {
            Config cf = new Config(ApplicationOrCenter.BillingCenter);
            driver = buildDriver(cf);

            new Login(driver).loginAndSearchAccountByAccountNumber("su", arUser.getPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
            pcSum = new BCPolicySummary(driver);
            pcSum.updateInvoicingOverride();
            chgStream = new PolicySummaryInvoicingOverrides(driver);
            if (!chgStream.checkIfOverridingPayerEditboxExists()) {
                throw new GuidewireException("BillingCenter: ", "Super user should have the permission of editting Overriding Payer.");
            }
        }
    }
}
