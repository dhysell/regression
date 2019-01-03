package repository.gw.enums;

public enum SecurityDictionaryExportType {
	
	HTML("HTML", "SecurityDictionary", "zip"), 
	XML("XML", "SecurityDictionary", "zip");
	String value;
	String fileNameValue;
	String fileNameExtension;
	
	private SecurityDictionaryExportType(String value, String fileNameValue, String fileNameExtension){
		this.value = value;
		this.fileNameValue = fileNameValue;
		this.fileNameExtension = fileNameExtension;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getFileNameValue(){
		return this.fileNameValue;
	}
	
	public String getFileNameExtension(){
		return this.fileNameExtension;
	}
}
