package repository.gw.generate.custom;

public class AbUserActivity {
	
	private String subject;
	private String name;
	private String description;
	private String activityDueDate;
	private String priority;
	private String status;
	private String activityCompletedDate;
	private String assignedTo;
	
	public AbUserActivity(String _subject, String _name, String _description, String _activityDueDate, String _priority, String _status, String _activityCompletedDate, String _assignedTo) {
		setSubject(_subject);
		setName(_name);
		setDescription(_description);
		setActivityDueDate(_activityDueDate);
		setPriority(_priority);
		setStatus(_status);
		setActivityCompletedDate(_activityCompletedDate);
		setAssignedTo(_assignedTo);
	}
	
	public AbUserActivity(String _subject, String _name, String _activityDueDate, String _assignedTo) {
		setSubject(_subject);
		setName(_name);
		setActivityDueDate(_activityDueDate);
		setAssignedTo(_assignedTo);
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public void setSubject(String _subject) {
		this.subject = _subject;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String _name) {
		this.name = _name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String _description) {
		this.description = _description;
	}
	
	public String getActivityDueDate() {
		return this.activityDueDate;
	}
	
	public void setActivityDueDate(String _activityDueDate) {
		this.activityDueDate = _activityDueDate;
	}
	
	public String getPriority() {
		return this.priority;
	}
	
	public void setPriority(String _priority) {
		this.priority = _priority;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String _status) {
		this.status = _status;
	}
	
	public String getActivityCompletedDate() {
		return this.activityCompletedDate;
	}
	
	public void setActivityCompletedDate(String _activityCompletedDate) {
		this.activityCompletedDate = _activityCompletedDate;
	}
	
	public String getAssignedTo() {
		return this.assignedTo;
	}
	
	public void setAssignedTo(String _assignedTo) {
		this.assignedTo = _assignedTo;
	}
}
