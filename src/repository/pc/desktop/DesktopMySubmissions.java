package repository.pc.desktop;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.TableUtils;

public class DesktopMySubmissions extends BasePage {
	
	private WebDriver driver;

    public DesktopMySubmissions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // My Submission
    public Guidewire8Select select_DesktopMySubmissionsOptions() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'DesktopSubmissionsLV:submissionsFilter-triggerWrap')]");
    }


    // sub #
    @FindBy(xpath = "//select[contains(@id,'DesktopSubmissions:DesktopSubmissionsScreen:SubmissionSearch-inputEl')]")
    public WebElement select_DesktopMySubmissionsSub;

    // Magnifying glass
    @FindBy(xpath = "//select[contains(@id,'DesktopSubmissions:DesktopSubmissionsScreen:SubmissionSearch_Button/img')]")
    public WebElement select_DesktopMySubmissionsMag;

    @FindBy(xpath = "//div[contains(@id,'DesktopSubmissions:DesktopSubmissionsScreen:DesktopSubmissionsLV')]")
    public WebElement table_DesktopMySubmission;

    
    public void setDesktopMySubmissionsOptions(String statusToSelect) {
    	Guidewire8Select mySubmissionOptions = select_DesktopMySubmissionsOptions();
    	mySubmissionOptions.selectByVisibleTextPartial(statusToSelect);
    }


    public void setSub(String statusToSelect) {
        waitUntilElementIsVisible(select_DesktopMySubmissionsSub);
        new Select(select_DesktopMySubmissionsSub).selectByVisibleText(statusToSelect);

    }

    public String getSubmissionStatusByTransactionNo(String TransNum){
    	SortMySubmissionByColumn("Effective Date");
		int row = new TableUtils(getDriver()).getRowNumberInTableByText(table_DesktopMySubmission, TransNum);
		return new TableUtils(getDriver()).getCellTextInTableByRowAndColumnName(table_DesktopMySubmission, row, "Status");
}
    
    public void SortMySubmissionByColumn(String column) {
    	new TableUtils(getDriver()).sortByHeaderColumn(table_DesktopMySubmission, column);
    }
}
