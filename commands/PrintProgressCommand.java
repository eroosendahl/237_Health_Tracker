package commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class PrintProgressCommand extends AbstractCommand{
	
public PrintProgressCommand(CommandPrompt cp ) {
		
		name = "printProgress";
		commandPrompt = cp;
	}

	@Override
	public int execute() {
		
		System.out.println("printProgress requires arguments.");
		System.out.println(descriptionMessage());
		System.out.println(formatMessage());
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod){
		
		int[] results = printMatchingEntries(executionMod);
		return results[0];
	}
	
	//first item in returned array is endstate result, 
	//second is number of printed entries(mostly for testing purposes)
	public int[] printMatchingEntries(String executionMod) {
		
		String activityIdentifier = executionMod;
		
		User currentUser = commandPrompt.getCurrentUser();
		String[] userRowParts = null;
		
		try {
			
			userRowParts = searchForUserRow(currentUser,userRowParts);
			
			if(userRowParts == null) {
			
				System.out.println("User row not found");
				return new int[] {endState.GENERAL_FAILURE.value(), 0};
			}
			
		} 
		catch (IOException e) {
			
			e.printStackTrace();
			return new int[] {endState.GENERAL_FAILURE.value(), 0};
		}
		
		System.out.println("Printing all entries that include " + activityIdentifier + ": ");
		
		int printedEntryCount = searchDatedEntries(activityIdentifier, userRowParts);
		
		
		return new int[] {endState.SUCCESS.value(), printedEntryCount};
	}
	
	
	private String[] searchForUserRow(User currentUser, String[] userRowParts) throws IOException {
		int count = 0;
		String userRow;
		
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

	private int searchDatedEntries(String activityIdentifier, String[] userRowParts) {
		
		int printedEntryCount = 0;
		
		for(int i = 1; i< userRowParts.length; i++) {
			
			String entry = userRowParts[i];
			String[] entryParts = entry.split(" ");
		
			for(int j = 1; j < entryParts.length; j++) {
				
				String activity = entryParts[j];
				String[] activityParts = parseActivityEntry(activity);
				String activityId = activityParts[0];
				
				if(activityId.equals(activityIdentifier)) {
						
					System.out.println(entry);
					printedEntryCount++;
				}
				
			}
			
		}
		return printedEntryCount;
	}
	
	private String[] parseActivityEntry(String activityEntry) {
		int openParenIndex = activityEntry.indexOf("(");
		int closeParenIndex = activityEntry.indexOf(")");

		String activityValue = activityEntry.substring(openParenIndex+1,closeParenIndex);
		String activityId = activityEntry.substring(0,openParenIndex);

		String[] activityParts = new String[] {activityId,activityValue};

		return activityParts;
	}
	
	
	@Override
	public String formatMessage() {
		
		return "printProgress [activity-name]";
		
	}

	@Override
	public String descriptionMessage() {
		
		return "prints all the entrys out that have a specified identifier";
	}

}
