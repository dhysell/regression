package repository.cc.claim.incidents;

import com.idfbins.enums.YesOrNo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.cc.claim.searchpages.SearchAddressBook;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class EditPropertyIncidents extends BasePage {

    private WebDriver driver;

    public EditPropertyIncidents(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, ':PropertyDescription-inputEl')]")
    private WebElement propertyDescription;

    private void setPropertyDescription(String input) {
        waitUntilElementIsClickable(propertyDescription).sendKeys(input);
    }

    @FindBy(xpath = "//textarea[contains(@id,':Description-inputEl')]")
    private WebElement inputDamageDescription;

    private void setDamageDescription(String input) {
        inputDamageDescription.clear();
        inputDamageDescription.sendKeys(input);
    }

    private Guidewire8Select selectBoxOwner() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Owner_Picker-triggerWrap')]");
    }

    private void selectOwner() {
        selectBoxOwner().selectByVisibleTextRandom();
    }

    @FindBy(xpath = "//a[contains(@id,':EditFixedPropertyIncidentScreen:Update')]")
    private WebElement buttonOk;

    private void clickOkButton() {
        clickWhenClickable(buttonOk);
    }

    @FindBy(xpath = "//a[contains(@id,':EditFixedPropertyIncidentScreen:Edit')]")
    private WebElement button_Edit;

    private void clickEditButton() {
        clickWhenClickable(button_Edit);
    }

    @FindBy(xpath = "//a[contains(@id,':Owner_Picker:Owner_PickerMenuIcon')]")
    private WebElement pickerOwner;

    @FindBy(xpath = "//a[contains(@id,':Owner_Picker:MenuItem_Search-itemEl')]")
    private WebElement linkSearchFromPicker;

    private Guidewire8Select select_MoldInvolved() {
        return new Guidewire8Select(driver, "//table[contains(@id,':LossLocation_Mold-triggerWrap')]");
    }

    private Guidewire8Select select_FireSubcause() {
        return new Guidewire8Select(driver, "//table[contains(@id,':Fire_Subcause-triggerWrap')]");
    }

    private Guidewire8Select selectBoxEquipmentBreakdown() {
        return new Guidewire8Select(driver, "//table[contains(@id,':EquipmentBreakdownSubcause_FBM-triggerWrap')]");
    }

    private void selectEquipmentBreakdownRandom() {
        selectBoxEquipmentBreakdown().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectBoxPeril() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PeriSubcause_FBM-triggerWrap')]");
    }

    private void selectPerilRandom() {
        selectBoxPeril().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectBoxResultantDamage() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ResultantDmgSubcause_FBM-triggerWrap')]");
    }

    private void selectResultandDamageRandom() {
        selectBoxResultantDamage().selectByVisibleTextRandom();
    }

    private Guidewire8Select selectBoxContributingFactor() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ContributFactSubcause_FBM-triggerWrap')]");
    }

    private void selectContributingFactorRandom() {
        selectBoxContributingFactor().selectByVisibleTextRandom();
    }


    private void clickSearchForOwner() {
        clickWhenClickable(pickerOwner);
        
        clickWhenClickable(linkSearchFromPicker);
    }

    public void property() {
        setPropertyDescription("Existing Property");
        
        setDamageDescription("Edit Damage Description");
        
        selectOwner();
        
        clickOkButton();
        
    }

    public void propertyEquipmentBreakdown() {
        setPropertyDescription("Existing Property");
        
        setDamageDescription("Edit Damage Description Equipment Breakdown");
        
        selectOwner();
        
        selectEquipmentBreakdownRandom();
        
        selectPerilRandom();
        
        selectResultandDamageRandom();
        
        selectContributingFactorRandom();
        

        clickOkButton();
        
    }

    public void thirdPartyProperty() {
        setPropertyDescription("Existing Property");
        
        setDamageDescription("Has third party owner.");
        
        clickSearchForOwner();
        

        SearchAddressBook ab = new SearchAddressBook(this.driver);
        ab.selectDefaultThirdParty();

        
        clickOkButton();
        
    }

    public void totalLossProperty() {
        clickEditButton();
        setPropertyDescription("Existing Property");
        setDamageDescription("Edit Damage Description");
        selectOwner();
        selectMoldInvolved(YesOrNo.No);
        selectFireSubcause();
        isTotalLoss(YesOrNo.Yes);
        clickOkButton();

    }

    private void isTotalLoss(YesOrNo no) {
        String xpath = no.equals(YesOrNo.Yes) ? "//input[contains(@id,':TotalLoss_true-inputEl')]" : "//input[contains(@id,':TotalLoss_false-inputEl')]";
        clickWhenClickable(find(By.xpath(xpath)));

    }

    private void selectMoldInvolved(YesOrNo no) {
    	Guidewire8Select mySelect = select_MoldInvolved();
        mySelect.selectByVisibleText(no.getValue());

    }

    private void selectFireSubcause() {
    	Guidewire8Select mySelect = select_FireSubcause();
        mySelect.selectByVisibleTextRandom();

    }

}
