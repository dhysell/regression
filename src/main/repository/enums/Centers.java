package repository.enums;

public enum Centers {

    PC("policy center"),
    BC("billing center"),
    AB("contact manager"),
    CC("claim center");

    private String name;

    Centers(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
