package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FindPolicy extends BasePage {

    private WaitUtils waitUtils;

    private WebDriver driver;

    public FindPolicy(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//input[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:policyNumber-inputEl']")
    private WebElement editBox_PolicyRootNumber;

    @FindBy(xpath = "//a[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Search']")
    private WebElement button_Search;

    @FindBy(xpath = "//a[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Reset']")
    private WebElement button_Reset;

    @FindBy(xpath = "//div[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV-body']/div/table[starts-with(@id,'gridview-')]")
    private WebElement table_SearchResults;

    @FindBy(xpath = "//a[contains(@id,'FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV:')]")
    private WebElement link_PolicyNumbers;

    private WebElement button_Select;

    @FindBy(xpath = "//input[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Claim_LossDate-inputEl']")
    private WebElement editBox_DateOfLoss;

    @FindBy(xpath = "//table[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:Claim_LossDate-triggerWrap']/tbody/tr/td/div[@class='x-trigger-index-0 x-form-trigger x-form-date-trigger x-form-trigger-first']")
    private WebElement button_DatePicker;

    @FindBy(xpath = "//a[contains(@title,'(Spacebar)')]")
    private WebElement button_CalendarToday;

    @FindBy(xpath = "//label[text()='Property']")
    private WebElement radio_Property;

    @FindBy(xpath = "//label[text()='Auto']")
    private WebElement radio_Auto;

    @FindBy(xpath = "//label[text()='Auto - ERS or Glass']")
    private WebElement radio_AutoERSGlass;

    @FindBy(xpath = "//label[text()='Property - Residential Glass']")
    private WebElement radio_PropertyResidentialGlass;

    @FindBy(xpath = "//a[@id='FNOLWizard:Next']")
    private WebElement button_Next;

    @FindBy(xpath = "//div[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:_msgs']/div[@class='message']")
    private WebElement element_errorMessage;

    @FindBy(xpath = "//a[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV:0:unselectButton']")
    private WebElement button_Unselect;
    @FindBy(xpath = "//div[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV-body']/div/table/tbody/tr/td[4]/div")
    private WebElement elementEffectiveDate;

    @FindBy(css = "input[id='FNOLWizard:FNOLWizard_FindPolicyScreen:ScreenMode_false-inputEl']")
    private WebElement createUnverifiedPolicyRadio;

    @FindBy(xpath = "//div[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV-body']/div/table/tbody/tr/td[5]/div")
    private WebElement elementExpiryDate;

    // HELPER METHODS
    // =======================================================================

    public void clickCreateUnverifiedPolicy() {
        waitUtils.waitUntilElementIsVisible(createUnverifiedPolicyRadio);
        clickWhenClickable(createUnverifiedPolicyRadio);
    }

    public Guidewire8Select select_PolicyType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PolicyType-triggerWrap')]");
    }


    public void setPolicyRootNumber(String rootNumber) {
        waitUntilElementIsClickable(editBox_PolicyRootNumber).sendKeys(rootNumber);
    }


    public void clickSearchButton() {
        clickWhenClickable(button_Search);

    }


    public void clickDateOfLossEditBox() {
        clickWhenClickable(editBox_DateOfLoss);
    }

    private void setDateOfLoss(String keysToSend) {
        waitUntilElementIsClickable(editBox_DateOfLoss).clear();
        waitUntilElementIsClickable(editBox_DateOfLoss).sendKeys(keysToSend);
    }


    public void clickResetButton() {
        clickWhenClickable(button_Reset);

    }


    public void clickPropertyRadio() {
        clickWhenClickable(radio_Property);
    }


    public void clickAutoRadio() {
        clickWhenClickable(radio_Auto);
    }


    public void clickAutoERSRadio() {
        clickWhenClickable(radio_AutoERSGlass);
    }


    public void clickPropertyResidentialGlass() {
        clickWhenClickable(radio_PropertyResidentialGlass);
    }


    public String clickNextButton() {
        String errorString = null;

        waitUtils.waitUntilElementIsClickable(button_Next);
        button_Next.click();

        try {
            waitUtils.waitUntilElementIsNotVisible(By.cssSelector("a[id$=':unselectButton']"));
        } catch (Exception e) {
            errorString = searchForErrors();
        }

        return errorString;
    }


    public int SearchResultsTableRows() {
        return table_SearchResults.findElements(By.tagName("tr")).size();
    }


    public void selectSpecificPolicyType(PolicyType selectString) {
        Guidewire8Select mySelect = select_PolicyType();
        mySelect.selectByVisibleText(selectString.getValue());
    }


    public void clickPolicyLink(String policyNumber, PolicyType policyType) {

        List<WebElement> tableRows = table_SearchResults.findElements(By.tagName("tr"));
        int numRows = tableRows.size();

        if (numRows > 1) {
            int clmnNum = 2;
            int rowNum = 0;

            

            if (policyType != null) {
                for (WebElement tableRow : tableRows) {
                    String currentRow = tableRow.getAttribute("data-recordindex");
                    String type = table_SearchResults
                            .findElement(By.xpath("//tr[@data-recordindex='" + currentRow + "']/td[3]/div")).getText();
                    String policyNum = table_SearchResults
                            .findElement(By.xpath("//tr[@data-recordindex='" + currentRow + "']/td[" + clmnNum + "]/div"))
                            .getText();

                    if (policyNum.contains(policyNumber.substring(3, 9)) && type.equalsIgnoreCase(policyType.getValue())) {
                        rowNum = Integer.parseInt(currentRow);
                        break;
                    }
                }
            } else {
                for (WebElement tableRow : tableRows) {
                    String currentRow = tableRow.getAttribute("data-recordindex");
                    String type = table_SearchResults
                            .findElement(By.xpath("//tr[@data-recordindex='" + currentRow + "']/td[3]/div")).getText();
                    String policyNum = table_SearchResults
                            .findElement(By.xpath("//tr[@data-recordindex='" + currentRow + "']/td[" + clmnNum + "]/div"))
                            .getText();

                    if (policyNum.equals(policyNumber) && !type.equalsIgnoreCase("Brokerage")) {
                        rowNum = Integer.parseInt(currentRow);
                        break;
                    }
                }
            }

            WebElement selectButton = find(By.xpath("//a[@id='FNOLWizard:FNOLWizard_FindPolicyScreen:FNOLWizardFindPolicyPanelSet:PolicyResultLV:"
                    + rowNum + ":selectButton']"));
            button_Select = selectButton;

            clickWhenClickable(button_Select);

        }
    }

    private LocalDate getEffectiveDate() {
        String dateFragments[] = elementEffectiveDate.getText().split("/");
        return LocalDate.of(Integer.parseInt(dateFragments[2]), Integer.parseInt(dateFragments[0]), Integer.parseInt(dateFragments[1]));
    }

    private LocalDate getExpiryDate() {
        String dateFragments[] = elementExpiryDate.getText().split("/");
        return LocalDate.of(Integer.parseInt(dateFragments[2]), Integer.parseInt(dateFragments[0]), Integer.parseInt(dateFragments[1]));
    }


    public LocalDate setLossDate(LocalDate customDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDate selectedDate = null;

        LocalDate effectiveDate = getEffectiveDate();
        LocalDate expiryDate = getExpiryDate();
        LocalDate todaysDate = LocalDate.now();

        if (customDate == null) {
            if (todaysDate.isAfter(expiryDate)) {
                selectedDate = getRandomDateBetween(expiryDate.minusMonths(11), expiryDate);
            } else if (todaysDate.isEqual(effectiveDate)) {
                selectedDate = todaysDate;
            } else if (todaysDate.isBefore(expiryDate) && todaysDate.isAfter(effectiveDate)) {
                selectedDate = todaysDate;
            } else if (todaysDate.isEqual(expiryDate)) {
                selectedDate = todaysDate.minusDays(1);
            } else if (todaysDate.isBefore(effectiveDate)) {
                Assert.fail("Claim effective date is in the future (" + effectiveDate + ").  A claim cannot be setup on a future date.");
            }
        } else {
            selectedDate = customDate;
        }

        setDateOfLoss(selectedDate.format(formatter));

        return selectedDate;
    }

    private LocalDate getRandomDateBetween(LocalDate effectiveDate, LocalDate expiryDate) {

        int daysBetween = (int) ChronoUnit.DAYS.between(effectiveDate, expiryDate);

        LocalDate randomDate = expiryDate.minusDays(NumberUtils.generateRandomNumberInt(0, daysBetween));

        return randomDate;
    }


    public boolean validateSearchResults() {
        boolean pass = true;

        List<WebElement> messageBanner = finds(By.xpath("//div[@class='message']"));
        if (messageBanner.size() > 0) {
            String messageText = messageBanner.get(0).getText();
            if (messageText.equalsIgnoreCase("The search returned zero results.")) {
                pass = false;
            }
        }

        return pass;

    }


    public LocalDate searchOrCreatePolicy(String policyNumber, List<String> policyNumbers, String claimType, LocalDate dol, PolicyType policyType) {

        boolean validData = false;
        int numPoliciesToTry = policyNumbers.size();
        String rootNumber = policyNumber.substring(3, 9);
        LocalDate dateOfLoss = null;

        while (!validData && numPoliciesToTry > 0) {
            setPolicyRootNumber(rootNumber);
            clickSearchButton();
            waitUntilElementIsVisible(By.cssSelector("div[id$='FNOLWizardFindPolicyPanelSet:PolicyResultLV-body'] tr"), 45);
            SearchResultsTableRows();
            clickPolicyLink(policyNumber, policyType);
            waitUntilElementIsClickable(editBox_DateOfLoss);
            String radioXpath = "";

            if (claimType.equalsIgnoreCase("Property")) {
                radioXpath = "//label[text()='Property']";
            } else if (claimType.equalsIgnoreCase("Auto")) {
                radioXpath = "//label[text()='Auto']";
            } else if (claimType.equalsIgnoreCase("Auto - ERS or Glass")) {
                radioXpath = "//label[text()='Auto - ERS or Glass']";
            } else if (claimType.equalsIgnoreCase("Property - Residential Glass")) {
                radioXpath = "//label[text()='Property - Residential Glass']";
            } else if (claimType.equalsIgnoreCase("Inland Marine")) {
                radioXpath = "//label[text()='Inland Marine']";
            } else if (claimType.equalsIgnoreCase("Crop")) {
                radioXpath = "//label[text()='Crop']";
            } else if (claimType.equalsIgnoreCase("MBRE")) {
                radioXpath = "//label[text()='Property']";
            } else if (claimType.equalsIgnoreCase("General Liability")) {
                radioXpath = "//label[text()='General Liability']";
            } else if (claimType.equalsIgnoreCase("Membership")) {
                radioXpath = "//label[text()='Membership']";
            }

            List<WebElement> radioArray = finds(By.xpath("" + radioXpath + ""));

            if (radioArray.size() < 1) {
                clickWhenClickable(button_Unselect);
                validData = false;
                clickWhenClickable(button_Reset);
                policyNumber = policyNumbers.get(NumberUtils.generateRandomNumberInt(0, (policyNumbers.size() - 1)));
                rootNumber = policyNumber.substring(3, 9);
                numPoliciesToTry--;
            } else {
                clickWhenClickable(find(By.xpath("" + radioXpath + "")));
                validData = true;
            }
        }

        if (!validData && numPoliciesToTry == 0) {
            Assert.fail("Unable to find a policy with the data to validate this test");
        }

        // Add Date of loss and proceed to next page while error checking.
        String errorString = null;

        do {
            dateOfLoss = setLossDate(dol);
            
            errorString = clickNextButton();

            try {
                dol = dol.minusDays(1);
            } catch (Exception e) {

            }

            
        }
        while (errorString != null && errorString.trim().equalsIgnoreCase("Date Of Loss : Date must be in the past."));

        if (errorString != null) {
            System.out.println(errorString);
        }

        if (dateOfLoss == null) {
            Assert.fail("Unable to retrieve date from Policy Center.");
        }

        return dateOfLoss;
    }


    public void comparePCData(GeneratePolicy myPolicyObj, String policyNumber, Date effectiveDate, Date expirationDate) {
        
        setPolicyRootNumber(myPolicyObj.accountNumber);
        
        selectSpecificPolicyType(PolicyType.BOP);
        
        clickSearchButton();
        

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        List<WebElement> errorArray = finds(By.xpath("//div[@class='message']"));
        List<String> messageArray = new ArrayList<>();
        boolean noResults = false;

        if (errorArray.size() > 0) {
            for (int i = 0; i < errorArray.size(); i++) {
                messageArray.add(errorArray.get(i).getText());
                if (errorArray.get(i).getText().equalsIgnoreCase("The search returned zero results.")) {
                    noResults = true;
                }
            }
        }

        if (!noResults) {
            String ccPolicyNum = table_SearchResults.findElement(By.xpath("//tr/td[2]/div/a")).getText();
            String ccEffectiveDate = table_SearchResults.findElement(By.xpath("//tr/td[4]")).getText();
            String ccExpiresDate = table_SearchResults.findElement(By.xpath("//tr/td[5]")).getText();
            String ccInsured = table_SearchResults.findElement(By.xpath("//tr/td[6]")).getText();
            String ccAddress = table_SearchResults.findElement(By.xpath("//tr/td[7]")).getText();
            ccAddress = ccAddress.replace("\n", " ");
            String pcAddress = myPolicyObj.pniContact.getAddress().getLine1() + " " + myPolicyObj.pniContact.getAddress().getCity()
                    + ", " + myPolicyObj.pniContact.getAddress().getState().getAbbreviation() + " "
                    + myPolicyObj.pniContact.getAddress().getZipNoDashes().substring(0, 5);

            if (policyNumber.equalsIgnoreCase(ccPolicyNum)) {
                if (myPolicyObj.pniContact.getCompanyName().equalsIgnoreCase(ccInsured)) {
                    if (pcAddress.equalsIgnoreCase(ccAddress)) {
                        if (formatter.format(effectiveDate).equalsIgnoreCase(ccEffectiveDate)
                                && formatter.format(expirationDate).equalsIgnoreCase(ccExpiresDate)) {
                            System.out.println("Policy retrieval succussful.");
                        } else {
                            throw new IllegalArgumentException(
                                    "Possible issue with policy retrieval - Dates do not match. Policy Number: "
                                            + policyNumber + "");
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "Possible issue with policy retrieval - Address does not match. Policy Number: "
                                        + policyNumber + "");
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Possible issue with policy retrieval - Company name does not match. Policy Number: "
                                    + policyNumber + "");
                }
            } else {
                throw new IllegalArgumentException(
                        "Possible issue with policy retrieval - Policy number does not match. Policy Number: "
                                + policyNumber + "");
            }
        } else {
            for (String message : messageArray) {
                System.out.println(message);
            }
        }
    }


    public String searchForErrors() {

        String errorString = null;

        try {
            errorString = find(By.cssSelector("div.message")).getText();
        } catch (Exception e) {
            System.out.println("No errors detected on \"Search or Create Policy\" page.");
        }

        return errorString;
    }
}
