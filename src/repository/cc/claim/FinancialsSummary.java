package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;

import java.util.List;

public class FinancialsSummary extends BasePage {
    public FinancialsSummary(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//img[contains(@id,'FinancialsSummaryLabelMenuIcon')]")
    public List<WebElement> pickers_Financials;

    @FindBy(xpath = "//a/span[contains(.,'Create Check')]")
    public WebElement link_CreateCheck;

    // HELPERS
    // ==============================================================================

    // Set arrayNumber to -1 for random selection
    public void useFinancialsPickers(int arrayNumber) {

//		CheckWizard checkWiz = new CheckWizard(this.driver);
//		checkWiz.abilityToPay();
        waitUntilElementIsClickable(By.xpath("//img[contains(@id,'FinancialsSummaryLabelMenuIcon')]"));
        List<WebElement> financialPickers = finds(By.xpath("//img[contains(@id,'FinancialsSummaryLabelMenuIcon')]"));
        int numPickers = (financialPickers.size());

        if (arrayNumber == -1) {
            arrayNumber = NumberUtils.generateRandomNumberInt(1, numPickers);
            System.out.println(arrayNumber);
        }

        try {
            clickWhenClickable(financialPickers.get(arrayNumber - 1));
        } catch (Exception e) {
            clickWhenClickable(financialPickers.get(0));
        }

        clickWhenClickable(link_CreateCheck);

    }

}
