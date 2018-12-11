package repository.gw.generate.custom;

import repository.gw.enums.CommercialAutoForm;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialAuto {
	
	private boolean hasYouthfullDrivers = false;
//	private boolean hasChanged = false;

	// COMMERCIAL AUTO LINE
	private CPPCommercialAutoLine commercialAutoLine = new CPPCommercialAutoLine();
	// GARAGEKEEPERS COVERAGE
	private List<CPPCommercialAutoGarageKeepers> garageKeepers = new ArrayList<CPPCommercialAutoGarageKeepers>();
	// VEHICLES
	private boolean duplicateVINCheck = true;
	private ArrayList<repository.gw.generate.custom.Vehicle> vehicleList = new ArrayList<repository.gw.generate.custom.Vehicle>();
	// STATE INFO
	private CPPCommercialAutoStateInfo CPP_CAStateInfo = new CPPCommercialAutoStateInfo();
	// DRIVERS
	private List<Contact> driversList = new ArrayList<Contact>();
	//COVERED VEHICLES
	private boolean setCoveredVehicle = false;
	// MODIFIERS

	// LINE REVIEW
	
	
	private List<repository.gw.enums.CommercialAutoForm> commercialAutoForms = new ArrayList<repository.gw.enums.CommercialAutoForm>();

	//////////////////////////////////
	////      CONSTRUCTORS    ////////
	//////////////////////////////////

	public CPPCommercialAuto() {

	}

	public CPPCommercialAuto(CPPCommercialAutoLine commercialAutoLine, ArrayList<repository.gw.generate.custom.Vehicle> vehicleList, List<Contact> driversList) {
		this.commercialAutoLine = commercialAutoLine;
		this.vehicleList = vehicleList;
		this.driversList = driversList;
	}

	//////////////////////////////////
	//// GETTERS AND SETTERS ////////
	//////////////////////////////////

	public CPPCommercialAutoLine getCommercialAutoLine() {
		return commercialAutoLine;
	}

	public void setCommercialAutoLine(CPPCommercialAutoLine commercialAutoLine) {
		this.commercialAutoLine = commercialAutoLine;
	}

	public ArrayList<repository.gw.generate.custom.Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(ArrayList<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	public List<Contact> getDriversList() {
		return driversList;
	}

	public void setDriversList(List<Contact> driversList) {
		this.driversList = driversList;
	}

	public CPPCommercialAutoStateInfo getCPP_CAStateInfo() {
		return CPP_CAStateInfo;
	}

	public void setCPP_CAStateInfo(CPPCommercialAutoStateInfo cPP_CAStateInfo) {
		CPP_CAStateInfo = cPP_CAStateInfo;
	}

	public List<CPPCommercialAutoGarageKeepers> getGarageKeepers() {
		return garageKeepers;
	}

	public void setGarageKeepers(List<CPPCommercialAutoGarageKeepers> garageKeepers) {
		this.garageKeepers = garageKeepers;
	}

	public boolean isHasYouthfullDrivers() {
		return hasYouthfullDrivers;
	}

	public void setHasYouthfullDrivers(boolean hasYouthfullDrivers) {
		this.hasYouthfullDrivers = hasYouthfullDrivers;
	}

	public List<repository.gw.enums.CommercialAutoForm> getCommercialAutoForms() {
		return commercialAutoForms;
	}

	public void setCommercialAutoForms(List<CommercialAutoForm> commercialAutoForms) {
		this.commercialAutoForms = commercialAutoForms;
	}

	public boolean isDuplicateVINCheck() {
		return duplicateVINCheck;
	}

	public void setDuplicateVINCheck(boolean duplicateVINCheck) {
		this.duplicateVINCheck = duplicateVINCheck;
	}

	public boolean isSetCoveredVehicle() {
		return setCoveredVehicle;
	}

	public void setSetCoveredVehicle(boolean setCoveredVehicle) {
		this.setCoveredVehicle = setCoveredVehicle;
	}
	
//	public void resetChangeCondition() {
//		setHasChanged(false);
//		commercialAutoLine.resetChangeCondition();
//		for(CPPCommercialAutoGarageKeepers garage : garageKeepers) {
//			garage.setHasChanged(false);
//		}
//		for(Vehicle vehicle : vehicleList) {
//			vehicle.setHasChanged(false);
//		}
//		CPP_CAStateInfo.setHasChanged(false);
//		for(Person person : driversList) {
//			person.setHasChanged(false);
//		}
//	}

//	public boolean hasChanged() {
//		return hasChanged;
//	}
//
//	public void setHasChanged(boolean hasChanged) {
//		this.hasChanged = hasChanged;
//	}
	
	
	
	
	
	
	
	
	

}



















