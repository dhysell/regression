package repository.pc.workorders.change;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ChangeReason;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.*;

import java.util.ArrayList;
import java.util.Date;

public class StartPolicyChange extends GenericWorkorder {
	
    private WebDriver driver;
    private ErrorHandlingHelpers errorHandlingHelpers;
    
    public StartPolicyChange(WebDriver driver) {
        super(driver);
        this.driver = driver;
        errorHandlingHelpers = new ErrorHandlingHelpers(driver);

        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[contains(@id, ':StartPolicyChangeDV:EffectiveDate_date-inputEl')]")
    private WebElement EffectiveDate_date;

    @FindBy(xpath = "//a[contains(@id, 'NewPolicyChange') or contains(@id, 'PolicyChangeWizard:Next') or contains(@id, ':Next')]")
    private WebElement NewPolicyChange;

    @FindBy(xpath = "//a[contains(@id, ':BindPolicyChange')]")
    private WebElement IssuePolicyChange;

    @FindBy(xpath = "//span[contains(@id, ':BindPolicyChange-btnEl')] | //span[contains(@id, ':RequestApproval-btnEl')]/span[contains(text(), 'Submit')]")
    private WebElement IssuePolicyChangeAlt;

    @FindBy(xpath = "//div[contains(@id, ':AccountInfoInputSet:Name-inputEl')]")
	public WebElement changePrimaryInsuredContact;

    @FindBy(xpath = "//label[contains(@id, 'PanelSet:Warning')]")
    private WebElement label_OOSChange;

    @FindBy(xpath = "//span[contains(@id, ':RequestApproval-btnEl')]")
    private WebElement SubmitPolicyChange;

    public Guidewire8Select select_AddressType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'InputSet:AddressType-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, ':AddressLine1-inputEl')]")
	private WebElement editBox_ContactAddress;
    
    @FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_ContactCity;
    
    private Guidewire8Select select_ContactState() {
 		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
	}
    
    @FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
	private WebElement editBox_ContactZip;

    /**
     * This method is used for working through the initial steps to start a policy change.
     *
     * @param description - String description of what the policy change is for.
     * @param date - Effective Date (formatted as MM/dd/yyyy) of the policy change. A null value can be passed in, in which case this method will default to the current system date.
     * @ends-on - Policy Info
     * @return policy transaction number
     * @throws Exception 
     */
    public String startPolicyChange(String description, Date date) throws Exception {
    	String policyChangeNumber = null;

        repository.pc.policy.PolicyMenu policyMenu = new repository.pc.policy.PolicyMenu(driver);
        policyMenu.clickMenuActions();
        
        policyMenu.clickChangePolicy();
        
        if (date != null) {
            setEffectiveDate(date);
        } else {
            setEffectiveDate(DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.PolicyCenter));
        }
        
        clickDescription();
        setDescription(description);
        
        clickPolicyChangeNext();
        selectOKOrCancelFromPopup(OkCancel.OK);
        policyChangeNumber = waitUntilElementIsVisible(find(By.xpath("//span[contains(@id, 'PolicyChangeWizard:0_header_hd-textEl')]"))).getText();
        policyChangeNumber = policyChangeNumber.substring(policyChangeNumber.indexOf("-")+1, policyChangeNumber.length()-6);
        new repository.pc.sidemenu.SideMenuPC(driver).clickSideMenuPolicyInfo();
        
        return policyChangeNumber;
    }

    // helper methods

    public void setEffectiveDate(Date effectiveDate) {
        waitUntilElementIsVisible(EffectiveDate_date);
        EffectiveDate_date.click();
        EffectiveDate_date.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        EffectiveDate_date.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", effectiveDate));
        EffectiveDate_date.sendKeys(Keys.TAB);
    }


    public void clickPolicyChangeNext() {
        clickWhenClickable(NewPolicyChange);
    }

    public void quoteAndIssue() {
        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();
        
        long endtime = new Date().getTime() + 30000;
        while(new ErrorHandlingHelpers(driver).getErrorMessage().contains("billing system") || new Date().getTime() > endtime) {
        	new GuidewireHelpers(driver).editPolicyTransaction();
        	genericWO.clickGenericWorkorderQuote();
        }
        
		
        if (new ErrorHandlingHelpers(driver).errorExistsValidationResults()) {
            systemOut("There was an error on the validation results page. going back to trigger automatic fixes. This was the error (for debugging purposes): " + new ErrorHandlingHelpers(driver).getValidationResultsErrorText());

            genericWO.clickGenericWorkorderBack();
            genericWO.clickGenericWorkorderQuote();
        }

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(getDriver());
        if (quotePage.isPreQuoteDisplayed()) {
        	quotePage.clickPreQuoteDetails();
        }
        if (quotePage.hasBlockBind() || quotePage.hasBlockQuoteRelease()) {
            GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(getDriver());
            try {
                riskAnalysis.handleBlockSubmitForPolicyChangeWithSameLogin(driver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(new GuidewireHelpers(getDriver()).isOnPage("//span[contains(@id, ':JobLabel-btnInnerEl')]/span[contains(text(), 'Policy Change (Draft)')]", 5000)){
            genericWO.clickGenericWorkorderQuote();
            if (new ErrorHandlingHelpers(driver).errorExistsValidationResults()) {
                systemOut("There was an error on the validation results page. going back to trigger automatic fixes. This was the error (for debugging purposes): " + new ErrorHandlingHelpers(driver).getValidationResultsErrorText());

                genericWO.clickGenericWorkorderBack();
                genericWO.clickGenericWorkorderQuote();
            }
            if (quotePage.hasBlockBind() || quotePage.hasBlockQuoteRelease()) {
                GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(getDriver());
                try {
                    riskAnalysis.handleBlockSubmitForPolicyChangeWithSameLogin(driver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        clickIssuePolicy();
        
//        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(getDriver());
//        if (!woComplete.woCompletePageExists()) {
//            UWActivityPC activity = new UWActivityPC(driver);
//            activity.setText("I changed some stuffs");
//            activity.setChangeReason(ChangeReason.AnyOtherChange);
//            activity.clickSendRequest();
//            
//            GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(getDriver());
//        }
        
    }


    public void quoteAndSubmit(ChangeReason reason, String changeDescription) {
        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();
        if(checkIfElementExists("//span[contains(@id, ':BindPolicyChange-btnEl')]", 2000)) {
        	clickIssuePolicy();
        } else {
        	genericWO.clickGenericWorkorderSubmitOptionsSubmit();
        }
        
        GenericWorkorderComplete woComplete = new GenericWorkorderComplete(getDriver());
        if (!woComplete.woCompletePageExists()) {
            repository.pc.activity.UWActivityPC activity = new UWActivityPC(driver);
            activity.setText(changeDescription);
            activity.setChangeReason(reason);
            activity.clickSendRequest();
        }
    }

    public void clickIssuePolicy() {
        try {
            clickWhenClickable(IssuePolicyChange);
        } catch (Exception e) {
            clickWhenClickable(IssuePolicyChangeAlt);
        }
        selectOKOrCancelFromPopup(OkCancel.OK);
    }
    
    public boolean checkIssuePolicy(){
    	return checkIfElementExists(IssuePolicyChange, 100);
    }


    public void clickSubmitPolicyChange() {
        
        clickWhenClickable(SubmitPolicyChange);
    }

    public void removeMembershipDues(String contactName) throws Exception {
        startPolicyChange("Remove Membership Dues", null);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(getDriver());
        policyInfo.removeMembershipDuesContact(contactName);

        quoteAndIssue();
    }


    public void removeMembershipDuesContact(String contactName) throws Exception {
        startPolicyChange("Remove Membership Dues Contact", null);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(getDriver());
        policyInfo.removeMembershipDuesContact(contactName);

        quoteAndIssue();
    }


    public GeneratePolicy addMembershipDues(GeneratePolicy policyObject, ArrayList<Contact> membershipMembersToAdd) throws Exception {
    	for (Contact contactToAdd : membershipMembersToAdd) {
    		policyObject.membership.getMembersList().add(contactToAdd);
    	}
    	
        startPolicyChange("Add Membership Dues", null);
        
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
    	sideMenu.clickSideMenuMembershipMembers();

		GenericWorkorderMembershipMembers membershipMembers = new GenericWorkorderMembershipMembers(getDriver());
		membershipMembers.setMembershipMembers(policyObject, false);

        sideMenu.clickSideMenuHouseholdMembers();
        
		clickGenericWorkorderSaveDraft();
		
		
		sideMenu = new repository.pc.sidemenu.SideMenuPC(getDriver());
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(getDriver());
        risk.Quote();
        
        GenericWorkorderQuote quote = new GenericWorkorderQuote(getDriver());
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        
        sideMenu.clickSideMenuQuote();
        
        new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).setMembershipDuesAmount(quote.getQuoteTotalMembershipDues());
        new GuidewireHelpers(getDriver()).getPolicyPremium(policyObject).setChangeInCost(quote.getQuoteChangeInCost());
        clickIssuePolicy();

        return policyObject;
    }

    public void removeBuildingByBldgNumber(int bldgNum) throws Exception {
        startPolicyChange("remove a building", null);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.removeBuildingByBldgNumber(bldgNum);
        quoteAndIssue();
    }
    
    
    public void removeBuildingAndChangePayer(GeneratePolicy policyObj, int bldgNum, boolean changePayer, String payerName) throws Exception {
        startPolicyChange("remove a building", null);
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(getDriver());
        switch (policyObj.productType) {
        case Businessowners:
	    	 sideMenu.clickSideMenuBuildings();	
	         GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
	         building.removeBuildingByBldgNumber(bldgNum);
	         if(changePayer){
	        	 sideMenu.clickSideMenuPayerAssignment();
	        	 GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(getDriver());
	        	 payerAssignment.setPayerAssignmentBillAllCoverages(payerName);
	        	 payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
	         }
	         quoteAndIssue();
	         break;
        case Squire:
        	sideMenu.clickSideMenuSquirePropertyDetail();
            GenericWorkorderSquirePropertyDetail propertyDetail = new GenericWorkorderSquirePropertyDetail(getDriver());
            propertyDetail.removeBuilding(Integer.toString(bldgNum));   
            if(changePayer){
	        	 sideMenu.clickSideMenuPayerAssignment();
	        	 GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(getDriver());
	        	 payerAssignment.setPayerAssignmentBillAllCoverages(payerName);
	        	 payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);
	         }
            GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(getDriver());
            sideMenu.clickSideMenuRiskAnalysis();
            risk.Quote();
            
            sideMenu.clickSideMenuRiskAnalysis();
            risk.approveAll();
//            risk.specialApproveAll();
            sideMenu.clickSideMenuQuote();
            
            clickIssuePolicy();
            break;
        //add other product lines below
		default:
			break;            
        }       
    }
    

    public void removeBuildingByBldgDescription(String buildingDescription, String policyChangeDescription) throws Exception {
        startPolicyChange(policyChangeDescription, null);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.removeBuildingByBldgDescription(buildingDescription);
        quoteAndIssue();
    }

    public void changeBuildingCoverage(int bldgNum, double buildingLimit, double bppLimit) throws Exception {
        startPolicyChange("change coverage", DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.PolicyCenter));

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.clickBuildingsBuildingEdit(bldgNum);
        
        building.setBuildingLimit(buildingLimit);
        
        building.setBuildingPersonalPropertyLimit(bppLimit);
        
        building.clickOK();
        
        quoteAndIssue();
        
    }

    // change insured/lienholder coverage-change the personal limit amount and
    // the property limit amount

    public void changeBuildingCoverage(int bldgNum, double buildingLimit, double propertyLimit, Date effectiveDate) throws Exception {
        startPolicyChange("change coverage", effectiveDate);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.clickBuildingsBuildingEdit(bldgNum);
        
        building.setBuildingLimit(buildingLimit);
        
        building.setBuildingPersonalPropertyLimit(propertyLimit);
        
        building.clickOK();
        
        quoteAndIssue();
        
    }

    public void changeBOPBuildingCoverage(int bldgNum, double buildingLimit, double bppLimit) throws Exception {
        startPolicyChange("change coverage", DateUtils.getCenterDate(getDriver(), ApplicationOrCenter.PolicyCenter));

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.clickBuildingsBuildingEdit(bldgNum);
        
        building.setBuildingLimit(buildingLimit);
        
        building.setBuildingPersonalPropertyLimit(bppLimit);
        
        building.clickOK();
        

        GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();
        
        if (errorHandlingHelpers.errorExistsValidationResults()) {
            systemOut("There was an error on the validation results page. going back to trigger automatic fixes. This was the error (for debugging purposes): " + errorHandlingHelpers.getValidationResultsErrorText());
            genericWO.clickGenericWorkorderBack();
            
            genericWO.clickGenericWorkorderQuote();
        }

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(getDriver());
        if (quotePage.hasBlockBind()) {
            GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(getDriver());
            try {
                sideMenu.clickSideMenuRiskAnalysis();
                riskAnalysis.approveAll_IncludingSpecial();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clickIssuePolicy();
        
    }

    public void changePLPropertyCoverage(double propertyLimit, Date effectiveDate) throws Exception {
        startPolicyChange("change coverage", effectiveDate);

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages propertyCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(getDriver());
        propertyCoverages.setCoverageALimit(propertyLimit);
        
        quoteAndIssue();
        
    }
    
    public void resetPayerAssignmentToInsured() throws Exception {
        startPolicyChange("Change Payer Assignment", null);

        repository.pc.sidemenu.SideMenuPC menu = new repository.pc.sidemenu.SideMenuPC(driver);
        menu.clickSideMenuPayerAssignment();
        
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(getDriver());
        payerAssignment.resetAllCoveragesBackToInsured();
        quoteAndIssue();
    }


    public void setWholeAddress(AddressInfo address) {
		
		if (address.getLine1() != null && address.getLine1() != "") {
			setAddress(address.getLine1());
		}
		

		if (address.getCity() != null && address.getCity() != "") {
			setCity(address.getCity());
		}
		

		if (address.getState() != null) {
			setState(address.getState());
		}
		

		if (address.getZip() != null && address.getZip() != "") {
			setZip(address.getZip());
		}
	}
    
    public void addFarmEquipmentToStdIM(FarmEquipment newFarmEquipToAdd, String description) throws Exception{
    	startPolicyChange(description, null);
		repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
		sideMenu.clickSideMenuStandardIMCoverageSelection();
		GenericWorkorderStandardIMCoverageSelection coverageSelection = new GenericWorkorderStandardIMCoverageSelection(driver);
		coverageSelection.checkCoverage(true, "Farm Equipment");
		sideMenu.clickSideMenuStandardIMFarmEquipment();
		GenericWorkorderStandardIMFarmEquipment farmEquipment = new GenericWorkorderStandardIMFarmEquipment(driver);		
		
		farmEquipment.addFarmEquip(true, newFarmEquipToAdd);	
		quoteAndIssue();
    }
    
    private void setAddress(String addressToFill) {
		clickWhenClickable(editBox_ContactAddress);
		editBox_ContactAddress.sendKeys(Keys.chord(Keys.CONTROL + "a"), addressToFill);
	}
    
    private void setCity(String cityToFill) {
		clickWhenClickable(editBox_ContactCity);
		editBox_ContactCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), cityToFill);
	}
    
    private void setState(State stateToSelect) {
		Guidewire8Select commonState = select_ContactState();
		commonState.selectByVisibleText(stateToSelect.getName());
	}
    
    private void setZip(String zipToFill) {
		clickWhenClickable(editBox_ContactZip);
		editBox_ContactZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipToFill);
	}
    
    public void changeExpirationDate(Date expDate, String description) {
        repository.pc.policy.PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickExpirationDateChange();
        
        setDescription(description);
        
        clickPolicyChangeNext();
        
        setExpirationDate(expDate);
        
        quoteAndIssue();
    }


    public void changeLoanNumber(int locationNumber, int bldgNum, String lienholderName, String newLoanNumber, String description, Date effectiveDate) throws Exception {
        if (description != null) {
            startPolicyChange(description, effectiveDate);
        } else {
            startPolicyChange("Change Policy", effectiveDate);
        }

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings building = new GenericWorkorderBuildings(getDriver());
        building.clickBuildingsLocationsRow(locationNumber);
        building.clickBuildingsBuildingEdit(bldgNum);
        
        GenericWorkorderAdditionalInterests additionalInterest = new GenericWorkorderAdditionalInterests(getDriver());
        additionalInterest.clickBuildingsPropertyAdditionalInterestsLink(lienholderName);
        
        additionalInterest.setAdditionalInterestsLoanNumber(newLoanNumber);
        
        additionalInterest.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        
        building.clickOK();
        

        sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();
        
        GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(getDriver());
        payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

        quoteAndIssue();
        
    }

    public void addVehicleToCPP(boolean basicSearch, Vehicle vehicle, String description, Date effectiveDate) throws Exception {
        if (description != null) {
            startPolicyChange(description, effectiveDate);
        } else {
            startPolicyChange("Change Policy", effectiveDate);
        }

        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();
        GenericWorkorderVehicles vehiclePage = new GenericWorkorderVehicles(getDriver());
        vehiclePage.createNewVehicleCPP(basicSearch, vehicle);
        
        quoteAndIssue();
    }

    public void selectMembershipLine() throws Exception {
        startPolicyChange("Select Membership Line", null);
        repository.pc.sidemenu.SideMenuPC sideMenu = new repository.pc.sidemenu.SideMenuPC(getDriver());
        sideMenu.clickSideMenuLineSelection();
        
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(getDriver());
        lineSelection.checkMembership(true);
        
        quoteAndIssue();
    }

    public void releaseLockForPolicyChangeSubmittedByUW(GeneratePolicy policy) throws Exception {

        new Login(driver).loginAndSearchAccountByAccountNumber(policy.underwriterInfo.getUnderwriterUserName(), policy.underwriterInfo.getUnderwriterPassword(),policy.accountNumber);
        repository.pc.account.AccountSummaryPC summary = new AccountSummaryPC(driver);
        summary.clickActivitySubject("Submitted policy change");
        repository.pc.sidemenu.SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.approveAll();
        risk.clickReleaseLock();
        new TopInfo(driver).clickTopInfoLogout();

    }
    
    

}
