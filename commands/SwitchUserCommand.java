package commands;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables.endState;

public class SwitchUserCommand extends AbstractCommand {
	
	public SwitchUserCommand(CommandPrompt cp) {
		this.name = "switchUser";
		this.commandPrompt = cp;
	}
	
	@Override
	public int execute() {
		System.out.println("Value required for switching users.");
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		System.out.println("Executing SwitchUserCommand");
		if(commandPrompt.switchActiveUser(executionMod) != endState.SUCCESS.value()) {
			System.out.println("Sorry, user '" + executionMod + "' doesn't exist.");
			return endState.GENERAL_FAILURE.value();
		}
		return endState.SUCCESS.value();
	}

	@Override
	public String formatMessage() {
		return "switchUser [username]";
	}

	@Override
	public String descriptionMessage() {
		return "Switch current user to the input username.";
	}
}
