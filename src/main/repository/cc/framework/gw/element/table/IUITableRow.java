package repository.cc.framework.gw.element.table;

import java.util.List;

public interface IUITableRow {
    List<UITableCell> getCells();

    UITableCell getCell(int cellNum);

    UITableCell getCellByText(String cellText);

    boolean hasValueAtCell(String whatToSearch, int cellNumber);

    void clickCheckBox();

    void clickSelectButton();

    void clickButtonWithText(String buttonText);

    void clickRadioWithLabel(String label);
}
