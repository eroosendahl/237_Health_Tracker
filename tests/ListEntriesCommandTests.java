package tests;

import static org.junit.Assert.assertTrue;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.jupiter.api.Test;

import commands.AbstractCommand;
import commands.DisplayEntryCommand;
import commands.ListEntriesCommand;
import commands.ListUsersCommand;
import commands.NewEntryCommand;
import commands.NewUserCommand;
import main.CommandPrompt;
import main.User;

public class ListEntriesCommandTests {
	PrintStream oldOut = System.out;

	@Test
	public void testListEntriesCommand() throws IOException {
		
		CommandPrompt cp = new CommandPrompt();
		cp.setFile("testUserInfo.csv");
		
		
		File testFile = new File("testUserInfo.csv");
		boolean createdNewFile = testFile.createNewFile();
		
		List<AbstractCommand> testCommands = getTestCommands(cp);
		cp = new CommandPrompt(testFile, testCommands);

		for (AbstractCommand command : cp.getCommands().values()) {
			command.setCommandPrompt(cp);
		}

		setupTestUsers(cp);
		cp.switchActiveUser(cp.getUsers().get(0).getName());

		Map<String, String> testEntries = getTestEntries();
		setupTestFileEntries(cp, testEntries);

		cp.addCommand(new ListEntriesCommand(cp));

		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(newOut));
		cp.getCommands().get("listEntries").execute();

		String output = newOut.toString();
		for (String date : testEntries.keySet()) {
			assertTrue(output.contains(date));
		}
	}

	private void setupTestFileEntries(CommandPrompt cp, Map<String, String> testEntries) {
		if (!cp.getCommands().containsKey("newEntry"))
			cp.addCommand(new NewEntryCommand(cp));

		for (String entry : testEntries.values()) {
			cp.getCommands().get("newEntry").execute(entry);
		}
	}

	@SuppressWarnings("serial")
	private List<AbstractCommand> getTestCommands(CommandPrompt cp) {
		return new ArrayList<AbstractCommand>() {
			{
				add(new DisplayEntryCommand(cp));
				add(new NewEntryCommand(cp));
				add(new NewUserCommand(cp));
				add(new ListUsersCommand(cp));
				add(new ListEntriesCommand(cp));
			}
		};
	}

	private Map<String, String> getTestEntries() {
		return Map.of(
				"01/01/2000", "01/01/2000 run 1", 
				"01/02/2000", "01/02/2000 run 2", 
				"01/03/2000", "01/03/2000 run 3", 
				"01/04/2000", "01/04/2000 run 4", 
				"01/05/2000", "01/05/2000 run 5"
				);
	}

	private void setupTestUsers(CommandPrompt cp) {
		for (User user :new ArrayList<User>() {
			{
				add(new User("testUser"));
			}
		}) {
			cp.getCommands().get("newUser").execute(user.getName());
			cp.addUser(user);
		}
	}

	@After
	public void cleanUp() {
		System.setOut(oldOut);
	}

}
