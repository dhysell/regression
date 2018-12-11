package repository.gw.enums;

public enum ActivityQueuesBillingCenter {
	ARGeneralFarmBureau("AR General Farm Bureau"),
	ARGeneralWesternCommunity("AR General Western Community"),
	ARSupervisorFarmBureau("AR Supervisor Farm Bureau"),
	ARSupervisorWesternCommunity("AR Supervisor Western Community"),
	UnattachedITDocuments("Unattached IT Documents - Operations"),
	UnattachedUserDocuments("Unattached User Documents - Unattached Document Approval");
	String value;
	
	private ActivityQueuesBillingCenter(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public static ActivityQueuesBillingCenter valueOfName(final String name) {
		final String enumName = name.replaceAll(" ", "");
		try {
			return valueOf(enumName);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}