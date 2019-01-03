package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlandMarineInstallation {

	private int limit = 1000;
	private InlandMarineCPP.InstallationFloaterCoverage_IDCM_31_4073_Deductible deductible = InlandMarineCPP.InstallationFloaterCoverage_IDCM_31_4073_Deductible.FiveHundred;
	private String descriptionOfProperty = "This is a property description";
	
	public CPPInlandMarineInstallation() {
		
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public InlandMarineCPP.InstallationFloaterCoverage_IDCM_31_4073_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.InstallationFloaterCoverage_IDCM_31_4073_Deductible deductible) {
		this.deductible = deductible;
	}

	public String getDescriptionOfProperty() {
		return descriptionOfProperty;
	}

	public void setDescriptionOfProperty(String descriptionOfProperty) {
		this.descriptionOfProperty = descriptionOfProperty;
	}

}
