package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.EchoCommand;
import main.CommandPrompt;

public class CommandPromptTests {
	private CommandPrompt commandPrompt;
	private final PrintStream oldOut = System.out;
	String expectedOutput = "";
	String receivedOutput = "";
	

	@BeforeEach
	void setup() {
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

	@After
	void cleanUp() {
		System.setOut(oldOut);
	}




}
