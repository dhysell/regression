package scratchpad.James;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.CancellationObject;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;

public class US17095 extends BaseTest{
	 private WebDriver driver;
	 private GeneratePolicy myPolicyObjPL;

 @Test
 public void testPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_withDOB();
		    	
		try {

			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			Squire mySquire = new Squire(SquireEligibility.City);
			myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase)
					.build(GeneratePolicyType.FullApp);
			
			
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
	        new StartRewrite(driver).startRewriteNewTerm(fixedDate);
	        new StartRewrite(driver).rewriteNewTermGuts(myPolicyObjPL);
	        System.out.println("rewrite done! YAY!");
	        new GuidewireHelpers(driver).logout();
			        
			
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			//switch Verisk Score enabled back to false
			try {
				new GuidewireHelpers(driver).logout();
			} catch(Exception e) {
				//Already logged out.
			}
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}
		

		driver.quit();  
 	}
 }
