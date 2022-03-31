package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import main.CommandPrompt;
import main.User;

public class DeleteUserCommand extends AbstractCommand {
	CommandPrompt commandPrompt;
	private ArrayList<String> remainingLines;
	
	public DeleteUserCommand(CommandPrompt cp) {
		this.name = "DeleteUser";
		this.commandPrompt = cp;
	}
	
	@Override
	public int execute() {
		System.out.println("Value required for insertion.");
		return 0;
	}

	private void filterUsername(ArrayList<String> lines, String username) throws IOException {
		BufferedReader csvBufferedReader = new BufferedReader(new FileReader(commandPrompt.getFile()));
		String line = csvBufferedReader.readLine();
		
		while (line != null) {
			String[] entries = line.split(",");
			if (!entries[0].equals(username)) { remainingLines.add(line); }
			line = csvBufferedReader.readLine();
		}
	}
	
	private void writeToCSV(ArrayList<String> lines) throws IOException {
		FileWriter csvWriter = new FileWriter(commandPrompt.getFile());
		BufferedWriter csvBufferedWriter = new BufferedWriter(csvWriter);
		
		for (String csvLine : lines) { csvBufferedWriter.write(csvLine + "\n"); }
		csvBufferedWriter.flush();
		csvBufferedWriter.close();
	}
	
	@Override
	public int execute(String executionMod) {
		System.out.println("Executing DeleteUserCommand");
		remainingLines = new ArrayList<String>();
		commandPrompt.loadExistantUsers();

		if (commandPrompt.containsUser(executionMod) && !commandPrompt.getCurrentUser().getName().equals(executionMod)) {
			try { filterUsername(remainingLines, executionMod); }
			catch (IOException e) { e.printStackTrace(); }
			
			try { writeToCSV(remainingLines); }
			catch (IOException e1) { e1.printStackTrace(); }
			
			commandPrompt.deleteUser(executionMod);
		} else if (!commandPrompt.containsUser(executionMod)) {
			System.out.println("Can't delete non-existant user: '" + executionMod + "'");
		} else if (commandPrompt.getCurrentUser().getName().equals(executionMod)) {
			System.out.println("User deletion failed: no user shall delete himself.");
		}

		return 0;
	}

	@Override
	public int helpMessage() {
		System.out.println("Format: DeleteUser [username]");
		return 0;
	}

}
