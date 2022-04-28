 237_Health_Tracker
# Project: Health Tracker

Team:
Erik Roosendahl
Nicholas Fletcher
Clara McKinley
Tony Ngok


## Iteration 1:

### Stories completed:

- Initial setup.
- A NewEntryCommand should be able to take an entry identifier and value and write it tothe provided csv file.
- A User data object should be able to store a row location and current column index.
- A CommandPrompt should be able to execute a command from user input.
- A CommandPrompt should be able to list all of its commands and their help messages.
- A newUserCommand should add a new user to a new line of a given csv file.

### Stories to-do:

- A newEntryCommand should be able to provide a helpful stat list message with a list of possible entry stats.
- An appendEntryCommand should be able to accept user input and append it to a user's entry.
- An Entry should have a String for its date, and an int for its index in the csv, and know its User.
- A User should be able to return a list of the inidicies of all its entries.
- A user should be able to return a list of the dates of all its entries.

### What that we've implemented doesn't work yet?

- Both NewEntry and NewUser commands will crash on execution, NewEntry from an ArrayIndexOutOfBounds Exception and NewUser from a NullPointerException.  
- Both of these are likely the result of not correctly providing file and User access from the CommandPrompt, which hasn't been implemented yet.

### To run program from Command Line:

Navigate to program's directory, containing commands and main packages.
Execute the following commands:
javac main/HealthTracker.java
java main.HealthTracker

* Note to clear up any confusion that we had unknowingly left our project set to "private" during class on our peer review date, which is likely the reason for any reviews indicating there was no project associated with this repo.  It was set to "public" during class, so any reviews after that should likely include it, and hopefully the dates on issues, branches, and merges will verify this.


## Iteration 2:

### What user stories were completed this iteration?
 
- SwitchUser Command should call commandPrompt's switchUser method with the user given input string.
- CommandPrompt.isUniqueUsername(String newName) should check for if the given string matches the names of any users already in CommandPrompt.users
- CommandPrompt.switchUser(String user) should check if there is a user in its list matching the input param and switch currentUser to it.
- DeleteEntryCommand should delete the given entry.
- deleteUser command should delete a user and clear their data from the file.
- ListUser command should list the current users loaded into the commandPrompt.
- ListCommands command should list the current commands loaded into the commandPrompt
- displayEntry command displays the entry for the active user matching the date given as a command exe mod in user input.
- ListEntriesCommand should print a list of dates of every entry for the current user.
- CommandPrompt should print a helpful message on general usage when the user inputs "help"
- AbstractCommands should have functions to print help messages for command format and description.
- ChangeUsername command should change the current User's username to match the command input.
- CommandPrompt should have a function that can check if it can open a default file on startup, and if it cannot it should create one and use it.
- NewUserCommand should assign an accurate row and column to a new user, which should be updated as necessary
- CommandPrompt.loadExistantUsers should read in any user entries from the supplied csv to initialize the commandprompt's list of users.
 
### What user stories do you intend to complete next iteration?
- An AppendEntryCommand should be able to accept user input and append it to a user's entry.
- The SetGoalCommand should set a goal entry for the provided health stat of the provided value.
- The CheckGoalCommand should compare an average of a stat's values with the corresponding goal entry, if it exists, and return a progress check corresponding to that health stat.
- ShowStatProgress command should parse all entries for current user, and print the values for the input stat identifier from all dated entries.
- HealthTrackerGeneralVariables.generateTestFile should fill a dummy file with possible healthtracker data for testing.
 
### Is there anything that you implemented but doesn't currently work?
- All of the currently implemented commands are functional.
- Not all tests are included in the currest Test Suite, but they can still be run individually.  The unincluded tests are: ListStatsCommandTests, NewEntryCommandTests, NewUserTest, DeleteUserTests, DeleteEntryTests
- DeleteUserTests and DeleteEntryTests currently fail/are unreliable.
 
### What commands are needed to compile and run your code from the command line (or better yet, provide a script that people can use to run your program!)
- To run the code, use the HealthTracker.sh bash script.


## Iteration 3:

### What user stories were completed this iteration?
- A summary command should take an identifier and date range and calculate statistics for ther specified input.
- The SetGoalCommand should set a goal entry for the provided health stat of the provided value.
- The CheckGoalCommand should compare an average of a stat's values with the corresponding goal entry, if it exists, and return a progress check corresponding to that health stat.
- ShowStatProgress command should parse all entries for current user and print the values for the input stat identifier.
- HealthTrackerGeneralVariables.generateTestFile should directly fill a dummy file with possible healthtracker data.
- HealthTrackerTestSuite should integrate existing tests into a single test suite.
- CommandPrompt.retargetUser should check if the current user exists in the current file, and if not it should set current uset as the first user in the current file.

### What user stories do you intend to complete next iteration?
Final iteration so no further stories are planned.

### Is there anything that you implemented but doesn't currently work?
No.

### What commands are needed to compile and run your code from the command line (or better yet, provide a script that people can use to run your program!)
- To run the code, use the HealthTracker.sh bash script.





