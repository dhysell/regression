package repository.gw.generate.custom;

public class RelatedContacts {
	
	private String firstName = null;
	private String middleInitial;
	private String lastName = null;
	private repository.gw.enums.RelatedContacts relation = repository.gw.enums.RelatedContacts.ChildWard;
	private AddressInfo address = null;
	private boolean lastNameAddressMatch = true;
	
	//Constructors
	public RelatedContacts(String _firstName, String _lastName, repository.gw.enums.RelatedContacts _relation, AddressInfo _address, boolean _lastNameAddressMatch){
		setFirstName(_firstName);
		setLastName(_lastName);
		setRelation(_relation);
		setAddressInfo(_address);
		setLastNameAddressMatch(_lastNameAddressMatch);
		
	}
	
	//Constructors
	public RelatedContacts(String _firstName, String _middleInitial, String _lastName, repository.gw.enums.RelatedContacts _relation, AddressInfo _address, boolean _lastNameAddressMatch){
			setFirstName(_firstName);
			setMiddleInitial(_middleInitial);
			setLastName(_lastName);
			setRelation(_relation);
			setAddressInfo(_address);
			setLastNameAddressMatch(_lastNameAddressMatch);
			
		}
	
	//Getters and Setters
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String _firstName){
		if(_firstName == null || _firstName == ""){
			this.firstName = "Jon";
		}
		else{			
			this.firstName = _firstName;
		}
	}
	
	public void setMiddleInitial(String _middleInitial){
			this.middleInitial = _middleInitial;
	}
	
	public String getMiddleInitial(){
		return this.middleInitial;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setLastName(String _lastName){
		if(_lastName == null || _lastName == ""){
			this.lastName = "Jon";
		}
		else{			
			this.lastName = _lastName;
		}
	}
	
	public repository.gw.enums.RelatedContacts getRelation(){
		return this.relation;
	}
	
	public void setRelation(repository.gw.enums.RelatedContacts _relation){
		this.relation = _relation;
	}
	
	public AddressInfo getAddress(){
		return this.address;
	}
	
	public void setAddressInfo(AddressInfo _address){
		this.address = _address;
	}
	
	public boolean getLastNameAddressMatch(){
		return this.lastNameAddressMatch;
	}
	
	public void setLastNameAddressMatch(boolean match){
		this.lastNameAddressMatch = match;
	}
}
