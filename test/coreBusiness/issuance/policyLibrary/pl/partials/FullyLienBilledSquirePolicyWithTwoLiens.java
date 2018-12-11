package coreBusiness.issuance.policyLibrary.pl.partials;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GeneratePolicyHelper;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class FullyLienBilledSquirePolicyWithTwoLiens extends BaseTest {

    private GeneratePolicy generatePolicy;
    private WebDriver driver;

    @Test
    public void testGenerateFullyLienBilledSquirePolicyWithTwoLiens() throws Exception {

        ArrayList<GenerateContact> generateContacts= new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<ContactRole> lienOneRolesToAdd = new ArrayList<ContactRole>();
        lienOneRolesToAdd.add(ContactRole.Lienholder);

        GenerateContact lienOne = new GenerateContact.Builder(driver)
                .withCompanyName("LH From Hell")
                .withRoles(lienOneRolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(lienOne);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        generatePolicy = new GeneratePolicyHelper(driver).generateInsuredAndLienBilledSquirePolicy(null ,null,null,null,null,generateContacts);

    }
}
