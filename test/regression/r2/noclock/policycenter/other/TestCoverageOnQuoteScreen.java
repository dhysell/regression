package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;

/**
 * @Author bmartin
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description This test sets up: Employee Dishonesty Limit Hired Auto BP
 * 04 04 Non-Owned Auto Liability PB 04 04 Money and Securities
 * It then checks that what is displayed is only the Limits
 * Premium.
 * @DATE Oct 8, 2015
 * @throws Exception
 */
@QuarantineClass
public class TestCoverageOnQuoteScreen extends BaseTest {

    GeneratePolicy myPolicyBoundObj = null;
    String userName;
    String userPass;
    String accountNumber;
    String policyNumber;

    private WebDriver driver;

    @Test
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

        PolicyLocationBuilding bldgToAdd = new PolicyLocationBuilding();
        locOneBuildingList.add(bldgToAdd);

        PolicyLocationAdditionalCoverages pladdtlcovs = new PolicyLocationAdditionalCoverages();
        pladdtlcovs.setMoneyAndSecuritiesCoverage(true);
        pladdtlcovs.setMoneySecNumMessengersCarryingMoney(1);
        pladdtlcovs.setMoneySecDepositDaily(true);
        pladdtlcovs.setMoneySecHowOftenDeposit("Once");
        pladdtlcovs.setMoneySecOnPremisesLimit(15000);
        pladdtlcovs.setMoneySecOffPremisesLimit(15000);

        PolicyLocation ploc = new PolicyLocation(new AddressInfo(), locOneBuildingList);
        ploc.setAdditionalCoveragesStuff(pladdtlcovs);
        locationsList.add(ploc);

        PolicyBusinessownersLineAdditionalCoverages plocAddtlCovs = new PolicyBusinessownersLineAdditionalCoverages(false, true);
        plocAddtlCovs.setEmployeeDishonestyCoverage(true);
        plocAddtlCovs.setEmpDisLimit(EmpDishonestyLimit.Dishonest25000);
        plocAddtlCovs.setEmpDisNumCoveredEmployees(20);
        plocAddtlCovs.setEmpDisReferencesChecked(true);
        plocAddtlCovs.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Monthly);
        plocAddtlCovs.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.CPA);
        plocAddtlCovs.setEmpDisDiffWriteThanReconcile(true);
        plocAddtlCovs.setEmpDisLargeCheckProcedures(true);
        plocAddtlCovs.setHiredAutoCoverage(true);
        plocAddtlCovs.setHiredAutoOwnedAuto(false);
        plocAddtlCovs.setNonOwnedAutoLiabilityCoverage(true);
        plocAddtlCovs.setNonOwnedAutoNonCompanyVehicle(true);

        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1))));
        boLine.setLiabilityLimits(liabilityLimits.get(NumberUtils.generateRandomNumberInt(0, (liabilityLimits.size() - 1))));
        boLine.setAdditionalCoverageStuff(plocAddtlCovs);

        this.myPolicyBoundObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Bruce", "Wayne")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.FullApp);

        userName = myPolicyBoundObj.agentInfo.getAgentUserName();
        userPass = myPolicyBoundObj.agentInfo.getAgentPassword();
        accountNumber = myPolicyBoundObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");

        new Login(driver).loginAndSearchAccountByAccountNumber(userName, userPass, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySideMenu = new SideMenuPC(driver);
        mySideMenu.clickSideMenuQuote();

        GenericWorkorderQuote myQuote = new GenericWorkorderQuote(driver);
        boolean exists = false;
        for (int i = 0; i < viewRemoved.size(); i++) {
            exists = myQuote.checkDescriptionOptions(viewRemoved.get(i));
            if (exists) {
                break;
            }
        }

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

    private List<String> viewRemoved = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            this.add("Number of Covered Employees");
            this.add("Are all references checked and previous employmentverified?");
            this.add("How often are audits conducted?");
            this.add("Who performs these audits?");
            this.add("Does applicant have different employees write checks from those who reconcile bank statements?");
            this.add("Are there procedures in place for large checks");
            this.add("Are there procedures in place for large checks to have multiple signatures?");
            this.add("Does Applicant have an Owned Auto?");
            this.add("Is Delivery Service with a Non-Company Vehicle?");
            this.add("Number of messengers carrying Money and Securities");
            this.add("Do you deposit daily?");
            this.add("How often?");
            this.add("Money & Securities On Premises Limit");
            this.add("Money & Securities Off Premises Limit");
        }
    };
}
