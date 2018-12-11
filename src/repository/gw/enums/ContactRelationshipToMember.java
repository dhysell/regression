package repository.gw.enums;

public enum ContactRelationshipToMember {

    None("none"),
    Child_Ward("Child/Ward"),
    Parent_Guardian("Parent/Guardian"),
    Spouse("Spouse");

    String value;

    private ContactRelationshipToMember(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

    public static ContactRelationshipToMember getEnumFromStringValue(String text) {
        // check that text isn't empty before doing comparison.
        if (text != null) {
            for (ContactRelationshipToMember type : ContactRelationshipToMember.values()) {
                if (text.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
        }
        return null; // text is null to begin with.
    }
}
