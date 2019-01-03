package repository.gw.generate.custom;

public class CPPInlandMarineComputerSystems_LocationBlanket {

	private repository.gw.generate.custom.CPPCommercialPropertyProperty property;
	private int computerEquipmentLimit = 1000;
	private int mediaAndDataLimit = 1000;
	
	public CPPInlandMarineComputerSystems_LocationBlanket(repository.gw.generate.custom.CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public repository.gw.generate.custom.CPPCommercialPropertyProperty getLocationProperty() {
		return property;
	}

	public void setLocation(CPPCommercialPropertyProperty property) {
		this.property = property;
	}

	public int getComputerEquipmentLimit() {
		return computerEquipmentLimit;
	}

	public void setComputerEquipmentLimit(int computerEquipmentLimit) {
		this.computerEquipmentLimit = computerEquipmentLimit;
	}

	public int getMediaAndDataLimit() {
		return mediaAndDataLimit;
	}

	public void setMediaAndDataLimit(int mediaAndDataLimit) {
		this.mediaAndDataLimit = mediaAndDataLimit;
	}
	
}
