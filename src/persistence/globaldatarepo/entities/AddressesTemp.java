package persistence.globaldatarepo.entities;

import javax.persistence.*;

/**
 * AddressesTemp generated by hbm2java
 */
@Entity
@Table(name = "AddressesTemp", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class AddressesTemp {

	private int id;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	private String county;
	private String teritoryCode;

	public AddressesTemp() {
	}

	public AddressesTemp(int id) {
		this.id = id;
	}

	public AddressesTemp(int id, String address, String city, String state,
			String zip, String zip4, String county, String teritoryCode) {
		this.id = id;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.zip4 = zip4;
		this.county = county;
		this.teritoryCode = teritoryCode;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "City")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "State")
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "Zip")
	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(name = "Zip_4")
	public String getZip4() {
		return this.zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	@Column(name = "County")
	public String getCounty() {
		return this.county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Column(name = "TeritoryCode")
	public String getTeritoryCode() {
		return this.teritoryCode;
	}

	public void setTeritoryCode(String teritoryCode) {
		this.teritoryCode = teritoryCode;
	}

}
