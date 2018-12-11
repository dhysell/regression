package repository.gw.enums;

public enum MembershipRenewalMemberDuesStatus {

    Charge_At_Renewal("Charge at Renewal"),
    No_Change("No Change"),
    Remove_At_Renewal("Remove at Renewal");

    String value;

    private MembershipRenewalMemberDuesStatus(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

    public static MembershipRenewalMemberDuesStatus getEnumFromStringValue(String text) {
        // check that text isn't empty before doing comparison.
        if (text != null) {
            for (MembershipRenewalMemberDuesStatus type : MembershipRenewalMemberDuesStatus.values()) {
                if (text.equalsIgnoreCase(type.value)) {
                    return type;
                }
            }
        }
        return null; // text is null to begin with.
    }
}
