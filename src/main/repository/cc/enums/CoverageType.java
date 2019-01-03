package repository.cc.enums;

public enum CoverageType {

    jewelry("Jewelry"),
    creditcards("Credit Cards"),
    textiles("Textiles");

    private String text;

    CoverageType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static CoverageType getEnumByString(String text) {
        for (CoverageType e : CoverageType.values()) {
            if (text.equalsIgnoreCase(e.text)) {
                return e;
            }
        }
        return null;
    }
}
