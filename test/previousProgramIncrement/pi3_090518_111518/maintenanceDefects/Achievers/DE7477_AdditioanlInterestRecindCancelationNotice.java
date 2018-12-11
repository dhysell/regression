package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Achievers;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;

public class DE7477_AdditioanlInterestRecindCancelationNotice extends BaseTest {

	
	@Test
    public void testValidateUnderwriterHasReviewedActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		AdditionalInterest lienHolder = new AdditionalInterest(ContactSubType.Company);
		lienHolder.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_All);
		lienHolder.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		Squire mySquire = new Squire();
		mySquire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().add(lienHolder);

		GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withDBA("FooBirt The Third")
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10))
				.isDraft()
				.build(GeneratePolicyType.PolicyIssued);
		
		
		//CANCEL SQUIRE
        new Login(driver).loginAndSearchPolicy_asUW(myPolObjPL);
        new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHAHAH WE GONA CANCEL YOU", null, false);
        new StartCancellation(driver).clickViewPolicyLink();
        
        
        //RESCIND CANCELATION
        new StartCancellation(driver).cancelRescind(true);
        new StartCancellation(driver).clickViewPolicyLink();
        
        //GET DOCUMENTS RELATED TO THE CANCELATION
        new SideMenuPC(driver).clickSideMenuToolsDocuments();
        new PolicyDocuments(driver).selectRelatedTo("Cancellation");
        new PolicyDocuments(driver).clickSearch();
        
        Assert.assertTrue(new PolicyDocuments(driver).getDocumentsDescriptionsFromTable().contains("Rescind Cancel - Additional Interest - UW"), "Rescind Cancel - Additional Interest - UW, DOCUMENT DIDN'T INFER AFTER CANCELLATION WAS RECINDED");
        
        //VERIFY USER IS ABLE TO PREVIEW DOCUMENT WITH NO FAILURES
        if(new PolicyDocuments(driver).clickPreviewButton("Rescind Cancel - Additional Interest - UW")) {
        	Assert.assertFalse(new GuidewireHelpers(driver).errorMessagesExist(), "RECIEVED FAILURE MESSAGE AFTER CLICKING PREVIEW ON Rescind Cancel - Additional Interest - UW");
        } else {
        	Assert.fail("WAS UNABLE TO CLICK PREVIEW BUTTON ON Rescind Cancel - Additional Interest - UW");
        }
        
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
