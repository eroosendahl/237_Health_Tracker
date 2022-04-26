package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.HealthTrackerGeneralVariables.endState;

public class AppendEntryCommand extends AbstractCommand {
	private ArrayList<String> modifiedLines;

	public AppendEntryCommand(CommandPrompt cp) {
		this.name = "append";
		this.commandPrompt = cp;
	}

	@Override
	public int execute() {
		System.out.println("Value required to append.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		System.out.println("Executing AppendEntryCommand");
		modifiedLines = new ArrayList<String>();
		commandPrompt.loadExistantUsers();

		String[] commandSections = executionMod.split(" ");
		if (commandSections.length == 3) {
			String entryDate = commandSections[0];
			String entryIdentifier = commandSections[1];
			String entryValue = "";
			if(commandSections.length == 3) {
				entryValue = commandSections[2];
			}

			if (HealthTrackerGeneralVariables.isAlphaNumeric(entryIdentifier) && (HealthTrackerGeneralVariables.isNumeric(entryValue) || entryValue == "")
					&& HealthTrackerGeneralVariables.isValidDate(entryDate)) {
				try {
					filterActivity(entryDate, entryIdentifier, entryValue);
					writeToCSV();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} else { 
				System.out.println("Error: at least one argument is invalid."); 
				System.out.println(formatMessage());
			}
		} else { 
			System.out.println("There should be exactly 3 arguments.");
			formatMessage();
		}
		return 0;
	}

	private int foundDate(String dateString, String[] userEntries) {
		for (int i = 1; i < userEntries.length; ++i) {
			String[] items = userEntries[i].split(" ");
			if (dateString.equals(items[0])) { return i; }
		}
		return endState.GENERAL_FAILURE.value();
	}

	private int foundActivity(String activity, String[] dayEntries) {
		for (int i = 1; i < dayEntries.length; ++i) {
			// https://stackoverflow.com/questions/15236108/groovy-java-split-string-on-parentheses
			String[] item = dayEntries[i].split("\\(");
			if (activity.equals(item[0])) { return i; }
		}
		return endState.GENERAL_FAILURE.value();
	}

	private int filterActivity(String dateString, String activity, String value) throws IOException {
		BufferedReader csvBufferedReader = new BufferedReader(new FileReader(commandPrompt.getFile()));
		String line = csvBufferedReader.readLine();
		int dateFound = 0, activityFound = 0;
		Boolean badInput = false;
		if (value == "") {
			System.out.println("Command failed from bad input.");
			csvBufferedReader.close();
			return endState.GENERAL_FAILURE.value();
		}

		while (line != null) {
			String[] userEntries = line.split(",");	
			if (userEntries[0].equals(commandPrompt.getCurrentUser().getName())) {
				dateFound = foundDate(dateString, userEntries);

				if (dateFound == endState.GENERAL_FAILURE.value()) { 
					modifiedLines.add(line); 
				}
				else {
					String[] dayEntries = userEntries[dateFound].split(" ");
					activityFound = foundActivity(activity, dayEntries);
					List<String> modifiedDayEntries = new ArrayList<String>(Arrays.asList(dayEntries));

					if (activityFound == endState.GENERAL_FAILURE.value()) {
						appendAsNewActivity(activity, value, dateFound, userEntries, modifiedDayEntries);
					}
					else {
						appendExistingActivity(value, dateFound, activityFound, userEntries, modifiedDayEntries);
					}
				}
			} else { modifiedLines.add(line); }
			line = csvBufferedReader.readLine();
		}

		if (dateFound == 0 || dateFound == endState.GENERAL_FAILURE.value()) { 
			System.out.println("Cannot append entry: date '" + dateString + "' not found."); 
			csvBufferedReader.close();
			return endState.GENERAL_FAILURE.value();
			}
		csvBufferedReader.close();
		return endState.GENERAL_FAILURE.value();
	}

	private void appendExistingActivity(String value, int dateFound, int activityFound, String[] userEntries,
			List<String> modifiedDayEntries) {
		String activityEntry = modifiedDayEntries.get(activityFound);
		int openParenIndex = activityEntry.indexOf("(");
		int closedParenIndex = activityEntry.indexOf(")");
		int previousActivityVal = Integer.parseInt((String) activityEntry.substring(openParenIndex+1, closedParenIndex));
		int newActivityVal = previousActivityVal + Integer.parseInt(value);
		String newActivityEntry = modifiedDayEntries.get(activityFound).substring(0, openParenIndex+1)
				+ newActivityVal + modifiedDayEntries.get(activityFound).substring(closedParenIndex);

		modifiedDayEntries.set(activityFound, newActivityEntry);

		userEntries[dateFound] = String.join(" ", modifiedDayEntries) + " ";
		modifiedLines.add(String.join(",", userEntries));
	}

	private void appendAsNewActivity(String activity, String value, int dateFound, String[] userEntries,
			List<String> modifiedDayEntries) {
		System.out.println("Activity not found, appended as new.");
		modifiedDayEntries.add(activity + "(" + value + ")");
		userEntries[dateFound] = String.join(" ", modifiedDayEntries) + " ";
		modifiedLines.add(String.join(",", userEntries));
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
		return "append [date-dd/mm/yyyy] <activity-name>";
	}

	@Override
	public String descriptionMessage() {
		return "Append to the entry at the given date.";
	}
}
