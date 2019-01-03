package repository.gw.enums;

public enum PaymentPlanType {
	Annual("Annual", 1 , -20, 12),
	Semi_Annual("Semi-Annual", 2 , -20, 6),
	Monthly("Monthly", 12 , -15, 1),
	Quarterly("Quarterly", 4 , -20, 3);
	String value;
	int numberOfPaymentPeriods;
	int invoicingLeadTime;
	int numberOfMonthsBetweenPaymentPeriods;

	private PaymentPlanType(String type, int numberOfPaymentPeriods, int invoicingLeadTime, int numberOfMonthsBetweenPaymentPeriods){
		this.value = type;
		this.numberOfPaymentPeriods = numberOfPaymentPeriods;
		this.invoicingLeadTime = invoicingLeadTime;
		this.numberOfMonthsBetweenPaymentPeriods = numberOfMonthsBetweenPaymentPeriods;
	}

	public String getValue(){
		return this.value;
	}

	public int getNumberOfPaymentPeriods(){
		return this.numberOfPaymentPeriods;
	}

	public int getInvoicingLeadTime(){
		return this.invoicingLeadTime;
	}

	public int getNumberOfMonthsBetweenPaymentPeriods(){
		return this.numberOfMonthsBetweenPaymentPeriods;
	}

	public static PaymentPlanType getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}

}
