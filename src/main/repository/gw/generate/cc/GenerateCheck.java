package repository.gw.generate.cc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import repository.cc.actionsmenu.ActionsMenu;
import repository.cc.claim.*;
import repository.cc.enums.CheckLineItemCategory;
import repository.cc.enums.CheckLineItemType;
import repository.cc.sidemenu.SideMenuCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimsUsers;
import repository.gw.enums.GenerateCheckType;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

public class GenerateCheck extends BasePage {

    public static class Builder {

        private String checkType = null;
        private String claimNumber = null;
        private String creatorPassword = null;
        private String creatorFullName = null;
        private String creatorUserName = null;
        private CheckLineItemType paymentType = null;
        private String paymentAmount = null;
        private boolean deductibleToAdd = false;
        private String deductibleAmount = null;
        private CheckLineItemCategory categoryType = null;
        private String companyCheckBook = null;
        private String checkNumber = null;
        private String payToName = null;
        private String mailingAddress = null;
        private String taxReporting = null;
        private String exposureInfo = null;
        private String reserveLine = null;
        private String costCategory = null;
        private WebDriver driver;

        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public GenerateCheck build(repository.gw.enums.GenerateCheckType checkType) throws Exception {
            this.checkType = checkType.toString();
            return new GenerateCheck(checkType, this);
        }

        public Builder withExposureInfo(String exposureInfo) {
            this.exposureInfo = exposureInfo;
            return this;
        }

        public Builder withMailingAddress(String mailingAddress) {
            this.mailingAddress = mailingAddress;
            return this;
        }

        public Builder withTaxReproting(String taxReporting) {
            this.taxReporting = taxReporting;
            return this;
        }

        public Builder withCategoryType(CheckLineItemCategory categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public Builder withPayToName(String payToName) {
            this.payToName = payToName;
            return this;
        }

        public Builder withClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
            return this;
        }

        public Builder withCompanyCheckBook(String companyCheckBook) {
            this.companyCheckBook = companyCheckBook;
            return this;
        }

        public Builder withCreatorUserNamePassword(ClaimsUsers user, String creatorPassword) {
            this.creatorUserName = user.toString();
            this.creatorFullName = user.getName();
            this.creatorPassword = creatorPassword;
            return this;
        }

        public Builder withDeductible(boolean addDeductible) {
            this.deductibleToAdd = addDeductible;
            return this;
        }

        public Builder withDeductibleAmount(String deductibleAmount) {
            this.deductibleAmount = deductibleAmount;
            return this;
        }

        public Builder withPaymentAmount(String paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder withPaymentType(CheckLineItemType paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Builder withCheckNumber(String checkNumber) {
            this.checkNumber = checkNumber;
            return this;
        }

        public Builder withReserveLine(String reserveLine) {
            this.reserveLine = reserveLine;
            return this;
        }

        public Builder withCostCategory(String costCategory) {
            this.costCategory = costCategory;
            return this;
        }

    }

    // Private and public named items

    private String creatorUserName = null;
    public String claimNumber = null;
    private String creatorPassword = null;
    private String creatorFullName = null;
    private CheckLineItemType paymentType = null;
    private String paymentAmount = null;
    private CheckLineItemCategory categoryType = null;
    private boolean deductibleToAdd;
    private String deductibleAmount = null;
    private String checkType = null;
    private String companyCheckBook = null;
    private String checkNumber = null;
    private String payToName = null;
    private String mailingAddress = null;
    private String taxReporting = null;
    private String exposureInfo = null;
    private String reserveLine = null;
    private String costCategory = null;
    private WebDriver driver;

    // /////////////////////////////////////////
    // Methods for the various types of Checks//
    // /////////////////////////////////////////

    public GenerateCheck(GenerateCheckType checkType, Builder builderDetails) throws Exception {
        super(builderDetails.driver);
        this.driver = builderDetails.driver;
        switch (checkType) {
            case Regular:
                regularCheck(builderDetails);
                break;
            case LAE:
                laeCheck(builderDetails);
                break;
            case Field:
                fieldCheck(builderDetails);
                break;
            case Draft:
                draftCheck(builderDetails);
                break;
            case ReissueVoided:
                reissueVoidedCheck(builderDetails);
                break;
            case Closed:
                // TO DO
                break;
            default:
                break;
        }
    }

    public String getCheckType() {
        return checkType;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getDeductibleAmount() {
        return deductibleAmount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    private void draftCheck(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.paymentType = builderStuff.paymentType;
        this.categoryType = builderStuff.categoryType;
        this.paymentAmount = builderStuff.paymentAmount;
        this.deductibleToAdd = builderStuff.deductibleToAdd;
        this.deductibleAmount = builderStuff.deductibleAmount;
        this.checkType = builderStuff.checkType;
        this.creatorFullName = builderStuff.creatorFullName;
        this.companyCheckBook = builderStuff.companyCheckBook;
        this.driver = builderStuff.driver;

        CheckWizard checkWiz = new CheckWizard(this.driver);
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);
        ActionsMenu actionMenu = new ActionsMenu(this.driver);
        FinancialsTransactions financialTransactions = new FinancialsTransactions(this.driver);
        ManualCheckWizard manualCheckWiz = new ManualCheckWizard(this.driver);
        ManualCheckRegister checkBook = new ManualCheckRegister(this.driver);

        checkBook.manualCheckBook(creatorFullName, checkType, companyCheckBook);

        new repository.gw.login.Login(this.driver).login(creatorUserName, creatorPassword);
        

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);
        
        actionMenu.checkAbilityToPay();
        
        sideMenu.clickFinancialTransactions();
        
        financialTransactions.selectReserves();
        
        String costCategory = financialTransactions.getCostCategory(0);
        
        actionMenu.checkTypeMenuPicker(checkType);
        

        this.checkNumber = manualCheckWiz.enterPayeeInformation(checkType, checkBook);

        manualCheckWiz.selectSpecific_ReserveLine(costCategory);
        
        manualCheckWiz.selectSpecific_PaymmentPartialOrFinal("Partial");
        
        manualCheckWiz.lineItemPopulate(paymentType, categoryType, paymentAmount, 1);
        
        if (deductibleToAdd) {
            this.paymentType = CheckLineItemType.DEDUCTIBLE;
            this.categoryType = CheckLineItemCategory.INDEMNITY;
            
            manualCheckWiz.clickAddItem();
            
            manualCheckWiz.lineItemPopulate(paymentType, categoryType, deductibleAmount, 2);
        }
        manualCheckWiz.clickNextButton();
        
        manualCheckWiz.clickFinishButton();
        checkWiz.validationControl();
        checkWiz.approveChecks(claimNumber);
        
    }

    @FindBy(css = "div[class='message']")
    private WebElement messageElement;

//    private String getMessage() {
//
//        String errorMessage = null;
//
//        try {
//            waitUntilElementIsVisible(messageElement, 5000);
//            messageElement.getText();
//        } catch(Exception e) {
//            System.out.println("No System Message Found.");
//        }
//
//        return errorMessage;
//    }

//    private Boolean isCheckNumberInUse() {
//
//        Boolean inUse = true;
//        System.out.println(getMessage());
//        if (getMessage() == null || getMessage().equalsIgnoreCase("")) {
//            inUse = false;
//        } else if(getMessage().contains("is already in use")) {
//            inUse = true;
//        }
//        return inUse;
//    }

    private void fieldCheck(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.paymentType = builderStuff.paymentType;
        this.categoryType = builderStuff.categoryType;
        this.paymentAmount = builderStuff.paymentAmount;
        this.deductibleToAdd = builderStuff.deductibleToAdd;
        this.deductibleAmount = builderStuff.deductibleAmount;
        this.checkType = builderStuff.checkType;
        this.creatorFullName = builderStuff.creatorFullName;
        this.companyCheckBook = builderStuff.companyCheckBook;
        this.checkNumber = builderStuff.checkNumber;
        this.driver = builderStuff.driver;

        CheckWizard checkWiz = new CheckWizard(this.driver);
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);
        ActionsMenu actionMenu = new ActionsMenu(this.driver);
        FinancialsTransactions financialTransactions = new FinancialsTransactions(this.driver);
        ManualCheckWizard manualCheckWiz = new ManualCheckWizard(this.driver);

        ManualCheckRegister checkBook = new ManualCheckRegister(this.driver);
        checkBook.manualCheckBook(creatorFullName, checkType, companyCheckBook);

        new repository.gw.login.Login(this.driver).login(creatorUserName, creatorPassword);
        

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);
        
        actionMenu.checkAbilityToPay();
        
        sideMenu.clickFinancialTransactions();
        
        financialTransactions.selectReserves();
        
        String costCategory = financialTransactions.getCostCategory(0);
        
        actionMenu.checkTypeMenuPicker(checkType);
        

            this.checkNumber = manualCheckWiz.enterPayeeInformation(checkType, checkBook);

        
        manualCheckWiz.selectSpecific_ReserveLine(costCategory);
        
        manualCheckWiz.selectSpecific_PaymmentPartialOrFinal("Partial");
        
        manualCheckWiz.lineItemPopulate(paymentType, categoryType, paymentAmount, 1);
        
        if (deductibleToAdd) {
            this.paymentType = CheckLineItemType.DEDUCTIBLE;
            this.categoryType = CheckLineItemCategory.INDEMNITY;
            
            manualCheckWiz.clickAddItem();
            
            manualCheckWiz.lineItemPopulate(paymentType, categoryType, deductibleAmount, 2);
        }
        manualCheckWiz.clickNextButton();
        
        manualCheckWiz.clickFinishButton();
        checkWiz.validationControl();
        checkWiz.approveChecks(claimNumber);
        
    }

    private void laeCheck(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.paymentType = builderStuff.paymentType;
        this.categoryType = builderStuff.categoryType;
        this.paymentAmount = builderStuff.paymentAmount;
        this.deductibleToAdd = builderStuff.deductibleToAdd;
        this.deductibleAmount = builderStuff.deductibleAmount;
        this.checkType = builderStuff.checkType;
        this.driver = builderStuff.driver;

        CheckWizard checkWiz = new CheckWizard(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        ActionsMenu actionMenu = new ActionsMenu(this.driver);
        FinancialsTransactions financialTransactions = new FinancialsTransactions(this.driver);


        String costCategory = null;

        new repository.gw.login.Login(this.driver).login(creatorUserName, creatorPassword);
        

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);
        
        actionMenu.checkAbilityToPay();
        
        sideMenu.clickFinancialTransactions();
        
        financialTransactions.selectReserves();
        
        costCategory = financialTransactions.getCostCategory(0);
        
        actionMenu.checkTypeMenuPicker(checkType);
        
        checkWiz.setPrimaryPayeeName("insured");
        
        checkWiz.setPrimaryPayeeType("Claimant");
        
        checkWiz.clickNextButton();
        
        checkWiz.selectSpecific_ReserveLine(costCategory);
        
        checkWiz.setPaymentType("Partial");
        
        checkWiz.setChecksLineItems(paymentType, categoryType, paymentAmount);
        
        if (deductibleToAdd) {
            this.paymentType = CheckLineItemType.DEDUCTIBLE;
            this.categoryType = CheckLineItemCategory.INDEMNITY;
            
            checkWiz.clickAddItemButton();
            
            checkWiz.setChecksLineItems(paymentType, categoryType, deductibleAmount);
        }
        checkWiz.clickNextButton();
        
        checkWiz.click_FinishButton();
        checkWiz.validationControl();
        checkWiz.approveChecks(claimNumber);

        
    }

    private void regularCheck(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.paymentType = builderStuff.paymentType;
        this.categoryType = builderStuff.categoryType;
        this.paymentAmount = builderStuff.paymentAmount;
        this.deductibleToAdd = builderStuff.deductibleToAdd;
        this.deductibleAmount = builderStuff.deductibleAmount;
        this.checkType = builderStuff.checkType;
        this.driver = builderStuff.driver;

        CheckWizard checkWiz = new CheckWizard(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        FinancialsSummary financials = new FinancialsSummary(this.driver);
        ActionsMenu actions = new ActionsMenu(this.driver);


        new repository.gw.login.Login(this.driver).login(creatorUserName, creatorPassword);
        

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);
        

        actions.checkAbilityToPay();


//	Pickers experiencing errors - Defect Created
        try {
            
            sideMenu.clickFinancialsLink();
        } catch (Exception e) {
            systemOut("error caught");
            
            actions.escapeMenu();
            
            sideMenu.clickFinancialsLink();
        }
        
        financials.useFinancialsPickers(1);
        

//		ActionsMenu actionsMenu = new ActionsMenu(this.driver);
//		
//		actionsMenu.clickActionsButton();
//		actionsMenu.hoverCheckLink();
//		actionsMenu.clickRegularPayment();


        checkWiz.enterPayeeInformation(checkWiz.getInsuredName());
        checkWiz.enterPaymentInformation(paymentAmount, deductibleToAdd, deductibleAmount);

        
        checkWiz.click_FinishButton();
        
        checkWiz.validationControl();
        
        checkWiz.approveChecks(claimNumber);
        
    }

    public void reissueVoidedCheck(Builder builderStuff) throws Exception {
        this.creatorUserName = builderStuff.creatorUserName;
        this.creatorPassword = builderStuff.creatorPassword;
        this.claimNumber = builderStuff.claimNumber;
        this.paymentType = builderStuff.paymentType;
        this.categoryType = builderStuff.categoryType;
        this.paymentAmount = builderStuff.paymentAmount;
        this.deductibleToAdd = builderStuff.deductibleToAdd;
        this.deductibleAmount = builderStuff.deductibleAmount;
        this.checkType = builderStuff.checkType;
        this.creatorFullName = builderStuff.creatorFullName;
        this.companyCheckBook = builderStuff.companyCheckBook;
        this.checkNumber = builderStuff.checkNumber;
        this.payToName = builderStuff.payToName;
        this.mailingAddress = builderStuff.mailingAddress;
        this.taxReporting = builderStuff.taxReporting;
        this.exposureInfo = builderStuff.exposureInfo;
        this.reserveLine = builderStuff.reserveLine;
        this.costCategory = builderStuff.costCategory;
        this.driver = builderStuff.driver;

        CheckWizard checkWiz = new CheckWizard(this.driver);
        ActionsMenu actions = new ActionsMenu(this.driver);
        SideMenuCC sideMenu = new SideMenuCC(this.driver);
        TopMenu topMenu = new TopMenu(this.driver);
        ActionsMenu actionMenu = new ActionsMenu(this.driver);
        FinancialsTransactions financialTransactions = new FinancialsTransactions(this.driver);


        new Login(this.driver).login(creatorUserName, creatorPassword);
        

        topMenu.clickClaimTabArrow();
        topMenu.setClaimNumberSearch(claimNumber);
        
        actionMenu.checkAbilityToPay();
        
        sideMenu.clickFinancialTransactions();
        
        financialTransactions.selectReserves();
        

        SystemGeneratedCheckWizard sysCheckWiz = actions.clickReissueClearedCheck();
        

        sysCheckWiz.enterPayeeInformation(payToName, taxReporting, mailingAddress, checkNumber);
        sysCheckWiz.EnterPaymentInformation(reserveLine, paymentAmount, "/" + costCategory);

        try {
            String errorMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
            if (errorMessage.contains("is already in use. Please choose another number.")) {
                Assert.fail("Check has already been reissued.");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        sysCheckWiz.lineItemPopulate("Indemnity", "Indemnity", paymentAmount, 1);

        sysCheckWiz.clickNextButton();
        
        sysCheckWiz.clickFinishButton();
        
        checkWiz.validationControl();
        checkWiz.approveChecks(claimNumber);
        

    }

    public String getExposureInfo() {
        return exposureInfo;
    }

}
