package repository.gw.enums;

public enum VacationStatus {

		OnVacation("On vacation"),
		AtWork("At work"),
		OnVacationInactive("On vacation (Inactive)");
		
		String value;
		
		private VacationStatus(String _vacationStatus){
			value = _vacationStatus;
		}
		
		public String getValue(){
			return value;
		}
}
