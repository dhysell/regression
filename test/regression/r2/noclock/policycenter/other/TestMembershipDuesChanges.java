package regression.r2.noclock.policycenter.other;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
 * @Author skandibanda
 * @Requirement : US9370 : [Part II] COMMON - Membership Dues Changes
 * @Description -  Creating new contact in Full Application, selecting the Related contact/relationship under Policy info and verifying contact update in AB
 * Related Contacts screen
 * Issue policy,
 * @DATE Nov 22, 2016
 */
public class TestMembershipDuesChanges extends BaseTest {
    private GeneratePolicy Squire_PolicyObj;
    private AbUsers abUser = null;

    private WebDriver driver;

    @Test()
    public void testGenerateSquireAuto() throws Exception {


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Test" + StringsUtils.generateRandomNumberDigits(8), "Person1",
                AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }});

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withANIList(listOfANIs)
                .withInsFirstLastName("Guy", "Auto")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.FullApp);

    }

    @Test(dependsOnMethods = {"testGenerateSquireAuto"})
    public void validateSquireAuto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(Squire_PolicyObj.agentInfo.getAgentUserName(), Squire_PolicyObj.agentInfo.getAgentPassword(), Squire_PolicyObj.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        polInfo.clickRelatedContactsTab();
        polInfo.clickRelatedContactsAddButton();

        polInfo.selectRelationship(1, "Spouse");
        String pcRelationship = polInfo.getRelationship(1);
        polInfo.setRelatedTo(1, Squire_PolicyObj.aniList.get(0).getPersonFirstName(), Squire_PolicyObj.aniList.get(0).getPersonLastName());
        String pcRelatedTo = polInfo.getRelatedTo(1);
        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickUpdate();

		new GenericWorkorder(driver).clickGenericWorkorderQuote();


        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
        payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, payments.getDownPaymentAmount());
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
        GenericWorkorder workorder = new GenericWorkorder(driver);
        workorder.clickGenericWorkorderSubmitOptionsSubmit();

        guidewireHelpers.logout();
        Login logMeIn = new Login(driver);

        this.abUser = AbUserHelper.getRandomDeptUser("Accounting");
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

        menu.clickSearchTab();
        
        search.searchByFirstLastName(Squire_PolicyObj.pniContact.getFirstName(), Squire_PolicyObj.pniContact.getLastName(), Squire_PolicyObj.pniContact.getAddress().getLine1());
        
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsRelatedContactsLink();

        ContactDetailsRelatedContactsAB dependents = new ContactDetailsRelatedContactsAB(driver);
        String abRelatedTo = dependents.getRelatedContactsName(1);
        String abRelationship = dependents.getRelatedContactsRelationship(1);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(pcRelationship.equals(abRelationship), "Relationship not updated in contact Manager");
        softAssert.assertTrue(pcRelatedTo.equals(abRelatedTo), "Relationship not updated in contact Manager");
        softAssert.assertAll();

    }

}
