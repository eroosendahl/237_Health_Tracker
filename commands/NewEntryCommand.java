package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NewEntryCommand extends AbstractCommand{

	String entryIdentifier;
	String entryValue;
	String entryDate;
	User currentUser;
	String filePath;
	
	
	public NewEntryCommand(User user, String path) {
		
		this.currentUser = user;
		name = "newEntry";
		this.filePath = path;
	}
	@Override
	public int execute() {
		
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		
		//Ex of command: "newEntry run 500 09/20/2020"
		
		//Ex of fileEntry: "03/10/2022 WALK(2) RUN(3) ATE(2200)"
		
		// commandSections comes after 'newEntry'
		String[] commandSections = executionMod.split(" ");
		this.entryIdentifier = commandSections[0];
		this.entryValue = commandSections[1];
		this.entryDate = commandSections[2];
		
		// https://stackoverflow.com/questions/13741751/modify-the-content-of-a-file-using-java
		List<String> lines = new ArrayList<String>();
		String line = null;
		boolean found = false;

		try {
			File csvFile = new File(this.filePath);
			FileReader csvReader = new FileReader(csvFile);
			BufferedReader csvBufferedReader = new BufferedReader(csvReader);
			
			int count = 0;
			
			while ((line = csvBufferedReader.readLine()) != null) {
				String[] userEntries = line.split(",");
				
				// if user is found (User.getUserRow() returns even number for every user?)
				if (userEntries[0] != "" && (count == this.currentUser.getUserRow())) {
					found = true;
					
					boolean foundDate = false;
					
					foundDate = editExistingDate(userEntries, foundDate);
					
					// https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
					line = reassembleUserLine(userEntries, foundDate);
				}
				
				lines.add(line);
				++count;
			}
			csvReader.close();
			csvBufferedReader.close();
			
			if (!found) { System.out.println("Error finding User position"); }
			else {
				FileWriter csvWriter = new FileWriter(this.filePath);
				BufferedWriter csvBufferedWriter = new BufferedWriter(csvWriter);
				
				for (String csvLine : lines) { csvBufferedWriter.write(csvLine); }
				csvBufferedWriter.flush();
				csvBufferedWriter.close();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return 0;
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
	public int helpMessage() {
		// TODO Auto-generated method stub
		System.out.println("Format: ");
		return 0;
	}
}
