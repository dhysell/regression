package random;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.bc.policy.summary.BCPolicySummary;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GLClassCode;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.CPPGLCoveragesAdditionalInsureds;
import repository.gw.generate.custom.CPPGeneralLiability;
import repository.gw.generate.custom.CPPGeneralLiabilityCoverages;
import repository.gw.generate.custom.CPPGeneralLiabilityExposures;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class MiniRegression extends BaseTest {
	private WebDriver driver;
    private boolean debugMode = false;
    private String debugEmail = "chofman@idfbins.com";
    private String environmentToCheck = "DEV2";
    private String environmentsToDeployTo = "QA2 and IT2";

    private String testTimeStamp = null;
    private ArrayList<String> failureTechnicalEmailToList = new ArrayList<String>();
    private String failureTechnicalEmailSubject = null;
    private String failureTechnicalEmailBody = null;
    private ArrayList<String> failureGeneralEmailToList = new ArrayList<String>();
    private String failureGeneralEmailSubject = null;
    private String failureGeneralEmailBody = null;
    private ArrayList<String> successEmailToList = new ArrayList<String>();
    private String successEmailSubject = null;
    private String successEmailBody = null;

    private GeneratePolicy myPolicyObjBOP = null;
    private GeneratePolicy myPolicyObjCPPAuto = null;
    private GeneratePolicy myPolicyObjectCPPGL = null;
    private GeneratePolicy myPolicyObjSquireAuto = null;

    private enum PolicyType {
        BOP,
        SquireAuto,
        CPPAuto,
        CPPGeneralLiability
    }

    public MiniRegression(boolean debugMode, String debugEmail, String environmentToCheck) {
        setDebugMode(debugMode);
        setDebugEmail(debugEmail);
        setEnvironmentToCheck(environmentToCheck);
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void setDebugEmail(String debugEmail) {
        this.debugEmail = debugEmail;
    }

    public void setEnvironmentToCheck(String environmentToCheck) {
        this.environmentToCheck = environmentToCheck;
    }

    public String getEnvironmentsToDeployTo() {
        return environmentsToDeployTo;
    }

    public String getTestTimeStamp() {
        return testTimeStamp;
    }

    public ArrayList<String> getFailureTechnicalEmailToList() {
        return failureTechnicalEmailToList;
    }

    public String getFailureTechnicalEmailSubject() {
        return failureTechnicalEmailSubject;
    }

    public void setFailureTechnicalEmailSubject(String failureTechnicalEmailSubject) {
        this.failureTechnicalEmailSubject = failureTechnicalEmailSubject;
    }

    public String getFailureTechnicalEmailBody() {
        return failureTechnicalEmailBody;
    }

    public void setFailureTechnicalEmailBody(String failureTechnicalEmailBody) {
        this.failureTechnicalEmailBody = failureTechnicalEmailBody;
    }

    public ArrayList<String> getFailureGeneralEmailToList() {
        return failureGeneralEmailToList;
    }

    public String getFailureGeneralEmailSubject() {
        return failureGeneralEmailSubject;
    }

    public void setFailureGeneralEmailSubject(String failureGeneralEmailSubject) {
        this.failureGeneralEmailSubject = failureGeneralEmailSubject;
    }

    public String getFailureGeneralEmailBody() {
        return failureGeneralEmailBody;
    }

    public ArrayList<String> getSuccessEmailToList() {
        return successEmailToList;
    }

    public String getSuccessEmailSubject() {
        return successEmailSubject;
    }

    public String getSuccessEmailBody() {
        return successEmailBody;
    }

    public void setMyPolicyObjBOP(GeneratePolicy myPolicyObjBOP) {
        this.myPolicyObjBOP = myPolicyObjBOP;
    }

    public void setMyPolicyObjCPPAuto(GeneratePolicy myPolicyObjCPPAuto) {
        this.myPolicyObjCPPAuto = myPolicyObjCPPAuto;
    }

    public GeneratePolicy getMyPolicyObj(PolicyType polType) {
        GeneratePolicy toReturn = null;
        switch (polType) {
            case BOP:
                toReturn = myPolicyObjBOP;
                break;
            case SquireAuto:
                toReturn = myPolicyObjSquireAuto;
                break;
            case CPPAuto:
                toReturn = myPolicyObjCPPAuto;
                break;
            case CPPGeneralLiability:
                toReturn = myPolicyObjectCPPGL;
                break;
        }
        return toReturn;
    }

    public void setMyPolicyObjSquireAuto(GeneratePolicy myPolicyObjSquireAuto) {
        this.myPolicyObjSquireAuto = myPolicyObjSquireAuto;
    }

    // Create and Issue BOP Policy
    public void testBOP_CreatePolicyIssued_Guts() throws Exception {
        GeneratePolicy polObjBOP = null;
        try {
        	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    		driver = buildDriver(cf);

            polObjBOP = new GeneratePolicy.Builder(driver)
                    .withInsCompanyName("Mini R2BOP")
                    .withBusinessownersLine()
                    .withDownPaymentType(PaymentType.Cash)
                    .build(GeneratePolicyType.PolicyIssued);

            setMyPolicyObjBOP(polObjBOP);

        } catch (Exception e) {
            setFailureTechnicalEmailSubject("***Failed*** " + PolicyType.BOP.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers (Technical Details) -- " + getTestTimeStamp());
            setFailureGeneralEmailSubject("***Failed*** " + PolicyType.BOP.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers -- " + getTestTimeStamp());
            captureFailureScreenshotPrepLogFileAndSendEmails(polObjBOP, "test" + PolicyType.BOP.name() + "_CreatePolicyIssued-Error", e);
            throw e;
        }

    }

    // Verify that BOP policy went to BC and correct charges came across
    public void testBOP_VerifyPolicyinBC_Wrapper() throws Exception {
        test_VerifyPolicyinBC_Guts("test" + PolicyType.BOP.name() + "_VerifyPolicyinBC-Error", PolicyType.BOP);
    }

    // Create and Issue CPP Policy
    public void testCPPAuto_CreatePolicyIssued_Guts() throws Exception {
        GeneratePolicy polObjCPPAuto = null;
        try {
            ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
            locOneBuildingList.add(new PolicyLocationBuilding());

            ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
            locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

            ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
            Vehicle newVeh = new Vehicle();
            newVeh.setGaragedAt(locationsList.get(0).getAddress());
            vehicleList.add(newVeh);

            ArrayList<Contact> driverList = new ArrayList<Contact>();
            Contact newPer = new Contact();
            newPer.setGender(Gender.Male);
            driverList.add(newPer);

            CPPCommercialAuto commercialAuto = new CPPCommercialAuto();
            commercialAuto.setCommercialAutoLine(new CPPCommercialAutoLine());
            commercialAuto.setVehicleList(vehicleList);
            commercialAuto.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            commercialAuto.setDriversList(driverList);

            Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    		driver = buildDriver(cf);

            polObjCPPAuto = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.CPP)
                    .withCPPCommercialAuto(commercialAuto)
                    .withLineSelection(LineSelection.CommercialAutoLineCPP)
                    .withPolicyLocations(locationsList)
                    .withInsCompanyName("Mini R2CPPAuto")
                    .withPolOrgType(OrganizationType.LLC)
                    .withInsPrimaryAddress(locationsList.get(0).getAddress())
                    .withDownPaymentType(PaymentType.Cash)
                    .build(GeneratePolicyType.PolicyIssued);

            setMyPolicyObjCPPAuto(polObjCPPAuto);

        } catch (Exception e) {
            setFailureTechnicalEmailSubject("***Failed*** " + PolicyType.CPPAuto.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers (Technical Details) -- " + getTestTimeStamp());
            setFailureGeneralEmailSubject("***Failed*** " + PolicyType.CPPAuto.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers -- " + getTestTimeStamp());
            captureFailureScreenshotPrepLogFileAndSendEmails(polObjCPPAuto, "test" + PolicyType.CPPAuto.name() + "_CreatePolicyIssued-Error", e);
            throw e;
        }

    }


    @SuppressWarnings("serial")
    public void testCPPGeneralLiability_CreatePolicyIssed_Guts() throws Exception {
        // LOCATIONS
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(new AddressInfo(true), false) {{
                this.setBuildingList(new ArrayList<PolicyLocationBuilding>() {{
                    this.add(new PolicyLocationBuilding() {{
                    }}); // END BUILDING
                }}); // END BUILDING LIST
            }});// END POLICY LOCATION
        }}; // END LOCATION LIST

        ArrayList<CPPGeneralLiabilityExposures> exposures = new ArrayList<CPPGeneralLiabilityExposures>();
        exposures.add(new CPPGeneralLiabilityExposures(locationsLists.get(0), GLClassCode.GL_10026.getValue()));

        CPPGeneralLiabilityCoverages coverages = new CPPGeneralLiabilityCoverages() {{
            this.setAdditionalInsuredslist(new ArrayList<CPPGLCoveragesAdditionalInsureds>() {{
                this.add(new CPPGLCoveragesAdditionalInsureds() {{
                    this.setLocationList(new ArrayList<PolicyLocation>() {{
                        this.add(locationsLists.get(0));
                    }});
                }});
            }});
        }};

        CPPGeneralLiability generalLiability = new CPPGeneralLiability() {{
            this.setCPPGeneralLiabilityExposures(exposures);
            this.setCPPGeneralLiabilityCoverages(coverages);
        }};
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        // GENERATE POLICY
        myPolicyObjectCPPGL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPGeneralLiability(generalLiability)
                .withLineSelection(LineSelection.GeneralLiabilityLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Mini GL")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsLists.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    // Verify that CPP policy went to BC and correct charges came across
    public void testCPPGeneralLiability_VerifyPolicyinBC_Wrapper() throws Exception {
        test_VerifyPolicyinBC_Guts("test" + PolicyType.CPPGeneralLiability.name() + "_VerifyPolicyinBC-Error", PolicyType.CPPGeneralLiability);
    }


    // Verify that CPP policy went to BC and correct charges came across
    public void testCPPAuto_VerifyPolicyinBC_Wrapper() throws Exception {
        test_VerifyPolicyinBC_Guts("test" + PolicyType.CPPAuto.name() + "_VerifyPolicyinBC-Error", PolicyType.CPPAuto);
    }

    // Creates and Issue PL Personal Auto Only Policy
    public void testSquireAuto_CreatePolicyIssued_Guts() throws Exception {
        GeneratePolicy polObjSquireAuto = null;
        try {
        	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    		driver = buildDriver(cf);

            Squire mySquire = new Squire(SquireEligibility.City);
            mySquire.squirePA = new SquirePersonalAuto();

            polObjSquireAuto = new GeneratePolicy.Builder(driver)
                    .withSquire(mySquire)
                    .withProductType(ProductLineType.Squire)
                    .withInsFirstLastName("Mini", "R2SquireAuto")
                    .withDownPaymentType(PaymentType.Cash)
                    .build(GeneratePolicyType.PolicyIssued);

            setMyPolicyObjSquireAuto(polObjSquireAuto);

        } catch (Exception e) {
            setFailureTechnicalEmailSubject("***Failed*** " + PolicyType.SquireAuto.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers (Technical Details) -- " + getTestTimeStamp());
            setFailureGeneralEmailSubject("***Failed*** " + PolicyType.SquireAuto.name() + "_CreatePolicyIssued Deploy Check for " + getEnvironmentsToDeployTo() + " Servers -- " + getTestTimeStamp());
            captureFailureScreenshotPrepLogFileAndSendEmails(polObjSquireAuto, "test" + PolicyType.SquireAuto.name() + "_CreatePolicyIssued-Error", e);
            throw e;
        }

    }

    // Verify that PL policy went to BC and correct charges came across
    public void testSquireAuto_VerifyPolicyinBC_Wrapper() throws Exception {
        test_VerifyPolicyinBC_Guts("test" + PolicyType.SquireAuto.name() + "_VerifyPolicyinBC-Error", PolicyType.SquireAuto);
    }

    private void test_VerifyPolicyinBC_Guts(String errorScreenshotName, PolicyType polType) throws Exception {
        GeneratePolicy polObj = getMyPolicyObj(polType);
        try {
        	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    		driver = buildDriver(cf);
            new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw", polObj.accountNumber);

            BCPolicySummary policySummaryPage = new BCPolicySummary(driver);
            getQALogger().info(String.valueOf(policySummaryPage.getPremiumCharges()));
            getQALogger().info(String.valueOf(new GuidewireHelpers(driver).getPolicyPremium(polObj).getInsuredPremium()));
            if (policySummaryPage.getPremiumCharges() != (new GuidewireHelpers(driver).getPolicyPremium(polObj).getInsuredPremium())) {
                Assert.fail("Charges did not match that of ones in PC");
            }
        } catch (Exception e) {
            setFailureTechnicalEmailSubject("***Failed*** " + polType.name() + "_VerifyPolicyinBC Deploy Check for " + getEnvironmentsToDeployTo() + " Servers (Technical Details) -- " + getTestTimeStamp());
            setFailureGeneralEmailSubject("***Failed*** " + polType.name() + "_VerifyPolicyinBC Deploy Check for " + getEnvironmentsToDeployTo() + " Servers -- " + getTestTimeStamp());
            captureFailureScreenshotPrepLogFileAndSendEmails(polObj, errorScreenshotName, e);
            throw e;
        }
    }

    private void captureFailureScreenshotPrepLogFileAndSendEmails(GeneratePolicy polObj, String filename, Exception e) {

//        String dateStamp = DateUtils.dateFormatAsString("yyyyMMddHHmmssSSS", new Date());
//        String screenshotPath = captureScreen(filename + "-" + dateStamp);
//        ArrayList<String> attachmentsList = new ArrayList<String>();
//        attachmentsList.add(screenshotPath);
//        String filePath = "C:\\tmp\\exceptions\\";
//        File miniregFolder = new File(filePath);
//        if (!miniregFolder.exists()) {
//            miniregFolder.mkdir();
//        }
//        filePath = filePath + filename + "-" + dateStamp + ".log";
//        PrintStream ps = new PrintStream(new File(filePath));
//        e.printStackTrace(ps);
//        ps.close();
//        setFailureTechnicalEmailBody(getFailureTechnicalEmailBody() + "<b>");
//        String failurePolicy = null;
//        failurePolicy = failurePolicy + "<ul>";
//        if (polObj != null) {
//            if (polObj.accountNumber != null) {
//                failurePolicy = failurePolicy + "<li>Account Number: " + polObj.accountNumber + "</li>";
//            }
//            if (!((polObj.pniContact.getCompanyName() == null) && (polObj.pniContact.getFirstName() == null) && (polObj.pniContact.getLastName() == null))) {
//                if (polObj.pniContact.getCompanyName() == null) {
//                    failurePolicy = failurePolicy + "<li>First/Last Name: " + polObj.pniContact.getFirstName() + " " + polObj.pniContact.getFirstName() + "</li>";
//                } else {
//                    failurePolicy = failurePolicy + "<li>Company Name: " + polObj.pniContact.getCompanyName() + "</li>";
//                }
//            }
//        }
//        failurePolicy = failurePolicy + "</ul>";
//        setFailureTechnicalEmailBody(getFailureTechnicalEmailBody() + failurePolicy);
//
//        setFailureTechnicalEmailBody(getFailureTechnicalEmailBody() + "<p>" + e.getMessage() + "</p>");
//        setFailureTechnicalEmailBody(getFailureTechnicalEmailBody() + "<b>");
//        attachmentsList.add(filePath);
//        systemOut(getFailureTechnicalEmailBody());
//        sendEmail(getFailureTechnicalEmailToList(), getFailureTechnicalEmailSubject(), getFailureTechnicalEmailBody(), attachmentsList);
//        sendEmail(getFailureGeneralEmailToList(), getFailureGeneralEmailSubject(), getFailureGeneralEmailBody(), null);
    }

}