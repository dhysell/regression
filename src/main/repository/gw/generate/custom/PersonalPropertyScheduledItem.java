package repository.gw.generate.custom;

import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;

import java.util.ArrayList;
import java.util.Date;

public class PersonalPropertyScheduledItem {
	
	private repository.gw.enums.PersonalPropertyType parentPersonalPropertyType;
	private repository.gw.enums.PersonalPropertyScheduledItemType type;
	private String description;
	private int number;
	private Date appraisalDate;
	private int year;
	private String make;
	private String model;
	private String vinSerialNum;
	private int limitPer;
	private int limit;	
	private ArrayList<String> additionalInsureds;
	private Date photoUploadDate;

	public repository.gw.enums.PersonalPropertyType getParentPersonalPropertyType() {
		return parentPersonalPropertyType;
	}

	public void setParentPersonalPropertyType(repository.gw.enums.PersonalPropertyType parentPersonalPropertyType) throws Exception {
		if(parentPersonalPropertyType == repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment || parentPersonalPropertyType == repository.gw.enums.PersonalPropertyType.RefrigeratedMilk || parentPersonalPropertyType == repository.gw.enums.PersonalPropertyType.MilkContaminationAndRefrigeration) {
			throw new Exception("ERROR: The selected type does not have scheduled items");
		}
		this.parentPersonalPropertyType = parentPersonalPropertyType;
	}
	
	private void checkIfParentTypeIsSet() throws Exception {
		if(getParentPersonalPropertyType() == null) {
			throw new Exception("ParentPersonalPropertyType must be set before you can set this field");
		}
	}

	public repository.gw.enums.PersonalPropertyScheduledItemType getType() {
		return type;
	}

	public void setType(PersonalPropertyScheduledItemType type) throws Exception {
		checkIfParentTypeIsSet();
		if(!(getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.PhotographicEquipment || getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.BlanketRadios || getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.SportingEquipment || getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.Jewelry || getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.SaddlesAndTack)) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) throws Exception {
		checkIfParentTypeIsSet();
		if(!(getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.BeeContainers || getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.BlanketRadios)) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.number = number;
	}

	public Date getAppraisalDate() {
		return appraisalDate;
	}

	public void setAppraisalDate(Date appraisalDate) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.Collectibles && getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.FineArts && getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.Furs && getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.Jewelry) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.appraisalDate = appraisalDate;
	}
	
	public Date getPhotoUploadDate(){
		return photoUploadDate;
	}
	
	public void setPhotoUploadDate(Date photoUploadDate) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.Jewelry) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.photoUploadDate = photoUploadDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.MedicalSuppliesAndEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SportingEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SaddlesAndTack &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.MedicalDevices &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.OfficeEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.RadioReceiversAndTransmitters &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TailoringEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TelephoneEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.VideoEquipment) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.PhotographicEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SportingEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SaddlesAndTack &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.MedicalDevices &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.OfficeEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.RadioReceiversAndTransmitters &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.StereoEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TailoringEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TelephoneEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.VideoEquipment) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) throws Exception{
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.PhotographicEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SportingEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SaddlesAndTack &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.OfficeEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.RadioReceiversAndTransmitters &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.StereoEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TailoringEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TelephoneEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.VideoEquipment) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.model = model;
	}

	public String getVinSerialNum() {
		return vinSerialNum;
	}

	public void setVinSerialNum(String vinSerialNum) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.PhotographicEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SportingEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.SaddlesAndTack &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.MedicalDevices &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.OfficeEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.RadioReceiversAndTransmitters &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.StereoEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TailoringEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.TelephoneEquipment &&
				getParentPersonalPropertyType() != repository.gw.enums.PersonalPropertyType.VideoEquipment) {
			throw new Exception("Value is only valid for Parent Type");
		}
		this.vinSerialNum = vinSerialNum;
	}

	public int getLimitPer() {
		return limitPer;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public ArrayList<String> getAdditionalInsureds() {
		return additionalInsureds;
	}

	public void setAdditionalInsureds(ArrayList<String> additionalInsureds) throws Exception {
		checkIfParentTypeIsSet();
		if(getParentPersonalPropertyType() == repository.gw.enums.PersonalPropertyType.RefrigeratedMilk || getParentPersonalPropertyType() == PersonalPropertyType.MilkContaminationAndRefrigeration) {
			throw new Exception("Value is not valid for selected Type");
		}
		this.additionalInsureds = additionalInsureds;
	}
	
	

}
