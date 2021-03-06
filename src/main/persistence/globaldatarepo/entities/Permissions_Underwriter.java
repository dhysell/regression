package persistence.globaldatarepo.entities;
// default package
// Generated Oct 25, 2016 5:54:02 PM by Hibernate Tools 5.2.0.Beta1

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * PermissionsUnderwriter generated by hbm2java
 */
@Entity
@Table(name = "Permissions_Underwriter", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Permissions_Underwriter {

	private Integer id;
	private String permission;
	private String permissionCode;
	private String dateLastUsed;

	public Permissions_Underwriter() {
	}
	
	public Permissions_Underwriter(String permission, String permissionCode) {
		this.permission = permission;
		this.permissionCode = permissionCode;
		this.dateLastUsed = null;
	}

	public Permissions_Underwriter(String permission, String permissionCode, String dateLastUsed) {
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
