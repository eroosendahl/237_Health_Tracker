package main;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import commands.AbstractCommand;
import commands.AppendEntryCommand;
import commands.ChangeUsernameCommand;
import commands.CheckGoalCommand;
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
import commands.PrintProgressCommand;
import commands.SetGoalCommand;
import commands.SummarizeCommand;
import commands.SwitchUserCommand;

public class HealthTracker {
	public static void main(String args[]) {
		System.out.println(healthTrackerStartupMessage());
		CommandPrompt commandPrompt = new CommandPrompt("userinfo.csv", new InputStreamReader(System.in));


//		//  Uncomment to generate and use a simple test file with some dummy data.
//		HealthTrackerGeneralVariables.generateTestFile();
//		commandPrompt.setFile("testUserInfo.csv");

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
		AbstractCommand changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);
		AbstractCommand summarizeCommand = new SummarizeCommand(commandPrompt);
		AbstractCommand appendEntryCommand = new AppendEntryCommand(commandPrompt);
		AbstractCommand setGoalCommand = new SetGoalCommand(commandPrompt);
		AbstractCommand printProgressCommand = new PrintProgressCommand(commandPrompt);
		AbstractCommand checkGoalCommand = new CheckGoalCommand(commandPrompt);

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
		commandPrompt.addCommand(changeUsernameCommand);
		commandPrompt.addCommand(summarizeCommand);
		commandPrompt.addCommand(printProgressCommand);
		commandPrompt.addCommand(setGoalCommand);
		commandPrompt.addCommand(appendEntryCommand);
		commandPrompt.addCommand(checkGoalCommand);

		commandPrompt.run();
	}

	private static String healthTrackerStartupMessage() {
		return "Health Tracker";
	}

	public static CommandPrompt getCommandFilledCommandPrompt() {

		CommandPrompt commandPrompt = new CommandPrompt();

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
		AbstractCommand changeUsernameCommand = new ChangeUsernameCommand(commandPrompt);

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
		commandPrompt.addCommand(changeUsernameCommand);

		return commandPrompt;
	}

	@SuppressWarnings("serial")
	public static ArrayList<AbstractCommand> getCurrentCommands(CommandPrompt cp) {
		return new ArrayList<AbstractCommand>() {
			{
				add(new EchoCommand());
				add(new NewEntryCommand(cp));
				add(new SwitchUserCommand(cp));
				add(new DeleteUserCommand(cp));
				add(new ListUsersCommand(cp));
				add(new ListCommandsCommand(cp));
				add(new DeleteEntryCommand(cp));
				add(new DisplayEntryCommand(cp));
				add(new ListEntriesCommand(cp));
				add(new ListStatsCommand(cp));
				add(new ChangeUsernameCommand(cp));

			}
		};
	}

}
