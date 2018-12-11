package scratchpad.jon;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityCoverages;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import scratchpad.jon.mine.PolicyXMLExporter;
import scratchpad.ryan.PolicyXMLInterpreter;

@SuppressWarnings("unused")
public class PolicyGL extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    boolean testFailed = false;
    String failureString = "";
    List<String> classList = new ArrayList<String>();

    @BeforeClass
    public void beforeClass() {

    }



    private boolean check(String[] a, String[] b) {
        if (a[0] == b[0]) {
            return true;
        } else {

        }

        return false;
    }


    //@SuppressWarnings({ "serial", "unused" })
    @Test//(enabled=false)
    public void generatePolicy() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);

        //PolicyXMLInterpreter.createPolicyFromXMLFile("C:/Users/rlonardo/Desktop/testXMLPolicyObject.txt");

//		login("su", "gw");
//		
//		ITopMenuAdministration topMenu = TopMenuFactory.getMenuAdministration();
//		topMenu.clickMessageQueues();
//		//
//		AdminEventMessages mQues = new AdminEventMessages(driver);
//		clickWhenClickable(find(By.xpath("//a[contains(text(), 'Email')]")));
////		mQues.selectSafeOrderObjectFilterOption(MessageQueuesSafeOrderObjectSelectOptions.AccountsWithanyunfinishedmessages);
//		
//		List<WebElement> foo = finds(By.xpath("//a[contains(@id, 'SOOName')]"));
//		//		for(WebElement account : foo) {
//			if(!account.getText().equals("Non-safe-ordered messages")) {
//				clickWhenClickable(account);
//				//				for(WebElement outbound : finds(By.xpath("//a[contains(text(), 'Outbound E-mail')]"))) {
//					clickWhenClickable(outbound);
//				}
//			}
//			
//		}
//		
//		
//		
//		mQues.clickEmailAccount(myPolicyObj.accountNumber);
//		int listSize = mQues.getDestinationList().size();
//		for (int i = 0; i < listSize; i++) {
//			List<WebElement> myList = mQues.getDestinationList();
//			//			myList.get(i).click();
//		}


//		List<GLUWIssues> uwIssuesToTest = GLUWIssuesHelper.getAllBlockQuoteReleaseUWIssues();
//		for(GLUWIssues issue : uwIssuesToTest) {
//			systemOut("case \"" + issue.getRuleMessage() + "\":");
//			systemOut("//" + issue.getRuleCondition());
//			systemOut("break;");
//		}


        String[] s1 = {"F", "o", "o"};
        String[] s2 = {"B", "o", "o", "a"};


        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>();
        PolicyLocation policyLocation = new PolicyLocation(new AddressInfo(true), false);
        locationsLists.add(policyLocation);
        ArrayList<PolicyLocationBuilding> plbs = new ArrayList<PolicyLocationBuilding>();
        plbs.add(new PolicyLocationBuilding());
        policyLocation.setBuildingList(plbs);

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), "10026"));

        CPPGeneralLiabilityCoverages coverages = new CPPGeneralLiabilityCoverages();
        ArrayList<CPPGLCoveragesAdditionalInsureds> glAdditionalInsureds = new ArrayList<>();
        CPPGLCoveragesAdditionalInsureds glAdditionalInsured = new CPPGLCoveragesAdditionalInsureds();
        ArrayList<PolicyLocation> locations = new ArrayList<>();
        locations.add(locationsLists.get(0));
        glAdditionalInsured.setLocationList(locations);
        glAdditionalInsureds.add(glAdditionalInsured);
        coverages.setAdditionalInsuredslist(glAdditionalInsureds);

//		CPPGeneralLiabilityCoverages coverages = new CPPGeneralLiabilityCoverages() {{
//			this.setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>(){{
//				this.add(new CPPGLCoveragesAdditionalInsureds() {{
//					this.setLocationList(new ArrayList<PolicyLocation>(){{
//						this.add(locationsLists.get(0));
//					}});
//				}});
//			}});
//		}};

        CPPGeneralLiability generalLiability = new CPPGeneralLiability();
        generalLiability.setCPPGeneralLiabilityExposures(exposures);
        generalLiability.setCPPGeneralLiabilityCoverages(coverages);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        // GENERATE POLICY

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("GL Policy")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObj.accountNumber);
        System.out.println(myPolicyObj.agentInfo.getAgentUserName());


        PolicyXMLExporter.createXML(myPolicyObj, myPolicyObj, 0);
        String fileNameToInterpret = PolicyXMLExporter.getXmlFileLocation();

        PolicyXMLInterpreter policyXMLInterpreter = new PolicyXMLInterpreter();
        policyXMLInterpreter.createPolicyFromXMLFile(fileNameToInterpret);
        policyXMLInterpreter.getPolicyFromXml().quickQuote(null);


    }


}
