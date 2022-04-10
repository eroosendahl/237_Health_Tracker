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
	UserTest.class
})

public class HealthTrackerTestSuite {
	
	// think about this all more carefully and thoughtfully alter main/command code to be easier and cleaner for
	// implementing tests. (rather than just hacky debugging your way to working tests but scuffed code)
	
	
	

}
