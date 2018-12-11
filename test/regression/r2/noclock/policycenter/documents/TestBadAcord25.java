package regression.r2.noclock.policycenter.documents;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
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
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderLocations;

public class TestBadAcord25 extends BaseTest {

    GeneratePolicy myPolObj = null;
    String userName;
    String password;
    String accountNumber;
    String policyNumber;
    String uwUserName;
    String dateString = DateUtils.dateFormatAsString("yyyyMMddHHmmssSS", new Date());

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(enabled = true, description = "Creates an account through Issued")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // ArrayList<PolicyLocationBuilding> locOneBuildingList = new
        // ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocation> locationsLists = null;

        // locOneBuildingList.add(new PolicyLocationBuilding());
        locationsLists = new ArrayList<PolicyLocation>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new PolicyLocation(new AddressInfo(true), false) {{
                    this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
                    }}); // END ADDITIONAL COVERAGES STUFF

                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                        this.add(new PolicyLocationBuilding() {{
                        }});
                    }});
                }});

                this.add(new PolicyLocation(new AddressInfo(true), false) {{
                    this.setAdditionalCoveragesStuff(new PolicyLocationAdditionalCoverages() {{
                    }}); // END ADDITIONAL COVERAGES STUFF

                    this.setAdditionalInsuredLocationsList(new ArrayList<PolicyLocationAdditionalInsured>() {{
                        this.add(new PolicyLocationAdditionalInsured(ContactSubType.Company) {{
                            this.setAiRole(AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
                        }});
                    }});

                    this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                        this.add(new PolicyLocationBuilding() {{
                        }});
                    }});
                }});
            }
        };

        this.myPolObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompanyDependingOnDay("Country", "Check", "Country Check")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsLists)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        userName = myPolObj.agentInfo.getAgentUserName();
        password = myPolObj.agentInfo.getAgentPassword();
        uwUserName = myPolObj.underwriterInfo.getUnderwriterUserName();
        policyNumber = myPolObj.busOwnLine.getPolicyNumber();
        accountNumber = myPolObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("UW: " + uwUserName);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Description Remove Location with AI, removing building first, Quote,
     * check Forms for lack or ACORD 25.
     * @DATE Nov 11, 2015
     */
    @Test(enabled = true, description = "Remove Locatin with AI, removing building first, Quote, check Forms for lack or ACORD 25.", dependsOnMethods = {
            "createPolicy"})
    public void checkACORD25AfterLocAIRemoval() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(uwUserName, password, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickPolicyNumber(policyNumber);

        StartPolicyChange myPolChange = new StartPolicyChange(driver);
        myPolChange.startPolicyChange("Remove First Location", null);

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings myBuldings = new GenericWorkorderBuildings(driver);
        myBuldings.removeBuildingOnLocation(2, 1);

        mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuLocations();

        GenericWorkorderLocations myLoc = new GenericWorkorderLocations(driver);
        myLoc.removeLocationByLocNumber(2);

        GenericWorkorder myGenWork = new GenericWorkorder(driver);
        myGenWork.clickGenericWorkorderQuote();

        mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuForms();

        GenericWorkorderForms myForms = new GenericWorkorderForms(driver);
        myForms.verifyFormIsMissing_Obsolete("ACORD 25");

    }

}
