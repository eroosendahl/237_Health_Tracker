package commands;

public class NewEntryCommand extends AbstractCommand{

	String entryIdentifier;
	String entryValue;
	
	public NewEntryCommand() {
		
		name = "newEntry";
	}
	@Override
	public int execute() {
		// TODO Auto-generated method stub
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public int helpMessage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
