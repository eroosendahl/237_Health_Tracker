package commands;

import main.CommandPrompt;

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

	@Override
	public int execute(String executionMod) {
		System.out.println("Executing SwitchUserCommand");
		commandPrompt.loadExistantUsers();

		if (commandPrompt.containsUser(executionMod)) {
			commandPrompt.setCurrentUser(commandPrompt.getUser(executionMod));
		} else { System.out.println("Sorry, user '" + executionMod + "' doesn't exist."); }
		return 0;
	}

	@Override
	public int helpMessage() {
		System.out.println("Format: SwitchUser [username]");
		return 0;
	}

}
