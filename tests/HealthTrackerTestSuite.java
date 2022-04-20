package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectClasses({
		AppendEntryCommandTests.class,
		ChangeUsernameTests.class,
		CommandPromptTests.class,// vintage junit
		DeleteEntryTests.class,
		DisplayEntryCommandTests.class,//
		EchoCommandTest.class,
		ListCommandsCommandTests.class,//
		ListEntriesCommandTests.class,//
		ListStatsCommandTests.class,
		ListUsersCommandTests.class,//
		NewEntryCommandTests.class,
		NewUserTest.class,
		SetGoalCommandTests.class,
		SummarizeCommandTests.class,
		SwitchUserCommandTests.class,//
		UserTest.class
})

public class HealthTrackerTestSuite {
}

