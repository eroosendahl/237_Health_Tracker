package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.CommandPrompt;
import main.User;

public class NewEntryCommand extends AbstractCommand{
	String entryIdentifier;
	String entryValue;
	String entryDate;
	User currentUser;
	String filePath;
	CommandPrompt commandPrompt;
	
	public NewEntryCommand(CommandPrompt cp) {
		name = "newEntry";
		commandPrompt = cp;
	}
	
//	public NewEntryCommand(User user, String path) {
//		this.currentUser = user;
//		name = "newEntry";
//		this.filePath = path;
//	}
	
	@Override
	public int execute() {
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		//Ex of command: "newEntry run 500 20/09/2020"
		//Ex of fileEntry: "10/03/2022 WALK(2) RUN(3) ATE(2200)"
		System.out.println("Executing NewEntryCommand");
		
		currentUser = commandPrompt.getCurrentUser();
		filePath = commandPrompt.getFile();
		
		// commandSections comes after 'newEntry'
		String[] commandSections = executionMod.split(" ");
		
		if (commandSections.length != 3) {
			System.out.println("There should be exactly 3 arguments.");
		} else {
			this.entryIdentifier = commandSections[0];
			this.entryValue = commandSections[1];
			this.entryDate = commandSections[2];
			
			if (commandPrompt.isAlphaNumeric(entryIdentifier) && commandPrompt.isNumeric(entryValue)
					&& commandPrompt.isValidDate(entryDate)) {
				try {
					readAndWriteCSV();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} else { System.out.println("Error: at least one argument is invalid."); }
		}

		return 0;
	}

	private void readAndWriteCSV() throws FileNotFoundException, IOException {
		// https://stackoverflow.com/questions/13741751/modify-the-content-of-a-file-using-java
		List<String> lines = new ArrayList<String>();
		
		boolean found = readFromCSV(lines); // in Java, lists are passed by reference
		if (!found) { System.out.println("Error finding User position"); }
		else {
			writeToCSV(lines);
		}
	}

	private boolean readFromCSV(List<String> lines) throws FileNotFoundException, IOException {
		File csvFile = new File(this.filePath);
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;
		boolean found = false;
//		int count = 0;
		
		while ((line = csvBufferedReader.readLine()) != null) {
			String[] userEntries = line.split(",");
			
			if (userEntries[0] != "" && userEntries[0].equals(this.currentUser.getName())) { // if user is found
				found = true;
				boolean foundDate = false;
				foundDate = editExistingDate(userEntries, foundDate);
				
				// https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
				line = reassembleUserLine(userEntries, foundDate);
			}
			
			lines.add(line);
//			++count;
		}
		csvReader.close();
		csvBufferedReader.close();
		return found;
	}

	private void writeToCSV(List<String> lines) throws IOException {
		FileWriter csvWriter = new FileWriter(this.filePath);
		BufferedWriter csvBufferedWriter = new BufferedWriter(csvWriter);
		
		for (String csvLine : lines) { csvBufferedWriter.write(csvLine + "\n"); }
		csvBufferedWriter.flush();
		csvBufferedWriter.close();
	}
	
	private boolean editExistingDate(String[] userEntries, boolean foundDate) {
		for (int i = 1; i < userEntries.length; ++i) {
			String individualEntry = userEntries[i];
			String[] entrySections = individualEntry.split(" ");
			String entrySectionDate = entrySections[0];
			
			if (entrySectionDate.equals(this.entryDate)) {
				foundDate = true;
				boolean foundActivity = false;
				
				foundActivity = checkForExistingActivity(entrySections);
				
				if (foundActivity) { // convert activity array (entrySection) to single string
					userEntries[i] = String.join(" ", entrySections);
				} else { // append a new activity
					userEntries[i] = individualEntry + this.entryIdentifier + "(" + this.entryValue + ")" + " ";
				}
				
				return true;
			}
		}
		return false;
	}
	
	private boolean checkForExistingActivity(String[] entrySections) {
		for (int j = 1; j < entrySections.length; ++j) {
			//String activityID = entrySections[j].substring(0, 3); // identifier
			//int activityAmount = Integer.parseInt(entrySections[j].substring(3));
			
			String[] activityParts = parseActivityEntry(entrySections[j]);
			String activityID = activityParts[0];
			int activityAmount = Integer.parseInt(activityParts[1]);
					
			// collect same event amount
			if (activityID.equals(this.entryIdentifier)) {
				int newEntryValue = Integer.parseInt(this.entryValue);
				entrySections[j] = activityID + "(" + Integer.toString(activityAmount + newEntryValue) + ")";
				return true;
			}
		}
		return false;
	}
	
	
	private String reassembleUserLine(String[] userEntries, boolean foundDate) {
		String line;
		if (foundDate) {
			line = String.join(",", userEntries);
		} else {
			String[] newUserEntries = new String[userEntries.length+1];
			
			for (int i = 0; i < userEntries.length; ++i) {
				newUserEntries[i] = userEntries[i];
			}
			
			// append new date & activity
			newUserEntries[newUserEntries.length-1] = this.entryDate + " "
			+ this.entryIdentifier +  "(" + this.entryValue + ")" + " ";
			
			line = String.join(",", newUserEntries);
		}
		return line;
	}
	
	private String[] parseActivityEntry(String activityEntry) {
		//everything between () is the value, everything before ( is the ID.
		int openParenIndex = activityEntry.indexOf("(");
		int closeParenIndex = activityEntry.indexOf(")");
		
		String activityValue = activityEntry.substring(openParenIndex+1,closeParenIndex);
		String activityId = activityEntry.substring(0,openParenIndex);
		
		String[] activityParts = new String[] {activityId,activityValue};
		
		return activityParts;
	}

	@Override
	public int formatMessage() {
		System.out.println("newEntry [activity-name] [amount] [date-dd/mm/yyyy]");
		return 0;
	}
}
