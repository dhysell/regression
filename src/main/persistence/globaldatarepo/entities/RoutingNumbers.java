package persistence.globaldatarepo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QA_RoutingNumbers", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class RoutingNumbers {

	private String routingNumber;
	private String officeCode;
	private String servicingFrbnumber;
	private int recordTypeCode;
	private String changeDate;
	private String newRoutingNumber;
	private String institutionName;
	private String address;
	private String city;
	private String stateCode;
	private String zipCode;
	private String zipCodeExtension;
	private String telephoneAreaCode;
	private String telephonePrefixNumber;
	private String telephoneSuffixNumber;
	private int institutionStatusCode;
	private int dataViewCode;

	public RoutingNumbers() {
	}

	public RoutingNumbers(String routingNumber, String officeCode,
			String servicingFrbnumber, int recordTypeCode,
			String institutionName, String address, String city,
			String zipCode, int institutionStatusCode, int dataViewCode) {
		this.routingNumber = routingNumber;
		this.officeCode = officeCode;
		this.servicingFrbnumber = servicingFrbnumber;
		this.recordTypeCode = recordTypeCode;
		this.institutionName = institutionName;
		this.address = address;
		this.city = city;
		this.zipCode = zipCode;
		this.institutionStatusCode = institutionStatusCode;
		this.dataViewCode = dataViewCode;
	}

	public RoutingNumbers(String routingNumber, String officeCode,
			String servicingFrbnumber, int recordTypeCode, String changeDate,
			String newRoutingNumber, String institutionName, String address,
			String city, String stateCode, String zipCode,
			String zipCodeExtension, String telephoneAreaCode,
			String telephonePrefixNumber, String telephoneSuffixNumber,
			int institutionStatusCode, int dataViewCode) {
		this.routingNumber = routingNumber;
		this.officeCode = officeCode;
		this.servicingFrbnumber = servicingFrbnumber;
		this.recordTypeCode = recordTypeCode;
		this.changeDate = changeDate;
		this.newRoutingNumber = newRoutingNumber;
		this.institutionName = institutionName;
		this.address = address;
		this.city = city;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
		this.zipCodeExtension = zipCodeExtension;
		this.telephoneAreaCode = telephoneAreaCode;
		this.telephonePrefixNumber = telephonePrefixNumber;
		this.telephoneSuffixNumber = telephoneSuffixNumber;
		this.institutionStatusCode = institutionStatusCode;
		this.dataViewCode = dataViewCode;
	}

	@Id
	@Column(name = "RoutingNumber", unique = true, nullable = false, length = 9)
	public String getRoutingNumber() {
		return this.routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	@Column(name = "OfficeCode", nullable = false, length = 1)
	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	@Column(name = "ServicingFRBNumber", nullable = false, length = 9)
	public String getServicingFrbnumber() {
		return this.servicingFrbnumber;
	}

	public void setServicingFrbnumber(String servicingFrbnumber) {
		this.servicingFrbnumber = servicingFrbnumber;
	}

	@Column(name = "RecordTypeCode", nullable = false)
	public int getRecordTypeCode() {
		return this.recordTypeCode;
	}

	public void setRecordTypeCode(int recordTypeCode) {
		this.recordTypeCode = recordTypeCode;
	}

	@Column(name = "ChangeDate", length = 10)
	public String getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "NewRoutingNumber", length = 9)
	public String getNewRoutingNumber() {
		return this.newRoutingNumber;
	}

	public void setNewRoutingNumber(String newRoutingNumber) {
		this.newRoutingNumber = newRoutingNumber;
	}

	@Column(name = "InstitutionName", nullable = false, length = 36)
	public String getInstitutionName() {
		return this.institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	@Column(name = "Address", nullable = false, length = 36)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "City", nullable = false, length = 20)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "StateCode", length = 2)
	public String getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	@Column(name = "ZipCode", nullable = false, length = 5)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "ZipCodeExtension", length = 4)
	public String getZipCodeExtension() {
		return this.zipCodeExtension;
	}

	public void setZipCodeExtension(String zipCodeExtension) {
		this.zipCodeExtension = zipCodeExtension;
	}

	@Column(name = "TelephoneAreaCode", length = 3)
	public String getTelephoneAreaCode() {
		return this.telephoneAreaCode;
	}

	public void setTelephoneAreaCode(String telephoneAreaCode) {
		this.telephoneAreaCode = telephoneAreaCode;
	}

	@Column(name = "TelephonePrefixNumber", length = 3)
	public String getTelephonePrefixNumber() {
		return this.telephonePrefixNumber;
	}

	public void setTelephonePrefixNumber(String telephonePrefixNumber) {
		this.telephonePrefixNumber = telephonePrefixNumber;
	}

	@Column(name = "TelephoneSuffixNumber", length = 4)
	public String getTelephoneSuffixNumber() {
		return this.telephoneSuffixNumber;
	}

	public void setTelephoneSuffixNumber(String telephoneSuffixNumber) {
		this.telephoneSuffixNumber = telephoneSuffixNumber;
	}

	@Column(name = "InstitutionStatusCode", nullable = false)
	public int getInstitutionStatusCode() {
		return this.institutionStatusCode;
	}

	public void setInstitutionStatusCode(int institutionStatusCode) {
		this.institutionStatusCode = institutionStatusCode;
	}

	@Column(name = "DataViewCode", nullable = false)
	public int getDataViewCode() {
		return this.dataViewCode;
	}

	public void setDataViewCode(int dataViewCode) {
		this.dataViewCode = dataViewCode;
	}

}
