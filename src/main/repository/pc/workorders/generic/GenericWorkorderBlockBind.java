package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class GenericWorkorderBlockBind extends BasePage {

    public GenericWorkorderBlockBind(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, 'BOPBuildingPopup:BOPSingleBuildingDetailScreen:Update-btnEl')]")
    private WebElement buildingsOKButton;

    @FindBy(xpath = "//label[contains(text(), 'UW Issues that block binding')]")
    private String text_BlockBinding;

    @FindBy(xpath = "//span[contains(@id, 'UWBlockProgressIssuesPopup:IssuesScreen:DetailsButton-btnInnerEl')]")
    private WebElement button_Details;


    public Boolean hasBlockBinding() {
        return !text_BlockBinding.isEmpty();
    }


    public void clickDetails() {
        clickWhenClickable(button_Details);
    }


}
















