package persistence.globaldatarepo.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "LexisNexis", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class LexisNexis {

	private int id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String ssn;
	private Date dob;
	private String gender;
	private String dlnumber;
	private String street;
	private String apartment;
	private String city;
	private String state;
	private String zip;
	private Boolean cbr;
	private Integer mvrNumEntriesOnDrivingRecord;
	private String mvrProcessingStatus;
	private Integer clueAutoNumClaimsForDriver;
	private Integer clueAutoNumClaimTypesForDriver;
	private Integer cluePropertyNumClaimsForDriver;
	private Integer cluePropertyNumClaimTypesForDriver;
	private Integer additionalDriverDiscovery;
	private Integer vin;
	private Integer currentCarrierAuto;
	private Integer currentCarrierProperty;
	private Boolean solutionsAtQuote;
	private Boolean autoDataPrefill;
	private Boolean commercialPrefill;

	public LexisNexis() {
	}

	public LexisNexis(int id) {
		this.id = id;
	}

	public LexisNexis(int id, String firstName, String middleName, String lastName, String ssn, Date dob, String gender, String dlnumber, 
			String houseNum, String street, String apartment, String city, String state, String zip, Boolean cbr, Integer mvrNumEntriesOnDrivingRecord, 
			String mvrProcessingStatus, Integer clueAutoNumClaimsForDriver, Integer clueAutoNumClaimTypesForDriver, Integer cluePropertyNumClaimsForDriver, 
			Integer cluePropertyNumClaimTypesForDriver, Integer additionalDriverDiscovery, Integer vin, Integer currentCarrierAuto, 
			Integer currentCarrierProperty, Boolean solutionsAtQuote, Boolean autoDataPrefill, Boolean commercialPrefill) {
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.ssn = ssn;
		this.dob = dob;
		this.gender = gender;
		this.dlnumber = dlnumber;
		this.street = street;
		this.apartment = apartment;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.cbr = cbr;
		this.mvrNumEntriesOnDrivingRecord = mvrNumEntriesOnDrivingRecord;
		this.setMvrProcessingStatus(mvrProcessingStatus);
		this.clueAutoNumClaimsForDriver = clueAutoNumClaimsForDriver;
		this.clueAutoNumClaimTypesForDriver = clueAutoNumClaimTypesForDriver;
		this.cluePropertyNumClaimsForDriver = cluePropertyNumClaimsForDriver;
		this.cluePropertyNumClaimTypesForDriver = cluePropertyNumClaimTypesForDriver;
		this.additionalDriverDiscovery = additionalDriverDiscovery;
		this.vin = vin;
		this.currentCarrierAuto = currentCarrierAuto;
		this.currentCarrierProperty = currentCarrierProperty;
		this.solutionsAtQuote = solutionsAtQuote;
		this.autoDataPrefill = autoDataPrefill;
		this.commercialPrefill = commercialPrefill;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "FirstName")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MiddleName")
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LastName")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "SSN", length = 9)
	public String getSsn() {
		return this.ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Column(name = "DOB")
	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name = "Gender", length = 1)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "DLNumber")
	public String getDlnumber() {
		return this.dlnumber;
	}

	public void setDlnumber(String dlnumber) {
		this.dlnumber = dlnumber;
	}

	@Column(name = "Street")
	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "Apartment")
	public String getApartment() {
		return this.apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	@Column(name = "City")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "State", length = 2)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "Zip", length = 10)
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "CBR")
	public Boolean getCbr() {
		return this.cbr;
	}

	public void setCbr(Boolean cbr) {
		this.cbr = cbr;
	}

	@Column(name = "MVRNumEntriesOnDrivingRecord")
	public Integer getMvrNumEntriesOnDrivingRecord() {
		return this.mvrNumEntriesOnDrivingRecord;
	}

	public void setMvrNumEntriesOnDrivingRecord(Integer mvrNumEntriesOnDrivingRecord) {
		this.mvrNumEntriesOnDrivingRecord = mvrNumEntriesOnDrivingRecord;
	}

	@Column(name = "MVRProcessingStatus")
	public String getMvrProcessingStatus() {
		return mvrProcessingStatus;
	}

	public void setMvrProcessingStatus(String mvrProcessingStatus) {
		this.mvrProcessingStatus = mvrProcessingStatus;
	}

	@Column(name = "ClueAutoNumClaimsForDriver")
	public Integer getClueAutoNumClaimsForDriver() {
		return this.clueAutoNumClaimsForDriver;
	}

	public void setClueAutoNumClaimsForDriver(Integer clueAutoNumClaimsForDriver) {
		this.clueAutoNumClaimsForDriver = clueAutoNumClaimsForDriver;
	}

	@Column(name = "ClueAutoNumClaimTypesForDriver")
	public Integer getClueAutoNumClaimTypesForDriver() {
		return this.clueAutoNumClaimTypesForDriver;
	}

	public void setClueAutoNumClaimTypesForDriver(Integer clueAutoNumClaimTypesForDriver) {
		this.clueAutoNumClaimTypesForDriver = clueAutoNumClaimTypesForDriver;
	}

	@Column(name = "CluePropertyNumClaimsForDriver")
	public Integer getCluePropertyNumClaimsForDriver() {
		return this.cluePropertyNumClaimsForDriver;
	}

	public void setCluePropertyNumClaimsForDriver(Integer cluePropertyNumClaimsForDriver) {
		this.cluePropertyNumClaimsForDriver = cluePropertyNumClaimsForDriver;
	}

	@Column(name = "CluePropertyNumClaimTypesForDriver")
	public Integer getCluePropertyNumClaimTypesForDriver() {
		return this.cluePropertyNumClaimTypesForDriver;
	}

	public void setCluePropertyNumClaimTypesForDriver(Integer cluePropertyNumClaimTypesForDriver) {
		this.cluePropertyNumClaimTypesForDriver = cluePropertyNumClaimTypesForDriver;
	}

	@Column(name = "AdditionalDriverDiscovery")
	public Integer getAdditionalDriverDiscovery() {
		return this.additionalDriverDiscovery;
	}

	public void setAdditionalDriverDiscovery(Integer additionalDriverDiscovery) {
		this.additionalDriverDiscovery = additionalDriverDiscovery;
	}

	@Column(name = "VIN")
	public Integer getVin() {
		return this.vin;
	}

	public void setVin(Integer vin) {
		this.vin = vin;
	}

	@Column(name = "CurrentCarrierAuto")
	public Integer getCurrentCarrierAuto() {
		return this.currentCarrierAuto;
	}

	public void setCurrentCarrierAuto(Integer currentCarrierAuto) {
		this.currentCarrierAuto = currentCarrierAuto;
	}

	@Column(name = "CurrentCarrierProperty")
	public Integer getCurrentCarrierProperty() {
		return this.currentCarrierProperty;
	}

	public void setCurrentCarrierProperty(Integer currentCarrierProperty) {
		this.currentCarrierProperty = currentCarrierProperty;
	}

	@Column(name = "SolutionsAtQuote")
	public Boolean getSolutionsAtQuote() {
		return this.solutionsAtQuote;
	}

	public void setSolutionsAtQuote(Boolean solutionsAtQuote) {
		this.solutionsAtQuote = solutionsAtQuote;
	}

	@Column(name = "AutoDataPrefill")
	public Boolean getAutoDataPrefill() {
		return this.autoDataPrefill;
	}

	public void setAutoDataPrefill(Boolean autoDataPrefill) {
		this.autoDataPrefill = autoDataPrefill;
	}

	@Column(name = "CommercialPrefill")
	public Boolean getCommercialPrefill() {
		return this.commercialPrefill;
	}

	public void setCommercialPrefill(Boolean commercialPrefill) {
		this.commercialPrefill = commercialPrefill;
	}

}
