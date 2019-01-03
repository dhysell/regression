package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.generate.custom.PolicyLocationBuilding;

import java.util.Arrays;
import java.util.List;

public class GenericWorkorderBuildingsClassCodes extends BasePage {
	
	private WebDriver driver;

    public GenericWorkorderBuildingsClassCodes(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[contains(@class, 'message') and contains(text(), 'contact brokerage')]")
    private WebElement banner_ContactBrokerage;

    Guidewire8RadioButton radio_InsuredOperateBusiness() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':BOPBuilding_DetailsDV:InsOperBuilding')]");
    }

    Guidewire8RadioButton radio_SellsMilkProducts() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, ':BOPBuilding_DetailsDV:RawMilk')]");
    }

    Guidewire8RadioButton radio_TanningBeds() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:TanningBeds')]");
    }

    Guidewire8RadioButton radio_SprayTanning() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:SprayTan')]");
    }

    Guidewire8RadioButton radio_BeautySchool() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:BeautySchool')]");
    }

    Guidewire8RadioButton radio_SpecialEvents() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:MotelSpecialEvents')]");
    }

    Guidewire8RadioButton radio_RawMilk() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:RawMilk')]");
    }

    Guidewire8Checkbox checkbox_PlayArea() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PlayArea')]");
    }

    Guidewire8Checkbox checkbox_FlameingDishes() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:FlamingDish')]");
    }

    Guidewire8Checkbox checkbox_Entertainment() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Entertainment')]");
    }

    Guidewire8Checkbox checkbox_Stage() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Stage')]");
    }

    Guidewire8Checkbox checkbox_ValetParking() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ValetParking')]");
    }

    Guidewire8Checkbox checkbox_Grill() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Grill')]");
    }

    Guidewire8Checkbox checkbox_Deepfryer() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:DeepFatFryer')]");
    }

    Guidewire8Checkbox checkbox_NoneOfTheAbove() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ExposureNone')]");
    }

    Guidewire8RadioButton radio_AutoServiceAndSales() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:SalesServiceReparis')]");
    }

    Guidewire8RadioButton radio_CarWash() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:CarWash')]");
    }

    Guidewire8RadioButton radio_FireArms() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Firearms')]");
    }

    Guidewire8RadioButton radio_Propaine() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:PropaneFilling')]");
    }

    Guidewire8RadioButton radio_ShowerSleep() {
        return new Guidewire8RadioButton(driver, "//table[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:ShowerSleep')]");
    }


    @FindBy(xpath = "//input[contains(@id, ':BOPBuilding_DetailsDV:BasisAmount-inputEl')]")
    private WebElement editbox_PremiumBasisAmount;

    //switch to allow for using the same code for classCode and Classification

    public void handleBuildingClassCodeQuestions(PolicyLocationBuilding building) {
        systemOut("BUILDING CLASS CODE USED: " + building.getClassCode());
        List<String> supermarkets = Arrays.asList("54231", "54221", "54251", "54241");
        List<String> health = Arrays.asList("54127", "54136(2)");
        List<String> grocery = Arrays.asList("54321", "54331", "54341", "54351");
        List<String> fastFood = Arrays.asList("09001", "090021", "09031", "09051", "09071", "09091", "09111", "09131", "09151", "09161", "09181",  "09251", "09191", "09201", "09221", "09241", "69171");
        List<String> dairy = Arrays.asList("54516");
        List<String> convenience = Arrays.asList("09341", "09361", "09321", "09331", "09351", "54136(1)");
        List<String> motel = Arrays.asList("69151", "69171", "69161");
        List<String> massage = Arrays.asList("71952(3)");
        List<String> estheticians = Arrays.asList("71952(2)");
        List<String> beauty = Arrays.asList("71952(1)");
        List<String> barber = Arrays.asList("71332");
        List<String> limitedCooking = Arrays.asList("09041", "09061", "09081", "09101", "09121", "09141", "09171", "09211", "09231", "09261", "09011");
        List<String> casualDining = Arrays.asList("09611", "09621", "09631", "09641", "09651", "09661");
        List<String> fineDining = Arrays.asList("09421", "09441", "09431");

        String classCode = String.valueOf(building.getClassCode());

        //INSURED OPERATE THE BUSINESS - ALL

        if (finds(By.xpath("//table[contains(@id, ':BOPBuilding_DetailsDV:InsOperBuilding')]")).size() > 0) {
            radio_InsuredOperateBusiness().select(true);
            clickWhenClickable(find(By.xpath("//input[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:BOPBuilding_DetailsDV:Description-inputEl')]")));
        }
        
        if (building.getClassClassification().contains("Supermarkets") || supermarkets.contains(classCode) ||
                building.getClassClassification().contains("Health or Natural Food Stores") || health.contains(classCode) ||
                building.getClassClassification().contains("Grocery Stores") || grocery.contains(classCode) ||
//				building.getClassClassification().contains("Fast Food Restaurants") || fastFood.contains(classCode) ||
                building.getClassClassification().contains("Dairy Products") || dairy.contains(classCode) ||
                building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //SELLS RAW MILK
            setRadioSellMilkProducts(false);
        }
        
        if (building.getClassClassification().contains("Motels") || motel.contains(classCode)) {
            //NIGHT CLUB
            setRadiospecialevents(false);
        }
        
        if (building.getClassClassification().contains("Massage Therapy") || massage.contains(classCode) ||
                building.getClassClassification().contains("Estheticians") || estheticians.contains(classCode) ||
                building.getClassClassification().contains("Beauty Parlors") || beauty.contains(classCode) ||
                building.getClassClassification().contains("Barber Shops") || barber.contains(classCode)
                ) {
            //SPRAY TANNING
            setRadioSprayTanning(false);
        }
        
        if (building.getClassClassification().contains("Massage Therapy") || massage.contains(classCode) ||
                building.getClassClassification().contains("Estheticians") || estheticians.contains(classCode) ||
                building.getClassClassification().contains("Beauty Parlors") || beauty.contains(classCode) ||
                building.getClassClassification().contains("Barber Shops") || barber.contains(classCode)
                ) {
            //TANNING BEDS
            setRadioTanningBeds(false);
        }
        
        if (building.getClassClassification().contains("Limited Cooking Restaurants") || limitedCooking.contains(classCode) ||
                building.getClassClassification().contains("Fine Dining Restaurants") || fineDining.contains(classCode) ||
                building.getClassClassification().contains("Fast Food Restaurants") || fastFood.contains(classCode) ||
                building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode) ||
                building.getClassClassification().contains("Casual Dining Restaurants") || casualDining.contains(classCode)
                ) {
            if (!building.getClassClassification().contains("No Restaurant")) {
                //EXPOSURE CHECKBOXES
                setPlayArea(false);
                setFlamingDishes(false);
                setEntertainment(false);
                setStage(false);
                setValetParking(false);
                setDeepFrier(false);
                setNoneOfTheAbove(true);
            }

        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //AUTO SALES
            setRadioAutoSales(false);
        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //CAR WASH
            setRadioCarWash(false);
        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //SLEEPING FACILITIES
            setRadioSleepingFacilities(false);
        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //PROPANE FILLING
            setRadioPropaineFilling(false);
        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode)
                ) {
            //FIRE ARMS
            setRadioFireArms(false);
        }
        
        if (building.getClassClassification().contains("Beauty Parlors") || beauty.contains(classCode) ||
                building.getClassClassification().contains("Barber Shops") || barber.contains(classCode)
                ) {
            //BEAUTY SCHOOL
            setRadioBeautySchool(false);
        }
        
        if (building.getClassClassification().contains("Convenience Food Stores") || convenience.contains(classCode) ||
                building.getClassClassification().contains("Casual Dining Restaurants") || casualDining.contains(classCode) ||
                building.getClassClassification().contains("Fine Dining Restaurants") || fineDining.contains(classCode) ||
                building.getClassClassification().contains("Limited Cooking Restaurants") || limitedCooking.contains(classCode) ||
                building.getClassClassification().contains("Motels") || motel.contains(classCode) ||
                building.getClassClassification().contains("Fast Food Restaurants") || fastFood.contains(classCode)) {
            //ANNUAL GROSS RECEIPTS
            setPremiumBasisAmount("5000");
        }


    }

    public void setPlayArea(Boolean checked) {
        checkbox_PlayArea().select(checked);
    }

    public void setFlamingDishes(Boolean checked) {
        checkbox_FlameingDishes().select(checked);
    }

    public void setEntertainment(Boolean checked) {
        checkbox_Entertainment().select(checked);
    }

    public void setStage(Boolean checked) {
        checkbox_Stage().select(checked);
    }

    public void setValetParking(Boolean checked) {
        checkbox_ValetParking().select(checked);
    }

    public void setDeepFrier(Boolean checked) {
        checkbox_Deepfryer().select(checked);
    }

    public void setNoneOfTheAbove(Boolean checked) {
        checkbox_NoneOfTheAbove().select(checked);
    }


    public void setRadioBeautySchool(Boolean yesno) {
        radio_BeautySchool().select(yesno);
    }


    public void setRadioPropaineFilling(Boolean yesno) {
        radio_Propaine().select(yesno);
    }


    public void setRadioFireArms(Boolean yesno) {
        radio_FireArms().select(yesno);
    }


    public void setRadioAutoSales(Boolean yesno) {
        radio_AutoServiceAndSales().select(yesno);
    }


    public void setRadioCarWash(Boolean yesno) {
        radio_CarWash().select(yesno);
    }


    public void setRadioSleepingFacilities(Boolean yesno) {
        radio_ShowerSleep().select(yesno);
    }


    public void setRadioTanningBeds(Boolean yesno) {
        radio_TanningBeds().select(yesno);
    }


    public void setRadiospecialevents(Boolean yesno) {
        radio_SpecialEvents().select(yesno);
    }


    public void setRadioSprayTanning(Boolean yesno) {
        radio_SprayTanning().select(yesno);
    }


    public void setRadioInsuredOperateBusiness(Boolean yesNo) {
        radio_InsuredOperateBusiness().select(yesNo);
    }


    public void setRadioSellMilkProducts(Boolean yesNo) {
        radio_SellsMilkProducts().select(yesNo);
    }


    public void setPremiumBasisAmount(String amount) {
        editbox_PremiumBasisAmount.clear();
        editbox_PremiumBasisAmount.sendKeys(amount);
    }


}















