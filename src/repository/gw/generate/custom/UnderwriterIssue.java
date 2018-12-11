package repository.gw.generate.custom;

import repository.gw.enums.UnderwriterIssueType;

import java.util.ArrayList;
import java.util.List;

public class UnderwriterIssue {

	repository.gw.enums.UnderwriterIssueType type = repository.gw.enums.UnderwriterIssueType.Informational;
	private String shortDescription = "";
	private String longDescription = "";
	private boolean approve = false;
	private boolean reject = false;
	private boolean reopen = false;
	private boolean specialApprove = false;
	private boolean approve_Available = false;
	private boolean reject_Available = false;
	private boolean reopen_Available = false;
	private boolean specialApprove_Available = false;
	
	
	private List<UnderwritingIssueHistory> history = new ArrayList<UnderwritingIssueHistory>();
	
	public UnderwriterIssue() {
		
	}
	
	public UnderwriterIssue(repository.gw.enums.UnderwriterIssueType type, String shortDescription, String longDescription) {
		this.type = type;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	public repository.gw.enums.UnderwriterIssueType getType() {
		return type;
	}

	public void setType(UnderwriterIssueType type) {
		this.type = type;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}

	public boolean isReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}

	public boolean isReopen() {
		return reopen;
	}

	public void setReopen(boolean reopen) {
		this.reopen = reopen;
	}

	public boolean isSpecialApprove() {
		return specialApprove;
	}

	public void setSpecialApprove(boolean specialApprove) {
		this.specialApprove = specialApprove;
	}

	public boolean isApprove_Available() {
		return approve_Available;
	}

	public void setApprove_Available(boolean approve_Available) {
		this.approve_Available = approve_Available;
	}

	public boolean isReject_Available() {
		return reject_Available;
	}

	public void setReject_Available(boolean reject_Available) {
		this.reject_Available = reject_Available;
	}

	public boolean isReopen_Available() {
		return reopen_Available;
	}

	public void setReopen_Available(boolean reopen_Available) {
		this.reopen_Available = reopen_Available;
	}

	public boolean isSpecialApprove_Available() {
		return specialApprove_Available;
	}

	public void setSpecialApprove_Available(boolean specialApprove_Available) {
		this.specialApprove_Available = specialApprove_Available;
	}

	public List<UnderwritingIssueHistory> getHistory() {
		return history;
	}

	public void setHistory(List<UnderwritingIssueHistory> history) {
		this.history = history;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}










