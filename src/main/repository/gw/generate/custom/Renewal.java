package repository.gw.generate.custom;

import repository.gw.enums.ProductLineType;

public class Renewal {

	private boolean stopOnDays = false;
	private int renewalStart = 0;
	private int scheduledTermStart = 0;
	private int lienHolderRenewalBill = 0;
	private int legalNotices = 0;
	private int insuredRenwalBill = 0;
	private int umbrellaScheduledTerm = 0;
	private int policyTerm_Days = 81;

	private repository.gw.enums.ProductLineType typeToRenew = repository.gw.enums.ProductLineType.CPP;

	public Renewal() {

	}

	public Renewal(repository.gw.enums.ProductLineType typeToRenew) {
		switch(typeToRenew) {
		case Businessowners:
		case CPP:
			renewalStart = 80;
			scheduledTermStart = 50;
			lienHolderRenewalBill = 46;
			legalNotices = 35;
			insuredRenwalBill = 21;
			break;
		case PersonalUmbrella:
			renewalStart = 80;
			scheduledTermStart = 50;
			lienHolderRenewalBill = 46;
			umbrellaScheduledTerm = 30;
			insuredRenwalBill = 21;
			break;
		case Squire:
			break;
//		case StandardFL:
//			break;
		case StandardIM:
			break;
            case Membership:
                break;
            case StandardFire:
                break;
            case StandardLiability:
                break;
		}
	}

	public boolean isStopOnDays() {
		return stopOnDays;
	}

	public void setStopOnDays(boolean stopOnDays) {
		this.stopOnDays = stopOnDays;
	}

	public int getRenewalStart() {
		return renewalStart;
	}

	public void setRenewalStart(int renewalStart) {
		this.renewalStart = renewalStart;
	}

	public int getScheduledTermStart() {
		return scheduledTermStart;
	}

	public void setScheduledTermStart(int scheduledTermStart) {
		this.scheduledTermStart = scheduledTermStart;
	}

	public int getLienHolderRenewalBill() {
		return lienHolderRenewalBill;
	}

	public void setLienHolderRenewalBill(int lienHolderRenewalBill) {
		this.lienHolderRenewalBill = lienHolderRenewalBill;
	}

	public int getLegalNotices() {
		return legalNotices;
	}

	public void setLegalNotices(int legalNotices) {
		this.legalNotices = legalNotices;
	}

	public int getInsuredRenwalBill() {
		return insuredRenwalBill;
	}

	public void setInsuredRenwalBill(int insuredRenwalBill) {
		this.insuredRenwalBill = insuredRenwalBill;
	}

	public int getUmbrellaScheduledTerm() {
		return umbrellaScheduledTerm;
	}

	public void setUmbrellaScheduledTerm(int umbrellaScheduledTerm) {
		this.umbrellaScheduledTerm = umbrellaScheduledTerm;
	}

	public int getPolicyTerm_Days() {
		return policyTerm_Days;
	}

	public void setPolicyTerm_Days(int policyTerm_Days) {
		this.policyTerm_Days = policyTerm_Days;
	}

	public repository.gw.enums.ProductLineType getTypeToRenew() {
		return typeToRenew;
	}

	public void setTypeToRenew(ProductLineType typeToRenew) {
		this.typeToRenew = typeToRenew;
	}


	







}












