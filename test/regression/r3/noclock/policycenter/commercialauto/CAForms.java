package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.CommercialAutoForm;
import repository.gw.enums.CommercialAutoLine.DeductibleLiabilityCoverage;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.StateInfo.Un_UnderInsuredMotoristLimit;
import repository.gw.enums.Vehicle.AdditionalInterestTypeCPP;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.forms.cpp.commercialauto.CommercialAutoFormInference;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoCommercialAutoLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoStateInfoCPP;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.globaldatarepo.entities.FormsCA;
import persistence.globaldatarepo.helpers.CAFormsHelpers;

/**
 * @Author jlarsen
 * @Requirement See Product Model
 * @Description Test Commercial Auto Forms
 * @DATE Aug 18, 2016
 */
@QuarantineClass
public class CAForms extends BaseTest {

    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy myPolicyObjForms = null;
    //	private List<FormsCA> changeFormsList = new ArrayList<>();
    private List<CommercialAutoForm> caFormsList = new ArrayList<>();

    private WebDriver driver;


    /**
     * @Author jlarsen
     * @Requirement See Product Model
     * @Description Test to Generate a policy to test forms against
     * @DATE Aug 18, 2016
     */
    @Test(enabled = false)
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));


        Vehicle newVehicle1 = new Vehicle();
        newVehicle1.setGaragedAt(locationsList.get(0).getAddress());

        Vehicle newVehicle2 = new Vehicle();
        newVehicle2.setGaragedAt(locationsList.get(0).getAddress());
        newVehicle2.setVehicleType(VehicleType.Miscellaneous);
        newVehicle2.setBodyType(BodyType.MotorHomesUpTo22Feet);
        newVehicle2.setMotorHomeHaveLivingQuarters(true);
        newVehicle2.setMotorHomeContentsCoverage(true);
        newVehicle2.setComprehensive(true);
        newVehicle2.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);

        final ArrayList<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(newVehicle1);
        vehicleList.add(newVehicle2);


        Contact newPerson1 = new Contact();
        newPerson1.setGender(Gender.randomGender());
        newPerson1.setPrimaryVehicleDriven(vehicleList.get(0).getVin());

        Contact newPerson2 = new Contact();
        newPerson2.setGender(Gender.randomGender());
        newPerson2.setExcludedDriver(true);

        final ArrayList<Contact> personList = new ArrayList<>();
        personList.add(newPerson1);
        personList.add(newPerson2);

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto();

        commercialAuto.setCommercialAutoLine(new CPPCommercialAutoLine());
        commercialAuto.setVehicleList(vehicleList);
        commercialAuto.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
        commercialAuto.setDriversList(personList);

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CA Forms")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(myPolicyObj.accountNumber);
    }//END generatePolicy


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void testCommercialAutoForms() throws Exception {
        boolean testFailed = false;
        List<String> extraForms = new ArrayList<>();
        List<FormsCA> missingForms = new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        CommercialAutoFormInference caFormsList = new CommercialAutoFormInference(driver);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);

        if (!guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
            login.loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        } else {
            login.loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        }


        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        List<String> formsList = formsPage.getFormDescriptionsFromTable();

        for (String form : formsList) {
            boolean found = false;
            for (FormsCA caform : caFormsList.getFormsList()) {

                //if policy is in Submission status
                if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                    if (caform.getSubmision().equalsIgnoreCase("Yes")) {
                        if (caform.getName().equalsIgnoreCase(form) || (caform.getName() + " " + caform.getNumber()).equalsIgnoreCase(form)) { //some forms have numbers in desc others dont.
                            found = true;
                            break;
                        }
                    }
                    //if policy is issued
                } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                    if (caform.getIssuance().equalsIgnoreCase("Yes")) {
                        if (caform.getName().equalsIgnoreCase(form) || (caform.getName() + " " + caform.getNumber()).equalsIgnoreCase(form)) { //some forms have numbers in desc others dont.
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

        for (FormsCA caform : caFormsList.getFormsList()) {
            boolean found = false;
            //if policy is in Submission status
            if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicySubmitted)) {
                if (caform.getSubmision().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(caform.getName()) || form.equalsIgnoreCase(caform.getName() + " " + caform.getNumber())) { //some forms have numbers in desc others dont.
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
                //if policy is issued
            } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObj).equals(GeneratePolicyType.PolicyIssued)) {
                if (caform.getIssuance().equalsIgnoreCase("Yes")) {
                    for (String form : formsList) {
                        if (form.equalsIgnoreCase(caform.getName()) || form.equalsIgnoreCase(caform.getName() + " " + caform.getNumber())) { //some forms have numbers in desc others dont.
                            found = true;
                            break;
                        }//END IF
                    }//END OFR
                }//END IF
            }//END ELSE IF
            if (!found) {
                missingForms.add(caform);
                testFailed = true;
            }//END IF
        }// END FOR
        if (testFailed) {
            String missingList = "";
            String extraList = "";
            for (String form : extraForms) {
                extraList = extraList + form + " \n ";
            }//END FOR
            for (FormsCA form : missingForms) {
                missingList = form.getName() + " \n ";
            }//END FOR
            Assert.fail(driver.getCurrentUrl() + myPolicyObj.accountNumber + "\nDocuments either didn't generate or generated when not supposed to\n" +
                    "Documents that FAILED to Generate: \n" + missingList + "\n" +
                    "Docuemnts that generated falsely: \n" + extraList);
        }//END IF testFailed
    }//END testCommercialAutoForms


    /**
     * @Author jlarsen
     * @Requirement See Product Model
     * @Description This test is used to generate a policy that will contain all the forms in User input list. or Randomly Generated list.
     * @DATE Aug 18, 2016
     */
    @Test()
    public void generateFormsPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

//		List<FormsCA> changeFormsList = CAFormsHelpers.getCommercialAutoChangeForm();

        //		caFormsList = new ArrayList<CommercialAutoForm>(){{
        //			this.add(CommercialAutoForm.IndividualNamedInsuredCA9917);
        //			this.add(CommercialAutoForm.LiabilityCoverageForRecreationalOrPersonalUseTrailersIDCA313005);
        //			this.add(CommercialAutoForm.AutoMedicalPaymentsCoverageCA9903);
        //			this.add(CommercialAutoForm.RoadsideAssistanceEndorsementIDCA313008);
        //			this.add(CommercialAutoForm.Lessor_AdditionalInsuredAndLossPayeeCA2001);
        //		}};


        for (int i = 0; i <= 10; i++) {
            CommercialAutoForm myForm = CommercialAutoForm.random();
//			newForm:
            if (caFormsList.contains(myForm)) {
                myForm = CommercialAutoForm.random();
//					break newForm;
            } else {
                System.out.println("Testing Form: " + myForm.getValue());
                caFormsList.add(myForm);
            }
        }

        GeneratePolicy myFormsPolicy = new CommercialAutoFormInference(driver).CreateFormsPolicyObject(caFormsList);


        myPolicyObjForms = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(myFormsPolicy.commercialAutoCPP)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(myFormsPolicy.commercialPackage.locationList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CA Forms")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(myFormsPolicy.commercialPackage.locationList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }//END generateFormsPolicy


    @Test(dependsOnMethods = {"generateFormsPolicy"})
    public void checkGeneratedForms() throws Exception {
        boolean testFailed = false;
        List<String> extraForms = new ArrayList<>();
        List<FormsCA> missingForms = new ArrayList<>();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        CommercialAutoFormInference caFormsList = new CommercialAutoFormInference(driver);

        SideMenuPC sidemenu = new SideMenuPC(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        if (!guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.PolicyIssued)) {
            login.loginAndSearchSubmission(myPolicyObjForms.agentInfo.getAgentUserName(), myPolicyObjForms.agentInfo.getAgentPassword(), myPolicyObjForms.accountNumber);
        } else {
            login.loginAndSearchPolicyByAccountNumber(myPolicyObjForms.agentInfo.getAgentUserName(), myPolicyObjForms.agentInfo.getAgentPassword(), myPolicyObjForms.accountNumber);
            PolicySummary summaryPage = new PolicySummary(driver);
            summaryPage.clickIssuanceTransaction(myPolicyObjForms.accountNumber);//issuance
        }

        sidemenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        List<String> formsListIssuance = formsPage.getFormDescriptionsFromTable();

        //		List<String> formsList = formsPage.getFormDescriptionsFromTable();

        for (String form : formsListIssuance) {
            boolean found = false;
            for (FormsCA caform : caFormsList.getFormsList()) {

                //if policy is in Submission status
                if (guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.PolicySubmitted)) {
                    if (caform.getSubmision().equalsIgnoreCase("Yes")) {
                        if (caform.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }//END IF
                    }//END IF
                    //if policy is issued
                } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.PolicyIssued)) {
                    if (caform.getIssuance().equalsIgnoreCase("Yes")) {
                        if (caform.getName().equalsIgnoreCase(form)) {
                            found = true;
                            break;
                        }//END IF
                    }//END IF
                }//END ELSE IF
            }//END FormsCA FOR
            if (!found) {
                extraForms.add(form);
                testFailed = true;
            }
        }//END String FOR

        for (FormsCA caForm : caFormsList.getFormsList()) {
            boolean found = false;
            //if policy is in Submission status
            if (guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.FullApp) || guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.PolicySubmitted)) {
                if (caForm.getSubmision().equalsIgnoreCase("Yes")) {
                    for (String form : formsListIssuance) {
                        if (form.equalsIgnoreCase(caForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
                //if policy is issued
            } else if (guidewireHelpers.getCurrentPolicyType(myPolicyObjForms).equals(GeneratePolicyType.PolicyIssued)) {
                if (caForm.getIssuance().equalsIgnoreCase("Yes")) {
                    for (String form : formsListIssuance) {
                        if (form.equalsIgnoreCase(caForm.getName())) {
                            found = true;
                            break;
                        }//END IF
                    }//END FOR
                }//END IF
            }//END ELSE IF
            if (!found) {
                missingForms.add(caForm);
                testFailed = true;
            }//END IF
        }//END FOR
        if (testFailed) {
            String missingList = "";
            StringBuilder extraList = new StringBuilder();
            for (String form : extraForms) {
                extraList.append(form).append(" \n ");
            }//END FOR
            for (FormsCA form : missingForms) {
                missingList = form.getName() + " \n ";
            }//END FOR
            Assert.fail(driver.getCurrentUrl() + myPolicyObjForms.accountNumber + "\nDocuments either didn't generate or generated when not supposed to\n" +
                    "Documents that FAILED to Generate: \n" + missingList + "\n" +
                    "Docuemnts that generated falsely: \n" + extraList);
        }//END IF testFailed
    }//END TEST checkGeneratedForms


    // this one did not run before
    @Test(enabled = false)
    public void testSpecialCaseForms() {
        //ROLLING STORES
        //IndividualNamedInsuredCA9917: Person type policy
        //		OutOfStateVehicleExclusionIDCA313011
        //Liability Coverage For Recreational Or Personal Use Trailers IDCA 31 3005
    }//END testSpecialCaseForms


    @Test(dependsOnMethods = {"generateFormsPolicy"}, enabled = false)
    public void testPolicyChangeCondition() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        switch (guidewireHelpers.getCurrentPolicyType(myPolicyObjForms)) {
            case QuickQuote:
            case FullApp:
            case PolicySubmitted:
                myPolicyObj.convertTo(driver, GeneratePolicyType.PolicyIssued);
            case PolicyIssued:
                new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjForms.agentInfo.getAgentUserName(), myPolicyObjForms.agentInfo.getAgentPassword(), myPolicyObjForms.accountNumber);
                break;
        }//END SWITCH

        StartPolicyChange change = new StartPolicyChange(driver);
        change.startPolicyChange("Forms Change", null);

        GenericWorkorder genwo = new GenericWorkorder(driver);
        SideMenuPC sidemenu = new SideMenuPC(driver);

        for (CommercialAutoForm form : caFormsList) {
            FormsCA myForm = CAFormsHelpers.getCommercialAutoFormByName(form.getValue());
            if (myForm.getCanFormChange().equals("Yes")) {
                switch (form) {
                    case DeductibleLiabilityCoverage_CA_03_01:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.DeductibleLiabilityCoverage_CA_03_01.getValue());
                        sidemenu.clickSideMenuCACommercialAutoLine();
                        GenericWorkorderCommercialAutoCommercialAutoLineCPP commautoline = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
                        commautoline.selectDeductibleLiabilityCoverageCA0301(DeductibleLiabilityCoverage.OneThousand1000);
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA_04_44:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.WaiverOfTransferOfRightsOfRecoveryAgainstOthersToUs_WaiverOfSubrogation_CA_04_44.getValue());

                        break;
                    case Lessor_AdditionalInsuredAndLossPayee_CA_20_01:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.Lessor_AdditionalInsuredAndLossPayee_CA_20_01.getValue());
                        sidemenu.clickSideMenuCAVehicles();
                        for (Vehicle vehicle : myPolicyObjForms.commercialAutoCPP.getVehicleList()) {
                            for (AdditionalInterest ai : vehicle.getAdditionalInterest()) {
                                if (ai.getAdditionalInterestTypeCPP().equals(AdditionalInterestTypeCPP.Lessor_AdditionalInsuredAndLossPayeeCA_20_01)) {
                                    GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
                                    vehiclesPage.editVehicleByVin(vehicle.getVin());
                                    vehiclesPage.clickVehicleAdditionalInterestsTab();
                                    GenericWorkorderAdditionalInterests addInterest = new GenericWorkorderAdditionalInterests(driver);
                                    addInterest.clickBuildingsPropertyAdditionalInterestsLink(ai.getCompanyName());
                                    addInterest.changePropertyAdditionalInterestAddressListing("New");
                                    AddressInfo newAddress = new AddressInfo(true);
                                    addInterest.setContactEditAddressLine1(newAddress.getLine1());
                                    addInterest.setContactEditAddressCity(newAddress.getCity());
                                    addInterest.setContactEditAddressState(State.Idaho);
                                    addInterest.setContactEditAddressZipCode(newAddress.getZip());
                                    addInterest.setContactEditAddressAddressType(AddressType.Billing);
                                    addInterest.setReasonForContactChange("Change to generate forms");
                                    addInterest.clickBuildingsPropertyAdditionalInterestsUpdateButton();


                                    vehiclesPage.clickOK();
                                }
                            }
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case MobileHomesContentsCoverage_CA_20_16:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.MobileHomesContentsCoverage_CA_20_16.getValue());
                        sidemenu.clickSideMenuCAVehicles();
                        for (Vehicle vehicle : myPolicyObjForms.commercialAutoCPP.getVehicleList()) {
                            if (vehicle.isMotorHomeContentsCoverage()) {
                                GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
                                vehiclesPage.editVehicleByVin(vehicle.getVin());
                                //change something on vehicle
                                vehiclesPage.clickVehicleAdditionalCoveragesTab();
                                vehiclesPage.setMobileHomeContentsCoverageCA2016Limit("500");
                                vehiclesPage.clickOK();
                            }
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.DesignatedInsuredForCoveredAutosLiabilityCoverage_CA_20_48.getValue());

                        break;
                    case CoverageForCertainOperationsInConnectionWithRailroads_CA_20_70:

                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.CoverageForCertainOperationsInConnectionWithRailroads_CA_20_70.getValue());
                        guidewireHelpers.logout();
                        Login login = new Login(driver);
                        login.loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObjForms.accountNumber);
                        sidemenu.clickSideMenuCACommercialAutoLine();
                        GenericWorkorderCommercialAutoCommercialAutoLineCPP commauto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
                        commauto.clickAdditionalCoveragesTab();
                        commauto.setScheduledRailroad("Policy Change For Form");
                        commauto.setDesignatedJobSite("Another Policy Change For Forms");
                        genwo.clickGenericWorkorderSaveDraft();
                        guidewireHelpers.logout();
                        login.loginAndSearchPolicyByAccountNumber(myPolicyObjForms.agentInfo.getAgentUserName(), myPolicyObjForms.agentInfo.getAgentPassword(), myPolicyObjForms.accountNumber);
                        break;
                    case AutoLoan_LeaseGapCoverage_CA_20_71:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.AutoLoan_LeaseGapCoverage_CA_20_71.getValue());
                        sidemenu.clickSideMenuCAVehicles();
                        for (Vehicle vehicle : myPolicyObjForms.commercialAutoCPP.getVehicleList()) {
                            if (!vehicle.getAdditionalInterest().isEmpty() && vehicle.isAutoLoanGapCoverage_CA2071()) {
                                GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
                                vehiclesPage.editVehicleByVin(vehicle.getVin());
                                //change soemtingn on vehicle
                                vehiclesPage.clickOK();
                            }
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case MotorCarriers_InsuranceForNon_TruckingUse_CA_23_09:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.MotorCarriers_InsuranceForNon_TruckingUse_CA_23_09.getValue());
                        sidemenu.clickSideMenuCAVehicles();
                        for (Vehicle vehicle : myPolicyObjForms.commercialAutoCPP.getVehicleList()) {
                            if (!vehicle.getAdditionalInterest().isEmpty() && vehicle.getCaVehicleAdditionalCoveragesList().contains(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309)) {
                                GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
                                vehiclesPage.editVehicleByVin(vehicle.getVin());
                                //change soemtingn on vehicle
                            }
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case IdahoUninsuredMotoristsCoverage_CA_31_15:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.IdahoUninsuredMotoristsCoverage_CA_31_15.getValue());
                        sidemenu.clickSideMenuCAStateInfo();
                        GenericWorkorderCommercialAutoStateInfoCPP stateInfoPage = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
                        if (stateInfoPage.getUninsuredMotoristLimit().equals("50,000")) {
                            System.out.println("UNDERINSURED LIMIT ALREADY SET TO LOWEST POSSIBLE VALUE");
                        } else {
                            stateInfoPage.selectUninsuredMotoristLimit(Un_UnderInsuredMotoristLimit.FiftyThousand50K);
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case IdahoUnderinsuredMotoristsCoverage_CA_31_18:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.IdahoUnderinsuredMotoristsCoverage_CA_31_18.getValue());
                        sidemenu.clickSideMenuCAStateInfo();
                        GenericWorkorderCommercialAutoStateInfoCPP stateInfoPage1 = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
                        if (stateInfoPage1.getUnderinsuredMotoristLimit().equals("50,000")) {
                            System.out.println("UNDERINSURED LIMIT ALREADY SET TO LOWEST POSSIBLE VALUE");
                        } else {
                            stateInfoPage1.selectUnderinsuredMotoristLimit(Un_UnderInsuredMotoristLimit.FiftyThousand50K);
                        }
                        genwo.clickGenericWorkorderSaveDraft();
                        break;
                    case DriveOtherCarCoverage_BroadenedCoverageForNamedIndividuals_CA_99_10:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.DriveOtherCarCoverage_BroadenedCoverageForNamedIndividuals_CA_99_10.getValue());

                        break;
                    case FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoverages_CA_99_14:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.FireFireAndTheftFireTheftAndWindstormAndLimitedSpecifiedCausesOfLossCoverages_CA_99_14.getValue());

                        break;
                    case RentalReimbursementCoverage_CA_99_23:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.RentalReimbursementCoverage_CA_99_23.getValue());

                        break;
                    case GaragekeepersCoverage_CA_99_37:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.GaragekeepersCoverage_CA_99_37.getValue());

                        break;
                    case ExclusionOrExcessCoverageHazardsOtherwiseInsured_CA_99_40:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.ExclusionOrExcessCoverageHazardsOtherwiseInsured_CA_99_40.getValue());

                        break;
                    case AudioVisualandDataElectronicEquipmentCoverageAddedLimits_CA_99_60:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.AudioVisualandDataElectronicEquipmentCoverageAddedLimits_CA_99_60.getValue());

                        break;
                    case MCS_90:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.MCS_90.getValue());

                        break;
                    case BusinessAutoDeclarations_IDCA_03_0001:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.BusinessAutoDeclarations_IDCA_03_0001.getValue());

                        break;
                    case IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.IdahoUninsuredMotoristAndUnderinsuredMotoristDisclosureStatement_IDCA_10_0002.getValue());

                        break;
                    case ExcludedDriverAcknowledgmentLetter_IDCA_18_0001:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.ExcludedDriverAcknowledgmentLetter_IDCA_18_0001.getValue());

                        break;
                    case LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimits_IDCA_31_3002:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.MobileHomesContentsNotCovered_IDCA_31_3003.getValue());

                        break;
                    case MobileHomesContentsNotCovered_IDCA_31_3003:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.MobileHomesContentsNotCovered_IDCA_31_3003.getValue());

                        break;
                    case RemovalOfPropertyDamageCoverage_IDCA_31_3006:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.RemovalOfPropertyDamageCoverage_IDCA_31_3006.getValue());

                        break;
                    case ExcludedDriverEndorsement_IDCA_31_3007:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009.getValue());

                        break;
                    case AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.AdditionalNamedInsuredForDesignatedPersonOrOrganization_IDCA_31_3009.getValue());

                        break;
                    case CommercialAutoManuscriptEndorsement_IDCA_31_3013:
                        System.out.println("Testing Change Condition For: " + CommercialAutoForm.CommercialAutoManuscriptEndorsement_IDCA_31_3013.getValue());

                        break;
                    default:
                        break;
                }//END SWITCH
            }//END IF
        }//END FOR
        genwo.clickGenericWorkorderQuote();
        sidemenu.clickSideMenuForms();
        ///GET FORMS LIST AND COMPAIR.
    }//END TEST testPolicyChangeCondition


}



















