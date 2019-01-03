package repository.ab.enums;

public enum RelatedTo {
	
	
	AffiliateTo("Affiliate To"),
	Affiliation("Affiliation"),
	AgentOf("Agent of"),	
	CollectionAgency("Collection Agency"),
	CommonOwnership("Common Ownership"),
	County("County"),
	CSRFor("CSR for"),
	Employee("Employee"),
	Employer("Employer"),
	ManagerOf("Manager of"),
	Partner("Partner to"),
	PrimaryContact("Primary Contact"),
	PrimaryContactFor("Primary Contact for"),
	Represents("Represents"),
	RepresentsFor("Represents for"),
	Subsidiary("Subsidiary"),
	SubsidiaryOf("Subsidiary of"),
	ThirdPartyInsurer("Third-Party Insurer"),
	Trustee("Trustee"),
	DBA("DBA");
	
	String value;
	
	private RelatedTo(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
