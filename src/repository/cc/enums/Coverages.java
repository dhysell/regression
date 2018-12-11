package repository.cc.enums;

public enum Coverages {

    EPLI("Employment Practices Liability Insurance", "Liability"),
    EquipmentBreakdown("Equipment Breakdown", "Property"),
    EquipmentBreakdownBPP("Equipment Breakdown - BPP", "Property"),
    EquipmentBreakdownBuilding("Equipment Breakdown - Building", "Property");

    private String coverageText;
    private String coverageType;

    Coverages(String coverageText, String coverageType) {
        this.coverageText = coverageText;
        this.coverageType = coverageType;
    }

    public String getText() {
        return this.coverageText;
    }

    public String getCoverageType() {
        return this.coverageType;
    }

}
