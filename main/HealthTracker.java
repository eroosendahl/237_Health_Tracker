package main;

import commands.EchoCommand;

public class HealthTracker {
	
	public static void main(String args[]) {
		
		EchoCommand echoCommand = new EchoCommand();
		
		echoCommand.execute("Hello World");
	}

}
