package repository.gw.enums;

public enum CreateNew {
	Create_New_Always("Create New Always"),
	Do_Not_Create_New("Don't Create New"),
	Create_New_Only_If_Does_Not_Exist("Create New Only If Contact Does Not Already Exist");
	String value;
	
	private CreateNew(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
