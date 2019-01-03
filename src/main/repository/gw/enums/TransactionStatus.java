package repository.gw.enums;

public enum TransactionStatus {
	New("New"),
	Draft("Draft"),
	Quoted("Quoted"),
	Quoting("Quoting"),
	Canceling("Canceling"),
	Renewing("Renewing");
	String value;
	
	private TransactionStatus(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
