package repository.cc.claim.searchpages;

import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ClaimStatus;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MedicareSearchsCC extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public MedicareSearchsCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select select_Reportable() {
        return new Guidewire8Select(driver, "//table[contains(@id,'MedicareSection111SearchDV:Reportable-triggerWrap')]");
    }

    private Guidewire8Select select_ReadyToReport() {
        return new Guidewire8Select(driver, "//table[contains(@id,'MedicareSection111SearchDV:ReadyToReport-triggerWrap')]");
    }

    private Guidewire8Select select_ClaimStatus() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ClaimStatus-triggerWrap')]");
    }

    private Guidewire8Select select_Adjuster() {
        return new Guidewire8Select(driver, "//table[contains(@id,'Adjuster-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,'MedicareSection111SearchDV:FlagshipID-inputEl')]")
    private WebElement textBox_FlagshipID;

    @FindBy(xpath = "//input[contains(@id,'MedicareSection111SearchDV:ReferredDate-inputEl')]")
    private WebElement textBox_ReferredDate;

    @FindBy(xpath = "//a[contains(@id,'MedicareSection111SearchDV:Search')]")
    private WebElement button_Search;

    @FindBy(xpath = "//a[contains(@id,'MedicareSection111SearchDV:Reset')]")
    private WebElement button_Reset;

    private void selectRandom_Reportable() {
    	Guidewire8Select mySelect = select_Reportable();
        mySelect.selectByVisibleTextRandom();
    }

    private void selectSpecific_Reportable(String item) {
    	Guidewire8Select mySelect = select_Reportable();
        mySelect.selectByVisibleTextPartial(item);
    }

    private void selectRandom_ReadyToReport() {
    	Guidewire8Select mySelect = select_ReadyToReport();
        mySelect.selectByVisibleTextRandom();
    }

    private void selectSpecific_ReadyToReport(String item) {
    	Guidewire8Select mySelect = select_ReadyToReport();
        mySelect.selectByVisibleText(item);
    }

    private void selectSpecific_ClaimStatus(ClaimStatus status) {
    	Guidewire8Select mySelect = select_ClaimStatus();
        mySelect.selectByVisibleTextPartial(status.toString());
    }

    private String selectRandom_Adjuster() {
        Guidewire8Select mySelect = select_Adjuster();
        return mySelect.selectByVisibleTextRandom();

    }

    private void inputFlagshipID(String id) {
        textBox_FlagshipID.sendKeys(id);
    }

    private void inputReferredDate(String date) {
        textBox_ReferredDate.sendKeys(date);
    }


    private void clickMedSearch() {
        clickWhenClickable(button_Search);
    }

    private void selectByReportable(String reportable) {
        if (reportable.equalsIgnoreCase("Random") || reportable == null) {
            selectRandom_Reportable();
        } else {
            selectSpecific_Reportable(reportable);
        }

    }

    private void selectByReadyToReport(String rToReport) {
        if (rToReport.equalsIgnoreCase("Random") || rToReport == null) {
            selectRandom_ReadyToReport();
        } else {
            selectSpecific_ReadyToReport(rToReport);
        }
    }

    public MedicareSearchsCC searchByReportable(String reportable) {
        selectByReportable(reportable);
        clickMedSearch();

        return this;
    }

    public MedicareSearchsCC searchByFlagShipID(String flagShipID) {
        selectByReportable(YesOrNo.Yes.getValue());
        inputFlagshipID(flagShipID);
        clickMedSearch();

        return this;
    }

    public MedicareSearchsCC searchByReferredDate(String referredDate) {
        selectByReportable(YesOrNo.Yes.getValue());
        inputReferredDate(referredDate);
        clickMedSearch();
        return this;
    }

    public MedicareSearchsCC searchByReadyToReport(YesOrNo yOrN) {
        selectByReportable(YesOrNo.Yes.getValue());
        
        selectByReadyToReport(yOrN.getValue());
        
        clickMedSearch();
        validateReadyToReportFilter(yOrN);
        return this;

    }

    public MedicareSearchsCC searchByAdjuster() {
        String adjusterName = selectRandom_Adjuster();
        
        clickMedSearch();
        
        if (checkResultsWereGenerated()) {
            validateAdjusterNameFilter(adjusterName);
        }
        return this;
    }

    public MedicareSearchsCC searchByClaimStatus(ClaimStatus status) {
        selectSpecific_ClaimStatus(status);
        
        clickMedSearch();
        
        validateClaimStatusFilter(status);
        return this;
    }

    private void searchReadyToReportClaimsByStatus() {

        waitUntilElementIsClickable(button_Search);

        selectSpecific_Reportable("Yes");
        selectSpecific_ReadyToReport("Yes");
        selectSpecific_ClaimStatus(ClaimStatus.Open);
        
        clickMedSearch();
    }

    public String getRandomReadyReportableClaim() {

        List<String> claimNumbers = new ArrayList<>();
        String claimNumber = null;

        searchReadyToReportClaimsByStatus();
        claimNumbers = gatherClaimNumbersFromResults();

        claimNumber = StringsUtils.getRandomStringFromList(claimNumbers);

        return claimNumber;
    }

    private List<String> gatherClaimNumbersFromResults() {

        

        WebElement pagesElement = find(By.cssSelector("div[id*='gpaging-'] div[id*='tbtext-'][style*='256']"));
        int numPages = Integer.parseInt(pagesElement.getText().replace("of ", ""));

        List<String> claimNumbers = new ArrayList<>();

        for (int i = 1; i < numPages; i++) {
            
            List<WebElement> claimNumberElements = finds(By.cssSelector("div[id*='MedicareSection111Search'] a[id*=':ClaimNumber']"));
            


            for (WebElement element : claimNumberElements) {
                
                claimNumbers.add(element.getText());
            }
            clickNextPageLink();
        }

        return claimNumbers;
    }

    @FindBy(css = "a[data-qtip='Next Page']")
    private WebElement linkNextPage;

    private void clickNextPageLink() {
        clickWhenClickable(linkNextPage);
    }

    public boolean checkResultsWereGenerated() {
        boolean errorMessage = finds(By.className("message")).size() > 0;
        boolean resultsFound = false;
        if (errorMessage) {
            resultsFound = finds(By.xpath("//div[contains(@id,'MedicareSection111SearchLV-body')]//tr")).size() == 0;
            Assert.assertTrue(resultsFound, "No rows should be found");
        } else {
            resultsFound = finds(By.xpath("//div[contains(@id,'MedicareSection111SearchLV-body')]//tr")).size() > 0;
            Assert.assertTrue(resultsFound, "Didn't seem to find any results when we expected some, condition");
        }
        return resultsFound;
    }

    private List<String> gatherFlagshipNumbers() {

        String resultsTableXpath = "//div[contains(@id,':MedicareSection111SearchLV')]";

        List<String> flagshipNums = null;

        if (checkResultsWereGenerated()) {
            flagshipNums = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Flagship ID");
        }

        return flagshipNums;
    }

    public String getRandomFlagshipNumber() {

        List<String> flagshipNums = gatherFlagshipNumbers();

        int randomNum = NumberUtils.generateRandomNumberInt(0, flagshipNums.size() - 1);

        return flagshipNums.get(randomNum);
    }

    private List<String> gatherReferredDates() {
        String resultsTableXpath = "//div[contains(@id,':MedicareSection111SearchLV')]";
        List<String> dates = null;
        if (checkResultsWereGenerated()) {
            dates = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Referred Date");
        }

        Iterator<String> it = dates.iterator();

        while (it.hasNext()) {
            String s = it.next();
            if (s.length() == 1) {
                it.remove();
            }
        }

        return dates;
    }

    public String getRandomReferredDate() {
        List<String> dates = gatherReferredDates();
        int randomNum = NumberUtils.generateRandomNumberInt(0, dates.size() - 1);

        return dates.get(randomNum);
    }

    private boolean gatherReadyToReport(YesOrNo respsonse) {
        String resultsTableXpath = "//div[contains(@id,':MedicareSection111SearchLV')]";
        List<String> rToReport = null;
        boolean resultsCorrect = false;
        if (checkResultsWereGenerated()) {
            rToReport = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Ready to Report");
        }

        for (String rToR : rToReport) {
            if (rToR.equals(respsonse.getValue())) {
                resultsCorrect = true;
            } else {
                resultsCorrect = false;
                break;
            }
        }
        return resultsCorrect;
    }

    private void validateReadyToReportFilter(YesOrNo response) {
        boolean resultCorrect = gatherReadyToReport(response);

        Assert.assertTrue(resultCorrect, "The filter found a result that wasn't expected.");

    }

    private boolean gatherAdjusterNames(String aName) {
        String resultsTableXpath = "//div[contains(@id,':MedicareSection111SearchLV')]";
        boolean resultsCorrect = false;
        List<String> adjusterNames = null;

        if (checkResultsWereGenerated()) {
            adjusterNames = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Adjuster");

            for (String name : adjusterNames) {
                if (name.equals(aName)) {
                    resultsCorrect = true;
                } else {
                    resultsCorrect = false;
                    break;
                }
            }
        }
        return resultsCorrect;
    }

    private void validateAdjusterNameFilter(String aName) {
        boolean filterWorked = gatherAdjusterNames(aName);
        Assert.assertTrue(filterWorked, "The filter returned a result other than what was expected.");
    }

    private void validateClaimStatusFilter(ClaimStatus status) {
        boolean filterWorked = gatherClaimStatuses(status);
        Assert.assertTrue(filterWorked, "The filter returned a result other than what was expected.");
    }

    private boolean gatherClaimStatuses(ClaimStatus status) {
        String resultsTableXpath = "//div[contains(@id,':MedicareSection111SearchLV')]";
        boolean resultsCorrect = false;
        List<String> cStatuses = null;
        if (checkResultsWereGenerated()) {
            cStatuses = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath(resultsTableXpath)), "Status");
        }

        for (String cStatus : cStatuses) {
            if (cStatus.equals(status.toString())) {
                resultsCorrect = true;
            } else {
                resultsCorrect = false;
                break;
            }
        }
        return resultsCorrect;
    }

}
