package persistence.globaldatarepo.entities;
// Generated Oct 30, 2018 2:48:46 PM by Hibernate Tools 5.2.10.Final

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * InsuranceScoreTestCases generated by hbm2java
 */
@Entity
@Table(name = "InsuranceScoreTestCases", schema = "dbo", catalog = "QAWIZPROGlobalDataRepository")
public class InsuranceScoreTestCases {

	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String suffix;
	private String houseNumber;
	private String direction;
	private String streetName;
	private String type;
	private String unitApt;
	private String city;
	private String state;
	private String zipCode;
	private String ssn;
	private String dobasof102017;
	private String deceased;
	private String model1;
	private String score1;
	private String scoreInd;
	private String fact1;
	private String fact2;
	private String fact3;
	private String fact4;
	private String scoreCardInd;

	public InsuranceScoreTestCases() {
	}

	public InsuranceScoreTestCases(String firstName, String middleName, String lastName, String suffix,
			String houseNumber, String direction, String streetName, String type, String unitApt, String city,
			String state, String zipCode, String ssn, String dobasof102017, String deceased, String model1,
			String score1, String scoreInd, String fact1, String fact2, String fact3, String fact4,
			String scoreCardInd) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.suffix = suffix;
		this.houseNumber = houseNumber;
		this.direction = direction;
		this.streetName = streetName;
		this.type = type;
		this.unitApt = unitApt;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.ssn = ssn;
		this.dobasof102017 = dobasof102017;
		this.deceased = deceased;
		this.model1 = model1;
		this.score1 = score1;
		this.scoreInd = scoreInd;
		this.fact1 = fact1;
		this.fact2 = fact2;
		this.fact3 = fact3;
		this.fact4 = fact4;
		this.scoreCardInd = scoreCardInd;
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

	@Column(name = "FirstName", length = 50)
	public String getFirstName() {
		return this.firstName.trim();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MiddleName", length = 50)
	public String getMiddleName() {
		return this.middleName.trim();
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LastName", length = 50)
	public String getLastName() {
		return this.lastName.trim();
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "Suffix", length = 50)
	public String getSuffix() {
		return this.suffix.trim();
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "HouseNumber", length = 50)
	public String getHouseNumber() {
		return this.houseNumber.trim();
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Column(name = "Direction", length = 50)
	public String getDirection() {
		return this.direction.trim();
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Column(name = "StreetName", length = 50)
	public String getStreetName() {
		return this.streetName.trim();
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	@Column(name = "Type", length = 50)
	public String getType() {
		return this.type.trim();
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "UnitApt", length = 50)
	public String getUnitApt() {
		return this.unitApt.trim();
	}

	public void setUnitApt(String unitApt) {
		this.unitApt = unitApt;
	}

	@Column(name = "City", length = 50)
	public String getCity() {
		return this.city.trim();
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "State", length = 50)
	public String getState() {
		return this.state.trim();
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "ZipCode", length = 50)
	public String getZipCode() {
		return this.zipCode.trim();
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "SSN", length = 50)
	public String getSsn() {
		return this.ssn.trim();
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	@Column(name = "DOBasof102017", length = 50)
	public String getDobasof102017() {
		return this.dobasof102017.trim();
	}

	public void setDobasof102017(String dobasof102017) {
		this.dobasof102017 = dobasof102017;
	}

	@Column(name = "Deceased", length = 50)
	public String getDeceased() {
		return this.deceased.trim();
	}

	public void setDeceased(String deceased) {
		this.deceased = deceased;
	}

	@Column(name = "Model1", length = 50)
	public String getModel1() {
		return this.model1.trim();
	}

	public void setModel1(String model1) {
		this.model1 = model1;
	}

	@Column(name = "Score1", length = 50)
	public String getScore1() {
		return this.score1.trim();
	}

	public void setScore1(String score1) {
		this.score1 = score1;
	}

	@Column(name = "ScoreInd", length = 50)
	public String getScoreInd() {
		return this.scoreInd.trim();
	}

	public void setScoreInd(String scoreInd) {
		this.scoreInd = scoreInd;
	}

	@Column(name = "Fact1", length = 50)
	public String getFact1() {
		return this.fact1.trim();
	}

	public void setFact1(String fact1) {
		this.fact1 = fact1;
	}

	@Column(name = "Fact2", length = 50)
	public String getFact2() {
		return this.fact2.trim();
	}

	public void setFact2(String fact2) {
		this.fact2 = fact2;
	}

	@Column(name = "Fact3", length = 50)
	public String getFact3() {
		return this.fact3.trim();
	}

	public void setFact3(String fact3) {
		this.fact3 = fact3;
	}

	@Column(name = "Fact4", length = 50)
	public String getFact4() {
		return this.fact4.trim();
	}

	public void setFact4(String fact4) {
		this.fact4 = fact4;
	}

	@Column(name = "ScoreCardInd", length = 50)
	public String getScoreCardInd() {
		return this.scoreCardInd.trim();
	}

	public void setScoreCardInd(String scoreCardInd) {
		this.scoreCardInd = scoreCardInd;
	}
	
	
	@Transient
	public String getaddressLine1() {
		String toReturn = "";
		toReturn = toReturn + houseNumber.trim();
		if(!direction.isEmpty()) {
			toReturn = toReturn + " " + direction.trim();
		}
		toReturn = toReturn + " " + streetName.trim();
		if(!type.isEmpty()) {
			toReturn = toReturn + " " + type.trim();
		}
		if(!unitApt.isEmpty()) {
			toReturn = toReturn + " " + unitApt.trim();
		}
//		toReturn = toReturn + " " + city;
//		toReturn = toReturn + ", " + state;
//		toReturn = toReturn + " " + zipCode;
		return toReturn;
	}

}



















