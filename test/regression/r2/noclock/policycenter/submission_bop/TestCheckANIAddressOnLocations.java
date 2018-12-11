package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
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
import repository.pc.workorders.generic.GenericWorkorderLocations;

public class TestCheckANIAddressOnLocations extends BaseTest {

    GeneratePolicy myPolicyBoundObj = null;

    String userName;
    String userPass;
    String accountNumber;
    AddressInfo aniAddress = new AddressInfo(true);
    String membershipTypeANI;

    String dateString = DateUtils.dateFormatAsString("yyyyMMddHHmmssSS", new Date());
    private WebDriver driver;

    @Test(enabled = true, description = "Creates an account through Full App")
    public void generateApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        locOneBuildingList.add(new PolicyLocationBuilding());

        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList) {{
        }});

        // BUSINESS OWNERS LINE
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(
                smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {{
            this.setLiabilityLimits(liabilityLimits.get(NumberUtils.generateRandomNumberInt(0, (liabilityLimits.size() - 1))));
            this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
            }}); // END ADDITIONAL COVERAGES
        }}; // END BUSINESS OWNERS LINE

        ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
        listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Additional", "Type",
                AdditionalNamedInsuredType.Spouse, aniAddress) {{
            this.setNewContact(CreateNew.Create_New_Always);
        }});

        this.myPolicyBoundObj = new GeneratePolicy.Builder(driver)
                .withBusinessownersLine(boLine)
                .withInsPersonOrCompanyDependingOnDay("Membership", "Type", "Membership Type")
                .withANIList(listOfANIs)
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        userName = myPolicyBoundObj.agentInfo.getAgentUserName();
        userPass = myPolicyBoundObj.agentInfo.getAgentPassword();
        accountNumber = myPolicyBoundObj.accountNumber;
        membershipTypeANI = aniAddress.getLine1();

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement 1.4.3.LD.22
     * @RequirementsLink <a href=
     * "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx">
     * "1.4.3.LD.22"</a>
     * @Description This test checks to make sure the address from the ANI is
     * not included as an option in the Location Address dropdown
     * list.
     * @DATE Nov 4, 2015
     */
    @Test(enabled = true, description = "Checks for ANI address in location dropdown on Locations page", dependsOnMethods = {
            "generateApp"})
    public void checkANIAddressOnLocation() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(userName, userPass, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuLocations();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderLocations myLoc = new GenericWorkorderLocations(driver);
        myLoc.clickPrimaryLocationEdit();
        myLoc.checkLocationAddressExistsInDropdown(membershipTypeANI);

    }

    private List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(SmallBusinessType.Apartments);
            this.add(SmallBusinessType.Offices);
            this.add(SmallBusinessType.Condominium);
            this.add(SmallBusinessType.SelfStorageFacilities);
        }
    };

    private List<LiabilityLimits> liabilityLimits = new ArrayList<LiabilityLimits>() {
        private static final long serialVersionUID = 1L;

        {
            this.add(LiabilityLimits.Three00_600_600);
            this.add(LiabilityLimits.Five00_1000_1000);
            this.add(LiabilityLimits.One000_2000_2000);
            this.add(LiabilityLimits.Two000_4000_4000);
        }
    };

}
