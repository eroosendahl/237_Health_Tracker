package main;

import commands.AbstractCommand;
import commands.EchoCommand;
import commands.NewUserCommand;

public class HealthTracker {

	public static void main(String args[]) {

		CommandPrompt commandPrompt = new CommandPrompt();
		AbstractCommand echoCommand = new EchoCommand();
//		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
//		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);

		commandPrompt.addCommand(echoCommand);
//		commandPrompt.addCommand(newEntryCommand);
//		commandPrompt.addCommand(newUserCommand);
		
		
		commandPrompt.run();

	}

}
