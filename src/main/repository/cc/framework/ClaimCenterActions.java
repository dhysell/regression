package repository.cc.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import repository.cc.framework.cc.claim.ClaimOperations;
import repository.cc.framework.cc.fnol.AutoFNOL;
import repository.cc.framework.cc.fnol.PropertyFNOL;
import repository.cc.framework.cc.storage.CCStorage;
import repository.cc.framework.utils.helpers.*;
import repository.cc.topmenu.TopMenu;
import repository.gw.enums.ClaimsUsers;

import java.util.ArrayList;
import java.util.List;

public class ClaimCenterActions {

    public repository.cc.topmenu.TopMenu topMenu;
    public Navigator navigator;
    public ClaimCenterDBActions db;

    public AutoFNOL autoFNOL = null;
    public PropertyFNOL propertyFNOL = null;
    public ClaimOperations claim = new ClaimOperations(this);
    private WebDriver driver;
    private String env;

    public CCStorage storage = new CCStorage();

    public ClaimCenterActions(WebDriver driver, String env) {
        this.driver = driver;
        this.env = env;
        init();
    }

    public ClaimCenterActions(String env) {
        init();
    }

    private void init() {
        this.topMenu = new TopMenu(this.driver);
        this.navigator = new Navigator(this.driver);
        this.db = new ClaimCenterDBActions(this.env);
        autoFNOL = new AutoFNOL(this);
        propertyFNOL = new PropertyFNOL(this);
    }

    public void login(ClaimsUsers user) {
        this.findElement(CCIDs.Login.Elements.USER_NAME).fill(user.name());
        this.findElement(CCIDs.Login.Elements.PASSWORD).fill(user.getPassword());
        this.findElement(CCIDs.Login.Elements.LOG_IN).click();
    }

    public void logout() {
        this.findElement(CCIDs.NavBar.Elements.GEAR).click();
        this.findElement(CCIDs.NavBar.Elements.LOG_OUT).click();
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    /**
     * Locates the first table in the DOM given an ID.
     *
     * @param id Element ID that is for or contains the desired table.
     */

    public ElementLocator findTable(Identifier id) {
        ElementLocator locator = findElement(id);
        WebElement firstElement = locator.getElement();
        if (!firstElement.getTagName().equalsIgnoreCase("table")) {
            WebElement firstTable = firstElement.findElements(By.tagName("table")).get(0);
            return new ElementLocator(this.driver, firstTable);
        } else {
            return locator;
        }

    }

    public String screenGrab(Identifier grabID) {
        return findElement(grabID).getElement().getText();
    }

    public ElementLocator findSelect(Identifier id) {

        this.findElement(CCIDs.ESCAPE_CLICKER).click();

        List<ElementLocator> listElements = new ArrayList<>();

        ElementLocator selectBox = findElement(id);
        selectBox.click();

        findElement(CCIDs.LIST_OPTIONS).getElement().findElements(By.tagName("li")).forEach(listElement -> {
            listElements.add(new ElementLocator(this.driver, listElement));
        });
        return new ElementLocator(this.driver, listElements, selectBox.getElement());
    }

    public ElementLocator findElement(Identifier identifier) {
        ElementLocator elementLocator = new ElementLocator(this.driver, identifier);
        return elementLocator.getElement() != null ? elementLocator : null;
    }

    public ElementLocator findElement(By byReference, int waitInSeconds) {
        ElementLocator elementLocator = new ElementLocator(this.driver, byReference, waitInSeconds);
        return elementLocator.getElement() != null ? elementLocator : null;
    }


}
