package repository.gw.generate.cc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.claim.CheckDetails;
import repository.cc.claim.searchpages.SearchChecksCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;


public class VoidedCheck extends BasePage {

    public static class Builder {
        private String userName = null;
        private String password = null;
        private String approvedBy = null;
        private String checkStatus = null;
        private String searchRange = null;
        private String checkNumber = null;
        private String payToName = null;
        private String amount = null;
        private String claimNumber = null;
        private String createdByUser = null;
        private String mailingAddress = null;
        private String taxReporting = null;
        private String exposureInfo = null;
        private int rowNumber;
        private WebDriver driver;

        public Builder(WebDriver driver) {
            this.driver = driver;
        }

        public VoidedCheck build() {
            return new VoidedCheck(this);
        }

        public Builder withUserNamePassword(ClaimsUsers user, String password) {
            this.userName = user.toString();
            this.password = password;
            return this;
        }

        public Builder withRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }

        public Builder withApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
            return this;
        }

        public Builder withCheckStatus(String checkStatus) {
            this.checkStatus = checkStatus;
            return this;
        }

        public Builder withSearchRange(String searchRange) {
            this.searchRange = searchRange;
            return this;
        }

        public Builder withCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
            return this;
        }

        public Builder withExposureInfo(String exposureInfo) {
            this.exposureInfo = exposureInfo;
            return this;
        }

        public String getCheckNumber() {
            return checkNumber;
        }

        public void setCheckNumber(String checkNumber) {
            this.checkNumber = checkNumber;
        }

        public String getPayToName() {
            return payToName;
        }

        public void setPayToName(String payToName) {
            this.payToName = payToName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getClaimNumber() {
            return claimNumber;
        }

        public void setClaimNumber(String claimNumber) {
            this.claimNumber = claimNumber;
        }

        public String getCreatedByUser() {
            return createdByUser;
        }

        public void setCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
        }

        public String getMailingAddress() {
            return mailingAddress;
        }

        public void setMailingAddress(String mailingAddress) {
            this.mailingAddress = mailingAddress;
        }

        public String getTaxReporting() {
            return taxReporting;
        }

        public void setTaxReporting(String taxReporting) {
            this.taxReporting = taxReporting;
        }

        public String getExposureInfo() {
            return exposureInfo;
        }

        public void setExposureInfo(String exposureInfo) {
            this.exposureInfo = exposureInfo;
        }

    }

    private WebElement checkNumberLink;
    private String approvedBy = null;
    private String checkStatus = null;
    private String searchRange = null;
    private String checkNumber = null;
    private String payToName = null;
    private String amount = null;
    private String claimNumber = null;
    private String createdByUser = null;
    private String mailingAddress = null;
    private String taxReporting = null;
    private String exposureData = null;
    private int rowNumber;
    private WebDriver driver;

    public VoidedCheck(Builder buildData) {
        super(buildData.driver);
        this.driver = buildData.driver;

        this.rowNumber = buildData.rowNumber;
        this.approvedBy = buildData.approvedBy;
        this.checkStatus = buildData.checkStatus;
        this.searchRange = buildData.searchRange;

        repository.gw.login.Login login = new Login(this.driver);

        login.login(buildData.userName, buildData.password);

        TopMenu topMenu = new TopMenu(this.driver);
        SearchChecksCC searchChecks = new SearchChecksCC(this.driver);

        topMenu.clickSearchTabArrow();
        topMenu.clickSearchChecks();

        searchChecks.searchVoidedChecks(approvedBy, checkStatus, searchRange);

        if (rowNumber == -1) {
            int rand = new TableUtils(getDriver()).getRowCount(find(By.xpath("//div[@id='PaymentSearch:PaymentSearchScreen:CheckSearchResultsLV']")));
            this.rowNumber = NumberUtils.generateRandomNumberInt(1, rand);
        }

        checkNumberLink = searchChecks.getCheckLink(rowNumber);
        this.checkNumber = checkNumberLink.getText();
        this.payToName = searchChecks.getPayTo(rowNumber);
        setAmount(searchChecks.getAmount(rowNumber));
        setClaimNumber(searchChecks.getClaim(rowNumber));

        CheckDetails checkDetails = clickCheckNumberLink();

        this.createdByUser = checkDetails.getCreatedByUser();
        this.mailingAddress = checkDetails.getMailingAddress();
        this.taxReporting = checkDetails.getTaxReporting();
        this.exposureData = checkDetails.getExposureData(amount);
    }


    // Check Number link
    private CheckDetails clickCheckNumberLink() {
        clickWhenClickable(checkNumberLink);
        return new CheckDetails(this.driver);
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public String getPayToName() {
        return payToName;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getTaxReporting() {
        return taxReporting;
    }


    public String getExposureData() {
        return exposureData;
    }

}
