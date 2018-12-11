package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.CommercialAutoLine.CALineExclutionsAndConditions;
import repository.gw.enums.CommercialAutoUWIssues;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.AdditionalInterestTypeCPP;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.BusinessUseClass;
import repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages;
import repository.gw.enums.Vehicle.CAVehicleExclusionsAndConditions;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.LongDistanceOptions;
import repository.gw.enums.Vehicle.Radius;
import repository.gw.enums.Vehicle.SecondaryClass;
import repository.gw.enums.Vehicle.SecondaryClassType;
import repository.gw.enums.Vehicle.SizeClass;
import repository.gw.enums.Vehicle.SpecifiedCauseOfLoss;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoCommercialAutoLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoDriver;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoStateInfoCPP;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import persistence.globaldatarepo.helpers.VINHelper;

@QuarantineClass
public class CAUnderWritingIssuesRework extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;

    boolean vehicles = false;
    boolean commAutoLine = false;
    boolean drivers = false;
    boolean stateInfo = false;
    boolean qualifications = false;

    boolean testFailed = false;
    String failureString = "Failed to find Underwriting Issues: \n";

    @SuppressWarnings("serial")
    public List<String> otherCarCoverages = new ArrayList<String>() {{
        this.add("Comprehensive");
        this.add("Liability");
        this.add("Medical Payments");
        this.add("Underinsured Motorists");
        this.add("Uninsured Motorists");
        this.add("Collision");
    }};

    private WebDriver driver;


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description Generate Policy for testing
     * @DATE Sep 14, 2016
     */
    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                }});
            }
        };

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
                this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};

        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("CA UWIssues")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description Test all Informational/NON Blocking type UW Issues generate
     * @DATE Sep 14, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyInformationalUWIssues() throws Exception {

        vehicles = false;
        commAutoLine = false;
        drivers = false;
        stateInfo = false;
        qualifications = false;

        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        List<CommercialAutoUWIssues> nonBlockingIssues = new ArrayList<CommercialAutoUWIssues>() {{
            this.add(CommercialAutoUWIssues.Pleaseprovideunderwritingwithcopiesofleaseagreementsifnotafinancialinstitution);
            this.add(CommercialAutoUWIssues.Mobilehomecontentscoveragedoesnotincludeofficeequipmentsalessamplestheftmoneyetc);
            this.add(CommercialAutoUWIssues.Notifytheapplicant_insuredthatprofessionalsareexcludedContactbrokerageforassistance);
            this.add(CommercialAutoUWIssues.TruckersInsuranceforNon_truckinguseislimitedtosituationswheretheinsuredisnothaulingpropertyofothershaulingotherstrailersoroperatingunderaleaseagreementwithothersExplaintherestrictionsofcoveragetotheapplicant_insured);
            this.add(CommercialAutoUWIssues.Provideunderwritingwithaphotodescriptionanduseoftheamphibiousvehicletobeinsured);
            this.add(CommercialAutoUWIssues.PleaseensurethatalldriversthatneedtobeaddedtotheDriveOtherCarareadded);
            this.add(CommercialAutoUWIssues.Pleasenotifytheapplicant_insuredthatnocoverageisprovidedforcontentsofthetrailerequippedwithlivingquartersorthemobilehomeIfcoverageisdesiredchooseCA2016MobileHomesContentsCoverage);
            this.add(CommercialAutoUWIssues.Informapplicant_insuredthatweareexcludingtheexistingdamage);
            this.add(CommercialAutoUWIssues.InordertoprocessanexcludeddrivertheinsuredmustsignarequestshowingthattheyacknowledgethatspecifieddriveristobeexcludedPleasescanthedocumenttofileandsubmittounderwritingforconsideration);
        }};

        setupPolicyObject(nonBlockingIssues);

        updatePolicy();

        verifyUWIssue(quoteAndGetUWIssues(), UnderwriterIssueType.Informational, nonBlockingIssues);

        myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().resetChangeCondition();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }
    }//END verifyInformationalUWIssues()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description Test all Block Bind type UW Issues generate
     * @DATE Sep 14, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyBlockBindlUWIssues() throws Exception {
        vehicles = false;
        commAutoLine = false;
        drivers = false;
        stateInfo = false;
        qualifications = false;

        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        List<CommercialAutoUWIssues> blockBindIssues = new ArrayList<CommercialAutoUWIssues>() {{
            this.add(CommercialAutoUWIssues.Applicant_insuredhasrejectedIdahoUninsuredandUnderinsuredMotoristcoveragecompleterejectionformIDCA100002andsubmittounderwriting);
            this.add(CommercialAutoUWIssues.ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940hasbeenselectedProvideunderwritingdetailsastotheothercoverageinplaceandreasonthatWCisnotinsuringtheautos);
            this.add(CommercialAutoUWIssues.Therearevehiclesover45001lbsanddriversunder25Pleasesubmittounderwritingforconsideration);
            this.add(CommercialAutoUWIssues.LongdistanceoptionissettoDenverProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
            this.add(CommercialAutoUWIssues.LongdistanceoptionissettoPortlandProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
            this.add(CommercialAutoUWIssues.LongdistanceoptionsettoSaltLakeCityProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
            this.add(CommercialAutoUWIssues.LongdistanceoptionissettoPacificProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
            this.add(CommercialAutoUWIssues.LongdistanceoptionissettoMountainProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
        }};

        setupPolicyObject(blockBindIssues);

        updatePolicy();


        verifyUWIssue(quoteAndGetUWIssues(), UnderwriterIssueType.BlockSubmit, blockBindIssues);

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }
    }//END verifyBlockBindlUWIssues()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description Test all Block Quote Release type UW Issues generate
     * @DATE Sep 14, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void verifyBlockQuoteReleaseUWIssues() throws Exception {
        vehicles = false;
        commAutoLine = false;
        drivers = false;
        stateInfo = false;
        qualifications = false;

        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        List<CommercialAutoUWIssues> blockQuoteReleaseIssues = new ArrayList<CommercialAutoUWIssues>() {{
////			this.add(CommercialAutoUWIssues.ForCA2070pleaserefertoUnderwritingsupervisorforreviewtreatyexceptionandpricing);
            this.add(CommercialAutoUWIssues.AudioVisualandDataElectronicEquipmentlimitsover10000requirepriorapprovalProvideUnderwritingalistofequipmentapproximatevaluesandthepurposeoftheequipmentforconsideration);
            this.add(CommercialAutoUWIssues.Applicant_insuredhasrequestedaMCS_90ThismustbereferredtounderwritingExplainthebondfeaturesofthisendorsementandtheirobligationtoreimburseusforotherwiseuncoveredlosses);
            this.add(CommercialAutoUWIssues.Applicant_insuredtransportsperishablerefrigeratedfoodproductsDescribecleaningandsanitationproceduresofthetrailerfromoneloadtotheother);
            this.add(CommercialAutoUWIssues.Applicant_insuredisajunkdealerPleasedescribenatureofitemstransportedtounderwritingtodetermineeligibility);
            this.add(CommercialAutoUWIssues.Applicant_insuredsautooperationisclassifiedasallotherPleasecontactunderwritingwithdetailregardingtheoperationforassistanceinrating);
////			this.add(CommercialAutoUWIssues.PleaseverifythatZipcodeisthecorrectzipcodeIfitiscorrectpleasecontactunderwritingforterritorycode);
            this.add(CommercialAutoUWIssues.PleaseexplainwhytheFactoryCostNewvaluewaschangedfromitsoriginalvalue);
////			this.add(CommercialAutoUWIssues.Pleaserequestapprovalfromtheunderwritertoaddsymbol123or4tothepolicyorremoveExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940);
        }};

        setupPolicyObject(blockQuoteReleaseIssues);

        updatePolicy();


        verifyUWIssue(quoteAndGetUWIssues(), UnderwriterIssueType.BlockQuoteRelease, blockQuoteReleaseIssues);

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }
    }//END verifyBlockQuoteReleaseUWIssues()


    /**
     * @throws Exception
     * @Author jlarsen
     * @Description Test all Reject type UW Issues generate
     * @DATE Sep 14, 2016
     */
    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void verifyRejectUWIssues() throws Exception {

        vehicles = false;
        commAutoLine = false;
        drivers = false;
        stateInfo = false;
        qualifications = false;

        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        List<CommercialAutoUWIssues> rejectIssues = new ArrayList<CommercialAutoUWIssues>() {{
            this.add(CommercialAutoUWIssues.CannothaveMedicalPaymentsCA9903ontrailersonlyorwithoutanyvehiclesPleaseremoveMedicalPaymentsCA9903);
            this.add(CommercialAutoUWIssues.DriveOtherCarCoverageisonlyavailableifpolicyhasaprivatepassengervehicleinsuredContactunderwritingasitmaybenecessarytochangetheratingonalightvehicle);
            this.add(CommercialAutoUWIssues.CannotsetLiabilitylimitsbelowUnderinsuredMotorist_UninsuredMotorist_DOCUnderinsuredMotorist_DOCUninsuredmotoristPleaseadjustlimitsbelowLiabilityOptionalCoveragesBasedonexistence);
            this.add(CommercialAutoUWIssues.Inordertoqualifyforgaragekeeperscoveragetheapplicant_insuredmusthaveliabilitycoverageonownedornon_ownedvehiclesPleaseaddliabilitycoverage);
            this.add(CommercialAutoUWIssues.TheremustbeoneyouthfuldriverpereachprivatepassengervehicleIftherearemoreyouthfuldriversthentherearevehiclesputtheexcessdriverasNotRated);
            this.add(CommercialAutoUWIssues.ThereismorethanoneyouthfuldriverratedonthesamevehiclePleaseassignoneyouthfuloperatorpervehicle);
            this.add(CommercialAutoUWIssues.Therearedriversundertheageof21pleaseaddthedriverstothedriversscreen);
            this.add(CommercialAutoUWIssues.ThequestionIstrailerbeingtowedbyalighttruckismarkedyesHowevertherearenolighttrucksonthepolicyPleaseaddalighttruckorchangestheanswertono);
            this.add(CommercialAutoUWIssues.LiabilityneedstoberemovedfromthepolicybecausetherearenovehicleswithLiabilityCoverage);
            this.add(CommercialAutoUWIssues.OneormoreofthevehicleshavetheLiabilityCoveragequestionmarkedYesPleaseaddLiabilityontothepolicyorchangetheanswertoNo);
        }};

        setupPolicyObject(rejectIssues);

        updatePolicy();

        verifyUWIssue(quoteAndGetUWIssues(), UnderwriterIssueType.Reject, rejectIssues);

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }
    }//END verifyRejectUWIssues()


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = false)
    public void testSpecialCaseUWIssues() throws Exception {

        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();


        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCADrivers();

        //Pleaseadddriverstothepolicy
        GenericWorkorderCommercialAutoDriver driversPage = new GenericWorkorderCommercialAutoDriver(driver);
        driversPage.selectAllDrivers();
        driversPage.clickRemoveDriver();

        verifyUWIssue(quoteAndGetUWIssues(), UnderwriterIssueType.Reject, CommercialAutoUWIssues.Pleaseadddriverstothepolicy);

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }


    }


    private FullUnderWriterIssues quoteAndGetUWIssues() {
        GenericWorkorder genwo = new GenericWorkorder(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        genwo.clickGenericWorkorderQuote();
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        quote.clickPreQuoteDetails();

        risk.clickUWIssuesTab();

        return risk.getUnderwriterIssues();
    }


    @SuppressWarnings("serial")
    private void verifyUWIssue(FullUnderWriterIssues uwIssues, UnderwriterIssueType expectedType, CommercialAutoUWIssues uwIssue) {
        List<CommercialAutoUWIssues> uwIssuesList = new ArrayList<CommercialAutoUWIssues>() {{
            this.add(uwIssue);
        }};
        verifyUWIssue(uwIssues, expectedType, uwIssuesList);
    }


    private void verifyUWIssue(FullUnderWriterIssues uwIssues, UnderwriterIssueType expectedType, List<CommercialAutoUWIssues> uwIssuesList) {
        testFailed = false;
        failureString = "Failed to find Underwriting Issues: \n";
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        for (CommercialAutoUWIssues uwIssueExpected : uwIssuesList) {
            UnderwriterIssueType returnedType = risk.hasUWIssue(uwIssues, getUWIssueString(uwIssueExpected));
            if (!returnedType.equals(expectedType)) {
                testFailed = true;
                failureString = failureString + getUWIssueString(uwIssueExpected) + " Generated as the wrong Type. Expected - " + expectedType.name() + " : Acctual - " + returnedType.name() + "\n";
            }
        }
    }


    @SuppressWarnings("serial")
    private void setupPolicyObject(List<CommercialAutoUWIssues> caUWIssuesList) throws Exception {
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        myPolicyObjCPP.commercialAutoCPP.setVehicleList(new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
            }});
        }});

        for (CommercialAutoUWIssues uwIssue : caUWIssuesList) {
            switch (uwIssue) {
                case AudioVisualandDataElectronicEquipmentlimitsover10000requirepriorapprovalProvideUnderwritingalistofequipmentapproximatevaluesandthepurposeoftheequipmentforconsideration:
                    //If limit for CA 99 60 is greater than $10,000 it is a referral to the company. Display the message
                    vehicles = true;
                    Vehicle bigAudioVehicle = new Vehicle() {{
                        this.setVehicleName("bigAudioVehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.PrivatePassenger);
                        this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                            this.add(new AdditionalInterest(ContactSubType.Company) {{
                                this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002);
                                this.setAdditionalInterestSubType(AdditionalInterestSubType.CAVehicles);
                            }});
                        }});
                        this.setAudioVisualData_CA_99_60(true);
                        this.setAudioVisualDataLimit(11000);
                        this.setUwIssue(CommercialAutoUWIssues.AudioVisualandDataElectronicEquipmentlimitsover10000requirepriorapprovalProvideUnderwritingalistofequipmentapproximatevaluesandthepurposeoftheequipmentforconsideration);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(bigAudioVehicle);
                    break;
                case Applicant_insuredhasrequestedaMCS_90ThismustbereferredtounderwritingExplainthebondfeaturesofthisendorsementandtheirobligationtoreimburseusforotherwiseuncoveredlosses:
                    commAutoLine = true;
                    //When MCS-90 is checked on the system display the message.
                    myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setMcs_90(true);
                    break;
                case Applicant_insuredtransportsperishablerefrigeratedfoodproductsDescribecleaningandsanitationproceduresofthetrailerfromoneloadtotheother:
                    //If any of the following are selected in the secondary class Fish and seafood, Frozen Food, or Meat and Poultry display the message.
                    vehicles = true;
                    Vehicle foodVehicle = new Vehicle() {{
                        this.setVehicleName("food Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setBusinessUseClass(BusinessUseClass.ServiceUse);
                        this.setSecondaryClassType(SecondaryClassType.FoodDelivery);
                        this.setSecondaryClass((guidewireHelpers.getRandBoolean()) ? ((guidewireHelpers.getRandBoolean()) ? SecondaryClass.FishAndSeafood : SecondaryClass.FrozenFood) : SecondaryClass.MeatAndPoultry);
                        this.setUwIssue(CommercialAutoUWIssues.Applicant_insuredtransportsperishablerefrigeratedfoodproductsDescribecleaningandsanitationproceduresofthetrailerfromoneloadtotheother);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(foodVehicle);
                    break;
                case Applicant_insuredisajunkdealerPleasedescribenatureofitemstransportedtounderwritingtodetermineeligibility:
                    //If junk dealers is selected in the secondary class print and display the message
                    vehicles = true;
                    Vehicle junkDealer = new Vehicle() {{
                        this.setVehicleName("junkDealer");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSecondaryClassType(SecondaryClassType.WasteDisposal);
                        this.setSecondaryClass(SecondaryClass.JunkDealers);
                        this.setUwIssue(CommercialAutoUWIssues.Applicant_insuredisajunkdealerPleasedescribenatureofitemstransportedtounderwritingtodetermineeligibility);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(junkDealer);
                    break;
                case Applicant_insuredsautooperationisclassifiedasallotherPleasecontactunderwritingwithdetailregardingtheoperationforassistanceinrating:
                    //If all other is selected in the secondary class print
                    vehicles = true;
                    Vehicle allOtherVehicle = new Vehicle() {{
                        this.setVehicleName("allOtherVehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSecondaryClassType(SecondaryClassType.WasteDisposal);
                        this.setSecondaryClass(SecondaryClass.AllOther);
                        this.setUwIssue(CommercialAutoUWIssues.Applicant_insuredsautooperationisclassifiedasallotherPleasecontactunderwritingwithdetailregardingtheoperationforassistanceinrating);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(allOtherVehicle);
                    break;
                case PleaseexplainwhytheFactoryCostNewvaluewaschangedfromitsoriginalvalue:
                    //When VIN Validation is run and the user changes the value of the Factory Cost New to be lower than the original value of the Factory Cost New, then display the message.
                    vehicles = true;
                    Vehicle vinVehicle = new Vehicle() {{
                        this.setVehicleName("vinVehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.PrivatePassenger);
                        this.setVin("1FAFP23166G173615");
                        this.setFactoryCostNew(10);
                        this.setOverrideFactoryCostNew(true);
                        this.setUwIssue(CommercialAutoUWIssues.PleaseexplainwhytheFactoryCostNewvaluewaschangedfromitsoriginalvalue);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(vinVehicle);
                    break;

                case Inordertoqualifyforgaragekeeperscoveragetheapplicant_insuredmusthaveliabilitycoverageonownedornon_ownedvehiclesPleaseaddliabilitycoverage:
                    //This policy needs auto Owned Liability limits or non-owned auto to be selected. Then display the message.

                    break;
                case TheremustbeoneyouthfuldriverpereachprivatepassengervehicleIftherearemoreyouthfuldriversthentherearevehiclesputtheexcessdriverasNotRated:
                    //If there are enough youthful operators per vehicle and some of them are selected as �Not Rated� we want a block quote. Display the message. Excluded drivers are not counted as a youthful operator.

                    break;
                case ThereismorethanoneyouthfuldriverratedonthesamevehiclePleaseassignoneyouthfuloperatorpervehicle:
                    //If there is more than one youthful drive on the same vehicle we need a block quote and display the message.
                    drivers = true;
                    Contact youngDriver1 = new Contact() {{
                        this.setExcludedDriver(true);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getDriversList().add(youngDriver1);
                    drivers = true;
                    Contact youngDriver2 = new Contact() {{
                        this.setExcludedDriver(true);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getDriversList().add(youngDriver2);
                    break;
                case Therearedriversundertheageof21pleaseaddthedriverstothedriversscreen:
                    //When question "Are there any drivers under the age of 21?" is selected yes and there is no driver under the age of 21 on the policy. Display the message.
                    drivers = true;
                    Contact under21Driver = new Contact() {{
                        this.setExcludedDriver(true);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getDriversList().add(under21Driver);

                    break;
                case ThequestionIstrailerbeingtowedbyalighttruckismarkedyesHowevertherearenolighttrucksonthepolicyPleaseaddalighttruckorchangestheanswertono:
                    //If question on the vehicle information screen under the details tab "Is the trailer being towed by a light truck" is checked yes then. Then display the message.

                    break;
                case LiabilityneedstoberemovedfromthepolicybecausetherearenovehicleswithLiabilityCoverage:
                    //When Owned Liability is selected and all of the autos on the policy have declined Liability (Located Screen Vehicle Information > Tab Vehicle Details > Liability Coverage answered No) display the message. If no liability then insured cannot have UM/ UIM do not fire off Rule  CA023.

                    break;
                case OneormoreofthevehicleshavetheLiabilityCoveragequestionmarkedYesPleaseaddLiabilityontothepolicyorchangetheanswertoNo:
                    //When Owned Liability is unselected and the user has selected Yes to Liability Coverage (Located Screen Vehicle Information > Tab Vehicle Details > Liability Coverage answered Yes) display the message.

                    break;


                case Applicant_insuredhasrejectedIdahoUninsuredandUnderinsuredMotoristcoveragecompleterejectionformIDCA100002andsubmittounderwriting:
                    stateInfo = true;
                    if (guidewireHelpers.getRandBoolean()) {
                        myPolicyObjCPP.commercialAutoCPP.getCPP_CAStateInfo().setUninsuredMotoristCA3115(false);
                    } else {
                        myPolicyObjCPP.commercialAutoCPP.getCPP_CAStateInfo().setUnderinsuredMotoristCA3118(false);
                    }
                    break;
                case ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940hasbeenselectedProvideunderwritingdetailsastotheothercoverageinplaceandreasonthatWCisnotinsuringtheautos:
                    vehicles = true;
                    Vehicle hazardVehicle = new Vehicle() {{
                        this.setVehicleName("hazard Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setCaVehicleExclusionsAndConditionsList(new ArrayList<CAVehicleExclusionsAndConditions>() {{
                            this.add(CAVehicleExclusionsAndConditions.ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940);
                        }});
                        this.setUwIssue(CommercialAutoUWIssues.ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940hasbeenselectedProvideunderwritingdetailsastotheothercoverageinplaceandreasonthatWCisnotinsuringtheautos);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(hazardVehicle);
                    break;
                case Therearevehiclesover45001lbsanddriversunder25Pleasesubmittounderwritingforconsideration:
                    vehicles = true;
                    Vehicle heavyVehicle = new Vehicle() {{
                        this.setVehicleName("heavy Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.ExtraHeavyTrucksGVWOver45000Pounds);
                        this.setUwIssue(CommercialAutoUWIssues.Therearevehiclesover45001lbsanddriversunder25Pleasesubmittounderwritingforconsideration);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(heavyVehicle);
                    drivers = true;
                    Contact underageDriver = new Contact() {{
                        this.setAge(driver, 24);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getDriversList().add(underageDriver);
                    break;
                case LongdistanceoptionissettoDenverProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                    vehicles = true;
                    Vehicle denverVehicle = new Vehicle() {{
                        this.setVehicleName("denver Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setRadius(Radius.LongDistancesOver301Miles);
                        this.setLongDistanceOptions(LongDistanceOptions.Denver);
                        this.setUwIssue(CommercialAutoUWIssues.LongdistanceoptionissettoDenverProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(denverVehicle);
                    break;
                case LongdistanceoptionissettoPortlandProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                    vehicles = true;
                    Vehicle portlandVehicle = new Vehicle() {{
                        this.setVehicleName("portland Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setRadius(Radius.LongDistancesOver301Miles);
                        this.setLongDistanceOptions(LongDistanceOptions.Portland);
                        this.setUwIssue(CommercialAutoUWIssues.LongdistanceoptionissettoPortlandProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(portlandVehicle);
                    break;
                case LongdistanceoptionsettoSaltLakeCityProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                    vehicles = true;
                    Vehicle saltLakeVehicle = new Vehicle() {{
                        this.setVehicleName("salt Lake Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setRadius(Radius.LongDistancesOver301Miles);
                        this.setLongDistanceOptions(LongDistanceOptions.SaltLakeCity);
                        this.setUwIssue(CommercialAutoUWIssues.LongdistanceoptionsettoSaltLakeCityProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(saltLakeVehicle);
                    break;
                case LongdistanceoptionissettoPacificProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                    vehicles = true;
                    Vehicle pacificVehicle = new Vehicle() {{
                        this.setVehicleName("pacific Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setRadius(Radius.LongDistancesOver301Miles);
                        this.setLongDistanceOptions(LongDistanceOptions.Pacific);
                        this.setUwIssue(CommercialAutoUWIssues.LongdistanceoptionissettoPacificProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(pacificVehicle);
                    break;
                case LongdistanceoptionissettoMountainProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                    vehicles = true;
                    Vehicle mountainVehicle = new Vehicle() {{
                        this.setVehicleName("mountain Vehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Trucks);
                        this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                        this.setRadius(Radius.LongDistancesOver301Miles);
                        this.setLongDistanceOptions(LongDistanceOptions.Mountain);
                        this.setUwIssue(CommercialAutoUWIssues.LongdistanceoptionissettoMountainProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(mountainVehicle);
                    break;
                case Pleaseprovideunderwritingwithcopiesofleaseagreementsifnotafinancialinstitution:
                    vehicles = true;
                    Vehicle leasedVehicle = new Vehicle() {{
                        this.setVehicleName("leasedVehicle");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setLeasedToOthers(true);
                        this.setUwIssue(CommercialAutoUWIssues.Pleaseprovideunderwritingwithcopiesofleaseagreementsifnotafinancialinstitution);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(leasedVehicle);
                    break;
                case Mobilehomecontentscoveragedoesnotincludeofficeequipmentsalessamplestheftmoneyetc:
                    vehicles = true;
                    Vehicle contentsNOTCoverered = new Vehicle() {{
                        this.setVehicleName("contentsNOTCoverered");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Miscellaneous);
                        this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                        this.setMotorHomeHaveLivingQuarters(true);
                        this.setMotorHomeContentsCoverage(true);
                        this.setCollision(true);
                        this.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                        this.setComprehensive(true);
                        this.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                        this.setMobileHomesContentsCoverageCA2016Limit(1000);
                        this.setExistingDamage(true);
                        this.setUwIssue(CommercialAutoUWIssues.Mobilehomecontentscoveragedoesnotincludeofficeequipmentsalessamplestheftmoneyetc);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(contentsNOTCoverered);
                    break;
                case Notifytheapplicant_insuredthatprofessionalsareexcludedContactbrokerageforassistance:
                    vehicles = true;
                    Vehicle servicesNotCovered = new Vehicle() {{
                        this.setVehicleName("servicesNotCovered");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Miscellaneous);
                        this.setBodyType(BodyType.FuneralLimo);
                        this.setUwIssue(CommercialAutoUWIssues.Notifytheapplicant_insuredthatprofessionalsareexcludedContactbrokerageforassistance);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(servicesNotCovered);
                    break;
                case TruckersInsuranceforNon_truckinguseislimitedtosituationswheretheinsuredisnothaulingpropertyofothershaulingotherstrailersoroperatingunderaleaseagreementwithothersExplaintherestrictionsofcoveragetotheapplicant_insured:
                    vehicles = true;
                    Vehicle truckersInsurance = new Vehicle() {{
                        this.setVehicleName("truckersInsurance");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309);
                        this.setUwIssue(CommercialAutoUWIssues.TruckersInsuranceforNon_truckinguseislimitedtosituationswheretheinsuredisnothaulingpropertyofothershaulingotherstrailersoroperatingunderaleaseagreementwithothersExplaintherestrictionsofcoveragetotheapplicant_insured);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(truckersInsurance);
                    break;
                case Provideunderwritingwithaphotodescriptionanduseoftheamphibiousvehicletobeinsured:
                    commAutoLine = true;
                    myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().setExclutionsAndConditions_HasChanged(true);
                    myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getExclutionsAndConditions().add(CALineExclutionsAndConditions.AmphibiousVehiclesCA2397);
                    break;
                case PleaseensurethatalldriversthatneedtobeaddedtotheDriveOtherCarareadded:
                    commAutoLine = true;
                    myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_Coverages().setHasChanged(true);
                    switch (otherCarCoverages.get(NumberUtils.generateRandomNumberInt(0, otherCarCoverages.size() - 1))) {
                        case "Comprehensive":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarCompresensive(true);
                            break;
                        case "Liability":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarLiability(true);
                            break;
                        case "Medical Payments":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarMedicalPayments(true);
                            break;
                        case "Underinsured Motorists":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarUnderinsuredMotorist(true);
                            break;
                        case "Uninsured Motorists":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarUninsuredMotorist(true);
                            break;
                        case "Collision":
                            myPolicyObjCPP.commercialAutoCPP.getCommercialAutoLine().getCommercialAutoLine_AdditionalCoverages().setOtherCarCollision(true);
                            break;
                    }
                    break;
                case Pleasenotifytheapplicant_insuredthatnocoverageisprovidedforcontentsofthetrailerequippedwithlivingquartersorthemobilehomeIfcoverageisdesiredchooseCA2016MobileHomesContentsCoverage:
                    vehicles = true;
                    Vehicle trailerContents = new Vehicle() {{
                        this.setVehicleName("trailerContents");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setVehicleType(VehicleType.Miscellaneous);
                        this.setBodyType(BodyType.TrailerEquippedAsLivingQuarters);
                        this.setCollision(true);
                        this.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                        this.setSpecifiedCauseofLoss(true);
                        this.setSpecifiedCauseOfLoss(SpecifiedCauseOfLoss.LimitedSpecifiedCausesofLoss_ExceptTheft);
                        this.setMotorHomeHaveLivingQuarters(true);
                        this.setMotorHomeContentsCoverage(false);
                        this.setUwIssue(CommercialAutoUWIssues.Pleasenotifytheapplicant_insuredthatnocoverageisprovidedforcontentsofthetrailerequippedwithlivingquartersorthemobilehomeIfcoverageisdesiredchooseCA2016MobileHomesContentsCoverage);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(trailerContents);
                    break;
                case Informapplicant_insuredthatweareexcludingtheexistingdamage:
                    vehicles = true;
                    Vehicle existingDamage = new Vehicle() {{
                        this.setVehicleName("existingDamage");
                        this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
                        this.setLeasedToOthers(true);
                        this.setCollision(true);
                        this.setCollisionDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                        this.setExistingDamage(true);
                        this.setUwIssue(CommercialAutoUWIssues.Informapplicant_insuredthatweareexcludingtheexistingdamage);
                    }};
                    GenericWorkorderVehicles myVehicle = new GenericWorkorderVehicles(driver);
                    myVehicle.setRandomPropertyDamage(existingDamage);
                    myPolicyObjCPP.commercialAutoCPP.getVehicleList().add(existingDamage);
                    break;
                case InordertoprocessanexcludeddrivertheinsuredmustsignarequestshowingthattheyacknowledgethatspecifieddriveristobeexcludedPleasescanthedocumenttofileandsubmittounderwritingforconsideration:
                    drivers = true;
                    Contact excludedDriver = new Contact() {{
                        this.setExcludedDriver(true);
                    }};
                    myPolicyObjCPP.commercialAutoCPP.getDriversList().add(excludedDriver);
                    break;


                case ProvideproofofrepairsandphotostounderwritinginordertoshowrepairshavebeenmadeforIDCA313006:
                    //When there is a request to remove IDCA 31 3006 endorsement display the message.
                    //SPECIAL CASE ON CHANGE
                case ForCA2070pleaserefertoUnderwritingsupervisorforreviewtreatyexceptionandpricing:
                    //When endorsement CA 20 70 is selected display the message. Also the Underwriting supervisor is the only one who can approve it.
                    //SPECIAL CASE
                    //Please verify that $Zipcode is the correct zip code. If it is correct please contact underwriting for territory code.
                case PleaseverifythatZipcodeisthecorrectzipcodeIfitiscorrectpleasecontactunderwritingforterritorycode:
                    //If a zip code is entered and it does not match an Idaho zip code display the message.
                    //SPECIAL CASE
                case VehiclehasbeendeletedifthereiscargocoveragepleasereviewInlandMarineforanychanges:
                    //if truck tracktor or trailer is deleted Issuance and after only
                    //QUOTE AND ISSUE
                    //REMOVE IN CHANGE
                    //SPECIAL CASE
                case OutofStatevehiclesnotlistedonthepolicyareexcludedNotifyApplicant_insuredofthislimitation:
                case CannothaveMedicalPaymentsCA9903ontrailersonlyorwithoutanyvehiclesPleaseremoveMedicalPaymentsCA9903:
                case CannotsetLiabilitylimitsbelowUnderinsuredMotorist_UninsuredMotorist_DOCUnderinsuredMotorist_DOCUninsuredmotoristPleaseadjustlimitsbelowLiabilityOptionalCoveragesBasedonexistence:
                case DriveOtherCarCoverageisonlyavailableifpolicyhasaprivatepassengervehicleinsuredContactunderwritingasitmaybenecessarytochangetheratingonalightvehicle:
                case IdahoUninsuredandUnderinsuredMotoristhavebeenchangedtomatchtheDriveOtherCarUninsuredandUnderinsuredMotorist:
                case Pleaseadddriverstothepolicy:
                case Pleaserequestapprovalfromtheunderwritertoaddsymbol123or4tothepolicyorremoveExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940:
                    //SPECIAL CASE
                    break;
                default:
                    break;

            }//END SWITCH
        }//END FOR

        //Verfiy no duplicate VIN's exist
        int vinCount = 0;
        for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
            for (Vehicle vehicle2 : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
                if (vehicle.getVin().equals(vehicle2.getVin())) {
                    vinCount++;
                }
            }//END FOR
            if (vinCount >= 2) {
                System.out.println("FOUND DUPLICATE VINS CHANGING ONE OF THEM. IF YOU DO NOT WANT, CHANGE THE isDuplicateVINCheck() FLAG");
                vehicle.setVin(VINHelper.getRandomVIN().getVin());
                vinCount = 0;
            }//END IF
        }//END FOR
    }//END setupPolicyObject(List<CommercialAutoUWIssues> caUWIssuesList)


    private void updatePolicy() throws Exception {
        SideMenuPC sidemenu = new SideMenuPC(driver);

        if (vehicles) {
            sidemenu.clickSideMenuCAVehicles();
            GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
            vehiclesPage.selectAll();
            vehiclesPage.clickRemoveVehicle();
            for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
                if (vehiclesPage.finds(By.xpath("//div[contains(text(), '" + vehicle.getVin() + "')]")).isEmpty()) {
                    System.out.println("Creating Vehicle: " + vehicle.getVehicleName());
                    vehiclesPage.createNewVehicleCPP(true, vehicle);
                }
            }//END FOR
        }//END VEHICLES IF

        if (commAutoLine) {
            sidemenu.clickSideMenuCACommercialAutoLine();
            GenericWorkorderCommercialAutoCommercialAutoLineCPP commauto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
            commauto.fillOutAllCommercialAutoLine_Change(myPolicyObjCPP);
        }//END IF COMM AUTO LINE

        if (drivers) {
            sidemenu.clickSideMenuCADrivers();
            GenericWorkorderCommercialAutoDriver driversPage = new GenericWorkorderCommercialAutoDriver(driver);
            for (Contact driver : myPolicyObjCPP.commercialAutoCPP.getDriversList()) {
                if (driversPage.finds(By.xpath("//div[contains(text(), '" + driver.getDriversLicenseNum() + "')]")).isEmpty()) {
                    System.out.println("Adding Driver: " + driver.getDriversLicenseNum());
                    driversPage.addDriver(driver);
                }
            }
        }//DRIVERS

        if (stateInfo) {
            sidemenu.clickSideMenuCAStateInfo();
            GenericWorkorderCommercialAutoStateInfoCPP stateInfoPage = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
            stateInfoPage.fillOutCommercialAutoStateInfo(myPolicyObjCPP);
        }//END STATEINFO

        if (qualifications) {

        }//END QUALIFICATIONS
    }


    private String getUWIssueString(CommercialAutoUWIssues uwIssue) {
        switch (uwIssue) {
            case Pleaseprovideunderwritingwithcopiesofleaseagreementsifnotafinancialinstitution:
            case Mobilehomecontentscoveragedoesnotincludeofficeequipmentsalessamplestheftmoneyetc:
            case TruckersInsuranceforNon_truckinguseislimitedtosituationswheretheinsuredisnothaulingpropertyofothershaulingotherstrailersoroperatingunderaleaseagreementwithothersExplaintherestrictionsofcoveragetotheapplicant_insured:
            case ExclusionOrExcessCoverageHazardsOtherwiseInsuredCA9940hasbeenselectedProvideunderwritingdetailsastotheothercoverageinplaceandreasonthatWCisnotinsuringtheautos:
            case AudioVisualandDataElectronicEquipmentlimitsover10000requirepriorapprovalProvideUnderwritingalistofequipmentapproximatevaluesandthepurposeoftheequipmentforconsideration:
            case Pleasenotifytheapplicant_insuredthatnocoverageisprovidedforcontentsofthetrailerequippedwithlivingquartersorthemobilehomeIfcoverageisdesiredchooseCA2016MobileHomesContentsCoverage:
            case ProvideproofofrepairsandphotostounderwritinginordertoshowrepairshavebeenmadeforIDCA313006:
            case Informapplicant_insuredthatweareexcludingtheexistingdamage:
            case Applicant_insuredtransportsperishablerefrigeratedfoodproductsDescribecleaningandsanitationproceduresofthetrailerfromoneloadtotheother:
            case Applicant_insuredisajunkdealerPleasedescribenatureofitemstransportedtounderwritingtodetermineeligibility:
            case Applicant_insuredsautooperationisclassifiedasallotherPleasecontactunderwritingwithdetailregardingtheoperationforassistanceinrating:
            case ThereismorethanoneyouthfuldriverratedonthesamevehiclePleaseassignoneyouthfuloperatorpervehicle:
            case PleaseexplainwhytheFactoryCostNewvaluewaschangedfromitsoriginalvalue:
            case ThequestionIstrailerbeingtowedbyalighttruckismarkedyesHowevertherearenolighttrucksonthepolicyPleaseaddalighttruckorchangestheanswertono:
            case LongdistanceoptionissettoDenverProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
            case LongdistanceoptionissettoPortlandProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
            case LongdistanceoptionsettoSaltLakeCityProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
            case LongdistanceoptionissettoPacificProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
            case LongdistanceoptionissettoMountainProvideunderwritingwithnumberoftripsandproductstransportedReinsurancelimitationsmustbeconsideredandaddressed:
                return getVehicleUWIssueString(uwIssue);
            default:
                return uwIssue.getValue();
        }
    }


    private String getVehicleUWIssueString(CommercialAutoUWIssues uwIssue) {
        for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
            if (vehicle.getUwIssue() != null) {
                if (vehicle.getUwIssue().equals(uwIssue)) {
                    return "Vehicle Number: " + vehicle.getVehicleNumber() + ", " + vehicle.getModelYear() + ", " + vehicle.getMake() + ", " + vehicle.getModel() + ", " + vehicle.getVin() + ". " + uwIssue.getValue();
                }
            }
        }
        return null;
    }


}


























