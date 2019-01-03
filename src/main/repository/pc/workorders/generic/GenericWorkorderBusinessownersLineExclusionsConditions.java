/**
 * @author bmartin Aug 25, 2015
 * @notes
 */
package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderBusinessownersLineExclusionsConditions extends GenericWorkorderBusinessownersLine {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	
    public GenericWorkorderBusinessownersLineExclusionsConditions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
        this.driver = driver;
    }


    // EXCLUSIONS AND CONDITIONS
    public void fillOutBusinessOwnersLineExclusionsConditions(PolicyBusinessownersLine busOwnLineObj) {
        clickBusinessownersLine_ExclusionsConditions();
        fillOutOtherLiabilityExclusions(busOwnLineObj);
        //fillOutOtherPolicyWideConditions(busOwnLineObj);
    }


    public void fillOutOtherLiabilityExclusions(PolicyBusinessownersLine busOwnLineObj) {
        checkBusinessownersLineExclusionsConditionsComprehensiveBusinessLiabilityExclusionCheckbox(busOwnLineObj.getExclusionsConditionsStuff().isComprehensiveBusinessLiabilityExclusion());
        checkBusinessownersLineExclusionsConditionsDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusionCheckbox(busOwnLineObj.getExclusionsConditionsStuff().isDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusion());


    }

    public void fillOutOtherPolicyWideConditions(PolicyBusinessownersLine busOwnLineObj) {
        if (checkBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsementCheckbox(busOwnLineObj.getExclusionsConditionsStuff().isLiabilityManuscriptEndorsement())) {
            setBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement(busOwnLineObj.getExclusionsConditionsStuff().getLiabilityManuscriptEndorsementDescription().get(0));
        }

        if (checkBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsementCheckbox(busOwnLineObj.getExclusionsConditionsStuff().isPropertyManuscriptEndorsement())) {
            setBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement(busOwnLineObj.getExclusionsConditionsStuff().getPropertyManuscriptEndorsementDescription().get(0));
        }
    }


    /**
     * "Recorded Elements" and their XPaths
     */
    private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineExclusionsConditionsComprehensiveBusinessLiabilityExclusion() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPExclusionsLiabilityOtherDV:0:FBAddnlOtherExclCondCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineExclusionsConditionsDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusion() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPExclusionsLiabilityOtherDV:1:FBAddnlOtherExclCondCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPConditionsPolicyWideDV:2:FBAddnlOtherExclCondCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "//div[contains(text(), 'Liability Manuscript Endorsement IDBP 31 2003')]/ancestor::legend/following-sibling::div/span/div/div")
    private WebElement table_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement;


    @FindBy(xpath = "(//div[contains(@id, ':LiabilityManuscriptExtLV-body')]//div[contains(@class,'x-grid-cell-inner')])[2]")
    private WebElement editBox_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement;

    private Guidewire8Checkbox checkbox_SubmissionBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement() {
        return new Guidewire8Checkbox(driver, "//table[contains(@id, 'BOPConditionsPolicyWideDV:3:FBAddnlOtherExclCondCoverageInputSet:CovPatternInputGroup-legendChk')]");
    }

    @FindBy(xpath = "(//div[contains(@id, ':PropertyManuscriptExtLV-body')]//div[contains(@class,'x-grid-cell-inner')])[2]")
    private WebElement editBox_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement;

    @FindBy(xpath = "//div[contains(text(), 'Property Manuscript Endorsement IDBP 31 1003')]/ancestor::legend/following-sibling::div/span/div/div")
    private WebElement talbe_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement;

    /**
     * Helper Methods for Above Elements
     */

    private void checkBusinessownersLineExclusionsConditionsComprehensiveBusinessLiabilityExclusionCheckbox(Boolean trueFalseChecked) {
        checkbox_SubmissionBusinessownersLineExclusionsConditionsComprehensiveBusinessLiabilityExclusion().select(trueFalseChecked);
    }

    private void checkBusinessownersLineExclusionsConditionsDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusionCheckbox(Boolean trueFalseChecked) {
        checkbox_SubmissionBusinessownersLineExclusionsConditionsDamageToWorkPerformedBySubcontractorsOnYourBehalfExclusion().select(trueFalseChecked);
    }

    private boolean checkBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsementCheckbox(Boolean trueFalseChecked) {
        checkbox_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement().select(trueFalseChecked);
        return trueFalseChecked;
    }

    private void setBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement(String description) {

        tableUtils.clickCellInTableByRowAndColumnName(table_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement, tableUtils.getRowCount(table_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement), "Please complete");
        tableUtils.setValueForCellInsideTable(table_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement, "manusripttxt", description);
    }

    private boolean checkBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsementCheckbox(Boolean trueFalseChecked) {
        checkbox_SubmissionBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement().select(trueFalseChecked);
        return trueFalseChecked;
    }

    private void setBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement(String description) {
        tableUtils.clickCellInTableByRowAndColumnName(talbe_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement, tableUtils.getRowCount(talbe_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement), "Please complete");
        tableUtils.setValueForCellInsideTable(talbe_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement, "manusripttxt", description);
    }

    public String getBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement() {
        return editBox_SubmissionBusinessownersLineExclusionsConditionsLiabilityManuscriptEndorsement.getText();
    }

    public String getBusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement() {
        return editBox_BusinessownersLineExclusionsConditionsPropertyManuscriptEndorsement.getText();
    }

}
