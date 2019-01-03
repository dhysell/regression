package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineMotorTruckCargo {

	private InlandMarineCPP.MotorTruckCargo_Deductible deductible = InlandMarineCPP.MotorTruckCargo_Deductible.FiveHundred;
	
	private List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> scheduledCargoList = new ArrayList<>();
	
	public CPPInlandMarineMotorTruckCargo(List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> scheduledCargoList) {
		this.scheduledCargoList = scheduledCargoList;
	}

	public InlandMarineCPP.MotorTruckCargo_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.MotorTruckCargo_Deductible deductible) {
		this.deductible = deductible;
	}

	public List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> getScheduledCargoList() {
		return scheduledCargoList;
	}

	public void setScheduledCargoList(List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> scheduledCargoList) {
		this.scheduledCargoList = scheduledCargoList;
	}

}
