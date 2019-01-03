package repository.gw.enums;

public enum AdditionalInsuredTypeGL {
	CertificateHolderOnly("Certificate Holder Only"), 
	AdditionalInsured_CoownerOfInsuredPremises_CG_20_27("Additional Insured - Co-Owner Of Insured Premises CG 20 27"), 
	AdditionalInsured_ConcessionairesTradingUnderYourName_CG_20_03("Additional Insured - Concessionaires Trading Under Your Name CG 20 03"), 
	AdditionalInsured_ControllingInterest_CG_20_05("Additional Insured - Controlling Interest CG 20 05"), 
	AdditionalInsured_DesignatedPersonOrOrganization_CG_20_26("Additional Insured - Designated Person Or Organization CG 20 26"), 
	AdditionalInsured_EngineersArchitectsOrSurveyorsNotEngagedByTheNamedInsured_CG_20_32("Additional Insured - Engineers, Architects Or Surveyors Not Engaged By The Named Insured CG 20 32"), 
	AdditionalInsured_GrantorOfFranchise_CG_20_29("Additional Insured - Grantor Of Franchise CG 20 29"), 
	AdditionalInsured_GrantorOfLicenses_CG_20_36("Additional Insured - Grantor Of Licenses CG 20 36"), 
	AdditionalInsured_LessorOfLeasedEquipment_CG_20_28("Additional Insured - Lessor Of Leased Equipment CG 20 28"),
	AdditionalInsured_ManagersOrLessorsOfPremises_CG_20_11("Additional Insured - Managers Or Lessors Of Premises CG 20 11"),
	AdditionalInsured_MortgageeAssigneeOrReceiver_CG_20_18("Additional Insured - Mortgagee, Assignee Or Receiver CG 20 18"),
	AdditionalInsured_OwnersOrOtherInterestsFromWhomLandHasBeenLeased_CG_20_24("Additional Insured - Owners Or Other Interests From Whom Land Has Been Leased CG 20 24"),
	AdditionalInsured_OwnersLesseesOrContractors_CompletedOperations_CG_20_37("Additional Insured - Owners, Lessees Or Contractors - Completed Operations CG 20 37"),
	AdditionalInsured_OwnersLesseesOrContractors_ScheduledPersonOrOrganization_CG_20_10("Additional Insured - Owners, Lessees Or Contractors - Scheduled Person Or Organization CG 20 10"),
	AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizations_CG_20_12("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations CG 20 12"),
	AdditionalInsured_StateOrGovernmentalAgencyOrSubdivisionOrPoliticalSubdivision_PermitsOrAuthorizationsRelatingToPremises_CG_20_13("Additional Insured - State Or Governmental Agency Or Subdivision Or Political Subdivision - Permits Or Authorizations Relating To Premises CG 20 13"),
	AdditionalInsured_Vendors_CG_20_15("Additional Insured - Vendors CG 20 15"),
	AdditionalInsured_CoOwnerOfInsuredPremises_CG_20_27("Additional Insured - Co-owner Of Insured Premises CG 20 27");
	String value;
	
	private AdditionalInsuredTypeGL(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
