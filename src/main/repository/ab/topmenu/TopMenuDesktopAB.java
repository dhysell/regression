package repository.ab.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;

public class TopMenuDesktopAB extends TopMenuAB {

    public TopMenuDesktopAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopActivities-itemEl')]")
    public WebElement Activities;

    @FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:DesktopGroup_DesktopQueuedActivities-itemEl')]")
    public WebElement Queues;

    @FindBy(how = How.XPATH, using = "//a[starts-with(@id,'TabBar:DesktopTab:Desktop_DesktopProofOfMail')]")
    public WebElement Proof_of_Mail;

    public void clickMyActivities(AbUsers user) {
    	clickDesktopArrow(user);
        clickWhenVisible(Activities);
    }

    public void clickMyQueues(AbUsers user) {
        clickDesktopArrow(user);
        clickWhenVisible(Queues);
    }

    public void clickProofOfMail() {
        clickWhenVisible(Proof_of_Mail);
    }

    public void clickBulkAgentChange() {
        // TODO Auto-generated method stub

    }

}
