package repository.pc.workorders.generic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;

import services.broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillClueADDSubjectType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.UIPersonalPrefillPartyReported;
import repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReported;
import repository.gw.generate.custom.UIPersonalPrefillPriorPolicyReportedVehicle;
import repository.gw.generate.custom.UIPersonalPrefillVehicleReported;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;

public class GenericWorkorderPrefillReportSummary extends GenericWorkorder {

    public GenericWorkorderPrefillReportSummary(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[contains(@id,'PrefillReportConfirmationPopup:__crumb__')]")
    private WebElement link_ReturnToOrderPrefillReport;

    @FindBy(xpath = "//div[@id='PrefillReportConfirmationPopup:PrefillReportConfirmationScreen:PartyPrefillLV-body']//table")
    private WebElement table_PartiesReport;

    private List<WebElement> getPartiesReportRows() {
        return table_PartiesReport.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[@id='PrefillReportConfirmationPopup:PrefillReportConfirmationScreen:VehiclePrefillLV-body']//table")
    private WebElement table_VehiclesReport;

    private List<WebElement> getVehiclesReportRows() {
        return table_VehiclesReport.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[@id='PrefillReportConfirmationPopup:PrefillReportConfirmationScreen:PriorPolicyPrefill:PriorPolicyPrefillLV-body']//table")
    private WebElement table_PriorPoliciesReport;

    private List<WebElement> getPriorPoliciesReportRows() {
        return table_PriorPoliciesReport.findElements(By.xpath(".//tr"));
    }

    @FindBy(xpath = "//div[@id='PrefillReportConfirmationPopup:PrefillReportConfirmationScreen:PriorPolicyPrefill:panelRefID:PriorPolicyVehicleInfoLV-body']//table")
    private WebElement table_PriorPoliciesVehiclesReport;

    private List<WebElement> getPriorPoliciesVehiclesReportRows() {
        return table_PriorPoliciesVehiclesReport.findElements(By.xpath(".//tr"));
    }


    public void clickReturnToOrderPrefillReport() {
        clickWhenClickable(link_ReturnToOrderPrefillReport);
    }

    
    
    public void usePrefill(GeneratePolicy policy) throws Exception {
		try {
			for (int i = 0; i < policy.prefillReport.getBrokerReport().getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().size(); i++) {
				DataprefillClueADDSubjectType person = policy.prefillReport.getBrokerReport().getReport().getDataprefillCCDriverDiscovery().getAdditionalDriverDiscovery().getSubject().get(i);
				String firstName = person.getName().getFirst();
				String lastName = person.getName().getLast();
				Date dob = DateUtils.convertStringtoDate(person.getBirthdate(), "MM/dd/yyyy");
				Gender gender = Gender.valueOfString(person.getGender().value());

				Contact newPerson2 = new Contact();
				newPerson2.setFirstName(firstName);
				newPerson2.setLastName(lastName);
				newPerson2.setRelationToInsured(RelationshipToInsured.SignificantOther);
				newPerson2.setDob(getDriver(), dob);
				newPerson2.setGender(gender);
				newPerson2.setFromPrefill(true);
				newPerson2.setAddress(policy.pniContact.getAddress());
				policy.squire.policyMembers.add(newPerson2);
			}
		} catch (Exception e) {
			Assert.fail("Prefill report does not have additional driver");
		}
		// END USEPREFILL()
	}
    
    
    
    
    
    

    public ArrayList<UIPersonalPrefillPartyReported> getPartiesReported() throws Exception {
        ArrayList<UIPersonalPrefillPartyReported> toReturn = new ArrayList<UIPersonalPrefillPartyReported>();

        for (WebElement we : getPartiesReportRows()) {
            UIPersonalPrefillPartyReported row = new UIPersonalPrefillPartyReported();
            row.setConsumerName(we.findElement(By.xpath(".//td[2]/div")).getText());
            String dobStr = we.findElement(By.xpath(".//td[3]/div")).getText();
            row.setDateOfBirth(DateUtils.convertStringtoDate(dobStr, "MM/dd/yyyy"));
            row.setGender(Gender.valueOfString(we.findElement(By.xpath(".//td[4]/div")).getText()));
            row.setSsn(we.findElement(By.xpath(".//td[5]/div")).getText());
            row.setLicenseState(State.valueOfName(we.findElement(By.xpath(".//td[6]/div")).getText()));
            row.setLicenseNumber(we.findElement(By.xpath(".//td[7]/div")).getText());

            toReturn.add(row);
        }

        return toReturn;
    }


    public ArrayList<UIPersonalPrefillVehicleReported> getVehiclesReported() throws Exception {
        ArrayList<UIPersonalPrefillVehicleReported> toReturn = new ArrayList<UIPersonalPrefillVehicleReported>();

        for (WebElement we : getVehiclesReportRows()) {
            UIPersonalPrefillVehicleReported row = new UIPersonalPrefillVehicleReported();
            row.setModelYear(Integer.valueOf(we.findElement(By.xpath(".//td[2]/div")).getText()));
            row.setMake(we.findElement(By.xpath(".//td[3]/div")).getText());
            row.setModel(we.findElement(By.xpath(".//td[4]/div")).getText());
            row.setVin(we.findElement(By.xpath(".//td[5]/div")).getText());
            String plateExpirationDateStr = we.findElement(By.xpath(".//td[6]/div")).getText();
            row.setPlateExpirationDate(DateUtils.convertStringtoDate(plateExpirationDateStr, "MM/dd/yyyy"));
            row.setWeight(we.findElement(By.xpath(".//td[7]/div")).getText());
            row.setPrice(new BigDecimal(NumberUtils.getCurrencyValueFromElement(we.findElement(By.xpath(".//td[8]/div")).getText())));
            row.setOwner(we.findElement(By.xpath(".//td[9]/div")).getText());

            toReturn.add(row);
        }

        return toReturn;
    }


    public ArrayList<UIPersonalPrefillPriorPolicyReported> getPriorPoliciesReported() throws Exception {
        ArrayList<UIPersonalPrefillPriorPolicyReported> toReturn = new ArrayList<UIPersonalPrefillPriorPolicyReported>();

        for (WebElement we : getPriorPoliciesReportRows()) {
            clickWhenClickable(we);

            UIPersonalPrefillPriorPolicyReported row = new UIPersonalPrefillPriorPolicyReported();
            row.setCarrier(we.findElement(By.xpath(".//td[2]/div")).getText());
            row.setPolicyNumber(we.findElement(By.xpath(".//td[3]/div")).getText());
            row.setPolicyStatus(we.findElement(By.xpath(".//td[4]/div")).getText());
            String firstInsuredDateStr = we.findElement(By.xpath(".//td[5]/div")).getText();
            row.setFirstInsuredDate(DateUtils.convertStringtoDate(firstInsuredDateStr, "MM/dd/yyyy"));
            String effectiveDateStr = we.findElement(By.xpath(".//td[6]/div")).getText();
            row.setEffectiveDate(DateUtils.convertStringtoDate(effectiveDateStr, "MM/dd/yyyy"));
            String expirationDateStr = we.findElement(By.xpath(".//td[7]/div")).getText();
            row.setExpirationDate(DateUtils.convertStringtoDate(expirationDateStr, "MM/dd/yyyy"));
            ArrayList<UIPersonalPrefillPriorPolicyReportedVehicle> vehiclesPerRow = new ArrayList<UIPersonalPrefillPriorPolicyReportedVehicle>();
            List<WebElement> allVehicleRows = getPriorPoliciesVehiclesReportRows();
            for (int i = 0; i < allVehicleRows.size(); i++) {
                UIPersonalPrefillPriorPolicyReportedVehicle rowVehicle = new UIPersonalPrefillPriorPolicyReportedVehicle();
                rowVehicle.setVehicleDescription(allVehicleRows.get(i).findElement(By.xpath(".//td[1]/div")).getText());
                rowVehicle.setLiabilityTypes(allVehicleRows.get(i).findElement(By.xpath(".//td[2]/div")).getText());
                rowVehicle.setVin(allVehicleRows.get(i).findElement(By.xpath(".//td[3]/div")).getText());
                rowVehicle.setLimits(allVehicleRows.get(i).findElement(By.xpath(".//td[4]/div")).getText());
                i++;
                if (i > 14) {
                    System.out.println("%&%&%&%&%&% -- THIS WILL CONTINUE TO THROW AN EXCEPTION UNTIL WE HELP IT ACCOUNT FOR MULTIPLE PAGES!!!!!!! -- %&%&%&%&%&%");
                }
                rowVehicle.setDeductiblesCollComp(allVehicleRows.get(i).findElement(By.xpath(".//td[1]/div")).getText());
                rowVehicle.setLimitsUninsuredUnderinsured(allVehicleRows.get(i).findElement(By.xpath(".//td[4]/div")).getText());

                vehiclesPerRow.add(rowVehicle);
            }
            row.setVehicles(vehiclesPerRow);

            toReturn.add(row);
        }

        return toReturn;
    }


}
