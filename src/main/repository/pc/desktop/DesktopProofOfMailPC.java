package repository.pc.desktop;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class DesktopProofOfMailPC extends BasePage {

	private TableUtils tableUtils;
	
    public DesktopProofOfMailPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//input[contains(@id,'DesktopProofOfMail:DesktopProofOfMailScreen:DesktopProofOfMailLV:0:_Checkbox')]")
    public WebElement checkbox_DesktopProofOfMailCheckbox;

    @FindBy(linkText = "Preview")
    public WebElement button_DesktopProofOfMailPreview;

    @FindBy(xpath = "//a[contains(@id,'DesktopProofOfMailLV_tb:MarkAsMailedButton')]")
    public WebElement button_DesktopProofOfMailMarkedAsRead;

    @FindBy(xpath = "//a[contains(@id,':DesktopProofOfMailLV_tb:DeleteButton')]")
    public WebElement button_DesktopProofOfMailDelete;

    @FindBy(xpath = "//div[contains(@id,'DesktopProofOfMail:DesktopProofOfMailScreen:PLDesktopProofOfMailLV:DesktopProofOfMailLV')]")
    public WebElement table_PLProofOfMail;

    @FindBy(xpath = "//div[contains(@id,'DesktopProofOfMail:DesktopProofOfMailScreen:CLDesktopProofOfMailLV:DesktopProofOfMailLV')]")
    public WebElement table_CLProofOfMail;

    @FindBy(xpath = "//span[contains(@id, 'DesktopProofOfMail:DesktopProofOfMailScreen:PLProofOfMailTab-btnEl')]")
    public WebElement tab_PersonalLinesProofOfMail;

    @FindBy(xpath = "//span[contains(@id, 'DesktopProofOfMail:DesktopProofOfMailScreen:CLProofOfMailTab-btnEl')]")
    public WebElement tab_CommercialLinesProofOfMail;


    public void clickDesktopProofOfMailCheckbox() {
        waitUntilElementIsClickable(checkbox_DesktopProofOfMailCheckbox);
        checkbox_DesktopProofOfMailCheckbox.click();
    }


    public void clickDesktopProofOfMailPreview() {
        waitUntilElementIsVisible(button_DesktopProofOfMailPreview);
        waitUntilElementIsClickable(button_DesktopProofOfMailPreview);
        button_DesktopProofOfMailPreview.click();
    }

    //YES I KNOW THIS MAY CREATE A POSSIBLE INFINITE LOOP :(
    public void checkAccountDocuments(String acctNum) {
        List<WebElement> checkboxes = finds(By.xpath("//a[contains(text(), '" + acctNum + "')]/../../../td[1]/div"));
        for (WebElement checkbox : checkboxes) {
        	if(checkbox.getAttribute("class").contains("-checked")) {
        		//do nothing it is already checked.
        	} else {
        		checkbox.click();
        	}
        }
    }


    public void clickDesktopProofOfMailMarkedAsRead() {
        clickWhenClickable(button_DesktopProofOfMailMarkedAsRead);
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public void clickDesktopProofOfMailDelete() {
        waitUntilElementIsClickable(button_DesktopProofOfMailDelete);
        button_DesktopProofOfMailDelete.click();
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public ArrayList<String> getPolicyNumberFromPLTable() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_PLProofOfMail, "Policy #");
    }


    public ArrayList<String> getPolicyNumberFromCLTable() {
        return tableUtils.getAllCellTextFromSpecificColumn(table_CLProofOfMail, "Policy #");
    }


    public boolean verifyPersonalLinesProofOfMailTabExists() {
        try {
            waitUntilElementIsVisible(tab_PersonalLinesProofOfMail, 200);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean verifyCommercialLinesProofOfMailTabExists() {
        try {
            waitUntilElementIsVisible(tab_CommercialLinesProofOfMail, 200);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public void setCheckBoxInPLTable(int row) {
        tableUtils.setCheckboxInTable(table_PLProofOfMail, row, true);
    }


    public void setCheckBoxInCLTable(int row) {
        tableUtils.setCheckboxInTable(table_CLProofOfMail, row, true);
    }


    public int getPLProofOfMailsNum() {
        return Integer.parseInt(tab_PersonalLinesProofOfMail.getText().replaceAll("[^0-9]", ""));
    }


    public int getCLProofOfMailsNum() {
        return Integer.parseInt(tab_CommercialLinesProofOfMail.getText().replaceAll("[^0-9]", ""));
    }


    public void clickCommercialLinesProofOfMailTab() {
//        waitUntilElementIsClickable(tab_CommercialLinesProofOfMail);
//        tab_CommercialLinesProofOfMail.click();
        clickWhenClickable(tab_CommercialLinesProofOfMail);
        waitForPostBack();
    }


    public String getDescriptionFromTablebyPolicyNo(String polNo) {
        int row = tableUtils.getRowNumberInTableByText(table_PLProofOfMail, polNo);
        return tableUtils.getCellTextInTableByRowAndColumnName(table_PLProofOfMail, row, "Description");

    }
}
