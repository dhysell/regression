package repository.gw.exception;

import java.util.List;

public class GuidewireNavigationException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public GuidewireNavigationException(String failureMessage, List<String> validationErrors) {
		super(failureMessage + " " + exceptionBuilder(validationErrors));
	}
	
	private static String exceptionBuilder(List<String> validationErrors) {
		String validationList = "";
		if(validationErrors != null && !validationErrors.isEmpty()) {
			for(String message : validationErrors) {
				validationList += "\nPage Validation: " + message;
			}
		} else {
			validationList = "NO PAGE VALIDATION PRESENT";
		}
		return validationList;
	}
}
