package regression.r2.noclock.policycenter.other;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
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
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

/* Steve Broderick
 *
 * When an Insured pays dues and has an Additional Named Insured with the type spouse, the spouse account will show the due paid on the other account.
 *
 * */
@QuarantineClass
public class AdditionalNamedInsuredDues extends BaseTest {

    GeneratePolicy newPolicy;
    private AddressInfo aniAddress = new AddressInfo(true);
    private AddressInfo aniCompAddress = new AddressInfo(true);

    private WebDriver driver;

    @Test(description = "Generates Policy", enabled = true)
    public void generatePolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        PolicyInfoAdditionalNamedInsured anitoadd = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "No", "Newdefects", AdditionalNamedInsuredType.Spouse, aniAddress);
        anitoadd.setNewContact(CreateNew.Create_New_Always);
        anitoadd.setHasMembershipDues(true);
        listOfANIs.add(anitoadd);

        PolicyInfoAdditionalNamedInsured anitoadd2 = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Employee", "Name1", AdditionalNamedInsuredType.Employee, aniCompAddress);
        anitoadd2.setNewContact(CreateNew.Create_New_Always);
        anitoadd2.setHasMembershipDues(false);
        listOfANIs.add(anitoadd2);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        newPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.City)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Dues", "Defect")
                .withMembershipDuesOnPNI()
                .withInsPrimaryAddress(new AddressInfo(true))
                .withANIList(listOfANIs)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Credit_Debit)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(description = "Checks ContactManager for Dues on Spouse", dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void checkAB() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login("kharrild", "gw");

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB contactSearch = new AdvancedSearchAB(driver);
        contactSearch.searchByFirstLastName("No", "Newdefects", aniAddress.getLine1());

        ContactDetailsBasicsAB myContact = new ContactDetailsBasicsAB(driver);
        myContact.clickContactDetailsBasicsPaidDuesLink();

        ContactDetailsPaidDuesAB duesPage = new ContactDetailsPaidDuesAB(driver);
        List<WebElement> dues = duesPage.getPaidDuesList();
        if (dues.size() < 1) {
            Assert.fail("The spouse dues were not found in ContactManager.");
        }

        menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        contactSearch = new AdvancedSearchAB(driver);
        contactSearch.searchByFirstLastName("Employee", "Name1", aniCompAddress.getLine1());

        myContact = new ContactDetailsBasicsAB(driver);
        boolean found = myContact.clickContactDetailsBasicsPaidDuesLink();
        if (found) {
            List<WebElement> compDues = duesPage.getPaidDuesList();
            if (compDues.size() >= 1) {
                Assert.fail("The employee has dues.");
            }
        }
    }
}
