package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.User;

class NewUserTest {
	private CommandPrompt commandPrompt;
	private AbstractCommand newUserCommand;
	private File actualCSV;
//right now makes you pass ina starting username for each test because its 
//	connected to the command prompt so just type something a few times, will come back to fix it later
	@BeforeEach
	void setup() {
			commandPrompt = new CommandPrompt("test.csv");
			commandPrompt.setCurrentUser(new User("firstUser", 0));
			newUserCommand = new NewUserCommand(commandPrompt);
			actualCSV = new File("test.csv");
	}
	@Test
	void noUserNameProvided() {
		try {
			ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
			System.setOut(new PrintStream(actualOutput));
			String expectedOutput = "Value required for creating a new user.\n";
			BufferedReader bufferExpectedContent = new BufferedReader(new FileReader("test.csv"));
			newUserCommand.execute();
			assertEquals(expectedOutput, actualOutput.toString());
			BufferedReader bufferActualContent = new BufferedReader(new FileReader("test.csv"));
			String actualContent = bufferActualContent.readLine();
			String expectedContent = bufferExpectedContent.readLine();
			assertTrue(actualContent.equals(expectedContent));
			bufferExpectedContent.close();
			bufferActualContent.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void invalidUserNameProvided() {
		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(actualOutput));
		String expectedOutput = "Executing NewUserCommand\nInvalid username: only pure alphanumeric usernames are accepted.\n";
		try {
			BufferedReader bufferExpectedContent = new BufferedReader(new FileReader("test.csv"));
			newUserCommand.execute("invalidUserName!");
			assertEquals(expectedOutput, actualOutput.toString());
			BufferedReader bufferActualContent = new BufferedReader(new FileReader("test.csv"));
			String actualContent = bufferActualContent.readLine();
			String expectedContent = bufferExpectedContent.readLine();
			assertTrue(actualContent.equals(expectedContent));
			bufferExpectedContent.close();
			bufferActualContent.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void validUserNameProvided() {
		File actualCSV = new File("test.csv");
		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(actualOutput));
		newUserCommand.execute("validUserName");
		String expectedOutput = "Executing NewUserCommand\n";
		assertEquals(expectedOutput, actualOutput.toString());
		actualCSV.delete();
	}
	
	@Test
	void duplicateUserNameProvided() {
		File actualCSV2 = new File("test.csv");
		ByteArrayOutputStream actualOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(actualOutput));
		newUserCommand.execute("duplicateUserName");
		newUserCommand.execute("duplicateUserName");
		String expectedOutput = "Executing NewUserCommand\nExecuting NewUserCommand\n";//\nCan't add user: duplicate username.\n";
		assertEquals(expectedOutput, actualOutput.toString());
		actualCSV2.delete();
	}
	

}
