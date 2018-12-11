package repository.gw.generate.custom;

import repository.gw.enums.CommercialAutoLine;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialAutoLine_Coverages {

	private boolean hasChanged = false;

	private boolean liability = true;
	private CommercialAutoLine.LiabilityLimit liabilityLimit = CommercialAutoLine.LiabilityLimit.FiveHundred500K;
	private CommercialAutoLine.DeductibleLiabilityCoverage deductibleLiabilityCoverageCA0301 = CommercialAutoLine.DeductibleLiabilityCoverage.FiveHundred500;
	private boolean medicalPaymentsCA9903 = true;
	private CommercialAutoLine.MedicalPaymentsLimit medicalPaymentsCA9903Limit = CommercialAutoLine.MedicalPaymentsLimit.FiveHundred500;
	private boolean hiredAutoLiability = true;
	private CommercialAutoLine.HiredAutoLiabilityLimitBI hiredAutoLiabilityLiabilityBI = CommercialAutoLine.HiredAutoLiabilityLimitBI.FiveHundred500K;
	private boolean hiredAutoCollision = false;
	private CommercialAutoLine.HiredAutoCollisionDeductible hiredAutoCollisionDeductible = CommercialAutoLine.HiredAutoCollisionDeductible.FiveHundred;
	private boolean hiredAutoComprehensive = false;
	private boolean nonOwnedAutoLiabilityBI = true;
	private CommercialAutoLine.NonOwnedAutoLiabilityBI nonOwnedliabilityBI = CommercialAutoLine.NonOwnedAutoLiabilityBI.FiveHundred500K;
	
	private List<HiredAutoState> hiredAutoState = new ArrayList<HiredAutoState>();
	
	private List<repository.gw.generate.custom.NonOwnedLiabilityStates> nonOwnedLiabilityState = new ArrayList<repository.gw.generate.custom.NonOwnedLiabilityStates>();
	
	public CPPCommercialAutoLine_Coverages() {
		this.hiredAutoState.add(new HiredAutoState());
		this.nonOwnedLiabilityState.add(new repository.gw.generate.custom.NonOwnedLiabilityStates());
	}




	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////

	public CommercialAutoLine.LiabilityLimit getLiabilityLimit() {
		return liabilityLimit;
	}

	public void setLiabilityLimit(CommercialAutoLine.LiabilityLimit liabilityLimit) {
		this.liabilityLimit = liabilityLimit;
	}

	public CommercialAutoLine.DeductibleLiabilityCoverage getDeductibleLiabilityCoverageCA0301() {
		return deductibleLiabilityCoverageCA0301;
	}

	public void setDeductibleLiabilityCoverageCA0301(CommercialAutoLine.DeductibleLiabilityCoverage deductibleLiabilityCoverageCA0301) {
		this.deductibleLiabilityCoverageCA0301 = deductibleLiabilityCoverageCA0301;
	}

	public boolean isMedicalPaymentsCA9903() {
		return medicalPaymentsCA9903;
	}

	public void setMedicalPaymentsCA9903(boolean medicalPaymentsCA9903) {
		this.medicalPaymentsCA9903 = medicalPaymentsCA9903;
	}

	public CommercialAutoLine.MedicalPaymentsLimit getMedicalPaymentsCA9903Limit() {
		return medicalPaymentsCA9903Limit;
	}

	public void setMedicalPaymentsCA9903Limit(CommercialAutoLine.MedicalPaymentsLimit medicalPaymentsCA9903Limit) {
		this.medicalPaymentsCA9903Limit = medicalPaymentsCA9903Limit;
	}

	public boolean isHiredAutoLiability() {
		return hiredAutoLiability;
	}

	public void setHiredAutoLiability(boolean checked) {
		this.hiredAutoLiability = checked;
	}

	public CommercialAutoLine.HiredAutoLiabilityLimitBI getHiredAutoLiabilityLiabilityBI() {
		return hiredAutoLiabilityLiabilityBI;
	}

	public void setHiredAutoLiabilityLiabilityBI(CommercialAutoLine.HiredAutoLiabilityLimitBI hiredAutoLiabilityLiabilityBI) {
		this.hiredAutoLiabilityLiabilityBI = hiredAutoLiabilityLiabilityBI;
	}

	public boolean isHiredAutoCollision() {
		return hiredAutoCollision;
	}

	public void setHiredAutoCollision(boolean checked) {
		this.hiredAutoCollision = checked;
	}

	public CommercialAutoLine.HiredAutoCollisionDeductible getHiredAutoCollisionDeductible() {
		return hiredAutoCollisionDeductible;
	}

	public void setHiredAutoCollisionDeductible(CommercialAutoLine.HiredAutoCollisionDeductible hiredAutoCollisionDeductible) {
		this.hiredAutoCollisionDeductible = hiredAutoCollisionDeductible;
	}

	public boolean isHiredAutoComprehensive() {
		return hiredAutoComprehensive;
	}

	public void setHiredAutoComprehensive(boolean checked) {
		this.hiredAutoComprehensive = checked;
	}

	public boolean isNonOwnedAutoLiabilityBI() {
		return nonOwnedAutoLiabilityBI;
	}

	public void setNonOwnedAutoLiabilityBI(boolean checked) {
		this.nonOwnedAutoLiabilityBI = checked;
	}

	public CommercialAutoLine.NonOwnedAutoLiabilityBI getNonOwnedliabilityBI() {
		return nonOwnedliabilityBI;
	}

	public void setNonOwnedliabilityBI(CommercialAutoLine.NonOwnedAutoLiabilityBI nonOwnedliabilityBI) {
		this.nonOwnedliabilityBI = nonOwnedliabilityBI;
	}
	
	public List<HiredAutoState> getHiredAutoState() {
		return hiredAutoState;
	}

	public void setHiredAutoState(List<HiredAutoState> hiredAutoState) {
		this.hiredAutoState = hiredAutoState;
	}
	
	public List<repository.gw.generate.custom.NonOwnedLiabilityStates> getNonOwnedLiabilityState() {
		return nonOwnedLiabilityState;
	}

	public void setNonOwnedLiabilityState(
			List<NonOwnedLiabilityStates> nonOwnedLiabilityState) {
		this.nonOwnedLiabilityState = nonOwnedLiabilityState;
	}

	public boolean isLiability() {
		return liability;
	}

	public void setLiability(boolean liability) {
		this.liability = liability;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}


}
