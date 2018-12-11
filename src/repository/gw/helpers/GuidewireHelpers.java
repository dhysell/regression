package repository.gw.helpers;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import persistence.globaldatarepo.helpers.EnvironmentsHelper;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.*;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyPremium;
import repository.gw.topinfo.TopInfo;
import services.enums.Broker;

import java.util.*;


/**
 * Holder Place for things used to in Guidewire that don't fit in other util classes or page factories.
 */
public class GuidewireHelpers extends BasePage {

	private WebDriver driver;
	private Config guidewireHelpersConfig;

	public GuidewireHelpers(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		try {
			this.guidewireHelpersConfig = new Config(driver.getCurrentUrl());
			if (!this.guidewireHelpersConfig.getUrl().toUpperCase().contains(this.guidewireHelpersConfig.getEnv().toUpperCase())) {
				this.guidewireHelpersConfig.setUrl(new EnvironmentsHelper().getEnvironmentFromURL(this.guidewireHelpersConfig.getUrl()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Unable to get URL from current Driver.");
		}
	}

	public ApplicationOrCenter getCurrentCenter() {
		String currentURL = driver.getCurrentUrl();
		ApplicationOrCenter applicationToReturn = null;
		if (currentURL.contains("/ab")) {
			applicationToReturn = ApplicationOrCenter.ContactManager;
		} else if (currentURL.contains("/bc")) {
			applicationToReturn = ApplicationOrCenter.BillingCenter;
		} else if (currentURL.contains("/cc")) {
			applicationToReturn = ApplicationOrCenter.ClaimCenter;
		} else if (currentURL.contains("/pc")) {
			applicationToReturn = ApplicationOrCenter.PolicyCenter;
		} else if (currentURL.contains("/bcb")) {
			applicationToReturn = ApplicationOrCenter.BillingCenterBatch;
		} else if (currentURL.contains("/ccb")) {
			applicationToReturn = ApplicationOrCenter.ClaimCenterBatch;
		} else if (currentURL.contains("/pcb")) {
			applicationToReturn = ApplicationOrCenter.PolicyCenterBatch;
		} else {
			Assert.fail("The URL used to get the current application did not contain a guidewire designation.");
		}
		return applicationToReturn;
	}


	/**
	 * @return String of environment you are in based on the current url. This is designed to work only with message broker setup.
	 */
	public String getMessageBrokerEnvironment() {
		String currentURL = driver.getCurrentUrl().toUpperCase();
		if (currentURL.contains("DEV")) {
			return "DEV";
		} else if (currentURL.contains("IT")) {
			return "IT";
		} else if (currentURL.contains("QA")) {
			return "QA";
		} else if (currentURL.contains("UAT")) {
			return "UAT";
		} else if (currentURL.contains("PRD")) {
			return "PRD";
		} else {
			return "DEV";
		}
	}

	/**
	 * @return String of environment you are in based on the default configuration (I.E. IT, QA, DEV2, BHILTBRAND, etc.).
	 */
	public String getEnvironment() {
		return guidewireHelpersConfig.getEnv().toUpperCase();
	}

	/**
	 * This method takes two lists and compares them to see if they are identical. If not, it outputs to the console what the differences are.
	 *
	 * @param list1
	 * @param list2
	 */
	public void verifyLists(List<?> list1, List<?> list2) {
		int firstListSize = list1.size();
		int secondListSize = list2.size();
		int temporaryListSize;
		int errorCount = 0;
		if (firstListSize != secondListSize) {
			Assert.fail("The lists passed in are not equal in size. They are not identical.");
		}
		temporaryListSize = (firstListSize <= secondListSize) ? firstListSize : secondListSize;
		for (int i = 0; i < temporaryListSize; i++) {
			if (!list1.get(i).equals(list2.get(i))) {
				errorCount++;
				System.out.println("List position #" + i + " for the two lists are not the same, they are " + list1.get(i) + " and " + list2.get(i) + " respectively");
			}
		}

		if (errorCount != 0) {
			Assert.fail("The two lists passed in are not identical, please see console output for details.");
		} else {
			System.out.println("These two lists are identical");
		}
	}

	/**
	 * Compares the values set in an enum to those of a passed in List. Assumes enum and list are in the same order.
	 *
	 * @param nameOfEnumClassYouWant      This is the enum you want with .class added
	 * @param listToBeCheckedAgainst      This the values in the list you get from the UI.
	 * @param nameOfTheListYouAreChecking This is the name of the list you are checking. It's used to give context as to which dropdown you are checking.
	 */
	public <E extends Enum<E> & GetValue> boolean compareEnumValuesWithListValues(Class<E> nameOfEnumClassYouWant, List<String> listToBeCheckedAgainst, String nameOfTheListYouAreChecking) {
		boolean found = false;
		// iterate through enums
		for (E enumValue : EnumSet.allOf(nameOfEnumClassYouWant)) {
			// reset to false for each value
			found = false;
			// iterate over the list
			for (String listValue : listToBeCheckedAgainst) {
				// compare enum value vs string value
				if (enumValue.getValue().equals(listValue)) {
					found = true;
					break;
				} // break out of inner for Loop
			}
			if (found) {
				continue;
			}
			System.out.println("The value: " + enumValue.getValue() + " wasn't found in the list named " + nameOfTheListYouAreChecking);
		}

		if (listToBeCheckedAgainst.size() != EnumSet.allOf(nameOfEnumClassYouWant).size()) {
			System.out.println(nameOfTheListYouAreChecking + " drop down has too many values");
		} else {
			found = true;
			System.out.println("all the values expected were found");
		}
		return found;
	}

	/**
	 * This method is designed to take a link WebElement that is passed in and check the wrapping table to
	 * see if it contains the class 'g-actionable'. This indicates that the text has a hyperlink tied to it.
	 *
	 * @param linkWebElement
	 * @return Boolean - true if the link is clickable as a hyperlink, false if it is not clickable as a hyperlink.
	 */
	public boolean checkIfLinkIsActive(WebElement linkWebElement) {
		waitUntilElementIsVisible(linkWebElement);
		List<WebElement> listOfFoundLinks = linkWebElement.findElements(By.xpath(".//ancestor::table[contains(@class, 'g-actionable')]"));
		if (listOfFoundLinks.size() > 0) {
			return true;
		} else {
			listOfFoundLinks = linkWebElement.findElements(By.xpath(".//self::a[contains(@class, 'g-actionable')]"));
			return listOfFoundLinks.size() > 0;
		}
	}

	public boolean isOnPage(String xpath) {
		return isOnPage(xpath, 5000);
	}

	public boolean isOnPage(String xpath, int timeToWait_Seconds) {
		long endTime = new Date().getTime() + (timeFixer(timeToWait_Seconds) * 1000);
		boolean onPage = !finds(By.xpath(xpath)).isEmpty();
		while (!onPage && new Date().getTime() < endTime) {
			try {
				Thread.sleep(50); //Used to check once every 50 milliseconds to see if you are on the page you expect to be on.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			onPage = !finds(By.xpath(xpath)).isEmpty();
		}
		return onPage;
	}

	public void isOnPage(String xpath, int timeToWait_Seconds, String failureMessage) throws GuidewireNavigationException {
		if (!new GuidewireHelpers(driver).isOnPage(xpath, timeToWait_Seconds)) {
			throw new GuidewireNavigationException(failureMessage, getErrorMessages());
		}
	}

	/**
	 * @author iclouser
	 * @Description - This is meant to refocus the window and fire off postbacks.
	 * @DATE - Jul 20, 2016
	 */
	public void clickProductLogo() {
		clickWhenClickable(find(By.xpath("//img[contains(@class,'product-logo')]")));
	}

	public boolean errorMessagesExist() {
		return checkIfElementExists("//div[contains(@id, '_msgs')]/descendant::div[@class='message']", 1000);
	}

	public List<String> getErrorMessages() {
		List<WebElement> messageList = finds(By.xpath("//div[contains(@id, '_msgs')]/descendant::div[@class='message']"));
		List<String> returnList = new ArrayList<String>();
		for (WebElement elememt : messageList) {
			returnList.add(elememt.getText());
		}
		return returnList;
	}

	public String getFirstErrorMessage() {
		List<String> returnList = getErrorMessages();
		return returnList.get(0);
	}

	public boolean containsErrorMessage(String errorMessage) {
		List<String> blockingMessage = getErrorMessages();

		for (String message : blockingMessage) {
			if (message.contains(errorMessage)) {
				return true;
			}
		}
		return false;
	}

	public boolean equalsErrorMessage(String errorMessage) {
		List<String> blockingMessage = getErrorMessages();

		for (String message : blockingMessage) {
			if (message.equals(errorMessage)) {
				return true;
			}
		}
		return false;
	}

	public String getPolicyNumber(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getPolicyNumber();
		case CPP:
			return policy.commercialPackage.getPolicyNumber();
		case Membership:
			return policy.membership.getPolicyNumber();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getPolicyNumber();
		case Squire:
			return policy.squire.getPolicyNumber();
		case StandardFire:
			return policy.standardFire.getPolicyNumber();
		case StandardIM:
			return policy.standardInlandMarine.getPolicyNumber();
		case StandardLiability:
			return policy.standardLiability.getPolicyNumber();
		default:
			return null;
		}
	}

	public void setPolicyEffectiveDate(GeneratePolicy policy, Date effectiveDate) {
		switch (policy.productType) {
		case Businessowners:
			policy.busOwnLine.setEffectiveDate(effectiveDate);
			break;
		case CPP:
			policy.commercialPackage.setEffectiveDate(effectiveDate);
			break;
		case Membership:
			policy.membership.setEffectiveDate(effectiveDate);
			break;
		case PersonalUmbrella:
			policy.squireUmbrellaInfo.setEffectiveDate(effectiveDate);
			break;
		case Squire:
			policy.squire.setEffectiveDate(effectiveDate);
			break;
		case StandardFire:
			policy.standardFire.setEffectiveDate(effectiveDate);
			break;
		case StandardIM:
			policy.standardInlandMarine.setEffectiveDate(effectiveDate);
			break;
		case StandardLiability:
			policy.standardLiability.setEffectiveDate(effectiveDate);
			break;
		}
	}

	public Date getPolicyEffectiveDate(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getEffectiveDate();
		case CPP:
			return policy.commercialPackage.getEffectiveDate();
		case Membership:
			return policy.membership.getEffectiveDate();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getEffectiveDate();
		case Squire:
			return policy.squire.getEffectiveDate();
		case StandardFire:
			return policy.standardFire.getEffectiveDate();
		case StandardIM:
			return policy.standardInlandMarine.getEffectiveDate();
		case StandardLiability:
			return policy.standardLiability.getEffectiveDate();
		}
		Assert.fail("THE PRODUCT TYPE ISN'T SET AND WILL RETURN NULL AND CAUSE THE CALENDAR.SETTIME NULL FAILURE!!!!");
		return null;
	}

	public void setPolicyExpirationDate(GeneratePolicy policy, Date expirationdate) {
		switch (policy.productType) {
		case Businessowners:
			policy.busOwnLine.setExpirationDate(expirationdate);
			break;
		case CPP:
			policy.commercialPackage.setExpirationDate(expirationdate);
			break;
		case Membership:
			policy.membership.setExpirationDate(expirationdate);
			break;
		case PersonalUmbrella:
			policy.squireUmbrellaInfo.setExpirationDate(expirationdate);
			break;
		case Squire:
			policy.squire.setExpirationDate(expirationdate);
			break;
		case StandardFire:
			policy.standardFire.setExpirationDate(expirationdate);
			break;
		case StandardIM:
			policy.standardInlandMarine.setExpirationDate(expirationdate);
			break;
		case StandardLiability:
			policy.standardLiability.setExpirationDate(expirationdate);
			break;
		}
	}

	public int getPolicyTermLength(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getPolTermLengthDays();
		case CPP:
			return policy.commercialPackage.getPolTermLengthDays();
		case Membership:
			return policy.membership.getPolTermLengthDays();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getPolTermLengthDays();
		case Squire:
			return policy.squire.getPolTermLengthDays();
		case StandardFire:
			return policy.standardFire.getPolTermLengthDays();
		case StandardIM:
			return policy.standardInlandMarine.getPolTermLengthDays();
		case StandardLiability:
			return policy.standardLiability.getPolTermLengthDays();
		}
		return 365;
	}

	public repository.gw.enums.GeneratePolicyType getTypeToGenerate(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getTypeToGenerate();
		case CPP:
			return policy.commercialPackage.getTypeToGenerate();
		case Membership:
			return policy.membership.getTypeToGenerate();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getTypeToGenerate();
		case Squire:
			return policy.squire.getTypeToGenerate();
		case StandardFire:
			return policy.standardFire.getTypeToGenerate();
		case StandardIM:
			return policy.standardInlandMarine.getTypeToGenerate();
		case StandardLiability:
			return policy.standardLiability.getTypeToGenerate();
		}
		return null;
	}

	public void setTypeToGenerate(GeneratePolicy policy, repository.gw.enums.GeneratePolicyType type) {
		switch (policy.productType) {
		case Businessowners:
			policy.busOwnLine.setTypeToGenerate(type);
		case CPP:
			policy.commercialPackage.setTypeToGenerate(type);
		case Membership:
			policy.membership.setTypeToGenerate(type);
		case PersonalUmbrella:
			policy.squireUmbrellaInfo.setTypeToGenerate(type);
		case Squire:
			policy.squire.setTypeToGenerate(type);
		case StandardFire:
			policy.standardFire.setTypeToGenerate(type);
		case StandardIM:
			policy.standardInlandMarine.setTypeToGenerate(type);
		case StandardLiability:
			policy.standardLiability.setTypeToGenerate(type);
		}
	}


	public void setPolicyType(GeneratePolicy policy, repository.gw.enums.GeneratePolicyType type) {
		switch (policy.productType) {
		case Businessowners:
			policy.busOwnLine.setCurrentPolicyType(type);
			break;
		case CPP:
			policy.commercialPackage.setCurrentPolicyType(type);
			break;
		case Membership:
			break;
		case PersonalUmbrella:
			break;
		case Squire:
			policy.squire.setCurrentPolicyType(type);
			break;
		case StandardFire:
			policy.standardFire.setCurrentPolicyType(type);
			break;
		case StandardIM:
			policy.standardInlandMarine.setCurrentPolicyType(type);
			break;
		case StandardLiability:
			policy.standardLiability.setCurrentPolicyType(type);
			break;
		}
	}

	public repository.gw.enums.GeneratePolicyType getCurrentPolicyType(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getCurrentPolicyType();
		case CPP:
			return policy.commercialPackage.getCurrentPolicyType();
		case Membership:
			return policy.membership.getCurrentPolicyType();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getCurrentPolicyType();
		case Squire:
			return policy.squire.getCurrentPolicyType();
		case StandardFire:
			return policy.standardFire.getCurrentPolicyType();
		case StandardIM:
			return policy.standardInlandMarine.getCurrentPolicyType();
		case StandardLiability:
			return policy.standardLiability.getCurrentPolicyType();
		}
		return null;
	}

	public CreateNew getCreateNew(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getCreateNew();
		case CPP:
			return policy.commercialPackage.getCreateNew();
		case Membership:
			return policy.membership.getCreateNew();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getCreateNew();
		case Squire:
			return policy.squire.getCreateNew();
		case StandardFire:
			return policy.standardFire.getCreateNew();
		case StandardIM:
			return policy.standardInlandMarine.getCreateNew();
		case StandardLiability:
			return policy.standardLiability.getCreateNew();
		}
		return null;
	}

	public GeneratePolicyType getCurrentLineType(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getCurrentPolicyType();
		case CPP:
			return policy.commercialPackage.getCurrentPolicyType();
		case Squire:
			return policy.squire.getCurrentPolicyType();
		case StandardFire:
			return policy.standardFire.getCurrentPolicyType();
		case StandardIM:
			return policy.standardInlandMarine.getCurrentPolicyType();
		case StandardLiability:
			return policy.standardLiability.getCurrentPolicyType();
		case Membership:
			return policy.membership.getCurrentPolicyType();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getCurrentPolicyType();
		}
		return null;
	}

	public boolean isDraft(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.isDraft();
		case CPP:
			return policy.commercialPackage.isDraft();
		case Squire:
			return policy.squire.isDraft();
		case StandardFire:
			return policy.standardFire.isDraft();
		case StandardIM:
			return policy.standardInlandMarine.isDraft();
		case StandardLiability:
			return policy.standardLiability.isDraft();
		case Membership:
			return policy.membership.isDraft();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.isDraft();
		}
		return false;
	}


	public PolicyPremium getPolicyPremium(GeneratePolicy policy) {
		switch (policy.productType) {
		case Businessowners:
			return policy.busOwnLine.getPremium();
		case CPP:
			return policy.commercialPackage.getPremium();
		case Squire:
			return policy.squire.getPremium();
		case StandardFire:
			return policy.standardFire.getPremium();
		case StandardIM:
			return policy.standardInlandMarine.getPremium();
		case StandardLiability:
			return policy.standardLiability.getPremium();
		case Membership:
			return policy.membership.getPremium();
		case PersonalUmbrella:
			return policy.squireUmbrellaInfo.getPremium();
		}
		return null;
	}

	public boolean isRequired(String label) {
		if (isAvailable(label)) {
			return finds(By.xpath("//div[contains(text(), '" + label + "')]/preceding-sibling::table")).isEmpty();
		} else {
			return false;
		}
	}

	//	@author ecoleman
	public boolean isRequiredField(String label) {
		if (isAvailable(label)) {
			return !finds(By.xpath("//label[contains(text(), '" + label + "')]/ancestor::table[contains(@class, 'required')][1]")).isEmpty();
		} else {
			return false;
		}
	}

	public Boolean getRandBoolean() {
		return (new Random()).nextBoolean();
	}

	public repository.gw.enums.SquireEligibility getRandomEligibility() {
		return (getRandBoolean()) ? repository.gw.enums.SquireEligibility.City : ((getRandBoolean()) ? repository.gw.enums.SquireEligibility.Country : SquireEligibility.FarmAndRanch);
	}

	// @edited ecoleman ( 4/3/18 ) : Genericizing for different styles of checkbox
	public boolean isElectable(String label) {
		if (isAvailable(label)) {
			if (!finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]/preceding-sibling::table")).isEmpty()
					|| !finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]/ancestor::table")).isEmpty()) {
				return finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]/preceding-sibling::table[contains(@class, 'cb-checked')]")).isEmpty()
						|| finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]//ancestor::table[contains(@class, 'cb-checked')]")).isEmpty();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isSuggested(String label) {
		if (isAvailable(label)) {
			return !finds(By.xpath("//div[contains(text(), '" + label + "')]/preceding-sibling::table[contains(@class, 'cb-checked')]")).isEmpty();
		} else {
			return false;
		}
	}

	public boolean isAvailable(String label) {
		return !finds(By.xpath("//*[self::div | self::label][contains(text(), '" + label + "')]")).isEmpty();
	}

	@FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:EditPolicy-btnEl') or contains(@id, ':JobWizardToolbarButtonSet:EditPolicyWorkflow-btnEl') or contains(@id, ':JobWizardToolbarButtonSet:EditPolicyPremiumStability-btnEl')]")
    private WebElement button_EditPolicyTransaction;
	public void editPolicyTransaction() {
		//The text searched below is for the phrase "Edit Policy Transaction". However, the E in Edit is a hotkey and thus not a part of the element text. Please do not change this phrase.
		if (!finds(By.xpath("//span[contains(@id, 'EditPolicy-btnInnerEl') or contains(text(), 'dit Policy Transaction')]")).isEmpty()) {
			clickWhenClickable(button_EditPolicyTransaction);
			selectOKOrCancelFromPopup(OkCancel.OK);
		} else {
			System.out.println("Could not find 'Edit policy transaction' button!");
		}
	}

	//    public boolean accountLocked() {
	//        return finds(By.xpath("//label[contains(text(), 'account has been locked')]")).size() > 0;
	//    }

	public Broker getMessageBrokerConnDetails() {
		return Broker.valueOf(getMessageBrokerEnvironment());
	}

	public boolean overrideAddressStandardization() {
		if (checkIfElementExists("//span[contains(text(), 'Override')]/parent::span", 2000)) {
			clickWhenClickable(finds(By.xpath("//span[contains(text(), 'Override')]/parent::span")).get(0));
			return true;
		}
		return false;
	}

	public boolean duplicateContacts() {
		if (checkIfElementExists("//span[contains(text(), 'Matching Contacts')]", 2000)) {
			clickWhenClickable(find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")));
			return true;
		}
		return false;
	}

	public boolean verifyAddress(AddressInfo address) {
		boolean returnValue = false;

		String addressText = getTextOrValueFromElement(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')] | //div[contains(@id, ':AddressLine1-inputEl')]"));
		if (!address.getLine1().equals(addressText)) {
			returnValue = true;
			systemOut("Updated Address Line1: " + address.getLine1() + "/" + addressText);
			address.setLine1(addressText);
		}

		if (address.getLine2() != null && !address.getLine2().equals("")) {
			String addressLine2Text = getTextOrValueFromElement(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')] | //div[contains(@id, ':AddressLine1-inputEl')]"));
			if (!address.getLine2().equals(addressLine2Text)) {
				returnValue = true;
				systemOut("Updated Address Line2: " + address.getLine2() + "/" + addressLine2Text);
				address.setLine2(addressLine2Text);
			}
		}

		String cityText = getTextOrValueFromElement(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')] | //div[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]"));
		if (!address.getCity().equals(cityText)) {
			returnValue = true;
			systemOut("Updated Address City: " + address.getCity() + "/" + cityText);
			address.setCity(cityText);
		}

		String zipText = getTextOrValueFromElement(By.xpath("//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')] | //div[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]"));
		if (!address.getZip().equals(zipText)) {
			if (zipText.contains(address.getZip())) {
				systemOut("Updated Address Zip: " + address.getZip() + "/" + zipText);
				address.setZip(zipText);
			} else {
				returnValue = true;
				systemOut("Updated Address Zip: " + address.getZip() + "/" + zipText);
				address.setZip(zipText);
			}
		}

		String countyText = getTextOrValueFromElement(By.xpath("//input[contains(@id, ':County-inputEl')] | //div[contains(@id, ':County-inputEl')]"));
		if (!address.getCounty().equals(countyText)) {
			returnValue = true;
			systemOut("Updated Address County: " + address.getCounty() + "/" + countyText);
			address.setCounty(countyText);
		}
		return returnValue;
	}

	// verify all required field of the address are filled out.
	// some contacts the fields are not editable so method will just skip past.
	public boolean verifyAddressComplete() {
		boolean setField = false;
		try {
			String countyText = getTextOrValueFromElement(find(By.xpath("//input[contains(@id, ':County-inputEl')]")));
			if (countyText == null || countyText.equalsIgnoreCase("")) {
				find(By.xpath("//input[contains(@id, ':County-inputEl')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"), "Bannock");
				setField = true;
			}
			String addressText = getTextOrValueFromElement(find(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")));
			if (addressText == null || addressText.equalsIgnoreCase("")) {
				find(By.xpath("//input[contains(@id, ':AddressLine1-inputEl')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"), "123 Main St");
				setField = true;
			}
			String cityText = getTextOrValueFromElement(find(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")));
			if (cityText == null || cityText.equalsIgnoreCase("")) {
				find(By.xpath("//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"), "Pocatello");
				setField = true;
			}
			String addressTypeText = new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]").getText();
			if (addressTypeText == null || addressTypeText.equalsIgnoreCase("") || addressTypeText.equalsIgnoreCase("<none>")) {
				new Guidewire8Select(driver, "//table[contains(@id, ':AddressType-triggerWrap')]").selectByVisibleText(AddressType.Business.getValue());
				setField = true;
			}
			String zipText = getTextOrValueFromElement(find(By.xpath("//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")));
			if (zipText == null || zipText.equalsIgnoreCase("")) {
				find(By.xpath("//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")).sendKeys(Keys.chord(Keys.CONTROL + "a"), "83201");
				setField = true;
			}
			String stateText = new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]").getText();
			if (stateText == null || stateText.equalsIgnoreCase("") || stateText.equalsIgnoreCase("<none>")) {
				new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]").selectByVisibleText(State.Idaho.getName());
				setField = true;
			}
		} catch (Exception e) {
		}
		return setField;
	}

	/**
	 * Logs out of the current application
	 */
	public void logout() {
		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();
	}

	/**
	 * This method is used after opening a new window, or clicking a link or button that opens a new window.
	 * It will save off the original window handle for the page you are leaving, and then cycle through all open windows
	 * until it finds the window containing the title that is passed in. It then returns the original window
	 * handle for use later when switching back to the original window.
	 *
	 * @param newWindowTitle String value of the title of the window you are switching to.
	 * @return String value of the window handle of the page you are leaving.
	 */
	public String switcWebDriverWindow(String newWindowTitle) {
		String mainPCWindow = driver.getWindowHandle();

		for (String windowHandle : driver.getWindowHandles()) {
			driver.switchTo().window(windowHandle);
			if (driver.getTitle().contains(newWindowTitle)) {
				systemOut("Switching to new window containing title: " + newWindowTitle);
				break;
			}
		}
		return mainPCWindow;
	}
	
	public String sanitizeFilePath(String fullFilePath) {
    	if (((RemoteWebDriver)getDriver()).getCapabilities().getPlatform().is(Platform.LINUX)) {
    		fullFilePath = fullFilePath.replace("\\", "/");
    		if (fullFilePath.startsWith("//")) {
    			fullFilePath = fullFilePath.substring(1);
    			if (fullFilePath.contains(".idfbins.com")) {
    				String[] stringSplit = fullFilePath.split(".idfbins.com");
    				fullFilePath = "/fbmsqa11" + stringSplit[1];
    			}
    		}
    	} else if (((RemoteWebDriver)getDriver()).getCapabilities().getPlatform().is(Platform.WINDOWS)) {
    		if (fullFilePath.startsWith("/") || fullFilePath.matches(".:/")) {
    			fullFilePath = fullFilePath.replace("/", "\\");
        		if (fullFilePath.startsWith("\\") && !fullFilePath.startsWith("\\\\")) {
        			fullFilePath = "\\" + fullFilePath;
        		}
    		}
    	}
		return fullFilePath;
	}
}
