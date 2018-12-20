package repository.cc.framework.gw.element.selectbox;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import repository.cc.framework.gw.element.UIElement;
import repository.gw.helpers.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class UISelectBox extends UIElement implements IUISelectBox {

    private List<WebElement> listElements;
    private WebDriver driver;

    public UISelectBox(WebDriver driver, WebElement element, List<WebElement> listElements) {
        super(driver, element);
        this.listElements = listElements;
        this.driver = driver;
    }

    @Override
    public void select(String selection) {
        for (WebElement listItem : this.listElements) {
            if (listItem.getText().equalsIgnoreCase(selection)) {
                listItem.click();
                break;
            }
        }
    }

    @Override
    public String selectRandom() {
        List<WebElement> validItems = new ArrayList<>();
        int count = 0;

        for (WebElement select : listElements) {
            if (!select.getText().equalsIgnoreCase("New...")
                    && !select.getText().equalsIgnoreCase("<none>")) {
                validItems.add(select);
                count++;
            }
        }
        if (validItems.size() > 0) {
            WebElement selectionElement = validItems.get(NumberUtils.generateRandomNumberInt(0, count - 1));
            selectionElement.click();
            return selectionElement.getText();
        } else {
            return null;
        }
    }

    @Override
    public String select(int itemNumber) {
        WebElement selectElement = listElements.get(itemNumber);
        selectElement.click();
        new Actions(this.driver).sendKeys(Keys.ESCAPE).perform();
        return selectElement.getText();
    }

    @Override
    public boolean hasOption(String selection) {
        for (WebElement listItem : listElements) {
            if (listItem.getText().equalsIgnoreCase(selection)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getOptions() {
        List<String> listStrings = new ArrayList<>();

        for (WebElement element : listElements) {
            listStrings.add(element.getText());
        }
        return listStrings;
    }

    @Override
    public String selectFirstExisting(String[] selections) {
        for (String selection: selections) {
            for (WebElement listElement: this.listElements) {
                if (selection.equalsIgnoreCase(listElement.getText())) {
                    this.select(selection);
                    return selection;
                }
            }
        }
        return null;
    }
}
