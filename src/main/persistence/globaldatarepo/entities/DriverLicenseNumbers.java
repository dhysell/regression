package persistence.globaldatarepo.entities;

// Generated Feb 20, 2016 2:25:07 PM by Hibernate Tools 4.0.0

import javax.persistence.*;

/**
 * DriverLicenseNumbers generated by hbm2java
 */
@Entity
@Table(name = "DriverLicenseNumbers", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class DriverLicenseNumbers {

	private int id;
	private String state;
	private String dlnumber;

	public DriverLicenseNumbers() {
	}

	public DriverLicenseNumbers(int id, String state, String dlnumber) {
		this.id = id;
		this.state = state;
		this.dlnumber = dlnumber;
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

	@Column(name = "State", nullable = false, length = 100)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "DLNumber", nullable = false, length = 100)
	public String getDlnumber() {
		return this.dlnumber;
	}

	public void setDlnumber(String dlnumber) {
		this.dlnumber = dlnumber;
	}

}
