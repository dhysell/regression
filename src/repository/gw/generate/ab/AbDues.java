package repository.gw.generate.ab;


import com.idfbins.enums.CountyIdaho;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.ab.contact.ContactDetailsPaidDuesAB;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;

import java.text.ParseException;
import java.util.Date;

public class AbDues {

    private CountyIdaho duesCounty;
    private double duesAmount;
    private String accountNum;
    private String policyNum;
    private Date duesEffectiveDate;
    private Date duesExpireDate;
    private boolean paid;
    private Date policyEffectiveDate;

    //ToDo  Constructors


    private WebDriver driver;

    public AbDues(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setDuesCounty(CountyIdaho _duesCounty) {
        this.duesCounty = _duesCounty;
    }

    public CountyIdaho getDuesCounty() {
        return this.duesCounty;
    }

    public void setDuesAmount(double _duesAmount) {
        this.duesAmount = _duesAmount;
    }

    public double getDuesAmount() {
        return this.duesAmount;
    }

    public void setAccountNum(String _accountNum) {
        this.accountNum = _accountNum;
    }

    public String getAccountNum() {
        return this.accountNum;
    }

    public void setPolicyNum(String _policyNum) {
        this.policyNum = _policyNum;
    }

    public String getPolicyNum() {
        return this.policyNum;
    }

    public void setDuesEffectiveDate(Date _duesEffectiveDate) {
        this.duesEffectiveDate = _duesEffectiveDate;
    }

    public Date getDuesEffectiveDate() {
        return this.duesEffectiveDate;
    }

    public void setDuesExpireDate(Date _duesExpireDate) {
        this.duesExpireDate = _duesExpireDate;
    }

    public Date getDuesExpireDate() {
        return this.duesExpireDate;
    }

    public void setStatus(Boolean _paid) {
        this.paid = _paid;
    }

    public void setStatus(String status) {
        this.paid = status.contains("Paid");
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public void setPolicyEffectiveDate(Date _policyEffectiveDate) {
        this.policyEffectiveDate = _policyEffectiveDate;
    }

    public Date getPolicyEffectiveDate() {
        return this.policyEffectiveDate;
    }
/*
    public void searchContactGetToDues(String firstName, String lastName, String addressLine1) {
        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

        menu.clickSearchTab();
        search.clickReset();
        search = new AdvancedSearchAB(driver);
        search.selectSpecificComboBox_ContactType("Person");
        search.setLastName(lastName);
        search.setFirstName(firstName);
        search.clickSearch();
        search.clickAdvancedSearchPersonSearchResults(firstName, lastName, addressLine1);

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
        String contactName = policyContact.getContactPageTitle();
        if (!contactName.equals(firstName + " " + lastName)) {
            Assert.fail("Unable to find " + firstName + " " + lastName + ".");
        }
        policyContact.clickContactDetailsBasicsPaidDuesLink();
    }
*/
    public void findDuesByPolicyEffectiveDate(String policyEffectiveDate) throws ParseException {
        ContactDetailsPaidDuesAB myDues = new ContactDetailsPaidDuesAB(driver);
        int row = myDues.findDuesRowInTableByText(policyEffectiveDate);
        setDuesCounty(CountyIdaho.valueOf(myDues.getDuesCounty(row)));
        setDuesAmount(NumberUtils.getCurrencyValueFromElement(myDues.getDuesAmount(row)));
        setAccountNum(myDues.getDuesAccountNumber(row));
        setPolicyNum(myDues.getDuesPolicyNumber(row));
        setDuesEffectiveDate(repository.gw.helpers.DateUtils.convertStringtoDate(myDues.getDuesEffectiveDate(row), "MM/dd/yyyy"));
        setDuesExpireDate(repository.gw.helpers.DateUtils.convertStringtoDate(myDues.getDuesEffectiveDate(row), "MM/dd/yyyy"));
        setStatus(myDues.getDuesStatus(row));
        setPolicyEffectiveDate(DateUtils.convertStringtoDate(policyEffectiveDate, "MM/dd/yyyy"));
    }
}
