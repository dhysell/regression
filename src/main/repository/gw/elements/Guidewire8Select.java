package repository.gw.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class Guidewire8Select extends BasePage {

    private By input = By.xpath(".//input");
    private By listSelector = By.cssSelector("div[class*='x-boundlist-floating']:not([style*='display: none']):not([role='presentation']) ul");
    private By tableLocator;

    public Guidewire8Select(WebDriver driver, String tableOrDivXpath) {
        super(driver);
        this.tableLocator = By.xpath(tableOrDivXpath);
    }

    public void selectByVisibleText(String textToSelect) {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {
                clickWhenClickable(find(tableLocator).findElement(input));
            } catch (Exception e) {
                systemOut("Element wasn't visible");
            }
            counter++;
        }
        waitUntilElementIsClickable(listSelector);
        ArrayList<String> tmpArr = getListItems();
        if (!tmpArr.contains(textToSelect)) {
            Assert.fail("The list item searched was not found in the list. The item searched was \"" + textToSelect + "\"." + tmpArr);
        }

        clickWhenClickable(find(listSelector).findElement(By.xpath(".//li[(text()=\"" + textToSelect + "\")]")));
        waitForPostBack();
    }
    
    public void selectByVisibleTextExactContent(String textToSelect) {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {
                clickWhenClickable(find(tableLocator).findElement(input));
            } catch (Exception e) {
                systemOut("Element wasn't visible");
            }
            counter++;
        }
        waitUntilElementIsClickable(listSelector);
        ArrayList<String> tmpArr = getListItemsExactContent();
        if (!tmpArr.contains(textToSelect)) {
            Assert.fail("The list item searched was not found in the list. The item searched was \"" + textToSelect + "\"." + tmpArr);
        }

        clickWhenClickable(find(listSelector).findElement(By.xpath(".//li[(text()=\"" + textToSelect + "\")]")));
        waitForPostBack();
    }

    public void selectByVisibleTextPartial(String... partialText) {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {
                waitUntilElementIsVisible(find(tableLocator));
                clickWhenClickable(find(tableLocator).findElement(input));
            } catch (Exception e) {
                systemOut("Element wasn't visible");
            }
            counter++;
        }
        waitUntilElementIsClickable(listSelector);
        ArrayList<String> tmpArr = getListItems();
//        boolean matchfound = false;
        
        for(String item : partialText) {
        	if (!isItemInList(item)) {
                Assert.fail("The list item searched was not found in the list. The item searched was \"" + item + "\"." + tmpArr);
            }
        }
        
//      find(listSelector).findElement(By.xpath(".//li[contains(text(), \"" + partialText + "\")]")).click();
        find(listSelector).findElements(By.xpath(".//li")).get(getIndexOfItemInList(partialText)).click();
        waitForPostBack();
    }

    public boolean selectByVisibleTextPartialWithNoFail(String partialText) {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {
                clickWhenClickable(find(tableLocator).findElement(input));
            } catch (Exception e) {
                systemOut("Element wasn't visible");
            }
            counter++;
        }
        boolean found;
        waitUntilElementIsClickable(listSelector);
        ArrayList<String> tmpArr = getListItems();

        if (!isItemInList(partialText)) {
            System.out.println("The list item searched was not found in the list. The item searched was \"" + partialText + "\"." + tmpArr);
            found = false;
        } else {
//          find(listSelector).findElement(By.xpath(".//li[contains(text(), \"" + partialText + "\")]")).click();
            find(listSelector).findElements(By.xpath(".//li")).get(getIndexOfItemInList(partialText)).click();
            found = true;
            waitForPostBack();
        }
        return found;

    }

    public String selectByVisibleTextRandom() {
        ArrayList<String> tmpArr = getListItems();

        int index;
        if (tmpArr.size() > 1) {
            index = NumberUtils.generateRandomNumberInt(0, tmpArr.size() - 1);
        } else {
            index = 0;
        }

        String listItem = tmpArr.get(index);


        if (listItem.equalsIgnoreCase("<none>") && tmpArr.size() > 1) {
            index++;
            listItem = tmpArr.get(index);
            selectByVisibleText(listItem);

        } else if (listItem.equalsIgnoreCase("<none>") && tmpArr.size() <= 1) {
            selectByVisibleText("<none>");

        } else if (!listItem.equalsIgnoreCase("<none>")) {
            selectByVisibleText(listItem);

        } else {
            selectByVisibleText(listItem);
        }
        waitForPostBack();
        return listItem;
    }

    /**
     * @return list of strings in the table.
     */
    public ArrayList<String> getListItems() {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {

                clickWhenClickable(find(tableLocator).findElement(input));

            } catch (Exception e) {
                systemOut("Element wasn't visible");

            }
            counter++;
        }
        waitUntilElementIsClickable(listSelector);
        List<WebElement> tmpList = find(listSelector).findElements(By.xpath("./li"));
        ArrayList<String> stringList = new ArrayList<>();
        for (WebElement ele : tmpList) {
            stringList.add(ele.getText());
        }
        waitForPostBack();
        return stringList;
    }
    
    /**
     * @return list of strings in the table. The strings are returned exactly as they appear in the DOM (including leading and trailing spaces).
     */
    public ArrayList<String> getListItemsExactContent() {
        int counter = 0;
        while (finds(listSelector).size() <= 0 && counter < 20) {
            try {

                clickWhenClickable(find(tableLocator).findElement(input));

            } catch (Exception e) {
                systemOut("Element wasn't visible");

            }
            counter++;
        }
        waitUntilElementIsClickable(listSelector);
        List<WebElement> tmpList = find(listSelector).findElements(By.xpath("./li"));
        ArrayList<String> stringList = new ArrayList<>();
        for (WebElement ele : tmpList) {
            stringList.add(ele.getAttribute("textContent"));
        }
        waitForPostBack();
        return stringList;
    }

    public boolean isItemInList(String item) {
        boolean isFound = false;
        for (String listItem : getListItems()) {
            if (listItem.toLowerCase().contains(item.toLowerCase())) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    private int getIndexOfItemInList(String... item) {
        int index = 0;
        ArrayList<String> listItems = getListItems();
        for (int i = 0; i < listItems.size(); i++) {
        	for(String text : item) {
        		if (listItems.get(i).toLowerCase().contains(text.toLowerCase())) {
                    index = i;
                } else {
                	break;
                }
        	}
        }
        return index;
    }

    public void sendKeys(CharSequence... arg0) {
        find(tableLocator).findElement(input).sendKeys(arg0);
    }

    public void clear() {
        find(tableLocator).findElement(input).clear();
    }

    public List<String> getList() {
        return getListItems();
    }

    public String getText() {
        String valueToReturn = waitUntilElementIsVisible(find(tableLocator).findElement(input)).getAttribute("value");
//        clickProductLogo();
        sendArbitraryKeys(Keys.ESCAPE);
        return valueToReturn;
    }

    public WebElement getSelectButtonElement() {
        return waitUntilElementIsVisible(find(tableLocator).findElement(input));
    }

    public boolean isEnabled() {
        return find(tableLocator).findElement(input).isEnabled();
    }
    
    public boolean checkIfElementExists(int checkTime) {
        int normalizedTime = timeFixer(checkTime);
        boolean found = finds(tableLocator).size() > 0;
        for (int i = 0; !found && i < normalizedTime; ++i) {
            sleep(1); //Unsure if this loop is necessary.
            found = finds(tableLocator).size() > 0;
        }
        return found;
    }

}
