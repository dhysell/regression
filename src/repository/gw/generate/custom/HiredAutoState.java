package repository.gw.generate.custom;

import com.idfbins.enums.State;

public class HiredAutoState {

	private State state = State.Idaho;
	private int primaryCostOfHire = 1;
	private int excessCostOfHire = 1;
	private int maxValueOfVehicle = 1;
	private int numberOfMonthsLeased = 1;
	
	public HiredAutoState() {
		
	}

	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getPrimaryCostOfHire() {
		return primaryCostOfHire;
	}
	public void setPrimaryCostOfHire(int primaryCostOfHire) {
		this.primaryCostOfHire = primaryCostOfHire;
	}
	public int getExcessCostOfHire() {
		return excessCostOfHire;
	}
	public void setExcessCostOfHire(int excessCostOfHire) {
		this.excessCostOfHire = excessCostOfHire;
	}
	public int getMaxValueOfVehicle() {
		return maxValueOfVehicle;
	}
	public void setMaxValueOfVehicle(int maxValueOfVehicle) {
		this.maxValueOfVehicle = maxValueOfVehicle;
	}
	public int getNumberOfMonthsLeased() {
		return numberOfMonthsLeased;
	}
	public void setNumberOfMonthsLeased(int numberOfMonthsLeased) {
		this.numberOfMonthsLeased = numberOfMonthsLeased;
	}
}
