package scratchpad.denver.pc.test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Address;
import com.github.javafaker.Name;

import repository.cc.framework.gw.BaseOperations;
import repository.cc.framework.gw.pc.pages.PCIDs;
import repository.cc.framework.init.Environments;
import gwclockhelpers.ApplicationOrCenter;


public class PolicyIssueTest extends BaseOperations {

    @BeforeMethod
    public void setupTests() {
        super.initOn(ApplicationOrCenter.PolicyCenter, Environments.DEV);
    }

    @Test
    public void policyIssueTest() {
        interact.withTexbox(PCIDs.Login.USER_NAME).fill("darmstrong");
        interact.withTexbox(PCIDs.Login.PASSWORD).fill("gw");
        interact.withElement(PCIDs.Login.LOGIN_BUTTON).click();

        interact.withTabArrow(PCIDs.PolicyTab.POLICY_TAB_ARROW);
        interact.withElement(PCIDs.PolicyTab.NEW_SUBMISSION_LINK).click();

        Name customer = faker.name();

        interact.withTexbox(PCIDs.NewPolicy.NewSubmissions.NAME).fill(customer.firstName() + " " + customer.lastName());
        interact.withElement(PCIDs.NewPolicy.NewSubmissions.SEARCH_BUTTON).click();
        interact.withElement(PCIDs.NewPolicy.NewSubmissions.CREATE_NEW_BUTTON).click();
        interact.withElement(PCIDs.NewPolicy.NewSubmissions.PERSON_LINK).click();

        interact.withTexbox(PCIDs.NewPolicy.CreateAccount.DATE_OF_BIRTH).fill(new SimpleDateFormat("MM/dd/yyyy").format(faker.date().birthday(18, 80)));
        interact.withTexbox(PCIDs.NewPolicy.CreateAccount.SSN).fill(faker.idNumber().ssnValid());

        Address address = faker.address();
        interact.withTexbox(PCIDs.NewPolicy.CreateAccount.ADDRESS1).fill(address.streetAddress());
        interact.withTexbox(PCIDs.NewPolicy.CreateAccount.CITY).fill(address.city());
        interact.withSelectBox(PCIDs.NewPolicy.CreateAccount.STATE).select(address.state());
        interact.withTexbox(PCIDs.NewPolicy.CreateAccount.ZIP).fill(address.zipCode());
        interact.withSelectBox(PCIDs.NewPolicy.CreateAccount.ADDRESS_TYPE).select("Home");

        interact.withElement(PCIDs.NewPolicy.CreateAccount.UPDATE_BUTTON).click();

        if (interact.withOptionalElement(PCIDs.NewPolicy.MatchingContacts.RETURN_TO_CREATE_ACCOUNT_LINK).isPresent()) {
            interact.withOptionalElement(PCIDs.NewPolicy.MatchingContacts.RETURN_TO_CREATE_ACCOUNT_LINK).click();
            interact.withElement(PCIDs.NewPolicy.CreateAccount.UPDATE_BUTTON).click();
        }

        interact.withSelectBox(PCIDs.NewPolicy.NewSubmissions.QUOTE_TYPE).select("Full Application");
        interact.withTable(PCIDs.NewPolicy.NewSubmissions.PRODUCT_OFFERS).getRowWithText("Squire").clickSelectButton();

        interact.withTable(PCIDs.NewPolicy.SquireEligibility.PERSONAL_LINES_SQUIRE_ELIGIBILITY_QUESTIONS).getRowWithText("Does the applicant have any farm revenue?").clickRadioWithLabel("Yes");
        interact.withTable(PCIDs.NewPolicy.SquireEligibility.PERSONAL_LINES_SQUIRE_ELIGIBILITY_QUESTIONS).getRowWithText("Is the total annual revenue greater than $40,000?").clickRadioWithLabel("Yes");
        interact.withTable(PCIDs.NewPolicy.SquireEligibility.PERSONAL_LINES_SQUIRE_ELIGIBILITY_QUESTIONS).getRowWithText("Is the custom farming annual revenue greater than 20% of your total annual revenue?").clickRadioWithLabel("No");

        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();
        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();

        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_PREQUAL_QUESTIONS).getRowWithText("Have you ever had insurance cancelled, refused or declined?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_PREQUAL_QUESTIONS).getRowWithText("Have you filed for bankruptcy in the last five (5) years?").clickRadioWithLabel("No");

        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Have you had any property losses in the past three (3) years?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Have you had prior property insurance?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Reason for no prior insurance").getCell(2).fillTextArea("Reason for no insurance box.");
        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Is there any existing damage to insured property?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Do you want flood insurance?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.PROPERTY_PREQUAL_QUESTIONS).getRowWithText("Is there business conducted in any buildings?").clickRadioWithLabel("No");

        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Have you had any liability losses in the past three (3) years?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Any special hazards on premises such as water features, slides or zip-lines?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Any manufacturing, processing, or retailing on premises?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Do you board or pasture other people's horses?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Do you own livestock?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.GENERAL_LIABILITY_PREQUAL_QUESTIONS).getRowWithText("Do you feed cattle for others on your premises?").clickRadioWithLabel("No");

        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Have you had any Auto losses in the past three (3) years?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Has any driver received any moving traffic citations in the last three (3) years?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Has any driver been charged with a felony within the last five (5) years?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Are any vehicles used for the following business purposes?").clickRadioWithLabel("Yes");
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Contractor?").getCell(2).clickCheckbox();
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Real Estate Agents?").getCell(2).clickCheckbox();
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Sales?").getCell(2).clickCheckbox();
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText(" Are there any employees that drive these vehicles?").clickRadioWithLabel("No");
        interact.withTable(PCIDs.NewPolicy.Qualification.AUTO_PREQUAL_QUESTIONS).getRowWithText("Are any owned vehicles insured with Hagerty or American Modern?").clickRadioWithLabel("No");

        interact.withTable(PCIDs.NewPolicy.Qualification.INLAND_MARINE_PREQUAL_QUESTIONS).getRowWithText("Have you had any Inland Marine losses in the past three (3) years?").clickRadioWithLabel("No");

        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.PolicyInfo.ORDER_PREFILL_REPORT_BUTTON).click();
        interact.withElement(PCIDs.NewPolicy.OrderPrefillReportFromVerisk.CANCEL_BUTTON).click();
        interact.withConfirmationWindow().clickOkButton();

        interact.withSelectBox(PCIDs.NewPolicy.PolicyInfo.ORGANIZATION_TYPE).select("Individual");
        interact.withSelectBox(PCIDs.NewPolicy.PolicyInfo.RATING_COUNTY).select("Bannock");
        interact.withSelectBox(PCIDs.NewPolicy.PolicyInfo.TERM_TYPE).select("Annual");
        interact.withTexbox(PCIDs.NewPolicy.PolicyInfo.EFFECTIVE_DATE).fill(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();
        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();

        interact.withSelectBox(PCIDs.NewPolicy.InsuranceScore.ORGANIZATION_TYPE).selectRandom();
        interact.withElement(PCIDs.NewPolicy.InsuranceScore.ORDER_INSURANCE_REPORT_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.Locations.NEW_LOCATION_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.LocationInformation.STANDARDIZE_ADDRESS).click();
        interact.withTexbox(PCIDs.NewPolicy.LocationInformation.ACRES).fill("2");
        interact.withTexbox(PCIDs.NewPolicy.LocationInformation.NUMBER_OF_RESIDENCE).fill("2");
        interact.withElement(PCIDs.NewPolicy.LocationInformation.OK_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.NewSubmissions.NEXT_BUTTON).click();

        interact.withElement(PCIDs.NewPolicy.PropertyDetail.ADD_BUTTON).click();

        System.out.println();
    }
}
