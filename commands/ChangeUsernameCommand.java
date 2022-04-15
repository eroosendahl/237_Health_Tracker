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
import java.util.stream.Collectors;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;


public class ChangeUsernameCommand extends AbstractCommand {
	String csvFileName;
	User user;
	String userName;
	String newName;
	
	public ChangeUsernameCommand(CommandPrompt cp) {
		name = "changeUsername";
		commandPrompt = cp;
	}

	@Override
	public int execute() {
		System.out.println("Value required for changing your username");
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		
		System.out.println("Executing ChangeUsernameCommand");
		
		csvFileName = commandPrompt.getFile();
		user = commandPrompt.getCurrentUser();
		userName = user.getName();
		newName = executionMod;
		commandPrompt.loadExistantUsers();
		 
		try {
			if (!commandPrompt.containsUser(newName) && HealthTrackerGeneralVariables.isAlphaNumeric(newName)) {
				readAndWriteCSV();
				user.setName(newName);
				commandPrompt.loadExistantUsers();
			} else if (!HealthTrackerGeneralVariables.isAlphaNumeric(newName)) {
				System.out.println("Invalid username: only pure alphanumeric usernames are accepted.");
			} else if (commandPrompt.containsUser(newName)) {
				System.out.println("Can't add user: duplicate username.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private void readAndWriteCSV() throws FileNotFoundException, IOException {
		// https://stackoverflow.com/questions/13741751/modify-the-content-of-a-file-using-java
		List<String> lines = new ArrayList<String>();
		if (readFromCSV(lines)) { 
			writeToCSV(lines); }
		else {
			System.out.println("Error finding User position");
		}
	}
	
	private boolean readFromCSV(List<String> lines) throws FileNotFoundException, IOException {
		File csvFile = new File(csvFileName);
		FileReader csvReader = new FileReader(csvFile);
		BufferedReader csvBufferedReader = new BufferedReader(csvReader);
		String line = null;
		boolean found = false;

		while ((line = csvBufferedReader.readLine()) != null) {
			String[] userEntries = line.split(",");
			if (userEntries[0].equals(user.getName())) { // if user is found
				found = true;
				// https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
				userEntries[0] = newName;
				line = String.join(",", userEntries);
			}
			lines.add(line);
		}
		csvReader.close();
		csvBufferedReader.close();
		return found;
	}

	private void writeToCSV(List<String> lines) throws IOException {
		FileWriter csvWriter = new FileWriter(csvFileName);
		BufferedWriter csvBufferedWriter = new BufferedWriter(csvWriter);

		for (String csvLine : lines) { csvBufferedWriter.write(csvLine + "\n"); }
		csvBufferedWriter.flush();
		csvBufferedWriter.close();
	}
	
	@Override
	public String formatMessage() {
		return "changeUsername [new-username]";
	}

	@Override
	public String descriptionMessage() {
		return "Updates current user to the given username.";
	}
	
}