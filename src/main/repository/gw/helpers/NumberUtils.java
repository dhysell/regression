package repository.gw.helpers;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class NumberUtils {
	private static double getCurrencyValueFrom (String elementText){
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
		BigDecimal bigDecimalValue = null;
		
		String parsedText = "";
		try {
			parsedText = numberFormat.parse(elementText).toString();
			bigDecimalValue = new BigDecimal(parsedText);
		} catch (ParseException e) {
			//e.printStackTrace();
			//System.out.println("\"" + elementText + "\" parsed weird and is most likely not numeric");
			if (!elementText.equals("-") && !elementText.trim().equals("")) {
				Assert.fail("The text passed in to the \"getCurrencyValue\" function used was not properly formatted as a US currency, or was not \"-\", indicating a Zero dollar amount. Please investigate this failure.");
			} else {
				bigDecimalValue = BigDecimal.ZERO;
			}
		}
		return bigDecimalValue.doubleValue();
	}
	public static double getCurrencyValueFromElement (WebElement element){
		
		String elementText = element.getText();
		
		return getCurrencyValueFrom(elementText);
	}
	
	public static double getCurrencyValueFromElement (String elementText){
		
		return getCurrencyValueFrom(elementText);
		
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int generateRandomNumberInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;

		return randomNum;
	}
	
	/**
	 * Returns a pseudo-random number between min and max, inclusive,
	 * excluding any numbers you don't want in the result. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @param excludedValues
	 * 				Value you want excluded from the result.
	 * @return Integer between min and max, inclusive and excluding results listed.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int generateRandomNumberInt(int min, int max, int excludedValue) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		
		if (randomNum == excludedValue){
			randomNum = randomNum + 1;
		}
		
		/*boolean randomNumberOnExcludedList = false;
		
		for (int value : excludedValues) {
			while (randomNumberOnExcludedList = false) {
				if (randomNum == value) {
					randomNumberOnExcludedList = true;
				}
			}
		}
		
		if (randomNumberOnExcludedList == true) {
			randomNum = randomNum + 1;
		}*/

		return randomNum;
	}
	
	public static int getRandomIntFromArray(int[] array) {
		int rnd = new Random().nextInt(array.length);		
		return array[rnd];
	}
	
	public static int getRandomIntFromList(List<Integer> array) {
		if (array.size() < 1) {
			Assert.fail("The passed-in array is empty.");
		}
		
		int randomOption = NumberUtils.generateRandomNumberInt(0, array.size()-1);
		return array.get(randomOption);
	}
	
	/**
	 * Returns a pseudo-random number as many digits long as you want.
	 *
	 * @param length
	 *            Length (Number of Digits) of random number generated.
	 * @return String random number x digits long.
	 * @see java.util.Random#nextInt(int)
	 */
	public static long generateRandomNumberDigits(int length) {
		String resultantNumber = StringsUtils.generateRandomNumberDigits(length);
		long numberFromResult = Long.valueOf(resultantNumber);
		return numberFromResult;
	}
	
	/**
	 * Returns a pseudo-random number as many digits long as you want.
	 *
	 * @param length
	 *            Length (Number of Digits) of random number generated.
	 * @return String random number x digits long.
	 * @see java.util.Random#nextInt(int)
	 */
	public static long generateRandomNumberDigits(int minLength, int maxLength) {
		String resultantNumber = StringsUtils.generateRandomNumberDigits(minLength, maxLength);
		long numberFromResult = Long.valueOf(resultantNumber);
		return numberFromResult;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) {
	    	throw new IllegalArgumentException("The number of places to round cannot be less than zero. Please correct this.");
	    }

	    BigDecimal roundedValue = new BigDecimal(value);
	    roundedValue = roundedValue.setScale(places, RoundingMode.HALF_UP);
	    return roundedValue.doubleValue();
	}
	
	public static List<Double> removeHighestOrLowestListValue(List<Double> listToManipulate, String highestOrLowestOption) {
        int index = 0;
        if (highestOrLowestOption.equalsIgnoreCase("Highest")) {
            if (listToManipulate.size() > 0) {
                double highest = listToManipulate.get(0);
                for (int i = 1; i < listToManipulate.size(); i++) {
                    double currentValue = listToManipulate.get(i);
                    if (currentValue > highest) {
                        highest = currentValue;
                        index = i;
                    }
                }
            }
        } else if (highestOrLowestOption.equalsIgnoreCase("Lowest")) {
            if (listToManipulate.size() > 0) {
                double lowest = listToManipulate.get(0);
                for (int i = 1; i < listToManipulate.size(); i++) {
                    double currentValue = listToManipulate.get(i);
                    if (currentValue < lowest) {
                        lowest = currentValue;
                        index = i;
                    }
                }
            }
        }
        listToManipulate.remove(index);
        return listToManipulate;
    }
	
	
	public boolean isBetween(int value, int lowValue, int highValue) {
        return lowValue < value && value < highValue;
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
