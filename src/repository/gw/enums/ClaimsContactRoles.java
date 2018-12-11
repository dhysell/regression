package repository.gw.enums;

public enum ClaimsContactRoles {
   Agent("Agent"),
   AlternateContact("Alternate Contact"),
   ArbitrationVenue("Arbitration Venue"),
   Arbitrator("Arbitrator"),
   Assessor("Assessor"),
   Attorney("Attorney"),
   Driver("Driver"),
   ExcludedParty("Excluded Party"),
   HospitalMedicalCenter("Hospital - Medical Center"),
   IndependentAdjuster("Independent Adjuster"),
   Insured("Insured"),
   InsuredRepresentative("Insured Representative"),
   Lienholder("Lienholder"),
   ListedDriver("Listed Driver"),
   ListedInsured("Listed Insured"),
   MainContact("Main Contact"),
   Other("Other"),
   Passenger("Passenger"),
   Pedestrian("Pedestrian"),
   Reporter("Reporter"),
   ResponsibleParty("Responsible Party"),
   SalvageVendor("Salvage Vendor"),
   ServicingAgent("Servicing Agent"),
   TowingVendor("Towing Vendor"),
   Vendor("Vendor"),
   Witness("Witness");
   String value;
   
        
    private ClaimsContactRoles(String type){
        value = type;
    }
    
    public String getValue(){
        return value;
    }
}
