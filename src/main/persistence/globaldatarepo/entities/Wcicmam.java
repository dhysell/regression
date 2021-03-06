package persistence.globaldatarepo.entities;
// Generated Jun 10, 2017 4:23:43 PM by Hibernate Tools 4.0.1.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Wcicmam generated by hbm2java
 */
@Entity
@Table(name = "wcicmam", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Wcicmam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String wcsiAccount;
	private String status;
	private String accountHoldersName;
	private String ani;
	private String address;
	private String city;
	private String state;
	private String zip;

	public Wcicmam() {
	}

	public Wcicmam(int id) {
		this.id = id;
	}

	public Wcicmam(int id, String wcsiAccount, String status, String accountHoldersName, String ani, String address,
			String city, String state, String zip) {
		this.id = id;
		this.wcsiAccount = wcsiAccount;
		this.status = status;
		this.accountHoldersName = accountHoldersName;
		this.ani = ani;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	@Id

	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "WCSI _Account")
	public String getWcsiAccount() {
		return this.wcsiAccount;
	}

	public void setWcsiAccount(String wcsiAccount) {
		this.wcsiAccount = wcsiAccount;
	}

	@Column(name = "Status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "AccountHoldersName")
	public String getAccountHoldersName() {
		return this.accountHoldersName;
	}

	public void setAccountHoldersName(String accountHoldersName) {
		this.accountHoldersName = accountHoldersName;
	}

	@Column(name = "ANI")
	public String getAni() {
		return this.ani;
	}

	public void setAni(String ani) {
		this.ani = ani;
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

}
