package persistence.globaldatarepo.entities;
// Generated Nov 29, 2018 1:09:51 PM by Hibernate Tools 5.2.10.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UsersGroups generated by hbm2java
 */
@Entity
@Table(name = "Users_Groups", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class UsersGroups implements java.io.Serializable {

	private String name;

	public UsersGroups() {
	}

	public UsersGroups(String name) {
		this.name = name;
	}

	@Id

	@Column(name = "NAME", unique = true, nullable = false, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
