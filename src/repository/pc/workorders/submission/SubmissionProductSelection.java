package repository.pc.workorders.submission;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.infobar.InfoBar;
import repository.pc.sidemenu.SideMenuPC;

public class SubmissionProductSelection extends BasePage {

    private WebDriver driver;

    public SubmissionProductSelection(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Guidewire8Select select_SubmissionProductSelectionQuoteType() {
        return new Guidewire8Select(driver, "//*[@id='NewSubmission:NewSubmissionScreen:ProductSettingsDV:QuoteType-triggerWrap']");
    }

    @FindBy(xpath = "//select[@id='NewSubmission:NewSubmissionScreen:ProductSettingsDV:DefaultBaseState']")
    public WebElement select_SubmissionProductSelectionState;

    @FindBy(xpath = "//input[@id='NewSubmission:NewSubmissionScreen:ProductSettingsDV:DefaultPPEffDate-inputEl']")
    public WebElement editbox_SubmissionProductSelectionEffectiveDate;

    // jon larsen R2
    // this table element was I think transfered over from V7 and never used
    // here in V8 till now
    // @FindBy(xpath =
    // "//table[@id='NewSubmission:NewSubmissionScreen:ProductOffersDV:ProductSelectionLV']")
    // public WebElement table_SubmissionProductSelectionProduct;

    @FindBy(xpath = "//div[contains(@id, 'NewSubmission:NewSubmissionScreen:ProductOffersDV:ProductSelectionLV-body')]/div/table")
    public WebElement table_SubmissionProductSelectionProduct;

    public WebElement button_SubmissionProductSelectionProductSelect(ProductLineType productToSelect) {
        // jon larsen R2
        // will choose the select button for the product type needed.
        return table_SubmissionProductSelectionProduct.findElement(By.xpath(".//child::tr/child::td/div[contains(text(), '" + productToSelect.getName() + "')]/../preceding-sibling::td/div/a[contains(@id, ':addSubmission')]"));

        // WebElement product =
        // find(By.xpath("//a[@id='NewSubmission:NewSubmissionScreen:ProductOffersDV:ProductSelectionLV:0:addSubmission']"));
        // return product;
    }

    public WebElement button_SubmissionDoneProductSelectionProductSelect(ProductLineType productToSelect) {

        String xpathToUse = "//td[contains(.,'" + productToSelect.getName() + "')]/preceding-sibling::td//img[contains(@id,':DoneSubmission')]";
        return table_SubmissionProductSelectionProduct.findElement(By.xpath(xpathToUse));
    }


    public void setSubmissionProductSelectionQuoteType(QuoteType quoteType) {
        Guidewire8Select mySelect = select_SubmissionProductSelectionQuoteType();
        switch (quoteType) {
            case FullApplication:
                mySelect.selectByVisibleText(quoteType.getValue());
                break;
            case QuickQuote:
                mySelect.selectByVisibleText(quoteType.getValue());
                break;
        }
    }


    public void setSubmissionProductSelectionEffectiveDate(String effectiveDate) {
    	setText(editbox_SubmissionProductSelectionEffectiveDate, effectiveDate);
    }


    public void clickSubmissionProductSelectionSelect(ProductLineType product) {
        // waitUntilElementIsVisible(select_SubmissionProductSelectionQuoteType());
        clickWhenClickable(button_SubmissionProductSelectionProductSelect(product));
    }


    public boolean checkIfSelectButtonAvailable(ProductLineType product) {
        // waitUntilElementIsVisible(select_SubmissionProductSelectionQuoteType());
        return checkIfElementExists(button_SubmissionProductSelectionProductSelect(product), 5000);
    }


    public String startQuoteSelectProductAndGetAccountNumber(QuoteType quoteType, ProductLineType product) throws Exception {
        setSubmissionProductSelectionQuoteType(quoteType);
        clickSubmissionProductSelectionSelect(product);
        if(product.equals(ProductLineType.PersonalUmbrella)) {
        	new SideMenuPC(driver).clickSideMenuPolicyInfo();
        }
        InfoBar infoBarStuff = new InfoBar(getDriver());
        return infoBarStuff.getInfoBarAccountNumber();

    }


    public boolean checkSelectButtonExists(ProductLineType product) {
        return checkIfElementExists("//div[contains(@id, 'NewSubmission:NewSubmissionScreen:ProductOffersDV:ProductSelectionLV-body')]/div/table//child::tr/child::td/div[contains(text(), '" + product.getName() + "')]/../preceding-sibling::td/div/a[contains(@id, ':addSubmission')]", 5000);
    }

}
