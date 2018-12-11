package repository.gw.generate.cc;

import com.idfbins.enums.YesOrNo;
import repository.gw.enums.VehiclePersonType;

public class BackendPeople {
    private repository.gw.enums.VehiclePersonType type      = repository.gw.enums.VehiclePersonType.Driver;
    private String     firstName      = "";
    private String     lastName       = "";
    private String     phoneNumber    = "";
    private YesOrNo      injured        = null;
    private String     nameInDropdown = "";

    public String getNameInDropdown() {
        return nameInDropdown;
    }

    public void setNameInDropdown(String fullName) {
        this.nameInDropdown = fullName;
    }

    public repository.gw.enums.VehiclePersonType getType() {
        return type;
    }

    public void setType(repository.gw.enums.VehiclePersonType type) {
        this.type = type;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public YesOrNo getInjured() {
        return injured;
    }

    public void setInjured(YesOrNo injured) {
        this.injured = injured;
    }

    public BackendPeople() {

    }
    
    
    // Contructor for a new person 
    public BackendPeople(repository.gw.enums.VehiclePersonType type, String firstName, String lastName, String phoneNumber, YesOrNo injured, String nameInDropdown) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.injured = injured;
        this.nameInDropdown = nameInDropdown;
    }
    
    // Use for selecting a person already in the drop down. 
    public BackendPeople(VehiclePersonType type, YesOrNo injured, String nameInDropdown){
        this.type = type;
        this.injured = injured;
        this.nameInDropdown = nameInDropdown;
    }
}
