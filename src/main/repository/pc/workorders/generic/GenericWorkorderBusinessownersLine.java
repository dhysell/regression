package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderBusinessownersLine extends BasePage {

    private WebDriver driver;

    public GenericWorkorderBusinessownersLine(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected void onBusinessOwnersLine() {
        if (finds(By.xpath("//span[contains(@class, 'g-title') and contains(text(), 'Businessowners Line')]")).isEmpty()) {
            new SideMenuPC(driver).clickSideMenuBusinessownersLine();
            waitForPostBack();
        }
    }


    //FILL OUT BUSINESS OWNERS LINE
    public void fillOutBusinessownersLinePages(boolean basicSearch, PolicyBusinessownersLine busOwnLineObj) throws Exception {
        GenericWorkorderBusinessownersLineIncludedCoverages boLineIncludedCoverages = new GenericWorkorderBusinessownersLineIncludedCoverages(getDriver());
        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages boLineAdditionalCoverage = new GenericWorkorderBusinessownersLineAdditionalCoverages(getDriver());
        repository.pc.workorders.generic.GenericWorkorderBusinessownersLineExclusionsConditions boLineExclusionConditions = new GenericWorkorderBusinessownersLineExclusionsConditions(getDriver());
       
        onBusinessOwnersLine();
        boLineIncludedCoverages.fillOutIncludedCoverages(basicSearch, busOwnLineObj);
        boLineAdditionalCoverage.fillOutBusinessOwnersLineAdditionalCoverages(busOwnLineObj);
        boLineExclusionConditions.fillOutBusinessOwnersLineExclusionsConditions(busOwnLineObj);
        boLineIncludedCoverages.addAdditionalInsureds(basicSearch, busOwnLineObj);
    }


    public void clickBusinessownersLine_IncludedCoverages() {
        onBusinessOwnersLine();
        clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesIncludedCoverages);
        waitForPostBack();
    }


    public void clickBusinessownersLine_AdditionalCoverages() {
        onBusinessOwnersLine();
        clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesAdditionalCoverages);
        waitForPostBack();
    }


    public void clickBusinessownersLine_ExclusionsConditions() {
        onBusinessOwnersLine();
//        scrollToElement(tab_SubmissionBusinessownersLineIncludedCoveragesExclusionsConditions);
        waitForPostBack();
        clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesExclusionsConditions);
        waitForPostBack();
        while (finds(By.xpath("//span[contains(text(), 'Other Liability Exclusions')]")).size() <= 0) {
            clickWhenClickable(tab_SubmissionBusinessownersLineIncludedCoveragesExclusionsConditions);
            waitForPostBack();
        }
        waitForPostBack();
    }


    @FindBy(xpath = "//span[contains(@id, ':BOPScreen:BOP_IncludedCardnTab-btnInnerEl')]")
    private WebElement tab_SubmissionBusinessownersLineIncludedCoveragesIncludedCoverages;

    @FindBy(xpath = "//span[contains(@id, ':BOPScreen:BOP_AdditionalCardTab-btnInnerEl')]")
    private WebElement tab_SubmissionBusinessownersLineIncludedCoveragesAdditionalCoverages;

    @FindBy(xpath = "//span[contains(@id, ':BOPScreen:ExclusionsAndConditionsCardTab-btnInnerEl')]")
    private WebElement tab_SubmissionBusinessownersLineIncludedCoveragesExclusionsConditions;


}
















