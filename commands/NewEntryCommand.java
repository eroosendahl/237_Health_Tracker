package commands;

public class NewEntryCommand extends AbstractCommand{

	String entryIdentifier;
	String entryValue;
	String entryDate;
	User currentUser;
	public NewEntryCommand(User user) {
		
		
		this.currentUser = User;
		name = "newEntry";
	}
	@Override
	public int execute() {
		
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		
		//Ex: "newEntry run 500 09/20/2020"
		
		String[] commandSections = executionMod.split(" ");
		this.entryIdentifier = commandSections[1];
		this.entryValue = commandSections[2];
		this.entryDate = commandSections[3];
		
		
		
		
		
		
		
		
		
		return 0;
	}

	@Override
	public int helpMessage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
