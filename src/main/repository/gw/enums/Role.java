package repository.gw.enums;

public enum Role {

	Agent("Agent"),
	UnderWriter("UnderWriter");

	String value;

	private Role(String role){
		value = role;
	}

	public String getValue(){
		return value;
	}
}
