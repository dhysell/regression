package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderInlandMarineCoverageSelectionCPP extends BasePage {

    private WebDriver driver;
    public GenericWorkorderInlandMarineCoverageSelectionCPP(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ///////////////////////
    //       Tabs        //
    ///////////////////////

    @FindBy(xpath = "//span[contains(@id , ':IMCoveragePartTab-btnInnerEl')]")
    private WebElement link_CoveragePartSelectionTabCPP;

    public void clickCoveragePartSelectionTab() {
        clickWhenClickable(link_CoveragePartSelectionTabCPP);
    }

    @FindBy(xpath = "//span[contains(@id , ':IMExclAndCondTab-btnEl')]")
    private WebElement link_ExclusionsConditionsTabCPP;

    public void clickExclusionsConditionsTab() {
        clickWhenClickable(link_ExclusionsConditionsTabCPP);
    }


    /////////////////////////
    //Coverage Part Section//
    /////////////////////////

    //getRadioXpath
    private String getRadioXpath(InlandMarineCoveragePart InlandMarineCoveragePart) {
        return "//div[contains(text() , '" + InlandMarineCoveragePart.getValue() + "')]/parent::td/preceding-sibling::td/div/img";
    }

    private void setCoveragePart(InlandMarineCoveragePart InlandMarineCoveragePart) {
        dragAndDrop(find(By.xpath(getRadioXpath(InlandMarineCoveragePart))), 2, 0);
    }

    Guidewire8Checkbox checkbox_CommIMEndorsement_IDCM_31_4004() {
        return new Guidewire8Checkbox(driver, "//div[contains(text(), 'Commercial Inland Marine Manuscript Endorsement IDCM 31 4004')]/preceding-sibling::table");
    }
	
	/*private boolean checkCommIMEndorsement_IDCM_31_4004(boolean checked) {
		checkbox_CommIMEndorsement_IDCM_31_4004().select(checked);
		return checked;
	}*/

    public void fillOutInlandMarineCoveragePartSelection(GeneratePolicy policy) {
        clickCoveragePartSelectionTab();

        for (InlandMarineCoveragePart coveragePart : policy.inlandMarineCPP.getCoveragePart()) {
            setCoveragePart(coveragePart);
        }

        //TODO Exclusions and Conditions
        clickExclusionsConditionsTab();

    }//end fillOutPropertyCoverages(CPPCommercialProperty_Building building)
}
