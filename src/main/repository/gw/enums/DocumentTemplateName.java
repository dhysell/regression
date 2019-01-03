package repository.gw.enums;

public enum DocumentTemplateName {
		Blank("Blank"),		
		OnlinePymtConfirmation("Online Pymt Confirmation"),
		PolicyLienHolderReturningCheck("Policy/Lien-Holder Returning Check"),
		MoneyOnCancelledPolicy("Money On Cancelled Policy");

		String value;

		private DocumentTemplateName(String role){
			value = role;
		}

		public String getValue(){
			return value;
		}
	}

