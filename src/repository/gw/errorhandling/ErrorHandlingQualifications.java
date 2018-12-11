package repository.gw.errorhandling;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.pc.workorders.generic.GenericWorkorderQualification;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandlingQualifications extends ErrorHandlingHelpers {
    private WebDriver driver;

    public ErrorHandlingQualifications(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void errorHandlingQualificationPage(int timeToWait) throws Exception {

        saveDraft(timeToWait);

        if (errorExists()) {
            GenericWorkorderQualification qualificationsPage = new GenericWorkorderQualification(driver);
            ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
            for (WebElement errorMessage : errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages()) {
                String errorString = errorMessage.getText();
                if (errorString.equalsIgnoreCase("Please fill in all required fields.")) {
                    ErrorHandling errorJobType = new ErrorHandling(driver);
                    String jobType = errorJobType.text_ErrorHandlingJobType().getText();
                    if (jobType.contains("Full Application")) {
                        qualificationsPage.setFullAppAllTo(false);
                    } else {
                        qualificationsPage.setQuickQuoteAll(false);
                    }
                }
                saveDraft(timeToWait);
            }
            checkForRemainingErrors("Qualifications");
            resetClassFields();
        }
    }


    public List<String> getMessageText() {
        if (errorExists()) {
            List<String> messages = new ArrayList<String>();
            ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
            for (WebElement errorMessage : errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages()) {
                String errorString = errorMessage.getText();
                messages.add(errorString);
            }
            return messages;
        }
        return null;
    }
}
