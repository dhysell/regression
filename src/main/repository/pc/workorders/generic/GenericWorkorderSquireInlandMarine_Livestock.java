package repository.pc.workorders.generic;


import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockScheduledItemType;
import repository.gw.enums.LivestockType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Livestock;
import repository.gw.generate.custom.LivestockScheduledItem;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderSquireInlandMarine_Livestock extends BasePage {

    private WebDriver driver;
    private TableUtils tableUtils;
    
    public GenericWorkorderSquireInlandMarine_Livestock(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutLivestock(GeneratePolicy policy) throws Exception {
        repository.pc.sidemenu.SideMenuPC sideMenu = new SideMenuPC(driver);

        if (policy.squire.inlandMarine.inlandMarineCoverageSelection_PL_IM.contains(InlandMarine.Livestock)) {
            sideMenu.clickSideMenuIMLivestock();

            int livestockCounter = 0;
            for (Livestock yummyAnimal : policy.squire.inlandMarine.livestock_PL_IM) {
                if (yummyAnimal != null) {

                    repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_LivestockList livestockListPage = new GenericWorkorderSquireInlandMarine_LivestockList(getDriver());
                    livestockListPage.clickAdd();

                    setType(yummyAnimal.getType());
                    if (yummyAnimal.getType() == LivestockType.Livestock) {
                        policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddLivestock);
                        setDeductible(yummyAnimal.getDeductible());

                        for (LivestockScheduledItem lsi : yummyAnimal.getScheduledItems()) {
                            addScheduledItem(lsi);
                            livestockCounter++;
                        }

                    } else if (yummyAnimal.getType() == LivestockType.DeathOfLivestock) {
                        policy.policyForms.eventsHitDuringSubmissionCreation.add(DocFormEvents.PolicyCenter.Squire_IM_AddDeathOfLivestock);
                        for (LivestockScheduledItem lsi : yummyAnimal.getScheduledItems()) {
                            addScheduledItemForDeathOfLivestock(lsi.getType(), lsi.getNumOfHead());
                        }
                    }

                    //set additional insured
                    if (yummyAnimal.getAdditionalInsureds() != null) {
                        addAdditionalInsureds(yummyAnimal.getAdditionalInsureds());
                    }
                    sendArbitraryKeys(Keys.TAB);
                    waitForPostBack();
                    clickOk();
                }
            }

            if (livestockCounter > 0) {
                sideMenu.clickSideMenuSquireProperty();
                sideMenu.clickSideMenuSquirePropertyCoverages();

                repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(getDriver());
                coverages.clickSectionIICoveragesTab();

                repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages sectionII = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(getDriver());
                if(!checkIfElementExists("//div[contains(@id, 'CovPatternInputGroup-legendTitle') and contains(text(), 'Livestock')]", 2000)) {
                sectionII.addCoverages(new SectionIICoverages(SectionIICoveragesEnum.Livestock, 0, 0));
                }
                int horse = 0;
                int cow = 0;
                for (Livestock livestockType : policy.squire.inlandMarine.livestock_PL_IM) {
                    if (livestockType != null) {
                        for (LivestockScheduledItem livestockGroup : livestockType.getScheduledItems()) {
                            if (livestockGroup.getType().equals(LivestockScheduledItemType.Cow) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Bull) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Steer) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Heifer) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Calf)) {
                                cow++;
                            } else if (livestockGroup.getType().equals(LivestockScheduledItemType.Horse) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Alpaca) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Llama) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Donkey) ||
                                    livestockGroup.getType().equals(LivestockScheduledItemType.Mule)) {
                                horse++;
                            }
                        }
                    }
                }

                if (policy.squire.propertyAndLiability.squireFPP != null & (policy.squire.isCountry() || policy.squire.isFarmAndRanch())) {
                    int fppCount = 0;
                    fppCount = fppCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Cows);
                    fppCount = fppCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Bulls);
                    fppCount = fppCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Steers);
                    fppCount = fppCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Heifers);
                    fppCount = fppCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Calves);
                    cow = cow + fppCount;

                    int fpphorseCount = 0;
                    fpphorseCount = fpphorseCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Horses);
                    fpphorseCount = fpphorseCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Alpacas);
                    fpphorseCount = fpphorseCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Llamas);
                    fpphorseCount = fpphorseCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Donkeys);
                    fpphorseCount = fpphorseCount + policy.squire.propertyAndLiability.squireFPP.getTotalNumItemsPerSubType(FPPFarmPersonalPropertySubTypes.Mules);
                    horse = horse + fpphorseCount;
                }

                if (horse > 0) {
                    sectionII.setLivestockTypeAndQuantity(LivestockScheduledItemType.Horse, horse);
                }
                if (cow > 0) {
                    sectionII.setLivestockTypeAndQuantity(LivestockScheduledItemType.Cow, cow);
                }
            }
        }
    }


    public Guidewire8Select select_Type() {
        return new Guidewire8Select(driver, "//*[@id='LivestockPopup:LivestockDetailsDV:Type-triggerWrap']");
    }


    public void setType(LivestockType type) {
        
        Guidewire8Select mySelect = select_Type();
        mySelect.selectByVisibleText(type.getValue());
    }

    public Guidewire8Select select_Deductible() {
        return new Guidewire8Select(driver, "//*[@id='LivestockPopup:LivestockDetailsDV:Deductible-triggerWrap']");
    }


    public void setDeductible(LivestockDeductible deductible) {
        Guidewire8Select mySelect = select_Deductible();
        mySelect.selectByVisibleText(deductible.getValue());
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:LivestockScheduleItemLV_tb:Add']")
    public WebElement button_AddScheduledItems;


    public void clickAddScheduledItems() {
        clickWhenClickable(button_AddScheduledItems);
    }

    public Guidewire8Select select_ScheduledItemsType() {
        return new Guidewire8Select(driver, "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:ItemType-triggerWrap']");
    }


    public void setScheduledItemsType(LivestockScheduledItemType scheduleItemType) {
        Guidewire8Select mySelect = select_ScheduledItemsType();
        mySelect.selectByVisibleText(scheduleItemType.getSection4Value());
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Description-inputEl']")
    public WebElement editbox_Description;


    public void setDescription(String description) {
        waitUntilElementIsClickable(editbox_Description);
        editbox_Description.sendKeys(description);
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:TagIDinput-inputEl']")
    public WebElement editbox_TagIdNum;


    public void setTagIdNum(String tagIdNum) {
        waitUntilElementIsClickable(editbox_TagIdNum);
        editbox_TagIdNum.sendKeys(tagIdNum);
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:BrandInput-inputEl']")
    public WebElement editbox_Brand;


    public void clickBrand() {
        clickWhenClickable(editbox_Brand);
    }


    public void setBrand(String brand) {
        waitUntilElementIsClickable(editbox_Brand);
        editbox_Brand.sendKeys(brand);
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Breed-inputEl']")
    public WebElement editbox_Breed;


    public void setBreed(String breed) {
        waitUntilElementIsClickable(editbox_Breed);
        editbox_Breed.sendKeys(breed);
        
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Limit-inputEl']")
    public WebElement editbox_Limit;


    public void setLimit(int limit) {
        waitUntilElementIsClickable(editbox_Limit);
        editbox_Limit.sendKeys(String.valueOf(limit));
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:addlInsuredsLV_tb:Add']")
    public WebElement button_AddScheduledItemsAdditionalInsured;


    public void clickAddAdditionalInsuredScheduledItem() {
        clickWhenClickable(button_AddScheduledItemsAdditionalInsured);
    }

    @FindBy(xpath = "//div[@id='LivestockPopup:LivestockScheduleItemsLDV:addlInsuredsLV-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_NameAdditionalInsuredScheduledItem;

    @FindBy(xpath = "//div[@id='LivestockPopup:LivestockScheduleItemsLDV:addlInsuredsLV']//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_NameAdditionalInsuredScheduledItem;


    public void setNameAdditionalInsuredScheduledItem(String name) {
        clickWhenClickable(div_NameAdditionalInsuredScheduledItem);
        waitUntilElementIsClickable(editbox_NameAdditionalInsuredScheduledItem);
        editbox_NameAdditionalInsuredScheduledItem.sendKeys(name);
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:NumOfHead-inputEl']")
    public WebElement editbox_NumOfHead;


    public void setNumOfHead(int numOfHead) {
        waitUntilElementIsClickable(editbox_NumOfHead);
        editbox_NumOfHead.sendKeys(String.valueOf(numOfHead));
    }

    @FindBy(xpath = "//*[@id='LivestockPopup:PIMLivestockAdditionalInsuredDV:AdditionalInsuredLV_tb:Add']")
    public WebElement button_AddAdditionalInsured;


    public void clickAddAdditionalInsured() {
        clickWhenClickable(button_AddAdditionalInsured);
    }

    @FindBy(xpath = "//div[contains(@id, ':PIMLivestockAdditionalInsuredDV:AdditionalInsuredLV')]")
    public WebElement Table_AdditionalInsured;

    @FindBy(xpath = "//div[@id='LivestockPopup:PIMLivestockAdditionalInsuredDV:AdditionalInsuredLV-body']//table[contains(@id, 'gridview-')]//tr[last()]/td[2]/div")
    public WebElement div_NameAdditionalInsured;

    @FindBy(xpath = "//div[@id='LivestockPopup:PIMLivestockAdditionalInsuredDV:AdditionalInsuredLV']//table[contains(@id, 'textfield-')]//tr[last()]/td[2]/input")
    public WebElement editbox_NameAdditionalInsured;

    @FindBy(xpath = "//span[contains(@id, ':AdditionalInsuredLV_tb:Remove-btnEl')]")
    public WebElement button_AditionalInsuredRemove;


    public void setNameAdditionalInsured(String name) {
        clickWhenClickable(div_NameAdditionalInsured);
        
        waitUntilElementIsClickable(editbox_NameAdditionalInsured);
        editbox_NameAdditionalInsured.sendKeys(name);
    }


    public void removeAdditionalInsured(String name) {
        int row = tableUtils.getRowNumberInTableByText(Table_AdditionalInsured, name);
        tableUtils.setCheckboxInTable(Table_AdditionalInsured, row, true);
        clickWhenClickable(button_AditionalInsuredRemove);
        
    }


    public String getAdditionalInsuredNameByRowNumber(int row) {
        return tableUtils.getCellTextInTableByRowAndColumnName(Table_AdditionalInsured, row, "Name");
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockDetailsDV:ClubName-inputEl']")
    public WebElement editbox_4HLivestockClubName;


    public void setClubName(String clubName) {
        clickWhenClickable(editbox_4HLivestockClubName);
        editbox_4HLivestockClubName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HLivestockClubName.sendKeys(clubName);
        editbox_4HLivestockClubName.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:PurchaseDate-inputEl']")
    public WebElement editbox_4HScheduledItemPurchaseDate;


    public void setPurchaseDate(Date purchaseDate) {
        clickWhenClickable(editbox_4HScheduledItemPurchaseDate);
        editbox_4HScheduledItemPurchaseDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemPurchaseDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", purchaseDate));
        editbox_4HScheduledItemPurchaseDate.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:RegistrationID-inputEl']")
    public WebElement editbox_4HScheduledItemID;


    public void setRegistrationIDNumber(String id) {
        clickWhenClickable(editbox_4HScheduledItemID);
        editbox_4HScheduledItemID.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemID.sendKeys(id);
        editbox_4HScheduledItemID.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Birthdate-inputEl']")
    public WebElement editbox_4HScheduledItemBirthDate;


    public void setBirthDate(Date birthDate) {
        clickWhenClickable(editbox_4HScheduledItemBirthDate);
        
//		editbox_4HScheduledItemBirthDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemBirthDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", birthDate));
        editbox_4HScheduledItemBirthDate.sendKeys(Keys.TAB);
        
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Color-inputEl']")
    public WebElement editbox_4HScheduledItemColor;


    public void setColor(String color) {
        clickWhenClickable(editbox_4HScheduledItemColor);
        editbox_4HScheduledItemColor.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemColor.sendKeys(color);
        editbox_4HScheduledItemColor.sendKeys(Keys.TAB);
    }

    public Guidewire8Select select_Gender() {
        return new Guidewire8Select(driver, "//*[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Sex-triggerWrap']");
    }


    public void setSex(String gender) {
        
        select_Gender().selectByVisibleText(gender);
        
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:Weight-inputEl']")
    public WebElement editbox_4HScheduledItemWeight;


    public void setWeight(String weight) {
        clickWhenClickable(editbox_4HScheduledItemWeight);
        
        editbox_4HScheduledItemWeight.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemWeight.sendKeys(weight);
        editbox_4HScheduledItemWeight.sendKeys(Keys.TAB);
    }

    @FindBy(xpath = "//input[@id='LivestockPopup:LivestockScheduleItemsLDV:ItemDetails:LivestockScheduleItemDetailsDV:PurchasePrice-inputEl']")
    public WebElement editbox_4HScheduledItemPurchasePrice;


    public void setPurchasePrice(String price) {
        clickWhenClickable(editbox_4HScheduledItemPurchasePrice);
        editbox_4HScheduledItemPurchasePrice.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_4HScheduledItemPurchasePrice.sendKeys(price);
        editbox_4HScheduledItemPurchasePrice.sendKeys(Keys.TAB);
    }


    public void setIM4HLivestock(Livestock ls) {
        clickAdd();
        
        setDeductible(ls.getDeductible());
        setClubName(ls.getClubName());
        for (LivestockScheduledItem livestockScheduledItem : ls.getScheduledItems()) {
            clickAddScheduledItems();
            setScheduledItemsType(livestockScheduledItem.getType());
            setPurchaseDate(livestockScheduledItem.getPurchaseDate());
            setRegistrationIDNumber(livestockScheduledItem.getTagIdNum());
            setBreed(livestockScheduledItem.getBreed());
            setBirthDate(livestockScheduledItem.getBirthdate());
            setColor(livestockScheduledItem.getColor());
            setWeight(Integer.toString(livestockScheduledItem.getWeight()));
            setSex(livestockScheduledItem.getGender().getValue());
            setPurchasePrice(livestockScheduledItem.getPurchasePrice().toString());
            setLimit(livestockScheduledItem.getLimit());
        }
        clickOK();
    }


    public void clickOk() {
        super.clickOK();
    }


    public void addScheduledItem(LivestockScheduledItem lsi) {
        clickAddScheduledItems();
        
        setScheduledItemsType(lsi.getType());
        
        setDescription(lsi.getDescription());
        
        setLimit(lsi.getLimit());
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();

        if (lsi.getBreed() != null) {
            setBreed(lsi.getBreed());
            
        }
        if (lsi.getTagIdNum() != null) {
            setTagIdNum(lsi.getTagIdNum());
            
        }
        if (lsi.getBrand() != null) {
            setBrand(lsi.getBrand());
            
        }

        // set additional insured for the scheduled item
        if (lsi.getAdditionalInsureds() != null) {
            for (String ai : lsi.getAdditionalInsureds()) {
                clickAddAdditionalInsuredScheduledItem();
                setNameAdditionalInsuredScheduledItem(ai);
                
            }
        }

        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
    }


    public void addAdditionalInsureds(ArrayList<String> additionalInsuredList) {
        for (String ai : additionalInsuredList) {
            clickAddAdditionalInsured();
            
            //seems to be not waiting long enough for table to generate.
            setNameAdditionalInsured(ai);
            
        }

    }


    public void addScheduledItemForDeathOfLivestock(LivestockScheduledItemType type, int numOfHead) {
        clickAddScheduledItems();
        
        setScheduledItemsType(type);
/*		The defect that was filed to have the system accept a description was found to not be wanted to be fixed by the users.
		setDescription(description);
*/
        setNumOfHead(numOfHead);
        
    }


    public void clickEditButton() {
        super.clickEdit();
    }

    @FindBy(xpath = "//div[contains(@id,':LineWizardStepSet:IMWizardStepGroup:LivestockScreen:LivestockPanelSet:CoverableLV')]")
    public WebElement table_LivestockList;


    public void removeLivestockByType(LivestockType type) {
        tableUtils.setCheckboxInTable(table_LivestockList, tableUtils.getRowNumberInTableByText(table_LivestockList, type.getValue()), true);
        
        super.clickRemove();
    }

}
