package main;

import java.util.HashMap;
import java.util.List;
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
	private List<User> users;
	private String file;
	private int numUsers = 0;
	
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

<<<<<<< HEAD
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
=======
	public int getNumUsers() {
		return numUsers;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
>>>>>>> 73b1a75307b736479598e5acb982486c274a7612
	}

}
