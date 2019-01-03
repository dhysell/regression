package repository.gw.generate.custom;

import java.util.ArrayList;

public class ActualExpectedDocumentDifferences {
	
	private ArrayList<String> inUserInterfaceNotInExpected;
	private ArrayList<String> inExpectedNotInUserInterface;
	
	public ArrayList<String> getInUserInterfaceNotInExpected() {
		return inUserInterfaceNotInExpected;
	}
	
	public void setInUserInterfaceNotInExpected(ArrayList<String> inUserInterfaceNotInExpected) {
		this.inUserInterfaceNotInExpected = inUserInterfaceNotInExpected;
	}
	
	public ArrayList<String> getInExpectedNotInUserInterface() {
		return inExpectedNotInUserInterface;
	}
	
	public void setInExpectedNotInUserInterface(ArrayList<String> inExpectedNotInUserInterface) {
		this.inExpectedNotInUserInterface = inExpectedNotInUserInterface;
	}
	
}