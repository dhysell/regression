package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class ManualCheckWizard extends BasePage {

    private WebDriver driver;

    public ManualCheckWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[@id='ManualCreateCheckWizard:Cancel']")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='ManualCreateCheckWizard:Next']")
    public WebElement button_Next;

    @FindBy(xpath = "//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:AddJointPayees']")
    public WebElement button_AddJointPayees;

    @FindBy(xpath = "//input[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:SpecialHandling-inputEl']")
    public WebElement input_SpecialHandling;

    @FindBy(xpath = "//input[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckNumber-inputEl']")
    public WebElement input_CheckNum;

    @FindBy(xpath = "//input[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Transaction_Comments-inputEl']")
    public WebElement input_Comments;

    @FindBy(xpath = "//a[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV_tb:Add']")
    public WebElement button_AddItem;

    @FindBy(xpath = "//a[@id='ManualCreateCheckWizard:Finish']")
    public WebElement button_Finish;

    @FindBy(xpath = "//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    public WebElement element_InsuredName;


    public String getInsuredName() {
        return element_InsuredName.getText();
    }


    public void clickAddItem() {
        button_AddItem.click();
    }


    public void clickAddJointPayeesButton() {
        clickWhenClickable(button_AddJointPayees);
    }


    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }


    public void clickFinishButton() {
        clickWhenClickable(button_Finish);
    }


    public void clickNextButton() {
        clickWhenClickable(button_Next);
    }


    public String inputCheckNum(String num) {
        input_CheckNum.sendKeys(num);
        return num;
    }

    public void inputComments(String comment) {
        input_Comments.sendKeys(comment);
    }


    public void inputSpecialHandling(String text) {
        input_SpecialHandling.sendKeys(text);
    }


    public void lineItemPopulate(CheckLineItemType type, CheckLineItemCategory category, String amount, int rowNum) {

        int maxID = -1;
        int currentID = 0;
        String tableID = null;
        String tempID = null;

        for (int i = 2; i < 4; i++) {
            clickWhenClickable(find(By
                    .xpath("//div[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body']/div/table/tbody/tr["
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
                
                select_LineItemsType(tableXpath).selectByVisibleText(type.getTextDescription());
                
            } else {
                try {
                    select_LineItemsType(tableXpath).clear();
                    
                    select_LineItemsType(tableXpath).selectByVisibleText(category.getTextDescription());
                    
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
            if (Integer.parseInt(checkAmount) > amountLimit) {
                checkAmount = Integer.toString(amountLimit);
            }

        }
        clickWhenClickable(find(By
                .xpath("//div[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body']/div/table/tbody/tr["
                        + rowNum + "]/td[5]/div")));

        

        WebElement editBox_Amount = find(By.xpath("//input[@name='Amount']"));
        editBox_Amount.sendKeys(checkAmount);
        clickProductLogo();
        

    }


    public String manaulCheckNumberChecker(int begNum, int endNum) throws GuidewireException {

        String checkNumber = null;

        for (int i = begNum; i <= endNum; i++) {
            input_CheckNum.clear();
            // Parsing to a string to an int removes the starting zero if it is
            // there eg 0132465 is parsed to 132465. Need to add it back for
            // valid check number sequences.

            if (String.valueOf(i).length() == 5) {
                checkNumber = inputCheckNum("0" + Integer.toString(i));
            } else if (String.valueOf(i).length() == 4) {
                checkNumber = inputCheckNum("00" + Integer.toString(i));
            } else {
                checkNumber = inputCheckNum(Integer.toString(i));
            }
            
            clickNextButton();
            
            if (validateCheckNumber()) {
                break;
            }
        }
        return checkNumber;
    }

    public Guidewire8Select select_LineItemsCategory(String textString) {
        return new Guidewire8Select(driver,"" + textString + "");
    }

    public Guidewire8Select select_LineItemsType(String textString) {
        return new Guidewire8Select(driver,"" + textString + "");
    }

    public Guidewire8Select select_ManualCheckType() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckType-triggerWrap']");
    }

    public Guidewire8Select select_PayeeType() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:PrimaryPayee_Type-triggerWrap']");
    }

    public Guidewire8Select select_PaymentPartialOrFinal() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:Payment_PaymentType-triggerWrap']");
    }

    public Guidewire8Select select_PrimaryPayee() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:PrimaryPayee_Name-triggerWrap']");
    }

    public Guidewire8Select select_ReportAs() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_Reportability-triggerWrap']");
    }

    public Guidewire8Select select_ReserveLine() {
        return new Guidewire8Select(driver,"//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-triggerWrap']");
    }

    private Guidewire8Select selectMailingAddress() {
        return new Guidewire8Select(driver,"//table[contains(@id,'Check_MailingAddress-triggerWrap')]");
    }

    private void setMailingAddress() {
        selectMailingAddress().selectByVisibleTextRandom();
    }


    public void selectRandom_PayeeType() {
        select_PayeeType().selectByVisibleTextRandom();
    }


    public void selectRandom_PrimaryPayee() {
        select_PrimaryPayee().selectByVisibleTextRandom();
    }


    public void selectSpecific_ManualCheckType(String type) {
        select_ManualCheckType().selectByVisibleTextPartial(type);
    }


    public void selectSpecific_PayeeType(String type) {
        try {
            select_PayeeType().selectByVisibleText(type);
        } catch (Exception e) {
            selectRandom_PayeeType();
        }
    }


    public void selectSpecific_PaymmentPartialOrFinal(String item) {
        select_PaymentPartialOrFinal().selectByVisibleTextPartial(item);
    }


    public void selectSpecific_ReportAs(String item) {
        select_ReportAs().selectByVisibleText(item);
    }


    public void selectSpecific_ReserveLine(String line) {
        
        select_ReserveLine().selectByVisibleTextPartial(line);
    }


    public boolean validateCheckNumber() throws GuidewireException {
        boolean validNumber = true;
        List<WebElement> errorMessages = finds(By.xpath("//div[@class='message']"));
        if (errorMessages.size() > 0) {
            if (errorMessages.get(0).getText().contains("Check Number :")) {
                validNumber = false;
            } else if (errorMessages.get(0).getText().contains("Mailing Address :")) {
                setMailingAddress();
            } else {
                throw new GuidewireException(driver.getCurrentUrl(),
                        "Unexpected Message: " + errorMessages.get(0).getText());
            }
        }
        return validNumber;
    }


    public void enterPayeeInformation(String checkType) {
        selectPrimaryPayee(getInsuredName());
        selectPrimaryPayeeType("Claimant");
        selectManualCheckType(checkType);

        
    }


    public String enterPayeeInformation(String checkType, ManualCheckRegister checkBook) throws Exception {

        selectPrimaryPayeeExcluding();
        selectPrimaryPayeeTypeExcluding();

        selectManualCheckType(checkType);
        String checkNumber = manaulCheckNumberChecker(checkBook.getFirstCheck(), checkBook.getLastCheck());
        

        return checkNumber;
    }


    public void enterPayeeInformation(String checkType, String payToName, String checkNumber) {
        selectPrimaryPayeeExcluding();
        selectPrimaryPayeeTypeExcluding();

        selectManualCheckType(checkType);
        setCheckNumber(checkNumber);

        clickNextButton();
        
    }


    public void reEnterPayeeInformation(String payToName, String payeeType, String checkNumber) {

        List<String> payeeNames = select_PayeeType().getList();

        for (String payee : payeeNames) {
            if (payToName.contains(payee)) {
                selectPrimaryPayee(payToName);
            }
        }

        selectPrimaryPayeeType(payeeType);

        selectManualCheckType("System Generated");
        setCheckNumber(checkNumber);

        clickNextButton();
        
    }

    private void setCheckNumber(String checkNumber) {
        input_CheckNum.sendKeys(checkNumber);
    }

    private void selectPrimaryPayeeExcluding() {

        List<String> payeeOptions = select_PrimaryPayee().getList();

        for (String payee : payeeOptions) {
            if (!payee.equalsIgnoreCase(getInsuredName()) && !payee.equalsIgnoreCase("<none>")) {
                
                select_PrimaryPayee().selectByVisibleText(payee);
                
                break;
            }
        }
    }

    private void selectPrimaryPayeeTypeExcluding() {

        List<String> typeOptions = select_PayeeType().getList();

        for (String type : typeOptions) {
            if (!type.equalsIgnoreCase("Claimant") && !type.equalsIgnoreCase("Insured") && !type.equalsIgnoreCase("<none>") || type.equalsIgnoreCase("Other")) {
                
                select_PayeeType().selectByVisibleText(type);
                
                break;
            }
        }
    }

    private void selectManualCheckType(String checkType) {
        select_ManualCheckType().selectByVisibleTextPartial(checkType);
    }

    private void selectPrimaryPayeeType(String payeeType) {
        select_PayeeType().selectByVisibleText(payeeType);
    }

    private void selectPrimaryPayee(String payee) {
        select_PrimaryPayee().selectByVisibleText(payee);
    }
}
