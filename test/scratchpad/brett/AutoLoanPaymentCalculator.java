package scratchpad.brett;

import java.text.ParseException;
import java.util.Date;

import repository.gw.enums.DateDifferenceOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
public class AutoLoanPaymentCalculator {

	public enum PaymentMethod {
		Annual("Annual", 1),
		Semi_Annual("Semi-Annual", 2),
		Monthly("Monthly", 12),
		Quarterly("Quarterly", 4);
		String value;
		int numberOfPayments;

        PaymentMethod(String type, int numberOfPayments) {
			this.value = type;
			this.numberOfPayments = numberOfPayments;
		}

		public String getValue(){
			return this.value;
		}

		public int getNumberOfPayments(){
			return this.numberOfPayments;
		}

		public static PaymentMethod getRandom() {
			return values()[(int) (Math.random() * values().length)];
		}
	}
	
	public static void main(String[] args) throws ParseException {
		PaymentMethod paymentMethod = PaymentMethod.Annual;
		double loanAmount = 37400.00;
		double percentageRate = 7.95;
		int loanTermInYears = 5;
		int numberOfDaysInYear = 365;
		Date effectiveDate = DateUtils.convertStringtoDate("10/07/2015", "MM/dd/yyyy");
		Date firstPaymentDate = DateUtils.convertStringtoDate("10/07/2016", "MM/dd/yyyy");
		
		
		double percentageRateAsADecimal = percentageRate / 100;
		double ratePerPeriod = (percentageRateAsADecimal / paymentMethod.getNumberOfPayments());
		int loanTermInPayments = loanTermInYears * paymentMethod.getNumberOfPayments();
		
		int daysDifference = DateUtils.getDifferenceBetweenDates(firstPaymentDate, effectiveDate, DateDifferenceOptions.Day) - 30;
		System.out.println("The difference in days between the Beginning and Effective Dates is: " + daysDifference);
		
		System.out.println("The percentage rate expressed as a decimal is: " + percentageRateAsADecimal);
		
		double dailyInterestPercentAsADecimal = percentageRateAsADecimal / numberOfDaysInYear;
		System.out.println("The Daily interest rate expressed as a decimal is: " + dailyInterestPercentAsADecimal);
		
		double dailyInterestCharge = loanAmount * dailyInterestPercentAsADecimal;
		System.out.println("The Daily interest charge (In Dollars) is: $" + NumberUtils.round(dailyInterestCharge, 2));
		
		double amountOfInterestForFirstPayment = dailyInterestCharge * daysDifference;
		System.out.println("The amount of interest charged (In Dollars) for the first payment is: $" + NumberUtils.round(amountOfInterestForFirstPayment, 2));
		
		double paymentPerPeriod = (loanAmount * ratePerPeriod) / (1 - Math.pow((1 + ratePerPeriod), -loanTermInPayments));
		System.out.println("The Payment amount per period of the loan is: $" + NumberUtils.round(paymentPerPeriod, 2));
		
		double firstPaymentAmount = paymentPerPeriod + amountOfInterestForFirstPayment;
		System.out.println("The Payment amount for the first payment on the loan is: $" + NumberUtils.round(firstPaymentAmount, 2));
		
	}

}
