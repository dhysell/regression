package scratchpad.sai;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateBasicPolicyPLProperty extends BaseTest {
    public ARUsers arUser;

    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("JOHN", "DOE")
                .withProductType(ProductLineType.Squire)
                //.withPolTermLengthDays(60)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.PolicyIssued);

        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickAccountArrow();

        BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
        topMenuStuff.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);


        System.out.println("insLastName: " + myPolicyObj.pniContact.getLastName());
        System.out.println("insFirstName: " + myPolicyObj.pniContact.getFirstName());
        System.out.println("insCompanyName: " + myPolicyObj.pniContact.getCompanyName());
        System.out.println("accountNumber: " + myPolicyObj.accountNumber);
        System.out.println("effectiveDate: " + myPolicyObj.squire.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObj.squire.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObj.squire.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObj.squire.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObj.squire.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObj.squire.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObj.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObj.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObj.squire.getPolicyNumber());
    }
}
