package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommercialAutoLine.OtherCarCollision;
import repository.gw.enums.CommercialAutoLine.OtherCarComprehensive;
import repository.gw.enums.CommercialAutoLine.OtherCarUnderinsuredMotorist;
import repository.gw.enums.CommercialAutoLine.OtherCarUninsuredMotorist;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DriverType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsuredCPP;
import repository.gw.enums.StateInfo.Un_UnderInsuredMotoristLimit;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.BusinessUseClass;
import repository.gw.enums.Vehicle.CAVehicleAdditionalCoverages;
import repository.gw.enums.Vehicle.HowManyTimesAYearDoesApplicantTravelOver300Miles;
import repository.gw.enums.Vehicle.LongDistanceOptions;
import repository.gw.enums.Vehicle.Radius;
import repository.gw.enums.Vehicle.SeatingCapacity;
import repository.gw.enums.Vehicle.SecondaryClass;
import repository.gw.enums.Vehicle.SecondaryClassType;
import repository.gw.enums.Vehicle.SizeClass;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.enums.VehicleUse;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoGarageKeepers;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
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
import repository.pc.workorders.generic.GenericWorkorderGaragekeepersCoverageCPP;
import repository.pc.workorders.generic.GenericWorkorderQualification_CommercialAuto;
import repository.pc.workorders.generic.GenericWorkorderVehicles;

@QuarantineClass
public class CAValidationRules extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;
    private String failureString = "";
    private boolean testFailed = false;

    private WebDriver driver;

    // Create and Issue CPP policy
    @SuppressWarnings("serial")
    @Test()
    public void createQQPolicyCPP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);


//		//		Underwriters foo = Underwriters.getUnderwriterByCounty("gem");
//


        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
                this.setVehicleType(VehicleType.Trucks);
                this.setMake("Light Truck");
                this.setModel("Truck");
                this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                this.setRadius(Radius.Local0To100Miles);
                this.setBusinessUseClass(BusinessUseClass.ServiceUse);
                this.setSecondaryClassType(SecondaryClassType.Truckers);
                this.setSecondaryClass(SecondaryClass.CommonCarriers);
            }});

            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
                this.setVehicleType(VehicleType.PublicTransportation);
                this.setMake("Public Transportation");
                this.setModel("Bus");
                this.setBodyType(BodyType.ChurchBus);
                this.setRadius(Radius.Local0To100Miles);
                this.setSeatingCapacity(SeatingCapacity.OneToEight);
                this.setEquippedWithAMechanicalLift(false);
            }});
        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};


        // GENERATE POLICY
        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Validation Rules")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"createQQPolicyCPP"}, groups = {"QuickQuote"})
    public void validationsQuickQuoteUM_UIM_Limits() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAStateInfo();
        GenericWorkorderCommercialAutoStateInfoCPP stateInfo = new GenericWorkorderCommercialAutoStateInfoCPP(driver);
        stateInfo.checkUnderinsuredMotoristCA3118(true);
        stateInfo.checkUninsuredMotoristCA3115(true);
        stateInfo.selectUnderinsuredMotoristLimit(Un_UnderInsuredMotoristLimit.OneMillion1M);
        stateInfo.selectUninsuredMotoristLimit(Un_UnderInsuredMotoristLimit.OneMillion1M);
        GenericWorkorder genWO = new GenericWorkorder(driver);

        try {
            genWO.clickGenericWorkorderQuote();
            if (!genWO.finds(By.xpath("//span[contains(@id, 'MultiLine_QuoteScreen:ttlBar')]")).isEmpty()) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "USER COULD QUOTE WITH THESE validationsQuickQuoteUM_UIM_Limits");
            }
            if (!genWO.finds(By.xpath("//a[contains(text(), 'Return to State Info')]")).isEmpty()) {
                genWO.clickWhenClickable(By.xpath("//a[contains(text(), 'Return to State Info')]"));
            }
        } catch (Exception e) {
        }

        genWO.clickPolicyChangeNext();
        stateInfo = new GenericWorkorderCommercialAutoStateInfoCPP(driver);

        //These commented out lines are a test that should be using the generic error message logic in generic helpers.
		
		/*List<WebElement> validationErrors = stateInfo.getErrorMessages();
		if(validationErrors.size() <= 0) {
			throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObjCPP.accountNumber, "Limits for Underinsured Motorists Coverage cannot be greater than the CSL limits was NOT displayed");
		}
		boolean found = false;
		for(WebElement message : validationErrors) {
			if(message.getText().contains("Coverage cannot be greater than the CSL limits for Liability")) {
				found = true;
				break;
			}
		}*/

        stateInfo.selectUnderinsuredMotoristLimit(Un_UnderInsuredMotoristLimit.FiftyThousand50K);
        stateInfo.selectUninsuredMotoristLimit(Un_UnderInsuredMotoristLimit.FiftyThousand50K);
        genWO.clickGenericWorkorderSaveDraft();

		/*if(!found) {
			throw new GuidewirePolicyCenterException(getCurrentURL(), myPolicyObjCPP.accountNumber, "Limits for $ Coverage cannot be greater than the CSL limits was NOT displayed correctly.");
		}*/
    }


    @SuppressWarnings("serial")
    @Test(dependsOnMethods = {"createQQPolicyCPP"}, groups = {"QuickQuote"}, enabled = true)
    public void validationQQGarageKeepers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAGarageKeepers();

        final CPPCommercialAutoGarageKeepers garage = new CPPCommercialAutoGarageKeepers(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress()) {{
            this.setCollision(false);
        }};

        myPolicyObjCPP.commercialAutoCPP.setGarageKeepers(new ArrayList<CPPCommercialAutoGarageKeepers>() {{
            this.add(garage);
        }});

        GenericWorkorderGaragekeepersCoverageCPP garageKeepers = new GenericWorkorderGaragekeepersCoverageCPP(driver);
        garageKeepers.fillOutGaragekeepersCoverageLine(myPolicyObjCPP);
        GenericWorkorder genWO = new GenericWorkorder(driver);

        try {
            genWO.clickGenericWorkorderQuote();
            if (!genWO.finds(By.xpath("//span[contains(@id, 'MultiLine_QuoteScreen:ttlBar')]")).isEmpty()) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "USER COULD QUOTE WITH THESE validationQQGarageKeepers");
            }
        } catch (Exception e) {
        }

//		genWO.clickGenericWorkorderNext();
        genWO.clickGenericWorkorderQuote();

        List<WebElement> validationErrors = garageKeepers.getValidationMessages();
        System.out.println(String.valueOf(validationErrors.size()));
        if (validationErrors.size() <= 0) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "At least one of the following coverages must be selected: Collision, Comprehensive, and/or Specified Causes of Loss was NOT displayed");
        }
        boolean found = false;
        for (WebElement message : validationErrors) {
            System.out.println(message.getText());
            if (message.getText().contains("At least one of the following coverages must be selected")) {
                found = true;
                break;
            }
        }

        garageKeepers.selectAllGarageKeepers();
        garageKeepers.clickRemove();
        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

        if (!found) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "At least one of the following coverages must be selected: Collision, Comprehensive, and//or Specified Causes of Loss was NOT displayed");
        }

        myPolicyObjCPP.commercialAutoCPP.setGarageKeepers(new ArrayList<CPPCommercialAutoGarageKeepers>() {{
            this.remove(garage);
        }});
    }


    @Test(dependsOnMethods = {"createQQPolicyCPP"}, groups = {"QuickQuote"}, enabled = true)
    public void validationQualifications() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();

        GenericWorkorderQualification_CommercialAuto qual = new GenericWorkorderQualification_CommercialAuto(driver);
        qual.clickCA_ServiceCommision(true);

        qual.clickQualificationNext();

        List<WebElement> validationMessages = qual.getValidationMessages();
        boolean found = false;
        for (WebElement message : validationMessages) {
            if (message.getText().contains("At least 1 question associated with \"Are state public service commission filings required? If yes, check all that apply.\" is required")) {
                found = true;
                break;
            }
        }

        try {
            GenericWorkorder genWO = new GenericWorkorder(driver);
            genWO.clickGenericWorkorderQuote();
            if (!genWO.finds(By.xpath("//span[contains(@id, 'MultiLine_QuoteScreen:ttlBar')]")).isEmpty()) {
                Assert.fail(myPolicyObjCPP.accountNumber + "USER COULD QUOTE WITH THESE validationQualifications");
            }
        } catch (Exception e) {
        }

        qual.clickCA_ServiceCommision(false);
        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderSaveDraft();

        if (!found) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "did NOT get validation message: At least 1 question associated with \"Are state public service commission filings required? If yes, check all that apply.\" is required");
        }
    }

    @Test(dependsOnMethods = {"createQQPolicyCPP"}, groups = {"QuickQuote"}, enabled = true)
    public void testYouthFullDriver() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        final Date youthDOB = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -16);

        //add youthful driver
        Contact youthfulDriver = new Contact() {{
            this.setFirstName("YouthFull");
            this.setLastName("Driver");
            this.setGender(Gender.Male);
            this.setDob(driver, youthDOB);
            this.setMaritalStatus(MaritalStatus.Divorced);
            this.setDriversLicenseNum("as123456s");
            this.setExcludedDriver(false);
            this.setDriverType(DriverType.Primary);
            this.setVehicleUse(VehicleUse.NotDriventoWorkorSchool);
            this.setAge(driver, 16);
            this.setRelationshipToInsuredCPP(RelationshipToInsuredCPP.Child);
        }};

        myPolicyObjCPP.commercialAutoCPP.getDriversList().add(youthfulDriver);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCADrivers();

        GenericWorkorderCommercialAutoDriver drivers = new GenericWorkorderCommercialAutoDriver(driver);
        drivers.addDriver(youthfulDriver);
        for (Vehicle vehicle : myPolicyObjCPP.commercialAutoCPP.getVehicleList()) {
            if (vehicle.getVehicleType().equals(VehicleType.Trucks) && vehicle.getSizeClass().equals(SizeClass.LightTrucksGVWOf10000PoundsOrLess)) {
                drivers.setPrimaryVehicleDriven(vehicle.getVin());
                break;
            }
        }
        GenericWorkorder genWO = new GenericWorkorder(driver);
        genWO.clickGenericWorkorderQuote();

        List<WebElement> validationMessages = drivers.getValidationErrors();
        if (!validationMessages.get(0).getText().contains("A youthful driver is rated to a light truck please change the light truck to a private passenger")) {
            sideMenu.clickSideMenuCADrivers();
            guidewireHelpers.editPolicyTransaction();
            drivers.removeDriverByName("YouthFull", "Driver");
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "did NOT get validation message: A youthful driver is rated to a light truck please change the light truck to a private passenger");
        }

        sideMenu.clickSideMenuCADrivers();
        guidewireHelpers.editPolicyTransaction();
        //remove youthful driver
        drivers.removeDriverByName("YouthFull", "Driver");
        //wrapped in a try catch cus the test already passed.
        try {
            genWO.clickClear();
            genWO.clickGenericWorkorderSaveDraft();
        } catch (Exception e) {
        }

    }


    @Test(dependsOnMethods = {"createQQPolicyCPP"}, dependsOnGroups = {"QuickQuote"}, enabled = true)
    public void convertToFullApp() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObjCPP.convertTo(driver, GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"convertToFullApp"}, enabled = true)
    public void validationCarOtherDriver() throws Exception {
        List<String> otherCarCoverages = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                this.add("Comprehensive");
                this.add("Liability");
                this.add("Medical Payments");
                this.add("Underinsured Motorists");
                this.add("Uninsured Motorists");
                this.add("Collision");
            }
        };

        String randomCoverage = otherCarCoverages.get(NumberUtils.generateRandomNumberInt(0, otherCarCoverages.size() - 1));


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        //go to commercial auto line additional coverages
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();
        GenericWorkorderCommercialAutoCommercialAutoLineCPP comAutoLine = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        comAutoLine.clickAdditionalCoveragesTab();
        //select one of the other drivers coverages
        switch (randomCoverage) {
            case "Comprehensive":
                comAutoLine.checkHiredAutoComprehensive(true);
                comAutoLine.setOtherCarComp(OtherCarComprehensive.FiveHundred);
                break;
            case "Liability":
                comAutoLine.checkHiredAutoLiability(true);
                //nothing to set
                break;
            case "Medical Payments":
                comAutoLine.checkOtherCarMedicalPayments(true);
                //nothing to set
                break;
            case "Underinsured Motorists":
                comAutoLine.checkOtherCarUnderinsuredMotorist(true);
                comAutoLine.setOtherCarUnderinsuredMotorist(OtherCarUnderinsuredMotorist.FiftyThousand);
                break;
            case "Uninsured Motorists":
                comAutoLine.checkOtherCarUninsuredMotorist(true);
                comAutoLine.setOtherCarUninsuredMotorist(OtherCarUninsuredMotorist.FiftyThousand);
                break;
            case "Collision":
                comAutoLine.checkOtherCarCollision(true);
                comAutoLine.setOtherCarCollision(OtherCarCollision.FiveHundred);
                break;
        }
        //set UW Questions
        comAutoLine.clickUnderwriterQuestionsTab();
        comAutoLine.clickDoesIndividualAllowFamilyMembersToDriveCar(false);
        //click quote
        GenericWorkorder genWO = new GenericWorkorder(driver);
        genWO.clickGenericWorkorderQuote();
        //get validation errors
        List<WebElement> validationMessages = genWO.getValidationResultsList();
        if (!validationMessages.get(0).getText().contains("Drive Other Car - Named Individuals CA 99 10 must contain at least one schedule item")) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "did NOT get validation message: Drive Other Car - Named Individuals CA 99 10 must contain at least one schedule item");
        }
    }

    //jlarsen 5/31/2016
    //check the Block User validations on the UnderWriter Questions Tabs.
    @Test(dependsOnMethods = {"convertToFullApp"})
    public void validationBlockUserUnderWriterIssues() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP comauto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        GenericWorkorder genwo = new GenericWorkorder(driver);

        comauto.clickAdditionalCoveragesTab();

        comauto.checkMCS_90(true);

        comauto.clickUnderwriterQuestionsTab();
        comauto.clickDoesApplicantRequireLimitsInExcessOf1M(false);
        comauto.setStatesOfOpperation(State.Idaho);
        comauto.clickDoesApplicantInsuredHaveHazardousPlacardOntheirVehicles(false);
        comauto.clickDoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured(true);

        //validate questions
        //Does applicant/insured require limits in excess of $1,000,000?
        comauto.clickDoesApplicantRequireLimitsInExcessOf1M(true);
        validateErrorMessageCommercialAutoLine("Does applicant/insured require limits in excess of $1,000,000?", "Applicant/insured is hauling hazardous materials that requires limits over $1,000,000. Contact Brokerage for assistance.");
        comauto.clickDoesApplicantRequireLimitsInExcessOf1M(false);

        //Does applicant/insured have hazardous placard on their vehicles?
        comauto.clickDoesApplicantInsuredHaveHazardousPlacardOntheirVehicles(true);
        validateErrorMessageCommercialAutoLine("Does applicant/insured have hazardous placard on their vehicles?", "Applicant/insured has hazardous placards on their vehicles. This exposure is not acceptable. Contact brokerage.");
        comauto.clickDoesApplicantInsuredHaveHazardousPlacardOntheirVehicles(false);


        //Do we insure all owned and leased vehicles for the applicant/insured?
        comauto.clickDoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured(false);
        validateErrorMessageCommercialAutoLine("Do we insure all owned and leased vehicles for the applicant/insured?", "All owned and leased vehicles for applicant/insured are not insured with WC. MCS 90 is a blanket endorsement. Risk is not eligible.");
        comauto.clickDoWeInsureAllOwnedAndLeasedVehiclesForTheApplicantInsured(true);
//		Need to clear validation message after

        comauto.clickAdditionalCoveragesTab();
        comauto.checkMCS_90(false);

        //vehicles
        Vehicle myVehicle = new Vehicle() {{
            this.setVehicleType(VehicleType.PrivatePassenger);
            this.getCaVehicleAdditionalCoveragesList().add(CAVehicleAdditionalCoverages.MotorCarriersInsuranceForNonTruckingUseCA2309);
            this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
        }};

        sidemenu.clickSideMenuCAVehicles();
        GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
        vehiclesPage.fillOutVehicleDetailsCPP(myVehicle);
        vehiclesPage.clickVehicleCoveragesTab();
        vehiclesPage.fillOutVehiclesCoveragesCPP(myVehicle);
        vehiclesPage.clickVehicleAdditionalCoveragesTab();
        vehiclesPage.fillOutVehiclesAdditionalCoverages(true, myVehicle);
        vehiclesPage.clickVehicleExclusionsAndConditionsTab();
        vehiclesPage.fillOutVehiclesExclusionsAndConditions(myVehicle);
        vehiclesPage.clickVehicleUnderwritingQuestionsTab();
        vehiclesPage.fillOutVehiclesUnderwritingQuestions(false, myVehicle);

        //Does applicant/insured back haul or transport goods while not operating under the authority of another carrier?
        vehiclesPage.clickDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Does applicant/insured back haul or transport goods while not operating under the authority of another carrier?", "Applicant/insured back hauls or transport goods while not operating under the authority of another carrier. CA 23 09 is not available under these circumstances.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickDoesApplicantInsuredBackHaulOrTransportGoodsWhileNotOperatingUnderTheAuthorityOfAnotherCarrier(false);

        //Does the party whose authority under which the applicant/insured have coverage for the trucks and trailers shown on the policy?
        vehiclesPage.clickDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy(false);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Does the party whose authority under which the applicant/insured have coverage for the trucks and trailers shown on the policy?", "The party whose authority under which the applicant/insured does not have coverage for the trucks and trailers shown on the policy. CA 23 09 is not available under these circumstances.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickDoesThePartyWhoseAuthorityUnderWhichTheApplicantInsuredHaveCoverageForTheTrucksAndTrailersShownOnThePolicy(true);

        //Does the applicant/insured lease or operate their vehicle under another parties PUC authority?
        vehiclesPage.clickDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority(false);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Does the applicant/insured lease or operate their vehicle under another parties PUC authority?", "Applicant/insured does not operate under another party�s PUC authority- CA 23 09 is not available under these circumstances.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickDoesTheApplicantInsuredLeaseOrOperateTheirVehicleUnderAnotherPartiesPUCAuthority(true);

        //Does the applicant/insured lease the vehicle without a driver?
        vehiclesPage.clickDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver(true);
        vehiclesPage.clickOK();

        validateErrorMessageVehicle("Does the applicant/insured lease the vehicle without a driver?", "Applicant/insured leases the vehicle without a driver. Risk is not eligible contact brokerage.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickDoesTheApplicantInsuredLeaseTheVehicleWithoutADriver(false);

        genwo.clickGenericWorkorderCancel();

        myVehicle = new Vehicle() {{
            this.setVehicleType(VehicleType.PublicTransportation);
            this.setBodyType(BodyType.MotelCourtesyBus);
            this.setRadius(Radius.Intermediate101To300Miles);
            this.setSeatingCapacity(SeatingCapacity.OneToEight);
            this.setEquippedWithAMechanicalLift(false);
            this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
        }};

        sidemenu.clickSideMenuCAVehicles();
        vehiclesPage.fillOutVehicleDetailsCPP(myVehicle);
        vehiclesPage.clickVehicleCoveragesTab();
        vehiclesPage.fillOutVehiclesCoveragesCPP(myVehicle);
        vehiclesPage.clickVehicleAdditionalCoveragesTab();
        vehiclesPage.fillOutVehiclesAdditionalCoverages(true, myVehicle);
        vehiclesPage.clickVehicleExclusionsAndConditionsTab();
        vehiclesPage.fillOutVehiclesExclusionsAndConditions(myVehicle);
        vehiclesPage.clickVehicleUnderwritingQuestionsTab();
        vehiclesPage.fillOutVehiclesUnderwritingQuestions(false, myVehicle);

        //Is bus used to transport seasonal or migrant workers?
        vehiclesPage.clickIsBusUsedToTransportSeasonalOrMigrantWorkers(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Is bus used to transport seasonal or migrant workers?", "Applicant/Insured is in the business of transporting seasonal or migrant workers. Risk is not eligible contact brokerage.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickIsBusUsedToTransportSeasonalOrMigrantWorkers(false);


        //Is the vehicle being driven further than a 300 mile radius?
        vehiclesPage.clickUWQuestionVehicleBeingDrivenMoreThan300MileRadius(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Is the vehicle being driven more than a 300 mile radius?", "Public Transportation is not eligible for long distance. Please contact Brokerage for coverage");
        returnToVehiclesPage(myVehicle);

        vehiclesPage.clickUWQuestionVehicleBeingDrivenMoreThan300MileRadius(false);
        genwo.clickGenericWorkorderCancel();


        myVehicle = new Vehicle() {{
            this.setVehicleType(VehicleType.Trucks);
            this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
            this.setRadius(Radius.LongDistancesOver301Miles);
            this.setLongDistanceOptions(LongDistanceOptions.Mountain);
            this.setBusinessUseClass(BusinessUseClass.CommercialUse);
            this.setSecondaryClassType(SecondaryClassType.DumpAndTransitMix);
            this.setSecondaryClass(SecondaryClass.SandAndGravelOtherThanQuarrying);
            this.setHowManyTimesAYearDoesApplicantTravelOver300Miles(HowManyTimesAYearDoesApplicantTravelOver300Miles.OneToFour);
            this.setDoesApplicantInsuredHaulSandOrGravelForOthers(false);
            this.setGaragedAt(myPolicyObjCPP.commercialPackage.locationList.get(0).getAddress());
        }};

        sidemenu.clickSideMenuCAVehicles();
        vehiclesPage.fillOutVehicleDetailsCPP(myVehicle);
        vehiclesPage.clickVehicleCoveragesTab();
        vehiclesPage.fillOutVehiclesCoveragesCPP(myVehicle);
        vehiclesPage.clickVehicleAdditionalCoveragesTab();
        vehiclesPage.fillOutVehiclesAdditionalCoverages(true, myVehicle);
        vehiclesPage.clickVehicleExclusionsAndConditionsTab();
        vehiclesPage.fillOutVehiclesExclusionsAndConditions(myVehicle);
        vehiclesPage.clickVehicleUnderwritingQuestionsTab();
        vehiclesPage.fillOutVehiclesUnderwritingQuestions(false, myVehicle);

        //How many times a year does applicant/insured travel over 300 miles?
        vehiclesPage.selectHowManyTimesAYearDoesApplicantTravelOver300Miles(HowManyTimesAYearDoesApplicantTravelOver300Miles.OverFour);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("How many times a year does applicant/insured travel over 300 miles?", "Applicant/Insured travels over 300 miles more than 4 times per year. Please refer to underwriting for consideration.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.selectHowManyTimesAYearDoesApplicantTravelOver300Miles(HowManyTimesAYearDoesApplicantTravelOver300Miles.OneToFour);

        //Does Applicant/insured haul sand or gravel for others?
        vehiclesPage.clickDoesApplicantInsuredHaulSandOrGravelForOthers(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Does Applicant/insured haul sand or gravel for others?", "Applicant/insured hauls sand or gravel for others, please classify as a trucker and change the secondary class to Contract Carriers (Other than Chemical or Iron and Steel Haulers).");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickDoesApplicantInsuredHaulSandOrGravelForOthers(false);


        genwo.clickGenericWorkorderCancel();

        sidemenu.clickSideMenuCAVehicles();
        vehiclesPage.editVehicleByVin(myPolicyObjCPP.commercialAutoCPP.getVehicleList().get(0).getVin());
        vehiclesPage.clickVehicleUnderwritingQuestionsTab();

        vehiclesPage.clickUWQuestionIsTheVehicleUsedForTowing(true);
        vehiclesPage.clickUWQuestionIsTheVehicleUsedForPoliceRotation(false);
        vehiclesPage.clickUWQuestionDoTheyRepossessAutos(false);
        vehiclesPage.clickUWQuestionDoTheyAdvertiseAsATowingCompany(false);


        //Is the vehicle used for Police Rotation?
        vehiclesPage.clickUWQuestionIsTheVehicleUsedForPoliceRotation(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Is the vehicle used for Police Rotation?", "The vehicle is used for Police Rotation. The vehicle is not eligible for a Commercial Auto Policy, please contact Brokerage for coverage.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickUWQuestionIsTheVehicleUsedForPoliceRotation(false);

        //Do they repossess autos?
        vehiclesPage.clickUWQuestionDoTheyRepossessAutos(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("User when Do they repossess autos?", "The vehicle is used to repossess autos. The vehicle is not eligible for a Commercial Auto Policy, please contact Brokerage for coverage.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickUWQuestionDoTheyRepossessAutos(false);

        //Do they advertise as a towing company?
        vehiclesPage.clickUWQuestionDoTheyAdvertiseAsATowingCompany(true);
        vehiclesPage.clickOK();
        validateErrorMessageVehicle("Do they advertise as a towing company?", "Applicant/Insured advertises as a towing company, they are not eligible for a Commercial Auto Policy, please contact Brokerage for coverage.");
        returnToVehiclesPage(myVehicle);
        vehiclesPage.clickUWQuestionDoTheyAdvertiseAsATowingCompany(false);

        genwo.clickGenericWorkorderCancel();

        genwo.clickGenericWorkorderSaveDraft();

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureString);
        }
    }


    private void validateErrorMessageVehicle(String question, String errorMessage) {
        GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
        boolean found = false;
        for (WebElement message : vehiclesPage.getValidationMessages()) {
            if (message.getText().contains(errorMessage)) {
                found = true;
                break;
            }
        }
        if (!found) {
            testFailed = true;
            failureString = failureString + "Failed to Block User when " + question + " was answered incorrectly.";
        }
    }

    private void validateErrorMessageCommercialAutoLine(String question, String errorMessage) {
        GenericWorkorder genWO = new GenericWorkorder(driver);
        genWO.clickPolicyChangeNext();
        genWO.clickGenericWorkorderQuote();
        if (!genWO.getValidationResultsList().get(0).getText().equals(errorMessage)) {
            testFailed = true;
            failureString = failureString + "Failed to Block User when setting " + question + " was answered incorectly. \n";

            returnToCoveragesPage();
        }
    }

    private void returnToVehiclesPage(Vehicle myVehicle) {
        GenericWorkorderVehicles vehiclesPage = new GenericWorkorderVehicles(driver);
        if (!vehiclesPage.finds(By.xpath("//span[contains(@class, 'g-title') and (text()='Vehicles')]")).isEmpty()) {
            vehiclesPage.editVehicleByVin(myVehicle.getVin());
            vehiclesPage.clickVehicleUnderwritingQuestionsTab();
        }
    }

    private void returnToCoveragesPage() {
        SideMenuPC sidemenu = new SideMenuPC(driver);
        GenericWorkorderCommercialAutoCommercialAutoLineCPP comauto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        if (!comauto.finds(By.xpath("//span[contains(@class, 'g-title') and contains(text(), 'Garagekeepers Coverage')]")).isEmpty()) {
            sidemenu.clickSideMenuCACommercialAutoLine();
            comauto.clickUnderwriterQuestionsTab();
        }
    }


}




















