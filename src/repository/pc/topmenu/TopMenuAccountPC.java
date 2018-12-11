package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuAccountPC extends TopMenuPC {

    public TopMenuAccountPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:AccountTab_NewAccount-itemEl')]")
    public WebElement New_Account;

    @FindBy(xpath =  "//input[starts-with(@id,'TabBar:AccountTab:AccountTab_AccountNumberSearchItem-inputEl')]")
    public WebElement Search_Input;

    @FindBy(xpath =  "//div[starts-with(@id,'TabBar:AccountTab:AccountTab_AccountNumberSearchItem_Button')]")
    public WebElement Search_Button;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:0:accountItem-itemEl')]")
    public WebElement First;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:1:accountItem-itemEl')]")
    public WebElement Second;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:2:accountItem-itemEl')]")
    public WebElement Third;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:3:accountItem-itemEl')]")
    public WebElement Fourth;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:AccountTab:4:accountItem-itemEl')]")
    public WebElement Fifth;


    public void clickNewAccount() {
        clickAccountArrow();
        clickWhenVisible(New_Account);
    }


    public void searchFor(String account) {
        clickAccountArrow();
        Search_Input.sendKeys(account);
        clickWhenVisible(Search_Button);
    }


    public void clickFirst() {
        clickAccountArrow();
        clickWhenVisible(First);
    }


    public void clickSecond() {
        clickAccountArrow();
        clickWhenVisible(Second);
    }


    public void clickThird() {
        clickAccountArrow();
        clickWhenVisible(Third);
    }


    public void clickFourth() {
        clickAccountArrow();
        clickWhenVisible(Fourth);
    }


    public void clickFifth() {
        clickAccountArrow();
        clickWhenVisible(Fifth);
    }
}
