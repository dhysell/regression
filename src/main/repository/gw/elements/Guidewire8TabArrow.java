package repository.gw.elements;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;



public class Guidewire8TabArrow {

	private WebDriver classDriver;
	private WebElement tabElement;
	
	public Guidewire8TabArrow(WebDriver driver, String tabXpath) {
		this.classDriver = driver;
		this.tabElement = classDriver.findElement(By.xpath(tabXpath));
	}

	public void clear() {
		tabElement.clear();
	}

	public void click() {
		Actions builder = new Actions(classDriver);

		Action test = null;
		
		test = builder.moveToElement(tabElement, tabElement.getSize().getWidth() - 20, 18).click().build();
		
		test.perform();
	}

	public WebElement findElement(By arg0) {
		return tabElement.findElement(arg0);
	}

	public List<WebElement> findElements(By arg0) {
		return tabElement.findElements(arg0);
	}

	public String getAttribute(String arg0) {
		return tabElement.getAttribute(arg0);
	}

	public String getCssValue(String arg0) {
		return tabElement.getCssValue(arg0);
	}

	public Point getLocation() {
		return tabElement.getLocation();
	}

	public Dimension getSize() {
		return tabElement.getSize();
	}

	public String getTagName() {
		return tabElement.getTagName();
	}

	public String getText() {
		return tabElement.getText();
	}

	public boolean isDisplayed() {
		return tabElement.isDisplayed();
	}

	public boolean isEnabled() {
		return tabElement.isEnabled();
	}

	public boolean isSelected() {
		return tabElement.isSelected();
	}

	public void sendKeys(CharSequence... arg0) {
		tabElement.sendKeys(arg0);
	}

	public void submit() {
		tabElement.submit();
	}
}
