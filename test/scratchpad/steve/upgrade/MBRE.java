package scratchpad.steve.upgrade;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.BatchProcess;
import repository.gw.enums.Building.BusinessIncomeOrdinaryPayroll;
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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.GuidewireHelpers;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;

//@Listeners(listeners.Listener.class)
public class MBRE extends BaseTest {

    private WebDriver driver;


    @Test
    public void testNewPerson() throws Exception {

//		Run Monthly MBRE batch job to clear out any previous policies
//		Create Policy with Equipment Breakdown

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuildingAdditionalCoverages addCoverages = new PolicyLocationBuildingAdditionalCoverages();
        addCoverages.setBusinessIncomeOrdinaryPayrollCoverage(true);
        addCoverages.setBusinessIncomeOrdinaryPayrollType(BusinessIncomeOrdinaryPayroll.Days90);
        PolicyLocationBuilding building = new PolicyLocationBuilding();
        building.setAdditionalCoveragesStuff(addCoverages);
        locOneBuildingList.add(building);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GeneratePolicy myPolicyObjMBRE = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("MBRE")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        System.out.println("The Account Number is " + myPolicyObjMBRE.accountNumber);
        System.out.println();

        new GuidewireHelpers(driver).logout();

//		Create Policy without Equipment Breakdown
        PolicyBusinessownersLine businessownersLine = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
        businessownersLine.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, false));
        GeneratePolicy myPolicyObjNoMBRE = new GeneratePolicy.Builder(driver)
                .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("No MBRE")
                .withPolOrgType(OrganizationType.Partnership)
                .withBusinessownersLine(businessownersLine)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println("The Account Number is " + myPolicyObjNoMBRE.accountNumber);


        //Run Intran
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Intran_File_Process);

//		Run Monthly MBRE batch job
        batchHelpers.runBatchProcess(BatchProcess.Mutual_Boiler_Re_Monthly);
		
/*		The file will be found on the ftp://fbms2077
		login is mbroker(Environment)  so mbrokerqa for todays testing.
		Password is zaq12wsx
		
*/
//		Interrogate Monthly file for Monthly File info.  

//		Ensure Policy without Equipment Breakdown does not exist in File.

//		Move Clock to April 1st / July 1st / Oct 1st / Jan 1st depending on Date

//		Run Quarterly Batch Job.
        batchHelpers.runBatchProcess(BatchProcess.Mutual_Boiler_Re_Quarterly);

//		Check Quarterly File for Policy with Equipment Breakdown

//		Check Quarterly File for Policy without Equipment Breakdown

//		Separate CC test will be created with Denver.

    }
}
