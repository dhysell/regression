/*Steve Broderick
 * This test ensures Defect 2750 is fixed.
 * Business Rule:
 * When Searching by number, no more than one result should be received.
 *
 */
package regression.r2.noclock.contactmanager.search;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.ab.topmenu.TopMenuAB;
import repository.ab.topmenu.TopMenuSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class SearchNumber extends BaseTest {
    private WebDriver driver;
    private AbUsers abuser;
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    // private ArrayList<PolicyLocationBuilding> locTwoBuildingList = new
    // ArrayList<PolicyLocationBuilding>();
    // private PolicyLocationBuildingAdditionalInterest loc1Bldg1AddInterest;
    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy tinPolicyObj = null;
    // private ContactSubType insPersonOrCompany = ContactSubType.Person;
    private String socialSecurityNumberFormatted;
    private String taxIDNumber;
    AddressInfo insPrimaryAddress;
    private String accountNumber;

    public String randomSocialSecurityNum() {
        int unformattedSocial = NumberUtils.generateRandomNumberInt(111111112, 899999998);
        String formattedSocial = String.valueOf(unformattedSocial);
//		formattedSocial = formattedSocial.substring(0, 3) + "-" + formattedSocial.substring(3, 5) + "-"
//				+ formattedSocial.substring(5);
        return formattedSocial;
    }

    public String randomTaxID() {
        int unformattedTax = NumberUtils.generateRandomNumberInt(980000000, 999999998);
        String formattedTax = String.valueOf(unformattedTax);
        // formattedTax = formattedTax.substring(0, 2) + "-" +
        // formattedTax.substring(2);
        return formattedTax;
    }

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy")
                - NumberUtils.generateRandomNumberInt(0, 50);
        this.taxIDNumber = randomTaxID();
        this.socialSecurityNumberFormatted = randomSocialSecurityNum();
        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
                false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(randomYear);
        loc1Bldg1.setClassClassification("storage");

        this.locOneBuildingList.add(loc1Bldg1);

        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Peter", "Parker")
                .withSocialSecurityNumber(socialSecurityNumberFormatted)
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }


    public void generateCompanyPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        int randomYear = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy")
                - NumberUtils.generateRandomNumberInt(0, 50);
        this.taxIDNumber = randomTaxID();
        this.socialSecurityNumberFormatted = randomSocialSecurityNum();
        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(
                false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(randomYear);
        loc1Bldg1.setClassClassification("storage");

        this.locOneBuildingList.add(loc1Bldg1);

        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        Contact myContact = new Contact();
        myContact.setCompanyName("TestTin");
        myContact.setPersonOrCompany(ContactSubType.Company);
        myContact.setTaxIDNumber(taxIDNumber);

        this.tinPolicyObj = new GeneratePolicy.Builder(driver)
//				.withInsPersonOrCompany(ContactSubType.Company)
//				.withInsCompanyName("TestTin")
//				.withTaxIDNumber(taxIDNumber)
                .withContact(myContact)
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test
    public void tinSearch() throws Exception {
        this.abuser = AbUserHelper.getRandomDeptUser("Policy Services");
        generateCompanyPolicy();
        getToSearch();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        advancedSearch.tinSearch(taxIDNumber);
        checkSearch(taxIDNumber, tinPolicyObj);

    }

    @Test()
    public void ssnSearch() throws Exception {
        this.abuser = AbUserHelper.getRandomDeptUser("Policy Services");
        generatePolicy();
        getToSearch();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        advancedSearch.clickReset();
        advancedSearch.ssnSearch(this.socialSecurityNumberFormatted);
        checkSearch(this.socialSecurityNumberFormatted, myPolicyObj);

    }

    @Test(dependsOnMethods = {"tinSearch"})
    public void accountSearch() throws Exception {
        this.abuser = AbUserHelper.getRandomDeptUser("Policy Services");
        getToSearch();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        advancedSearch.searchAccountGetResults(abuser, myPolicyObj.accountNumber);
        checkSearch(myPolicyObj.accountNumber, myPolicyObj);
    }

    public WebDriver getToSearch() throws Exception {
        this.abuser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login lp = new Login(driver);
        lp.login(this.abuser.getUserName(), this.abuser.getUserPassword());

        TopMenuSearchAB menu = new TopMenuSearchAB(driver);
        menu.clickSearch();

        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAdvancedSearchLink();
        return driver;
    }

    public void checkLienholder() {
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        List<WebElement> searchResult = advancedSearch.getSearchResults(myPolicyObj.busOwnLine.locationList.get(0)
                .getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber());
        if (searchResult.size() != 1) {
            Assert.fail("Only one search result should exist when searching by number.");
        }
        System.out.println(searchResult.get(0));
        for (WebElement i : searchResult) {
            if (!i.getText()
                    .contains(locOneBuildingList.get(0).getAdditionalInterestList().get(0).getLienholderNumber())) {
                Assert.fail("The Lienholder Number was not shown in the Search Results.");
            }

            if (!i.getText().contains(locOneBuildingList.get(0).getAdditionalInterestList().get(0).getCompanyName())) {
                Assert.fail("The Lienholder Name was not found in the Search Results.");
            }

            if (!i.getText()
                    .contains(locOneBuildingList.get(0).getAdditionalInterestList().get(0).getAddress().getLine1())) {
                Assert.fail("The first line of the Address was not found in the Search Results.");
            }
            if (!i.getText()
                    .contains(locOneBuildingList.get(0).getAdditionalInterestList().get(0).getAddress().getCity())) {
                Assert.fail("The City was not found in the Search Results.");
            }
            if (!i.getText().contains(
                    locOneBuildingList.get(0).getAdditionalInterestList().get(0).getAddress().getState().getName())) {
                Assert.fail("The State was not found in the Search Results.");
            }
            if (!i.getText()
                    .contains(locOneBuildingList.get(0).getAdditionalInterestList().get(0).getAddress().getZip())) {
                Assert.fail("The Zip Code was not found in the Search Results.");
            }
        }
    }

    public void checkSearch(String number, GeneratePolicy policyObject) {
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        List<WebElement> searchResult = advancedSearch.getSearchResults(number);
        if (searchResult.size() < 1) {
            Assert.fail("Only one search result should exist when searching by number.");
        }
        if (searchResult.size() > 1) {
            Assert.fail("One search result should exist when searching by tin.  Check the Message Queue ");
        }
        System.out.println(searchResult.get(0).getText());
        for (WebElement i : searchResult) {
            if (!i.getText().contains(policyObject.accountNumber)) {
                Assert.fail("The Account Number was not shown in the Search Results.");
            }

            if (!i.getText().contains("##-###" + taxIDNumber.substring(5))) {
                if (!i.getText().contains("###-##-" + socialSecurityNumberFormatted.substring(5))) {
                    System.out.println("###-##-" + socialSecurityNumberFormatted.substring(5) + " = control");
                    System.out.println(i.getText() + " = result");
                    Assert.fail("The last four of the Social Security Number or Tax ID Number did not match the Search Results.");
                }

                if (!i.getText().contains(policyObject.pniContact.getLastName() + ", " + myPolicyObj.pniContact.getFirstName())) {
                    Assert.fail("The Insured Name was not found in the Search Results.");
                }
            }
            if (i.getText().contains("##-###")) {
                if (!i.getText().contains(policyObject.pniContact.getCompanyName())) {
                    Assert.fail("The Insured Name was not found in the Search Results.");
                }
            }
            if (!i.getText().contains(policyObject.pniContact.getAddress().getLine1())) {
                Assert.fail("The first line of the Address was not found in the Search Results.");
            }
            if (!i.getText().contains(policyObject.pniContact.getAddress().getCity())) {
                Assert.fail("The City was not found in the Search Results.");
            }
            if (!i.getText().contains(policyObject.pniContact.getAddress().getState().getName())) {
                Assert.fail("The State was not found in the Search Results.");
            }
            if (!i.getText().contains(policyObject.pniContact.getAddress().getZip())) {
                Assert.fail("The Zip Code was not found in the Search Results.");
            }
            if (!i.getText().contains(policyObject.pniContact.getAddress().getPhoneBusinessFormatted())) {
                Assert.fail("The Phone Number was not found in the Search Results.");
            }

            if (!i.getText()
                    .contains(policyObject.agentInfo.getAgentNum())) {
                Assert.fail("The Agent information was not found in the Search Results.");
            }
        }
    }

    @Test
    public void testDuplicateSsnError() throws Exception {
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults searchResults = searchMe.getSearchResultsWithSSN("Joslin");
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        String ssn = basicsPage.getSsn();
        getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();
        searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults searchResultsNoSsn = searchMe.getSearchResultsWithoutSSN("Guy");
        basicsPage = new ContactDetailsBasicsAB(driver);
        basicsPage.clickContactDetailsBasicsEditLink();
        basicsPage.setContactDetailsBasicsSSN(ssn);
        basicsPage.clickContactDetailsBasicsUpdateLink();
        if (!new GuidewireHelpers(driver).containsErrorMessage("Duplicate SSN found - matching contact " + searchResults.getFirstName() + " " + searchResults.getLastNameOrCompanyName())) {

            Assert.fail("Check the duplicate SSN error, it should exist and not have the ssn in the error. \r\n"
                    + "The ssn is " + ssn + " r\n The Contact is " + searchResultsNoSsn.getFirstName() + " " + searchResultsNoSsn.getLastNameOrCompanyName());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws Exception {

    }

}
