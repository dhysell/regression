package repository.gw.generate.custom;

import repository.gw.enums.CommercialAutoLine;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialAutoLine_AdditionalCoverages {
	
	private boolean hasChanged = false;
	
	private boolean otherCarCompresensive = false;
	private CommercialAutoLine.OtherCarComprehensive comprehensive = CommercialAutoLine.OtherCarComprehensive.FiveHundred;
	private boolean otherCarLiability = false;
	private boolean otherCarMedicalPayments = false;
	private boolean otherCarUnderinsuredMotorist = false;
	private CommercialAutoLine.OtherCarUnderinsuredMotorist underinsuredMotorist = CommercialAutoLine.OtherCarUnderinsuredMotorist.FiftyThousand;
	private boolean otherCarUninsuredMotorist = false;
	private CommercialAutoLine.OtherCarUninsuredMotorist uninsuredMotorist = CommercialAutoLine.OtherCarUninsuredMotorist.FiftyThousand;
	private boolean otherCarCollision = false;
	private CommercialAutoLine.OtherCarCollision collision = CommercialAutoLine.OtherCarCollision.FiveHundred;
	private boolean mcs_90 = false;
	
	private boolean CoverageForCertainOperationsInConnectionWithRailroadsCA2070 = false;
	private int railRoadCA2070_Premium = 100;
	private String ScheduledRailroad = "Something Goes Here";
	private String DesignatedJobSite = "Another Thing Goes Here";
	
	List<String> otherCarNamedIndividuals = new ArrayList<String>();
	
	public CPPCommercialAutoLine_AdditionalCoverages() {
		this.otherCarNamedIndividuals.add("Carmen");
	}

	public boolean isOtherCarCompresensive() {
		return otherCarCompresensive;
	}

	public void setOtherCarCompresensive(boolean otherCarCompresensive) {
		this.otherCarCompresensive = otherCarCompresensive;
	}

	public CommercialAutoLine.OtherCarComprehensive getComprehensive() {
		return comprehensive;
	}

	public void setComprehensive(CommercialAutoLine.OtherCarComprehensive comprehensive) {
		this.comprehensive = comprehensive;
	}

	public boolean isOtherCarLiability() {
		return otherCarLiability;
	}

	public void setOtherCarLiability(boolean otherCarLiability) {
		this.otherCarLiability = otherCarLiability;
	}

	public boolean isOtherCarMedicalPayments() {
		return otherCarMedicalPayments;
	}

	public void setOtherCarMedicalPayments(boolean otherCarMedicalPayments) {
		this.otherCarMedicalPayments = otherCarMedicalPayments;
	}

	public boolean isOtherCarUnderinsuredMotorist() {
		return otherCarUnderinsuredMotorist;
	}

	public void setOtherCarUnderinsuredMotorist(boolean otherCarUnderinsuredMotorist) {
		this.otherCarUnderinsuredMotorist = otherCarUnderinsuredMotorist;
	}

	public CommercialAutoLine.OtherCarUnderinsuredMotorist getUnderinsuredMotorist() {
		return underinsuredMotorist;
	}

	public void setUnderinsuredMotorist(
			CommercialAutoLine.OtherCarUnderinsuredMotorist underinsuredMotorist) {
		this.underinsuredMotorist = underinsuredMotorist;
	}

	public boolean isOtherCarUninsuredMotorist() {
		return otherCarUninsuredMotorist;
	}

	public void setOtherCarUninsuredMotorist(boolean otherCarUninsuredMotorist) {
		this.otherCarUninsuredMotorist = otherCarUninsuredMotorist;
	}

	public CommercialAutoLine.OtherCarUninsuredMotorist getUninsuredMotorist() {
		return uninsuredMotorist;
	}

	public void setUninsuredMotorist(CommercialAutoLine.OtherCarUninsuredMotorist uninsuredMotorist) {
		this.uninsuredMotorist = uninsuredMotorist;
	}

	public boolean isOtherCarCollision() {
		return otherCarCollision;
	}

	public void setOtherCarCollision(boolean otherCarCollision) {
		this.otherCarCollision = otherCarCollision;
	}

	public CommercialAutoLine.OtherCarCollision getCollision() {
		return collision;
	}

	public void setCollision(CommercialAutoLine.OtherCarCollision collision) {
		this.collision = collision;
	}

	public boolean isMcs_90() {
		return mcs_90;
	}

	public void setMcs_90(boolean mcs_90) {
		this.mcs_90 = mcs_90;
	}

	public List<String> getOtherCarNamedIndividuals() {
		return otherCarNamedIndividuals;
	}

	public void setOtherCarNamedIndividuals(List<String> otherCarNamedIndividuals) {
		this.otherCarNamedIndividuals = otherCarNamedIndividuals;
	}

	public boolean isCoverageForCertainOperationsInConnectionWithRailroadsCA2070() {
		return CoverageForCertainOperationsInConnectionWithRailroadsCA2070;
	}

	public void setCoverageForCertainOperationsInConnectionWithRailroadsCA2070(boolean coverageForCertainOperationsInConnectionWithRailroadsCA2070) {
		CoverageForCertainOperationsInConnectionWithRailroadsCA2070 = coverageForCertainOperationsInConnectionWithRailroadsCA2070;
	}

	public int getRailRoadCA2070_Premium() {
		return railRoadCA2070_Premium;
	}

	public void setRailRoadCA2070_Premium(int railRoadCA2070_Premium) {
		this.railRoadCA2070_Premium = railRoadCA2070_Premium;
	}

	public String getScheduledRailroad() {
		return ScheduledRailroad;
	}

	public void setScheduledRailroad(String scheduledRailroad) {
		ScheduledRailroad = scheduledRailroad;
	}

	public String getDesignatedJobSite() {
		return DesignatedJobSite;
	}

	public void setDesignatedJobSite(String designatedJobSite) {
		DesignatedJobSite = designatedJobSite;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}
	
	
	
	
	
	
	
}















