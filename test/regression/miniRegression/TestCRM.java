package regression.miniRegression;

import com.idfbins.driver.BaseTest;
import services.helpers.com.idfbins.emailphoneupdate.EmailPhoneUpdateHelper;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo;

public class TestCRM extends BaseTest {


    //http://fbmis139.idfbins.com:8280/ab
    //"http://ab8uat.idfbins.com/ab"

    //	@Test
    public void testUpdateContact() throws Exception {
        CRMContactInfo contactInfo = new CRMContactInfo();
        contactInfo.setAddress("724 Fairmont St");
        contactInfo.setAddressBookUID("prd:96477");
        contactInfo.setBusinessPhone("866-468-4968");
        contactInfo.setCRMGUID("");
        contactInfo.setCity("Burley");
        contactInfo.setEmailAddress("ChrisHiltbrand@fantasticpeople.com");
        contactInfo.setHomePhone("866-468-4968");
        contactInfo.setMobilePhone("866-468-4968");
        contactInfo.setPostalCode("83318-2826");
        contactInfo.setStateCode("ID");
        contactInfo.setUserDisplayName("yourfriendlyneighborhoodcsremployee");
        contactInfo.setWorkPhone("866-468-4968");

        EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper("http://ab8uat.idfbins.com/ab");
        if (!updateContact.testUpdateContact(contactInfo)) {
            Assert.fail("The contact should be updated.");
        }
    }

    //	@Test
    public void crm1099Addresses() throws Exception {
        //Lakeland Chiropractic
        CRMContactInfo contactInfo = new CRMContactInfo();
        contactInfo.setAddress("16432 N Saddlewood Rd");
        contactInfo.setAddressBookUID("prd:159331");
        contactInfo.setBusinessPhone("866-468-4968");
        contactInfo.setCRMGUID("");
        contactInfo.setCity("Nine Mile Falls");
        contactInfo.setEmailAddress("Loreeeads@gmail.com");
        contactInfo.setHomePhone("801-250-1289");
        contactInfo.setMobilePhone("801-250-1289");
        contactInfo.setPostalCode("99026-9403");
        contactInfo.setStateCode("WA");
        contactInfo.setUserDisplayName("940101");
        contactInfo.setWorkPhone("801-250-1289");

        EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper("http://ab8uat.idfbins.com/ab");
        if (!updateContact.testUpdateContact(contactInfo)) {
            Assert.fail("The contact should be updated.");
        }
    }


    @Test
    public void crmAgentWorkAddress() throws Exception {
        CRMContactInfo contactInfo = new CRMContactInfo();
        contactInfo.setAddress("PO Box 1387");
        contactInfo.setAddressBookUID("prd:162778");
        contactInfo.setBusinessPhone("208-239-4369");
        contactInfo.setCRMGUID("");
        contactInfo.setCity("Bonners Ferry");
        contactInfo.setEmailAddress("waltdinning@gmail.com");
        contactInfo.setHomePhone("208-239-4369");
        contactInfo.setMobilePhone("208-239-4369");
        contactInfo.setPostalCode("83805-1387");
        contactInfo.setStateCode("ID");
        contactInfo.setUserDisplayName("Walt Dinning");
        contactInfo.setWorkPhone("208-239-4369");

        EmailPhoneUpdateHelper updateContact = new EmailPhoneUpdateHelper("http://ab8uat.idfbins.com/ab");
        if (!updateContact.testUpdateContact(contactInfo)) {
            Assert.fail("The contact should be updated.");
        }
    }

}
