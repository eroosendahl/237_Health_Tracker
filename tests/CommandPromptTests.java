package tests;

import static org.junit.Assert.assertEquals;
import commands.AbstractCommand;
import commands.DeleteEntryCommand;
import commands.DeleteUserCommand;
import commands.DisplayEntryCommand;

import static org.junit.Assert.assertNotEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.EchoCommand;
import commands.ListCommandsCommand;
import commands.ListEntriesCommand;
import commands.ListStatsCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTracker;

public class CommandPromptTests {
	private CommandPrompt commandPrompt;
	private final PrintStream oldOut = System.out;
	String expectedOutput = "";
	String receivedOutput = "";


	@BeforeEach
	void setup() {
		commandPrompt = HealthTracker.createFullyFunctioningCommandPrompt("testUserInfo.csv", new InputStreamReader(System.in));
	}

	@Test
	void testCommandExecution() throws IOException {
		executeEcho("test");
		assertEquals(expectedOutput, receivedOutput);
		executeEcho("another test");
		assertEquals(expectedOutput, receivedOutput);
		receivedOutput = "";
		assertNotEquals(expectedOutput, receivedOutput);
	}

	void executeEcho(String expression) throws IOException {
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(newOut));

		expectedOutput = expectedOutputFromEcho(expression);
		commandPrompt = new CommandPrompt("testUserInfo.csv", new StringReader("echo "+ expression + "\nquit"));
		EchoCommand echo = new EchoCommand();
		commandPrompt.addCommand(echo);
		commandPrompt.run();
		receivedOutput = newOut.toString();
	}

	String expectedOutputFromEcho(String expressionToEcho) {
		return "Found already existing file.\r\n"
				+ "CommandPrompt Running\r\n"
				+ "Type 'quit' to quit or 'help' for help.\n\r\n"
				+ "Erik enter command.\r\n"
				+ expressionToEcho + "\r\n"
				+ "Erik enter command.\r\n"
				+ "Shutting down...\r\n";
	}

	@Test
	void testListCommands() {
		List<List<AbstractCommand>> variedCommandLists = getVariedCommandLists();
		int linesInOneCommandHelpMessage = 3;
		int linesPrintedWithZeroCommands = 13;
		
		for (List<AbstractCommand> commandList : variedCommandLists) {
			ByteArrayOutputStream newOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(newOut));
			commandPrompt = new CommandPrompt("testUserInfo.csv", new StringReader("help\nquit"), commandList);
			commandPrompt.run();
			receivedOutput = newOut.toString();
			
			String[] lines = receivedOutput.split("\r\n|\r|\n");
			int numLinesPrinted = lines.length - linesPrintedWithZeroCommands;
			int numCommands = commandList.size();
			int expectedNumLinesPrinted = linesInOneCommandHelpMessage * numCommands;
			
			assertEquals(numLinesPrinted, expectedNumLinesPrinted);
		}
	}

	private List<List<AbstractCommand>> getVariedCommandLists() {
		AbstractCommand echoCommand = new EchoCommand();
		AbstractCommand newEntryCommand = new NewEntryCommand(commandPrompt);
		AbstractCommand newUserCommand = new NewUserCommand(commandPrompt);
		AbstractCommand switchUserCommand = new SwitchUserCommand(commandPrompt);
		AbstractCommand deleteUserCommand = new DeleteUserCommand(commandPrompt);
		AbstractCommand listUsersCommand = new ListUsersCommand(commandPrompt);

		return new ArrayList<List<AbstractCommand>>(){
			{
				add(new ArrayList<AbstractCommand>());
				add(new ArrayList<AbstractCommand>() {
					{
						add(echoCommand);
					}
				});
				add(new ArrayList<AbstractCommand>(){
					{
						add(echoCommand);
						add(newEntryCommand);
					}
				});
				add(new ArrayList<AbstractCommand>() {
					{
						add(echoCommand);
						add(newEntryCommand);
						add(newUserCommand);
						add(switchUserCommand);
					}
				});
				add(new ArrayList<AbstractCommand>() {
					{
						add(echoCommand);
						add(newEntryCommand);
						add(newUserCommand);
						add(switchUserCommand);
						add(deleteUserCommand);
						add(listUsersCommand);
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
