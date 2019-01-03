package repository.cc.claim;

import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.cc.enums.PhoneType;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GenerateFNOLType;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;


public class BasicInfo extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public BasicInfo(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//span[@id='FNOLWizard:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']")
    public WebElement elementInsuredsName;

    @FindBy(xpath = "//input[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:ReportedBy_Name-inputEl']")
    public WebElement editBox_ReportedByName;

    public WebElement checkBox_IncidentInvolved;

    @FindBy(xpath = "//div[@id='FNOLWizard/FB_BasicInfo']/table/tbody/tr/td/div/table/tbody/tr/td[2]/div/table[starts-with(@id='ext-')]")
    public WebElement table_Incidents;

    @FindBy(xpath = "//div[contains(@id,'FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:RightPanel:FNOLWizard_BasicInfoRightPanelSet:')]//div[@class='x-grid-cell-inner x-grid-cell-inner-checkcolumn']")
    public List<WebElement> checkBox_Incidents;

    @FindBy(xpath = "//input[contains(@id,':InsuredVehicleDV:InsuredVehicleInputGroup:_checkbox')]")
    public List<WebElement> checkBox_IncidentsERS;

    @FindBy(xpath = "//input[contains(@id, ':InsuredVehicleDV:InlandMarineInputGroup:_checkbox')]")
    public List<WebElement> checkbox_IncidentsInlandMarine;

    @FindBy(xpath = "//span[@id='FNOLWizard:Next-btnInnerEl']")
    public WebElement button_Next;

    @FindBy(xpath = "//span[@id='NewClaimDuplicatesWorksheet:NewClaimDuplicatesScreen:NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']")
    public WebElement button_Close;

    @FindBy(xpath = "//span[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:ttlBar']")
    public WebElement element_PageHeader;

    @FindBy(xpath = "//table[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:ReportedBy_Name-triggerWrap']/tbody/tr/td[2]")
    public WebElement nameDropDownTrigger;

    @FindBy(xpath = "//span[starts-with(@id,'ext-gen')]")
    public WebElement element_PolicyNum;

    @FindBy(xpath = "//div[contains(@id, ':InsuredVehicleDV:InsuredVehicleInputGroup-legendTitle')]")
    public List<WebElement> table_IncidentsToBeValidated;

    @FindBy(xpath = "//input[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:BusinessPhone:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    public WebElement input_Business;

    @FindBy(xpath = "//input[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:FNOLWizard_ContactInfoInputSet:FBContactInfoInputSet:Work:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    public WebElement input_Work;

    @FindBy(css = "input[id*=':Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']")
    private WebElement inputMobile;

    private String setMobileRandom() {
        waitUntilElementIsClickable(inputMobile);

        String randPhone = "208555" + NumberUtils.generateRandomNumberInt(1000, 9999);

        inputMobile.clear();
        inputMobile.sendKeys(randPhone);

        return randPhone;
    }


    @FindBy(xpath = "//input[contains(@id,':reporter_email-inputEl')]")
    private WebElement inputEmailAddress;

    // This is a modified version of the TableUtils Class's getRowCount to deal with rogue Claim Center tables.
    private int getRowCount(WebElement tableDivElement) {
        int rowCount1;
        int rowCount2;
        int rowCount = 0;
        if (getXpathFromElement(tableDivElement).contains("/div/table")) {
            try {
                rowCount1 = tableDivElement.findElements(By.xpath(".//tbody/tr")).size();
            } catch (StaleElementReferenceException staleElementException) {
                rowCount1 = tableDivElement.findElements(By.xpath(".//tbody/tr")).size();
            }
        } else {
            try {
                rowCount1 = tableDivElement.findElements(By.xpath(".//div[contains(@id,'-body')]//tbody/tr")).size();
                rowCount2 = tableDivElement.findElements(By.xpath(".//div[contains(@id,'-body')]//tbody/tr[descendant::tr]")).size();

                if ((rowCount2 < rowCount1 && rowCount2 != 0)) {
                    rowCount = rowCount2;
                } else if (rowCount2 > rowCount1 && rowCount1 != 0) {
                    rowCount = rowCount1;
                }

            } catch (StaleElementReferenceException staleElementException) {
                rowCount1 = tableDivElement.findElements(By.xpath(".//div[contains(@id,'-body')]//tbody/tr")).size();
            }
        }
        return rowCount;
    }

    // This is a modified version of the TableUtils Class's returnRowInTableByText to deal with rogue Claim Center tables.
    private static int returnRowInTableByText(WebElement tableDivElement, String textInTable) {

        WebElement tableRowToFind;
        int rowNumber = -1;

        try {
            tableRowToFind = tableDivElement.findElement(By.xpath(".//tbody/descendant::label[contains(text(), '" + textInTable + "')]/ancestor::tr[2]"));
            rowNumber = Integer.parseInt(tableRowToFind.getAttribute("data-recordindex"));
        } catch (Exception e) {
            tableRowToFind = tableDivElement.findElement(By.xpath(".//tbody/descendant::label[contains(text(), '" + textInTable + "')]/ancestor::tr[2]"));
            rowNumber = Integer.parseInt(tableRowToFind.getAttribute("data-recordindex"));
        }

        return rowNumber + 1;
    }

    public void checkPhoneNumberErrorMessages() {
        List<WebElement> errorMessage = finds(By.xpath("//div[@class='message']"));
        if (errorMessage.size() > 0) {

            if (errorMessage.get(0).getText().contains("Must specify a value for the selected primary phone type") || errorMessage.get(0).getText().contains("Contact number missing")) {
                find(By.xpath("//input[contains(@id,':Cell:')]")).sendKeys("5555555555");
                new Guidewire8Select(this.driver, "//table[contains(@id,'primaryPhone-triggerWrap')]").selectByVisibleText("Mobile");
                clickNextButton();
            }
        }

    }

    @FindBy(css = "span[id*='NewClaimDuplicatesWorksheet_CloseButton-btnInnerEl']")
    private WebElement buttonClose;

    private void clickCloseButton() {
        waitUtils.waitUntilElementIsClickable(buttonClose, 2000);
        buttonClose.click();
    }

    public void clickCloseButtonIfPresent() {

        boolean isButtonFound = false;

        try {
            waitUtils.waitUntilElementIsVisible(buttonClose, 5000);
            isButtonFound = true;
        } catch (Exception e) {
            systemOut("Duplicate Claims Close Button Not Found.");
        }

        if (isButtonFound) {
            clickCloseButton();
            waitUtils.waitUntilElementIsNotVisible(buttonClose);

            clickNextButton();
            waitUntilElementIsNotVisible(By.cssSelector("input[id*=':Cell:GlobalPhoneInputSet:NationalSubscriberNumber-inputEl']"));
        }
    }

    public void clickNameTrigger() {
        nameDropDownTrigger.click();
    }

    public void clickNextButton() {
        clickWhenClickable(button_Next);
        hoverOver(elementInsuredsName);
        waitUntilElementIsNotVisible(By.cssSelector("a[class*='x-focus'][class*='x-btn-focus']"));

    }

//    public void clickReportedByName() {
//        select_ReportedByName().clickButton();
//    }

    public String getNameOfInsured() {
        waitUtils.waitUntilElementIsVisible(elementInsuredsName, 25);
        return elementInsuredsName.getText();
    }

    public String incidentPicker(Integer numItems, String incidentName, String fnolType, String coverageName, String lossCause, int numTries) throws Exception {
        WebElement tableEle = null;

        // Verify table exists before actually trying to click the table.
        try {
            tableEle = find(By.xpath(setTableXpath(fnolType)));
        } catch (Exception e) {
            Assert.fail("No incidents available on the selected policy.");
        }

        int incidentsAvailable = getRowCount(tableEle);
        int rowToChoose = incidentName.equalsIgnoreCase("random") ?
                NumberUtils.generateRandomNumberInt(1, incidentsAvailable) :
                returnRowInTableByText(tableEle, incidentName);

        By xpath = By.xpath(setTableXpath(fnolType) + "//tbody/tr[" + (rowToChoose) + "]/descendant::img[contains(@class, 'x-grid-checkcolumn')]");
        setIncidentCB(xpath, true);

        if (!checkIncidentHasSpecificCoverage(coverageName, fnolType, lossCause)) {
            if (incidentsAvailable <= 1) {
                Assert.fail("Only one incident is available and it didn't have the coverage needed for this test");
            }
            // Make sure the incident originally clicked is unclicked.
            else {
                setIncidentCB(xpath, false);
                numTries++;
                if (numTries >= 25) {
                    Assert.fail("It took more than 25 times to try and find a valid incident more than likely there isn't one for this policy.");
                }
                incidentPicker(numItems, incidentName, fnolType, coverageName, lossCause, numTries);
                waitForPageLoad();
            }
        }

        
        String pickedIncidentName = waitUntilElementIsVisible(find(By.xpath("//img[contains(@class,'grid-checkcolumn-checked')]/ancestor::tr[1]//label[not(contains(@for,'coverage'))]"))).getText();
        return pickedIncidentName;

    }

    private void setIncidentCB(By cb, boolean setCheckBoxTrueFalse) {
        boolean setCorrectly = false;
        int i = 0;

        while (!setCorrectly && i < 10) {

            WebElement cbEle = find(cb);
            // If you want to check a check box
            if (setCheckBoxTrueFalse) {
                // if the element isn't checked based and you wanted it to be.
                if (!cbEle.getAttribute("class").contains("-checkcolumn-checked")) {
                    waitUntilElementIsClickable(find(cb));
                    dnd(cbEle);
                    waitForPageLoad();
                    setCorrectly = finds(By.cssSelector("img.x-grid-checkcolumn-checked")).size() > 0;
                } else {
                    setCorrectly = true;
                }
            }
            // You want to uncheck a checkbox
            else {
                // element is currently checked and reverse logic on boolean setCheckbox
                if (cbEle.getAttribute("class").contains("-checkcolumn-checked")) {
                    dnd(cbEle);
                    waitForPageLoad();
                    setCorrectly = finds(By.cssSelector("img.x-grid-checkcolumn-checked")).size() == 0;
                } else {
                    setCorrectly = true;
                }
            }
            i++;
            System.out.println("Tried times to click checkbox: " + i);
        }
        waitForPageLoad();
        if (!setCorrectly) {
            Assert.fail("Unable to pick an incident.");
        }
    }

    public Guidewire8Select select_LossCause() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FNOLWizard_NewQuickClaimScreen:QuickClaimPanelSet:Claim_LossCause-triggerWrap']");
    }

    private Guidewire8Select selectPrimaryPhone() {
        return new Guidewire8Select(driver, "//table[contains(@id,':primaryPhone-triggerWrap')]");
    }

    private void setPrimaryPhone(PhoneType phoneType) {
        selectPrimaryPhone().selectByVisibleText(phoneType.toString());


//        By listSelector = By.cssSelector("div:not([style*='display: none']):not([role='presentation']) ul");
//        By input = By.cssSelector("input[id$=':primaryPhone-inputEl']");
//
//        clickWhenClickable(input);
//
//        try {
//            waitUntilElementIsVisible(listSelector, 10);
//            find(listSelector).findElement(By.xpath("//li[contains(text(),'" + phoneType.getSelectionText() + "')]")).click();
//        } catch (Exception e) {
//            systemOut("This element is horseshit.");
//        }

    }

    public Guidewire8Select select_ReportedByName() {
        return new Guidewire8Select(driver, "//table[@id='FNOLWizard:FBClaimWizardStepSet:FNOLWizard_BasicInfoScreen:PanelRow:BasicInfoDetailViewPanelDV:ReportedBy_Name-triggerWrap']");
    }

    public void selectSpecific_LossCause(String selectString) {
        Guidewire8Select mySelect = select_LossCause();
        mySelect.selectByVisibleText(selectString);
    }

    public void selectSpecific_ReportedByName(String selectString) {
        Guidewire8Select mySelect = select_ReportedByName();
        mySelect.selectByVisibleText(selectString);
        waitUntilElementIsVisible(By.cssSelector("input[id*='BusinessPhone']"));
    }

    // Validates whether the incident picked has the specified coverage.
    private boolean checkIncidentHasSpecificCoverage(String coverage, String fnolType, String lossCause) {
        boolean coverageFound = false;
        if (!fnolType.equalsIgnoreCase("crop")) {
            try {
                waitUtils.waitUntilElementIsClickable(By.xpath("//label[contains(@for,':coverage')]"));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        List<WebElement> coverages = finds(By.xpath("//label[contains(@for,':coverage')]"));
        if (coverage.equalsIgnoreCase("Random")) {
            switch (fnolType) {
                case "Auto - ERS or Glass":
                    if (lossCause.equalsIgnoreCase("Roadside Assistance")) {
                        for (WebElement rsText : coverages) {
                            if (rsText.getText().equalsIgnoreCase("Roadside Assistance")) {
                                coverageFound = true;
                                break;
                            } else {
                                coverageFound = false;
                            }
                        }
                    } else if (lossCause.contains("Auto Glass")) {
                        for (WebElement compText : coverages) {
                            if (compText.getText().equalsIgnoreCase("Comprehensive")) {
                                coverageFound = true;
                                break;
                            } else {
                                coverageFound = false;
                            }
                        }
                    }
                    break;

                default:
                    coverageFound = true;
                    break;
            }
        } else {
            
            coverages = finds(By.xpath("//label[contains(@for,':coverage')]"));
            for (WebElement cText : coverages) {
                if (scrambledStringMatch(coverage, cText.getText())) {
                    coverageFound = true;
                    break;
                }
            }
        }

        return coverageFound;
    }

    private boolean scrambledStringMatch(String coverage, String cText) {

        boolean isMatch = true;

        if (cText.equalsIgnoreCase(coverage)) {
            return true;
        } else {
            String stringParts[] = coverage.split(" ");

            for (int i = 1; i < stringParts.length; i++) {
                if (!cText.contains(stringParts[i])) {
                    isMatch = false;
                }
            }

            return isMatch;
        }
    }

    private boolean incidentsAvailable() {
        return finds(By.xpath("//div[contains(@id,':QuickClaimPanelSet:8') or contains(@id,'FNOLWizard_BasicInfoRightPanelSet:3')]")).size() > 0;
    }

    private String setTableXpath(String fnolType) throws Exception {

        if (incidentsAvailable()) {
            return (fnolType.equals(GenerateFNOLType.AutoGlass.getValue()) || fnolType.equals(GenerateFNOLType.ResidentialGlass.getValue())) ? "//div[contains(@id,':QuickClaimPanelSet:8') and not(contains(@id, '-body'))]" : "//div[contains(@id, 'FNOLWizard_BasicInfoRightPanelSet:3') and not(contains(@id, '-body'))]";
        } else {
            throw new GuidewireException(ApplicationOrCenter.ClaimCenter.getName(), "There didn't seem to be any incidents available");
        }
    }

    public void setEmailAddress(String keysToSend) {
        waitUtils.waitUntilElementIsClickable(inputEmailAddress);
        clickWhenClickable(inputEmailAddress);
        inputEmailAddress.clear();
        clickWhenClickable(inputEmailAddress);
        inputEmailAddress.sendKeys(keysToSend);
    }

    private void dnd(WebElement ele) {
        Actions action = new Actions(driver);
        try {
            waitForPostBack(60);
//            action.clickAndHold(ele).release().clickAndHold().release().build().perform();
            dragAndDrop(ele, 1, 1);
            
            waitForPostBack(60);
            clickProductLogo();
        } catch (Exception e) {
            System.out.println("Error waiting on postback.");
        }

    }

    public String setRandomMobileNumber() {
        setPrimaryPhone(PhoneType.MOBILE);
        String randNum = setMobileRandom();
        return randNum;
    }
}
