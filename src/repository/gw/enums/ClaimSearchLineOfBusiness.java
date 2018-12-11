package repository.gw.enums;

public enum ClaimSearchLineOfBusiness {
   None("<none>"),
   FourH_Calf_policy("4H Calf policy"),
   Assigned_Risk_Policy("Assigned Risk Policy"),
   Brokerage_Policy("Brokerage Policy"),
   BOP_General_Liability("Business Owner's Policy - General Liability"),
   BOP_Property("Business Owner's Policy - Property"),
   City_Squire_Policy_Auto("City Squire Policy - Auto"),
   City_Squire_Policy_General_Liability("City Squire Policy - General Liability"),
   City_Squire_Policy_Inland_Marine("City Squire Policy - Inland Marine"),
   City_Squire_Policy_Property("City Squire Policy - Property"),
   Commercial_Auto_Policy("Commercial Auto Policy"),
   Commercial_Inland_Marine_Policy("Commercial Inland Marine Policy"),
   Commercial_Liability_Policy("Commercial Liability Policy"),
   CPP_Auto("Commercial Package Policy - Auto"),
   CPP_General_Liability("Commercial Package Policy - General Liability"),
   CPP_Inland_Marine("Commercial Package Policy - Inland Marine"),
   CPP_Property("Commercial Package Policy - Property"),
   Commercial_Property_Policy("Commercial Property Policy"),
   Commercial_Umbrella_Policy("Commercial Umbrella Policy"),
   County_Squire_Policy_Auto("County Squire Policy - Auto"),
   County_Squire_Policy_General_Liability("County Squire Policy - General Liability"),
   County_Squire_Policy_Inland_Marine("County Squire Policy - Inland Marine"),
   County_Squire_Policy_Property("County Squire Policy - Property"),
   Crop_Grain("Crop Grain Policy"),
   Crop_Hail("Crop Hail Policy"),
   Farm_And_Ranch_Squire_Policy_Auto("Farm and Ranch Squire Policy - Auto"),
   Farm_And_Ranch_Squire_Policy_General_Liability("Farm and Ranch Squire Policy - General Liability"),
   Farm_And_Ranch_Squire_Policy_Inland_Marine("Farm and Ranch Squire Policy - Inland Marine"),
   Farm_And_Ranch_Squire_Policy_Property("Farm and Ranch Squire Policy - Property"),
   Fidelity_Bonds_Policy("Fidelity Bonds Policy"),
   Membership_Policy("Membership Policy"),
   Standard_Auto_Policy("Standard Auto Policy - Auto"),
   Standard_Fire_Policy("Standard Fire Policy"),
   Standard_Inland_Marine("Standard Inland Marine Policy"),
   Standard_Liability_Policy("Standard Liability Policy"),
   Surety_Bonds_Policy("Surety Bonds Policy"),
   Umbrella_Policy("Umbrella Policy"),
   Wescom_Auto_Policy("Wescom Auto Policy");

   String value;

    ClaimSearchLineOfBusiness(String type){
        value = type;
    }
    
    public String getValue(){
        return value;
    }
}
