package repository.pc.workorders.forms.cpp.commercialproperty;

import org.testng.Assert;
import persistence.globaldatarepo.entities.CPForms;
import repository.gw.enums.Building.FireBurglaryAlarmType;
import repository.gw.enums.BusinessIncomeCoverageForm.CoverageOptions;
import repository.gw.enums.CommercialProperty.*;
import repository.gw.enums.CommercialPropertyForms;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.Vehicle.AdditionalInterestTypeCPP;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;

import java.util.ArrayList;
import java.util.List;

public class CommercialPropertyFormInference extends Thread {

	public CommercialPropertyForms form;
	public GeneratePolicy policy;
	List<CPForms> formsList = new ArrayList<CPForms>();
	List<CommercialPropertyForms> expectedFormsList = new ArrayList<CommercialPropertyForms>();

	public CommercialPropertyFormInference(GeneratePolicy policy, CommercialPropertyForms form) {
		System.out.println("crating new thread.");
		this.policy = policy;
		this.form = form;
	}


	public CommercialPropertyFormInference() {
	}


	public void run() {
		try {
			CreateFormsPolicyObject(policy, form);
		} catch (Exception e) {
			Assert.fail("Failed to add form informaiton to your Generate Object.");
		}
	}

	public static GeneratePolicy CreateFormsPolicyObject(GeneratePolicy policy, CommercialPropertyForms form) throws Exception {
		//		policy.productType = ProductLineType.CPP;
		//		policy.commercialPropertyCPP = new CPPCommercialProperty(new CPPCommercialPropertyProperty(new CPPCommercialProperty_Building()));
		//		policy.lineSelection.add(LineSelection.CommercialPropertyLineCPP);

		switch (form) {
		case AdditionalBuildingProperty_CP_14_15:
			CPPCommercialProperty_Building additionalBuildingProperty_CP_14_15 = new CPPCommercialProperty_Building();
			additionalBuildingProperty_CP_14_15.getAdditionalCoverages().setAdditionalBuildingProperty_CP_14_15(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(additionalBuildingProperty_CP_14_15);
			break;
		case AdditionalCoveredProperty_CP_14_10:
			CPPCommercialProperty_Building additionalCoveredProperty_CP_14_10 = new CPPCommercialProperty_Building();
			additionalCoveredProperty_CP_14_10.getAdditionalCoverages().setAdditionalCoveredProperty_CP_14_10(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(additionalCoveredProperty_CP_14_10);
			break;
		case AdditionalInsured_BuildingOwner_CP_12_19:
			CPPCommercialProperty_Building additionalCoveredProperty_CP_12_19 = new CPPCommercialProperty_Building();
			AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
			additionalInterest.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.CP_AdditionalInsured_BuildingOwner_CP_12_19);
			additionalCoveredProperty_CP_12_19.getAdditionalInterestList().add(additionalInterest);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(additionalCoveredProperty_CP_12_19);
			break;
		case AdditionalPropertyNotCovered_CP_14_20:
			CPPCommercialProperty_Building additionalCoveredProperty_CP_14_20 = new CPPCommercialProperty_Building();
			additionalCoveredProperty_CP_14_20.getExclusionsConditions().setAdditionalPropertyNotCovered_CP_14_20(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(additionalCoveredProperty_CP_14_20);
			break;
		case BindingArbitration_CR_20_12:
			//When one of the following coverages are selected: � Clients' Property CR 04 01 � Employee Theft, Forgery Or Alteration � Guests' Property CR 04 11 � Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10 � Inside The Premises � Theft Of Money And Securities � Inside The Premises � Theft Of Other Property CR 04 05 � Joint Loss Payable CR 20 15 � Outside The Premises � Binding Arbitration CR 20 12 � Change In Control Of The Insured � Notice To The Company CR 20 29 � Commercial Crime Manuscript Endorsement IDCR 31 1001 � Convert To An Aggregate Limit Of Insurance CR 20 08 � Idaho Changes CR 02 12 � Commercial Crime Manuscript Endorsement IDCR 31 1001 � Exclude Certain Risks Inherent In Insurance Operations CR 25 23 � Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01 � Exclude Designated Premises CR 35 13 � Exclude Specified Property CR 35 01 � and/or Exclude Unauthorized Advances7, Require Annual Audit CR 25 25
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setClientsProperty_CR_04_01(true);
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setForgeryOrAlteration(true);

			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10(true);
			break;
		case BrokenOrCrackedGlassExclusionForm_IDCP_31_1006:
			CPPCommercialProperty_Building brokenOrCrackedGlassExclusionForm_IDCP_31_1006 = new CPPCommercialProperty_Building();
			brokenOrCrackedGlassExclusionForm_IDCP_31_1006.getExclusionsConditions().setBrokenOrCrackedGlassExclusionForm_IDCP_31_1006(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(brokenOrCrackedGlassExclusionForm_IDCP_31_1006);
			break;
		case BuildersRiskCoverageForm_CP_00_20:
			//Not available when "Building Coverage", "Business Personal Property Coverage", or "Property In The Open" is selected.
			CPPCommercialProperty_Building buildersRiskCoverageForm_CP_00_20 = new CPPCommercialProperty_Building();
			List<PropertyCoverages> propertyCoverages = new ArrayList<PropertyCoverages>();
			propertyCoverages.add(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);
			buildersRiskCoverageForm_CP_00_20.getCoverages().setBuildingCoveragesList(propertyCoverages);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(buildersRiskCoverageForm_CP_00_20);
			break;
		case BuildersRiskRenovations_CP_11_13:
			CPPCommercialProperty_Building BuildersRiskRenovations_CP_11_13 = new CPPCommercialProperty_Building();
			propertyCoverages = new ArrayList<PropertyCoverages>();
			propertyCoverages.add(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);
			BuildersRiskRenovations_CP_11_13.getCoverages().setBuildingCoveragesList(propertyCoverages);
			BuildersRiskRenovations_CP_11_13.getAdditionalCoverages().setBuildersRiskRenovations_CP_11_13(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(BuildersRiskRenovations_CP_11_13);
			break;
		case BuildingAndPersonalPropertyCoverageForm_CP_00_10:
			CPPCommercialProperty_Building buildingAndPersonalPropertyCoverageForm_CP_00_10 = new CPPCommercialProperty_Building();
			buildingAndPersonalPropertyCoverageForm_CP_00_10.setCondominium(Condominium.NotACondominium);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(buildingAndPersonalPropertyCoverageForm_CP_00_10);
			break;
		case BurglaryAndRobberyProtectiveSafeguards_CP_12_11:
			CPPCommercialProperty_Building burglaryAndRobberyProtectiveSafeguards_CP_12_11 = new CPPCommercialProperty_Building();
			burglaryAndRobberyProtectiveSafeguards_CP_12_11.setBurglaryAndRobberyProtectiveSafeguards_CP_12_11(FireBurglaryAlarmType.CentralStationWithKeys);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(burglaryAndRobberyProtectiveSafeguards_CP_12_11);
			break;
		case BusinessIncome_AndExtraExpense_CoverageForm_CP_00_30:
			//Business Income Coverage Form	Business Income Coverage Type
			//TODO
			break;
		case BusinessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03:
			//Available when the coverage ""Business Income Coverage Form"" is selected and when under ""Business Income Coverage Form"" the term ""Coverage Options"" the has the term option ""Coinsurance"" or ""Business Income Agreed Value"" is selected.
			//Not available when the question ""Is the building owned by the insured?"" is answered Yes."
			CPPCommercialProperty_Building businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
			businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03.getAdditionalCoverages().setBusinessIncomeCoverageForm_CoverageOptions(CoverageOptions.Coinsurance);
			businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03.getAdditionalCoverages().setBusinessIncome_LandlordAsAdditionalInsuredRentalValue_CP_15_03(true);
			businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03.setBuildingDescription("BusinessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(businessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03);
			break;
		case BusinessIncome_WithoutExtraExpense_CoverageForm_CP_00_32:
			//Business Income Coverage Form	Business Income Coverage Type
			CPPCommercialProperty_Building businessIncome_WithoutExtraExpense_CoverageForm_CP_00_32 = new CPPCommercialProperty_Building();
			businessIncome_WithoutExtraExpense_CoverageForm_CP_00_32.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(businessIncome_WithoutExtraExpense_CoverageForm_CP_00_32);
			break;
		case CausesOfLoss_BasicForm_CP_10_10:
			CPPCommercialProperty_Building causesOfLoss_BasicForm_CP_10_10 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			causesOfLoss_BasicForm_CP_10_10.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Basic);
			causesOfLoss_BasicForm_CP_10_10.setBuildingDescription("CausesOfLoss_BasicForm_CP_10_10");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(causesOfLoss_BasicForm_CP_10_10);
			break;
		case CausesOfLoss_BroadForm_CP_10_20:
			CPPCommercialProperty_Building causesOfLoss_BroadForm_CP_10_20 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			causesOfLoss_BroadForm_CP_10_20.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Broad);
			causesOfLoss_BroadForm_CP_10_20.setBuildingDescription("CausesOfLoss_BroadForm_CP_10_20");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(causesOfLoss_BroadForm_CP_10_20);
			break;
		case CausesOfLoss_SpecialForm_CP_10_30:
			CPPCommercialProperty_Building causesOfLoss_SpecialForm_CP_10_30 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			causesOfLoss_SpecialForm_CP_10_30.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Special);
			causesOfLoss_SpecialForm_CP_10_30.setBuildingDescription("Special Form Building");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(causesOfLoss_SpecialForm_CP_10_30);
			break;
		case CertificateOfPropertyInsurance_ACORD_24:
			//TODO
			break;
		case ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29:
			//Available when at least one of these coverages is selected: Clients' Property CR 04 01, Employee Theft, Forgery Or Alteration, Guests' Property CR 04 11, Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10, Inside The Premises � Theft Of Money And Securities, Inside The Premises � Theft Of Other Property CR 04 05, Joint Loss Payable CR 20 15, and/or Outside The Premises.
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
			break;
		case ClientsProperty_CR_04_01:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setClientsProperty_CR_04_01(true);
			break;
		case CommercialCrimeCoverageForm_LossSustainedForm__CR_00_21:
			//TODO
			break;
		case CommercialCrimeManuscriptEndorsement_IDCR_31_1001:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition(true);
			break;
		case CommercialCrimePolicyDeclarations_IDCR_03_0001:
			//TODO
			break;
		case CommercialPropertyConditions_CP_00_90:
			// REQUIRED
			break;
		case CommercialPropertyCoveragePartDeclarationsPage_IDCP_03_0001:
			// REQUIRED
			break;
		case CommercialPropertyManuscriptEndorsement_IDCP_31_1005:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition(true);
			break;
		case CondominiumAssociationCoverageForm_CP_00_17:
			//Available when under the building "Details" tab Condominium is select with the option "Association".
			CPPCommercialProperty_Building condominiumAssociationCoverageForm_CP_00_17 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			condominiumAssociationCoverageForm_CP_00_17.setBuildingDescription("CondominiumAssociationCoverageForm_CP_00_17");
			condominiumAssociationCoverageForm_CP_00_17.setCondominium(Condominium.Association);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(condominiumAssociationCoverageForm_CP_00_17);
			break;
		case CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18:
			//Available when under the building "Details" tab Condominium is select with the option "Unit Owner".
			CPPCommercialProperty_Building condominiumCommercialUnit_OwnersCoverageForm_CP_00_18 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			condominiumCommercialUnit_OwnersCoverageForm_CP_00_18.setBuildingDescription("CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18");
			condominiumCommercialUnit_OwnersCoverageForm_CP_00_18.setCondominium(Condominium.UnitOwner);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(condominiumCommercialUnit_OwnersCoverageForm_CP_00_18);
			break;
		case CondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18:
			//TODO
			break;
		case ConvertToAnAggregateLimitOfInsurance_CR_20_08:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
			break;
		case CrimePolicyholderDisclosureNotice_IDCR_10_0001:
			//RENEWAL JOB ONLY
			break;
		case DischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38:
			CPPCommercialProperty_Building dischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			dischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38.getAdditionalCoverages().setDischargeFromSewerDrainOrSumpNotFlood_Related_CP_10_38(true);
			dischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38.setSprinklered(true);
			dischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38.setBuildingDescription("DischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(dischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38);
			break;
		case DiscretionaryPayrollExpense_CP_15_04:
			CPPCommercialProperty_Building discretionaryPayrollExpense_CP_15_04 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			discretionaryPayrollExpense_CP_15_04.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
			discretionaryPayrollExpense_CP_15_04.getAdditionalCoverages().setDiscretionaryPayrollExpense_CP_15_04(true);
			discretionaryPayrollExpense_CP_15_04.setSprinklered(true);
			discretionaryPayrollExpense_CP_15_04.setBuildingDescription("DiscretionaryPayrollExpense_CP_15_04");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(discretionaryPayrollExpense_CP_15_04);
			break;
		case EquipmentBreakdownEnhancementEndorsement_IDCP_31_1002:
			//TODO
			break;
		case EvidenceOfCommercialPropertyInsurance_ACORD_28:
			break;
		case ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setExcludeCertainRisksInherentInInsuranceOperations_CR_25_23(true);
			break;
		case ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01(true);
			break;
		case ExcludeDesignatedPremises_CR_35_13:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setExcludeDesignatedPremises_CR_35_13(true);
			break;
		case ExcludeSpecifiedProperty_CR_35_01:
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().setExcludeSpecifiedProperty_CR_35_01(true);
			break;
		case ExcludeUnauthorizedAdvances_RequireAnnualAudit_CR_25_25:
			//Available when question "Is the applicant/insured a fraternal organization or labor union?" is answered Yes.
			//TODO
			break;
		case ExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34:
			//Not available when you have coverage "Builders' Risk Coverage Form CP 00 20", "Condominium Association Coverage Form CP 00 17", or "Condominium Commercial Unit-Owners Coverage Form CP 00 18" is selected.
			//This endorsement also becomes required when under the details tab in the field ""Sq. ft. rented to others"" has a number greater than 0.
			//Other than that when this endorsement is not required it is electable."
			CPPCommercialProperty_Building exclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			exclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34.setBuildingDescription("ExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34");
			exclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34.setCondominium(Condominium.NotACondominium);
			exclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34.getExclusionsConditions().setExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperationsRentalProperties_CP_10_34(true);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(exclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperations_RentalProperties__CP_10_34);
			break;
		case ExclusionOfLossDueToVirusOrBacteria_CP_01_40:
			//REQUIRED
			break;
		case ExtraExpenseCoverageForm_CP_00_50:
			//Not Available when "Business Income Coverage Form" or "Builders' Risk Coverage Form CP 00 20" is selected.
			CPPCommercialProperty_Building extraExpenseCoverageForm_CP_00_50 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			extraExpenseCoverageForm_CP_00_50.getAdditionalCoverages().setExtraExpenseCoverageForm_CP_00_50(true);
			extraExpenseCoverageForm_CP_00_50.setSprinklered(true);
			extraExpenseCoverageForm_CP_00_50.setBuildingDescription("ExtraExpenseCoverageForm_CP_00_50");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(extraExpenseCoverageForm_CP_00_50);
			break;
		case FoodContamination_BusinessInterruptionAndExtraExpense__CP_15_05:
			//TODO
			break;
		case FunctionalBuildingValuation_CP_04_38:
			CPPCommercialProperty_Building functionalBuildingValuation_CP_04_38 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			functionalBuildingValuation_CP_04_38.getAdditionalCoverages().setFunctionalBuildingValuation_CP_04_38(true);
			functionalBuildingValuation_CP_04_38.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Broad);
			functionalBuildingValuation_CP_04_38.setSprinklered(true);
			functionalBuildingValuation_CP_04_38.setBuildingDescription("FunctionalBuildingValuation_CP_04_38");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(functionalBuildingValuation_CP_04_38);
			break;
		case GuestsProperty_CR_04_11:
			CPPCommercialProperty_Building guestsProperty_CR_04_11 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			guestsProperty_CR_04_11.getAdditionalCoverages().setGuestsPropertyValuation_CP_04_11(true);
			guestsProperty_CR_04_11.setSprinklered(true);
			guestsProperty_CR_04_11.setBuildingDescription("GuestsProperty_CR_04_11");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(guestsProperty_CR_04_11);
			break;
		case HighValueLetter_IDCP_11_0002:
			// REVIEW CASE DOCUMENT
			break;
		case IdahoChanges_CR_02_12:
			//Available when at least one of these coverages is selected: Clients' Property CR 04 01, Employee Theft, Forgery Or Alteration, Guests' Property CR 04 11, Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10, Inside The Premises � Theft Of Money And Securities, Inside The Premises � Theft Of Other Property CR 04 05, Joint Loss Payable CR 20 15, and/or Outside The Premises.
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setEmployeeTheft(true);
			break;
		case IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10:
			//CommercialPropertyLine	Crime Category
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10(true);
			break;
		case InsideThePremises_TheftOfOtherProperty_CR_04_05:
			//CommercialPropertyLine	Crime Category
			policy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().setInsideThePremises_TheftOfOtherProperty_CR_04_05(true);
			break;
		case JointLossPayable_CR_20_15:
			//Available when "Joint Loss Payable CR 20 15" is selected under the Additional Interest tab.
			break;
		case LeasedProperty_CP_14_60:
			CPPCommercialProperty_Building leasedProperty_CP_14_60 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage, PropertyCoverages.BuildingCoverage);
			leasedProperty_CP_14_60.getAdditionalCoverages().setLeasedProperty_CP_14_60(true);
			leasedProperty_CP_14_60.setSprinklered(true);
			leasedProperty_CP_14_60.setBuildingDescription("LeasedProperty_CP_14_60");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(leasedProperty_CP_14_60);
			break;
		case LegalLiabilityCoverageForm_CP_00_40:
			//Available when �Business Personal Property Coverage�, �Personal Property Of Others�, "Building And Personal Property Coverage Form CP 00 10", and/or "Condominium Commercial Unit-Owners Coverage Form CP 00 18" are the only coverages selected under the Coverage tab. This is not available when any other coverage is selected.
			CPPCommercialProperty_Building legalLiabilityCoverageForm_CP_00_40 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage, PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);
			legalLiabilityCoverageForm_CP_00_40.setBuildingDescription("LegalLiabilityCoverageForm_CP_00_40");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(legalLiabilityCoverageForm_CP_00_40);
			break;
		case LegalLiabilityCoverageSchedule_CP_DS_05:
			//Available when �Business Personal Property Coverage�, �Personal Property Of Others�, "Building And Personal Property Coverage Form CP 00 10", and/or "Condominium Commercial Unit-Owners Coverage Form CP 00 18" are the only coverages selected under the Coverage tab. This is not available when any other coverage is selected.
			CPPCommercialProperty_Building legalLiabilityCoverageSchedule_CP_DS_05 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage, PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);
			legalLiabilityCoverageSchedule_CP_DS_05.setBuildingDescription("LegalLiabilityCoverageSchedule_CP_DS_05");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(legalLiabilityCoverageSchedule_CP_DS_05);
			break;
		case LimitationsOnCoverageForRoofSurfacing_CP_10_36:
			CPPCommercialProperty_Building limitationsOnCoverageForRoofSurfacing_CP_10_36 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			limitationsOnCoverageForRoofSurfacing_CP_10_36.getExclusionsConditions().setLimitationsOnCoverageForRoofSurfacing_CP_10_36(true);
			limitationsOnCoverageForRoofSurfacing_CP_10_36.setSprinklered(true);
			limitationsOnCoverageForRoofSurfacing_CP_10_36.setBuildingDescription("LimitationsOnCoverageForRoofSurfacing_CP_10_36");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(limitationsOnCoverageForRoofSurfacing_CP_10_36);
			break;
		case LossPayableProvisions_CP_12_18:
			//Additional interest loss payable
			CPPCommercialProperty_Building lossPayableProvisions_CP_12_18 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			lossPayableProvisions_CP_12_18.setBuildingDescription("LossPayableProvisions_CP_12_18");
			AdditionalInterest lossPayable = new AdditionalInterest(ContactSubType.Company);
			lossPayable.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.CP_LossPayableClause_CP_12_18);
			ArrayList<AdditionalInterest> buildingAddtionalInterestList = new ArrayList<AdditionalInterest>();
			buildingAddtionalInterestList.add(lossPayable);
			lossPayableProvisions_CP_12_18.setAdditionalInterestList(buildingAddtionalInterestList);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(lossPayableProvisions_CP_12_18);
			break;
		case MultipleDeductibleForm_IDCP_31_1001:
			CPPCommercialProperty_Building multipleDeductible_1 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			multipleDeductible_1.setBuildingDescription("MultipleDeductible_1");
			multipleDeductible_1.getCoverages().setBusinessPersonalPropertyCoverage_Deductible(BusinessPersonalPropertyCoverageDeductible.FiveHundred);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(multipleDeductible_1);

			CPPCommercialProperty_Building multipleDeductible_2 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			multipleDeductible_2.setBuildingDescription("MultipleDeductible_2");
			multipleDeductible_2.getCoverages().setBusinessPersonalPropertyCoverage_Deductible(BusinessPersonalPropertyCoverageDeductible.OneThousand);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(multipleDeductible_2);
			break;
		case OrdinanceOrLawCoverage_CP_04_05:
			CPPCommercialProperty_Building ordinanceOrLawCoverage_CP_04_05 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			ordinanceOrLawCoverage_CP_04_05.getAdditionalCoverages().setOrdinanceorLawCoverage_CP_04_05(true);
			ordinanceOrLawCoverage_CP_04_05.setSprinklered(true);
			ordinanceOrLawCoverage_CP_04_05.setBuildingDescription("OrdinanceOrLawCoverage_CP_04_05");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(ordinanceOrLawCoverage_CP_04_05);
			break;
		case OutdoorSigns_CP_14_40:
			//Available when class code 1185 7a or 1185 7b is selected.
			CPPCommercialProperty_Building outdoorSigns_CP_14_40 = new CPPCommercialProperty_Building(PropertyCoverages.PropertyInTheOpen);
			outdoorSigns_CP_14_40.setClassCode("1185 7a");
			outdoorSigns_CP_14_40.setBuildingDescription("OutdoorSigns_CP_14_40");
			outdoorSigns_CP_14_40.getAdditionalCoverages().setOutdoorSigns_CP_14_40(true);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(outdoorSigns_CP_14_40);
			break;
		case PayrollLimitationOrExclusion_CP_15_10:
			//Available when the coverage ""Business Income Coverage Form"" is selected and Term ""Coverage Options"" is selected with a Term Option of ""Coinsurance"".
			//Not available when ""Discretionary Payroll Expense CP 15 04"" is selected."
			CPPCommercialProperty_Building payrollLimitationOrExclusion_CP_15_10 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			payrollLimitationOrExclusion_CP_15_10.setBuildingDescription("PayrollLimitationOrExclusion_CP_15_10");
			payrollLimitationOrExclusion_CP_15_10.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
			payrollLimitationOrExclusion_CP_15_10.getAdditionalCoverages().setBusinessIncomeCoverageForm_CoverageOptions(CoverageOptions.Coinsurance);
			payrollLimitationOrExclusion_CP_15_10.getAdditionalCoverages().setPayrollLimitationOrExclusion_CP_15_10(true);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(payrollLimitationOrExclusion_CP_15_10);
			break;
		case PeakSeasonLimitOfInsurance_CP_12_30:
			//Available when coverage ""Business Personal Property Coverage"" is selected.
			//Not available when ""Condominium Association Coverage Form CP 00 17"" is selected."
			CPPCommercialProperty_Building peakSeasonLimitOfInsurance_CP_12_30 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			peakSeasonLimitOfInsurance_CP_12_30.setBuildingDescription("PeakSeasonLimitOfInsurance_CP_12_30");
			peakSeasonLimitOfInsurance_CP_12_30.setCondominium(Condominium.NotACondominium);
			peakSeasonLimitOfInsurance_CP_12_30.getAdditionalCoverages().setPeakSeasonLimitOfInsurance_CP_12_30(true);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(peakSeasonLimitOfInsurance_CP_12_30);
			break;
		case PropertyDisclosureNotice_IDCP_10_0002:
			//RENEWAL FORM ONLY
			break;
		case ProtectiveSafeguards_CP_04_11:
			//Available when any of the following options are selected under the Details tab: "Automatic Fire System (P-1)", "Automatic Fire Alarm (P-2)", "Security Service (P-3)", "Service Contract (P-4)", "Automatic Commercial Cooking Exhaust and Extinguishing System (P-5)", or "Other (P-9)".
			CPPCommercialProperty_Building protectiveSafeguards_CP_04_11 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			protectiveSafeguards_CP_04_11.getExclusionsConditions().setSprinklerLeakageExclusion_CP_10_56(true);
			protectiveSafeguards_CP_04_11.setSprinklered(true);
			protectiveSafeguards_CP_04_11.setBuildingDescription("ProtectiveSafeguards_CP_04_11");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(protectiveSafeguards_CP_04_11);
			break;
		case RadioOrTelevisionAntennas_CP_14_50:
			//Available when class code 1190 34a or 1190 34b is selected.
			CPPCommercialProperty_Building radioOrTelevisionAntennas_CP_14_50 = new CPPCommercialProperty_Building(PropertyCoverages.PropertyInTheOpen);
			radioOrTelevisionAntennas_CP_14_50.setClassCode("1190 34a");
			radioOrTelevisionAntennas_CP_14_50.setBuildingDescription("RadioOrTelevisionAntennas_CP_14_50");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(radioOrTelevisionAntennas_CP_14_50);
			break;
		case RoofExclusionEndorsement_IDCP_31_1004:
			CPPCommercialProperty_Building roofExclusionEndorsement_IDCP_31_1004 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			roofExclusionEndorsement_IDCP_31_1004.getExclusionsConditions().setRoofExclusionEndorsement_IDCP_31_1004(true);
			roofExclusionEndorsement_IDCP_31_1004.setSprinklered(true);
			roofExclusionEndorsement_IDCP_31_1004.setBuildingDescription("RoofExclusionEndorsement_IDCP_31_1004");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(roofExclusionEndorsement_IDCP_31_1004);
			break;
		case SpoilageCoverage_CP_04_40:
			//Available when the coverage ""Business Personal Property Coverage"" is selected.
			//Not Available when ""Condominium Association Coverage Form CP 00 17"" is selected."
			CPPCommercialProperty_Building spoilageCoverage_CP_04_40 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			spoilageCoverage_CP_04_40.setCondominium(Condominium.UnitOwner);
			spoilageCoverage_CP_04_40.getAdditionalCoverages().setSpoilageCoverage_CP_04_40(true);
			spoilageCoverage_CP_04_40.setBuildingDescription("SpoilageCoverage_CP_04_40");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(spoilageCoverage_CP_04_40);
			break;
		case SprinklerLeakageExclusion_CP_10_56:
			CPPCommercialProperty_Building sprinklerLeakageExclusion_CP_10_56 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			sprinklerLeakageExclusion_CP_10_56.getExclusionsConditions().setSprinklerLeakageExclusion_CP_10_56(true);
			sprinklerLeakageExclusion_CP_10_56.setSprinklered(true);
			sprinklerLeakageExclusion_CP_10_56.setBuildingDescription("SpoilageCoverage_CP_04_40");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(sprinklerLeakageExclusion_CP_10_56);
			break;
		case TentativeRate_CP_99_93:
			//Tentative Rate
			CPPCommercialProperty_Building tentativeRate_CP_99_93 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			tentativeRate_CP_99_93.setRateType(RateType.Tentative);
			tentativeRate_CP_99_93.setBuildingDescription("TentativeRate_CP_99_93");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(tentativeRate_CP_99_93);
			break;
		case TheftExclusion_CP_10_33:
			CPPCommercialProperty_Building theftExclusion_CP_10_33 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			theftExclusion_CP_10_33.getExclusionsConditions().setTheftExclusion_CP_10_33(true);
			theftExclusion_CP_10_33.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Special);
			theftExclusion_CP_10_33.setBuildingDescription("TheftExclusion_CP_10_33");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theftExclusion_CP_10_33);
			break;
		case TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44:
			CPPCommercialProperty_Building theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44.getAdditionalCoverages().setTheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRisk_CP_10_44(true);
			theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44.getCoverages().setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Special);
			theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44.setSprinklered(true);
			theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44.setBuildingDescription("TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(theftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44);
			break;
		case UnscheduledBuildingPropertyTenantsPolicy_CP_14_02:
			//Available when ""Business Personal Property Coverage"" is selected and ""Building Coverages"" is not selected.
			//Not available when ""Building Coverage"" or ""Condominium Commercial Unit-Owners Coverage Form CP 00 18"" is selected."
			CPPCommercialProperty_Building unscheduledBuildingPropertyTenantsPolicy_CP_14_02 = new CPPCommercialProperty_Building(PropertyCoverages.BusinessPersonalPropertyCoverage);
			unscheduledBuildingPropertyTenantsPolicy_CP_14_02.setBuildingDescription("UnscheduledBuildingPropertyTenantsPolicy_CP_14_02");
			unscheduledBuildingPropertyTenantsPolicy_CP_14_02.setCondominium(Condominium.NotACondominium);
			unscheduledBuildingPropertyTenantsPolicy_CP_14_02.getAdditionalCoverages().setUnscheduledBuildingPropertyTenantsPolicy_CP_14_02(true);

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(unscheduledBuildingPropertyTenantsPolicy_CP_14_02);
			break;
		case UtilityServices_DirectDamage_CP_04_17:
			CPPCommercialProperty_Building utilityServices_DirectDamage_CP_04_17 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			utilityServices_DirectDamage_CP_04_17.getAdditionalCoverages().setUtilityServices_DirectDamage_CP_04_17(true);
			utilityServices_DirectDamage_CP_04_17.setSprinklered(true);
			utilityServices_DirectDamage_CP_04_17.setBuildingDescription("UtilityServices_DirectDamage_CP_04_17");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(utilityServices_DirectDamage_CP_04_17);
			break;
		case UtilityServices_TimeElements_CP_15_45:
			CPPCommercialProperty_Building utilityServices_TimeElements_CP_15_45 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			utilityServices_TimeElements_CP_15_45.getAdditionalCoverages().setBusinessIncomeCoverageForm(true);
			utilityServices_TimeElements_CP_15_45.getAdditionalCoverages().setUtilityServices_DirectDamage_CP_04_17(true);
			utilityServices_TimeElements_CP_15_45.getAdditionalCoverages().setUtilityServices_TimeElements_CP_15_45(true);
			utilityServices_TimeElements_CP_15_45.setSprinklered(true);
			utilityServices_TimeElements_CP_15_45.setBuildingDescription("UtilityServices_TimeElements_CP_15_45");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(utilityServices_TimeElements_CP_15_45);
			break;
		case VandalismExclusion_CP_10_55:
			CPPCommercialProperty_Building vandalismExclusion_CP_10_55 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			vandalismExclusion_CP_10_55.getExclusionsConditions().setVandalismExclusion_CP_10_55(true);
			vandalismExclusion_CP_10_55.setSprinklered(true);
			vandalismExclusion_CP_10_55.setBuildingDescription("VandalismExclusion_CP_10_55");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(vandalismExclusion_CP_10_55);
			break;
		case WindstormOrHailExclusion_CP_10_54:
			CPPCommercialProperty_Building windstormOrHailExclusion_CP_10_54 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			windstormOrHailExclusion_CP_10_54.getExclusionsConditions().setWindstormOrHailExclusion_CP_10_54(true);
			windstormOrHailExclusion_CP_10_54.setSprinklered(true);
			windstormOrHailExclusion_CP_10_54.setBuildingDescription("WindstormOrHailExclusion_CP_10_54");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(windstormOrHailExclusion_CP_10_54);
			break;
		case ProtectiveDeviceLetter_IDCP_11_0001:
			CPPCommercialProperty_Building protectiveDeviceLetter_IDCP_11_0001 = new CPPCommercialProperty_Building(PropertyCoverages.BuildingCoverage);
			protectiveDeviceLetter_IDCP_11_0001.setSprinklered(true);
			protectiveDeviceLetter_IDCP_11_0001.setBuildingDescription("ProtectiveDeviceLetter_IDCP_11_0001");

			policy.commercialPropertyCPP.getCommercialPropertyList().get(0).getCPPCommercialProperty_Building_List().add(protectiveDeviceLetter_IDCP_11_0001);
			break;
		}//edn switch

		return policy;

	}//end CreatFormsPolicyObject


	public List<CommercialPropertyForms> getExpectedForms(GeneratePolicy myPolicy) {

		//REQUIRED FORMS
		expectedFormsList.add(CommercialPropertyForms.CommercialPropertyConditions_CP_00_90);
		expectedFormsList.add(CommercialPropertyForms.CommercialPropertyCoveragePartDeclarationsPage_IDCP_03_0001);
		expectedFormsList.add(CommercialPropertyForms.ExclusionOfLossDueToVirusOrBacteria_CP_01_40);

		//AdditionalBuildingProperty_CP_14_15//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isAdditionalBuildingProperty_CP_14_15()) {
					expectedFormsList.add(CommercialPropertyForms.AdditionalBuildingProperty_CP_14_15);
				}
			}
		}

		//AdditionalCoveredProperty_CP_14_10//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isAdditionalCoveredProperty_CP_14_10()) {
					expectedFormsList.add(CommercialPropertyForms.AdditionalCoveredProperty_CP_14_10);
				}
			}
		}

		//AdditionalInsured_BuildingOwner_CP_12_19//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isAdditionalInsured_BuildingOwner_CP_12_19()) {
					expectedFormsList.add(CommercialPropertyForms.AdditionalInsured_BuildingOwner_CP_12_19);
				}
			}
		}

		//AdditionalPropertyNotCovered_CP_14_20//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isAdditionalPropertyNotCovered_CP_14_20()) {
					expectedFormsList.add(CommercialPropertyForms.AdditionalPropertyNotCovered_CP_14_20);
				}
			}
		}

		//BindingArbitration_CR_20_12//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isClientsProperty_CR_04_01() ||
				myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isEmployeeTheft() ||
				myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isForgeryOrAlteration()) {
			expectedFormsList.add(CommercialPropertyForms.BindingArbitration_CR_20_12);
		}

		//BrokenOrCrackedGlassExclusionForm_IDCP_31_1006//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isBrokenOrCrackedGlassExclusionForm_IDCP_31_1006()) {
					expectedFormsList.add(CommercialPropertyForms.BrokenOrCrackedGlassExclusionForm_IDCP_31_1006);
				}
			}
		}

		//BuildersRiskCoverageForm_CP_00_20//
		//BuildersRiskRenovations_CP_11_13//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCoverages().isBuildersRiskCoverageForm_CP_00_20()) {
					expectedFormsList.add(CommercialPropertyForms.BuildersRiskCoverageForm_CP_00_20);
				}
				if (building.getAdditionalCoverages().isBuildersRiskRenovations_CP_11_13()) {
					expectedFormsList.add(CommercialPropertyForms.BuildersRiskRenovations_CP_11_13);
				}
			}
		}

		//BuildingAndPersonalPropertyCoverageForm_CP_00_10//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCondominium().equals(Condominium.NotACondominium)) {
					expectedFormsList.add(CommercialPropertyForms.BuildingAndPersonalPropertyCoverageForm_CP_00_10);
				}
			}
		}

		//BurglaryAndRobberyProtectiveSafeguards_CP_12_11//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getBurglaryAndRobberyProtectiveSafeguards_CP_12_11().equals(FireBurglaryAlarmType.CentralStationWithKeys)) {
					expectedFormsList.add(CommercialPropertyForms.BurglaryAndRobberyProtectiveSafeguards_CP_12_11);
				}
			}
		}

		//BusinessIncome_AndExtraExpense_CoverageForm_CP_00_30//

		//BusinessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isBusinessIncomeCoverageForm() &&
						building.getAdditionalCoverages().getBusinessIncomeCoverageForm_CoverageOptions().equals(CoverageOptions.Coinsurance) &&
						building.getAdditionalCoverages().isBusinessIncome_LandlordAsAdditionalInsuredRentalValue_CP_15_03()) {
					expectedFormsList.add(CommercialPropertyForms.BusinessIncome_LandlordAsAdditionalInsured_RentalValue__CP_15_03);
				}
			}
		}

		//BusinessIncome_WithoutExtraExpense_CoverageForm_CP_00_32//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isBusinessIncomeCoverageForm()) {
					expectedFormsList.add(CommercialPropertyForms.BusinessIncome_WithoutExtraExpense_CoverageForm_CP_00_32);
				}
			}
		}

		//CausesOfLoss_BasicForm_CP_10_10//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCoverages().isBuildingCoverage()) {
					if (building.getCoverages().getBuildingCoverage_CauseOfLoss().equals(BuildingCoverageCauseOfLoss.Basic)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_BasicForm_CP_10_10);
					}
					if (building.getCoverages().getBuildingCoverage_CauseOfLoss().equals(BuildingCoverageCauseOfLoss.Broad)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_BroadForm_CP_10_20);
					}
					if (building.getCoverages().getBuildingCoverage_CauseOfLoss().equals(BuildingCoverageCauseOfLoss.Special)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_SpecialForm_CP_10_30);
					}
				} else if (building.getCoverages().isBusinessPersonalPropertyCoverage()) {
					if (building.getCoverages().getBusinessPersonalPropertyCoverage_CauseOfLoss().equals(BusinessPersonalPropertyCoverageCauseOfLoss.Basic)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_BasicForm_CP_10_10);
					}
					if (building.getCoverages().getBusinessPersonalPropertyCoverage_CauseOfLoss().equals(BusinessPersonalPropertyCoverageCauseOfLoss.Broad)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_BroadForm_CP_10_20);
					}
					if (building.getCoverages().getBusinessPersonalPropertyCoverage_CauseOfLoss().equals(BusinessPersonalPropertyCoverageCauseOfLoss.Special)) {
						expectedFormsList.add(CommercialPropertyForms.CausesOfLoss_SpecialForm_CP_10_30);
					}
				}
			}
		}

		//ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isEmployeeTheft()) {
			expectedFormsList.add(CommercialPropertyForms.ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29);
		}

		//ClientsProperty_CR_04_01//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isClientsProperty_CR_04_01()) {
			expectedFormsList.add(CommercialPropertyForms.ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29);
		}

		//CommercialCrimeManuscriptEndorsement_IDCR_31_1001//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isCommercialCrimeManuscriptEndorsement_IDCR_31_1001_Condition()) {
			expectedFormsList.add(CommercialPropertyForms.CommercialCrimeManuscriptEndorsement_IDCR_31_1001);
		}

		//CommercialPropertyManuscriptEndorsement_IDCP_31_1005//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isCommercialPropertyManuscriptEndorsement_IDCP_31_1005_Condition()) {
			expectedFormsList.add(CommercialPropertyForms.CommercialPropertyManuscriptEndorsement_IDCP_31_1005);
		}

		//CondominiumAssociationCoverageForm_CP_00_17//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCondominium().equals(Condominium.Association)) {
					expectedFormsList.add(CommercialPropertyForms.CondominiumAssociationCoverageForm_CP_00_17);
				}
			}
		}

		//CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCondominium().equals(Condominium.UnitOwner)) {
					expectedFormsList.add(CommercialPropertyForms.CondominiumAssociationCoverageForm_CP_00_17);
				}
			}
		}

		//ConvertToAnAggregateLimitOfInsurance_CR_20_08//
		CPPCommercialPropertyLine_Coverages propLine = myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages();
		if (propLine.isEmployeeTheft() || propLine.isForgeryOrAlteration() || propLine.isInsideThePremises_TheftOfMoneyAndSecurities() || propLine.isInsideThePremises_TheftOfOtherProperty_CR_04_05() || propLine.isOutsideThePremises()) {
			expectedFormsList.add(CommercialPropertyForms.ConvertToAnAggregateLimitOfInsurance_CR_20_08);
		}
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isGuestsPropertyValuation_CP_04_11()) {
					expectedFormsList.add(CommercialPropertyForms.ConvertToAnAggregateLimitOfInsurance_CR_20_08);
				}
			}
		}


		//DischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isDischargeFromSewerDrainOrSumpNotFlood_Related_CP_10_38()) {
					expectedFormsList.add(CommercialPropertyForms.DischargeFromSewer_DrainOrSump_NotFlood_Related_CP_10_38);
				}
			}
		}

		//DiscretionaryPayrollExpense_CP_15_04//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isDiscretionaryPayrollExpense_CP_15_04()) {
					expectedFormsList.add(CommercialPropertyForms.DiscretionaryPayrollExpense_CP_15_04);
				}
			}
		}

		//EquipmentBreakdownEnhancementEndorsement_IDCP_31_1002//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isEquipmentBreakdownEnhancementEndorsementID_CP_31_1002()) {
					expectedFormsList.add(CommercialPropertyForms.EquipmentBreakdownEnhancementEndorsement_IDCP_31_1002);
				}
			}
		}

		//ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isExcludeCertainRisksInherentInInsuranceOperations_CR_25_23()) {
			expectedFormsList.add(CommercialPropertyForms.ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23);
		}

		//ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01()) {
			expectedFormsList.add(CommercialPropertyForms.ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01);
		}

		//ExcludeDesignatedPremises_CR_35_13//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01()) {
			expectedFormsList.add(CommercialPropertyForms.ExcludeDesignatedPremises_CR_35_13);
		}

		//ExcludeSpecifiedProperty_CR_35_01//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineExclusionsConditions().isExcludeSpecifiedProperty_CR_35_01()) {
			expectedFormsList.add(CommercialPropertyForms.ExcludeSpecifiedProperty_CR_35_01);
		}

		//ExtraExpenseCoverageForm_CP_00_50//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isExtraExpenseCoverageForm_CP_00_50() && building.isSprinklered()) {
					expectedFormsList.add(CommercialPropertyForms.ExtraExpenseCoverageForm_CP_00_50);
				}
			}
		}

		//FunctionalBuildingValuation_CP_04_38//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isFunctionalBuildingValuation_CP_04_38() && building.isSprinklered()) {
					expectedFormsList.add(CommercialPropertyForms.FunctionalBuildingValuation_CP_04_38);
				}
			}
		}

		//GuestsProperty_CR_04_11//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isGuestsPropertyValuation_CP_04_11() && building.isSprinklered()) {
					expectedFormsList.add(CommercialPropertyForms.FunctionalBuildingValuation_CP_04_38);
				}
			}
		}

		//IdahoChanges_CR_02_12//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isEmployeeTheft()) {
			expectedFormsList.add(CommercialPropertyForms.IdahoChanges_CR_02_12);
		}

		//IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isIncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10()) {
			expectedFormsList.add(CommercialPropertyForms.IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10);
		}

		//InsideThePremises_TheftOfOtherProperty_CR_04_05//
		if (myPolicy.commercialPropertyCPP.getCommercialPropertyLine().getPropertyLineCoverages().isInsideThePremises_TheftOfOtherProperty_CR_04_05()) {
			expectedFormsList.add(CommercialPropertyForms.InsideThePremises_TheftOfOtherProperty_CR_04_05);
		}

		//LeasedProperty_CP_14_60//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isLeasedProperty_CP_14_60() && building.isSprinklered()) {
					expectedFormsList.add(CommercialPropertyForms.LeasedProperty_CP_14_60);
				}
			}
		}

		//LegalLiabilityCoverageForm_CP_00_40//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCoverages().isLegalLiabilityCoverageForm_CP_00_40()) {
					expectedFormsList.add(CommercialPropertyForms.LegalLiabilityCoverageForm_CP_00_40);
				}
			}
		}

		//LegalLiabilityCoverageSchedule_CP_DS_05//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getCoverages().isLegalLiabilityCoverageForm_CP_00_40()) {
					expectedFormsList.add(CommercialPropertyForms.LegalLiabilityCoverageSchedule_CP_DS_05);
				}
			}
		}

		//LimitationsOnCoverageForRoofSurfacing_CP_10_36//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isLimitationsOnCoverageForRoofSurfacing_CP_10_36()) {
					expectedFormsList.add(CommercialPropertyForms.LimitationsOnCoverageForRoofSurfacing_CP_10_36);
				}
			}
		}

		//LossPayableProvisions_CP_12_18//


		//OrdinanceOrLawCoverage_CP_04_05//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isOrdinanceorLawCoverage_CP_04_05()) {
					expectedFormsList.add(CommercialPropertyForms.LimitationsOnCoverageForRoofSurfacing_CP_10_36);
				}
			}
		}

		//OutdoorSigns_CP_14_40//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isOutdoorSigns_CP_14_40()) {
					expectedFormsList.add(CommercialPropertyForms.OutdoorSigns_CP_14_40);
				}
			}
		}

		//PayrollLimitationOrExclusion_CP_15_10//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isPayrollLimitationOrExclusion_CP_15_10()) {
					expectedFormsList.add(CommercialPropertyForms.PayrollLimitationOrExclusion_CP_15_10);
				}
			}
		}

		//PeakSeasonLimitOfInsurance_CP_12_30
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isPeakSeasonLimitOfInsurance_CP_12_30()) {
					expectedFormsList.add(CommercialPropertyForms.PeakSeasonLimitOfInsurance_CP_12_30);
				}
			}
		}

		//ProtectiveSafeguards_CP_04_11//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isProtectiveSafeguards_CP_04_11()) {
					expectedFormsList.add(CommercialPropertyForms.ProtectiveSafeguards_CP_04_11);
				}
			}
		}

		//RadioOrTelevisionAntennas_CP_14_50//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getClassCode().equals("1190 34a")) {
					expectedFormsList.add(CommercialPropertyForms.RadioOrTelevisionAntennas_CP_14_50);
				}
			}
		}

		//RoofExclusionEndorsement_IDCP_31_1004//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isRoofExclusionEndorsement_IDCP_31_1004()) {
					expectedFormsList.add(CommercialPropertyForms.ProtectiveSafeguards_CP_04_11);
				}
			}
		}

		//SpoilageCoverage_CP_04_40//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isSpoilageCoverage_CP_04_40()) {
					expectedFormsList.add(CommercialPropertyForms.SpoilageCoverage_CP_04_40);
				}
			}
		}

		//SprinklerLeakageExclusion_CP_10_56//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isSprinklerLeakageExclusion_CP_10_56()) {
					expectedFormsList.add(CommercialPropertyForms.SprinklerLeakageExclusion_CP_10_56);
				}
			}
		}

		//TentativeRate_CP_99_93//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getRateType().equals(RateType.Tentative)) {
					expectedFormsList.add(CommercialPropertyForms.TentativeRate_CP_99_93);
				}
			}
		}

		//TheftExclusion_CP_10_33//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isTheftExclusion_CP_10_33()) {
					expectedFormsList.add(CommercialPropertyForms.TheftExclusion_CP_10_33);
				}
			}
		}

		//TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isTheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRisk_CP_10_44()) {
					expectedFormsList.add(CommercialPropertyForms.TheftOfBuildingMaterialsAndSupplies_OtherThanBuildersRisk__CP_10_44);
				}
			}
		}

		//UnscheduledBuildingPropertyTenantsPolicy_CP_14_02//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isUnscheduledBuildingPropertyTenantsPolicy_CP_14_02()) {
					expectedFormsList.add(CommercialPropertyForms.UnscheduledBuildingPropertyTenantsPolicy_CP_14_02);
				}
			}
		}

		//UtilityServices_DirectDamage_CP_04_17//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isUtilityServices_DirectDamage_CP_04_17()) {
					expectedFormsList.add(CommercialPropertyForms.UtilityServices_DirectDamage_CP_04_17);
				}
			}
		}

		//UtilityServices_TimeElements_CP_15_45//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getAdditionalCoverages().isUtilityServices_TimeElements_CP_15_45()) {
					expectedFormsList.add(CommercialPropertyForms.UtilityServices_TimeElements_CP_15_45);
				}
			}
		}

		//VandalismExclusion_CP_10_55//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isVandalismExclusion_CP_10_55()) {
					expectedFormsList.add(CommercialPropertyForms.VandalismExclusion_CP_10_55);
				}
			}
		}

		//WindstormOrHailExclusion_CP_10_54//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.getExclusionsConditions().isWindstormOrHailExclusion_CP_10_54()) {
					expectedFormsList.add(CommercialPropertyForms.WindstormOrHailExclusion_CP_10_54);
				}
			}
		}

		//ProtectiveDeviceLetter_IDCP_11_0001//
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				if (building.isSprinklered()) {
					expectedFormsList.add(CommercialPropertyForms.ProtectiveDeviceLetter_IDCP_11_0001);
				}
			}
		}


		//MultipleDeductibleForm_IDCP_31_1001//
		List<String> deductibleList = new ArrayList<String>();
		for (CPPCommercialPropertyProperty property : myPolicy.commercialPropertyCPP.getCommercialPropertyList()) {
			for (CPPCommercialProperty_Building building : property.getCPPCommercialProperty_Building_List()) {
				for (PropertyCoverages coverage : building.getCoverages().getBuildingCoverageList()) {
					switch (coverage) {
					case BuildersRiskCoverageForm_CP_00_20:
						deductibleList.add(building.getCoverages().getBuildersRiskCoverageForm_CP_00_20_Deductible().getValue());
						break;
					case BuildingCoverage:
						deductibleList.add(building.getCoverages().getBuildingCoverage_Deductible().getValue());
						break;
					case BusinessPersonalPropertyCoverage:
						deductibleList.add(building.getCoverages().getBusinessPersonalPropertyCoverage_Deductible().getValue());
						break;
					case PropertyInTheOpen:
						deductibleList.add(building.getCoverages().getPropertyInTheOpen_Deductible().getValue());
						break;
					case PersonalPropertyOfOthers:
						deductibleList.add(building.getCoverages().getBusinessPersonalPropertyCoverage_Deductible().getValue());
						break;
					case CondominiumAssociationCoverageForm_CP_00_17:
						break;
					case CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18:
						break;
					case LegalLiabilityCoverageForm_CP_00_40:
						break;
					}
				}
			}
		}
		String compairTo = deductibleList.get(0);
		for (String deductible : deductibleList) {
			if (!compairTo.equals(deductible)) {
				expectedFormsList.add(CommercialPropertyForms.MultipleDeductibleForm_IDCP_31_1001);
				break;
			}
		}


		return expectedFormsList;
	}//end getExpectedForms(GeneratePolicy policy)


} //EOF




















