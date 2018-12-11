package repository.gw.enums;

public class CommercialPropertyLine {


	//COVERAGES PROPERTY
	public enum Property {

		ClientsProperty_CR_04_01("Clients' Property CR 04 01"),
		ComdominiumAssociationCoverageForm_CP_00_17("Comdominium Association Coverage Form CP 00 17"),
		CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18("Condominium Commercial Unit-Owners Coverage Form CP 00 18"),
		ElectronicData("Electronic Data");

		String value;

		private Property(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	public enum ClientsProperty_CR_04_01Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");

		String value;

		private ClientsProperty_CR_04_01Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	//COVERAGES CRIME
	public enum Crime {

		EmployeeTheft("Employee Theft"),
		ForgeryOrAlteration("Forgery Or Alteration"),
		IncludeVolunteerWorkersOtherThanFundSolicitorsAsEmployees_CR_25_10("Include Volunteer Workers Other Than Fund Solicitors As Employees CR 25 10"),
		InsideThePremises_TheftOfMoneyAndSecurities("Inside The Premises � Theft Of Money And Securities"),
		InsideThePremises_TheftOfOtherProperty_CR_04_05("Inside The Premises � Theft Of Other Property CR 04 05"),
		JointLossPayable_CR_20_15("Joint Loss Payable CR 20 15"),
		OutsideThePremises("Outside The Premises"),
		RequiredCrimeInformation("Required Crime Information");

		String value;

		private Crime(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum EmployeeTheftDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");

		String value;

		private EmployeeTheftDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum InsideThePremises_TheftOfMoneyAndSecuritiesDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");

		String value;

		private InsideThePremises_TheftOfMoneyAndSecuritiesDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	
	public enum InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");

		String value;

		private InsideThePremises_TheftOfOtherProperty_CR_04_05Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum OutsideThePremisesDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");

		String value;

		private OutsideThePremisesDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum ProtectiveSafeguards_CP_04_11AlarmType {

		AutomaticFireSystemP_1("Automatic Fire System (P-1)"),
		AutomaticFireAlarmP_2("Automatic Fire Alarm (P-2)"),
		SecurityServiceP_3("Security Service (P-3)"),
		ServiceContractP_4("Service Contract (P-4)"),
		AutomaticCommercialCookingExhaustandExtinguishingSystemP_5("Automatic Commercial Cooking Exhaust and Extinguishing System (P-5)");


		String value;

		private ProtectiveSafeguards_CP_04_11AlarmType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum BurglaryAndRobberyProtectiveSafeguardsCP_12_11AlarmType {

		Central("Central Station with keys"),
		AutomaticFireAlarmP_2("Central Station without keys"),
		SecurityServiceP_3("Local Alarm");

		String value;

		private BurglaryAndRobberyProtectiveSafeguardsCP_12_11AlarmType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}

	public enum BurglaryAndRobberyProtectiveSafeguardsCP_12_11WtchmanType {

		Sevicewithinpremises("Service applying within the premises of the insured at all times when the premises are not regularly open for business and which will require at least hourly rounds and signal at least hourly to a central station location outside the insured�s plant or to a policy station provided there is at least one policeman on duty at the station at all times."),
		Servicedoesnotsignalbutregisters("Service which does not signal to an outside station but registers at least hourly on a watchman�s clock in the insured�s premises."),
		Servocedoesnotsignalorregister("Service which does not signal to an outside station or register on a watchman�s clock."),
		Other("Other");


		String value;

		private BurglaryAndRobberyProtectiveSafeguardsCP_12_11WtchmanType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	


	
	//EXCLUSIONS AND CONDITIONS
	public enum Exclusions {
		
		CommercialCrimeManuscriptEndorsementIDCR_31_1001("Commercial Crime Manuscript Endorsement IDCR 31 1001"),
		CommercialPropertyManuscriptEndorsementIDCP_31_1005("Commercial Property Manuscript Endorsement IDCP 31 1005"),
		ExcludeCertainRisksInherentInInsuranceOperationsCR_25_23("Exclude Certain Risks Inherent In Insurance Operations CR 25 23"),
		ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployeesCR_25_01("Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01"),
		ExcludeDesignatedPremisesCR_35_13("Exclude Designated Premises CR 35 13"),
		ExcludeSpecifiedPropertyCR_35_01("Exclude Specified Property CR 35 01"),
		ExcludeUnauthorizedAdvancesRequireAnnualAuditCR_25_25("Exclude Unauthorized Advances, Require Annual Audit CR 25 25"),
		ExclusionOfLossDueToVirusOrBacteriaCP_01_40("Exclusion Of Loss Due To Virus Or Bacteria CP 01 40");
		String value;

		private Exclusions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	
	public enum Conditions {

		BindingArbitrationCR_20_12("Binding Arbitration CR 20 12"),
		ChangeInControlOfTheInsured_NoticeToTheCompanyCR_20_29("Change In Control Of The Insured � Notice To The Company CR 20 29"),
		CommercialCrimeManuscriptEndorsementIDCR_31_1001("Commercial Crime Manuscript Endorsement IDCR 31 1001"),
		CommercialPropertyConditionsCP_00_90("Commercial Property Conditions CP 00 90"),
		CommercialPropertyManuscriptEndorsementIDCP_31_1005("Commercial Property Manuscript Endorsement IDCP 31 1005"),
		ConvertToAnAggregateLimitOfInsuranceCR_20_08("Convert To An Aggregate Limit Of Insurance CR 20 08"),
		IdahoChangesCR_02_12("Idaho Changes CR 02 12"),
		MultipleDeductibleFormIDCP_31_1001("Multiple Deductible Form IDCP 31 1001");

		String value;

		private Conditions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}












