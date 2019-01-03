package repository.gw.enums;

public enum ValidationRules_SectionIII {
	
	
	////////////////////////////////////////////
	//THESE ARE NOT THE FULL MESSAGES. DID NOT UPDATE THEM CUS MOST COULD NOT BE FOUND IN REQUIREMENTS
	/////////////////////////////////////////////
	AU020SRPHigherThan25("Driver is ineligible because they have exceeded the allowed number of SRP points."),
	AU046BusRule("That's some light weight Tractor Truck. (If the GVW is equal or less than 50,000, its not a Tractor Type Truck) (AU046)"),
	AU051BusRule("Collision deductible cannot be less than Comprehensive deductible"),
	AU052BusRule("Show car vehicles must have the following coverages: Liability, Comprehensive, and Collision"),
	AU068UnassignedMVRIncident("MVR incidents remain to be assigned for Driver ${Name here}. Assign to proceed. (AU068)"),
	AU077BusRule("The deductibles and/or limits for the following coverages has been changed and will be applied to all other vehicles: Collision Coverage, Comprehensive Coverage"),
	AU082BusRule("The deductibles and/or limits for the following coverages has been changed and will be applied to all other vehicles: Rental Reimbursement Coverage"),
	AU094BusRule("No Driver Assigned"),
//	AU094BusRule("The vehicle must either have a driver assigned or have unassigned driver checked"),
	AU040BusRule("Commuting Miles (one way) is required"),
	AU090BusRule("Driver is ineligible because they have a vehicular homicide");
	String value;
	
	ValidationRules_SectionIII(String value) {
		this.value = value;
	}
	
	
	public String getMessage(){
		return value;
	}

}
