package commands;

import main.CommandPrompt;

//base class all further commands will inherit from
public abstract class AbstractCommand {
	public CommandPrompt commandPrompt;
	public CommandPrompt getCommandPrompt() {return commandPrompt;}
	public void setCommandPrompt(CommandPrompt cp) {commandPrompt = cp;}
	
	protected String name;
	public String getName() { return name; }
	public abstract int execute();
	public abstract int execute(String executionMod);
	public abstract String formatMessage();
	public abstract String descriptionMessage();
	
}