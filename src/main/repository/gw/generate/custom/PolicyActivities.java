package repository.gw.generate.custom;

import repository.gw.generate.GeneratePolicy;

import java.util.ArrayList;
import java.util.List;

public class PolicyActivities {
	
	private List<Activity> activityList = new ArrayList<Activity>();
	
	public PolicyActivities(List<Activity> activityList) {
		this.activityList = activityList;
	}
	
	public List<Activity> getPolicyActivityList() {
		return activityList;
	}
	
	public void hasActivity(String activitySubjectOrDescription) {
		
	}

	public void getAssignedUser(Activity activity) {
		
	}
	
	public void getEscalationDate(Activity activity) {
		
	}
	
	public void getTargetDate(Activity activity) {
		
	}
	
	public void getPolicyActivities(GeneratePolicy policy) {
		
	}
	
	public void getEscalatedToUser(Activity activity) {
		
	}
	
	
}
