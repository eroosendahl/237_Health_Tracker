package main;

import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import commands.AbstractCommand;
import main.HealthTrackerGeneralVariables.endState;

public class CommandPrompt {
	private HashMap<String, AbstractCommand> commands;
	
	public CommandPrompt() {
		commands = new HashMap<String, AbstractCommand>();
	}
	
	public int run() {
		String nothing = "";
		String userInput = nothing;
		Scanner scanner = new Scanner(System.in);
		boolean userPrompted = false;

		while (true) {

			if (!userPrompted) {
				System.out.println("Enter input.");
				userPrompted = true;
			}

			if (scanner.hasNext()) {
				userInput = scanner.nextLine();
				userPrompted = false;
				//System.out.println("\nYou said: " + userInput + "\n");
				if (Objects.equals(userInput, "quit"))
					break;
			}

			if (!Objects.equals(userInput, nothing)) {
				if (!userInput.contains(" "))
					commands.get(userInput).execute();
				else {
					String[] input_split = userInput.split(" ", 2);
					commands.get(input_split[0]).execute(input_split[1]);
				}
				userInput = nothing;
			}
		}
		scanner.close();
		return endState.SUCCESS.value();
	}
	
	public void commandHelpList() {
		System.out.println("Command List:");
		commands.forEach((k,v) -> {
			System.out.print("Command: " + k + "; ");
			v.helpMessage();
		});
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

}
