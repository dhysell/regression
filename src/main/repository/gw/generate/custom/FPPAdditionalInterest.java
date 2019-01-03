package repository.gw.generate.custom;


import com.idfbins.enums.State;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;

public class FPPAdditionalInterest extends AdditionalInterest {

	private repository.gw.enums.AdditionalInterestType additionalInterestType;

	public FPPAdditionalInterest(repository.gw.enums.AdditionalInterestType _additionalInterestType) throws Exception {
		super();
		setAdditionalInterestType(_additionalInterestType);
	}
	
	public FPPAdditionalInterest(repository.gw.enums.AdditionalInterestType _additionalInterestType, ContactSubType compOrPerson, String companyName, AddressInfo address) throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomPersonOrCompWithRole(compOrPerson.getValue(), ContactRole.Lienholder.getValue());
		setAdditionalInterestType(_additionalInterestType);
		setCompanyOrInsured(compOrPerson);
		setCompanyName(newContact.getContactName());
		setAddress(new AddressInfo(newContact.getContactAddressLine1(), newContact.getContactCity(), State.valueOf(newContact.getContactState()), newContact.getContactZip()));
		
	}

	// Getters and Setters
	public void setAdditionalInterestType(repository.gw.enums.AdditionalInterestType addType) {
		if ((addType.compareTo(repository.gw.enums.AdditionalInterestType.LienholderPL) == 0)
				|| (addType.compareTo(repository.gw.enums.AdditionalInterestType.LessorPL) == 0)) {
			this.additionalInterestType = addType;
		} else {
			Assert.fail("FPPAdditionalInterest Type must be either LienholderFPP or LessorFPP");
		}
	}

	public AdditionalInterestType getAdditionalInterestType() {
		return this.additionalInterestType;
	}
}
