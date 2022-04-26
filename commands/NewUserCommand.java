package commands;

import commands.AbstractCommand;
import main.CommandPrompt;
import main.HealthTrackerGeneralVariables;
import main.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NewUserCommand extends AbstractCommand {
	String csvFileName;
	User user;
	String userName;
	
	public NewUserCommand(CommandPrompt cp) {
		name = "newUser";
		commandPrompt = cp;
	}
	
	@Override
	public int execute() {
		System.out.println("Value required for creating a new user.");
		return 0;
	}
	
	@Override
	public int execute(String executionMod) {
		
		System.out.println("Executing NewUserCommand");
		
		csvFileName = commandPrompt.getFile();
		user = commandPrompt.getCurrentUser();
		userName = executionMod;
		
		List<String> user = new LinkedList<String>();
		user.add(userName + "\n");
		try {
			File outputFile = new File(csvFileName);
			FileWriter data = new FileWriter(outputFile, true);
			String collect = user.stream().collect(Collectors.joining());
			
			if (!commandPrompt.containsUser(collect.trim()) && HealthTrackerGeneralVariables.isAlphaNumeric(collect.trim())) {
				data.append(collect);
				int row = commandPrompt.getUsers().size();
				commandPrompt.addUser(new User(userName, row));
			} else if (!HealthTrackerGeneralVariables.isAlphaNumeric(collect.trim())) {
				System.out.println("Invalid username: only pure alphanumeric usernames are accepted.");
			} else if (commandPrompt.containsUser(collect.trim())) {
				System.out.println("Can't add user: duplicate username.");
			}

			data.flush();
			data.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public String formatMessage() {
		return "newUser [new-username]";
	}

	@Override
	public String descriptionMessage() {
		return "Creates a user with the given username.";
	}
}
