package repository.ab.contact;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsCommoditiesAB extends BasePage    {

    public ContactDetailsCommoditiesAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    private ArrayList<String> commodities = new ArrayList<>();

    protected WebElement checkboxElement(String keyword) {
        return find(By.xpath("//label[contains(@id, ':FBCommoditiesPanelSet:" + keyword + "-boxLabelEl')]/preceding-sibling::input"));
    }

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Seeds:')]")
    public List<WebElement> text_ContactDetailsCommoditiesSeeds;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Fruits:')]")
    public List<WebElement> text_ContactDetailsCommoditiesFruits;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Vegetables:')]")
    public List<WebElement> text_ContactDetailsCommoditiesVegetables;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Grains:')]")
    public List<WebElement> text_ContactDetailsCommoditiesGrainForage;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Livestock:')]")
    public List<WebElement> text_ContactDetailsCommoditiesLivestock;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Silvaculture:')]")
    public List<WebElement> text_ContactDetailsCommoditiesSilvaculture;

    @FindBy(xpath = "//div[contains(@id, 'ContactDetail:ABContactDetailScreen:FBCommoditiesPanelSet:Other:')]")
    public List<WebElement> text_ContactDetailsCommoditiesOther;

    public void clickUpdate() {
        super.clickUpdate();
    }

    //I did not see a way to ensure that the checkbox is checked. so here we go.
    public void setCheckbox(String keyword) {
        checkboxElement(keyword).click();
    }

    private void addCommodities(List<WebElement> commodities) {
        for (WebElement commodity : commodities) {
            if (!commodity.getText().contains("< none selected >")) {
                this.commodities.add(commodity.getText());
            }
        }
    }

    public ArrayList<String> getCommoditiesOnPage() {
        this.commodities.clear();
        addCommodities(text_ContactDetailsCommoditiesSeeds);
        addCommodities(text_ContactDetailsCommoditiesFruits);
        addCommodities(text_ContactDetailsCommoditiesVegetables);
        addCommodities(text_ContactDetailsCommoditiesGrainForage);
        addCommodities(text_ContactDetailsCommoditiesLivestock);
        addCommodities(text_ContactDetailsCommoditiesSilvaculture);
        addCommodities(text_ContactDetailsCommoditiesOther);
        return this.commodities;
    }
}