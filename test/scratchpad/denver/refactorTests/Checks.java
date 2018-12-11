package scratchpad.denver.refactorTests;

import repository.cc.framework.init.ClaimCenterBaseTest;
import repository.cc.framework.init.Environments;
import repository.cc.framework.utils.helpers.CCIDs;
import repository.cc.framework.utils.helpers.ElementLocator;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.NumberUtils;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @Author Denver Hysell
 * @Requirement //TODO :Defect or User Story Number - Short description of requirements
 * @RequirementsLink //TODO <a href="Rally Link"Link> Text</a>
 * @Description : //TODO Description of what the test is doing
 * @DATE 10/1/2018
 */

public class Checks extends ClaimCenterBaseTest {

    @BeforeMethod
    public void beforeMethod() {
        super.initDriverOn(Environments.DEV);
        super.init();
    }

    @Test(invocationCount = 1)
    public void systemCheck() {

        actions.login(ClaimsUsers.abatts);

        String claimNumber = actions.db.getRandomClaimForChecks();

        actions.navigator.navigateTo(CCIDs.Claim.Navigation.STEPS);
        actions.findElement(CCIDs.Claim.Elements.CLAIM_NUMBER).fill(claimNumber);
        actions.findElement(CCIDs.Claim.Elements.FIND_CLAIM).click();

        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.ACTIONS_BUTTON).click();
        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.CHECK).hover();
        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.REGULAR_PAYMENT).click();

        String insured = actions.screenGrab(CCIDs.Claim.Elements.INSURED_NAME);
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.NAME).select(insured);
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.TYPE).select("Other");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.REPORT_AS).select("Not reportable");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.CHECK_DELIVERY).select("Send");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.CHECK_TYPE).select("System Generated");

        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.NEXT_BUTTON).click();

        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.RESERVE_LINE).selectRandom();
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.PAYMENT_TYPE).selectRandom();
        List<ElementLocator> lineItems = actions.findTable(CCIDs.Claim.SystemCheckWizard.Elements.PAYMENT_LINE_ITEMS).getRows();
        lineItems.get(lineItems.size() - 1).getElement().findElement(By.cssSelector("td:nth-child(2)")).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.LINE_ITEM_TYPE).fill("Indemnity");
        lineItems.get(lineItems.size() - 1).getElement().findElement(By.cssSelector("td:nth-child(5)")).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.AMOUNT).fill(String.valueOf(NumberUtils.generateRandomNumberInt(1, 1000)));

        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.NEXT_BUTTON).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.FINISH_BUTTON).click();

        actions.navigator.navigateTo(CCIDs.Claim.Workplan.Navigation.STEPS);
        ElementLocator approvalLinkRow = actions.findTable(CCIDs.Claim.Workplan.Elements.ACTIVITIES_TABLE).findRowWithText("Review and approve new payment");
        approvalLinkRow.getElement().findElement(By.linkText("Review and approve new payment")).click();
        actions.findElement(CCIDs.Claim.Workplan.Elements.APPROVE_BUTTON).click();

        actions.logout();
        actions.getDriver().quit();
    }

    @Test(invocationCount = 1)
    public void systemCheckHoldForAdjuster() {

        actions.login(ClaimsUsers.abatts);

        String claimNumber = actions.db.getRandomClaimForChecks();

        actions.navigator.navigateTo(CCIDs.Claim.Navigation.STEPS);
        actions.findElement(CCIDs.Claim.Elements.CLAIM_NUMBER).fill(claimNumber);
        actions.findElement(CCIDs.Claim.Elements.FIND_CLAIM).click();

        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.ACTIONS_BUTTON).click();
        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.CHECK).hover();
        actions.findElement(CCIDs.Claim.ActionsMenu.Elements.REGULAR_PAYMENT).click();

        String insured = actions.screenGrab(CCIDs.Claim.Elements.INSURED_NAME);
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.NAME).select(insured);
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.TYPE).select("Other");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.REPORT_AS).select("Not reportable");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.CHECK_DELIVERY).select("Hold For Adjuster");
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.CHECK_TYPE).select("System Generated");

        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.NEXT_BUTTON).click();

        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.RESERVE_LINE).selectRandom();
        actions.findSelect(CCIDs.Claim.SystemCheckWizard.Elements.PAYMENT_TYPE).selectRandom();
        List<ElementLocator> lineItems = actions.findTable(CCIDs.Claim.SystemCheckWizard.Elements.PAYMENT_LINE_ITEMS).getRows();
        lineItems.get(lineItems.size() - 1).getElement().findElement(By.cssSelector("td:nth-child(2)")).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.LINE_ITEM_TYPE).fill("Indemnity");
        lineItems.get(lineItems.size() - 1).getElement().findElement(By.cssSelector("td:nth-child(5)")).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.AMOUNT).fill(String.valueOf(NumberUtils.generateRandomNumberInt(1, 1000)));

        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.NEXT_BUTTON).click();
        actions.findElement(CCIDs.Claim.SystemCheckWizard.Elements.FINISH_BUTTON).click();

        actions.navigator.navigateTo(CCIDs.Claim.Workplan.Navigation.STEPS);
        ElementLocator approvalLinkRow = actions.findTable(CCIDs.Claim.Workplan.Elements.ACTIVITIES_TABLE).findRowWithText("Review and approve new payment");
        approvalLinkRow.getElement().findElement(By.linkText("Review and approve new payment")).click();
        actions.findElement(CCIDs.Claim.Workplan.Elements.APPROVE_BUTTON).click();

        actions.logout();
        actions.getDriver().quit();
    }

}