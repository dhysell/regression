package repository.pc.activity;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ActivtyRequestType;
import repository.pc.policy.PolicyMenu;

public class NewActivityRequestPC extends BasePage {


    public NewActivityRequestPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(text(), 'New Activity')]/parent::a/parent::div[contains(@id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity')]")
    private WebElement link_NewActivity;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity:NewActivityMenuItemSet:5:NewActivityMenuItemSet_Category')]")
    private WebElement link_NewActivityRequest;

    @FindBy(xpath = "//div[contains(@id, 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity:NewActivityMenuItemSet:5:NewActivityMenuItemSet_Category:24:item')]")
    public WebElement link_NewActivityRequestPleaseReturnRenewalPaperwork;

    /**********************************************************************************************************************************************************************************************************************
     * Helper Methods
     ************************************************************************************************************************************************************************************************************************************************************/
    public void selectByVisibleText(ActivtyRequestType selectString) {
        // TODO Verify if scroll to element is needed.
        By xpath = By.xpath("//span[contains(text(), '" + selectString.getValue() + "')]/parent::a/parent::div");

        if(finds(xpath).size() > 0 ){
            clickWhenClickable(finds(xpath).get(0));
        }

//        List<WebElement> activity = finds(
//                By.xpath("//span[contains(text(), '" + selectString.getValue() + "')]/parent::a/parent::div"));
//        if (!activity.isEmpty()) {
//            scrollToElement(activity.get(0));
//            activity.get(0).click();
//        }

        // List<WebElement> elementList =
        // finds(By.xpath("//div[contains(@id,
        // 'PolicyFile:PolicyFileMenuActions:PolicyFileMenuActions_Create:PolicyFileMenuActions_NewActivity:NewActivityMenuItemSet:5:NewActivityMenuItemSet_Category:15:item')]/../div"));
        // for(int i = 0; i < elementList.size(); i++){
        // systemOut(elementList.get(i).getText());
        // scrollToElement(elementList.get(i));
        // if(elementList.get(i).getText().equalsIgnoreCase(selectString.getValue()))
        // {
        //
        // scrollToElement(elementList.get(i));
        //
        // elementList.get(i).click();
        // break;
        // }
        // }
    }

    public void clickAction() {
        repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(getDriver());
        policyMenu.clickMenuActions();
    }


    public void initiateActivity(ActivtyRequestType activity) {
        clickAction();
        hoverOverAndClick(link_NewActivity);
        hoverOverAndClick(link_NewActivityRequest);
        selectByVisibleText(activity);
    }
}
