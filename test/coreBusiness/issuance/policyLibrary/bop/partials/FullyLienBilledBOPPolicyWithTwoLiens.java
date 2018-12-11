package coreBusiness.issuance.policyLibrary.bop.partials;

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

public class FullyLienBilledBOPPolicyWithTwoLiens extends BaseTest {

    GeneratePolicy generatePolicy;
    private WebDriver driver;


    @Test
    public void testGenerateFullyLienBilledWithTwoLiensPolicy() throws  Exception {

        ArrayList<GenerateContact> generateContacts= new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<ContactRole> lienOneRolesToAdd = new ArrayList<ContactRole>();
        lienOneRolesToAdd.add(ContactRole.Lienholder);

        GenerateContact lienOne = new GenerateContact.Builder(driver)
                .withCompanyName("LH1 FullLienBilled")
                .withRoles(lienOneRolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(lienOne);
        driver.quit();

        cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<ContactRole> lienTwoRolesToAdd = new ArrayList<ContactRole>();
        lienTwoRolesToAdd.add(ContactRole.Lienholder);

        GenerateContact lienTwo = new GenerateContact.Builder(driver)
                .withCompanyName("LH2 FullLienBilled")
                .withRoles(lienTwoRolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(lienTwo);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        generatePolicy = new GeneratePolicyHelper(driver).generateFullyLienBilledWithTwoLiensPolicy(null,null,null,null , generateContacts);

    }

}
