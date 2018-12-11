package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto;

/**
 * @Author jlarsen
 * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20Auto%20Commercial/WCIC%20Commercial%20Auto-Product-Model.xlsx
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description test user gets blocking messages on Qualification questions answered incorrectly.
 * @DATE Apr 1, 2016
 */
@QuarantineClass
public class CAQualificationQuestions extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;

    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }
        };

        final ArrayList<Contact> personList = new ArrayList<Contact>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                    this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
                }});
            }
        };

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};

        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("AvailablityRules")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void verifyQualificationBlockingMessages() throws Exception {
        boolean testFailed = false;
        String failureString = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();

        GenericWorkorder genwo = new GenericWorkorder(driver);

        GenericWorkorderQualification_CommercialAuto qual = new GenericWorkorderQualification_CommercialAuto(driver);
        qual.clickCA_AutoWithOutDrivers(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured leases autos to others without drivers. Risk is not eligible please contact Brokerage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Does applicant/insured lease autos to others without drivers? was answered incorrectly \n";
        }
        qual.clickCA_AutoWithOutDrivers(false);

        qual.clickCA_InterchangeAgreement(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured has trailer interchange agreements. Risk is not eligible please contact Brokerage for coverage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Does applicant/insured have any trailer interchange agreements? was answered incorrectly \n";
        }
        qual.clickCA_InterchangeAgreement(false);

        qual.clickCA_IndustrialUseVehicles(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured is involved in high risk activities. Please contact Brokerage for coverage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Does applicant/insured do any of the following: Logging or lumbering, auto dismantling, hauling of chemicals, hauling of iron and steel, mining, quarrying, tow truck for rotations, armored car delivery, or building wrecking operation? was answered incorrectly \n";
        }
        qual.clickCA_IndustrialUseVehicles(false);

        qual.clickCA_InterstateCommerce(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured requires federal filings. Risk is not eligible please contact Brokerage for assistance.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Are any of the following permits held from the Interstate Commerce Commission: BMC 91 X, BMC 91, and/or BMC 34? was answered incorrectly \n";
        }
        qual.clickCA_InterstateCommerce(false);

        qual.clickCA_HazardousMaterialWarning(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured transports hazardous materials. Risk is not eligible please contact Brokerage for coverage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Do any automobiles or trailers require hazardous material warnings or placards? was answered incorrectly \n";
        }
        qual.clickCA_HazardousMaterialWarning(false);

        qual.clickCA_TransportTheFollowing(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/Insured transports unacceptable cargo. Risk is not eligible please contact Brokerage for coverage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User when Do you transport any of the following: Explosives, logs, fertilizer, heavy equipment or autos, petroleum, any hazardous, or flammable substance? was answered incorrectly \n";
        }
        qual.clickCA_TransportTheFollowing(false);

        qual.clickCA_ServiceCommision(true);
        qual.check_OtherStates(true);
        genwo.clickPolicyChangeNext();
        genwo.clickGenericWorkorderQuote();
        if (!validateMessage(qual.getValidationMessages(), "Applicant/insured requires PUC in a state that we cannot provide. Risk is not eligible please contact Brokerage.")) {
            testFailed = true;
            failureString = failureString + "Failed to block User whenApplicant/insured requires PUC in a state that we cannot provide. Risk is not eligible please contact Brokerage. was answered incorrectly \n";
        }
        qual.clickCA_ServiceCommision(false);

        throwException(testFailed, failureString);

    }


    private boolean validateMessage(List<WebElement> messagesRecieved, String correctErrorMessage) {
        for (WebElement message : messagesRecieved) {
            if (message.getText().trim().equalsIgnoreCase(correctErrorMessage)) {
                return true;
            }
        }
        return false;
    }

    private void throwException(boolean testFailed, String failureString) {
        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }

    }


}















