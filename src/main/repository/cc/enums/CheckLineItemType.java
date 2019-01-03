package repository.cc.enums;

public enum CheckLineItemType {

    BETTERMENTDEPRECIATION("Betterment / Depreciation", false),
    DEDUCTIBLE("Deductible", false),
    INDEMNITY("Indemnity", true),
    OWNERBUYBACK("Owner Buy Back", false),
    PREMIUMPAYMENT("Premium Payment", false),
    SALVAGESALE("Salvage Sale", true),

    LAE("LAE", true),
    SUPPLEMENTAL("Supplemental", false);


    private String textDescription;
    private Boolean containsCategory;

    CheckLineItemType(String textDescription, Boolean containsCategory) {
        this.textDescription = textDescription;
        this.containsCategory = containsCategory;
    }

    public String getTextDescription() {
        return this.textDescription;
    }

    public Boolean hasCategory() {
        return containsCategory;
    }
}
