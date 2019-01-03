package persistence.flatfile.mbre.entities;

public class PCMonthlyLocationRecord {

	private String currentPolicyNumber;
	private String transactionCode;
	private String locationSequenceNumber;
	private String locationAddress1;
	private String locationAddress2;
	private String locationCity;
	private String locationState;
	private String locationCounty;
	private String locationZipCode;
	private String locationOccupancyCode;
	private String locationBuildingValue;
	private String locationContentsValue;
	private String locationBusinessIncomeValue;
	private String locationContactName;
	private String locationContactPhoneNumber;
	private String tenantOwner;
	
	public PCMonthlyLocationRecord() {
		
	}
	
	public PCMonthlyLocationRecord(String currentPolicyNumber, String transactionCode, String locationSequenceNumber, String locationAddress1, String locationAddress2, String locationCity, String locationState, String locationCounty, String locationZipCode, String locationOccupancyCode, String locationBuildingValue, String locationContentsValue, String locationBusinessIncomeValue, String locationContactName, String locationContactPhoneNumber, String tenantOwner) {
		setCurrentPolicyNumber(currentPolicyNumber);
		setTransactionCode(transactionCode);
		setLocationSequenceNumber(locationSequenceNumber);
		setLocationAddress1(locationAddress1);
		setLocationAddress2(locationAddress2);
		setLocationCity(locationCity);
		setLocationState(locationState);
		setLocationCounty(locationCounty);
		setLocationZipCode(locationZipCode);
		setLocationOccupancyCode(locationOccupancyCode);
		setLocationBuildingValue(locationBuildingValue);
		setLocationContentsValue(locationContentsValue);
		setLocationBusinessIncomeValue(locationBusinessIncomeValue);
		setLocationContactName(locationContactName);
		setLocationContactPhoneNumber(locationContactPhoneNumber);
		setTenantOwner(tenantOwner);
	}
	
	public boolean equals(PCMonthlyLocationRecord recordToCompare) {
    	return (this.getCurrentPolicyNumber().equals(recordToCompare.getCurrentPolicyNumber()) &&
    			this.getTransactionCode().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationSequenceNumber().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationAddress1().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationAddress2().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationCity().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationState().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationCounty().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationZipCode().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationOccupancyCode().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationBuildingValue().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationContentsValue().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationBusinessIncomeValue().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationContactName().equals(recordToCompare.getTransactionCode()) &&
    			this.getLocationContactPhoneNumber().equals(recordToCompare.getTransactionCode()) &&
    			this.getTenantOwner().equals(recordToCompare.getTransactionCode()));
    }

	public String getCurrentPolicyNumber() {
		return currentPolicyNumber;
	}

	public void setCurrentPolicyNumber(String currentPolicyNumber) {
		this.currentPolicyNumber = currentPolicyNumber;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getLocationSequenceNumber() {
		return locationSequenceNumber;
	}

	public void setLocationSequenceNumber(String locationSequenceNumber) {
		this.locationSequenceNumber = locationSequenceNumber;
	}

	public String getLocationAddress1() {
		return locationAddress1;
	}

	public void setLocationAddress1(String locationAddress1) {
		this.locationAddress1 = locationAddress1;
	}

	public String getLocationAddress2() {
		return locationAddress2;
	}

	public void setLocationAddress2(String locationAddress2) {
		this.locationAddress2 = locationAddress2;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationState() {
		return locationState;
	}

	public void setLocationState(String locationState) {
		this.locationState = locationState;
	}

	public String getLocationCounty() {
		return locationCounty;
	}

	public void setLocationCounty(String locationCounty) {
		this.locationCounty = locationCounty;
	}

	public String getLocationZipCode() {
		return locationZipCode;
	}

	public void setLocationZipCode(String locationZipCode) {
		this.locationZipCode = locationZipCode;
	}

	public String getLocationOccupancyCode() {
		return locationOccupancyCode;
	}

	public void setLocationOccupancyCode(String locationOccupancyCode) {
		this.locationOccupancyCode = locationOccupancyCode;
	}

	public String getLocationBuildingValue() {
		return locationBuildingValue;
	}

	public void setLocationBuildingValue(String locationBuildingValue) {
		this.locationBuildingValue = locationBuildingValue;
	}

	public String getLocationContentsValue() {
		return locationContentsValue;
	}

	public void setLocationContentsValue(String locationContentsValue) {
		this.locationContentsValue = locationContentsValue;
	}

	public String getLocationBusinessIncomeValue() {
		return locationBusinessIncomeValue;
	}

	public void setLocationBusinessIncomeValue(
			String locationBusinessIncomeValue) {
		this.locationBusinessIncomeValue = locationBusinessIncomeValue;
	}

	public String getLocationContactName() {
		return locationContactName;
	}

	public void setLocationContactName(String locationContactName) {
		this.locationContactName = locationContactName;
	}

	public String getLocationContactPhoneNumber() {
		return locationContactPhoneNumber;
	}

	public void setLocationContactPhoneNumber(String locationContactPhoneNumber) {
		this.locationContactPhoneNumber = locationContactPhoneNumber;
	}

	public String getTenantOwner() {
		return tenantOwner;
	}

	public void setTenantOwner(String tenantOwner) {
		this.tenantOwner = tenantOwner;
	}

}
