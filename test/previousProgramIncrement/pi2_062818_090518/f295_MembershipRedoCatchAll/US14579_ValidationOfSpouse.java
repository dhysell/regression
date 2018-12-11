package previousProgramIncrement.pi2_062818_090518.f295_MembershipRedoCatchAll;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRelationshipToMember;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import scratchpad.evan.SideMenuPC;

import java.util.List;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE VALIDATION TRIGGERIS IF USER TRIES TO ADD MULTIPLE SPICES
* @DATE Sep 20, 2018
*/
public class US14579_ValidationOfSpouse extends BaseTest {

    @Test
    public void validateMultipleSpouses() throws Exception {
        SoftAssert softAssert = new SoftAssert();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh, repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        GeneratePolicy myPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .isDraft()
                .build(GeneratePolicyType.QuickQuote);

        new Login(driver).loginAndSearchSubmission(myPolObj);
        new SideMenuPC(driver).clickSideMenuMembershipMembers();
        new GenericWorkorderMembershipMembers(driver).clickEditMembershipMember(myPolObj.pniContact);
        new GenericWorkorderMembershipMembers(driver).clickMemberRelatedContactsTab();
        Contact wife1 = new Contact();
        wife1.setContactRelationshipToPNI(ContactRelationshipToMember.Spouse);
        Contact wife2 = new Contact();
        wife2.setContactRelationshipToPNI(ContactRelationshipToMember.Spouse);
        new GenericWorkorderMembershipMembers(driver).addMembershipDuesRelatedContacts(true, wife1);
//        boolean exceptionCaught = false;
//        try {
            new GenericWorkorderMembershipMembers(driver).addMembershipDuesRelatedContacts(true, wife2);
            new GenericWorkorderMembershipMembers(driver).clickOK();
//        } catch (NoSuchElementException e) {
//            exceptionCaught = true;
            List<WebElement> validationResultsList = new GenericWorkorderMembershipMembers(driver).getValidationResultsList();
            boolean found = false;
            for(WebElement message : validationResultsList) {
                if(message.getText().contains("Only one spouse per household member is allowed")) {
                    found = true;
                    break;
                }
            }
            softAssert.assertTrue(found, "VALIDATION RESULTS MESSAGE DID NOT DISPLAY WHEN SPOUSE WAS SELECTED ON THE SECOND RELATED CONTACT");
//        }

//        Assert.assertTrue(exceptionCaught, "SETTING THE SECOND RELATED CONTACT TO SPOUSE DID NOT TRIGGER VALIDATION OR DISABLE CONTACT SEARCH BUTTON");
//        new GenericWorkorderMembershipMembers(driver).clickClear();
//        new GenericWorkorderMembershipMembers(driver).clickOK();
//        String validationMessage = new GuidewireHelpers(driver).getFirstErrorMessage();
//        softAssert.assertTrue(validationMessage.equals("Relationship : Only one spouse per household member is allowed"), "VALIDATION MESSAGE AFTER CLICKING 'OK' WITH 2 SPICES SELECTED IS INCORRECT| " +
//                "\nFOUND: " + validationMessage +
//                "\nEXPECTED: Relationship : Only one spouse per household member is allowed");


        softAssert.assertAll();
    }//end validateMultipleSpices()
}
