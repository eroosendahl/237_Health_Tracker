 237_Health_Tracker
Project:
Health Tracker

Team:
Erik Roosendahl
Nicholas Fletcher
Clara McKinley
Tony Ngok


Iteration 1:


Stories completed:

Initial setup.

A NewEntryCommand should be able to take an entry identifier and value and write it tothe provided csv file.

A User data object should be able to store a row location and current column index.

A CommandPrompt should be able to execute a command from user input.

A CommandPrompt should be able to list all of its commands and their help messages.

A newUserCommand should add a new user to a new line of a given csv file.


Stories to-do:

A newEntryCommand should be able to provide a helpful stat list message with a list of possible entry stats.

An appendEntryCommand should be able to accept user input and append it to a user's entry.

An Entry should have a String for its date, and an int for its index in the csv, and know its User.

A User should be able to return a list of the inidicies of all its entries.

A user should be able to return a list of the dates of all its entries.


What that we've implemented doesn't work yet?

Both NewEntry and NewUser commands will crash on execution, NewEntry from an ArrayIndexOutOfBounds Exception and NewUser from a NullPointerException.  
Both of these are likely the result of not correctly providing file and User access from the CommandPrompt, which hasn't been implemented yet.



To run program from Command Line:

Navigate to program's directory, containing commands and main packages.
Execute the following commands:
javac main/HealthTracker.java
java main.HealthTracker

