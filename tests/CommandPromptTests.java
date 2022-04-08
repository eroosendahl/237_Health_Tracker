package tests;

import static org.junit.Assert.assertEquals;
import commands.AbstractCommand;
import commands.DeleteUserCommand;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import commands.SwitchUserCommand;
import main.CommandPrompt;
import main.HealthTracker;
import main.User;

public class CommandPromptTests {
	private CommandPrompt commandPrompt;
	private final PrintStream oldOut = System.out;
	String testFileName = "testUserInfo.csv";
	String expectedOutput = "";
	String receivedOutput = "";


	@BeforeEach
	void setup() {
		commandPrompt = HealthTracker.createFullyFunctioningCommandPrompt(testFileName, new InputStreamReader(System.in));
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
		commandPrompt = new CommandPrompt(testFileName, new StringReader("echo "+ expression + "\nquit"));
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
			commandPrompt = new CommandPrompt(testFileName, new StringReader("help\nquit"), commandList);
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
	
	@Test
	void testQuit() {
		String expectedQuitMessage = "Shutting down...";
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(newOut));
		
		commandPrompt = new CommandPrompt(testFileName, new StringReader("quit"));
		commandPrompt.run();
		
		receivedOutput = newOut.toString();
		assertTrue(receivedOutput.contains(expectedQuitMessage));
	}
	
	@Test
	void testGetUser() {
		User originalUser = new User("originalUser", 0);
		commandPrompt = new CommandPrompt(originalUser);
		User receivedUser = commandPrompt.getCurrentUser();
		
		assertNotNull(receivedUser);
		assertEquals(originalUser, receivedUser);
	}
	
	@Test
	void testGetFile() {
		String receivedFileName = commandPrompt.getFile();
		assertNotNull(receivedFileName);
		assertEquals(receivedFileName, testFileName);
	}

	@After
	void cleanUp() {
		System.setOut(oldOut);
	}




}
