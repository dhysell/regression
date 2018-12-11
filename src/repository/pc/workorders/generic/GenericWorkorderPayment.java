package repository.pc.workorders.generic;

import com.idfbins.ccnumberutils.CCType;
import com.idfbins.ccnumberutils.CCUtils;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.*;
import repository.gw.helpers.*;
import repository.gw.infobar.InfoBar;
import repository.pc.sidemenu.SideMenuPC;

import java.util.*;

public class GenericWorkorderPayment extends GenericWorkorder {

    private TableUtils tableUtils;
    private WebDriver driver;

    public GenericWorkorderPayment(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id, 'PaymentScreen:BindSummaryDV:TotalCost-inputEl')]")
    public WebElement text_InsuredPremium;

    @FindBy(xpath = "//div[contains(@id, 'PaymentScreen:BindSummaryDV:TotalPremium-inputEl')]")
    public WebElement text_TotalPremium;

    @FindBy(xpath = "//div[contains(@id, 'PaymentScreen:BindSummaryDV:TotalMembershipDues-inputEl')]")
    public WebElement text_MembershipDuesAmount;

    @FindBy(xpath = "//div[contains(@id, '_PaymentScreen:BindSummaryDV:1')]")
    public WebElement wrapperDiv_LienBills;

    @FindBy(xpath = "//span[contains(@id, 'BillingAdjustmentsDV:DownpaymentLV_tb:AddDownPaymentInstrument-btnEl') or contains(@id, ':AddPaymentInstrument-btnEl')]")
    public WebElement AddDownPaymentInstrument;

    @FindBy(xpath = "//span[contains(@id, 'BillingAdjustmentsDV:DownpaymentLV_tb:AddDownPaymentInstrument-btnInnerEl') or contains(@id, 'BillingAdjustmentsDV:PaymentInfotLV_tb:AddPaymentInstrument-btnEl')]")
    public WebElement AddPaymentInfoInstrument;

    @FindBy(xpath = "//span[contains(@id, 'DownpaymentLV_tb:checkedthign-btnInnerEl')]")
    public WebElement UseSelectedMethodForRecurring;

    @FindBy(xpath = "//input[contains(@id, 'NewDownPaymentMethodPopup:Amount-inputEl')]")
    public WebElement Amount;

    public Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//table[contains(@id,'NewDownPaymentMethodPopup:Type-triggerWrap')]");
    }

    @FindBy(xpath = "//textarea[(contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:Notes-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:PaymentInfoInputSet:Notes-inputEl'))]")
    public WebElement Notes;

    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl'))]")
    public WebElement BusinessPhone;

    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:WorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:WorkPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl'))]")
    public WebElement WorkPhone;

    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:HomePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:HomePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewDownPaymentMethodPopup:homePhone'))]")
    public WebElement HomePhone;

    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:MobilePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:MobilePhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewDownPaymentMethodPopup:mobilePhone'))]")
    public WebElement MobilePhone;

    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:FaxPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:FaxPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl')) or (contains(@id, 'NewDownPaymentMethodPopup:faxPhone'))]")
    public WebElement FaxPhone;

    public Guidewire8Select select_PrimaryPhone() {
        return new Guidewire8Select(driver, "//table[(contains(@id,'NewDownPaymentMethodPopup:primaryPhone-triggerWrap')) or (contains(@id, 'NewPaymentInfoPopup:primaryPhone-triggerWrap'))]");
    }

    @FindBy(xpath = "//div[contains(@id, 'NewDownPaymentMethodPopup:AvailableAmount-inputEl')]")
    public WebElement AvailableAmount;

    public Guidewire8Select select_CreditCardType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DownPaymentMethodInputSet:CreditCardType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:CrediCardNumber-inputEl')]")
    public WebElement CreditCardNumber;

    @FindBy(xpath = "//input[contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:CardHolderName-inputEl')]")
    public WebElement CardHolderName;

    @FindBy(xpath = "//input[contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:ExpirationDate-inputEl')]")
    public WebElement ExpirationDate;

    @FindBy(xpath = "//input[contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:CCVCode-inputEl')]")
    public WebElement CCVCode;

    @FindBy(xpath = "//div[contains(@id, 'BillingAdjustmentsInstallmentsLV-body')]/div/table")
    public WebElement table_PaymentPlans;

    @FindBy(xpath = "//div[contains(@id, 'BillingAdjustmentsDV:DownpaymentLV-body')]/div/table")
    public WebElement table_DownPayments;

    @FindBy(xpath = "//div[contains(@id, ':BillingAdjustmentsDV:DownpaymentLV')]")
    public WebElement table_DownPaymentTable;


    @FindBy(xpath = "//div[contains(@id, ':BillingAdjustmentsDV:PaymentInfotLV')]")
    public WebElement table_PaymentInfo;

    @FindBy(xpath = "//input[contains(@id, ':BankAccountType-inputEl')]")
    public WebElement label_BankAccountType;

    @FindBy(xpath = "//label[contains(text(), 'Payment Info')]")
    private List<WebElement> text_MonthlyRequiredPaymentText;

    public WebElement radio_PaymentPlan(PaymentPlanType payPlanType) {
        return find(By.xpath("//div[contains(@id, 'BillingAdjustmentsDV:PlanInputSet:InstallmentPlan:BillingAdjustmentsInstallmentsLV-body')]/div/table/descendant::tr/child::td/div[contains(text(), '" + payPlanType.getValue() + "')]/parent::td/preceding-sibling::td[contains(@class, 'radiocolumn')]/div/div"));
    }

    @FindBy(xpath = "//input[contains(@id, 'BillingAdjustmentsDV:CollectedByAgent')]")
    public WebElement editbox_DownPaymentAmount;

    @FindBy(xpath = "//label[contains(@id, 'BillingAdjustmentsDV:CollectedByAgent')]/parent::td/following-sibling::td/div")
    public WebElement label_InsuredPaymentAmount;

    @FindBy(xpath = "//*[contains(@id, 'BindSummaryDV:CreditOnAccount-labelEl')]")
    public WebElement label_CreditDebitOnAccount;

    @FindBy(xpath = "//*[contains(@id, 'BindSummaryDV:CreditOnAccount-inputEl')]")
    public WebElement text_CreditDebitOnAccountValue;

    @FindBy(xpath = "//*[contains(@id, 'BindSummaryDV:CollectionAmountOnAccount-labelEl')]")
    public WebElement label_CollectionAmountOnAccount;

    @FindBy(xpath = "//*[contains(@id, 'CollectionAmountOnAccount-inputEl')]")
    public WebElement text_CollectionAmountOnAccountValue;

    @FindBy(xpath = "//div[contains(@class, 'message')]")
    public List<WebElement> text_FailedMessage;

    @FindBy(xpath = "//span[contains(@id, ':RewriteWizard_PaymentScreen:JobWizardToolbarButtonSet:BindRewrite-btnEl')]")
    public WebElement button_issueRewrite;

    @FindBy(xpath = "//input[contains(@id, ':BillingMethod-inputEl')]")
    public WebElement billingMethod;

    @FindBy(xpath = "//label[contains(@id, ':PaymentInfoWarning')]")
    public WebElement paymentInfoWarning;


    public Guidewire8Select select_Payer() {
        return new Guidewire8Select(driver, "//table[contains(@id,':selectPayerInput-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':ACHAccountHolderInput-inputEl')]")
    public WebElement editBox_PayerACHAccountHolder;


    @FindBy(xpath = "//span[contains(@id, ':BillingAdjustmentsDV:PaymentInfotLV_tb:AddPaymentInstrument-btnInnerEl')]")
    public WebElement link_AddPaymentInfo;

    @FindBy(xpath = "//a[contains(@id, ':PolicyChangeWizard_PaymentScreen:PreviewPaymentsLink')  or contains(@id, ':BillingAdjustmentsDV:PlanInputSet:PreviewPaymentsLink')]")
    public WebElement link_PreviewPayment;

    @FindBy(xpath = "//div[@id='BillingAdjustmentPreviewPaymentsPopup:BillingAdjustmentPreviewPaymentsScreen:InvoiceItemsLV']")
    public WebElement table_PreviewPaymentsTable;

    @FindBy(xpath = "//a[contains(@id,'BillingAdjustmentPreviewPaymentsPopup:__crumb__')]")
    public WebElement link_ReturnToPayment;

    public void clickPreviewPayment() {
        
        try {
            clickWhenClickable(link_PreviewPayment);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unable to clik Preview payment on Policy change screen");
        }
        
    }

    public void clickReturnToPayment() {
        waitUntilElementIsClickable(link_ReturnToPayment);
        link_ReturnToPayment.click();
    }

    public WebElement getAccountInvoicesTable() {
        waitUntilElementIsVisible(table_PreviewPaymentsTable);
        return table_PreviewPaymentsTable;
    }

    public List<Double> getListOfInvoiceAmounts() {
        List<Double> listOfInvoiceAmounts = new ArrayList<Double>();
        List<String> listOfInvoiceAmountsAsStrings = tableUtils.getAllCellTextFromSpecificColumn(table_PreviewPaymentsTable, "Amount");
        for (int i = 0; i < listOfInvoiceAmountsAsStrings.size(); i++) {
            listOfInvoiceAmounts.add(NumberUtils.getCurrencyValueFromElement(listOfInvoiceAmountsAsStrings.get(i)));
        }
        return listOfInvoiceAmounts;
    }

    public String getFailMessage() {
        if (!text_FailedMessage.isEmpty()) {
            return text_FailedMessage.get(0).getText();
        } else {
            return "";
        }

    }


    public boolean isBillingMethodEditable() {
        return checkIfElementExists(billingMethod, 1000);
    }


    public double getDownPaymentAmount() {
        
        waitUntilElementIsVisible(editbox_DownPaymentAmount);
        String strAmount = editbox_DownPaymentAmount.getAttribute("value");

        return Double.valueOf(strAmount);
    }

    public double getInsuredPaymentAmount() {
        
        waitUntilElementIsVisible(label_InsuredPaymentAmount);
        String strAmount = label_InsuredPaymentAmount.getAttribute("textContent");
        strAmount = strAmount.replace("$", "");
        strAmount = strAmount.replace(",", "");
        return Double.valueOf(strAmount);
    }


    public boolean checkQuarterlyPaymentPlanExists() {
        try {
            waitUntilElementIsVisible(radio_PaymentPlan(PaymentPlanType.Quarterly));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void clickPaymentPlan(PaymentPlanType payPlanType) {
        clickWhenClickable(radio_PaymentPlan(payPlanType));
    }


    public void clickAddDownPayment() {
        waitUntilElementIsClickable(AddDownPaymentInstrument);
        long endTime = new Date().getTime() + (30 * 1000);
        do {
            clickWhenClickable(AddDownPaymentInstrument);
            
        }
        while (finds(By.xpath("//input[contains(@id, 'NewDownPaymentMethodPopup:Amount-inputEl')]")).size() <= 0 && new Date().getTime() < endTime);
    }


    public void clickAddPaymentInfo() {
        clickWhenClickable(AddPaymentInfoInstrument);
        
        if (checkIfElementExists(AddPaymentInfoInstrument, 1)) {
            AddPaymentInfoInstrument.click();
        }
    }


    public void setDownPaymentAmount(double amount) {
        waitUntilElementIsVisible(Amount);
        Amount.sendKeys("" + amount);
        clickProductLogo();
    }


    public void setDownPaymentToStillOwing() {
        setDownPaymentAmount(NumberUtils.getCurrencyValueFromElement(AvailableAmount));
    }


    @FindBy(css = "input[id$=':BankABANumber-inputEl'], input[id$='routingNumberInput-inputEl']")
    private WebElement editBox_RoutingNumber;

    @FindBy(css = "*[id$=':BankInfo-inputEl']")
    private WebElement text_BankAddress;

    @FindBy(css = "input[id$=':BankAccountNumber-inputEl']")
    private WebElement input_AccountNumber;

    public Guidewire8Select select_BankAccountType() {

        return new Guidewire8Select(driver, "//table[contains(@id, ':BankAccountType-triggerWrap') or contains(@id,':accountTypeDropdown-triggerWrap')]");
    }

    public void setPaymentInfo(BankAccountInfo bankAccountInfo) {

        // Moving payment info over.
        String bankCityStateZip = null;

        clickWhenClickable(label_BankAccountType);
        select_BankAccountType().selectByVisibleText(bankAccountInfo.getBankAccountType().getValue());

        setText(editBox_RoutingNumber, bankAccountInfo.getRoutingNumber());
        waitUntilElementIsVisible(text_BankAddress);

        int i = 1;
        Scanner scanner = new Scanner(text_BankAddress.getText());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (i == 1) {
                bankAccountInfo.setBankName(line);
            } else if (i == 2) {
                bankAccountInfo.setBankAddress(line);
            } else if (i == 3) {
                bankCityStateZip = line;
            }
            i++;
        }
        scanner.close();

        String[] cityStateZipSplit = StringsUtils.cityStateZipParser(bankCityStateZip);
        bankAccountInfo.setBankCity(cityStateZipSplit[0]);
        bankAccountInfo.setBankState(State.valueOfAbbreviation(cityStateZipSplit[1]));
        bankAccountInfo.setBankZip(cityStateZipSplit[2]);

        setText(input_AccountNumber, bankAccountInfo.getAccountNumber());

        // end of payment info move over

        String payer = "";
        if (bankAccountInfo.usePNIAsPaymentPayer()) {
            InfoBar infoBar = new InfoBar(getDriver());
            payer = infoBar.getInfoBarAccountName();
        } else {
            payer = bankAccountInfo.getPaymentPayer();
        }
        setPayer(payer);
        setPaymentEmailInfo();
        setPaymentPhoneInfo();
    }


    public void clickAddPaymentInfoButton() {
        clickWhenClickable(link_AddPaymentInfo);
        
    }


    public void setDownPaymentType(PaymentType type) {
        
        select_Type().selectByVisibleText(type.getValue());
        long endTime = new Date().getTime() + 5000;
        switch (type) {
            case ACH_EFT:
                do {
                    
                }
                while (finds(By.xpath("//label[text()='Bank Account Type']")).isEmpty() && new Date().getTime() < endTime);
                setACHEFTPayment();
                break;
            case Credit_Debit:
                do {
                    
                }
                while (finds(By.xpath("//label[text()='Card Information']")).isEmpty() && new Date().getTime() < endTime);
                setCreditDebitPayment();
                break;
            case Intercompany_Transfer:
                do {
                    
                }
                while (finds(By.xpath("//input[contains(@id, 'NewDownPaymentMethodPopup:policyNumber-inputEl')]")).isEmpty() && new Date().getTime() < endTime);
                setInterCompanyTransferAccount();
                break;
            default:
                break;
        }
        
    }


    private void setInterCompanyTransferAccount() {
        setText(find(By.xpath("//input[contains(@id, 'NewDownPaymentMethodPopup:policyNumber-inputEl')]")), "12-123456-12");
    }

    private void setACHEFTPayment() {
        select_BankAccountType().selectByVisibleTextRandom();
        if (select_Payer().selectByVisibleTextRandom().equals("other")) {
            setACHAccountHolder("Not Other");
        }
        verifyRoutingNumberReturnsBankInfo();
        setText(input_AccountNumber, StringsUtils.generateRandomNumberDigits(10));
        setPaymentEmailInfo();
        setPaymentPhoneInfo();
    }


    public void setPayer(String payer) {
        Guidewire8Select mySelect = select_Payer();
        List<String> payerOptionsList = mySelect.getList();
        clickProductLogo();
        
        if (payerOptionsList.contains(payer)) {
            mySelect.selectByVisibleTextPartial(payer);
        } else {
            mySelect.selectByVisibleText("other");
            
            setACHAccountHolder(payer);
        }
    }

    private void setACHAccountHolder(String holderName) {
        clickWhenClickable(editBox_PayerACHAccountHolder);
        setText(editBox_PayerACHAccountHolder, holderName);
    }

    private void verifyRoutingNumberReturnsBankInfo() {
        clickWhenClickable(editBox_RoutingNumber);
        setText(editBox_RoutingNumber, "324079555");
        clickWhenClickable(input_AccountNumber);
        new WaitUtils(driver).waitForPostBack();
        waitUntilElementIsVisible(text_BankAddress);
        String bankName = text_BankAddress.getText();
        if (!bankName.contains("MOUNTAIN AMERICA FCU") || !bankName.contains("7181 SOUTH CAMPUS VIEW DRIVE") || !bankName.contains("WEST JORDAN, UT 84084-0000")) {
            Assert.fail("The Bank Name and Address presented after inputing the Mountain America Routing Number did not match expected values.");
        }
    }

    private void setCreditDebitPayment() {
        Guidewire8Select mySelect = select_CreditCardType();
        
        mySelect.selectByVisibleTextRandom();
        

        String creditCardText = select_CreditCardType().getText();

        waitUntilElementIsVisible(CreditCardNumber);
        CreditCardNumber.sendKeys(Keys.HOME);


        CCType ccType = null;
        switch (creditCardText.toLowerCase()) {
            case "visa":
                ccType = CCType.Visa;
                break;
            case "mastercard":
                ccType = CCType.Mastercard;
                break;
            case "amex":
                ccType = CCType.Amex;
                break;
            case "discover":
                ccType = CCType.Discover;
                break;
        }
        CreditCardNumber.sendKeys(CCUtils.generateOneCardNumber(ccType));

        waitUntilElementIsVisible(CardHolderName);
        CardHolderName.sendKeys("Random Person");

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        waitUntilElementIsVisible(ExpirationDate);
        ExpirationDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        ExpirationDate.sendKeys(DateUtils.dateFormatAsString("MM/yy", DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, 5)));
        clickProductLogo();

        waitUntilElementIsVisible(CCVCode);
        CCVCode.sendKeys(Keys.HOME);
        CCVCode.sendKeys(StringsUtils.generateRandomNumberDigits(3));

        setPaymentEmailInfo();
        setPaymentPhoneInfo();
    }

    private void setPaymentEmailInfo() {
        if (emailAddress.getAttribute("value").equals("")) {
            waitUntilElementIsVisible(emailAddress);
            emailAddress.sendKeys(Keys.chord(Keys.CONTROL + "a"));
            emailAddress.sendKeys("none@none.com");
        }
    }


    public void setPaymentPhoneInfo() {
        if (BusinessPhone.getAttribute("value").equals("") &&
                WorkPhone.getAttribute("value").equals("") &&
                HomePhone.getAttribute("value").equals("") &&
                MobilePhone.getAttribute("value").equals("") &&
                FaxPhone.getAttribute("value").equals("")) {

            String primaryPhoneText = select_PrimaryPhone().getText();
            String orginalPhoneText = primaryPhoneText;
            select_PrimaryPhone().selectByVisibleTextRandom();
            

            primaryPhoneText = select_PrimaryPhone().getText();
            if (!primaryPhoneText.equalsIgnoreCase(orginalPhoneText)) {
                String phone = "208-" + StringsUtils.generateRandomNumberDigits(3) + "-" + StringsUtils.generateRandomNumberDigits(4);
                switch (primaryPhoneText.toLowerCase()) {
                    case "work":
                        WorkPhone.sendKeys(Keys.HOME, phone);
                        break;
                    case "home":
                        HomePhone.sendKeys(Keys.HOME, phone);
                        break;
                    case "mobile":
                        MobilePhone.sendKeys(Keys.HOME, phone);
                        break;
                    case "business":
                        BusinessPhone.sendKeys(Keys.HOME, phone);
                        break;
                }
            }
        }
    }


    public void clickOk() {
        super.clickUpdate();
        
    }


    public void clickCancel(String type) {
        super.clickGenericWorkorderCancel();
    }


    public void clickPolicyChangeNext() {
        super.clickPolicyChangeNext();
    }


    public boolean SubmitOnly() {
    	clickProductLogo();
        clickGenericWorkorderSubmitOptionsSubmitOnly();
        return !finds(By.xpath("//span[contains(@id, 'JobComplete:JobCompleteScreen:ttlBar')]")).isEmpty();
    }


    public void IssueOnly() {

        long endTime = new Date().getTime() + (60 * 1000);
        do {
            clickIssuePolicyButton();
        }
        while (finds(By.xpath("//span[contains(@id, 'JobComplete:JobCompleteScreen:ttlBar')]")).size() <= 0 && new Date().getTime() < endTime);

    }


    public void issueRewrite() {
        clickWhenClickable(button_issueRewrite);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public int getDownPaymentRowCount() {
        int downPaymentRowCount = tableUtils.getRowCount(table_DownPayments);
        waitForPostBack();
        return downPaymentRowCount;
    }


    public void removeFirstDownPayment() {
        List<WebElement> downPaymentList = table_DownPayments.findElements(By.xpath(".//tbody/child::tr"));
        if (downPaymentList.size() > 0) {
            downPaymentList.get(0).findElement(By.xpath(".//td/div/img[contains(@class, '-checkcolumn')]")).click();
        }
        if (downPaymentList.get(0).findElements(By.xpath(".//td/div/img[contains(@class, '-checkcolumn-checked')]")).size() > 0) {
            clickRemove();
        }
    }

    public void removePaymentInfo() {
    	scrollToElement(find(By.xpath("//div[contains(@id, ':BillingAdjustmentsDV:PaymentInfotLV')]/child::div/div/div/div/div/span/div")));
    	clickWhenClickable(find(By.xpath("//div[contains(@id, ':BillingAdjustmentsDV:PaymentInfotLV')]/child::div/div/div/div/div/span/div")));
        
        clickWhenClickable(find(By.xpath("//span[contains(@id, ':PaymentInfotLV_tb:Remove-btnEl')]")));
    	
    }


    public void clickDownPaymentByRow(int rowNum) {
        tableUtils.clickLinkInSpecficRowInTable(table_DownPayments, rowNum);
    }


    public String getDownPaymentRowTextbyRow(int rowNum, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_DownPaymentTable, rowNum, columnName);
    }


    public String getPaymentInfoRowTextbyRow(int rowNum, String columnName) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PaymentInfo, rowNum, columnName);
    }


    public void clickPaymentInfoByRow(int rowNum) {
        tableUtils.clickLinkInSpecficRowInTable(table_PaymentInfo, rowNum);
    }


    public void verifyMonthlyRequiredPaymentText() throws Exception {
        if (text_MonthlyRequiredPaymentText.size() == 1) {
            if (text_MonthlyRequiredPaymentText.get(0).getText().contains("(At Least One Required)")) {
                throw new Exception("ERROR: (At Least one Required) is Present");
            }
        } else {
            if (text_MonthlyRequiredPaymentText.size() != 1) {
                throw new Exception("ERROR: Monthly Payment Option is not Present");
            }
        }
    }


    public String getCollectionAmount() {
        waitUntilElementIsVisible(label_CreditDebitOnAccount);
        waitUntilElementIsVisible(text_CreditDebitOnAccountValue);
        waitUntilElementIsVisible(label_CollectionAmountOnAccount);
        waitUntilElementIsVisible(text_CollectionAmountOnAccountValue);
        return text_CollectionAmountOnAccountValue.getText();
    }

    
    public void copyDownPaymentACHInfoToPaymentInfo() {
        List<WebElement> downPaymentList = table_DownPayments.findElements(By.xpath(".//tbody/child::tr"));
        if (downPaymentList.size() > 0) {
            downPaymentList.get(0).findElement(By.xpath(".//td/div/img[contains(@class, '-checkcolumn')]")).click();
        }
        try{
            clickWhenClickable(UseSelectedMethodForRecurring);
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail(" Use Selected Method For Recurring button is missing or not available to click, please investigate");
        }

    }


    public void makeDownPayment(PaymentPlanType paymentPlanType, PaymentType paymentType, double downPayment) throws Exception {
        if (paymentPlanType != null) {
            clickPaymentPlan(paymentPlanType);
            if (paymentPlanType == PaymentPlanType.Monthly) {
                verifyMonthlyRequiredPaymentText();
            }
        } else {
            clickPaymentPlan(PaymentPlanType.Annual);
        }
        
        clickAddDownPayment();
        if (downPayment > 0.0) {
            setDownPaymentAmount(downPayment);
        } else {
            setDownPaymentToStillOwing();
        }
        
        if (paymentType != null) {
            setDownPaymentType(paymentType);
        } else {
            setDownPaymentType(PaymentType.Cash);
        }
        
        clickOk();
        
    }


    public double getMembershipDuesAmount() {
        
        double membershipDuesAmount = 0.00;
        if (checkIfElementExists(text_MembershipDuesAmount, 1000)) {
            membershipDuesAmount = NumberUtils.getCurrencyValueFromElement(text_MembershipDuesAmount);
        }
        return membershipDuesAmount;
    }


    public double getTotalInsuredPremiumPortion() {
        double policyPremium = 0.00;
        if (checkIfElementExists(text_InsuredPremium, 1000)) {
            policyPremium = NumberUtils.getCurrencyValueFromElement(text_InsuredPremium);
        }
        return policyPremium;
    }


    public double getTotalPremiumPortion() {
        
        double policyPremium = 0.00;
        if (checkIfElementExists(text_TotalPremium, 1000)) {
            policyPremium = NumberUtils.getCurrencyValueFromElement(text_TotalPremium);
        }
        return policyPremium;
    }


    public double getAdditionalInterestPremiumPortion(ArrayList<PolicyLocation> locationList) {
        double toReturn = 0;

        if (checkIfElementExists(wrapperDiv_LienBills, 2000)) {
            List<WebElement> allRows = tableUtils.getAllTableRows(wrapperDiv_LienBills);

            for (WebElement row : allRows) {
                toReturn = toReturn + NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(wrapperDiv_LienBills, row, "Amount"));
            }//end for

            for (PolicyLocation loc : locationList) {
                if (!loc.getBuildingList().isEmpty()) { //For BOP Buildings List
                    for (PolicyLocationBuilding bldg : loc.getBuildingList()) {
                        if (bldg.getAdditionalInterestList().size() > 0) {
                            for (AdditionalInterest ai : bldg.getAdditionalInterestList()) {
                                if (ai.getAdditionalInterestBilling().equals(AdditionalInterestBilling.Bill_Lienholder) || ai.getAdditionalInterestBilling().equals(AdditionalInterestBilling.Bill_All)) {
                                    String aiName = ai.getLienholderNameFromPolicyCenter();
                                    String aiAccountNumber = ai.getLienholderNumber();
                                    String aiLoanNumber = ai.getLoanContractNumber();
                                    String locAddress = loc.getFullAddressString();

                                    HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

                                    columnRowKeyValuePairs.put("Additional Interest Bill(s)", aiName);
                                    columnRowKeyValuePairs.put("Lienholder Account #", aiAccountNumber);
                                    columnRowKeyValuePairs.put("Loan #", aiLoanNumber);
                                    columnRowKeyValuePairs.put("Building / Property Address", locAddress);

                                    List<WebElement> foundRows = tableUtils.getRowsInTableByColumnsAndValues(wrapperDiv_LienBills, columnRowKeyValuePairs);

                                    double toSet = 0;
                                    for (WebElement foundRow : foundRows) {
                                        toSet = toSet + NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(wrapperDiv_LienBills, foundRow, "Amount"));
                                    }//end for

                                    ai.setAdditionalInterestPremiumAmount(toSet);
                                }//end if
                            }//end for
                        }//end if
                    }//end for
                } else { // For PL Buildings List
                    for (PLPolicyLocationProperty bldg : loc.getPropertyList()) {
                        if (bldg.getBuildingAdditionalInterest().size() > 0) {
                            for (AdditionalInterest ai : bldg.getBuildingAdditionalInterest()) {
                                if (ai.getAdditionalInterestBilling().equals(AdditionalInterestBilling.Bill_Lienholder) || ai.getAdditionalInterestBilling().equals(AdditionalInterestBilling.Bill_All)) {
                                    String aiName = ai.getLienholderNameFromPolicyCenter();
                                    String aiAccountNumber = ai.getLienholderNumber();
                                    String aiLoanNumber = ai.getLoanContractNumber();
                                    String locAddress = loc.getFullAddressString();

                                    HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

                                    columnRowKeyValuePairs.put("Additional Interest Bill(s)", aiName);
                                    columnRowKeyValuePairs.put("Lienholder Account #", aiAccountNumber);
                                    columnRowKeyValuePairs.put("Loan #", aiLoanNumber);
                                    columnRowKeyValuePairs.put("Building / Property Address", locAddress);

                                    List<WebElement> foundRows = tableUtils.getRowsInTableByColumnsAndValues(wrapperDiv_LienBills, columnRowKeyValuePairs);

                                    double toSet = 0;
                                    for (WebElement foundRow : foundRows) {
                                        toSet = toSet + NumberUtils.getCurrencyValueFromElement(tableUtils.getCellTextInTableByRowAndColumnName(wrapperDiv_LienBills, foundRow, "Amount"));
                                    }//end for

                                    ai.setAdditionalInterestPremiumAmount(toSet);
                                }//end if
                            }//end for
                        }//end if
                    }//end for
                }//end else
            }//END FOR
        }//END IF

        return toReturn;
    }

    public void fillOutPaymentPage(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenuStuff = new SideMenuPC(getDriver());
        sideMenuStuff.clickSideMenuPayment();

        //The following if statement is designed to set the insured premium ONLY for policy types that don't have payer assignment possibilities. Please do not add policy types that have payer assignment possibilities here.
//      if ((policy.productType.equals(ProductLineType.CPP)) || (policy.productType.equals(ProductLineType.StandardFL)) || (policy.productType.equals(ProductLineType.StandardIM))) {
        if ((policy.productType.equals(ProductLineType.CPP)) || (policy.productType.equals(ProductLineType.StandardFire)) || (policy.productType.equals(ProductLineType.StandardLiability)) || (policy.productType.equals(ProductLineType.StandardIM) || (policy.productType.equals(ProductLineType.Squire)))) {
            new GuidewireHelpers(getDriver()).getPolicyPremium(policy).setInsuredPremium(getTotalInsuredPremiumPortion());
        }

        if (policy.productType.equals(ProductLineType.Membership)) {
            if (checkQuarterlyPaymentPlanExists() == true) {
                throw new Exception("The Quarterly Payment Plan option was displayed on the payment page, but the Policy was a Membership Only Policy. This policy type should only allow Annual Payment plans.");
            }
        }

        if (new GuidewireHelpers(getDriver()).getPolicyPremium(policy).getInsuredPremium() > 150) {
            if (!policy.productType.equals(ProductLineType.PersonalUmbrella)) {
                clickPaymentPlan(policy.paymentPlanType);
            }
        } else {
            if (policy.productType.equals(ProductLineType.Squire) || (policy.productType.equals(ProductLineType.CPP)) || (policy.productType.equals(ProductLineType.Businessowners))) {
                if (checkQuarterlyPaymentPlanExists() == true) {
                    throw new Exception("The Quarterly Payment Plan option was displayed on the payment page, but the insured premium was " + new GuidewireHelpers(getDriver()).getPolicyPremium(policy).getInsuredPremium() + ". This should not be possible.");
                }
            }
            policy.paymentPlanType = PaymentPlanType.Annual;
        }//END ELSE
        if ((new GuidewireHelpers(getDriver()).getPolicyPremium(policy).getInsuredPremium() > 0) || (policy.productType.equals(ProductLineType.Membership))) {
            double downPaymentAmountToPay = getDownPaymentAmount();
            new GuidewireHelpers(getDriver()).getPolicyPremium(policy).setDownPaymentAmount(downPaymentAmountToPay);

            clickAddDownPayment();
            
            setDownPaymentAmount(downPaymentAmountToPay);
            
            setDownPaymentType(policy.downPaymentType);
         
            clickOk();

            // fail generate if it failed to process CC payment
            switch (getFailMessage()) {
                case "Failed to process this payment!":
                    Assert.fail("Failed to process payment. " + policy.accountNumber);
                    break;
                case "Exp Date : Card has already expired":
                    Assert.fail("Failed to process payment. Credit Card Expired or system Clock too far out of date. "
                            + policy.accountNumber);
                    break;
            }//END SWITCH
        }//END IF

        if (policy.paymentPlanType.equals(PaymentPlanType.Monthly) && !policy.productType.equals(ProductLineType.PersonalUmbrella)) {
            if(policy.downPaymentType.equals(PaymentType.ACH_EFT)){
                copyDownPaymentACHInfoToPaymentInfo();
                Assert.assertTrue(table_PaymentInfo.findElements(By.xpath(".//tbody/child::tr")).size()==1,"Failed to copy down payment ACH info to recurring payment info");
            } else {
                clickAddPaymentInfoButton();
                if (policy.bankAccountInfo != null) {
                    setPaymentInfo(policy.bankAccountInfo);
                } else {
                    setPaymentInfo(new BankAccountInfo());
                }
                clickOk();
            }

            // fail generate if it failed to process CC payment
            if (getFailMessage().contains("Failed to process this payment!")) {
                Assert.fail("Failed to process payment. " + policy.accountNumber);
            }
        }//END IF
    }//END fillOutPaymentPage()


    public void clickGenericWorkorderSubmitOptionsIssuePolicy() {
    	clickProductLogo();
        waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsArrow).click();
        waitUntilElementIsClickable(button_GenericWorkorderSubmitOptionsIssuePolicy).click();
        selectOKOrCancelFromPopup(OkCancel.OK);
        
    }


    public boolean verifySquirePaymentPlanExistsInUmbrella(PaymentPlanType paymentPlanType) {
        return checkIfElementExists("//div[contains(@id, 'BillingAdjustmentsDV:PlanInputSet:InstallmentPlan:BillingAdjustmentsInstallmentsLV-body')]/div/table/descendant::tr/child::td/div[contains(text(), '" + paymentPlanType + "')]/parent::td/preceding-sibling::td[contains(@class, 'gridcolumn')]/div/a[contains(text(),'X')]", 2000);

    }


    public boolean checkPaymentWarningDisplayed() {
        return checkIfElementExists(paymentInfoWarning, 2000);
    }


    public String getPaymentWarningMessage() {
        return paymentInfoWarning.getText();
    }

	public boolean canSubmit() {
		clickWhenClickable(button_GenericWorkorderSubmitOptionsArrow);
		return finds(By.xpath("//span[contains(@id, ':BindOptions:BindAndIssue-textEl')]")).isEmpty();
	}
}










