package scratchpad.steve.utilities;

public class ScriptItem {
	
	private String memberNum = null;
	private String ssn = null;
	private String lastName = null;
	private String firstName = null;
	private String email = null;
	private String address = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String homePhone = null;
	private String cellPhone = null;
	private String altPhone = null;
	private String businessPhone = null;
	
	//Constructor
	public ScriptItem() {}
	
	public ScriptItem(String _memberNum, String _ssn, String _lastName, String _firstName, String _email, String _address, String _city, String _state, String _zip, String _homePhone, String _cellPhone, String _altPhone, String _businessPhone) {
		setMemberNum(_memberNum);
		setSSN(_ssn);
		setLastName(_lastName);
		setFirstName(_firstName);
		setEmail(_email);
		setAddress(_address);
		setCity(_city);
		setState(_state);
		setZip(_zip);
		setHomePhone(_homePhone);
		setCellPhone(_cellPhone); 
		setAltPhone(_altPhone);
		setBusinessPhone(_businessPhone);
	}
	
	//Getters and Setters
	
	public String getMemberNum() {
		return this.memberNum;
	}
	
	public void setMemberNum(String _memberNum) {
		this.memberNum = _memberNum;
	}
	
	public String getSSN() {
		return this.ssn;
	}
	
	public void setSSN(String _ssn) {
		this.ssn = _ssn;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String _lastName) {
		this.lastName = _lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String _firstName) {
		this.firstName = _firstName;
	}
	
	public String getEmail() {
		return this.email;	
	}
	public void setEmail(String _email) {
		this.email = _email;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String _address) {
		this.address = _address;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String _city) {
		this.city = _city;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setState(String _state) {
		this.state = _state;
	}
	
	public String getZip() {
		return this.zip;
	}
	
	public void setZip(String _zip) {
		this.zip = _zip;
	}
	
	public String getHomePhone() {
		return this.homePhone;
	}
	
	public void setHomePhone(String _homePhone) {
		this.homePhone = _homePhone;
	}
	
	public String getCellPhone() {
		return this.cellPhone;
	}
	
	public void setCellPhone(String _cellPhone) {
		this.cellPhone = _cellPhone;
	}
	public String getAltPhone() {
		return this.altPhone;
	}
	
	public void setAltPhone(String _altPhone) {
		this.altPhone = _altPhone;
	}
	
	public String getBusinessPhone() {
		return this.businessPhone;
	}
	
	public void setBusinessPhone(String _businessPhone) {
		this.businessPhone = _businessPhone;
	}
	

}
