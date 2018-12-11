package repository.gw.generate.custom;

import persistence.globaldatarepo.entities.Underwriters;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PolicyCommon {

    private repository.gw.enums.GeneratePolicyType currentPolicyType = repository.gw.enums.GeneratePolicyType.QuickQuote;
    private repository.gw.enums.GeneratePolicyType typeToGenerate = repository.gw.enums.GeneratePolicyType.QuickQuote;
    private boolean draft = false;

    private repository.gw.enums.CreateNew createNew = repository.gw.enums.CreateNew.Create_New_Always;
    private String policyNumber = null;
    private String fullAccountNumber = null;
    private String submissionNumber = null;

    private boolean overriedEffExpDates = false;
    private Date effectiveDate = null;
    private Date expirationDate = null;
    private Integer polTermLengthDays = 365;

    private repository.gw.enums.PaymentPlanType paymentPlanType = repository.gw.enums.PaymentPlanType.getRandom();
    private repository.gw.enums.PaymentType downPaymentType = repository.gw.enums.PaymentType.getRandom();
    private BankAccountInfo bankAccountInfo = null;

    public Underwriters underwriterInfo = null;

    private repository.gw.generate.custom.PolicyPremium premium = new repository.gw.generate.custom.PolicyPremium();

    private List<String> submissionForms = new ArrayList<String>();
    private List<String> issuanceForms = new ArrayList<String>();
    private List<String> changeForms = new ArrayList<String>();

    private boolean PolicyCanceled = false;

    public List<String> getSubmissionForms() {
		return submissionForms;
	}

	public List<String> getIssuanceForms() {
		return issuanceForms;
	}

	public List<String> getChangeForms() {
		return changeForms;
	}

	public void setSubmissionForms(List<String> submissionForms) {
		this.submissionForms = submissionForms;
	}

	public void setIssuanceForms(List<String> issuanceForms) {
		this.issuanceForms = issuanceForms;
	}

	public void setChangeForms(List<String> changeForms) {
		this.changeForms = changeForms;
	}

	public repository.gw.enums.CreateNew getCreateNew() {
        return createNew;
    }

    public void setCreateNew(CreateNew createNew) {
        this.createNew = createNew;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public boolean isOverriedEffExpDates() {
        return overriedEffExpDates;
    }

    public void setOverriedEffExpDates(boolean overriedEffExpDates) {
        this.overriedEffExpDates = overriedEffExpDates;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getPolTermLengthDays() {
        return polTermLengthDays;
    }

    public void setPolTermLengthDays(Integer polTermLengthDays) {
        this.polTermLengthDays = polTermLengthDays;
    }

    public repository.gw.enums.PaymentPlanType getPaymentPlanType() {
        return paymentPlanType;
    }

    public void setPaymentPlanType(PaymentPlanType paymentPlanType) {
        this.paymentPlanType = paymentPlanType;
    }

    public repository.gw.enums.PaymentType getDownPaymentType() {
        return downPaymentType;
    }

    public void setDownPaymentType(PaymentType downPaymentType) {
        this.downPaymentType = downPaymentType;
    }

    public BankAccountInfo getBankAccountInfo() {
        return bankAccountInfo;
    }

    public void setBankAccountInfo(BankAccountInfo bankAccountInfo) {
        this.bankAccountInfo = bankAccountInfo;
    }

    public repository.gw.generate.custom.PolicyPremium getPremium() {
        return premium;
    }

    public void setPremium(PolicyPremium premium) {
        this.premium = premium;
    }

    public repository.gw.enums.GeneratePolicyType getCurrentPolicyType() {
        return currentPolicyType;
    }

    public void setCurrentPolicyType(repository.gw.enums.GeneratePolicyType currentPolicyType) {
        this.currentPolicyType = currentPolicyType;
    }

    public repository.gw.enums.GeneratePolicyType getTypeToGenerate() {
        return typeToGenerate;
    }

    public void setTypeToGenerate(GeneratePolicyType typeToGenerate) {
        this.typeToGenerate = typeToGenerate;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public String getFullAccountNumber() {
        return fullAccountNumber;
    }

    public void setFullAccountNumber(String fullAccountNumber) {
        this.fullAccountNumber = fullAccountNumber;
    }

    public String getSubmissionNumber() {
        return submissionNumber;
    }

    public void setSubmissionNumber(String submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

	public boolean isPolicyCanceled() {
		return PolicyCanceled;
	}

	public void setPolicyCanceled(boolean policyCanceled) {
		PolicyCanceled = policyCanceled;
	}

	
	
	
}
