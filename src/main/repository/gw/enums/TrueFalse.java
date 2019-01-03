package repository.gw.enums;

public enum TrueFalse {
    True(true),
    False(false);
    Boolean value;

    private TrueFalse(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
