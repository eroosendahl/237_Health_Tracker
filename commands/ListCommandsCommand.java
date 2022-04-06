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
		System.out.println("Listing commands without help info:");
		commandPrompt.listCommands(false);
		return endState.SUCCESS.value();
	}

	@Override
	public int execute(String executionMod) {
		if (executionMod.equals("-help")) {
			System.out.println("Listing commands WITH help info");
			commandPrompt.listCommands(true);
			return endState.SUCCESS.value();
		}
		else {
			System.out.println("Incorrect command arg");
			return endState.GENERAL_FAILURE.value();
		}
	}

	@Override
	public int formatMessage() {
		System.out.println("listCommands <-h>");
		return 0;
	}

	@Override
	public int descriptionMessage() {
		System.out.println("Lists available commands.  Include -h for more info on commands.");
		return 0;
	}

}
