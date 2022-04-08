package main;

import java.io.InputStreamReader;
import java.io.Reader;

import commands.AbstractCommand;
import commands.DeleteEntryCommand;
import commands.DeleteUserCommand;
import commands.DisplayEntryCommand;
import commands.EchoCommand;
import commands.ListCommandsCommand;
import commands.ListEntriesCommand;
import commands.ListStatsCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;

public class HealthTracker {
	public static void main(String args[]) {
		CommandPrompt commandPrompt = new CommandPrompt("userinfo.csv", new InputStreamReader(System.in));
		AbstractCommand echoCommand = new EchoCommand();
		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);
		AbstractCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		AbstractCommand deleteUserCommand = new DeleteUserCommand(commandPrompt);
		AbstractCommand listUsersCommand = new ListUsersCommand(commandPrompt);
		AbstractCommand listCommandsCommand = new ListCommandsCommand(commandPrompt);
		AbstractCommand deleteEntryCommand = new DeleteEntryCommand(commandPrompt);
		AbstractCommand displayEntryCommand = new DisplayEntryCommand(commandPrompt);
		AbstractCommand listEntriesCommand = new ListEntriesCommand(commandPrompt);
		AbstractCommand listStatsCommand = new ListStatsCommand(commandPrompt);

		commandPrompt.addCommand(echoCommand);
		commandPrompt.addCommand(newEntryCommand);
		commandPrompt.addCommand(newUserCommand);
		commandPrompt.addCommand(switchUserCommand);
		commandPrompt.addCommand(deleteUserCommand);
		commandPrompt.addCommand(listUsersCommand);
		commandPrompt.addCommand(listCommandsCommand);
		commandPrompt.addCommand(deleteEntryCommand);
		commandPrompt.addCommand(displayEntryCommand);
		commandPrompt.addCommand(listEntriesCommand);
		commandPrompt.addCommand(listStatsCommand);
		
		commandPrompt.run();
	}
	
	public static CommandPrompt createFullyFunctioningCommandPrompt(String filename, Reader reader) {
		CommandPrompt commandPrompt = new CommandPrompt(filename, reader);
		AbstractCommand echoCommand = new EchoCommand();
		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);
		AbstractCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		AbstractCommand deleteUserCommand = new DeleteUserCommand(commandPrompt);
		AbstractCommand listUsersCommand = new ListUsersCommand(commandPrompt);
		AbstractCommand listCommandsCommand = new ListCommandsCommand(commandPrompt);
		AbstractCommand deleteEntryCommand = new DeleteEntryCommand(commandPrompt);
		AbstractCommand displayEntryCommand = new DisplayEntryCommand(commandPrompt);
		AbstractCommand listEntriesCommand = new ListEntriesCommand(commandPrompt);
		AbstractCommand listStatsCommand = new ListStatsCommand(commandPrompt);

		commandPrompt.addCommand(echoCommand);
		commandPrompt.addCommand(newEntryCommand);
		commandPrompt.addCommand(newUserCommand);
		commandPrompt.addCommand(switchUserCommand);
		commandPrompt.addCommand(deleteUserCommand);
		commandPrompt.addCommand(listUsersCommand);
		commandPrompt.addCommand(listCommandsCommand);
		commandPrompt.addCommand(deleteEntryCommand);
		commandPrompt.addCommand(displayEntryCommand);
		commandPrompt.addCommand(listEntriesCommand);
		commandPrompt.addCommand(listStatsCommand);
		
		return commandPrompt;
	}
	
}
