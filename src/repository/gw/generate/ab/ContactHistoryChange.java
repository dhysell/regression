package repository.gw.generate.ab;

public class ContactHistoryChange {
	
	private String changedField;
	private String oldValue;
	private String newValue;
	
//	public ContactHistoryChange(){}
	public ContactHistoryChange(String _changedField, String _oldValue, String _newValue){
		setChangedField(_changedField);
		setOldValue(_oldValue);
		setNewValue(_newValue);
	}
	
	//Getters and Setters
	public String getChangedField(){
		return this.changedField;
	}
	
	public void setChangedField(String _changedField){
/*		if(_changedField.isEmpty()){
			
		}
		else{
*/			this.changedField = _changedField;
//		}
	}
	
	public String getOldValue(){
		return this.oldValue;
	}
	
	public void setOldValue(String _oldValue){
/*		if(_changedField.isEmpty()){
			
		}
		else{
*/			this.oldValue = _oldValue;
//		}
	}

	
	public String getNewValue(){
		return this.newValue;
	}
	
	public void setNewValue(String _newValue){
/*		if(_changedField.isEmpty()){
			
		}
		else{
*/			this.newValue = _newValue;
//		}
	}


}
