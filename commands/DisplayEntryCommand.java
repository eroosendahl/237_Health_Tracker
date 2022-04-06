package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.CommandPrompt;
import main.HealthTrackerGeneralVariables.endState;

public class DisplayEntryCommand extends AbstractCommand {
	private CommandPrompt commandPrompt;
	private String filename;

	public DisplayEntryCommand(CommandPrompt cp) {
		name = "displayEntry";
		commandPrompt = cp;
	}

	@Override
	public int execute() {
		return endState.UNIMPLEMENTED.value();
	}

	@Override
	public int execute(String executionMod) {
		System.out.println("Executing DisplayEntry Command");
		filename = commandPrompt.getFile();
		String entryDate = executionMod;

		if (!verifyInput(entryDate)) {
			System.out.println("Bad date format.");
			return endState.GENERAL_FAILURE.value();
		}

		List<String> previousData = new ArrayList<String>();

		try {
			System.out.println(retreiveEntryFromDate(entryDate, previousData));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return endState.SUCCESS.value();
	}

	public String retreiveEntryFromDate(String date, List<String> previousData) throws IOException {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(filename));
			
			String line;

			while ((line = csvReader.readLine()) != null) {
				previousData.add(line);
				String[] userData = line.split(",");
				if (userData[0].equals(commandPrompt.getCurrentUser().getName())) {
					for (String entry: userData) {
						if (entry.startsWith(date)) {
							csvReader.close();
							return entry;
						}
					}
				}
			}
			System.out.println("Failed to find a matching entry.");
			csvReader.close();
			return "failed";

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("File failed to open.");
			return "file not found";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to readLine()");
			return "ioexception";
		}
	}

	private void writeData(List<String> commaSeparatedData) {
		try {
			BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filename));
			
			for (String userData: commaSeparatedData) {
				csvWriter.write(userData + "\n");
			}
			csvWriter.flush();
			csvWriter.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed opening file for writing.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed opening file for writing.");
		}
	}

	private boolean verifyInput(String date) {
		if (date.length() != 10 || date.indexOf("/") != 2 || date.lastIndexOf("/") != 5)
			return false;
		return true;
	}

	@Override
	public int formatMessage() {
		System.out.println("displayEntry <dd/mm/yyyy>");
		return 0;
	}

	@Override
	public int desciptionMessage() {
		System.out.println("Display the entry at the given date for the current user.");
		return 0;
	}














}
