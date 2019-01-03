package repository.gw.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;
import java.util.List;

public class WaitUtils {

    private WebDriver driver;
    private int defaultWait = 15;

    //private By xmaskPresent = By.cssSelector("div[role='presentation'][class='x-mask x-mask-fixed']");

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriverWait newWait() {
        return new WebDriverWait(this.driver, this.defaultWait);
    }

    protected WebDriverWait newWait(int waitInSeconds) {
        return new WebDriverWait(this.driver, timeFixer(waitInSeconds), 100);
    }

    protected int timeFixer(int time) {
    	int tmp = (time > 300 && time < 1000) ? 1000 : time;
    	tmp = tmp % 1000 == 0 ? (time / 1000 == 0 ? 1 : time / 1000) : tmp;
//      System.out.println("Wait up to: " + tmp + "s");
    	return tmp;
    }

    protected boolean waitForPageLoad() {
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                // no jQuery present
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        waitForPostBack();

        return newWait().until(jQueryLoad) && newWait().until(jsLoad);
    }

    public void waitForPostBack() {
        try {
            Thread.sleep(400); // initial pause to make sure postback kicks off.
        } catch (Exception e) {
        }
        
        int timeWaited = 0;
        newWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("body")));
        if (driver.findElement(By.cssSelector("body")).getAttribute("class").contains("x-mask")) {
        	long timeToWait = new Date().getTime() + (120 * 1000);
            while (driver.findElement(By.xpath("//body")).getAttribute("class").contains("x-mask") && (new Date().getTime() < timeToWait)) {
                try {
                    Thread.sleep(100); // loop to check until xmask is gone, every 100 milliseconds.
                    timeWaited += 100;
                } catch (Exception e) {
                }
            }
        }
        if (timeWaited > 0) {
//            System.out.println("Waited: " + timeWaited + "ms for x-mask to be gone");
        }
    }

    public void waitForPostBack(int secondsToWait) {
        try {
            Thread.sleep(400); // initial pause to make sure postback kicks off.
        } catch (Exception e) {
        }
        
        int timeWaited = 0;
        newWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("body")));
        if (driver.findElement(By.cssSelector("body")).getAttribute("class").contains("x-mask")) {
        	long timeToWait = new Date().getTime() + (timeFixer(secondsToWait) * 1000);
            while (driver.findElement(By.xpath("//body")).getAttribute("class").contains("x-mask") && (new Date().getTime() < timeToWait)) {
                try {
                    Thread.sleep(100); // loop to check until xmask is gone, every 100 milliseconds.
                    timeWaited += 100;
                } catch (Exception e) {
                }
            }
        }
        
        if (timeWaited > 0) {
//            System.out.println("Waited: " + timeWaited + "ms for x-mask to be gone");
        }
    }

    public boolean waitUntilElementNotClickable(By locator) {
        waitForPageLoad();
        try {
            newWait().until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementNotClickable(By locator, int time) {
        waitForPageLoad();
        try {
            newWait(time).until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementNotClickable(WebElement ele) {
        waitForPageLoad();
        try {
            newWait().until(ExpectedConditions.elementToBeClickable(ele));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementNotClickable(WebElement ele, int time) {
        waitForPageLoad();
        try {
            newWait(time).until(ExpectedConditions.elementToBeClickable(ele));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement waitUntilElementIsClickable(By locator) {
        waitForPageLoad();
        newWait().until(ExpectedConditions.elementToBeClickable(locator));
        return driver.findElement(locator);
    }

    public WebElement waitUntilElementIsClickable(WebElement ele) {
        waitForPageLoad();
        newWait().until(ExpectedConditions.elementToBeClickable(ele));
        return ele;
    }

    public WebElement waitUntilElementIsClickable(By locator, int waitInSeconds) {
        waitForPageLoad();
        newWait(waitInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
        return driver.findElement(locator);
    }

    public WebElement waitUntilElementIsClickable(WebElement ele, int waitInSeconds) {
        waitForPageLoad();
        newWait(waitInSeconds).until(ExpectedConditions.elementToBeClickable(ele));
        return ele;
    }


    public WebElement waitUntilElementIsVisible(By locator) {
        waitForPageLoad();
        newWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator);
    }

    public WebElement waitUntilElementIsVisible(WebElement ele) {
        waitForPageLoad();
        newWait().until(ExpectedConditions.visibilityOf(ele));
        return ele;
    }

    public WebElement waitUntilElementIsVisible(By locator, int waitInSeconds) {
        waitForPageLoad();
        newWait(waitInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator);
    }

    public List<WebElement> waitUntilElementsAreVisible(By locator, int waitInSeconds) {
        waitForPageLoad();
        newWait(waitInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElements(locator);
    }

    public WebElement waitUntilElementIsVisible(WebElement ele, int waitInSeconds) {
        waitForPageLoad();
        newWait(waitInSeconds).until(ExpectedConditions.visibilityOf(ele));
        return ele;
    }


    public boolean waitUntilElementIsNotVisible(By locator) {
        try {
            newWait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(locator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementIsNotVisible(WebElement ele) {
        try {
            newWait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(ele)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementIsNotVisible(By locator, int waitInSeconds) {
        try {
            newWait(waitInSeconds).until(ExpectedConditions.refreshed(ExpectedConditions.invisibilityOfElementLocated(locator)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitUntilElementIsNotVisible(WebElement ele, int waitInSeconds) {
        try {
            newWait(waitInSeconds).until(ExpectedConditions.visibilityOf(ele));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
