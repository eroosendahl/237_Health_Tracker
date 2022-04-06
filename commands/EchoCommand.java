package commands;

import commands.AbstractCommand;

public class EchoCommand extends AbstractCommand {
	
	public EchoCommand() {
		name = "echo";
	}

	@Override
	public int execute() {
		System.out.println("No input provided to echo.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		System.out.println(executionMod);
		return 0;
	}

	@Override
	public int formatMessage() {
		// TODO Auto-generated method stub
		System.out.println("echo [execution-mode]");
		return 0;
	}

}