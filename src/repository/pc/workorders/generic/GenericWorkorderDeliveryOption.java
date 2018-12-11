package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class GenericWorkorderDeliveryOption extends GenericWorkorder {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderDeliveryOption(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//input[contains(@id, 'RoleDeliveryOptionPopup:Description-inputEl')]")
    private WebElement deliveryOptionDescription;

    @FindBy(xpath = "//span[contains(@id, ':FBM_PolicyContactDetailsDV:Add-btnEl')]")
    private WebElement deliveryOptionsOK;

    @FindBy(xpath = "//span[contains(@id, ':FBM_PolicyContactDetailsDV:Add-btnEl')]")
    private WebElement deliveryOptionsCancel;

    @FindBy(xpath = "//span[contains(@id, ':DeliveryOptionListInputSet:ToolbarAddButton-btnEl')]")
    private WebElement addDeliveryOptions;

    @FindBy(xpath = "//a[contains(@id, ':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:addRemoveToFromDetailButton1')]")
    private WebElement addToSelectedDeliveryOptions;

    @FindBy(xpath = "//div[contains(@id, ':DeliveryOptionsInputSet:deliveryOption-inputEl')]")
    private WebElement selectedDeliveryOptions;

    public Guidewire8Select select_DeliveryOptionType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'RoleDeliveryOptionPopup:Type-triggerWrap')]");
    }

    @FindBy(xpath = "//div[contains(@id,':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:0')]")
    public WebElement table_DeliveryOptions;

    @FindBy(xpath = "//div[contains(@id,':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:0-body')]//table")
    private WebElement table_AvailableDeliveryOptions;

    @FindBy(xpath = "//a[contains(@id,':DeliveryOptionsInputSet:DeliveryOptionListInputSet:ToolbarRemoveButton')]")
    private WebElement button_DeliveryoptionsRemove;


    public void clickOK() {
        super.clickOK();
    }


    public void clickCancel() {
        super.clickCancel();
    }


    public void chooseDeliveryType(DeliveryOptionType optionType) {
        Guidewire8Select deliveryType = select_DeliveryOptionType();
        deliveryType.selectByVisibleText(optionType.getTypeValue());
    }


    public void setDescription(String description) {
        waitUntilElementIsClickable(deliveryOptionDescription);
        deliveryOptionDescription.click();
        deliveryOptionDescription.sendKeys(description);
    }


    public void clickAddDeliveryOptions() {
        clickWhenClickable(addDeliveryOptions);
    }


    public void clickAddToSelectedDeliveryOptions() {
        waitUntilElementIsClickable(addToSelectedDeliveryOptions);
        clickWhenClickable(addToSelectedDeliveryOptions);
    }


    public void clickAddToSelectedDeliveryOptionsByText(String typeValue) {
        int row = getDeliveryOptionRowNumber(typeValue);
        WebElement element = table_DeliveryOptions.findElement(By.xpath("//a[contains(@id, ':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:addRemoveToFromDetailButton" + row + "')]"));
        waitUntilElementIsClickable(element);
        clickWhenClickable(element);
    }


    public void addDeliveryOption(DeliveryOptionType option) {
        clickAddDeliveryOptions();
        tableUtils.selectValueForSelectInTable(table_DeliveryOptions, tableUtils.getRowCount(table_DeliveryOptions), "Type", option.getTypeValue());
    }


    public String getSelectedDeliveryOptions() {
        return selectedDeliveryOptions.getText();
    }

    private WebElement getAvailableDeliveryOptionsTable() {
        return table_AvailableDeliveryOptions;
    }

    private List<WebElement> getDeliveryOptionsRows() {
        return table_AvailableDeliveryOptions.findElements(By.xpath(".//tr"));
    }


    public void clickAddToSelectedDeliveryOptionsButton() {
        for (int rowNumber = 1; rowNumber <= getDeliveryOptionsRows().size(); rowNumber++) {
            clickWhenClickable(getAvailableDeliveryOptionsTable().findElement(By.xpath("//a[contains(@id, ':FBM_PolicyContactDetailsDV:DeliveryOptionsInputSet:DeliveryOptionListInputSet:addRemoveToFromDetailButton" + rowNumber + "')]")));
        }
    }


    public void removeDeliveryOption(int row) {
        tableUtils.setCheckboxInTable(table_DeliveryOptions, row, true);
        clickWhenClickable(button_DeliveryoptionsRemove);
    }


    public Integer getDeliveryOptionRowNumber(String contactName) {
        return tableUtils.getRowNumberInTableByText(table_DeliveryOptions, contactName);
    }

}
