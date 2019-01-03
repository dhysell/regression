package repository.gw.enums;

public enum RelatedContacts {
	
		ChildWard("Child / Ward"), 
		ParentGuarding("Parent / Guardian"),
		Spouse("Spouse");
		
		String value;
		
		private RelatedContacts(String type){
			value = type;
		}
		
		public String getValue(){
			return value;
		}
}
