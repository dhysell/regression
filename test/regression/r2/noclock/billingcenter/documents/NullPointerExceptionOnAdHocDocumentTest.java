package regression.r2.noclock.billingcenter.documents;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.actions.BCCommonActionsCreateNewDocumentFromTemplate;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DocumentTemplateName;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ReturnCheckReason;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author JQU
* @Requirement DE6018-NullPointerException on Ad-Hoc Documents - No Insured Invoice
* 				Navigate to an account that is fully lien billed (has no insured invoices at all).
				Select "New Document -> Create New Document From Template" from the Account/Policy Actions menu.
				Press the Search button inside the "Document Template" field.
				Select "Ad Hoc Document" for the "Document Type" in the "New Document" tab.
				Press the "Search" button in the "New Document" tab.
				Choose one of the 4 Ad Hoc Document types.
				Fill in (required/desired) fields.
				Press the "Create Document" button.
				Actual Result: "NullPointerException: null...".
				Expected Result: A document pops up.
* 
* @DATE August 23, 2017*/
public class NullPointerExceptionOnAdHocDocumentTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private String loanNumber="LN12345";
	private ARUsers arUser = new ARUsers();
	private double amountToPay=NumberUtils.generateRandomNumberInt(100, 200);
	private String confirmationNumber="1234567";
	
	private void setPaymentAmountAndRelatedTo(BCCommonActionsCreateNewDocumentFromTemplate docTemplate, double paymentAmount, String accountNumber){
		docTemplate.setPaymentAmount(paymentAmount);
		docTemplate.selectRelatedTo(myPolicyObj.accountNumber);
	}
	@Test 
	public void generatePolicy() throws Exception {		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building1=new PolicyLocationBuilding();
		building1.setBuildingLimit(150000);
		building1.setBppLimit(150000);		
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Always);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));					

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Ad-HocDoc")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generatePolicy" })	
	public void createDocument() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuActionsCreateNewDocumentFromTemplate();		

		BCCommonActionsCreateNewDocumentFromTemplate newDocument = new BCCommonActionsCreateNewDocumentFromTemplate(driver);
		DocumentTemplateName documentName=newDocument.randomSelectDocumentTemplate(DocumentType.Ad_Hoc_Document);
		switch (documentName) {
		case Blank:				
			newDocument.setCopies("2");
			newDocument.selectRelatedTo(myPolicyObj.accountNumber);
			break;
		case MoneyOnCancelledPolicy:			
			setPaymentAmountAndRelatedTo(newDocument, amountToPay, myPolicyObj.accountNumber);
			break;
		case OnlinePymtConfirmation:
			newDocument.setConfirmationNumber(confirmationNumber);
			setPaymentAmountAndRelatedTo(newDocument, amountToPay, myPolicyObj.accountNumber);
			newDocument.setEffectiveDate(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter));
			break;
		case PolicyLienHolderReturningCheck:
			newDocument.setCheckNumber(confirmationNumber);
			newDocument.selectReturnReason(ReturnCheckReason.Other);
			newDocument.setReturnedReasonOther("whatever");
			setPaymentAmountAndRelatedTo(newDocument, amountToPay, myPolicyObj.accountNumber);
			break;
		}			
		try{				
			newDocument.clickCreateDocument();	
		}catch(Exception e){
			Assert.fail("Create document from template "+"'"+documentName+"'"+" failed.");
		}		
	}
}
