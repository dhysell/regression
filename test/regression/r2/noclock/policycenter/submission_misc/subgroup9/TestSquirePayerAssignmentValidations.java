package regression.r2.noclock.policycenter.submission_misc.subgroup9;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;

/**
 * @Author skandibanda
 * @Requirement :DE4137 : Validation on Payer assignment is triggering incorrectly
 * @DATE Feb 17, 2017
 */
public class TestSquirePayerAssignmentValidations extends BaseTest {
    private GeneratePolicy myPolicyObjPL;

    private WebDriver driver;


    @Test
    public void testGenerateSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
        PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

        AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
        loc1Bldg2AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Bldg2AddInterest.setNewContact(CreateNew.Do_Not_Create_New);
        loc1Bldg2AddInterest.setFirstMortgage(true);
        loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);


        loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);

        PLPolicyLocationProperty loc1Bldg2 = new PLPolicyLocationProperty();
        loc1Bldg2.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg2.setpropertyType(PropertyTypePL.Shed);

        PLPolicyLocationProperty loc1Bldg3 = new PLPolicyLocationProperty();
        loc1Bldg3.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg2AdditionalInterests);
        loc1Bldg3.setpropertyType(PropertyTypePL.Barn);

        locOnePropertyList.add(loc1Bldg1);
        locOnePropertyList.add(loc1Bldg2);
        locOnePropertyList.add(loc1Bldg3);
        locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Squire", "AdditionalInterest")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    public void testVerifyPayerAssignmentErrorMessage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuPayerAssignment();
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
        String payerName = myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getCompanyName();
        payerName = payerName.substring(0, payerName.length() - 5);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 1, payerName, true, false);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 2, payerName, true, false);
        payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(1, 3, payerName, true, false);
        payerAssignment.setPayerAssignmentBillLiabilityCoverages("General Liability", true, false, payerName);
        payerAssignment.setPayerAssignmentBillMembershipDues(myPolicyObjPL.pniContact.getFirstName(), true, false, payerName);
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        ErrorHandlingHelpers errorHandlingHelpers = new ErrorHandlingHelpers(driver);
        if (errorHandlingHelpers.errorExists()) {
            if (errorHandlingHelpers.getErrorMessage().contains("The current payer assigned to Section II must also be a current payer assigned to at least one residence on Section I. (SQ054)")) {
                Assert.fail("when General Liability Current payer match the Section I Current payer then error message :" + errorHandlingHelpers.getErrorMessage() + "Should not Exists");

            }
        }
    }
}
