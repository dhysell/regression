package regression.r3.noclock.billingcenter.invoicing;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.policy.summary.PolicySummaryInvoicingOverrides;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author bhiltbrand
 * @Requirement When overriding invoice streams on a policy containing LH charges, the LH charges were being reset to the insured as the payer.
 * This test ensures that they don't change and stay assigned to the correct payers.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-01%20General%20Installment%20Scheduling.docx">Requirements Documentation</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/50416908688">Rally Story US6727</a>
 * @Description
 * @DATE Mar 18, 2016
 */
public class TestLHChargePayerChange extends BaseTest {
    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy multiPolicyObj = null;
    private Agents agent;
    private ARUsers arUser = new ARUsers();

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> driverList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(driverList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("LH Charge Payer")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        ArrayList<AdditionalInterest> lienholderBuildingAdditionalInterests = new ArrayList<AdditionalInterest>();
        PolicyLocationBuilding lienholderBuilding = new PolicyLocationBuilding();
        AdditionalInterest lienholderBuildingAddInterest = new AdditionalInterest(ContactSubType.Company);
        lienholderBuildingAddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        lienholderBuildingAddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        lienholderBuildingAdditionalInterests.add(lienholderBuildingAddInterest);
        lienholderBuilding.setAdditionalInterestList(lienholderBuildingAdditionalInterests);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locOneBuildingList.add(lienholderBuilding);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(myPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(myPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(myPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(myPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(myPolicyObj.pniContact.getAddress().getZip());

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.multiPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(myPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(myPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(OrganizationType.LLC)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withMembershipDuesOnAllInsureds()
                .withPaymentPlanType(myPolicyObj.paymentPlanType)
                .withDownPaymentType(myPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(myPolicyObj.toString());
        System.out.println(multiPolicyObj.toString());
    }

    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void overrideInvoiceStream() throws Exception {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
        try {
            charges.getChargesOrChargeHoldsPopupTableRow(this.multiPolicyObj.busOwnLine.getEffectiveDate(), this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber(), null, ChargeCategory.Premium, TransactionType.Issuance, null, null, null, this.multiPolicyObj.busOwnLine.getPolicyNumber(), this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), null, null, this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber(), null, null, null);
        } catch (Exception e) {
            Assert.fail("The charge tied to the lienholder on the BOP policy was not found in the Charges Table. Test cannot continue and has failed.");
        }

        BCTopMenuPolicy policyTopMenu = new BCTopMenuPolicy(driver);
        policyTopMenu.menuPolicySearchPolicyByPolicyNumber(this.multiPolicyObj.busOwnLine.getPolicyNumber());

        BCPolicySummary summaryPage = new BCPolicySummary(driver);
        summaryPage.updateInvoicingOverride();

        PolicySummaryInvoicingOverrides invoiceOverrides = new PolicySummaryInvoicingOverrides(driver);
        invoiceOverrides.overrideInvoiceStream(myPolicyObj.commercialPackage.getPolicyNumber());

        BCTopMenuAccount accountTopMenu = new BCTopMenuAccount(driver);
        accountTopMenu.menuAccountSearchAccountByAccountNumber(this.myPolicyObj.accountNumber);

        menu = new BCAccountMenu(driver);
        menu.clickBCMenuCharges();

        charges = new AccountCharges(driver);
        try {
            charges.getChargesOrChargeHoldsPopupTableRow(this.multiPolicyObj.busOwnLine.getEffectiveDate(), this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber(), null, ChargeCategory.Premium, TransactionType.Issuance, null, null, null, this.multiPolicyObj.busOwnLine.getPolicyNumber(), this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), null, null, this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber(), null, null, null);
        } catch (Exception e) {
            try {
                charges.getChargesOrChargeHoldsPopupTableRow(this.myPolicyObj.busOwnLine.getEffectiveDate(), this.myPolicyObj.accountNumber, null, ChargeCategory.Premium, TransactionType.Issuance, null, null, null, this.multiPolicyObj.busOwnLine.getPolicyNumber(), this.multiPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount(), null, null, null, null, null, null);
                Assert.fail("The charge tied to the lienholder on the BOP policy was originally set correctly, but has since changed payers. This is incorrect. Test Failed.");
            } catch (Exception e2) {
                Assert.fail("The charge tied to the lienholder on the BOP policy was not found in the Charges Table. Test failed.");
            }
        }

    }

}
