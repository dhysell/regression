package repository.gw.generate.custom;

import com.idfbins.enums.State;
import persistence.globaldatarepo.entities.Contacts;
import persistence.globaldatarepo.helpers.ContactsHelpers;
import repository.gw.enums.AdditionalInsuredTypeGL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;

import java.util.ArrayList;
import java.util.List;

public class CPPGLCoveragesAdditionalInsureds {

	
	repository.gw.enums.ContactSubType companyPerson = repository.gw.enums.ContactSubType.Company;
	String companyName = "";
	String firstName = "";
	String lastName = "";
	AddressInfo address = null;
	repository.gw.enums.AdditionalInsuredTypeGL type = repository.gw.enums.AdditionalInsuredTypeGL.AdditionalInsured_CoownerOfInsuredPremises_CG_20_27;
	boolean specialWording = false;
	String specialWordingDesc = "Some kind of thing here";
	String acord101Desc = "Acord descriptin goes here";
	boolean waiverOfsubroCA2404 = false;
	boolean designatedLocationsAggeragateLimitCG2504 = false;
	boolean DesignatedConstructionProjectGeneralAggregateLimit_CG_25_03 = false;
	List<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();

	List<State> stateList = new ArrayList<State>();
	String descriptionOfActivities = "I do something for some person";
	boolean oilAndGas = false;
	boolean undergroundTanks = false;
	boolean aircraftAndAirport = false;
	boolean bridgeConstruction = false;
	boolean damsAndReservoirs = false;
	boolean firearms = false;
	boolean railroads = false;
	boolean mining = false;
	String lsitOfProduct = "Stuff and Things";
	repository.gw.enums.CreateNew newContact = repository.gw.enums.CreateNew.Create_New_Only_If_Does_Not_Exist;
	String descOfLeasedEquipment = "Stuff and Things";
	
	public CPPGLCoveragesAdditionalInsureds() throws Exception {
		Contacts newContact = ContactsHelpers.getRandomContact("company");
		this.companyName = newContact.getContactName();
		
		// Set up address
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setLine1(newContact.getContactAddressLine1());
		addressInfo.setCity(newContact.getContactCity());
		addressInfo.setState(State.Idaho);
		addressInfo.setZip(newContact.getContactZip());
		this.address = addressInfo;
		
		this.stateList.add(State.Idaho);
	}
	
	public String getFormatedAddress() {
		return getAddress().getLine1() + ", " + getAddress().getCity() + ", " + getAddress().getState().getAbbreviation();
		//return "(ID) " + getAddress().getCity() + " - " + getAddress().getLine1();
	}
	
	
	public repository.gw.enums.ContactSubType getCompanyPerson() {
		return companyPerson;
	}
	public void setCompanyPerson(ContactSubType companyPerson) {
		this.companyPerson = companyPerson;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public repository.gw.enums.AdditionalInsuredTypeGL getType() {
		return type;
	}
	public void setType(AdditionalInsuredTypeGL type) {
		this.type = type;
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
	public String getAcord101Desc() {
		return acord101Desc;
	}
	public void setAcord101Desc(String acord101Desc) {
		this.acord101Desc = acord101Desc;
	}
	public boolean isWaiverOfsubroCA2404() {
		return waiverOfsubroCA2404;
	}
	public void setWaiverOfsubroCA2404(boolean waiverOfsubroCA0444) {
		this.waiverOfsubroCA2404 = waiverOfsubroCA0444;
	}
	public List<repository.gw.generate.custom.PolicyLocation> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<PolicyLocation> locationList) {
		this.locationList = locationList;
	}
	public List<State> getStateList() {
		return stateList;
	}
	public void setStateList(List<State> stateList) {
		this.stateList = stateList;
	}
	public String getDescriptionOfActivities() {
		return descriptionOfActivities;
	}
	public void setDescriptionOfActivities(String descriptionOfActivities) {
		this.descriptionOfActivities = descriptionOfActivities;
	}
	public boolean isOilAndGas() {
		return oilAndGas;
	}
	public void setOilAndGas(boolean oilAndGas) {
		this.oilAndGas = oilAndGas;
	}
	public boolean isUndergroundTanks() {
		return undergroundTanks;
	}
	public void setUndergroundTanks(boolean undergroundTanks) {
		this.undergroundTanks = undergroundTanks;
	}
	public boolean isAircraftAndAirport() {
		return aircraftAndAirport;
	}
	public void setAircraftAndAirport(boolean aircraftAndAirport) {
		this.aircraftAndAirport = aircraftAndAirport;
	}
	public boolean isBridgeConstruction() {
		return bridgeConstruction;
	}
	public void setBridgeConstruction(boolean bridgeConstruction) {
		this.bridgeConstruction = bridgeConstruction;
	}
	public boolean isDamsAndReservoirs() {
		return damsAndReservoirs;
	}
	public void setDamsAndReservoirs(boolean damsAndReservoirs) {
		this.damsAndReservoirs = damsAndReservoirs;
	}
	public boolean isFirearms() {
		return firearms;
	}
	public void setFirearms(boolean firearms) {
		this.firearms = firearms;
	}
	public boolean isRailroads() {
		return railroads;
	}
	public void setRailroads(boolean railroads) {
		this.railroads = railroads;
	}
	public boolean isMining() {
		return mining;
	}
	public void setMining(boolean mining) {
		this.mining = mining;
	}
	public String getLsitOfProduct() {
		return lsitOfProduct;
	}
	public void setLsitOfProduct(String lsitOfProduct) {
		this.lsitOfProduct = lsitOfProduct;
	}
	public AddressInfo getAddress() {
		return address;
	}
	public void setAddress(AddressInfo address) {
		this.address = address;
	}
	public repository.gw.enums.CreateNew getNewContact() {
		return newContact;
	}
	public void setNewContact(CreateNew newContact) {
		this.newContact = newContact;
	}
	public String getDescOfLeasedEquipment() {
		return descOfLeasedEquipment;
	}
	public void setDescOfLeasedEquipment(String descOfLeasedEquipment) {
		this.descOfLeasedEquipment = descOfLeasedEquipment;
	}
	public boolean isDesignatedLocationsAggeragateLimitCG2504() {
		return designatedLocationsAggeragateLimitCG2504;
	}
	public void setDesignatedLocationsAggeragateLimitCG2504(boolean designatedLocationsAggeragateLimitCG2504) {
		this.designatedLocationsAggeragateLimitCG2504 = designatedLocationsAggeragateLimitCG2504;
	}

	public boolean isDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03() {
		return DesignatedConstructionProjectGeneralAggregateLimit_CG_25_03;
	}

	public void setDesignatedConstructionProjectGeneralAggregateLimit_CG_25_03(boolean designatedConstructionProjectGeneralAggregateLimit_CG_25_03) {
		DesignatedConstructionProjectGeneralAggregateLimit_CG_25_03 = designatedConstructionProjectGeneralAggregateLimit_CG_25_03;
	}
	
	
	
	
	
	
	
}
