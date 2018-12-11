package repository.gw.enums;

public enum PLLocationType {

	Address("Address"),
	OutOfState("Out of State");
	
	private String name;
	
	private PLLocationType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
