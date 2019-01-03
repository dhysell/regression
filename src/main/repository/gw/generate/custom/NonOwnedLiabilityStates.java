package repository.gw.generate.custom;

import com.idfbins.enums.State;

public class NonOwnedLiabilityStates {
	
	private State state = State.Idaho;
	private int numberOfEmployees = 1;
	
	public NonOwnedLiabilityStates() {
		
	}
	
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public int getNumberOfEmployees() {
		return numberOfEmployees;
	}
	public void setNumberOfEmployees(int numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}
	
}
