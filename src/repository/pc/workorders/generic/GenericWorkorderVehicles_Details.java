package repository.pc.workorders.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.idfbins.enums.State;
import services.broker.objects.vin.requestresponse.ValidateVINResponse;
import services.broker.services.vin.ServiceVINValidation;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.Vehicle.CommutingMiles;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.MileageFactor;
import repository.gw.enums.Vehicle.OccasionalDriverReason;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.Usage;
import repository.gw.enums.Vehicle.VehicleFieldsCPP;
import repository.gw.enums.Vehicle.VehicleTruckTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

public class GenericWorkorderVehicles_Details extends GenericWorkorderVehicles {

    private WebDriver driver;
    private TableUtils tableUtils;

    @FindBy(css = "input[id$=':AddressLine1-inputEl']")
    private WebElement input_AddressLineOne;

    @FindBy(css = "input[id$=':AddressLine2-inputEl']")
    private WebElement inputAddressLineTwo;

    @FindBy(css = "input[id$=':city-inputEl']")
    private WebElement input_City;

    private Guidewire8Select select_State() {
        return new Guidewire8Select(driver, "//table[contains(@id,':state')]");

    }

    @FindBy(css = "input[id$=':postalCode-inputEl']")
    private WebElement input_Zip;

    private void setAddress(String addressLineOne) {
        setText(input_AddressLineOne, addressLineOne);
    }

    private void setCity(String city) {
        setText(input_City, city);
    }

    private void setState(State state) {
        select_State().selectByVisibleText(state.getName());
    }

    private void setZip(String zip) {
        setText(input_Zip, zip);
    }

    private void setAddressLine2(String addressLine2) {
        setText(inputAddressLineTwo, addressLine2);
    }


    public GenericWorkorderVehicles_Details(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutVehicleDetails_QQ(boolean basicSearch, Vehicle vehicle) throws Exception {
        fillOutBasicVehicleInformation_QQ(vehicle);
        fillOutPersonalAutoAdditionalInterest(basicSearch, vehicle);
        fillOutPersonalAutoAdditionalInsureds(vehicle);
    }

    public void fillOutVehicleDetails_FA(Vehicle vehicle) {
        fillOutBasicVehicleInformation_FA(vehicle);

    }

    public void fillOutVehicleDetails(boolean basicSearch, Vehicle vehicle) throws Exception {
        fillOutBasicVehicleInformation(vehicle);
        fillOutPersonalAutoAdditionalInterest(basicSearch, vehicle);
        fillOutPersonalAutoAdditionalInsureds(vehicle);
    }


    public void fillOutBasicVehicleInformation_QQ(Vehicle vehicle) {
        selectVehicleType(vehicle.getVehicleTypePL());

        if (vehicle.hasComprehensive()) {
			clickVehicleCoveragesTab();
			GenericWorkorderVehicles_CoverageDetails pcPAAdditionalCoveragesTab = new GenericWorkorderVehicles_CoverageDetails(driver);
			pcPAAdditionalCoveragesTab.setComprehensive(true);
			clickVehicleDetailsTab();
            if (Objects.equals(vehicle.getVin(), "make new vin")) {
                setRandomVin();
            } else {
                setVIN(vehicle.getVin());
            }
            if (vehicle.getVehicleTypePL() != VehicleTypePL.ShowCar) {
                setFactoryCostNew(vehicle.getCostNew());
            }
        }

        // @editor ecoleman
        if (editbox_VIN.isDisplayed()) {
            if (Objects.equals(vehicle.getVin(), "make new vin")) {
                setRandomVin();
            } else {
                setVIN(vehicle.getVin());
            }
        }

        if(editbox_ModelYear.getAttribute("value").equals("")) {
        	setModelYear(vehicle.getModelYear());
        } else {
        	vehicle.setModelYear(Integer.valueOf(editbox_ModelYear.getAttribute("value")));
        }
        
        if(editbox_Make.getAttribute("value").equals("")) {
        	setMake(vehicle.getMake());
        } else {
        	vehicle.setMake(editbox_Make.getAttribute("value"));
        }
        
        if(editbox_Model.getAttribute("value").equals("")) {
        	setModel(vehicle.getModel());
        } else {
        	vehicle.setModel(editbox_Model.getAttribute("value"));
        }
        
        boolean addressFound = false;
        if (vehicle.getGaragedAt() != null) {
        	addressFound = selectGaragedAt(vehicle.getGaragedAt());
        }
        
        if(!addressFound) {
        	if(vehicle.getGaragedAt() == null) { 
        		vehicle.setGaragedAt(new AddressInfo(true));
        	}
            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':PAGarageLocationInputMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
            }
            setAddress(vehicle.getGaragedAt().getLine1());
            
            setCity(vehicle.getGaragedAt().getCity());
            
            setState(vehicle.getGaragedAt().getState());
            
            setZip(vehicle.getGaragedAt().getZip());
            
            clickProductLogo();
        }


        switch (vehicle.getVehicleTypePL()) {
            case FarmTruck:
                selectMileageFactor(vehicle.getMileageFactor());
                selectUsage(vehicle.getUsage());
                
                selectTruckType(vehicle.getTruckType());
                break;
            case LocalService1Ton:
                break;
            case MotorHome:
                break;
            case Motorcycle:
                break;
            case PassengerPickup:
                selectCommunity(vehicle.getCommutingMiles());
                break;
            case PrivatePassenger:
                selectCommunity(vehicle.getCommutingMiles());
                break;
            case SemiTrailer:
                selectUsage(vehicle.getUsage());
                
                selectTruckType(vehicle.getTruckType());
                selectTrailer(TrailerTypePL.Semi);
                setFactoryCostNew(vehicle.getCostNew());
                break;
            case ShowCar:
                setStatedValue(vehicle.getStatedValue());
                break;
            case Trailer:
                if (vehicle.hasFireAndTheft() || vehicle.hasComprehensive()) {
                    setFactoryCostNew(vehicle.getCostNew());
                }
                break;
        }


//        if (vehicle.getGaragedAt() != null) {
//            selectGaragedAt(vehicle.getGaragedAt());
//        } else {
//            vehicle.setGaragedAt(new AddressInfo(true));
//            //set to new
//            Guidewire8Select mySelect = select_GaragedAt();
//            mySelect.selectByVisibleTextPartial("New");
//            new WaitUtils(driver).waitForPostBack();
//            setCity(vehicle.getGaragedAt().getCity());
//            new WaitUtils(driver).waitForPostBack();
//            setAddress(vehicle.getGaragedAt().getLine1());
//            new WaitUtils(driver).waitForPostBack();
//            setAddress(vehicle.getGaragedAt().getLine1());
//            new WaitUtils(driver).waitForPostBack();
//            setState(vehicle.getGaragedAt().getState());
//            new WaitUtils(driver).waitForPostBack();
//            setZip(vehicle.getGaragedAt().getZip());
//            new WaitUtils(driver).waitForPostBack();
//        }


        Contact driverName = vehicle.getDriverPL();

        if (!vehicle.getVehicleTypePL().equals(VehicleTypePL.ShowCar) && !vehicle.getVehicleTypePL().equals(VehicleTypePL.Trailer) && !vehicle.getVehicleTypePL().equals(VehicleTypePL.SemiTrailer) && !vehicle.getVehicleTypePL().equals(VehicleTypePL.FarmTruck)) {
			if (driverName == null || vehicle.isNoDriverAssigned() || select_DriverToAssign().getList().size() == 1) {
				switch (vehicle.getVehicleTypePL()) {
				case PrivatePassenger:
				case PassengerPickup:
				case Motorcycle:
					setNoDriverAssigned(true);
					break;
				default:
					break;
				}//end switch
			} else {
				switch (vehicle.getVehicleTypePL()) {
				case PrivatePassenger:
				case PassengerPickup:
				case Motorcycle:
					addDriver(driverName, DriverVehicleUsePL.Principal);
					break;
				case ShowCar:
					//				DriverVehicleUsePL vehicleUse = DriverVehicleUsePL.Occasional;
					//				addDriver(driverName, vehicleUse);
					break;
				default:
					break;
				}
			} 
		}

//		if (driverName != null) {
//			switch(vehicle.getVehicleTypePL()) {
//			case PrivatePassenger:
//			case PassengerPickup:
//			case Motorcycle:
//				addDriver(driverName, DriverVehicleUsePL.Principal);
//				break;
//			case ShowCar:
//				DriverVehicleUsePL vehicleUse = DriverVehicleUsePL.Occasional;
//				addDriver(driverName, vehicleUse);
//				break;
//			default:
//				break;
//			}
//		} else {
//			switch(vehicle.getVehicleTypePL()) {
//			case PrivatePassenger:
//			case PassengerPickup:
//			case Motorcycle:
//				setNoDriverAssigned(true);
//				break;
//			default:
//				break;
//			}//end switch
//		}//end else
    }//END fillOutBasicVehicleInformation_QQ(Vehicle vehicle)

    public void fillOutBasicVehicleInformation_FA(Vehicle vehicle) {

        if (Objects.equals(vehicle.getVin(), "make new vin")) {
            setRandomVin();
        } else {
            setVIN(vehicle.getVin());
        }

        
        vinAutoFill(vehicle);
        VehicleTypePL vehicleType = vehicle.getVehicleTypePL();

        // odometer
        if (vehicleType == VehicleTypePL.FarmTruck || vehicleType == VehicleTypePL.ShowCar) {
            setOdometer(vehicle.getOdometer());
        }

        // trailer type
        if (vehicleType == VehicleTypePL.Trailer) {
            selectTrailer(vehicle.getTrailerType());
        }
        
    }

    public void fillOutBasicVehicleInformation(Vehicle vehicle) {

    }


    public void fillOutPersonalAutoAdditionalInterest(boolean basicSearch, Vehicle vehicle) throws GuidewireNavigationException {
        for (AdditionalInterest additionalInterest : vehicle.getAdditionalInterest()) {
            addAutoAdditionalInterest(basicSearch, additionalInterest);
        }
    }

    public void fillOutPersonalAutoAdditionalInsureds(Vehicle vehicle) {

    }


    private Guidewire8Select select_VehicleType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'VehicleDV:Type-triggerWrap') or contains(@id, ':PersonalAuto_VehicleDV:Type_DV-triggerWrap')]");
    }

    public void selectVehicleType(VehicleTypePL vehicleType) {
        Guidewire8Select selectVehicleType = select_VehicleType();
        selectVehicleType.selectByVisibleText(vehicleType.getValue());
    }

    public void addAutoAdditionalInterest(boolean basicSearch, AdditionalInterest vehicleAI) throws GuidewireNavigationException {
        GenericWorkorderAdditionalInterests addInterest = new GenericWorkorderAdditionalInterests(driver);
        addInterest.fillOutAdditionalInterest(basicSearch, vehicleAI);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void setRandomVin() {
        Vin vin;
        boolean invalidVIN = true;

        while (invalidVIN) {
            try {
                vin = VINHelper.getRandomVIN();
                setVIN(vin.getVin());
            } catch (Exception e) {
            }
            
            String make = getMake();
            if (make != null && make != "") {
                invalidVIN = false;
            }
        }
    }

    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Vin-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Vin_DV-inputEl')]")
    private WebElement editbox_VIN;
    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Year-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Year_DV-inputEl')]")
    private WebElement editbox_ModelYear;

    public List<VehicleFieldsCPP> setVIN(String vin) {
        List<VehicleFieldsCPP> neededFields = new ArrayList<>();
        clickWhenClickable(editbox_VIN);
        
        editbox_VIN.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_VIN.sendKeys(vin);
        
        clickWhenClickable(editbox_ModelYear);
        
        return neededFields;
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:CostNew_DV-inputEl') or contains(@id, ':BAVehicleCV:VehicleDV:Cost-inputEl')]")
    private WebElement editbox_FactoryCostNew;

    public void setFactoryCostNew(double costNew) {
        
        clickWhenClickable(editbox_FactoryCostNew);
        editbox_FactoryCostNew.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_FactoryCostNew.sendKeys(String.valueOf(costNew));
    }

    public void setModelYear(int year) {
        
        clickWhenClickable(editbox_ModelYear);
        
        editbox_ModelYear.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_ModelYear.sendKeys(String.valueOf(year));
    }

    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Make-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Make_DV-inputEl')]")
    private WebElement editbox_Make;

    public void setMake(String make) {
        clickWhenClickable(editbox_Make);
        
        editbox_Make.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Make.sendKeys(make);
    }

    public String getMake() {
        return editbox_Make.getAttribute("value");
    }

    @FindBy(xpath = "//input[contains(@id, 'VehicleDV:Model-inputEl') or contains(@id, ':PersonalAuto_VehicleDV:Model_DV-inputEl')]")
    private WebElement editbox_Model;

    public void setModel(String model) {
        clickWhenClickable(editbox_Model);
        editbox_Model.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Model.sendKeys(model);
    }

    public String getModel() {
        return editbox_Model.getAttribute("value");
    }

    public boolean selectGaragedAt(AddressInfo address) {
    	String garagedAt = address.getLine1() + ", " + address.getCity() + ", " + address.getState().getAbbreviation();
        Guidewire8Select mySelect = select_GaragedAt();
        return mySelect.selectByVisibleTextPartialWithNoFail(garagedAt);
    }


    private Guidewire8Select select_MileageFactor() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:MileageFactor-triggerWrap')]");
    }

    public void selectMileageFactor(MileageFactor mileageFactor) {
        
        Guidewire8Select selectMileage = select_MileageFactor();
        selectMileage.selectByVisibleTextPartial(mileageFactor.getValue());
        
    }

    private Guidewire8Select select_Usage() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:UsageType-triggerWrap')]");
    }

    public void selectUsage(Usage farmUsage) {
        Guidewire8Select usage = select_Usage();
        usage.selectByVisibleText(farmUsage.getValue());
    }

    private Guidewire8Select select_TruckType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:TruckType-triggerWrap')]");
    }

    public void selectTruckType(VehicleTruckTypePL truckType) {
        Guidewire8Select truck = select_TruckType();
        truck.selectByVisibleText(truckType.getValue());
    }

    private Guidewire8Select select_CommutingMiles() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:commutemiles_DV-triggerWrap')]");
    }

    public void selectCommunity(CommutingMiles commuting) {
        Guidewire8Select selectCommutingMiles = select_CommutingMiles();
        selectCommutingMiles.selectByVisibleTextPartial(commuting.getValue());
    }

    private Guidewire8Select select_TrailerType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:TrailerType-triggerWrap')]");
    }

    public void selectTrailer(TrailerTypePL trailerType) {
        Guidewire8Select trailer = select_TrailerType();
        trailer.selectByVisibleText(trailerType.getValue());
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:StatedValue_DV-inputEl')]")
    private WebElement text_StatedValue;

    public void setStatedValue(double statedValue) {
        clickWhenClickable(text_StatedValue);
        text_StatedValue.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        text_StatedValue.sendKeys(String.valueOf(statedValue));
    }


    public void addDriver(Contact personDriver, DriverVehicleUsePL vehicleUse) {

        selectDriverToAssign(personDriver);

        int age = DateUtils.getDifferenceBetweenDates(personDriver.getDob(), DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateDifferenceOptions.Year);

        if (age < 16) {

            selectDriverVehicleUse(vehicleUse);
            if (vehicleUse.equals(DriverVehicleUsePL.Occasional)) {
                selectOcassionalDriverReason(OccasionalDriverReason.Other);
            }
        } else {

            if (checkIfElementExists("//table[contains(@id,'PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverVehicleUse-triggerWrap') or contains(@id, ':assignDriverVehicleUse-triggerWrap')]", 1000)) {
                selectDriverVehicleUse(vehicleUse);
            }
        }
    }

    private Guidewire8Select select_OcassionalDriverReason() {
        return new Guidewire8Select(driver, "//table[@id='PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverOccasionalDriverReason-triggerWrap']");
    }

    public void selectOcassionalDriverReason(OccasionalDriverReason reason) {
    	Guidewire8Select selectElement = select_OcassionalDriverReason();
        selectElement.selectByVisibleTextPartial(reason.toString());
    }

    private Guidewire8Select select_DriverVehicleUse() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverVehicleUse-triggerWrap') or contains(@id, ':assignDriverVehicleUse-triggerWrap')]");
    }

    public void selectDriverVehicleUse(DriverVehicleUsePL vehicleUse) {
        Guidewire8Select selectElement = select_DriverVehicleUse();
        selectElement.selectByVisibleTextPartial(vehicleUse.toString());
    }
    
    
    public boolean checkDriverVehicleUse(){
    	return checkIfElementExists("//table[contains(@id, ':assignDriverVehicleUse-triggerWrap')]", 1000);
    }

    private Guidewire8Select select_DriverToAssign() {
        return new Guidewire8Select(driver, "//table[contains(@id,'PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverPolicyDriver-triggerWrap')]");
    }

    public void selectDriverToAssign(Contact driverToAssign) {
        Guidewire8Select selectElement = select_DriverToAssign();
        selectElement.selectByVisibleTextPartial(driverToAssign.getLastName());
    }
/*	Once upon a time, the garaged at was going to change from a dropdown to text and a button.    
    @FindBy(xpath = "//div[contains(@id, ':PAGarageLocationInputReadOnly-inputEl')]")
    private WebElement text_GaragedAtValue; 
*/
    private Guidewire8Select select_GaragedAt() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'GarageLocationInput-triggerWrap')]");
    }

    private Guidewire8Checkbox checkbox_NoDriverAssigned() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:IsDriverAssigned') or contains (@id, ':PersonalAuto_AssignDriversDV:IsDriverAssigned')]");
    }

    public void setNoDriverAssigned(boolean trueFalse) {
        checkbox_NoDriverAssigned().select(trueFalse);
    }

    public void vinAutoFill(Vehicle vehicle) {

        if (getYear() == null | getYear().equals("")) {
            setModelYear(vehicle.getModelYear());
        } else {
            systemOut("setting Vehicle info Model Year: " + getYear());
            vehicle.setModelYear(Integer.valueOf(getYear()));
        }
        if (getMake() == null | getMake().equals("")) {
            setMake(vehicle.getMake());
        } else {
            systemOut("setting Vehicle info Make: " + getMake());
            vehicle.setMake(getMake());
        }
        if (getModel() == null | getModel().equals("")) {
            setModel(vehicle.getModel());
        } else {
            systemOut("setting Vehicle info Model: " + getModel());
            vehicle.setModel(getModel());
        }
        if (!finds(By.xpath("//input[contains(@id, 'VehicleDV:Cost-inputEl')]")).isEmpty()) {
            if (getFactoryCostNew() == null | getFactoryCostNew().equals("")) {
                setFactoryCostNew(vehicle.getCostNew());
            } else {
                if (vehicle.isOverrideFactoryCostNew()) {
                    setFactoryCostNew(vehicle.getCostNew());
                } else {
                    systemOut("setting Vehicle info FactoryCost New: " + getFactoryCostNew());
                    vehicle.setFactoryCostNew(Double.valueOf(getFactoryCostNew()));
                }
            }
        } 
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:Year_DV')]")
    private WebElement text_Year;

    public String getYear() {
        return text_Year.getAttribute("value");
    }

    private String getFactoryCostNew() {
        return editbox_FactoryCostNew.getAttribute("value");
    }

    public void setGaragedAt(AddressInfo address) {
    	Guidewire8Select mySelect = select_GaragedAt();
       	List<String> garagedAtList = mySelect.getList();
        boolean addressExists = false;
        for (String garagedAt : garagedAtList) {
            if (garagedAt.contains(address.getLine1())) {
                selectGaragedAt(address);
                addressExists = true;
            }
        }
        if (!addressExists) {

            List<WebElement> newButton = finds(By.xpath("//a[contains(@id, ':PAGarageLocationInputMenuIcon')]"));
            if (!newButton.isEmpty()) {
                clickWhenClickable(newButton.get(0));
                clickWhenClickable(find(By.xpath("//div[contains(@id, ':newAddressBuddy')]")));
            }

            
            setCity(address.getCity());
            
            setState(address.getState());
            
            setAddress(address.getLine1());
            
            setAddressLine2(address.getLine2());
            
            setZip(address.getZip());
            sendArbitraryKeys(Keys.TAB);
            waitForPostBack();
        }
    }

    public void selectGaragedAtZip(String address) {
        Guidewire8Select mySelect = select_GaragedAt();
        mySelect.selectByVisibleTextPartial(address);
    }

    public String getGaragedAt() {
        return select_GaragedAt().getText();
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:annualmiles_DV-inputEl')]")
    private WebElement text_Odometer;

    public void setOdometer(int odometer) {
        clickWhenClickable(text_Odometer);
        text_Odometer.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        text_Odometer.sendKeys(String.valueOf(odometer));
    }

    @FindBy(xpath = "//a[contains(@id, 'PAVehiclePopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton')]")
    private WebElement button_AdditionalInterests_AddExisting;
    @FindBy(xpath = "//a[contains(@id, 'PAVehiclePopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-itemEl')]")
    private WebElement link_AdditionalInterests_AddExistingOtherContacts;
    @FindBy(xpath = "//div[contains(@id, 'PAVehiclePopup:AdditionalInterestDetailsDV:AdditionalInterestLV_tb:AddContactsButton:AddOtherContact-arrowEl')]")
    private WebElement link_AdditionalInterests_AddExistingOtherContactsArrow;

    private Guidewire8Select AdditionalInterestInterestType() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'EditAdditionalInterestPopup:ContactDetailScreen:PolicyContactRoleDetailsCV:FBM_PolicyContactDetailsDV:InterestType-triggerWrap')]");
    }

    public void addExistingAutoAdditionalInterest(String contactName, String interestType) {
        
        hoverOverAndClick(button_AdditionalInterests_AddExisting);
        
        if (!checkIfElementExists(link_AdditionalInterests_AddExistingOtherContacts, 100)) {
            clickWhenClickable(button_AdditionalInterests_AddExisting, 1000);
        }
        hoverOverAndClick(link_AdditionalInterests_AddExistingOtherContacts);
        hoverOverAndClick(link_AdditionalInterests_AddExistingOtherContactsArrow);
        
        String xPath = "//span[contains(text(), '" + contactName + "')]/parent::a/parent::div";
        WebElement othercontact = find(By.xpath(xPath));
        clickWhenClickable(othercontact);
        
        Guidewire8Select newSelect = AdditionalInterestInterestType();
        newSelect.selectByVisibleTextPartial(interestType);
        clickUpdate();
    }

    public ValidateVINResponse setVINAndAlsoValidateDirectlyFromBrokerForComparison(String string)
            throws Exception {
        setVIN(string);

        // Configuration.setMbConnDetails(BrokerMQ.CHRIS);
        boolean printRequestXMLToConsole = true;
        boolean printResponseXMLToConsole = true;
        ServiceVINValidation testService = new ServiceVINValidation();
		ValidateVINResponse testResponse = testService.validateVIN2(testService.setUpTestValidateVINRequest(string),
                new GuidewireHelpers(driver).getMessageBrokerConnDetails(), printRequestXMLToConsole, printResponseXMLToConsole);

        return testResponse;
    }

//	public boolean isVinValid() {
//		if (topBannerExists()) {
//			if (getTopBanner().contains("VIN Validation failed")) {
//				return false;
//			}
//		}
//		return true;
//	}

    public void clearVIN() {
        editbox_VIN.clear();
    }

    public void addDriver(Contact personDriver) {
        addDriver(personDriver, DriverVehicleUsePL.Principal);
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:Vin_DV')]")
    private WebElement text_VIN;

    public String getVIN() {
        return text_VIN.getText();
    }


    public void setStatedValueWithSpecialEquipment(double statedValue, String desc) {
        setStatedValue(statedValue);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        setSpecialEquipment(true);
        clickWhenClickable(text_StatedValue);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        setSpecialEquipmentDescription(desc);
    }

    @FindBy(xpath = "//textarea[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:StatedValSpcEquipDesc_FBM-inputEl')]")
    private WebElement editbox_SpecialEquipmentDescription;

    private void setSpecialEquipmentDescription(String desc) {
        editbox_SpecialEquipmentDescription.sendKeys(desc);
    }

    private Guidewire8RadioButton radio_SpecialEquipment() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:LeaseOrRent_DV-containerEl')]/table");
    }

    private void setSpecialEquipment(boolean checked) {
        radio_SpecialEquipment().select(checked);
    }

    private Guidewire8Checkbox checkbox_NoVIN() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, ':NoVinCheckbox')]");
    }

    public void setNOVIN(boolean checked) {
        checkbox_NoVIN().select(checked);
        
    }

    @FindBy(xpath = "//input[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:GVW-inputEl')]")
    private WebElement editbox_GVW;

    public void setGVW(int gvwValue) {
        clickWhenClickable(editbox_GVW);
        
        editbox_GVW.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        
        editbox_GVW.sendKeys(Integer.toString(gvwValue));
        
    }

    public Vin createGenericVehicle(VehicleTypePL vehicleType) {

        Vin vin = null;
        boolean invalidVIN = true;

        while (invalidVIN) {
            try {
                vin = VINHelper.getRandomVIN();
                setVIN(vin.getVin());
            } catch (Exception e) {
            }
            String make = getMake();
            if (make != null && make != "") {
                invalidVIN = false;
            }
        }
        Vehicle vehicle = new Vehicle();
        vinAutoFill(vehicle);
        selectVehicleType(vehicleType);

        // commuting miles
        if (vehicleType == VehicleTypePL.PrivatePassenger || vehicleType == VehicleTypePL.PassengerPickup) {
            
            selectCommunity(CommutingMiles.WorkSchool10PlusMiles);
        }

        // cost new
        if (vehicleType == VehicleTypePL.Motorcycle || vehicleType == VehicleTypePL.SemiTrailer
                || vehicleType == VehicleTypePL.MotorHome || vehicleType == VehicleTypePL.Trailer
                || vehicleType == VehicleTypePL.FarmTruck || vehicleType == VehicleTypePL.LocalService1Ton) {
            setFactoryCostNew(1.0);
        }

        // odometer
        if (vehicleType == VehicleTypePL.FarmTruck || vehicleType == VehicleTypePL.ShowCar) {
            setOdometer(1000);
        }

        // mileage factor
        if (vehicleType == VehicleTypePL.FarmTruck) {
            selectMileageFactor(MileageFactor.From2500To7499);
        }

        // usage and truck type
        if (vehicleType == VehicleTypePL.FarmTruck || vehicleType == VehicleTypePL.SemiTrailer) {
            selectUsage(Usage.FarmUseMoreThanOccasionalHire);
            
            selectTruckType(VehicleTruckTypePL.TractorType);
        }

        // trailer type
        if (vehicleType == VehicleTypePL.SemiTrailer) {
            selectTrailer(TrailerTypePL.Semi);
        } else if (vehicleType == VehicleTypePL.Trailer) {
            selectTrailer(TrailerTypePL.Camper);
        }

        // stated value
        if (vehicleType == VehicleTypePL.ShowCar) {
            setStatedValue(100);
        }
        return vin;
    }

    private Guidewire8RadioButton radio_GoodStudentDiscount() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:goodStudentDiscount-containerEl')]/table");
    }

    public void setGoodStudentDiscountRadio(boolean truefalse) {
        radio_GoodStudentDiscount().select(truefalse);
    }


    private Guidewire8RadioButton radio_SeniorDiscount() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:seniorDiscount-containerEl')]/table");
    }

    public void setSeniorDiscountRadio(boolean truefalse) {
        radio_SeniorDiscount().select(truefalse);
    }

    private Guidewire8RadioButton radio_MotorcycleStarDiscount() {
        return new Guidewire8RadioButton(driver, "//div[contains(@id, 'PAVehiclePopup:PersonalAuto_VehicleDV:STARDiscount-containerEl')]/table");
    }

    public void setMotorCyclesStarDiscountRadio(boolean truefalse) {
        radio_MotorcycleStarDiscount().select(truefalse);
    }

    public boolean checkNoDiscountOptionExists() {
        try {
            boolean discountExists;
            discountExists = radio_SeniorDiscount().isDisplayed();
            if (!discountExists)
                discountExists = radio_GoodStudentDiscount().isDisplayed();
            return discountExists;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean checkMotorCyclesDiscountOptionExists() {
        try {
            radio_MotorcycleStarDiscount().select(true);
            
            radio_MotorcycleStarDiscount().select(false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkCommunityMilesUneditable() {
        try {
            return !select_CommutingMiles().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }


    public CommutingMiles getCommutingMiles() {
        try {
            String commutingMiles = select_CommutingMiles().getText();
            return commutingMiles != null ? CommutingMiles.valueOf(commutingMiles) : null;
        } catch (Exception e) {
            return null;
        }
    }


    public void selectDriverToAssign(String driverToAssign) {
        Guidewire8Select selectElement = select_DriverToAssign();
        selectElement.selectByVisibleTextPartial(driverToAssign);
    }

    public String getVehicleCommutingMiles() {
        
        return select_CommutingMiles().getText();
    }

    @FindBy(xpath = "//div[contains(@id, 'PAVehiclePopup:PersonalAuto_AssignDriversDV:assignDriverVehicleUse-inputEl')]")
    private WebElement text_VehicleUse;

    public String getVehicleUse() {
        return text_VehicleUse.getText();
    }

    @FindBy(xpath = "//div[contains(@id, ':PAVehicleAdditionalInsuredDV:AdditionalInsuredLV')]")
    private WebElement table_AdditionalInsured;
    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    private WebElement button_AditionalInsuredRemove;

    public void removeAutoAdditionalInsured(String name) {
        int row = tableUtils.getRowNumberInTableByText(table_AdditionalInsured, name);
        tableUtils.setCheckboxInTable(table_AdditionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }

    public void addAutoAdditionalInsured(String name) {
        clickAdditionalInsured();
        tableUtils.setValueForCellInsideTable(table_AdditionalInsured, 1, "Name", "LastName", name);
        
    }

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Add-btnEl')]")
    private WebElement button_additionalinsuredAddButton;

    private void clickAdditionalInsured() {
        clickWhenClickable(button_additionalinsuredAddButton);
        
    }

    @FindBy(xpath = "//div[contains(@id, 'AdditionalInterestDetailsDV:AdditionalInterestLV')]")
    private WebElement table_VehicleAdditionalInterests;

    public void clickVehicleAdditionalInterestByName(String name) {
        tableUtils.clickCellInTableByRowAndColumnName(table_VehicleAdditionalInterests, tableUtils.getRowNumberInTableByText(table_VehicleAdditionalInterests, name), "Name");
    }

    public boolean checkVehicleAdditionalInterestByName(String name) {
        return tableUtils.getRowNumberInTableByText(table_VehicleAdditionalInterests, name) > 0;
    }

    public void removeVehicleAdditionalInterestByName(String name) {
        tableUtils.setCheckboxInTable(table_VehicleAdditionalInterests, tableUtils.getRowNumberInTableByText(table_VehicleAdditionalInterests, name), true);
        clickRemove();
    }
    
    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(table_AdditionalInsured, row, "Name");
    }

    public String getVehicleType() {
        return select_VehicleType().getText();
    }
    
    public String getGarageAt() {
        return select_GaragedAt().getText();
    }

    public String getDriverToAssign() {
        return select_DriverToAssign().getText();
    }
}


















