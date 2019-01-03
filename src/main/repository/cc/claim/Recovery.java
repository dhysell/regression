package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.actionsmenu.ActionsMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Recovery extends BasePage {

    private WaitUtils waitUtils;
    private WebDriver driver;

    public Recovery(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addRecoveries(String selection) {

        BigDecimal recoveryAmount = new BigDecimal(NumberUtils.generateRandomNumberInt(20, 1000));

        selectRandomPayer();
        selectRandomReserveLine();
        

        setRecoveryCategory(selection);

/*        setCountyThroughPicker();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();

        setRandomCountyOffice();
        setPaymentType();
        setTransmittalDate(LocalDate.now());*/

        int numRows = finds(By.cssSelector("div[id*='RecoveryDetailDV:EditableRecoveryLineItemsLV-body'] div table tbody tr")).size();

        TableUtils tables = new TableUtils(driver);
        tables.selectRandomValueForSelectInTable(tableLineItems, numRows, "Category Values");
        tables.clickCellInTableByRowAndColumnName(tableLineItems, numRows, "Amount");
        tables.setValueForCellInsideTable(tableLineItems, "Amount", recoveryAmount.toString());
        sendArbitraryKeys(Keys.ESCAPE);

        FinancialsTransactions financialsTransactions = clickUpdateButton();

        if (getWarningMessage().equalsIgnoreCase("The recovery you entered will result in negative total recoveries.")) {
            while (getWarningMessage().equalsIgnoreCase("The recovery you entered will result in negative total recoveries.")) {
                clickAdd();
                numRows++;
                tables.selectRandomValueForSelectInTable(tableLineItems, numRows, "Category Values");
                tables.clickCellInTableByRowAndColumnName(tableLineItems, numRows, "Amount");
                recoveryAmount = recoveryAmount.add(new BigDecimal(NumberUtils.generateRandomNumberInt(20, 1000)));
                tables.setValueForCellInsideTable(tableLineItems, "Amount", recoveryAmount.toString());
                sendArbitraryKeys(Keys.ESCAPE);
                financialsTransactions = clickUpdateButton();
            }
        }

        waitUntilElementIsVisible(financialsTransactions.table_Transactions);

    }

    @FindBy(css = "div[class*='message']")
    private WebElement elementMessage;

    private String getWarningMessage() {

        String messageText = "";

        try {
            waitUtils.waitUntilElementIsVisible(elementMessage, 2);
            messageText = elementMessage.getText();
        } catch (Exception e) {
            messageText = "No Warnings";
        }

        return messageText;
    }

    @FindBy(css = "input[id*=':OfficeListing-inputEl']")
    private WebElement inputCountyOffice;

    public boolean addRandomRecoveries() {

        repository.cc.actionsmenu.ActionsMenu actions = new ActionsMenu(driver);

        BigDecimal recoveryAmount = new BigDecimal(NumberUtils.generateRandomNumberInt(20, 1000));

        actions.clickActionsButton();
        actions.clickOtherLink();
        actions.clickRecoveryLink();

        selectRandomPayer();
        selectRandomReserveLine();
        

        selectRandomRecoveryCategory();

        setRandomCountyName();
        setRandomCountyOffice();
        setPaymentType();
        setTransmittalDate(LocalDate.now());

        TableUtils tables = new TableUtils(getDriver());
        tables.selectRandomValueForSelectInTable(tableLineItems, 1, "Category Values");
        tables.clickCellInTableByRowAndColumnName(tableLineItems, 1, "Amount");
        tables.setValueForCellInsideTable(tableLineItems, "Amount", recoveryAmount.toString());
        sendArbitraryKeys(Keys.ESCAPE);

        FinancialsTransactions financialsTransactions = clickUpdateButton();

        waitUntilElementIsVisible(financialsTransactions.table_Transactions);

        List<WebElement> tableRows = tables.getAllTableRows(financialsTransactions.table_Transactions);

        boolean containsAmount = false;

        for (WebElement tableRow: tableRows) {
            if (tableRow.getText().contains(recoveryAmount.toString())) {
                containsAmount = true;
            }
        }

        return containsAmount;
    }

    private Guidewire8Select countyName() {
        return new Guidewire8Select(driver, "//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:CountyListing-triggerWrap']");
    }

    @FindBy(xpath = "//div[contains(@id,':EditableRecoveryLineItemsLV')]/ancestor::table")
    private WebElement tableLineItems;

    @FindBy(css = "input[id*=':RecoveryTransmittalDate-inputEl']")
    private WebElement inputTransmittalDate;

    private void setTransmittalDate(LocalDate dateSelection) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        waitUntilElementIsClickable(inputTransmittalDate);
        inputTransmittalDate.sendKeys(dateSelection.format(formatter));
    }

    private Guidewire8Select selectPaymentType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':RecoveryPaymentType-triggerWrap')]");
    }

    private void setPaymentType() {
        selectPaymentType().selectByVisibleTextRandom();
    }

    private void setRandomCountyName() {
        countyName().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectCountyOffice() {
        return new Guidewire8Select(driver, "//table[contains(@id,':OfficeListing-triggerWrap')]");
    }

    private void setRandomCountyOffice() {
        waitUntilElementIsClickable(inputCountyOffice);
        inputCountyOffice.click();
        selectCountyOffice().selectByVisibleTextRandom();
    }

    @FindBy(xpath = "//a[@id='NewRecoverySet:NewRecoveryScreen:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='NewRecoverySet:NewRecoveryScreen:Cancel']")
    public WebElement button_Cancel;

    public Guidewire8Select select_Payer() {
        return new Guidewire8Select(driver,"//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:Payer-triggerWrap']");
    }

    public Guidewire8Select select_ReserveLine() {
        return new Guidewire8Select(driver,"//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:RecoveryLineInputSet:ReserveLine-triggerWrap']");
    }

    public Guidewire8Select select_County() {
        return new Guidewire8Select(driver,"//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:CountyContact-triggerWrap']");
    }

    public Guidewire8Select select_SpecificLineItem(String tableXpath) {
        return new Guidewire8Select(driver,"" + tableXpath + "");
    }

    public Guidewire8Select select_CostType() {
        return new Guidewire8Select(driver,"//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:RecoveryLineInputSet:CostType-triggerWrap']");
    }

    @FindBy(css = "a[id*=':CountyContact:CountyContactMenuIcon']")
    private WebElement countyPicker;

    @FindBy(css = "a[id*=':CountyContact:MenuItem_Search-itemEl']")
    private WebElement countyPickerSearchLink;

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void selectCostType() {
        select_CostType().selectByVisibleText("Claim Cost");
    }

    public String selectRandomPayer() {

        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        List<String> validOptions = new ArrayList<>();
        List<String> options = select_Payer().getList();

        for (String option : options) {
            if (!option.equalsIgnoreCase("<none>")) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        select_Payer().selectByVisibleText(selection);
        return selection;
    }

    public void addRandomRecovery() {
        
        String payer = selectRandomPayer();
        System.out.println(payer);
        
        String reserveLine = selectRandomReserveLine();
        System.out.println(reserveLine);
        String recoveryCategory = selectRandomRecoveryCategory();
        System.out.println(recoveryCategory);
        
        String categoryValue = setRandomCategoryValues();
        System.out.println(categoryValue);
        
        String amount = setRandomAmount();
        System.out.println("$" + amount);
        
        clickUpdateButton();
    }

    public String selectRandomReserveLine() {

        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        List<String> validOptions = new ArrayList<>();
        List<String> options = select_ReserveLine().getList();

        for (String option : options) {
            if (!option.equalsIgnoreCase("<none>")) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        select_ReserveLine().selectByVisibleText(selection);
        return selection;
    }

    public List<String> getReserveLineList() {
        List<String> items = select_ReserveLine().getList();
        return items;
    }

    public void selectRandomCounty() {
        select_County().selectByVisibleTextRandom();
    }

    public Guidewire8Select recoveryCategorySelect() {
        return new Guidewire8Select(driver,"//table[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:RecoveryCategory-triggerWrap']");
    }

    public List<String> getRecoveryCategories() {
        List<String> options = recoveryCategorySelect().getList();
        options.remove("<none>");
        return options;
    }

    private void setRecoveryCategory(String selection) {
        recoveryCategorySelect().selectByVisibleText(selection);
    }

    public String selectRandomRecoveryCategory() {

        List<String> validOptions = new ArrayList<>();
        List<String> options = recoveryCategorySelect().getList();

        for (String option : options) {
            if (!option.equalsIgnoreCase("<none>")) {
                validOptions.add(option);
            }
        }

        String selection = validOptions.get(NumberUtils.generateRandomNumberInt(0, validOptions.size() - 1));

        recoveryCategorySelect().selectByVisibleText(selection);
        return selection;
    }

    @FindBy(css = "div[id*=':EditableRecoveryLineItemsLV-body'] table tbody tr:nth-child(1) td:nth-child(3)")
    private WebElement categoryValuesCell;

    public String setRandomCategoryValues() {

        try {
            waitUtils.waitUntilElementIsVisible(categoryValuesCell);
        } catch (Exception e) {
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            waitUtils.waitUntilElementIsVisible(categoryValuesCell);
        }

        categoryValuesCell.click();

        List<WebElement> items = finds(By.cssSelector("div[id*='boundlist-'] ul li"));
        List<String> options = new ArrayList<>();
        for (WebElement item : items) {
            if (!item.getText().equalsIgnoreCase("<none>"))
                options.add(item.getText());
        }

        String selectionString = options.get(NumberUtils.generateRandomNumberInt(0, options.size() - 1));
        find(By.xpath("//li[contains(text(),'" + selectionString + "')]")).click();

        return selectionString;
    }

    @FindBy(css = "div[id*=':EditableRecoveryLineItemsLV-body'] table tbody tr:nth-child(1) td:nth-child(5)")
    private WebElement amountCell;

    @FindBy(css = "input[name*='Amount']")
    private WebElement amountInput;

    public String setRandomAmount() {

        waitUtils.waitUntilElementIsVisible(amountCell);
        amountCell.click();

        while (!amountInput.isDisplayed()) {
            amountCell.click();
        }

        String amount = Integer.toString(NumberUtils.generateRandomNumberInt(5, 101));

        amountInput.sendKeys(amount);

        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        return amount;
    }

    @FindBy(xpath = "//span[contains(@id,'NewRecoverySet:NewRecoveryScreen:Update-btnInnerEl')]/ancestor::a")
    private WebElement updateButton;

    public FinancialsTransactions clickUpdateButton() {
        
        waitUntilElementIsClickable(updateButton);
        updateButton.click();
        return new FinancialsTransactions(getDriver());
    }

    public Integer selectRandom_SpecificSelectFromTable(String tableID, int row, int col, int previousValue) {

        int maxValue = -1;
        int count = 0;
        int index = -1;

        // Focus Page
        find(By.xpath("//label[contains(text(),'Line Items')]")).click();

        System.out.println("//table[@id='" + tableID + "']/tbody/tr[" + (row) + "]/td[" + col + "]");

        WebElement tdCell = find(By.xpath("//table[@id='" + tableID + "']/tbody/tr[" + (row) + "]/td[" + col + "]"));

        System.out.println(tdCell.getAttribute("id"));

        Actions build = new Actions(this.driver);
        build.moveToElement(tdCell, 30, 0).click().build().perform();
        
        List<WebElement> hiddenComboBox = finds(
                By.xpath("//table[starts-with(@id,'simplecombo-')]//table[starts-with(@id,'simplecombo-')]"));

        for (WebElement webElement : hiddenComboBox) {
            int currentValue = Integer.parseInt(webElement.getAttribute("id").replaceAll("[^0-9.]", ""));
            if (currentValue > previousValue) {
                maxValue = currentValue;
                index = count;
            }
            count++;
        }

        String selectID = hiddenComboBox.get(index).getAttribute("id");
        String tableXpath = "//table[starts-with(@id,'" + selectID + "')]";
        
        System.out.println(tableXpath);
        select_SpecificLineItem(tableXpath).sendKeys(Keys.ENTER);
        
        select_SpecificLineItem(tableXpath).selectByVisibleTextRandom();
        

        return maxValue;

    }

    public void addRecoveryLineItem() {
        
        String lineItemTable = find(By
                .xpath("//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:EditableRecoveryLineItemsLV-body']/div/table"))
                .getAttribute("id");
        int row = finds(By
                .xpath("//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:EditableRecoveryLineItemsLV-body']/div/table/tbody/tr"))
                .size();

        int newAmount = 0;
        int previousValue = 0;

        // Reserve Category Select

        previousValue = selectRandom_SpecificSelectFromTable(lineItemTable, row, 2, previousValue);
        WebElement reservesCategory = find(By.xpath("//label[contains(text(),'Line Items')]"));
        
        reservesCategory.click();
        

        // Category Values Select

        lineItemTable = find(By
                .xpath("//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:EditableRecoveryLineItemsLV-body']/div/table"))
                .getAttribute("id");
        row = finds(By
                .xpath("//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:EditableRecoveryLineItemsLV-body']/div/table/tbody/tr"))
                .size();
        previousValue = selectRandom_SpecificSelectFromTable(lineItemTable, row, 3, previousValue);
        WebElement catValues = find(By.xpath("//label[contains(text(),'Line Items')]"));
        
        catValues.click();
        

        // Amount Select

        WebElement payments = find(
                By.xpath("//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:Payments-inputEl']"));
        if (payments.getText().equalsIgnoreCase("-")) {
            
            selectCostType();
            newAmount = NumberUtils.generateRandomNumberInt(500, 100000);
        } else {
            String[] paymentsString = payments.getText().split("\\.");
            String intString = paymentsString[0].replaceAll("[^0-9.]", "");
            int max = Integer.parseInt(intString);

            newAmount = NumberUtils.generateRandomNumberInt(1, max);
        }

        

        find(By.xpath("//label[contains(text(),'Line Items')]")).click();

        WebElement tableCell = find(By.xpath(
                "//div[@id='NewRecoverySet:NewRecoveryScreen:RecoveryDetailDV:EditableRecoveryLineItemsLV-body']/div/table/tbody/tr[1]/td[5]"));
        Actions build = new Actions(driver);
        build.moveToElement(tableCell, 25, 0).click().build().perform();

        
        WebElement amountInput = find(By.xpath("//table[starts-with(@id,'textfield-')]//input[starts-with(@id,'textfield-')]"));
        
        amountInput.sendKeys(Integer.toString(newAmount));

        clickUpdateButton();

        

    }

}
