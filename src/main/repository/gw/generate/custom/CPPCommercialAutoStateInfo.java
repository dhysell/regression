package repository.gw.generate.custom;

import repository.gw.enums.StateInfo;

public class CPPCommercialAutoStateInfo {
	
	private boolean hasChanged = false;

	// COVERAGES
	private boolean underinsuredMotoristCA3118 = true;
	private StateInfo.Un_UnderInsuredMotoristLimit underInsuredLimit = StateInfo.Un_UnderInsuredMotoristLimit.OneHundredThousand100K;
	private boolean uninsuredMotoristCA3115 = true;
	private StateInfo.Un_UnderInsuredMotoristLimit uninsueredMotorist = StateInfo.Un_UnderInsuredMotoristLimit.OneHundredThousand100K;

	// EXCLUSIONS AND CONDITIONS
	private boolean idahoChangesCA0118 = false;

	//////////////////////////////////
	//// CONSTRUCTORS ////////
	//////////////////////////////////

	public CPPCommercialAutoStateInfo() {

	}

	public CPPCommercialAutoStateInfo(boolean underinsuredMotoristCA3118,
                                      StateInfo.Un_UnderInsuredMotoristLimit underInsuredLimit, boolean uninsuredMotoristCA3115,
                                      StateInfo.Un_UnderInsuredMotoristLimit uninsueredMotorist) {
		this.underinsuredMotoristCA3118 = underinsuredMotoristCA3118;
		this.underInsuredLimit = underInsuredLimit;
		this.uninsuredMotoristCA3115 = uninsuredMotoristCA3115;
		this.uninsueredMotorist = uninsueredMotorist;
	}

	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////

	public boolean isUnderinsuredMotoristCA3118() {
		return underinsuredMotoristCA3118;
	}

	public void setUnderinsuredMotoristCA3118(boolean underinsuredMotoristCA3118) {
		this.underinsuredMotoristCA3118 = underinsuredMotoristCA3118;
	}

	public StateInfo.Un_UnderInsuredMotoristLimit getUnderInsuredLimit() {
		return underInsuredLimit;
	}

	public void setUnderInsuredLimit(StateInfo.Un_UnderInsuredMotoristLimit underInsuredLimit) {
		this.underInsuredLimit = underInsuredLimit;
	}

	public boolean isUninsuredMotoristCA3115() {
		return uninsuredMotoristCA3115;
	}

	public void setUninsuredMotoristCA3115(boolean uninsuredMotoristCA3115) {
		this.uninsuredMotoristCA3115 = uninsuredMotoristCA3115;
	}

	public StateInfo.Un_UnderInsuredMotoristLimit getUninsueredMotorist() {
		return uninsueredMotorist;
	}

	public void setUninsueredMotorist(StateInfo.Un_UnderInsuredMotoristLimit uninsueredMotorist) {
		this.uninsueredMotorist = uninsueredMotorist;
	}

	public boolean isIdahoChangesCA0118() {
		return idahoChangesCA0118;
	}

	public void setIdahoChangesCA0118(boolean idahoChangesCA0118) {
		this.idahoChangesCA0118 = idahoChangesCA0118;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

}
