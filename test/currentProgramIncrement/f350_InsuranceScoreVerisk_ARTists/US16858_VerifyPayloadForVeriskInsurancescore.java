package currentProgramIncrement.f350_InsuranceScoreVerisk_ARTists;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminReportPayloadRetrieval;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReportPayloadRetrieval.Direction;
import repository.gw.enums.ReportPayloadRetrieval.ReportType;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.InsuranceScoreTestCases;
import persistence.globaldatarepo.helpers.InsuranceScoreTestCasesHelper;
@Test(groups= {"ClockMove"})
public class US16858_VerifyPayloadForVeriskInsurancescore extends BaseTest {

	
	
	
	@Test(enabled = true)
	public void verifyPayloadForVeriskInsurancescore() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		InsuranceScoreTestCases testCase = InsuranceScoreTestCasesHelper.getRandomContact_withDOB();
		
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.insuranceScoreReport.setLivedAtAddressLessThan6Months(true);
		mySquire.insuranceScoreReport.setNameChangelast6Months(true);

		try {
			
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
			
			GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withVeriskContact(testCase)
					.withAdvancedSearch()
					.isDraft()
					.build(GeneratePolicyType.FullApp);
			
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickReportPayloadRetrieval();
			List<String> isoList = new AdminReportPayloadRetrieval(driver).getListOfISOResultsStrings(ReportType.Insurance_Score, new Date(), null, Direction.Request);
			boolean found = false;
			for(String report : isoList) {
				if(report.contains("<Surname>" + myPolicyObj.pniContact.getLastName())) {
					if(report.contains("<Surname>" + myPolicyObj.squire.insuranceScoreReport.getFormerLastName())) {
						if(report.contains("<GivenName>" + myPolicyObj.pniContact.getFirstName())) {
							if(report.contains("<GivenName>" + myPolicyObj.squire.insuranceScoreReport.getFormerFirstName())) {
								if(report.contains("<Addr1>" + StringsUtils.capitalizeAllWords(myPolicyObj.pniContact.getAddress().getLine1()))) {
									if(report.contains("<Addr1>" + StringsUtils.capitalizeAllWords(myPolicyObj.squire.insuranceScoreReport.getFormerAddress().getLine1()))) {
										if(report.contains("<City>" + StringsUtils.capitalizeAllWords(myPolicyObj.pniContact.getAddress().getCity()))) {
											if(report.contains("<City>" + StringsUtils.capitalizeAllWords(myPolicyObj.squire.insuranceScoreReport.getFormerAddress().getCity()))) {
												if(report.contains("<StateProvCd>" + myPolicyObj.pniContact.getAddress().getState().getAbbreviation().toUpperCase())) {
													if(report.contains("<StateProvCd>" + myPolicyObj.squire.insuranceScoreReport.getFormerAddress().getState().getAbbreviation().toUpperCase())) {
														if(report.contains("<PostalCode>" + myPolicyObj.pniContact.getAddress().getZip())) {
															if(report.contains("<PostalCode>" + myPolicyObj.squire.insuranceScoreReport.getFormerAddress().getZip())) {
																found = true;
																break;
															}	
														}	
													}	
												}	
											}	
										}	
									}	
								}
							}
						}
					}
				}
			}
			
			if(!found) {
				String payLoadString = "";
				for(String payload : isoList) {
					payLoadString += payload;
				}
				Assert.fail("DID NOT FIND A REQUEST PAYLOAD THAT CONTAINED BOTH CURRENT AND FORMER NAME OR CURRENT AND FORMER ADDRESS.\n" + payLoadString);
			}
			
		
		
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
//			//End Script Parameter Setup
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
}
