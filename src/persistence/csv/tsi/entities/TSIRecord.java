package persistence.csv.tsi.entities;

import com.idfbins.enums.State;

import java.util.Date;

public class TSIRecord {

	private String transmittalNumber;
	private String clientNumber;
	private String debtorName;
	private String street;
	private String optionalAddress;
	private String city;
	private State state;
	private String zip;
	private String serviceCode;
	private Date dateOfDebt;
	private Date dateLastPay;
	private Double faceValue;
	private Double balance;
	private String socialSecurity;
	private String taxID;
	private Date birthDate;
	private String homePhone;
	private String mobilePhone;
	private String workPhone;
	private String primaryPhone;
	
	public TSIRecord() {
		
	}
	
	public TSIRecord(String transmittalNumber, String clientNumber, String debtorName, String street, String optionalAddress, String city, State state, String zip, String serviceCode, Date dateOfDebt, Date dateLastPay, Double faceValue, Double balance, String socialSecurity, String taxID, Date birthDate, String homePhone, String mobilePhone, String workPhone, String primaryPhone) {
		setTransmittalNumber(transmittalNumber);
		setClientNumber(clientNumber);
		setDebtorName(debtorName);
		setStreet(street);
		setOptionalAddress(optionalAddress);
		setCity(city);
		setState(state);
		setZip(zip);
		setServiceCode(serviceCode);
		setDateOfDebt(dateOfDebt);
		setDateLastPay(dateLastPay);
		setFaceValue(faceValue);
		setBalance(balance);
		setSocialSecurity(socialSecurity);
		setTaxID(taxID);
		setBirthDate(birthDate);
		setHomePhone(homePhone);
		setMobilePhone(mobilePhone);
		setWorkPhone(workPhone);
		setPrimaryPhone(primaryPhone);
	}
	
	public boolean equals(TSIRecord recordToCompare) {
    	return (this.getTransmittalNumber().equals(recordToCompare.getTransmittalNumber()) &&
    			this.getClientNumber().equals(recordToCompare.getClientNumber()) &&
    			this.getDebtorName().equals(recordToCompare.getDebtorName()) &&
    			this.getStreet().equals(recordToCompare.getStreet()) &&
    			this.getOptionalAddress().equals(recordToCompare.getOptionalAddress()) &&
    			this.getCity().equals(recordToCompare.getCity()) &&
    			this.getState().equals(recordToCompare.getState()) &&
    			this.getZip().equals(recordToCompare.getZip()) &&
    			this.getServiceCode().equals(recordToCompare.getServiceCode()) &&
    			this.getDateOfDebt().equals(recordToCompare.getDateOfDebt()) &&
    			this.getDateLastPay().equals(recordToCompare.getDateLastPay()) &&
    			this.getFaceValue().equals(recordToCompare.getFaceValue()) &&
    			this.getBalance().equals(recordToCompare.getBalance()) &&
    			this.getSocialSecurity().equals(recordToCompare.getSocialSecurity()) &&
    			this.getTaxID().equals(recordToCompare.getTaxID()) &&
    			this.getBirthDate().equals(recordToCompare.getBirthDate()) &&
    			this.getHomePhone().equals(recordToCompare.getHomePhone()) &&
    			this.getMobilePhone().equals(recordToCompare.getMobilePhone()) &&
    			this.getWorkPhone().equals(recordToCompare.getWorkPhone()) &&
    			this.getPrimaryPhone().equals(recordToCompare.getPrimaryPhone()));
    }
    
    

	public String getTransmittalNumber() {
		return transmittalNumber;
	}

	public void setTransmittalNumber(String transmittalNumber) {
		this.transmittalNumber = transmittalNumber;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public void setDebtorName(String debtorName) {
		this.debtorName = debtorName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getOptionalAddress() {
		return optionalAddress;
	}

	public void setOptionalAddress(String optionalAddress) {
		this.optionalAddress = optionalAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Date getDateOfDebt() {
		return dateOfDebt;
	}

	public void setDateOfDebt(Date dateOfDebt) {
		this.dateOfDebt = dateOfDebt;
	}

	public Date getDateLastPay() {
		return dateLastPay;
	}

	public void setDateLastPay(Date dateLastPay) {
		this.dateLastPay = dateLastPay;
	}

	public Double getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Double faceValue) {
		this.faceValue = faceValue;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getSocialSecurity() {
		return socialSecurity;
	}

	public void setSocialSecurity(String socialSecurity) {
		this.socialSecurity = socialSecurity;
	}

	public String getTaxID() {
		return taxID;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	
}
