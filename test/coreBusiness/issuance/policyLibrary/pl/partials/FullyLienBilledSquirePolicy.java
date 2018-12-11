package coreBusiness.issuance.policyLibrary.pl.partials;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;

public class FullyLienBilledSquirePolicy extends BaseTest {

    GeneratePolicy generatePolicy;
    private WebDriver driver;

    @Test
    public void testGenerateFullyLienBilledBOPPolicy() throws  Exception {

        ArrayList<GenerateContact> generateContacts= new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact generateContact = new GenerateContact.Builder(driver)
                .withCompanyName("LH FullLienBilled")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(generateContact);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        generatePolicy = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy(null,null,null,null ,null, generateContacts);


    }
}
