package main;

import java.util.regex.Pattern;

public class HealthTrackerGeneralVariables {
	
	public enum endState {
		UNIMPLEMENTED(-2),
		GENERAL_FAILURE(-1),
		SUCCESS(0),
		;
		
		private final int value;	
		
		endState(int value) {
			this.value = value;
		}
		
		// easily confusable with default "enum.values()"
		public int value() { return value; }
	}
	
	public static boolean isDateFormat(String date) {
		if (date.length() != 10 || date.indexOf("/") != 2 || date.lastIndexOf("/") != 5)
			return false;
		return true;
	}

	public static boolean isNumeric(String myString) {
		Pattern alphaNumeric = Pattern.compile("^[0-9]+$");
		return alphaNumeric.matcher(myString).find();
	}

	// https://www.techiedelight.com/check-string-contains-alphanumeric-characters-java/
	public static boolean isAlphaNumeric(String myString) {
		Pattern alphaNumeric = Pattern.compile("^[a-zA-Z0-9]+$");
		return alphaNumeric.matcher(myString).find();
	}

	public static boolean isValidDate(String myString) {
		int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
		Pattern datePattern = Pattern.compile("^[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}$");
		if (datePattern.matcher(myString).find()) {
			String[] myDate = myString.split("\\/");
			int day = Integer.parseInt(myDate[0]);
			int month = Integer.parseInt(myDate[1]);
			int yyyy = Integer.parseInt(myDate[2]);
	
			if ((yyyy > 1900) && (yyyy < 2100) && (month >= 1) && (month <= 12) && (day >= 1)) {
				if ((month == 2) && (yyyy % 4 == 0)) {
					if (day <= 29) { return true; }
				} else {
					if (day <= days[month]) { return true; }
				}
			}
		}
	
		return false;
	}

}
