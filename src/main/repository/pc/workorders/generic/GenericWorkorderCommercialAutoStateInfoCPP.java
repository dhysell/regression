package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.StateInfo.Un_UnderInsuredMotoristLimit;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderCommercialAutoStateInfoCPP extends GenericWorkorder {
	
	private WebDriver driver;

    public GenericWorkorderCommercialAutoStateInfoCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, ':BAStateInfoScreen:exclusionCondition:CoveragesCardTab-btnEl')]")
    private WebElement link_CoveragesTab;

    @FindBy(xpath = "//span[contains(@id, ':exclusionCondition:ExclusionsAndConditionsCardTab-btnEl')]")
    private WebElement link_ExclutionsAndConditionsTab;

    @FindBy(xpath = "//span[contains(@id, ':InsBridgeQuoute-btnEl')]")
    private WebElement button_Quote;
	
	/*@FindBy(xpath = "//div[contains(@class, 'message')]")
	private List<WebElement> errorMessages;*/

    Guidewire8Checkbox checkbox_UnderInsuredMotoristCA3118() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Underinsured Motorists - Bodily Injury CA 31 18')]/preceding-sibling::table");
    }

    Guidewire8Select select_UnderinsuredMotoristLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Underinsured Motorists - Bodily Injury CA 31 18')]/ancestor::legend[1]/following-sibling::div/descendant::table[2]");
    }

    Guidewire8Checkbox checkbox_UninsuredMotoristCA3115() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Uninsured Motorists - Bodily Injury CA 31 15')]/preceding-sibling::table");
    }

    Guidewire8Select select_UninsuredMotoristLimit() {
        return new Guidewire8Select(driver, "//div[contains(text(), 'Uninsured Motorists - Bodily Injury CA 31 15')]/ancestor::legend[1]/following-sibling::div/descendant::table[2]");
    }
	
	
	/*public List<WebElement> getErrorMessages() {
		return errorMessages;
	}*/


    public boolean checkUnderinsuredMotoristCA3118(boolean checked) {
        checkbox_UnderInsuredMotoristCA3118().select(checked);
        return checked;
    }


    public void selectUnderinsuredMotoristLimit(Un_UnderInsuredMotoristLimit limit) {
        Guidewire8Select mySelect = select_UnderinsuredMotoristLimit();
        mySelect.selectByVisibleText(limit.getValue());
    }


    public String getUnderinsuredMotoristLimit() {
        Guidewire8Select mySelect = select_UnderinsuredMotoristLimit();
        return mySelect.getText();
    }


    public boolean checkUninsuredMotoristCA3115(boolean checked) {
        checkbox_UninsuredMotoristCA3115().select(checked);
        return checked;
    }


    public void selectUninsuredMotoristLimit(Un_UnderInsuredMotoristLimit limit) {
        Guidewire8Select mySelect = select_UninsuredMotoristLimit();
        mySelect.selectByVisibleText(limit.getValue());
    }


    public String getUninsuredMotoristLimit() {
        Guidewire8Select mySelect = select_UninsuredMotoristLimit();
        return mySelect.getText();
    }


    public void fillOutCommercialAutoStateInfo(GeneratePolicy policy) {
        if (checkUnderinsuredMotoristCA3118(policy.commercialAutoCPP.getCPP_CAStateInfo().isUnderinsuredMotoristCA3118())) {
            selectUnderinsuredMotoristLimit(policy.commercialAutoCPP.getCPP_CAStateInfo().getUnderInsuredLimit());
        }
        if (checkUninsuredMotoristCA3115(policy.commercialAutoCPP.getCPP_CAStateInfo().isUninsuredMotoristCA3115())) {
            selectUninsuredMotoristLimit(policy.commercialAutoCPP.getCPP_CAStateInfo().getUninsueredMotorist());
        }
        clickWhenClickable(link_ExclutionsAndConditionsTab);
    }


    public void clickExclusionsandconditionsTab() {
        clickWhenClickable(link_ExclutionsAndConditionsTab);
    }


    public void clickBack() {
        super.clickBack();
    }


    public void clickNext() {
        super.clickNext();
    }


    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }


    public void clickSaveDraft() {
        super.clickGenericWorkorderSaveDraft();
    }


    public void clickFullApp() {
        super.clickGenericWorkorderFullApp();
    }

}
