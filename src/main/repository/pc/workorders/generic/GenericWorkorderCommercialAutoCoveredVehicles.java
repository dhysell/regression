package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderCommercialAutoCoveredVehicles extends BasePage {

    private WebDriver driver;

    public GenericWorkorderCommercialAutoCoveredVehicles(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':EditAutoSymbols-btnEl')]")
    public WebElement button_EditCoveredVehicles;

    @FindBy(xpath = "//td[contains(@id, 'HOWizardStepGroup:HOLineReview')]/parent::tr")
    public WebElement link_SideMenuPropertyGlLineReview;

    public void clickEditCoveredVehicles() {
        clickWhenClickable(button_EditCoveredVehicles);
    }

    public void checkLiabiltiyAny(boolean checked) {
        WebElement table_CoveredAuto = find(By.xpath("//div[contains(@id, 'CoveredAutoSymbolsPanelSet:CoveredAutoSymbolsLV')]"));

        new TableUtils(getDriver()).setCheckboxInTable(table_CoveredAuto, new TableUtils(getDriver()).getRowNumberInTableByText(table_CoveredAuto, "Liability"), checked);
//		clickAndHoldAndRelease(find(By.xpath("//div[contains(text(), 'Liability')]/parent::td/following-sibling::td/div/img")));
    }

    public void setCoveredVehiclesPage(GeneratePolicy policy) {

        Login login = new Login(driver);
        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);

        if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote)) {
            if (policy.commercialAutoCPP.isSetCoveredVehicle()) {
                repository.pc.workorders.generic.GenericWorkorder genwo = new GenericWorkorder(driver);
                genwo.clickGenericWorkorderSaveDraft();
                gwHelpers.logout();
                login.loginAndSearchSubmission("hhill", "gw", policy.accountNumber);
                repository.pc.sidemenu.SideMenuPC sidemenu = new repository.pc.sidemenu.SideMenuPC(driver);
                sidemenu.clickSideMenuCACoveredVehicles();

                clickEditCoveredVehicles();
                checkLiabiltiyAny(true);

                genwo.clickGenericWorkorderSaveDraft();
                gwHelpers.logout();
                login.loginAndSearchSubmission(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(), policy.accountNumber);
                sidemenu = new SideMenuPC(driver);
                sidemenu.clickSideMenuCACoveredVehicles();
            }
        }
    }

}
