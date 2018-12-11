package repository.gw.enums;

public enum RelationshipToInsuredPolicyMember {

    Child("Child"),
    Employee("Employee"),
    Insured("Insured"),
    Organization("Organization"),
    Other("Other"),
    Partnership("Partnership"),
    Relative("Relative"),
    SignificantOther("Significant Other/Partner"),
    SignificantOtherForPortal("Significant Other"),
    SignificantOtherFriend("Significant Other/ Friend"),// Portal use only
    PolicyHolder("Policyholder"), // Portal use only
    Spouse("Spouse"); // Portal

    String value;

    RelationshipToInsuredPolicyMember(String relationship) {
        value = relationship;
    }

    public String getValue() {
        return value;
    }
}
