package repository.gw.enums;

public enum AdditionalInsuredTypeCA {
	CertificateHolderOnly("Certificate Holder Only"),
	DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48("Designated Insured For Covered Autos Liability Coverage CA 20 48"),
	IdahoFormE("Idaho Form E"),
	WashingtonFormE("Washington Form E");
	String value;
	
	private AdditionalInsuredTypeCA(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
