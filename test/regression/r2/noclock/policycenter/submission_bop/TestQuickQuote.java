package regression.r2.noclock.policycenter.submission_bop;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
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

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

@QuarantineClass
public class TestQuickQuote extends BaseTest {

    public GeneratePolicy myPolicyObjInsuredOnly = null;
    public GeneratePolicy myPolicyObjInsuredAndLien = null;

    private WebDriver driver;

    @Test
    public void testBasicQuickQuoteInsuredOnly() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredOnly = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("PolicyInsuredOnly")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.getRandom())
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test
    public void testBasicQuickQuoteInsuredAndLien() throws Exception {

        // Get Basic 2 Location with 1 Building Each, 1 Building's Lien With
        // Charges
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        ArrayList<PolicyLocationBuilding> locTwoBuildingList = new ArrayList<PolicyLocationBuilding>();
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc2Bldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        PolicyLocationBuilding loc2Bldg1 = new PolicyLocationBuilding();
        loc2Bldg1.setAdditionalInterestList(loc2Bldg1AdditionalInterests);
        locTwoBuildingList.add(loc2Bldg1);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
        locationsList.add(new PolicyLocation(new AddressInfo(), locTwoBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObjInsuredAndLien = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("PolicyInsuredLien")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.getRandom())
                .build(GeneratePolicyType.QuickQuote);
    }

}
