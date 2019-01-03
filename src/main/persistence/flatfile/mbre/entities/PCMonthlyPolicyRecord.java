package persistence.flatfile.mbre.entities;

public class PCMonthlyPolicyRecord {
	
	private String companyNumber;
	private String namedInsuredLine1;
	private String namedInsuredLine2;
	private String principalAddressLine1;
	private String principalAddressLine2;
	private String principalCity;
	private String principalState;
	private String principalCounty;
	private String principalZipCode;
	private String currentPolicyNumber;
	private String previousPolicyNumber;
	private String policyEffectiveDate;
	private String policyExpirationDate;
	private String accountingDate;
	private String transactionEffectiveDate;
	private String equipmentBreakdownPremium;
	private String packagePropertyPremium;
	private String deductible;
	private String transactionCode;
	private String companyBranchCode;
	private String policyType;

	public PCMonthlyPolicyRecord() {
		
	}
	
	public PCMonthlyPolicyRecord(String companyNumber, String namedInsuredLine1, String namedInsuredLine2, String principalAddressLine1, String principalAddressLine2, String principalCity, String principalState, String principalCounty, String principalZipCode, String currentPolicyNumber, String previousPolicyNumber, String policyEffectiveDate, String policyExpirationDate, String accountingDate, String transactionEffectiveDate, String equipmentBreakdownPremium, String packagePropertyPremium, String deductible, String transactionCode, String companyBranchCode, String policyType) {
		setCompanyNumber(companyNumber);
		setNamedInsuredLine1(namedInsuredLine1);
		setNamedInsuredLine2(namedInsuredLine2);
		setPrincipalAddressLine1(principalAddressLine1);
		setPrincipalAddressLine2(principalAddressLine2);
		setPrincipalCity(principalCity);
		setPrincipalState(principalState);
		setPrincipalCounty(principalCounty);
		setPrincipalZipCode(principalZipCode);
		setCurrentPolicyNumber(currentPolicyNumber);
		setPreviousPolicyNumber(previousPolicyNumber);
		setPolicyEffectiveDate(policyEffectiveDate);
		setPolicyExpirationDate(policyExpirationDate);
		setAccountingDate(accountingDate);
		setTransactionEffectiveDate(transactionEffectiveDate);
		setEquipmentBreakdownPremium(equipmentBreakdownPremium);
		setPackagePropertyPremium(packagePropertyPremium);
		setDeductible(deductible);
		setTransactionCode(transactionCode);
		setCompanyBranchCode(companyBranchCode);
		setPolicyType(policyType);
	}
	
	public boolean equals(PCMonthlyPolicyRecord recordToCompare) {
    	return (this.getCompanyNumber().equals(recordToCompare.getCompanyNumber()) &&
    			this.getNamedInsuredLine1().equals(recordToCompare.getNamedInsuredLine1()) &&
    			this.getNamedInsuredLine2().equals(recordToCompare.getNamedInsuredLine2()) &&
    			this.getPrincipalAddressLine1().equals(recordToCompare.getPrincipalAddressLine1()) &&
    			this.getPrincipalAddressLine2().equals(recordToCompare.getPrincipalAddressLine2()) &&
    			this.getPrincipalCity().equals(recordToCompare.getPrincipalCity()) &&
    			this.getPrincipalState().equals(recordToCompare.getPrincipalState()) &&
    			this.getPrincipalCounty().equals(recordToCompare.getPrincipalCounty()) &&
    			this.getPrincipalZipCode().equals(recordToCompare.getPrincipalZipCode()) &&
    			this.getCurrentPolicyNumber().equals(recordToCompare.getCurrentPolicyNumber()) &&
    			this.getPreviousPolicyNumber().equals(recordToCompare.getPreviousPolicyNumber()) &&
    			this.getPolicyEffectiveDate().equals(recordToCompare.getPolicyEffectiveDate()) &&
    			this.getPolicyExpirationDate().equals(recordToCompare.getPolicyExpirationDate()) &&
    			this.getAccountingDate().equals(recordToCompare.getAccountingDate()) &&
    			this.getTransactionEffectiveDate().equals(recordToCompare.getTransactionEffectiveDate()) &&
    			this.getEquipmentBreakdownPremium().equals(recordToCompare.getEquipmentBreakdownPremium()) &&
    			this.getPackagePropertyPremium().equals(recordToCompare.getPackagePropertyPremium()) &&
    			this.getDeductible().equals(recordToCompare.getDeductible()) &&
    			this.getTransactionCode().equals(recordToCompare.getTransactionCode()) &&
    			this.getCompanyBranchCode().equals(recordToCompare.getCompanyBranchCode()) &&
    			this.getPolicyType().equals(recordToCompare.getPolicyType()));
    }

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getNamedInsuredLine1() {
		return namedInsuredLine1;
	}

	public void setNamedInsuredLine1(String namedInsuredLine1) {
		this.namedInsuredLine1 = namedInsuredLine1;
	}

	public String getNamedInsuredLine2() {
		return namedInsuredLine2;
	}

	public void setNamedInsuredLine2(String namedInsuredLine2) {
		this.namedInsuredLine2 = namedInsuredLine2;
	}

	public String getPrincipalAddressLine1() {
		return principalAddressLine1;
	}

	public void setPrincipalAddressLine1(String principalAddressLine1) {
		this.principalAddressLine1 = principalAddressLine1;
	}

	public String getPrincipalAddressLine2() {
		return principalAddressLine2;
	}

	public void setPrincipalAddressLine2(String principalAddressLine2) {
		this.principalAddressLine2 = principalAddressLine2;
	}

	public String getPrincipalCity() {
		return principalCity;
	}

	public void setPrincipalCity(String principalCity) {
		this.principalCity = principalCity;
	}

	public String getPrincipalState() {
		return principalState;
	}

	public void setPrincipalState(String principalState) {
		this.principalState = principalState;
	}

	public String getPrincipalCounty() {
		return principalCounty;
	}

	public void setPrincipalCounty(String principalCounty) {
		this.principalCounty = principalCounty;
	}

	public String getPrincipalZipCode() {
		return principalZipCode;
	}

	public void setPrincipalZipCode(String principalZipCode) {
		this.principalZipCode = principalZipCode;
	}

	public String getCurrentPolicyNumber() {
		return currentPolicyNumber;
	}

	public void setCurrentPolicyNumber(String currentPolicyNumber) {
		this.currentPolicyNumber = currentPolicyNumber;
	}

	public String getPreviousPolicyNumber() {
		return previousPolicyNumber;
	}

	public void setPreviousPolicyNumber(String previousPolicyNumber) {
		this.previousPolicyNumber = previousPolicyNumber;
	}

	public String getPolicyEffectiveDate() {
		return policyEffectiveDate;
	}

	public void setPolicyEffectiveDate(String policyEffectiveDate) {
		this.policyEffectiveDate = policyEffectiveDate;
	}

	public String getPolicyExpirationDate() {
		return policyExpirationDate;
	}

	public void setPolicyExpirationDate(String policyExpirationDate) {
		this.policyExpirationDate = policyExpirationDate;
	}

	public String getAccountingDate() {
		return accountingDate;
	}

	public void setAccountingDate(String accountingDate) {
		this.accountingDate = accountingDate;
	}

	public String getTransactionEffectiveDate() {
		return transactionEffectiveDate;
	}

	public void setTransactionEffectiveDate(String transactionEffectiveDate) {
		this.transactionEffectiveDate = transactionEffectiveDate;
	}

	public String getEquipmentBreakdownPremium() {
		return equipmentBreakdownPremium;
	}

	public void setEquipmentBreakdownPremium(String equipmentBreakdownPremium) {
		this.equipmentBreakdownPremium = equipmentBreakdownPremium;
	}

	public String getPackagePropertyPremium() {
		return packagePropertyPremium;
	}

	public void setPackagePropertyPremium(String packagePropertyPremium) {
		this.packagePropertyPremium = packagePropertyPremium;
	}

	public String getDeductible() {
		return deductible;
	}

	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getCompanyBranchCode() {
		return companyBranchCode;
	}

	public void setCompanyBranchCode(String companyBranchCode) {
		this.companyBranchCode = companyBranchCode;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

}
