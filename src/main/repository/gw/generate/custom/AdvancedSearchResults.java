package repository.gw.generate.custom;

import com.idfbins.enums.State;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;

import java.util.ArrayList;

public class AdvancedSearchResults {
	
	protected ArrayList<String> numbers = new ArrayList<String>();
	protected String ssnTin;
	protected String firstName;
	protected String middleName;
	protected String lastNameOrCompanyName;
	protected AddressInfo address;
	protected String phone;
	protected repository.gw.enums.ContactSubType contactSubType;
	protected ArrayList<repository.gw.enums.ContactRole> roles = new ArrayList<repository.gw.enums.ContactRole>();
	protected Agents agent;
	protected int age = -1; //age will be -1 if not listed.
	
//	protected boolean returnMail;
//	Add returned mail if necessary
	
	public AdvancedSearchResults(){}
	
	public AdvancedSearchResults(String _numbers, 
								 String _ssnTin,
								 String _firstName,
								 String _middleName,
								 String _lastNameOrCompanyName,
								 String _addressLine1,
								 String city,
								 State state,
								 String zip,
								 String _phone,
								 String _contactSubType,
								 String _roles,
								 String _agent) throws Exception{
		
		setNumbersFromSearchResults(_numbers);
		setPartialSSN(_ssnTin);
		setFirstName(_firstName);
		setMiddleName(_middleName);
		setLastNameOrCompanyName(_lastNameOrCompanyName);
		setAddress(_addressLine1, city, state, zip);
		setPhone( _phone);
		setContactSubType( _contactSubType);
		setRolesFromSearchResults( _roles);
		setAgentFromSearchResults(_agent);
	}
	
	public ArrayList<String> getNumbers(){
		return this.numbers;
	}
	
	public void setNumbersFromSearchResults(String numbersString){
		String[] parsedString = numbersString.split("[,\\s]+");
		for(String num : parsedString){
			numbers.add(num);
		}
	}
	
	
	public String getPartialSsn(){
		return this.ssnTin;
	}
	
	public void setPartialSSN(String _ssnTin){
		if(_ssnTin.matches("^[\\S]{4}$")){
			this.ssnTin = _ssnTin;
		}
	}
	
	public String getLastNameOrCompanyName(){
		return this.lastNameOrCompanyName;
	}
	
	public void setLastNameOrCompanyName(String _name){
		this.lastNameOrCompanyName = _name;
	}
	
	public String getMiddleName(){
		return this.middleName;
	}
	
	public void setMiddleName(String _middleName){
		this.middleName = _middleName;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setFirstName(String _name){
		this.firstName = _name;
	}
	
	public AddressInfo getAddress(){
		return this.address;
	}
	
	public void setAddress(String _addressLine1, String city, State state, String zip){
		AddressInfo myAddress = new AddressInfo();
		myAddress.setLine1(_addressLine1);
		myAddress.setCity(city);
		myAddress.setState(state);
		myAddress.setZip(zip);
		this.address = myAddress;
	}
		
	public String getPhone(){
		return this.phone;
	}
	
	public void setPhone(String _phone){
		this.phone = _phone;
	}
	
	public repository.gw.enums.ContactSubType getContactSubType(){
		return this.contactSubType;
	}
	
	public void setContactSubType(String _contactSubType){
		this.contactSubType = ContactSubType.valueOf(_contactSubType);
	}
	
	public ArrayList<repository.gw.enums.ContactRole> getRoles(){
		return this.roles;
	}
	
	public void setRolesFromSearchResults(String _roles){
		if(_roles == "" ||_roles == null ||_roles.isEmpty()){
			String[] parsedString = _roles.split("[,]+"); 
			for(String role : parsedString){
				if(role.equals("Claim Party")){
					this.roles.add(repository.gw.enums.ContactRole.ClaimParty);
				} else{
					this.roles.add(ContactRole.valueOf(role.trim()));
				} 
			}
			this.roles = null;
		}
	}
	
	public Agents getAgent(){
		return this.agent;
	}
	
	public Agents setAgentFromSearchResults(String _agent) throws Exception{
		if(_agent.length()>=2){
			String[] agentName = _agent.split("\\s+");
			if(agentName.length == 5){
				return AgentsHelper.getAgentByName(agentName[2], agentName[3]);
			}else{
				return AgentsHelper.getAgentByName(agentName[3], agentName[4]);
			}
			
		}else{
			return null;
		}
	}
	
	public int getAge(){
		return age;
	}
	
	public void setAge(int _age){
		if(_age<-1 || _age > 112){
			this.age = -1;
		} else{
			this.age = _age;
		}
	}
}
