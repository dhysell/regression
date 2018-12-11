package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.search.SearchSidebarPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;

public class SearchPoliciesTests extends BaseTest {

    private SearchSidebarPC searchSidebar;
    private TopMenuPC topMenu;
    private SearchPoliciesPC policySearchPage;
    private GeneratePolicy myPolicyObj;
    private String accountNumber;
    private String uwUserName;
    private String uwPassword;
    private String agentUserName;

    private WebDriver driver;

    @Test
    public void generate() throws Exception {

        // customizing location and building
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("TestNameChanges")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        accountNumber = myPolicyObj.accountNumber;
        Agents agent = myPolicyObj.agentInfo;
        agentUserName = agent.getAgentLastName();
        Underwriters underwriter = myPolicyObj.underwriterInfo;
        uwUserName = underwriter.getUnderwriterUserName();
        uwPassword = underwriter.getUnderwriterPassword();

    }

    /**
     * @throws Exception
     * @Author drichards
     * @Requirement DE2789
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description This checks for a null pointer, caused by the search cache
     * being enabled
     * @DATE Oct 19, 2015
     */
    @Test(dependsOnMethods = {"generate"}, enabled = false)
    public void testSearchException() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).login(uwUserName, uwPassword);

        topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        searchSidebar = new SearchSidebarPC(driver);
        searchSidebar.clickPolicies();

        policySearchPage = new SearchPoliciesPC(driver);
        policySearchPage.searchPolicyByAccountNumber(accountNumber);

        topMenu = new TopMenuPC(driver);
        topMenu.clickSearchTab();

        searchSidebar = new SearchSidebarPC(driver);
        searchSidebar.clickPolicies();

        ErrorHandling errorHandle = new ErrorHandling(driver);
        if (!errorHandle.text_ErrorHandlingErrorBannerMessages().isEmpty()) {
            String errorMsg = errorHandle.text_ErrorHandlingErrorBannerMessages().get(0).getText();
            boolean isContained = errorMsg.contains("NullPointerException");
            System.out.println(errorMsg);
            Assert.assertFalse(isContained,
                    "There is a null pointer exception, due to the search cache being enabled.");
        }

    }

    /**
     * @throws Exception
     * @Author drichards
     * @Requirement US4378
     * @RequirementsLink <a href="http:// ">Link Text</a>
     * @Description This checks to see if the basic search has been removed
     * @DATE Oct 19, 2015
     */
    @Test(dependsOnMethods = {"generate"}, enabled = true)
    public void testAddtlInterestSearch() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.startPolicyChange("Test Search Functionality", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings buildingPage = new GenericWorkorderBuildings(driver);
        buildingPage.clickBuildingsBuildingEdit(1);

        GenericWorkorderAdditionalInterests addtlInterest = new GenericWorkorderAdditionalInterests(driver);
        addtlInterest.clickBuildingsPropertyAdditionalInterestsSearch();

        if (addtlInterest.finds(By.xpath("//span[contains(text(), 'Advanced Search')]"))
                .size() > 0) {
            throw new Exception("The Advanced Search button exists. It should have been taken out.");
        }

    }

}
