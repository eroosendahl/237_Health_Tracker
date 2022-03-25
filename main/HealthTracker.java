package main;

import commands.EchoCommand;
import commands.NewUserCommand;

public class HealthTracker {
	
	public static void main(String args[]) {
	
		EchoCommand echoCommand = new EchoCommand();
		NewUserCommand newUser = new NewUserCommand("Erik", "userinfo.csv");
		echoCommand.execute("Hello World");
		newUser.execute();
	}

}
