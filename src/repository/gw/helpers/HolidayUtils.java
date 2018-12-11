package repository.gw.helpers;

import org.openqa.selenium.WebDriver;
import repository.gw.enums.DateAddSubtractOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HolidayUtils {

	public static Date NewYearsDayObserved (int yearContainingHoliday) {
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.JANUARY, 1);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SATURDAY :
			return (new GregorianCalendar(--yearContainingHoliday, Calendar.DECEMBER, 31)).getTime();
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.JANUARY, 2)).getTime();
		case Calendar.MONDAY :
		case Calendar.TUESDAY :
		case Calendar.WEDNESDAY :
		case Calendar.THURSDAY :
		case Calendar.FRIDAY :
		default :
			return gregorianCalendar.getTime();
		}
	}

	public static Date MemorialDay (int yearContainingHoliday) {
		// Last Monday in May
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 1);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 30)).getTime();
		case Calendar.MONDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 29)).getTime();
		case Calendar.TUESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 28)).getTime();
		case Calendar.WEDNESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 27)).getTime();
		case Calendar.THURSDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 26)).getTime();
		case Calendar.FRIDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 25)).getTime();
		default : // Saturday
			return (new GregorianCalendar(yearContainingHoliday, Calendar.MAY, 31)).getTime();
		}
	}

	public static Date IndependenceDayObserved (int yearContainingHoliday) {
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.JULY, 4);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SATURDAY:
			return (new GregorianCalendar(yearContainingHoliday, Calendar.JULY, 3)).getTime();
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.JULY, 5)).getTime();
		case Calendar.MONDAY :
		case Calendar.TUESDAY :
		case Calendar.WEDNESDAY :
		case Calendar.THURSDAY :
		case Calendar.FRIDAY :
		default :
			return gregorianCalendar.getTime();
		}
	}

	public static Date LaborDay (int yearContainingHoliday) {
		// The first Monday in September
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 1);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.TUESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 7)).getTime();
		case Calendar.WEDNESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 6)).getTime();
		case Calendar.THURSDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 5)).getTime();
		case Calendar.FRIDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 4)).getTime();
		case Calendar.SATURDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 3)).getTime();
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.SEPTEMBER, 2)).getTime();
		case Calendar.MONDAY :
		default :
			return gregorianCalendar.getTime();
		}
	}

	public static Date Thanksgiving(int yearContainingHoliday) {
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 1);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 26)).getTime();
		case Calendar.MONDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 25)).getTime();
		case Calendar.TUESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 24)).getTime();
		case Calendar.WEDNESDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 23)).getTime();
		case Calendar.THURSDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 22)).getTime();
		case Calendar.FRIDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 28)).getTime();
		default : // Saturday
			return (new GregorianCalendar(yearContainingHoliday, Calendar.NOVEMBER, 27)).getTime();
		}
	}
	
	public static Date FridayAfterThanksgiving(int yearContainingHoliday) {
		return repository.gw.helpers.DateUtils.dateAddSubtract(Thanksgiving(yearContainingHoliday), DateAddSubtractOptions.Day, 1);
	}

	public static Date ChristmasDayObserved (int yearContainingHoliday) {
		Calendar gregorianCalendar = new GregorianCalendar(yearContainingHoliday, Calendar.DECEMBER, 25);
		switch(gregorianCalendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SATURDAY:
			return (new GregorianCalendar(yearContainingHoliday, Calendar.DECEMBER, 24)).getTime();
		case Calendar.SUNDAY :
			return (new GregorianCalendar(yearContainingHoliday, Calendar.DECEMBER, 26)).getTime();
		case Calendar.MONDAY :
		case Calendar.TUESDAY :
		case Calendar.WEDNESDAY :
		case Calendar.THURSDAY :
		case Calendar.FRIDAY :
		default :
			return gregorianCalendar.getTime();
		}
	}
	
	public static boolean isHoliday (Date dateToCheck) {
		int dateYear = Integer.parseInt(repository.gw.helpers.DateUtils.dateFormatAsString("yyyy", dateToCheck));
		if (dateToCheck.equals(NewYearsDayObserved(dateYear)) ||
				dateToCheck.equals(MemorialDay(dateYear)) ||
				dateToCheck.equals(IndependenceDayObserved(dateYear)) ||
				dateToCheck.equals(LaborDay(dateYear)) ||
				dateToCheck.equals(Thanksgiving(dateYear)) ||
				dateToCheck.equals(FridayAfterThanksgiving(dateYear)) ||
				dateToCheck.equals(ChristmasDayObserved(dateYear))) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean currentCenterDateIsHoliday (WebDriver driver, Date dateToCheck) {
		int centerDate = DateUtils.getCenterDateAsInt(driver, new GuidewireHelpers(driver).getCurrentCenter(), "yyyy");
		if (dateToCheck.equals(NewYearsDayObserved(centerDate)) ||
				dateToCheck.equals(MemorialDay(centerDate)) ||
				dateToCheck.equals(IndependenceDayObserved(centerDate)) ||
				dateToCheck.equals(LaborDay(centerDate)) ||
				dateToCheck.equals(Thanksgiving(centerDate)) ||
				dateToCheck.equals(FridayAfterThanksgiving(centerDate)) ||
				dateToCheck.equals(ChristmasDayObserved(centerDate))) {
			return true;
		} else {
			return false;
		}
	}
	
	/*public static void main(String [ ] args) {
		int beginingYear = 2016;
		int endingYear = 2060;
		int iterator = 0;
		StringBuilder stringBuilder = new StringBuilder();
		
		List<String> Holidays = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
		{
			this.add("New Year's Day");
			this.add("Memorial Day");
			this.add("Independence Day");
			this.add("Labor Day");
			this.add("Thanksgiving Day");
			this.add("Friday After Thanksgiving");
			this.add("Christmas Day");
		}};
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		stringBuilder.append("<?xml version=\"1.0\"?>");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder.append("<import xmlns=\"http://guidewire.com/pc/exim/import\" version=\"p5.86.a12.310.90\" usePeriodicFlushes=\"true\">");
		stringBuilder.append(System.getProperty("line.separator"));
		for (int yearContainingHoliday = beginingYear; yearContainingHoliday <= endingYear; yearContainingHoliday++) {
			for (String holiday : Holidays) {
				iterator ++;
				stringBuilder.append("<Holiday public-id=\"hol:" + iterator + "\">");
				stringBuilder.append(System.getProperty("line.separator"));
				stringBuilder.append("<AppliesToAllZones>true</AppliesToAllZones>");
				stringBuilder.append(System.getProperty("line.separator"));
				stringBuilder.append("<Name>" + holiday + "</Name>");
				stringBuilder.append(System.getProperty("line.separator"));
				stringBuilder.append("<Name_L10N_ARRAY/>");
				stringBuilder.append(System.getProperty("line.separator"));
				
				switch (holiday) {
				case "New Year's Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.NewYearsDayObserved(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Memorial Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.MemorialDay(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Independence Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.IndependenceDayObserved(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Labor Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.LaborDay(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Thanksgiving Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.Thanksgiving(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Friday After Thanksgiving":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.FridayAfterThanksgiving(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				case "Christmas Day":
					stringBuilder.append("<OccurrenceDate>" + dateFormat.format(HolidayHelpers.ChristmasDayObserved(yearContainingHoliday)) + " 00:00:00.000</OccurrenceDate>");
					break;
				}
				
				stringBuilder.append(System.getProperty("line.separator"));
				stringBuilder.append("</Holiday>");
				stringBuilder.append(System.getProperty("line.separator"));				
			}
		}
		stringBuilder.append("</import>");
		System.out.println(stringBuilder.toString());
	}*/

	/*public static void main(String [ ] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d");
		for (int yearContainingHoliday = 2010; yearContainingHoliday <= 2055; yearContainingHoliday++) {
			System.out.println("Federal Holidays for " + yearContainingHoliday + ":");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.NewYearsDayObserved(yearContainingHoliday)), "New Year's Day (observed)");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.MemorialDay(yearContainingHoliday)), "Memorial Day (observed)");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.IndependenceDayObserved(yearContainingHoliday)), "Independence Day (observed)");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.LaborDay(yearContainingHoliday)), "Labor Day");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.Thanksgiving(yearContainingHoliday)), "Thanksgiving Day");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.FridayAfterThanksgiving(yearContainingHoliday)), "Friday After Thanksgiving");
			System.out.printf("%-23s = %s%n", dateFormat.format(HolidayHelpers.ChristmasDayObserved(yearContainingHoliday)), "Christmas Day (observed)");
			System.out.println();
		}
	}*/
}
