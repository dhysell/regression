package repository.gw.generate.ab.builders;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Agents;
import repository.ab.enums.TaxFilingStatus;
import repository.gw.enums.*;
import repository.gw.generate.ab.AbDues;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.ContactEFTData;
import repository.gw.generate.custom.MergeContact;
import repository.gw.generate.custom.RelatedContacts;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimPartyType;
import services.services.com.idfbins.emailphoneupdate.com.guidewire.ab.typekey.PrimaryPhoneType;

import java.util.ArrayList;
import java.util.Date;

public class MergeContactsBuilder {
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
		private String ssn;
		
		//Addresses
		private ArrayList<AddressInfo> addresses;
		
		//RelatedContacts
		private ArrayList<RelatedContacts> relatedContacts;
		
		//EFT Information
		private ArrayList<ContactEFTData> eft;
		
		//Paid Dues
		private ArrayList<repository.gw.generate.ab.AbDues> dues = new ArrayList<>();
		
		public MergeContactsBuilder(boolean _keep, boolean _isCompany, String _firstName, String _middleName, String _lastNameOrCompanyName, repository.gw.enums.PersonPrefix _prefix, repository.gw.enums.Suffix _suffix, String _formerName, String _lienNumber, ArrayList<repository.gw.enums.ContactRole> _roles, String _work, String _home, String _mobile,
                                    String _fax, PrimaryPhoneType _primaryPhone, String _email, String _altEmail, String _taxID, Date _dateOfBirth, Gender _gender, repository.gw.enums.MaritalStatus _maritalStatus, Currency money, String _occupation, String _dlNumber, State _state, String _notes, ArrayList<AddressInfo> _addresses,
                                    ArrayList<RelatedContacts> _relatedContacts, ArrayList<ContactEFTData> _eft, String acctNum, repository.gw.enums.MembershipType _membershipType, ClaimPartyType _claimPartyType, Agents agent, String _website, boolean _cancelled, String id, ArrayList<repository.gw.generate.ab.AbDues> _dues) {
				
				this.keep=_keep;
				this.isCompany = _isCompany;
				this.firstName = _firstName;	
				this.middleName = _middleName;
				this.lastNameOrCompanyName = _lastNameOrCompanyName;
				this.prefix = _prefix;
				this.suffix = _suffix;
				this.formerName = _formerName;
				this.lienNumber = _lienNumber;
				this.roles = _roles;
				this.work = _work;
				this.home = _home;
				this.mobile = _mobile;
				this.fax = _fax;
				this.primaryPhone = _primaryPhone;
				this.email = _email;
				this.altEmail = _altEmail;
				this.taxId = _taxID;
				this.dateOfBirth = _dateOfBirth;
				this.gender = _gender;
				this.maritalStatus = _maritalStatus;
				this.preferredCurrency = money;
				this.occupation = _occupation;
				this.dlNumber = _dlNumber;
				this.dlState = _state;
				this.notes = _notes;
				this.addresses = _addresses;
				this.relatedContacts = _relatedContacts;
				this.eft = _eft;
				this.accountNumber = acctNum;
				this.membershipType = _membershipType;
				this.claimPartyType = _claimPartyType;
				this.farmBureauAgent = agent;
				this.website = _website;
				this.cancelled = _cancelled;
				this.publicID = id;
				this.dues = _dues;
				
		}
		
		//ContactDetail
		
		
		
		public MergeContactsBuilder setKeep(boolean _keep) {
			this.keep=_keep;
			return this;
		}
		
		
		
		public MergeContactsBuilder setIsCompany(boolean _isCompany) {
			this.isCompany = _isCompany;
			return this;
		}
		
		

		public MergeContactsBuilder setFirstName(String _firstName) {
			this.firstName = _firstName;
			return this;
		}	
		
		public MergeContactsBuilder setMiddleName(String _middleName) {
			this.middleName = _middleName;
			return this;
		}	
		
		

		public MergeContactsBuilder setLastNameOrCompanyName(String _lastName) {
			this.lastNameOrCompanyName = _lastName;
			return this;
		}	
		
		

		public MergeContactsBuilder setPrefix(PersonPrefix _prefix) {
			this.prefix = _prefix;
			return this;
		}
		
		

		public MergeContactsBuilder setSuffix(Suffix _suffix) {
			this.suffix = _suffix;
			return this;
		}
		
		

		public MergeContactsBuilder setFormerName(String _formerName) {
			this.formerName = _formerName;
			return this;
		}	
		
		

		public MergeContactsBuilder setLienNumber(String _lienNumber) {
			if(_lienNumber.matches("^\\d{6}$")) {
				this.lienNumber = _lienNumber;
			}
			return this;
		}
		
		
		
		public MergeContactsBuilder setRoles(ArrayList<ContactRole> _roles) {
			this.roles = _roles;
			return this;
		}
		
		
		public MergeContactsBuilder setWork(String _work) {
			this.work = _work;
			return this;
		}	
		
		public MergeContactsBuilder setHome(String _home) {
			this.home = _home;
			return this;
		}	
		
		

		public MergeContactsBuilder setMobile(String _mobile) {
			this.mobile = _mobile;
			return this;
		}	
		
		
		public MergeContactsBuilder setFax(String _fax) {
			this.fax = _fax;
			return this;
		}	
			
			
		

		public MergeContactsBuilder setPrimaryPhone(PrimaryPhoneType _primaryPhone) {
			this.primaryPhone = _primaryPhone;
			return this;
		}	
			
		public MergeContactsBuilder setEmail(String _taxID) {
			this.taxId = _taxID;
			return this;
		}	
			
		

		public MergeContactsBuilder setAltEmail(String _altEmail) {
			this.altEmail = _altEmail;
			return this;
		}	
			
		

		public MergeContactsBuilder setTaxID(String _taxID) {
			this.taxId = _taxID;
			return this;
		}	
			
		public MergeContactsBuilder setDateOfBirth(Date _dateOfBirth) {
			this.dateOfBirth = _dateOfBirth;
			return this;
		}	
			
		
		public MergeContactsBuilder setGender(Gender _gender) {
			this.gender = _gender;
			return this;
		}
			

		
		public MergeContactsBuilder setLimit(MaritalStatus _maritalStatus) {
			this.maritalStatus = _maritalStatus;
			return this;
		}

		

		public MergeContactsBuilder setCurrency(Currency money) {
			this.preferredCurrency = money;
			return this;
		}

		

		public MergeContactsBuilder setOccupation(String _occupation) {
			this.occupation = _occupation;
			return this;
		}

		public MergeContactsBuilder setDlNumber(String _dlNumber) {
			this.dlNumber =_dlNumber;
			return this;
		}
		

		public MergeContactsBuilder setDlState(State _state) {
			this.dlState = _state;
			return this;
		}

		
		public MergeContactsBuilder setNotes(String _notes) {
			this.notes = _notes;
			return this;
		}

		public MergeContactsBuilder setAddressInfo(ArrayList<AddressInfo> _addresses) {
			this.addresses = _addresses;
			return this;
		}


		public MergeContactsBuilder setRelatedContacts(ArrayList<RelatedContacts> _relatedContacts) {
			this.relatedContacts = _relatedContacts;
			return this;
		}
		
		
		
		public MergeContactsBuilder setEFT(ArrayList<ContactEFTData> _eft) {
			this.eft = _eft;
			return this;
		}

		
		public MergeContactsBuilder setAccountNumber(String acctNum) {
			if(acctNum.matches("^\\d{6}$")) {
				this.accountNumber = acctNum;
			} else {
				
			}
			return this;
		}	
		
		public MergeContactsBuilder setMembershipType(MembershipType type) {
			this.membershipType = type;
			return this;
		}
		
		
		
		public MergeContactsBuilder setClaimPartyType(ClaimPartyType type) {
			this.claimPartyType = type;
			return this;
		}
		
		
		
		public MergeContactsBuilder setFarmBureauAgent(Agents agent) {
			this.farmBureauAgent = agent;
			return this;
		}
		
		
		
		public MergeContactsBuilder setWebsite(String _website) {
			this.website = _website;
			return this;
		}
		
				
		public MergeContactsBuilder setCancelled(boolean _cancelled) {
			this.cancelled = _cancelled;
			return this;
		}
		
		public MergeContactsBuilder setLienholderNumber(String lienNum) {
			if(lienNum.matches("^\\d{6}$")) {
				this.lienNumber = lienNum;
			} else {
				Assert.fail("Account Numbers must be six digits.  You passed in " + lienNum + ".");
			}
			return this;
		}
		
		public MergeContactsBuilder setSSN(String _ssn) {
			this.ssn = _ssn;
			return this;
		}
		
		public MergeContactsBuilder setPublicID(String id) {
			this.publicID = id;
			return this;
		}
		
				
		public MergeContactsBuilder setDues(ArrayList<AbDues> _dues) {
			this.dues = _dues;
			return this;
		}
		
		public MergeContactsBuilder setTaxFilingStatus(TaxFilingStatus _taxFilingStatus) {
			this.taxFilingStatus = _taxFilingStatus;
			return this;
		}
		
		public MergeContact build() {
			return new MergeContact(keep, isCompany, firstName, middleName, lastNameOrCompanyName, prefix, suffix, formerName, lienNumber, roles, work, home, mobile, fax, primaryPhone, email, altEmail, taxId, dateOfBirth, gender, maritalStatus, preferredCurrency, occupation, dlNumber, dlState, notes, addresses, relatedContacts, eft, accountNumber, membershipType, claimPartyType, farmBureauAgent, website, cancelled, publicID, dues);
		}
}