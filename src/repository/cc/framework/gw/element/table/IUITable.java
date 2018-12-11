package repository.cc.framework.gw.element.table;

import java.util.List;

public interface IUITable {
    List<UITableRow> getRows();

    void clickAdd();

    void clickRemove();

    UITableRow getRowWithText(String value);
}
