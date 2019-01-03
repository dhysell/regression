package persistence.globaldatarepo.entities;
// Generated Nov 7, 2017 8:51:56 AM by Hibernate Tools 4.0.1.Final

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AB_Users", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class AbUsers implements java.io.Serializable {
	@Id
	@Column(name = "UserName", unique = true, nullable = false, length = 50)
	private String userName;
	
	@Column(name = "UserFirstName", nullable = false, length = 50)
	private String userFirstName;
	
	@Column(name = "UserLastName", nullable = false, length = 50)
	private String userLastName;
	
	@Column(name = "UserPassword", length = 50)
	private String userPassword;
	
	@Column(name = "UserDepartment", length = 50)
	private String userDepartment;
	
	@Column(name = "UserTitle", length = 50)
	private String userTitle;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "AB_UserRole", joinColumns = {
			@JoinColumn(name = "ABUser", nullable = false, updatable = false)
			}, inverseJoinColumns = {	
			@JoinColumn(name = "ABRole", nullable = false, updatable = false)
			})
	
	private Set<AbRole> abRoles = new HashSet<AbRole>();

	public AbUsers() {
		setAbRoles(new HashSet<AbRole>());
	}
	
	public AbUsers(String _userName) {
		setUserName(_userName);
		setAbRoles(new HashSet<AbRole>());
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFirstName() {
		return this.userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return this.userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserDepartment() {
		return this.userDepartment;
	}

	public void setUserDepartment(String userDepartment) {
		this.userDepartment = userDepartment;
	}

	public String getUserTitle() {
		return this.userTitle;
	}

	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}
	
	public Set<AbRole> getAbRoles() {
		return this.abRoles;
	}

	public void setAbRoles(Set<AbRole> _abRoles) {
		this.abRoles = _abRoles;
	}

}
