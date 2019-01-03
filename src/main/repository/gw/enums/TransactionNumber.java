package repository.gw.enums;

public enum TransactionNumber {
    Membership_Dues_Charged("Membership Dues Charged"),
    Premium_Charged("Premium Charged"),
    CarryOver_Charge_Charged("Carry Over Charge Charged"),
    Policy_Recapture_Charged("Policy Recapture Charged"),
    Policy_Payment_Reversal_Fee_Charged("Policy Payment Reversal Fee Charged");

    String value;

    private TransactionNumber(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}