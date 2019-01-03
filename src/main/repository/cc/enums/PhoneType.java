package repository.cc.enums;

public enum PhoneType {

    WORK("Work"),
    HOME("Home"),
    MOBILE("Mobile"),
    BUSINESS("Business"),
    FAX("Fax");

    private String selectionText;

    PhoneType(String selectionText) {
        this.selectionText = selectionText;
    }

    public static PhoneType getEnumByString(String selectionText) {
        for (PhoneType e : PhoneType.values()) {
            if (selectionText.equalsIgnoreCase(e.selectionText)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return selectionText;
    }
}
