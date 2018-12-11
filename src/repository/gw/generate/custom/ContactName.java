package repository.gw.generate.custom;

import repository.gw.enums.ContactRole;

import java.util.ArrayList;

public class ContactName {

	private String currentName;
	private String newName;
	private ArrayList<repository.gw.enums.ContactRole> roles;
	
	public ContactName(String _currentName, String _newName) throws Exception{
		setCurrentName(_currentName);
		setNewName(_newName);
		
	}
	
	public ContactName(String _currentName, String _newName, ArrayList<repository.gw.enums.ContactRole> _roles) throws Exception{
		setCurrentName(_currentName);
		setNewName(_newName);
		setRoles(_roles);
		
	}
	
	//Getters and Setters
	public void setCurrentName(String name) throws Exception{
		if(name != null){
			if(!name.equals("")) {
				this.currentName = name;
			} else {
				throw new Exception("The current Name cannot contain an empty string.");
			}
		} else {
			throw new Exception("The current Name cannot be null");
		}	
	}
	
	public String getCurrentName(){
		return currentName;
	}
	
	public void setNewName(String name) throws Exception{
		if(name != null){
			if(!name.equals("")) {
				this.newName = name;
			} else {
				throw new Exception("The New Name cannot contain an empty string.");
			}
		} else {
			throw new Exception("The New Name cannot be null");
		}	
	}
	
	public String getNewName(){
		return this.newName;
	}
	
	public void setRoles(ArrayList<repository.gw.enums.ContactRole> _roles){
		this.roles = _roles;
	}
	
	public void addRole(repository.gw.enums.ContactRole role){
		this.roles.add(role);
	}
	
	public ArrayList<ContactRole> getRoles(){
		return this.roles;
	}
}
