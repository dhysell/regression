package repository.gw.generate;


import org.testng.Assert;
import repository.gw.enums.SquirePersonalAutoCoverages;
import repository.gw.enums.*;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.*;
import repository.gw.helpers.GuidewireHelpers;

import java.util.ArrayList;

public class FieldIntegrityAndDefaults_Squire {

	public void checkClassFieldIntegrityAndDefaults(GeneratePolicy policy) {
        GuidewireHelpers gwh = new GuidewireHelpers(policy.getWebDriver());
		if(policy.squire.isFarmAndRanch()) {
			if(gwh.getTypeToGenerate(policy).equals(repository.gw.enums.GeneratePolicyType.QuickQuote)) {
				System.out.println("SQUIRE POLICY IS A FARM AND RANCH WITH GENERATE TYPE OF QUICK QUOTE, THIS IS NOT ALLOWED SO IT HAS BEEN FORCED TO FULL APP");
				gwh.setTypeToGenerate(policy, GeneratePolicyType.FullApp);
			}
		}


        for (repository.gw.enums.LineSelection line : policy.lineSelection) {
            switch (line) {
			case Businessowners:
				break;
			case CommercialAutoLineCPP:
				break;
			case CommercialPropertyLineCPP:
				break;
			case GeneralLiabilityLineCPP:
				break;
			case InlandMarineLineCPP:
				break;
			case InlandMarineLinePL:
				if (policy.squire.inlandMarine.farmEquipment != null) {
					for (FarmEquipment farmEquip : policy.squire.inlandMarine.farmEquipment) {
						for (AdditionalInterest addint : farmEquip.getAdditionalInterests()) {
							addint.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLFarmEquipment);
						}
					}
				}
				break;
			case PersonalAutoLinePL:
				if (policy.squire.squirePA.isPrimaryInsuredAsDriver()) {
					Contact newPerson = new Contact();
					newPerson.setAddress(policy.pniContact.getAddress());
					newPerson.setFirstName(policy.pniContact.getFirstName());
					newPerson.setMiddleName(policy.pniContact.getMiddleName());
					newPerson.setLastName(policy.pniContact.getLastName());
					newPerson.setFullName(newPerson.getFirstName() + " " + newPerson.getLastName());
					newPerson.setDob(policy.getWebDriver(), policy.pniContact.getDob());
					newPerson.setMaritalStatus(policy.pniContact.getMaritalStatus());
					newPerson.setRelationToInsured(RelationshipToInsured.Insured);
					newPerson.setDriversLicenseNum(policy.pniContact.getDriversLicenseNum());
					newPerson.setStateLicenced(policy.pniContact.getStateLicenced());
					newPerson.setSR22Charges(policy.hasSR22ChargesForPrimaryNamedInsured);
					newPerson.setContactIsPNI(true);
					policy.squire.squirePA.addToDriversList(newPerson);

					if (policy.squire.squirePA.areAllVehiclesUnassignedDrivers()) {
						Vehicle toMarkAsAssigned = policy.squire.squirePA.getVehicleListUnassignedDrivers().get(0);
						toMarkAsAssigned.setDriverPL(newPerson);
					}
				}
				
				for(Vehicle vehicle : policy.squire.squirePA.getVehicleList()) {
					if(vehicle.getGaragedAt() == null) {
						vehicle.setGaragedAt(policy.pniContact.getAddress());
					}
				}
				break;
			case PropertyAndLiabilityLinePL:
				if (policy.squire.propertyAndLiability.locationList == null || policy.squire.propertyAndLiability.locationList.isEmpty()) {
					Assert.fail("MUST HAVE LOCATION ON A SQUIRE POLICY");
				}
                    for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
                        for (PLPolicyLocationProperty property : location.getPropertyList()) {
						property.setAddress(location.getAddress());
						for (AdditionalInterest additionalint : property.getBuildingAdditionalInterest()) {
							additionalint.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIProperty);
						}
					}
				}
				String locationAddress = "";
				int lcv = 0;
				for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
					if (location.getAddress().getLine1().equals(locationAddress)) {
						Assert.fail("You must have a different address for each location. Please specify a different address for each location.");
					}
					if (lcv == 0) {
						locationAddress = location.getAddress().getLine1();
						lcv++;
				}
                    }
				//Verify HabitatForHumanity Validation rule. If more than one habitable building is present, all buildings must have an owner assigned.
				ArrayList<Property.PropertyTypePL> propertyTypeList = new ArrayList<Property.PropertyTypePL>();
				ArrayList<Property.PropertyTypePL> habitablePropertyTypeListForPolicy = new ArrayList<Property.PropertyTypePL>();
				ArrayList<Property.PropertyTypePL> habitablePropertyTypeList = new ArrayList<Property.PropertyTypePL>();
				habitablePropertyTypeList.add(Property.PropertyTypePL.ResidencePremises);
				habitablePropertyTypeList.add(Property.PropertyTypePL.DwellingPremises);
				habitablePropertyTypeList.add(Property.PropertyTypePL.VacationHome);
				habitablePropertyTypeList.add(Property.PropertyTypePL.CondominiumDwellingPremises);
				habitablePropertyTypeList.add(Property.PropertyTypePL.DwellingUnderConstruction);
				habitablePropertyTypeList.add(Property.PropertyTypePL.CondominiumResidencePremise);
				habitablePropertyTypeList.add(Property.PropertyTypePL.CondominiumVacationHome);
				habitablePropertyTypeList.add(Property.PropertyTypePL.Contents);
				habitablePropertyTypeList.add(Property.PropertyTypePL.DwellingPremisesCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.ResidencePremisesCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.VacationHomeCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.CondominiumDwellingPremisesCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.DwellingPremisesCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.DwellingUnderConstructionCovE);
				habitablePropertyTypeList.add(Property.PropertyTypePL.CondoVacationHomeCovE);

				for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
					for (PLPolicyLocationProperty locationProperty : location.getPropertyList()) {
						propertyTypeList.add(locationProperty.getpropertyType());
					}
					for (Property.PropertyTypePL propertyTypeToCheck : propertyTypeList) {
						if (habitablePropertyTypeList.contains(propertyTypeToCheck)) {
							habitablePropertyTypeListForPolicy.add(propertyTypeToCheck);
						}
					}
				}

				if (habitablePropertyTypeListForPolicy.size() > 1 && !policy.squire.overridePropertyOwnerValidation) {
					System.out.println("THERE ARE AT LEAST 2 HABITABLE PROPERTY TYPES DECLARED ON YOUR POLICY. THIS WILL REQUIRE "
							+ "THAT ALL HABITABLE PROPERTIES HAVE AN OWNER ASSIGNED. WE WILL AUTOMATICALLY SET THE OWNER BOOLEAN "
							+ "VALUE TO TRUE FOR ALL HABITABLE PROPERTIES ON THE POLICY. IF YOU WANT TO OVERRIDE THIS BEHAVIOR, "
							+ "PLEASE CALL THE \"withoutValidatingPropertyOwners\" FLAG IN THE BUILDER!");
					for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
						for (PLPolicyLocationProperty locationProperty : location.getPropertyList()) {
							if (habitablePropertyTypeList.contains(locationProperty.getpropertyType())) {
								locationProperty.setOwner(true);
						}
					}
				}
				}
				if(!policy.overrideDefaults) {
					int numberOfUnits = 0;
					for (PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
						for (PLPolicyLocationProperty locationProperty : location.getPropertyList()) {
							numberOfUnits += Integer.valueOf(locationProperty.getNumberOfUnits().getValue().replace(" Units", "").replace(" Unit", ""));
						}
						location.setPlNumResidence(numberOfUnits);
						numberOfUnits = 0;
					}
				}

				break;
			case StandardFirePL:
				break;
			case StandardInlandMarine:
				break;
			case StandardLiabilityPL:
				break;
			case Membership:
				break;
			default:
				break;
			}
		}


		//This section tests that the livestock(horses and Cows) entered in IM and FPP is not more than the count entered on Section II Squire Liability.
        if ((policy.squire.inlandMarine.livestock_PL_IM != null || !policy.squire.inlandMarine.livestock_PL_IM.isEmpty()) && (policy.squire.propertyAndLiability.squireFPP != null || !policy.squire.propertyAndLiability.squireFPP.getItems().isEmpty()) && (policy.squire.isCountry() || policy.squire.isFarmAndRanch())) {
			int sectionII = 0;
			int im = 0;
			int fpp = 0;			
			//cows, bulls, Steers, Heifers, Calves
			//Section II
			if(policy.squire.propertyAndLiability.liabilitySection.getCoverageByEnum(Property.SectionIICoveragesEnum.Livestock) != null){
				for(SquireLiablityCoverageLivestockItem sectionIILivestock : policy.squire.propertyAndLiability.liabilitySection.getCoverageByEnum(Property.SectionIICoveragesEnum.Livestock).getCoverageLivestockItems()){
					if(sectionIILivestock.getType() == repository.gw.enums.LivestockScheduledItemType.Cow){
						sectionII = sectionIILivestock.getQuantity();
					}
				}
			}

			//Inland Marine
			for(Livestock livestockType : policy.squire.inlandMarine.livestock_PL_IM){
				if(livestockType != null){
					for(LivestockScheduledItem livestockGroup : livestockType.getScheduledItems()){
						if(livestockGroup.getType().equals(repository.gw.enums.LivestockScheduledItemType.Cow) ||livestockGroup.getType().equals(repository.gw.enums.LivestockScheduledItemType.Bull) || livestockGroup.getType().equals(repository.gw.enums.LivestockScheduledItemType.Steer) || livestockGroup.getType().equals(repository.gw.enums.LivestockScheduledItemType.Heifer) || livestockGroup.getType().equals(repository.gw.enums.LivestockScheduledItemType.Calf)){
							im++;
						}
					}
				}
			}

			//FPP
			if(policy.squire.propertyAndLiability.squireFPP != null){
				ArrayList<FPP.FPPFarmPersonalPropertySubTypes> fppCowTypes = new ArrayList<FPP.FPPFarmPersonalPropertySubTypes>();
				fppCowTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Cows);
				fppCowTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Bulls);
				fppCowTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Steers);
				fppCowTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Heifers);
				fppCowTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Calves);

				for(FPP.FPPFarmPersonalPropertySubTypes fppType : fppCowTypes){
					fpp += policy.squire.propertyAndLiability.squireFPP.getTotalValuePerSubType(fppType);
				}
			}
			if(sectionII < im || sectionII < fpp){
				Assert.fail("Section II livestock must be greater or equal to the aggregate livestock on the FPP or the IM sections for Cows, Bulls, Steers, Heifers, and Calves types. ");	
			}

			//horses, mules, Donkeys, Llamas, Alpacas
			sectionII = 0;
			im = 0;
			fpp = 0;	
			//Section II
			if(policy.squire.propertyAndLiability.liabilitySection.getCoverageByEnum(Property.SectionIICoveragesEnum.Livestock) != null) {
			for(SquireLiablityCoverageLivestockItem sectionIILivestock : policy.squire.propertyAndLiability.liabilitySection.getCoverageByEnum(Property.SectionIICoveragesEnum.Livestock).getCoverageLivestockItems()){
					if (sectionIILivestock.getType().equals(repository.gw.enums.LivestockScheduledItemType.Horse)) {
					sectionII = sectionIILivestock.getQuantity();
				}
			}
			}


			//Inland Marine
			for(Livestock livestockGroup : policy.squire.inlandMarine.livestock_PL_IM){
				if(livestockGroup != null){
					for(LivestockScheduledItem scheduledLivestock : livestockGroup.getScheduledItems()){
						if(scheduledLivestock.getType().equals(repository.gw.enums.LivestockScheduledItemType.Horse) || scheduledLivestock.getType().equals(repository.gw.enums.LivestockScheduledItemType.Mule) || scheduledLivestock.getType().equals(repository.gw.enums.LivestockScheduledItemType.Donkey) || scheduledLivestock.getType().equals(repository.gw.enums.LivestockScheduledItemType.Llama)|| scheduledLivestock.getType().equals(LivestockScheduledItemType.Alpaca)){
							im++;
						}
					}
				}
			}

			//FPP
			if(policy.squire.propertyAndLiability.squireFPP != null){
				ArrayList<FPP.FPPFarmPersonalPropertySubTypes> fppHorseTypes = new ArrayList<FPP.FPPFarmPersonalPropertySubTypes>();
				fppHorseTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Horses);
				fppHorseTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Mules);
				fppHorseTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Donkeys);
				fppHorseTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Llamas);
				fppHorseTypes.add(FPP.FPPFarmPersonalPropertySubTypes.Alpacas);
				for(FPP.FPPFarmPersonalPropertySubTypes fppType : fppHorseTypes){
					fpp += policy.squire.propertyAndLiability.squireFPP.getTotalValuePerSubType(fppType);
				}				
			}
			if(sectionII < im || sectionII < fpp){
				Assert.fail("Section II livestock must be greater or equal to the livestock on the FPP or the IM sections for Horses, Mules, Donkeys, Llamas, Alpacas.");	
			}
		}

		if (policy.squire.inlandMarine.watercrafts_PL_IM != null) {
			for (SquireIMWatercraft inMarine : policy.squire.inlandMarine.watercrafts_PL_IM) {
				for (AdditionalInterest addint : inMarine.getAdditionalInterest()) {
					addint.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIVWatercraft);
				}
			}
		}//end if watercrafts_PL_IM
		
		if(!policy.lineSelection.contains(repository.gw.enums.LineSelection.Membership)) {
			System.out.println("Squire policies require that at least one policy member has membership dues. As such, the membership line must be included on the policy object. You have not included one, so we are adding it for you.");
			//policy.lineSelection.add(LineSelection.Membership);
		}
		
		// verify that at least one line selection for product is present
		if(policy.polOrgType.equals(OrganizationType.Sibling)) {
			if(!policy.lineSelection.contains(repository.gw.enums.LineSelection.PersonalAutoLinePL)) {
				Assert.fail("You must have an Auto Line if you have a Sibling Policy");
			}
			if (policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.OneHundredLow)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.OneHundredHigh)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.ThreeHundredLow)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.ThreeHundredHigh)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.CSL300K)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.CSL500K)
					|| policy.squire.squirePA.getCoverages().getLiability().equals(SquirePersonalAutoCoverages.LiabilityLimit.CSL100K)) {
				Assert.fail("You must have specific general liability limits (25/50/15 -- 25/50/25 -- 50/100/25 -- 50/100/50 -- 75,000 CSL -- 100,000 CSL) if you have a Sibling Policy");
			}
		}


		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.PersonalAutoLinePL)) {
			if(policy.squire.squirePA.getVehicleList().size() == 0) {
				Assert.fail("You must have at least one vehicle if you are going to have Personal Auto.");
			}

			if(policy.squire.squirePA.getDriversList().size() == 0) {

				if(!policy.squire.squirePA.isPrimaryInsuredAsDriver() && policy.squire.squirePA.getDriversList().size() == 0) {
					Assert.fail("You must have at least one driver or set your PNI as a driver.");
				}

				if(!policy.squire.squirePA.isPrimaryInsuredAsDriver()) {
					if(policy.squire.squirePA.areAllVehiclesUnassignedDrivers()) {
						Vehicle toMarkAsAssigned = policy.squire.squirePA.getVehicleListUnassignedDrivers().get(0);
						toMarkAsAssigned.setDriverPL(policy.squire.squirePA.getDriversList().get(0));
					}
				}
			}
		}

		if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.PersonalAutoLinePL) && !policy.lineSelection.contains(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)) {
			Assert.fail("You must select at least one LineSelection for Personal Lines.");
		}

//		if(policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL) && (policy.squire.isFarmAndRanch() || policy.squire.isCountry())) {
//			if(policy.squire.propertyAndLiability.squireFPP != null && !policy.squire.propertyAndLiability.squireFPP.isOverrideEmptyItemsList()) {
//				if(policy.squire.propertyAndLiability.squireFPP.getItems() != null && policy.squire.propertyAndLiability.squireFPP.getItems().size() == 0) {
//					throw new GuidewirePolicyCenterException(Configuration.getWebDriver().getCurrentUrl(), "ERROR: you must have at least 1 item on the squire FPP");
//				}
//			}
//		}



		//checks Squire Liability
		if(policy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL) && policy.squire.propertyAndLiability.liabilitySection == null){
			policy.squire.propertyAndLiability.liabilitySection = new SquireLiability();
			System.out.println("You must have a SquireLiability object because you have PropertyAndLiabilityLinePL so we added a default one for you! :)");
		}
	}//END OF METHOD
}//EOF
