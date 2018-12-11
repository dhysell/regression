package repository.gw.helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PopUpWindow {
	private WebDriver driver;
	private String mainWindowHandle;
	
	
	public PopUpWindow(WebDriver driver, WebElement elementToClick) throws InterruptedException {
		this.driver = driver;
		mainWindowHandle = driver.getWindowHandle();
		
		elementToClick.click();
		
		Thread.sleep(2);//2 seconds for popup window to spawn
		
	    for (String activeHandle : driver.getWindowHandles()) {
	        if (!activeHandle.equals(mainWindowHandle)) {
	            driver.switchTo().window(activeHandle);
	            getTitle();
	        }
	    }
		
	}
	
	private void getTitle(){
		System.out.println("Driver is currently focused on window titled: "+driver.getTitle());
		//System.out.println("Driver is currently focused on window with Handle: "+driver.getWindowHandle());
	};
	
	
	public void closePopUp(){
			driver.close();
	}
	
	public void returnFocusToOriginalWindow() {
		driver.switchTo().window(mainWindowHandle);
		getTitle();
	}
}
