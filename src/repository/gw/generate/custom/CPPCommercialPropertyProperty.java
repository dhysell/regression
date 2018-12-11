package repository.gw.generate.custom;

import repository.gw.enums.CPUWIssues;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialPropertyProperty {
	
	private boolean hasChanged = false;
	private String propertyLocationNumber = "";

	//LOCATION
	private AddressInfo address = new AddressInfo(true);
	
	//BUILDINGS LIST
	List<repository.gw.generate.custom.CPPCommercialProperty_Building> CPPCommercialProperty_Building_List = new ArrayList<repository.gw.generate.custom.CPPCommercialProperty_Building>();
	
	ArrayList<repository.gw.enums.CPUWIssues> underwritingIssuesList = new ArrayList<repository.gw.enums.CPUWIssues>();
	
	public CPPCommercialPropertyProperty() {
		
	}
	
	public CPPCommercialPropertyProperty(AddressInfo address) {
		this.address = address;
	}
	
	public CPPCommercialPropertyProperty(repository.gw.generate.custom.CPPCommercialProperty_Building building) {
		CPPCommercialProperty_Building_List.add(building);
	}
	
	

	public AddressInfo getAddress() {
		return address;
	}

	public void setAddress(AddressInfo address) {
		this.address = address;
	}

	public List<repository.gw.generate.custom.CPPCommercialProperty_Building> getCPPCommercialProperty_Building_List() {
		return CPPCommercialProperty_Building_List;
	}

	public void setCPPCommercialProperty_Building_List(List<CPPCommercialProperty_Building> cPPCommercialProperty_Building_List) {
		CPPCommercialProperty_Building_List = cPPCommercialProperty_Building_List;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public String getPropertyLocationNumber() {
		return propertyLocationNumber;
	}

	public void setPropertyLocationNumber(String propertyLocationNumber) {
		this.propertyLocationNumber = propertyLocationNumber;
	}

	public ArrayList<repository.gw.enums.CPUWIssues> getUnderwritingIssuesList() {
		return underwritingIssuesList;
	}

	public void setUnderwritingIssuesList(ArrayList<CPUWIssues> underwritingIssuesList) {
		this.underwritingIssuesList = underwritingIssuesList;
	}
	
	
	
}
