package regression.r3.noclock.policycenter.commercialproperty;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommercialProperty.BuildingCoverageCauseOfLoss;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.CommercialPropertyForms;
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
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyLine_Coverages;
import repository.gw.generate.custom.CPPCommercialPropertyLine_ExclusionsConditions;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.forms.cpp.commercialproperty.CommercialPropertyFormInference;
import repository.pc.workorders.generic.GenericWorkorderForms;
import persistence.globaldatarepo.entities.CPForms;
import persistence.globaldatarepo.helpers.CPFormsHelper;

@QuarantineClass
public class CPFormsTest extends BaseTest {

    private static int numberOfRandomFormsToTest = 3;

    public GeneratePolicy myPolicyObj = null;
//	public volatile GeneratePolicy myFormsPolicy; = new GeneratePolicy(driver);

    SoftAssert softAssert = new SoftAssert();
    public List<CommercialPropertyForms> formsList = new ArrayList<CommercialPropertyForms>();

    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test(enabled = true)
    public void generateQQPolicy() throws Exception {
        AddressInfo pniAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>() {{
            this.add(new PolicyLocation(pniAddress, true));
        }};

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine() {{
            this.setPropertyLineCoverages(new CPPCommercialPropertyLine_Coverages() {{
                //SET COMMERCIAL PROPERTY LINE COVERAGES HERE
            }});
            this.setPropertyLineExclusionsConditions(new CPPCommercialPropertyLine_ExclusionsConditions() {{
                //SET COMMERCIAL PROPERTY LINE EXCLUSIONS CONDITIONS HERE
            }});
        }};

        //LIST OF COMMERCIAL PROPERTY
        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>() {{
            this.add(new CPPCommercialPropertyProperty() {{
                this.setAddress(pniAddress);
                this.setCPPCommercialProperty_Building_List(new ArrayList<CPPCommercialProperty_Building>() {{
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildingCoverage) {{
                            this.setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Broad);
                        }});
                    }});// end building 0
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BusinessPersonalPropertyCoverage) {{
                            this.setBuildingCoverage_CauseOfLoss(BuildingCoverageCauseOfLoss.Broad);
                        }});
                    }});// end building 1
                    this.add(new CPPCommercialProperty_Building("1150") {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BuildersRiskCoverageForm_CP_00_20) {{
                        }});
                    }});// end building 2
                    this.add(new CPPCommercialProperty_Building("1160") {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.PropertyInTheOpen) {{
                        }});
                    }});// end building 3
                    this.add(new CPPCommercialProperty_Building() {{
                        this.setCoverages(new CPPCommercialProperty_Building_Coverages(PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40) {{
                        }});
                    }});// end building 4
                }});
            }});
        }};

        CPPCommercialProperty commercialProperty = new CPPCommercialProperty() {{
            this.setCommercialPropertyLine(commercialPropertyLine);
            this.setCommercialPropertyList(commercialPropertyList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CP Forms")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(pniAddress)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);


        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        Login login = new Login(driver);

        CommercialPropertyFormInference formInference = new CommercialPropertyFormInference();
        myPolicyObj.commercialPropertyCPP.setCpForms(formInference.getExpectedForms(myPolicyObj));

        if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
            login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        } else {
            login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        }

        boolean testFailed = false;
        List<String> extraForms = new ArrayList<String>();
        List<CPForms> missingForms = new ArrayList<CPForms>();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        List<String> formsList = formsPage.getFormDescriptionsFromTable();

        for (String form : formsList) {
            boolean found = false;
            for (CommercialPropertyForms cpform : myPolicyObj.commercialPropertyCPP.getCpForms()) {
                //if policy is in Submission status
                CPForms tempForm = CPFormsHelper.getCommercialPropertyFormByName(cpform.getValue());
                if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                    if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                    //if policy is issued
                } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                    if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                }
            }//END FormsCA FOR
            if (!found) {
                extraForms.add(form);
                testFailed = true;
            }//END IF
        }//END String FOR


        for (CommercialPropertyForms cpform : myPolicyObj.commercialPropertyCPP.getCpForms()) {
            CPForms tempForm = CPFormsHelper.getCommercialPropertyFormByName(cpform.getValue());
            boolean found = false;
            //if policy is in Submission status
            if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
                //if policy is issued
            } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END OFR
                }//END IF
            }//END ELSE IF
            if (!found) {
                missingForms.add(tempForm);
                testFailed = true;
            }//END IF
        }// END FOR
        if (testFailed) {
            String missingList = "";
            String extraList = "";
            for (String form : extraForms) {
                extraList = extraList + form + " \n ";
            }//END FOR
            for (CPForms form : missingForms) {
                missingList = form.getName() + " \n ";
            }//END FOR
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "\nDocuments either didn't generate or generated when not supposed to\n" +
                    "Documents that FAILED to Generate: \n" + missingList + "\n" +
                    "Docuemnts that generated falsely: \n" + extraList);
        }//END IF testFailed

    }

    //THIS HAD TO BE ITS OWN STATIC CLASS FOR IT TO BE ABLE TO BE RUN AS A THREAD.
    public static class GetFormsList extends Thread {
        public List<CommercialPropertyForms> formsList = new ArrayList<CommercialPropertyForms>();

        public void run() {
            CommercialPropertyForms myForm = CommercialPropertyForms.random();
            for (int i = 0; i < numberOfRandomFormsToTest; i++) {
                myForm = CommercialPropertyForms.random();
                System.out.println(myForm.getValue());
                formsList.add(myForm);
            }
        }
    }

    //THIS HAD TO BE ITS OWN STATIC CLASS FOR IT TO BE ABLE TO BE RUN AS A THREAD.
    public static class StartDriver extends Thread {
        public void run() {
            // TODO Jon fix this.
//            Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
        }
    }


    @Test
    public void createPolicyFromFormsList() throws Exception {
        //^THIS IS A MAIN THREAD

        //THESE ARE OTHER THREADS
        GetFormsList getFormsList = new GetFormsList();//
        StartDriver startDriver = new StartDriver();
        //THESE START THE OTHER THREADS
        startDriver.start();
        getFormsList.start();

        //THESE WAIT FOR THE OTHER THREADS TO BE DONE BEFORE MOVING ON WITH THE MAIN THREAD.
        getFormsList.join();
        startDriver.join();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GeneratePolicy myFormsPolicy = new GeneratePolicy(driver);
        myFormsPolicy.productType = ProductLineType.CPP;
        myFormsPolicy.commercialPropertyCPP = new CPPCommercialProperty(new CPPCommercialPropertyProperty(new CPPCommercialProperty_Building()));
        myFormsPolicy.lineSelection.add(LineSelection.CommercialPropertyLineCPP);


        //THIS TAKES ALL THE FORMS NEEDED TO GENERATE
        //CREATE SINGLE THREADS OUT OF THEM AND RUNS EACH THRU COMMERCIALPROPERTYFORMINFERENCE
        //TO CREATE THE GENERATE OBJECT NEEDED TO INFER ALL THE FORMS.
        CommercialPropertyFormInference[] threadArray = new CommercialPropertyFormInference[getFormsList.formsList.size()];
        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new CommercialPropertyFormInference(myFormsPolicy, getFormsList.formsList.get(i));
            threadArray[i].start();
        }

        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i].join();
        }

        myFormsPolicy.generate(GeneratePolicyType.PolicyIssued);


        CommercialPropertyFormInference formInference = new CommercialPropertyFormInference();
        myFormsPolicy.commercialPropertyCPP.setCpForms(formInference.getExpectedForms(myFormsPolicy));

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        if (!guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.PolicyIssued)) {
            login.loginAndSearchSubmission(myFormsPolicy.agentInfo.getAgentUserName(), myFormsPolicy.agentInfo.getAgentPassword(), myFormsPolicy.accountNumber);
        } else {
            login.loginAndSearchPolicyByAccountNumber(myFormsPolicy.agentInfo.getAgentUserName(), myFormsPolicy.agentInfo.getAgentPassword(), myFormsPolicy.accountNumber);
        }

        boolean testFailed = false;
        List<String> extraForms = new ArrayList<String>();
        List<CPForms> missingForms = new ArrayList<CPForms>();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        List<String> formsList = formsPage.getFormDescriptionsFromTable();

        for (String form : formsList) {
            boolean found = false;
            for (CommercialPropertyForms cpform : myFormsPolicy.commercialPropertyCPP.getCpForms()) {
                //if policy is in Submission status
                CPForms tempForm = CPFormsHelper.getCommercialPropertyFormByName(cpform.getValue());
                if (guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.PolicySubmitted)) {
                    if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                    //if policy is issued
                } else if (guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.PolicyIssued)) {
                    if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                        if (tempForm.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }
                    }
                }
            }//END FormsCA FOR
            if (!found) {
                extraForms.add(form);
                testFailed = true;
            }//END IF
        }//END String FOR


        for (CommercialPropertyForms cpform : myFormsPolicy.commercialPropertyCPP.getCpForms()) {
            CPForms tempForm = CPFormsHelper.getCommercialPropertyFormByName(cpform.getValue());
            boolean found = false;
            //if policy is in Submission status
            if (guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.PolicySubmitted)) {
                if (tempForm.getSubmission().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
                //if policy is issued
            } else if (guidewireHelpers.getCurrentPolicyType(myFormsPolicy).equals(GeneratePolicyType.PolicyIssued)) {
                if (tempForm.getIssuance().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(tempForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END OFR
                }//END IF
            }//END ELSE IF
            if (!found) {
                missingForms.add(tempForm);
                testFailed = true;
            }//END IF
        }// END FOR
        if (testFailed) {
            String missingList = "";
            String extraList = "";
            for (String form : extraForms) {
                extraList = extraList + form + " \n ";
            }//END FOR
            for (CPForms form : missingForms) {
                missingList = form.getName() + " \n ";
            }//END FOR
            Assert.fail(driver.getCurrentUrl() + myFormsPolicy.accountNumber + "\nDocuments either didn't generate or generated when not supposed to\n" +
                    "Documents that FAILED to Generate: \n" + missingList + "\n" +
                    "Docuemnts that generated falsely: \n" + extraList);
        }//END IF testFailed
    }


}

















