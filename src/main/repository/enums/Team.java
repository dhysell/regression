package repository.enums;

public enum Team {
    ACES("aces"),
    TRITON("triton"),
    SCOPE_CREEPS("scope creeps"),
    ACHIEVERS("achievers"),
    NUCLEUS("nucleus"),
    NO_EXCEPTIONS("no exceptions"),
    DATA_WIZARDS("data wizards"),
    ARTISTS("artists"),
    SYSTEMS("systems"),
    $$$("$$$");

    private String name;

    Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
