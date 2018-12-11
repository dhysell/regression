package previousProgramIncrement.pi3_090518_111518.f350_InsuranceScore_Verisk_TransUnion;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;
import com.idfbins.helpers.EmailUtils;

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
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.globaldatarepo.entities.ISSupportUsers;
import persistence.globaldatarepo.helpers.ISSupportUsersHelper;
import services.verisk.iso.ISO;
import services.verisk.tu.TU;

/**
* @Author bhiltbrand
* @Requirement When an insurance score is retrieved, verify that values are mapped in the UI correctly.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558478112/detail/userstory/251396659940">Rally Story US16363</a>
* @Description This test will click the button to request an insurance score and then verify that the values map as they should in the UI.
* @DATE Oct 17, 2018
*/
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class US16363VeriskResponseUIMapping extends BaseTest {
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void generatePolicyAndVerifyUI() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		SoftAssert softAssert = new SoftAssert();
		
		Squire mySquire = new Squire(SquireEligibility.City);
		
		try {	
			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
	
			myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withInsFirstLastName("Verisk", "UI")
					.build(GeneratePolicyType.QuickQuote);
			
			String jobNumber = new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new GuidewireHelpers(driver).editPolicyTransaction();
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPLInsuranceScore();
			GenericWorkorderInsuranceScore pcInsuranceScorePage = new GenericWorkorderInsuranceScore(driver);
			pcInsuranceScorePage.fillOutCreditReport(myPolicyObjPL);
			
			pcInsuranceScorePage.clickInsuranceScoreVerisk();
			if (pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTableRowCount() == 0) {
				getQALogger().info("There were no rows in the Verisk Details table. Attempting to click off of the page and come back to see if the score result shows up.");
				sideMenu.clickSideMenuPolicyInfo();
				sideMenu.clickSideMenuPLInsuranceScore();
				if (pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTableRowCount() > 0) {
					getQALogger().info("There was at least 1 row in the Verisk Details table. Leaving the page and coming back worked. Moving on with test...");
				} else {
					Assert.fail("The Verisk Details table still had no rows after leaving the page and coming back. This most likely indicates that there was a problem getting a response from Verisk. Please investigate. Test cannot continue.");
				}
			} else {
				getQALogger().info("There were rows in the Verisk Details Table on the first attempt. This is the way it should be working. Sending a notification that the previous issue was fixed and continuing with the test...");
				new EmailUtils().sendEmail(new ArrayList<String>(Arrays.asList("bhiltbrand@idfbins.com")), "Verisk Details Table Fixed", "Looks like the Verisk Details Table was Fixed. Please check the test for US16363 and adapt.", null);
			}
			
			TableUtils tableUtils = new TableUtils(driver);
			String reportType = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Report Type");
			String referenceNumber = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Reference Number");
			String reportOrderDate = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Date Report Ordered");
			String nameFromReport = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Name From Report");
			String insuranceStatus = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Insurance Status");
			String flag = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Flag");
			
			softAssert.assertNotEquals(reportType, " ", "The value in the 'Report Type' cell in the Verisk Details Table was empty. This should not be the case.");
			softAssert.assertNotEquals(referenceNumber, " ", "The value in the 'Reference Number' cell in the Verisk Details Table was empty. This should not be the case.");
			softAssert.assertNotEquals(reportOrderDate, " ", "The value in the 'Date Report Ordered' cell in the Verisk Details Table was empty. This should not be the case.");
			softAssert.assertNotEquals(nameFromReport, " ", "The value in the 'Name From Report' cell in the Verisk Details Table was empty. This should not be the case.");
			softAssert.assertNotEquals(insuranceStatus, " ", "The value in the 'Insurance Status' cell in the Verisk Details Table was empty. This should not be the case.");
			softAssert.assertNotEquals(flag, " ", "The value in the 'Flag' cell in the Verisk Details Table was empty. This should not be the case.");
			new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
			new GuidewireHelpers(driver).logout();
			
			ISSupportUsers isSupportUser = ISSupportUsersHelper.getRandomUser();
			new Login(driver).loginAndSearchSubmission(isSupportUser.getUserName(), isSupportUser.getPassword(), myPolicyObjPL.accountNumber);
			sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPLInsuranceScore();
			
			tableUtils = new TableUtils(driver);
			String vendorReferenceNumber = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Vendor Reference Number");
			String insuranceScore = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Insurance Score");
			String enhancedInsuranceScore = tableUtils.getCellTextInTableByRowAndColumnName(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTable(), 1, "Enhanced Insurance Score");
			
			softAssert.assertNotEquals(vendorReferenceNumber, " ", "The value in the 'Vendor Reference Number' cell in the Verisk Details Table was empty. This should not be the case.");
			if (!insuranceStatus.equals("No Hit")) {
				softAssert.assertNotEquals(insuranceScore, " ", "The value in the 'Insurance Score' cell in the Verisk Details Table was empty. This should not be the case.");
				softAssert.assertNotEquals(enhancedInsuranceScore, " ", "The value in the 'Enhanced Insurance Score' cell in the Verisk Details Table was empty. This should not be the case.");
			}
			
			new TopMenuAdministrationPC(driver).clickReportPayloadRetrieval();
			List<ISO> listOfISORecords = new AdminReportPayloadRetrieval(driver).getListOfISOResults(ReportType.Insurance_Score, null, jobNumber, Direction.Response); //Should, in theory, only return 1 response record.
			List<String> listOfISOResponseStrings = new AdminReportPayloadRetrieval(driver).getListOfISOResultsStrings(ReportType.Insurance_Score, null, jobNumber, Direction.Response);
			
			softAssert.assertEquals(listOfISORecords.get(0).getPassportSvcRs().getStatus().getStatusCd(), "8", "The Response status code for ISO was not an 8 for complete as expected.");
			softAssert.assertEquals(listOfISORecords.get(0).getPassportSvcRs().getReports().getReport().get(0).getStatus().getStatusCd(), "9", "The Response status code for TU was not a 9 for complete as expected.");
			softAssert.assertEquals(listOfISORecords.get(0).getPassportSvcRs().getRequestId(), referenceNumber, "The Report Reference Number in the response did not match the UI as expected.");
			softAssert.assertEquals(listOfISORecords.get(0).getPassportSvcRs().getReports().getReport().get(0).getOrderTimeStamp().split(" ")[0], reportOrderDate, "The Report Order Date in the response did not match the UI as expected.");
			listOfISORecords.get(0).getPassportSvcRs().getReports().getReport().get(0).getCriticalResult();
		
			JAXBContext jaxbContext = JAXBContext.newInstance(TU.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			int beginningIndex = listOfISOResponseStrings.get(0).indexOf("<TU>");
			int endingIndex = listOfISOResponseStrings.get(0).indexOf("</TU>");
			String tuSection = listOfISOResponseStrings.get(0).substring(beginningIndex, (endingIndex + 5));
			StringReader reader = new StringReader(tuSection);
		    TU transUnionReport = (TU) unmarshaller.unmarshal(reader);
		    
		    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADNAME().get(0).getFIRSTNAME().toUpperCase().trim(), this.myPolicyObjPL.pniContact.getFirstName().toUpperCase().trim(), "The First Name in the Response didn't match.");
		    String[] lastNameArray = this.myPolicyObjPL.pniContact.getLastName().toUpperCase().split("-");
		    String lastNameToCheck = "";
		    for (int i = 0; i < lastNameArray.length - 1; i++) {
		    	if (i > 0) {
		    		lastNameToCheck = lastNameToCheck + "-";
		    	}
		    	lastNameToCheck = lastNameToCheck + lastNameArray[i];
		    }
		    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADNAME().get(0).getLASTNAME().toUpperCase(), lastNameToCheck, "The Last Name in the Response didn't match.");
		    softAssert.assertTrue(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getUPSTREETADDR().toUpperCase().replaceAll(" ", "").contains(this.myPolicyObjPL.pniContact.getAddress().getLine1().toUpperCase().replaceAll(" ", "")), "The Address Line 1 in the Response didn't match. ADDRESS FOUND: " + transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getUPSTREETADDR().toUpperCase().replaceAll(" ", "") + " ADDRESS EXPECTED: " + this.myPolicyObjPL.pniContact.getAddress().getLine1().toUpperCase().replaceAll(" ", ""));
		    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getCITYNAME().toUpperCase(), this.myPolicyObjPL.pniContact.getAddress().getCity().toUpperCase(), "The City in the Response didn't match. CITY FOUND: " + transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getCITYNAME().toUpperCase() + " CITY EXPECTED: " + this.myPolicyObjPL.pniContact.getAddress().getCity().toUpperCase());
		    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getSTATE(), this.myPolicyObjPL.pniContact.getAddress().getState().getAbbreviation(), "The State in the Response didn't match. STATE FOUND: " + transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getSTATE() + " STATE EXPECTED: " + this.myPolicyObjPL.pniContact.getAddress().getState().getAbbreviation());
		    softAssert.assertTrue(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getZIPCODE().contains(this.myPolicyObjPL.pniContact.getAddress().getZip()), "The Zip Code in the Response didn't match. ZIP FOUND: " + transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getZIPCODE() + " ZIP EXPECTED: " + this.myPolicyObjPL.pniContact.getAddress().getZip());
			
		    new GuidewireHelpers(driver).logout(); 
			softAssert.assertAll();
			
			
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
  }
}
