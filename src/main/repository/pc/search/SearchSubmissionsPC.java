package repository.pc.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.TableUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.topmenu.TopMenuSearchPC;

import java.util.ArrayList;

public class SearchSubmissionsPC extends BasePage {

    public SearchSubmissionsPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, ':PolicySearchDV:SubmissionNumber-inputEl')]")
    private WebElement editbox_SearchSubSubmissionNumber;
    
    @FindBy(xpath = "//input[contains(@id, 'AccountNumber-inputEl') or contains(@id, ':AccountCriterion-inputEl') or contains(@id, ':AccountNumberCriterion-inputEl') or contains(@id, 'accountNumberInput-inputEl') or contains(@id, ':AccountNumber-inputEl')]")
    protected WebElement editBox_SearchSubmissionsPCAccountNumber;
    
    @FindBy(xpath = "//div[contains(@id, 'SubmissionSearch:SubmissionSearchScreen:PolicySearch_ResultsLV')]")
    private WebElement table_SearchSubmissionsPCSearchResults;

    private void getToSubmissionSearch() {
    	TopMenuSearchPC topMenu = new TopMenuSearchPC(getDriver());
        topMenu.clickSubmissions();
    }
    
    public String selectSubmission() {
    	String submissionJobNumber = "";
    	if(finds(By.xpath("//div[contains(@id, ':PolicySearch_ResultsLV-body')]/div/table/tbody/child::tr[last()]/child::td[3]/div/a")).isEmpty()) {
    		clickWhenClickable(find(By.xpath("//div[contains(@id, ':PolicySearch_ResultsLV-body')]/div/table/tbody/child::tr[last()]/child::td[5]/div/a")));
    		new AccountSummaryPC(getDriver()).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Membership);
    	} else {
    		WebElement submissionLink = waitUntilElementIsVisible(find(By.xpath("//div[contains(@id, ':PolicySearch_ResultsLV-body')]/div/table/tbody/child::tr[last()]/child::td[3]/div/a")));
    		submissionJobNumber = submissionLink.getText();
    		clickWhenClickable(submissionLink);
    	}
    	return submissionJobNumber;
    }
    
    private void setAccountAccountNumber(String numToFill) {
		clickReset();
		clickWhenClickable(editBox_SearchSubmissionsPCAccountNumber);
		editBox_SearchSubmissionsPCAccountNumber.sendKeys(numToFill);
	}
    
    public void searchSubmissionWithoutSelecting(String accountNumber) {
    	getToSubmissionSearch();
        setAccountAccountNumber(accountNumber);
        clickSearch();
        waitForPostBack();
    }

    public String searchAndSelectSubmission(String accountNumber) {
    	getToSubmissionSearch();
        setAccountAccountNumber(accountNumber);
        clickSearch();
        return selectSubmission();
    }
    
    public String searchSubmission(GeneratePolicy policy) {
        return searchAndSelectSubmission(policy.accountNumber);
    }
    
    public SearchSubmissionsPC setSearchSubmissionNumber(String submission) {
        waitUntilElementIsVisible(editbox_SearchSubSubmissionNumber);
        editbox_SearchSubSubmissionNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_SearchSubSubmissionNumber.sendKeys(submission);
        return this;
    }

    public int getSearchResultsRows() {
        int rows = new TableUtils(getDriver()).getRowCount(table_SearchSubmissionsPCSearchResults);
        return rows;
    }

    public ArrayList<String> getStatusOfSubmissions() {
        return new TableUtils(getDriver()).getAllCellTextFromSpecificColumn(table_SearchSubmissionsPCSearchResults, "Submission Status");
    }

    /**
     * @param policy SELECTS SUBMISSION OF CURRENT PRODUCT TYPE
     */
    public void selectSubmission(GeneratePolicy policy) {
        ProductLineType lineType = policy.productType;
        String submissionNumber = null;
        switch (policy.productType) {
            case Businessowners:
                submissionNumber = policy.busOwnLine.getSubmissionNumber();
                break;
            case CPP:
                submissionNumber = policy.commercialPackage.getSubmissionNumber();
                break;
            case Membership:
                submissionNumber = policy.membership.getSubmissionNumber();
                break;
            case PersonalUmbrella:
                submissionNumber = policy.squireUmbrellaInfo.getSubmissionNumber();
                break;
            case Squire:
                submissionNumber = policy.squire.getSubmissionNumber();
                break;
            case StandardFire:
                submissionNumber = policy.standardFire.getSubmissionNumber();
                break;
            case StandardIM:
                submissionNumber = policy.standardInlandMarine.getSubmissionNumber();
                break;
            case StandardLiability:
                submissionNumber = policy.standardLiability.getSubmissionNumber();
                break;
        }
        if (submissionNumber != null) {
            clickWhenClickable(find(By.xpath("//div[contains(@id, ':PolicySearch_ResultsLV-body')]/div/table/tbody/child::tr/child::td[3]/div/a[contains(text(), '" + submissionNumber + "')]")));
        } else {
            clickWhenClickable(find(By.xpath("//div[contains(@id, ':PolicySearch_ResultsLV-body')]/div/table/tbody/child::tr/child::td[6]/div[contains(text(), '" + lineType.getValue() + "')]/parent::td/parent::tr/child::td[3]/div/a")));
        }
    }
}













