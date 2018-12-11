package updateUsers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.UsersInfo;

public class UpdateUsersCommon extends BasePage {

	private WebDriver driver;

	public UpdateUsersCommon(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	List<String> userList = new ArrayList<String>();


	public void createUserList() {
		List<WebElement> userNames = finds(By.xpath("//div[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a"));
		for (WebElement element : userNames) {
			userList.add(element.getText());
		}
	}

	public void nextPage() {
		WebElement pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
		clickWhenClickable(pageNumber);
		pageNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		pageNumber.sendKeys(Integer.toString(Integer.parseInt(pageNumber.getAttribute("value")) + 1));
		pageNumber.sendKeys(Keys.ENTER);
		waitForPostBack();

	}

	public String getUserRegion() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetail_AccessCardTab-btnInnerEl')]")));

		if (finds(By.xpath("//a[contains(@id, 'UserDetail_AccessDV:UserGroupsLV:0:Group')]")).size() > 0) {
			return find(By.xpath("//a[contains(@id, 'UserDetail_AccessDV:UserGroupsLV:0:Group')]")).getText();
		} else {
			return null;
		}
	}


	public String getAgentPA() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, 'UserDetailScreen:UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Production Assistant')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, 'UserDetailScreen:UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Production Assistant')]/parent::td/parent::tr/child::td[3]/div")).getText();
		} else {
			return null;
		}
	}


	public String getAgentSA() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, 'UserDetailScreen:UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Service Associate')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, 'UserDetailScreen:UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/child::tbody/tr/child::td/div[contains(text(), 'Service Associate')]/parent::td/parent::tr/child::td[3]/div")).getText();
		} else {
			return null;
		}
	}


	public String getAgentNumber() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AgentNumber-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AgentNumber-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getJobTitle() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':UserProfileDV:Title-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':UserProfileDV:Title-inputEl')]")).getText();
		} else {
			return null;
		}
	}


	public String getUserName() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:Username-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:Username-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getUserPrefix() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:Prefix-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:Prefix-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getUserSuffix() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:Suffix-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:Suffix-inputEl')]")).getText();
		} else {
			return null;
		}
	}


	public UsersInfo getUserInfo() {
		UsersInfo userinfo = new UsersInfo();
		userinfo.setUsername(getUserName());
		userinfo.setFirstName(getFirstName2());
		userinfo.setMiddleName(getMiddleName2());
		userinfo.setLastName(getLastName2());
		userinfo.setPreferedName(getPreferredName());
		userinfo.setSuffix(getUserSuffix());
		userinfo.setPrefix(getUserPrefix());
		userinfo.setPassword("gw");
		userinfo.setJobTitle(getJobTitle());
		userinfo.setNumber(getAgentNumber());


		return userinfo;
	}



	public void userSearch_ByUserName(String user) {
		clickReset();
		WebElement userName = find(By.xpath("//input[contains(@id, ':UserSearchDV:Username-inputEl')]"));
		userName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		userName.sendKeys(user);
		sendArbitraryKeys(Keys.TAB);
		find(By.xpath("//a[contains(@id, ':SearchAndResetInputSet:SearchLinksInputSet:Search')]")).click();
		waitForPostBack();
		if (finds(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).size() > 0) {
			find(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).click();
			waitForPostBack();
		} else {
			waitForPostBack();
		}
	}



	//searches for user through the Administration Menu
	public Boolean userSearch(String user) {
		String[] split = user.split(" ");
		switch (user) { //special people with special last names and such
		case "Donna D Ambra":
			split[0] = "Donna";
			split[split.length - 1] = "D Ambra";
			break;
		case "Ryan Bruce Harrigfeld Jr.":
			split[0] = "Ryan";
			split[split.length - 1] = "Harrigfeld";
			break;
		case "Alejandro Salinas Jr.":
			split[0] = "Alejandro";
			split[split.length - 1] = "Salinas";
			break;
		case "Jerry L Von Brethorst":
			split[0] = "Jerry";
			split[split.length - 1] = "Von Brethorst";
			break;
		case "Philip Paul Zemaitis Jr.":
			split[0] = "Philip";
			split[split.length - 1] = "Zemaitis";
			break;
		case "John W Hill IV":
			split[0] = "John";
			split[split.length - 1] = "Hill";
			break;
		case "Bianca Von Brethorst":
			split[0] = "Bianca";
			split[split.length - 1] = "Von Brethorst";
			break;
		case "Abby Van Biezen":
			split[0] = "Abby";
			split[split.length - 1] = "Van Biezen";
			break;
		case "Danika DeGiulio SA":
			split[0] = "Danika";
			split[split.length - 1] = "DeGiulio";
			break;
		case "Sha Nae Blaisdell":
			split[0] = "Sha Nae";
			split[split.length - 1] = "Blaisdell";
			break;
		}

		WebElement fName = find(By.xpath("//input[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]"));
		fName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		fName.sendKeys(split[0]);
		sendArbitraryKeys(Keys.TAB);
		WebElement lName = find(By.xpath("//input[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]"));
		lName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		lName.sendKeys(split[split.length - 1]);
		find(By.xpath("//a[contains(@id, ':SearchAndResetInputSet:SearchLinksInputSet:Search')]")).click();
		waitForPostBack();
		if (finds(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[6]/div/a[contains(text(), 'Crop')]")).isEmpty()) {
			if (finds(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).size() > 0) {
				find(By.xpath("//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV-body')]/descendant::tbody/child::tr[1]/child::td[3]/div/a")).click();
				waitForPostBack();
				return true;
			} else {
				waitForPostBack();
				return false;
			}
		}
		waitForPostBack();
		return false;
	}

	//get list of all users in an existing Role
	public List<String> getUserList(String role) {
		TopMenuAdministrationPC topMenu = new TopMenuAdministrationPC(driver);
		topMenu.clickRoles();
		WebElement pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
		WebElement maxPages = find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
		String maxpage = maxPages.getText().substring(maxPages.getText().length() - 1, maxPages.getText().length());

		while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
			if (finds(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).size() > 0) {
				find(By.xpath("//div[contains(@id, 'Roles:RolesScreen:RolesLV-body')]/descendant::tbody/child::tr/child::td[2]/div/a[(text()='" + role + "')]")).click();
				break;
			} else {
				nextPage();
			}
		}
		waitForPostBack();
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'RoleDetailScreen:RoleDetail_UsersCardTab-btnEl')]")));
		pageNumber = find(By.xpath("//input[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV:_ListPaging-inputEl')]"));
		maxPages = find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
		maxpage = maxPages.getText().replace("of ", "");

		userList.clear();
		while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
			pageNumber = find(By.xpath("//input[contains(@id, 'RoleDetailPage:RoleDetailScreen:RoleUsersLV:_ListPaging-inputEl')]"));
			createUserList();
			nextPage();

		}
		return userList;
	}




	public String getPreferredName() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:AlternateName-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':UserDetailCommons:UserDetailInputSet:AlternateName-inputEl')]")).getText();
		} else {
			return null;
		}
	}


	public String getFirstName2() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:FirstName-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getMiddleName2() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:MiddleName-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getLastName2() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, ':GlobalPersonNameInputSet:LastName-inputEl')]")).getText();
		} else {
			return null;
		}
	}

	public String getUserCounty() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'ProfileCardTab-btnEl')]")));
		List<WebElement> agentcounty = finds(By.xpath("//div[contains(@id, ':CountyList-inputEl')]"));
		if (agentcounty.isEmpty()) {
			return null;
		} else {
			return agentcounty.get(0).getText();//.replaceAll("[\\d]", "").replace(" - ", "");
		}
	}

	public Boolean isAgent() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_RolesCardTab-btnEl')]")));
		List<WebElement> rolesList = finds(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserRolesLV-body')]/div/table/child::tbody/child::tr/child::td/div/a[contains(@id, 'RoleName')]"));
		//        if (rolesList.size() > 1) {
		for (WebElement role : rolesList) {
			if(role.getText().equalsIgnoreCase("Auto Issue")) {
				continue;
			}
			if (!role.getText().equalsIgnoreCase("Agent")) {
				return false;
			}
		}
		//        }
		return true;
	}

	public Boolean isActive() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_BasicCardTab-btnEl')]")));
		List<WebElement> rolesList = finds(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetailDV:UserDetailCommons:UserDetailInputSet:AccountLocked-inputEl')]"));
		if (rolesList.size() > 1) {
			return !rolesList.get(0).getText().equalsIgnoreCase("Yes");
		}
		return true;
	}


	public String getRegion() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Region')]")).size() > 0) {
			return find(By.xpath("//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessDV:UserGroupsLV-body')]/descendant::tbody/child::tr/child::td/div/a[contains(text(), 'Region')]")).getText();
		} else {
			return null;
		}
	}


	//get persons associated with user
	public String getAssociation() {
		clickWhenClickable(find(By.xpath("//span[contains(@id, ':UserDetailScreen:UserDetail_AgentPAsTab-btnEl')]")));

		if (finds(By.xpath("//div[contains(@id, 'UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/tbody/tr/child::td[3]/div")).size() > 0) {
			return find(By.xpath("//div[contains(@id, 'UserAssociationsPanelSet:UserAssociationsLV-body')]/div/table/tbody/tr/child::td[3]/div")).getText();
		} else {
			return null;
		}
	}

	@FindBy(css = "input[id$=':Username-inputEl']")
	private WebElement input_UserName;

	public List<String> getALLUsers_UserName() {
		List<String> returnList = new ArrayList<String>();

		for(char a = 'a'; a!='b'; a++) {
			setText(input_UserName, String.valueOf(a));
			clickSearch();

			if (!finds(By.xpath("//table[(@id='AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV:_ListPaging') and not(contains(@style, 'display: none'))]")).isEmpty()) {
				WebElement pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
				WebElement maxPages = find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
				String maxpage = maxPages.getText().replace("of ", "");
				while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
					pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
					List<WebElement> userList = finds(By.xpath("//div[contains(@id, ':UserSearchResultsLV-body')]/div/table/tbody/child::tr/child::td[4]/div"));
					for (WebElement group : userList) {
						returnList.add(group.getText());
					}
					nextPage();
				}
			} else {
				List<WebElement> rolesList = finds(By.xpath("//div[contains(@id, ':UserSearchResultsLV-body')]/div/table/tbody/child::tr/child::td[4]/div"));
				for (WebElement group : rolesList) {
					returnList.add(group.getText());
				}
			}
		}

		return returnList;
	}


	public List<String> getRoles() {
		List<String> returnList = new ArrayList<String>();
		clickWhenClickable(find(By.xpath("//span[contains(@id, ':UserDetail_RolesCardTab-btnEl')]")));

		if (!finds(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]")).isEmpty()) {
			WebElement pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
			WebElement maxPages = find(By.xpath("//div[contains(@id, 'tbtext-')][last()]"));
			String maxpage = maxPages.getText().replace("of ", "");
			while (Integer.parseInt(pageNumber.getAttribute("value")) <= Integer.parseInt(maxpage)) {
				pageNumber = find(By.xpath("//input[contains(@id, ':_ListPaging-inputEl')]"));
				List<WebElement> rolesList = finds(By.xpath("//div[contains(@id, ':UserRolesLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
				for (WebElement group : rolesList) {
					returnList.add(group.getText());
				}
				nextPage();

			}
		} else {
			List<WebElement> rolesList = finds(By.xpath("//div[contains(@id, ':UserRolesLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
			for (WebElement group : rolesList) {
				returnList.add(group.getText());
			}
		}
		return returnList;
	}


	public List<String> getAccess() {
		List<String> returnList = new ArrayList<String>();
		clickWhenClickable(find(By.xpath("//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_AccessCardTab-btnEl')]")));
		List<WebElement> groupList = finds(By.xpath("//div[contains(@id, ':UserGroupsLV-body')]/div/table/tbody/child::tr/child::td[2]/div/a"));
		for (WebElement group : groupList) {
			returnList.add(group.getText());
		}
		return returnList;
	}


}
