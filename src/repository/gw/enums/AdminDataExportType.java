package repository.gw.enums;

public enum AdminDataExportType {
	None("<none>", "", ""),
	ActivityPatterns("Activity Patterns", "ActivityPatterns", "xml"),
	Admin("Admin", "admin", "xml"),
	AuthorityProfiles("Authority Profiles", "AuthorityProfiles", "xml"),
	Counties("Counties", "Counties", "xml"),
	FarmBureauDocumentPackets("Farm Bureau Document Packets", "DocumentPackets", "xml"),
	FarmBureauReinsurranceData("Farm Bureau Reinsurrance Data", "ReinsuranceData", "xml"),
	Groups("Groups", "Groups", "xml"),
	PolicyFormPatterns("Policy Form Patterns", "PolicyformPatterns", "xml"),
	PolicyHolds("Policy Holds", "policy holds", "xml"),
	Roles("Roles", "roles", "xml");
	String value;
	String fileNameValue;
	String fileNameExtension;
	
	private AdminDataExportType(String value, String fileNameValue, String fileNameExtension){
		this.value = value;
		this.fileNameValue = fileNameValue;
		this.fileNameExtension = fileNameExtension;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getFileNameValue(){
		return this.fileNameValue;
	}
	
	public String getFileNameExtension(){
		return this.fileNameExtension;
	}
	
	public static AdminDataExportType getEnumValueFromString(final String name) {
		final String enumName = name.replaceAll(" ", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}
