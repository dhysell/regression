package repository.cc.claim.searchpages;

import com.idfbins.enums.YesOrNo;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ClaimSearchLineOfBusiness;
import repository.gw.enums.ClaimStatus;
import repository.gw.enums.Status;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.helpers.WaitUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class AdvancedSearchCC extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    private WaitUtils waitUtils;

    public AdvancedSearchCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // Elements

    @FindBy(xpath = "//div[contains(@id, 'ClaimSearchScreen:ClaimSearchResultsLV')]")
    private WebElement table_SearchSearchResults;
    
    @FindBy(xpath = "//input[contains(@id,':ClaimNumber-inputEl')]")
    private WebElement textBox_ClaimNumber;

    @FindBy(xpath = "//input[contains(@id,':PolicyNumber-inputEl')]")
    private WebElement textBox_PolicyNumber;

    private Guidewire8Select select_PartyInvolvedType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'SearchFor-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':Name-inputEl')]")
    private WebElement textBox_CompanyName;

    @FindBy(xpath = "//input[contains(@id,'LastName-inputEl')]")
    private WebElement textBox_LastName;

    @FindBy(xpath = "//input[contains(@id,'FirstName-inputEl')]")
    private WebElement textBox_FirstName;

    @FindBy(xpath = "//input[contains(@id,'FormerName-inputEl')]")
    private WebElement textBox_FormerName;

    @FindBy(xpath = "//input[contains(@id,'AlternateName-inputEl')]")
    private WebElement textBox_AlternateName;

    private Guidewire8Select select_SearchBySSNorTIN() {
        return new Guidewire8Select(driver, "//table[contains(@id,':TaxReportingOption-triggerWrap')]");
    }

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:SSN-inputEl']")
    private WebElement textBox_SSN;

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:TaxId-inputEl']")
    private WebElement textBox_TIN;

    private Guidewire8Select select_AssignedToUser() {
        return new Guidewire8Select(driver, "//table[contains(@id,'AssignedToUser-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id,'AssignedToUser:AssignedToUserMenuIcon')]")
    private WebElement picker_AssignedToUser;

    private Guidewire8Select select_CreatedBy() {
        return new Guidewire8Select(driver, "//table[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CreatedBy-triggerWrap']");
    }

    @FindBy(xpath = "//a[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:CreatedBy:CreatedByMenuIcon']")
    private WebElement picker_CreatedBy;

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:VinNumber-inputEl']")
    private WebElement textBox_Vin;

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchRequiredInputSet:LicensePlate-inputEl']")
    private WebElement textBox_LicensePlate;

    private Guidewire8Select select_Jurisdiction() {
        return new Guidewire8Select(driver, "//table[contains(@id,'triggerWrap')]");
    }

    private Guidewire8Select select_ClaimStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimStatus-triggerWrap')]");
    }

    private void setClaimStatus(String selection) {
        select_ClaimStatus().selectByVisibleText(selection);
    }

    private Guidewire8Select select_LineOfBusiness() {
        return new Guidewire8Select(driver, "//table[contains(@id,'LOB-triggerWrap')]");
    }

    private Guidewire8Select select_LossType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'LossType-triggerWrap')]");
    }

    private Guidewire8Select select_CoverageInQuestion() {
        return new Guidewire8Select(driver, "//table[contains(@id,'CoverageInQuestion-triggerWrap')]");
    }

    private Guidewire8Select select_FlaggedType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'FlaggedType-triggerWrap')]");
    }

    private Guidewire8Select select_LitigationStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'LitigationStatus-triggerWrap')]");
    }

    private Guidewire8Select select_SalvageStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'SalvageStatus-triggerWrap')]");
    }

    private Guidewire8Select select_SubrogationStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'SubrogationStatus-triggerWrap')]");
    }

    private Guidewire8Select select_TypeOfDateSearch() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DateSearchChosenOption-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,'DateSearchDirectChoice_Choice-inputEl')]")
    private WebElement searchForDateFrom_Radio;

    @FindBy(xpath = "//input[contains(@id,'DateSearchStartDate-inputEl')]")
    private WebElement searchFrom_EditBox;

    @FindBy(xpath = "//input[contains(@id,'DateSearchEndDate-inputEl')]")
    private WebElement searchTo_EditBox;

    private Guidewire8Select select_FinancialValue() {
        return new Guidewire8Select(driver, "//table[contains(@id,'FinancialChosenOption-triggerWrap')]");
    }

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:FinancialAmountStart-inputEl']")
    private WebElement textBox_FinancialValueStart;

    @FindBy(xpath = "//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:FinancialAmountEnd-inputEl']")
    private WebElement textBox_FinancialValueEnd;

    @FindBy(xpath = "//a[contains(text(),'Select')]")
    private WebElement button_Select;

    @FindBy(xpath = "//a[@data-qtip='Next Page']")
    private WebElement button_NextPage;

    @FindBy(xpath = "//a[contains(@id,'ClaimSearchAndResetInputSet:Search')]")
    public WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id,ClaimSearchAndResetInputSet:Reset:)]")
    public WebElement button_Reset;

    // private helper functions

    private void inputFinancialValueStart(String amount) {
        textBox_FinancialValueStart.sendKeys(amount);
    }

    private void inputPolicyNumber(String pNum) {
        textBox_PolicyNumber.sendKeys(pNum);
    }

    private void clickSearchButton() {
        clickWhenClickable(button_Search);
        PageFactory.initElements(driver, this);
    }

    private void clickResetButton() {
        clickWhenClickable(button_Reset);
    }

    private void inputSearchFromDate(String dateFrom) {
        sendArbitraryKeys(Keys.ESCAPE);
        waitUntilElementIsNotVisible(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
        waitUntilElementIsVisible(searchFrom_EditBox).click();
        waitUntilElementIsVisible(searchFrom_EditBox).clear();
        waitUntilElementIsVisible(searchFrom_EditBox).sendKeys(dateFrom);
        sendArbitraryKeys(Keys.ESCAPE);
    }

    private void inputSearchToDate(String dateTo) {
        sendArbitraryKeys(Keys.ESCAPE);
        waitUntilElementIsNotVisible(By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']"));
        waitUntilElementIsVisible(searchTo_EditBox).click();

        waitUntilElementIsVisible(searchTo_EditBox).clear();
        waitUntilElementIsVisible(searchTo_EditBox).sendKeys(dateTo);
        sendArbitraryKeys(Keys.ESCAPE);
    }

    private void clickSearchForDateFrom() {
        searchForDateFrom_Radio.click();
    }

    /**
     * @param lossType - this string is derived from an enum. Auto and Property Glass
     *                 claims use LOB of auto and Property. The rest of lossTypes
     *                 fall under the default.
     */
    private void selectLossType(String lossType) {
    	Guidewire8Select mySelect = select_LossType();

        switch (lossType) {
            case "Auto - ERS or Glass":
                lossType = "Auto";
                mySelect.selectByVisibleText(lossType);
                break;
            case "Property - Residential Glass":
                lossType = "Property";
                mySelect.selectByVisibleText(lossType);
                break;
            default:
                mySelect.selectByVisibleText(lossType);
                break;
        }
    }

    private void selectCoverageInQuestion(YesOrNo yOrN) {
    	Guidewire8Select mySelect = select_CoverageInQuestion();
        mySelect.selectByVisibleText(yOrN.getValue());
    }

    private void selectClaimStatus(String text) {
    	Guidewire8Select mySelect = select_ClaimStatus();
        mySelect.selectByVisibleText(text);
    }

    private void selectFinancialValue(String fValue) {
    	Guidewire8Select mySelect = select_FinancialValue();
        mySelect.selectByVisibleText(fValue);
    }

    private void waitForSearch() {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(By.cssSelector("div[id$='ClaimSearchResultsLV-body'] tr")),
                ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='ClaimSearch:ClaimSearchScreen:_msgs']/div[@class='message']"))
                )
        );
    }

    private void checkThatResultsWereReturned() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String toManyResultsText = "Please provide more specific search criteria";
        String noResultsText = "The search returned zero results.";
        boolean errExists = false;
        int daysAway = 3;

        waitForSearch();
        List<WebElement> errBanner = finds(By.xpath("//div[@id='ClaimSearch:ClaimSearchScreen:_msgs']/div[@class='message']"));

        if (errBanner.size() > 0) {
            errExists = true;
            Integer outerCount = 0;
            Integer innerCount = 0;

            while (errExists == true && outerCount < 5) {
                if ((errBanner.get(0).getText()).toLowerCase().contains(noResultsText.toLowerCase()) || (errBanner.get(0).getText()).toLowerCase().contains(toManyResultsText.toLowerCase())) {

                    LocalDate firstDate;

                    try {
                        firstDate = DateUtils.convertDateToLocalDate(DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.ClaimCenter));
                    } catch (Exception e) {
                        firstDate = LocalDate.now();
                    }


                    if (firstDate == null) {
                        firstDate = LocalDate.now();
                    }

                    LocalDate secondDate = firstDate.minusDays(daysAway);
                    String dateStringTo = firstDate.format(formatter);
                    String dateStringFrom = secondDate.format(formatter);

                    clickSearchForDateFrom();

                    while (errExists == true && innerCount < 25) {
                        inputSearchFromDate(dateStringFrom);
                        inputSearchToDate(dateStringTo);
                        clickSearchButton();
                        waitForSearch();
                        List<WebElement> pageBox = finds(By.xpath("//input[@id='ClaimSearch:ClaimSearchScreen:ClaimSearchResultsLV:_ListPaging-inputEl']"));

                        String pageValue = "0";
                        try {
                            pageValue = pageBox.get(0).getAttribute("value");
                        } catch (Exception e) {
                            pageValue = "0";
                        }

                        
                        errBanner = finds(By.xpath("//div[@id='ClaimSearch:ClaimSearchScreen:_msgs']/div[@class='message']"));
                        if ((errBanner.size() <= 0) && (!pageValue.equalsIgnoreCase("0"))) {
                            errExists = false;
                        } else {
                            firstDate = firstDate.minusDays(daysAway);
                            secondDate = secondDate.minusDays(daysAway);
                            dateStringTo = firstDate.format(formatter);
                            dateStringFrom = secondDate.format(formatter);
                        }
                        innerCount++;
                    }

                    
                }
                outerCount++;
            }
        }
    }

    private void clickNextPageButton() {
        clickWhenClickable(button_NextPage);
    }

    private boolean checkRedundancy(String tempAccntNum, List<String> accountNums) {
        boolean pass = true;
        for (String accountNum : accountNums) {
            if (accountNum.equalsIgnoreCase(tempAccntNum)) {
                pass = false;
            }
        }
        return pass;
    }

    // public available interface methods

    public void inputClaimNumber(String cNum) {
        textBox_ClaimNumber.sendKeys(cNum);
    }

    public void clickSelectButton() {
        clickWhenClickable(button_Select);
    }

    public AdvancedSearchCC searchForClaimNumbers(String polNumber, String fnolType, ClaimSearchLineOfBusiness lob, ClaimStatus claimStatus) {
        waitUntilElementIsClickable(By.xpath("//table[contains(@id,'LOB-triggerWrap')]"));

        if (claimStatus != null) {
            setClaimStatus(claimStatus.toString());
        }

        if (lob != null) {
            
            Guidewire8Select mySelect = select_LineOfBusiness();
            
            mySelect.selectByVisibleText(lob.getValue());
            
            selectCoverageInQuestion(YesOrNo.No);
            
            clickSearchButton();
            checkThatResultsWereReturned();
        } else if (polNumber == null || polNumber.equalsIgnoreCase("Random")) {
            
            selectLossType(fnolType);
            
            selectCoverageInQuestion(YesOrNo.No);
            
            clickSearchButton();
            checkThatResultsWereReturned();
        } else {
            inputPolicyNumber(polNumber);
            clickSearchButton();
            checkThatResultsWereReturned();
        }
        PageFactory.initElements(driver, this);
        return this;
    }

    public String getRandomPolicyNumberFromList(List<String> pNumberList) {

        String policyNumber = pNumberList.get(NumberUtils.generateRandomNumberInt(0, (pNumberList.size() - 1)));

        System.out.println("/////////////////////////////////////////");
        System.out.println(" ");
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Root Number: " + policyNumber.substring(3, 9));

        return policyNumber;
    }

    public List<String> gatherPolicyRootNumbers(String policyType) {
        
        String resultsTableXpath = "//div[contains(@id,'ClaimSearchScreen:ClaimSearchResultsLV')]";
        int numPages = getNumResultPages();
        List<String> policyNumbers = new ArrayList<>();
        List<String> claimNumbers = new ArrayList<>();

        // Gather claimResults
        for (int i = 0; i < numPages; i++) {
            waitUntilElementIsClickable(find(By.xpath(resultsTableXpath)));
            List<String> claimElements = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Claim");
            claimNumbers.addAll(claimElements);
            if (numPages > 1) {
                clickNextPageButton();
            }
        }

        // Get Policy numbers from claimNumbers
        for (String claimNum : claimNumbers) {
            // policy prefix = substring(0,2)
            // policy root = substring(2,8)
            // policy suffix = substring(8,10)
            String policyNum = claimNum.substring(0, 2) + "-" + claimNum.substring(2, 8) + "-" + claimNum.substring(8, 10);

            policyNumbers.add(policyNum);
        }
        // Return a list policy numbers that are unique. 
        return policyNumbers.parallelStream().distinct().collect(Collectors.toList());
    }

    private int getNumResultPages() {

        int pages = finds(By.cssSelector("input[id*='_ListPaging-inputEl']")).size() > 0 ?
                1 : Integer.parseInt(find(By.xpath("//div[contains(text(),'of')]")).getText().replaceAll("[^0-9]", ""));

        return pages;
    }

    public String findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness lineOfBusiness) {
    	Guidewire8Select mySelect = select_LineOfBusiness();

        waitUtils.waitUntilElementIsVisible(mySelect.getSelectButtonElement());
        mySelect.selectByVisibleText(lineOfBusiness.getValue());

        clickSearchButton();

        String claimNumber = clickRandomClaimNumber();
        return claimNumber;
    }

    private Guidewire8Select selectSearchForDateSince() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DateSearch:DateSearchRangeValue-triggerWrap')]");
    }

    private void setSearchForDateSince(String selection) {
        selectSearchForDateSince().selectByVisibleText(selection);
    }

    public String findRandomClaimFromLineOfBusiness(ClaimSearchLineOfBusiness lineOfBusiness, Status status) {

        String claimNumber = "";
        List<String> options = selectSearchForDateSince().getList();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        setClaimStatus(status.getValue());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        Guidewire8Select mySelect = select_LineOfBusiness();
        mySelect.selectByVisibleText(lineOfBusiness.getValue());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();

        int num = options.size();
        int count = num-1;

        do {
            setSearchForDateSince(options.get(count));
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
            clickSearchButton();
        try {
            claimNumber = clickRandomClaimNumber();
        } catch (Exception e) {
            count--;
        }

        } while(claimNumber.equalsIgnoreCase("") && count >=0);

        return claimNumber;
    }

    public String findRandomClaimReadyForCheckWriting() {
        waitUntilElementIsVisible(textBox_ClaimNumber);
        
        selectClaimStatus("Open");
        selectCoverageInQuestion(YesOrNo.No);
        selectFinancialValue("Open Reserves");
        
        inputFinancialValueStart("2000");
        clickSearchButton();
        checkThatResultsWereReturned();
        return clickRandomClaimNumber();

    }

    private String clickRandomClaimNumber() {
        int randomRow = NumberUtils.generateRandomNumberInt(1, tableUtils.getRowCount(table_SearchSearchResults) - 1);
        String claimNumber = tableUtils.getCellTextInTableByRowAndColumnName(table_SearchSearchResults, randomRow, "Claim");
        tableUtils.clickLinkInTableByRowAndColumnName(table_SearchSearchResults, randomRow, "Claim");
        return claimNumber;
    }
}