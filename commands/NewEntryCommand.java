package commands;

import java.io.File;
import java.util.Scanner;

public class NewEntryCommand extends AbstractCommand{

	String entryIdentifier;
	String entryValue;
	String entryDate;
	User currentUser;
	String filePath;
	
	
	public NewEntryCommand(User user, String path) {
		
		
		this.currentUser = user;
		name = "newEntry";
		this.filePath = path;
	}
	@Override
	public int execute() {
		
		System.out.println("Value required for insertion.");
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		
		//Ex of command: "newEntry run 500 09/20/2020"
		
		//Ex of fileEntry: "03/10/2022 WALK(2) RUN(3) ATE(2200)"
		
		
		String[] commandSections = executionMod.split(" ");
		this.entryIdentifier = commandSections[1];
		this.entryValue = commandSections[2];
		this.entryDate = commandSections[3];
		
		
		Scanner sc = new Scanner(new File(this.filePath));  
		sc.useDelimiter(","); 
		
		int count =0;
		while(count< this.currentUser.getUserRow()) {
			
			if(sc.hasNext()) sc.next();
			
			else System.out.println("Error finding User position");
			
			
			count++;
		}
		
		String userRow = sc.next();
		String[] userEntries = userRow.split(",");
		
		for(int i =0; i<userEntries.length; i++) {
			
			String individualEntry = userEntries[i];
			
			String[] entrySections = individualEntry.split(" ");
			
			
		}
		
		
		return 0;
	}

	@Override
	public int helpMessage() {
		// TODO Auto-generated method stub
		System.out.println("Format: ");
		return 0;
	}

}
