package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineMotorTruckCargo_ScheduledCargo {

	private repository.gw.generate.custom.Vehicle vehicle;
	private InlandMarineCPP.MotorTruckCargo_RadiusOfOperation radiusOfOperation = InlandMarineCPP.MotorTruckCargo_RadiusOfOperation.Intermediate;
	private List<InlandMarineCPP.InlandMarine_Cargo> cargoList = new ArrayList<InlandMarineCPP.InlandMarine_Cargo>();
	private String limit = "1000";
	private InlandMarineCPP.MotorTruckCargo_Coverage coverage = InlandMarineCPP.MotorTruckCargo_Coverage.Both_IDCM_31_4072_and_IDCM_31_4076;
	
	public CPPInlandMarineMotorTruckCargo_ScheduledCargo(repository.gw.generate.custom.Vehicle vehicle) {
		this.vehicle = vehicle;
		cargoList.add(InlandMarineCPP.InlandMarine_Cargo.AgriculturalMachineryImplementsParts);
	}

	public repository.gw.generate.custom.Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public InlandMarineCPP.MotorTruckCargo_RadiusOfOperation getRadiusOfOperation() {
		return radiusOfOperation;
	}

	public void setRadiusOfOperation(InlandMarineCPP.MotorTruckCargo_RadiusOfOperation radiusOfOperation) {
		this.radiusOfOperation = radiusOfOperation;
	}

	public List<InlandMarineCPP.InlandMarine_Cargo> getCargoList() {
		return cargoList;
	}

	public void setCargoList(List<InlandMarineCPP.InlandMarine_Cargo> cargoList) {
		this.cargoList = cargoList;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public InlandMarineCPP.MotorTruckCargo_Coverage getCoverage() {
		return coverage;
	}

	public void setCoverage(InlandMarineCPP.MotorTruckCargo_Coverage coverage) {
		this.coverage = coverage;
	}
	
}
