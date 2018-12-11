package repository.pc.workorders.cancellation;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.helpers.TableUtils;
import repository.pc.workorders.generic.GenericWorkorder;

public class RelatedInterests extends GenericWorkorder {

    public RelatedInterests(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@id, 'PendingCancelWizard:PendingCancelWizard_InterestsScreen:1')]")
    private WebElement table_RelatedInterests;

    @FindBy(xpath = "//a[contains(@id, 'PendingCancelWizard:PendingCancelWizard_InterestsScreen:addRelatedAdditionalInterests')]")
    private WebElement link_addRelatedAdditionalInterests;


    public int getRelatedInterestDisplayedRowNumberByName(String name) {
        return new TableUtils(getDriver()).getRowNumberInTableByText(table_RelatedInterests, name);
    }


    public void selectRelatedInterestByRow(int row) {
        new TableUtils(getDriver()).setCheckboxInTable(table_RelatedInterests, row, true);
    }


    public void clickAddRelatedAdditionalInterestsButton() {
        clickWhenClickable(link_addRelatedAdditionalInterests);
    }


    public void clickFinishButton() {
        super.clickFinish();
    }

}
