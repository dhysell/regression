package previousProgramIncrement.pi2_062818_090518.nonFeatures.Nucleus;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.generate.custom.AdditionalInterest;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import persistence.globaldatarepo.helpers.AbUserHelper;

import static org.testng.Assert.assertEquals;

public class US15412AddressDescriptionDisplayedOnAddressTab extends BaseTest {
    

    /**
     * @throws Exception
     * @Author sbroderick
     * @Requirement The address description will appear in the address table.
     * @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:b:/s/TeamNucleus/EWljmoR51m5JimqclDkp9yQBPvEIPWnE_wuZipNottHRJw?e=a1Ah8Y">Diplay address description on address tab for CM users.</a>
     * Page 3
     * @DATE Jul 23, 2018
     */

    @Test
    public void testExistingContactsAddressDescription() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        AdditionalInterest additionalInterest = new AdditionalInterest(ContactSubType.Company);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
        searchMe.searchLienholderNumber(additionalInterest.getLienholderNumber());
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        addressPage.clickEdit();
        addressPage.setContactDetailsAddressesDescription("Use this address for all AUTO FINANCE LLC");
        addressPage.clickUpdate();
        String addressDescription = addressPage.getAddressDescriptionByAddress(additionalInterest.getAddress());
        assertEquals(addressDescription, "Use this address for all AUTO FINANCE LLC", "According to the requirements, the address description should be displayed in the Address table. Please ensure that the address description is displayed in the address table.");
    }
}
