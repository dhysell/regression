package repository.gw.enums;

public enum MembershipCurrentMemberDuesStatus {

    Charged("Charged"),
    Charged_On_Another_Policy("Charged on Another Policy"),
    Honorary("Honorary"),
    Paid_Out_Of_State("Paid Out of State"),
    Waived("Waived");

    String value;

    private MembershipCurrentMemberDuesStatus(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

    public static MembershipCurrentMemberDuesStatus getEnumFromStringValue(String text) {
        // check that text isn't empty before doing comparison.
        if (text != null) {
            for (MembershipCurrentMemberDuesStatus type : MembershipCurrentMemberDuesStatus.values()) {
                if (text.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
        }
        return null; // text is null to begin with.
    }
}
