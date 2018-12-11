package repository.ab.search;


import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ContactSubType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchAB extends BasePage {

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    private TableUtils tableUtils;

    public SearchAB(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//a[contains(@class, 'bigButton') and contains(text(), 'earch')]")
    private WebElement button_SearchSearch;

    @FindBy(xpath = "//a[contains(@class, 'bigButton') and contains(text(), 'eset')]")
	protected WebElement button_SearchReset;
    //ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV_tb:ClaimContacts_CreateNewContactButton-btnEl
    @FindBy(xpath = "//span[contains(@id, 'tb:ClaimContacts_CreateNewContactButton-btnEl') and not(@unselectable)]")
	protected WebElement button_SearchCreateNew;

    @FindBy(xpath = "//div[contains(@id,':ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewCompanyMenuItem')]")
    private WebElement link_SearchCreateNewCompany;

    @FindBy(xpath = "//div[contains(@id, ':ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewPersonMenuItem')]")
	protected WebElement link_SearchCreateNewPerson;

    @FindBy(xpath = "//div[contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/desendant::tbody/tr")
    private List<WebElement> text_SimpleSearchSearchResults;

    @FindBy(xpath = "//div[contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/descendant::table")
	protected WebElement table_SimpleSearchSearchResults;

    @FindBy(xpath = "//img[contains(@id, 'Minor')]")
    private WebElement img_SearchResultsMinor;

    @FindBy(css = "input[id*='GlobalContactNameInputSet:Name']")
	protected WebElement input_Name;

    @FindBy(css = "input[id$=':LastName-inputEl']")
	protected WebElement input_LastName;

    @FindBy(css = "input[id$=':FirstName-inputEl']")
    private WebElement input_FirstName;


    private WebElement button_SearchResultsDetails(String lastName, String agentNum) {
        return find(By.xpath("//a[contains(., '" + agentNum + "')]/ancestor::td/following-sibling::td/div/a[contains(., '" + lastName + "')]/ancestor::td/following-sibling::td/div/a[contains(., 'Details')]"));

    }


    private WebElement link_SearchResultsMinor(String firstName, String lastName, String address) {
        return find(By.xpath("//div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(., '" + lastName + "') and contains(., '" + firstName + "')]/ancestor::tr/td/div/span[contains(@id,'Minor')]"));

    }

    private WebElement link_Agent(String fullName) {
        return find(By.xpath("//a[. ='" + fullName + "']"));
    }

    protected List<WebElement> link_SearchResultsPerson(String firstName, String lastName, String address) {
        return finds(By.xpath("//div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(., '" + lastName + "') and contains(., '" + firstName + "')]"));
    }

    protected WebElement link_SearchResultsPersonNotAcctNum(String firstName, String lastName, String acctNum) {
        return find(By.xpath("//div[not(contains(., '" + acctNum + "'))]/parent::td/preceding-sibling::td/div/a[contains(., '" + lastName + "') and contains(., '" + firstName + "')]"));
    }

    protected WebElement link_SearchResultsCompany(String company, String address, String state) {
        return find(By.xpath("//div[contains(., '" + state + "')]/parent::td/preceding-sibling::td/div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(.,'" + company + "')]"));
    }

    private WebElement link_SimpleSearchResultsCompany(String company, String address) {
        return find(By.xpath("//div[contains(., '" + address + "')]/parent::td/parent::tr/preceding-sibling::tr/td/div/a[contains(.,'" + company + "')]"));
    }

    private WebElement link_SearchResultsCompanySelect(String company, String addressLine1) {
        return find(By.xpath("//div[contains(@id, 'ABContactSearchPopup:ABContactSearchScreen:FBContactSearchResultsLV-body')]//table//td[contains(., '" + addressLine1 + "')]/preceding-sibling::td/div/a[contains(., '" + company + "')]/ancestor::tr[1]/td//a[contains(., 'Select')]"));
    }

    protected WebElement link_SearchResultsPersonClickSelect(String firstName, String lastName, String address) {
        return find(By.xpath("//div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(., '" + lastName + ", " + firstName + "')]/../../../td/div/a[contains(., 'Select')]"));

    }

    private WebElement link_SearchResultsAccountClickSelect(String acct) {
        return find(By.xpath("//div[contains(., '" + acct + "')]/parent::td/preceding-sibling::td/div/a[contains(., 'Select')]"));

    }

    protected WebElement link_SearchResultsPersonClickSelect(String lastName, String firstName, String middleName, String address) {
        
        return find(By.xpath("//div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(., '" + lastName + ", " + firstName + "')]/../../../td/div/a[contains(., 'Select')]"));

    }

    private WebElement button_SearchResultsCompanyClickSelect(String role, String contactName) {
        
        return find(By.xpath("//div[contains(., '" + role + "')]/parent::td/parent::tr/td/div/a[. = '" + contactName + "']/../../../td/div/a[contains(., 'Select')]"));

    }

    private WebElement button_SearchResultsCompanyClickSelect(String contactName) {
        
        return find(By.xpath("//div/a[. = '" + contactName + "']/../../../td/div/a[contains(., 'Select')]"));

    }

    @FindBy(xpath = "//div[contains(@id, ':FBContactSearchResultsLV-body') or contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/descendant::table")
	protected WebElement table_SearchSearchResults;

    @FindBy(xpath = "//div[contains(@id, ':FBContactSearchResultsLV-body') or contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body')]/descendant::table/descendant::tbody/descendant::a")
    private List<WebElement> link_SearchSearchResults;

    private List<WebElement> text_SearchResultsPersonAddress(String lastName, String firstName, String AddressLineOne) {
        return finds(By.xpath("//a[contains(., '" + lastName + ", " + firstName + "')]/ancestor::td/following-sibling::td/div[contains(., '" + AddressLineOne + "')]"));
    }

    private List<WebElement> listResults_SearchSearchResults() {
        return finds(By.xpath("//div[contains(@id, ':FBContactSearchResultsLV-body')]/descendant::table/descendant::tr"));
    }

    @FindBy(xpath = "//div[contains(@id, 'ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV')]")
	protected WebElement div_SearchAdvancedSearchResultsContainer;

    @FindBy(xpath = "//div[contains(@id, 'ClaimVendorSearch:ClaimVendorSearchScreen:1')]")
    private WebElement div_ClaimVendorSearchResults;


    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------

//	//	public boolean isMinor(String name, String addressLine1){
//		
//	}

//	//	public AdvancedSearchResults minorSearchResults(){
//		if(checkIfElementExists("//img[contains(@id, 'Minor')]", 3000)){
//			
//		} else {
//			return null;
//		}
//		
//	}

    public void clickSearch() {
    	waitForPostBack();
        clickWhenClickable(button_SearchSearch);
        int i = 0;
        boolean searchResultsFound = false;
        do {
            searchResultsFound = checkIfElementExists(div_SearchAdvancedSearchResultsContainer, 5);
            i++;
        } while (searchResultsFound == false && i < 100);
    }

    public void clickReset() {
        clickWhenClickable(button_SearchReset);
    }

    public void clickCreateNew() {
        clickWhenClickable(button_SearchCreateNew);
    }

    public boolean clickCreateNewPerson() {
        clickCreateNew();
        if(checkIfElementExists(link_SearchCreateNewPerson, 2000)){
        	waitUntilElementIsClickable(By.xpath("//div[contains(@id, ':ClaimContacts_CreateNewContactButton:ContactsMenuActions_NewPersonMenuItem')]"));
        clickWhenClickable(link_SearchCreateNewPerson);
        	return true; 
        } else {
        	return false;
    }
    }

    public void clickCreateNewCompany() {
        clickCreateNew();
        waitUntilElementIsClickable(link_SearchCreateNewCompany);
        link_SearchCreateNewCompany.click();
    }

    public void clickSearchPersonSearchResults(String firstName, String lastName, String address) {
        clickWhenClickable(link_SearchResultsPerson(firstName, lastName, address).get(0));
    }

    public void clickSearchCompanySearchResults(String company, String address, State state) {
        
        clickWhenClickable(link_SearchResultsCompany(company, address, state.getName()));
    }

    public void selectContactWithRoleName(String role, String contactName) {
        
        clickWhenClickable(button_SearchResultsCompanyClickSelect(role, contactName));
    }

    private void selectContactByName(String name) {
        
        clickWhenClickable(button_SearchResultsCompanyClickSelect(name));
    }

    public void clickSimpleSearchCompanySearchResults(String company, String address) {
        clickWhenClickable(link_SimpleSearchResultsCompany(company, address));
    }

    public void clickSearchPersonSearchResultsSelect(String firstName, String lastName, String address) {
        clickWhenClickable(link_SearchResultsPersonClickSelect(firstName, lastName, address));
    }

    public void clickSearchPersonSearchResultsSelect(String firstName, String middleName, String lastName, String address) {
        clickWhenClickable(link_SearchResultsPersonClickSelect(lastName, firstName, middleName, address));
    }

    public List<WebElement> getSearchResultsRows() {
        List<WebElement> myList1 = finds(By.xpath("//div[contains(@id, ':FBContactSearchResultsLV-body') or contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body') or contains(@id,':AddressSearchLV')]/div/table/tbody/tr"));
        //jlarsen 12/1/2015 commented out code used for degugging only.
//		List<WebElement> myList2 = table_SearchSearchResults.findElements(By.xpath("/descendant::tr"));
//		//		for(int i = 0; i < myList2.size(); i++){
//			System.out.println("Search Results toString = " myList2.get(i).getText());
//		}
        return myList1;
    }

    public List<WebElement> getClickableSearchResults() {
        return link_SearchSearchResults;

    }

    public List<WebElement> searchResultsName() {
        clickSearch();
        
        List<WebElement> searchResultsName = table_SearchSearchResults.findElements(By.xpath(".//tbody//a"));
        return searchResultsName;
    }

    public List<WebElement> checkSearchResultByNameAddress(String lastName, String firstName, String AddressLineOne) {
        return text_SearchResultsPersonAddress(lastName, firstName, AddressLineOne);
    }

    public List<WebElement> searchResultsCriteria(String criteria) {
        clickSearch();
        
        List<WebElement> searchResults = table_SearchSearchResults.findElements(By.xpath("//tbody/child::tr[contains(., '" + criteria + "')]"));
        return searchResults;
    }

    public void clickSearchResultsSelect(String company, String addressLine1) {
        clickSearch();
        
        waitUntilElementIsClickable(link_SearchResultsCompanySelect(company, addressLine1));
        link_SearchResultsCompanySelect(company, addressLine1).click();
    }

    public void clickAdvancedSearchAccountNumberSearchResultsSelect(String acctNum) {
        clickWhenClickable(link_SearchResultsAccountClickSelect(acctNum));
    }

    public boolean checkForValidationMessages() {
        if (getSearchResultCount() < 1) {
            ErrorHandling message = new ErrorHandling(getDriver());
            List<WebElement> messages = message.getValidationMessages();
            if (messages.size() > 0) {
                for (WebElement mess : messages) {
                    if (mess.getText().contains("The search returned zero results."))
                        return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public int getSearchResultCount() {
        waitUntilElementIsVisible(div_SearchAdvancedSearchResultsContainer);
        return tableUtils.getRowCount(div_SearchAdvancedSearchResultsContainer);
    }

    public WebElement getRandomSearchResults(String resultsDelimiter) {
        List<WebElement> searchResults = getSearchResultsRows();
        List<WebElement> searchResultsDelimited = searchResultsCriteria(resultsDelimiter);
        int resultsNumber = (int) (Math.random() * searchResultsDelimited.size() - 1) + 1;
        int lcv = 0;
        do {
            if (searchResultsDelimited.get(resultsNumber).findElement(By.xpath("//a")) == searchResults.get(lcv).findElement(By.xpath("//a"))) {
                break;
            } else {
                lcv++;
            }
        } while (lcv <= searchResults.size());

        return searchResults.get(lcv);
    }

    public AdvancedSearchResults clickSearchResultWithSsn() throws Exception {
        List<WebElement> searchResults = finds(By.xpath("//div[contains(., '###-##-')]/parent::td/parent::tr/td/div/a"));
        int randomResult = (int) ((Math.random() * (searchResults.size())));
        AdvancedSearchResults result = setAdvancedSearchResults(randomResult);
        searchResults.get(randomResult).click();
        return result;
    }

    public AdvancedSearchResults clickRandomAdvancedSearchResult() throws Exception {
    	  waitUntilElementIsVisible(div_SearchAdvancedSearchResultsContainer);
          List<WebElement> searchResults = tableUtils.getAllTableRows(div_SearchAdvancedSearchResultsContainer);
          int row = -1;
          if(searchResults.size()>0) {
              row = tableUtils.getRandomRowFromTable(div_SearchAdvancedSearchResultsContainer);
          } else {
        	  return null;
          }
          AdvancedSearchResults newResult = setAdvancedSearchResults(row);
	      tableUtils.clickLinkInSpecficRowInTable(div_SearchAdvancedSearchResultsContainer, row);
	      return newResult;
    }

    public AdvancedSearchResults clickRandomSearchResultWithSSN() throws Exception {
        List<String> searchResults = tableUtils.getAllCellTextFromSpecificColumn(div_SearchAdvancedSearchResultsContainer, "SSN / TIN");
        searchResults = searchResults.stream().filter(s -> s.length() > 9).collect(Collectors.toList());
        int random = (int) (Math.random() * searchResults.size());
        int rowNumber = tableUtils.getRowNumberInTableByText(div_SearchAdvancedSearchResultsContainer, searchResults.get(random));
        AdvancedSearchResults newResult = setAdvancedSearchResults(rowNumber);
        tableUtils.clickLinkInSpecficRowInTable(div_SearchAdvancedSearchResultsContainer, rowNumber);
        return newResult;
    }

    public AdvancedSearchResults clickRandomSearchResultWithoutSSN() throws Exception {
        List<String> searchResults = tableUtils.getAllCellTextFromSpecificColumn(div_SearchAdvancedSearchResultsContainer, "SSN / TIN");
        searchResults = searchResults.stream().filter(s -> s.length() <= 1).collect(Collectors.toList());
        int random = (int) (Math.random() * searchResults.size());
        int rowNumber = tableUtils.getRowNumberInTableByText(div_SearchAdvancedSearchResultsContainer, searchResults.get(random));
        AdvancedSearchResults newResult = setAdvancedSearchResults(rowNumber);
        tableUtils.clickLinkInSpecficRowInTable(div_SearchAdvancedSearchResultsContainer, rowNumber);
        return newResult;
    }

    public AdvancedSearchResults clickRandomAdvancedSearchResultWithPhone() throws Exception {
        
        waitUntilElementIsVisible(div_SearchAdvancedSearchResultsContainer);
        ArrayList<String> phoneNumbers = tableUtils.getAllCellTextFromSpecificColumn(div_SearchAdvancedSearchResultsContainer, "Phone #");
        Iterator<String> i = phoneNumbers.iterator();
        while (i.hasNext()) {
            if (!i.next().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")) {
                i.remove();
            }
        }

        int x = (int) Math.random() * phoneNumbers.size() + 1;
        List<WebElement> searchResults = tableUtils.getRowsInTableByColumnNameAndValue(div_SearchAdvancedSearchResultsContainer, "Phone #", phoneNumbers.get(x));
        int row = tableUtils.getRowNumberFromWebElementRow(searchResults.get(0));
        AdvancedSearchResults newResult = setAdvancedSearchResults(row);
        tableUtils.clickLinkInSpecficRowInTable(div_SearchAdvancedSearchResultsContainer, row);
        return newResult;
    }

    public AdvancedSearchResults clickAdvancedSearchResult(int row) throws Exception {
        
        waitUntilElementIsVisible(div_SearchAdvancedSearchResultsContainer);
        int rowCount = tableUtils.getRowCount(div_SearchAdvancedSearchResultsContainer);
        if (row > rowCount) {
            return null;
        }

        AdvancedSearchResults newResult = setAdvancedSearchResults(row);
        tableUtils.clickLinkInSpecficRowInTable(div_SearchAdvancedSearchResultsContainer, row);
        return newResult;
    }

    public AdvancedSearchResults setAdvancedSearchResults(int row) throws Exception {
        int age = -1;

        String address = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Address");
        String name = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Name");
        String city = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "City");
        String state = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "State");
        String zip = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "ZIP");
        String phone = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Phone #");
        String type = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Type");
        String roles = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Roles");
        String agent = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Agent").trim();
//		String returnedMailDate = tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Return Mail Date");
        AdvancedSearchResults newResult = new AdvancedSearchResults();

        if (!type.contains("C")) {
            ArrayList<String> nameArray = StringsUtils.lastFirstMiddleInitialNameParser(name);

            if (name.contains(",")) {
                if (checkIfElementExists("//div[contains(., '" + address + "')]/parent::td/preceding-sibling::td/div/a[contains(., '" + nameArray.get(0) + "') and contains(., '" + nameArray.get(1) + "')]/ancestor::tr/td/div/span[contains(@id,'Minor')]", 3000)) {
                    age = Integer.valueOf(link_SearchResultsMinor(nameArray.get(0), nameArray.get(1), address).getText());
                }

                newResult.setFirstName(nameArray.get(0));
                if (nameArray.size() > 2) {
                    newResult.setMiddleName(nameArray.get(1));
                }
                newResult.setLastNameOrCompanyName(nameArray.get(nameArray.size() - 1));
            } else {
                newResult.setLastNameOrCompanyName(name);
            }
        } else {
            newResult.setLastNameOrCompanyName(tableUtils.getCellTextInTableByRowAndColumnName(div_SearchAdvancedSearchResultsContainer, row, "Name"));
        }
        if (type.contains("C")) {
            newResult.setContactSubType(ContactSubType.Company.getValue());
        } else {
            newResult.setContactSubType(ContactSubType.Person.getValue());
        }
        System.out.println("Restart please");
        System.out.println("the first name is: " + newResult.getFirstName() + "\r\n the LastName is " + newResult.getLastNameOrCompanyName());
        newResult.setAddress(address, city, State.valueOfName(state), zip);
//		ArrayList<String> insName = gw.helpers.StringsUtils.lastFirstMiddleInitialNameParser(name);
//		newResult.setFirstName(insName.get(1));
//		if(insName.size()>2){
//			newResult.setMiddleName(insName.get(2));
//		}
//		newResult.setLastNameOrCompanyName(insName.get(0));
        newResult.setPhone(phone);
        newResult.setRolesFromSearchResults(roles);
        if (age > -1) {
            newResult.setAge(age);
        }
        if (!(agent == null || agent.equals("") || agent.contains("096"))) {
            newResult.setAgentFromSearchResults(agent);
        }

        return newResult;

    }

    public AdvancedSearchResults getSearchResultWithMinorDesignation() throws Exception {
        WebElement tableRow = img_SearchResultsMinor.findElement(By.xpath(".//ancestor::tr[1]"));
        int row = Integer.valueOf(tableRow.getAttribute("data-recordindex")) + 1;
        return setAdvancedSearchResults(row);
    }

    public List<String> converListWebElementToListString(List<WebElement> listOfWebElements) {
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < listOfWebElements.size(); i++) {
            stringList.add(listOfWebElements.get(i).getText());
        }
        return stringList;
    }

    public List<WebElement> getSingleSimpleSearchResult(String linkToFind) {
        List<WebElement> simpleSearchResult = new ArrayList<WebElement>();
        boolean found = false;
        List<WebElement> companyLinks = table_SimpleSearchSearchResults.findElements(By.xpath("//a"));
        for (int i = 0; i <= companyLinks.size() && found == false; i++) {
            if (companyLinks.get(i).getText().contains(linkToFind)) {
                found = true;
                break;
            }
        }
        if (found == false) {
            Assert.fail("The requested search Result was not found: " + linkToFind);
        }
        return simpleSearchResult;
    }

    public ArrayList<String> getSearchResultsNameColumn() {
        
        return tableUtils.getAllCellTextFromSpecificColumn(div_SearchAdvancedSearchResultsContainer, "Name");
    }

    public boolean searchResultsContainCriteria(String columnName, String searchCriterion) {
        List<String> searchSSN = tableUtils.getAllCellTextFromSpecificColumn(find(By.xpath("//div[contains(@id, 'ABContactSearch:ABContactSearchScreen:FBContactSearchResultsLV')]")), columnName);
        for (String ssn : searchSSN) {
            if (!ssn.contains(searchCriterion)) {
                return false;
            }
        }
        return true;
    }

    public void clickSearchDetailsButton(String lastName, String agentNum) {
        
        clickWhenClickable(button_SearchResultsDetails(lastName, agentNum));
    }

    public ContactDetailsBasicsAB clickClaimVendorSearchResult(int row) {
//		List<WebElement> searchResults = tableUtils.getAllTableRows(div_ClaimVendorSearchResults);
//		int rowCount = tableUtils.getRowCount(div_ClaimVendorSearchResults);
//		if(row>rowCount){
//			return null;
//		}
//		boolean hasNum = false;
//		for(int i = 0; i<rowCount; i++){
//			WebElement searchResult = searchResults.get(row).findElement(By.xpath("./td[3]/div"));
//			if(searchResult.getText().matches("\\d+")){
//				hasNum = true;
//				break;
//			}			
//		}
        
        waitUntilElementIsClickable(div_ClaimVendorSearchResults);
        tableUtils.clickLinkInSpecficRowInTable(div_ClaimVendorSearchResults, row);
        return new ContactDetailsBasicsAB(getDriver());
    }

    public void clickVendorSimpleSearchResults(ContactSubType type) {
        String typeAbbreviation = type.getValue().substring(0, 1);
        List<WebElement> results = finds(By.xpath("//div[contains(@id, ':FBContactSearchResultsLV-body') or contains(@id, 'SimpleABContactSearch:SimpleSearchScreen:3-body') or contains(@id,':AddressSearchLV')]/div/table/tbody/tr/td/div[contains(., 'Vendor')]/parent::td/parent::tr/td/div[contains(.,'" + typeAbbreviation + "')]/parent::td/parent::tr/td/div/a"));
        int randomResult = (int) ((Math.random() * (results.size())));
        results.get(randomResult).click();
    }


}
