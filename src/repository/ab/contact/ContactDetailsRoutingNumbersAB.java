package repository.ab.contact;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsRoutingNumbersAB extends BasePage {
    public ContactDetailsRoutingNumbersAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // **************************************************************************
    // Methods below are the Repository Items.
    // ***************************************************************************

    @FindBy(xpath = "//span[contains(@id, '_tb:Edit-btnEl')]")
    private WebElement button_ContactDetailsRoutingNumbersEdit;

//	@FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:Edit-btnEl']")
//	public WebElement button_ContactDetailsRoutingNumbersEdit;

    @FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:Update-btnInnerEl']")
    private WebElement button_ContactDetailsRoutingNumbersUpdate;

    @FindBy(xpath = "//span[contains(@id, ':RoutingNumberLV_tb:ActivityDetailScreen_CancelButton-btnEl')]")
    private WebElement button_ContactDetailsRoutingNumbersCancel;

    @FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:Add-btnInnerEl']")
    private WebElement button_ContactDetailsRoutingNumbersAdd;

    @FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:Remove-btnInnerEl']")
    private WebElement button_ContactDetailsRoutingNumbersRemove;

    @FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:ABContactDetailScreen_RetireRoutingButton-btnInnerEl']")
    private WebElement button_ContactDetailsRoutingNumbersRetire;

    @FindBy(xpath = "//span[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV_tb:ABContactDetailScreen_UnretireRoutingButton-btnEl']")
    private WebElement button_ContactDetailsRoutingNumberUnretire;

    @FindBy(xpath = "//div[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV-body']/descendant::table")
    private WebElement table_ContactDetailsRoutingNumberEditTable;

    @FindBy(xpath = "//input[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV:_ListPaging-inputEl']")
    private WebElement textbox_ContactDetailsRoutingNumberResultsPageNum;

    @FindBy(xpath = "//table[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV:_ListPaging']/following-sibling::div[contains(., 'of')]")
    private WebElement text_ContactDetailsRoutingNumberMaxPageResults;

    @FindBy(xpath = "//div[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV-body']/descendant::table/descendant::tr/td[2]")
    private List<WebElement> routingNumberList;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:RoutingNumberLV-body')]/descendant::table/descendant::tr[last()]/td[2]/div")
    private WebElement input_ContactDetailsRoutingNumberinput;

    @FindBy(xpath = "//div[@id = 'ContactDetail:ABContactDetailScreen:RoutingNumberLV']")
    private WebElement div_ContactDetailsRoutingNumberTableContainer;

    @FindBy(xpath = "//input[contains(@id, ':_ListPaging-inputEl')]")
    private WebElement input_PageSelector;

    private WebElement checkbox_ContactDetailsRoutingNumberSelect(String value) {
        if (finds(By.xpath("//div[contains(., '" + value + "')]/ancestor::tr/td[1]/div/img")).size() <= 0) {
            return null;
        } else {
            return find(By.xpath("//div[contains(., '" + value + "')]/ancestor::tr/td[1]/div/img"));
        }
    }

    private WebElement text_ContactDetailsRoutingNumberDate(String routingNum) {
        return find(
                By.xpath("//div[contains(., '" + routingNum + "')]/ancestor::td[1]/following-sibling::td[1]"));
    }

    private WebElement input_ContactDetailsRoutingNumbersPriorRoutingNumber(String routingNumber) {
        return find(
                By.xpath("//div[contains(., '" + routingNumber + "')]/ancestor::td[1]/following-sibling::td[2]"));
    }

    @FindBy(xpath = "//input[@name= 'RoutingNumber']")
    private WebElement input_ContactDetailsRoutingNumbersRoutingNumberInput;

    @FindBy(xpath = "//input[@name= 'PriorRouting']")
    private WebElement input_ContactDetailsRoutingNumbersPriorRoutingInput;

    // ********************************************************************
    // Methods below are the helper methods.
    // ********************************************************************

    public void clickContactDetailRoutingNumberEditButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumbersEdit);
    }

    private void clickContactDetailRoutingNumberUpdateButton() {
        
        clickWhenClickable(button_ContactDetailsRoutingNumbersUpdate);
        
    }

    public void clickContactDetailRoutingNumberCancelButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumbersCancel);
        if (checkIfElementExists("//div[contains(@id, 'messagebox-1001-displayfield-inputEl')]", 1000)) {
            selectOKOrCancelFromPopup(OkCancel.OK);
        }
    }

    private void clickContactDetailRoutingNumberAddButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumbersAdd);
    }

    private void clickContactDetailRoutingNumberRemoveButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumbersRemove);
    }

    private void clickContactDetailRoutingNumberRetireButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumbersRetire);
    }

    private void clickContactDetailRoutingNumberUnretireButton() {
        clickWhenClickable(button_ContactDetailsRoutingNumberUnretire);
    }

    private boolean checkExistsContactDetailRoutingNumberEditButton() {
        boolean exists = checkIfElementExists(table_ContactDetailsRoutingNumberEditTable, 1000);
        return exists;
    }

    private boolean checkExistsContactDetailsRoutingNumberPageNumber() {
        boolean exists = checkIfElementExists(textbox_ContactDetailsRoutingNumberResultsPageNum, 1000);
        return exists;
    }

    private int getContactDetailsRoutingNumberPageCount() {
        
        boolean exists = checkExistsContactDetailsRoutingNumberPageNumber();
        int maxValue;
        if (exists) {
            String textMaxValue = text_ContactDetailsRoutingNumberMaxPageResults.getText();
            textMaxValue = textMaxValue.substring(2);
            textMaxValue = textMaxValue.trim();
            maxValue = (Integer.valueOf(textMaxValue)).intValue();
        } else {
            maxValue = -1;
        }
        return maxValue;
    }

    public ArrayList<String> getContactDetailsRoutingNumbers() throws Exception {
        boolean exists = checkExistsContactDetailsRoutingNumberPageNumber();
        ArrayList<String> routingNumbers = new ArrayList<String>();
        if (exists) {
            int pageCount = getContactDetailsRoutingNumberPageCount();
            int i = 1;
            while (i <= pageCount) {
                setTextHitEnter(input_PageSelector, String.valueOf(i));
                ArrayList<String> pageRoutingNumbers = new TableUtils(getDriver()).getAllCellTextFromSpecificColumn(div_ContactDetailsRoutingNumberTableContainer, "Routing #");
                routingNumbers.addAll(pageRoutingNumbers);
                i++;
            }
        } else {
            throw new Exception("This bank does not have routing numbers.");

        }
        return routingNumbers;
    }

    private boolean clickRoutingNumberCheckbox(String routingNumber) {
        

        boolean found = false;
        boolean pageNumberExists = checkExistsContactDetailsRoutingNumberPageNumber();
        if (pageNumberExists) {
            int pageCount = getContactDetailsRoutingNumberPageCount();
            for (int i = 1; i <= pageCount && found == false; i++) {
            	setTextHitEnter(input_PageSelector, String.valueOf(i));
                if (checkbox_ContactDetailsRoutingNumberSelect(routingNumber) != null) {
                    checkbox_ContactDetailsRoutingNumberSelect(routingNumber).click();
                    found = true;
                }
            }
        } else {
            checkbox_ContactDetailsRoutingNumberSelect(routingNumber).click();
            found = true;
        }
        return found;
    }

    private String checkRoutingNumberRetireDate(String routingNumber) {
        clickRoutingNumberCheckbox(routingNumber);
        String retireDate = text_ContactDetailsRoutingNumberDate(routingNumber).getText();
        return retireDate;
    }

    public void retireRoutingNumber(String routingNumber) {
        clickContactDetailRoutingNumberEditButton();
        if (text_ContactDetailsRoutingNumberDate(routingNumber).getText() != null || (!text_ContactDetailsRoutingNumberDate(routingNumber).getText().equals(""))) {
            clickRoutingNumberCheckbox(routingNumber);
            clickWhenClickable(button_ContactDetailsRoutingNumberUnretire);
        }
        clickRoutingNumberCheckbox(routingNumber);
        clickContactDetailRoutingNumberRetireButton();
        clickContactDetailRoutingNumberUpdateButton();
        waitUntilElementIsClickable(button_ContactDetailsRoutingNumbersEdit);
    }

    public void unretireRoutingNumber(String routingNumber) throws Exception {
        clickContactDetailRoutingNumberEditButton();
        if (checkRoutingNumberRetireDate(routingNumber) == null) {
            throw new Exception("Unable to retire routing number.");
        }
        // clickRoutingNumberCheckbox("323170112");
        
        clickContactDetailRoutingNumberUnretireButton();
        clickContactDetailRoutingNumberUpdateButton();
        
        waitUntilElementIsClickable(button_ContactDetailsRoutingNumbersEdit);
    }

    public void addRoutingNumber(String routingNum) {
        clickWhenClickable(button_ContactDetailsRoutingNumbersEdit);
        clickWhenClickable(button_ContactDetailsRoutingNumbersAdd);
        
        input_ContactDetailsRoutingNumberinput.click();
        input_ContactDetailsRoutingNumbersRoutingNumberInput.sendKeys(routingNum);
        input_ContactDetailsRoutingNumbersRoutingNumberInput.sendKeys(Keys.TAB);
        clickWhenClickable(button_ContactDetailsRoutingNumbersUpdate);
        
        waitUntilElementIsClickable(button_ContactDetailsRoutingNumbersEdit);
    }

    public void removeRoutingNumber(String routingNum) {
        clickContactDetailRoutingNumberEditButton();
        clickRoutingNumberCheckbox(routingNum);
        clickWhenClickable(button_ContactDetailsRoutingNumbersRemove);
        
        clickWhenClickable(button_ContactDetailsRoutingNumbersUpdate);
        waitUntilElementIsClickable(button_ContactDetailsRoutingNumbersEdit);

    }

    public void updateRoutingNumber(String oldRouting, String newRouting) {
        clickContactDetailRoutingNumberEditButton();
        clickRoutingNumberCheckbox(oldRouting);
        input_ContactDetailsRoutingNumbersPriorRoutingNumber(oldRouting).click();
        input_ContactDetailsRoutingNumbersPriorRoutingInput.sendKeys(oldRouting);
        clickWhenClickable(input_ContactDetailsRoutingNumberinput);
        for (int i = 0; i <= 9; i++) {
            input_ContactDetailsRoutingNumbersRoutingNumberInput.sendKeys(Keys.BACK_SPACE);
        }
        input_ContactDetailsRoutingNumbersRoutingNumberInput.sendKeys(newRouting);
        clickWhenClickable(button_ContactDetailsRoutingNumbersUpdate);
        waitUntilElementIsClickable(button_ContactDetailsRoutingNumbersEdit);

    }

}
