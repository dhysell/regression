
package persistence.globaldatarepo.entities;

import javax.persistence.*;

@Entity
@Table(name = "Words", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class Names
{

	private int id;
	private String name;
	
	private String firstName;
	private String lastName;
	private String companyName;

	public Names() {
	}

	public Names(int id) {
		this.id = id;
	}

	public Names(int id, String name) {
		this.id = id;
		this.name = name;
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

	@Column(name = "Word")
	public String getWord() {
		return this.name;
	}

	public void setWord(String name) {
		this.name = name;
	}
	
	@Transient
	public String getFirstName() {
		return firstName;
	}
	
	@Transient
	public void setFirstName(String fName) {
		this.firstName = fName;
	}
	
	@Transient
	public String getLastName() {
		return lastName;
	}
	
	@Transient
	public void setLastName(String lName) {
		this.lastName = lName;
	}
	
	@Transient
	public String getCompanyName() {
		return companyName;
	}
	
	@Transient
	public void setCompanyName(String cName) {
		this.companyName = cName;
	}
	
	
}










