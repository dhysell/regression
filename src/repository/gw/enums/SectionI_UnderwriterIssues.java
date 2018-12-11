package repository.gw.enums;

public enum SectionI_UnderwriterIssues {
	
	PR001_10000Deductible("Section I deductible is set at <<Deductible>>.  Underwriting approval required to submit.  (PR001)", UnderwriterIssueType.BlockSubmit),
	PR002_PropertyValueGreaterThan750000("Section I: Property #<<property number>> with limit greater than $750,000 require underwriting approval to submit policy.  (PR002)", UnderwriterIssueType.BlockSubmit),
	PR003_Only4RentalUnitsAllowed("Section I: The rental property with 4 units require underwriting approval to issue the policy.  (PR003)",UnderwriterIssueType.BlockIssuance),
	PR004_Morethan1rentalunitperproperty("Section I: Property #<<property number>> on location #<<location number>> has more than 1 unit. Underwriting approval required to issue. (PR004)",UnderwriterIssueType.BlockIssuance),
	PR005_Additionalguncoveragehighlimit("Section I: The gun coverages has been increased by over $20,000 on property #<<Property number>> on location #<<location number>>. Underwriting approval required to submit.  (PR005)",UnderwriterIssueType.BlockIssuance),
	PR006_CovApropertypriorto1954("Section I: Property #<<property number>> on location #<<location number>> has Coverage A and was built prior to 1954. Underwriting approval required to issue policy. (PR006)",UnderwriterIssueType.BlockIssuance),
	
	PR007_Propertyover20yearsandCovE("Property <<property number>> on location <<location number>> has Coverage E with <<coverage type>> is older than 20 years. Underwriting approval required to issue policy. �(PR007)",UnderwriterIssueType.BlockIssuance),
	PR008_Addpropertyover1500000("Adding a property with limit over $1.5 million requires underwriting approval to issue policy. �(PR008)",UnderwriterIssueType.BlockIssuance),
	PR009_AddingEndorsement209("Access Yes Endorsement (UW I209) is added to policy. Underwriting approval required to issue policy. �(PR009)",UnderwriterIssueType.BlockIssuance),
	PR010_Vacantproperty("Property <<Property number>> on Location <<Location number>> is vacant. Underwriting approval required to issue policy. �(PR010)",UnderwriterIssueType.BlockIssuance),
	PR011_SquirePropertyshouldhaveatleastoneResidencePremisesCondominiumResidencePremisesResidencePremisesCovEContentsDwellingUnderConstructionorDwellingUnderConstructionCovE("Squire Section I must have either Residence Premises, Condominium Residence Premises, Residence Premises Cov E, Contents, Dwelling Under Construction or Dwelling Under Construction Cov E insured. Underwriting approval required to quote policy.  (PR011)",UnderwriterIssueType.BlockQuote),
	PR022_RemovingorChangetoLivestock("Changes to or removal of FPP Livestock will require Underwriting approval. (PR022)",UnderwriterIssueType.BlockIssuance),
	PR023_RemovingorChangetoCommodities("Changes to or removal of FPP Commodities will require Underwriting approval. (PR023)",UnderwriterIssueType.BlockIssuance),
	PR053_Squirechange("Line of business was changed from <<old offering>> to <<new offering>>. (PR053)",UnderwriterIssueType.BlockIssuance),
	PR054_Removingsection("<<Section>> removed from policy. (PR054)",UnderwriterIssueType.BlockIssuance),
	PR056_ANIchangetopolicy("Additional Named Insured change. (PR056)",UnderwriterIssueType.BlockIssuance),
	PR057_PNIorANIremoved("Named insured removed from Policy Member. (PR057)",UnderwriterIssueType.BlockIssuance),
	PR059_Policymembercontactchange("Contact detail for <<Policy member>> changed. (PR059)",UnderwriterIssueType.BlockIssuance),
	PR060_Newpropertyadded("New property <<property number>> added to policy. (PR060)",UnderwriterIssueType.BlockIssuance),
	PR061_Existingpropertyremoved("Squire: Property <<property number>> removed from policy. (PR061)",UnderwriterIssueType.BlockIssuance),
	PR064_Propertydetailchange("Squire: Property information on <<property number>> changed. (PR064)",UnderwriterIssueType.BlockIssuance),
	PR065_PropertyAdditionalinsuredchange("Squire: Additional Insured change on property <<property number>>. (PR065)",UnderwriterIssueType.BlockIssuance),
	PR067_SectionIdeductibleincrease("Squire: Section I deductible increased. (PR067)",UnderwriterIssueType.BlockIssuance),
	PR068_Coveragechange("Squire: Secction I Coverages was changed. (PR068)",UnderwriterIssueType.BlockIssuance),
	PR069_LiabilitylimitonFR("Liability limit increased to $1 million. (PR069)",UnderwriterIssueType.BlockIssuance),
	PR070_SectionIIcoveragedecrease("Squire: Liability limit decreased. (PR070)",UnderwriterIssueType.BlockIssuance),
	PR071_AccessYesremoved("Access Yes Endorsement 209 removed from policy. (PR071)",UnderwriterIssueType.BlockIssuance),
	PR051_UnclassifiedCovABuilding("Coverage A property or Contents has failed to be classified, underwriting review required to quote. (PR051)",UnderwriterIssueType.BlockQuote),
	PR085_TrelliswithWeightofIceandSnow("Weight of Ice and Snow added to Trellis, Underwriting approval required to bind. (PR085)",UnderwriterIssueType.BlockSubmit),
	PR087_BlockBindTrellisedHops("Trellised Hops exists on policy, must have Underwritng Approval to bind. (PR087)",UnderwriterIssueType.BlockSubmit),
	PR090_Defensibleornot("The defensible space is not maintained on property <<property number>>. Underwriting will need to see the space which is not maintained. (PR090)",UnderwriterIssueType.BlockIssuance),
	PR095_CLUEPropertynotordered("CLUE Property has not been ordered on this policy. Please order the report. (PR095)",UnderwriterIssueType.BlockIssuance),
	PR097_NoPropertyDetailentered("Policy: There are no properties added on property detail. Underwriter review required to quote. (PR097) ",UnderwriterIssueType.BlockQuote);
	String longDescription;
	UnderwriterIssueType type;
	
	private SectionI_UnderwriterIssues(String longDescription, UnderwriterIssueType type){
		this.longDescription = longDescription;
		this.type = type;
	}
	
	public String getLongDescription(){
		return longDescription;
	}
	
	public UnderwriterIssueType getUWIssueType() {
		return type;
	}
	
	public static SectionI_UnderwriterIssues valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replaceAll("-", "_");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
	
	
}
