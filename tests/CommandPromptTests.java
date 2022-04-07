package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.EchoCommand;
import main.CommandPrompt;

public class CommandPromptTests {
	private CommandPrompt commandPrompt;
	private final ByteArrayOutputStream newOut = new ByteArrayOutputStream();
	private final PrintStream oldOut = System.out;
	private final InputStream oldIn = System.in;
	

	@BeforeEach
	void setup() {
		System.setOut(new PrintStream(newOut));
	}

	@Test
	void testCommandExecution() throws UnsupportedEncodingException {
		String expectedOutput = 
				"Found already existing file.\r\n"
				+ "CommandPrompt Running\r\n"
				+ "Type 'quit' to quit or 'help' for help.\n\r\n"
				+ "Erik enter command.\r\n"
				+ "echoed input\r\n"
				+ "Erik enter command.\r\n"
				+ "Shutting down...\r\n";
		commandPrompt = new CommandPrompt("testUserInfo.csv", new StringReader("echo echoed input\nquit"));
		EchoCommand echo = new EchoCommand();
		commandPrompt.addCommand(echo);
		commandPrompt.run();
		String receivedOutput = newOut.toString();
		assertEquals(expectedOutput, receivedOutput);
	}

	@After
	void cleanUp() {
		System.setOut(oldOut);
	}




}
