package scratchpad.brett;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateBasicPolicyPLAuto extends BaseTest {
    public ARUsers arUser;
    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Squire", "Test")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                //.withRandomPaymentPlanType()
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);

        ARUsers arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickAccountArrow();

        BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
        topMenuStuff.menuAccountSearchAccountByAccountNumber(myPolicyObjPL.accountNumber);


        System.out.println("insLastName: " + myPolicyObjPL.pniContact.getLastName());
        System.out.println("insFirstName: " + myPolicyObjPL.pniContact.getFirstName());
        System.out.println("insCompanyName: " + myPolicyObjPL.pniContact.getCompanyName());
        System.out.println("accountNumber: " + myPolicyObjPL.accountNumber);
        System.out.println("effectiveDate: " + myPolicyObjPL.squire.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObjPL.squire.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObjPL.squire.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObjPL.squire.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObjPL.squire.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObjPL.squire.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObjPL.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObjPL.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObjPL.squire.getPolicyNumber());
    }
}
