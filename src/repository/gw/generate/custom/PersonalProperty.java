package repository.gw.generate.custom;

import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;

import java.util.ArrayList;

public class PersonalProperty {

	private repository.gw.enums.PersonalPropertyType type;
	private int limit;
    private repository.gw.enums.PersonalPropertyDeductible deductible = repository.gw.enums.PersonalPropertyDeductible.Ded500;
	private ArrayList<String> additionalInsureds;
	private int year;
	private String make;
	private String model;
	private String vinSerialNum;
	private String description;
	private ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> scheduledItems = new ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem>();
	
	public repository.gw.enums.PersonalPropertyType getType() {
		return type;
	}
	
	public void setType(repository.gw.enums.PersonalPropertyType type) {
		this.type = type;
	}
	
	private void checkIfTypeIsSet() throws Exception {
		if(getType() == null) {
			throw new Exception("Type must be set before you can set this field");
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) throws Exception {
		checkIfTypeIsSet();
		if(getType().equals(repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration) || getType().equals(repository.gw.enums.PersonalPropertyType.RefrigeratedMilk) || getType().equals(repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) || (getType().equals(repository.gw.enums.PersonalPropertyType.BeeContainers) && limit > 24)){
			this.limit = limit;
		}else{
			this.limit = 26;
		}
	}
	
	public repository.gw.enums.PersonalPropertyDeductible getDeductible() {
		return deductible;
	}

	public void setDeductible(PersonalPropertyDeductible deductible) throws Exception {
		checkIfTypeIsSet();
		this.deductible = deductible;
	}

	public ArrayList<String> getAdditionalInsureds() {
		return additionalInsureds;
	}

	public void setAdditionalInsureds(ArrayList<String> additionalInsureds) throws Exception {
		checkIfTypeIsSet();
		if(getType() == repository.gw.enums.PersonalPropertyType.RefrigeratedMilk && getType() == repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.additionalInsureds = additionalInsureds;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.model = model;
	}

	public String getVinSerialNum() {
		return vinSerialNum;
	}

	public void setVinSerialNum(String vinSerialNum) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.vinSerialNum = vinSerialNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment && getType() != repository.gw.enums.PersonalPropertyType.RefrigeratedMilk && getType() != repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration && getType() != repository.gw.enums.PersonalPropertyType.GolfEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.description = description;
	}

	public ArrayList<repository.gw.generate.custom.PersonalPropertyScheduledItem> getScheduledItems() {
		return scheduledItems;
	}

	public void setScheduledItems(ArrayList<PersonalPropertyScheduledItem> scheduledItems) throws Exception {
		checkIfTypeIsSet();
		if(getType() == repository.gw.enums.PersonalPropertyType.RefrigeratedMilk && getType() == repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration && getType() == PersonalPropertyType.MedicalSuppliesAndEquipment) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.scheduledItems = scheduledItems;
	}
	
	
	
}
