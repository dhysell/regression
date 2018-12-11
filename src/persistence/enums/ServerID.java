package persistence.enums;


public enum ServerID {
	QA3("QA3"),
	DEV3("DEV3"), 
	IT("IT"), 
	DEV("DEV"),
	UAT("UAT"),
	QA2("QA2"),
	IT2("IT2"), 
	UAT2("UAT2"), 
	DEV2("DEV2");
	
	String serverID;
	
	private ServerID(String serverID){
		this.serverID = serverID;
	}
	
	public String getValue(){
		return serverID;
	}
	
	public static ServerID valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}
