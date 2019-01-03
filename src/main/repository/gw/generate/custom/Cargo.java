package repository.gw.generate.custom;

import repository.gw.enums.Vehicle;
import repository.gw.helpers.NumberUtils;

public class Cargo {
	
	private Vehicle.VehicleType vehicleType = Vehicle.VehicleType.Trailer;
	private String limit;
	private String vin = "VIN" + NumberUtils.generateRandomNumberDigits(5);
	private int modelYear = 2015;
	private String make = "Scion";
	private String model = "FR-S";

	public Cargo(Vehicle.VehicleType trailer, String vin2, int year, String make2, String model2) {
		setVehicleType(trailer);
		setVin(vin2);
		setModelYear(year);
		setMake(make2);
		setModel(model2);
	}
	
	public Cargo(Vehicle.VehicleType trailer, String limit){
		setLimit(limit);
		setVehicleType(trailer);
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
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

	public Vehicle.VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Vehicle.VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	

}
