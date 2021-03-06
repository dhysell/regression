package persistence.globaldatarepo.entities;

// Generated Jun 29, 2015 10:26:58 AM by Hibernate Tools 4.0.0

import javax.persistence.*;

/**
 * UserRegionsBak generated by hbm2java
 */
@Entity
@Table(name = "User_Regions", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class UserRegions {

	private int id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String region;
	private String role;

	public UserRegions() {
	}

	public UserRegions(int id, String firstName, String lastName,
			String userName, String password, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}

	public UserRegions(int id, String firstName, String lastName,
			String userName, String password, String region, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.region = region;
		this.role = role;
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

	@Column(name = "FirstName", nullable = false, length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LastName", nullable = false, length = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "UserName", nullable = false, length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "Password", nullable = false, length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "Region", length = 10)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "Role", nullable = false, length = 10)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
