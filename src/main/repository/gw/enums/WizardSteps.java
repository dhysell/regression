package repository.gw.enums;

public enum WizardSteps {
	Qualifications("Qualifications"), 
	PolicyInfo("Policy Info"),
	PolicyMembers("Policy Members"),
	InsuranceScore("InsuranceScore"),
	PropertyLiability_Locations("PropertyLiability Locations"),
	PropertyLiability_PropertyDetail("PropertyLiability PropertyDetail"),
	PropertyLiability_Coverages("PropertyLiability Coverages"),
	PropertyLiability_CLUEProperty("PropertyLiability CLUEProperty"),
	PropertyLiability_LineReiew("PropertyLiability LineReiew"),
	BusinessownersLine("Businessowners Line"),
	Locations("Locations"),
	Buildings("Buildings"),
	Supplemental("Supplemental"),
	Modifiers("Modifiers"),
	PayerAssignment("Payer Assignment"),
	RiskAnalysis("Risk Analysis"),
	Quote("Quote"),
	Forms("Forms"),
	Payment("Payment"),
	Commercial_Property("Commercial Property"),
	CommercialProperty_PropertyLine(""),
	CommercialProperty_Property(""),
	CommercialProperty_Modifiers(""),
	General_Liability(""),
	GeneralLiability_Exposures(""),
	GeneralLiability_Coverages(""),
	GeneralLiability_Modifiers(""),
	Commercial_Auto(""),
	CommercialAuto_AutoLine(""),
	CommercialAuto_Garagekeepers(""),
	CommercialAuto_Vehicles(""),
	CommercialAuto_StateInfo(""),
	CommercialAuto_Drivers(""),
	CommercialAuto_CoveredVehicles(""),
	CommercialAuto_Modifiers(""),
	Commercial_InlandMarine(""),
	CommercialInlandMarine_AccountsReceivable(""),
	CommercialInlandMarine_BaileesCustomers(""),
	CommercialInlandMarine_CameraAndMusicalInstrumentDealers(""),
	CommercialInlandMarine_CommercialArticles(""),
	CommercialInlandMarine_ComputerSystems(""),
	CommercialInlandMarine_ContractorsEquipment(""),
	CommercialInlandMarine_Exhibition(""),
	CommercialInlandMarine_Installation(""),
	CommercialInlandMarine_MiscellaneousArticles(""),
	CommercialInlandMarine_MotorTruckCargo(""),
	CommercialInlandMarine_Signs(""),
	CommercialInlandMarine_TripTransit(""),
	CommercialInlandMarine_ValuablePapers(""),
	Vehicles("Vehicles"),
	RecreationalEquipment("Recreational Equipment"),
	Watercraft("Watercraft"), 
	FarmEquipment("Farm Equipment"), 
	PersonalProperty("Personal Property"),
	Cargo("Cargo"),
	Livestock("Livestock"),
	SectionIV_InlandMarine_ExclusionsAndConditions("Exclusions and Conditions"),
	MembershipType("Membership Type"),
	Members("Members");
	String value;
	
	private WizardSteps(String topic){
		value = topic;
	}
	
	public String getValue(){
		return value;
	}

	public static WizardSteps getEnumFromStringValue(String text){
		for(WizardSteps status: WizardSteps.values()){
			if(text.equalsIgnoreCase(status.value)){
				return status;
			}
		}
		return null;
	}
	
	
}
