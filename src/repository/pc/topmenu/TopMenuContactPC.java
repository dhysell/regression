package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuContactPC extends TopMenuPC {

    public TopMenuContactPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:ContactTab:NewContact-itemEl')]")
    public WebElement New_Contact;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:ContactTab:NewContact:NewCompany-itemEl')]")
    public WebElement New_Company;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:ContactTab:NewContact:NewPerson-itemEl')]")
    public WebElement New_Person;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:ContactTab:Search-itemEl')]")
    public WebElement Search;


    public void clickNewCompany() {
        clickContactArrow();
        hoverOver(New_Contact);
        clickWhenVisible(New_Company);
    }


    public void clickNewPerson() {
        clickContactArrow();
        hoverOver(New_Contact);
        clickWhenVisible(New_Person);
    }


    public void clickSearch() {
        clickContactArrow();
        clickWhenVisible(Search);
    }

}
