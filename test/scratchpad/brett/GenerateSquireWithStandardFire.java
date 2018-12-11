package scratchpad.brett;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateSquireWithStandardFire extends BaseTest {

    private GeneratePolicy myPolicyObj = null;

    private WebDriver driver;

    @Test
    public void issueSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);

        locToAdd.setPlNumAcres(8);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("SquireWith", "StdFire")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"issueSquirePolicy"})
    public void issueStandardFire() throws Exception {
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.Potatoes));
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locationsList1.add(locToAdd1);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.commodity = true;
        myStandardFire.setLocationList(locationsList1);

        //		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
        //				this.multiPolicyObj = new GeneratePolicy.Builder(driver)
        //				.withAgent(this.myPolicyObj.agentInfo)
        //				.withProductType(ProductLineType.StandardFire)
        //				.withLineSelection(LineSelection.StandardFirePL)
        //				.withSquirePolicyUsedForStandardFire(this.myPolicyObj,true, true)//Not setting everything the same as was used for the squire.
        //				.withPaymentPlanType(myPolicyObj.paymentPlanType)
        //				.withDownPaymentType(myPolicyObj.downPaymentType)
        //				.withPolicyLocations(locationsList1)
        //				.build(GeneratePolicyType.PolicyIssued);

        myPolicyObj.standardFire = myStandardFire;
        myPolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        myPolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"issueSquirePolicy"})
    public void payOnSquirePolicyAndOverrideInvoiceStreams() throws Exception {
        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        int randomAmount = NumberUtils.generateRandomNumberInt(25, 500);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(this.myPolicyObj, (this.myPolicyObj.standardFire.getPremium().getDownPaymentAmount() + randomAmount), this.myPolicyObj.standardFire.getPolicyNumber());
        System.out.println("The amount extra that went to unapplied funds was $" + randomAmount + ".00");

        BCTopMenuPolicy topMenuStuff;
        try {
            topMenuStuff = new BCTopMenuPolicy(driver);
            topMenuStuff.menuPolicySearchPolicyByPolicyNumber(this.myPolicyObj.standardFire.getPolicyNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }


        BCPolicySummary policySummary = new BCPolicySummary(driver);
        policySummary.overrideInvoiceStream(this.myPolicyObj.standardFire.getPolicyNumber());

        new GuidewireHelpers(driver).logout();
    }

}
