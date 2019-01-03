package repository.gw.enums;

public enum AdditionalInsuredRole {
	CertificateHolderOnly("Certificate Holder Only", true), 
	ControllingInterest("Controlling interest - BP 04 06", true), 
	DesignatedPersonOrOrganization("Designated person or organization - BP 04 48", true), 
	GrantorOfFranchise("Grantor of Franchise - BP 14 05", true), 
	StateOrPoliticalSubdivisions("State or Political Subdivisions - Permits Relating to Premises - BP 04 07", true), 
	Vendors("Vendors - BP 04 47", true), 
	CoOwnerOfInsuredPremises("Co-owner of Insured Premises - BP 04 11", false), 
	LessorOfLeasedEquipment("Lessor of Leased Equipment - BP 04 16", false), 
	ManagersOrLessorsOrPremises("Managers or Lessors of Premises - BP 04 02", false), 
	MortgageesAssigneesOrReceivers("Mortgagees, Assignees or Receivers - BP 04 09", false), 
	OwnersOrOtherInterests("Owners or Other Interests From Whom Land Has Been Leased - BP 04 10", false);
	
	String role;
	boolean existsOnBOLine;
	
	private AdditionalInsuredRole(String role, boolean existsOnBOLine){
		this.role = role;
		this.existsOnBOLine = existsOnBOLine;
	}
	
	public String getRole(){
		return role;
	}
	
	public boolean isExistsOnBOLine(){
		return existsOnBOLine;
	}
}
