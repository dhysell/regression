package repository.cc.enums;

public enum CheckLineItemCategory {

    // Indemnity Category
    INDEMNITY(repository.cc.enums.CheckLineItemType.INDEMNITY, "Indemnity"),
    PREPFEES(repository.cc.enums.CheckLineItemType.INDEMNITY, "Prep Fees"),
    STORAGEFEES(repository.cc.enums.CheckLineItemType.INDEMNITY, "Storage Fees"),
    TITLEFEES(repository.cc.enums.CheckLineItemType.INDEMNITY, "Title Fees"),
    TOWINGFEES(repository.cc.enums.CheckLineItemType.INDEMNITY, "Towing Fees"),

    // Salvage Category
    SALVAGESALE(repository.cc.enums.CheckLineItemType.SALVAGESALE, "Salvage Sale"),
    OTHER(repository.cc.enums.CheckLineItemType.SALVAGESALE, "Other"),

    // LAE Category
    ATTORNEYFEES(repository.cc.enums.CheckLineItemType.LAE, "Attorney Fees"),
    EXPERTWITNESSFEES(repository.cc.enums.CheckLineItemType.LAE, "Expert Witness Fees"),
    ALLOTHERCOSTS(repository.cc.enums.CheckLineItemType.LAE, "All Other Costs");

    CheckLineItemCategory(repository.cc.enums.CheckLineItemType checkLineItemType, String categoryText) {
        this.checkLineItemType = checkLineItemType;
        this.categoryText = categoryText;
    }

    private CheckLineItemType checkLineItemType;
    private String categoryText;

    public String getTextDescription() {
        return categoryText;
    }
}
