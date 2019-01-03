package repository.gw.generate.custom;

import repository.gw.enums.Priority;
import repository.gw.enums.Status;

public class Activity {

	private String subject = "";
	private String description = "";
	private repository.gw.enums.Priority priority = repository.gw.enums.Priority.Normal;
	private repository.gw.enums.Status status = repository.gw.enums.Status.Open;
	private boolean mandatory = false;
	private boolean recurring = false;
	private String receivedDate;
	private String targetDate;
	private String escalationDate;
	private String assignedTo = null;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public repository.gw.enums.Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public repository.gw.enums.Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isRecurring() {
		return recurring;
	}
	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getEscalationDate() {
		return escalationDate;
	}
	public void setEscalationDate(String escalationDate) {
		this.escalationDate = escalationDate;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	
	
	
	
	
	
	
}
