package scratchpad.brett;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class TestPolicyBOP extends BaseTest {

    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Insured Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
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
        System.out.println("effectiveDate: " + myPolicyObj.busOwnLine.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObj.busOwnLine.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObj.busOwnLine.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObj.busOwnLine.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObj.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObj.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObj.busOwnLine.getPolicyNumber());

    }

}
