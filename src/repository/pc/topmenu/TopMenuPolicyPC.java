package repository.pc.topmenu;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;

public class TopMenuPolicyPC extends TopMenuPC {

    public TopMenuPolicyPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // @FindBy(xpath =  "//a[@id='TabBar:PolicyTab-btnWrap']")
    // public WebElement Policy;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:PolicyTab_NewSubmission')]")
    public WebElement New_Submission;

    @FindBy(xpath =  "//input[starts-with(@id,'TabBar:PolicyTab:PolicyTab_SubmissionNumberSearchItem-inputEl')]")
    public WebElement Submission_Number_Search_Input;

    @FindBy(xpath =  "//div[starts-with(@id,'TabBar:PolicyTab:PolicyTab_SubmissionNumberSearchItem_Button')]")
    public WebElement Submission_Number_Search_Button;

    @FindBy(xpath =  "//input[starts-with(@id,'TabBar:PolicyTab:PolicyTab_PolicyRetrievalItem-inputEl')]")
    public WebElement Policy_Number_Search_Input;

    @FindBy(xpath =  "//div[starts-with(@id,'TabBar:PolicyTab:PolicyTab_PolicyRetrievalItem_Button')]")
    public WebElement Policy_Number_Search_Button;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:0:PolicyMenuPolicy-itemEl')]")
    public WebElement First;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:1:PolicyMenuPolicy-itemEl')]")
    public WebElement Second;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:2:PolicyMenuPolicy-itemEl')]")
    public WebElement Third;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:3:PolicyMenuPolicy-itemEl')]")
    public WebElement Fourth;

    @FindBy(xpath =  "//a[starts-with(@id,'TabBar:PolicyTab:4:PolicyMenuPolicy-itemEl')]")
    public WebElement Fifth;


    public void clickNewSubmission() {
        clickPolicyArrow();
        long endTime = new Date().getTime() + 5000;
        while (finds(By.xpath("//div[contains(@id, 'TabBar:PolicyTab:PolicyTab_NewSubmission')]/parent::div/parent::div")).size() <= 0 && new Date().getTime() < endTime) {
            clickPolicyArrow();
        }
        clickWhenClickable(New_Submission);
    }


    public void searchForSubmissionNumber(String submissionNumber) {
        clickPolicyArrow();
        waitUntilElementIsVisible(Submission_Number_Search_Input);
        setText(Submission_Number_Search_Input, submissionNumber);
        clickWhenClickable(Submission_Number_Search_Button);
    }


    public void searchForPolicyNumber(String policyNumber) {
        clickPolicyArrow();
        waitUntilElementIsVisible(Policy_Number_Search_Input);
        setText(Policy_Number_Search_Input, policyNumber);
        clickWhenClickable(Policy_Number_Search_Button);
    }


    public void clickFirst() {
        clickPolicyArrow();
        clickWhenVisible(First);
    }


    public void clickSecond() {
        clickPolicyArrow();
        clickWhenVisible(Second);
    }


    public void clickThird() {
        clickPolicyArrow();
        clickWhenVisible(Third);
    }


    public void clickFourth() {
        clickPolicyArrow();
        clickWhenVisible(Fourth);
    }


    public void clickFifth() {
        clickPolicyArrow();
        clickWhenVisible(Fifth);
    }


    public void clickSixth() {
        // TODO Auto-generated method stub

    }


    public void clickSeventh() {
        // TODO Auto-generated method stub

    }


    public void clickEighth() {
        // TODO Auto-generated method stub

    }


    public void clickNinth() {
        // TODO Auto-generated method stub

    }


    public void clickTenth() {
        // TODO Auto-generated method stub

    }


    public boolean newSubmissionExists() {
        clickPolicyArrow();
        return checkIfElementExists("//div[contains(@id, 'TabBar:PolicyTab:PolicyTab_NewSubmission')]/parent::div/parent::div", 1000);

    }

}
