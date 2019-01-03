package repository.cc.enums;

public enum PolicyType {

    FOURHCALF("4H Calf"),
    ASSIGNEDRISK("Assigned Risk"),
    BOP("BOP"),
    CITY("City"),
    COMMERCIALAUTO("Commercial Auto"),
    COMMERCIALCRIME("Commercial Crime"),
    COMMERCIALGARAGE("Commercial Garage"),
    COMMERCIALINLANDMARINE("Commercial Inland Marine"),
    COMMERCIALLIABILITY("Commercial Liability"),
    COMMERCIALPROPERTY("Commercial Property"),
    COMMERCIALUMBRELLA("Commercial Umbrella"),
    COUNTRY("County"),
    CPP("CPP"),
    CROPGRAIN("Crop Grain"),
    CROPHAIL("Crop Hail"),
    FARMANDRANCH("Farm and Ranch"),
    FIDELITYBONDS("Fidelity Bonds"),
    GARAGEKEEPERS("Garage Keepers"),
    GARAGELIABILITY("Garage Liability"),
    GARAGELIABILITYBROADENED("Garage Liability Broadened"),
    MEMBERSHIP("Membership"),
    STANDARDAUTO("Standard Auto"),
    STANDARDFIRE("Standard Fire"),
    STANDARDINLANDMARINE("Standard Inland Marine"),
    STANDARDLIABILITY("Standard Liability"),
    SURETYBONDS("Surety Bonds"),
    UMBRELLA("Umbrella"),
    WESCOMAUTO("Wescom Auto");

    private String policyText;

    PolicyType(String policyText) {
        this.policyText = policyText;
    }

    public String getPolicyText() {
        return this.policyText;
    }

}
