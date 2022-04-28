package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class SetGoalCommand extends AbstractCommand{
	String entryIdentifier;
	String entryValue;
	String entryDate;
	User currentUser;
	String filePath;

	public SetGoalCommand(CommandPrompt cp) {
		name = "setGoal";
		commandPrompt = cp;
	}

	@Override
	public int execute() {
		System.out.println("setGoal requires arguments.");
		System.out.println(descriptionMessage());
		System.out.println(formatMessage());
		return endState.SUCCESS.value();
	}

	// can add values but not subtract them
	@Override
	public int execute(String executionMod) {
		System.out.println("Executing SetGoalCommand");

		currentUser = commandPrompt.getCurrentUser();
		filePath = commandPrompt.getFile();

		String[] commandSections = executionMod.split(" ");

		if (commandSections.length != 2) {
			System.out.println("There should be exactly 2 arguments.");
			System.out.println(formatMessage());
		} else {
			this.entryIdentifier = commandSections[0];
			this.entryValue = commandSections[1];


			if (HealthTrackerGeneralVariables.isAlphaNumeric(entryIdentifier) && HealthTrackerGeneralVariables.isNumeric(entryValue)) {
				try {
					readAndWriteCSV();
//					displayProgress();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} else { 
				System.out.println("Error: at least one argument is invalid."); 
				System.out.println(formatMessage());
				}
		}

		return 0;
	}

	private void readAndWriteCSV() throws FileNotFoundException, IOException {
		// https://stackoverflow.com/questions/13741751/modify-the-content-of-a-file-using-java
		List<String> lines = new ArrayList<String>();

		boolean found = readFromCSV(lines); 
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

		while ((line = csvBufferedReader.readLine()) != null) {
			String[] userEntries = line.split(",");
			if (userEntries[0] != "" && userEntries[0].equals(this.currentUser.getName())) { 
				found = true;
				List<String> modifiedLineList = new ArrayList<String>(Arrays.asList(userEntries));
				
				int goalPos = -1;
				for (int i = 0; i < modifiedLineList.size(); ++i) {
					if (modifiedLineList.get(i).length() == 4 && modifiedLineList.get(i).substring(0, 4).equals("goal")) {
						int openParenIndex = modifiedLineList.get(i).indexOf("(");
						if (modifiedLineList.get(i).substring(5, openParenIndex).equals(entryIdentifier)) { goalPos = i; }
					}
				}
				if (goalPos == -1) { modifiedLineList.add("goal " + entryIdentifier + "(" + entryValue + ")"); }
				else { modifiedLineList.set(goalPos, "goal " + entryIdentifier + "(" + entryValue + ")"); }
				
				// https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
				line = String.join(",", modifiedLineList);
			}

			lines.add(line);
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
		csvWriter.close();
		csvBufferedWriter.close();
	}

	@Override
	public String formatMessage() {
		return "setGoal [activity-name] [amount]";
	}


	@Override
	public String descriptionMessage() {

		return "Create a new goal amount for a given activity.  (Goal represents target average daily value for the given activity.)";
	}
}
