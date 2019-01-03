package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum PreRenewalDirectionExplanation {

	LossRatioOf70Percent_SectionI("Section I loss ratio of 70%", "Section I  has a loss ratio of 70% in three consecutive terms"),
	LossRatioOf70Percent_SectionII("Section II loss ratio of 70%", "Section II has a loss ratio of 70% in three consecutive terms"),
	LossRatioOf70Percent_SectionIII("Section III loss ratio of 70%", "Section III has a loss ratio of 70% in three consecutive terms"),
	LossRatioOf70Percent_SectionIV("Section IV loss ratio of 70%", "Section IV has a loss ratio of 70% in three consecutive terms"),
	VehicleWithSRP21OrOver("Vehicle with SRP 21 or Over", "Policy has one or more vehicle with SRP of 21 or more"),
	CoverageLimitOfOver1Million("Coverage Limit of Over $1 Million", "Policy has coverage limit of over $1 million"),
	TwoOrMoreClaimsOfTheSameType("Two or More Claims of the Same Type", "Policy has had two or more claim of same type in three consequent terms"),
	ClaimOf50000OrMore("Claim of $50,000 or More", "Policy has claim of $50,000 or more in current term"),
	PolicyIsManuallyRated("Policy is Manually Rated", "Policy is manually rated"),
	ManualProtectionClass("Manual Protection Class", "Protection class is manually entered on policy"),
	PropertyPhotosNeeded("Photos Needed", "Policy has property(ies) that require new photo"),
	MarshallAndSwiftNeeded("Marshall and Swift Needed", "Policy has property(ies) that require new Marshall & Swift"),
	LimitOver5Million("Limit Over $5 Million", "Policy has limit over $5 million"),
	VehicleWithSRP15OrOver("Vehicle with SRP 15 or Over", "Squire has vehicle with SRP 15 or more"),
	CategoryCodeValuationOver10Million("Category code valuation over $10 Million", "Policy has Category code valuation of over $10 million"),
	NotRatedDriverWithEarlyFollowUpDate("Not Rated Driver with Early Follow-up Date", "Policy has not rated driver with follow-up date before the renewal effective date"),
	JewelryPhotosRequired("Jewelry Photos Required", "Policy has Jewelry that require photo"),
	JewelryPhotoUpdateRequired("Jewelry Photo Update Required", "Policy has Jewelry and require photo for renewal"),
	CustomFarmingAmountUpdateRequired("Custom Farming Amount Update Required", "Policy has Custom Farming and require amount updated on Renewal Information screen"),
	FollowUpWithAgentforCustomFarming("Follow Up With Agent for Custom Farming", "Policy has Custom Farming. Follow-up with agent to get updated amount"),
	FarmTruckShowCarUpdateRequired("Farm Truck/Show Car Update Required", "Policy has Farm Truck or/and Show car and require odometer updated on Renewal Information screen"),
	FollowUpWithAgentForFarmTruckShowCar("Follow Up With Agent for Farm Truck/Show Car", "Policy has Farm Truck or/and Show car. Follow-up with agent to get updated odometer"),
	VacantProperty("Vacant Property", "Policy has a property that is vacant"),
	RatingCountyandPrimaryLocationCountyMismatch("Rating County and Primary Location County Mismatch", "The billing county rating county and the primary location county are not the same"),
	OverrideCategoryCode("Override Category Code", "The category code is overriden on property(ies)"),
	SiblingPolicyChange("Sibling Policy Change", "The policy is no longer eligible for Sibling policy. Please review the policy."),
	PolicyRewrittenToNewAccount("Policy rewritten to new account", "The policy was rewritten to new account. Please review the policy discounts and Loss Ratio to ensure they are correct"),
	ClassIIICarGoOnPolicy("Class III Cargo on policy", "Review Cargo Class III coverages"),
	DeathOfLivestockOnPolicy("Death of Livestock on Policy", "Review Death of Livestock coverage"),
	OutOfStateDriversLicense("Out-of-State Driver\'s License", "Out-of-state driver's license on policy");
	String code;
	String description;
	
	
	private PreRenewalDirectionExplanation(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getDescription(){
		return description;
	}
	
	public static PreRenewalDirectionExplanation getEnum(String codeOrDescription) {
		for(PreRenewalDirectionExplanation explanation : values()) {
			if(explanation.getCode().equals(codeOrDescription) || explanation.getDescription().equals(codeOrDescription)) {
				return explanation;
			}
		}
		return null;
	}
	
	private static final List<PreRenewalDirectionExplanation> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final Random RANDOM = new Random();

	public static PreRenewalDirectionExplanation random()  {
		return VALUES.get(RANDOM.nextInt(VALUES.size()));
	}
	
	
	
	
	
}
