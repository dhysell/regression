package repository.cc.framework.gw.element.table;

public interface IUITableCell {
    String getText();

    void clickSelect();

    void clickLink();

    void fillTextArea(String text);

    void fillTextBox(String text);

    void clickCheckbox();
}
