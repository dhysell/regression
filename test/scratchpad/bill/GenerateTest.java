/**
 * @author bmartin Aug 12, 2015
 * @notes Creates test policies
 */
package scratchpad.bill;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;

public class GenerateTest extends BaseTest {

    public GenerateTest() {
        super();
    }

    GeneratePolicy myPolicyObj = null;

    private String agentUserName;
    private String agentPassword;
    private String accountNumber;

    private WebDriver driver;

    @Test()
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        final int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

        final PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail) {{
            this.setAdditionalCoverageStuff(myLineAddCov);
        }};

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding() {{
            this.setYearBuilt(yearTest);
            this.setClassClassification("storage");
        }};

        locOneBuildingList.add(loc1Bldg1);

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));


        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Test Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        this.agentUserName = myPolicyObj.agentInfo.getAgentUserName();
        this.agentPassword = myPolicyObj.agentInfo.getAgentPassword();
        this.accountNumber = myPolicyObj.accountNumber;

        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(agentUserName, agentPassword, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuPayment();

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + agentUserName + "\n#############");

    }
}
