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
					
					// from fileEntries[1] onwards are data
					for (int i = 1; i < userEntries.length; ++i) {
						String individualEntry = userEntries[i];
						String[] entrySections = individualEntry.split(" ");
						String entrySectionDate = entrySections[0];
						
						if (entrySectionDate == this.entryDate) {
							foundDate = true;
							boolean foundActivity = false;
							
							for (int j = 1; j < entrySections.length; ++i) {
								String activityID = entrySections[i].substring(0, 3); // identifier
								int activityAmount = Integer.parseInt(entrySections[i].substring(3));
								
								// collect same event amount
								if (activityID == this.entryIdentifier) {
									int intEntryValue = Integer.parseInt(this.entryValue);
									entrySections[j] = activityID + Integer.toString(activityAmount + intEntryValue);
								}
							}
							
							if (foundActivity) { // convert activity array (entrySection) to single string
								userEntries[i] = String.join(" ", entrySections);
							} else { // append a new activity
								userEntries[i] = individualEntry + this.entryIdentifier + this.entryValue + " ";
							}
						}
					}
					
					// https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
					if (foundDate) {
						line = String.join(",", userEntries);
					} else {
						String[] newUserEntries = new String[userEntries.length+1];
						
						for (int i = 0; i < userEntries.length; ++i) {
							newUserEntries[i] = userEntries[i];
						}
						
						// append new date & activity
						newUserEntries[newUserEntries.length-1] = this.entryDate + " "
						+ this.entryIdentifier + this.entryValue + " ";
						
						line = String.join(",", newUserEntries);
					}
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
		
		
//		Scanner sc = new Scanner(new File(this.filePath));  
//		sc.useDelimiter(",");
//		
//		int count =0;
//		
//		while(count< this.currentUser.getUserRow()) {	
//			if(sc.hasNext()) sc.next();
//			else System.out.println("Error finding User position");
//			
//			count++;
//		}
//		
//		String userRow = sc.next();
//		String[] userEntries = userRow.split(",");
//		
//		boolean foundDate = false;
//		for(int i =1; i<userEntries.length; i++) { // userEntries[0]: username
//			String individualEntry = userEntries[i];
//			String[] entrySections = individualEntry.split(" ");
//			String entrySectionDate = entrySections[0];
//			if (this.entryDate == entrySections[0]) {
//				// append new activity on one specific date
//			}
//		}
//		if (!foundDate) {
//			// if date not found, append date and activity
//
//		}
	
		
		return 0;
	}

	@Override
	public int helpMessage() {
		// TODO Auto-generated method stub
		System.out.println("Format: ");
		return 0;
	}

}
