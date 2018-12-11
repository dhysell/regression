package repository.gw.generate.custom;

import repository.gw.enums.CoverageType;
import repository.gw.enums.IMFarmEquipment;

import java.util.ArrayList;

public class FarmEquipment {
	
	private IMFarmEquipment.IMFarmEquipmentType type;
	private repository.gw.enums.CoverageType coverageType;
	private IMFarmEquipment.IMFarmEquipmentDeductible deductible;
	private Boolean inspected;
	private Boolean existingDamage;
	private String owner;
	private ArrayList<String> additionalInsured = new ArrayList<String>();
	private ArrayList<IMFarmEquipmentScheduledItem> scheduledFarmEquipment;
	private String description = "";
	private String value = "15150";
	private String serialNumber = "1Tractor";
	private ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterest = new ArrayList<repository.gw.generate.custom.AdditionalInterest>();

	// Constructors
	// Inland Marine Farm Equipment
	public FarmEquipment(IMFarmEquipment.IMFarmEquipmentType _type, repository.gw.enums.CoverageType _coverageType,
                         IMFarmEquipment.IMFarmEquipmentDeductible _deductible, Boolean _inspected, Boolean _existingDamage, String _owner,
                         ArrayList<IMFarmEquipmentScheduledItem> _scheduledFarmEquipment) {
		setIMFarmEquipmentType(_type);
		setCoverageType(_coverageType);
		setDeductible(_deductible);
		setInspected(_inspected);
		setExistingDamage(_existingDamage);
		setOwner(_owner);
		setScheduledFarmEquipment(_scheduledFarmEquipment);
	}

	// Getters and Setters
	public void setFPPDescription(String _description) {
		this.description = _description;
	}

	public String getFPPDescription() {
		return this.description;
	}

	public void setFPPValue(String _value) {
		this.value = _value;
	}

	public String getFPPValue() {
		return this.value;
	}

	public void setFPPSerialNumber(String _serialNumber) {
		this.serialNumber = _serialNumber;
	}

	public String getFPPSerialNumber() {
		return this.serialNumber;
	}

	public void addAdditionalInterest(repository.gw.generate.custom.AdditionalInterest _addInterest) {
		this.additionalInterest.add(_addInterest);
	}

	public void setAdditionalInterests(ArrayList<repository.gw.generate.custom.AdditionalInterest> _addInterest) {
		this.additionalInterest = _addInterest;
	}

	public ArrayList<AdditionalInterest> getAdditionalInterests() {
		return this.additionalInterest;
	}

	public void setCoverageType(repository.gw.enums.CoverageType broadOrSpecial) {
		this.coverageType = broadOrSpecial;
	}

	public CoverageType getCoverageType() {
		return this.coverageType;
	}

	public void setDeductible(IMFarmEquipment.IMFarmEquipmentDeductible _deductible) {
		this.deductible = _deductible;
	}

	public IMFarmEquipment.IMFarmEquipmentDeductible getDeductible() {
		return this.deductible;
	}

	public void setInspected(Boolean _inspected) {
		this.inspected = _inspected;
	}

	public Boolean getInspected() {
		return this.inspected;
	}

	public void setExistingDamage(Boolean _existingDamage) {
		this.existingDamage = _existingDamage;
	}

	public Boolean getExistingDamage() {
		return this.existingDamage;
	}

	public void setOwner(String _owner) {
		this.owner = _owner;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setAdditionalInsured(ArrayList<String> _additionalInsured) {
		this.additionalInsured = _additionalInsured;
	}

	public ArrayList<String> getAdditionalInsured() {
		return this.additionalInsured;
	}

	public void addAdditionalInsured(String addInsured) {
		this.additionalInsured.add(addInsured);
	}

	public void setScheduledFarmEquipment(ArrayList<IMFarmEquipmentScheduledItem> _farmStuff) {
		this.scheduledFarmEquipment = _farmStuff;
	}

	public ArrayList<IMFarmEquipmentScheduledItem> getScheduledFarmEquipment() {
		return this.scheduledFarmEquipment;
	}

	public void addScheduledFarmEquipment(IMFarmEquipmentScheduledItem _farmStuff) {
		this.scheduledFarmEquipment.add(_farmStuff);
	}

	public void setIMFarmEquipmentType(IMFarmEquipment.IMFarmEquipmentType _type) {
		this.type = _type;
	}

	public IMFarmEquipment.IMFarmEquipmentType getIMFarmEquipmentType() {
		return this.type;
	}

}
