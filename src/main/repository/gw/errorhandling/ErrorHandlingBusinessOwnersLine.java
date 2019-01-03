package repository.gw.errorhandling;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;

import java.util.List;

public class ErrorHandlingBusinessOwnersLine extends ErrorHandlingHelpers {

    private WebDriver driver;

    public ErrorHandlingBusinessOwnersLine(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    int i = 0;


    public void errorHandlingBusinessOwnersLinePage(int timeToWait, PolicyBusinessownersLine businessOwnersLine) throws GuidewireException {

        saveDraft(timeToWait);

        if (errorExists()) {
            ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
            this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
            for (this.i = 0; i < errorMessage.size(); i++) {
                errorMessage = errorHandlingBusinessOwnersLinePageSwitchCase(timeToWait, businessOwnersLine);
            }
            saveDraft(timeToWait);
        }
        checkForRemainingErrors("BusinessOwners Line");
        resetClassFields();
    }

    public List<WebElement> errorHandlingBusinessOwnersLinePageSwitchCase(int timeToWait, PolicyBusinessownersLine businessOwnersLine) throws GuidewireException {
//        GenericWorkorderBusinessownersLineAdditionalCoverages businessOwnersLineAdditionalCoveragesPage = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
        String errorString = this.errorMessage.get(this.i).getText();
        switch (errorString) {
            case "On current page:":
                saveDraft(timeToWait);
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                if (!this.errorMessage.get(0).getText().equalsIgnoreCase("On current page:")) {
                    i = -1;
                }
                break;
            case "On \"Additional Coverages\":":
                ErrorHandling errorAdditionalCoverages = new ErrorHandling(driver);
                clickWhenClickable(errorAdditionalCoverages.link_ErrorHandlingOnAdditionalCoverages());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                i = -1;
                break;
            case "On \"Included Coverages\":":
                ErrorHandling errorIncludedCoverages = new ErrorHandling(driver);
                clickWhenClickable(errorIncludedCoverages.link_ErrorHandlingOnIncludedCoverages());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                i = -1;
                break;
            case "Small Business Type : Missing required field \"Small Business Type\"":
                GenericWorkorderBusinessownersLineIncludedCoverages businessOwnersLineIncludedCoveragesPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
                businessOwnersLineIncludedCoveragesPage
                        .setSmallBusinessType(businessOwnersLine.getSmallBusinessType());
                this.errorMessage = errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages();
                break;
            case "How often are audits conducted? : Missing required field \"How often are audits conducted?\"":
//			businessOwnersLineAdditionalCoveragesPage.setBusinessownersLineAdditionalCoveragesEmployeeDishonestAuditsConductedFrequency(EmployeeDishonestyAuditsConductedFrequency.Monthly);
                i = -1;
                break;
            case "Who performs these audits? : Missing required field \"Who performs these audits?\"":
//			businessOwnersLineAdditionalCoveragesPage.setBusinessownersLineAdditionalCoveragesEmployeeDishonestyAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.CPA);
                break;
            case "Is Applicant in the business of selling or charging for liquor? : Missing required field \"Is Applicant in the business of selling or charging for liquor?\"":
//			businessOwnersLineAdditionalCoveragesPage.setBusinessownersLineAdditionalCoveragesSellingChargingLiquor(false);
            default:
                saveDraft(10);
                checkForRemainingErrors("BusinessOwners Line");
                break;
        }
        return errorMessage;
    }
}
