package repository.pc.workorders.change;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.helpers.TableUtils;
import repository.pc.workorders.generic.GenericWorkorder;

import java.util.List;

public class PolicyChangeReview extends GenericWorkorder {

	private TableUtils tableUtils;
	
    public PolicyChangeReview(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[contains(@id,':PolicyChangeWizard_DifferencesScreen:DifferencesPanelSet:DiffTreePanelSet:DiffTreePanelLV') or contains(@id, ':IssuanceWizard_PolicyReviewScreen:DifferencesPanelSet:DiffTreePanelSet:0')]")
    WebElement table_PolicyChangeReview;

    @FindBy(xpath = "//span[contains(@id, ':OOSConflictsTab-btnInnerEl')]")
    private WebElement link_ChangeConflicts;

    @FindBy(xpath = "//span[contains(@id, ':OOSConflictPanelSet:ConflictTableLV_tb:OverrideAll-btnInnerEl')]")
    private WebElement button_OverrideAll;

    @FindBy(xpath = "//a[contains(@id, ':OOSConflictPanelSet:Done')]")
    private WebElement button_Submit;


    public String getPolicyChangeValue(String coverageType, String type) {

        WebElement element = find(By.xpath("//tr[contains(.,'" + coverageType + "')]/following-sibling::tr/td/div/span[text()='" + type + "']/../../../td[3]"));

        String value = element.getText();
        return value;
    }


    public void expandAllTreeNodes() {

        List<WebElement> treeNodes = tableUtils.getAllTableRows(table_PolicyChangeReview);

        for (WebElement element : treeNodes) {

            try {
                if (element.getAttribute("class").contains("x-grid-tree-node-expanded") || element.getAttribute("class").contains("x-grid-tree-node-leaf")) {
                    continue;
                }
                WebElement node = element.findElement(By.xpath(".//td/div/img[contains(@class,'expander')]"));

                clickWhenClickable(node);
                expandAllTreeNodes();
            } catch (Exception e) {
                continue;
            }

        }

    }


    public int PolicyReviewRowCount() {
        return tableUtils.getRowCount(table_PolicyChangeReview);
    }

    public String getPolicyChangeValue(String itemName) {

        int rownumber = tableUtils.getRowNumberInTableByText(table_PolicyChangeReview, itemName);
        String itemValue = table_PolicyChangeReview.findElement(By.xpath(".//tr[" + rownumber + "]/td[3]")).getText();

        return itemValue;
    }


    public void clickQuote() {
        super.clickGenericWorkorderQuote();
    }


    public void clickChangeConflictsTab() {
        clickWhenClickable(link_ChangeConflicts);
    }


    public void clickOverrideAllButton() {
        clickWhenClickable(button_OverrideAll);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public void clickSubmitButton() {
        clickWhenClickable(button_Submit);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


}
