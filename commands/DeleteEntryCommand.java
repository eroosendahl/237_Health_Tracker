package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.CommandPrompt;

public class DeleteEntryCommand extends AbstractCommand {
	private ArrayList<String> modifiedLines;
	
	public DeleteEntryCommand(CommandPrompt cp) {
		this.name = "deleteEntry";
		this.commandPrompt = cp;
	}
	
	@Override
	public int execute() {
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		System.out.println("Executing DeleteEntryCommand");
		modifiedLines = new ArrayList<String>();
		commandPrompt.loadExistantUsers();
		
		String[] commandSections = executionMod.split(" ");
		if (commandSections.length == 1) {
			try { filterDate(executionMod); }
			catch (IOException e) { e.printStackTrace(); }
			
			try { writeToCSV(); }
			catch (IOException e1) { e1.printStackTrace(); }
		} else if (commandSections.length == 2) {
			try { filterActivity(commandSections[0], commandSections[1]); }
			catch (IOException e2) { e2.printStackTrace(); }
			
			try { writeToCSV(); }
			catch (IOException e3) { e3.printStackTrace(); }
		} else {
			System.out.println("There should be either 1 argument or 2 arguments.");
		}
		
		return 0;
	}
	
	private int foundDate(String dateString, String[] userEntries) {
		for (int i = 1; i < userEntries.length; ++i) {
			String[] items = userEntries[i].split(" ");
			if (dateString.equals(items[0])) { return i; }
		}
		return 0;
	}
	
	private int foundActivity(String activity, String[] dayEntries) {
		for (int i = 1; i < dayEntries.length; ++i) {
			// https://stackoverflow.com/questions/15236108/groovy-java-split-string-on-parentheses
			String[] item = dayEntries[i].split("\\(");
			
			if (activity.equals(item[0])) { return i; }
		}
		return 0;
	}
	
	private void filterDate(String dateString) throws IOException {
		BufferedReader csvBufferedReader = new BufferedReader(new FileReader(commandPrompt.getFile()));
		String line = csvBufferedReader.readLine();
		int dateFound = 0;
		
		while (line != null) {
			String[] userEntries = line.split(",");
			
			if (userEntries[0].equals(commandPrompt.getCurrentUser().getName())) {
				dateFound = foundDate(dateString, userEntries);
				if (dateFound == 0) { modifiedLines.add(line); }
				else {
					// https://www.geeksforgeeks.org/conversion-of-array-to-arraylist-in-java/
					List<String> modifiedLineList = new ArrayList<String>(Arrays.asList(userEntries));

					modifiedLineList.remove(dateFound);
					String modifiedLine = String.join(",", modifiedLineList);
					modifiedLines.add(modifiedLine);
				}
			} else { modifiedLines.add(line); }
			line = csvBufferedReader.readLine();
		}
		
		if (dateFound == 0) { System.out.println("Cannot delete entry: date '" + dateString + "' not found."); }
		csvBufferedReader.close();
	}
	
	private void filterActivity(String dateString, String activity) throws IOException {
		BufferedReader csvBufferedReader = new BufferedReader(new FileReader(commandPrompt.getFile()));
		String line = csvBufferedReader.readLine();
		int dateFound = 0, activityFound = 0;
		
		while (line != null) {
			String[] userEntries = line.split(",");	
			if (userEntries[0].equals(commandPrompt.getCurrentUser().getName())) {
				dateFound = foundDate(dateString, userEntries);

				if (dateFound == 0) { modifiedLines.add(line); }
				else {
					String[] dayEntries = userEntries[dateFound].split(" ");
					activityFound = foundActivity(activity, dayEntries);
					
					if (activityFound == 0) { modifiedLines.add(line); }
					else {
						List<String> modifiedDayEntries = new ArrayList<String>(Arrays.asList(dayEntries));
						modifiedDayEntries.remove(activityFound);
						userEntries[dateFound] = String.join(" ", modifiedDayEntries) + " ";
						modifiedLines.add(String.join(",", userEntries));
					}
				}
			} else { modifiedLines.add(line); }
			line = csvBufferedReader.readLine();
		}
		
		if (dateFound == 0) { System.out.println("Cannot delete entry: date '" + dateString + "' not found."); }
		else if (activityFound == 0) { System.out.println("Cannot delete entry: activity '" + activity + "' not found."); }
		csvBufferedReader.close();
	}

	private void writeToCSV() throws IOException {
		FileWriter csvWriter = new FileWriter(commandPrompt.getFile());
		BufferedWriter csvBufferedWriter = new BufferedWriter(csvWriter);
		
		for (String csvLine : modifiedLines) { csvBufferedWriter.write(csvLine + "\n"); }
		csvBufferedWriter.flush();
		csvBufferedWriter.close();
	}
	
	@Override
	public String formatMessage() {
		return "deleteEntry [date-dd/mm/yyyy] <optional-activity-name>";
	}

	@Override
	public String descriptionMessage() {
		return "Delete the entry at the given date.";
	}

}
