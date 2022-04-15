package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import commands.AbstractCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.HealthTrackerGeneralVariables.endState;

public class CommandPrompt {
	private HashMap<String, AbstractCommand> commands;
	private boolean userPrompted = false;
	private Scanner scanner;
	private String userInput = "";
	private User currentUser;
	private String filename;
	private int numUsers = 0;
	private ArrayList<User> userList;
	private final BufferedReader inputReader;
	HashMap<String, String> supportedStats;
	private File mainFile;
	
	public CommandPrompt() {
		inputReader = null;	
		currentUser = new User("0", 0);
		commands = new HashMap<String, AbstractCommand>();
		userList = new ArrayList<User>();
		mainFile = new File("");
	}
	
	public CommandPrompt(File inputFile, List<AbstractCommand> inputCommands) {
		filename = inputFile.getName();
		inputReader = null;
		loadExistantUsers();
		addCommands(inputCommands);
		findFile();
	}
	
	public CommandPrompt(String contrivedUserInput) {
		inputReader = null;
		currentUser = new User("0", 0);
		userInput = contrivedUserInput;
		commands = new HashMap<String, AbstractCommand>();
	}
	
	public CommandPrompt(ArrayList<User> initialUsers, String contrivedUserInput) {
		userInput = contrivedUserInput;
		inputReader = null;
		userList = initialUsers;
		commands = new HashMap<String, AbstractCommand>();
	}

	public CommandPrompt(User primaryUser) {
		inputReader = null;
		userList = new ArrayList<User>();
		userList.add(primaryUser);
		currentUser = userList.get(0);
		commands = new HashMap<String, AbstractCommand>();
		initializeHealthStats();
	}

	public CommandPrompt(String fileName, Reader inputSource) {

		initializeHealthStats();
		inputReader = new BufferedReader(inputSource);
		scanner = new Scanner(inputReader);
		commands = new HashMap<String, AbstractCommand>();
		filename = fileName;

		findFile();
		loadExistantUsers();
		loadInitialUser();
	}

	private void loadInitialUser() {
		while (userList.isEmpty()) {
			gatherInitialUser();
		}
		currentUser = userList.get(0);
	}

	public CommandPrompt(String fileName, Reader inputSource, List<AbstractCommand> commandsList) {
		initializeHealthStats();
		inputReader = new BufferedReader(inputSource);
		scanner = new Scanner(inputReader);
		commands = new HashMap<String, AbstractCommand>();
		filename = fileName;
		mainFile = new File(filename);
		findFile();
		loadExistantUsers();
		loadInitialUser();

		for (AbstractCommand command : commandsList) {
			commands.put(command.getName(), command);
		}
	}



	public int run() {
		startUpMessage();

		while (true) {
			if (mainFile.exists()) {
				loadExistantUsers();
			}
			promptUser();
			
			if (!Objects.isNull(scanner)) {
				gatherUserInput();
			}

			if (Objects.equals(userInput, "quit")) {
				quit();
				break;
			}

			if (userInput.equals("help")) {
				listCommands(true);
				userInput = "";
			}

			attemptCommandExecution();
		}
		return endState.SUCCESS.value();
	}

	public int findFile() {
		File file = new File(filename);

		try {
			boolean createdNewFile = file.createNewFile();
			if (createdNewFile) {
				System.out.println("Initialized from newly created file.");
			}
			else {
				System.out.println("Initialized from already existing file.");
			}

			if (!file.exists()) {
				System.out.println("File still doesn't exist!");
				return endState.GENERAL_FAILURE.value();
			}
			mainFile = file;
			return endState.SUCCESS.value();
		} catch (IOException e) {
			System.out.println("createNewFile() failed");
			e.printStackTrace();
			return endState.GENERAL_FAILURE.value();
		}
	}

	public int gatherInitialUser() {
		
		scanner = new Scanner(System.in);
		
		String newUsername = null;
		System.out.println("Please input a username:");

		if (scanner.hasNext()) {
			newUsername = scanner.nextLine();
		}

		if (newUsername.length() > 0 && HealthTrackerGeneralVariables.isAlphaNumeric(newUsername)) {
			System.out.println("Successfully created initial user.");
			User newUser = new User(newUsername, 0);
			userList.add(newUser);
			currentUser = newUser;

			try {
				BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename));
				csvWriter.write(newUsername + "\n");
				csvWriter.close();
			} catch (IOException e) {
				System.out.println("Failed to write initial user to empty file.");
				e.printStackTrace();
			}
			return endState.SUCCESS.value();
		}

		System.out.println("Failed to create initial user from user input.");
		return endState.GENERAL_FAILURE.value();
	}

	public int switchActiveUser(String destinationUsername) {
		for (User user: userList) {
			if (user.getName().equals(destinationUsername)) {
				currentUser = user;
				return endState.SUCCESS.value();
			}
		}
		return endState.GENERAL_FAILURE.value();
	}

	public int loadExistantUsers() {
		// https://www.journaldev.com/709/java-read-file-line-by-line
		try {
			userList = new ArrayList<User>();
			BufferedReader csvBufferedReader = new BufferedReader(new FileReader(this.filename));
			int row = 0;
			String line = csvBufferedReader.readLine();

			while (line != null) {
				String[] entries = line.split(",");

				if (entries[0] != "") { userList.add(new User(entries[0], row)); }

				row++;
				line = csvBufferedReader.readLine();
			}
			
			csvBufferedReader.close();
			return endState.SUCCESS.value();
		} catch(Exception ex) {
			ex.printStackTrace();
			return endState.GENERAL_FAILURE.value();
		}
	}

	private void quit() {
		System.out.println("Shutting down...");

	}

	private void startUpMessage() {
		System.out.println("CommandPrompt Running");
		System.out.println("Type 'quit' to quit or 'help' for help.\n");
	}

	public void attemptCommandExecution() {
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

	public void listCommands(boolean helpInfo) {
		if (helpInfo == false) {
			System.out.println("\nCommand List:\n");
			commands.forEach((k,v) -> {
				System.out.println(k);
			});	
			System.out.println();
		}
		else {
			System.out.println("\nCommand Help List:");
			commands.forEach((k,v) -> {
				System.out.println("\n" + k);
				System.out.println(v.descriptionMessage());
				System.out.println("Input format: " + v.formatMessage());
			});	
			System.out.println("\nType 'quit' to quit.");
			System.out.println("Type 'help' for help.\n");
		}

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
	
	public void addCommands(List<AbstractCommand> inputCommands) {
		if (Objects.equals(commands, null)) {
			commands = new HashMap<String, AbstractCommand>();
		}
		for (AbstractCommand command : inputCommands) {
			int returned = addCommand(command);
			if (returned != endState.SUCCESS.value()) {
				System.out.println("failed to add command");
			}
		}
	}
	
	public void refreshCommands(List<AbstractCommand> inputCommands) {
		commands = new HashMap<String, AbstractCommand>();
		addCommands(inputCommands);
	}

	public int addUser(User newUser) {
		if (isUniqueUser(newUser.getName())) {
			userList.add(newUser);
			return endState.SUCCESS.value();
		}
		return endState.GENERAL_FAILURE.value();
	}
	
	public int addUsers(List<User> newUsers) {
		if (!commands.containsKey("newUser")) {
			addCommand(new NewUserCommand(this));
		}
		int startingRow = this.userList.size();
		
		for (User user: newUsers) {
			user.setRow(startingRow);
			int addedNewUser = addUser(user);
			if (addedNewUser == endState.SUCCESS.value()) {
				commands.get("newUser").execute(user.getName());
				++startingRow;
			}
			else {
				System.out.println("Failed to add user.");
			}
		}
		
		return endState.GENERAL_FAILURE.value();
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
	
	public void setUserInput(String intendedUserInput) {
		userInput = intendedUserInput;
	}

	public String getFile() {
		return filename;
	}

	public void setFile(String file) {
		this.filename = file;
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
	
	public HashMap<String, String> getSupportedStats() {
		return supportedStats;
	}
	
	public HashMap<String, AbstractCommand> getCommands() {
		return commands;
	}


	public boolean isUniqueUser(String username) {
		for (User user : this.userList) {
			// https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
			if (username.equals(user.getName())) { return false; }
		}
		return true;
	}

	public boolean containsUser(String username) {
		for (User user : this.userList) {
			// https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
			if (username.equals(user.getName())) { return true; }
		}
		return false;
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

	public void initializeHealthStats() {
		supportedStats = new HashMap<String, String>();
		supportedStats.put("run", "running");
		supportedStats.put("med", "meditation");
		supportedStats.put("run", "running");
		supportedStats.put("stdy", "studying");
		supportedStats.put("cal", "calories consumed");
		supportedStats.put("water", "water drank");
	}

	public void printSupportedHealthStats() {
		supportedStats.forEach((key, value) -> {
			System.out.println(key+ " : " + value);
		});
	}

	public void setScanner(Scanner inputScanner) {
		scanner = inputScanner;
		
	}
}
