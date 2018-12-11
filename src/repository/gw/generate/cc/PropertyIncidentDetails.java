package repository.gw.generate.cc;

import com.idfbins.enums.State;

public class PropertyIncidentDetails {
    private String propertyDescription = "";
    private String damageDescription = "";
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String city = "";
    private State state = null;
    private String zipCode = "";
    

    public PropertyIncidentDetails() {
        
    }
    
    public PropertyIncidentDetails(String propertyDescription, String damageDescription, String addressLine1, String addressLine2, String city, State state, String zipCode) {
        this.propertyDescription = propertyDescription;
        this.damageDescription = damageDescription;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
    
    public PropertyIncidentDetails(String addressLine1,String city, State state){
        this.propertyDescription = "";
        this.damageDescription = "";
        this.addressLine1 = addressLine1;
        this.addressLine2 = "";
        this.city = city;
        this.state = state;
        this.zipCode = "";
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
