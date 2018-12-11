package scratchpad.jon.mine;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

@SuppressWarnings("unused")
public class TempTestSearch extends BaseTest {

    public TempTestSearch() {
        super();
    }

    private String username;
    private String password;

    @BeforeClass
    public void beforeClass() {

    }

    @AfterClass
    public void afterMethod() throws Exception {
//
    }

    @Test
    public void testDesktopMenu() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
//		username = "su";
//		password = "gw";
//		Login lp = new Login(driver);
//		lp.login(username, password);


        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("The Generate")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Credit_Debit)
                .build(GeneratePolicyType.PolicyIssued);


    }
}
















