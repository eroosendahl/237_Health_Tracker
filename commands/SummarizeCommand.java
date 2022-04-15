package commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.HealthTrackerGeneralVariables.endState;
import main.User;

public class SummarizeCommand extends AbstractCommand{
	
	
	
	public SummarizeCommand(CommandPrompt cp ) {
		
		name = "summarize";
		commandPrompt = cp;
	}

	@Override
	public int execute() {
		
		System.out.println("summarize requires arguments.");
		System.out.println(descriptionMessage());
		System.out.println(formatMessage());
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod){
		
		int[] values = calculateStatistics(executionMod);
		printSummary(values,executionMod);
		
		return values[0];
		
	}
	
	//returns int array of statistics
	//first array item is endstate value
	//second is total
	//third is average
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
		
		if(!(HealthTrackerGeneralVariables.isDateFormat(startDateRaw)) || !(HealthTrackerGeneralVariables.isDateFormat(endDateRaw))) {
			
			System.out.println("One(or both) of your dates does not have the correct date format");
			System.out.println(formatMessage());
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		if(!(HealthTrackerGeneralVariables.isValidDate(startDateRaw)) || !(HealthTrackerGeneralVariables.isValidDate(endDateRaw))) {
			
			System.out.println("One(or both) of your dates is not a valid date");
			System.out.println(formatMessage());
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
		
		Date startDate;
		Date endDate;
		
		try {
			
			startDate = formatter.parse(startDateRaw);
			endDate = formatter.parse(endDateRaw);
		}
		catch (ParseException e) {
			
			e.printStackTrace();
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		//apparently Date class is primitive
		Date[] newDates = verifyChronologicalOrder(startDate, endDate);
		
		startDate = newDates[0];
		endDate = newDates[1];
		
		User currentUser = commandPrompt.getCurrentUser();
		String userRow;
		String[] userRowParts = null;
		
		
		int count = 0;
		try {
			
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
			
				System.out.println("User row not found");
				return new int[] {endState.GENERAL_FAILURE.value()};
				
			}
			
			
		} 
		catch (IOException e) {
			
			e.printStackTrace();
			return new int[] {endState.GENERAL_FAILURE.value()};
		}
		
		ArrayList<String> relaventActivityValues = new ArrayList<String>();
		
		
		for(int i = 1; i< userRowParts.length; i++) {
			
			String entry = userRowParts[i];
			String[] entryParts = entry.split(" ");
			String entryDateRaw = entryParts[0];
			
			Date entryDate;
			
			try {
				entryDate = formatter.parse(entryDateRaw);
			} 
			catch (ParseException e) {
				
				e.printStackTrace();
				return new int[] {endState.GENERAL_FAILURE.value()};
				
			}
			
			
			for(int j = 1; j < entryParts.length; j++) {
				
				String activity = entryParts[j];
				String[] activityParts = parseActivityEntry(activity);
				String activityId = activityParts[0];
				String activityValue = activityParts[1];
				
				if((entryDate.after(startDate) && entryDate.before(endDate))|| entryDate.equals(startDate) || entryDate.equals(endDate)) {
					
					
					if(activityId.equals(activityIdentifier)) {
						
						relaventActivityValues.add(activityValue);
					}
				}
				
				
			}
			
			
		}
		
		int total = calculateTotal(relaventActivityValues);
		int average = calculateMean(relaventActivityValues,calculateNumberOfDaysInPeriod(startDate,endDate));
		
		return new int[] {endState.SUCCESS.value(), total, average};
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
	
	
	public void printSummary(int[] values,String executionMod) {
		
		if(values[0] == HealthTrackerGeneralVariables.endState.SUCCESS.value()) {
			
			String[] inputParts = executionMod.split(" ");
			String activityIdentifier = inputParts[0];
			String startDateRaw = inputParts[1];
			String endDateRaw = inputParts[2];
			
			System.out.println("Operation Successful");
			System.out.print("Statistics for activity {" + activityIdentifier + "} for entered date range: ");
			System.out.println(startDateRaw + " - " + endDateRaw);
			System.out.println("==========================");
			System.out.println("Total: "+ values[1]);
			System.out.println("Mean: " + values[2]);
			
		}
		else {
			System.out.println("Operation Unsuccessful");
		}
		
	}
	@Override
	public String formatMessage() {
		
		return "summarize [activity-name] [startDate(DD/MM/YYYY)] [endDate(DD/MM/YYYY)]";
		
	}

	@Override
	public String descriptionMessage() {
		
		return "Summarize a given entry identifier over a certain date range with common statistics like average";
	}

}
