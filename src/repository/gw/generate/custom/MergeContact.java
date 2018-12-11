package repository.gw.generate.custom;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import persistence.globaldatarepo.entities.Agents;
import repository.ab.enums.TaxFilingStatus;
import repository.gw.enums.*;
import repository.gw.generate.ab.AbDues;
import repository.gw.helpers.DateUtils;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimPartyType;
import services.services.com.idfbins.emailphoneupdate.com.guidewire.ab.typekey.PrimaryPhoneType;

import java.util.ArrayList;
import java.util.Date;

public class MergeContact{
	//ContactDetail
	private boolean keep;
	private boolean isCompany;
	private String firstName;
	private String middleName;
	private String lastNameOrCompanyName;
	private repository.gw.enums.PersonPrefix prefix;
	private repository.gw.enums.Suffix suffix;
	private String formerName;
	private String lienNumber;
	private ArrayList<repository.gw.enums.ContactRole> roles;
	private String work;
	private String home;
	private String mobile;
	private String fax;
	private PrimaryPhoneType primaryPhone;
	private String email;
	private String altEmail;
	private String taxId;
	private String ssn;
	private TaxFilingStatus taxFilingStatus;
	private Date dateOfBirth;
	private Gender gender;
	private repository.gw.enums.MaritalStatus maritalStatus;
	private Currency preferredCurrency;
	private String occupation = "";
	private String dlNumber = "";
	private State dlState = null;
	private String notes = "";
	private String accountNumber;
	private repository.gw.enums.MembershipType membershipType;
	private ClaimPartyType claimPartyType;
	private Agents farmBureauAgent;
	private String website;
	private boolean cancelled;
	private String publicID;
	
	
	//Addresses
	private ArrayList<AddressInfo> addresses;
	
	//RelatedContacts
	private ArrayList<repository.gw.generate.custom.RelatedContacts> relatedContact;
	
	//EFT Information
	private ArrayList<ContactEFTData> eft;
	
	//Paid Dues
	private ArrayList<repository.gw.generate.ab.AbDues> dues = new ArrayList<>();
	
	//This constructor is for ease of use with the builder.  It won't do anyone much good without data.
	public MergeContact() {}
	
	//Constructors
	public MergeContact(boolean _keep, boolean _isCompany, String _firstName, String _middleName, String _lastNameOrCompanyName, repository.gw.enums.PersonPrefix _prefix, repository.gw.enums.Suffix _suffix, String _formerName, String _lienNumber, ArrayList<repository.gw.enums.ContactRole> _roles, String _work, String _home, String _mobile,
                        String _fax, PrimaryPhoneType _primaryPhone, String _email, String _altEmail, String _taxID, Date _dateOfBirth, Gender _gender, repository.gw.enums.MaritalStatus _maritalStatus, Currency money, String _occupation, String _dlNumber, State _state, String _notes, ArrayList<AddressInfo> _addresses,
                        ArrayList<repository.gw.generate.custom.RelatedContacts> _relatedContacts, ArrayList<ContactEFTData> _eft, String acctNum, repository.gw.enums.MembershipType _membershipType, ClaimPartyType _claimPartyType, Agents agent, String _website, boolean _cancelled, String id, ArrayList<repository.gw.generate.ab.AbDues> _dues) {

		setKeep(keep);
		setIsCompany(isCompany);
		setFirstName(firstName);
		setMiddleName(middleName);
		setLastNameOrCompanyName(lastNameOrCompanyName);
		setPrefix(prefix);
		setSuffix(_suffix);
		setFormerName(formerName);
		setLienNumber(lienNumber);
		setRoles(roles);
		setWork(work);
		setHome(home);
		setMobile(mobile);
		setFax(fax);
		setPrimaryPhone(primaryPhone);
		setEmail(email) ;
		setAltEmail(altEmail) ;
		setTaxID(_taxID);
		setDateOfBirth(dateOfBirth);
		setGender(gender);
		setMaritalStatus(maritalStatus);
		setCurrency(preferredCurrency);
		setOccupation(occupation);
		setState(dlState);
		setDLNumber(_dlNumber);
		setNotes(notes);
		setAddressInfo(addresses); 
		setRelatedContacts(_relatedContacts); 
		setEFT(eft);
		setAccountNumber(accountNumber); 
		setMembershipType(membershipType); 
		setClaimPartyType(claimPartyType); 
		setFarmBureauAgent(farmBureauAgent);
		setWebsite(website);
		setCancelled(cancelled); 
		setPublicID(publicID);
		setDues(_dues);
	}
	
		 
	
	//ContactDetail
	
	public boolean getKeep() {
		return this.keep;
	}
	
	public void setKeep(boolean _keep) {
		this.keep=_keep;
	}
	
	public boolean getIsCompany() {
		return isCompany;
	}
	
	public void setIsCompany(boolean _isCompany) {
		this.isCompany = _isCompany;
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
	
	public String getLastNameOrCompanyName() {
		return this.lastNameOrCompanyName;
	}

	public void setLastNameOrCompanyName(String _lastName) {
		this.lastNameOrCompanyName = _lastName;
	}	
	
	public repository.gw.enums.PersonPrefix getPrefix() {
		return this.prefix;
	}

	public void setPrefix(PersonPrefix _prefix) {
		this.prefix = _prefix;
	}
	
	public repository.gw.enums.Suffix getSuffix() {
		return this.suffix;
	}

	public void setSuffix(Suffix _suffix) {
		this.suffix = _suffix;
	}
	
	public String getFormerName() {
		return this.formerName;
	}

	public void setFormerName(String _formerName) {
		this.formerName = _formerName;
	}	
	
	public String getLienNumber() {
		return this.lienNumber;
	}

	public void setLienNumber(String _lienNumber) {
		if(_lienNumber.matches("^[0-9]{6}$")) {
			this.lienNumber = _lienNumber;
		}
	}
	
	public ArrayList<repository.gw.enums.ContactRole> getRoles(){
		return this.roles;
	}
	
	public void setRoles(ArrayList<repository.gw.enums.ContactRole> _roles) {
		this.roles = _roles;
	}
	
	public void addRole(ContactRole role) {
		this.roles.add(role);
	}
	
	public String getWork() {
		return this.work;
	}

	public void setWork(String _work) {
		this.work = _work;
	}	
	
	public String getHome() {
		return this.home;
	}

	public void setHome(String _home) {
		this.home = _home;
	}	
	
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String _mobile) {
		this.mobile = _mobile;
	}	
	
	public String getFax() {
		return this.fax;
	}

	public void setFax(String _fax) {
		this.fax = _fax;
	}	
		
		
	public PrimaryPhoneType getPrimaryPhone() {
		return this.primaryPhone;
	}

	public void setPrimaryPhone(PrimaryPhoneType _primaryPhone) {
		this.primaryPhone = _primaryPhone;
	}	
		
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String _email) {
		this.email = _email;
	}	
		
	public String getAltEmail() {
		return this.altEmail;
	}

	public void setAltEmail(String _altEmail) {
		this.altEmail = _altEmail;
	}	
		
	public String getTaxID() {
		return this.taxId;
	}

	public void setTaxID(String _taxID) {
		this.taxId = _taxID;
	}	
		
	public String getDateOfBirth(String dateFormat) {
		return DateUtils.dateFormatAsString(dateFormat, this.dateOfBirth);
	}
	
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date _dateOfBirth) {
		this.dateOfBirth = _dateOfBirth;
	}	
		
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender _gender) {
		this.gender = _gender;
	}
		

	public repository.gw.enums.MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus _maritalStatus) {
		this.maritalStatus = _maritalStatus;
	}

	public Currency getCurrency() {
		return preferredCurrency;
	}

	public void setCurrency(Currency money) {
		this.preferredCurrency = money;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String _occupation) {
		this.occupation = _occupation;
	}

	public State getState() {
		return dlState;
	}

	public void setState(State _state) {
		this.dlState = _state;
	}
	
	public String getDLNumber() {
		return this.dlNumber;
	}

	public void setDLNumber(String _dlNumber) {
		this.dlNumber = _dlNumber;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String _notes) {
		this.notes = _notes;
	}

	public ArrayList<AddressInfo> getAddresses() {
		return this.addresses;
	}

	public void setAddressInfo(ArrayList<AddressInfo> _addresses) {
		this.addresses = _addresses;
	}
	
	public ArrayList<repository.gw.generate.custom.RelatedContacts> getRelatedContacts() {
		return this.relatedContact;
	}
	
	public void addRelatedContacts(repository.gw.generate.custom.RelatedContacts _relatedContact) {
		this.relatedContact.add(_relatedContact);
	}

	public void setRelatedContacts(ArrayList<RelatedContacts> _relatedContacts) {
		this.relatedContact = _relatedContacts;
	}
	
	public ArrayList<ContactEFTData> getEFT() {
		return eft;
	}
	
	public void setEFT(ArrayList<ContactEFTData> _eft) {
		this.eft = _eft;
	}

	public void addEFT(ContactEFTData _eft) {
		this.eft.add(_eft);
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String acctNum) {
		if(acctNum.matches("^\\d{6}$")) {
			this.accountNumber = acctNum;
		} else {
			
		}
	}

	public repository.gw.enums.MembershipType getMembershipType() {
		return this.membershipType;
	}
	
	public void setMembershipType(MembershipType type) {
		this.membershipType = type;
	}
	
	public ClaimPartyType getClaimPartyType() {
		return this.claimPartyType;
	}
	
	public void setClaimPartyType(ClaimPartyType type) {
		this.claimPartyType = type;
	}
	
	public Agents getFarmBureauAgent() {
		return this.farmBureauAgent;
	}
	
	public void setFarmBureauAgent(Agents agent) {
		this.farmBureauAgent = agent;
	}
	
	public String getWebsite() {
		return this.website;
	}
	
	public void setWebsite(String _website) {
		this.website = _website;
	}
	
	public boolean getCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean _cancelled) {
		this.cancelled = _cancelled;
	}

	public String getPublicID() {
		return this.publicID;
	}
	
	public void setPublicID(String id) {
		this.publicID = id;
	}
	
	public ArrayList<repository.gw.generate.ab.AbDues> getDues(){
		return this.dues;
	}
	
	public void setDues(ArrayList<AbDues> _dues) {
		this.dues = _dues;
	}
	
	public TaxFilingStatus getTaxFilingStatus() {
		return this.taxFilingStatus;
	}
	
	public void setTaxFilingStatus(TaxFilingStatus _taxFilingStatus) {
		this.taxFilingStatus = _taxFilingStatus;
	}
	
	public String getSSN() {
		return this.ssn;
	}
	
	public void setSSN(String _ssn) {
		this.ssn = _ssn;
	}
}

