package scratchpad.brett;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.bc.topmenu.BCTopMenu;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

public class GenerateBasicPolicyCPPAuto extends BaseTest {
    public ARUsers arUser;
    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void testPolicy() throws Exception {
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
//			this.setCommercialAutoLine(new CPPCommercialAutoLine(LiabilityLimit.OneMillion1M, DeductibleLiabilityCoverage.TwoThousandFiveHundred2500));
            this.setCommercialAutoLine(new CPPCommercialAutoLine());
            this.setVehicleList(new ArrayList<Vehicle>() {{
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }});
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(new ArrayList<Contact>() {{
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                }});
            }});
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        GeneratePolicy myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CPP Policy Test")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        driver.quit();
        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        ARUsers arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Commercial);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickAccountArrow();

        BCTopMenuAccount topMenuStuff = new BCTopMenuAccount(driver);
        topMenuStuff.menuAccountSearchAccountByAccountNumber(myPolicyObjCPP.accountNumber);


        System.out.println("insLastName: " + myPolicyObjCPP.pniContact.getLastName());
        System.out.println("insFirstName: " + myPolicyObjCPP.pniContact.getFirstName());
        System.out.println("insCompanyName: " + myPolicyObjCPP.pniContact.getCompanyName());
        System.out.println("accountNumber: " + myPolicyObjCPP.accountNumber);
        System.out.println("effectiveDate: " + myPolicyObjCPP.commercialPackage.getEffectiveDate());
        System.out.println("expirationDate: " + myPolicyObjCPP.commercialPackage.getExpirationDate());
        System.out.println("totalPremium: " + myPolicyObjCPP.commercialPackage.getPremium().getTotalNetPremium());
        System.out.println("insuredPremium: " + myPolicyObjCPP.commercialPackage.getPremium().getInsuredPremium());
        System.out.println("memberDues: " + myPolicyObjCPP.commercialPackage.getPremium().getMembershipDuesAmount());
        System.out.println("downPaymentAmount: " + myPolicyObjCPP.commercialPackage.getPremium().getDownPaymentAmount());
        System.out.println("paymentPlanType: " + myPolicyObjCPP.paymentPlanType.getValue());
        System.out.println("downPaymentType: " + myPolicyObjCPP.downPaymentType.getValue());
        System.out.println("policyNumber: " + myPolicyObjCPP.commercialPackage.getPolicyNumber());
    }
}
