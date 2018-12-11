package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlandMarineValuablePapers {

	private InlandMarineCPP.ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible deductible = InlandMarineCPP.ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible.FiveHundred;
	private boolean valuablePapersAtYourPremises = true;
	private boolean valuablePapersAwayFromYourPremises = true;
	private CPPCommercialPropertyProperty property;
	private int atYourPremisesLimit = 3000;
	private int awayFromYourPremisesLimit = 6000;
	private boolean librariesCM_67_02 = true;
	
	public CPPInlandMarineValuablePapers(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public InlandMarineCPP.ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.ValuablePapersAndRecordsCoverageForm_CM_00_67_Deductible deductible) {
		this.deductible = deductible;
	}

	public boolean isValuablePapersAtYourPremises() {
		return valuablePapersAtYourPremises;
	}

	public void setValuablePapersAtYourPremises(boolean valuablePapersAtYourPremises) {
		this.valuablePapersAtYourPremises = valuablePapersAtYourPremises;
	}

	public boolean isValuablePapersAwayFromYourPremises() {
		return valuablePapersAwayFromYourPremises;
	}

	public void setValuablePapersAwayFromYourPremises(boolean valuablePapersAwayFromYourPremises) {
		this.valuablePapersAwayFromYourPremises = valuablePapersAwayFromYourPremises;
	}

	public CPPCommercialPropertyProperty getProperty() {
		return property;
	}

	public void setProperty(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public int getAtYourPremisesLimit() {
		return atYourPremisesLimit;
	}

	public void setAtYourPremisesLimit(int atYourPremisesLimit) {
		this.atYourPremisesLimit = atYourPremisesLimit;
	}

	public int getAwayFromYourPremisesLimit() {
		return awayFromYourPremisesLimit;
	}

	public void setAwayFromYourPremisesLimit(int awayFromYourPremisesLimit) {
		this.awayFromYourPremisesLimit = awayFromYourPremisesLimit;
	}

	public boolean isLibrariesCM_67_02() {
		return librariesCM_67_02;
	}

	public void setLibrariesCM_67_02(boolean librariesCM_67_02) {
		this.librariesCM_67_02 = librariesCM_67_02;
	}

}
