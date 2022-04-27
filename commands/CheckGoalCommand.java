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
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class CheckGoalCommand extends AbstractCommand {
	int goalValue;
	User currentUser;
	String filePath;
	
	public CheckGoalCommand(CommandPrompt cp) {
		name = "checkGoal";
		commandPrompt = cp;
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
		
		if (executionMod.split(" ").length == 1) {
			try {
				if(findGoal(executionMod)) {
					if (goalAchieved(executionMod)) {
						System.out.println("The goal for " + executionMod + " is achieved.");
					} else {
						System.out.println("The goal for " + executionMod + " isn't achieved.");
					}
				} else { System.out.println("There's no goal for " + executionMod + "."); }
			} catch (IOException e) { e.printStackTrace(); }
		} else { System.out.println("There should be exactly one argument."); }
		
		return 0;
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
						for (int j = 1; j < userEntry.length; ++j) {
							String record = userEntry[j];
							int openParenIndex = record.indexOf("(");
							int closeParenIndex = record.indexOf(")");
							if (record.substring(0, openParenIndex).equals(goalName)) {
								this.goalValue = Integer.parseInt(record.substring(openParenIndex+1, closeParenIndex));
								foundGoal = true;
								break;
							}
						}
					}
					if (foundGoal) { break; }
				}
			}
		}
		csvReader.close();
		csvBufferedReader.close();
		return foundGoal;
	}

	private boolean goalAchieved(String goalName) throws IOException {
		int activityTotal = 0;
		File csvFile = new File(this.filePath);
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;

		while ((line = csvBufferedReader.readLine()) != null) {
			String[] userEntries = line.split(",");
			if (userEntries[0] != "" && userEntries[0].equals(this.currentUser.getName())) {
				for (int i = 1; i < userEntries.length; ++i) {
					String[] userEntry = userEntries[i].split(" ");
					if (!userEntry[0].equals("goal")) {
						for (int j = 1; j < userEntry.length; ++j) {
							String record = userEntry[j];
							int openParenIndex = record.indexOf("(");
							int closeParenIndex = record.indexOf(")");
							String activityName = record.substring(0, openParenIndex);
							if (activityName.equals(goalName)) {
								int activityAmount = Integer.parseInt(record.substring(openParenIndex+1, closeParenIndex));
								activityTotal += activityAmount;
							}
						}
					}
				}
			}
		}
		csvReader.close();
		csvBufferedReader.close();
		return (activityTotal >= this.goalValue);
	}
	
	@Override
	public String formatMessage() {
		return "checkGoal [activity-name]";
	}

	@Override
	public String descriptionMessage() {
		return "Check whether the goal of an activity is attained.";
	}
}
