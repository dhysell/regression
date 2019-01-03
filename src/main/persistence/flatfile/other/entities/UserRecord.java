package persistence.flatfile.other.entities;

public class UserRecord {

	private String firstName;
	private String lastName;
	private String username;
	
	public UserRecord() {
		
	}
	
	public UserRecord(String firstName, String lastName, String username) {
		setFirstName(firstName);
		setLastName(lastName);
		setUsername(username);
	}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }
    
    public boolean equals(UserRecord recordToCompare) {
    	return (this.getFirstName().equals(recordToCompare.getFirstName()) &&
    			this.getLastName().equals(recordToCompare.getLastName()) &&
    			this.getUsername().equals(recordToCompare.getUsername()));
    }
}
