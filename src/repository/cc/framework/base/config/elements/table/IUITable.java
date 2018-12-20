package repository.cc.framework.base.config.elements.table;

import repository.cc.framework.gw.element.table.UITableRow;

import java.util.List;

public interface IUITable {
    List<UITableRow> getRows();

    void clickAdd();

    void clickRemove();

    UITableRow getRowWithText(String value);

    void clickNextPage();

    void clickPreviousPage();

    void clickLastPage();

    void clickFirstPage();

    void clickToolbarButtonWithText(String buttonText);
}
