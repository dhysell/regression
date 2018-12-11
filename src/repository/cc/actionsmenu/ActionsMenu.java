package repository.cc.actionsmenu;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.YesOrNo;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import repository.cc.claim.SystemGeneratedCheckWizard;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class ActionsMenu extends BasePage {


    private WaitUtils waitUtils;
    private WebDriver driver;

    public ActionsMenu(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions']")
    public WebElement button_Actions;

    @FindBy(xpath = "//span[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewExposure:NewExposureMenuItemSet:NewExposureMenuItemSet_ByCoverage-textEl']")
    public WebElement link_chooseByCoverage;

    @FindBy(xpath = "//a[@id='NewExposure:NewExposureScreen:Cancel']")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_ClaimExposureValidation-itemEl']")
    public WebElement link_ValidateClaimExposures;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimFileMenuItemSet:ClaimMenuActions_ClaimActions:ClaimMenuActions_ClaimExposureValidation:4:item-itemEl']")
    public WebElement link_AbilityToPay;

    @FindBy(xpath = "//span[text()='Create In-house Claim']/..")
    private WebElement link_CreatInhouseClaim;

    @FindBy(css = "span[id*=':ClaimMenuActions_ClaimActions:ClaimMenuActions_SplitFile-textEl']")
    private WebElement link_CreateSplitFile;

    // New... part of menu
    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimNewOtherMenuItemSet:ClaimMenuActions_NewOther:ClaimMenuActions_NewNote-itemEl']")
    public WebElement link_Note;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimNewOtherMenuItemSet:ClaimMenuActions_NewOther:ClaimMenuActions_Email-itemEl']")
    public WebElement link_Email;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimNewOtherMenuItemSet:ClaimMenuActions_NewOther:ClaimMenuActions_NewMatter-itemEl']")
    public WebElement link_Matter;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimNewOtherMenuItemSet:ClaimMenuActions_NewOther:ClaimMenuActions_NewEvaluation-itemEl']")
    public WebElement link_Evaluation;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimNewOtherMenuItemSet:ClaimMenuActions_NewOther:ClaimMenuActions_NewNegotiation-itemEl']")
    public WebElement link_Negotiation;


    // New Transactions
    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_ReserveSet-itemEl']")
    public WebElement link_Reserve;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet-itemEl']")
    public WebElement link_Check;

    @FindBy(xpath = "//a/span[contains(text(),'Other')]")
    public WebElement link_Other;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewOtherTrans:ClaimMenuActions_NewTransaction_RecoverySet-itemEl']")
    public WebElement link_Recovery;

    // New Document

    @FindBy(xpath = "//a[contains(@id,':ClaimNewDocumentMenuItemSet_Create-itemEl')]")
    public WebElement link_CreateFromTemplate;

    @FindBy(xpath = "//a[contains(@id, 'ClaimNewDocumentMenuItemSet_Link-itemEl')]")
    public WebElement link_AttachExistingDocument;

    @FindBy(xpath = "//a[contains(@id,'ClaimNewMultiDocumentMenuItemSet_Link-itemEl')]")
    public WebElement link_AttachMultipleDocuments;

    @FindBy(xpath = "Claim:ClaimMenuActions:ClaimMenuActions_NewDocument:ClaimNewDocumentMenuItemSet:ClaimNewDocumentMenuItemSet_IndicateExists-itemEl")
    public WebElement link_IndicateExistenceOfDoc;

    // Checks

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Payment-itemEl']")
    public WebElement link_RegularPayment;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_LAEPayment-itemEl']")
    public WebElement link_PaymentLAE;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Check-itemEl']")
    public WebElement link_FieldCheckDraft;

    @FindBy(xpath = "//a[@id='Claim:ClaimMenuActions:ClaimMenuActions_NewTransaction:ClaimMenuActions_NewTransaction_CheckSet:ClaimMenuActions_NewTransaction_Closed_Exposure-itemEl']")
    public WebElement link_PayClosedExposure;

    // Reminders

    @FindBy(xpath = "//span[text()='Reminder']/..")
    public WebElement link_Reminder;

    @FindBy(xpath = "//span[text()='Verify coverage']/..")
    public WebElement link_VerifyCoverage;

    // HELPERS
    // ==============================================================================


    public void escapeMenu() {
        button_Actions.sendKeys(Keys.ESCAPE);
    }
	
	/*private boolean scanCoverageList(List<WebElement> coverages, String coverageToLookFor) {
		
		boolean found = false;
		
		for(WebElement coverage : coverages) {
			if (coverage.getText().equalsIgnoreCase(coverageToLookFor)) {
				found = true;
			}
		}
		
		return found;
			
	}*/

	private boolean isPolicyLevelCoverage(String exposureType) {

	    boolean isPolicyLevelCoverage = false;

        if (exposureType.equalsIgnoreCase("Employment Practices Liability Insurance") ||
            exposureType.equalsIgnoreCase("Damage to Premises Rented to You") ||
            exposureType.equalsIgnoreCase("Non-Owned Auto Liability Bodily Injury") ||
            exposureType.equalsIgnoreCase("Premises Medical Expense") ||
            exposureType.equalsIgnoreCase("Non-Owned Auto Liability Property Damage") ||
            exposureType.equalsIgnoreCase("Non-Owned Auto Liability Vehicle Damage") ||
            exposureType.equalsIgnoreCase("Accidental Death and Dismemberment") ||
            exposureType.equalsIgnoreCase("Accidental Death")) {
                isPolicyLevelCoverage = true;
        }

        return isPolicyLevelCoverage;
    }


    public String lessCrappyCoveragePicker(String incident, List<String> incidentParts, String exposureType) {

        boolean success = false;
        int numTries = 0;
        String makeOrAdress = "";

        String yearOrItem = incidentParts.get(0);
        if (incidentParts.size() > 1) {
            makeOrAdress = incidentParts.get(1);
        }
        String altIncident = null;

        String firstMenuSelector = "div[id*=':NewExposureMenuItemSet_ByCoverage:'][id$=':item']";
        String locatorToBeUsed;

        while (!success && numTries < 25) {
            numTries++;
            //getCurrentDriver().navigate().refresh();
            clickElementByCordinates(button_Actions, 5, 5);
            waitUtils.waitUntilElementIsClickable(link_chooseByCoverage);
            clickElementByCordinates(link_chooseByCoverage, 5, 5);
             // convert to webdriver wait after working solution.
            List<WebElement> firstMenuItems = finds(By.cssSelector(firstMenuSelector));
            // Validate menu items exist before trying to locate incident.
            if (firstMenuItems.size() > 0) {
                if (isPolicyLevelCoverage(exposureType)) {
                    locatorToBeUsed = "//span[contains(text(),'Policy Coverage(s) :')]";
                } else {
                    locatorToBeUsed = "//span[contains(text(),'" + menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", firstMenuItems) + "')]";
                }
                altIncident = menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", firstMenuItems);
                // Click matching item in the first menu
                WebElement eleToBeClicked = find(By.xpath(locatorToBeUsed + "/../.."));
                clickElementByCordinates(eleToBeClicked, 5, 5);
                
                // Check ele is actually selected
                if (eleToBeClicked.getAttribute("class").contains("x-menu-item-active")) {
                    // Gather next menu and iterate over it. this gets the give element need to go to span to do text verification.
                    String id = eleToBeClicked.getAttribute("id");
                    String secondMenuSelector = "div[id^='" + id + ":'][id$=':item'] > a";
                    List<WebElement> secondMenu = finds(By.cssSelector("div[id^='" + id + ":'][id$=':item']"));

                    if (secondMenu.size() > 0) {
                        // check that to see if you can pick the coverage
                        pickCoverageType(secondMenuSelector, exposureType);
                        
                        success = validateActionMenuTraversal(success);
                        // Go third level menu and try to find the coverage
                        if (!success) {
                            locatorToBeUsed = ".//span[contains(text(),'" + menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", secondMenu) + "')]";
                            altIncident = menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", secondMenu);
                            systemOut("div selector is: " + secondMenuSelector);
                            systemOut("text locator is: " + locatorToBeUsed);
                            WebElement secondMenuItemToClick = find(By.xpath(locatorToBeUsed + "/../.."));
                            clickElementByCordinates(secondMenuItemToClick, 1, 1);
                            
                            if (checkIfElementExists(secondMenuItemToClick, 100)) {
                                if (secondMenuItemToClick.getAttribute("class").contains("x-menu-item-active")) {
                                    // Gather next menu and iterate over it. this gets the give element need to go to span to do text verification.
                                    String thirdMenuid = secondMenuItemToClick.getAttribute("id");
                                    String thirdMenuSelector = "div[id^='" + thirdMenuid + ":'][id$=':item'] > a";
                                    List<WebElement> thirdMenu = finds(By.cssSelector("div[id^='" + thirdMenuid + ":'][id$=':item']"));
                                    if (thirdMenu.size() > 0) {
                                        pickCoverageType(thirdMenuSelector, exposureType);
                                        
                                        success = validateActionMenuTraversal(success);
                                        if (!success) {
                                            locatorToBeUsed = ".//span[contains(text(),'" + menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", thirdMenu) + "')]";
                                            altIncident = menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", thirdMenu);
                                            WebElement thirdMenuItemToClick = thirdMenu.get(0).findElement(By.xpath(locatorToBeUsed + "/../.."));
                                            clickElementByCordinates(thirdMenuItemToClick, 2, 2);
                                            

                                            if (thirdMenuItemToClick.getAttribute("class").contains("x-menu-item-active")) {
                                                String fourthMenuid = thirdMenuItemToClick.getAttribute("id");
                                                String fourthMenuSelector = "div[id^='" + thirdMenuid + ":'][id$=':item'] > a";
                                                List<WebElement> fourthMenu = finds(By.cssSelector("div[id^='" + fourthMenuid + ":'][id$=':item']"));

                                                if (fourthMenu.size() > 0) {
                                                    pickCoverageType(fourthMenuSelector, exposureType);
                                                    
                                                    success = validateActionMenuTraversal(success);
                                                    if (!success) {
                                                        locatorToBeUsed = ".//span[contains(text(),'" + menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", thirdMenu) + "')]";
                                                        altIncident = menuParserV2(incident, makeOrAdress, yearOrItem, "'Item(s)')", thirdMenu);
                                                        WebElement fourthMenuItemToClick = fourthMenu.get(0).findElement(By.xpath(locatorToBeUsed + "/../.."));
                                                        clickElementByCordinates(fourthMenuItemToClick, 5, 5);

                                                        if (fourthMenuItemToClick.getAttribute("class").contains("x-menu-item-active")) {
                                                            String fifthMenuId = fourthMenuItemToClick.getAttribute("id");
//                                                      String fifthMenuSelector = "div[id^='" + fourthMenuid + ":'][id$=':item'] > a";
                                                            List<WebElement> fifthMenu = finds(By.cssSelector("div[id^='" + fifthMenuId + ":'][id$=':item']"));

                                                            if (fifthMenu.size() > 0) {
                                                                pickCoverageType(fourthMenuSelector, exposureType);
                                                                
                                                                success = validateActionMenuTraversal(success);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        waitForPostBack();
        return altIncident;
    }

    private void pickCoverageType(String coverageIDs, String coverageToLookFor) {
        try {
            // Get the number of coverages in the list.
            List<WebElement> coverages = finds(By.cssSelector(coverageIDs));
            WebElement choosenOne = null;
            // Picks a random coverage
            if (coverageToLookFor.equalsIgnoreCase("Random")) {
                int randomNum = NumberUtils.generateRandomNumberInt(0, coverages.size() - 1);
                coverages.get(randomNum).click();

            } else {
                // Picks a Specific coverage.
                String bestMatch = "";
                Double highestJaroWinklerValue = 0.0;
                JaroWinklerDistance winklerDistance = new JaroWinklerDistance();
                for (WebElement item : coverages) {
                    if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), coverageToLookFor)) {
                        highestJaroWinklerValue = winklerDistance.apply(item.getText(), coverageToLookFor);
                        bestMatch = item.getText();
                        choosenOne = item;
                    }
                }
                systemOut("I think this is the best match: " + bestMatch);
                systemOut("Passed in coverage" + coverageToLookFor);

                // This auto coverage has a submenu that you must navigate through to get the actual coverage
                if (coverageToLookFor.equalsIgnoreCase("Auto Property Damage - Property") || coverageToLookFor.equalsIgnoreCase("Auto Property Damage - Vehicle Damage")) {
                    WebElement autoEle = coverages.get(0).findElement(By.xpath("//span[contains(text(),'" + bestMatch + "')]/../.."));
                    String id = autoEle.getAttribute("id");
                    clickElementByCordinates(autoEle, 2, 2);
                    
                    // String secondMenuSelector = "div[id^='" + id + ":'][id$=':item'] > a";
                    List<WebElement> subMenu = finds(By.cssSelector("div[id^='" + id + ":'][id$=':item']"));
                    clickElementByCordinates(subMenu.get(0).findElement(By.xpath("//span[contains(text(),'" + coverageToLookFor + "')]")), 2, 2);

                }
                // Property coverage that has a submenu that you must navigate through to get the actual coverage.
                else if (coverageToLookFor.equalsIgnoreCase("Liability - Property Damage") || coverageToLookFor.equalsIgnoreCase("Liability - Vehicle Damage")) {
                    WebElement liabEle = coverages.get(0).findElement(By.xpath("//span[contains(text(),'" + bestMatch + "')]/../.."));
                    String id = liabEle.getAttribute("id");
                    clickElementByCordinates(liabEle, 2, 2);
                    
                    // String secondMenuSelector = "div[id^='" + id + ":'][id$=':item'] > a";
                    List<WebElement> subMenu = finds(By.cssSelector("div[id^='" + id + ":'][id$=':item']"));
                    clickElementByCordinates(subMenu.get(0).findElement(By.xpath("//span[contains(text(),'" + coverageToLookFor + "')]")), 2, 2);
                } else {
                    clickElementByCordinates(choosenOne, 2, 2);
                }
            }

        } catch (Exception mrgllrgle) {

        }
        waitForPostBack();
    }


    public void clickAbilityToPay() {
        clickWhenClickable(link_AbilityToPay);
    }


    public void clickCreateFromTemplate() {
        clickWhenClickable(link_CreateFromTemplate);
    }


    public void clickActionsButton() {
        waitUtils.waitUntilElementIsVisible(button_Actions, 30);
        button_Actions.click();
    }


    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }


    public void hoverChooseByCoverage() {
        customHover(link_chooseByCoverage);
    }
	
	/*private void clickChooseByCoverage() {
		clickWhenClickable(link_chooseByCoverage);
	}*/

    private void clickFieldOrDraftCheck() {
        if (link_FieldCheckDraft.isDisplayed()) {
            clickWhenClickable(link_FieldCheckDraft);
        }
    }


    private void clickLaePayment() {
        if (link_PaymentLAE.isDisplayed()) {
            clickWhenClickable(link_PaymentLAE);
        }
    }


    public void clickNewEmail() {
        clickWhenClickable(link_Email);
    }


    public void clickNewEvaluation() {
        clickWhenClickable(link_Evaluation);
    }


    public void clickNewMatter() {
        clickWhenClickable(link_Matter);
    }


    public void clickNewNegotiation() {
        clickWhenClickable(link_Negotiation);
    }


    public void clickNewNote() {
        clickWhenClickable(link_Note);
    }

    public void clickActionsOtherLink() {
        clickActionsButton();
        clickOtherLink();
    }

    public void clickOtherLink() {
        clickWhenClickable(link_Other);
    }


    public repository.cc.claim.Recovery clickRecoveryLink() {
        clickWhenClickable(link_Recovery);
        return new repository.cc.claim.Recovery(this.driver);
    }


    private void clickRegularPayment() {
        if (link_RegularPayment.isDisplayed()) {
            clickWhenClickable(link_RegularPayment);
        }
    }

    private void customHover(WebElement element) {
        element = waitUtils.waitUntilElementIsVisible(element);
        Actions move = new Actions(driver);
        move.moveToElement(element).build().perform();
        sleep(1); //unsure if this wait is needed anymore.
    }
	
	/*private void customHoverAndClick(WebElement element) {		
		element = waitUntilElementIsVisible(element);
		Actions move = new Actions(driver);
		move.moveToElement(element).build().perform();
		clickWhenClickable(element);		
	}
	
	private void clickValidateClaimExposures() {
		Actions move = new Actions(driver);
		move.dragAndDropBy(link_ValidateClaimExposures,1,1).build().perform();
	}*/

    private void hoverCheckLink() {
        customHover(link_Check);
        
        customHover(link_Check);
    }


    public void checkTypeMenuPicker(String checkType) throws Exception {


        boolean validateClicked = false;
        int numTries = 0;
        while (!validateClicked && numTries < 25) {

            numTries++;
            try {

                waitUtils.waitUntilElementIsVisible((By.xpath("//span[contains(text(),'Loss Type:')]")));
                WebElement lossTypeElement = find(By.xpath("//span[contains(text(),'Loss Type:')]"));
                lossTypeElement.click();

                clickActionsButton();
                

                switch (checkType) {
                    case "Regular":
                        hoverCheckLink();
                        
                        clickRegularPayment();
                        break;
                    case "LAE":
                        hoverCheckLink();
                        
                        clickLaePayment();
                        break;
                    case "Field":
                        hoverCheckLink();
                        
                        clickFieldOrDraftCheck();
                        break;
                    case "Draft":
                        hoverCheckLink();
                        
                        clickFieldOrDraftCheck();
                        break;
                    case "ReissueVoided":
                        hoverCheckLink();
                        
                        clickReissueClearedCheck();
                        break;
                    case "Closed":
                        // TO DO
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {

            }

            List<WebElement> cancelButtonExists = finds(
                    By.xpath("//a[@id='NormalCreateCheckWizard:Cancel' or @id='ManualCreateCheckWizard:Cancel' or @id='SystemGeneratedCheckWizard:Cancel']"));
            if (cancelButtonExists.size() > 0) {
                validateClicked = true;
            }
        }

        if (numTries >= 25) {
            throw new Exception(
                    "Tried 25 times and couldn't make it through the actions menu you to choose the coverage.");
        }

    }

    public void clickAttachExistingDocument() {
        clickWhenClickable(link_AttachExistingDocument);
    }


    public void clickAttachMulitipleDocuments() {
        clickWhenClickable(link_AttachMultipleDocuments);
    }


    public String menuParser(String incident) {
        List<WebElement> menuItems = finds(By.xpath(
                "//a[contains(@id, 'Claim:ClaimMenuActions:ClaimMenuActions_NewExposure:NewExposureMenuItemSet:NewExposureMenuItemSet_ByCoverage:')]"));
        String bestMatch = "";
        Double highestJaroWinklerValue = 0.0;

        JaroWinklerDistance winklerDistance = new JaroWinklerDistance();
        for (WebElement item : menuItems) {
            if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), incident)) {
                highestJaroWinklerValue = winklerDistance.apply(item.getText(), incident);
                bestMatch = item.getText();
            }
        }
        return bestMatch;
    }

    private String menuParserV2(String incident, String s1, String s2, String s3, List<WebElement> menuToCheck) {
//        List<WebElement> menuItems = finds(By.xpath(
//                "//a[contains(@id, 'Claim:ClaimMenuActions:ClaimMenuActions_NewExposure:NewExposureMenuItemSet:NewExposureMenuItemSet_ByCoverage:')]"));
        String bestMatch = "";
        Double highestJaroWinklerValue = 0.0;

        JaroWinklerDistance winklerDistance = new JaroWinklerDistance();
        for (WebElement item : menuToCheck) {
            if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), incident)) {
                highestJaroWinklerValue = winklerDistance.apply(item.getText(), incident);
                bestMatch = item.getText();
            }
            if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), s1)) {
                highestJaroWinklerValue = winklerDistance.apply(item.getText(), s1);
                bestMatch = item.getText();
            }
            if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), s2)) {
                highestJaroWinklerValue = winklerDistance.apply(item.getText(), s2);
                bestMatch = item.getText();
            }
            if (highestJaroWinklerValue < winklerDistance.apply(item.getText(), s3)) {
                highestJaroWinklerValue = winklerDistance.apply(item.getText(), s3);
                bestMatch = item.getText();
            }
        }
        return bestMatch;
    }


    public boolean validateActionMenuTraversal(boolean success) {

        try {
            waitUntilElementIsClickable(By.xpath("//a[@id='NewExposure:NewExposureScreen:Cancel']"));
        } catch (Exception e) {
        }

        List<WebElement> cancelExists = finds(By.xpath("//a[@id='NewExposure:NewExposureScreen:Cancel']"));

        if (cancelExists.size() > 0) {
            success = true;
        }
        return success;
    }


    public repository.cc.claim.ActivityPageCC clickVerifyCoverageActivity() {
        WebDriverWait wait = new WebDriverWait(this.driver, 20);
        boolean validateClicked = false;
        int numTried = 0;
        do {
            clickWhenClickable(By.xpath("//img[contains(@class,'product-logo')]"));
            clickActionsButton();
            
            clickWhenClickable(link_Reminder);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Verify coverage']")));
            boolean verifyCoverageClickable = finds(By.xpath("//span[text()='Verify coverage']/..")).size() > 0;

            if (verifyCoverageClickable) {
                clickWhenClickable(link_VerifyCoverage);
            }

            validateClicked = finds(By.xpath("//input[contains(@id, ':Activity_Subject')]")).size() > 0;
            numTried++;
        } while (!validateClicked && numTried < 25);

        return new repository.cc.claim.ActivityPageCC(this.driver);
    }

    @FindBy(xpath = "//a[contains(@id,':ClaimMenuActions_NewTransaction_Check_ReissueClearedCheck-itemEl')]")
    public WebElement link_ReissueClearedCheck;

    @FindBy(xpath = "//a[contains(@id,':ReissueClearedCheckRegular-itemEl')]")
    private WebElement linkRegularPayment;


    public repository.cc.claim.SystemGeneratedCheckWizard clickReissueClearedCheck() {
        boolean validateClicked = false;
        int numTries = 0;
        while (!validateClicked && numTries < 25) {

            numTries++;
            try {
                clickActionsButton();
                
                hoverCheckLink();
                
                if (link_ReissueClearedCheck.isDisplayed()) {
                    clickWhenClickable(link_ReissueClearedCheck);
                    
                    clickWhenClickable(linkRegularPayment);
                    
                }
            } catch (Exception e) {
                systemOut("Attempts to click Reissue Cleard Check: " + (numTries));
            }

            List<WebElement> cancelButtonExists = finds(
                    By.xpath("//a[@id='SystemGeneratedCheckWizard:Cancel']"));
            if (cancelButtonExists.size() > 0) {
                validateClicked = true;
            }
        }
        return new SystemGeneratedCheckWizard(this.driver);
    }

    @FindBy(xpath = "//span[contains(text(),'Reopen Claim')]")
    WebElement Link_ReopenClaim;


    public void clickReopenClaim() {

        clickWhenClickable(Link_ReopenClaim);

    }


    public void checkAbilityToPay() throws Exception {
        boolean validateClicked = false;
        int numTried = 0;
        while (!validateClicked && numTried < 5) {


            numTried++;
            waitUntilElementIsClickable(button_Actions);
            clickElementByCordinates(button_Actions, 5, 5);
            waitUtils.waitUntilElementIsClickable(link_Check);
            clickElementByCordinates(link_ValidateClaimExposures, 5, 5);
            waitUtils.waitUntilElementIsClickable(link_AbilityToPay);
            
            boolean aToPayClickable = finds(By.xpath("//span[contains(@id,'ClaimExposureValidation') and contains(text(),'Ability to pay')]")).size() > 0;

            if (aToPayClickable) {
                clickElementByCordinates(link_AbilityToPay, 5, 5);
                waitUntilElementIsNotVisible(link_AbilityToPay);
                waitUntilElementIsVisible(By.cssSelector("a[id$=':WebMessageWorksheet_ClearButton']"));
            }

            systemOut("Current attempts to click through the actions menu: " + numTried);
            validateClicked = finds(By.className("message")).size() > 0;
            sendArbitraryKeys(Keys.ESCAPE);
        }
        checkAndFixAbilityToPayIssues();
    }

    private void checkAndFixAbilityToPayIssues() throws Exception {
        String faultRatingError = "The claim's fault rating is either not selected, undetermined, or unknown.";
        String addressError = "The claimant's primary address must have a street, city and state";
        List<WebElement> messageElements = finds(By.xpath("//div[@class='message']"));

        for (WebElement ele : messageElements) {
            if (ele.getText().equalsIgnoreCase(faultRatingError)) {
                clickWhenClickable(find(By.xpath("//a[contains(text(),'On \"Loss Details\":')]")));
                repository.cc.claim.LossDetails lossDetails = new repository.cc.claim.LossDetails(this.driver);
                lossDetails.clickEditButton();
                
                lossDetails.selectFaultRating();
                
                lossDetails.clickUpdateButton();
            } else if (ele.getText().equalsIgnoreCase(addressError)) {
                clickWhenClickable(find(By.xpath("//a[contains(text(),'On \"Contacts\":')]")));
                clickWhenClickable(find(By.xpath("//a[contains(@text(),'Clear')]")));
                repository.cc.claim.PartiesInvolvedContacts contacts = new repository.cc.claim.PartiesInvolvedContacts(this.driver);
                
                contacts.setAddressInfo();
            } else if (ele.getText().equalsIgnoreCase("No validation errors.")) {
                systemOut("No Errors on ability to pay");
            } else {
                throw new Exception("Unexpected error: " + ele.getText());
            }
        }
    }

    /**
     * splits the claim with an in-house claim
     * Yes for with notes no for without notes.
     *
     * @return
     */

    public String createInHouseClaimWithOrWithoutNotes(YesOrNo notesOrNo) {
        TopMenu topMenu = new TopMenu(this.driver);
        String textNeeded = notesOrNo.getValue().equals(YesOrNo.Yes.getValue()) ? "With Notes" : "Without Notes";
        String linkXpath = "//span[text()='" + textNeeded + "']/..";
        String currentClaimNumber = topMenu.gatherClaimNumber();
        int lastTwoOfClaimNum = Integer.valueOf(currentClaimNumber.substring(currentClaimNumber.length() - 2));
        String incrementedClaimNumber = null;

        boolean validateClicked = false;
        int numTried = 0;
        while (!validateClicked && numTried < 10) {

            clickWhenClickable(By.xpath("//img[contains(@class,'product-logo')]"));
            numTried++;
            clickActionsButton();
            
            Actions move = new Actions(this.driver);
            move.dragAndDropBy(link_CreatInhouseClaim, 1, 1).build().perform();
            boolean subMenuVisible = (finds(By.xpath(linkXpath)).size() > 0);

            if (subMenuVisible) {
                clickWhenClickable(find(By.xpath(linkXpath)));
            }

            systemOut("Current attempts to click through the actions menu: " + numTried);


            validateClicked = finds(By.xpath("//div[@id='messagebox-1001-displayfield-inputEl']")).size() > 0;

            incrementedClaimNumber = currentClaimNumber.substring(0, currentClaimNumber.length() - 2) + "0" + (String.valueOf(lastTwoOfClaimNum + 1));

            //WebElement claimEle = find(By.xpath("//span[contains(text(),'"+incrementedClaimNumber+"') and @id='TabBar:ClaimTab-btnInnerEl']"));
            if (validateClicked) {
                selectOKOrCancelFromPopup(OkCancel.OK);
                WebDriverWait wait = new WebDriverWait(this.driver, 60);
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[src='images/app/indicator_icon_litigation.png']")));
            }


        }
        return incrementedClaimNumber;
    }

    /**
     * splits the claim with an in-house claim
     * Yes for with notes no for without notes.
     *
     * @return
     */

    public String createSplitClaimWithOrWithoutNotes() {
        TopMenu topMenu = new TopMenu(this.driver);

        String currentClaimNumber = topMenu.gatherClaimNumber();
        String newClaimNumber = null;

        int numTried = 0;

        
        waitUtils.waitUntilElementIsClickable(By.xpath("//img[contains(@class,'product-logo')]"), 30);
        numTried++;
        
        try {
            waitUtils.waitUntilElementIsClickable(button_Actions, 15);
            button_Actions.click();
        } catch (Exception e) {
            waitUtils.waitUntilElementIsClickable(button_Actions, 15);
            button_Actions.click();
        }

        try {
            
            waitUtils.waitUntilElementIsClickable(link_CreateSplitFile, 15);
            link_CreateSplitFile.click();
        } catch (Exception e) {
            waitUtils.waitUntilElementIsClickable(link_CreateSplitFile, 15);
            link_CreateSplitFile.click();
        }

        try {
            clickSplitFileWithoutNotesLink();
        } catch (Exception e) {
            clickSplitFileWithoutNotesLink();
        }

        systemOut("Current attempts to click through the actions menu: " + numTried);

        newClaimNumber = waitForClaimNumberChange(currentClaimNumber);

        return newClaimNumber;
    }

    private String waitForClaimNumberChange(String currentClaimNumber) {
        TopMenu topMenu = new TopMenu(getDriver());
        boolean isChanged = false;
        int count = 1;

        while (isChanged==false && count <= 5) {
            if (currentClaimNumber.equalsIgnoreCase(topMenu.gatherClaimNumber())) {
                waitUtils.waitUntilElementNotClickable(By.xpath("//a[@id='TabBar:ClaimTab']/span/span/span" +
                        "[contains(text(),'"+currentClaimNumber+"')]"),30);
                System.out.println("Waiting " + count + " time(s) for claim number to change.");
                count++;
            } else {
                isChanged = true;
            }
        }

        if (isChanged==false) {
            Assert.fail("Waiting for split claim timed out.");
        }
        return topMenu.gatherClaimNumber();
    }

    @FindBy(css = "a[id*=':ClaimMenuActions_CoverageFileWithoutNotes-itemEl']")
    private WebElement linkSplitFileWithoutNotes;

    private void clickSplitFileWithoutNotesLink() {
        waitUtils.waitUntilElementIsClickable(linkSplitFileWithoutNotes, 15);
        linkSplitFileWithoutNotes.click();
    }
}
