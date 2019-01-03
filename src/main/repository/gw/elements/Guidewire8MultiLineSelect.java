package repository.gw.elements;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.ArrayList;
import java.util.List;


public class Guidewire8MultiLineSelect extends BasePage {

    private WebDriver classDriver;
    private WebElement listElement;
    private List<WebElement> listItemsElement;
    private List<String> listItemsStrings;


    public Guidewire8MultiLineSelect(WebDriver driver, String uniqueContainerDivXpath) {
        super(driver);
        this.classDriver = driver;

        String completeXPath = uniqueContainerDivXpath + "/span/div/div/div/ul[@class='x-list-plain']";
        this.listElement = classDriver.findElement(By.xpath(completeXPath));

        setListElements();
        setListStrings();
        PageFactory.initElements(driver, this);
    }

    public void clear() {
        listElement.clear();
    }

    public void click() {
        listElement.click();
    }

    public WebElement findElement(By arg0) {
        return listElement.findElement(arg0);
    }

    public List<WebElement> findElements(By arg0) {
        return listElement.findElements(arg0);
    }

    public String getAttribute(String arg0) {
        return listElement.getAttribute(arg0);
    }

    public String getCssValue(String arg0) {
        return listElement.getCssValue(arg0);
    }

    public Point getLocation() {
        return listElement.getLocation();
    }

    public Dimension getSize() {
        return listElement.getSize();
    }

    public String getTagName() {
        return listElement.getTagName();
    }

    public String getText() {
        return listElement.getText();
    }

    public boolean isDisplayed() {
        return listElement.isDisplayed();
    }

    public boolean isEnabled() {
        return listElement.isEnabled();
    }

    public void setListElements() {
        List<WebElement> liItems = listElement.findElements(By.xpath(".//li[starts-with(@class,'x-boundlist-item')]"));

        this.listItemsElement = liItems;
    }

    public List<WebElement> getListElements() {
        return this.listItemsElement;
    }

    public void setListStrings() {
        List<WebElement> liItems = getListElements();
        ArrayList<String> itemsList = new ArrayList<String>();
        for (WebElement elementIterator : liItems) {
            itemsList.add(elementIterator.getText());
        }

        this.listItemsStrings = itemsList;
    }

    public List<String> getListStrings() {
        return this.listItemsStrings;
    }

    public void selectListItemByText(String listItemToSelect) {
        for (int index = 0; index < listItemsElement.size(); index++) {
            if (listItemsElement.get(index).getText().equalsIgnoreCase(listItemToSelect)) {
                listItemsElement.get(index).click();
                break;
            }
        }
    }
}
