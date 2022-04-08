package commands;

//base class all further commands will inherit from
public abstract class AbstractCommand {
	
	protected String name;
	public String getName() { return name; }
	public abstract int execute();
	public abstract int execute(String executionMod);
	public abstract String formatMessage();
	public abstract String descriptionMessage();
	
}