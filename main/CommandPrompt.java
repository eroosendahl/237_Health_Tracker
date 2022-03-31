package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import commands.AbstractCommand;
import main.HealthTrackerGeneralVariables.endState;

public class CommandPrompt {
	private HashMap<String, AbstractCommand> commands;
	private boolean userPrompted = false;
	private Scanner scanner;
	private String userInput = "";
	private User currentUser;
	private String file;
	private int numUsers = 0;
	private ArrayList<String> usernameList;
	
	public CommandPrompt() {
		commands = new HashMap<String, AbstractCommand>();
	}
	
	public CommandPrompt(String fileName) {
		currentUser = new User("fakeUser", 0);
		commands = new HashMap<String, AbstractCommand>();
		file = fileName;
	}
	
	public int run() {
		startUpMessage();
		
		scanner = new Scanner(System.in);

		while (true) {

			promptUser();

			gatherUserInput();
			
			if (Objects.equals(userInput, "quit")) {
				quit();
				break;
			}

			attemptCommandExecution();
		}
		scanner.close();
		return endState.SUCCESS.value();
	}

	public void loadExistantUsers() {
		// https://www.journaldev.com/709/java-read-file-line-by-line
		try {
			usernameList = new ArrayList<String>();
			BufferedReader csvBufferedReader = new BufferedReader(new FileReader(this.file));
			String line = csvBufferedReader.readLine();
			
			while (line != null) {
				String[] entries = line.split(",");
				if (entries[0] != "") { usernameList.add(entries[0]); }
				line = csvBufferedReader.readLine();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private void quit() {
		System.out.println("Shutting down...");
		
	}

	private void startUpMessage() {
		System.out.println("CommandPrompt Running");
		commandHelpList();
	}

	private void attemptCommandExecution() {
		String nothing = "";
		String space = " ";
		if (!Objects.equals(userInput, nothing)) {
			if (!userInput.contains(space)) {
				if (commands.containsKey(userInput)) {
					commands.get(userInput).execute();
				}
				else {
					System.out.println("Command not found.");
				}
			}
			else {
				String[] command_exeMod_split = userInput.split(space, 2);
				if (commands.containsKey(command_exeMod_split[0])) {
					commands.get(command_exeMod_split[0]).execute(command_exeMod_split[1]);
				}
				else {
					System.out.println("Command not found.");
				}
			}
			userInput = nothing;
		}
	}

	private void gatherUserInput() {
		if (scanner.hasNext()) {
			userInput = scanner.nextLine();
			userPrompted = false;
		}
	}

	private void promptUser() {
		if (!userPrompted) {
			System.out.println(currentUser.getName() +  " enter command.");
			userPrompted = true;
		}
	}
	
	public void commandHelpList() {
		System.out.println("Command List:");
		commands.forEach((k,v) -> {
			System.out.print("Command: " + k + " || ");
			v.helpMessage();
		});	
		System.out.println("Type 'quit' to quit.\n");
	}
	
	public int addCommand(AbstractCommand command) {
		AbstractCommand returned_command = commands.put(command.getName(), command);

		if (Objects.equals(returned_command, null)) {
			return endState.SUCCESS.value();
		}
		else {
			return endState.GENERAL_FAILURE.value();
		}
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}

	public boolean isUniqueUsername(String username) {
		return !(this.usernameList.contains(username));
	}
}
