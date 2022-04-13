package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	
	public static File generateTestFile() {
		File file = new File("");
		FileWriter fileWriter;
		BufferedWriter writer;
		
		try {
			file = new File("testUserInfo.csv");
			file.delete();
			file.createNewFile();
			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			List<String> content = defaultTestContent();

			for (String input : content) { writer.write(input + "\n"); }
			writer.flush();
			fileWriter.close();
			writer.close();
		} catch (IOException e) {
			System.out.println("generateTestFile failed");
			e.printStackTrace();
		}	
		return file;
	}

	@SuppressWarnings("serial")
	private static ArrayList<String> defaultTestContent() {
		return new ArrayList<String>() {
			{
				add("One,01/01/2000 run(1) walk(1) ,02/01/2000 run(2) walk(2) ,03/01/2000 run(3) walk(3) ,");
				add("Two,01/02/2000 run(1) walk(1) ,02/02/2000 run(2) walk(2) ,03/02/2000 run(3) walk(3) ,");
				add("Three,01/03/2000 run(1) walk(1) ,02/03/2000 run(2) walk(2) ,03/03/2000 run(3) walk(3) ,");
				add("Four,01/04/2000 run(1) walk(1) ,02/04/2000 run(2) walk(2) ,03/04/2000 run(3) walk(3) ,");
				add("Five,01/05/2000 run(1) walk(1) ,02/05/2000 run(2) walk(2) ,03/05/2000 run(3) walk(3) ,");
				
			}
		};
	}
	
	public static void main(String[] args) {
		try {
			File file = generateTestFile();
		} catch (Exception e) {
			System.out.println("oof");
			e.printStackTrace();
		}
	}

}
