package repository.gw.enums;

public enum ChangeReason {
	AdditionalInsuredAdded("Additional Insured - Added"), 
	AdditionalInsuredRemoved("Additional Insured - Removed"), 
	AddressLocationAndMailing("Address - Location and Mailing"), 
	AddressLocationChange("Address - Location Change"), 
	AddressMailing("Address - Mailing"),
	BPPLimitsChanged("BPP Limits - Changed"), 
	BPPLimitsRemoved("BPP Limits - Removed"), 
	BuildingLimitsChanged("Building Limits Changed"), 
	BuildingOrBPPAdded("Building or BPP - Added"), 
	BuildingRemoved("Building Removed"), 
	CertificateOnlyRequest("Certificate Only Request"), 
	Deductible("Deductible"), 
	EndorsementLiabilityAdded("Endorsement - Liability Added"),
	EndorsementLiabilityRemoved("Endorsement - Liability Removed"), 
	EndorsementPropertyAdded("Endorsement - Property Added"), 
	EndorsementPropertyRemoved("Endorsement - Property Removed"), 
	ExpirationDateChange("Expiration Date Change"), 
	LiabilityLimits("Liability Limits"), 
	LienholderAndMortgagee("Lienholder and Mortgagee"), 
	LocationsAdded("Locations - Added"), 
	LocationsRemoved("Locations - Removed"),
	ModifierDiscountsAndSurcharges("Modifier (Discounts and Surcharges)"),
	MultipleChanges("Multiple Changes"),
	AnyOtherChange("Any Other Change"),
	AutoOnlyChange("Auto Only Change"),
	Other("Other");
	
	String value;
	
	private ChangeReason(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
