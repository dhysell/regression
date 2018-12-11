package repository.gw.generate.custom;

import repository.gw.enums.Property;

import java.util.ArrayList;

public class StandardFireAndLiability extends PolicyCommon {

//	public GeneratePolicyType currentPolicyType = GeneratePolicyType.QuickQuote;
//	public GeneratePolicyType typeToGenerate = GeneratePolicyType.QuickQuote;
//	public boolean draft = false;
//	private boolean overriedEffExpDates = false;
//	public Date effectiveDate = null;
//	public Date expirationDate = null;
//	public Integer polTermLengthDays = 365;
//	public String policyNumber = null;
//	public String fullAccountNumber = null;
//	
//	public CreateNew createNew = CreateNew.Create_New_Always;
	
	private ArrayList<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
	private ArrayList<repository.gw.generate.custom.Contact> policyMembers = new ArrayList<repository.gw.generate.custom.Contact>();
	private repository.gw.generate.custom.InsuranceScoreInfo insuranceScoreReport = new repository.gw.generate.custom.InsuranceScoreInfo();
	private boolean StdFireCommodity = false;

    public repository.gw.generate.custom.SquireLiability liabilitySection = new SquireLiability();
    public Property.SectionIDeductible section1Deductible = Property.SectionIDeductible.FiveHundred;

    private CLUEPropertyInfo cluePropertyReport;
    public boolean commodity = false;

//	public PaymentPlanType paymentPlanType = PaymentPlanType.getRandom();
//	public PaymentType downPaymentType = PaymentType.getRandom();
//	public BankAccountInfo bankAccountInfo = null;
	
	public StandardFireAndLiability() {
		
	}
	
	public ArrayList<repository.gw.generate.custom.PolicyLocation> getLocationList() {
		return locationList;
	}
	public void setLocationList(ArrayList<PolicyLocation> locationList) {
		this.locationList = locationList;
	}
	public ArrayList<repository.gw.generate.custom.Contact> getPolicyMembers() {
		return policyMembers;
	}
	public void setPolicyMembers(ArrayList<Contact> policyMembers) {
		this.policyMembers = policyMembers;
	}
	public repository.gw.generate.custom.InsuranceScoreInfo getInsuranceScoreReport() {
		return insuranceScoreReport;
	}
	public void setInsuranceScoreReport(InsuranceScoreInfo insuranceScoreReport) {
		this.insuranceScoreReport = insuranceScoreReport;
	}
	public CLUEPropertyInfo getCluePropertyReport() {
		return cluePropertyReport;
	}
	public void setCluePropertyReport(CLUEPropertyInfo cluePropertyReport) {
		this.cluePropertyReport = cluePropertyReport;
	}
	public boolean hasStdFireCommodity() {
		return StdFireCommodity;
	}
	public void setStdFireCommodity(boolean stdFireCommodity) {
		StdFireCommodity = stdFireCommodity;
	}
//	public boolean isOverriedEffExpDates() {
//		return overriedEffExpDates;
//	}
//
//	public void setOverriedEffExpDates(boolean overriedEffExpDates) {
//		this.overriedEffExpDates = overriedEffExpDates;
//	}


//	public static class StandardFireAndLiability_Builder {
//		
//		private boolean draft_builder = false;
//		private boolean overriedEffExpDates_builder = false;
//		private Date effectiveDate_builder = null;
//		private Date expirationDate_builder = null;
//		private Integer polTermLengthDays_builder = 365;
//		private CreateNew createNew_builder = CreateNew.Create_New_Always;
//		private ArrayList<PolicyLocation> locationList_builder = new ArrayList<PolicyLocation>();
//		private ArrayList<Contact> policyMembers_builder = new ArrayList<Contact>();
//		private boolean StdFireCommodity_builder = false;
//		private SquireLiability liabilitySection_builder = new SquireLiability();
//		private SectionIDeductible section1Deductible_builder = SectionIDeductible.FiveHundred;
//		private CLUEPropertyInfo cluePropertyReport_builder;
//		
//		public StandardFireAndLiability build(GeneratePolicyType typeToGenerate) {
//			StandardFireAndLiability newStandardFireAndLiability = new StandardFireAndLiability();
//			newStandardFireAndLiability.typeToGenerate = typeToGenerate;
//			newStandardFireAndLiability.draft = this.draft_builder;
//			newStandardFireAndLiability.overriedEffExpDates = this.overriedEffExpDates_builder;
//			newStandardFireAndLiability.effectiveDate = this.effectiveDate_builder;
//			newStandardFireAndLiability.expirationDate = this.expirationDate_builder;
//			newStandardFireAndLiability.polTermLengthDays = this.polTermLengthDays_builder;
//			newStandardFireAndLiability.createNew = this.createNew_builder;
//			newStandardFireAndLiability.locationList = this.locationList_builder;
//			newStandardFireAndLiability.policyMembers = this.policyMembers_builder;
//			newStandardFireAndLiability.StdFireCommodity = this.StdFireCommodity_builder;
//			newStandardFireAndLiability.liabilitySection = this.liabilitySection_builder;
//			newStandardFireAndLiability.section1Deductible = this.section1Deductible_builder;
//			newStandardFireAndLiability.cluePropertyReport = this.cluePropertyReport_builder;
//			return newStandardFireAndLiability;
//		}
//		public StandardFireAndLiability_Builder withEffectiveDate(Date newEffectiveDate) {
//			this.overriedEffExpDates_builder = true;
//			this.effectiveDate_builder = newEffectiveDate;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withExpirationDate(Date newExpirationDate) {
//			this.overriedEffExpDates_builder = true;
//			this.expirationDate_builder = newExpirationDate;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withPolicyTermLength(int numberOfDays) {
//			this.polTermLengthDays_builder = numberOfDays;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withCreateNew(CreateNew createNew) {
//			this.createNew_builder = createNew;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withLocationList(ArrayList<PolicyLocation> locationList) {
//			this.locationList_builder = locationList;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withPolicyMembers(ArrayList<Contact> policyMembersList) {
//			this.policyMembers_builder = policyMembersList;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withCommodities() {
//			this.StdFireCommodity_builder = true;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withLiability(SquireLiability liability) {
//			this.liabilitySection_builder = liability;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withSection1Deductible(SectionIDeductible deductible) {
//			this.section1Deductible_builder = deductible;
//			return this;
//		}
//		public StandardFireAndLiability_Builder withCLUEPropertyInfo(CLUEPropertyInfo clueInfo) {
//			this.cluePropertyReport_builder = clueInfo;
//			return this;
//		}
//	}//END BUILDER
	
	
	
	

}

































































