package regression.r2.noclock.policycenter.submission_misc.subgroup7;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorderPayment;

/**
 * @Author nvadlamudi
 * @Requirement : US10929: COMMON - Add a new field for ACH used as Monthly and Down Payment
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Common-Admin/PC8%20-%20Common%20-%20QuoteApplication%20-%20Payment.xlsx">Story card</a>
 * @Description : checking the Payer list names
 * @DATE Oct 13, 2017
 */
public class TestPaymentPayerDetails extends BaseTest {
    GeneratePolicy myPolicyObj;

    private WebDriver driver;

    @Test()
    private void testValidateSquirePaymentPayerInfo() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
                "New" + StringsUtils.generateRandomNumberDigits(8), "Payer", AdditionalNamedInsuredType.Affiliate,
                new AddressInfo(false));
        ani.setHasMembershipDues(true);
        ani.setNewContact(CreateNew.Create_New_Always);
        listOfANIs.add(ani);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.liabilitySection = myLiab;
        property.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = property;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withANIList(listOfANIs)
                .withInsFirstLastName("Payment", "Submis")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.ACH_EFT)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(),
                myPolicyObj.accountNumber);

        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        paymentPage.fillOutPaymentPage(myPolicyObj);

        //Downpayment
        paymentPage.clickDownPaymentByRow(1);
        paymentPage.setPayer(myPolicyObj.pniContact.getLastName());
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        String errorMessage = "";
        if (!paymentPage.getDownPaymentRowTextbyRow(1, "Payer").contains(myPolicyObj.pniContact.getLastName())) {
            errorMessage = errorMessage + "DownPayment - Expected Insured name not listed in Payer \n";
        }
        paymentPage.clickDownPaymentByRow(1);

        paymentPage.setPayer(myPolicyObj.aniList.get(0).getPersonLastName());
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        if (!paymentPage.getDownPaymentRowTextbyRow(1, "Payer").contains(myPolicyObj.aniList.get(0).getPersonLastName())) {
            errorMessage = errorMessage + "DownPayment - Expected ANI name not listed in Payer \n";
        }
        paymentPage.clickDownPaymentByRow(1);
        paymentPage.setPayer("Testing ACH Holder");
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        if (!paymentPage.getDownPaymentRowTextbyRow(1, "Payer").contains("Testing ACH Holder")) {
            errorMessage = errorMessage + "DownPayment - Expected Other name not listed in Payer \n";
        }

        //Monthly Payment Info
        paymentPage.clickPaymentPlan(PaymentPlanType.Monthly);
        paymentPage.removeFirstDownPayment();
        paymentPage.clickAddPaymentInfoButton();
        paymentPage.setPaymentInfo(new BankAccountInfo());
        paymentPage.clickOk();

        paymentPage.clickPaymentInfoByRow(1);
        paymentPage.setPayer(myPolicyObj.pniContact.getLastName());
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        if (!paymentPage.getPaymentInfoRowTextbyRow(1, "Payer").contains(myPolicyObj.pniContact.getLastName())) {
            errorMessage = errorMessage + "PaymentInfo - Expected Insured name not listed in Payer \n";
        }
        paymentPage.clickPaymentInfoByRow(1);

        paymentPage.setPayer(myPolicyObj.aniList.get(0).getPersonLastName());
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        if (!paymentPage.getPaymentInfoRowTextbyRow(1, "Payer").contains(myPolicyObj.aniList.get(0).getPersonLastName())) {
            errorMessage = errorMessage + "PaymentInfo - Expected ANI name not listed in Payer \n";
        }
        paymentPage.clickPaymentInfoByRow(1);
        paymentPage.setPayer("Testing ACH Holder");
        paymentPage.clickOk();
        paymentPage.sendArbitraryKeys(Keys.TAB);
        if (!paymentPage.getPaymentInfoRowTextbyRow(1, "Payer").contains("Testing ACH Holder")) {
            errorMessage = errorMessage + "PaymentInfo - Expected Other name not listed in Payer \n";
        }

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }
    }
}
