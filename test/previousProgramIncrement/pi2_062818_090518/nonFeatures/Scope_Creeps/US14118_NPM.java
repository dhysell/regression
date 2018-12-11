package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiablityCoverageNamedPersonsMedicalPerson;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;

import java.util.ArrayList;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/197934445352">US14118 - MOVING TO 2.2 DUE TO DEPENDENCY - Changes to Named Persons Medical functionality</a>
 * @Description
-On Named Persons Medical we need the ability to also add a name. The person does not have to be on the policy.

DEPENDENCY: US14136 for claims/Aces team.

Add a search screen to the field. User can search AB and select or create a new contact. --------WAIT NO: Do a free form box instead. Can we do similar to the 304 driver exclusion free form box? YES PLEASE

Note: Need to address the following error message - If the user tries to remove contact from policy member that exist on NPM display blocking error "Remove the individual from Named Persons Medical before removing them from Policy Members". - Do we want an UW issue instead ?  6-21-18 meeting with Lisa. Yes INSTEAD OF BLOCKING, Please do an  UW issue for UW to clarify and decide if persons need removed from NPM.  

Revision after 6-21-18 meeting for above question. We DO NOT want a block error. Just an UW issue that reads: "A person with named persons medical has been removed as a policy member".  

Revision after 7-9-18 DEV discussion - this will be a screen validation NOT an UW issue or any type of blocker.

______________________________________________________________________________

Steps:

Create a new submission with Section II
Enter all required information
On Coverage wizard >> Section II Coverage tab >> Click Add Coverage
Select Section II Coverages as category and hit 'Search' button
Select Named persons medical Coverage from the displayed list and hit 'Add Selected Coverages' button


Requirements: PC8 - HO - QuoteApplication - Liability Additional Coverages (Refer UI Final tab)

Product Model: Personal Lines Product Model (Added PGL020 on Bus Rules and UW Issues tab)

Added Coverages to Section II

Add screen validation "Please add {name of person} in Named Persons Medical using the free form box on Named Persons Medical.


Acceptance Criteria:

Ensure that under Named Persons Medical, there are Add Existing, Add Policy Member (free form) and Remove buttons

1. Add Existing:

    Ensure that Add Existing button is a drop down option and it will list all the policy members listed on the policy members screen
    Ensure that the format display shows the Full Name & Birth Date

2. Add Policy Member (free form):

    Ensure that user can add a free form name if the person does not exist as a policy member. 
    Ensure that user is not required to add all info like a policy member, Only Free form name and Date of Birth is required. 


3. Remove:

    Ensure that Remove button is grayed out until user checks the check box next to at least one policy member

Ensure that UW issue (PGL020) trigger per requirements.

Ensure that after the user adds a contact through the Add Policy Member (free form) or Add Existing button

        Ensure that Under the Full Name column and Date of Birth cloumn, displays the contact name and birth date in the following format
            ${Contact First Name} ${Contact Last Name}    OR
            ${Contact First Name} ${Contact Middle Name} ${Contact Last Name}
            MM/DD/YYYY
        Ensure that once the user adds the contact, the name is NOT editable. Since only UW will be able to add this exclusion, we do not have to display the contact name as a link. If UW makes a typo on the contact name, the UW would have to contact AB  6-21-18 checking with Lisa on this rule 6-28-18 This is not accurate - an agent or PA can absolutely add named persons medical. CSR & SA cannot add. Also, talking with Dayna O the UW should not need to contact AB.
        Ensure that if agent/PA submits with wrong spelling and UW correct that AB updates.
        At this time the SSN is not required per UW. It will be looked at in the future.


Acceptance Criteria:

    Ensure that user (agent, PA, underwriter) can add to named persons medical from the existing drop down
    Ensure that user (agent, PA, underwriter) can add to named persons medical with the free form box. Will be required to put name and date of birth (00/00/0000)
    Ensure that user (agent, PA, underwriter) can remove any named persons medical by checking the box and then the Remove button is available.
    Ensure that when user (agent, PA, underwriter) removes a policy member that is on named persons medical that the screen validation is triggered
 * @DATE July 16, 2018
 */

public class US14118_NPM extends BaseTest {

    public GeneratePolicy myPolicyObject = null;

    @Test(enabled = true)
    public void NPMQuickQuote() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
  
            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withInsFirstLastName("ScopeCreeps", "NonFeature")
                    .build(GeneratePolicyType.QuickQuote);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,myPolicyObject.agentInfo.agentPassword,myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages pcSquireSectionIICoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        
        new GuidewireHelpers(driver).editPolicyTransaction();
        
        pcSquireSectionIICoverages.clickSectionIICoveragesTab();
        
        SectionIICoverages coverages = new SectionIICoverages(SectionIICoveragesEnum.NamedPersonsMedical, 1000, 1);
        pcSquireSectionIICoverages.addCoverages(coverages);        
        
        // Acceptance Criteria: Ensure that user (agent, PA, underwriter) can add to named persons medical with the free form box. Will be required to put name and date of birth (00/00/0000)
        Assert.assertTrue(driver.getPageSource().contains("Add Policy Member (Not Search)"), "Free-form entry for NPM not visible!");
        Assert.assertTrue(driver.findElements(By.xpath("//span[contains(@id, 'Remove-btnInnerEl')]/ancestor::a[1][contains(@class, 'disabled')]")).size() == 1, "Remove button was not disabled, it should be");
        
        pcSquireSectionIICoverages.setNamedPersonsMedical(myPolicyObject.pniContact.getLastName());
        pcSquireSectionIICoverages.checkBoxInNamedPersonsMedicalTable(myPolicyObject.pniContact.getLastName());
        // Acceptance Criteria: Ensure that user (agent, PA, underwriter) can add to named persons medical from the existing drop down
        // Acceptance Criteria: Ensure that user (agent, PA, underwriter) can remove any named persons medical by checking the box and then the Remove button is available.
        Assert.assertTrue(driver.findElements(By.xpath("//span[contains(@id, 'Remove-btnInnerEl')]/ancestor::a[1][contains(@class, 'disabled')]")).size() == 0, "Remove button was still disabled, it should not be");
    }
    
    @Test(enabled = true)
    public void NPMPolicyIssued() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        
        WebDriver driver = buildDriver(cf);
        
        SectionIICoverages sectionIICoverages = new SectionIICoverages(SectionIICoveragesEnum.NamedPersonsMedical, 1000, 1);
        SquireLiablityCoverageNamedPersonsMedicalPerson namedPersonMedical = new SquireLiablityCoverageNamedPersonsMedicalPerson();
        namedPersonMedical.setDateOfBirth(DateUtils.convertStringtoDate("01/01/1980", "mm/dd/yyyy"));
        namedPersonMedical.setFullName("NonFea");
        ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson> npmList = new ArrayList<SquireLiablityCoverageNamedPersonsMedicalPerson>();
        npmList.add(namedPersonMedical);
        sectionIICoverages.setCoverageNamedPersonsMedicalPersons(npmList);
        Squire mySquire = new Squire();
        
        mySquire.propertyAndLiability.liabilitySection.getSectionIICoverageList().add(sectionIICoverages);


            myPolicyObject = new GeneratePolicy.Builder(driver)
                    .withProductType(ProductLineType.Squire)
                    .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                    .withPolOrgType(OrganizationType.Individual)
                    .withDownPaymentType(PaymentType.Cash)
                    .withSquire(mySquire)
                    .withInsFirstLastName("ScopeC", "NonFeature")
                    .build(GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,myPolicyObject.agentInfo.agentPassword,myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages pcSquireSectionIICoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        
        policyChangePage.startPolicyChange("Add NPM", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        pcSideMenu.clickSideMenuSquirePropertyCoverages();        
        
        pcSquireSectionIICoverages.clickSectionIICoveragesTab();
        
        pcSquireSectionIICoverages.checkBoxInNamedPersonsMedicalTable(myPolicyObject.pniContact.getLastName());
        pcSquireSectionIICoverages.clickRemoveNamedPersonsMedicalButton();	
        
        pcWorkOrder.clickGenericWorkorderQuote();
        
        // Acceptance Criteria: Ensure that when user (agent, PA, underwriter) removes a policy member that is on named persons medical that the screen validation is triggered
        Assert.assertTrue(driver.getPageSource().contains("Named Persons Medical must contain at least one scheduled item"), "Quote Validation for needing a NPM scheduled item did not show");
    }
}




















