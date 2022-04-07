package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.NewUserCommand;
import main.CommandPrompt;

class NewUserTest {
	private CommandPrompt commandPrompt;
	private AbstractCommand newUserCommand;
	private File expectedCSV;

	@BeforeEach
	void setup() {
//			if(new File("test.csv").exists()) {
				commandPrompt = new CommandPrompt("test.csv");
//			} else {
//				commandPrompt = new CommandPrompt("test.csv");
//			}
			newUserCommand = new NewUserCommand(commandPrompt);
			File expectedCSV = new File("expectedCSV.csv");
	}
	@Test
	void noUserNameProvided() {
		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(actualOutput));
		newUserCommand.execute();
		String expectedOutput = "Value required for creating a new user.\n";
		assertEquals(expectedOutput, actualOutput.toString());
		File actualCSV = new File("test.csv");
		assertTrue(actualCSV.equals(expectedCSV));
		actualCSV = expectedCSV;
	
	}
	
//	@Test
//	void validUserNameProvided() {
//		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(actualOutput));
//		newUserCommand.execute("validNewUser");
//		String expectedOutput = "Value required for creating a new user.";
//		System.out.println("hm" + actualOutput.toString());
//		assertEquals(expectedOutput, actualOutput.toString());
//		assertFalse(new File("test.csv").exists());
//	}

}
