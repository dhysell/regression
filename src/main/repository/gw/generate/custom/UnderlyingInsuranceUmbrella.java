package repository.gw.generate.custom;

public class UnderlyingInsuranceUmbrella {
	
	private String company = "Fake Block"; // @editor ecoleman 8/3/18: adding default values for these so you don't waste time figuring out why it doesn't work.
	private String policyNumber = "012345";
	private String limitOfInsurance = "20000";
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getLimitOfInsurance() {
		return limitOfInsurance;
	}

	public void setLimitOfInsurance(String limitOfInsurance) {
		this.limitOfInsurance = limitOfInsurance;
	}

}
