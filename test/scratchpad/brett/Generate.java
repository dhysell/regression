package scratchpad.brett;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
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
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class Generate extends BaseTest {

    private WebDriver driver;

    @Test
    public void testPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);

        //PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        PolicyBusinessownersLineAdditionalCoverages myLineAddCov = new PolicyBusinessownersLineAdditionalCoverages(false, false);
        myLineAddCov.setEmployeeDishonestyCoverage(true);
        myLineAddCov.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.Private_Auditing_Firm);
        myLineAddCov.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Annually);
        myLineAddCov.setEmpDisLimit(EmpDishonestyLimit.Dishonest5000);
        PolicyBusinessownersLine myline = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        myline.setAdditionalCoverageStuff(myLineAddCov);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();

        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        AddressInfo addIntTest = new AddressInfo();
        addIntTest.setLine1("PO Box 711");
        addIntTest.setCity("Pocatello");
        addIntTest.setState(State.Idaho);
        addIntTest.setZip("83204");//-0711

		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Additional Interest", addIntTest);
        loc1Bld1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc1Bld1AddInterest.setAddress(addIntTest);
        loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
        loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

        PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
        loc1Bldg2.setYearBuilt(2010);
        loc1Bldg2.setClassClassification("storage");

        AddressInfo macuAddress = new AddressInfo();
        macuAddress.setLine1("PO Box 691010");
        macuAddress.setCity("San Antonio");
        macuAddress.setState(State.Texas);
        macuAddress.setZip("78269-1010");

		AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest("Mountain America", macuAddress);
        loc1Bldg2AddInterest.setAddress(macuAddress);
        loc1Bldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
        loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

        locOneBuildingList.add(loc1Bldg1);
        locOneBuildingList.add(loc1Bldg2);

        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
        loc2Bldg1.setYearBuilt(yearTest);
        loc2Bldg1.setClassClassification("storage");

        AddressInfo wellsAddress2 = new AddressInfo();
        wellsAddress2.setLine1("421 N Orchard St");
        wellsAddress2.setCity("Boise");
        wellsAddress2.setState(State.Idaho);
        wellsAddress2.setZip("83706");//-1976

		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Wells Fargo", wellsAddress2);
        loc2Bldg1AddInterest.setAddress(wellsAddress2);
        loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);

        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Test Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(myline)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickAccountArrow();

        BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
        topMenuStuff.menuAccountSearchAccountByAccountNumber(myPolicyObj.accountNumber);


        System.out.println("insLastName: " + myPolicyObj.pniContact.getLastName());
        System.out.println("insFirstName: " + myPolicyObj.pniContact.getFirstName());
        System.out.println("insCompanyName: " + myPolicyObj.pniContact.getCompanyName());
        System.out.println("accountNumber: " + myPolicyObj.accountNumber);
        System.out.println("effectiveDate: " + myPolicyObj.busOwnLine.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObj.busOwnLine.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObj.busOwnLine.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObj.busOwnLine.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObj.busOwnLine.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObj.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObj.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObj.busOwnLine.getPolicyNumber());

    }

}
