package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import commands.AbstractCommand;
import commands.NewUserCommand;
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

	public CommandPrompt() {
		commands = new HashMap<String, AbstractCommand>();
	}

	public CommandPrompt(String fileName) {
		commands = new HashMap<String, AbstractCommand>();
		filename = fileName;
		scanner = new Scanner(System.in);
		findFile();
		loadExistantUsers();
		while (userList.isEmpty()) {
			gatherInitialUser();
		}
		currentUser = userList.get(0);
	}

	public int run() {
		startUpMessage();

		while (true) {
			loadExistantUsers();
			promptUser();
			gatherUserInput();

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
				System.out.println("Created new file.");
			}
			else {
				System.out.println("Found already existing file.");
			}

			if (!file.exists()) {
				System.out.println("File still doesn't exist!");
				return endState.GENERAL_FAILURE.value();
			}

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
		
		if (newUsername.length() > 0 && isAlphaNumeric(newUsername)) {
			System.out.println("Successfully created initial user.");
			User newUser = new User(newUsername, 0);
			userList.add(newUser);
			currentUser = newUser;
			
			try {
				BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename));
				csvWriter.write(newUsername);
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

				// user indices temporarily set to 0
				if (entries[0] != "") { userList.add(new User(entries[0], row)); }

				row++;
				line = csvBufferedReader.readLine();
			}
			return endState.SUCCESS.value();
		} catch(Exception ex) {
			ex.printStackTrace();
			return endState.GENERAL_FAILURE.value();
		}
	}

	public boolean isNumeric(String myString) {
		Pattern alphaNumeric = Pattern.compile("^[0-9]+$");
		return alphaNumeric.matcher(myString).find();
	}

	// https://www.techiedelight.com/check-string-contains-alphanumeric-characters-java/
	public boolean isAlphaNumeric(String myString) {
		Pattern alphaNumeric = Pattern.compile("^[a-zA-Z0-9]+$");
		return alphaNumeric.matcher(myString).find();
	}

	public boolean isValidDate(String myString) {
		int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		Pattern datePattern = Pattern.compile("^[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}$");
		if (datePattern.matcher(myString).find()) {
			String[] myDate = myString.split("\\/");
			int day = Integer.parseInt(myDate[0]);
			int month = Integer.parseInt(myDate[1]);
			int yyyy = Integer.parseInt(myDate[2]);

			if ((yyyy > 1900) && (yyyy < 2100) && (month >= 1) && (month <= 12) && (day >= 1)) {
				if ((month == 2) && (yyyy % 4 == 0)) {
					if (day <= 29) { return true; }
				} else {
					if (day <= days[month]) { return true; }
				}
			}
		}

		return false;
	}

	private void quit() {
		System.out.println("Shutting down...");

	}

	private void startUpMessage() {
		System.out.println("CommandPrompt Running");
		listCommands(true);
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
				System.out.print("\n" + k + ": ");
				v.descriptionMessage();
				System.out.print("Input format: ");
				v.formatMessage();
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

	/*
	 * This is poorly named - it's actually going through the list and returning false if there is a user in the list with the matching name.
	 * Should be alreadyContainsUsername(String username) or something.
	 */
	//	public boolean isUniqueUser(String username) {
	//		for (User user : this.userList) {
	//			// https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
	//			if (username.equals(user.getName())) { return false; }
	//		}
	//		return true;
	//	}

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
}
