package scratchpad.rusty;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInsuredRole;
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
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;


public class TemplateTest extends BaseTest {

    //	private GeneratePolicy myPolicyObj = null;
    public GeneratePolicy myPolicyObj2 = null;
    public GeneratePolicy myPolicyObj3 = null;

    private WebDriver driver;

    @Test
    public void testBasicIssuanceFromBound() throws Exception {

//		TestBind bound = new TestBind();
//		this.myPolicyObj = bound.myPolicyObjInsuredOnly;
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj2.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test
    public void testBasicIssuanceCustomForPolicyChange() throws Exception {

        // customizing location and building
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        building.setClassClassification("");
        building.setClassCode("63611");
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        // customizing the BO Line, including additional insureds
        AddressInfo address = new AddressInfo();
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.Motels);
        ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
        additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "dafigudhfiuhdafg", AdditionalInsuredRole.CertificateHolderOnly, address));
        boLine.setAdditonalInsuredBOLineList(additonalInsuredBOLineList);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj3 = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("TestPolicyChanges")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

}

