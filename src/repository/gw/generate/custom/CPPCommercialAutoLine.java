package repository.gw.generate.custom;

import repository.gw.enums.CommercialAutoLine;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialAutoLine {
	
	private boolean hasChanged = false;

	// COVERAGES
	private CPPCommercialAutoLine_Coverages  commercialAutoLine_Coverages = new CPPCommercialAutoLine_Coverages();

	// ADDITIONAL COVERAES
	private CPPCommercialAutoLine_AdditionalCoverages commercialAutoLine_AdditionalCoverages = new CPPCommercialAutoLine_AdditionalCoverages();

	// EXCLUTIONS AND CONDITIONS
	private boolean exclutionsAndConditions_HasChanged = false;
	private List<CommercialAutoLine.CALineExclutionsAndConditions> exclutionsAndConditions = new ArrayList<CommercialAutoLine.CALineExclutionsAndConditions>();

	// ADDITIONAL INSURED CERTIFICATE HOLDERS
	private boolean additionalInterest_HasChanged = false;
	List<AdditionalInsured_CommercialAuto> additionalInterest = new ArrayList<AdditionalInsured_CommercialAuto>();
	
	// UNDERWRITING QUESTIONS
	private boolean hasApplicantLeasedGreaterThan6Months = false;
	private boolean doesIndividualFamilyMembersDrive = false;
	
	
	
	

	//////////////////////////////////
	////     CONSTRUCTORS     ////////
	//////////////////////////////////

	public CPPCommercialAutoLine() {

	}

	public CPPCommercialAutoLine_Coverages getCommercialAutoLine_Coverages() {
		return commercialAutoLine_Coverages;
	}

	public void setCommercialAutoLine_Coverages(CPPCommercialAutoLine_Coverages commercialAutoLine_Coverages) {
		this.commercialAutoLine_Coverages = commercialAutoLine_Coverages;
	}
	
	public List<CommercialAutoLine.CALineExclutionsAndConditions> getExclutionsAndConditions() {
		return exclutionsAndConditions;
	}

	public void setExclutionsAndConditions(List<CommercialAutoLine.CALineExclutionsAndConditions> exclutionsAndConditions) {
		this.exclutionsAndConditions = exclutionsAndConditions;
	}


	public CPPCommercialAutoLine_AdditionalCoverages getCommercialAutoLine_AdditionalCoverages() {
		return commercialAutoLine_AdditionalCoverages;
	}


	public void setCommercialAutoLine_AdditionalCoverages(
			CPPCommercialAutoLine_AdditionalCoverages commercialAutoLine_AdditionalCoverages) {
		this.commercialAutoLine_AdditionalCoverages = commercialAutoLine_AdditionalCoverages;
	}


	public boolean isHasApplicantLeasedGreaterThan6Months() {
		return hasApplicantLeasedGreaterThan6Months;
	}


	public void setHasApplicantLeasedGreaterThan6Months(
			boolean hasApplicantLeasedGreaterThan6Months) {
		this.hasApplicantLeasedGreaterThan6Months = hasApplicantLeasedGreaterThan6Months;
	}


	public boolean isDoesIndividualFamilyMembersDrive() {
		return doesIndividualFamilyMembersDrive;
	}


	public void setDoesIndividualFamilyMembersDrive(
			boolean doesIndividualFamilyMembersDrive) {
		this.doesIndividualFamilyMembersDrive = doesIndividualFamilyMembersDrive;
	}


	public List<AdditionalInsured_CommercialAuto> getAdditionalInterest() {
		return additionalInterest;
	}


	public void setAdditionalInterest(List<AdditionalInsured_CommercialAuto> additionalInterest) {
		this.additionalInterest = additionalInterest;
	}


	public boolean exclutionsAndConditions_HasChanged() {
		return exclutionsAndConditions_HasChanged;
	}


	public void setExclutionsAndConditions_HasChanged(boolean exclutionsAndConditions_HasChanged) {
		this.exclutionsAndConditions_HasChanged = exclutionsAndConditions_HasChanged;
	}


	public boolean additionalInterest_HasChanged() {
		return additionalInterest_HasChanged;
	}


	public void setAdditionalInterest_HasChanged(boolean additionalInterest_HasChanged) {
		this.additionalInterest_HasChanged = additionalInterest_HasChanged;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public void resetChangeCondition() {
		setHasChanged(false);
		setExclutionsAndConditions_HasChanged(false);
		setAdditionalInterest_HasChanged(false);
		commercialAutoLine_Coverages.setHasChanged(false);
		commercialAutoLine_AdditionalCoverages.setHasChanged(false);
	}



	

}
