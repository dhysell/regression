package repository.gw.enums;

public enum OpenClosed {
	Open("Open"), 
	Closed("Closed");
	String value;
	
	private OpenClosed(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
