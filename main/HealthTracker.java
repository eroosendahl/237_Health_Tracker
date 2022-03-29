package main;

import commands.AbstractCommand;
import commands.EchoCommand;
import commands.NewUserCommand;

public class HealthTracker {
	
	public static void main(String args[]) {

	  CommandPrompt commandPrompt = new CommandPrompt();
		AbstractCommand echoCommand = new EchoCommand();
		
		commandPrompt.addCommand(echoCommand);
		
		commandPrompt.run();

	}

}
