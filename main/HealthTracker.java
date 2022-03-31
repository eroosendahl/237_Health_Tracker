package main;

import commands.AbstractCommand;

//import commands.DeleteUserCommand;
import commands.EchoCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
//import commands.SwitchUserCommand;

public class HealthTracker {
	public static void main(String args[]) {
		CommandPrompt commandPrompt = new CommandPrompt("userinfo.csv");
		AbstractCommand echoCommand = new EchoCommand();
		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);
		//AbstractCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		//AbstractCommand deleteUserCommand = new DeleteUserCommand(commandPrompt);

		commandPrompt.addCommand(echoCommand);
		commandPrompt.addCommand(newEntryCommand);
		commandPrompt.addCommand(newUserCommand);
		//commandPrompt.addCommand(switchUserCommand);
		//commandPrompt.addCommand(deleteUserCommand);
		
		commandPrompt.run();
	}
}
