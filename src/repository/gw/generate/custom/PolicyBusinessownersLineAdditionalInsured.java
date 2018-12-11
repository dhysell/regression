package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.CityCountyHelper;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.exception.GuidewireException;

public class PolicyBusinessownersLineAdditionalInsured extends AbstractAdditionalInsured {

	private repository.gw.enums.AdditionalInsuredRole aiRole = repository.gw.enums.AdditionalInsuredRole.GrantorOfFranchise;
	private boolean specialWording = false;
	private String specialWordingDesc = "Default special wording description.";
	private String specialWordingAcord101Desc = "Default acord 101 description.";
	private boolean waiverOfSubro = false;
	private String vendorRoleListProducts = "Default, List, Of, Products";
	private repository.gw.enums.CreateNew newContact = repository.gw.enums.CreateNew.Do_Not_Create_New;

	public PolicyBusinessownersLineAdditionalInsured() throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomContact();
		if (newContact.isContactIsCompany()) {
			this.setCompanyName(newContact.getContactName());
		} else {
			String[] name = newContact.getContactName().split("\\s*(=>|,|\\s)\\s*");
			this.setPersonLastName(name[0]);
			String[] firstMiddleName = name[1].split(" ");
			this.setPersonFirstName(firstMiddleName[0]);
		}
		this.setMemberNumber(newContact.getContactNumber().split(",")[0]);
		this.setContactRole(newContact.getContactRoles().split(",")[0]);
		this.setAddress(new AddressInfo() {
			{
				this.setLine1(newContact.getContactAddressLine1());
				// this.setLine2("");
				this.setCity(newContact.getContactCity());
				this.setState(State.valueOfName(newContact.getContactState()));
				this.setZip(newContact.getContactZip());
				this.setCounty(getCountyEnum(CityCountyHelper.getCounty(newContact.getContactCity()).getCounty()));
				// this.setCountry("");
				this.setType(repository.gw.enums.AddressType.Business);
			}
		});
	}

	public PolicyBusinessownersLineAdditionalInsured(repository.gw.enums.ContactSubType companyOrPerson) throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomContact(companyOrPerson.getValue());
		if (newContact.isContactIsCompany()) {
			this.setCompanyName(newContact.getContactName());
			this.setCompanyOrPerson(repository.gw.enums.ContactSubType.Company);
		} else {
			String[] name = newContact.getContactName().split(",");
			this.setPersonLastName(name[0]);
			String[] firstMiddleName = name[1].split(" ");
			this.setPersonLastName(firstMiddleName[0]);
			this.setCompanyOrPerson(repository.gw.enums.ContactSubType.Person);
		}
		this.setMemberNumber(newContact.getContactNumber().split(",")[0]);
		if (newContact.getContactRoles() != " ") {
			this.setContactRole(newContact.getContactRoles().split(",")[0]);
		}
		this.setAddress(new AddressInfo() {
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
	}

	public PolicyBusinessownersLineAdditionalInsured(repository.gw.enums.ContactSubType companyOrPerson, String companyName,
                                                     repository.gw.enums.AdditionalInsuredRole aiRole, AddressInfo address) throws GuidewireException {
		super(companyOrPerson, companyName, aiRole, address);
		setAiRole(aiRole);
	}

	public PolicyBusinessownersLineAdditionalInsured(ContactSubType companyOrPerson, String personFirstName,
                                                     String personLastName, repository.gw.enums.AdditionalInsuredRole aiRole, AddressInfo address) throws GuidewireException {
		super(companyOrPerson, personFirstName, personLastName, aiRole, address);
		setAiRole(aiRole);
	}

	public repository.gw.enums.AdditionalInsuredRole getAiRole() {
		return aiRole;
	}

	public void setAiRole(AdditionalInsuredRole aiRole) throws GuidewireException {
		if (!aiRole.isExistsOnBOLine()) {
			Assert.fail("aiRole is Not Possible for a Businessowners Line Additional Insured");
		} else {
			this.aiRole = aiRole;
		}
	}

	public boolean isSpecialWording() {
		return specialWording;
	}

	public void setSpecialWording(boolean specialWording) {
		this.specialWording = specialWording;
	}

	public String getSpecialWordingDesc() {
		return specialWordingDesc;
	}

	public void setSpecialWordingDesc(String specialWordingDesc) {
		this.specialWordingDesc = specialWordingDesc;
	}

	public String getSpecialWordingAcord101Desc() {
		return specialWordingAcord101Desc;
	}

	public void setSpecialWordingAcord101Desc(String specialWordingAcord101Desc) {
		this.specialWordingAcord101Desc = specialWordingAcord101Desc;
	}

	public boolean isWaiverOfSubro() {
		return waiverOfSubro;
	}

	public void setWaiverOfSubro(boolean waiverOfSubro) {
		this.waiverOfSubro = waiverOfSubro;
	}

	public String getVendorRoleListProducts() {
		return vendorRoleListProducts;
	}

	public void setVendorRoleListProducts(String vendorRoleListProducts) throws GuidewireException {
		if (!this.aiRole.getRole().contains("Vendor")) {
			Assert.fail("Vendor Role Must be Chosen to Set the ListProducts");
		}
		this.vendorRoleListProducts = vendorRoleListProducts;
	}

	public repository.gw.enums.CreateNew getNewContact() {
		return newContact;
	}

	public void setNewContact(CreateNew newContact) {
		this.newContact = newContact;
	}

	// jon larsen 8/11/2015
	// methods moved to appropriate enums
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
}
