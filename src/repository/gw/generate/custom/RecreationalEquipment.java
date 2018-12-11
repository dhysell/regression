package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineTypes;
import repository.gw.enums.Vehicle;
import repository.gw.helpers.NumberUtils;

public class RecreationalEquipment {
	
	private InlandMarineTypes.RecreationalEquipmentType recEquipmentType;
	private String limit;
	private Vehicle.PAComprehensive_CollisionDeductible deductible;
	private String owner;
	private String vin = "VIN" + NumberUtils.generateRandomNumberDigits(5);
	private int modelYear = 2015;
	private String make = "Scion";
	private String model = "FR-S";
	private boolean inspected = true;
	private String describeExistingDamage;
	private String description = "Testing Purpose";
	
	public RecreationalEquipment(InlandMarineTypes.RecreationalEquipmentType recEquipmentType, String limit, Vehicle.PAComprehensive_CollisionDeductible deductible, String owner) {
		setRecEquipmentType(recEquipmentType);
		setLimit(limit);
		setDeductible(deductible);
		setOwner(owner);
	}

	public InlandMarineTypes.RecreationalEquipmentType getRecEquipmentType() {
		return recEquipmentType;
	}

	public void setRecEquipmentType(InlandMarineTypes.RecreationalEquipmentType recEquipmentType) {
		this.recEquipmentType = recEquipmentType;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public Vehicle.PAComprehensive_CollisionDeductible getDeductible() {
		return deductible;
	}

	public void setDeductible(Vehicle.PAComprehensive_CollisionDeductible deductible) {
		this.deductible = deductible;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
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

	public boolean isInspected() {
		return inspected;
	}

	public void setInspected(boolean inspected) {
		this.inspected = inspected;
	}

	public String getDescribeExistingDamage() {
		return describeExistingDamage;
	}

	public void setDescribeExistingDamage(String describeExistingDamage) {
		this.describeExistingDamage = describeExistingDamage;
	}
	
	public void setDescription(String desc){
		this.description = desc;
	}
	
	public String getDescription(){
		return this.description;
	}

}
