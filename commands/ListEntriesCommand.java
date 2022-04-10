package commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;

public class ListEntriesCommand extends AbstractCommand {
	
	public ListEntriesCommand(CommandPrompt cp) {
		name = "listEntries";
		commandPrompt = cp;
	}
	
	
	@Override
	public int execute() {
		String filename = commandPrompt.getFile();
		
		System.out.println("Executing listEntries Command.\n");
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(filename));
			
			String line;
			
			while ((line = csvReader.readLine()) != null) {
				String[] entries = line.split(",");
				if (entries[0].equals(commandPrompt.getCurrentUser().getName())) {
					for (String entry : entries) {
						if (entry.length()>=10 && HealthTrackerGeneralVariables.isDateFormat(entry.substring(0, 10))) {
							System.out.println(entry);
						}
					}
					System.out.println();
				}
			}
			csvReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not readLine()");
			e.printStackTrace();
		}
		
		
		return 0;
	}

	@Override
	public int execute(String executionMod) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String formatMessage() {
		return "listEntries";
	}

	@Override
	public String descriptionMessage() {
		return "Lists all current (dated) entries for the currentUser.";
	}

}
