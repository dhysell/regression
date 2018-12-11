package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.cc.claim.searchpages.SearchAddressBook;
import repository.cc.entities.CheckLineItem;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.math.BigDecimal;
import java.util.List;


public class CheckWizard extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public CheckWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//div[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body']/div/table")
    private WebElement table_CheckLineItems;

    @FindBy(xpath = "//a[@id='NormalCreateCheckWizard:Next' or @id='ManualCreateCheckWizard:Next' or @id='EditCheckWizard:Next']")
    private WebElement button_Next;

    @FindBy(xpath = "//a[@id='NormalCreateCheckWizard:Finish' or @id='ManualCreateCheckWizard:Finish' or @id='EditCheckWizard:Finish']")
    private WebElement button_Finish;

    @FindBy(xpath = "//div[contains(@id,'Transaction_AvailableReserves')]")
    private WebElement element_ReserveAmount;

    @FindBy(xpath = "//span[@id='WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton-btnInnerEl']")
    private WebElement button_Clear;

    @FindBy(xpath = "//a[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV_tb:Add']")
    private WebElement button_AddItem;

    @FindBy(xpath = "//a[@id='NormalCreateCheckWizard:Prev' or @id='ManualCreateCheckWizard:Prev']")
    private WebElement button_Back;

    @FindBy(xpath = "//a[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV_tb:Remove' or @id='ManualCreateCheckWizard:ManualCheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV_tb:Remove']")
    private WebElement button_Remove;

    @FindBy(xpath = "//a[@id='ClaimFinancialsChecksDetail:ClaimFinancialsChecksDetailScreen:ClaimFinancialsChecksDetail_EditButton']")
    private WebElement button_Edit;

    @FindBy(xpath = "//a[contains(@id,':AddJointPayees')]")
    private WebElement button_AddJointPayees;

    @FindBy(xpath = "//span[contains(text(),'Search')]")
    private WebElement link_Search;

    @FindBy(xpath = "//a[contains(@id,':PrimaryPayee_Name:PrimaryPayee_NameMenuIcon')]")
    private WebElement link_PrimaryNamePicker;

    @FindBy(xpath = "//span[contains(text(),'View Contact Details')]")
    private WebElement link_ContactDetails;


    public EditPerson clickContactDetails() {
        clickWhenClickable(link_ContactDetails);
        return new EditPerson(this.driver);
    }


    public void clickPrimaryNamePicker() {
        clickWhenClickable(link_PrimaryNamePicker);
    }


    public void clickSearchLink() {
        clickWhenClickable(link_Search);
    }


    public void clickAddJointPayees() {
        clickWhenClickable(button_AddJointPayees);
    }


    public void clickEditButton() {
        clickWhenClickable(button_Edit);
    }

//	
//	public void abilityToPay() {
//
//		ActionsMenu actionsMenu = new ActionsMenu(this.driver);
//
//		boolean validateClicked = false;
//		int numTries = 0;
//		while (!validateClicked && numTries < 25) {
//
//			
//			find(By.xpath("//span[contains(text(),'Loss Type:')]")).click();
//			numTries++;
//			actionsMenu.clickActionsButton();
//			
//
//			try {
//				actionsMenu.clickValidateClaimExposures();
//				
//				actionsMenu.clickValidateClaimExposures();
//				if (driver
//						.findElement(
//								By.id("Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_ClaimExposureValidation"))
//						.getAttribute("class").contains("x-menu-item-active")) {
//					List<WebElement> list = finds(By.xpath(
//							"//a[contains(@id, 'Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_ClaimExposureValidation:')]"));
//					if (list.size() > 0) {
//						
//						actionsMenu.clickAbilityToPay();
//						
//					}
//				}
//			} catch (Exception e) {
//				driver.manage().window().maximize();
//				
//				find(By.xpath("//span[contains(text(),'Loss Type:')]")).click();
//				
//			}
//
//			List<WebElement> messages = finds(By.xpath("//div[@class='message']"));
//			if (messages.size() > 0) {
//				validateClicked = true;
//			}
//
//		}
//
//		List<WebElement> messageElements = driver
//				.findElements(By.xpath("//div[@class='message']"));
//		List<String> errorMessages = new ArrayList<String>();
//
//		for (WebElement messageElement : messageElements) {
//			errorMessages.add(messageElement.getText());
//		}
//
//		for (String message : errorMessages) {
//			if (message
//					.equalsIgnoreCase("The claim's fault rating is either not selected, undetermined, or unknown.")) {
//				WebElement lossDetailsLink = driver
//						.findElement(By.xpath("//a[contains(text(),'On \"Loss Details\":')]"));
//				lossDetailsLink.click();
//
//				LossDetails lossDetails = new LossDetails(this.driver);
//
//				lossDetails.clickEditButton();
//				lossDetails.selectFaultRating();
//				
//				lossDetails.clickUpdateButton();
//
//			} else if (message
//					.equalsIgnoreCase("The claimant's primary address must have a street, city and state")) {
//				WebElement contactsLink = driver
//						.findElement(By.xpath("//a[contains(text(),'On \"Contacts\":')]"));
//				contactsLink.click();
//				
//				PartiesInvolvedContacts contacts = new PartiesInvolvedContacts(this.driver);
//				
//				WebElement clearButton = find(By.xpath("//a[contains(@text(),'Clear')]"));
//				clearButton.click();
//
//				contacts.setAddressInfo();
//			}
//		}
//
//	}


    public void approveChecks(String claimNumber) {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        
        sideMenu.clickWorkplanLink();

        WorkplanCC workPlan = new WorkplanCC(this.driver);
        String activityName = "Review and approve new payment";

        workPlan.completeActivityAsPersonAssignedTo(activityName, claimNumber);


    }


    public void click_FinishButton() {
        clickWhenClickable(button_Finish);
    }


    public void clickNextButton() {
        waitUntilElementIsClickable(button_Next);
        button_Next.click();
        waitUtils.waitUntilElementIsNotVisible(By.cssSelector("input[id*='SpecialHandling']"));
    }


    public void clickAddItemButton() {
        clickWhenClickable(button_AddItem);
    }


    public void clickClearButton() {
        clickWhenClickable(button_Clear);
    }


    public void clickBackButton() {
        clickWhenClickable(button_Back);
    }


    public void clickRemoveButton() {
        clickWhenClickable(button_Remove);
    }

    // TODO

    public void populateLineItems(CheckLineItem lineItem) {

        waitUntilElementIsVisible(table_CheckLineItems, 15);

        By lastRowLocator = By.xpath("//div[contains(@id,':NewPaymentDetailDV:EditablePaymentLineItemsLV-body')]//tr[not(contains(@id, 'summary-record'))][last()]");
        String lastLineAmount = find(lastRowLocator).findElement(By.xpath("//td[5]")).getText();
        By listSelector = By.cssSelector("div:not([style*='display: none']):not([role='presentation']) ul");
        // Add a new line item if current is not blank.
        if (!lastLineAmount.contains("")) {
            clickAddItemButton();
        }

        // Click check type to bring up the select list options.
        clickWhenClickable(find(lastRowLocator).findElement(By.xpath(".//td[2]")));

        try {
            waitUntilElementIsVisible(listSelector, 10);
            clickWhenClickable(find(listSelector).findElement(By.xpath("//li[contains(text(),'" + lineItem.getType().getTextDescription() + "')]")));
            waitForPostBack();
        } catch (Exception e) {
            systemOut("This element is doodoo.");
        }

        if (lineItem.getType().hasCategory()) {

            // Click the category type  to bring up the select list options.

            //new WebDriverWait(driver, 60).until(ExpectedConditions.not(ExpectedConditions.attributeToBe(By.cssSelector("body"), "class", "x-mask")));
            clickWhenClickable(find(lastRowLocator).findElement(By.xpath(".//td[3]")));
            waitForPostBack();
            waitUntilElementIsVisible(listSelector, 10);
            clickWhenClickable(find(listSelector).findElement(By.xpath("//li[contains(text(),'" + lineItem.getType().getTextDescription() + "')]")));
            sendArbitraryKeys(Keys.ESCAPE);
            waitForPostBack();
            //waitUntilElementIsNotVisible(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"), 60);
        }

        clickWhenClickable(find(lastRowLocator).findElement(By.xpath(".//td[5]")));
        setAmount(lineItem.getAmount().toString());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        //waitUntilElementIsNotVisible(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"), 60);
    }

    @FindBy(xpath = "//input[@name='Amount']")
    private WebElement inputAmount;

    private void setAmount(String amount) {
        waitUntilElementIsClickable(inputAmount);
        inputAmount.clear();
        waitUntilElementIsClickable(inputAmount).sendKeys(amount);
    }

    private String extractTableXpathFromElement(WebElement gwSelectBox) {
        String xpath = gwSelectBox.getAttribute("id");
        String tableXpath = "//table[starts-with(@id,'" + xpath + "')]";
        return tableXpath;
    }


    public void lineItemPopulate(String type, String category, String amount, int rowNum) {

        int maxID = -1;
        int currentID;
        String tableID = null;
        String tempID;

        for (int i = 2; i < 4; i++) {
            clickWhenClickable(driver.findElement(By.xpath("//div[contains(@id,':NewPaymentDetailDV:EditablePaymentLineItemsLV-body']" +
                    "/div/table/tbody/tr[" + rowNum + "]/td[" + i + ")]/div")));

            List<WebElement> hiddenComboBoxs = driver.findElements(By.xpath("//table[starts-with(@id,'simplecombo-')]"));

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
                selectLineItemBox(tableXpath).clear();
                
                selectLineItemBox(tableXpath).selectByVisibleText(type);
                
            } else {
                try {
                    selectLineItemBox(tableXpath).clear();
                    
                    selectLineItemBox(tableXpath).selectByVisibleText(category);
                    
                } catch (Exception e) {

                }
            }
        }

        String rawReserveString = driver
                .findElement(By.xpath("//div[contains(@id,'Transaction_AvailableReserves')]")).getText();

        String wholeNum = rawReserveString.replace("$", "");
        String[] parseData = wholeNum.split("\\.");
        String numsOnly = parseData[0].replaceAll("[^a-zA-Z0-9]", "");

        

        // Used for supplemental payments where reserves do not exits.
        if (numsOnly.isEmpty() || numsOnly.equalsIgnoreCase("")) {
            numsOnly = "1000";
        }

        int amountLimit = Integer.parseInt(numsOnly);
        String checkAmount;
        if (amount == null) {
            checkAmount = Integer.toString(NumberUtils.generateRandomNumberInt(1, amountLimit));
        } else {
            checkAmount = amount;
            if (amountLimit < Integer.parseInt(checkAmount)) {
                checkAmount = Integer.toString(NumberUtils.generateRandomNumberInt(1, amountLimit));
            }
        }
        clickWhenClickable(driver
                .findElement(By
                        .xpath("//div[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:EditablePaymentLineItemsLV-body']/div/table/tbody/tr["
                                + rowNum + "]/td[5]/div")));

        

        WebElement editBox_Amount = find(By.xpath("//input[@name='Amount']"));
        editBox_Amount.sendKeys(checkAmount);
        
        editBox_Amount.sendKeys(Keys.TAB);

        
    }

    public Guidewire8Select select_CheckDelivery() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:DeliveryMethod-triggerWrap']");
    }

    public Guidewire8Select select_CheckFieldOrDraft() {
        return new Guidewire8Select(
                driver, "//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckType-triggerWrap']");
    }

    public Guidewire8Select select_CheckType() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Check_CheckType-triggerWrap']");
    }

    public Guidewire8Select select_CoverageType() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-triggerWrap']");
    }

    public Guidewire8Select select_LineItemsCategory(String textString) {
        return new Guidewire8Select(driver, "" + textString + "");
    }

    public Guidewire8Select select_EditCheckDelivery() {
        return new Guidewire8Select(
                driver, "//table[@id='EditCheckWizard:EditCheckWizard_CheckPayeesScreen:EditCheckPayeeDV:DeliveryMethod-triggerWrap']");
    }

    public Guidewire8Select selectLineItemBox(String textString) {
        return new Guidewire8Select(driver, "" + textString + "");
    }

    public Guidewire8Select select_ManualCheckType() {
        return new Guidewire8Select(
                driver, "//table[@id='ManualCreateCheckWizard:ManualCheckWizard_CheckPayeesScreen:NewManualCheckPayeeDV:Check_CheckType-triggerWrap']");
    }

    public Guidewire8Select select_PaymentType() {
        return new Guidewire8Select(
                driver, "//table[contains(@id, ':Payment_PaymentType-triggerWrap')]");
    }


    public void setcheckType(String checkType) {
        select_CheckType().selectByVisibleText(checkType);
    }


    public void setcheckTypeRandom() {
        select_CheckType().selectByVisibleTextRandom();
    }


    public void setPaymentType(String string) {
        select_PaymentType().selectByVisibleText(string);

    }


    public void setRandomPaymentType() {
        select_PaymentType().selectByVisibleTextRandom();

    }

    public Guidewire8Select select_PrimaryPayeeName() {
        return new Guidewire8Select(
                driver, "//table[contains(@id,':PrimaryPayee_Name-triggerWrap')]");
    }

    public Guidewire8Select select_PrimaryPayeeType() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:PrimaryPayee_Type-triggerWrap']");
    }

    public Guidewire8Select select_ReportAs() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPayeesScreen:NewCheckPayeeDV:Reportability-triggerWrap' or @id='EditCheckWizard:EditCheckWizard_CheckPayeesScreen:EditCheckPayeeDV:Reportability-triggerWrap']");
    }

    public Guidewire8Select select_ReserveLine() {
        return new Guidewire8Select(
                driver, "//table[@id='NormalCreateCheckWizard:CheckWizard_CheckPaymentsScreen:NewCheckPaymentPanelSet:NewPaymentDetailDV:ReserveLineInputSet:ReserveLine-triggerWrap']");
    }


    public String getReserveLine() {
        return select_ReserveLine().getText();
    }


    public void selectReserveLine(String reserveDescription) {
        select_ReserveLine().selectByVisibleText(reserveDescription);
    }


    public void setPrimaryPayeeName(String textString) {
        TopMenu topMenu = new TopMenu(this.driver);
        if (textString.equalsIgnoreCase("insured")) {
            textString = topMenu.getInsuredsName();
        }
        sendArbitraryKeys(Keys.ESCAPE);
        select_PrimaryPayeeName().selectByVisibleText(textString);
    }


    public void setPrimaryPayeeNameRandom() {
        select_PrimaryPayeeName().selectByVisibleTextRandom();
    }


    public void selectRandomReserveLine() {
        select_ReserveLine().selectByVisibleTextRandom();
    }


    public void setCheckDelivery(String textString) {
        try {
            select_CheckDelivery().selectByVisibleText(textString);
        } catch (Exception e) {
            select_CheckDelivery().selectByVisibleText("Not reportable");
        }
    }

    @FindBy(xpath = "//span[contains(@id,'ClaimInfoBar:Insured-btnInnerEl')]/span[@class='Claim:ClaimInfoBar:Insured-btnInnerEl' or contains(@class,'infobar_elem_val')]")
    public WebElement elementInsuredName;


    public String getInsuredName() {
        return elementInsuredName.getText();
    }


    public String getPrimaryPayeeType() {
        String currentValue = select_PrimaryPayeeType().getText();
        return currentValue;
    }


    public void setPrimaryPayeeType(String textString) {

        List<String> listOptions = select_PrimaryPayeeType().getList();
        boolean foundMatch = false;

        for (String string : listOptions) {
            if (string.equalsIgnoreCase(textString)) {
                foundMatch = true;
                break;
            }
        }

        if (foundMatch) {
            select_PrimaryPayeeType().selectByVisibleText(textString);
        } else {
            select_PrimaryPayeeType().selectByVisibleTextRandom();
        }
    }


    public void setPrimaryPayeeTypeRandom() {
        select_PrimaryPayeeType().selectByVisibleTextRandom();
    }

    public Guidewire8Select select_MailingAddress() {
        return new Guidewire8Select(
                driver, "//table[contains(@id,':MailingAddress-triggerWrap')]");
    }


    public void setMailingAddressRandom() {
        select_PrimaryPayeeType().selectByVisibleTextRandom();
    }


    public void setReportAs(String textString) {
        try {
            select_ReportAs().selectByVisibleText(textString);
        } catch (Exception e) {
            select_ReportAs().selectByVisibleText("Not reportable");
        }

    }


    public void selectSpecific_ReserveLine(String line) {
        select_CoverageType().selectByVisibleTextPartial(line);
    }


    public void setChecksLineItems(CheckLineItemType type, CheckLineItemCategory category, String amount) {

        CheckLineItem lineItem = new CheckLineItem(type, category, new BigDecimal(amount));
        populateLineItems(lineItem);

        

    }


    public void validationControl() {
        List<WebElement> messageElement = driver
                .findElements(By.xpath("//div[@class='message']"));
        for (WebElement webElement : messageElement) {
            String errMssg = webElement.getText();
            if (errMssg.equalsIgnoreCase("All exposures on a claim must have the same coverage incident.")) {
                
                clickClearButton();
                
                click_FinishButton();
            } else if (errMssg.contains("Deductible Amount goes over the whole deductible limit of")) {
                clickWhenClickable(button_Clear);
                
                clickWhenClickable(button_Back);
                
                clickWhenClickable(find(By.xpath("//div[text()='Deductible']/../..//img")));
                
                clickWhenClickable(button_Remove);
                
                clickWhenClickable(button_Next);
                
                clickWhenClickable(button_Finish);
            } else if (errMssg.contains("Cannot continue check without either an SSN or a HICN")) {
                try {
                    throw new Exception("Cannot continue check without either an SSN or a HICN");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void editCheck() {

        SearchAddressBook searchAB = new SearchAddressBook(this.driver);

        WebElement checkAmount = find(By.xpath(
                "//a[contains(@id,':GrossAmount') and contains(@id,'ClaimFinancialsChecks:ClaimFinancialsChecksScreen:ChecksLV:')]"));

        clickWhenClickable(checkAmount);
        
        clickEditButton();
        
        select_EditCheckDelivery().selectByVisibleText("Send");
        clickAddJointPayees();
        

        List<WebElement> nameSelects = finds(By.xpath(
                "//img[contains(@id,':PayeeName:PayeeNameMenuIcon') and contains(@id,':NewCheckPayeeDVPayeesLV:')]"));

        clickWhenClickable(nameSelects.get(nameSelects.size() - 1));
        clickSearchLink();

        searchAB.setName("Abc Towing LLC");
        searchAB.clickSearchButton();
        searchAB.selectFirstResult();

        List<WebElement> mailToBox = finds(By.xpath(
                "//div[contains(@id,'EditCheckWizard:EditCheckWizard_CheckPayeesScreen:EditCheckPayeeDV:NewCheckPayeeDVPayeesLV-body')]/div/table/tbody/tr/td[4]/div/img"));
        WebElement thisBox = mailToBox.get(mailToBox.size() - 1);
        clickWhenClickable(thisBox);
        clickWhenClickable(thisBox);

        select_ReportAs().selectByVisibleText("Not reportable");

        clickNextButton();
        
        clickNextButton();
        
        click_FinishButton();
        

    }


    public void enterPayeeInformation(String payeeName) {

        waitUntilElementIsClickable(By.cssSelector("input[id$=':PrimaryPayee_Name-inputEl']"));
        setPrimaryPayeeName(payeeName);

        sendArbitraryKeys(Keys.ESCAPE);
        

        waitUntilElementIsClickable(By.cssSelector("input[id$=':PrimaryPayee_Type-inputEl']"));

        if (select_PrimaryPayeeType().getList().contains("Insured")) {
            setPrimaryPayeeType("Insured");
            
        } else if (select_PrimaryPayeeType().getList().contains("Claimant") && !select_PrimaryPayeeType().getList().contains("Insured")) {
            setPrimaryPayeeType("Claimant");
            
        } else {
            setPrimaryPayeeType("Other");
        }

        setReportAs("Not reportable");
        setcheckType("System Generated");

        clickNextButton();
    }


    public void enterPayeeInformation(String payeeName, String payeeType, String reportAs, String checkType) {

        setPrimaryPayeeName(payeeName);
        setPrimaryPayeeType(payeeType);
        setReportAs(reportAs);

        if (payeeType.equalsIgnoreCase("Vendor")) {
            if (select_MailingAddress().getList().contains("<none>") && select_MailingAddress().getList().size() < 2) {
                clickPrimaryNamePicker();
                clickContactDetails().addDefaultAddress();
            } else {
                setMailingAddressRandom();
            }
        }

        setcheckType(checkType);
        clickNextButton();
    }


    public void enterPaymentInformation(String paymentAmount, Boolean deductibleToAdd, String deductibleAmount) {

        try {
            if (select_ReserveLine().getText().equalsIgnoreCase("<none>")) {
                selectRandomReserveLine();
            }
        } catch (Exception e) {
            System.out.println("Reserve Line is not selectable.");
        }

        sendArbitraryKeys(Keys.ESCAPE);
        waitUntilElementIsClickable(By.cssSelector("input[id$=':Transaction_Comments-inputEl']"), 30);
        
        setRandomPaymentType();

        setChecksLineItems(CheckLineItemType.INDEMNITY, CheckLineItemCategory.INDEMNITY, paymentAmount);

        if (deductibleToAdd) {
            
            clickAddItemButton();
            
            setChecksLineItems(CheckLineItemType.DEDUCTIBLE, CheckLineItemCategory.INDEMNITY, deductibleAmount);
        }

        clickNextButton();

        checkErrors();


    }

    private void checkErrors() {

        try {
            if (driver.findElement(By.cssSelector("div[id*='CheckPaymentsScreen:_msgs']"))
                    .getText().equalsIgnoreCase("Payment amount greater than reserve amount!")) {
                String availableReserves = driver.findElement(By.cssSelector("div[id*=':Transaction_AvailableReserves-inputEl']")).getText();
                availableReserves = availableReserves.replace("$", "");
                By lastRowLocator = By.xpath("//div[contains(@id,':NewPaymentDetailDV:EditablePaymentLineItemsLV-body')]//tr[not(contains(@id, 'summary-record'))][last()]");
                clickWhenClickable(find(lastRowLocator).findElement(By.xpath(".//td[5]")));

                if (availableReserves.equalsIgnoreCase("-")) {
                    Assert.fail("Available Reserves have been exhausted.");
                }

                setAmount(availableReserves);
                sendArbitraryKeys(Keys.TAB);
                waitForPostBack();
                sendArbitraryKeys(Keys.ESCAPE);
                try {
                    
                    clickNextButton();
                } catch (Exception e) {
                    Assert.fail("Unable to update check line item.");
                }
            }
        } catch (Exception q) {
            Assert.assertTrue(true, "No Check Errors Detected.");
        }


    }

}
