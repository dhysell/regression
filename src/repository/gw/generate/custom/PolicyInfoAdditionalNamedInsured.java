package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.CityCountyHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import repository.gw.enums.*;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PolicyInfoAdditionalNamedInsured extends AbstractAdditionalInsured {

	private List<AddressInfo> addressList = new ArrayList<AddressInfo>();
	private Date aniDOB = DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -35);
	private repository.gw.enums.CreateNew newContact = repository.gw.enums.CreateNew.Do_Not_Create_New;
    private repository.gw.enums.AdditionalNamedInsuredType relationshipToPNI = repository.gw.enums.AdditionalNamedInsuredType.Affiliate;
    private boolean unique = true;

    //membership dues stuff
    private boolean hasMembershipDues = false;
	private double membershipDuesAmount = 0;
    private boolean membershipDuesAreLienPaid = false;
    private repository.gw.generate.custom.AdditionalInterest membershipDuesLienHolder = null;
    //End membership dues stuff
    
    public PolicyInfoAdditionalNamedInsured(Contact contact) {
    	this.unique = false;
    	this.setPersonFirstName(contact.getFirstName());
    	this.setPersonLastName(contact.getLastName());
    	this.setPersonMiddleName(contact.getMiddleName());
    	this.setAddress(contact.getAddress());
    	this.addressList.add(getAddress());
    	this.aniDOB = contact.getDob();
    	this.newContact = repository.gw.enums.CreateNew.Create_New_Only_If_Does_Not_Exist;
    	this.setCompanyOrPerson(repository.gw.enums.ContactSubType.Person);
    }
    
	
	public PolicyInfoAdditionalNamedInsured() throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomContact();
		if (newContact.isContactIsCompany()) {
			this.setCompanyName(newContact.getContactName());
		} else {
			String[] name = newContact.getContactName().split(",");
			this.setPersonLastName(name[0]);
			String[] firstMiddleName = name[1].split(" ");
			this.setPersonLastName(firstMiddleName[0]);
		}
		this.setMemberNumber(newContact.getContactNumber().split(",")[0]);
		this.setContactRole(newContact.getContactRoles().split(",")[0]);
		this.setAddress(new AddressInfo() {
			{
				this.setLine1(newContact.getContactAddressLine1());
				// this.setLine2("");
				this.setCity(newContact.getContactCity());
				this.setState(setGetStateEnum(newContact.getContactState()));
				this.setZip(newContact.getContactZip());
				this.setCounty(getCountyEnum(CityCountyHelper.getCounty(newContact.getContactCity()).getCounty()));
				// this.setCountry("");
				this.setType(repository.gw.enums.AddressType.Business);
			}
		});
		this.addressList.add(this.getAddress());
	}

	public PolicyInfoAdditionalNamedInsured(repository.gw.enums.ContactSubType companyOrPerson) throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomContact(companyOrPerson.getValue());
		if (newContact.isContactIsCompany()) {
			setCompanyName(newContact.getContactName());
			setCompanyOrPerson(repository.gw.enums.ContactSubType.Company);
		} else {
			String[] name = newContact.getContactName().split("\\s*(=>|,|\\s)\\s*");
			setPersonLastName(name[0]);
			String[] firstMiddleName = name[1].split(" ");
			setPersonFirstName(firstMiddleName[0]);
			setCompanyOrPerson(repository.gw.enums.ContactSubType.Person);
		}
		setMemberNumber(newContact.getContactNumber().split(",")[0]);
		if (newContact.getContactRoles() != " ") {
			setContactRole(newContact.getContactRoles().split(",")[0]);
		}
		setAddress(new AddressInfo() {
			{
				this.setLine1(newContact.getContactAddressLine1());
				// this.setLine2("");
				this.setCity(newContact.getContactCity());
				this.setState(setGetStateEnum(newContact.getContactState()));
				this.setZip(newContact.getContactZip());
				this.setCounty(getCountyEnum(CityCountyHelper.getCounty(newContact.getContactCity()).getCounty()));
				// this.setCountry("");
				this.setType(AddressType.Business);
			}
		});
		this.addressList.add(this.getAddress());
	}

	public PolicyInfoAdditionalNamedInsured(repository.gw.enums.ContactSubType companyOrPerson, String companyName,
                                            repository.gw.enums.AdditionalNamedInsuredType relationshipToPNI, AddressInfo address) {
		if (companyOrPerson == null) {
			Assert.fail("CompanyOrPerson Name Cannot be Null");
		}

		if (companyOrPerson != repository.gw.enums.ContactSubType.Company) {
			Assert.fail("CompanyOrPerson Must be a Company For This Constructor");
		}

		if (companyName == null) {
			Assert.fail("CompanyName Cannot be Null");
		}

		if (relationshipToPNI == null) {
			Assert.fail("RelationshipToPNI Cannot be Null");
		}

		if (address == null) {
			Assert.fail("Address Cannot be Null");
		}

		this.setCompanyOrPerson(companyOrPerson);
		this.setCompanyName(companyName);
		this.relationshipToPNI = relationshipToPNI;
		this.address = address;
		this.addressList.add(this.address);
	}

	public PolicyInfoAdditionalNamedInsured(repository.gw.enums.ContactSubType companyOrPerson, String personFirstName,
                                            String personLastName, repository.gw.enums.AdditionalNamedInsuredType relationshipToPNI, AddressInfo address) {
		if (companyOrPerson == null) {
			Assert.fail("CompanyOrPerson Name Cannot be Null");
		}

		if (companyOrPerson != repository.gw.enums.ContactSubType.Person) {
			Assert.fail("CompanyOrPerson Must be a Person For This Constructor");
		}

		if (personFirstName == null) {
			Assert.fail("PersonFirstName Cannot be Null");
		}

		if (personLastName == null) {
			Assert.fail("PersonLastName Cannot be Null");
		}

		if (relationshipToPNI == null) {
			Assert.fail("RelationshipToPNI Cannot be Null");
		}

		if (address == null) {
			Assert.fail("Address Cannot be Null");
		}

		setCompanyOrPerson(companyOrPerson);
		setPersonFirstName(personFirstName);
		setPersonLastName(personLastName);
		setRelationshipToPNI(relationshipToPNI);
		setAddress(address);
		this.addressList.add(this.address);
	}

	public repository.gw.enums.ContactSubType getCompanyOrPerson() {
		return companyOrPerson;
	}

	public void setCompanyOrPerson(repository.gw.enums.ContactSubType companyOrPerson) {
		if (companyOrPerson == ContactSubType.Contact) {
			Assert.fail("Contact Type Must be a Company or Person");
		}
		this.companyOrPerson = companyOrPerson;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		if(unique){
			companyName = new repository.gw.helpers.StringsUtils().getUniqueName(companyName);
		}
		this.companyName = companyName;
	}
	
	public String getPersonFullName() {
		String personFullName = "";
		if (getPersonMiddleName() == null) {
			personFullName = getPersonFirstName() + " " + getPersonLastName();
		} else {
			personFullName = getPersonFirstName() + " " + getPersonMiddleName() + " " + getPersonLastName();
		}
		return personFullName;
	}

	public String getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(String personLastName) {
		if(unique){
			String [] name = new StringsUtils().getUniqueName(this.personFirstName, null, personLastName);
			personLastName = name[name.length - 1];
		}
		this.personLastName = personLastName;
	}

	public String getSocialSecurityTaxNum() {
		return socialSecurityTaxNum;
	}

	public void setSocialSecurityTaxNum(String socialSecurityTaxNum) {
		this.socialSecurityTaxNum = socialSecurityTaxNum;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContactRole() {
		return contactRole;
	}

	public void setContactRole(String contactRole) {
		this.contactRole = contactRole;
	}

	public repository.gw.enums.AdditionalNamedInsuredType getRelationshipToPNI() {
		return relationshipToPNI;
	}

	public void setRelationshipToPNI(AdditionalNamedInsuredType relationshipToPNI) {
		this.relationshipToPNI = relationshipToPNI;
	}

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}
	
	public List<AddressInfo> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<AddressInfo> addressList) {
		this.addressList = addressList;
	}

	public repository.gw.enums.CreateNew getNewContact() {
		return newContact;
	}

	public void setNewContact(CreateNew newContact) {
		this.newContact = newContact;
	}

	private State setGetStateEnum(String state) {
		State[] states = State.values();
		for (State state1 : states) {
			if (state1.getName().equalsIgnoreCase(state)) {
				return state1;
			}
		}
		return null;
	}

	private CountyIdaho getCountyEnum(String county) {
		CountyIdaho[] counties = CountyIdaho.values();
		for (CountyIdaho county1 : counties) {
			if (county1.getValue().equalsIgnoreCase(county)) {
				return county1;
			}
		}
		return null;
	}

	public String getPersonMiddleName() {
		return this.personMiddleName;
	}

	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}
	
	public boolean getUnique(){
		return this.unique;
	}
	
	public void setUnique(boolean _unique){
		this.unique = _unique;
	}

	public Date getAniDOB() {
		return aniDOB;
	}

	public void setAniDOB(Date aniDOB) {
		this.aniDOB = aniDOB;
	}

    public boolean hasMembershipDues() {
        return hasMembershipDues;
    }

    public void setHasMembershipDues(boolean hasMembershipDues) {
        this.hasMembershipDues = hasMembershipDues;
    }

    public double getMembershipDuesAmount() {
        return membershipDuesAmount;
    }

    public void setMembershipDuesAmount(double membershipDuesAmount) {
        this.membershipDuesAmount = membershipDuesAmount;
    }

    public boolean isMembershipDuesAreLienPaid() {
        return membershipDuesAreLienPaid;
    }

    public void setMembershipDuesAreLienPaid(boolean membershipDuesAreLienPaid) {
        this.membershipDuesAreLienPaid = membershipDuesAreLienPaid;
    }

    public repository.gw.generate.custom.AdditionalInterest getMembershipDuesLienHolder() {
        return membershipDuesLienHolder;
    }

    public void setMembershipDuesLienHolder(AdditionalInterest membershipDuesLienHolder) {
        this.membershipDuesLienHolder = membershipDuesLienHolder;
    }
}
