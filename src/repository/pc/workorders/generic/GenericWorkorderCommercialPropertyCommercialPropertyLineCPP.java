package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;
import repository.pc.sidemenu.SideMenuPC;

import java.util.Date;
import java.util.List;

public class GenericWorkorderCommercialPropertyCommercialPropertyLineCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialPropertyCommercialPropertyLineCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * @param policy
     * @throws Exception
     * @Description FILL OUT COMMERCIAL PROPERTY LINE WIZARD STEP
     */
    public void fillOutCommercialPropertyLine(GeneratePolicy policy) throws Exception {
        fillOutCoveragesTab(policy);
        fillOutExclusionsAndConditionsTab(policy);
        fillOutUnderwritingQuestionsTab(policy);
    }


    /**
     * @param GeneratePolicy policy
     * @Description FILL OUT COMMERCIAL PROPERTY LINE COVERAGES TAB
     */
    public void fillOutCoveragesTab(GeneratePolicy policy) {
        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages coverages = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_Coverages(getDriver());
        coverages.fillOutCoverages(policy);
    }

    /**
     * @param GeneratePolicy policy
     * @throws Exception
     * @Description FILL OUT COMMERCIAL PROPERTY LINE EXCLUSIONS AND CONDITIONS TAB
     */
    public void fillOutExclusionsAndConditionsTab(GeneratePolicy policy) throws Exception {
        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_ExclusionsConditions exclusionsAndConditions = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_ExclusionsConditions(getDriver());
        exclusionsAndConditions.fillOutExclusionsAndConditions(policy);
    }

    /**
     * @param GeneratePolicy policy
     * @Description FILL OUT COMMERCIAL PROPERTY LINE UNDERWRITING QUESTIONS TAB
     */
    public void fillOutUnderwritingQuestionsTab(GeneratePolicy policy) {
        GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions underwritingQuestion = new GenericWorkorderCommercialPropertyCommercialPropertyLineCPP_UWQuestions(getDriver());
        underwritingQuestion.fillOutUnderwritingQuestions(policy);
    }


    private boolean isOnCommercialPropertyWizardStep() {
        List<WebElement> title = finds(By.xpath("//span[contains(@id, ':CPWizardStepGroup:CPLineScreen:ttlBar') and contains(text(), 'Commercial Property Line')]"));
        return !title.isEmpty();
    }

    private boolean isOnCommercialPropertyLineCoveragesTab() {
        List<WebElement> title = finds(By.xpath("//span[(@class='g-title') and (text()='Property')]"));
        return !title.isEmpty();
    }

    private boolean isOnCommercialPropertyLineExclusionsAndConditionaTab() {
        List<WebElement> title = finds(By.xpath("//span[(@class='g-title') and (text()='Exclusions')]"));
        return !title.isEmpty();
    }

    private boolean isOnCommercialPropertyLineUnderwritingQuestionsTab() {
        List<WebElement> title = finds(By.xpath("//label[(text()='General Questions')]"));
        return !title.isEmpty();
    }

    private boolean isOnCommercialPropertyLineAdditoianlInterestTab() {
        List<WebElement> title = finds(By.xpath("//span[(text()='Search')]"));
        return !title.isEmpty();
    }

    @FindBy(xpath = "//span[contains(@id, ':CoveragesCardTab-btnEl')]")
    private WebElement link_CoveragesTab;

    public void clickCoveragesTab() {
        if (!isOnCommercialPropertyWizardStep()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
            sideMenu.clickSideMenuCPCommercialPropertyLine();
        }
        clickWhenClickable(link_CoveragesTab);
        long endtime = new Date().getTime() + 5000;
        boolean onPage = isOnCommercialPropertyLineCoveragesTab();
        while (!onPage && new Date().getTime() < endtime) {
            sleep(1); //Unsure if this is still needed.
            onPage = isOnCommercialPropertyLineCoveragesTab();
        }
        if (!onPage) {
            Assert.fail("UNABLE TO GET TO COVERAGES TAB");
        }
    }

    @FindBy(xpath = "//span[contains(@id, ':ExclusionsAndConditionsCardTab-btnEl')]")
    private WebElement link_ExclusionsConditionsTab;

    public void clickExclusionsConditionsTab() {
        if (!isOnCommercialPropertyWizardStep()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
            sideMenu.clickSideMenuCPCommercialPropertyLine();
        }
        clickWhenClickable(link_ExclusionsConditionsTab);
        long endtime = new Date().getTime() + 5000;
        boolean onPage = isOnCommercialPropertyLineExclusionsAndConditionaTab();
        while (!onPage && new Date().getTime() < endtime) {
            sleep(1); //Unsure if this is still needed.
            onPage = isOnCommercialPropertyLineExclusionsAndConditionaTab();
        }
        if (!onPage) {
            Assert.fail("UNABLE TO GET TO EXCLUSIONS AND CONDITOINS TAB");
        }
    }

    @FindBy(xpath = "//span[contains(@id, ':UnderwritingQuestionsCardTab-btnEl')]")
    private WebElement link_UnderwritingQuestionsTab;

    public void clickUnderwritingQuestionsTab() {
        if (!isOnCommercialPropertyWizardStep()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
            sideMenu.clickSideMenuCPCommercialPropertyLine();
        }
        clickWhenClickable(link_UnderwritingQuestionsTab);
        long endtime = new Date().getTime() + 5000;
        boolean onPage = isOnCommercialPropertyLineUnderwritingQuestionsTab();
        while (!onPage && new Date().getTime() < endtime) {
            sleep(1); //Unsure if this is still needed.
            onPage = isOnCommercialPropertyLineUnderwritingQuestionsTab();
        }
        if (!onPage) {
            Assert.fail("UNABLE TO GET TO UNDERWRITING QUESTIONS TAB");
        }
    }

    @FindBy(xpath = "//span[contains(@id, ':UnderwritingQuestionsCardTab-btnEl')]")
    private WebElement link_AdditioanlInterestTab;

    public void clickAdditioanlInterestTab() {
        if (!isOnCommercialPropertyWizardStep()) {
            repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
            sideMenu.clickSideMenuCPCommercialPropertyLine();
        }
        clickWhenClickable(link_AdditioanlInterestTab);
        long endtime = new Date().getTime() + 5000;
        boolean onPage = isOnCommercialPropertyLineAdditoianlInterestTab();
        while (!onPage && new Date().getTime() < endtime) {
            sleep(1); //Unsure if this is still needed.
            onPage = isOnCommercialPropertyLineAdditoianlInterestTab();
        }
        if (!onPage) {
            Assert.fail("UNABLE TO GET TO ADDITIOANL INTEREST TAB");
        }
    }


}























