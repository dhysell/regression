package regression.r2.noclock.policycenter.submission_misc.subgroup3;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyReviewPL;

/**
 * @Author skandibanda
 * @Requirement : US8842: PL - Section level Additional insured
 * @Description : Checking for question Is this a Trust? under New Policy Member
 * screen and also checking for additional insured count on Section
 * II Coverages when additional insured members added with
 * selection of additional insured YES, Trust is YES/NO and one of
 * the options selected yes
 * @DATE Aug 23, 2016
 */
public class TestSectionLevelAdditionalInsured3 extends BaseTest {
    private GeneratePolicy myStandardIMPolicy;


    @Test()
    public void createStandardInlandMarine() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
        inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);

        ArrayList<Contact> policyMembers = new ArrayList<Contact>();
        Contact propertyAI = new Contact("Test" + NumberUtils.generateRandomNumberDigits(8), "ANI", Gender.Male, DateUtils.convertStringtoDate("01/01/1980", "MM/dd/yyyy"));
        ArrayList<LineSelection> aiLines = new ArrayList<LineSelection>();
        aiLines.add(LineSelection.StandardInlandMarine);
        propertyAI.setAdditionalInsured(aiLines);
        policyMembers.add(propertyAI);

        // PersonalProperty
        PersonalProperty pprop = new PersonalProperty();
        pprop.setType(PersonalPropertyType.SportingEquipment);
        pprop.setLimit(5000);
        pprop.setDeductible(PersonalPropertyDeductible.Ded1000);
        PersonalPropertyScheduledItem sportsScheduledItem = new PersonalPropertyScheduledItem();
        sportsScheduledItem.setParentPersonalPropertyType(PersonalPropertyType.SportingEquipment);
        sportsScheduledItem.setLimit(5000);
        sportsScheduledItem.setDescription("Sports Stuff");
        sportsScheduledItem.setType(PersonalPropertyScheduledItemType.Guns);
        sportsScheduledItem.setMake("Honda");
        sportsScheduledItem.setModel("Accord");
        sportsScheduledItem.setYear(2015);
        sportsScheduledItem.setVinSerialNum("abcd12345");
        ArrayList<PersonalPropertyScheduledItem> sportsScheduledItems = new ArrayList<PersonalPropertyScheduledItem>();
        sportsScheduledItems.add(sportsScheduledItem);
        pprop.setScheduledItems(sportsScheduledItems);

        PersonalPropertyList ppropList = new PersonalPropertyList();
        ppropList.setSportingEquipment(pprop);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();
        myStandardInlandMarine.policyMembers = policyMembers;

        this.myStandardIMPolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Im", "Dues")
                .withPaymentPlanType(PaymentPlanType.Quarterly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchJob(myStandardIMPolicy.agentInfo.getAgentUserName(), myStandardIMPolicy.agentInfo.getAgentPassword(), myStandardIMPolicy.accountNumber);
        SideMenuPC sidebar = new SideMenuPC(driver);
        sidebar.clickSideMenuPolicyReview();

        GenericWorkorderPolicyReviewPL policyReview = new GenericWorkorderPolicyReviewPL(driver);
        ArrayList<String> additionalInsureds = policyReview.getAdditionalInsureds();
        if (!additionalInsureds.get(0).equals(policyMembers.get(0).getFirstName() + " " + policyMembers.get(0).getLastName())) {
            Assert.fail(driver.getCurrentUrl() + myStandardIMPolicy.accountNumber + "The only Additional Insured created during generate should be on the App.");
        }
    }
}
