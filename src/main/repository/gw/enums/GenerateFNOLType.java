package repository.gw.enums;

public enum GenerateFNOLType {
    Auto("Auto"),
    AutoGlass("Auto - ERS or Glass"),
    GeneralLiability("General Liability"),
    InlandMarine("Inland Marine"),
    Property("Property"),
    ResidentialGlass("Property - Residential Glass"),
    CropHail("Crop"),
    Umbrella("Umbrella"),
    PortalDraft("Portal Draft"),
    Membership("Membership");
    
    private String value;


    GenerateFNOLType(String type) {
        value = type;
    }
    
    public String getValue(){
        return value;
    }
}
