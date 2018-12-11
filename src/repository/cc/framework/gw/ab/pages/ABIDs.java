package repository.cc.framework.gw.ab.pages;

import org.openqa.selenium.By;
import repository.cc.framework.gw.element.Identifier;

public class ABIDs {
    public static class Login {
        public static final Identifier USER_NAME = new Identifier(By.id("Login:LoginScreen:LoginDV:username-inputEl"));
        public static final Identifier PASSWORD = new Identifier(By.id("Login:LoginScreen:LoginDV:password-inputEl"));
        public static final Identifier LOG_IN = new Identifier(By.id("Login:LoginScreen:LoginDV:submit"));
    }

    public static class NavBar {
        public static final Identifier SEARCH = new Identifier(By.id("TabBar:SearchTab-btnInnerEl"));
    }

    public static class SideMenu {
        public static final Identifier PENDING_CHANGES = new Identifier(By.id("ABContacts:MenuLinks:ABContacts_PendingChangesGroup"));
        public static final Identifier SEARCH = new Identifier(By.id("ABContacts:MenuLinks:ABContacts_ABContactSearchesGroup"));
    }

    public static class AdvancedSearch {
        public static final Identifier NAME_LAST_NAME = new Identifier(By.id("ABContactSearch:ABContactSearchScreen:ContactSearchDV:FBMSearchInputSet:GlobalContactNameInputSet:Name-inputEl"));
        public static final Identifier SEARCH = new Identifier(By.id("ABContactSearch:ABContactSearchScreen:ContactSearchDV:Search"));
        public static final Identifier SEARCH_RESULTS = new Identifier(By.id("ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV-body"));
    }

    public static class CCPendingChanges {
        public static final Identifier CREATES = new Identifier(By.id("PendingChangesCC:PendingChangesScreen:CreatesCardTab-btnInnerEl"));

        public static class Creates {
            public static final Identifier PENDING_CHANGES = new Identifier(By.id("PendingChangesCC:PendingChangesScreen:PendingCreatesListDetailPanel:PendingContactCreateDetailedLV-body"));
            public static final Identifier APPROVE = new Identifier(By.id("PendingChangesCC:PendingChangesScreen:PendingCreatesListDetailPanel:PendingContactCreateDetailedLV_tb:PendingContactCreate_ApproveButton-btnInnerEl"));
            public static final Identifier OK = new Identifier(By.linkText("OK"));
        }
    }

    public static class Contact {
        public static final Identifier HISTORY_TAB = new Identifier(By.id("ContactDetail:ABContactDetailScreen:HistoryCardTab"));

        public static class History {
            public static final Identifier CONTACT_HISTORY = new Identifier(By.id("ContactDetail:ABContactDetailScreen:ContactHistoryLV-body"));
        }
    }
}
