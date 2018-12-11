package repository.cc.framework.utils.helpers;

public class Identifier {

    public static final String ID = "id";
    public static final String CLASS = "class";
    public static final String NAME = "name";
    public static final String TEXT = "text";
    public static final String WAIT_CLICK = "wait until clickable";
    public static final String WAIT_VISIBLE = "wait until visible";
    public static final String OFFSET = "requires offset";
    public static final String CSS = "css";
    public static final String WAIT_OPTIONAL = "may or may not exist";
    private String type;
    private String value;
    private String waitType;

    public Identifier(String type, String value) {
        this.type = type;
        this.value = value;
        this.waitType = WAIT_CLICK;
    }

    public Identifier(String type, String value, String waitType) {
        this.type = type;
        this.value = value;
        this.waitType = waitType;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getWaitType() {
        return waitType;
    }

    @Override
    public String toString() {
        return this.getType() + ":" + this.getWaitType() + ":" + this.getValue();
    }
}
