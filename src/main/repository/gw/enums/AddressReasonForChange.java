package repository.gw.enums;

public enum AddressReasonForChange {
    MOVED("Moved"),
    ATTENDINGSCHOOL("Temporary address while attending school"),
    SEASONALTRAVEL("Temporary address for seasonal travel"),
    WORKINGOUTOFSTATE("Temporary address while working out of state"),
    PARENTORGUARDIAN("Parent/Guardian Address"),
    OTHER("Other");

    public String getText() {
        return text;
    }

    private String text;


    AddressReasonForChange(String text) {
        this.text = text;
    }


}
