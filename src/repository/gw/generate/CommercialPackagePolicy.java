package repository.gw.generate;

import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PackageRiskType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.custom.*;

import java.util.ArrayList;

public class CommercialPackagePolicy extends PolicyCommon {
	
	public AccountInfo accountInfo = new AccountInfo();

	public repository.gw.enums.GeneratePolicyType currentPolicyType = repository.gw.enums.GeneratePolicyType.QuickQuote;
	public repository.gw.enums.GeneratePolicyType typeToGenerate = GeneratePolicyType.QuickQuote;
	public boolean draft = false;
	public repository.gw.enums.ProductLineType productType = ProductLineType.CPP;
	public repository.gw.enums.PackageRiskType packageRisk = PackageRiskType.getRandomRiskType();
	public ArrayList<PolicyLocation> locationList = new ArrayList<PolicyLocation>();

	//COMMERCIAL LINES
	private CPPCommercialProperty commercialPropertyCPP = null;
	private CPPGeneralLiability generalLiabilityCPP = null;
	private CPPCommercialAuto commercialAutoCPP = null;
	private CPPInlandMarine inlandMarineCPP = null;

//	public PaymentPlanType paymentPlanType = PaymentPlanType.getRandom();
//	public PaymentType downPaymentType = PaymentType.getRandom();
//	public BankAccountInfo bankAccountInfo = null;
//	
//	private boolean overriedEffExpDates = false;
//	public Date effectiveDate = null;
//	public Date expirationDate = null;
//	public Integer polTermLengthDays = 365;
//	
//	public CreateNew createNew = CreateNew.Create_New_Always;
//	public String policyNumber = null;

	public CommercialPackagePolicy() {

	}
	
	public CommercialPackagePolicy(CPPCommercialProperty commercialPropertyCPP) {
		this.commercialPropertyCPP = commercialPropertyCPP;
	}

	public CommercialPackagePolicy(CPPGeneralLiability generalLiabilityCPP) {
		this.generalLiabilityCPP = generalLiabilityCPP;
	}

	public CommercialPackagePolicy(CPPCommercialAuto commercialAutoCPP) {
		this.commercialAutoCPP = commercialAutoCPP;
	}

	public CommercialPackagePolicy(CPPInlandMarine inlandMarineCPP) {
		this.inlandMarineCPP = inlandMarineCPP;
	}

	public CommercialPackagePolicy(CPPCommercialProperty commercialPropertyCPP, CPPGeneralLiability generalLiabilityCPP, CPPCommercialAuto commercialAutoCPP, CPPInlandMarine inlandMarineCPP) {
		this.commercialPropertyCPP = commercialPropertyCPP;
		this.generalLiabilityCPP = generalLiabilityCPP;
		this.commercialAutoCPP = commercialAutoCPP;
		this.inlandMarineCPP = inlandMarineCPP;
	}

	public CPPCommercialProperty getCommercialPropertyCPP() {
		return commercialPropertyCPP;
	}

	public CPPGeneralLiability getGeneralLiabilityCPP() {
		return generalLiabilityCPP;
	}

	public CPPCommercialAuto getCommercialAutoCPP() {
		return commercialAutoCPP;
	}

	public CPPInlandMarine getInlandMarineCPP() {
		return inlandMarineCPP;
	}


    public boolean isOverriedEffExpDates() {
        return isOverriedEffExpDates();
    }

    public void setOverriedEffExpDates(boolean overriedEffExpDates) {
        this.setOverriedEffExpDates(overriedEffExpDates);
    }
}//EOF

















