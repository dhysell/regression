package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import repository.cc.sidemenu.SideMenuCC;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.generate.cc.ReserveLine;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reserve extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;

    public Reserve(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }

    private void localDelay(WebElement elementName) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(elementName));
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//a[@id='ExposureDetail:ExposureDetailScreen:ExposureDetailScreen_CreateReserveButton']")
    public WebElement button_CreateReserve;

    @FindBy(xpath = "//a[@id='NewReserveSet:NewReserveSetScreen:Add']")
    public WebElement button_Add;

    @FindBy(xpath = "//a[@id='NewReserveSet:NewReserveSetScreen:Cancel']")
    public WebElement button_Cancel;

    @FindBy(xpath = "//a[@id='NewReserveSet:NewReserveSetScreen:Update']")
    public WebElement button_Save;

    @FindBy(xpath = "//a[@id='NewReserveSet:NewReserveSetScreen:Remove']")
    public WebElement button_Remove;

    @FindBy(xpath = "//a[@id='WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton']")
    public WebElement button_Clear;

    @FindBy(xpath = "//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table")
    public WebElement table_Reserves;

    public Guidewire8Select select_CostCategory(String xpath) {
        return new Guidewire8Select(driver, "" + xpath + "");
    }

    // HELPERS
    // ==============================================================================

    public void clickCreateReserveButton() {
        localDelay(button_CreateReserve);
        button_CreateReserve.click();
    }

    public void clickRemoveButton() {
        clickWhenClickable(button_Remove);
    }

    public void clickCancelButton() {
        clickWhenClickable(button_Cancel);
    }

    public void clickAddButton() {
        localDelay(button_Add);
        button_Add.click();
    }

    public void clickClearButton() {
        clickWhenClickable(button_Clear);
    }

    public void clickSaveButton() {
        localDelay(button_Save);
        button_Save.click();
    }

    public int getNumberOfReserves() {

        int count;

        try {
            List<WebElement> rowElements = table_Reserves.findElements(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr"));
            count = rowElements.size();
        } catch (Exception e) {
            count = 0;
        }

        return count;
    }

    public Boolean checkElementExits(String elementXpath) {
        Boolean isPresent = finds(By.xpath("" + elementXpath + "")).size() > 0;

        return isPresent;
    }

    public void setReserveLines() {

        List<WebElement> incidentTypeLink = finds(By.xpath("//div[@id='ClaimExposures:ClaimExposuresScreen:ExposuresLV-body']//td[3]//a[contains(@id,'ClaimExposures:ClaimExposuresScreen:ExposuresLV')]"));

        for (WebElement incidentLink : incidentTypeLink) {
            clickWhenClickable(incidentLink);
            
            clickCreateReserveButton();
            
            clickAddButton();
            
            setReserveLine();
        }
    }

    public String errorchecking(int lineItem) {

        String correctAmount = "";
        try{
            waitUntilElementIsVisible(By.xpath("//div[@class='message']"));
        }
        catch (Exception e){
            systemOut("No messages to check.");
        }

        Boolean isPresent = finds(By.xpath("//div[@class='message']")).size() != 0;
        

        if (isPresent == true) {
            List<WebElement> messageBanners = finds(By.xpath("//div[@class='message']"));

            // ************************** Add Error Handling for Multiple
            // Scenarios Here ******************************

            for (WebElement singleBanner : messageBanners) {
                String currentMessage = singleBanner.getText();

                if (currentMessage.contains("limit of")) {
                    if (currentMessage.contains("$")) {
                        Pattern regex = Pattern.compile("\\$[0-9\\.,]*\\b");
                        Matcher matcher = regex.matcher(currentMessage);
                        if (matcher.find()) {
                            currentMessage = matcher.group(0);
                            correctAmount = currentMessage.replace("$", "");
                        }

                        // Locate reserve amount data cell
                        List<WebElement> reserveElements = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr/td[7]/div[@class='x-grid-cell-inner ']"));
                        WebElement reserveElement = reserveElements.get(lineItem - 1);
                        

                        try {
                            clickWhenClickable(reserveElement);
                        } catch (Exception e) {
                            clickProductLogo();
                            clickWhenClickable(reserveElement);
                        }


                        

                        // Assign EditBox to variable
                        WebElement hiddenEditBox = find(By.xpath("//input[@name='NewAmount']"));

                        // Focus EditBox
                        try {
                            hiddenEditBox.click();
                        } catch (Exception e) {
                            reserveElement.click();
                            hiddenEditBox.click();
                        }

                        

                        // Correct Input
                        hiddenEditBox.clear();
                        hiddenEditBox.sendKeys(correctAmount);
                        clickProductLogo();

                        // Save changes or cancel if reserve line is invalid
                        try {
                            clickSaveButton();
                        } catch (Exception e) {
                            
                            clickSaveButton();
                        }

                    }
                } else if (currentMessage.equalsIgnoreCase("All exposures on a claim must have the same coverage incident.")) {
                    clickClearButton();
                    
                    clickSaveButton();
                } else if (currentMessage.equalsIgnoreCase("Cost Category : Missing required field \"Cost Category\"")) {
                    List<WebElement> tableRows = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr"));
                    int lastRow = tableRows.size();
                    WebElement checkBoxElement = find(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr[" + lastRow + "]/td[1]/div/img"));
                    checkBoxElement.click();
                    clickRemoveButton();
                    
                    clickSaveButton();
                } else {
                    Assert.fail(currentMessage);
                }
            }

            // ************************** Add Error Handling for Multiple
            // Scenarios Here ******************************
        }

        
        return correctAmount;

    }

    // NEW RESERVE FUNCTIONALITY

    // TODO

    public void setAllReserveLines() {

    }

    public void setReserveLine(ReserveLine line, String coverageType) {
        if (pickIncidentToCreateReserve(line.getExposureNumber(), coverageType)) {
            
            try {
                chooseCostCategory(line.getCostCategory());
            } catch (Exception e) {
                chooseCostCategory("Random");
            }

            
            try {
                enterReserveAmount(line.getReserveAmount());
            } catch (Exception e) {
                Assert.fail("Unable to set reserve for " + coverageType + " coverage type.");
            }
            
            clickSaveButton();
            
            errorchecking(getNumberOfReserves());
        } else {
            System.out.println("Valid reserve line already exists.");
        }
    }

    @FindBy(css = "div[id='ClaimExposures:ClaimExposuresScreen:ExposuresLV']")
    private WebElement exposuresTable;

    @FindBy(css = "div[id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV']")
    private WebElement setReservesTable;

    public void setReserveLineRandom() {

        boolean reservesError = false;
        int reservesRow = 0;

        int exposureRows = tableUtils.getAllTableRows(exposuresTable).size();
        tableUtils.clickLinkInTableByRowAndColumnName(exposuresTable, NumberUtils.generateRandomNumberInt(1, exposureRows), "Incident Type");
        clickCreateReserveButton();

        localDelay(setReservesTable);

        try {
            reservesRow = tableUtils.getAllTableRows(setReservesTable).size();
        } catch (Exception e) {
            System.out.println("WARNING: No table rows located.");
        }

        clickAddButton();
        reservesRow++;

        try {
            tableUtils.selectRandomValueForSelectInTable(setReservesTable, reservesRow, "Coverage");
        } catch (Exception e) {
            System.out.println("WARNING: Unable to update coverage selection.");
        }

        try {
            tableUtils.selectRandomValueForSelectInTable(setReservesTable, reservesRow, "Cost Category");
            sendArbitraryKeys(Keys.ESCAPE);
        } catch (Exception e) {
            System.out.println("WARNING: Unable to update cost category selection.");
            reservesError = true;
        }

        tableUtils.clickCellInTableByRowAndColumnName(setReservesTable, 1, "New Available Reserves");
        
        sendArbitraryKeys((new BigDecimal(NumberUtils.generateRandomNumberInt(500, 2000)).setScale(2, RoundingMode.HALF_UP) + ""));
        clickProductLogo();

        if (reservesError) {
            clickCancelButton();
        } else {
            clickSaveButton();
        }

        errorchecking(getNumberOfReserves());

        System.out.println();
    }

    private boolean pickIncidentToCreateReserve(String iName, String coverageType) throws NumberFormatException {
        

        int rowNumber;

        if (iName.equalsIgnoreCase("Random")) {
            clickWhenClickable(find(By.xpath("//a[contains(@id,':CoverageItem')]")));
        } else {
            WebElement table = find(By.xpath("//div[@id='ClaimExposures:ClaimExposuresScreen:ExposuresLV']"));

            if (coverageType != null) {
                rowNumber = Integer.parseInt(find(By.xpath("//div[@id='ClaimExposures:ClaimExposuresScreen:ExposuresLV-body']" +
                        "//tbody//tr/td/div[contains(text(),'" + coverageType + "')]/ancestor::tr[1]")).getAttribute("data-recordindex"));
            } else {
                rowNumber = Integer.parseInt(iName);
            }

            // Row count difference between maintenance and R2 coverages.
            try {
                tableUtils.clickLinkInTableByRowAndColumnName(table, (rowNumber + 1), "#");
            } catch (Exception e) {
                tableUtils.clickLinkInTableByRowAndColumnName(table, (rowNumber), "#");
            }

        }
        

        clickCreateReserveButton();
        waitUntilElementIsClickable(By.cssSelector("a[id$='NewReserveSetScreen:Add']"));

        List<WebElement> reserveLines = finds(By.cssSelector("div[id*=':EditableReservesLV-body'] div table tbody tr"));
        if (reserveLines.size() > 0 && reserveLines != null) {
            clickCancelButton();
            return false;
        } else {
            clickAddButton();
            waitUntilElementIsClickable(By.cssSelector("div[id$=':EditableReservesLV-body']"));
            return true;
        }
    }

    private void chooseCostCategory(String costCategory) {

        localDelay(reservesTable);

        int reserveRow = Integer.parseInt(find(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']//tr[not(contains(@class,'summary'))][last()]")).getAttribute("data-recordindex")) + 1;
        String columnName = "Cost Category";
        String tableXPATH = "//div[contains(@id,'ReservesSummaryDV:EditableReservesLV')]";

        if (costCategory.equals("Random")) {
            tableUtils.selectRandomValueForSelectInTable(find(By.xpath(tableXPATH)), reserveRow, columnName);
        } else {
            tableUtils.selectValueForSelectInTable(find(By.xpath(tableXPATH)), reserveRow, columnName, costCategory);
        }

    }

    private void enterReserveAmount(String amount) {
        String tableXPATH = "//div[contains(@id,'ReservesSummaryDV:EditableReservesLV')]";
        WebElement eleNeedsClicked = find(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']//tr[not(contains(@class,'summary'))][last()]//td[7]/div"));
        String name = "NewAmount";

        if (eleNeedsClicked.getText().equalsIgnoreCase("-")) {
            if (amount.equalsIgnoreCase("random")) {
                amount = Integer.toString(NumberUtils.generateRandomNumberInt(1000, 20000));
                tableUtils.setValueForCellInsideTable(find(By.xpath(tableXPATH)), eleNeedsClicked, name, amount);
            } else {
                tableUtils.setValueForCellInsideTable(find(By.xpath(tableXPATH)), eleNeedsClicked, name, amount);
            }
        }
        sendArbitraryKeys(Keys.ESCAPE);
    }

    public void setReserveLine() {

        // Find number of rows, start reserves on next line
        int lineItem = getNumberOfReserves();
        List<WebElement> rowElements = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr/td[4]/div"));
        WebElement costCatElement = rowElements.get(lineItem - 1);
        clickWhenClickable(costCatElement);

        // Locate ComboBox in table, select random item
        WebElement hiddenComboBox = find(By.xpath("//table[starts-with(@id,'simplecombo-')]"));
        String tableID = hiddenComboBox.getAttribute("id");
        String tableXpath = "//table[starts-with(@id,'" + tableID + "')]";
        
        select_CostCategory(tableXpath).sendKeys(Keys.ENTER);
        
        select_CostCategory(tableXpath).selectByVisibleTextRandom();
        

        // Locate reserve amount data cell
        List<WebElement> reserveElements = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr/td[7]/div[@class='x-grid-cell-inner ']"));
        WebElement reserveElement = reserveElements.get(lineItem - 1);
        

        // Check current value in reserve amount cell
        String currentValue = reserveElement.getText();
        
        if (currentValue.equalsIgnoreCase("-")) {
            clickWhenClickable(reserveElement);
            

            // Assign EditBox to variable
            WebElement hiddenEditBox = find(By.xpath("//input[@name='NewAmount']"));

            // Focus EditBox
            try {
                hiddenEditBox.click();
            } catch (Exception e) {
                reserveElement.click();
                hiddenEditBox.click();
            }

            

            // Generate and input random amount
            String amount = Integer.toString(NumberUtils.generateRandomNumberInt(1000, 20000));
            hiddenEditBox.sendKeys(amount);
            
            hiddenEditBox.sendKeys(Keys.TAB);
            

            // Save changes or cancel if reserve line is invalid
            clickSaveButton();
            errorchecking(lineItem);
        }

        List<WebElement> needsSaved = finds(By.xpath("//a[@id='NewReserveSet:NewReserveSetScreen:Update']"));
        if (needsSaved.size() > 0) {
            clickSaveButton();
        }

        waitForPageLoad();
    }

    @FindBy(xpath = "//div[contains(@id,':EditableReservesLV-body')]/div/table")
    private WebElement reservesTable;

    public void setCustomReserveLine(String exposureName, String amount) {

        // Find reserve to edit
        waitUntilElementIsClickable(reservesTable);
        WebElement reserveRow = find(By.xpath("//div[contains(@id,':EditableReservesLV-body')]/div/table/tbody/tr/td[2]/div[contains(text(),'" + exposureName + "')]/parent::td/parent::tr"));
        List<WebElement> rowElements = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr/td[4]/div"));

        int lineItem = Integer.parseInt(reserveRow.getAttribute("data-recordindex"));

        WebElement costCatElement = rowElements.get(lineItem);
        clickWhenClickable(costCatElement);

        // Locate reserve amount data cell
        List<WebElement> reserveElements = finds(By.xpath("//div[@id='NewReserveSet:NewReserveSetScreen:ReservesSummaryDV:EditableReservesLV-body']/div/table/tbody/tr/td[7]/div[@class='x-grid-cell-inner ']"));
        WebElement reserveElement = reserveElements.get(lineItem);
        

        clickWhenClickable(reserveElement);
        

        // Assign EditBox to variable
        WebElement hiddenEditBox = find(By.xpath("//input[@name='NewAmount']"));

        // Focus EditBox
        try {
            hiddenEditBox.click();
        } catch (Exception e) {
            reserveElement.click();
            hiddenEditBox.click();
        }

        

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");

        BigDecimal dollarAmount = new BigDecimal(Double.parseDouble(amount)).setScale(0, BigDecimal.ROUND_UP);

        amount = dollarAmount.toString();

        hiddenEditBox.clear();
        
        hiddenEditBox.sendKeys(amount);
        
        hiddenEditBox.sendKeys(Keys.TAB);
        

        clickSaveButton();
        errorchecking(lineItem);

        List<WebElement> needsSaved = finds(By.xpath("//a[@id='NewReserveSet:NewReserveSetScreen:Update']"));
        if (needsSaved.size() > 0) {
            clickSaveButton();
        }

        waitForPageLoad();

    }

    public void waitForPageToLoad() {
        waitUntilElementIsNotVisible(button_Save, 120);
    }

    public void approveReserves(String claimNumber) {

        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        sideMenu.clickWorkplanLink();

        WorkplanCC workPlan = new WorkplanCC(this.driver);
        String activityName = "Review and approve reserve change";

        workPlan.completeActivityAsPersonAssignedTo(activityName, claimNumber);

        

        // If above function has been working well past 7/21/2016 remove the below commented out code. 


//		IActivities activities = DesktopFactory.getActivities();
//		TopInfo topInfoStuff = new TopInfo();
//		SideMenu sideMenu = new SideMenu();
//
//		sideMenu.clickWorkplanLink();
//		
//		List<WebElement> rowsArray = finds(
//				By.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body']/div/table/tbody/tr"));
//		List<String> names = new ArrayList<String>();
//		List<String> exposures = new ArrayList<String>();
//		List<String> dueDates = new ArrayList<String>();
//		List<String> users = new ArrayList<String>();
//
//		for (WebElement row : rowsArray) {
//			String rowIndex = row.getAttribute("data-recordindex");
//			WebElement subject = driver
//					.findElement(By
//							.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body']/div/table/tbody/tr[@data-recordindex='"
//									+ rowIndex + "']/td[7]"));
//			if (subject.getText().equalsIgnoreCase("Review and approve reserve change")) {
//				names.add(driver
//						.findElement(By
//								.xpath("//span[@id='Claim:ClaimInfoBar:Insured-btnInnerEl']/span[@class='infobar_elem_val']"))
//						.getText());
//				exposures.add(driver
//						.findElement(By
//								.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body']/div/table/tbody/tr[@data-recordindex='"
//										+ rowIndex + "']/td[8]"))
//						.getText());
//				dueDates.add(driver
//						.findElement(By
//								.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body']/div/table/tbody/tr[@data-recordindex='"
//										+ rowIndex + "']/td[4]"))
//						.getText());
//				String[] userPart = driver
//						.findElement(By
//								.xpath("//div[@id='ClaimWorkplan:ClaimWorkplanScreen:WorkplanLV-body']/div/table/tbody/tr[@data-recordindex='"
//										+ rowIndex + "']/td[10]"))
//						.getText().split((" "));
//				String firstInitial = userPart[0].substring(0, 1);
//				users.add(firstInitial.toLowerCase() + userPart[1].toLowerCase());
//
//			}
//		}
//
//		if (users.size() > 0) {
//			String assignedUser = users.get(0).toString();
//
//			for (int i = 0; i < users.size(); i++) {
//				if (!users.get(i).equalsIgnoreCase(assignedUser) || i == 0) {
//
//					// Log out from current User
//					topInfoStuff.clickTopInfoLogout();
//					
//
//					// Log in as assigned User
//					Configuration.setProduct(ApplicationOrCenter.ClaimCenter);
//					Login lp = new Login(driver);
//					String password = "gw";
//					lp.login(users.get(i), password);
//
//					activities.approveReserveAndChecks(names.get(i), claimNumber, dueDates.get(i), "Reserve");
//
//					
//
//				}
//			}
//		}

    }

}
