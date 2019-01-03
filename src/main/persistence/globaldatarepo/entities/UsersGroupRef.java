package persistence.globaldatarepo.entities;
// Generated Nov 29, 2018 2:03:35 PM by Hibernate Tools 5.2.10.Final

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * UsersGroupRef generated by hbm2java
 */
@Entity
@Table(name = "Users_Group_Ref", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class UsersGroupRef implements java.io.Serializable {

	private Integer id;
	private String infoUsername;
	private String group;
	private String member;
	private String manager;
	private String loadFactorPermission;
	private String loadFactor;

	public UsersGroupRef() {
	}

	public UsersGroupRef(String infoUsername, String group, String member, String manager, String loadFactorPermission,
			String loadFactor) {
		this.infoUsername = infoUsername;
		this.group = group;
		this.member = member;
		this.manager = manager;
		this.loadFactorPermission = loadFactorPermission;
		this.loadFactor = loadFactor;
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

	@Column(name = "INFO_USERNAME", length = 200)
	public String getInfoUsername() {
		return this.infoUsername;
	}

	public void setInfoUsername(String infoUsername) {
		this.infoUsername = infoUsername;
	}

	@Column(name = "GROUP", length = 200)
	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Column(name = "MEMBER", length = 50)
	public String getMember() {
		return this.member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	@Column(name = "MANAGER", length = 50)
	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Column(name = "LOAD_FACTOR_PERMISSION", length = 50)
	public String getLoadFactorPermission() {
		return this.loadFactorPermission;
	}

	public void setLoadFactorPermission(String loadFactorPermission) {
		this.loadFactorPermission = loadFactorPermission;
	}

	@Column(name = "LOAD_FACTOR", length = 50)
	public String getLoadFactor() {
		return this.loadFactor;
	}

	public void setLoadFactor(String loadFactor) {
		this.loadFactor = loadFactor;
	}

}