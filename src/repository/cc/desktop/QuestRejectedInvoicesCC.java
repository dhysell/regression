package repository.cc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class QuestRejectedInvoicesCC extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;


    public QuestRejectedInvoicesCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id,':DesktopQuestRejectedInvoiceSearchDV:ControlNumber-inputEl')]")
    WebElement input_ControlNumber;

    private void inputControlNumber(String controlNumber) {
        input_ControlNumber.sendKeys(controlNumber);
    }

    private Guidewire8Select select_Status() {
        return new Guidewire8Select(driver,"//table[contains(@id,':status-triggerWrap')]");
    }


    private void selectSpecific_Status(String status) {
        Guidewire8Select mySelect = select_Status();
        mySelect.selectByVisibleTextPartial(status);
    }


    @FindBy(xpath = "//a[contains(@id,':DesktopQuestRejectedInvoiceSearchDV:Search')]")
    WebElement button_Search;

    public void clickSearchButton() {
        clickWhenClickable(button_Search);
    }

    public void searchByControlNumberRandom() {

        this.waitUtils.waitUntilElementIsClickable(button_Search);
        selectSpecific_Status("Pending");
        

        List<WebElement> controlNumberElements = getControlNumbers();
        String selectedControlNumber;

        int selection = NumberUtils.generateRandomNumberInt(0, controlNumberElements.size() - 1);

        WebElement controlNumberLink = controlNumberElements.get(selection);
        String controlNumber = controlNumberLink.getText();
        selectedControlNumber = controlNumber;

        inputControlNumber(controlNumber);
        
        clickSearchButton();

        

        List<WebElement> controlNumberResults = getControlNumbers();

        for (WebElement controlNumberResult : controlNumberResults) {
            if (!controlNumberResult.getText().equalsIgnoreCase(selectedControlNumber)) {
                Assert.fail("Control Number results do not match search criteria.");
            }
        }

    }

    public void testCommentsField() {
        selectSpecific_Status("All");
        
        List<WebElement> controlNumberResults = getControlNumbers();
        String comment = "Test Comments Here.";
        String commentContents;

        String controlNumber = controlNumberResults.get(0).getText();
        clickWhenClickable(By.xpath("//a[contains(text(),'" + controlNumber + "')]"));
        

        find(By.xpath("//a[contains(@id,':DesktopSGRejectedInvoiceDetailScreen:Edit')]")).click();
        

        QuestRejectedInvoiceCC invoice = new QuestRejectedInvoiceCC(this.driver);
        invoice.clearCommentsTextArea();
        invoice.setCommentsTextArea(comment);
        invoice.clickUpdateButton();
        
        try {
            commentContents = find(By.xpath("//div[contains(@id,'"
                    + ":DesktopQuestRejectedInvoiceLV-body')]/div/table/tbody/tr[1]/td[10]/div")).getText();
            if (!commentContents.equalsIgnoreCase(comment)) {
                Assert.fail("'Comments' field may not have been properly updated.");
            }
        } catch (Exception e) {
            Assert.fail("Unable to read 'Comments' field.");
        }

    }

    private List<WebElement> getControlNumbers() {
        return finds(By.xpath("//div[contains(@id,'"
                + ":DesktopQuestRejectedInvoiceLV-body')]/div/table/tbody/tr/td[1]"));
    }

}
