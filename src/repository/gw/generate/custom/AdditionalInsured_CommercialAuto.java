package repository.gw.generate.custom;

import repository.gw.enums.AdditionalInsuredTypeCA;

public class AdditionalInsured_CommercialAuto extends AbstractAdditionalInsured {

	repository.gw.enums.AdditionalInsuredTypeCA additionalInsuredType = repository.gw.enums.AdditionalInsuredTypeCA.DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48;
	boolean subro = false;
	String subroString = "What ever Subro is.";
	boolean specialWording = false;
	String specialWordingString = "Your Mother Says Your Very Special!!";
	
	public AdditionalInsured_CommercialAuto() {
		
	}

	public repository.gw.enums.AdditionalInsuredTypeCA getAdditionalInsuredType() {
		return additionalInsuredType;
	}

	public void setAdditionalInsuredType(AdditionalInsuredTypeCA additionalInsuredType) {
		this.additionalInsuredType = additionalInsuredType;
	}

	public boolean isSubro() {
		return subro;
	}

	public void setSubro(boolean subro) {
		this.subro = subro;
	}

	public String getSubroString() {
		return subroString;
	}

	public void setSubroString(String subroString) {
		this.subroString = subroString;
	}

	public boolean isSpecialWording() {
		return specialWording;
	}

	public void setSpecialWording(boolean specialWording) {
		this.specialWording = specialWording;
	}

	public String getSpecialWordingString() {
		return specialWordingString;
	}

	public void setSpecialWordingString(String specialWordingString) {
		this.specialWordingString = specialWordingString;
	}
	
	
	
}
