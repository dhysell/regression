package repository.gw.enums;

public enum BlockingAction {
	NONE("none"),
	Blockuser("Block user");
	String value;
		
	private BlockingAction(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static BlockingAction valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "").replaceAll("-", "_");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
	
}
