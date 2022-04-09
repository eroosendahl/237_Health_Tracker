package tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.DisplayEntryCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.User;

public class DisplayEntryCommandTests {
	PrintStream oldOut = System.out;
	private String testDate = "01/01/2000";
	
	@SuppressWarnings("unused")
	@Test
	void testDisplayEntryCommand() throws IOException {
		File testFile = new File("testUserInfo.csv");
		boolean createdNewFile = testFile.createNewFile();
		CommandPrompt cp = new CommandPrompt();
		List<AbstractCommand> testCommands = getTestCommands(cp);
		cp = new CommandPrompt(testFile, testCommands);
		
		for (AbstractCommand command : cp.getCommands().values()) {
			command.setCommandPrompt(cp);
		}
		
		setupTestUsers(cp);
		setupTestFileEntries(cp);
		
		for (User user : cp.getUsers()) {
			ByteArrayOutputStream newOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(newOut));
			
			cp.switchActiveUser(user.getName());
			cp.getCommands().get("displayEntry").execute(testDate);
			
			String output = newOut.toString();
			assertTrue(output.contains(testDate));
			assertTrue(output.contains(user.getName()));
		}
		
		System.setOut(oldOut);
		
	}
	
	
	@SuppressWarnings("serial")
	private List<AbstractCommand> getTestCommands(CommandPrompt cp) {
		return new ArrayList<AbstractCommand>() {
			{
				add(new DisplayEntryCommand(cp));
				add(new NewEntryCommand(cp));
				add(new NewUserCommand(cp));
				add(new ListUsersCommand(cp));
			}
		};
	}


	private void setupTestFileEntries(CommandPrompt cp) {
		for (User user : cp.getUsers()) {
			cp.switchActiveUser(user.getName());
			cp.getCommands().get("newEntry").execute(testDate + " " + user.getName() + " 5");
		}
	}
	
	private void setupTestUsers(CommandPrompt cp) {
		for (User user :new ArrayList<User>() {
			{
				add(new User("0"));
				add(new User("1"));
				add(new User("2"));
				add(new User("3"));
				add(new User("4"));
				add(new User("5"));
			}
		}) {
			cp.getCommands().get("newUser").execute(user.getName());
			cp.addUser(user);
		}
	}
	
	


	@After
	void cleanUp() {
		System.setOut(oldOut);
	}

}
