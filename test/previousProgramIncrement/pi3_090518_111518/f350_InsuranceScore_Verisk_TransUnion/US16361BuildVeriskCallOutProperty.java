package previousProgramIncrement.pi3_090518_111518.f350_InsuranceScore_Verisk_TransUnion;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.globaldatarepo.entities.ISSupportUsers;
import persistence.globaldatarepo.helpers.ISSupportUsersHelper;
import services.verisk.iso.ISO;
import services.verisk.tu.TU;

/**
* @Author bhiltbrand
* @Requirement When a call is made to the Verisk APIs, verify that the payload is sent and received correctly.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558478112/detail/userstory/251394997924">Rally Story US16361</a>
* @Description This test will make the various API calls in PC, and then go to the GW backend to retrieve the XML used in the request and the response.
* It will then parse that XML and verify that it matches the required values. 
* @DATE Oct 16, 2018
*/
@Test(groups = {"ClockMove"}) //This is not technically a clock move test, but it will mess with script parameters, so it needs to run in series.
public class US16361BuildVeriskCallOutProperty extends BaseTest {
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void generatePolicyAndVerifyXML() throws Exception {
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
					.withInsFirstLastName("Verisk", "XML")
					.build(GeneratePolicyType.QuickQuote);
			
			String jobNumber = new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new GuidewireHelpers(driver).editPolicyTransaction();
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPLInsuranceScore();
			GenericWorkorderInsuranceScore pcInsuranceScorePage = new GenericWorkorderInsuranceScore(driver);
			pcInsuranceScorePage.fillOutCreditReport(myPolicyObjPL);
			//get basic info regarding report and do verification on screen.
			new GuidewireHelpers(driver).logout();
			ISSupportUsers isSupportUser = ISSupportUsersHelper.getRandomUser();
			new Login(driver).login(isSupportUser.getUserName(), isSupportUser.getPassword());
			new TopMenuAdministrationPC(driver).clickReportPayloadRetrieval();
			List<ISO> listOfISORequestRecords = new AdminReportPayloadRetrieval(driver).getListOfISOResults(ReportType.Insurance_Score, null, jobNumber, Direction.Request); //Should, in theory, only return 1 request record.
			List<ISO> listOfISOResponseRecords = new AdminReportPayloadRetrieval(driver).getListOfISOResults(ReportType.Insurance_Score, null, jobNumber, Direction.Response); //Should, in theory, only return 1 response record.
			List<String> listOfISOResponseStrings = new AdminReportPayloadRetrieval(driver).getListOfISOResultsStrings(ReportType.Insurance_Score, null, jobNumber, Direction.Response);
			List<ISO> listOfISORecords = new ArrayList<ISO>();
			listOfISORecords.addAll(listOfISORequestRecords);
			listOfISORecords.addAll(listOfISOResponseRecords);
			//Should check for a response and a request record.
			for (ISO isoRecord : listOfISORecords) {
				if (isoRecord.getPassportSvcRq() != null) {
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getNameInfo().get(0).getPersonName().getGivenName().getValue().toUpperCase(), this.myPolicyObjPL.pniContact.getFirstName().toUpperCase(), "The First Name in the Request didn't match.");
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getNameInfo().get(0).getPersonName().getSurname().getValue().toUpperCase(), this.myPolicyObjPL.pniContact.getLastName().toUpperCase(), "The Last Name in the Request didn't match.");
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getNameInfo().get(0).getTaxIdentity().get(0).getTaxId().getValue(), ((this.myPolicyObjPL.pniContact.getSocialSecurityNumber() == null) ? "" : this.myPolicyObjPL.pniContact.getSocialSecurityNumber()), "The SSN in the Request didn't match.");
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getAddresses().getAddr().get(0).getUnparsedAddress().getAddr1().getValue().toUpperCase(), this.myPolicyObjPL.pniContact.getAddress().getLine1().toUpperCase(), "The Address Line 1 in the Request didn't match.");
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getAddresses().getAddr().get(0).getUnparsedAddress().getCity().getValue().toUpperCase(), this.myPolicyObjPL.pniContact.getAddress().getCity().toUpperCase(), "The City in the Request didn't match.");
					softAssert.assertEquals(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getAddresses().getAddr().get(0).getUnparsedAddress().getStateProvCd().getValue(), this.myPolicyObjPL.pniContact.getAddress().getState().getAbbreviation(), "The State in the Request didn't match.");
					softAssert.assertTrue(isoRecord.getPassportSvcRq().getPassportInqRq().getPropertyRequest().getFirstParty().getGeneralPartyInfo().getAddresses().getAddr().get(0).getUnparsedAddress().getPostalCode().getValue().contains(this.myPolicyObjPL.pniContact.getAddress().getZip()), "The Zip Code in the Request didn't match.");
				} else if (isoRecord.getPassportSvcRs() != null) {
					softAssert.assertEquals(isoRecord.getPassportSvcRs().getStatus().getStatusCd(), "8", "The Response status code for ISO was not an 8 for complete as expected.");
					softAssert.assertEquals(isoRecord.getPassportSvcRs().getReports().getReport().get(0).getStatus().getStatusCd(), "9", "The Response status code for TU was not a 9 for complete as expected.");
					isoRecord.getPassportSvcRs().getReports().getReport().get(0).getCriticalResult();
				
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
				    softAssert.assertTrue(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getUPSTREETADDR().toUpperCase().replaceAll(" ", "").contains(this.myPolicyObjPL.pniContact.getAddress().getLine1().toUpperCase().replaceAll(" ", "")), "The Address Line 1 in the Response didn't match.");
				    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getCITYNAME().toUpperCase(), this.myPolicyObjPL.pniContact.getAddress().getCity().toUpperCase(), "The City in the Response didn't match.");
				    softAssert.assertEquals(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getSTATE(), this.myPolicyObjPL.pniContact.getAddress().getState().getAbbreviation(), "The State in the Response didn't match.");
				    softAssert.assertTrue(transUnionReport.getREPORTS().get(0).getHEADADDR().get(0).getZIPCODE().contains(this.myPolicyObjPL.pniContact.getAddress().getZip()), "The Zip Code in the Response didn't match.");
				} else if (isoRecord.getSignonRq() != null) {
					//Not sure what to do here.
				}
			}
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
