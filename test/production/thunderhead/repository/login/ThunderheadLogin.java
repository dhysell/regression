package production.thunderhead.repository.login;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;

public class ThunderheadLogin extends BasePage {
    public ThunderheadLogin(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
	
	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath="//input[@name='j_username']")
	public WebElement editbox_LoginUsername;
	
	@FindBy(xpath="//input[@name='j_password']")
	public WebElement editbox_LoginPassword;
	
	@FindBy(xpath="//input[(@class='button') and (@type='submit') and (@value='Login')]")
	public WebElement button_LoginLogin;
	
	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------
	public void setLoginUserame(String userName) {
		waitUntilElementIsVisible(editbox_LoginUsername);
		editbox_LoginUsername.sendKeys(userName);
	}
	
	public void setLoginPassword(String password) {
		waitUntilElementIsVisible(editbox_LoginPassword);
		editbox_LoginPassword.sendKeys(password);
	}
	
	public void clickLoginLogin() {
		clickWhenClickable(button_LoginLogin);
	}
	
	public void login(String userName, String password) {
		setLoginUserame(userName);
		setLoginPassword(password);
		clickLoginLogin();
	}
}
