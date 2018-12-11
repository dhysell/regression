/**
 * @author bmartin Oct 21, 2015
 * @notes 
 * 
 */
package repository.gw.enums;

public class Supplemental {
	
	public enum ApplicantHaveOffPremisesExposuresCharacteristics {
		Hospital ("Hospital"),
		NursingHome ("Nursing home (or similar exposure)"),
		Hotel ("Hotel"),
		DepartmentStore ("Department Store"),
		PrivateResidence ("Private residence"),
		Other ("Other");
		
		String value;
		
		private ApplicantHaveOffPremisesExposuresCharacteristics(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
}
