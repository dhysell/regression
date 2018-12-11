package scratchpad.sai.sgunda;

import repository.gw.enums.GenerateContactType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class SquirePolLHBilled extends BaseTest {


    private GeneratePolicy myPolicyObj;

    private String loanNumber = "ThisIsLoan";
    private WebDriver driver;

    @Test
    public void SquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
        rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);
        driver.quit();

        AdditionalInterest additionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
        additionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
        additionalInterest.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIProperty);//THIS I BELEIVE IS SET IN FIELD INTEGRITY AND DEFAULTS
        additionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);
        additionalInterest.setLoanContractNumber(loanNumber);

        ArrayList<repository.gw.enums.LineSelection> productLines = new ArrayList<repository.gw.enums.LineSelection>();
        productLines.add(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL);
        productLines.add(repository.gw.enums.LineSelection.PersonalAutoLinePL);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        ArrayList<PLPolicyLocationProperty> locTwoPropertyList = new ArrayList<PLPolicyLocationProperty>();


        PLPolicyLocationProperty location1Property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        location1Property1.setBuildingAdditionalInterest(additionalInterest);
        locOnePropertyList.add(location1Property1);

        PLPolicyLocationProperty location2Property2 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locTwoPropertyList.add(location2Property2);

        locationsList.add(new PolicyLocation(locOnePropertyList));
        locationsList.add(new PolicyLocation(locTwoPropertyList));

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withSquireEligibility(repository.gw.enums.SquireEligibility.City)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolicyLocations(locationsList)
                .withPolTermLengthDays(100)
                .withInsFirstLastName("Reinst", "Ins")
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual)
                .withDownPaymentType(repository.gw.enums.PaymentType.ACH_EFT)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        driver.quit();

        cf.setCenter(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber("skane", "gw", myPolicyObj.accountNumber);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(new GuidewireHelpers(driver).getPolicyNumber(myPolicyObj));

    }

}
