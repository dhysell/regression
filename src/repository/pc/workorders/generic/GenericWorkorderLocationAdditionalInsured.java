package repository.pc.workorders.generic;

import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.enums.AddressType;
import repository.gw.enums.CreateNew;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.custom.PolicyLocationAdditionalInsured;
import repository.gw.helpers.GuidewireHelpers;
import repository.pc.search.SearchAddressBookPC;

import java.util.List;

public class GenericWorkorderLocationAdditionalInsured extends GenericWorkorderAdditionalInsured {
    private WebDriver driver;

    public GenericWorkorderLocationAdditionalInsured(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // @FindBy(xpath = "//span[contains(@id,
    // 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    // private static WebElement button_Override;

    @FindBy(xpath = "//span[contains(@id, 'AddressStandardizationPopup:LocationScreen:Update-btnEl')]")
    private List<WebElement> button_Override;

    @FindBy(xpath = "//span[contains(@id, ':FinishPCR-btnEl')]")
    private List<WebElement> button_Update;


    public void addAdditionalInsured(boolean basicSearch, PolicyLocationAdditionalInsured additonalInsured, Boolean retry) throws GuidewireException {
        

        GenericWorkorderAdditionalInsured ai = new GenericWorkorderAdditionalInsured(driver);

        ai.clickSearch();
        systemOut("setRolesAndAIAddress");
        setRolesAndAIAddress(additonalInsured, findContact(basicSearch, additonalInsured));
        systemOut("setSpecialAndSubroQuestions");
        setSpecialAndSubroQuestions(additonalInsured);
        systemOut("setLocationAIQuestions");
        setLocationAIQuestions(additonalInsured);
        
        System.out.println("setRelatedContact");
        repository.pc.search.SearchAddressBookPC searchPC = new repository.pc.search.SearchAddressBookPC(driver);
        if(additonalInsured.getRelationship() != null) {
        	searchPC.addRelatedContact(additonalInsured.getRelatedContact(), additonalInsured.getRelationship());
        }
        systemOut("completeLocAIpage");
        completeLocAIpage();
        
    }


    public void addAdditionalInsured(boolean basicSearch, PolicyLocationAdditionalInsured additonalInsured) {
        GenericWorkorderAdditionalInsured ai = new GenericWorkorderAdditionalInsured(driver);
        ai.clickSearch();

        setRolesAndAIAddress(additonalInsured, findContact(basicSearch, additonalInsured));
        setSpecialAndSubroQuestions(additonalInsured);

        setLocationAIQuestions(additonalInsured);

        completeLocAIpage();
    }

    @SuppressWarnings("unused")
    private void resetLocationAIPage() {
        System.out.println("Running resetLocationAIPage");
        if (finds(By.xpath("//a[contains(@id, 'AddlInsuredLocContactSearchPopup:__crumb__')]"))
                .size() > 0) {
            clickWhenClickable(find(By.xpath("//a[contains(@id, 'AddlInsuredLocContactSearchPopup:__crumb__')]")));
            selectOKOrCancelFromPopup(OkCancel.OK);
        } else if (finds(
                By.xpath("//a[contains(@id, 'EditPolicyAddnlInsuredContactRoleForLocationPopup:__crumb__')]"))
                .size() > 0) {
            clickWhenClickable(find(By.xpath("//a[contains(@id, 'EditPolicyAddnlInsuredContactRoleForLocationPopup:__crumb__')]")));
        }

    }

    private Boolean findContact(boolean basicSearch, PolicyLocationAdditionalInsured additonalInsured) {
        System.out.println("Running findContact");
        String newCompName;
        String newFirstName;
        boolean found = false;
        repository.pc.search.SearchAddressBookPC searchAddressBookPage = new SearchAddressBookPC(driver);

        if (additonalInsured.getCompanyName() == null || additonalInsured.getCompanyName().equals("") || additonalInsured == null) {
            if (additonalInsured.getNewContact() == CreateNew.Create_New_Always) {
                newFirstName = additonalInsured.getPersonFirstName();// +
                // dateString;
                found = searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, newFirstName,
                        additonalInsured.getPersonLastName(), additonalInsured.getAddress().getLine1(),
                        additonalInsured.getAddress().getCity(), additonalInsured.getAddress().getState(),
                        additonalInsured.getAddress().getZip(), CreateNew.Create_New_Always);
            } else if (additonalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                newFirstName = additonalInsured.getPersonFirstName();// +
                // dateString;
                found = searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, newFirstName,
                        additonalInsured.getPersonLastName(), additonalInsured.getAddress().getLine1(),
                        additonalInsured.getAddress().getCity(), additonalInsured.getAddress().getState(),
                        additonalInsured.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist);
            } else {
                found = searchAddressBookPage.searchAddressBookByFirstLastName(basicSearch, additonalInsured.getPersonFirstName(),
                        additonalInsured.getPersonLastName(), additonalInsured.getAddress().getLine1(),
                        additonalInsured.getAddress().getCity(), additonalInsured.getAddress().getState(),
                        additonalInsured.getAddress().getZip(), CreateNew.Do_Not_Create_New);
            }
        } else {
            if (additonalInsured.getNewContact() == CreateNew.Create_New_Always) {
                newCompName = additonalInsured.getCompanyName();// + dateString;
                found = searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, newCompName,
                        additonalInsured.getAddress().getLine1(), additonalInsured.getAddress().getCity(),
                        additonalInsured.getAddress().getState(), additonalInsured.getAddress().getZip(),
                        CreateNew.Create_New_Always);
            } else if (additonalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist) {
                newCompName = additonalInsured.getCompanyName();// + dateString;
                found = searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, newCompName,
                        additonalInsured.getAddress().getLine1(), additonalInsured.getAddress().getCity(),
                        additonalInsured.getAddress().getState(), additonalInsured.getAddress().getZip(),
                        CreateNew.Create_New_Only_If_Does_Not_Exist);
            } else if (additonalInsured.getNewContact() == CreateNew.Do_Not_Create_New) {
                found = searchAddressBookPage.searchAddressBookByCompanyName(basicSearch, additonalInsured.getCompanyName(),
                        additonalInsured.getAddress().getLine1(), additonalInsured.getAddress().getCity(),
                        additonalInsured.getAddress().getState(), additonalInsured.getAddress().getZip(),
                        CreateNew.Do_Not_Create_New);
            }
        }
        System.out.println(found);
        return found;
    }// END findContact


    private void setRolesAndAIAddress(PolicyLocationAdditionalInsured additonalInsured, Boolean found) {
        System.out.println("Running setRolesAndAIAddress");

        
        selectRoles(additonalInsured.getAiRole());
        
        if (additonalInsured.getNewContact() == CreateNew.Create_New_Always || (additonalInsured.getNewContact() == CreateNew.Create_New_Only_If_Does_Not_Exist && !found)) {
            setEditAdditionalInsuredLocAddressListing("New...");
            setEditAdditionalInsuredLocAddressLine1(additonalInsured.getAddress().getLine1());
            setEditAdditionalInsuredLocAddressCity(additonalInsured.getAddress().getCity());
            setEditAdditionalInsuredLocAddressState(additonalInsured.getAddress().getState());
            setEditAdditionalInsuredLocAddressZipCode(additonalInsured.getAddress().getZip());
            setEditAdditionalInsuredLocAddressAddressType(additonalInsured.getAddress().getType());
            
            new GuidewireHelpers(driver).verifyAddressComplete();
        } else {
//			String addressListingToSelect = "(" + additonalInsured.getAddress().getState().getAbbreviation() + ") "	+ additonalInsured.getAddress().getCity() + " - " + additonalInsured.getAddress().getLine1();
            setEditAdditionalInsuredLocAddressListing(additonalInsured.getAddress().getDropdownAddressFormat());
        }
    }

    private void setSpecialAndSubroQuestions(PolicyLocationAdditionalInsured additonalInsured) {
        System.out.println("Running setSpecialAndSubroQuestions");
        
        setSpecialWordingRadio(additonalInsured.isSpecialWording());
        
        if (additonalInsured.isSpecialWording()) {
            setSpecialWording(additonalInsured.getSpecialWordingDesc());
        }
        
        setWaiverOfSubroBP04(additonalInsured.isWaiverOfSubro());
    }

    private void setLocationAIQuestions(PolicyLocationAdditionalInsured additonalInsured) {
        System.out.println("Running setLocationAIQuestions");

        switch (additonalInsured.getAiRole()) {
            case CoOwnerOfInsuredPremises:
            case ManagersOrLessorsOrPremises:
            case MortgageesAssigneesOrReceivers:
                break;
            case LessorOfLeasedEquipment:
                additonalInsured.setLessorLeasedEquipment(true);
                setLeasedEquipmentDescription(additonalInsured.getLessorLeasedEquipmentDesc());
            case OwnersOrOtherInterests:
                setStatesInsuredPerformsActivities(additonalInsured.getStatesList());
                setDescribeActivities(additonalInsured.getActivityDescription());
                setOilGasRadio(additonalInsured.getOilAndGas());
                setUndergroundTanksRadio(additonalInsured.getUndergroundTanks());
                setAircraftAirportRadio(additonalInsured.getAircraftAndAirport());
                setBridgeConstructionRadio(additonalInsured.getBridgeConstrusction());
                setFirearmsRadio(additonalInsured.getFirearms());
                setRailroadsRadio(additonalInsured.getRailroads());
                setDamsAndReservoirsRadio(additonalInsured.getDamsAndReservoirs());
                setMiningRadio(additonalInsured.getMining());
                break;
            case CertificateHolderOnly:
            case ControllingInterest:
            case DesignatedPersonOrOrganization:
            case GrantorOfFranchise:
            case StateOrPoliticalSubdivisions:
            case Vendors:
            default:
                break;
        }
    }

    private void setEditAdditionalInsuredLocAddressListing(String addressListing) {
        super.setContactEditAddressListing(addressListing);
    }

    private void setEditAdditionalInsuredLocAddressLine1(String address) {
        super.setContactEditAddressLine1(address);
    }

    private void setEditAdditionalInsuredLocAddressCity(String city) {
        super.setContactEditAddressCity(city);
    }

    private void setEditAdditionalInsuredLocAddressState(State state) {
        super.setContactEditAddressState(state);
    }

    private void setEditAdditionalInsuredLocAddressZipCode(String zip) {
        super.setContactEditAddressZipCode(zip);
    }

    private void setEditAdditionalInsuredLocAddressAddressType(AddressType addType) {
        super.setContactEditAddressAddressType(addType);
    }

    private void completeLocAIpage() {
        clickEditAdditionalInsuredLocOK();
        if (!button_Override.isEmpty()) {
            if (button_Override.get(0).isEnabled()) {
                clickWhenClickable(button_Override.get(0));
                clickEditAdditionalInsuredLocOK();

            }//end button_Override.get(0).isEnabled()
        }//end !button_Override.isEmpty()
        
        //Duplicate contacts page handling
        if (!finds(By.xpath("//span[contains(text(), 'Matching Contacts')]")).isEmpty()) {
            find(By.xpath("//a[contains(@id, 'DuplicateContactsPopup:__crumb__')]")).click();
            clickEditAdditionalInsuredLocOK();
        }//end duplicate contact
    }//end completeLocAIpage()

    private void clickEditAdditionalInsuredLocOK() {
        if (button_Update.isEmpty()) {
            clickOK();
        } else {
            clickWhenClickable(button_Update.get(0));
        }
    }
}




























