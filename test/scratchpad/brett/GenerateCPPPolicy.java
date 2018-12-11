package scratchpad.brett;

import java.io.File;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
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
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import com.idfbins.helpers.EmailUtils;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;

/**
 * @Author jlarsen
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description mini regression for CPP PL and BOP. Test will generate a policy for each product.
 * @DATE Dec 8, 2015
 */
public class GenerateCPPPolicy extends BaseTest {

    public boolean debugMode = true;
    public GeneratePolicy myPolicyObjBOP = null;
    public GeneratePolicy myPolicyObjCPP = null;
    public GeneratePolicy myPolicyObjPL = null;
    public ArrayList<String> failureTechnicalEmailToList = new ArrayList<String>();
    public String failureTechnicalEmailSubject = null;
    public String failureTechnicalEmailBody = null;
    public ArrayList<String> failureGeneralEmailToList = new ArrayList<String>();
    public String failureGeneralEmailSubject = null;
    public String failureGeneralEmailBody = null;
    public ArrayList<String> successEmailToList = new ArrayList<String>();
    public String successEmailSubject = null;
    public String successEmailBody = null;
    public ARUsers arUser;


    @BeforeClass
    public void beforeClass() {
        String testTimeStamp = new Timestamp(new Date().getTime()).toString();
        String beginBody = "All,<br/><br/><p>Prior to doing a deploy to the QA Servers, a test was run to ensure that we are able to quote, bind, and issue a policy in the DEV Servers.  This also verifies that the account and down payment are in fact sent over from PolicyCenter and received by BillingCenter.  If you are receiving this email, then ";

        if (debugMode) {
            this.failureTechnicalEmailToList.add("jlarsen@idfbins.com");
        } else {
            this.failureTechnicalEmailToList.add("jnthompson@idfbins.com");
            this.failureTechnicalEmailToList.add("rmoreira@idfbins.com");
            this.failureTechnicalEmailToList.add("cjoslin@idfbins.com");
            this.failureTechnicalEmailToList.add("ryoung@idfbins.com");
            this.failureTechnicalEmailToList.add("chofman@idfbins.com");
            this.failureTechnicalEmailToList.add("bhiltbrand@idfbins.com");
            this.failureTechnicalEmailToList.add("bmartin@idfbins.com");
            this.failureTechnicalEmailToList.add("drichards@idfbins.com");
            this.failureTechnicalEmailToList.add("dhysell@idfbins.com");
            this.failureTechnicalEmailToList.add("jqu@idfbins.com");
            this.failureTechnicalEmailToList.add("jlarsen@idfbins.com");
            this.failureTechnicalEmailToList.add("sbroderick@idfbins.com");
            this.failureTechnicalEmailToList.add("iclouser@idfbins.com");
        }
        this.failureTechnicalEmailSubject = "***Failed*** Deploy Check for QA Servers (Technical Details) -- " + testTimeStamp;
        this.failureTechnicalEmailBody = beginBody + "the following error has occurred:</p>";

        if (debugMode) {
            this.failureGeneralEmailToList.add("jlarsen@idfbins.com");
        } else {
            this.failureGeneralEmailToList.add("panderson@idfbins.com");
            this.failureGeneralEmailToList.add("hhill@idfbins.com");
            this.failureGeneralEmailToList.add("rmisner@idfbins.com");
            this.failureGeneralEmailToList.add("sbrunson@idfbins.com");
            this.failureGeneralEmailToList.add("bcarling@idfbins.com");
            this.failureGeneralEmailToList.add("skane@idfbins.com");
            this.failureGeneralEmailToList.add("eroseborough@idfbins.com");
            this.failureGeneralEmailToList.add("iwong@idfbins.com");
            this.failureGeneralEmailToList.add("jeckersley@idfbins.com");
            this.failureGeneralEmailToList.add("jsouth-tellez@idfbins.com");
        }
        this.failureGeneralEmailSubject = "***Failed*** Deploy Check for QA Servers -- " + testTimeStamp;
        this.failureGeneralEmailBody = beginBody + "an error occurred during this validation and it is currently being investigated. We will inform you as soon as we have this taken care of and another deployment has been scheduled.</p>";

        this.successEmailToList.addAll(failureTechnicalEmailToList);
        this.successEmailToList.addAll(failureGeneralEmailToList);
        if (!debugMode) {
            this.successEmailToList.add("awaldron@idfbins.com");
        }
        this.successEmailSubject = "***Successful*** Deploy Check for QA Servers -- " + testTimeStamp;
        this.successEmailBody = beginBody + "everything passed and they are currently being updated with the latest code from DEV. You will get a subsequent email if something goes wrong in pulling up the servers.</p>";
    }

    private WebDriver driver;


    @Test(description = "create a BOP policy through Policy Bound", enabled = false)
    public void createPolicyBoundBOP() throws Exception {

        try {
            PolicyBusinessownersLine basicBOLine = new PolicyBusinessownersLine(SmallBusinessType.StoresRetail);
            ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
            locOneBuildingList.add(new PolicyLocationBuilding());
            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

            Agents agentInfo = new Agents();
            agentInfo.setAgentUserName("darmstrong");

            GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                    .withCreateNew(CreateNew.Create_New_Only_If_Does_Not_Exist)
                    .withAgent(agentInfo)
                    .withInsPersonOrCompany(ContactSubType.Company)
                    .withInsCompanyName("Mini Regression BOP")
                    .withPolOrgType(OrganizationType.Partnership)
                    .withBusinessownersLine(basicBOLine)
                    .withPolicyLocations(locationsList)
                    .withPaymentPlanType(PaymentPlanType.getRandom())
                    .withDownPaymentType(PaymentType.Check)
                    .build(GeneratePolicyType.PolicySubmitted);

            this.myPolicyObjBOP = myPolicyObj;
        } catch (Exception e) {
            captureFailureScreenshotPrepLogFileAndSendEmails("createPolicyBoundError", e);
            throw e;
        }
    }

    @Test(dependsOnMethods = {"createPolicyBoundBOP"}, enabled = false)
    public void updatePolicyIssued() throws Exception {

        try {
            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);

//			GeneratePolicy myPolicyObj2 = new GeneratePolicy(GeneratePolicyType.PolicyIssued, this.myPolicyObjBOP);

            this.myPolicyObjBOP.convertTo(driver, GeneratePolicyType.PolicyIssued);
        } catch (Exception e) {
            captureFailureScreenshotPrepLogFileAndSendEmails("updatePolicyIssuedError", e);
            throw e;
        }
    }


    @SuppressWarnings("serial")
    @Test()
    public void createPolicyBoundCPP() throws Exception {
        try {
            ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
            locOneBuildingList.add(new PolicyLocationBuilding());
            final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

            CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
//				this.setCommercialAutoLine(new CPPCommercialAutoLine(LiabilityLimit.OneMillion1M, DeductibleLiabilityCoverage.TwoThousandFiveHundred2500));
                this.setCommercialAutoLine(new CPPCommercialAutoLine());
                this.setVehicleList(new ArrayList<Vehicle>() {{
                    this.add(new Vehicle() {{
                        this.setGaragedAt(locationsList.get(0).getAddress());
                    }});
                }});
                this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
                this.setDriversList(new ArrayList<Contact>() {{
                    this.add(new Contact() {{
                        this.setGender(Gender.Male);
                    }});
                }});
            }};


            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
            driver = buildDriver(cf);
            // GENERATE POLICY
            myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.CPP)
                    .withCPPCommercialAuto(commercialAuto)
                    .withLineSelection(LineSelection.CommercialAutoLineCPP)
                    .withPolicyLocations(locationsList)
                    .withCreateNew(CreateNew.Create_New_Always)
                    .withInsPersonOrCompany(ContactSubType.Company)
                    .withInsCompanyName("Mini Regression CPP")
                    .withPolOrgType(OrganizationType.LLC)
                    .withInsPrimaryAddress(locationsList.get(0).getAddress())
                    .withPaymentPlanType(PaymentPlanType.getRandom())
                    .withDownPaymentType(PaymentType.Check)
                    .build(GeneratePolicyType.PolicyIssued);

        } catch (Exception e) {
            captureFailureScreenshotPrepLogFileAndSendEmails("createPolicyBoundCPPError", e);
            throw e;
        }
    }


    @Test(enabled = false)
    public void createPolicyBoundPL() throws Exception {
        try {

        } catch (Exception e) {
            captureFailureScreenshotPrepLogFileAndSendEmails("createPolicyBoundPLError", e);
            throw e;
        }
    }


    @Test(dependsOnMethods = {"updatePolicyIssued", "createPolicyBoundCPP", "createPolicyBoundPL"}, enabled = false)
    public void successfulTest() {

        new EmailUtils().sendEmail(this.successEmailToList, this.successEmailSubject, this.successEmailBody, null);

    }


    private void captureFailureScreenshotPrepLogFileAndSendEmails(String filename, Exception e) throws Exception {
        String dateStamp = DateUtils.dateFormatAsString("yyyyMMddHHmmssSSS", new Date());
//        String screenshotPath = captureScreen(filename + "-" + dateStamp);
        ArrayList<String> attachmentsList = new ArrayList<String>();
//        attachmentsList.add(screenshotPath);
        String filePath = "C:\\tmp\\exceptions\\";
        File miniregFolder = new File(filePath);
        if (!miniregFolder.exists()) {
            miniregFolder.mkdir();
        }
        filePath = filePath + filename + "-" + dateStamp + ".log";
        PrintStream ps = new PrintStream(new File(filePath));
        e.printStackTrace(ps);
        ps.close();
        this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<b>";
        if (myPolicyObjBOP != null) {
            this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<ul>";
            if (myPolicyObjBOP.accountNumber != null) {
                this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<li>Account Number: " + myPolicyObjBOP.accountNumber + "</li>";
            }

            if (!((myPolicyObjBOP.pniContact.getCompanyName() == null) && (myPolicyObjBOP.pniContact.getFirstName() == null) && (myPolicyObjBOP.pniContact.getLastName() == null))) {
                if (myPolicyObjBOP.pniContact.getCompanyName() == null) {
                    this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<li>First/Last Name: " + myPolicyObjBOP.pniContact.getFirstName() + " " + myPolicyObjBOP.pniContact.getLastName() + "</li>";
                } else {
                    this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<li>Company Name: " + myPolicyObjBOP.pniContact.getCompanyName() + "</li>";
                }
            }
            this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "</ul>";
        }
        this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<p>" + e.getMessage() + "</p>";
        this.failureTechnicalEmailBody = this.failureTechnicalEmailBody + "<b>";
        attachmentsList.add(filePath);
        EmailUtils emailUtils = new EmailUtils();
        emailUtils.sendEmail(this.failureTechnicalEmailToList, this.failureTechnicalEmailSubject, this.failureTechnicalEmailBody, attachmentsList);
        emailUtils.sendEmail(this.failureGeneralEmailToList, this.failureGeneralEmailSubject, this.failureGeneralEmailBody, null);
    }


}










