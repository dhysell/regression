package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyScheduledItemType;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_PersonalProperty extends GenericWorkorderBuildings {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquireInlandMarine_PersonalProperty(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutPersonalProperty(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.PersonalProperty)) {

            sideMenu.clickSideMenuIMPersonalEquipment();

            for (PersonalProperty personalProperty : policy.squire.inlandMarine.personalProperty_PL_IM) {
                if (personalProperty != null) {
                    clickAdd();

                    setType(personalProperty.getType());
                    switch (personalProperty.getType()) {
                        case MedicalSuppliesAndEquipment:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_RecEquipmentOrWheelchair);
                            addMedicalSuppliesAndEquipment(personalProperty);
                            break;
                        case RefrigeratedMilk:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddRefrigeratedMilk);
                            addRefrigeratedMilkDetails(personalProperty.getLimit(), personalProperty.getDescription());
                            break;
                        case MilkContaminationAndRefrigeration:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddPersonalPropertyMilkContamination);
                            addRefrigeratedMilkDetails(personalProperty.getLimit(), personalProperty.getDescription());
                            break;
                        case SportingEquipment:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddSportingEquipment);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case GolfEquipment:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case PhotographicEquipment:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case ExteriorOrnaments:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case Collectibles:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case FineArts:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case Furs:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            setLimit(personalProperty.getLimit());
                            break;
                        case Jewelry:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddOutsidePersonalProperty);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case SaddlesAndTack:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case OfficeEquipment:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case RadioAntennae:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case RadioReceiversAndTransmitters:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case StereoEquipment:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case TailoringEquipment:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case TelephoneEquipment:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case VideoEquipment:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case Tools:
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case MedicalDevices:
                            setLimit(personalProperty.getLimit());
                            break;
                        case BeeContainers:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddBeeContainers);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        case BlanketRadios:
                            policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddBlanketRadios);
                            addLimitDeductible(personalProperty.getLimit(), personalProperty.getDeductible());
                            break;
                        default:
                            break;
                    }

                    if (personalProperty.getType() != PersonalPropertyType.RefrigeratedMilk && personalProperty.getType() != PersonalPropertyType.MilkContaminationAndRefrigeration && personalProperty.getType() != PersonalPropertyType.MedicalSuppliesAndEquipment) {
                        for (PersonalPropertyScheduledItem ppsi : personalProperty.getScheduledItems()) {
                            addScheduledItem(ppsi, personalProperty.getType());
                        }

                        if (personalProperty.getAdditionalInsureds() != null)
                            addAdditionalInsured(personalProperty.getAdditionalInsureds());

                    }
                    
                    clickOk();
                }
            }
        }
    }


    public void clickAdd() {
        super.clickAdd();
        
        long endtime = new Date().getTime() + 5000;
        boolean onPage = !finds(By.xpath("//span[contains(@class, 'PersonalEquipmentPopup:DetailsTab-btnInnerEl')]")).isEmpty();
        while (onPage && new Date().getTime() < endtime) {
            
            onPage = !finds(By.xpath("//span[contains(@class, 'PersonalEquipmentPopup:DetailsTab-btnInnerEl')]")).isEmpty();
        }
    }


    public void clickOk() {
        super.clickOK();
    }


//    public void clickAdd() {
//        super.clickAdd();
//    }


    public void addIMPersonalProperty(PersonalProperty personalProperty) {
        clickAdd();
        setDeductible(personalProperty.getDeductible());
        for (PersonalPropertyScheduledItem ppItem : personalProperty.getScheduledItems()) {
            clickAddScheduledItems();
            setDescription(ppItem.getDescription());
            setLimit(ppItem.getLimit());

            for (String addInsured : ppItem.getAdditionalInsureds()) {
                setNameAdditionalInsuredScheduledItem(addInsured);
            }
        }
        clickOk();
    }


    public Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//table[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Type-triggerWrap']");
    }


    public void setType(PersonalPropertyType type) {
        
        Guidewire8Select mySelect = select_Type();
        mySelect.selectByVisibleText(type.getValue());
    }


    public List<String> getTypeValues() {
        
        Guidewire8Select mySelect = select_Type();
        return mySelect.getList();
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PIMPersonalEquipmentAdditionalInsuredDV:AdditionalInsuredLV_tb:Add']")
    public WebElement button_AddAdditionalInsured;

    @FindBy(xpath = "//*[contains (@id, ':AdditionalInsuredLV') or contains(@id, ':PIMPersonalEquipmentAdditionalInsuredDV:AdditionalInsuredLV')]")
    public WebElement Table_AdditionalInsured;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;


    public void clickAddAdditionalInsured() {
        clickWhenClickable(button_AddAdditionalInsured);
    }

    @FindBy(xpath = "//div[@id='PersonalEquipmentPopup:PIMPersonalEquipmentAdditionalInsuredDV:AdditionalInsuredLV-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_NameAdditionalInsured;

    @FindBy(xpath = "//div[@id='PersonalEquipmentPopup:PIMPersonalEquipmentAdditionalInsuredDV:AdditionalInsuredLV']//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_NameAdditionalInsured;


    public void setNameAdditionalInsured(String name) {
        clickWhenClickable(div_NameAdditionalInsured);
        waitUntilElementIsClickable(editbox_NameAdditionalInsured);
        editbox_NameAdditionalInsured.sendKeys(name);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Year-inputEl']")
    public WebElement editbox_Year;


    public void setYear(int year) {
        waitUntilElementIsClickable(editbox_Year);
        editbox_Year.sendKeys(String.valueOf(year));
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Make-inputEl']")
    public WebElement editbox_Make;


    public void setMake(String make) {
        waitUntilElementIsClickable(editbox_Make);
        editbox_Make.sendKeys(make);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Model-inputEl']")
    public WebElement editbox_Model;


    public void setModel(String model) {
        waitUntilElementIsClickable(editbox_Model);
        editbox_Model.sendKeys(model);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Serial-inputEl']")
    public WebElement editbox_VinSerialNum;


    public void setVinSerialNum(String vinSerialNum) {
        waitUntilElementIsClickable(editbox_VinSerialNum);
        editbox_VinSerialNum.sendKeys(vinSerialNum);
    }

    @FindBy(xpath = "//input[contains(@id, ':Description-inputEl')]")
    public WebElement editbox_Description;


    public void setDescription(String description) {
        waitUntilElementIsClickable(editbox_Description);
        editbox_Description.sendKeys(description);
        setText(editbox_Description, description);
        
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Limit-inputEl' or @id = 'PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Limit-inputEl']")
    public WebElement editbox_Limit;


    public void setLimit(int limit) {
        waitUntilElementIsClickable(editbox_Limit);
        editbox_Limit.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Limit.sendKeys(String.valueOf(limit));
    }

    public Guidewire8Select select_DeductibleMoney() {
        return new Guidewire8Select(driver, "//table[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:DeductibleMoney-triggerWrap']");
    }

    public Guidewire8Select select_DeductiblePercentage() {
        return new Guidewire8Select(driver, "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:DeductiblePercent-triggerWrap']");
    }


    public void setDeductible(PersonalPropertyDeductible deductible) {
        Guidewire8Select mySelect;
        if (deductible.isPercentage()) {
            mySelect = select_DeductiblePercentage();
        } else {
            mySelect = select_DeductibleMoney();
        }
        waitForPostBack();
        mySelect.selectByVisibleText(deductible.getValue());
    }

    public String getDeductible(){
    	return select_DeductibleMoney().getText();
    }

    public Guidewire8RadioButton radio_Inspected() {
        return new Guidewire8RadioButton(driver, "//table[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:Inspected']");
    }


    public void setInspected(boolean yes) {
        radio_Inspected().select(yes);
        
    }

    public Guidewire8RadioButton radio_ExistingDamage() {
        return new Guidewire8RadioButton(driver, "//table[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:ExistingDamage']");
    }


    public void setExistingDamage(boolean yes) {
        radio_ExistingDamage().select(yes);
        
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentDetailsDV:HasExistingDamage-inputEl']")
    public WebElement editbox_ExistingDamageDescription;


    public void setExistingDamageDescription(String description) {
        waitUntilElementIsClickable(editbox_ExistingDamageDescription);
        editbox_ExistingDamageDescription.sendKeys(description);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:Add']")
    public WebElement button_AddScheduledItems;


    public void clickAddScheduledItems() {
        clickWhenClickable(button_AddScheduledItems);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Year-inputEl']")
    public WebElement editbox_ScheduledItemYear;


    public void setScheduledItemYear(int year) {
        
        waitUntilElementIsClickable(editbox_ScheduledItemYear);
        editbox_ScheduledItemYear.sendKeys(String.valueOf(year));
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Make-inputEl']")
    public WebElement editbox_ScheduledItemMake;


    public void setScheduledItemMake(String make) {
        
        waitUntilElementIsClickable(editbox_ScheduledItemMake);
        editbox_ScheduledItemMake.sendKeys(make);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Model-inputEl']")
    public WebElement editbox_ScheduledItemModel;


    public void setScheduledItemModel(String model) {
        
        waitUntilElementIsClickable(editbox_ScheduledItemModel);
        editbox_ScheduledItemModel.sendKeys(model);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Serial-inputEl']")
    public WebElement editbox_ScheduledItemVinSerialNum;


    public void setScheduledItemVinSerialNum(String vinSerialNum) {
        waitUntilElementIsClickable(editbox_ScheduledItemVinSerialNum);
        editbox_ScheduledItemVinSerialNum.sendKeys(vinSerialNum);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Description-inputEl']")
    public WebElement editbox_ScheduledItemDescription;


    public void setScheduledItemDescription(String description) {
        clickWhenClickable(editbox_ScheduledItemDescription);
        editbox_ScheduledItemDescription.sendKeys(description);
    }

    @FindBy(xpath = "//input[contains(@id, 'PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Limit')]")
    public WebElement editbox_ScheduledItemLimit;


    public void setScheduledItemLimit(int limit) {
        waitUntilElementIsClickable(editbox_ScheduledItemLimit);
        
        editbox_ScheduledItemLimit.click();
        Actions actions = new Actions(driver);
        actions.sendKeys(String.valueOf(limit)).build().perform();
    }

    //Steve Modified		 //PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PIMPersEquipScheduleItemAdditionalInsuredDV:AdditionalInsuredLV_tb:Add-btnEl
    @FindBy(xpath = "//*[contains(@id,'PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PIMPersEquipScheduleItemAdditionalInsuredDV:AdditionalInsuredLV_tb:Add')]")
    public WebElement button_AddScheduledItemsAdditionalInsured;


    public void clickAddAdditionalInsuredScheduledItem() {
        clickWhenClickable(button_AddScheduledItemsAdditionalInsured);
    }

    @FindBy(xpath = "//div[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PIMPersEquipScheduleItemAdditionalInsuredDV:AdditionalInsuredLV-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_NameAdditionalInsuredScheduledItem;

    @FindBy(xpath = "//div[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PIMPersEquipScheduleItemAdditionalInsuredDV:AdditionalInsuredLV']//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_NameAdditionalInsuredScheduledItem;


    public void setNameAdditionalInsuredScheduledItem(String name) {
        clickWhenClickable(div_NameAdditionalInsuredScheduledItem);
        waitUntilElementIsClickable(editbox_NameAdditionalInsuredScheduledItem);
        editbox_NameAdditionalInsuredScheduledItem.sendKeys(name);
    }

    /***** This code was commented out because addresses of additional entities are required at quick quote
     Uncomment and comment the method below when addresses are not required at quick quote. *****/
//	
//	public void setAdditionalInterest(AdditionalInterest addInterest, Boolean retry, Boolean qqWorkorder) throws Exception {
//		super.setBuildingAdditionalInterests(addInterest, retry, qqWorkorder);
//	}
    public void setAdditionalInterest(boolean basicSearch, AdditionalInterest addInterest, Boolean retry) throws Exception {
        super.setBuildingAdditionalInterests(basicSearch, addInterest, retry);
    }

    private Guidewire8Select select_TypeScheduledItem() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:') and contains(@id, 'ItemType-triggerWrap')]");
    }


    public void setScheduledItemType(PersonalPropertyScheduledItemType type) {
        Guidewire8Select mySelect = select_TypeScheduledItem();
        mySelect.selectByVisibleText(type.getValue());
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:AppraisalDate-inputEl']")
    public WebElement editbox_ScheduledItemAppraisalDate;


    public void setScheduledItemAppraisalDate(Date date) {
        waitUntilElementIsClickable(editbox_ScheduledItemAppraisalDate);
        editbox_ScheduledItemAppraisalDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:Number-inputEl']")
    public WebElement editbox_ScheduledItemNumber;


    public void setScheduledItemNumber(int number) {
        
        waitUntilElementIsClickable(editbox_ScheduledItemNumber);
        editbox_ScheduledItemNumber.sendKeys(String.valueOf(number));
        editbox_ScheduledItemNumber.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//*[@id='PersonalEquipmentPopup:PersonalEquipmentScheduleItemsLDV:PersonalEquipmentScheduleItemDetailsDV:PhotoUploadDate-inputEl']")
    public WebElement editbox_ScheduledItemPhotoUploadDate;


    public void setScheduledItemPhotoUploadDate(Date date) {
        
        waitUntilElementIsClickable(editbox_ScheduledItemPhotoUploadDate);
        editbox_ScheduledItemPhotoUploadDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));

    }


    public void addMedicalSuppliesAndEquipment(PersonalProperty pp) {
        
        setYear(pp.getYear());
        setMake(pp.getMake());
        setModel(pp.getModel());
        
        setVinSerialNum(pp.getVinSerialNum());
        setLimit(pp.getLimit());
        setDeductible(pp.getDeductible());
        
        if (pp.getDescription() != null) {
            setDescription(pp.getDescription());
        }
    }


    public void addRefrigeratedMilkDetails(int limit, String description) {
        
        setLimit(limit);
        
        if (description != null) {
            setDescription(description);
        }
    }


    public void addAdditionalInsured(ArrayList<String> additionalInsureds) {
        for (String ai : additionalInsureds) {
            clickAddAdditionalInsured();
            setNameAdditionalInsured(ai);
        }
    }


    public void removeAdditionalInsuredByName(String additionalInsured) {
        int row = tableUtils.getRowNumberInTableByText(Table_AdditionalInsured, additionalInsured);
        tableUtils.setCheckboxInTable(Table_AdditionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(Table_AdditionalInsured, row, "Name");
    }


    public void addAdditionalInsuredScheduledItem(ArrayList<String> additionalInsureds) {
        if (additionalInsureds != null) {
            for (String ai : additionalInsureds) {
                clickAddAdditionalInsuredScheduledItem();
                setNameAdditionalInsuredScheduledItem(ai);
            }
        }
    }


    public void addScheduledItem(PersonalPropertyScheduledItem ppsi, PersonalPropertyType type) {
        clickAddScheduledItems();
        
        setScheduledItemDescription(ppsi.getDescription());

        switch (type) {
            case SportingEquipment:
                setScheduledItemType(ppsi.getType());
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null)
                    setScheduledItemMake(ppsi.getMake());

                setScheduledItemModel(ppsi.getModel());

                if (ppsi.getVinSerialNum() != null)
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                break;
            case PhotographicEquipment:
                setScheduledItemType(ppsi.getType());
                if (ppsi.getType() != PersonalPropertyScheduledItemType.Misc) {
                    setScheduledItemMake(ppsi.getMake());
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                break;
            case Collectibles:
                setScheduledItemAppraisalDate(ppsi.getAppraisalDate());
                break;
            case FineArts:
                setScheduledItemAppraisalDate(ppsi.getAppraisalDate());
                break;
            case Furs:
                setScheduledItemAppraisalDate(ppsi.getAppraisalDate());
                break;
            case Jewelry:
                setScheduledItemType(ppsi.getType());
                
                setScheduledItemAppraisalDate(ppsi.getAppraisalDate());
                setScheduledItemPhotoUploadDate(ppsi.getPhotoUploadDate());
                break;
            case BeeContainers:
                setScheduledItemNumber(ppsi.getNumber());
                break;
            case BlanketRadios:
                setScheduledItemType(ppsi.getType());
                sendArbitraryKeys(Keys.TAB);
                waitForPostBack();
                setScheduledItemNumber(ppsi.getNumber());
                break;
            case SaddlesAndTack:
                setScheduledItemType(ppsi.getType());
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case MedicalDevices:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case OfficeEquipment:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case RadioReceiversAndTransmitters:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case StereoEquipment:
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case TailoringEquipment:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case TelephoneEquipment:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            case VideoEquipment:
                if (ppsi.getYear() != 0) {
                    setScheduledItemYear(ppsi.getYear());
                }
                if (ppsi.getMake() != null) {
                    setScheduledItemMake(ppsi.getMake());
                }
                if (ppsi.getModel() != null) {
                    setScheduledItemModel(ppsi.getModel());
                }
                if (ppsi.getVinSerialNum() != null) {
                    setScheduledItemVinSerialNum(ppsi.getVinSerialNum());
                }
                break;
            default:
                break;
        }
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        setScheduledItemLimit(ppsi.getLimit());
        addAdditionalInsuredScheduledItem(ppsi.getAdditionalInsureds());
    }


    public void addLimitDeductible(int limit, PersonalPropertyDeductible deductible) {
        
        setLimit(limit);
        
        setDeductible(deductible);
    }


    public void clickEditButton() {
        super.clickEdit();
    }

}
