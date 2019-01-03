package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.pc.search.SearchPoliciesPC;

public class GenericWorkorderRiskAnalysis_Claims extends GenericWorkorderRiskAnalysis {

    public GenericWorkorderRiskAnalysis_Claims(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, 'PolicyNumberPicker-inputEl')]")
    private WebElement Editbox_RiskAnalysisClaimsSearchByRelatedPolicy;

    @FindBy(xpath = "//div[contains(@id, 'PolicyNumberPicker:SelectPolicyNumberPicker')]")
    private WebElement div_RiskAnalysisClaimsSearchByRelatedPolicySearchIcon;
   
      
    
    public void setClaimsSearchByRelatedPolicy(String policy) {
		setText(Editbox_RiskAnalysisClaimsSearchByRelatedPolicy, policy);
}

    public void clickClaimsSearchByRelatedPolicySearch(){
    	clickWhenClickable(div_RiskAnalysisClaimsSearchByRelatedPolicySearchIcon);
    }

    public boolean checkClaimsSearchByRelatedPolicy(){
    	return checkIfElementExists(Editbox_RiskAnalysisClaimsSearchByRelatedPolicy, 1000);
    }

    public void searchClaimsRelatedPolicy(String policy){
    	setClaimsSearchByRelatedPolicy(policy);
    	clickClaimsSearchByRelatedPolicySearch();
    	repository.pc.search.SearchPoliciesPC policySearchPC = new SearchPoliciesPC(getDriver());
		policySearchPC.searchPolicyByPolicyNumber(policy);

    }

}










