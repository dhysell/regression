package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlaneMarineExhibition {

	private String blanketLimit = "1000";
	private InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Deductible deductible = InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Deductible.FiveHundred;
	private InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Coinsurance coinsurance = InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Coinsurance.EightlyPercent;
	
	private String city = "Pocatello";
	private String state = "Idaho";
	private InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_HazardousCategory hazardousCategory = InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_HazardousCategory.Low;
	
	public CPPInlaneMarineExhibition() {
		
	}

	public String getBlanketLimit() {
		return blanketLimit;
	}

	public void setBlanketLimit(String blanketLimit) {
		this.blanketLimit = blanketLimit;
	}

	public InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_HazardousCategory getHazardousCategory() {
		return hazardousCategory;
	}

	public void setHazardousCategory(InlandMarineCPP.ExhibitionCoverageForm_IH_00_92_HazardousCategory hazardousCategory) {
		this.hazardousCategory = hazardousCategory;
	}

}
