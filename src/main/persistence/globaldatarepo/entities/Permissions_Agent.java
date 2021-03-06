package persistence.globaldatarepo.entities;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PermissionsAgent generated by hbm2java
 */
@Entity
@Table(name = "Permissions_Agent", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Permissions_Agent {

	private Integer id;
	private String permission;
	private String permissionCode;
	private String dateLastUsed;

	public Permissions_Agent() {
	}
	
	public Permissions_Agent(String permission, String permissionCode) {
		this.permission = permission;
		this.permissionCode = permissionCode;
		this.dateLastUsed = null;
	}

	public Permissions_Agent(String permission, String permissionCode, String dateLastUsed) {
		this.permission = permission;
		this.permissionCode = permissionCode;
		this.dateLastUsed = dateLastUsed;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "Permission", length = 100)
	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Column(name = "PermissionCode", length = 100)
	public String getPermissionCode() {
		return this.permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	@Column(name = "DateLastUsed", length = 10)
	public String getDateLastUsed() {
		return this.dateLastUsed;
	}

	public void setDateLastUsed(String dateLastUsed) {
		this.dateLastUsed = dateLastUsed;
	}

}
