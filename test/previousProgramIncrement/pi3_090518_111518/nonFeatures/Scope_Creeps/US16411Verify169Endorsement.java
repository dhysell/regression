package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Building;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US16411
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/252716941520">US16411</a>
 * @Description
 *
 * As a anyone, I want the 169 endorsement to attach to a policy when there is a Coverage E mobile home without foundation.
 * Requirements: PC8 - Squire - QuoteApplication - Property Detail
 * Steps to get there:
 * Have a squire policy
 * Add a coverage E dwelling (residence, dwelling, vacation home, etc)
 * Choose the construction type as Mobile Home and the foundation type as No Foundation
 * Quote
 * Acceptance criteria:
 * Ensure that the 169 endorsement appears in forms
 * Ensure that the 169 endorsement appears in documents
 * Ensure that the 169 endorsement is listed next to the dwelling on the declaration
 * @DATE September 17
 */
public class US16411Verify169Endorsement extends BaseTest {
    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    @Test(enabled = true)
    public void verifyFormsWhenMobileHomeNoFoundationSelectedThen169FormForCovE() throws Exception {
        generateWithFoundationType(Building.ConstructionTypePL.MobileHome, repository.gw.enums.Property.FoundationType.NoFoundation);
        List<String> formsDescriptions = getForms();
        Assert.assertTrue(formsDescriptions.contains("Mobile Home Endorsement"), "UW I169 -not Found");

    }

     @Test(enabled = true)
    public void verifyFormsWhenMobileHomeFoundationSelectedforNO169FormForCovE() throws Exception {
        generateWithFoundationType(Building.ConstructionTypePL.MobileHome, repository.gw.enums.Property.FoundationType.Foundation);
        List<String> formsDescriptions = getForms();
        Assert.assertFalse(formsDescriptions.contains("Mobile Home Endorsement"), "Mobile Home Endorsement - found");

    }

    private void generateWithFoundationType(Building.ConstructionTypePL modularManufactured, repository.gw.enums.Property.FoundationType foundation) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locOnePropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();

        repository.gw.generate.custom.PLPolicyLocationProperty property = new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE);
        property.setConstructionType(modularManufactured);
        property.setFoundationType(foundation);
        locOnePropertyList.add(property);

        repository.gw.generate.custom.PolicyLocation locToAdd = new repository.gw.generate.custom.PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);

        repository.gw.generate.custom.SquireLiability myLiab = new repository.gw.generate.custom.SquireLiability();


        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("Form", "169")
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    private  List<String> getForms() throws Exception {
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSideMenu.clickSideMenuForms();
        return new GenericWorkorderForms(driver).getFormDescriptionsFromTable();
    }
}
