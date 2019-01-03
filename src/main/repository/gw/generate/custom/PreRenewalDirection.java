package repository.gw.generate.custom;

import repository.gw.enums.PreRenewalDirectionType;

import java.util.ArrayList;
import java.util.List;

public class PreRenewalDirection {

	private PreRenewalDirectionType preRenewalDirection = PreRenewalDirectionType.ReferToUnderwriter;
	private String assignedUser = "Barney The Purple DinoSaur";
	private String openedDate = "";
	private String openedBy = "System";
	private String closedDate = "";
	private String completedBy = "Bob The Builder";
	private List<PreRenewalDirection_Explanation> explanationList = new ArrayList<PreRenewalDirection_Explanation>();
	
	public PreRenewalDirection() {
		
	}
	
	public PreRenewalDirectionType getPreRenewalDirection() {
		return preRenewalDirection;
	}
	public void setPreRenewalDirection(PreRenewalDirectionType preRenewalDirection) {
		this.preRenewalDirection = preRenewalDirection;
	}
	public String getAssignedUser() {
		return assignedUser;
	}
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}
	public String getOpenedDate() {
		return openedDate;
	}
	public void setOpenedDate(String openedDate) {
		this.openedDate = openedDate;
	}
	public String getOpenedBy() {
		return openedBy;
	}
	public void setOpenedBy(String openedBy) {
		this.openedBy = openedBy;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public String getCompletedBy() {
		return completedBy;
	}
	public void setCompletedBy(String completedBy) {
		this.completedBy = completedBy;
	}
	public List<PreRenewalDirection_Explanation> getExplanationList() {
		return explanationList;
	}
	public void setExplanationList(List<PreRenewalDirection_Explanation> explanationList) {
		this.explanationList = explanationList;
	}
	
	
	
	
	
	
	
}
