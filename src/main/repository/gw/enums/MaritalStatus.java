package repository.gw.enums;
	
	public enum MaritalStatus {
		//8/15/2018 - US15442 requires that the Marital Status Drop down be the same for AB as it is in PC.  I updated the enum to be so.
		None("<none>"),
		  CommonLawSpouse("Common law spouse"),
		  Divorced("Divorced"),
	//	DomesticPartner("Domestic partner"),
		  Married("Married"),
		  Separated("Separated"),
		  Single("Single"),
		//SpouseDeceased("Spouse Deceased"),
		  Unknown("Unknown"),
		  Widowed("Widowed");
		String value;
		
		MaritalStatus(String status) {
			value = status;
		}
		
		public String getValue(){
			return value;
		}
	
		public static MaritalStatus getEnumFromStringValue(String text){
	        for(MaritalStatus status: MaritalStatus.values()){
	            if(text.equalsIgnoreCase(status.value)){
	                return status;
	            }
	        }
	        return null;
	    }
	}

