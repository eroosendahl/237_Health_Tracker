package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

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
	private ArrayList<User> userList;
	
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
			userList = new ArrayList<User>();
			BufferedReader csvBufferedReader = new BufferedReader(new FileReader(this.file));
			String line = csvBufferedReader.readLine();
			
			while (line != null) {
				String[] entries = line.split(",");

				// user indices temporarily set to 0
				if (entries[0] != "") { userList.add(new User(entries[0], 0)); }
				
				line = csvBufferedReader.readLine();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// https://www.techiedelight.com/check-string-contains-alphanumeric-characters-java/
	public boolean isAlphaNumericUsername(String username) {
		Pattern alphaNumeric = Pattern.compile("^[a-zA-Z0-9]+$");
		return alphaNumeric.matcher(username).find();
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
		System.out.println("\nCommand List:");
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
	
	public User getUser(String username) {
		for (User user : this.userList) {
			if (user.getName().equals(username)) { return user; }
		}
		return null;
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

	public ArrayList<User> getUsers() {
		return this.userList;
	}
	
	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}

	public boolean isUniqueUser(String username) {
		for (User user : this.userList) {
			// https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
			if (username.equals(user.getName())) { return false; }
		}
		return true;
	}
	
	public boolean containsUser(String username) {
		return !(this.isUniqueUser(username));
	}
	
	public boolean deleteUser(String username) {
		for (int i = 0; i < this.userList.size(); ++i) {
			if (userList.get(i).getName().equals(username)) {
				userList.remove(i);
				return true;
			}
		}
		
		return false;
	}
}
