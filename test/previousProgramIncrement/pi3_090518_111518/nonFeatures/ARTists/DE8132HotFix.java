package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.Date;

//import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CancellationObject;
import repository.gw.generate.custom.Renewal;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;

public class DE8132HotFix extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	
	/**
	* @Author jkim
	* @Requirement 
	* @RequirementsLink <a href="http:// ">Link Text</a>
	* @Description 
	* @DATE Dec 7, 2018
	* @throws Exception
	*/
	@Test
	public void testPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// Create Squire policy with issued option
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability.liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
		myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("James", "Kim") // If I remove this code then, name will be randomly input.
				.withProductType(ProductLineType.Squire) //specify product type
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withSquire(mySquire)
				// .isDraft() // full app without quoted
				.build(GeneratePolicyType.PolicyIssued); // generate policy has 3 different type: FullApp, PolicyIssued, PolicySubmitted, QuickQuote
		
		myPolicyObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
		
		// Cancellation for Squire Policy
        myPolicyObjPL.productType = ProductLineType.Squire;
        new Login(driver).loginAndSearchPolicy_asUW(myPolicyObjPL);
               
        // to get cancel date.
        CancellationObject cancelDate = new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "James is testing",  DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
        new StartCancellation(driver).clickViewPolicyLink(); 
        
        // Change cancel date to fixed date so that new issue date works.
        Date fixedDate = DateUtils.dateAddSubtract(cancelDate.getCancellationDate(), DateAddSubtractOptions.Day, 40);
        System.out.println("fixed date is: " + fixedDate.toString());
        
        // Rewrite for the squire policy
        new StartRewrite(driver).startRewriteNewTerm(fixedDate);
        new StartRewrite(driver).rewriteNewTermGuts(myPolicyObjPL);
		System.out.println("RewriteNewTermGut finished!!");
        
        new GuidewireHelpers(driver).logout();
		System.out.println("Safely Logged out!");
				
		// Start canceling Umbrella policy
		myPolicyObjPL.productType = ProductLineType.PersonalUmbrella;
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObjPL);
		System.out.println("Login successfully for canceling Umbrella policy");
		System.out.println("Umbrella policy already canceled with squire. I don't need to cancel the umbrella policy again.");
		System.out.println("So I am going to log out!");

		// rewrite new term for umbrella
		
		new Renewal(); // from what to what?
		
        new StartRewrite(driver).startRewriteNewTerm(fixedDate);
        new StartRewrite(driver).rewriteNewTermGuts(myPolicyObjPL);
        System.out.println("rewrite done! YAY!");
        new GuidewireHelpers(driver).logout();
		        
		driver.quit();
	}

}
