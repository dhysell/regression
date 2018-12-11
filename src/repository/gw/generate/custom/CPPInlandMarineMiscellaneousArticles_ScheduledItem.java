package repository.gw.generate.custom;

public class CPPInlandMarineMiscellaneousArticles_ScheduledItem {
	
	private String modelYear = "2017";
	private String make = "Make";
	private String model = "Model";
	private String equipmentID = "ID1234567";
	private String description = "This is a miscellaneous articles scheduled item description";
	private int limit = 1000;
	
	public CPPInlandMarineMiscellaneousArticles_ScheduledItem() {
		//default constructor
	}

	public String getModelYear() {
		return modelYear;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEquipmentID() {
		return equipmentID;
	}

	public void setEquipmentID(String equipmentID) {
		this.equipmentID = equipmentID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
