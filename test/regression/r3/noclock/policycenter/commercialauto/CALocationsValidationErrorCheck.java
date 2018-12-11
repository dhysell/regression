package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;

public class CALocationsValidationErrorCheck extends BaseTest {

    GeneratePolicy myPolObj;
    String userName;
    String password;
    String accountNumber;
    String policyNumber;
    String uwUserName;
    String dateString = DateUtils.dateFormatAsString("yyyyMMddHHmmssSS", new Date());
    final AddressInfo pniAddress = new AddressInfo(true);

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(enabled = true,
            description = "Creates an account through Quick Quote")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{

            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
                this.setVehicleType(VehicleType.PrivatePassenger);
            }});

            this.add(new Vehicle(VehicleType.PublicTransportation) {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
                this.setAge(driver, 30);
                this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};

        // GENERATE POLICY
        myPolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Wayne Tech")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Check)
                .build(GeneratePolicyType.QuickQuote);

        userName = myPolObj.agentInfo.getAgentUserName();
        password = myPolObj.agentInfo.getAgentPassword();
        accountNumber = myPolObj.accountNumber;

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }


    /**
     * @throws Exception
     * @Author bmartin
     * @Requirement CA087 Technically, but this is for zipcode, but still covers the same thing.
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20Auto%20Commercial/WCIC%20Commercial%20Auto-Product-Model.xlsx">WCIC Commercial Auto-Product-Model</a>
     * @Description This checks to see if Liability Limit is blank.
     * @DATE Mar 3, 2016
     */
    @Test(enabled = true,
            description = "This checks to see if Liability Limit is blank. ",
            dependsOnMethods = {"createPolicy"})
    public void checkLiabilityLimitExistence() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(userName, password, accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickWorkOrderbySuffix("001");

        SideMenuPC mySide = new SideMenuPC(driver);
        mySide.clickSideMenuLocations();

        checkForLocationsValidationError();
    }

    private void checkForLocationsValidationError() {
        if (null == By.xpath("//div[contains(text(), 'You must enter at least 1 row(s) of Locations')]")) {
            Assert.fail(driver.getCurrentUrl() +
                    "ERROR: Validation Error shows You must enter at least 1 row(s) of Locations");
        }
    }

}
