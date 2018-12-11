package scratchpad.jon.userTemplates;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import helpers.DateUtils;

public class CreateTestingPolicies extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private int numberToCreate = 12;
    private List<String> accountNumbers = new ArrayList<String>();

    private WebDriver driver;


    @Test
    public void createPolicies() throws Exception {

        for (int i = 0; i <= numberToCreate; i++) {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter, "UAT");
            driver = buildDriver(cf);
            SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
            coverages.setUnderinsured(false);
            coverages.setAccidentalDeath(true);

            // driver
            ArrayList<Contact> driversList = new ArrayList<Contact>();
            Contact person = new Contact("Test", "PropClassReg", Gender.Male, DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
            person.setMaritalStatus(MaritalStatus.Married);
            person.setRelationToInsured(RelationshipToInsured.Insured);
            person.setOccupation("Software");
            driversList.add(person);

            // Vehicle
            ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
            Vehicle toAdd = new Vehicle();
            toAdd.setEmergencyRoadside(true);
            vehicleList.add(toAdd);

            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
            PLPolicyLocationProperty myLocationProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
            AdditionalInterest myAdditionalInterest = new AdditionalInterest();
            myAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
            myAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
            myLocationProperty.getAdditionalInterestList().add(myAdditionalInterest);
            locOnePropertyList.add(myLocationProperty);
            PLPolicyLocationProperty myLocationPropertyINS = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
            locOnePropertyList.add(myLocationPropertyINS);
            PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
            locToAdd.setPlNumAcres(11);
            locToAdd.setPlNumResidence(2);
            locationsList.add(locToAdd);

            SquireLiability myLiab = new SquireLiability();
            myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

            SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
            squirePersonalAuto.setCoverages(coverages);
            squirePersonalAuto.setVehicleList(vehicleList);
            squirePersonalAuto.setDriversList(driversList);

            SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = myLiab;


            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.squirePA = squirePersonalAuto;
            mySquire.propertyAndLiability = myPropertyAndLiability;

            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(/*LineSelection.PersonalAutoLinePL, */LineSelection.PropertyAndLiabilityLinePL)
                    .build(GeneratePolicyType.PolicyIssued);

            accountNumbers.add(myPolicyObject.accountNumber);

        }

        for (String account : accountNumbers) {
            System.out.println(account);
        }

    }


}
