package repository.driverConfiguration;

import com.idfbins.enums.OkCancel;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import repository.gw.helpers.WaitUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * This class is to hold all basic methods you need to interact with a page.
 */
public class BasePage extends WaitUtils {

    private WebDriver driver;

    public BasePage(WebDriver webDriver) {
        super(webDriver);
        this.driver = webDriver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void visit(String url) {
        driver.get(url);
    }

    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    public void systemOut(String outputMessage) {
        System.out.println(outputMessage);
    }

    public void refreshPage() {
        this.driver.navigate().refresh();
    }

    public WebElement find(By locator) {
        return this.driver.findElement(locator);
    }

    public List<WebElement> finds(By locator) {
        return driver.findElements(locator);
    }

    public String getTextOrValueFromElement(WebElement ele) {
    	waitUntilElementIsVisible(ele);
        if (ele.getTagName().equalsIgnoreCase("div")) {
            return ele.getText();
        } else {
            return ele.getAttribute("value");
        }
    }

    public String getTextOrValueFromElement(By locator) {
        if (find(locator).getTagName().equalsIgnoreCase("div")) {
            return find(locator).getText();
        } else {
            return find(locator).getAttribute("value");
        }
    }

    public void clickWhenClickable(WebElement ele) {
        waitUntilElementIsClickable(ele).click();
        waitForPostBack();
    }

    public void clickWhenClickable(By locator) {
        waitUntilElementIsClickable(locator).click();
        waitForPostBack();
    }

    public void clickWhenClickable(WebElement ele, int wait) {
        waitUntilElementIsClickable(ele, wait).click();
        waitForPostBack();
    }

    public void clickWhenClickable(By locator, int wait) {
        waitUntilElementIsClickable(locator, wait).click();
        waitForPostBack();
    }


    public void setText(WebElement textBox, String text) {
        waitUntilElementIsClickable(textBox);
        textBox.sendKeys(Keys.chord(Keys.CONTROL + "a"), text);
        clickProductLogo();
        waitForPostBack();
    }

    public void setTextHitEnter(WebElement textBox, String text) {
        waitUntilElementIsClickable(textBox);
        textBox.clear();
        textBox.sendKeys(text);
        textBox.sendKeys(Keys.ENTER);
        waitForPostBack();
    }

    public void clickWhenVisible(WebElement elementToClick) {
        elementToClick = this.waitUntilElementIsVisible(elementToClick);
        elementToClick.click();
        waitForPostBack();
    }

    public void clickWhenVisible(WebElement elementToClick, int wait) {
        elementToClick = this.waitUntilElementIsVisible(elementToClick, wait);
        elementToClick.click();
        waitForPostBack();
    }

    public void clickWhenVisible(By locator) {
        this.waitUntilElementIsVisible(locator).click();
        waitForPostBack();
    }

    public void clickWhenVisible(By locator, int wait) {
        this.waitUntilElementIsVisible(locator, wait).click();
        waitForPostBack();
    }


    public void sleep(int timeInSeconds) {
    	int normalizedTime = timeFixer(timeInSeconds);
        try {
        	Thread.sleep(normalizedTime * 1000); //Time Fixer will normalize all values to seconds, but Thread.sleep requires milliseconds as an argument, so we need to multiply by 1000 to get it back to milliseconds in this instance.
        } catch (Exception e) {
            systemOut(e.getMessage());
        }
    }

    /**
     * This method will check to see if an element is useable or not on a page. This is useful for elements that are displayed on a page,
     * but that are grayed-out, for instance. A normal check to see if an element exists will always return true in this instance,
     * even though the element is not actually useable.
     *
     * @param element WebElement to check
     * @return boolean disabled or not.
     */
    public boolean isElementDisabled(WebElement element) {
        return element.getAttribute("class").contains("x-item-disabled") || element.getAttribute("class").contains("x-disabled") || element.getAttribute("class").contains("x-btn-disabled");
    }

    // wait for element to be clickable
    // visible etc.

    /**
     * This method checks to see if an alert (pop-up) message is present on the page, indicating the need for some user interaction.
     *
     * @return boolean Alert present or not.
     */
    public boolean isAlertPresent() {
        try {
            find(By.xpath("//div[contains(@id, 'messagebox-1001_header-body')]/parent::div/parent::div/preceding-sibling::div[contains(@style, 'display: block')]"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getPopupTextContents() {
        String popUpTextContents = "";
        popUpTextContents = find(By.xpath("//div[contains(@id, 'messagebox-1001-displayfield-inputEl')]")).getText();
        return popUpTextContents;
    }

    public String selectOKOrCancelFromPopup(OkCancel okOrCancel) {
        String popUpTextContents = "";
        popUpTextContents = find(By.xpath("//div[contains(@id, 'messagebox-1001-displayfield-inputEl')]")).getText();
        WebElement okButton = find(By.xpath("//div[contains(@id,'messagebox-1001')]//div//a[contains(.,'OK')]"));
        WebElement cancelButton = find(By.xpath("//div[contains(@id,'messagebox-1001')]//div//a[contains(.,'Cancel')]"));

        switch (okOrCancel) {
            case OK:
                clickWhenClickable(okButton);
                waitForPostBack();
                break;
            case Cancel:
            	clickWhenClickable(cancelButton);
                waitForPostBack();
                break;
        }
        return popUpTextContents;
    }

    /**
     * Presses the Alt+Shift+I Key combination
     */
    public void pressAltShiftI() {
        sendArbitraryKeys(Keys.chord(Keys.ALT, Keys.SHIFT, "i"));
        waitForPostBack();
    }

    /**
     * Presses the Alt+Shift+T Key combination
     */
    public void pressAltShiftT() {
        sendArbitraryKeys(Keys.chord(Keys.ALT, Keys.SHIFT, "t"));
        waitForPostBack();
    }

    public void sendArbitraryKeys(Keys keysToSend) {
        (new Actions(driver)).sendKeys(keysToSend).perform();
        waitForPostBack();
    }

    public void sendArbitraryKeys(String keysToSend) {
        (new Actions(driver)).sendKeys(keysToSend).perform();
        waitForPostBack();
    }

    public void clickElementByCordinates(WebElement ele, int x, int y) {
    	waitUntilElementIsClickable(ele);
        Actions builder = new Actions(driver);
        builder.moveToElement(ele, x, y).click().build().perform();
        waitForPostBack();
    }

    public void uploadOrSaveFile(String filePath) throws AWTException {
        StringSelection ss = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        Robot robot = new Robot();
        if (((RemoteWebDriver)getDriver()).getCapabilities().getPlatform().is(Platform.LINUX)) {
        	robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_L);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        sleep(1); //Used to ensure that paste function has time to fully enter in the pop-up dialogue.
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        sleep(1); //Used to ensure that the pop-up dialogue closes fully before continuing.
        waitForPostBack();
    }

    public void dragAndDrop(WebElement elementToDrag, int xOffset, int yOffset) {
        Actions builder = new Actions(driver);
        Action dragAndDrop = builder.clickAndHold(elementToDrag)
                .moveByOffset(xOffset, yOffset)
                .release(elementToDrag)
                .build();
        dragAndDrop.perform();
        waitForPostBack();
    }

    public void hoverOverAndClick(WebElement element) {
//    	waitUntilElementIsClickable(element);
        element = find(By.xpath(getXpathFromElement(element)));
        Actions mouse = new Actions(this.driver);
        mouse.moveToElement(element);
        mouse.click();
        mouse.build().perform();
        waitForPostBack();
    }

    public void hoverOver(WebElement element) {
        element = waitUntilElementIsVisible(element);
        Actions mouse = new Actions(this.driver);
        mouse.moveToElement(element);
        mouse.build().perform();
        waitForPostBack();
    }
    
    /**
	 * Hovers over the first element to click the second. Use this if the second element doesn't exist until the first one is hovered over.
	 * 
	 * @param first The first element to hover over.
	 * @param secondXPath The xPath of the second.
	 * @return Whether this method succeeded in clicking the second element.
	 */
	public boolean hoverOverFirstToClickSecond(WebElement first, String secondXPath) {
		if (hoverOverFirstToShowSecond(first, secondXPath)) {
			WebElement second = find(By.xpath(secondXPath));
			clickWhenClickable(second);
			return true;
		}
		return false;
	}
	
	/**
	 * Hovers over the first element until the second shows up. Use this if the second element doesn't exist until the first one is hovered over.
	 * 
	 * @param first The first element to hover over.
	 * @param secondXPath The xPath of the second.
	 * @return Whether this method succeeded in under 25 tries.
	 */
	public boolean hoverOverFirstToShowSecond(WebElement first, String secondXPath) {
		WebElement second;
		int index = 0;
		boolean passed = false;
		do {
			index++;
			hoverOver(first);
			try {
				second = find(By.xpath(secondXPath));
				hoverOver(second);
				passed = true;
			} catch (Exception e) {
			}
		} while (!passed && index < 25);
		return passed;
	}

    /**
     * @author iclouser
     * @Description - This is meant to refocus the window and fire off postbacks.
     * @DATE - Jul 20, 2016
     */
    public void clickProductLogo() {
        clickWhenClickable(By.xpath("//img[contains(@class,'product-logo')]"));
        waitForPostBack();
    }

    public boolean checkIfElementExists(WebElement element, int checkTimeInSeconds) {
        try {
            waitUntilElementIsVisible(element, checkTimeInSeconds);
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

    public boolean checkIfElementExists(String xpath, int checkTimeInSeconds) {
    	int normalizedTime = timeFixer(checkTimeInSeconds);
    	boolean found = finds(By.xpath(xpath)).size() > 0;
    	
    	for (int i = 0; !found && i < normalizedTime; ++i) {
    		this.sleep(1);
	        found = finds(By.xpath(xpath)).size() > 0;
	    }
	    return found;
    }

    public String getXpathFromElement(WebElement element) {
        String[] xpathSplit = element.toString().split("-> xpath: ");
        String xPath = "";
        if (xpathSplit.length == 1) {
            xpathSplit = element.toString().split("By.xpath: ");
            if (xpathSplit.length == 1) {
                systemOut("The Element passed in is a proxy WebElement. As such, the original xPath cannot be extracted.");
            }
        }

        for (int i = 1; i < xpathSplit.length; ++i) {
            String sanitizedXPath = xpathSplit[i];
            if (sanitizedXPath.startsWith(".")) {
                sanitizedXPath = sanitizedXPath.substring(1, sanitizedXPath.length());
            }

            if (i == xpathSplit.length - 1) {
                xPath = xPath + sanitizedXPath.substring(0, sanitizedXPath.length() - 1);
            } else {
                xPath = xPath + sanitizedXPath.substring(0, sanitizedXPath.length() - 3);
            }
        }

        return xPath;
    }

    public void doubleClickMouse(int x, int y) throws Exception {
        int windowX = this.driver.manage().window().getPosition().getX();
        int offsetBecauseIWant = 10;

        Robot r = new Robot();
        r.mouseMove(windowX + offsetBecauseIWant + x, y);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(50);//Used to ensure 2 individual button clicks.
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForPostBack();
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void clickAndHoldAndRelease(WebElement elementToClick) {
        Actions action = new Actions(this.driver);
        action.clickAndHold(elementToClick).release().build().perform();
        waitForPostBack();
    }

    public void clickOK() {
        clickWhenClickable(By.xpath("//span[contains(text(), 'OK')]/parent::span"));
        waitForPostBack();
    }

    public void clickNext() {
        clickWhenClickable(By.xpath("//span[contains(@id, ':Next-btnEl') or contains(@id, ':Next-btnInnerEl') or contains(@id, ':nextButton') or contains(@id, 'NewPolicyChange-btnInnerEl')]"));
        waitForPostBack();
    }

    public void clickBack() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Prev') or contains(@id, 'SubmissionWizard:Prev')]"));
        waitForPostBack();
    }

    public void clickDone() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Done')]"));
        waitForPostBack();
    }

    public void clickComplete() {
        clickWhenClickable(By.xpath("//a[contains(@id, 'Complete')]"));
        selectOKOrCancelFromPopup(OkCancel.OK);
        waitForPostBack();
    }
    
    public void clickCompleteWithoutPopup() {
        clickWhenClickable(By.xpath("//a[contains(@id, 'Complete')]"));
        waitForPostBack();
    }
    
    public void clickDiscardUnsavedChangesLink() {
		if (checkIfElementExists("//div[contains(@id, ':_msgs')]/div/a[contains(., 'Discard Unsaved Change')]", 1000)) {
			clickWhenClickable(By.xpath("//div[contains(@id, ':_msgs')]/div/a[contains(., 'Discard Unsaved Change')]"));
			waitForPostBack();
		}
	}

    public void clickClose() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Close') or (contains(@id, 'NewDocumentFromTemplateScreen:ToolbarButton'))]"));
        waitForPostBack();
    }

    public void clickAdd() {
        clickWhenClickable(By.xpath("//span[contains(@id, ':Add-btnEl') or (contains(@id, ':AddEmptyPayments-btnInnerEl'))]"));
        waitForPostBack();
    }

    public void clickRemove() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Remove')]"));
        waitForPostBack();
    }

    public void clickUpdate() {
        clickWhenClickable(By.xpath("//a[contains(@id, 'Update')] | //span[contains(@id, ':FinishPCR-btnEl')]"));
        waitForPostBack();
    }

    public void clickFinish() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Finish')]"));
        waitForPostBack();
    }
    public void clickCancel() {
        clickWhenClickable(By.xpath("//a[contains(@id, 'LocationDetailPanelSet:fred:ToolbarButton') or contains (@id, 'LocationDetailPanelSet:fred:LocationToolbarButtonSet:ToolbarButton') or contains(@id, ':Cancel') or contains(@id, 'BOPSingleBuildingDetailScreen:ToolbarButton') or contains(@id, 'CancelButton')] | //span[contains(@id, ':Cancel-btnInnerEl')] | //span[contains(@id, 'HOBuilding_FBMPopup:ToolbarButton')]"));
        waitForPostBack();
    }

    public void clickEdit() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':EditLink') or contains(@id, ':Edit') or contains(@id, ':editbutton') or contains(@id, 'viewEdit_LinkAsBtn')]"));
        waitForPostBack();
    }

    public void clickReset() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Reset')]"));
        waitForPostBack();
    }

    public void clickSearch() {
        clickWhenClickable(By.xpath("//a[(contains(@id, ':SearchLinksInputSet:Search')) or contains (@id, ':SearchLinksDocumentsInputSet:Search') or (contains(@id, ':SearchLinksNotesInputSet:Search')) or (contains(@id, 'AdditionalInterestLV_tb:ToolbarButton')) or (contains(@id, 'AddlInterestContactSearchPopup:LienholderSearchPanelSet:Search')) or (contains(@id, ':ABContactSearchScreen:ContactSearchDV:Search')) or (contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:Search')) or (contains(@id, 'RecentlyViewedSearch:Search')) or (contains(@id, 'MenuItem_Search')) or (contains(@id, ':PhoneSearchDV:Search'))] |  //a[(contains(@id, ':InterestAddRemoveToolbarButtonSet:ToolbarButton'))]//span[(contains(text(), 'Search'))] | //*[contains(@id,':AddressSearchScreen:AddressSearchDV:Search')] | //a[contains(@id, 'SearchDV:Search')] | //a[contains(@id, ':LienholderSearchPanelSet:Search')]"));
        waitForPostBack();
    }

    public void clickOverride() {
        clickWhenClickable(By.xpath("//a[contains(@id, ':Update')]"));
        waitForPostBack();
    }


}
