package commands;

import java.util.ArrayList;

import main.CommandPrompt;
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class ListUsersCommand extends AbstractCommand {
	
	public ListUsersCommand(CommandPrompt cp) {
		commandPrompt = cp;
		name = "listUsers";
	}

	@Override
	public int execute() {
		System.out.println("\nAvailable users:");
		ArrayList<User> users = commandPrompt.getUsers();
		for (User user: users) {
			System.out.println(user.getName());
		}
		
		System.out.println();
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod) {
		return 0;
	}

	@Override
	public String formatMessage() {
		return "listUsers";
	}

	@Override
	public String descriptionMessage() {
		return "Lists all currently active users.";
	}

}
