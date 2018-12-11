package repository.gw.enums;


import java.util.Random;

public enum SquireEligibility {
    City("City Squire"),
    Country("Country Squire"),
    CustomAuto("Custom Auto"),
    FarmAndRanch("Farm And Ranch"),
    CountryIneligibleCustomFarmingCoverage("Country Ineligible Custom Farming Coverage");

    String value;

    SquireEligibility(String type) {
        this.value = type;
    }

    public String getValue() {
        return value;
    }

    public static SquireEligibility random() {
        return (getRandBoolean()) ? SquireEligibility.City : ((getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);
    }

    private static Boolean getRandBoolean() {
        return new Random().nextBoolean();
    }
}
