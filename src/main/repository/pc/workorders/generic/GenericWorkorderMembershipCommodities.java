package repository.pc.workorders.generic;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import repository.gw.enums.Commodities;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Membership.AcresPlanted;

import java.util.List;

public class GenericWorkorderMembershipCommodities extends GenericWorkorder {

    public GenericWorkorderMembershipCommodities(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // elements
    private List<WebElement> editboxes_AcresPlanted(Commodities.AcresPlanted acresPlanted) {
        return finds(By.xpath("//label[contains(text(), '" + acresPlanted.getValue() + "')]/parent::td/following-sibling::td/input"));
    }

    //moethods    
    public void setAcresPlanted(Commodities.AcresPlanted acresPlanted, int value) {
        clickWhenClickable(editboxes_AcresPlanted(acresPlanted).get(0));
        editboxes_AcresPlanted(acresPlanted).get(0).sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editboxes_AcresPlanted(acresPlanted).get(0).sendKeys(String.valueOf(value));
        editboxes_AcresPlanted(acresPlanted).get(0).sendKeys(Keys.TAB);
        waitForPostBack();
    }

    public void setMembershipCommodities(GeneratePolicy policyObject) {
        for (AcresPlanted acrePlanted : policyObject.membership.getAcresPlantedList()) {
            setAcresPlanted(acrePlanted.getAcesPlanted(), acrePlanted.getNumberOfAcres());
        }
    }

}
