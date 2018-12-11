package repository.pc.workorders.renewal;

import com.idfbins.enums.OkCancel;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderRenewalInformation;

import java.util.Date;

public class StartRenewal extends BasePage {


    private WebDriver driver;

    public StartRenewal(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    ////////////
    //Elements//
    ////////////
    public Guidewire8Select select_RenewalCode() {
        return new Guidewire8Select(getDriver(), "//*[@id='RenewalWizard_RenewalPopup:RenewalWizard_RenewalScreen:RenewalCode-triggerWrap']");
    }

    @FindBy(xpath = "//*[@id='PolicyFile_Summary:Policy_SummaryScreen:preRenewalDirectionWarning']")
    public WebElement text_PreRenewalWarningMessagePolicyPage;

    ///////////
    //Methods//
    ///////////

    /**
     * @throws Exception
     * @ExitsOn PolicyInfo
     */
    public void startRenewal() throws Exception {
        repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
    }

    //This method is only for clicking the renew policy button and selecting a renewal code.

    public void clickRenewPolicy(RenewalCode renewalCode) throws Exception {
        repository.pc.workorders.generic.GenericWorkorder genericWO = new repository.pc.workorders.generic.GenericWorkorder(driver);
        genericWO.clickGenericWorkorderSubmitOptionsRenew();
        repository.pc.workorders.generic.GenericWorkorderQuote quote = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            riskAnalysis.approveAll_IncludingSpecial();
            genericWO.clickGenericWorkorderSubmitOptionsRenew();
         }
        Guidewire8Select mySelect = select_RenewalCode();
        clickProductLogo();
        mySelect.selectByVisibleText(renewalCode.getValue());
        clickUpdateButton();
        selectOKOrCancelFromPopup(OkCancel.OK);
        if (new GuidewireHelpers(driver).errorMessagesExist()) {
            if (new GuidewireHelpers(driver).getErrorMessages().get(0).contains("Please cancel and retry your change.")) {
                new GuidewireHelpers(driver).clickDiscardUnsavedChangesLink();
                genericWO.clickGenericWorkorderSubmitOptionsRenew();
                if (quote.isPreQuoteDisplayed()) {
                    quote.clickPreQuoteDetails();
                    riskAnalysis.approveAll_IncludingSpecial();
                    genericWO.clickGenericWorkorderSubmitOptionsRenew();
                }
                mySelect.selectByVisibleText(renewalCode.getValue());
                clickUpdateButton();
                selectOKOrCancelFromPopup(OkCancel.OK);
            } else {
                Assert.fail("There was an unexpected error. The error was: " + new GuidewireHelpers(driver).getFirstErrorMessage());
            }
        }

    }

    public void setDefensibleSpaceQuestion(GeneratePolicy policy) throws GuidewireNavigationException {
    	new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuSquirePropertyDetail();
    	for(PolicyLocation location : policy.squire.propertyAndLiability.locationList) {
    		for(PLPolicyLocationProperty property : location.getPropertyList()) {
    			new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property);
    			new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).clickOK();
    		}
    	}
    }

    public void setAllDefensibleSpace(GeneratePolicy localPolicy) throws Exception {
    	repository.pc.sidemenu.SideMenuPC pcSideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails pcSquireProtectionDetailsPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcSquirePropertyLiabilityPropertyDetailPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkorder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        int numLocations = new TableUtils(driver).getRowCount(pcSquirePropertyLiabilityPropertyDetailPage.getPropertyDetailLocationsTable());
        for (int locationRowNumber = 1; locationRowNumber <= numLocations; locationRowNumber++) {
//        	pcBuildingsWorkorder.clickBuildingsLocationsRow(locationRowNumber);
        	pcSquirePropertyLiabilityPropertyDetailPage.clickLocation(locationRowNumber);
        	int numBuildingsInLocation = new TableUtils(driver).getRowCount(pcSquirePropertyLiabilityPropertyDetailPage.getPropertyDetailPropertiesTable());
        	for (int buildingRowNumber = 1; buildingRowNumber <= numBuildingsInLocation; buildingRowNumber++) {
//        		pcBuildingsWorkorder.clickBuildingRowByNumber(buildingRowNumber);
        		pcSquirePropertyLiabilityPropertyDetailPage.clickViewOrEditBuildingButtonByRowNumber(buildingRowNumber);
        		pcSquireProtectionDetailsPage.clickProtectionDetailsTab();
        		pcSquireProtectionDetailsPage.setDefensibleSpace(localPolicy.squire.propertyAndLiability.locationList.get(locationRowNumber - 1).getPropertyList().get(buildingRowNumber - 1).getDefensibleSpace());
        		pcWorkorder.clickOK();
        	}
        }
    }


    public void quoteAndIssueRenewal(RenewalCode renewalCode, GeneratePolicy localPolicy) throws Exception {
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();
        waitForPreRenewalDirections();
        repository.pc.workorders.generic.GenericWorkorder genericWO = new repository.pc.workorders.generic.GenericWorkorder(driver);
        if (checkIfElementExists(text_PreRenewalWarningMessagePolicyPage, 1000)) {
            repository.pc.policy.PolicySummary summaryPage = new repository.pc.policy.PolicySummary(driver);
            if (summaryPage.checkIfActivityExists("Pre-Renewal Review")) {
                summaryPage.clickViewPreRenewalDirection();
                repository.pc.policy.PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);

                boolean preRenewalDirectionExists = preRenewalPage.checkPreRenewalExists();
                if (preRenewalDirectionExists) {
                    preRenewalPage.clickViewInPreRenewalDirection();
                    preRenewalPage.closeAllPreRenewalDirectionExplanations();
                    preRenewalPage.clickClosePreRenewalDirection();
                    preRenewalPage.clickReturnToSummaryPage();
                }
            }
        }

        repository.pc.policy.PolicySummary policySummary = new repository.pc.policy.PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        if (checkIfElementExists("//span[contains(@class, 'tree-node') and text()='Renewal Information']", 1000)) {
            new repository.pc.sidemenu.SideMenuPC(driver).clickRenewalInformation();
            if (checkIfElementExists("//div[text()='Custom Farming']", 1000)) {
                new GenericWorkorderRenewalInformation(getDriver()).setCustomFarmingLastYearsAmount(1000);
        		new GenericWorkorderRenewalInformation(driver).setCustomFarmingRenewalExtimatedReceipts(1000);
            }
            if (checkIfElementExists("//div[contains(@id, ':RenewalPastTermSectionIIIVehicleDV')]", 1000)) {
                new GenericWorkorderRenewalInformation(getDriver()).setFarmAndShowcarRenewalOdometer("500");
            }
            //add code for farm truck and show car
        }
        
        
        
        //Check to see if the quote button is there and click it if so. Otherwise, skip it.
        try {
        	new GuidewireHelpers(driver).editPolicyTransaction();
            if(localPolicy.lineSelection.contains(LineSelection.PropertyAndLiabilityLinePL)){
            setAllDefensibleSpace(localPolicy);
//            new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickOk();
            }
            genericWO.clickGenericWorkorderQuote();
        } catch (Exception e) {
            systemOut("The quote button could not be clicked. It may have already been clicked. If your test fails, look here for the error.");
        }

        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQuote();
        repository.pc.workorders.generic.GenericWorkorderQuote quote = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnaysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
            riskAnaysis.approveAll_IncludingSpecial();
            riskAnaysis.Quote();
        }

        clickRenewPolicy(renewalCode);
    }

    //This method will start a renewal from the policy menu and go through the requisite steps to manually renew it.

    public void renewPolicyManually(RenewalCode renewalCode, GeneratePolicy policy) throws Exception {
    	startRenewal();
//    	quoteAndIssueRenewal(renewalCode, policy);
    	InfoBar infoBar = new InfoBar(getDriver());
    	if(infoBar.getInfoBarProductLineType() != ProductLineType.Membership){ 
    		quoteAndIssueRenewal(renewalCode, policy);

    		repository.pc.workorders.generic.GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
    		waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    	}
    }


    public void clickUpdateButton() {
        super.clickUpdate();
    }


    public void waitForPreRenewalDirections() throws GuidewireNavigationException {
        long endtime = new Date().getTime() + 10000;
        boolean checkPreRenewal = checkIfElementExists(text_PreRenewalWarningMessagePolicyPage, 100);
        while (!checkPreRenewal && new Date().getTime() < endtime) {
            sleep(1); //Used to wait for a second in between checks for pre-renewal directions to show up.
            repository.pc.policy.PolicySummary policySummary = new repository.pc.policy.PolicySummary(driver);
            policySummary.clickPendingTransaction(TransactionType.Renewal);
            InfoBar infoBar = new InfoBar(driver);
            infoBar.clickInfoBarPolicyNumber();
            checkPreRenewal = checkIfElementExists(text_PreRenewalWarningMessagePolicyPage, 100);
        }
    }

    public void loginAsUWAndIssueRenewal(GeneratePolicy generatePolicy) throws Exception{
        new Login(driver).loginAndSearchPolicy_asUW(generatePolicy);
        try {
            startRenewal();
            quoteAndIssueRenewal(RenewalCode.Renew_Good_Risk, generatePolicy);
        } catch (Exception e) {
            e.printStackTrace();
            new InfoBar(driver).clickInfoBarPolicyNumber();
            Assert.assertTrue(new PolicySummary(driver).verifyPolicyTransaction(null, null, null, TransactionType.Renewal, null, 120),"Policy was not able to review please investigate ");
        }
    }

}
