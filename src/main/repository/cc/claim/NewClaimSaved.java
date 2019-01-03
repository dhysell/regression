package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ClaimsUsers;
import repository.gw.exception.GuidewireException;
import repository.gw.helpers.WaitUtils;

import java.util.ArrayList;
import java.util.List;


public class NewClaimSaved extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public NewClaimSaved(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//label[@id='NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:Header']")
    public WebElement label_ClaimNumber;

    @FindBy(xpath = "//div[@id='NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:AssignedUser-inputEl']")
    public WebElement label_AssignedUser;

    @FindBy(xpath = "//div[@id='NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:GoToClaim-inputEl']")
    public WebElement link_GoToClaim;

    // HELPERS
    // ==============================================================================

    public void validateAssignedUser(String usersFullName, String lossRouter) throws GuidewireException {

        String userRole = "";

        for (ClaimsUsers user : ClaimsUsers.values()) {

            String tempUser = user.getName();

            if (usersFullName.equalsIgnoreCase(tempUser)) {
                userRole = user.getRole();
            }
        }

        if (!userRole.equalsIgnoreCase("")) {
            if (lossRouter.contains("Minor") && userRole.equalsIgnoreCase("Adjuster")) {
                String messageString = "User " + usersFullName + " assigned to claim with loss router " + lossRouter
                        + ".  Please check routing.";
                throw new GuidewireException(driver.getCurrentUrl(), messageString);
            }
        }
    }

    public String assignedUser() {
        String rawString = label_AssignedUser.getText();
        String parsedString = rawString.replaceAll("Assigned User", "");
        try {
            parsedString = parsedString.replaceAll(" ext", "");
        } catch (Exception e) {
            // TODO: handle exception
        }
        String nameString = parsedString.replaceAll("[^A-Za-z ]", "").trim();

        return nameString;
    }

    public List<String> waitForNewClaimSaved(String lossRouter) throws GuidewireException {
        waitUntilElementIsNotVisible(By.xpath("//a[@id='FNOLWizard:Finish']"), 120);
        waitUntilElementIsClickable(label_ClaimNumber);
        String claimString = label_ClaimNumber.getText();
        String assignedUser = assignedUser();
        System.out.println(label_ClaimNumber.getText());
        System.out.println("Loss Router: " + lossRouter + "");
        System.out.println("Assigned User: " + assignedUser + "");
        System.out.println(" ");
        System.out.println("/////////////////////////////////////////");
        String claimNumber = claimString.replaceAll("[^0-9]", "");

        validateAssignedUser(assignedUser, lossRouter);

        List<String> claimSavedValues = new ArrayList<>();
        claimSavedValues.add(claimNumber);
        claimSavedValues.add(assignedUser);

        return claimSavedValues;
    }

    public void clickGoToClaimLink() {
        clickWhenClickable(link_GoToClaim);
    }
}
