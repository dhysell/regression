package currentProgramIncrement.nonFeatures.Achievers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.globaldatarepo.entities.BulkLienChange;
import persistence.globaldatarepo.helpers.BulkLienChangeHelper;

public class US15361_QuoteWithoutDocumentsButton extends BaseTest {

	
	
	
	
	@Test(enabled=true)
    public void quoteWithoutDocumentsButton_AddressOnlyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withProductType(ProductLineType.Businessowners)
                .withBusinessownersLine(new PolicyBusinessownersLine())
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10))
                .withLineSelection(LineSelection.Businessowners)
                .build(GeneratePolicyType.PolicyIssued);
        
        BulkLienChange bulkChange = BulkLienChangeHelper.getRandomClassCode(); 
        new Login(driver).loginAndSearchPolicyByAccountNumber(bulkChange.getUserName(), bulkChange.getPassword(), myPolicyObj.accountNumber);
        new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Signature, "SORRY HAD TO CUT SOME PEOPLE.....YOU DREW THE SHORT STRAW", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 0), true);
        new StartCancellation(driver).clickViewPolicyLink();
        new StartPolicyChange(driver).startPolicyChange("CHANGE TO ADDRESS ONLY",  DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2));
        new GenericWorkorderPolicyInfo(driver).addNewAddress(new AddressInfo(true), "SEE IF I GETS THE RIGHT BUTTONS");
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        Assert.assertTrue(!new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':QuoteWithoutDocuments-btnInnerEl')]")).isEmpty(), "Quote without documents BUTTON WAS NOT AVAILABLE AFTER A PNI ADDRESS CHANGE");
        
        new GenericWorkorderRiskAnalysis(driver).clickQuoteWithoutDocuments();
        new SideMenuPC(driver).clickSideMenuForms();
        Assert.assertTrue(new GenericWorkorderForms(driver).getFormDescriptionsFromTable().isEmpty(), "QUOTING WITHOUT FORMS BUTTON GENERATED DOCUMENTS ON THE FORMS PAGE. THIS IS BAD. IT SHOULD NOT HAVE GENRATED ANYTHING");
	}
	
	//BULK LEIHN CHANGE
	@Test(enabled=true)
    public void quoteWithoutDocumentsButton_AddressAndOtherChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withProductType(ProductLineType.Businessowners)
                .withBusinessownersLine(new PolicyBusinessownersLine())
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10))
                .withLineSelection(LineSelection.Businessowners)
                .build(GeneratePolicyType.PolicyIssued);
        
        BulkLienChange bulkChange = BulkLienChangeHelper.getRandomClassCode(); 
        new Login(driver).loginAndSearchPolicyByAccountNumber(bulkChange.getUserName(), bulkChange.getPassword(), myPolicyObj.accountNumber);
        new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Signature, "SORRY HAD TO CUT SOME PEOPLE.....YOU DREW THE SHORT STRAW", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 0), true);
        new StartCancellation(driver).clickViewPolicyLink();
        new StartPolicyChange(driver).startPolicyChange("CHANGE TO ADDRESS AND THE BUILDNG",  DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2));
        new GenericWorkorderPolicyInfo(driver).addNewAddress(new AddressInfo(true), "SEE IF I GETS THE RIGHT BUTTONS");
        myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setBuildingLimit(125000);
        new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
        
        
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        Assert.assertTrue(!new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':QuoteWithoutDocuments-btnInnerEl')]")).isEmpty(), "Quote without documents BUTTON WAS NOT AVAILABLE AFTER A PNI ADDRESS CHANGE");
        
        new GenericWorkorderRiskAnalysis(driver).clickQuoteWithoutDocuments();
        new SideMenuPC(driver).clickSideMenuForms();
        Assert.assertTrue(new GenericWorkorderForms(driver).getFormDescriptionsFromTable().isEmpty(), "QUOTING WITHOUT FORMS BUTTON GENERATED DOCUMENTS ON THE FORMS PAGE. THIS IS BAD. IT SHOULD NOT HAVE GENRATED ANYTHING");
	}
	
	
	@Test(enabled=true)
    public void quoteWithoutDocumentsButton_NonAddressChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withProductType(ProductLineType.Businessowners)
                .withBusinessownersLine(new PolicyBusinessownersLine())
                .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10))
                .withLineSelection(LineSelection.Businessowners)
                .build(GeneratePolicyType.PolicyIssued);
        
        BulkLienChange bulkChange = BulkLienChangeHelper.getRandomClassCode(); 
        new Login(driver).loginAndSearchPolicyByAccountNumber(bulkChange.getUserName(), bulkChange.getPassword(), myPolicyObj.accountNumber);
        new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Signature, "SORRY HAD TO CUT SOME PEOPLE.....YOU DREW THE SHORT STRAW", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 0), true);
        new StartCancellation(driver).clickViewPolicyLink();
        new StartPolicyChange(driver).startPolicyChange("CHANGE SOMETHING OTHER THAN ADDRESS",  DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -2));
        myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).setBuildingLimit(125000);
        new GenericWorkorderBuildings(driver).updateBuilding(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0));
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        Assert.assertFalse(new GuidewireHelpers(driver).finds(By.xpath("//span[contains(@id, ':QuoteWithoutDocuments-btnInnerEl')]/parent::span/parent::span/parent::a[contains(@hidefocus, 'on')]")).isEmpty(), "Quote without documents BUTTON WAS AVAILABLE AFTER SOMETHING OTHER THAN PNI ADDRESS CHANGE");
	}
	
	
	
	
	
	
	
}
