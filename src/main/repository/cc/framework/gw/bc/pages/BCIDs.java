package repository.cc.framework.gw.bc.pages;

import org.openqa.selenium.By;
import repository.cc.framework.gw.element.Identifier;

public class BCIDs {
    public static class Login {
        public static final Identifier USER_NAME = new Identifier(By.id("Login:LoginScreen:LoginDV:username-inputEl"));
        public static final Identifier PASSWORD = new Identifier(By.id("Login:LoginScreen:LoginDV:password-inputEl"));
        public static final Identifier LOGIN_BUTTON = new Identifier(By.id("Login:LoginScreen:LoginDV:submit"));
    }
}
