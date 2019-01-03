package repository.gw.helpers;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.Vehicle;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.SquireInlandMarine;

import java.util.ArrayList;
import java.util.Date;

public class GeneratePolicyHelper {

	private WebDriver driver;
    public GeneratePolicy generatePolicy;


    private String generateLoanNumber() {
      return "Loan".concat(StringsUtils.generateRandomNumberDigits(6));
    }

    public GeneratePolicyHelper(WebDriver driver) {
        this.driver = driver;
    }

    public GeneratePolicy generatePLSectionIIIPersonalAutoLinePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType) throws  Exception{
        if(firstName==null){
            firstName = "Sec III";
        }
        if(lastName==null){
            lastName = "Auto";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;

    }

    public GeneratePolicy generateMembershipOnlyPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Membership";
        }
        if(lastName==null){
            lastName = "Only";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Membership)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;

    }

    public GeneratePolicy generatePLSectionIAndIIPropertyAndLiabilityLinePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Sec I and II";
        }
        if(lastName==null){
            lastName = "Policy";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;
    }

   public GeneratePolicy generatePLSectionIAndIIWithAutoPLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Sec I II ";
        }
        if(lastName==null){
            lastName = "Auto";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;

    }


   public GeneratePolicy generateFarmAndRanchPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Farm And";
        }
        if(lastName==null){
            lastName = "Ranch";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
               generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withSquireEligibility(repository.gw.enums.SquireEligibility.FarmAndRanch)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;
    }


    public GeneratePolicy generateSquireWithUmbrellaPLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Squire With";
        }
        if(lastName==null){
            lastName = "Umbrella";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            repository.gw.generate.custom.PLPolicyLocationProperty myProperty = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            myProperty.getPropertyCoverages().getCoverageA().setLimit(350000);

            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> propertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
            propertyList.add(myProperty);
            repository.gw.generate.custom.PolicyLocation myLocation = new repository.gw.generate.custom.PolicyLocation(propertyList);
            ArrayList<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            locationList.add(myLocation);
            repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationList;

            repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
            mySquire.propertyAndLiability = myPropertyAndLiability;
            mySquire.propertyAndLiability.liabilitySection.setGeneralLiabilityLimit(repository.gw.enums.Property.SectionIIGeneralLiabLimit.Limit_500000_CSL);

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withSquire(mySquire)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            generatePolicy.addLineOfBusiness(repository.gw.enums.ProductLineType.PersonalUmbrella, repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }

    public GeneratePolicy generateInlandMarineLinePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "Sec I&II";
        }
        if(lastName==null){
            lastName = "InlandMarine";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            Date centerDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            locOnePropertyList.add(new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises));
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locOnePropertyList));
            repository.gw.generate.custom.SquireLiability liabilitySection = new repository.gw.generate.custom.SquireLiability();
            liabilitySection.getSectionIICoverageList().add(new SectionIICoverages(repository.gw.enums.Property.SectionIICoveragesEnum.CustomFarming, 1001, 0));

            repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = liabilitySection;

            repository.gw.generate.custom.SquirePersonalAuto myPersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
            myPersonalAuto.addVehicle(Vehicle.VehicleTypePL.ShowCar);

            repository.gw.generate.custom.SquireInlandMarine myInlandMarine = new SquireInlandMarine();
            repository.gw.generate.custom.PersonalPropertyScheduledItem myItem = new repository.gw.generate.custom.PersonalPropertyScheduledItem();
            myItem.setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType.Jewelry);
            myItem.setType(repository.gw.enums.PersonalPropertyScheduledItemType.Ring);
            myItem.setDescription("Diamonds");
            myItem.setAppraisalDate(DateUtils.dateAddSubtract(centerDate, repository.gw.enums.DateAddSubtractOptions.Year, -7));
            myItem.setPhotoUploadDate(DateUtils.dateAddSubtract(centerDate, repository.gw.enums.DateAddSubtractOptions.Year, -7));
            myItem.setLimit(25000);

            repository.gw.generate.custom.PersonalProperty myPersonalProperty = new repository.gw.generate.custom.PersonalProperty();
            myPersonalProperty.setType(repository.gw.enums.PersonalPropertyType.Jewelry);
            myPersonalProperty.setLimit(25000);
            myPersonalProperty.setDeductible(repository.gw.enums.PersonalPropertyDeductible.Ded5Perc);
            myPersonalProperty.getScheduledItems().add(myItem);
            myInlandMarine.personalProperty_PL_IM.add(myPersonalProperty);
            myInlandMarine.inlandMarineCoverageSelection_PL_IM.add(repository.gw.enums.InlandMarineTypes.InlandMarine.PersonalProperty);

            repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.Country);
            mySquire.propertyAndLiability = myPropertyAndLiability;
            mySquire.inlandMarine = myInlandMarine;

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.InlandMarineLinePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;

    }

    public GeneratePolicy generateStandardInlandMarinePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "StdInlandMarine";
        }
        if(lastName==null){
            lastName = "Marine";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            ArrayList<repository.gw.enums.InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<repository.gw.enums.InlandMarineTypes.InlandMarine>();

            inlandMarineCoverageSelection_PL_IM.add(repository.gw.enums.InlandMarineTypes.InlandMarine.FarmEquipment);
            repository.gw.generate.custom.IMFarmEquipmentScheduledItem scheduledItem1 = new repository.gw.generate.custom.IMFarmEquipmentScheduledItem("Circle Sprinkler","Manly Farm Equipment", 100000);
            ArrayList<repository.gw.generate.custom.IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<repository.gw.generate.custom.IMFarmEquipmentScheduledItem>();
            farmEquip.add(scheduledItem1);
            repository.gw.generate.custom.FarmEquipment imFarmEquip1 = new repository.gw.generate.custom.FarmEquipment(repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType.FarmEquipment, repository.gw.enums.CoverageType.BroadForm, repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible.FiveHundred, true, false, "Farm Equipment", farmEquip);
            ArrayList<repository.gw.generate.custom.FarmEquipment> allFarmEquip = new ArrayList<repository.gw.generate.custom.FarmEquipment>();
            allFarmEquip.add(imFarmEquip1);

            repository.gw.generate.custom.StandardInlandMarine myStandardInlandMarine = new repository.gw.generate.custom.StandardInlandMarine();
            myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
            myStandardInlandMarine.farmEquipment = allFarmEquip;

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.StandardIM)
                    .withLineSelection(repository.gw.enums.LineSelection.StandardInlandMarine)
                    .withStandardInlandMarine(myStandardInlandMarine)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;

    }

    public GeneratePolicy generateStandardFirePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "StandardFire";
        }
        if(lastName==null){
            lastName = "Policy";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            repository.gw.generate.custom.PLPolicyLocationProperty property = new repository.gw.generate.custom.PLPolicyLocationProperty();
            property.setpropertyType(repository.gw.enums.Property.PropertyTypePL.DwellingPremises);

            locOnePropertyList.add(property);
            repository.gw.generate.custom.PolicyLocation locToAdd = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList);
            locToAdd.setPlNumResidence(12);
            locToAdd.setPlNumAcres(12);
            locationsList.add(locToAdd);

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.StandardFire)
                    .withLineSelection(repository.gw.enums.LineSelection.StandardFirePL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withPolicyLocations(locationsList)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }

    public GeneratePolicy generateStandardLiabilityPLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(firstName==null){
            firstName = "StandardFire";
        }
        if(lastName==null){
            lastName = "Policy";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            repository.gw.generate.custom.PLPolicyLocationProperty property = new repository.gw.generate.custom.PLPolicyLocationProperty();
            property.setpropertyType(repository.gw.enums.Property.PropertyTypePL.DwellingPremises);

            locOnePropertyList.add(property);
            repository.gw.generate.custom.PolicyLocation locToAdd = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList);
            locToAdd.setPlNumResidence(12);
            locToAdd.setPlNumAcres(12);
            locationsList.add(locToAdd);

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.StandardLiability)
                    .withLineSelection(repository.gw.enums.LineSelection.StandardLiabilityPL)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
                    .withPolicyLocations(locationsList)
                    .withInsFirstLastName(firstName, lastName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }


    public GeneratePolicy generateFullyLienBilledSquirePLPolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType, ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(firstName==null){
            firstName = "FullLienBilled";
        }
        if(lastName==null){
            lastName = "Squire";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            repository.gw.generate.custom.AdditionalInterest additionalInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            additionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_All);
            additionalInterest.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIProperty);
            additionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);
            additionalInterest.setLoanContractNumber(generateLoanNumber());

            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            repository.gw.generate.custom.PLPolicyLocationProperty location1Property1 = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            location1Property1.setBuildingAdditionalInterest(additionalInterest);
            locOnePropertyList.add(location1Property1);

            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locOnePropertyList));

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withSquireEligibility(repository.gw.enums.SquireEligibility.City)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withPolicyLocations(locationsList)
                    .withPolTermLengthDays(termLengthDays)
                    .withInsFirstLastName(firstName, lastName)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;

    }

    public GeneratePolicy generateFullyLienBilledSquirePolicyWithTwoLiens(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType, ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(firstName==null){
            firstName = "FullyLien";
        }
        if(lastName==null){
            lastName = "Lien Billed";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationTwoPropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            repository.gw.generate.custom.AdditionalInterest locationOnePropertyOneAdditionalInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            locationOnePropertyOneAdditionalInterest.setLienholderNumber(lienHolderContacts.get(0).lienNumber);
            locationOnePropertyOneAdditionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
            locationOnePropertyOneAdditionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);

            repository.gw.generate.custom.PLPolicyLocationProperty locationOnePropertyOne = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            locationOnePropertyOne.setBuildingAdditionalInterest(locationOnePropertyOneAdditionalInterest);
            locationOnePropertyList.add(locationOnePropertyOne);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locationOnePropertyList));

            repository.gw.generate.custom.AdditionalInterest locationTwoPropertyOneAdditionalInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(1).companyName, lienHolderContacts.get(1).addresses.get(0));
            locationTwoPropertyOneAdditionalInterest.setLienholderNumber(lienHolderContacts.get(1).lienNumber);
            locationTwoPropertyOneAdditionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
            locationTwoPropertyOneAdditionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);

            repository.gw.generate.custom.PLPolicyLocationProperty locationTwoPropertyOne = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            locationTwoPropertyOne.setBuildingAdditionalInterest(locationTwoPropertyOneAdditionalInterest);
            locationTwoPropertyList.add(locationTwoPropertyOne);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locationTwoPropertyList, new repository.gw.generate.custom.AddressInfo(true)));

            repository.gw.generate.custom.SquireLiability liabilitySection = new repository.gw.generate.custom.SquireLiability();

            repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = liabilitySection;

            repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
            mySquire.propertyAndLiability = myPropertyAndLiability;

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withInsFirstLastName(firstName, lastName)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withPolTermLengthDays(termLengthDays)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }


    public GeneratePolicy generateInsuredAndLienBilledSquirePolicy(String firstName, String lastName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType, ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(firstName==null){
            firstName = "Insured and";
        }
        if(lastName==null){
            lastName = "Lien Billed";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
            ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationTwoPropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

            repository.gw.generate.custom.PLPolicyLocationProperty locationOnePropertyOne = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            locationOnePropertyList.add(locationOnePropertyOne);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locationOnePropertyList));

            repository.gw.generate.custom.AdditionalInterest locationTwoPropertyOneAdditionalInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            locationTwoPropertyOneAdditionalInterest.setLienholderNumber(lienHolderContacts.get(0).lienNumber);
            locationTwoPropertyOneAdditionalInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_All);
            locationTwoPropertyOneAdditionalInterest.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);

            repository.gw.generate.custom.PLPolicyLocationProperty locationTwoPropertyOne = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
            locationTwoPropertyOne.setBuildingAdditionalInterest(locationTwoPropertyOneAdditionalInterest);
            locationTwoPropertyList.add(locationTwoPropertyOne);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(locationTwoPropertyList, new repository.gw.generate.custom.AddressInfo(true)));

            repository.gw.generate.custom.SquireLiability liabilitySection = new repository.gw.generate.custom.SquireLiability();

            repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
            myPropertyAndLiability.locationList = locationsList;
            myPropertyAndLiability.liabilitySection = liabilitySection;

            repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
            mySquire.propertyAndLiability = myPropertyAndLiability;

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withInsFirstLastName(firstName, lastName)
                    .withProductType(repository.gw.enums.ProductLineType.Squire)
                    .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                    .withPolTermLengthDays(termLengthDays)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }

    /*
    *
    *
    * BOP Policies
    *
    *
    * */


    public GeneratePolicy generateBasicBOPPolicy(String companyName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType)  throws  Exception{
        if(companyName==null){
            companyName = "BOPInsuredOnlyPolicy";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
                    .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                    .withInsCompanyName(companyName)
                    .withPolTermLengthDays(termLengthDays)
                    .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
                    .withBusinessownersLine(new repository.gw.generate.custom.PolicyBusinessownersLine(repository.gw.enums.BusinessownersLine.SmallBusinessType.StoresRetail))
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
            return generatePolicy;
    }

    public GeneratePolicy generateFullyLienBilledBOPPolicy(String companyName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType , ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(companyName==null){
            companyName = "FullyLienBilledBOPPolicy";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            ArrayList<repository.gw.generate.custom.AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <repository.gw.generate.custom.AdditionalInterest>();
            ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locOneBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();
            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();

            repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
            loc1Bldg1.setClassClassification("storage");

            repository.gw.generate.custom.AdditionalInterest loc1Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            loc1Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_All);
            loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
            loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

            locOneBuildingList.add(loc1Bldg1);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(new repository.gw.generate.custom.AddressInfo(), locOneBuildingList));

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
                    .withInsCompanyName(companyName)
                    .withBusinessownersLine(new repository.gw.generate.custom.PolicyBusinessownersLine(repository.gw.enums.BusinessownersLine.SmallBusinessType.StoresRetail))
                    .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
                    .withPolicyLocations(locationsList)
                    .withPolTermLengthDays(termLengthDays)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }


    public GeneratePolicy generateInsuredAndLienBilledBOPPolicy(String companyName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType , ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(companyName==null){
            companyName = "InsuredAndLienBilledBOP";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }
            ArrayList<repository.gw.generate.custom.AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <repository.gw.generate.custom.AdditionalInterest>();
            ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locOneBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();
            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();

            repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
            loc1Bldg1.setClassClassification("storage");

            repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg2 = new repository.gw.generate.custom.PolicyLocationBuilding();
            loc1Bldg2.setClassClassification("storage");

            repository.gw.generate.custom.AdditionalInterest loc1Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            loc1Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
            loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
            loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

            locOneBuildingList.add(loc1Bldg1);
            locOneBuildingList.add(loc1Bldg2);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(new repository.gw.generate.custom.AddressInfo(), locOneBuildingList));

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
                    .withInsCompanyName(companyName)
                    .withBusinessownersLine(new repository.gw.generate.custom.PolicyBusinessownersLine(repository.gw.enums.BusinessownersLine.SmallBusinessType.StoresRetail))
                    .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
                    .withPolicyLocations(locationsList)
                    .withPolTermLengthDays(termLengthDays)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

            return generatePolicy;
    }

    public GeneratePolicy generateFullyLienBilledWithTwoLiensPolicy(String companyName, Integer termLengthDays, repository.gw.enums.PaymentPlanType paymentPlanType, repository.gw.enums.PaymentType downPaymentType , ArrayList<GenerateContact> lienHolderContacts)  throws  Exception{
        if(companyName==null){
            companyName = "InsuredAndLienBilledBOP";
        }
        if(paymentPlanType==null){
            paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
        }
        if(downPaymentType==null){
            downPaymentType = repository.gw.enums.PaymentType.getRandom();
        }
        if(termLengthDays==null){
            termLengthDays = 365;
        }

            ArrayList<repository.gw.generate.custom.AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <repository.gw.generate.custom.AdditionalInterest>();
            ArrayList<repository.gw.generate.custom.AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <repository.gw.generate.custom.AdditionalInterest>();
            ArrayList<repository.gw.generate.custom.PolicyLocationBuilding> locOneBuildingList = new ArrayList<repository.gw.generate.custom.PolicyLocationBuilding>();
            ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();

            repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg1 = new repository.gw.generate.custom.PolicyLocationBuilding();
            loc1Bldg1.setClassClassification("storage");

            repository.gw.generate.custom.PolicyLocationBuilding loc1Bldg2 = new repository.gw.generate.custom.PolicyLocationBuilding();
            loc1Bldg2.setClassClassification("storage");

            repository.gw.generate.custom.AdditionalInterest loc1Bld1AddInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(0).companyName, lienHolderContacts.get(0).addresses.get(0));
            loc1Bld1AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
            loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
            loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);

            repository.gw.generate.custom.AdditionalInterest loc1Bld2AddInterest = new repository.gw.generate.custom.AdditionalInterest(lienHolderContacts.get(1).companyName, lienHolderContacts.get(1).addresses.get(0));
            loc1Bld2AddInterest.setAdditionalInterestBilling(repository.gw.enums.AdditionalInterestBilling.Bill_Lienholder);
            loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
            loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

            locOneBuildingList.add(loc1Bldg1);
            locOneBuildingList.add(loc1Bldg2);
            locationsList.add(new repository.gw.generate.custom.PolicyLocation(new repository.gw.generate.custom.AddressInfo(), locOneBuildingList));

            generatePolicy = new GeneratePolicy.Builder(driver)
                    .withInsPersonOrCompany(repository.gw.enums.ContactSubType.Company)
                    .withInsCompanyName(companyName)
                    .withBusinessownersLine(new repository.gw.generate.custom.PolicyBusinessownersLine(repository.gw.enums.BusinessownersLine.SmallBusinessType.StoresRetail))
                    .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
                    .withPolicyLocations(locationsList)
                    .withPolTermLengthDays(termLengthDays)
                    .withPaymentPlanType(paymentPlanType)
                    .withDownPaymentType(downPaymentType)
                    .build(GeneratePolicyType.PolicyIssued);

            return generatePolicy;

    }



}
