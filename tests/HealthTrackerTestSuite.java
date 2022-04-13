package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


//https://www.tutorialspoint.com/junit/junit_suite_test.htm
@RunWith(Suite.class)
@Suite.SuiteClasses({
	ChangeUsernameTests.class,
	CommandPromptTests.class,
	DisplayEntryCommandTests.class,
	ListCommandsCommandTests.class,
	ListEntriesCommandTests.class,
	ListUsersCommandTests.class,
	SwitchUserCommandTests.class,
	EchoCommandTest.class,
	UserTest.class
})

public class HealthTrackerTestSuite {
	

}
