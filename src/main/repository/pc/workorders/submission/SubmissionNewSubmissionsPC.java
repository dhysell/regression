package repository.pc.workorders.submission;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.LexisNexis;
import persistence.globaldatarepo.helpers.LexisNexisHelper;
import repository.pc.search.SearchAddressBookPC;

//I decided to create a new NewSubmissionPC 

public class SubmissionNewSubmissionsPC extends SearchAddressBookPC {
	private WebDriver driver;

	public SubmissionNewSubmissionsPC(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public List<String> fillOutFormSearchAndEitherCreateNewOrUseExisting(boolean basicSearch, CreateNew createNew, ContactSubType insPersonOrCompany, String insLastName, String insFirstName, String middleName, String insCompanyName, String city, State state, String zip) throws Exception {

		if (zip != null) {
			if (zip.length() > 5 && zip.length() < 10) {
				zip = zip.substring(0, 5);
			}
		}
		String companyNameToCreate = null;
		String lastNameToCreate = null;
		String fullName = null;
		boolean found = false;

		if (insPersonOrCompany == ContactSubType.Company) {
			if (createNew == CreateNew.Create_New_Always) {
				companyNameToCreate = new StringsUtils().getUniqueName(insCompanyName);
				found = searchAddressBookByCompanyName(basicSearch, companyNameToCreate, null, city, state, zip, createNew);
			} else if (createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
				companyNameToCreate = insCompanyName;
				if (companyNameToCreate.length() > 30) {
					int numberOfCharactersToRemove = companyNameToCreate.length() - 29;
					int companyNameLength = companyNameToCreate.length();
					companyNameToCreate = companyNameToCreate.substring(0, companyNameLength - numberOfCharactersToRemove);
				}
				found = searchAddressBookByCompanyName(basicSearch, companyNameToCreate, null, city, state, zip, CreateNew.Create_New_Only_If_Does_Not_Exist);
			} else if (createNew == CreateNew.Do_Not_Create_New) {
				companyNameToCreate = insCompanyName;
				found = searchAddressBookByCompanyName(basicSearch, companyNameToCreate, null, city, state, zip, CreateNew.Do_Not_Create_New);
			}
		} else {
			if (createNew == CreateNew.Create_New_Always) {
				String[] uniqueFullName = new StringsUtils().getUniqueName(insFirstName, null, insLastName);
				lastNameToCreate = uniqueFullName[2];
				found = searchAddressBookByFirstLastName(basicSearch, uniqueFullName[0], lastNameToCreate, null, city, state, zip, CreateNew.Create_New_Always);
			} else if (createNew == CreateNew.Create_New_Only_If_Does_Not_Exist) {
				lastNameToCreate = insLastName;
				if (middleName != null && middleName.length() > 0) {
					fullName = insFirstName + " " + middleName + " " + lastNameToCreate;
				} else {
					fullName = insFirstName + " " + lastNameToCreate;
				}
				if (fullName.length() > 30) {
					int numberOfCharactersToRemove = fullName.length() - 29;
					int lastNameLength = lastNameToCreate.length();
					lastNameToCreate = lastNameToCreate.substring(0, lastNameLength - numberOfCharactersToRemove);
				}
				found = searchAddressBookByFirstLastName(basicSearch, insFirstName, lastNameToCreate, null, city, state, zip, CreateNew.Create_New_Only_If_Does_Not_Exist);
			} else if (createNew == CreateNew.Do_Not_Create_New) {
				lastNameToCreate = insLastName;
				found = searchAddressBookByFirstLastName(basicSearch, insFirstName, insLastName, null, city, state, zip, CreateNew.Do_Not_Create_New);
			}
		}

		String foundString = null;
		if (found) {
			foundString = "found";
		} else {
			foundString = "notFound";
		}

		List<String> toReturn = new ArrayList<String>();
		toReturn.add(lastNameToCreate);
		toReturn.add(insFirstName);
		toReturn.add(companyNameToCreate);
		toReturn.add(foundString);
		toReturn.add(middleName);

		return toReturn;
	}


	public boolean fillOutFormSearchCreateNewWithoutStamp(boolean basicSearch, ContactSubType insPersonOrCompany, String insLastName, String insFirstName, String middleName, String insCompanyName, String city, State state, String zip) throws Exception {

		boolean found = false;
		found = searchAddressBookByCompanyName(basicSearch, insCompanyName, null, city, state, zip, CreateNew.Create_New_Always);
		return found;
	}

	public String searchOrCreateSubmission(GeneratePolicy policy) throws Exception {

		boolean addressFound;
		LexisNexis customer = null;
		if (policy.lexisNexisData) {
			addressFound = fillOutFormSearchCreateNewWithoutStamp(policy.basicSearch, policy.pniContact.getPersonOrCompany(), policy.pniContact.getLastName(), policy.pniContact.getFirstName(), policy.pniContact.getMiddleName(), policy.pniContact.getCompanyName(), policy.pniContact.getAddress().getCity(), policy.pniContact.getAddress().getState(), policy.pniContact.getAddress().getZip());
			int i = 0;
			while (addressFound && i < 20) {
				customer = LexisNexisHelper.getRandomCustomer(ClockUtils.getCurrentDates(getDriver()).get(ApplicationOrCenter.PolicyCenter), policy.pniContact.isPerson(), policy.prefillPersonal, policy.prefillCommercial, policy.insuranceScore, policy.mvr, policy.clueAuto, policy.clueProperty);
				policy.pniContact.setFirstName(customer.getFirstName());
				policy.pniContact.setMiddleName(customer.getMiddleName());
				policy.pniContact.setLastName(customer.getLastName());
				policy.pniContact.setDob(driver, customer.getDob());
				policy.pniContact.setSocialSecurityNumber(customer.getSsn());
				policy.pniContact.setAddress(new AddressInfo(customer.getStreet(), customer.getApartment(), customer.getCity(), State.valueOfAbbreviation(customer.getState()), customer.getZip(), CountyIdaho.Ada, "United States", AddressType.Home));
				addressFound = fillOutFormSearchCreateNewWithoutStamp(policy.basicSearch, policy.pniContact.getPersonOrCompany(), policy.pniContact.getLastName(), policy.pniContact.getFirstName(), policy.pniContact.getMiddleName(), policy.pniContact.getCompanyName(), policy.pniContact.getAddress().getCity(), policy.pniContact.getAddress().getState(), policy.pniContact.getAddress().getZip());
				i++;
			}//END WHILE
		} else {
			if(policy.veriskData) {
				addressFound = fillOutFormSearchCreateNewWithoutStamp(policy.basicSearch, policy.pniContact.getPersonOrCompany(), policy.pniContact.getLastName(), policy.pniContact.getFirstName(), policy.pniContact.getMiddleName(), policy.pniContact.getCompanyName(), null, null, null);
				return "";
			}
			List<String> namesCreated = fillOutFormSearchAndEitherCreateNewOrUseExisting(policy.basicSearch, new GuidewireHelpers(getDriver()).getCreateNew(policy), policy.pniContact.getPersonOrCompany(), policy.pniContact.getLastName(), policy.pniContact.getFirstName(), policy.pniContact.getMiddleName(), policy.pniContact.getCompanyName(), null, null, null);


			policy.pniContact.setLastName(namesCreated.get(0));
			policy.pniContact.setFirstName(namesCreated.get(1));
			policy.pniContact.setCompanyName(namesCreated.get(2));

			if (policy.pniContact.getMiddleName() == null) {
				policy.pniContact.setFullName(policy.pniContact.getFirstName() + " " + policy.pniContact.getLastName());
			} else {
				policy.pniContact.setFullName(policy.pniContact.getFirstName() + " " + policy.pniContact.getMiddleName() + " " + policy.pniContact.getLastName());
			}//END ELSE
			return namesCreated.get(3);
		}//END ELSE
		return "";
	}	
}
