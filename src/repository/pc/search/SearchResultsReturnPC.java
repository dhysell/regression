package repository.pc.search;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SearchResultsReturnPC {

    public SearchResultsReturnPC(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    private WebElement selectToClick;
    private boolean isFound;

    public WebElement getSelectToClick() {
        return selectToClick;
    }

    public void setSelectToClick(WebElement selectToClick) {
        this.selectToClick = selectToClick;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean isFound) {
        this.isFound = isFound;
    }

}
