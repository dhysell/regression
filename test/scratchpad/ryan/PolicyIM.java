package scratchpad.ryan;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.CommercialProperty.PropertyCoverages;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineCPP.InlandMarineCoveragePart;
import repository.gw.enums.InlandMarineCPP.InlandMarine_Cargo;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.BaileesCustomer;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.CPPCommercialProperty;
import repository.gw.generate.custom.CPPCommercialPropertyLine;
import repository.gw.generate.custom.CPPCommercialPropertyProperty;
import repository.gw.generate.custom.CPPCommercialProperty_Building;
import repository.gw.generate.custom.CPPCommercialProperty_Building_Coverages;
import repository.gw.generate.custom.CPPInlandMarine;
import repository.gw.generate.custom.CPPInlandMarineAccountsReceivable;
import repository.gw.generate.custom.CPPInlandMarineAccountsReceivable_LocationBlanket;
import repository.gw.generate.custom.CPPInlandMarineBaileesCustomers;
import repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers;
import repository.gw.generate.custom.CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket;
import repository.gw.generate.custom.CPPInlandMarineComputerSystems;
import repository.gw.generate.custom.CPPInlandMarineComputerSystems_LocationBlanket;
import repository.gw.generate.custom.CPPInlandMarineMiscellaneousArticles;
import repository.gw.generate.custom.CPPInlandMarineMiscellaneousArticles_ScheduledItem;
import repository.gw.generate.custom.CPPInlandMarineMotorTruckCargo;
import repository.gw.generate.custom.CPPInlandMarineMotorTruckCargo_ScheduledCargo;
import repository.gw.generate.custom.CPPInlandMarineSignsCM;
import repository.gw.generate.custom.CPPInlandMarineSignsCM_ScheduledSign;
import repository.gw.generate.custom.CPPInlandMarineTripTransit;
import repository.gw.generate.custom.CPPInlandMarineValuablePapers;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import scratchpad.jon.mine.PolicyXMLExporter;

public class PolicyIM extends BaseTest {

    GeneratePolicy myPolicyObj = null;
    boolean testFailed = false;
    String failureString = "";
    List<String> classList = new ArrayList<String>();

    @BeforeClass
    public void beforeClass() {

    }



    @SuppressWarnings("unused")
    private boolean check(String[] a, String[] b) {
        if (a[0] == b[0]) {
            return true;
        } else {

        }

        return false;
    }


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

        // LOCATIONS
        AddressInfo pniAddress = new AddressInfo(true);
        AddressInfo altAddress = new AddressInfo(true);
        ArrayList<PolicyLocation> locationsLists = new ArrayList<PolicyLocation>();
        PolicyLocation policyLocation = new PolicyLocation(pniAddress, true);
        PolicyLocation policyLocation2 = new PolicyLocation(altAddress, true);
        locationsLists.add(policyLocation);
        locationsLists.add(policyLocation2);
        ArrayList<PolicyLocationBuilding> plbs = new ArrayList<PolicyLocationBuilding>();
        plbs.add(new PolicyLocationBuilding());
        policyLocation.setBuildingList(plbs);

        //COMMERCIAL PROPERTY LINE
        CPPCommercialPropertyLine commercialPropertyLine = new CPPCommercialPropertyLine();
        CPPCommercialProperty commercialProperty = new CPPCommercialProperty();
        commercialProperty.setCommercialPropertyLine(commercialPropertyLine);

        List<CPPCommercialPropertyProperty> commercialPropertyList = new ArrayList<CPPCommercialPropertyProperty>();
        CPPCommercialProperty_Building_Coverages coverages = new CPPCommercialProperty_Building_Coverages(PropertyCoverages.BusinessPersonalPropertyCoverage);
        CPPCommercialProperty_Building cpBuilding = new CPPCommercialProperty_Building();
        List<CPPCommercialProperty_Building> cpBuildings = new ArrayList<CPPCommercialProperty_Building>();
        CPPCommercialPropertyProperty cpProperty1 = new CPPCommercialPropertyProperty();
        CPPCommercialPropertyProperty cpProperty2 = new CPPCommercialPropertyProperty();
        cpBuilding.setCoverages(coverages);
        cpBuildings.add(cpBuilding);
        //Property 1
        cpProperty1.setCPPCommercialProperty_Building_List(cpBuildings);
        cpProperty1.setAddress(pniAddress);
        commercialPropertyList.add(cpProperty1);
        //Property 2
        cpProperty2.setCPPCommercialProperty_Building_List(cpBuildings);
        cpProperty2.setAddress(altAddress);
        commercialPropertyList.add(cpProperty2);

        commercialProperty.setCommercialPropertyList(commercialPropertyList);

        //COMMERCIAL AUTO LINE
        CPPCommercialAuto commercialAuto = new CPPCommercialAuto();
        commercialAuto.setCommercialAutoLine(new CPPCommercialAutoLine());
        commercialAuto.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());

        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle();
        vehicle.setGaragedAt(locationsLists.get(0).getAddress());
        vehicleList.add(vehicle);
        commercialAuto.setVehicleList(vehicleList);

        ArrayList<Contact> personList = new ArrayList<Contact>();
        Contact person = new Contact();
        person.setGender(Gender.Male);
        person.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
        personList.add(person);
        commercialAuto.setDriversList(personList);

        //INLAND MARINE LINE
        List<InlandMarineCoveragePart> coverageParts = new ArrayList<>();
        coverageParts.add(InlandMarineCoveragePart.ContractorsEquipment_IH_00_68);
        coverageParts.add(InlandMarineCoveragePart.CommercialArticles_CM_00_20);
        coverageParts.add(InlandMarineCoveragePart.Exhibition_IH_00_92); // Has UW issues
        coverageParts.add(InlandMarineCoveragePart.AccountsReceivable_CM_00_66);
//		coverageParts.add(InlandMarineCoveragePart.MotorTruckCargo);
        coverageParts.add(InlandMarineCoveragePart.BaileesCustomers_IH_00_85);
        coverageParts.add(InlandMarineCoveragePart.CameraAndMusicalInstrumentDealers_CM_00_21);
//		coverageParts.add(InlandMarineCoveragePart.ComputerSystems_IH_00_75); //Has missing UW quesitons
        coverageParts.add(InlandMarineCoveragePart.Signs_CM_00_28);
        coverageParts.add(InlandMarineCoveragePart.Installation_IDCM_31_4073);
        coverageParts.add(InlandMarineCoveragePart.ValuablePapers_CM_00_67);
        coverageParts.add(InlandMarineCoveragePart.MiscellaneousArticles_IH_00_79);
        coverageParts.add(InlandMarineCoveragePart.TripTransit_IH_00_78);
        CPPInlandMarine cppInlandMarine = new CPPInlandMarine(coverageParts);

        // Add Accounts Receivable
        List<CPPInlandMarineAccountsReceivable_LocationBlanket> accountsReceivableLocationBlankets = new ArrayList<CPPInlandMarineAccountsReceivable_LocationBlanket>();
        accountsReceivableLocationBlankets.add(new CPPInlandMarineAccountsReceivable_LocationBlanket(commercialPropertyList.get(0)));
        accountsReceivableLocationBlankets.add(new CPPInlandMarineAccountsReceivable_LocationBlanket(commercialPropertyList.get(1)));
        cppInlandMarine.setAccountsReceivable(new CPPInlandMarineAccountsReceivable(accountsReceivableLocationBlankets));

        // Add Motor truck cargo
        List<CPPInlandMarineMotorTruckCargo_ScheduledCargo> listOfScheduledCargo = new ArrayList<>();
        listOfScheduledCargo.add(new CPPInlandMarineMotorTruckCargo_ScheduledCargo(vehicle));
        cppInlandMarine.setMotorTruckCargo(new CPPInlandMarineMotorTruckCargo(listOfScheduledCargo));

        // Add Bailees Customers
        List<BaileesCustomer> propertyAtYourPremises = new ArrayList<>();
        propertyAtYourPremises.add(new BaileesCustomer(commercialPropertyList.get(0), 1000));
        propertyAtYourPremises.add(new BaileesCustomer(commercialPropertyList.get(1), 2000));
        List<BaileesCustomer> propertyInStorageAtYourPremises = new ArrayList<>();
        propertyInStorageAtYourPremises.add(new BaileesCustomer(commercialPropertyList.get(0), 3000));
        propertyInStorageAtYourPremises.add(new BaileesCustomer(commercialPropertyList.get(1), 4000));
        List<BaileesCustomer> propertyAwayFromYourPremises = new ArrayList<>();
        propertyAwayFromYourPremises.add(new BaileesCustomer(commercialPropertyList.get(0), 5000));
        propertyAwayFromYourPremises.add(new BaileesCustomer(commercialPropertyList.get(1), 6000));

        cppInlandMarine.setBaileesCustomers(new CPPInlandMarineBaileesCustomers(propertyAtYourPremises, propertyInStorageAtYourPremises, propertyAwayFromYourPremises));

        // Add Cameras and Musical Instruments
        List<CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket> cameraMusicalLocationBlankets = new ArrayList<>();
        cameraMusicalLocationBlankets.add(new CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket(commercialPropertyList.get(0)));
        cameraMusicalLocationBlankets.add(new CPPInlandMarineCameraAndMusicalInstrumentDealers_LocationBlanket(commercialPropertyList.get(1)));
        cppInlandMarine.setCameraAndMusicalInstrumentDealers(new CPPInlandMarineCameraAndMusicalInstrumentDealers(cameraMusicalLocationBlankets));

        // Add Computer Systems
        List<CPPInlandMarineComputerSystems_LocationBlanket> computerSystemsLocationBlankets = new ArrayList<>();
        computerSystemsLocationBlankets.add(new CPPInlandMarineComputerSystems_LocationBlanket(commercialPropertyList.get(0)));
        computerSystemsLocationBlankets.add(new CPPInlandMarineComputerSystems_LocationBlanket(commercialPropertyList.get(1)));
        cppInlandMarine.setComputerSystems(new CPPInlandMarineComputerSystems(computerSystemsLocationBlankets));

        // Add Signs
        List<CPPInlandMarineSignsCM_ScheduledSign> scheduledSigns = new ArrayList<>();
        scheduledSigns.add(new CPPInlandMarineSignsCM_ScheduledSign(commercialPropertyList.get(0)));
        scheduledSigns.add(new CPPInlandMarineSignsCM_ScheduledSign(commercialPropertyList.get(1)));
        cppInlandMarine.setSigns(new CPPInlandMarineSignsCM(scheduledSigns));

        // Add Valuable Papers
        cppInlandMarine.setValuablePapers(new CPPInlandMarineValuablePapers(commercialPropertyList.get(0)));

        // Add Miscellaneous Articles
        List<CPPInlandMarineMiscellaneousArticles_ScheduledItem> miscArticlesScheduledItems = new ArrayList<CPPInlandMarineMiscellaneousArticles_ScheduledItem>();
        miscArticlesScheduledItems.add(new CPPInlandMarineMiscellaneousArticles_ScheduledItem());
        miscArticlesScheduledItems.add(new CPPInlandMarineMiscellaneousArticles_ScheduledItem());
        cppInlandMarine.setMiscellaneousArticles(new CPPInlandMarineMiscellaneousArticles(miscArticlesScheduledItems));

        // Add Trip Transit
        List<InlandMarine_Cargo> cargoList = new ArrayList<InlandMarine_Cargo>();
        cargoList.add(InlandMarine_Cargo.AgriculturalMachineryImplementsParts);
        cppInlandMarine.setTripTransit(new CPPInlandMarineTripTransit(cargoList));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        // GENERATE POLICY

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPInlandMarine(cppInlandMarine)
                .withCPPCommercialProperty(commercialProperty)
                .withLineSelection(LineSelection.InlandMarineLineCPP, LineSelection.CommercialPropertyLineCPP)
                .withPolicyLocations(locationsLists)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("IM Policy")
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
