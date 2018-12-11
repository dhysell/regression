package repository.gw.generate.custom;

import java.time.LocalDate;

public class CPPInlandMarineComputerSystems_PortableComputer {
	
	private String modelYear = ""+LocalDate.now().getYear();
	private String make = "Computer Make";
	private String model = "Computer Model";
	private String equipmentID = "equipmentID";
	private int limit = 1000;
	
	public CPPInlandMarineComputerSystems_PortableComputer() {
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

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
