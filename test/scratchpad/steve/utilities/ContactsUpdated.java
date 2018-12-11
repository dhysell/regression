package scratchpad.steve.utilities;

import java.util.HashMap;
import java.util.Map;

import com.idfbins.enums.State;

import repository.gw.generate.custom.AddressInfo;
public class ContactsUpdated {

	private boolean updatedEmail;
	private String account;
	private String firstName;
	private String middleName;
	private String lastName;
	private AddressInfo address;
	private String email;
	private Map<String, String> phoneNumbers = new HashMap<String, String>();
	
	public ContactsUpdated(boolean updatedEmail, String firstName, String lastName, String email) {
		
	}
	public ContactsUpdated(boolean updatedEmail, String firstName, String lastName, String address, String phoneNumber, String phoneNumberType) {
		
	}
	
	public ContactsUpdated() {}
	
	public boolean getUpdatedEmail() {
		return this.updatedEmail;
	}
	
	public void setUpdatedEmail(boolean _email) {
		this.updatedEmail = _email;
	}
	
	public String getAccount() {
		return this.account;
	}
	
	public void setAccount(String _account) {
		this.account = _account;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String _firstName) {
		this.firstName = _firstName;
	}
	
	public String getMiddleName() {
		return this.middleName;
	}
	
	public void setMiddleName(String _middleName) {
		this.middleName = _middleName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String _lastName) {
		this.lastName = _lastName;
	}
	
	public AddressInfo getAddress() {
		return this.address;
	}
	
	public void setAddress(AddressInfo _address) {
		this.address = _address;
	}
	
	public void setAddress(String addressLine, String city, State state, String zip) {
		this.address = new AddressInfo(addressLine, city, state, zip);
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String _email) {
		this.email = _email;
	}
	
	public String getPhoneNumber(String type) {
		return this.phoneNumbers.get(type);
	}
	
	public void setPhoneNumber(String type, String _phoneNumber) {
		this.phoneNumbers.put(type, _phoneNumber);
	}	
}
