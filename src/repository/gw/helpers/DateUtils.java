package repository.gw.helpers;

import gwclockhelpers.ApplicationOrCenter;
import org.joda.time.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

	private static int getDayOfWeek(Calendar cal){
		return cal.get(Calendar.DAY_OF_WEEK);
	}

    public Date getDate(int year, int month, int day) {

        return null;
    }
	
	public static Date dateAddSubtract(Date dateToChange, DateAddSubtractOptions changeFormat, int amountToChange) {
		Date newDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToChange);
		switch (changeFormat) {
		case Day:
			calendar.add(Calendar.DATE, amountToChange);
			break;
		case Week:
			calendar.add(Calendar.DATE, (7 * amountToChange));
			break;
		case Month:
			calendar.add(Calendar.MONTH, amountToChange);
			break;
		case Quarter:
			calendar.add(Calendar.MONTH, (3 * amountToChange));
			break;
		case Year:
			calendar.add(Calendar.YEAR, amountToChange);
			break;
		case Hour:
			calendar.add(Calendar.HOUR_OF_DAY, amountToChange);
			break;
		case BusinessDay:
			int dayOfWeek;
			for(int i = 0; i < amountToChange; i++){
				calendar.add(Calendar.DATE, 1);
			    
				dayOfWeek = getDayOfWeek(calendar);
				if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || HolidayUtils.isHoliday(calendar.getTime())){
					do{
						calendar.add(Calendar.DATE, 1);
						dayOfWeek = getDayOfWeek(calendar);					
					}while(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || HolidayUtils.isHoliday(calendar.getTime()));
				}
			}
			break;
		default:
			break;
		}

		newDate = calendar.getTime();
		return newDate;
	}
	
	public static LocalDate convertDateToLocalDate(Date date) {
		
		String dateString = dateFormatAsString("MM/dd/YYYY", date);
		
		int month = Integer.parseInt(dateString.substring(0, 2));
		int dayOfMonth = Integer.parseInt(dateString.substring(3, 5));
		int year = Integer.parseInt(dateString.substring(6, 10));

		return LocalDate.of(year, month, dayOfMonth);
	}
	
	public static int getYearFromDate(Date date) {
		
		String dateString = dateFormatAsString("MM/dd/YYYY", date);
		int year = Integer.parseInt(dateString.substring(6, 10));

		return year;
	}
	
	public static Date getCenterDate(WebDriver driver, ApplicationOrCenter gwProduct) {
		Map<ApplicationOrCenter, Date> datesMap = null;
		try {
			datesMap = ClockUtils.getCurrentDates(driver);
		} catch (Exception e) {
			if((e.getMessage().contains(ApplicationOrCenter.PolicyCenter.getValue())) || (e.getMessage().contains(ApplicationOrCenter.BillingCenter.getValue())) || (e.getMessage().contains(ApplicationOrCenter.ContactManager.getValue()))) {
				String product = null;
				if (e.getMessage().contains(ApplicationOrCenter.PolicyCenter.getValue())) {
					product = "Policy Center";
				} else if (e.getMessage().contains(ApplicationOrCenter.BillingCenter.getValue())) {
					product = "Billing Center";
				} else if (e.getMessage().contains(ApplicationOrCenter.ContactManager.getValue())) {
					product = "Contact Manager";
				}
				e.printStackTrace();
				Assert.fail("Could not retrieve the Center date for " + product + ". This most likely indicates that the center is down. Please investigate. Thank you.");
			} else {
				if (e.getMessage().contains(ApplicationOrCenter.ClaimCenter.getValue())) {
					System.out.println("Could not retrieve the center date for ClaimCenter. This is probably expected, but I don't know. We don't actually have any way to figure out if ClaimCenter should be up in this environment.");
					e.printStackTrace();
				} else {
					e.printStackTrace();
					Assert.fail("There was an unexpected error getting the center dates. Please investigate!");
				}
			}
		}
		
		Date date = datesMap.get(gwProduct);
		return date;
	}
	
	public static Date getCenterDate(Config webDriverConfiguration, ApplicationOrCenter gwProduct) {
		Map<ApplicationOrCenter, Date> datesMap = null;
		try {
			datesMap = ClockUtils.getCurrentDates(webDriverConfiguration);
		} catch (Exception e) {
			StringWriter stringWriter = new StringWriter();
			e.printStackTrace(new PrintWriter(stringWriter));
			if((stringWriter.toString().contains("getPCDate")) || (stringWriter.toString().contains("getBCDate")) || (stringWriter.toString().contains("getABDate"))) {
				String product = null;
				if (stringWriter.toString().contains("getPCDate")) {
					product = "Policy Center";
				} else if (stringWriter.toString().contains("getBCDate")) {
					product = "Billing Center";
				} else if (stringWriter.toString().contains("getABDate")) {
					product = "Contact Manager";
				}
				e.printStackTrace();
				Assert.fail("Could not retrieve the Center date for " + product + ". This most likely indicates that the center is down. Please investigate. Thank you.");
			} else {
				if (stringWriter.toString().contains("getCCDate")) {
					System.out.println("Could not retrieve the center date for ClaimCenter. This is probably expected, but I don't know. We don't actually have any way to figure out if ClaimCenter should be up in this environment.");
					e.printStackTrace();
				} else {
					e.printStackTrace();
					Assert.fail("There was an unexpected error getting the center dates. Please investigate!");
				}
			}
		}
		
		Date date = datesMap.get(gwProduct);
		return date;
	}
	
	public static int getDifferenceBetweenDatesAbsoluteValue(Date firstDate, Date secondDate, repository.gw.enums.DateDifferenceOptions dateDifferenceOption) {
		int dateDifference = getDifferenceBetweenDates(firstDate, secondDate, dateDifferenceOption);
		return Math.abs(dateDifference);
	}
	
	public static int getDifferenceBetweenDates(Date firstDate, Date secondDate, DateDifferenceOptions dateDifferenceOption) {
		DateTime firstDateTime = new DateTime(firstDate.getTime());
		DateTime secondDateTime = new DateTime(secondDate.getTime());
		int dateDifference = 0;
		switch (dateDifferenceOption) {
		case Day:
			dateDifference = Days.daysBetween(firstDateTime, secondDateTime).getDays();
			break;
		case Week:
			dateDifference = Weeks.weeksBetween(firstDateTime, secondDateTime).getWeeks();
			break;
		case Month:
			dateDifference = Months.monthsBetween(firstDateTime, secondDateTime).getMonths();
			break;
		case Year:
			dateDifference = Years.yearsBetween(firstDateTime, secondDateTime).getYears();
			break;
		case Hour:
			dateDifference = Hours.hoursBetween(firstDateTime, secondDateTime).getHours();
			break;
		default:
			break;
		}
        return dateDifference;
    }
	
	/** This method will return the current system date for the Center passed in, as a string in the format specified.
	 * 
	 * @param gwProduct
	 * The GW Center Enum for the Center you want to get the date from (i.e. - Product.PolicyCenter, Product.BillinCenter)
	 * @param dateFormat
	 * String format to be used for formatting the date returned. This uses the "SimpleDateFormat" class from oracle.
	 * The documentation for this class can be found here: <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
	 * @return dateFormattedAsString
	 * Returns the date from the center, formatted according to the format given.
	 */
	public static String getCenterDateAsString(WebDriver driver, ApplicationOrCenter gwProduct, String dateFormat) {
		Date productDate = getCenterDate(driver, gwProduct);
		return new SimpleDateFormat(dateFormat).format(productDate);
	}
	
	public static int getCenterDateAsInt(WebDriver driver, ApplicationOrCenter gwProduct, String dateFormat) {
		Date productDate = getCenterDate(driver, gwProduct);
		String formattedDate = new SimpleDateFormat(dateFormat).format(productDate);
		return Integer.parseInt(formattedDate);
	}
	
	public static int getCenterDateAsInt(Config webDriverConfiguration, ApplicationOrCenter gwProduct, String dateFormat) {
		Date productDate = getCenterDate(webDriverConfiguration, gwProduct);
		String formattedDate = new SimpleDateFormat(dateFormat).format(productDate);
		return Integer.parseInt(formattedDate);
	}
	
	/** This method simply takes the date passed in and formats it according to the format specified. It then returns it as a string.
	 * 
	 * @param dateFormat
	 * String format to be used for formatting the date returned. This uses the "SimpleDateFormat" class from oracle.
	 * The documentation for this class can be found here: <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
	 * @param dateToFormat
	 * Date that you need to have formatted
	 * @return
	 */
	public static String dateFormatAsString(String dateFormat, Date dateToFormat) {
		return new SimpleDateFormat(dateFormat).format(dateToFormat);
	}
	
	/** This method simply takes the date passed in and formats it according to the format specified. It then returns it as an Integer.
	 * 
	 * @param dateFormat
	 * String format to be used for formatting the date returned. This uses the "SimpleDateFormat" class from oracle.
	 * The documentation for this class can be found here: <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
	 * @param dateToFormat
	 * Date that you need to have formatted
	 * @return
	 */
	public static int dateFormatAsInt(String dateFormat, Date dateToFormat) {
		String formattedDate = new SimpleDateFormat(dateFormat).format(dateToFormat);
		return Integer.parseInt(formattedDate);
	}
	
	/** This method takes the date passed in as a String (useful when pulling a date from a page in Guidewire) and converts
	 * it to a date object for use elsewhere.
	 * 
	 * @param dateString
	 * The date needing conversion, passed in as a string.
	 * @param dateFormat
	 * String format to be used for formatting the date returned. This uses the "SimpleDateFormat" class from oracle.
	 * The documentation for this class can be found here: <a href="http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html</a>
	 * @return date
	 * The date object resulting from the conversion from a string value.
	 * @throws Exception 
	 * @throws ParseException
	 */
	public static Date convertStringtoDate(String dateString, String dateFormat) throws ParseException {
		Date date = new Date();
		try {
			DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			date = format.parse(dateString);
		} catch (Exception e) {
			throw new ParseException(("Parse Exception: The String could not be parsed to convert to a date. dateString: " + dateString + " dateFormat: " + dateFormat), 0);
		}
		return date;
	}
	
	/** This method is useful for taking a date-time combo (Most prevalent when using the "getCenterDate" methods) and getting just the portion declared in the dateFormat passed in.
	 * For options regarding the date formats possible, please follow this link: <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
	 * @param dateTimeCombo
	 * A Full Date-Time combination (Usually obtained from the getCenterDate Methods)
	 * @param dateFormat
	 * String Format for the date-time combo.
	 * @return dateValueOfFormat
	 * The Date (fastTime) returned as only the format passed in.
	 */
	public static Date getDateValueOfFormat (Date dateTimeCombo, String dateFormat) {
		String dateTimeComboString = dateFormatAsString(dateFormat, dateTimeCombo);
		Date dateValueOfFormat = new Date();
		try {
			dateValueOfFormat = convertStringtoDate(dateTimeComboString, dateFormat);
		} catch (Exception e) {
			Assert.fail("Parse Exception: The Date-Time Combo could not be parsed to convert to a date without time.");
		}
		return dateValueOfFormat;
	}
}
