package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.GaragekeepersCoverage.CollisionDeductible;
import repository.gw.enums.GaragekeepersCoverage.ComprehensiveDeductible;
import repository.gw.enums.GaragekeepersCoverage.LegalOrPrimary;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CPPCommercialAutoGarageKeepers;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;

import java.util.List;

public class GenericWorkorderGaragekeepersCoverageCPP extends BasePage {

    private WebDriver driver;

    public GenericWorkorderGaragekeepersCoverageCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //////////////////////////////////
    ////     PUBLIC METHODS   ////////
    //////////////////////////////////

    /**
     * @param policy
     * @Description - Set Auto Garage Keepers Page
     */
    public void fillOutGaragekeepersCoverageLine(GeneratePolicy policy) {
        for (CPPCommercialAutoGarageKeepers keeper : policy.commercialAutoCPP.getGarageKeepers()) {
            if (new GuidewireHelpers(getDriver()).getCurrentPolicyType(policy).equals(GeneratePolicyType.QuickQuote) || !checkIfElementExists(editbox_Limit, 10)) {
                setNewGarageKeeperLocation(keeper);
            } else {
                setExistingGarageKeeperLocation(keeper);
            }
        }
    }

    public List<WebElement> getValidationMessages() {
        List<WebElement> errors = validationMessages;
        return errors;
    }

    public void selectAllGarageKeepers() {
        new TableUtils(getDriver()).setTableTitleCheckAllCheckbox(table_GaragekeepersCoverageLocationTable, true);
    }

    public void clickRemove() {
        super.clickRemove();
    }

    /**
     * @param rowNumber
     * @Description - Select a location to be edited or view First row number is 0
     */
    public void clickGaragekeeperLocationByRowNumber(int rowNumber) {
        table_GaragekeepersCoverageLocationTable.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + String.valueOf(rowNumber) + "')]")).click();
    }


    //////////////////////////////////
    /////     PAGE ELEMENTS   ////////
    //////////////////////////////////

    @FindBy(xpath = "//span[contains(@id,':exclusionCondition:toolbar:ToolbarButton-btnInnerEl')]")
    private WebElement button_AddExistingLocation;

    @FindBy(xpath = "//span[contains(@id,':toolbar:ToolbarButton:0:menuLocation-textEl')]")
    private WebElement text_FirstExistingAddress;

    @FindBy(xpath = "//div[contains(text(), 'Garagekeepers Coverage')]/ancestor::legend/following-sibling::div/descendant::label[contains(text(), 'Limit')]/parent::td/following-sibling::td/input")
    private WebElement editbox_Limit;

    @FindBy(xpath = "//div[contains(@id,':BAGarageKeepersScreen:exclusionCondition:locationLV-body')]/parent::div")
    private WebElement table_GaragekeepersCoverageLocationTable;

    @FindBy(xpath = "//div[@class='message']")
    private List<WebElement> validationMessages;


    private Guidewire8Checkbox checkbox_Collision() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(),'Collision')]/preceding-sibling::table");
    }

    private Guidewire8Checkbox checkbox_Comprehensive() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(),'Comprehensive')]/preceding-sibling::table");
    }

    private Guidewire8Checkbox checkbox_SpecifiedCausesOfLoss() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(),'Specified Causes of Loss')]/preceding-sibling::table");
    }

    private Guidewire8Select select_LegalOrPrimary() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':TypekeyTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_CollisionDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':OptionTermInput-triggerWrap')]");
    }

    private Guidewire8Select select_ComprehensiveDeductible() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PackageTermInput-triggerWrap')]");
    }

//	Guidewire8Select select_SpecifiedCausesOfLoss() {
//		return new Guidewire8Select(driver, "//table[contains(@id, ':TypekeyTermInput-triggerWrap')]");
//	}

	/*private Guidewire8Select select_SpecifiedCausesOfLossDeductible() {
		return new Guidewire8Select(driver, "//table[contains(@id, ':TypekeyTermInput-triggerWrap')]");
	}*/

    //////////////////////////////////
    /////   PRIVATE METHODS   ////////
    //////////////////////////////////

	/*private void setGaragekeepersCoverage() {
		clickWhenClickable(button_AddExistingLocation);
		delay(100);
		clickWhenClickable(text_FirstExistingAddress);
	}*/

    /**
     * @param limit
     * @Description - Puts number in Limit under Garagekeepers Coverage section
     */
    private void setLimit(String limit, LegalOrPrimary legalOrPrimary) {
        clickWhenClickable(editbox_Limit);
        editbox_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Limit.sendKeys(limit);
        Guidewire8Select mySelect = select_LegalOrPrimary();
        mySelect.selectByVisibleText(legalOrPrimary.getValue());
    }

	/*private void clickCollision(boolean checked) {
		checkbox_Collision().select(checked);
	}*/

    private void setCollision(CollisionDeductible collisionDeductible) {
        Guidewire8Select mySelect = select_CollisionDeductible();
        mySelect.selectByVisibleText(collisionDeductible.getValue());
    }

	/*private void clickComprehensive() {
		checkbox_Collision().select(true);
	}*/

    private void setComprehensive(ComprehensiveDeductible comprehensiveDeductible) {
        Guidewire8Select mySelect = select_ComprehensiveDeductible();
        mySelect.selectByVisibleText(comprehensiveDeductible.getValue());
    }

	/*private void clickSpecifiedCausesOfLoss() {
		checkbox_Collision().select(true);
	}*/

//	public void setSpecifiedCausesOfLoss(SpecifiedCausesOfLoss specifiedCausesOfLoss) {
//		Guidewire8Select mySelect = select_SpecifiedCausesOfLoss();
//		mySelect.selectByVisibleText(specifiedCausesOfLoss.getValue());
//	}

	/*private void setSpecifiedCausesOfLossDeductible(SpecifiedCausesOfLossDeductible specifiedCausesOfLossDeductible) {
		Guidewire8Select mySelect = select_SpecifiedCausesOfLossDeductible();
		mySelect.selectByVisibleText(specifiedCausesOfLossDeductible.getValue());
	}*/


    public List<WebElement> getLocationList() {
        clickWhenClickable(button_AddExistingLocation);
        return finds(By.xpath("//span[contains(@id, ':menuLocation')]"));
    }

    public void addGarageKeeperByLocation(CPPCommercialAutoGarageKeepers keeper) {
        clickWhenClickable(button_AddExistingLocation);
//		translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'test'
        clickWhenClickable(find(By.xpath("//span[contains(text(), '" + keeper.getAddress().getLine1() + "')]/parent::a/parent::div")));
//		find(By.xpath("//span[contains(text(), '" + keeper.getAddress().getLine1() + "')]/parent::a/parent::div")).click();
    }

    private void fillOutCollision(CPPCommercialAutoGarageKeepers keeper) {
        if (keeper.isCollision()) {
            checkbox_Collision().select(true);
            setCollision(keeper.getCollisionDeductible());
        }
    }

    private void fillOutComprehensive(CPPCommercialAutoGarageKeepers keeper) {
        if (keeper.isComprehensive()) {
            checkbox_Comprehensive().select(true);
            setComprehensive(keeper.getComprehensiveDeductible());
        }
    }

    private void fillOutSpecifiedCausesOfLoss(CPPCommercialAutoGarageKeepers keeper) {
        if (keeper.isSpecifiedCausesOfLoss()) {
            checkbox_SpecifiedCausesOfLoss().select(true);
            //set specified cause of loss
        }
    }

    private void setNewGarageKeeperLocation(CPPCommercialAutoGarageKeepers keeper) {
//		for(CPPCommercialAutoGarageKeepers keeper : policy.commercialAutoCPP.getGarageKeepers()) {
        addGarageKeeperByLocation(keeper);
        setLimit(String.valueOf(keeper.getGarageKeeperCoverageLimit()), keeper.getLegalOrPrimary());
        fillOutCollision(keeper);
        fillOutComprehensive(keeper);
        fillOutSpecifiedCausesOfLoss(keeper);
//		}
    }

    private void setExistingGarageKeeperLocation(CPPCommercialAutoGarageKeepers keeper) {
//		for(CPPCommercialAutoGarageKeepers keeper : policy.commercialAutoCPP.getGarageKeepers()) {
        setLimit(String.valueOf(keeper.getGarageKeeperCoverageLimit()), keeper.getLegalOrPrimary());
        fillOutCollision(keeper);
        fillOutComprehensive(keeper);
        fillOutSpecifiedCausesOfLoss(keeper);
//		}
    }


}
