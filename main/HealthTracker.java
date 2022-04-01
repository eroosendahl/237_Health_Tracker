package main;

import commands.AbstractCommand;
import commands.DeleteEntryCommand;
import commands.DeleteUserCommand;
import commands.DisplayEntryCommand;
import commands.EchoCommand;
import commands.ListCommandsCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;

public class HealthTracker {
	public static void main(String args[]) {
		CommandPrompt commandPrompt = new CommandPrompt("userinfo.csv");
		AbstractCommand echoCommand = new EchoCommand();
		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);
		AbstractCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		AbstractCommand deleteUserCommand = new DeleteUserCommand(commandPrompt);
		AbstractCommand listUsersCommand = new ListUsersCommand(commandPrompt);
		AbstractCommand listCommandsCommand = new ListCommandsCommand(commandPrompt);
		AbstractCommand deleteEntryCommand = new DeleteEntryCommand(commandPrompt);
		AbstractCommand displayEntryCommand = new DisplayEntryCommand(commandPrompt);

		commandPrompt.addCommand(echoCommand);
		commandPrompt.addCommand(newEntryCommand);
		commandPrompt.addCommand(newUserCommand);
		commandPrompt.addCommand(switchUserCommand);
		commandPrompt.addCommand(deleteUserCommand);
		commandPrompt.addCommand(listUsersCommand);
		commandPrompt.addCommand(listCommandsCommand);
		commandPrompt.addCommand(deleteEntryCommand);
		commandPrompt.addCommand(displayEntryCommand);
		
		commandPrompt.run();
	}
}
