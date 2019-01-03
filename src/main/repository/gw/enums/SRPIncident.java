package repository.gw.enums;

public enum SRPIncident {

	Speed("Speed"),
//	ChargeableAccident("Chargeable Accident"),
	UnpaidInfraction("Unpaid Infraction"),
	InattentiveDriving("Inattentive/Careless Driving"),
	StopOrYield("Stop or Yield Traffic Signal"),
	ALS("ALS"),
	NegligentDriving("Negligent Driving"),
	DragRacing("Drag Racing"),
	EludingPolice("Eluding Police"),
	RecklessDriving("Reckless Driving"),
	DUI("Driving Under the Influence"),
//	Other("Other (or not in list)"),
	Other("Other"),
	DUIWithheld("DUI With a Withheld Judgement"),
	ChemicalTestRefused("Chemical Test Refused"),
	RevokedSuspendedLicense("Revoked/Suspended License/Court Ordered Suspension"),
	DrivingWithoutLicense("Driving Without a License"),
	International("International Driver's License"),
	PassingSchoolBus("Passing Stopped School Bus"),
	LeavingAccident("Leaving the Scene Of An Accident"),
	VehicularHomicide("Vehicular Homicide"),
	FelonyManslaughter("Felony Manslaughter"),
	UnverifiableDrivingRecord("Unpaid Infraction"),
	NoProof("No Proof of Prior Insurance"),
	Waived("Waived");
	String value;
	
	private SRPIncident(String reason){
		value = reason;
	}
	
	public String getValue(){
		return value;
	}


}
