package repository.cc.framework.gw.pc;
import repository.cc.framework.gw.element.UIActions;
import repository.cc.framework.gw.pc.pages.PCIDs;

public class PCOperations {

    private UIActions interact;

    public PCOperations(UIActions uiActions) {
        this.interact = uiActions;
    }

    public void loginAs(String user, String password) {
        interact.withTexbox(PCIDs.Login.USER_NAME).fill(user);
        interact.withTexbox(PCIDs.Login.PASSWORD).fill(password);
        interact.withElement(PCIDs.Login.LOGIN_BUTTON).click();
    }
}
