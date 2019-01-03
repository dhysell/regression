package repository.gw.generate.ab;

import com.idfbins.enums.State;
import repository.gw.enums.PhoneType;
import repository.gw.generate.custom.AddressInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfile {
	
	private String firstName;
	private String middleName;
	private String lastName;
	private String userName;
	private String password;
	private boolean activeDirectory = false;
	private boolean active;
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> groups = new ArrayList<String>();
	private String jobTitle;
	private String department;
	private AddressInfo address = new AddressInfo("275 Tierra Vista Dr.", "Pocatello", State.Idaho, "83201");
	private Map<String , repository.gw.enums.PhoneType> phoneNumbers = new HashMap<String , repository.gw.enums.PhoneType>();
	private String email;
	private repository.gw.enums.PhoneType primaryPhone = repository.gw.enums.PhoneType.Work;
	
	//Constructors
	
	public UserProfile(String _firstName, String _lastName, String _userName, String _password, boolean _activeDirectory, boolean _active, ArrayList<String> _roles, ArrayList<String> _groups, String _jobTitle, String _department, AddressInfo _address, String _email,  Map<String, repository.gw.enums.PhoneType> _phoneNumbers){
		setFirstName(_firstName);
		setLastName(_lastName);
		setUserName(_userName);
		setPassword(_password);
		setActiveDirectory(_activeDirectory);
		setActive(_active);
		setRoles(_roles);
		setGroups(_groups);
		setJobTitle(_jobTitle);
		setDepartment(_department);
		setAddressInfo(_address);
		setEmail(_email);
		setPhoneNumber(_phoneNumbers);
		setAddressInfo(_address);
		setEmail(_email);
	};
	
	//Getters and Setters
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String _firstName){
		if(!(_firstName == null || _firstName.equals("") || _firstName.isEmpty())){
			this.firstName = _firstName;
		}
	}
	
	public String getMiddleName(){
		return this.middleName;
	}
	
	public void setMiddleName(String _middleName){
		if(!(_middleName == null || _middleName.equals("") || _middleName.isEmpty())){
			this.middleName = _middleName;
		}
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setLastName(String _lastName){
		if(!(_lastName == null || _lastName.equals("") || _lastName.isEmpty())){
			this.lastName = _lastName;
		}
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String _userName){
		if(!(_userName == null || _userName.equals("") || _userName.isEmpty())){
			this.userName = _userName;
		}		
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String _password){
		if(!(_password == null || _password.equals("") || _password.isEmpty())){
			this.password = _password;
		} else{
			System.out.println("The password should not be blank.");
		}
	}
	
	public boolean getActiveDirectory() {
		return this.activeDirectory;
	}
	
	public void setActiveDirectory(boolean _activeDirectory) {
		this.activeDirectory = _activeDirectory;
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean _active) {
		this.active = _active;
	}
	
	public ArrayList<String> getRoles(){
		return this.roles;
	}
	
	public void setRoles(ArrayList<String> _roles){
		if(!(_roles == null || _roles.isEmpty())){
			this.roles = _roles;
		}
	}
	
	public void addRole(String role){
		if(!(role == null || role.equals("") || role.isEmpty())){
			this.roles.add(role);
		} else{
			System.out.println("The role should not be blank.");
		}
	}
	
	public ArrayList<String> getGroup(){
		return this.groups;
	}
	
	public void setGroups(ArrayList<String> _groups){
		if(!(_groups == null || _groups.isEmpty())){
			this.groups = _groups;
		}
	}
	
	public void addGroup(String _group){
		if(!(_group == null || _group.equals("") || _group.isEmpty())){
				this.groups.add(_group);
		} else{
			System.out.println("The group should not be blank.");
		}
	}
	
	public String getJobTitle(){
		return this.jobTitle;
	}
	
	public void setJobTitle(String _jobTitle){
		if(!(_jobTitle == null || _jobTitle.equals("") || _jobTitle.isEmpty())){
			this.jobTitle = _jobTitle;
		}
	}
	
	public String getDepartment(){
		return this.department;
	}
	
	public void setDepartment(String _department){
		if(!(_department == null || _department.equals("") || _department.isEmpty())){
			this.department = _department;
		}
	}
	
	public AddressInfo getAddressInfo(){
		return this.address;
	}
	
	public void setAddressInfo(AddressInfo _address){
		this.address = _address;
	}
	
	public void setAddressInfo(String line1, String city, State state, String zip){
		this.address = new AddressInfo(line1, city, state, zip);
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String _email){
		if(!(_email == null || _email.equals("") || _email.isEmpty())){
			this.email = _email;
		}
	}
	//private Map phoneNumbers = new HashMap();
	public Map<String, repository.gw.enums.PhoneType> getPhoneNumbers(){
		return this.phoneNumbers;
	}
		
	public void setPhoneNumber(String phone, repository.gw.enums.PhoneType phoneType){
		this.phoneNumbers.put(phone, phoneType);
	}
	
	public void setPhoneNumber(Map<String , repository.gw.enums.PhoneType> _phoneNumbers) {
		this.phoneNumbers = _phoneNumbers;
	}
	
	public repository.gw.enums.PhoneType getPrimaryPhone(){
		return this.primaryPhone;
	}
	
	public void setPrimaryPhone(PhoneType type){
		this.primaryPhone = type;
	}
	

}
