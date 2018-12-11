package repository.gw.generate;


import org.testng.Assert;
import persistence.globaldatarepo.helpers.VINHelper;
import repository.gw.enums.*;
import repository.gw.generate.custom.Vehicle;
import repository.gw.generate.custom.*;

import java.util.ArrayList;
import java.util.List;

public class FieldIntegrityAndDefaults_CPP {



	public void checkClassFieldIntegrityAndDefaults_CPP(GeneratePolicy policy) throws Exception {

		if(policy.commercialPackage.locationList == null) {
			Assert.fail("MUST HAVE LOCATION ON CPP POLICY");
		}



		for(PolicyLocation location : policy.commercialPackage.locationList) {
			for(PolicyLocationBuilding property : location.getBuildingList()) {
				property.setLocationAddress(location.getAddress());
			}
		}


		ArrayList<PolicyLocation> tempList = new ArrayList<PolicyLocation>();
		tempList.addAll(policy.commercialPackage.locationList);
		//COMMERCIAL PROPERTY
		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP)) {
			//			int orCount = 0;
			for(CPPCommercialPropertyProperty property : policy.commercialPropertyCPP.getCommercialPropertyList()) {
				//ADDS POLICY LOCATIONS AND ARN'T ALREADY INCLUDED IN POLCIY LOCATION LIST.
				boolean addressFound = false;
				for(PolicyLocation location : tempList) {
					if(location.getAddress().getLine1().equals(property.getAddress().getLine1())) addressFound = true;
				}
				if(!addressFound) policy.commercialPackage.locationList.add(new PolicyLocation(property.getAddress()));
				addressFound = false;

				for(CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
					for(CPPCPClassCodeUWQuestions question : building.getUwQuestionList()) {
						switch (question.getClassCode()) {
						case "41677":
						case "45190":
						case "45192":
						case "46671":
						case "46773":
						case "48557":
						case "48558":
						case "51999":
						case "95410":
						case "70412":
						case "58161":
						case "50911":
						case "58165":
						case "58166":
							question.getChildQuestions().get(0).setCorrectAnswer("Checked");
							break;
						}
					}
				}
			}
		}

		//LEGAL LIABILITY COVERGES HAS TO HAVE BPP COVERAGE ALSO
		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP)) {
			for(CPPCommercialPropertyProperty property : policy.commercialPropertyCPP.getCommercialPropertyList()) {
				for(CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
					if(building.getCoverages().getBuildingCoverageList().contains(CommercialProperty.PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40) && !building.getCoverages().getBuildingCoverageList().contains(CommercialProperty.PropertyCoverages.BusinessPersonalPropertyCoverage)) {
						List<CommercialProperty.PropertyCoverages> oldCoverageList = new ArrayList<CommercialProperty.PropertyCoverages>();
						oldCoverageList.addAll(building.getCoverages().getBuildingCoverageList());
						building.getCoverages().getBuildingCoverageList().clear();
						building.getCoverages().getBuildingCoverageList().add(CommercialProperty.PropertyCoverages.BusinessPersonalPropertyCoverage);
						building.getCoverages().getBuildingCoverageList().addAll(oldCoverageList);
					}
				}
			}
		}

		//CHECK TO SET APPLY TO BUILDING AND APPLY TO BPP
		//IF THESE COVERAGES AREN'T ON THE BUILDING THE CHECKBOXES DON'T EXIST AND TEST WILL FAIL
		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP)) {
			for(CPPCommercialPropertyProperty property : policy.commercialPropertyCPP.getCommercialPropertyList()) {
				for(CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
					for(AdditionalInterest additionalInterest : building.getAdditionalInterestList()) {
						if(building.getCoverages().isBuildingCoverage()) additionalInterest.setSetAppliedToBuilding(true);
						if(building.getCoverages().isBusinessPersonalPropertyCoverage()) additionalInterest.setSetAppliedToBPP(true);
					}

				}
			}
		}




		//GENERAL LIABILITY
		if (policy.lineSelection.contains(repository.gw.enums.LineSelection.GeneralLiabilityLineCPP)) {
			//SET AT LEAST ONE 'OR' QUESTION TO CHECKED
			int orCount = 0;
			for (CPPGLExposureUWQuestions question : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()
					.get(0).getUnderWritingQuestions()) {
				switch (policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().get(0).getClassCode()) {
				case "41677":
				case "45190":
				case "46671":
				case "46773":
				case "48557":
				case "48558":
				case "51999":
				case "95410":
				case "70412":
				case "58161":
				case "50911":
				case "59211":
				case "58165":
				case "58166":
					for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
						if (childQuestion.getFormatType().equals(repository.gw.enums.FormatType.BooleanCheckbox)) {
							orCount++;
						} //END IF
					} //END FOR
					if (orCount > 0) {
						for (CPPGLExposureUWQuestions childQuestion : question.getChildrenQuestions()) {
							if (childQuestion.getFormatType().equals(FormatType.BooleanCheckbox)) {
								childQuestion.setCorrectAnswer("Checked");
								break;
							} //END IF
						} //END FOR
					} //END IF
					orCount = 0;
					break;
				}//END SWITCH
			} //END FOR

			//When you have one of the following class codes selected: 16910, 16911, 16915, 16916, 16930, or 16931, the user needs to be stopped on the wizard step until class code 58161 has been added to the same location.
			for(CPPGeneralLiabilityExposures exposure : policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures()) {
				boolean classFound = false;
				switch(exposure.getClassCode()) {
				case "16910":
				case "16911":
				case "16915":
				case "16916":
				case "16930":
				case "16931":
					System.out.println("ADDING EXPOSURE CLASS 58161 TO POLICY OBJECT. OR WILL GET A VALIDATION ERROR BLOCKING USER. \n THIS IS NORMAL FUNCITONALITY");
					policy.generalLiabilityCPP.getCPPGeneralLiabilityExposures().add(new CPPGeneralLiabilityExposures(exposure.getLocation(), "58161"));
					classFound = true;
					break;
				}//END SWITCH
				if(classFound) {
					break;
				}//END IF
			}//END FOR
		}







		//COMMERCIAL AUTO
		//for setting the additional intrest/insured types for each object types
		if (policy.commercialAutoCPP != null && policy.commercialAutoCPP.getVehicleList() != null) {
			for (Vehicle vehicle : policy.commercialAutoCPP.getVehicleList()) {
				for (AdditionalInterest addint : vehicle.getAdditionalInterest()) {
					addint.setAdditionalInterestSubType(AdditionalInterestSubType.CAVehicles);
				}
			}
		}


		if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialAutoLineCPP) && !policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP) && !policy.lineSelection.contains(repository.gw.enums.LineSelection.GeneralLiabilityLineCPP) && !policy.lineSelection.contains(repository.gw.enums.LineSelection.InlandMarineLineCPP)) {
			Assert.fail("You must select at least one LineSelection for CPP.");
		}

		//CPP Inland Marine
		if (policy.lineSelection.contains(repository.gw.enums.LineSelection.InlandMarineLineCPP)) {

			//Needs BPP Coverage on Property
			if (policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.AccountsReceivable_CM_00_66) || policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.BaileesCustomers_IH_00_85) || policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.CameraAndMusicalInstrumentDealers_CM_00_21) || policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.ComputerSystems_IH_00_75) || policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.ValuablePapers_CM_00_67)) {
				if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP)) {
					Assert.fail("For the included Inland Marine coverage parts, you must have the Commercial Property line selected for the policy.");
				} else if (policy.commercialPropertyCPP.getCommercialPropertyList().isEmpty()) {
					Assert.fail("For the included Inland Marine coverage parts, you must have a property attached to the policy.");
				} else if (policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().isEmpty()) {
					Assert.fail("For the included Inland Marine coverage parts, you must have a building on your property.");
				} else if (!policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().get(0).getCoverages().isBusinessPersonalPropertyCoverage()) {
					Assert.fail("For the included Inland Marine coverage parts, you must have a building with BPP coverage.");
				}
			}
			//Needs a property for Signs
			if (policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.Signs_CM_00_28)) {
				if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP)) {
					Assert.fail("For the included Inland Marine coverage parts, you must have the Commercial Property line selected for the policy.");
				} else if (policy.commercialPropertyCPP.getCommercialPropertyList().isEmpty()) {
					Assert.fail("For the included Inland Marine coverage parts, you must have a property attached to the policy.");
				}
			}
			//Needs a vehicle for Motor Truck Cargo
			if (policy.inlandMarineCPP.getCoveragePart().contains(InlandMarineCPP.InlandMarineCoveragePart.MotorTruckCargo)) {
				if (!policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialAutoLineCPP)) {
					Assert.fail("You must have the commercial auto line selected for Motor Truck Cargo coverage");
				} else if (policy.commercialAutoCPP.getVehicleList().isEmpty()) {
					Assert.fail("You must have at least one vehicle for Motor Truck Cargo coverage");
				}
			}
		}



		//jlarsen 5/12/2016
		//verify all vehicle VINs are Unique
		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialAutoLineCPP)) {
			if(policy.commercialAutoCPP.isDuplicateVINCheck()) {
				int vinCount = 0;
				for(Vehicle vehicle : policy.commercialAutoCPP.getVehicleList()) {
					for(Vehicle vehicle2 : policy.commercialAutoCPP.getVehicleList()) {
						if(vehicle.getVin().equals(vehicle2.getVin())) {
							vinCount++;
						}
					}//END FOR
					if(vinCount >= 2) {
						System.out.println("FOUND DUPLICATE VINS CHANGING ONE OF THEM. IF YOU DO NOT WANT policy, CHANGE THE isDuplicateVINCheck() FLAG");
						vehicle.setVin(VINHelper.getRandomVIN().getVin());
						vinCount = 0;
					}//END IF
				}//END FOR
			}//END IF
		}//END IF

		if(policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialAutoLineCPP) || policy.lineSelection.contains(repository.gw.enums.LineSelection.CommercialPropertyLineCPP) || policy.lineSelection.contains(repository.gw.enums.LineSelection.GeneralLiabilityLineCPP) || policy.lineSelection.contains(LineSelection.InlandMarineLineCPP)) {
			if (policy.commercialPackage.locationList == null || policy.commercialPackage.locationList.size() == 0) {
				Assert.fail("locationList Must Have At Least One Policy Location");
			}
		}

	}//END checkClassFieldIntegrityAndDefaults_CPP()
}//EOF


























