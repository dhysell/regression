package previousProgramIncrement.pi2_062818_090518.f97_QuickerQuickQuote_ContactChanges;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.bc.account.summary.BCAccountSummary;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AbUserHelper;

import java.util.ArrayList;

public class US15516ContactCreationInAbAtBind extends BaseTest {

    private GeneratePolicy myBopPolicy = null;
    private GeneratePolicy mySquirePolicy = null;

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement Contacts will now come over at bind and not when they are created.
     * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:b:/s/TeamNucleus/EQo8s6wvEodOsC_0MOhlTkMBwxFfmS8kk9ytjRi1dsQOPQ?e=OC8GGd"></a>
     * @Description
     * @DATE Jul 23, 2018
     */
    
    public void createBopPolicy() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

        ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
        locOneBuildingList.add(loc1Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        ArrayList<PolicyInfoAdditionalNamedInsured> aiList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        PolicyInfoAdditionalNamedInsured ai = new PolicyInfoAdditionalNamedInsured();
        ai.setCompanyOrPerson(ContactSubType.Person);
        ai.setPersonFirstName("mrs");
        ai.setPersonLastName("Noacctyet");
        ai.setRelationshipToPNI(AdditionalNamedInsuredType.Spouse);
        ai.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        aiList.add(ai);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        this.myBopPolicy = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("US15516", "Noacctyet")
                .withPolOrgType(OrganizationType.Individual)
                .withANIList(aiList)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);
    }

    @Test
    public void bopContactCreation() throws Exception {

        createBopPolicy();

        Assert.assertFalse(checkAB(), "When creating a full app bop, the contact should not be found in ContactManager");
    }
    
    @Test
    public void createIssuedBop() throws Exception {
    	 Config cf = new Config(ApplicationOrCenter.PolicyCenter);
         WebDriver driver = buildDriver(cf);
        
         ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
         ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

         ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
         AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
         loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
         loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
         PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
         loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
         locOneBuildingList.add(loc1Bldg1);
         locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

         ArrayList<PolicyInfoAdditionalNamedInsured> aiList = new ArrayList<PolicyInfoAdditionalNamedInsured>();
         PolicyInfoAdditionalNamedInsured ai = new PolicyInfoAdditionalNamedInsured();
         ai.setCompanyOrPerson(ContactSubType.Person);
         ai.setPersonFirstName("mrs");
         ai.setPersonLastName("Noacctyet");
         ai.setRelationshipToPNI(AdditionalNamedInsuredType.Spouse);
         ai.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
         aiList.add(ai);

         this.myBopPolicy = new GeneratePolicy.Builder(driver)
                 .withCreateNew(CreateNew.Create_New_Always)
                 .withInsPersonOrCompany(ContactSubType.Person)
                 .withInsFirstLastName("US15516", "Noacctyet")
                 .withPolOrgType(OrganizationType.Individual)
                 .withANIList(aiList)
                 .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
                 .withPolicyLocations(locationsList)
                 .withPaymentPlanType(PaymentPlanType.Annual)
                 .withDownPaymentType(PaymentType.Cash)
                 .build(GeneratePolicyType.PolicyIssued);
               
        driver.quit();

        Assert.assertTrue(checkAB(), "The issued BOP policy should exist in ContactManager");
        
        driver.quit();
        cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Login login = new Login(driver);
        login.loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myBopPolicy.accountNumber);
        BCAccountSummary bcSummaryPage = new BCAccountSummary(driver);
        Assert.assertTrue(bcSummaryPage.checkIfPolicyNumberLinkInOpenPolicyStatusTableIsActive(), "Ensure Policy can be found in BillingCenter.");
        driver.quit();

    }
    
    public boolean checkAB() throws Exception {
    	 Config cf = new Config(ApplicationOrCenter.ContactManager);
         WebDriver driver = buildDriver(cf);
         AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy");
         Login logMeIn = new Login(driver);
         logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

         TopMenuAB getToSearch = new TopMenuAB(driver);
         getToSearch.clickSearchTab();

         AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
         AdvancedSearchResults searchResult = searchMe.searchByFirstNameLastNameAnyAddress(this.myBopPolicy.pniContact.getFirstName(), this.myBopPolicy.pniContact.getLastName());
         driver.quit();
        return searchResult != null;
        
    }

}
