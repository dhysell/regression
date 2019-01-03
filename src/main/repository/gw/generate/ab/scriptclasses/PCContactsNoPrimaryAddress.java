package repository.gw.generate.ab.scriptclasses;

public class PCContactsNoPrimaryAddress {
		
		private String publicID = null;
		private String firstName = null;
		private String lastName = null;
		private String name = null;
		
		//Constructor
		public PCContactsNoPrimaryAddress() {}
		
		public PCContactsNoPrimaryAddress(String _publicID, String _firstName, String _lastName, String _name) {
			setPublicID(_publicID);
			setName(_name);
			setLastName(_lastName);
			setFirstName(_firstName);
		}
		
		//Getters and Setters
		
		public String getPublicID() {
			return this.publicID;
		}
		
		public void setPublicID(String _publicID) {
			this.publicID = _publicID;
		}
		
		public String getFirstName() {
			return this.firstName;
		}
		
		public void setFirstName(String _firstName) {
			this.firstName = _firstName;
		}
		
		public String getLastName() {
			return this.lastName;
		}
		
		public void setLastName(String _lastName) {
			this.lastName = _lastName;
		}
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String _name) {
			this.name = _name;
		}
}
