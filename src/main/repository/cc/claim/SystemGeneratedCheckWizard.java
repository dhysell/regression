package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class SystemGeneratedCheckWizard extends BasePage {

    private WebDriver driver;

    public SystemGeneratedCheckWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div.message")
    private WebElement errorMessageElement;

    private String getErrorMessageText() {
        return errorMessageElement.getText();
    }

    private String checkForErrors() {

        String message = null;

        if (waitUntilElementIsVisible(errorMessageElement, 5000) != null) {
            
            if (getErrorMessageText().contains("'Report As' should be set to 'Reportable'")) {
                selectReportAs("Reportable");
                
                clickNextButton();
            }
        }

        return message;
    }

    public void enterPayeeInformation(String payToName, String taxReporting, String mailingAddress, String checkNumber) {
        
        selectPrimaryPayeeName(payToName);
        
        selectReportAs("Not reportable");
        

        if (taxReporting.equalsIgnoreCase("Reportable")) {
            selectPrimaryPayeeType("Vendor");
        }

        selectMailingAddress(mailingAddress);
        
        setCheckNumber(checkNumber);
        
        clickNextButton();
        

        checkForErrors();
    }

    @FindBy(xpath = "//a[@id='SystemGeneratedCheckWizard:Next']")
    private WebElement button_Next;

    public void clickNextButton() {
        clickWhenClickable(button_Next);
    }

    @FindBy(xpath = "//a[@id='SystemGeneratedCheckWizard:Finish']")
    private WebElement button_Finish;

    public void clickFinishButton() {
        clickWhenClickable(button_Finish);
    }


    // Check number input box
    @FindBy(xpath = "//input[contains(@id,':Check_CheckNumber-inputEl')]")
    private WebElement input_CheckNumber;

    private void setCheckNumber(String checkNumber) {
        input_CheckNumber.sendKeys(checkNumber);
    }

    // Primary payee name select box.
    private Guidewire8Select select_PrimaryPayeeName() {
        return new Guidewire8Select(driver,"//table[contains(@id,':PrimaryPayee_Name-triggerWrap')]");
    }

    private void selectPrimaryPayeeName(String payToName) {
        List<String> payeeNames = select_PrimaryPayeeName().getList();
        for (String name : payeeNames) {
            if (payToName.contains(name)) {
                select_PrimaryPayeeName().selectByVisibleText(name);
                break;
            }
        }
    }

    // Primary payee type select box.
    private Guidewire8Select select_PrimaryPayeeType() {
        return new Guidewire8Select(driver,"//table[contains(@id,':PrimaryPayee_Type-triggerWrap')]");
    }

    private void selectPrimaryPayeeType(String payeeType) {
        select_PrimaryPayeeType().selectByVisibleText(payeeType);
    }

    // Report As select box.
    private Guidewire8Select select_ReportAs() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Check_Reportability-triggerWrap')]");
    }

    private void selectReportAs(String reportAs) {
        select_ReportAs().selectByVisibleText(reportAs);
    }

    // Mailing address select box.
    private Guidewire8Select select_MailingAddress() {
        return new Guidewire8Select(driver,"//table[contains(@id,':Check_MailingAddress-triggerWrap')]");
    }

    private void selectMailingAddress(String mailingAddress) {
        List<String> addresses = select_MailingAddress().getList();
        for (String address : addresses) {
            if (mailingAddress.equalsIgnoreCase(address)) {
                select_MailingAddress().selectByVisibleText(mailingAddress);
                break;
            } else {
                int count = 0;
                while (select_MailingAddress().getText().equalsIgnoreCase("<none>") && count < 5) {
                    select_MailingAddress().selectByVisibleTextRandom();
                    count++;
                }
                if (count >= 5) {
                    Assert.fail("A valid mailing address could not be selected.");
                }
            }
        }

    }

    // Check Type select box.
//	private Guidewire8Select select_CheckType() {
//		return new Guidewire8Select(
//				"//table[contains(@id,':Check_CheckType-triggerWrap')]");
//	}

//	private void selectCheckType(String checkType) {
//		select_CheckType().selectByVisibleText(checkType);
//	}

    public void EnterPaymentInformation(String reserveLine, String paymentAmount, String costCat) {
        selectReserveLine(reserveLine, costCat);
        
        selectPaymentType("Final");


    }

    public void lineItemPopulate(String type, String category, String amount, int rowNum) {

        int maxID = -1;
        int currentID = 0;
        String tableID = null;
        String tempID = null;

        for (int i = 2; i < 4; i++) {
            clickWhenClickable(find(By
                    .xpath("//div[@id='SystemGeneratedCheckWizard:SystemGeneratedCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body']/div/table/tbody/tr["
                            + rowNum + "]/td[" + i + "]/div")));

            List<WebElement> hiddenComboBoxs = finds(By.xpath("//table[starts-with(@id,'simplecombo-')]"));

            for (WebElement hiddenComboBox : hiddenComboBoxs) {
                tempID = hiddenComboBox.getAttribute("id");
                
                String tempString = tempID.replaceAll("[^0-9]", "");
                currentID = Integer.parseInt(tempString);

                if (currentID > maxID) {
                    maxID = currentID;
                    tableID = tempID;
                }
            }

            String tableXpath = "//table[starts-with(@id,'" + tableID + "')]";

            if (i == 2) {
                select_LineItemsType(tableXpath).clear();
                
                select_LineItemsType(tableXpath).selectByVisibleText(type);
                
            } else {
                try {
                    select_LineItemsType(tableXpath).clear();
                    
                    select_LineItemsType(tableXpath).selectByVisibleText(category);
                    
                } catch (Exception e) {

                }
            }
        }

        String rawReserveString = find(By.xpath("//div[contains(@id,'Transaction_AvailableReserves')]")).getText();

        String wholeNum = rawReserveString.replace("$", "");
        String[] parseData = wholeNum.split("\\.");
        String numsOnly = parseData[0].replaceAll("[^a-zA-Z0-9]", "");

        

        int amountLimit = Integer.parseInt(numsOnly);
        String checkAmount = "";
        if (amount == null) {
            checkAmount = Integer.toString(NumberUtils.generateRandomNumberInt(1, amountLimit));
        } else {
            checkAmount = amount;
            if (Integer.parseInt(numsOnly) > amountLimit) {
                checkAmount = Integer.toString(amountLimit);
            }

        }
        clickWhenClickable(find(By.xpath("//div[contains(@id,':EditablePaymentLineItemsLV-body')]/div/table/tbody/tr["
                + rowNum + "]/td[5]/div")));

        

        WebElement editBox_Amount = find(By.xpath("//input[@name='Amount']"));

        checkAmount = formatCheckAmount(checkAmount);

        editBox_Amount.sendKeys(checkAmount);
        
        editBox_Amount.sendKeys(Keys.TAB);

        
    }

    private Guidewire8Select select_LineItemsType(String tableXpath) {
        return new Guidewire8Select(driver,"" + tableXpath + "");
    }

    private Guidewire8Select select_PaymentType() {
        return new Guidewire8Select(driver,"//table[@id='SystemGeneratedCheckWizard:SystemGeneratedCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Payment_PaymentType-triggerWrap']");
    }

    private void selectPaymentType(String type) {
        select_PaymentType().selectByVisibleText(type);
    }

    private Guidewire8Select selectReserveLine() {
        return new Guidewire8Select(driver,"//table[contains(@id,':ReserveLine-triggerWrap')]");
    }

    private void selectReserveLine(String reserveLine, String costCat) {
        List<String> options = selectReserveLine().getList();

        boolean validSelection = false;

        for (String option : options) {
            if (option.contains(reserveLine) && option.contains(costCat)) {
                selectReserveLine().selectByVisibleText(option);
                validSelection = true;
                break;
            }
        }
        if (validSelection == false) {
            for (String option : options) {
                if (option.contains(reserveLine)) {
                    selectReserveLine().selectByVisibleText(option);
                    validSelection = true;
                    break;
                }
            }
        }
    }

    private String formatCheckAmount(String amount) {
        String tempString = amount.replace("$", "");
        tempString = tempString.replace(",", "");

        return tempString;
    }

}
