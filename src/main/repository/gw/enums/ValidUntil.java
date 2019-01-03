package repository.gw.enums;

public enum ValidUntil {
	NextChange("Next Change"),
    EndOfTerm("End Of Term"),
	OneYear("One Year"),
	ThreeYears("Three Years"),
	Rescinded("Rescinded"),
	Issuance("Issuance");
    String value;

    private ValidUntil(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
