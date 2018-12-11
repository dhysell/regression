package repository.gw.generate.custom;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.entities.LHTable;
import persistence.globaldatarepo.helpers.CityCountyHelper;
import persistence.globaldatarepo.helpers.LHTableHelper;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.Vehicle;
import repository.gw.enums.*;
import repository.gw.generate.GenerateContact;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;

import java.util.ArrayList;

public class AdditionalInterest {

	private repository.gw.enums.ContactSubType companyOrInsured = repository.gw.enums.ContactSubType.Company;
	private repository.gw.enums.CreateNew newContact = repository.gw.enums.CreateNew.Do_Not_Create_New;
	private String companyName = "Mountain America Credit Union";
	private String personFirstName;
	private String personLastName;
	private String socialSecurityTaxNum;
	private String lienholderNumber = "985375";
	private String phone = "208" + StringsUtils.generateRandomNumberDigits(7);
	private String contactRole = "Lienholder";
	private AddressInfo address = new AddressInfo("PO Box 961292", "", "Fort Worth", State.Texas, "76161-0292", "Tarrant", "United States", repository.gw.enums.AddressType.Lienholder);
	private repository.gw.enums.AdditionalInterestType additionalInterestType = repository.gw.enums.AdditionalInterestType.Mortgagee;
	private Vehicle.AdditionalInterestTypeCPP additionalInterestTypeCPP = Vehicle.AdditionalInterestTypeCPP.LossPayableClauseIDCA_31_3001;
	private boolean setFirstMortgage = true;
	private boolean firstMortgage = true;
	private boolean setAppliedToBuilding = false;
	private boolean appliedToBuilding = true;
	private boolean setAppliedToBPP = false;
	private boolean appliedToBPP = true;
	private String loanContractNumber = "LN" + String.valueOf(NumberUtils.generateRandomNumberDigits(5));
	private String lienholderPayerAssignmentString;
	private String lienholderNameFromPolicyCenter;
	private repository.gw.enums.AdditionalInterestBilling additionalInterestBilling = repository.gw.enums.AdditionalInterestBilling.Bill_Insured;
	private double additionalInterestPremiumAmount = 0;
	private repository.gw.enums.AdditionalInterestSubType additionalInterestSubType = repository.gw.enums.AdditionalInterestSubType.BOPBuilding;
	private int buildingNumberTiedToLH;
	private int locationNumberTiedToLH;
	
	
	public AdditionalInterest(AdditionalInterest ai) {
		this.companyOrInsured = ai.companyOrInsured;
		this.newContact = ai.newContact;
		this.companyName = ai.companyName;
		this.personFirstName = ai.personFirstName;
		this.personLastName = ai.personLastName;
		this.socialSecurityTaxNum = ai.socialSecurityTaxNum;
		this.lienholderNumber = ai.lienholderNumber;
		this.phone = ai.phone;
		this.contactRole = ai.contactRole;
		this.address = ai.address;
		this.additionalInterestType = ai.additionalInterestType;
		this.additionalInterestTypeCPP = ai.additionalInterestTypeCPP;
		this.setFirstMortgage = ai.setFirstMortgage;
		this.firstMortgage = ai.firstMortgage;
		this.setAppliedToBuilding = ai.setAppliedToBuilding;
		this.appliedToBuilding = ai.appliedToBuilding;
		this.setAppliedToBPP = ai.setAppliedToBPP;
		this.appliedToBPP = ai.appliedToBPP;
		this.loanContractNumber = ai.loanContractNumber;
		this.lienholderPayerAssignmentString = ai.lienholderPayerAssignmentString;
		this.lienholderNameFromPolicyCenter = ai.lienholderNameFromPolicyCenter;
		this.additionalInterestBilling = ai.additionalInterestBilling;
		this.additionalInterestPremiumAmount = ai.additionalInterestPremiumAmount;
		this.additionalInterestSubType = ai.additionalInterestSubType;
		this.buildingNumberTiedToLH = ai.buildingNumberTiedToLH;
		this.locationNumberTiedToLH = ai.locationNumberTiedToLH;
	}
	

	public AdditionalInterest(String companyName, AddressInfo address) {
		setCompanyOrInsured(repository.gw.enums.ContactSubType.Company);
		setCompanyName(companyName);
		setAddress(address);
	}

	// Note ** This constructor creates a new lienholder in ContactManager.
	public AdditionalInterest(String firstName, String lastNameOrCompanyName, AddressInfo address) throws Exception {
		if (lastNameOrCompanyName == null || lastNameOrCompanyName.equals("")) {
			Assert.fail("lastNameOrCompanyName Cannot be Null");
		}

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver batchDriver = new DriverBuilder().buildGWWebDriver(cf);
		ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<>();
		rolesToAdd.add(ContactRole.Lienholder);
		repository.gw.generate.GenerateContact myLien = null;
		if(firstName==null || firstName.equals("")) {
			myLien = new repository.gw.generate.GenerateContact.Builder(batchDriver)
					.withRoles(rolesToAdd)
					.withCompanyName(lastNameOrCompanyName)
					.withGeneratedLienNumber(true)
					.build(repository.gw.enums.GenerateContactType.Company);

			setCompanyOrInsured(repository.gw.enums.ContactSubType.Company);
			setCompanyName(lastNameOrCompanyName);
		}else {
			myLien = new GenerateContact.Builder(batchDriver)
					.withRoles(rolesToAdd)
					.withFirstLastName(firstName, lastNameOrCompanyName)
					.withGeneratedLienNumber(true)
					.build(GenerateContactType.Company);

			setCompanyOrInsured(repository.gw.enums.ContactSubType.Person);
			setPersonFirstName(myLien.firstName);
			setPersonLastName(myLien.lastName);
		}
		setLienholderNumber(myLien.lienNumber);	
		setAddress(myLien.addresses.get(0));
		GuidewireHelpers gwHelpers = new GuidewireHelpers(batchDriver);
		gwHelpers.logout();

		batchDriver.quit();
	}

	public AdditionalInterest() throws Exception {
		LHTable lhContact = new LHTableHelper().getRandomCompany();
		final Contacts newContact = convertFromTable(lhContact);
		this.lienholderNumber = lhContact.getLhnumber();


		if (newContact.isContactIsCompany()) {
			this.setCompanyName(newContact.getContactName());
			this.setCompanyOrInsured(repository.gw.enums.ContactSubType.Company);
		} else {
			String[] name = newContact.getContactName().split(",");
			this.setPersonLastName(name[0]);
			String[] firstMiddleName = name[1].trim().split(" ");
			this.setPersonFirstName(firstMiddleName[0]);
			this.setCompanyOrInsured(repository.gw.enums.ContactSubType.Person);
		}
		this.setLienholderNumber(newContact.getContactNumber().split(",")[0]);
		this.setContactRole(newContact.getContactRoles().split(",")[0]);
		this.setAddress(new AddressInfo() {
			{
				this.setLine1(newContact.getContactAddressLine1());
				this.setCity(newContact.getContactCity());
				this.setState(setGetStateEnum(newContact.getContactState()));
				this.setZip(newContact.getContactZip());
				this.setCounty(getCountyEnum(CityCountyHelper.getCounty(newContact.getContactCity()).getCounty()));
				this.setType(AddressType.Business);
			}
		});
	}

	private Contacts convertFromTable(LHTable lhFromTable) {
		Contacts foo = new Contacts();
		foo.setContactName((lhFromTable.getPersonCompany().equals("company") ? lhFromTable.getCompanyName() : lhFromTable.getFirstName() + lhFromTable.getLastName()));
		foo.setContactIsCompany((lhFromTable.getPersonCompany().equals("company")));
		foo.setContactAddressLine1(lhFromTable.getAddressLine());
		foo.setContactCity(lhFromTable.getCity());
		foo.setContactState(lhFromTable.getState());
		foo.setContactZip(lhFromTable.getZip());
		return foo;
	}

	public AdditionalInterest(repository.gw.enums.ContactSubType companyPerson) throws Exception {

		if (companyPerson.equals(repository.gw.enums.ContactSubType.Company)) {
			LHTable newAI = new LHTableHelper().getRandomCompany();
			this.setCompanyOrInsured(repository.gw.enums.ContactSubType.Company);
			setCompanyName(newAI.getCompanyName());
			setAddress(new AddressInfo(newAI.getAddressLine(), newAI.getCity(), State.valueOfAbbreviation(newAI.getState()), newAI.getZip()));
			setLienholderNumber(newAI.getLhnumber());
		} else {
			LHTable newAI = new LHTableHelper().getRandomPerson();
			this.setCompanyOrInsured(repository.gw.enums.ContactSubType.Person);
			setPersonFirstName(newAI.getFirstName());
			setPersonLastName(newAI.getLastName());
			setAddress(new AddressInfo(newAI.getAddressLine(), newAI.getCity(), State.valueOfAbbreviation(newAI.getState()), newAI.getZip()));
			setLienholderNumber(newAI.getLhnumber());
		}
	}

	public AdditionalInterest(repository.gw.enums.ContactSubType company, repository.gw.enums.AdditionalInterestType lessorpl) throws Exception {
		AdditionalInterest foo = new AdditionalInterest(company);
		foo.setAdditionalInterestType(lessorpl);
	}

	public repository.gw.enums.ContactSubType getCompanyOrInsured() {
		return companyOrInsured;
	}

	public void setCompanyOrInsured(repository.gw.enums.ContactSubType companyOrInsured) {
		if (companyOrInsured == ContactSubType.Contact) {
			Assert.fail("Contact Type Must be a Company or Person");
		}
		this.companyOrInsured = companyOrInsured;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPersonFullName() {
		return (personFirstName + " " + personLastName);
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

	public String getLienholderNumber() {
		return lienholderNumber;
	}

	public void setLienholderNumber(String lienholderNumber) {
		this.lienholderNumber = lienholderNumber;
	}

	public repository.gw.enums.CreateNew getNewContact() {
		return newContact;
	}

	public void setNewContact(CreateNew newContact) {
		this.newContact = newContact;
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

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public boolean isFirstMortgage() {
		return firstMortgage;
	}

	public void setFirstMortgage(boolean firstMortgage) {
		this.firstMortgage = firstMortgage;
	}

	public boolean isAppliedToBuilding() {
		return appliedToBuilding;
	}

	public void setAppliedToBuilding(boolean appliedToBuilding) {
		this.appliedToBuilding = appliedToBuilding;
	}

	public boolean isAppliedToBPP() {
		return appliedToBPP;
	}

	public void setAppliedToBPP(boolean appliedToBPP) {
		this.appliedToBPP = appliedToBPP;
	}

	public repository.gw.enums.AdditionalInterestType getAdditionalInterestType() {
		return additionalInterestType;
	}

	public void setAdditionalInterestType(AdditionalInterestType additionalInterestType) {
		this.additionalInterestType = additionalInterestType;
	}

	public String getLoanContractNumber() {
		return loanContractNumber;
	}

	public void setLoanContractNumber(String loanContractNumber) {
		this.loanContractNumber = loanContractNumber.toUpperCase();
	}

	public String getLienholderPayerAssignmentString() {
		return lienholderPayerAssignmentString;
	}

	public void setLienholderPayerAssignmentString(String lienholderPayerAssignmentString) {
		this.lienholderPayerAssignmentString = lienholderPayerAssignmentString;
	}

	public String getLienholderNameFromPolicyCenter() {
		return lienholderNameFromPolicyCenter;
	}

	public void setLienholderNameFromPolicyCenter(String lienholderNameFromPolicyCenter) {
		this.lienholderNameFromPolicyCenter = lienholderNameFromPolicyCenter;
	}

	public repository.gw.enums.AdditionalInterestBilling getAdditionalInterestBilling() {
		return additionalInterestBilling;
	}

	public void setAdditionalInterestBilling(AdditionalInterestBilling additionalInterestBilling) {
		this.additionalInterestBilling = additionalInterestBilling;
	}

	public double getAdditionalInterestPremiumAmount() {
		return additionalInterestPremiumAmount;
	}

	public void setAdditionalInterestPremiumAmount(double additionalInterestPremiumAmount) {
		this.additionalInterestPremiumAmount = additionalInterestPremiumAmount;
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

	public String getAdditionalInterestAddressListingCheck() {
		String additionalInterestAddressListing = "(" + getAddress().getState().getAbbreviation() + ") "
				+ getAddress().getCity() + " - " + getAddress().getLine1();
		return additionalInterestAddressListing;
	}

	public Vehicle.AdditionalInterestTypeCPP getAdditionalInterestTypeCPP() {
		return additionalInterestTypeCPP;
	}

	public void setAdditionalInterestTypeCPP(Vehicle.AdditionalInterestTypeCPP additionalInterestTypeCPP) {
		this.additionalInterestTypeCPP = additionalInterestTypeCPP;
	}

	public repository.gw.enums.AdditionalInterestSubType getAdditionalInterestSubType() {
		return additionalInterestSubType;
	}

	public void setAdditionalInterestSubType(AdditionalInterestSubType additionalInterestSubType) {
		this.additionalInterestSubType = additionalInterestSubType;
	}

	public int getBuildingNumberTiedToLH() {
		return buildingNumberTiedToLH;
	}

	public void setBuildingNumberTiedToLH(int buildingNumberTiedToLH) {
		this.buildingNumberTiedToLH = buildingNumberTiedToLH;
	}

	public int getLocationNumberTiedToLH() {
		return locationNumberTiedToLH;
	}

	public void setLocationNumberTiedToLH(int locationNumberTiedToLH) {
		this.locationNumberTiedToLH = locationNumberTiedToLH;
	}

	public boolean isSetFirstMortgage() {
		return setFirstMortgage;
	}

	public void setSetFirstMortgage(boolean setFirstMortgage) {
		this.setFirstMortgage = setFirstMortgage;
	}

	public boolean isSetAppliedToBuilding() {
		return setAppliedToBuilding;
	}

	public void setSetAppliedToBuilding(boolean setAppliedToBuilding) {
		this.setAppliedToBuilding = setAppliedToBuilding;
	}

	public boolean isSetAppliedToBPP() {
		return setAppliedToBPP;
	}

	public void setSetAppliedToBPP(boolean setAppliedToBPP) {
		this.setAppliedToBPP = setAppliedToBPP;
	}

}
