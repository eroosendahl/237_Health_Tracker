package commands;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables.endState;

public class SwitchUserCommand extends AbstractCommand {
	CommandPrompt commandPrompt;
	
	public SwitchUserCommand(CommandPrompt cp) {
		this.name = "SwitchUser";
		this.commandPrompt = cp;
	}
	
	@Override
	public int execute() {
		System.out.println("Value required for switching users.");
		return 0;
	}

//	@Override
//	public int execute(String executionMod) {
//		System.out.println("Executing SwitchUserCommand");
//
//		if (commandPrompt.containsUser(executionMod)) {
//			commandPrompt.setCurrentUser(commandPrompt.getUser(executionMod));
//		} else { System.out.println("Sorry, user '" + executionMod + "' doesn't exist."); }
//		return 0;
//	}  
	
	public int execute(String executionMod) {
		System.out.println("Executing SwitchUserCommand");
		if(commandPrompt.switchActiveUser(executionMod) != endState.SUCCESS.value()) {
			System.out.println("Failed to switch user");
			return endState.GENERAL_FAILURE.value();
		}
		return endState.SUCCESS.value();
	}

	@Override
	public int helpMessage() {
		System.out.println("Format: SwitchUser [username]");
		return 0;
	}

}
