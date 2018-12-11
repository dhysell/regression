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

import java.util.ArrayList;

public class PolicyLocationAdditionalInsured extends AbstractAdditionalInsured {

	private repository.gw.enums.AdditionalInsuredRole aiRole;
	private boolean specialWording = false;
	private String specialWordingDesc = "Default special wording description.";
	private String specialWordingAcord101Desc = "Default acord 101 description.";
	private boolean waiverOfSubro = false;
	private boolean lessorLeasedEquipment = false;
	private String lessorLeasedEquipmentDesc = "Default lessor leased equipment description.";
	@SuppressWarnings("serial")
	private ArrayList<State> statesList = new ArrayList<State>() {
		{
			this.add(State.Idaho);
		}
	};
	private String activitiesDescription = "Activity 1, Activity 2, Activity 3 :)";
	private Boolean oilAndGas = false;
	private Boolean UndergroundTanks = false;
	private Boolean aircraftAndAirport = false;
	private Boolean bridgeConstruction = false;
	private Boolean firearms = false;
	private Boolean railroads = false;
	private Boolean damsAndReservoirs = false;
	private Boolean mining = false;
	private repository.gw.enums.CreateNew newContact = repository.gw.enums.CreateNew.Do_Not_Create_New;

	public PolicyLocationAdditionalInsured() throws Exception {
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
				this.setState(setGetStateEnum(newContact.getContactState()));
				this.setZip(newContact.getContactZip());
				this.setCounty(getCountyEnum(CityCountyHelper.getCounty(newContact.getContactCity()).getCounty()));
				// this.setCountry("");
				this.setType(repository.gw.enums.AddressType.Business);
			}
		});
		this.setAiRole(repository.gw.enums.AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
	}

	public PolicyLocationAdditionalInsured(repository.gw.enums.ContactSubType companyPerson) throws Exception {
		final Contacts newContact = ContactsHelpers.getRandomContact(companyPerson.getValue());
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
		this.setAiRole(repository.gw.enums.AdditionalInsuredRole.MortgageesAssigneesOrReceivers);
	}

	public PolicyLocationAdditionalInsured(repository.gw.enums.ContactSubType companyOrPerson, String companyName,
                                           repository.gw.enums.AdditionalInsuredRole aiRole, AddressInfo address) throws GuidewireException {
		super(companyOrPerson, companyName, aiRole, address);
		setAiRole(aiRole);
	}

	public PolicyLocationAdditionalInsured(ContactSubType companyOrPerson, String personFirstName,
                                           String personLastName, repository.gw.enums.AdditionalInsuredRole aiRole, AddressInfo address) throws GuidewireException {
		super(companyOrPerson, personFirstName, personLastName, aiRole, address);
		setAiRole(aiRole);
	}

	public repository.gw.enums.AdditionalInsuredRole getAiRole() {
		return aiRole;
	}

	public void setAiRole(AdditionalInsuredRole aiRole) {
		if (aiRole.isExistsOnBOLine()) {
			Assert.fail("aiRole is Not Possible for a Locations Additional Insured");
		} else {
			this.aiRole = aiRole;
		}
	}

	public boolean isSpecialWording() {
		return specialWording;
	}

	public void setStatesList(ArrayList<State> states) {
		this.statesList.clear();// Clear defaulted State.Idaho
		this.statesList = states;
	}

	public void setActivityDescription(String desc) {
		this.activitiesDescription = desc;
	}

	public String getActivityDescription() {
		return this.activitiesDescription;
	}

	public ArrayList<State> getStatesList() {
		return this.statesList;
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

	public boolean isLessorLeasedEquipment() {
		return lessorLeasedEquipment;
	}

	public void setLessorLeasedEquipment(boolean lessorLeasedEquipment) {
		if (this.aiRole.getRole().contains("Leased Equipment")) {
			this.lessorLeasedEquipment = lessorLeasedEquipment;
		} else {
			Assert.fail("Lessor of Leased Equipment Role Must be Chosen to Set the ListProducts");
		}
	}

	public String getLessorLeasedEquipmentDesc() {
		return lessorLeasedEquipmentDesc;
	}

	public void setLessorLeasedEquipmentDesc(String lessorLeasedEquipmentDesc) {
		if (this.aiRole.getRole().contains("Leased Equipment")) {
			this.lessorLeasedEquipmentDesc = lessorLeasedEquipmentDesc;
		} else {
			Assert.fail("Lessor of Leased Equipment Role Must be Chosen to Set the ListProducts");
		}
	}

	public void setOilAndGas(Boolean yesno) {
		this.oilAndGas = yesno;
	}

	public void setUndergroundTanks(Boolean yesno) {
		this.UndergroundTanks = yesno;
	}

	public void setAircraftAndAirport(Boolean yesno) {
		this.aircraftAndAirport = yesno;
	}

	public void setBridgeConstrusction(Boolean yesno) {
		this.bridgeConstruction = yesno;
	}

	public void setFirearms(Boolean yesno) {
		this.firearms = yesno;
	}

	public void setRailroads(Boolean yesno) {
		this.railroads = yesno;
	}

	public void setDamsAndReservoirs(Boolean yesno) {
		this.damsAndReservoirs = yesno;
	}

	public void setMining(Boolean yesno) {
		this.mining = yesno;
	}

	public Boolean getOilAndGas() {
		return this.oilAndGas;
	}

	public Boolean getUndergroundTanks() {
		return this.UndergroundTanks;
	}

	public Boolean getAircraftAndAirport() {
		return this.aircraftAndAirport;
	}

	public Boolean getBridgeConstrusction() {
		return this.bridgeConstruction;
	}

	public Boolean getFirearms() {
		return this.firearms;
	}

	public Boolean getRailroads() {
		return this.railroads;
	}

	public Boolean getDamsAndReservoirs() {
		return this.damsAndReservoirs;
	}

	public Boolean getMining() {
		return this.mining;
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

}
