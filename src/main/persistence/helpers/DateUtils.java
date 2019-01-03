package persistence.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DateUtils {
	
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
		default:
			break;
		}

		newDate = calendar.getTime();
		return newDate;
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
	 * The documentation for this class can be found here: <a href="http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
	 * @return date
	 * The date object resulting from the conversion from a string value.
	 * @throws ParseException
	 */
	public static Date convertStringtoDate(String dateString, String dateFormat) throws ParseException{
		DateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		Date date = format.parse(dateString);
		return date;
	}

}
