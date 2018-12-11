package scratchpad.steve.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.AddressInfo;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class EmailPhoneUpdateScriptTest extends BaseTest {

    private ArrayList<String> updates = new ArrayList<>();
    private String log = "";
    private WebDriver driver;

    public void readFile() {

        try {
//				        	\\fbmis141vm-gw8.idfbins.com\tmp
            File f = new File("\\\\fbmis141vm-gw8.idfbins.com\\tmp\\contacts_updated.csv");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                this.updates.add(readLine);
            }
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //problem with 5670
    }

    private ContactsUpdated createContactObjFromString(int lineItem) {
        ContactsUpdated updatedContact = new ContactsUpdated();
        if (this.updates.get(lineItem).contains("Updated main email")) {
            updatedContact.setUpdatedEmail(true);
            if (this.updates.get(lineItem).contains("--")) {
                String[] parsedItem = this.updates.get(lineItem).split(" -- ");
                if (parsedItem[1].contains("\"")) {
                    updatedContact.setEmail(parsedItem[1].replace("\"", ""));
                } else {
                    updatedContact.setEmail(parsedItem[1]);
                }
            }
        } else {
            updatedContact.setUpdatedEmail(false);
            if (this.updates.get(lineItem).contains("on address")) {
                updatedContact.setAddress(parseAddress(this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf("on address"))));
                if (this.updates.get(lineItem).contains("--")) {
                    String[] parsedItem = this.updates.get(lineItem).split(" -- ");
                    String[] udpatedItems = parsedItem[1].split(";");
                    for (String item : udpatedItems) {
                        if (!item.equals("\"")) {
                            String[] value = item.split("=");
                            updatedContact.setPhoneNumber(value[0], value[1]);
                        }
                    }
                }
            }
        }
        String chopEnds;
        if (this.updates.get(lineItem).contains(" for contact ")) {

            int indexOf = this.updates.get(lineItem).indexOf(" for contact ");
            System.out.println("The index of for contact is " + indexOf + "         " + this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf(" for contact ")));
            if (this.updates.get(lineItem).contains(" on account ")) {
                chopEnds = this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf(" for contact ") + 13, this.updates.get(lineItem).indexOf(" on account "));
            } else {
                chopEnds = this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf(" for contact ") + 13, this.updates.get(lineItem).indexOf(" -- "));
            }
        } else {
//            int indexOf = this.updates.get(lineItem).indexOf(" on contact ");
            if (this.updates.get(lineItem).contains(" on account ")) {
                chopEnds = this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf(" on contact ") + 12, this.updates.get(lineItem).indexOf(" on account "));
            } else {
                chopEnds = this.updates.get(lineItem).substring(this.updates.get(lineItem).indexOf(" on contact ") + 12, this.updates.get(lineItem).indexOf(" -- "));
            }
        }
        String[] nameArray = chopEnds.split(" ");
        updatedContact.setFirstName(nameArray[0]);
        updatedContact.setMiddleName(nameArray[nameArray.length - 2]);
        updatedContact.setLastName(nameArray[nameArray.length - 1]);

        if (this.updates.get(lineItem).contains("on account")) {
            String[] parsedItem = this.updates.get(lineItem).split("on account ");
            updatedContact.setAccount(parsedItem[1].substring(0, 6));
        }
        return updatedContact;
    }

    @Test
    public void verifyChanges() {
        readFile();
        ContactsUpdated updatedContact = new ContactsUpdated();
        for (int i = this.updates.size() - 1; i > 3289; i--) {
            System.out.println("i=" + i);
            try {
                updatedContact = createContactObjFromString(i);
                AbUsers user = AbUserHelper.getRandomDeptUser("Admin");
                Config cf = new Config(ApplicationOrCenter.ContactManager);
                driver = buildDriver(cf);
                AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
				
		/*		if(this.contact.isContactIsCompany()) {
					//loginAndSearchContact(AbUsers abUser, String firstName, String lastName, String address, State state)
					searchMe.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Admin"), "", this.contact.getContactName(), this.contact.getContactAddressLine1(), State.valueOfName(this.contact.getContactState()));
				} else {
		*/
                if (updatedContact.getAccount() != null) {
                    searchMe.loginAndSearchByAccountNumber(user, updatedContact.getAccount(), updatedContact.getLastName() + ", " + updatedContact.getFirstName());
                } else {
                    searchMe.loginAndSearchContact(user, updatedContact.getFirstName(), updatedContact.getLastName(), updatedContact.getAddress().getLine1(), updatedContact.getAddress().getState());
                }
                ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
                if (updatedContact.getEmail() != null) {
                    if (!basicsPage.getContactDetailsBasicsMainEmail().equals(updatedContact.getEmail())) {
                        log += "After Running the script the emails should match.";
                        break;
                    }
                    PageLinks links = new PageLinks(driver);
                    links.clickContactDetailsBasicsHistoryLink();
                    ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
                    history.verifyHistoryTypeUserDescriptionNewValue("Email Address Added", "Main Email Address Added", "System User", "Main Email", updatedContact.getEmail());
                } else {
                    basicsPage.clickContactDetailsBasicsAddressLink();
                    ContactDetailsAddressesAB addressTab = new ContactDetailsAddressesAB(driver);
                    ArrayList<String> phoneTypes = new ArrayList<String>();
                    phoneTypes.add("Business");
                    phoneTypes.add("Work");
                    phoneTypes.add("Home");
                    phoneTypes.add("Cell");
                    phoneTypes.add("Fax");
                    for (String phoneType : phoneTypes) {
                        if (updatedContact.getPhoneNumber(phoneType) != null) {
                            if (phoneType.equals("Cell")) {
                                if (!addressTab.getPhoneOnAddress("Mobile", updatedContact.getAddress().getLine1()).equals(updatedContact.getPhoneNumber("Cell"))) {
                                    Assert.fail("After running the update batch job, the phone numbers should match \r\n The contacts name is " + updatedContact.getFirstName() + " " + updatedContact.getLastName());
                                }
                                PageLinks links = new PageLinks(driver);
                                links.clickContactDetailsBasicsHistoryLink();
                                ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
                                history.verifyHistoryTypeUserDescriptionNewValue("Phone Number Added", "Cell Number Added", "System User", "Main Email", updatedContact.getEmail());
                            } else {
                                if (!addressTab.getPhoneOnAddress(phoneType, updatedContact.getAddress().getLine1()).equals(updatedContact.getPhoneNumber(phoneType))) {
                                    Assert.fail("After running the update batch job, the phone numbers should match \r\n The contacts name is " + updatedContact.getFirstName() + " " + updatedContact.getLastName());
                                }
                                PageLinks links = new PageLinks(driver);
                                links.clickContactDetailsBasicsHistoryLink();
                                ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
                                history.verifyHistoryTypeUserDescriptionNewValue("Phone Number Added", "Cell Number Added", "System User", "Main Email", updatedContact.getEmail());
                            }
                        }
                    }
                    PageLinks links = new PageLinks(driver);
                    links.clickContactDetailsBasicsHistoryLink();
                    ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
                    history.verifyHistoryTypeUserDescriptionNewValue("Phone Number Email Address Added", "Main Email Address Added", "System User", "Main Email", updatedContact.getEmail());
                }
                System.out.println("Success!");

            } catch (Exception e) {
                this.log += "\r\n The contact: " + updatedContact.getFirstName() + " " + updatedContact.getLastName() + " was unable to be verified.  See line " + i + 1 + " \r\n";
            }

        }
        System.out.println(this.log);
    }

    private AddressInfo parseAddress(String lineItem) {
        String[] splitAddress = lineItem.split(", ");
        String addressLine1 = splitAddress[0].replace("on address ", "");
        String city;
        State state;
        String zip;
        if (splitAddress[1].contains("STE ")) {
            addressLine1 = addressLine1 + " " + splitAddress[1];
            city = splitAddress[2];
            state = State.valueOfAbbreviation(splitAddress[3].substring(0, 2));
            int indexOfZip = splitAddress[3].indexOf(" for contact ");
            zip = splitAddress[3].substring(3, indexOfZip);
        } else if (splitAddress[1].contains("355")) {
            addressLine1 = addressLine1 + ", " + splitAddress[1];
            city = splitAddress[2];
            state = State.valueOfAbbreviation(splitAddress[3].substring(0, 2));
            int indexOfZip = splitAddress[3].indexOf(" for contact ");
            zip = splitAddress[3].substring(3, indexOfZip);
        } else {
            city = splitAddress[1];
            state = State.valueOfAbbreviation(splitAddress[2].substring(0, 2));
            int indexOfZip = splitAddress[2].indexOf(" for contact ");
            zip = splitAddress[2].substring(3, indexOfZip);
        }
        return new AddressInfo(addressLine1, city, state, zip);
    }

}
