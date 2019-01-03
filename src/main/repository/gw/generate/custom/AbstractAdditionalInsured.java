package repository.gw.generate.custom;


import org.testng.Assert;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.RelationshipsAB;

public abstract class AbstractAdditionalInsured {

	protected repository.gw.enums.ContactSubType companyOrPerson;
	protected String companyName;
	protected String personFirstName;
	protected String personMiddleName;
	protected String personLastName;
	protected String socialSecurityTaxNum;
	protected String memberNumber;
	protected String phone;
	protected String contactRole;
	protected repository.gw.generate.custom.AddressInfo address;
	protected RelationshipsAB relationship;
	protected boolean relatedToPrimaryNamedInsured;
	protected repository.gw.generate.custom.Contact relatedContact;

	public AbstractAdditionalInsured() {

	}

	public AbstractAdditionalInsured(repository.gw.enums.ContactSubType companyOrPerson, String companyName, repository.gw.enums.AdditionalInsuredRole aiRole,
                                     repository.gw.generate.custom.AddressInfo address) {
		if (companyOrPerson == null) {
			Assert.fail("CompanyOrPerson cannot be Null");
		}
		if (companyOrPerson != repository.gw.enums.ContactSubType.Company) {
			Assert.fail("CompanyOrPerson must be a ContactSubType.Company for this constructor");
		}
		if (companyName == null) {
			Assert.fail("CompanyName cannot be Null");
		}
		if (aiRole == null) {
			Assert.fail("aiRole cannot be Null");
		}
		if (address == null) {
			Assert.fail("address cannot be Null");
		}

		setCompanyOrPerson(companyOrPerson);
		setCompanyName(companyName);
		setAddress(address);

	}

	public AbstractAdditionalInsured(repository.gw.enums.ContactSubType companyOrPerson, String personFirstName, String personLastName,
                                     AdditionalInsuredRole aiRole, repository.gw.generate.custom.AddressInfo address) {
		if (companyOrPerson == null) {
			Assert.fail("CompanyOrPerson cannot be Null");
		}
		if (companyOrPerson != repository.gw.enums.ContactSubType.Person) {
			Assert.fail("CompanyOrPerson must be a ContactSubType.Person for this Constructor");
		}
		if (personFirstName == null) {
			Assert.fail("PersonFirstName cannot be Null");
		}
		if (personLastName == null) {
			Assert.fail("PersonLastName cannot be Null");
		}
		if (aiRole == null) {
			Assert.fail("AdditionalInsuredRole cannot be Null");
		}

		setCompanyOrPerson(companyOrPerson);
		setPersonFirstName(personFirstName);
		setPersonLastName(personLastName);
		setAddress(address);
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
		this.companyName = companyName;
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

	public repository.gw.generate.custom.AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}
	
	public RelationshipsAB getRelationship() {
		return this.relationship;
	}

	public void setRelationship(RelationshipsAB _relationship) {
		this.relationship = _relationship;
	}
	
	public boolean getRelatedToPrimaryNamedInsured() {
		return this.relatedToPrimaryNamedInsured;
	}
	
	public void setRelationshipToPrimaryNamedInsured(boolean _relatedToPni) {
		this.relatedToPrimaryNamedInsured = _relatedToPni;
	}

	public repository.gw.generate.custom.Contact getRelatedContact() {
		return relatedContact;
	}

	public void setRelatedContact(Contact relatedContact) {
		this.relatedContact = relatedContact;
	}
	
	public String getPersonMiddleName() {
		return personMiddleName;
	}

	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}

}
