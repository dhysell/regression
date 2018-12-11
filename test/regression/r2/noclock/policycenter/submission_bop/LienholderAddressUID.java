package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
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
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import regression.r2.noclock.contactmanager.search.SearchNumber;

/**
 * @Author sbroderick
 * @Requirement DE 2986
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/9877319305d/teamstatus?iterationKey=45349056219">
 * Rally Defect 2986, Selecting Lienholder yields
 * IllegalArgument</a>
 * @Description This test class will randomly grab a lienholder and add them as
 * an additional Interest.
 * @DATE Nov 12, 2015
 */
public class LienholderAddressUID extends BaseTest {

    private ContactSubType lienCompOrPerson;
    private String lienComp;
    private String lienFirstName;
    private String lienLastName;
    private String lienNum;
    private AddressInfo lienAddress = new AddressInfo();
    private WebDriver driver;

    public String getRandomLienNum() {
        int unformattedTax = NumberUtils.generateRandomNumberInt(980000, 984611);
        Integer lienNum = unformattedTax;
        return lienNum.toString();
    }

    // This method gets a random Lienholder.
    @Test
    public void getLienInfo() throws Exception {

        //Configuration.setProduct(ApplicationOrCenter.ContactManager);

        SearchNumber useSearch = new SearchNumber();
        useSearch.getToSearch();
        AdvancedSearchAB advancedSearch = new AdvancedSearchAB(driver);
        this.lienNum = getRandomLienNum();
        SidebarAB sidebar = new SidebarAB(driver);
        sidebar.clickSidebarAdvancedSearchLink();
//		advancedSearch.searchLienholderNumber(lienNum);
        List<WebElement> searchResult = advancedSearch.getSearchResults(lienNum);
        lienComp = searchResult.get(0).findElement(By.xpath(".//td[5]/div/a")).getText();
        if (lienComp.contains(",")) {
            this.lienCompOrPerson = ContactSubType.Person;
            String[] split = lienComp.split(", ");
            this.lienLastName = split[0].trim();
            this.lienFirstName = split[1].trim();
        } else {
            this.lienCompOrPerson = ContactSubType.Company;
        }

        this.lienAddress.setLine1(searchResult.get(0).findElement(By.xpath("//td[6]/div")).getText());
        this.lienAddress.setCity(searchResult.get(0).findElement(By.xpath("//td[7]/div")).getText());
        this.lienAddress.setState(State.valueOfName(searchResult.get(0).findElement(By.xpath("//td[8]/div")).getText()));
        this.lienAddress.setZip(searchResult.get(0).findElement(By.xpath("//td[9]/div")).getText());
    }

    // Generate
    @Test(dependsOnMethods = {"getLienInfo"})
    public void makePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy")
                - NumberUtils.generateRandomNumberInt(0, 50);

        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        AdditionalInterest loc1Bld1AddInterest = null;

        if (lienCompOrPerson.equals(ContactSubType.Company)) {
			loc1Bld1AddInterest = new AdditionalInterest(lienComp, lienAddress);
        } else {
			loc1Bld1AddInterest = new AdditionalInterest(this.lienFirstName, this.lienLastName, lienAddress);
        }

        loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        // loc1Bld1AddInterest.setAddress(lienAddress);
        loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
        loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

        locOneBuildingList.add(loc1Bldg1);

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company).withInsCompanyName("Test Policy")
                .withPolOrgType(OrganizationType.Partnership).withBusinessownersLine(myline)
                .withPolicyLocations(locationsList).withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
    }

}
