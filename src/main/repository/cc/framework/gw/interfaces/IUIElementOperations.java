package repository.cc.framework.gw.interfaces;

public interface IUIElementOperations {
    void click();

    void doubleClick();

    void hover();

    String screenGrab();

    boolean isPresent();

    void refreshElement();
}
