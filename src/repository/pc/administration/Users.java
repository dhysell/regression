package repository.pc.administration;

import com.idfbins.enums.State;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.AddressType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.pc.topmenu.TopMenuAdministrationPC;

import java.util.ArrayList;
import java.util.List;

public class Users extends BasePage {
	
	WebDriver driver;

    public Users(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//input[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchDV:Username-inputEl')]")
    private WebElement editbox_UsersUsername;

    @FindBy(xpath = "//input[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchDV:ContactInputSet:GlobalPersonNameInputSet:FirstName-inputEl')]")
    private WebElement editbox_UsersFirstname;

    @FindBy(xpath = "//input[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchDV:ContactInputSet:GlobalPersonNameInputSet:LastName-inputEl')]")
    private WebElement editbox_UsersLastname;

    @FindBy(xpath = "//input[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchDV:Username-inputEl')]")
    private WebElement editbox_UsersAvailableAgentNumber;

    @FindBy(xpath = "//input[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchDV:Organization-inputEl')]")
    private WebElement editbox_UsersOrganization;

    @FindBy(xpath = "//div[contains(@id, 'AdminUserSearchPage:UserSearchScreen:UserSearchResultsLV')]")
    private WebElement div_UsersSearchResults;

    @FindBy(xpath = "//span[contains(@id, 'UserDetailPage:UserDetailScreen:UserDetail_ProfileCardTab-btnEl')]")
    private WebElement link_UsersProfileTab;

    @FindBy(xpath = "//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AddressInputSet:OfficeNumber-inputEl')]")
    private WebElement text_UsersProfileTabOfficeNumber;

    @FindBy(xpath = "//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AddressInputSet:globalAddressContainer:GlobalAddressInputSet:AddressSummary-inputEl')]")
    private WebElement text_UsersProfileTabAddress;

    @FindBy(xpath = "//div[contains(@id, 'UserDetailPage:UserDetailScreen:UserProfileDV:AddressInputSet:AddressType-inputEl')]")
    private WebElement text_UsersProfileTabAddressType;

    @FindBy(css = "input[id$=':Username-inputEl']")
    private WebElement input_UserName;

    public boolean userSearch(String username) {
        //searches for user through the Administration Menu
	/*		String[] split = user.split(" ");
			switch(user) { //special people with special last names and such
				case "Donna D Ambra":
					split[0] = "Donna";
					split[split.length -1] = "D Ambra";
					break;
				case "Ryan Bruce Harrigfeld Jr.":
					split[0] = "Ryan";
					split[split.length -1] = "Harrigfeld";
					break;
				case "Alejandro Salinas Jr.":
					split[0] = "Alejandro";
					split[split.length -1] = "Salinas";
					break;
				case "Jerry L Von Brethorst":
					split[0] = "Jerry";
					split[split.length -1] = "Von Brethorst";
					break;
				case "Philip Paul Zemaitis Jr.":
					split[0] = "Philip";
					split[split.length -1] = "Zemaitis";
					break;
				case "John W Hill IV":
					split[0] = "John";
					split[split.length -1] = "Hill";
					break;
				case "Bianca Von Brethorst":
					split[0] = "Bianca";
					split[split.length -1] = "Von Brethorst";
					break;
			}
*/
        setText(input_UserName, username);
        clickSearch();
        if (new TableUtils(getDriver()).getRowCount(div_UsersSearchResults) >= 1) {
            new TableUtils(getDriver()).clickLinkInTableByRowAndColumnName(div_UsersSearchResults, 1, "Name");
            return true;
        } else {
            return false;
        }
    }


    public void clickProfile() {
        clickWhenClickable(link_UsersProfileTab);
    }


    public AddressInfo get_Profile_Address() throws Exception {
        //get Office Number
        String officeNumber = text_UsersProfileTabOfficeNumber.getText();
        //get Address
        String uiAddress = text_UsersProfileTabAddress.getText();
        String[] address = uiAddress.split("\n");
        String[] cityStateZip = StringsUtils.cityStateZipParser(address[1]);
//		String city = cityStateZip[0];

        //get Address Type
        String addressType = text_UsersProfileTabAddressType.getText();

        AddressInfo addressInfo = new AddressInfo(address[0], cityStateZip[0], State.valueOfAbbreviation(cityStateZip[1]), cityStateZip[2]);
        if (!addressType.isEmpty()) {
            addressInfo.setType(AddressType.valueOf(addressType));
        }
        addressInfo.setNumber(Integer.valueOf(officeNumber));

        return addressInfo;

    }

    public AddressInfo getAgentAddressFromUserProfile(String username) throws Exception {
        userSearch(username);
        clickProfile();
        return get_Profile_Address();
    }
    
    public List<String> getALLUsers_UserName() {
    	List<String> toReturn = new ArrayList<String>();
    	new TopMenuAdministrationPC(driver).clickUserSearch();
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	return toReturn;
    }

    
    
    
    
    
    
    
    
    
}
















