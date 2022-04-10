package tests;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import commands.ListUsersCommand;
import main.CommandPrompt;
import main.User;

public class ListUsersCommandTests {
	private final PrintStream oldOut = System.out;
	String testFileName = "testUserInfo.csv";
	
	
	@Test
	public void testListUsers() {
		int outputLengthWithoutUsers = 21;
		int outputLengthAddedPerUser = 3;
		
		List<CommandPrompt> commandPrompts = getPrompts();
		
		for (CommandPrompt cp: commandPrompts) {
			ByteArrayOutputStream newOut = new ByteArrayOutputStream();
			System.setOut(new PrintStream(newOut));
			
			List<User> users = cp.getUsers();
			cp.addCommand(new ListUsersCommand(cp));
			cp.attemptCommandExecution();
			String output = newOut.toString();
			int outputLength = output.length();
			int expectedOutputLength = outputLengthWithoutUsers + outputLengthAddedPerUser*users.size();
			assertEquals(expectedOutputLength, outputLength);
			for (User user : users) {
				assertTrue(output.contains(user.getName()));
			}
		}
	}
	
	@SuppressWarnings("serial")
	public List<CommandPrompt> getPrompts() {
		return new ArrayList<CommandPrompt>() {
			{
				add(new CommandPrompt(new ArrayList<User>(), "listUsers"));
				add(new CommandPrompt(new ArrayList<User>() {
					{
						add(new User("1", 0));
					}
				}, "listUsers"));
				add(new CommandPrompt(new ArrayList<User>() {
					{
						add(new User("1", 0));
						add(new User("2", 1));
					}
				}, "listUsers"));
				add(new CommandPrompt(new ArrayList<User>() {
					{
						add(new User("1", 0));
						add(new User("2", 1));
						add(new User("3", 2));
						add(new User("4", 3));
					}
				}, "listUsers"));
				add(new CommandPrompt(new ArrayList<User>() {
					{
						add(new User("1", 0));
						add(new User("2", 1));
						add(new User("3", 2));
						add(new User("4", 3));
						add(new User("5", 4));
						add(new User("6", 5));
					}
				}, "listUsers"));
			}
		};
	}
	
	@After
	public void cleanUp() {
		System.setOut(oldOut);
	}

}
