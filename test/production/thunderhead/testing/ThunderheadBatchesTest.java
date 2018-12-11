package production.thunderhead.testing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.helpers.EmailUtils;

import repository.driverConfiguration.Config;
import production.thunderhead.repository.batches.Batches;
import production.thunderhead.repository.batches.UIStuffToReturn;
import production.thunderhead.repository.login.ThunderheadLogin;
import production.thunderhead.repository.overview.Overview;
public class ThunderheadBatchesTest extends BaseTest {

    private String userName = "thunderhead";
    private String password = "password";
    private WebDriver driver;

    @Test()
    public void checkThunderheadBatchesAndSendEmail() throws Exception {
    	Config cf = new Config("http://th51prd/thunderhead");
    	cf.setHost("local");
		driver = buildDriver(cf);

        ThunderheadLogin loginPage = new ThunderheadLogin(driver);
        loginPage.login(userName, password);
        
        Overview overviewPage = new Overview(driver);
        overviewPage.clickOverviewErroredBatches();

        Batches batchesPage = new Batches(driver);
		Date todayDate = new Date();
		String emailContents = "<p><ul>";
		int totalIdErrors = 0;
		UIStuffToReturn returned = batchesPage.getErroredBatchIdsAssumingAllErroredForDate(todayDate);
		String[] erroredIdsForToday = returned.getBatchIds();
		String[] erroredNamesForToday = returned.getBatchNames();
		totalIdErrors = erroredIdsForToday.length;
		if(totalIdErrors > 0) {
			for(int i = 0; i < totalIdErrors; i++) {
				emailContents = emailContents + "<li>" + "Batch ID: " + erroredIdsForToday[i] + " Batch Name: " + erroredNamesForToday[i] + "</li>";
			}
		}
		
		String csvListOfEmails = System.getenv("CSVListOfEmails");
		List<String> listOfEmails = Arrays.asList(csvListOfEmails.split(","));
		for (String listItem : listOfEmails) {
			listItem = listItem.trim();
		}
		ArrayList<String> emailsToSendTo = new ArrayList<String>();
		emailsToSendTo.addAll(listOfEmails);
		if(totalIdErrors > 0) {
			emailContents = "<p>To whom it may concern, <br/><br/>We checked the batches for the last 24 hours on http://th51prd/thunderhead and scanned for any batch failures.  Unfortunately, we did find some errored batches and here is what we found: </p>" + emailContents + "</ul></p>";
			java.util.Date date = new Date();
			String emailSubject = "TH51PRD -- Batches Failed -- Last 24 Hours From " + new Timestamp(date.getTime());
			new EmailUtils().sendEmail(emailsToSendTo, emailSubject, emailContents, null);
			System.out.println(emailContents);
			Assert.fail(emailSubject);
		}
    }
}
