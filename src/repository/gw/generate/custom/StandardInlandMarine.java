package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineTypes;

import java.util.ArrayList;

public class StandardInlandMarine extends PolicyCommon {

    //	public GeneratePolicyType currentPolicyType = GeneratePolicyType.QuickQuote;
//	public GeneratePolicyType typeToGenerate = GeneratePolicyType.QuickQuote;
//	public boolean draft = false;
//	private boolean overriedEffExpDates = false;
//	public Date effectiveDate = null;
//	public Date expirationDate = null;
//	public Integer polTermLengthDays = 365;
//	public String policyNumber = null;
//	
//	public CreateNew createNew = CreateNew.Create_New_Always;
    public ArrayList<Contact> policyMembers = new ArrayList<Contact>();

    public ArrayList<FarmEquipment> farmEquipment = new ArrayList<FarmEquipment>();
    public ArrayList<InlandMarineTypes.InlandMarine> inlandMarineCoverageSelection_Standard_IM = new ArrayList<InlandMarineTypes.InlandMarine>();
    public ArrayList<PersonalProperty> personalProperty_PL_IM = new ArrayList<PersonalProperty>();

//	public PaymentPlanType paymentPlanType = PaymentPlanType.getRandom();
//	public PaymentType downPaymentType = PaymentType.getRandom();
//	public BankAccountInfo bankAccountInfo = null;

//	public boolean isOverriedEffExpDates() {
//		return overriedEffExpDates;
//	}
//
//	public void setOverriedEffExpDates(boolean overriedEffExpDates) {
//		this.overriedEffExpDates = overriedEffExpDates;
//	}
}
