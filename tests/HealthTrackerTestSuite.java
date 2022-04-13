package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//https://www.tutorialspoint.com/junit/junit_suite_test.htm
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ChangeUsernameTests.class,
	CommandPromptTests.class,
	DisplayEntryCommandTests.class,
	EchoCommandTest.class,
	ListCommandsCommandTests.class,
	ListEntriesCommandTests.class,
	ListUsersCommandTests.class,
	ListStatsCommandTests.class,
	SwitchUserCommandTests.class,
	UserTest.class,
	NewUserTest.class,
	DeleteUserTests.class
})

public class HealthTrackerTestSuite {
}
