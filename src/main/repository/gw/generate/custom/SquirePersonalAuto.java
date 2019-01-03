package repository.gw.generate.custom;

import java.util.ArrayList;

public class SquirePersonalAuto {

	private boolean primaryInsuredAsDriver = true;
	private ArrayList<Contact> driversList = new ArrayList<Contact>();
	private ArrayList<repository.gw.generate.custom.Vehicle> vehicleList = new ArrayList<repository.gw.generate.custom.Vehicle>();
	private SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages();
	
	public CLUEAutoInfo clueAutoReport;
	//public CLUEAutoInfo clueAutoReport;

	public SquirePersonalAuto() {
		addToVehiclesList(new repository.gw.generate.custom.Vehicle());
	}

	public SquirePersonalAuto(ArrayList<Contact> driversList, ArrayList<repository.gw.generate.custom.Vehicle> vehicleList, SquirePersonalAutoCoverages coverages) {
		this.driversList = driversList;
		this.vehicleList = vehicleList;
		this.coverages = coverages;
	}

	public boolean isPrimaryInsuredAsDriver() {
		return primaryInsuredAsDriver;
	}

	public void setPrimaryInsuredAsDriver(boolean primaryInsuredAsDriver) {
		this.primaryInsuredAsDriver = primaryInsuredAsDriver;
	}

	public ArrayList<Contact> getDriversList() {
		return driversList;
	}

	public void setDriversList(ArrayList<Contact> driversList) {
		this.driversList = driversList;
	}
	
	public void addToDriversList(Contact driver){
		this.driversList.add(driver);
	}

	public ArrayList<repository.gw.generate.custom.Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(ArrayList<repository.gw.generate.custom.Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}
	
	public void addToVehiclesList(repository.gw.generate.custom.Vehicle vehicle){
		this.vehicleList.add(vehicle);
	}
	
	public boolean areAllVehiclesUnassignedDrivers() {
		boolean allUnassigned = true;
		
		for(repository.gw.generate.custom.Vehicle veh : getVehicleList()) {
			if(veh.getDriverPL() != null) {
				allUnassigned = false;
				break;
			}
		}
		
		return allUnassigned;
	}
	
	public ArrayList<repository.gw.generate.custom.Vehicle> getVehicleListUnassignedDrivers() {
		ArrayList<repository.gw.generate.custom.Vehicle> allUnassignedVehicles = new ArrayList<repository.gw.generate.custom.Vehicle>();
		
		for(repository.gw.generate.custom.Vehicle veh : getVehicleList()) {
			if(veh.getDriverPL() == null) {
				allUnassignedVehicles.add(veh);
			}
		}
		
		return allUnassignedVehicles;
	}

	public SquirePersonalAutoCoverages getCoverages() {
		return coverages;
	}

	public void setCoverages(SquirePersonalAutoCoverages coverages) {
		this.coverages = coverages;
	}
	
	
	public void addVehicle(repository.gw.enums.Vehicle.VehicleTypePL vehicleType) {
		switch(vehicleType) {
		case FarmTruck:
			break;
		case LocalService1Ton:
			break;
		case MotorHome:
			break;
		case Motorcycle:
			break;
		case PassengerPickup:
			break;
		case PrivatePassenger:
			break;
		case SemiTrailer:
			break;
		case ShowCar:
			repository.gw.generate.custom.Vehicle myVehicle = new Vehicle();
			myVehicle.setVehicleTypePL(vehicleType);
			vehicleList.add(myVehicle);
			break;
		case Trailer:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
