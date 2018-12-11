package repository.ab.contact;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ContactNameCasingFilter;
import repository.gw.enums.ContactRole;
import repository.gw.generate.custom.ContactName;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactNameCasingAB extends BasePage {
	
	private WebDriver driver;

    public ContactNameCasingAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /******************************************************************************************************************************************************
     *   										Repository Items
     * *****************************************************************************************************************************************************/

    @FindBy(xpath = "//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen')]/child::div[contains(@id, '-body')]/..")
    private WebElement tableDiv_ContactNameCasingPage;


    private Guidewire8Select select_ContactNameCasingFilter() {
        return new Guidewire8Select(driver, "//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:ActivitiesFilter-triggerWrap");
    }

    @FindBy(xpath = "//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:Update-btnEl")
    private WebElement button_ContactNameCasingUpdate;

    @FindBy(xpath = "//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:Cancel-btnEl")
    private WebElement button_ContactNameCasingCancel;

    @FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
    private WebElement input_PageSelector;

    private WebElement row_ContactNameCasingRowByName(String name) {
        return find(By.xpath("//div[.= '" + name + "']/ancestor::tr[1]"));
    }

    private WebElement row_ContactNameCasingRowByNumber(String row) {
        return find(By.xpath("//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:0-body')]//tr[contains(@data-recordindex, '" + row + "')]"));
    }

    private WebElement button_ContactNameCasingRowView(String row) {
        return find(By.xpath("//a[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:" + row + ":View')]"));
    }

    private WebElement button_ContactNameCasingRowRevert(String row) {
        return find(By.xpath("//a[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:" + row + ":Revert')]"));
    }

    private WebElement button_ContactNameCasingRowIgnore(String row) {
        return find(By.xpath("//a[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:" + row + ":Ignore')]"));
    }

    private WebElement button_ContactNameCasingRowApply(String row) {
        return find(By.xpath("//a[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:" + row + ":Apply')]"));
    }

    @FindBy(xpath = "//div[contains(@id, 'DesktopContactNames:DesktopQueuedActivitiesScreen:0')]")
    private WebElement table_ContactNameCasing;

    private WebElement row_ContactNameCasingRowByRole(String role) {
        return find(By.xpath("//div[contains(., '" + role + "')]/../../"));
    }


    // *****************************************************************************************************************************************************************************
    // 								Methods below are the helper methods
    // *****************************************************************************************************************************************************************************

    public void clickUpdate() {
        super.clickUpdate();
    }

    public void clickCancel() {
        super.clickCancel();
    }

    private void selectFilter(ContactNameCasingFilter filterBy) {
        select_ContactNameCasingFilter().selectByVisibleText(filterBy.getValue());
    }

    private int getRowByName(String name) {
        waitUntilElementIsVisible(row_ContactNameCasingRowByName(name));
        WebElement mine = row_ContactNameCasingRowByName(name);
        String mineText = mine.getText();
        System.out.println("the text of this row is " + mineText);

        if (new TableUtils(getDriver()).hasMultiplePages(tableDiv_ContactNameCasingPage)) {
            try {
                String rowString = row_ContactNameCasingRowByName(name).getAttribute("data-recordindex");
                int rowNum = Integer.parseInt(rowString);
                return rowNum;
            } catch (Exception e) {
                setTextHitEnter(input_PageSelector, String.valueOf(Integer.parseInt(getTextOrValueFromElement(input_PageSelector)) + 1));
                getRowByName(name);
            }
        }

        String rowString = row_ContactNameCasingRowByName(name).getAttribute("data-recordindex");
        int rowNum = Integer.parseInt(rowString);
        return rowNum;
    }

    private void clickView(int row) {
        clickWhenClickable(button_ContactNameCasingRowView(Integer.toString(row)));
    }

    private void clickRevert(int row) {
        clickWhenClickable(button_ContactNameCasingRowRevert(Integer.toString(row)));
    }

    private void clickIgnore(int row) {
        
        int i = 0;
        boolean buttonExists = false;
        do {
            clickWhenClickable(button_ContactNameCasingRowIgnore(Integer.toString(row)));
            buttonExists = checkIfElementExists(button_ContactNameCasingRowIgnore(Integer.toString(row)), 100);
            i++;
        } while (i < 20 && !buttonExists);
    }

    private void clickApply(int row) {
        
        clickWhenClickable(button_ContactNameCasingRowApply(Integer.toString(row)));
        
    }

    public ContactName getNameData(int row) throws Exception {
        String currentName = row_ContactNameCasingRowByNumber(Integer.toString(row)).findElement(By.xpath(".//td[1]")).getText();
        String newName = row_ContactNameCasingRowByNumber(Integer.toString(row)).findElement(By.xpath(".//td[2]")).getText();
        String roles = row_ContactNameCasingRowByNumber(Integer.toString(row)).findElement(By.xpath(".//td[3]")).getText();
        if (roles.trim() != null || roles.trim().equals("")) {
            ContactName dummyData = new ContactName(currentName, newName);
            return dummyData;
        } else {
            String[] contactRoleStrings = roles.split("\\s*,\\s*");
            ArrayList<ContactRole> contactRoles = new ArrayList<ContactRole>();
            for (String role : contactRoleStrings) {
                contactRoles.add(ContactRole.valueOf(role));
            }
            ContactName dummyData = new ContactName(currentName, newName, contactRoles);
            return dummyData;
        }
    }

    private int findRowWithActionButton(String button) {
        List<WebElement> tr = table_ContactNameCasing.findElements(By.xpath("//tr[contains(., '" + button + "')]"));
        return NumberUtils.generateRandomNumberInt(0, tr.size());
    }

    private ContactName searchByRole(ContactRole role) throws Exception {
        waitUntilElementIsVisible(row_ContactNameCasingRowByRole(role.getValue()));
        return getNameData(Integer.parseInt(row_ContactNameCasingRowByRole(role.getValue()).getAttribute("data-recordindex")));
    }

    private void changeNewName(String name, String newName) {
        new TableUtils(getDriver()).setValueForCellInsideTable(table_ContactNameCasing, getRowByName(name) + 1, "New Name", "CorrectedName", newName);
    }

    public boolean checkIfNameExistsCurrentPage(String name) {
        return checkIfElementExists("//div[contains(., '" + name + "')]/../../tr", 1000);
    }

    private int getContactNameCasingRowCount() {
        return new TableUtils(getDriver()).getRowCount(tableDiv_ContactNameCasingPage);
    }
    
    public ContactName ignoreContactCase() throws Exception {
    	 int row = findRowWithActionButton("Ignore");
    	 ContactName contactName = getNameData(row);
         clickIgnore(row);
         clickUpdate();
         waitForPostBack();
         return contactName;
    }
    
    public void fixName(String name, String newName) throws Exception {
        int row = NumberUtils.generateRandomNumberInt(0, getContactNameCasingRowCount());
        changeNewName(name, newName);
        clickApply(row);
        clickUpdate();
        waitForPostBack();
    }
    
    public int revertNameFix() throws Exception {
    	 int row = findRowWithActionButton("Apply");
         clickApply(row);
         clickRevert(row);
         waitForPostBack();
         return row;
    }    
}
