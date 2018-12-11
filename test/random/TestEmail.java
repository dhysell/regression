package random;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.idfbins.helpers.EmailUtils;
import com.idfbins.driver.BaseTest;
public class TestEmail extends BaseTest {

	@Test
    public void testEmailSend() {
		ArrayList<String> failureGeneralEmailToList = new ArrayList<String>();
		failureGeneralEmailToList.add("chofman@idfbins.com");
		failureGeneralEmailToList.add("ryoung@idfbins.com");
		failureGeneralEmailToList.add("bhiltbrand@idfbins.com");
		
		String failureGeneralEmailSubject = "Email Test from Server";
		
		String failureGeneralEmailBody = "Test Email Body";
		
		new EmailUtils().sendEmail(failureGeneralEmailToList, failureGeneralEmailSubject, failureGeneralEmailBody, null);
	}

}
