package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commands.ChangeUsernameCommand;
import commands.ListStatsCommand;
import main.CommandPrompt;
import main.User;

public class ListStatsCommandTests {
	CommandPrompt commandPrompt;
	ListStatsCommand listStatsCommand;
	
	@BeforeEach
	void setup() {
		User originalUser = new User("originalUser");
		commandPrompt = new CommandPrompt();
		commandPrompt.setCurrentUser(originalUser);
		listStatsCommand = new ListStatsCommand(commandPrompt);
		commandPrompt.addCommand(listStatsCommand);
	}

	@Test
	public void testStatsCommand() {
		listStatsCommand.execute();
		assertNotEquals(null, commandPrompt.getSupportedStats());
	}

}
