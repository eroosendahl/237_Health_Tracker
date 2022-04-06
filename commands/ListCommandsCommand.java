package commands;

import java.util.ArrayList;

import main.CommandPrompt;
import main.User;
import main.HealthTrackerGeneralVariables.endState;

public class ListCommandsCommand extends AbstractCommand {
	CommandPrompt commandPrompt;
	
	public ListCommandsCommand(CommandPrompt cp) {
		commandPrompt = cp;
		name = "listCommands";
	}

	@Override
	public int execute() {
		commandPrompt.listCommands();
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int formatMessage() {
		System.out.println("listCommands");
		return 0;
	}

	@Override
	public int desciptionMessage() {
		System.out.println("Lists available commands.");
		return 0;
	}

}
