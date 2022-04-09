package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.ChangeUsernameCommand;
import commands.DeleteEntryCommand;
import commands.DeleteUserCommand;
import commands.DisplayEntryCommand;
import commands.EchoCommand;
import commands.ListCommandsCommand;
import commands.ListEntriesCommand;
import commands.ListStatsCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.User;

public class ListCommandsCommandTests {
	PrintStream oldOut = System.out;
	
	@Test
	void testListCommandsCommand() {
		int outputLinesWithMinimumCommands = 4;
		int outputLinesForSingleCommand = 1;
		
		CommandPrompt commandPrompt = new CommandPrompt();
		ArrayList<ArrayList<AbstractCommand>> variedCommandLists = getVariedCommandLists(commandPrompt);
		
		for(ArrayList<AbstractCommand> commandList : variedCommandLists) {
			ByteArrayOutputStream newOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(newOut));
			
			commandPrompt.refreshCommands(commandList);
			commandPrompt.setUserInput("listCommands");
			commandPrompt.attemptCommandExecution();
			
			String output = newOut.toString();
			String[] lines = output.split("\r\n|\n|\r");
			int expectedLineCount = outputLinesWithMinimumCommands + outputLinesForSingleCommand*commandList.size();
			int receivedLineCount = lines.length;
			
			assertEquals(expectedLineCount, receivedLineCount);
			commandList.forEach((command) -> assertTrue(output.contains(command.getName())));
		}
	}
	
	public ArrayList<CommandPrompt> getPrompts() {
		
		ArrayList<CommandPrompt> prompts = new ArrayList<CommandPrompt>() {
			{
				add(new CommandPrompt("listCommands"));
				add(new CommandPrompt("listCommands"));
				add(new CommandPrompt("listCommands"));
				add(new CommandPrompt("listCommands"));
			}
		};

		return prompts;
	}
	
	@SuppressWarnings("serial")
	public ArrayList<ArrayList<AbstractCommand>> getVariedCommandLists(CommandPrompt cp) {
		return new ArrayList<ArrayList<AbstractCommand>>() {
			{
				add(new ArrayList<AbstractCommand>() {
					{
						add(new ListCommandsCommand(cp));
					}
				});
				add(new ArrayList<AbstractCommand>() {
					{
						add(new ListCommandsCommand(cp));
						add(new EchoCommand());
						add(new NewEntryCommand(cp));
					}
				});
				add(new ArrayList<AbstractCommand>() {
					{
						add(new ListCommandsCommand(cp));
						add(new EchoCommand());
						add(new NewEntryCommand(cp));
						add(new SwitchUserCommand(cp));
						add(new DeleteUserCommand(cp));
					}
				});
				add(new ArrayList<AbstractCommand>() {
					{
						add(new EchoCommand());
						add(new NewEntryCommand(cp));
						add(new SwitchUserCommand(cp));
						add(new DeleteUserCommand(cp));
						add(new ListUsersCommand(cp));
						add(new ListCommandsCommand(cp));
						add(new DeleteEntryCommand(cp));
					}
				});
			}
		};
	}
	
	@After
	void cleanUp() {
		System.setOut(oldOut);
	}

}
