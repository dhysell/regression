package repository.gw.enums;

public enum PLUWIssues {

	Only4RentalAllowed("Only 4 Rental Units allowed", "The rental property with 4 units require underwriting approval to issue the policy"),
	GunCoverage20KMore("Gun coverage cannot be $20,000 or more", "The gun coverages has been increased by over $20,000"),
	CovABuildEarlier1954("Cov A building built earlier than 1954", "has Coverage A and was built prior to 1954. Underwriting approval required to issue policy"),
	DefensibleNot("Defensible Or Not", "The defensible space is not maintained on property"),
	CluePropertyNotOrder("CLUE Property not ordered (PR095_01)", "CLUE Property has not been ordered on this policy. Please order the report"),
	SectionIDed10KHigh("Section I deductible $10,000 or higher", "Section I deductible is set at 10000.  Underwriting approval required to submit"),
	HighPropLimitGreater750K("High property limit", "with limit greater than $750,000 require underwriting approval to submit policy"),
	HighPropLimitGreater250K("High property limit", "Any Property with a type of Contents, and limit greater than $250,000 requires underwriting approval to submit policy"),
	AccessYesEndoAdded("Access Yes endorsement added", "Access Yes Endorsement (UW I209) is added to policy. Underwriting approval required to issue policy"),
	MoreThan1RentalUnit("More than 1 rental unit per property", "has more than 1 unit. Underwriting approval required to issue"),
	VacantProperty("Vacant Property", "is vacant. Underwriting approval required to issue policy"),
	ANIChange("ANI change to policy", "Squire: Additional Named Insured added or removed. (PR056)"),
	SectionIDedIncrease("Section I deductible increase", "Squrie: Section I deductible increased"),
	Coveragechange("Coverage Change", "Coverage change"),
	IMCoverageChange("Inland Marine Coverage Change", "Inland Marine Coverage changed"),
	AContactChanged("Inland Marine Coverage Change", "Contact detail for "),
	PropertyAddInsuredchange("Property Additional insured change", "Additional Insured change on property"),
	NewPropertyAdded("New property added", "New property"),
	PropertyDetailChange("Property detail change", "Property detail on property"),
	ExistingPropRemoved("Existing property removed", "removed from policy"),
	RemoveSection("Removing Section", "Section"),
	TrellisedHopExists("Trellised Hops Exists","Trellised Hops exists on policy, must have Underwritng Approval to submit"),
	AtLeastOneResidence("Squire Property should have at least one Residence", "Section I: Squire Section I must have either Residence Premises, Condominium Residence Premises, Residence Premises Cov E, Contents, Dwelling Under Construction or Dwelling Under Construction Cov E insured. Underwriting approval required to quote policy.  (PR011)"),
	PropertyOver20YearsAndCovE("Property over 20 years and Cov E (PR007_01)", "which has Coverage E with Broad Form, is older than 20 years. Underwriting approval required to issue policy.  (PR007)"),
	HighValuePropertyAdded("High value property added", "Section I: Adding a property with limit over $1.5 million requires underwriting approval to issue policy.  (PR008)"),
	PNIOrANIRemoved("PNI or ANI removed (PR057_01)", "Squire: Named insured removed from Policy Member. (PR057)"),
	RemovingChangeToLivestockFPP("Removing or Change to Livestock (PR022_01)", "Section I: FPP Livestock coverage cannot be decreased or removed midterm. Please update the effective date or undo that change. (PR022)"),
	RemovingChangeToCommoditiesFPP("Removing or Change to Commodities (PR023_01)", "Section I: FPP Commodities coverage cannot be decreased or removed midterm. Please update the effective date or undo that change. (PR023)"),
	RiskOnAPropertyOrFPPBlank("Risk on a property or FPP is blank. (PR080_02)", "Section I: Risk must be entered on all properties and Farm Personal Property (if added).  Underwriter: please enter the correct value in the Risk field.  (PR080)"),
	//Section II block Submit and Block Issues
	DairyConfinementAdded("Dairy Confinement added", "Underwriter needs to review the application to add Dairy Confinement Coverage to the policy.  Please attach the application with the submission"),
	LiabilityLimitAtOneMillion("Liability limit at one million", "That's a lot of cheddar!  You better get an underwriter to look at that for you"),
	StateIsNotIdaho("State is not Idaho", "is outside the boundaries of Idaho, underwriting approval is required to submit this policy"),
	InsufficientAcreageForLOB("Insufficient acreage for line of business", "policy with less than 10 acres requires underwriting approval to issue policy"),
	IncidentalOccupancyCoverageExists("Incidental Occupancy coverage exists", "Incidental occupancy is selected, underwriting approval is required to issue policy"),
	WatercraftLengthOver40FT("Watercraft length of over 40", "Section II: Watercraft length is over 40 ft. (PGL015)"),
	CityAcreageOver10("City acreage over 10", "Total acreage on the City Squire policy exceeds 10 acres, underwriting approval is required to issue policy"),
	SectionIICoverageDecrease("Section II coverage decrease","Squire: Liability limit decreased. (PR070)"),

	// Section III 
	MVRForEachDriver("AUTO: MVR for each driver", "MVR not ordered for Driver"),
	PhysicalImpairmentEpilepsy("AUTO: Driver with physical impairment or epilepsy", "Driver has physical impairment(s) or epilepsy. Underwriting requires medical history to submit.(AU027)"),
	PassengerVehOlderThan20yrsCompColl("AUTO: Passenger vehicle older Than twenty years with comp and collision", "is older than 20 years and has material damage. Underwriting approval is required to issue.(AU018)"),
	VehicleUsedForBusiness("Vehicle(s) is Used for business purpose (contractor). Choose commuting miles as business on applicable vehicle(s)", "Contractor"),

	UsageAndTruckTypeSemiTrailer("Usage and Truck type for Semi-Trailer (AU043_01)","AUTO: At least one of the semi-trailer's usage and truck type must match that of existing farm trucks. (AU043)"),
	NonAllowedTickets("Non Allowed Tickets (AU084_01)","Auto: The applicant has tickets that require Underwriting Approval to submit (AU084)"),
	NoPrivatePassenferPickup("No Private Passenger or Pickups (AU012_01)","AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"),
    UnassignedDriverWithHighSRP("Higher SRP unassigned (AU086_01)", "AUTO: Policy has unassigned driver(s) with higher SRP than driver(s) assigned to vehicle. Consider moving the drivers or request Underwritering for approval. (AU086)"),
	DriverUnder16("AUTO: Driver under 16", "is under 16 years of age and is assigned"),
	MotorCycleDiscountNewBusiness("AUTO: Motorcycle Discount and New Business", "Motorcycle Discount and New Business: Motorcycle discount applied to the policy"),
	OutOfStateLicense("AUTO: Out of state license", "has out-of-state driver's license.  Underwriting approval is required to issue. (AU026)"),
	ShowCarExists("Show Car Exists (AU016_01)", "AUTO: There is a show car on the policy. (AU016)"),
	PolicyHasMotorCycleAndDriverLessThan25("AUTO: Policy has a motorcycle and driver under 25 not assigned to a motorcycle", "There is an underage driver on policy not assigned to motorcycle. Please review (AU080)"),
	UMUIMDisclosure("UM/UIM Disclosure", "Applicant/Insured has rejected Idaho Uninsured and/or Underinsured Motorist coverage. Complete rejection form IDCA 0002 and submit to Underwriting. (AU089)"),
	CLUEAutoNotOrdered("Auto report not ordered (AU091_01)", "Section III: CLUE Auto has not been ordered on this policy. Please order the report. (AU091)"),
	NotRatedDriver("Auto: Not-rated drivers", "is marked as not rated. Underwriting approval required to issue. (AU050)"),
	NoVIN("No VIN. System Generated","No VIN. System Generated"),
	FarmTruckOver50K("AUTO: Farm Trucks over 50,000 GVW", "is over 50,000 GVW. Underwriting approval required to issue policy. (AU038)"),
	SR22Charge("SR-22 Charge", "Agent has requested SR-22 be filed for"),
	ExcludedDriverwithout304("Excluded Driver without 304", "an excluded driver. Add 304 endorsement"),
	CitySquireWithFarmTruck("AUTO: City squire With Farm Truck", "Farm truck added to City Squire policy. Underwriting approval is required to submit.(AU021)"),
	GoodStudentDiscount("AUTO: Good Student Discount", "Good Student Discount added"),
	Auto55AndAliveDiscount("AUTO: 55 and Alive Discount", "Senior discount applied to vehicle"),
	WaivedMVRIncident("Waived MVR incident", "Requested MVR incident to be waived. Underwriter will need to wave their magic wand to approve it before you can submit policy. (AU069)"),
	NotValidLicenseMVR("Auto: Not valid license on MVR", "AUTO: The License Status on MVR for driver"),
	SRPGreaterThan15("SRP Greater than 15", "Auto: The applicant has an SRP score > 15. Underwriting approval is required to issue. (AU083)"),
	ShowCarMileageOver1000("AUTO: Show Car Mileage over 1000", "exceeds 1,000 miles. Underwriting approval is required to issue.(AU014)"),
	AddedOrRemovedCondition301("Added or removed condition 301", "Squire"),
	AddedRemoved310("310 added or removed", "Deletion of Material Damage Coverage Endorsement 310 change"),
	AdditionalInsuredChange("Auto Additional insured change", "Squire: Additional Insured change on Auto 1. (AU072)"),
	ChangeToDriverInfo("Change to driver info", "Squire: Driver info for driver"),
	LineLevelCoverageReducedRemoved("Line level coverage reduced or removed", "Squire: Line level coverage changed on policy. Underwriting approval required to issue policy. (AU060)"),
	VehicleLevelCoverageAddedForFirstTime("Vehicle level coverage added for first time", "Squire: Existing vehicle(s) is/are liability only and Comprehensive added to the policy. Underwriting approval required to submit issue policy. (AU062)"),
	SiblingPolicyLiabilityLimits("Sibling Policy Liability Limits", "Policy: Underwriter approval is needed for Sibling Policy with the chosen liability limits (AU092)"),
	AU097NoPriorInsurance("AU097 No Prior Insurance", "no prior auto insurance, charge appropriate SRP on this drivers self rating plan tab.(AU097)"),

	//Section IV
	ValidDeathOfLivestock("Valid Death of Livestock", "Livestock: Select Livestock Liability Coverage on Section II for a valid Death of Livestock Coverage on Section IV. (IM003)"),
	LivestockPerheadOver10K("Livestock per head over $10,000", "Livestock: What livestock is worth over $10,000? Limit per head for livestock over $10,000. Underwriting approval required to submit policy. (IM011)"),
	PersonalPropertyEqualOver20K("Personal Property equal to or over $20,000", "limit is over $20,000. Underwriting approval required to submit policy. (IM010)"),
	PersonalPropertyEqualOver50K("Personal Property equal to or over $50,000", "limit is over $50,000. Underwriting approval required to submit policy. (IM009)"),
	RecEquipEqualOver50K("Recreational Equipment equal to or over $50,000", "Recreational Equipment: $55,000.00! That's an expensive Snowmobile, brah! Underwriting approval required to submit policy. (IM004)"),
	AppraisalDateOver2yrs("Appraisal date older than 2 years", "is over 2 years old. Get a new one or reach out to an underwriter to issue policy. Peace out. (IM008)"),
	ClassIIICargoRaduisOver500("Class III Cargo radius over 500", "Cargo: This cargo is going places. Class III Cargo Milk has radius over 500. Underwriting approval required to issue policy. (IM012)"),
	PersonalWatercraftequalOver10K("Personal Watercraft equal to or over $15,000", "Watercraft: Personal watercraft with limit equal to or over $15,000. Underwriting approval required to issue policy. (IM006)"),
	WatercraftEqualOver65K("Watercraft equal to or over $65,000", "Watercraft: Boat and Motor has limit equal to or over $65,000. Underwriting approval required to issue policy. (IM007)"),
	RecEquipmentEqualMore15K("Recreational Equipment equal or more than $15,000", "Recreational Equipment: $16,000.00 is equal to or over $15,000. Underwriting approval required to issue policy. (IM005)"),
	MissingWagonCarriagePhotoYear("Missing Wagon/Carriage Photo year", "Inland Marine: Photos are required for Wagons/Carriages"),
	InlandMarinCoverageChange("Inland Marine Coverage Change", "Squire: Inland Marine Coverage changed. (IM015)"),
	CargoOnCitySquire("Cargo on City Squire","Policy: Cargo on City Squire Policy. (IM021)"),
	NewJewelryPhotosAndAppraisal("New Jewelry Photos and Appraisal (IM001_01)", "New Jewelry: New jewelry PersonalEquipment #1 added to policy, submit photos and appraisal to underwriting before policy can be issued."),
	SectionIVwithoutSectionsIAndII("Section IV without Sections I and II (IM022_01)","Section IV exists without Sections I and II: Section IV exists without Sections I and II (IM022)." ),
	
	// umbrella 
	LimitOver2Million("Limit over $2 million", "Umbrella: A request for limit over $2 million require underwriting approval to quote. (UB008)"),
	SRPof16OrMore("has an SRP of 16 or More", "has an SRP over 15. Underwriting approval required to submit policy. (UB003)"),
	NoAutoUmbrella("No Auto Umbrella", "Umbrella: The underlying squire policy lacks a vehicle. Underwriting approval required to submit policy. (UB007)"),
	DUIOnSquire("DUI on Squire", "has a DUI on record. Underwriting approval required to submit policy. (UB004)"),
	PolicyChange("Policy Change", "Umbrella: A policy change on Umbrella must be reviewed by Underwriting Supervisor. (UB012)"),


	//Policy Level
	SquirePNIUnder18("Squire PNI under age of 18", "Policy: The policy holder must be over the age of 17. (SQ008)"),
	MissingMSYear("Missing MS Year (SQ021_01)","Property: MS year is required for Property #<<property number>> on Location #<<location number>> at policy issuance. (SQ021)"),
	MissingPhotoYear("Missing Photo Year (SQ022_01)", "Property: Photo year is required for Property #<<property number>> on Location #<<location number>> at policy issuance. (SQ022)"),
	MSYearOver3Yrs("MS Year Over 3 Years", "cannot be more than 3 years prior."),
	PhotoYearOver6Yrs("Photo Year Over 6 Years", "cannot be more than 6 years prior."),
	TenmillionPolicy("$10 million policy", "Policy: The category value is over $10 million. Underwriting approval required to quote policy. (SQ041)"),
	HighPropertyLimit("High property limit", "Section I: Property #1 with limit greater than $750,000 require underwriting approval to submit policy.  (PR002)"),
	TenDaySubmittingAuthority("10 day submitting authority", "Policy: Effective Date falls outside of 10 day submitting authority. Underwriting must approve this policy. Please provide appropriate documentation."),
	PriorLosses("Prior losses", "Policy: Applicant has prior losses. (SQ016)"),
	DesignatedOutOfState("Designated Out-of-State", "Policy: The mailing address for the policy is out of state. (SQ042)"),
	OrganizationTypeOfOther("Organization type of other (SQ004_01)", "Policy: The organization type is other on the policy. Underwriting approval is required to issue policy. (SQ004)"),
    TermTypeOther("Term Type Other", "Policy: The term of the policy is set to other. Underwriting approval required to issue policy. (SQ002)"),
	HighProtectionClass("High Protection Class", "Underwriting review Protection Class. (SQ043)"),
	ManualPCCodeChanged("Manual PC Code Changed", "Underwriting review Protection Class. (SQ044)"),
	InsuranceScoreLessThan500("Insurance Score Less Than 500", "has Insurance Score less than or equal to 500. Underwriting approval required to submit. (SQ005)"),
	CountryFRAutoOnly("Country, or Farm and Ranch Squire Auto only", "Squire policy with Auto only requires UW Approval. (SQ010)"),
	StopSubmit("Stop Submit", "Policy: There is an existing (Squire) on the account. Cannot submit this policy until the (Squire) has been cancelled. (SQ049)"),
	SiblingANI("Sibling ANI", "Policy: Additional Named Insured added on Sibling policy. (SQ051)"),
	SiblingNotSingle("Sibling and Not Single", "Policy: The Primary Named Insured is marred on a sibling policy. Underwriting review required. (SQ053)"),
	ISRBNotReturnAddress("ISRB not return address", "Could result in a protection class change. Contact UW with details. (SQ026)"),
	ExistingLocationChange("Existing Location Change", "address was changed; this could lead to change in PC Code. Underwriting review Protection Class. (SQ045)"),
	UWClaimItemChange("UW claim item change", "Policy: You are initiating a policy change prior to a date of loss. Request must be approved by underwriting."),
	AgentClaimItemChange("Agent claim item change", "Policy: You are initiating a policy change prior to a date of loss. Request must be approved by underwriting."),
	AgentLossRatioChange("Agent Loss Ratio Change", "Risk Analysis: The Loss Ratio was changed. Underwriting approval required to submit policy. (SQ048)"),


    //Membership Dues
	AdditionalInterestOnMembershipDues("Additional Interest payer on membership dues", "Review Payer Assignment - Policy has premium charged to the insured. Membership dues should be charged to the insured. (SQ064)"),
	MembershipDuesOnPayerAssignment("Additional Interest payer on membership dues", "All Policy premium is being charged to the Additional Interest. Change payer to the Additional Interest for membership dues on the Payer Assignment screen. (SQ065)"),
	MembershipDuesOnPayerAssigmmentForChange("Additional Interest payer on membership dues", "All Policy premium is being charged to the Additional Interest.  Review Payer Assignment and add additional interest as renewal payer to membership dues (if applicable). (SQ066)"),

	
	//Standard Fire
	SupportingBusinessDeclarationPagesNeeded("Supporting business declaration pages needed", "Please submit copy of the supporting business declaration pages with submission"),

	
	//Center down
	ClaimCenterDown("ClaimCenter Down", "Policy: ClaimCenter was down. Please verify claims and contact the Help Desk.");
	
	String shortdesc;
	String longdesc;

    PLUWIssues(String shortdesc, String longdesc) {
		this.shortdesc = shortdesc;
		this.longdesc = longdesc;
	}

	public String getShortDesc() {
		return this.shortdesc;
	}

	public String getLongDesc(){
		return this.longdesc;
	}
}
