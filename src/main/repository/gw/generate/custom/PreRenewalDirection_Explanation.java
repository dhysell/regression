package repository.gw.generate.custom;

import repository.gw.enums.PreRenewalDirectionExplanation;

public class PreRenewalDirection_Explanation {

	private boolean closed = false;
	private PreRenewalDirectionExplanation codeAndDescription = null;
	private String closedDate = "";
	public PreRenewalDirection_Explanation() {
		
	}
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public PreRenewalDirectionExplanation getCodeAndDescription() {
		return codeAndDescription;
	}
	public void setCodeAndDescription(PreRenewalDirectionExplanation codeAndDescription) {
		this.codeAndDescription = codeAndDescription;
	}
	
	
	
	
	
	
	
}
