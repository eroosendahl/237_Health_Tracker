package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class CheckGoalCommand extends AbstractCommand {
	int goalValue;
	User currentUser;
	String filePath;
	SimpleDateFormat formatter;
	
	public CheckGoalCommand(CommandPrompt cp) {
		name = "checkGoal";
		commandPrompt = cp;
		formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
	}
	
	@Override
	public int execute() {
		System.out.println("checkGoal requires an argument.");
		System.out.println(descriptionMessage());
		System.out.println(formatMessage());
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod) {
		System.out.println("Executing CheckGoalCommand");
		this.filePath = commandPrompt.getFile();
		this.currentUser = commandPrompt.getCurrentUser();
		this.goalValue = 0;
		String[] arguments = executionMod.split(" ");
		String activity = arguments[0];
		
		if (arguments.length == 3) {
			int[] stats = calculateStatistics(executionMod);
			if (stats[0] != HealthTrackerGeneralVariables.endState.SUCCESS.value()) {
				System.out.println("The operation is unsuccessful.");
				return endState.GENERAL_FAILURE.value();
			}
			try {
				if(findGoal(activity)) {
					int mean = stats[2];
					System.out.println("Current daily amount for " + activity + ": " + mean);
					if (mean >= this.goalValue) {
						System.out.println("The goal for " + activity + " is achieved.");
					} else {
						System.out.println("The goal for " + activity + " isn't achieved.");
					}
				} else {
					System.out.println("There's no goal for " + activity + ".");
					return endState.ALTERNATIVE_SUCCESS.value();
				}
			} catch (IOException e) { e.printStackTrace(); }
		} else {
			System.out.println("There should be exactly 3 arguments.");
			System.out.println(descriptionMessage());
			System.out.println(formatMessage());
			return endState.GENERAL_FAILURE.value();
		}
		return endState.SUCCESS.value();
	}

	private boolean findGoal(String goalName) throws IOException {
		File csvFile = new File(this.filePath);
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;
		boolean foundGoal = false;

		while ((line = csvBufferedReader.readLine()) != null) {
			String[] userEntries = line.split(",");
			if (userEntries[0] != "" && userEntries[0].equals(this.currentUser.getName())) {
				for (int i = 1; i < userEntries.length; ++i) {
					String[] userEntry = userEntries[i].split(" ");
					if (userEntry[0].equals("goal")) {
						String record = userEntry[1];
						int openParenIndex = record.indexOf("(");
						int closeParenIndex = record.indexOf(")");
						String activityName = record.substring(0, openParenIndex);
						if (activityName.equals(goalName)) {
							foundGoal = true;
							int activityAmount = Integer.parseInt(record.substring(openParenIndex+1, closeParenIndex));
							System.out.println("==========================");
							System.out.println("Daily goal for " + goalName + ": " + activityAmount);
							this.goalValue = activityAmount;
						}
					}
				}
			}
		}
		csvReader.close();
		csvBufferedReader.close();
		return foundGoal;
	}
	
	public int[] calculateStatistics(String executionMod) {
		
		String[] inputParts = executionMod.split(" ");
		
		if(inputParts.length <3 || inputParts.length >3) {
			System.out.println("Summarize command requires exactly three values.");
			System.out.println(formatMessage());
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		String activityIdentifier = inputParts[0];
		String startDateRaw = inputParts[1];
		String endDateRaw = inputParts[2];
		
		int[] dateCheckResults = verifyCorrectDates(startDateRaw,endDateRaw);
		if(dateCheckResults[0] == endState.GENERAL_FAILURE.value()) {
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		Date[] convertedDates = convertStringToDates(startDateRaw,endDateRaw);
		Date startDate;
		Date endDate;
		if(convertedDates ==null) {
			return new int[] {endState.GENERAL_FAILURE.value()}; 
		}
		else {
			startDate = convertedDates[0];
			endDate = convertedDates[1];
		}
		
		String[] userRowParts =null;
		try {
			User currentUser = commandPrompt.getCurrentUser();
			userRowParts = searchForUserRow(currentUser);
			
			if(userRowParts == null) {
				return new int[] {endState.GENERAL_FAILURE.value()}; 
			}
		} 
		catch (IOException e) {
			
			e.printStackTrace();
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		ArrayList<String> relaventActivityValues = new ArrayList<String>();
		
		relaventActivityValues = loadRelevantActivityValues(userRowParts, new Date[] {startDate,endDate},activityIdentifier);
		
		if(relaventActivityValues == null) {
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		int total = calculateTotal(relaventActivityValues);
		int average = calculateMean(relaventActivityValues,calculateNumberOfDaysInPeriod(startDate,endDate));
		
		return new int[] {endState.SUCCESS.value(), total, average};
	}
	
	private ArrayList<String> loadRelevantActivityValues(String[] userRowParts,Date[] dates, String activityIdentifier){
		
			ArrayList<String> relaventActivityValues = new ArrayList<String>();
		
			for(int i = 1; i< userRowParts.length; i++) {
			
			String entry = userRowParts[i];
			String[] entryParts = entry.split(" ");
			String entryDateRaw = entryParts[0];
			
			if (!HealthTrackerGeneralVariables.isValidDate(entryDateRaw)) { continue; }
			
			Date entryDate;
			
			try {
				entryDate = formatter.parse(entryDateRaw);
			} 
			catch (ParseException e) {
				
				e.printStackTrace();
				return null;
				
			}
			
			for(int j = 1; j < entryParts.length; j++) {
				
				String activity = entryParts[j];
				String[] activityParts = parseActivityEntry(activity);
				String activityId = activityParts[0];
				String activityValue = activityParts[1];
				
				if((entryDate.after(dates[0]) && entryDate.before(dates[1])) || entryDate.equals(dates[0]) || entryDate.equals(dates[1])) {
					
					
					if(activityId.equals(activityIdentifier)) {
						
						relaventActivityValues.add(activityValue);
					}
				}
				
				
			}
			
			
		}
		return relaventActivityValues;
	}
	
	private String[] searchForUserRow(User currentUser) throws IOException {
		int count = 0;
		String userRow;
		String[] userRowParts = null;
		
		File csvFile = new File(commandPrompt.getFile());
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;
		boolean found = false;
		
		while ((line = csvBufferedReader.readLine()) != null) {

			if(currentUser.getRow()==count) {
				
				userRow = line;
				userRowParts = userRow.split(",");
				if(userRowParts[0].equals(currentUser.getName())) found = true;
				
			}
			
			count++;
			
		}
		csvReader.close();
		csvBufferedReader.close();
		
		if(!found) {
			
			return null;
		}
		else return userRowParts;
		
	}
	
	private Date[] convertStringToDates(String startDateRaw, String endDateRaw) {
		
		Date startDate;
		Date endDate;
		
		try {
			
			startDate = formatter.parse(startDateRaw);
			endDate = formatter.parse(endDateRaw);
		}
		catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
		
		
		Date[] newDates = verifyChronologicalOrder(startDate, endDate);
		return newDates;
	}

	private int[] verifyCorrectDates(String startDateRaw, String endDateRaw) {
		
		if(!(HealthTrackerGeneralVariables.isDateFormat(startDateRaw)) || !(HealthTrackerGeneralVariables.isDateFormat(endDateRaw))) {
			
			System.out.println("One (or both) of your dates does not have the correct date format");
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		if(!(HealthTrackerGeneralVariables.isValidDate(startDateRaw)) || !(HealthTrackerGeneralVariables.isValidDate(endDateRaw))) {
			
			System.out.println("One (or both) of your dates is not a valid date");
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		else {
			return new int[] {endState.SUCCESS.value()};
		}
	}

	private Date[] verifyChronologicalOrder(Date startDate, Date endDate) {
		
		if(startDate.after(endDate)){
			
			System.out.println("Your start date takes place after your end date");
			System.out.println(formatMessage());
			System.out.println("Switching start and end dates");
			
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
			
			return new Date[] {startDate, endDate};
			
			
		}
		else return new Date[] {startDate, endDate};
	}
	
	
	//https://stackoverflow.com/questions/7103064/java-calculate-the-number-of-days-between-two-dates
	private int calculateNumberOfDaysInPeriod(Date start, Date end) {
		
		return (int)( (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24)) + 1;
	}
	
	private String[] parseActivityEntry(String activityEntry) {
		int openParenIndex = activityEntry.indexOf("(");
		int closeParenIndex = activityEntry.indexOf(")");

		String activityValue = activityEntry.substring(openParenIndex+1,closeParenIndex);
		String activityId = activityEntry.substring(0,openParenIndex);

		String[] activityParts = new String[] {activityId,activityValue};

		return activityParts;
	}
	
	private int calculateMean(ArrayList<String> activityValues, int numDaysInPeriod) {
		
		if(activityValues.size() ==0) {
			return 0;
		}
		
		int sum = 0;
		for(int i =0; i<activityValues.size();i++) {
			sum += Integer.parseInt(activityValues.get(i));
		}
		
		return sum / numDaysInPeriod;
		
		
	}
	
	private int calculateTotal(ArrayList<String> activityValues) {
		
		int sum = 0;
		for(int i =0; i<activityValues.size();i++) {
			sum += Integer.parseInt(activityValues.get(i));
		}
		
		return sum;
		
		
	}
	
	@Override
	public String formatMessage() {
		return "checkGoal [activity-name] [startDate(DD/MM/YYYY)] [endDate(DD/MM/YYYY)]";
	}

	@Override
	public String descriptionMessage() {
		return "Check whether the goal of an activity is attained. (Goal represents target average daily value for the given activity.)";
	}
}
